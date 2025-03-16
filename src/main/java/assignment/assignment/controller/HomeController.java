package assignment.assignment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import assignment.assignment.DAO.CategoryDAO;
import assignment.assignment.DAO.ProductDAO;
import assignment.assignment.entity.Category;
import assignment.assignment.entity.Product;


@Controller
public class HomeController {
    @Autowired
    CategoryDAO categoryDAO;

    @Autowired
    private ProductDAO productDAO;
    
    @RequestMapping("/home")
    public String home(Model model) {
        List<Category> categories = categoryDAO.findAll();
        model.addAttribute("categories", categories);

        List<Product> products = productDAO.findTop8NewestProductsSQL();
        model.addAttribute("products", products);

        List<Product> xuongRong = productDAO.findAllXuongRongJQL();
        model.addAttribute("xuongRong", xuongRong);

        List<Product> thuySinh = productDAO.findAllThuySinhJQL();
        model.addAttribute("thuySinh", thuySinh);
        
        model.addAttribute("view", "user-page/home");
        return "layouts/layout";
    }
}
