package ranto.co.io.entity;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

  private LocalDateTime date;
  private long amount;
  private String method;

  private String pspType = "ORANGE_MONEY"; // constant
  private String payerEmail;
  private PaymentStatus status = PaymentStatus.VERIFYING;

  private String pspPaymentId;

  private static final Set<String> VALID_PAYMENT_IDS =
      Set.of(
          "MP250804.0904.A01637",
          "MP205804.0908.D15807",
          "MP250804.0910.A02057",
          "MP250804.1224.B31974",
          "MP250804.1224.B31976",
          "MP250804.1856.D63944");

  public void setPspPaymentId(String pspPaymentId) {
    if (!VALID_PAYMENT_IDS.contains(pspPaymentId)) {
      throw new IllegalArgumentException("ID de paiement invalide : " + pspPaymentId);
    }
    this.pspPaymentId = pspPaymentId;
  }
}
