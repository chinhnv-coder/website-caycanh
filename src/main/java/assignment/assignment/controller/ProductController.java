package assignment.assignment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import assignment.assignment.DAO.CategoryDAO;
import assignment.assignment.DAO.ProductDAO;
import assignment.assignment.entity.Product;

@Controller
public class ProductController {
    @Autowired
    CategoryDAO categoryDAO;

    @Autowired
    ProductDAO productDAO;

    @GetMapping("/product")
    public String hello(Model model, 
                        @RequestParam(name = "category", required = false) String categoryId,
                        @RequestParam(name = "keyword", required = false) String keyword,
                        @RequestParam(name = "page", defaultValue = "0") int page){
        int pageSize = 12; // Số sản phẩm trên mỗi trang
        Pageable pageable = PageRequest.of(page, pageSize);
        
        
        Page<Product> products;
        if (keyword != null && !keyword.isEmpty()) {
            products = productDAO.searchByName(keyword, pageable);
        }else if (categoryId != null && !categoryId.isEmpty()) {
            products = productDAO.findByCategoryId(categoryId, pageable);
        }else{
            products = productDAO.findAllProducts(pageable);
        }
        model.addAttribute("products", products.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("keyword",keyword);
        model.addAttribute("view", "user-page/product");
        return "layouts/layout";
    }
}
