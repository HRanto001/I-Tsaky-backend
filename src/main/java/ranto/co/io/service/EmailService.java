package ranto.co.io.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ranto.co.io.mail.EmailLog;
import ranto.co.io.mail.EmailRequest;
import ranto.co.io.repository.EmailLogRepository;

@Service
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender mailSender;
  private final EmailLogRepository emailLogRepository;

  public void envoyerEmail(EmailRequest request) {
    EmailLog log =
        EmailLog.builder()
            .recipient(request.getTo())
            .subject(request.getSubject())
            .body(request.getBody())
            .status("EN_ATTENTE")
            .sentAt(LocalDateTime.now())
            .build();

    emailLogRepository.save(log);

    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);

      helper.setTo(request.getTo());
      helper.setSubject(request.getSubject());
      helper.setText(request.getBody(), true);

      mailSender.send(message);

      log.setStatus("ENVOYE");
    } catch (MessagingException e) {
      log.setStatus("ECHEC");
    }

    emailLogRepository.save(log);
  }
}
