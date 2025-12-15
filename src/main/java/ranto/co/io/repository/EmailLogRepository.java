package ranto.co.io.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ranto.co.io.mail.EmailLog;

public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {}
