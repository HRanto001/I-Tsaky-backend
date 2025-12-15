package ranto.co.io.mail;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class EmailRequest {
    @NotBlank @Email private String to;

    @NotBlank private String subject;

    @NotBlank private String body;
}
