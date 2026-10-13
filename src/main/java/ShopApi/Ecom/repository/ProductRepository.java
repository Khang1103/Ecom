package ShopApi.Ecom.repository;

import ShopApi.Ecom.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Lấy danh sách sản phẩm theo Category ID (có hỗ trợ phân trang)
    Page<Product> findByCategoryId(Integer categoryId, Pageable pageable);

    // Tìm kiếm sản phẩm theo tên (không phân biệt hoa/thường)
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    // Đếm số lượng sản phẩm theo danh mục
    long countByCategoryId(Integer categoryId);
}