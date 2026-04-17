package com.securitydemo;

import com.securitydemo.jwt.AuthEntryPointJwt;
import com.securitydemo.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AuthEntryPointJwt unAuthorisedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter(){
        return new AuthTokenFilter();
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(requests ->
                // Standard best practice: whitelist public paths first.
                // '/h2-console/**' matches anything starting with /h2-console/.
                requests.requestMatchers("/h2-console/**", "/signin")
                        .permitAll()
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
                exception.authenticationEntryPoint(unAuthorisedHandler)
        );

        // Set X-Frame-Options to SAME ORIGIN for frames to work ---
        http.headers(headers ->
                // Instructs browser to only display content in frames from the same origin.
                headers.frameOptions(frame -> frame.sameOrigin()));
        /*
        Adding our own custom filer
        Processing the authentication from submission
         */
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public JdbcUserDetailsManager userDetailsService(DataSource dataSource){
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public CommandLineRunner initData(JdbcUserDetailsManager userDetailsService){
        return args -> {
            if(!userDetailsService.userExists("user")){
                UserDetails user = User.withUsername("user")
                        .password(passwordEncoder().encode("user"))
                        .roles("USER")
                        .build();
                userDetailsService.createUser(user);
            }
            if(!userDetailsService.userExists("admin")){
                UserDetails admin = User.withUsername("admin")
                        .password(passwordEncoder().encode("admin"))
                        .roles("ADMIN")
                        .build();
                userDetailsService.createUser(admin);
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}