package ranto.co.io.endpoint.controller;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import ranto.co.io.repository.CommandeRepository;
import ranto.co.io.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

  private final DashboardService dashboardService;
  private final CommandeRepository commandeRepository;

  public DashboardController(
      DashboardService dashboardService, CommandeRepository commandeRepository) {
    this.dashboardService = dashboardService;
    this.commandeRepository = commandeRepository;
  }

  @GetMapping("/benefice")
  public BeneficeResponse getBenefice() {
    BigDecimal benefice = dashboardService.calculerBenefice();
    return new BeneficeResponse(benefice);
  }

  // DTO simple pour JSON
  public static class BeneficeResponse {
    private BigDecimal benefice;

    public BeneficeResponse(BigDecimal benefice) {
      this.benefice = benefice;
    }

    public BigDecimal getBenefice() {
      return benefice;
    }

    public void setBenefice(BigDecimal benefice) {
      this.benefice = benefice;
    }
  }

  @GetMapping("/top-products")
  public List<Object[]> getTopProducts() {
    return commandeRepository.findTopProducts();
  }
}
