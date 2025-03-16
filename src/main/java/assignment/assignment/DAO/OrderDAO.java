package assignment.assignment.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import assignment.assignment.DTO.VipCustomerReport;
import assignment.assignment.entity.Order;

public interface OrderDAO extends JpaRepository<Order, Long>{
    List<Order> findByAccount_Username(String username);
    Order findByIdAndAccount_Username(Long id, String username);
    
    // Truy vấn khách hàng VIP - tìm ra 10 khách hàng chi tiêu nhiều nhất 
    @Query("""
        SELECT new assignment.assignment.DTO.VipCustomerReport(
            a.fullname, 
            SUM(od.price * od.quantity), 
            MIN(o.createDate), 
            MAX(o.createDate)
        ) 
        FROM Order o 
        JOIN o.account a 
        JOIN o.orderDetails od 
        GROUP BY a.fullname 
        ORDER BY SUM(od.price * od.quantity) DESC
    """)
    List<VipCustomerReport> getTop10VipCustomers();
}
