package assignment.assignment.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import assignment.assignment.DTO.RevenueReport;
import assignment.assignment.entity.OrderDetail;

public interface OrderDetailDAO extends JpaRepository<OrderDetail, Long>{

    @Query("""
        SELECT new assignment.assignment.DTO.RevenueReport(
            c.name, 
            SUM(od.price * od.quantity), 
            SUM(od.quantity), 
            MAX(od.price), 
            MIN(od.price), 
            AVG(od.price)
        ) 
        FROM OrderDetail od 
        JOIN od.product p 
        JOIN p.category c 
        GROUP BY c.name
    """)
    List<RevenueReport> getRevenueReports();
}
