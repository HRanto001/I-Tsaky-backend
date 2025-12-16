package ranto.co.io.endpoint.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ranto.co.io.mail.EmailRequest;
import ranto.co.io.service.EmailService;

@RestController
@RequestMapping("/api/emails")
@RequiredArgsConstructor
public class EmailController {

  private final EmailService emailService;

  @PostMapping("/send")
  public ResponseEntity<String> sendEmail(@Valid @RequestBody EmailRequest request) {
    emailService.envoyerEmail(request);
    return ResponseEntity.ok("Email traité (voir statut en base) !");
  }
}
