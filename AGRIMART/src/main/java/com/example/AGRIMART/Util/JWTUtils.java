
package com.example.AGRIMART.Util;

        import io.jsonwebtoken.*;
        import org.springframework.stereotype.Component;
        import java.util.Date;
        import java.util.HashMap;
        import java.util.Map;

@Component
public class JWTUtils {

    private String secret = "YourSecretKey"; // Replace with a secure secret key
    private int jwtExpirationMs = 86400000; // Token validity (1 day)

    // Generate token
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    // Extract username from token
    public String extractUsername(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    // Validate token
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
