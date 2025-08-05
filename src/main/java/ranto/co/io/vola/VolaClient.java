package ranto.co.io.vola;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class VolaClient {

  @Value("${vola.api.url}")
  private String baseUrl;

  @Value("${vola.api.key}")
  private String apiKey;

  private final RestTemplate restTemplate = new RestTemplate();

  public JsonNode createPayment(String payerEmail, String pspPaymentId) {
    String url =
        UriComponentsBuilder.fromHttpUrl(baseUrl + "/payment")
            .queryParam("apiKey", apiKey)
            .queryParam("payerEmail", payerEmail)
            .queryParam("pspType", "ORANGE_MONEY")
            .queryParam("pspPaymentId", pspPaymentId)
            .toUriString();

    try {
      return restTemplate.postForObject(url, null, JsonNode.class);
    } catch (Exception e) {
      throw new RuntimeException("Erreur POST Vola : " + e.getMessage());
    }
  }

  public JsonNode checkPayment(String payerEmail, String pspPaymentId, String paymentId) {
    String url =
        UriComponentsBuilder.fromHttpUrl(baseUrl + "/payment")
            .queryParam("apiKey", apiKey)
            .queryParam("payerEmail", payerEmail)
            .queryParam("pspType", "ORANGE_MONEY")
            .queryParam("pspPaymentId", pspPaymentId)
            .toUriString();

    try {
      return restTemplate.getForObject(url, JsonNode.class);
    } catch (Exception e) {
      return null; // silencieux
    }
  }
}
