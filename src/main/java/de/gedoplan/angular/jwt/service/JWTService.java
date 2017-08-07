package de.gedoplan.angular.jwt.service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.NotAuthorizedException;

/**
 * @author Dominik Mathmann
 */
@ApplicationScoped
public class JWTService {

  private List<String> validJWTTokens = new ArrayList();

  public String generateJWTToken(String user) {
    // ...read user information from database...
    String token = Jwts.builder()
        .setSubject(user)
        .claim("groups", new String[] { "admin", "customer" })
        .claim("mail", "dominik.mathmann@gedoplan.de")
        .signWith(SignatureAlgorithm.HS512, System.getProperty("JWT-KEY"))
        .compact();

    this.validJWTTokens.add(token);
    return token;
  }

  public void valid(String token) {
    if (!this.validJWTTokens.contains(token)) {
      throw new RuntimeException("Token is not valid anymore");
    }

    JwtParser signed = Jwts.parser().setSigningKey(System.getProperty("JWT-KEY"));

    String username = signed.parseClaimsJws(token).getBody().getSubject();
    System.out.println("Request is JWT-sigend with user: " + username);
  }

  public void removeToken(String token) {
    this.validJWTTokens.remove(token);
  }
}
