package gh.springboot.jpaboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers("/style.css").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/user/create").permitAll()
                        .requestMatchers("/user/login").permitAll()
                        .requestMatchers("/user/modify").permitAll()
                        .anyRequest().authenticated())

                .formLogin(formLogin -> formLogin
                        .loginPage("/user/login")
                        .loginProcessingUrl("/user/login")
                        .defaultSuccessUrl("/"))

                .logout(logout -> logout
                        .logoutRequestMatcher(new OrRequestMatcher(
                                new AntPathRequestMatcher("/user/logout", "GET"),
                                new AntPathRequestMatcher("/user/logout", "POST")
                        ))
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true))

                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.sendRedirect("/");
                        }))
        ;

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
