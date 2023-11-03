package com.electronicstore.config;

import com.electronicstore.model.Role;
import com.electronicstore.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class DatabaseInitializationConfig {
    @Autowired
    private RoleRepository roleRepository;



    @Value("${admin.role.id}")
    private String role_admin_id;

    @Value("${normal.role.id}")
    private String role_normal_id;

    @Bean
    public CommandLineRunner initializeDatabase() {
        return args -> {
            try {
                Role role_admin = Role.builder().roleId(role_admin_id).roleName("ROLE_ADMIN").build();
                Role role_normal = Role.builder().roleId(role_normal_id).roleName("ROLE_NORMAL").build();
                roleRepository.save(role_admin);
                roleRepository.save(role_normal);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }
}
