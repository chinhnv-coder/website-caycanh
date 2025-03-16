package assignment.assignment.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "OrderDetails")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double price;
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "Orderid")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "Productid")
    private Product product;

}
