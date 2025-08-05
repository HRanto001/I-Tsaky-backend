package ranto.co.io.entity;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
  private String payerEmail;
  private String pspPaymentId;
  private String pspType; // Exemple : "ORANGE_MONEY"
  private LocalDateTime date;
  private long amount;
  private String method;
  private PaymentStatus status;
}
