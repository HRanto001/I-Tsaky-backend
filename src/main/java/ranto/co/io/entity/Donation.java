package ranto.co.io.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Donation {
  private Donor donor;
  private Payment payment;
}
