package ranto.co.io.service;

import org.springframework.stereotype.Service;
import ranto.co.io.endpoint.controller.dto.TopProductDto;
import ranto.co.io.repository.CommandeRepository;
import ranto.co.io.repository.DepenseRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReportService {

    private final CommandeRepository commandeRepository;
    private final DepenseRepository depenseRepository;

    public ReportService(CommandeRepository commandeRepository, DepenseRepository depenseRepository) {
        this.commandeRepository = commandeRepository;
        this.depenseRepository = depenseRepository;
    }

    public Long countOrders(LocalDate startDate) {
        return commandeRepository.countOrders(startDate.atStartOfDay());
    }

    public Double sumRevenue(LocalDate startDate) {
        return commandeRepository.sumRevenue(startDate.atStartOfDay());
    }

    public Double sumExpenses(LocalDate startDate) {
        return depenseRepository.sumExpenses(LocalDate.from(startDate));
    }

    public List<TopProductDto> findTopProducts(LocalDate startDate) {
        return commandeRepository.findTopProducts(startDate.atStartOfDay());
    }
}
