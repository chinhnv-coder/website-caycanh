package assignment.assignment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import assignment.assignment.DAO.CategoryDAO;
import assignment.assignment.DAO.ProductDAO;
import assignment.assignment.entity.Category;
import assignment.assignment.entity.Product;

@Controller
public class DetailController {
    @Autowired
    CategoryDAO categoryDAO;
    
    @Autowired
    ProductDAO productDAO;

    @GetMapping("/detail")
    public String detail(Model model, @RequestParam("id") Integer id){
        List<Category> categories = categoryDAO.findAll();
        model.addAttribute("categories", categories);
        
        Product product = productDAO.findById(id).get();
        if (product == null) {
            return "error-page";
        }
        model.addAttribute("product", product);
        model.addAttribute("view", "user-page/detail");
        return "layouts/layout";
    }
}
