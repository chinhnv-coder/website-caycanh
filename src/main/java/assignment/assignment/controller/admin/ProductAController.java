package assignment.assignment.controller.admin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import assignment.assignment.DAO.CategoryDAO;
import assignment.assignment.DAO.ProductDAO;
import assignment.assignment.entity.Category;
import assignment.assignment.entity.Product;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.data.domain.Sort;

@Controller
@RequestMapping("/admin/product")
public class ProductAController {
    @Autowired
    ProductDAO productDAO;

    @Autowired
    CategoryDAO categoryDAO;

    @GetMapping("/index")
    public String index( Model model, 
                        @RequestParam(defaultValue = "0") int page, 
                        @RequestParam(defaultValue = "10") int size) {
                            
        Product product = new Product();
        model.addAttribute("product", product);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Product> productPage = productDAO.findAll(pageable);
        
        List<Category> categories = categoryDAO.findAll();
        model.addAttribute("categories", categories);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("view", "admin/productCRUD");
        return "admin/layout";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable("id") Integer id) {
        Product product = productDAO.findById(id).get();
        model.addAttribute("product", product);

        List<Product> products = productDAO.findAll();
        model.addAttribute("products", products);
        List<Category> categories = categoryDAO.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("view", "admin/productCRUD");
        return "admin/layout";
    }
    
    @PostMapping("/create")
    public String create(@ModelAttribute Product product, 
                        @RequestParam("photoFile") MultipartFile photoFile, 
                        Model model,
                        RedirectAttributes redirectAttributes) {
        
        // Kiểm tra ràng buộc dữ liệu
        if (product.getName().isEmpty() || product.getPrice() == null || product.getQuantity() == null|| product.getCategory() == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng nhập đầy đủ thông tin!");
            return "redirect:/admin/product/index";
        }

        if (product.getAvailable() == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng chọn trạng thái sản phẩm!");
            return "redirect:/admin/product/index";
        }

        if (!photoFile.isEmpty()) {
            String uploadDir = "E:\\javaspringboot\\assignment\\photos\\";
            String fileName = System.currentTimeMillis() + "-" + photoFile.getOriginalFilename();
            File savedFile = new File(uploadDir + fileName);
            try {
                photoFile.transferTo(savedFile);
                product.setImage(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("error", "Lỗi tải ảnh!");
                return "redirect:/admin/product/index";
            }
        }else {
            redirectAttributes.addFlashAttribute("error", "Vui lòng chọn hình ảnh!");
            return "redirect:/admin/product/index";
        }

        productDAO.save(product);
        redirectAttributes.addFlashAttribute("success", "Thêm sản phẩm thành công!");
        // Trả về danh sách sản phẩm sau khi thêm
        List<Product> products = productDAO.findAll();
        model.addAttribute("products", products);
        return "redirect:/admin/product/index";
    }
    
    @PostMapping("/update")
    public String update(@ModelAttribute Product product, 
                        @RequestParam("photoFile") MultipartFile photoFile, 
                        Model model,
                        RedirectAttributes redirectAttributes) {
        Product existingProduct = productDAO.findById(product.getId()).orElse(null);
        if (existingProduct == null) {
            redirectAttributes.addFlashAttribute("error", "Sản phẩm không tồn tại!");
            return "redirect:/admin/product/index";
        }

        // Kiểm tra ràng buộc dữ liệu
        if (product.getName().isEmpty() || product.getPrice() == null || product.getQuantity() == null || product.getCategory() == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng nhập đầy đủ thông tin!");
            return "redirect:/admin/product/index";
        }

        if (product.getAvailable() == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng chọn trạng thái sản phẩm!");
            return "redirect:/admin/product/index";
        }

        // Không cho phép cập nhật ID
        product.setId(existingProduct.getId());

        if (!photoFile.isEmpty()) { // Nếu có ảnh mới được tải lên
            String uploadDir = "E:\\javaspringboot\\assignment\\photos\\";
            String fileName = System.currentTimeMillis() + "-" + photoFile.getOriginalFilename();
            File savedFile = new File(uploadDir + fileName);
            try {
                photoFile.transferTo(savedFile);
                product.setImage(fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            product.setImage(existingProduct.getImage()); // Giữ ảnh cũ nếu không upload mới
        }

        productDAO.save(product);
        redirectAttributes.addFlashAttribute("success", "Cập nhật sản phẩm thành công!");
        // Trả về danh sách sản phẩm sau khi cập nhật
        List<Product> products = productDAO.findAll();
        model.addAttribute("products", products);
        return "redirect:/admin/product/index";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        if (productDAO.existsById(id)) {
            productDAO.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Xóa sản phẩm thành công!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Sản phẩm không tồn tại!");
        }
        return "redirect:/admin/product/index";
    }
}
