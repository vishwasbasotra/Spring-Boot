package com.securitydemo;

import org.springframework.beans.factory.annotation.Autowired;
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

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration

// Critical for high-level software engineering roles. This switches on the default
// web security infrastructure and allows us to customize it.
@EnableWebSecurity

// Critical for implementing fine-grained, method-level security in your Service Layer,
// reinforcing standardized execution and code quality. This activates annotations like
// @PreAuthorize for precise authorization constraints.
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private DataSource dataSource;

    // ==========================================
    // 1. Web Security (The main gatekeeper)
    // ==========================================

    // Marks this method as producing a bean (a reusable Spring component).
    // The Spring Security Filter Chain is the single most important security bean.
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        // --- 1. Authorization Rules (Access Control) ---
        // This systematically ensuring application reliability by defining
        // distinct access rules for distinct URL patterns.
        http.authorizeHttpRequests(
                (requests) ->
                        // Standard best practice: whitelist public paths first.
                        // '/h2-console/**' matches anything starting with /h2-console/.
                        requests.requestMatchers("/h2-console/**")
                                // Standard public access rule: allow anyone (even unauthenticated) to access this path.
                                .permitAll()
                                // Any request that does NOT match an explicit matcher above...
                                .anyRequest()
                                // ...must be authenticated (user must be logged in).
                                // A senior engineer must always include this final, restrictive 'anyRequest().authenticated()'.
                                .authenticated());

        // --- 2. Session Management (Stateless) ---
        // Critical for high-scale, modern APIs prefer stateless execution (learned in [your career shift prep]).
        // The server will NOT create or maintain standard session state (cookies, HttpSession).
        // For stateless security, every single request must carry the user's credentials (e.g., in a JWT).
        http.sessionManagement(session ->
                // Standard stateless preference for large, professional projects.
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //http.formLogin(withDefaults()); // Commented out standard username/password form login

        // --- 3. HTTP Basic Authentication ---
        // Critical for standardizing the **Authentication Flow**.
        // Standard, simple authentication method for APIs (reinforced by predictable execution).
        // The browser or API client will provide 'Authorization' header like: 'Basic [Base64(username:password)]'.
        http.httpBasic(withDefaults());

        // --- 4. Disable CSRF only for H2 console paths ---
        // Critical for third-party tools (like H2 Console or standard cURL on Windows) which
        // do not carry standard CSRF tokens in state-changing requests (like POST).
        // This systematically resolves the 'unmatched brace' and 'profile!!!}}' re-authentication
        // errors you encountered previously.
        http.csrf(csrf ->
                // Instructs Spring Security to skip CSRF validation for any URL matching this pattern.
                csrf.ignoringRequestMatchers("/h2-console/**"));

        // --- 5. Set X-Frame-Options to SAME ORIGIN for frames to work ---
        // Standard Frame Options configuration (reinforcing standardized execution).
        // By default, Spring sets 'X-Frame-Options' to 'DENY' (prevents standard "Clickjacking").
        // We set it to 'SAMEORIGIN' to allow frames from this same domain (localhost).
        // This is necessary for H2 Console's standard bilateral relationship views (JPA associations) to load properly.
        http.headers(headers ->
                // Instructs browser to only display content in frames from the same origin.
                headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }

    // ==========================================
    // 2. User Management (The User Database)
    // ==========================================

    // Bean producing the standard source for user data.
    @Bean
    public UserDetailsService userDetailsService(){

        // Defines an immutable, standard set of user properties.
        UserDetails user = User.withUsername("user")
                // {noop} is a mandatory standard prefix when using plain text passwords.
                // Critical for debugging in standard tooling; never use plain text in large, professional projects.
                .password(passwordEncoder().encode("user"))
                // A specialized authority that maps to 'ROLE_USER'.
                .roles("USER")
                // Final standard build step.
                .build();

        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN")
                .build();

        // In-memory implementation of UserDetailsService. It's only for standard testing,
        // reinforcing predictable error handling. A professional application requires loading
        // user state ([SocialUser] and [SocialProfile]) from an external database for Horizontal Scaling.

        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
        userDetailsManager.createUser(user);
        userDetailsManager.createUser(admin);
        return  userDetailsManager;
        //return new InMemoryUserDetailsManager(user, admin);
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