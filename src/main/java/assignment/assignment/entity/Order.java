package assignment.assignment.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "Createdate")
    private Date createDate = new Date();

    @Column(columnDefinition = "NVARCHAR(255)", nullable = false)
    private String address;

    @ManyToOne
    @JoinColumn(name = "username")
    private Account account;

    @OneToMany(mappedBy = "order")
    List<OrderDetail> orderDetails;

    // Tính tổng số lượng sản phẩm của đơn hàng
    public int getTotalQuantity() {
        return orderDetails.stream()
                .mapToInt(OrderDetail::getQuantity)
                .sum();
    }

    public double getTotalAmount() {
        return orderDetails.stream()
                .mapToDouble(OrderDetail::getPrice) 
                .sum();
    }    
    
}
