package ranto.co.io.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.function.Function;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  private final String SECRET_KEY =
      "3s5djhsiooixxx5sjkappok2fz5eef4445454rtrdfgdfegtrs5s4gr454gdgf";
  private final long EXPIRATION_MS = 1000 * 60 * 60 * 10; // 10 heures

  // Générer un token avec email et rôle
  public String generateToken(String email, String role) {
    return Jwts.builder()
        .setSubject(email)
        .claim("role", role)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
        .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
        .compact();
  }

  // Extraire l'email
  public String getEmailFromToken(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  // Extraire le rôle
  public String getRoleFromToken(String token) {
    return extractClaim(token, claims -> claims.get("role", String.class));
  }

  // Vérifier l'expiration
  public Boolean isTokenExpired(String token) {
    return extractClaim(token, Claims::getExpiration).before(new Date());
  }

  // Valider token
  public Boolean validateToken(String token) {
    return !isTokenExpired(token);
  }

  // Méthodes internes
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
  }
}
