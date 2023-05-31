package uk.gov.moj.sdt.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Disabling csrf breaks Spring Security compliance and is marked as a security hotspot
        // in the pipeline build
        // BUT this is needed to allow access to SDT hence commented for now!
        //        http.csrf().disable().authorizeHttpRequests().anyRequest().permitAll();
        return http.build();
    }
}
