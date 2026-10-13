package ShopApi.Ecom.repository;

import ShopApi.Ecom.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    // Kiểm tra tên danh mục đã tồn tại chưa
    Boolean existsByName(String name);

    // Tìm danh mục theo tên
    Optional<Category> findByName(String name);
}