package ranto.co.io.service;

import com.fasterxml.jackson.databind.JsonNode;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ranto.co.io.entity.Payment;
import ranto.co.io.entity.PaymentStatus;
import ranto.co.io.repository.PaymentRepository;
import ranto.co.io.vola.VolaClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class VolaPollingService {

  private final PaymentRepository paymentRepository;
  private final VolaClient volaClient;

  @Scheduled(fixedDelay = 60000)
  public void pollPayments() throws SQLException {
    List<Payment> verifyingPayments = paymentRepository.findByStatus(PaymentStatus.VERIFYING);

    for (Payment payment : verifyingPayments) {
      JsonNode volaResponse;
      try {
        volaResponse =
            volaClient.checkPayment(
                payment.getPayerEmail(), payment.getPspType(), payment.getPspPaymentId());
      } catch (Exception e) {
        log.error(
            "Erreur lors de la vérification du paiement {} : {}",
            payment.getPspPaymentId(),
            e.getMessage(),
            e);
        continue; // passer au paiement suivant
      }

      if (volaResponse == null) {
        log.warn("Réponse nulle pour le paiement {}", payment.getPspPaymentId());
        continue;
      }

      JsonNode statusNode = volaResponse.get("verificationStatus");
      if (statusNode == null) {
        log.warn(
            "Champ 'verificationStatus' manquant dans la réponse pour paiement {}",
            payment.getPspPaymentId());
        continue;
      }

      String newStatusStr = statusNode.asText();
      if (!newStatusStr.equalsIgnoreCase("VERIFYING")) {
        try {
          PaymentStatus newStatus = PaymentStatus.valueOf(newStatusStr);
          payment.setStatus(newStatus);
          paymentRepository.save(payment);
          log.info(
              "Mise à jour du paiement {} avec statut {}", payment.getPspPaymentId(), newStatus);
        } catch (IllegalArgumentException e) {
          log.error(
              "Statut de paiement inconnu '{}' pour paiement {}",
              newStatusStr,
              payment.getPspPaymentId());
        } catch (Exception e) {
          log.error(
              "Erreur lors de la sauvegarde du paiement {} : {}",
              payment.getPspPaymentId(),
              e.getMessage(),
              e);
        }
      }
    }
  }
}
