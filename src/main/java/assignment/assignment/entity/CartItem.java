package assignment.assignment.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CartItem {
    private Product product;
    private Integer qty;
    private String username;
    public double getTotalPrice() {
        return (product.getPrice() > 0) ? product.getPrice() * qty : 0.0;
    }   
}
