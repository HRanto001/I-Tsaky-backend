package ranto.co.io.service;

import org.springframework.stereotype.Service;
import ranto.co.io.endpoint.controller.dto.TopProductDto;
import ranto.co.io.repository.CommandeRepository;
import ranto.co.io.repository.DepenseRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {

    private final CommandeRepository commandeRepository;
    private final DepenseRepository depenseRepository;

    public ReportService(CommandeRepository commandeRepository, DepenseRepository depenseRepository) {
        this.commandeRepository = commandeRepository;
        this.depenseRepository = depenseRepository;
    }

    public Long countOrders(LocalDateTime startDate) {
        return commandeRepository.countOrders(startDate);
    }

    public Double sumRevenue(LocalDate startDate) {
        Double result = commandeRepository.sumRevenue(startDate.atStartOfDay());
        return result != null ? result : 0.0;
    }

    public Double sumExpenses(LocalDate startDate) {
        Double result = depenseRepository.sumExpenses(startDate);
        return result != null ? result : 0.0;
    }

    public String findMainExpense(LocalDate startDate) {
        // ⚡ tu dois implémenter cette méthode dans ton DepenseRepository
        // qui renvoie la dépense la plus élevée (categorie + montant)
        return depenseRepository.findMainExpense(startDate);
    }

    public List<TopProductDto> findTopProducts(LocalDateTime startDate) {
        return commandeRepository.findTopProducts(startDate);
    }

    public String calculateGrowth(Double current, Double previous) {
        if (previous == null || previous == 0) return "+0%";
        double growth = ((current - previous) / previous) * 100;
        return String.format("%.2f%%", growth);
    }
}
