package ranto.co.io.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.stereotype.Repository;
import ranto.co.io.entity.Donation;
import ranto.co.io.entity.Donor;
import ranto.co.io.entity.Payment;

@Repository
public class DonationRepository {

  private final DataSource dataSource;

  public DonationRepository(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void save(Donation donation) throws SQLException {
    try (Connection conn = dataSource.getConnection()) {
      // Insérer donor
      String insertDonorSQL = "INSERT INTO donor (name, email) VALUES (?, ?) RETURNING id";
      int donorId;
      try (PreparedStatement stmt = conn.prepareStatement(insertDonorSQL)) {
        stmt.setString(1, donation.getDonor().getName());
        stmt.setString(2, donation.getDonor().getEmail());

        ResultSet rs = stmt.executeQuery();
        rs.next();
        donorId = rs.getInt("id");
      }

      // Insérer payment
      String insertPaymentSQL =
          "INSERT INTO payment (date, amount, method, psp_payment_id, psp_type, payer_email,"
              + " status) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";
      int paymentId;
      try (PreparedStatement stmt = conn.prepareStatement(insertPaymentSQL)) {
        Payment payment = donation.getPayment();
        stmt.setTimestamp(1, Timestamp.valueOf(payment.getDate()));
        stmt.setLong(2, payment.getAmount());
        stmt.setString(3, payment.getMethod());
        stmt.setString(4, payment.getPspPaymentId());
        stmt.setString(5, payment.getPspType());
        stmt.setString(6, payment.getPayerEmail());
        stmt.setString(7, payment.getStatus().toString());

        ResultSet rs = stmt.executeQuery();
        rs.next();
        paymentId = rs.getInt("id");
      }

      // Insérer donation (association)
      String insertDonationSQL = "INSERT INTO donation (donor_id, payment_id) VALUES (?, ?)";
      try (PreparedStatement stmt = conn.prepareStatement(insertDonationSQL)) {
        stmt.setInt(1, donorId);
        stmt.setInt(2, paymentId);
        stmt.executeUpdate();
      }
    }
  }

  public List<Donation> findAll() throws SQLException {
    List<Donation> donations = new ArrayList<>();

    String sql =
        """
        SELECT d.id as donation_id,
               donor.id as donor_id, donor.name as donor_name, donor.email as donor_email,
               payment.id as payment_id, payment.date, payment.amount, payment.method,
               payment.psp_payment_id, payment.psp_type, payment.payer_email, payment.status
        FROM donation d
        JOIN donor ON d.donor_id = donor.id
        JOIN payment ON d.payment_id = payment.id
        ORDER BY payment.date DESC
        """;

    try (Connection conn = dataSource.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {

      while (rs.next()) {
        Donor donor = new Donor();
        donor.setName(rs.getString("donor_name"));
        donor.setEmail(rs.getString("donor_email"));

        Payment payment = new Payment();
        payment.setDate(rs.getTimestamp("date").toLocalDateTime());
        payment.setAmount(rs.getLong("amount"));
        payment.setMethod(rs.getString("method"));
        payment.setPspPaymentId(rs.getString("psp_payment_id"));
        payment.setPspType(rs.getString("psp_type"));
        payment.setPayerEmail(rs.getString("payer_email"));
        payment.setStatus(
            Enum.valueOf(ranto.co.io.entity.PaymentStatus.class, rs.getString("status")));

        Donation donation = new Donation();
        donation.setDonor(donor);
        donation.setPayment(payment);

        donations.add(donation);
      }
    }
    return donations;
  }
}
