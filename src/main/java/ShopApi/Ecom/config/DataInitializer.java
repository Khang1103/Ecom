package ShopApi.Ecom.config;

import ShopApi.Ecom.entity.Role;
import ShopApi.Ecom.entity.User;
import ShopApi.Ecom.entity.Category;
import ShopApi.Ecom.repository.CategoryRepository;
import ShopApi.Ecom.repository.RoleRepository;
import ShopApi.Ecom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Tự động khởi tạo dữ liệu ban đầu khi server khởi động:
 * - Tạo ROLE_USER và ROLE_ADMIN nếu chưa có
 * - Tạo tài khoản admin mặc định (admin / admin123)
 * - Tạo các danh mục mặc định nếu chưa có
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        // 1. Đảm bảo roles tồn tại
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    log.info("[Init] Tạo role ROLE_USER");
                    return roleRepository.save(Role.builder().name("ROLE_USER").build());
                });

        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> {
                    log.info("[Init] Tạo role ROLE_ADMIN");
                    return roleRepository.save(Role.builder().name("ROLE_ADMIN").build());
                });

        // 2. Tạo tài khoản admin mặc định nếu chưa có
        if (!userRepository.existsByUsername("admin")) {
            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(userRole);
            adminRoles.add(adminRole);

            User adminUser = User.builder()
                    .username("admin")
                    .email("admin@yody.vn")
                    .password(passwordEncoder.encode("admin123"))
                    .fullName("Super Admin")
                    .enabled(true)
                    .roles(adminRoles)
                    .build();

            userRepository.save(adminUser);
            log.info("[Init] ✅ Đã tạo tài khoản admin mặc định: admin / admin123");
        } else {
            // Older databases may already contain the seeded "admin" account
            // without ROLE_ADMIN. Restore the missing role without changing its password.
            User adminUser = userRepository.findByUsername("admin").orElseThrow();
            boolean updated = false;
            if (adminUser.getRoles().stream().noneMatch(role -> "ROLE_ADMIN".equals(role.getName()))) {
                adminUser.getRoles().add(adminRole);
                updated = true;
                log.info("[Init] ✅ Đã bổ sung ROLE_ADMIN cho tài khoản admin hiện có");
            }
            if (adminUser.getPassword() == null || adminUser.getPassword().length() != 60 || !adminUser.getPassword().startsWith("$2")) {
                adminUser.setPassword(passwordEncoder.encode("admin123"));
                updated = true;
                log.info("[Init] ✅ Đã cập nhật mật khẩu BCrypt chuẩn cho tài khoản admin");
            }
            if (updated) {
                userRepository.save(adminUser);
            }
        }

        // 3. Khởi tạo một số danh mục mặc định chuyên về quần áo thời trang nếu chưa có
        if (categoryRepository.count() == 0) {
            List<Category> defaultCategories = List.of(
                    Category.builder().name("Áo Nam & Nữ").description("Áo sơ mi, áo thun, áo polo, áo len và croptop").build(),
                    Category.builder().name("Quần & Jeans").description("Quần jeans, quần âu, quần baggy, quần short thời trang").build(),
                    Category.builder().name("Váy & Đầm").description("Đầm dạ hội, váy midi, đầm suông, chân váy chữ A").build(),
                    Category.builder().name("Áo Khoác & Blazer").description("Áo khoác dạ, blazer unisex, áo bomber và jacket").build(),
                    Category.builder().name("Set Đồ & Đồ Bộ").description("Set đồ linen, set phối thanh lịch, đồ bộ thời trang").build()
            );
            categoryRepository.saveAll(defaultCategories);
            log.info("[Init] ✅ Đã khởi tạo các danh mục quần áo thời trang mặc định");
        }

        log.info("[Init] ✅ Hệ thống YODY khởi động thành công!");
    }
}
