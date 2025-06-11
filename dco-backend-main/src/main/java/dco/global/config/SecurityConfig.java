package dco.global.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import dco.global.auth.CustomUserDetailsService;
import dco.global.auth.JwtAuthenticationFilter;
import dco.global.auth.JwtTokenProvider;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .httpBasic(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .sessionManagement(sessionManagement ->
                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                    .requestMatchers("/api/login/**", "/api/signup",
                            "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()

                    .requestMatchers( "/api/notification/professor/**",
                        "/api/problem/professor/**", "/api/question/professor/**", "/api/group/professor/**",
                        "/api/comment/professor/**", "/api/submission/professor/**").hasAnyAuthority("PROFESSOR", "ADMIN")

                    .requestMatchers("/api/member/**", "/api/notification/member/**", "/api/problem/member/**",
                        "/api/question/member/**", "/api/group/member/**", "/api/comment/member/**",
                        "/api/member/github/**",
                        "/api/submission/member/**").hasAnyAuthority("STUDENT", "PROFESSOR", "ADMIN")
                    .anyRequest().permitAll()
            )
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                    UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService);
    }
}
