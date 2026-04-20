package com.ecommerce.project.security;

import com.ecommerce.project.security.jwt.AuthEntryPointJwt;
import com.ecommerce.project.security.jwt.AuthTokenFilter;
import com.ecommerce.project.security.services.UserDetailsServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Autowired
    private UserDetailsServiceImplementation userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorisedHandler;

    @Bean
    public AuthTokenFilter authTokenFilter(){
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig){
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(requests ->
                // Standard best practice: whitelist public paths first.
                // '/h2-console/**' matches anything starting with /h2-console/.
                requests.requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers( "/v3/api-docs/**").permitAll()
                        .requestMatchers( "/api/auth/**").permitAll()
                        .requestMatchers( "/api/public/**").permitAll()
                        .requestMatchers( "/api/admin/**").permitAll()
                        .requestMatchers( "/api/test/**").permitAll()
                        .requestMatchers( "/images/**").permitAll()
                        .anyRequest()
                        .authenticated());

        http.sessionManagement(session ->
                // Standard stateless preference for large, professional projects.
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.formLogin(withDefaults()); // Commented out standard username/password form login
        //http.httpBasic(withDefaults());

        // Disable CSRF only
        http.csrf(csrf -> csrf.disable());

        // handling the exception if the user is unauthorized
        http.exceptionHandling(exception ->
                exception.authenticationEntryPoint(unauthorisedHandler)
        );

        // Set X-Frame-Options to SAME ORIGIN for frames to work ---
        http.headers(headers ->
                // Instructs browser to only display content in frames from the same origin.
                headers.frameOptions(frame -> frame.sameOrigin()));
        /*
        Adding our own custom filer
        Processing the authentication from submission
         */
        http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        http.authenticationProvider(authenticationProvider());

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web -> web.ignoring().requestMatchers(
                "/configuration/ui",
                "/webjars/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/swagger-resources/**"
        ));
    }
}
