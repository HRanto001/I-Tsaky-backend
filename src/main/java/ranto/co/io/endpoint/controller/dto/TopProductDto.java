package ranto.co.io.endpoint.controller.dto;

public class TopProductDto {
    private String name;
    private Long quantity;
    private Double sales;

    public TopProductDto(String name, Long quantity, Double sales) {
        this.name = name;
        this.quantity = quantity;
        this.sales = sales;
    }

    // getters & setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getQuantity() { return quantity; }
    public void setQuantity(Long quantity) { this.quantity = quantity; }

    public Double getSales() { return sales; }
    public void setSales(Double sales) { this.sales = sales; }
}
