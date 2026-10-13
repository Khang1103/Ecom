package ShopApi.Ecom.service;

import ShopApi.Ecom.dto.JwtResponse;
import ShopApi.Ecom.dto.LoginRequest;
import ShopApi.Ecom.dto.RegisterRequest;
import ShopApi.Ecom.entity.Role;
import ShopApi.Ecom.entity.User;
import ShopApi.Ecom.exception.ResourceNotFoundException;
import ShopApi.Ecom.repository.RoleRepository;
import ShopApi.Ecom.repository.UserRepository;
import ShopApi.Ecom.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // 1. Đăng ký tài khoản
    public String register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại!");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã được sử dụng!");
        }

        // Lấy hoặc tự động tạo Role mặc định nếu chưa có trong DB
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName("ROLE_USER");
                    return roleRepository.save(role);
                });

        // Tận dụng @Builder sẵn có từ User entity
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName()) // Nếu RegisterRequest chưa có fullName thì có thể bỏ dòng này
                .enabled(true)
                .roles(new HashSet<>(Collections.singletonList(userRole)))
                .build();

        userRepository.save(user);
        return "Đăng ký tài khoản thành công!";
    }

    // 2. Đăng nhập
    public JwtResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", request.getUsername()));

        String token = jwtService.generateToken(user);

        List<String> roles = user.getRoles().stream()
                .map(role -> role.getName())
                .toList();

        return JwtResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .username(user.getUsername())
                .email(user.getEmail())
                .userId(user.getId())
                .roles(roles)
                .build();
    }
}