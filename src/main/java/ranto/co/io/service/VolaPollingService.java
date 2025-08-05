package ranto.co.io.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ranto.co.io.entity.Payment;
import ranto.co.io.entity.PaymentStatus;
import ranto.co.io.repository.PaymentRepository;
import ranto.co.io.vola.VolaClient;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VolaPollingService {

    private final PaymentRepository paymentRepository;
    private final VolaClient volaClient;

    @Scheduled(fixedDelay = 60000)
    public void pollPayments() {
        List<Payment> verifyingPayments = paymentRepository.findByStatus(PaymentStatus.VERIFYING);

        for (Payment payment : verifyingPayments) {
            JsonNode volaResponse = volaClient.checkPayment(
                    payment.getPayerEmail(),
                    payment.getPspType(),
                    payment.getPspPaymentId()
            );

            if (volaResponse == null) continue;

            String newStatus = volaResponse.get("verificationStatus").asText();
            if (!newStatus.equalsIgnoreCase("VERIFYING")) {
                payment.setStatus(PaymentStatus.valueOf(newStatus));
                paymentRepository.save(payment);
                log.info("Mise à jour du paiement {} avec statut {}", payment.getPspPaymentId(), newStatus);
            }
        }
    }
}
