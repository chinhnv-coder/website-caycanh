package assignment.assignment.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Categories")
public class Category {
    @Id
    private String id;  

    @Column(columnDefinition = "NVARCHAR(50)", nullable = false)
    private String name;

    @OneToMany(mappedBy = "category")
    List<Product> products;
}
