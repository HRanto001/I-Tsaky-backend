package ranto.co.io.endpoint.controller.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest {
  private String email;
  private String activationKey;
  private String newPassword;
}
