package assignment.assignment.service;

import assignment.assignment.DAO.OrderDAO;
import assignment.assignment.DAO.OrderDetailDAO;
import assignment.assignment.DTO.RevenueReport;
import assignment.assignment.DTO.VipCustomerReport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    @Autowired
    private OrderDetailDAO orderDetailDAO;

    @Autowired
    private OrderDAO orderDAO;
    
    // Lấy báo cáo doanh thu theo sản phẩm
    public List<RevenueReport> getRevenueReports() {
        return orderDetailDAO.getRevenueReports();
    }

    // Lấy danh sách 10 khách hàng VIP
    public List<VipCustomerReport> getVipCustomerReports() {
        List<VipCustomerReport> vipCustomers = orderDAO.getTop10VipCustomers();
        return vipCustomers.size() > 10 ? vipCustomers.subList(0, 10) : vipCustomers;
    }
}
