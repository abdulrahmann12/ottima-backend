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

    @org.springframework.beans.factory.annotation.Value("${app.bootstrap-admin.email:admin@ottima.com}")
    private String bootstrapEmail;

    @org.springframework.beans.factory.annotation.Value("${app.bootstrap-admin.password:admin123}")
    private String bootstrapPassword;

    @Override
    public void run(String... args) throws Exception {

        // 1. التأكد من وجود الرولز، ولو مش موجودة نكريتها
        if (roleRepository.count() == 0) {
            roleRepository.save(createRole("ADMIN"));
            roleRepository.save(createRole("ENGINEER"));
            roleRepository.save(createRole("CLIENT"));
            System.out.println("✅ Roles seeded successfully!");
        }

        // 2. التأكد من وجود أدمن، لو مفيش نكريت واحد
        Role adminRole = roleRepository.findByRoleName("ADMIN")
                .orElse(null);

        if (adminRole != null && userRepository.count() == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPasswordHash(passwordEncoder.encode(bootstrapPassword));
            admin.setRole(adminRole);
            admin.setEmail(bootstrapEmail != null && !bootstrapEmail.isBlank() ? bootstrapEmail : "admin@ottima.com");
            admin.setFullNameAr("مدير النظام");
            admin.setFullNameEn("System Administrator");
            admin.setActive(true);
            userRepository.save(admin);
            System.out.println("✅ Bootstrap Admin created! (Username: admin | Email: " + admin.getEmail() + ")");
        }
    }

    private Role createRole(String roleName) {
        Role role = new Role();
        role.setRoleName(roleName);
        return role;
    }
}