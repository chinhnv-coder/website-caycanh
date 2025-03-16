package assignment.assignment.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import assignment.assignment.DTO.RevenueReport;
import assignment.assignment.DTO.VipCustomerReport;
import assignment.assignment.service.ReportService;


@Controller
@RequestMapping("admin")
public class ReportAController {
    @Autowired
    ReportService reportService;

    // Thống kê doanh thu theo sản phẩm
    @GetMapping("/report/revenue")
    public String revenue(Model model) {
        List<RevenueReport> revenueReports = reportService.getRevenueReports();
        model.addAttribute("revenueReports", revenueReports);
        model.addAttribute("view", "admin/revenue");
        return "admin/layout";
    }
    
    // Thống kê khách hàng VIP
    @GetMapping("/report/vip")
    public String vip(Model model) {
       List<VipCustomerReport> vipCustomerReports = reportService.getVipCustomerReports();
       model.addAttribute("vipCustomerReports", vipCustomerReports);
       model.addAttribute("view", "admin/vip");
       return "admin/layout";
    }
    
}
