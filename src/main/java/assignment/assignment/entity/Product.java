package assignment.assignment.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "NVARCHAR(50)", nullable = false)
    private String name;
    
    private String image;
    private Double price;
    private Integer quantity;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "Createdate")
    private Date createDate = new Date();

    private Boolean available;

    @ManyToOne
    @JoinColumn(name = "categoryid")
    @ToString.Exclude
    private Category category;

    @OneToMany(mappedBy = "product")
    @ToString.Exclude
    private List<OrderDetail> orderDetails;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String description;
}
