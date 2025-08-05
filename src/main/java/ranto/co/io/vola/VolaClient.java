package ranto.co.io.vola;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class VolaClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${vola.api.url}")
    private String volaBaseUrl;

    @Value("${vola.api.key}")
    private String apiKey;

    public JsonNode checkPayment(String payerEmail, String pspType, String pspPaymentId) {
        String url = UriComponentsBuilder
                .fromHttpUrl(volaBaseUrl + "/payment")
                .queryParam("apiKey", apiKey)
                .queryParam("payerEmail", payerEmail)
                .queryParam("pspType", pspType)
                .queryParam("pspPaymentId", pspPaymentId)
                .toUriString();

        log.info("Checking payment status from Vola for {}", pspPaymentId);

        try {
            return restTemplate.getForObject(url, JsonNode.class);
        } catch (Exception e) {
            log.warn("Erreur lors de l'appel à Vola : {}", e.getMessage());
            return null;
        }
    }
}
