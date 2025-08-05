package ranto.co.io.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ranto.co.io.entity.Payment;
import ranto.co.io.entity.PaymentStatus;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByStatus(PaymentStatus status);
}
