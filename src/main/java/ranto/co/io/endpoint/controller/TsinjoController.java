package ranto.co.io.endpoint.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ranto.co.io.entity.Donation;
import ranto.co.io.entity.Donor;
import ranto.co.io.entity.Payment;
import ranto.co.io.entity.PaymentStatus;
import ranto.co.io.entity.Help;
import ranto.co.io.repository.DonationRepository;
import ranto.co.io.repository.HelpRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TsinjoController {

    private final DonationRepository donationRepository;
    private final HelpRepository helpRepository;

    @GetMapping("/")
    public String showPage(Model model) throws SQLException {
        List<Donation> donations = donationRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(d -> d.getPayment().getDate(), Comparator.reverseOrder()))
                .toList();

        List<Help> helps = helpRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(h -> h.getPayment().getDate(), Comparator.reverseOrder()))
                .toList();

        model.addAttribute("donations", donations);
        model.addAttribute("helps", helps);

        return "index";
    }

    @PostMapping("/donations")
    public String submitDonation(@RequestParam String name,
                                 @RequestParam String email,
                                 @RequestParam long amount,
                                 @RequestParam String pspPaymentId) {
        Donor donor = new Donor();
        donor.setName(name);
        donor.setEmail(email);

        Payment payment = new Payment();
        payment.setDate(LocalDateTime.now());
        payment.setAmount(amount);
        payment.setPspPaymentId(pspPaymentId);
        payment.setPspType("ORANGE_MONEY");
        payment.setStatus(PaymentStatus.VERIFYING);
        payment.setPayerEmail(email);
        payment.setMethod("ORANGE_MONEY");

        Donation donation = new Donation();
        donation.setDonor(donor);
        donation.setPayment(payment);

        try {
            donationRepository.save(donation);
        } catch (Exception e) {
            e.printStackTrace();
            // Ici, tu peux aussi logger l'erreur ou retourner une vue d'erreur
        }

        return "redirect:/";
    }
}
