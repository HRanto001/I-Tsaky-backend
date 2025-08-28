package ranto.co.io.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import ranto.co.io.service.HistoriqueService;

@Component
public class HistoriqueFilter extends OncePerRequestFilter {

  private final HistoriqueService historiqueService;

  public HistoriqueFilter(HistoriqueService historiqueService) {
    this.historiqueService = historiqueService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    // ⚠️ wrap la request pour pouvoir relire le body après
    ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);

    try {
      // continue la chaîne normalement
      filterChain.doFilter(wrappedRequest, response);
    } finally {
      // Une fois le traitement fini, tu peux lire le body
      enregistrerHistorique(wrappedRequest);
    }
  }

  private void enregistrerHistorique(ContentCachingRequestWrapper request) {
    String methode = request.getMethod();

    if (methode.equals("POST") || methode.equals("PUT") || methode.equals("DELETE")) {
      String endpoint = request.getRequestURI();
      String utilisateur = request.getRemoteUser() != null ? request.getRemoteUser() : "ANONYMOUS";

      // Lire le payload
      String payload = "";
      byte[] buf = request.getContentAsByteArray();
      if (buf.length > 0) {
        payload = new String(buf, StandardCharsets.UTF_8);
      }

      // 🚨 Sécurité : ne jamais stocker les mots de passe
      if (endpoint.contains("/login") || endpoint.contains("/register")) {
        try {
          // On parse le JSON et on supprime le champ password
          ObjectMapper mapper = new ObjectMapper();
          JsonNode jsonNode = mapper.readTree(payload);
          if (jsonNode.has("motDePasse")) {
            ((ObjectNode) jsonNode).put("motDePasse", "***SECRET***");
          }
          payload = mapper.writeValueAsString(jsonNode);
        } catch (Exception e) {
          // fallback si parsing échoue
          payload = "{ \"message\": \"Payload sensible masqué\" }";
        }
      }

      historiqueService.enregistrer(methode, endpoint, utilisateur, payload);
    }
  }
}
