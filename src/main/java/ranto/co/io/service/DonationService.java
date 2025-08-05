package ranto.co.io.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ranto.co.io.entity.*;
import ranto.co.io.repository.DonationRepository;
import ranto.co.io.vola.VolaClient;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final DonationRepository donationRepository;
    private final VolaClient volaClient;

    public void createDonation(String name, String email, long amount, String pspPaymentId) {
        Donor donor = new Donor(name, email);

        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setDate(LocalDateTime.now());
        payment.setMethod("ORANGE_MONEY");
        payment.setPspPaymentId(pspPaymentId);
        payment.setPayerEmail(email);
        payment.setPspType("ORANGE_MONEY");
        payment.setStatus(PaymentStatus.VERIFYING);

        // Appel vers Vola (POST)
        volaClient.createPayment(email, pspPaymentId);

        Donation donation = new Donation(donor, payment);

        // Sauvegarde avec gestion d'exception
        try {
            donationRepository.save(donation);
        } catch (Exception e) {
            // Log ou gestion d’erreur spécifique selon contexte
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la sauvegarde de la donation", e);
        }
    }
}
