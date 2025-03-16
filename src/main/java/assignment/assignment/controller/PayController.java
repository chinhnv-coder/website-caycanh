package assignment.assignment.controller;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import assignment.assignment.DAO.CategoryDAO;
import assignment.assignment.DAO.OrderDAO;
import assignment.assignment.DAO.OrderDetailDAO;
import assignment.assignment.entity.Account;
import assignment.assignment.entity.CartItem;
import assignment.assignment.entity.Order;
import assignment.assignment.entity.OrderDetail;
import assignment.assignment.service.CartService;
import assignment.assignment.service.SessionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/payment")
public class PayController {
    @Autowired 
    CategoryDAO categoryDAO;

    @Autowired
    CartService cartService;

    @Autowired
    SessionService sessionService;

    @Autowired
    OrderDAO orderDAO;

    @Autowired
    OrderDetailDAO orderDetailDAO;

    @GetMapping("/index")
    public String pay(Model model) {
        // Lấy user hiện tại từ session
        Account user = sessionService.get("user", Account.class);
        
        // Kiểm tra nếu chưa đăng nhập, yêu cầu đăng nhập trước khi thanh toán
        if (user == null) {
            return "redirect:/auth/login";
        }

        // Lấy giỏ hàng của user đang đăng nhập
        Collection<CartItem> cartItems = cartService.getAllItems(user.getUsername());
        model.addAttribute("cartItems", cartItems);

        double totalPrice = cartService.getAmount(user.getUsername());
        model.addAttribute("totalPrice", totalPrice);

        // Thêm tên user vào model
        model.addAttribute("user", user);

        model.addAttribute("view", "user-page/payment");
        return "layouts/layout";
    }

    @PostMapping("/checkout")
    public String checkout( @RequestParam("address") String address, RedirectAttributes redirectAttributes) {
        Account user = sessionService.get("user", Account.class);
        if (user == null) {
            return "redirect:/auth/login";
        }

        // Kiểm tra giỏ hàng có sản phẩm không
        Collection<CartItem> cartItems = cartService.getAllItems(user.getUsername());
        if (cartItems.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Giỏ hàng trống!");
            return "redirect:/payment/index";
        }

        // Tạo đơn hàng mới
        Order order = new Order();
        order.setAccount(user);
        order.setAddress(address);
        order.setCreateDate(new Date());

        // Lưu đơn hàng vào database
        orderDAO.save(order);

        // Lưu từng sản phẩm vào OrderDetail
        for (CartItem cartItem : cartItems) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(cartItem.getProduct());
            orderDetail.setQuantity(cartItem.getQty());
            orderDetail.setPrice(cartItem.getTotalPrice());
            
            orderDetailDAO.save(orderDetail);
        }

        // Xóa giỏ hàng sau khi thanh toán thành công
        cartService.clear(user.getUsername());

        // Thông báo thanh toán thành công
        redirectAttributes.addFlashAttribute("success", "Thanh toán thành công!");

        return "redirect:/order/index";
    }
    
}
