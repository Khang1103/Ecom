package ShopApi.Ecom.repository;

import ShopApi.Ecom.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Lấy danh sách đơn hàng của một người dùng cụ thể (Lịch sử mua hàng)
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);

    // Đếm số đơn hàng theo trạng thái
    Long countByStatus(String status);

    // Tổng doanh thu toàn hệ thống (chỉ đơn DELIVERED)
    @Query("SELECT COALESCE(SUM(o.totalPrice), 0) FROM Order o WHERE o.status = 'DELIVERED'")
    BigDecimal getTotalRevenue();

    // Doanh thu hôm nay
    @Query("SELECT COALESCE(SUM(o.totalPrice), 0) FROM Order o WHERE o.status = 'DELIVERED' AND o.createdAt >= :startOfDay")
    BigDecimal getRevenueToday(LocalDateTime startOfDay);

    // Tất cả đơn hàng cho admin (mới nhất trước)
    List<Order> findAllByOrderByCreatedAtDesc();
}