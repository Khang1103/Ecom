package ShopApi.Ecom.repository;

import ShopApi.Ecom.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    // Tìm role theo tên (ví dụ: ROLE_USER, ROLE_ADMIN)
    Optional<Role> findByName(String name);
}