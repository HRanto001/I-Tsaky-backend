package ranto.co.io.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(0)
public class ApiKeyFilter extends OncePerRequestFilter {

  @Value("${app.api.key}")
  private String validApiKey;

  private static final String API_KEY_HEADER = "X-API-KEY";

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String path = request.getRequestURI();

    // Vérifie uniquement pour /api/auth/? et /pingR
    if (path.startsWith("/api/auth/register")
        || path.equals("/api/auth/login")
        || path.equals("/api/auth/reset-password")
        || path.equals("/api/auth/request-activation-key")
        || path.equals("/api/auth/request-reset-password")
        || path.equals("/api/emails/send")
        || path.equals("/api/auth/check-email")
        || path.equals("/pingR")) {
      String apiKey = request.getHeader(API_KEY_HEADER);

      if (path.startsWith("/api/auth/") || path.equals("/pingR")) {
        filterChain.doFilter(request, response);
        return;
      }

      if (apiKey == null || !apiKey.equals(validApiKey)) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Invalid or missing API key");
        return;
      }
    }

    filterChain.doFilter(request, response);
  }
}
