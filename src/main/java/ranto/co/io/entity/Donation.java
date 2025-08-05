package ranto.co.io.entity;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Donation {
  private Donor donor;
  private Payment payment;
}
