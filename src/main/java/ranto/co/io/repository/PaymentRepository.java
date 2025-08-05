package ranto.co.io.repository;

import org.springframework.stereotype.Repository;
import ranto.co.io.entity.Payment;
import ranto.co.io.entity.PaymentStatus;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PaymentRepository {

    private final DataSource dataSource;

    public PaymentRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(Payment payment) throws SQLException {
        String sql = "INSERT INTO payment (date, amount, method, psp_payment_id, psp_type, payer_email, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(payment.getDate()));
            stmt.setLong(2, payment.getAmount());
            stmt.setString(3, payment.getMethod());
            stmt.setString(4, payment.getPspPaymentId());
            stmt.setString(5, payment.getPspType());
            stmt.setString(6, payment.getPayerEmail());
            stmt.setString(7, payment.getStatus().toString());

            stmt.executeUpdate();
        }
    }

    public List<Payment> findAll() throws SQLException {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payment";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                payments.add(mapRowToPayment(rs));
            }
        }

        return payments;
    }

    public List<Payment> findByStatus(PaymentStatus status) throws SQLException {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payment WHERE status = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    payments.add(mapRowToPayment(rs));
                }
            }
        }

        return payments;
    }

    private Payment mapRowToPayment(ResultSet rs) throws SQLException {
        return new Payment(
                rs.getTimestamp("date").toLocalDateTime(),
                rs.getLong("amount"),
                rs.getString("method"),
                rs.getString("psp_type"),
                rs.getString("payer_email"),
                PaymentStatus.valueOf(rs.getString("status")),
                rs.getString("psp_payment_id")
        );
    }
}
