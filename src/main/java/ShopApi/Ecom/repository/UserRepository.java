package ShopApi.Ecom.repository;

import ShopApi.Ecom.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Tìm user theo username (Phục vụ cho Login & JWT Authentication)
    Optional<User> findByUsername(String username);

    // Kiểm tra username đã tồn tại chưa (Phục vụ cho Register)
    Boolean existsByUsername(String username);

    // Kiểm tra email đã tồn tại chưa (Phục vụ cho Register)
    Boolean existsByEmail(String email);
}