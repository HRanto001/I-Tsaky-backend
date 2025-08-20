package ranto.co.io.endpoint.controller;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class MockDashboardController {

    // --- Commandes récentes ---
    @GetMapping("/orders/recent")
    public List<Map<String, Object>> getRecentOrders() {
        return List.of(
                Map.of("id", 1, "client", "Epicerie Tsena Soa", "total", "17 000 Ar", "status", "PAYEE", "date", "2025-01-15"),
                Map.of("id", 2, "client", "Jean Rakoto", "total", "4 500 Ar", "status", "EN_ATTENTE", "date", "2025-01-15"),
                Map.of("id", 3, "client", "Shoprite Analakely", "total", "25 000 Ar", "status", "LIVREE", "date", "2025-01-14")
        );
    }

    // --- Produits en stock faible ---
    @GetMapping("/stocks/low")
    public List<Map<String, Object>> getLowStock() {
        return List.of(
                Map.of("name", "Chips Voanjo", "stock", 15, "seuil", 20),
                Map.of("name", "Cacapigeon", "stock", 8, "seuil", 15)
        );
    }

    // --- Stats Dashboard ---
    @GetMapping("/dashboard/stats")
    public List<Map<String, Object>> getStats() {
        return List.of(
                Map.of("title", "Chiffre d'affaires", "value", "2 450 000 Ar", "change", "+12.5%", "trend", "up", "icon", "DollarSign", "color", "green"),
                Map.of("title", "Commandes", "value", "145", "change", "+8.2%", "trend", "up", "icon", "ShoppingCart", "color", "blue"),
                Map.of("title", "Clients actifs", "value", "87", "change", "+15.3%", "trend", "up", "icon", "Users", "color", "purple"),
                Map.of("title", "Produits en stock", "value", "12", "change", "-2", "trend", "down", "icon", "Package", "color", "amber")
        );
    }
}
