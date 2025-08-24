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

        Long orders = reportService.countOrders(LocalDate.from(startDate));
        Double revenue = reportService.sumRevenue(LocalDate.from(startDate));
        Double expenses = reportService.sumExpenses(LocalDate.from(startDate));
        List<TopProductDto> topProducts = reportService.findTopProducts(LocalDate.from(startDate));

        String growth = "+0%"; // tu peux calculer par rapport à la période précédente

        Map<String, Object> sales = Map.of(
                "revenue", revenue,
                "orders", orders,
                "growth", growth
        );

        Map<String, Object> expensesMap = Map.of(
                "total", expenses,
                "main", "Matière première: XXX Ar"
        );

        Map<String, Object> response = new HashMap<>();
        response.put("sales", sales);
        response.put("expenses", expensesMap);
        response.put("topProducts", topProducts);

        return response;
    }

    private LocalDateTime getStartDate(String period) {
        LocalDateTime now = LocalDateTime.now();
        switch (period) {
            case "week": return now.minusWeeks(1);
            case "month": return now.minusMonths(1);
            case "year": return now.minusYears(1);
            default: return now.minusMonths(1);
        }
    }
}
