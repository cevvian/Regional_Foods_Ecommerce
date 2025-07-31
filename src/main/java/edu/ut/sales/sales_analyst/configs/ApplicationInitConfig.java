package edu.ut.sales.sales_analyst.configs;

import edu.ut.sales.sales_analyst.model.entities.Cart;
import edu.ut.sales.sales_analyst.model.entities.User;
import edu.ut.sales.sales_analyst.model.enums.Role;
import edu.ut.sales.sales_analyst.repositories.CartRepo;
import edu.ut.sales.sales_analyst.repositories.UserRepo;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;

//    @NonFinal
//    @Value("${admin-password}")
    static String adminPassword = "adminadmin";

    @Bean
    @Transactional
    ApplicationRunner applicationRunner(UserRepo userRepo, CartRepo cartRepo) {
        return args -> {
            if(userRepo.findByEmail("admin@gmail.com") == null) {
                User user = new User();
                user.setEmail("admin@gmail.com");
                user.setUserName("Admin");
                user.setPhone("0946587325");
                user.setPassword(passwordEncoder.encode(adminPassword));
                user.setRole(Role.ADMIN);
                userRepo.save(user);
                log.warn("admin account created with default password : adminadmin, please change it!");
            }

            var users = userRepo.findAll();
            for (User user : users) {
                boolean hasCart = cartRepo.existsByUser(user);
                if (!hasCart) {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    cart.setUpdatedAt(LocalDateTime.now());
                    cartRepo.save(cart);
                    log.info("Created cart for user: {}", user.getEmail());
                }
            }
        };
    }
}

