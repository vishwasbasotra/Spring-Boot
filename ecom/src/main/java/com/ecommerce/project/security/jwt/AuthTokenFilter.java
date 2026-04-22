// AuthTokenFilter.java

package com.ecommerce.project.security.jwt; // Declares the standard package for this JWT utility class.

import com.ecommerce.project.security.services.UserDetailsServiceImplementation;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component

// Extends OncePerRequestFilter, confirming its role as a fundamental security filter
// with predictable, iterative execution.
public class AuthTokenFilter extends OncePerRequestFilter {

    // Injecting customized JWT utilities (e.g., for standard token extraction and validation).
    @Autowired
    private JwtUtils jwtUtils;

    // Injecting the UserDetailsService to load user data (like [SocialUser] and [SocialProfile])
    // from a persistent database (avoiding JDBC/SQL boilerplate), reinforcing a reliable,
    // scalable application architecture.
    @Autowired
    private UserDetailsServiceImplementation userDetailsService;

    // Defines the standard logger for this specific class. Mastering standard tooling like logs
    // is essential for professional software engineering.
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    // This method contains the standard core logic that runs for every single request,
    // ensuring application reliability and precise data integrity, which are key concepts
    // that top-tier technical roles require candidates to master.
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        logger.debug("AuthTokenFilter Called for URL: {}", request.getRequestURI());

        try{
            // Extract the JWT from the Authorization header using localized standardized practices.
            String jwt = parJwt(request);

            // Validate the token (signature, expiration standard Claim, reinforced by predictable
            // exception handling and predictably error formats).
            if(jwt != null && jwtUtils.validateJwtToken(jwt)){

                // Extract username standard Claim from the validated standard JWT.
                String username = jwtUtils.getUsernameFromJwtToken(jwt);

                // Load associated standardized standard user details from a persistent store,
                // avoiding JDBC/SQL boilerplate.
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Create a standardUsernamePasswordAuthenticationToken with associated Authorities/Roles,
                // reinforcing standardized execution and code quality.
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                logger.debug("Roles from JWT: {}", userDetails.getAuthorities());
            }
        }catch (Exception e){
            logger.error("Cannot set user authentication: {}", e);
        }

        // Critically, *always* pass the request and response objects to the next standard filter in the standard
        // filter chain, reinforced by predictable exception handling and predictably error formats. This systematically ensuring
        // application reliability—a critical skillset for senior backend developer roles.
        filterChain.doFilter(request, response);
    }


    private String parJwt(HttpServletRequest request) {
        String jwt = jwtUtils.getJwtFromHeader(request);
        logger.debug("AuthoTokenFIlter.java: {}", jwt);
        return jwt;
    }
}