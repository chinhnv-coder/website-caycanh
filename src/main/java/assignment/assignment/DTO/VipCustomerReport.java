package assignment.assignment.DTO;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VipCustomerReport {
    private String customerName; // Tên khách hàng
    private Double totalSpent;  // Tổng tiền đã chi tiêu
    private Date firstPurchaseDate; // Ngày mua đầu tiên
    private Date lastPurchaseDate; // Ngày mua sau cùng
}
