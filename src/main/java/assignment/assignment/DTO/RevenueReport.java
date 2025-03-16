package assignment.assignment.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevenueReport {
    private String group; // Tên sản phẩm
    private Double sum; // Tổng doanh thu
    private Long count; // Tổng số lượng
    private Double maxPrice; // Gía cao nhất
    private Double minPrice; // Gía thấp nhất
    private Double avgPrice; // Gía trung bình
}
