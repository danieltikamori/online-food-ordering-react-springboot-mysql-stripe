package me.amlu.config;

import me.amlu.repository.OrderRepository;
import me.amlu.repository.UserRepository;
import me.amlu.service.*;
import me.amlu.service.Tasks.RecordCleanupTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class AppConfig {

    // Cross-Site Request Forgery
    // CSRF protection disabled for testing purposes

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.sessionManagement(management -> management.sessionCreationPolicy(
            SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        Authorize -> Authorize
                                .requestMatchers("/api/v1/admin/**")
                                .hasAnyRole("ADMIN", "RESTAURANT_OWNER")
                                .requestMatchers("/api/**").authenticated()
                                .anyRequest().permitAll()
                ).addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));
        return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration corsConfiguration = new CorsConfiguration();
            corsConfiguration.applyPermitDefaultValues();
            corsConfiguration.setAllowedOrigins(Arrays.asList(
                    "https://tika-food.vercel.app/",
                    "http://localhost:3000"
            ));
            corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
            corsConfiguration.setAllowCredentials(true);
            corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
            corsConfiguration.setExposedHeaders(List.of("Authorization"));
            corsConfiguration.setMaxAge(3600L);

            return corsConfiguration;
        };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public NotificationService notificationService() {
        return new NotificationServiceImp();
    }

    @Bean
    public DataRetentionPolicyImp dataRetentionPolicy(UserRepository userRepository, OrderRepository orderRepository) {
        return new DataRetentionPolicyImp(userRepository, orderRepository);
    }

    @Bean
    public RecordCleanupTask recordCleanUpTask(UserRepository userRepository, OrderRepository orderRepository) {
        return new RecordCleanupTask(userRepository, orderRepository, dataRetentionPolicy(userRepository, orderRepository));
    }

    @Bean
    public DataTransferServiceImp dataTransferServiceImp(AnonymizationService anonymizationService, NotificationService notificationService, DataTransferService dataTransferService, DataRetentionPolicy dataRetentionPolicy) {
        return new DataTransferServiceImp(anonymizationService, notificationService, dataTransferService, dataRetentionPolicy);
    }
}
