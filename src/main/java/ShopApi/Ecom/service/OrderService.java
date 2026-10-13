package ShopApi.Ecom.service;

import ShopApi.Ecom.dto.*;
import ShopApi.Ecom.entity.Order;
import ShopApi.Ecom.entity.OrderItem;
import ShopApi.Ecom.entity.Product;
import ShopApi.Ecom.entity.User;
import ShopApi.Ecom.exception.ResourceNotFoundException;
import ShopApi.Ecom.repository.OrderRepository;
import ShopApi.Ecom.repository.ProductRepository;
import ShopApi.Ecom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public OrderResponse createOrder(User user, OrderRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new RuntimeException("Đơn hàng phải có ít nhất 1 sản phẩm");
        }

        Order order = Order.builder()
                .user(user)
                .shippingAddress(request.getShippingAddress())
                .paymentMethod(request.getPaymentMethod() != null ? request.getPaymentMethod() : "COD")
                .status("PENDING")
                .build();

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "id", itemRequest.getProductId()));

            if (product.getStock() < itemRequest.getQuantity()) {
                throw new RuntimeException("Sản phẩm " + product.getName() + " không đủ số lượng trong kho");
            }

            product.setStock(product.getStock() - itemRequest.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .price(product.getPrice())
                    .build();

            orderItems.add(orderItem);
            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity())));
        }

        order.setOrderItems(orderItems);
        order.setTotalPrice(total);

        Order savedOrder = orderRepository.save(order);
        return mapToResponse(savedOrder, true);
    }

    public List<OrderResponse> getOrdersByUser(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(o -> mapToResponse(o, true))
                .toList();
    }

    // Admin: Get all orders
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(o -> mapToResponse(o, true))
                .toList();
    }

    // Admin: Get single order
    public OrderResponse getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        return mapToResponse(order, true);
    }

    // User: Get single order with ownership check
    public OrderResponse getOrderByIdAndUser(Long orderId, User user) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bạn không có quyền truy cập đơn hàng này");
        }
        return mapToResponse(order, true);
    }

    // Admin: Update order status and tracking
    public OrderResponse updateOrderStatus(Long orderId, UpdateOrderStatusRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        if (request.getStatus() != null) {
            order.setStatus(request.getStatus());
        }
        if (request.getTrackingNumber() != null) {
            order.setTrackingNumber(request.getTrackingNumber());
        }

        Order saved = orderRepository.save(order);
        return mapToResponse(saved, true);
    }

    // Admin: Dashboard statistics
    public DashboardDTO getDashboardStats() {
        long totalOrders = orderRepository.count();
        long totalUsers = userRepository.count();
        long totalProducts = productRepository.count();
        BigDecimal totalRevenue = orderRepository.getTotalRevenue();
        BigDecimal revenueToday = orderRepository.getRevenueToday(LocalDate.now().atStartOfDay());
        long pendingOrders = orderRepository.countByStatus("PENDING");
        long processingOrders = orderRepository.countByStatus("PROCESSING");
        long shippedOrders = orderRepository.countByStatus("SHIPPED");
        long deliveredOrders = orderRepository.countByStatus("DELIVERED");
        long cancelledOrders = orderRepository.countByStatus("CANCELLED");

        // Revenue by month (last 6 months)
        Map<String, BigDecimal> revenueByMonth = new LinkedHashMap<>();
        List<Order> allDeliveredOrders = orderRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .filter(o -> "DELIVERED".equals(o.getStatus()))
                .toList();

        for (int i = 5; i >= 0; i--) {
            LocalDate monthDate = LocalDate.now().minusMonths(i);
            String monthKey = monthDate.getYear() + "-" + String.format("%02d", monthDate.getMonthValue());
            BigDecimal monthRevenue = allDeliveredOrders.stream()
                    .filter(o -> o.getCreatedAt() != null &&
                            o.getCreatedAt().getYear() == monthDate.getYear() &&
                            o.getCreatedAt().getMonthValue() == monthDate.getMonthValue())
                    .map(Order::getTotalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            revenueByMonth.put(monthKey, monthRevenue);
        }

        Map<String, Long> ordersByStatus = new LinkedHashMap<>();
        ordersByStatus.put("PENDING", pendingOrders);
        ordersByStatus.put("PROCESSING", processingOrders);
        ordersByStatus.put("SHIPPED", shippedOrders);
        ordersByStatus.put("DELIVERED", deliveredOrders);
        ordersByStatus.put("CANCELLED", cancelledOrders);

        return DashboardDTO.builder()
                .totalOrders(totalOrders)
                .totalUsers(totalUsers)
                .totalProducts(totalProducts)
                .totalRevenue(totalRevenue)
                .revenueToday(revenueToday)
                .pendingOrders(pendingOrders)
                .processingOrders(processingOrders)
                .shippedOrders(shippedOrders)
                .deliveredOrders(deliveredOrders)
                .cancelledOrders(cancelledOrders)
                .revenueByMonth(revenueByMonth)
                .ordersByStatus(ordersByStatus)
                .build();
    }

    private OrderResponse mapToResponse(Order order, boolean includeCustomer) {
        List<OrderItemResponse> items = order.getOrderItems().stream()
                .map(item -> OrderItemResponse.builder()
                        .id(item.getId())
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .build())
                .toList();

        OrderResponse.OrderResponseBuilder builder = OrderResponse.builder()
                .id(order.getId())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .shippingAddress(order.getShippingAddress())
                .paymentMethod(order.getPaymentMethod())
                .trackingNumber(order.getTrackingNumber())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .items(items);

        if (includeCustomer && order.getUser() != null) {
            builder.customerUsername(order.getUser().getUsername())
                   .customerEmail(order.getUser().getEmail());
        }

        return builder.build();
    }
}
