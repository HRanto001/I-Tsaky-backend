package ranto.co.io.config;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ranto.co.io.service.HistoriqueService;

import java.io.IOException;

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

        String methode = request.getMethod();

        // On log seulement PUT, POST, DELETE
        if (methode.equals("POST") || methode.equals("PUT") || methode.equals("DELETE")) {
            String endpoint = request.getRequestURI();

            // si tu as la sécurité (Spring Security), tu récupères l’utilisateur authentifié
            String utilisateur = request.getRemoteUser() != null ? request.getRemoteUser() : "ANONYMOUS";

            // ⚠️ récupérer le body est tricky car il est déjà consommé par Spring
            // solution : utiliser ContentCachingRequestWrapper
            String payload = "[Payload non récupéré ici]";

            historiqueService.enregistrer(methode, endpoint, utilisateur, payload);
        }

        filterChain.doFilter(request, response);
    }
}
