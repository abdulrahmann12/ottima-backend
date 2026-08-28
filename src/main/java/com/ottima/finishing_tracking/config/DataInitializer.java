package com.ottima.finishing_tracking.config;

import com.ottima.finishing_tracking.role.entity.Role;
import com.ottima.finishing_tracking.role.repository.RoleRepository;
import com.ottima.finishing_tracking.user.entity.User;
import com.ottima.finishing_tracking.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // 1. التأكد من وجود الرولز، ولو مش موجودة نكريتها
        if (roleRepository.count() == 0) {
            // (ملاحظة: لو الكونتراكتور بتاع Role عندك بياخد Id، خليه null، ولو بياخد اسم بس اكتبه)
            roleRepository.save(createRole("ADMIN"));
            roleRepository.save(createRole("ENGINEER"));
            roleRepository.save(createRole("CLIENT"));
            System.out.println("✅ Roles seeded successfully!");
        }

        // 2. التأكد من وجود أدمن، لو مفيش نكريت واحد
        // استخدم اسم الـ Method اللي بتجيب الرول بالاسم عندك
        Role adminRole = roleRepository.findByRoleName("ADMIN")
                .orElse(null);

        if (userRepository.count() == 0 && adminRole != null) {
            User admin = new User();

            admin.setUsername("admin");
            admin.setPasswordHash(passwordEncoder.encode("admin123")); // الباسورد admin123
            admin.setRole(adminRole);
            admin.setEmail("bodynafea2@gmail.com");
            // لو عندك حقل isActive خليه true
             admin.setActive(true);

            userRepository.save(admin);
            System.out.println("✅ Default Admin created! (Username: admin | Password: admin123)");
        }
    }

    private Role createRole(String roleName) {
        Role role = new Role();
        role.setRoleName(roleName);
        return role;
    }
}