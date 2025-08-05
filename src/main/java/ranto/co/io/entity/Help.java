package ranto.co.io.entity;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Help {
  private Beneficiary beneficiary;
  private Payment payment;
  private String description;
}
