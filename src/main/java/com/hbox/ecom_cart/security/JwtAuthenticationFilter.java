package com.hbox.ecom_cart.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.AllArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtTokenProvider tokenProvider;
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("**********Inside Authentication Entry Point**********");

        // Get JWT token from HTTP request
        String token = getTokenFromRequest(request);
        System.out.println("Token: " + token);

        try {
            // validate Token
            if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {

                // Get username from the token, it fetches the email of the user
                String username = tokenProvider.getUsername(token);
                String role = tokenProvider.getRoleFromToken(token);
                Long userId = tokenProvider.getUserIdFromToken(token);
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);

                System.out.println("Decoded Username: " + username);
                System.out.println("Decoded Role: " + role);
                System.out.println("Decoded User ID: " + userId);

                UserDetails userDetails;
                try {
                    userDetails = userDetailsService.loadUserByUsername(username);
                } catch (UsernameNotFoundException exception) {
                    System.out.println("UsernameNotFoundException: " + exception.getMessage());
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage());
                    return;
                }
                // String userId = userRepository.findByEmail(username).map(User::getId).map(String::valueOf).orElse(null);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                System.out.println("userdetails Password: " + userDetails.getPassword());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                System.out.println("Authentication set in contextHolder: " + auth);
            }
            else
            {
                System.out.println("Invalid token");
            }
        }
        catch(Exception exception)
        {
            System.out.println("Error in authentication process: " + exception.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication error");
            return;
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

}
