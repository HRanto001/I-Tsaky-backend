package ranto.co.io.endpoint.controller;

import org.springframework.web.bind.annotation.*;
import ranto.co.io.endpoint.controller.dto.TopProductDto;
import ranto.co.io.service.ReportService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequestMapping("/api")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/reports")
    public Map<String, Object> getReports(@RequestParam String period) {
        LocalDateTime startDate = getStartDate(period);
        LocalDateTime prevStartDate = getPreviousStartDate(period);

        // ⚡ Période actuelle
        Long orders = reportService.countOrders(startDate);
        Double revenue = reportService.sumRevenue(LocalDate.from(startDate));
        Double expenses = reportService.sumExpenses(LocalDate.from(startDate));
        List<TopProductDto> topProducts = reportService.findTopProducts(startDate);
        String mainExpense = reportService.findMainExpense(LocalDate.from(startDate));

        // ⚡ Période précédente (pour le calcul du growth)
        Double prevRevenue = reportService.sumRevenue(LocalDate.from(prevStartDate));

        // Calcul du growth
        String growth = reportService.calculateGrowth(revenue, prevRevenue);

        // Construction de la réponse
        Map<String, Object> sales = Map.of(
                "revenue", revenue,
                "orders", orders,
                "growth", growth
        );

        Map<String, Object> expensesMap = Map.of(
                "total", expenses,
                "main", mainExpense
        );

        Map<String, Object> response = new HashMap<>();
        response.put("sales", sales);
        response.put("expenses", expensesMap);
        response.put("topProducts", topProducts);

        return response;
    }

    private LocalDateTime getStartDate(String period) {
        LocalDateTime now = LocalDateTime.now();
        return switch (period) {
            case "week" -> now.minusWeeks(1);
            case "month" -> now.minusMonths(1);
            case "year" -> now.minusYears(1);
            default -> now.minusMonths(1);
        };
    }

    @GetMapping("/growth")
    public Map<String, String> getGrowth(@RequestParam String period) {
        LocalDateTime start = getStartDate(period);
        LocalDateTime end = LocalDateTime.now();
        String growth = reportService.calculateGrowth(LocalDate.from(start).atStartOfDay(), LocalDate.from(end).atStartOfDay());
        return Map.of("growth", growth);
    }

    @GetMapping("/sales-goal")
    public Map<String, String> getSalesGoal() {
        return Map.of("salesGoal", reportService.getSalesGoal());
    }

    @GetMapping("/customer-satisfaction")
    public Map<String, String> getCustomerSatisfaction() {
        return Map.of("customerSatisfaction", reportService.getCustomerSatisfaction());
    }

    private LocalDateTime getPreviousStartDate(String period) {
        LocalDateTime now = LocalDateTime.now();
        return switch (period) {
            case "week" -> now.minusWeeks(2);
            case "month" -> now.minusMonths(2);
            case "year" -> now.minusYears(2);
            default -> now.minusMonths(2);
        };
    }
}
