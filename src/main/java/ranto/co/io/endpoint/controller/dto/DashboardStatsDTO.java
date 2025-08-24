package ranto.co.io.endpoint.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
  private long clientsActifs;
  private long produitsEnStock;
  private double chiffreAffaires;
  private long totalCommandes;
}
