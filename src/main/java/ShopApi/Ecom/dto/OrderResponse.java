package ShopApi.Ecom.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long id;
    private BigDecimal totalPrice;
    private String status;
    private String shippingAddress;
    private String paymentMethod;
    private String trackingNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderItemResponse> items;

    // Customer info (for admin view)
    private String customerUsername;
    private String customerEmail;
}