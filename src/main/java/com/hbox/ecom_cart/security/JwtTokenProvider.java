package com.hbox.ecom_cart.security;

import com.hbox.ecom_cart.entity.User;
import com.hbox.ecom_cart.exception.EcomCartException;
import com.hbox.ecom_cart.repositoty.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Key;
import java.util.Date;


@Component
public class JwtTokenProvider {

    @Autowired
    private UserRepository userRepository;

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-expiration-milliseconds}")
    private Long jwtExpirationDate;

    // Generate JWT Token
    public String generateToken(Authentication authentication) {
        //String username = authentication.getName();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() ->
                        new EcomCartException(HttpStatus.NOT_FOUND, "User Not Found")); // new UsernameNotFoundException("User not found!"));

        Date currentDate = new Date();
        Date expiryDate = new Date(currentDate.getTime() + jwtExpirationDate);

        String role = authentication.getAuthorities().iterator().next().getAuthority();
        System.out.println("role: " + role);

        authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authentication: " + authentication);

        String token = Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getId())
                .claim("role", role)
                .setIssuedAt(currentDate)
                .setExpiration(expiryDate)
                .signWith(key())
                .compact();
        System.out.println("**********Inside Generated Token**********");
        System.out.println("token: " + token);
        return token;
    }

    private Key key()
    {
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret));
    }

    // Get user id from JWT Token
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("userId", Long.class);
    }

    // Get username(email) from JWT Token
    public String getUsername(String token)
    {
        Claims claims = Jwts.parser()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();

        String username = claims.getSubject();
        //List<String> role = claims.get("role", List.class);

//        List<GrantedAuthority> authorities = role.stream()
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toList());
        return username;
    }

    // Get Role from token
    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return  claims.get("role", String.class);
    }

    // Validate JWT Token
    public boolean validateToken(String token) {
        Jwts.parser()
                .setSigningKey(key())
                .build()
                .parse(token);
        return true;
    }
}
