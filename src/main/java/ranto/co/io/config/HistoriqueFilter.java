package ranto.co.io.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import ranto.co.io.service.HistoriqueService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class HistoriqueFilter extends OncePerRequestFilter {

    private final HistoriqueService historiqueService;

    public HistoriqueFilter(HistoriqueService historiqueService) {
        this.historiqueService = historiqueService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
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

            // Lire le payload depuis le cache
            String payload = "";
            byte[] buf = request.getContentAsByteArray();
            if (buf.length > 0) {
                payload = new String(buf, StandardCharsets.UTF_8);
            }

            historiqueService.enregistrer(methode, endpoint, utilisateur, payload);
        }
    }
}
