package assignment.assignment.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import assignment.assignment.DAO.CategoryDAO;
import assignment.assignment.entity.Category;

@Controller
@RequestMapping("/admin/category")
public class CategoryAController {
    @Autowired
    CategoryDAO categoryDAO;

    @GetMapping("/index")
    public String index( Model model) {
        Category category = new Category();
        model.addAttribute("category", category);
        List<Category> categories = categoryDAO.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("view", "admin/product_catelogyCRUD");
        return "admin/layout";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable("id") String id) {
        Category category = categoryDAO.findById(id).get();
        model.addAttribute("category", category);
        List<Category> categories = categoryDAO.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("view", "admin/product_catelogyCRUD");
        return "admin/layout";
    }
    
    @PostMapping("/create")
    public String create(@ModelAttribute Category category, RedirectAttributes redirectAttributes) {
        if (category.getId().isEmpty() || category.getName().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng nhập đầy đủ thông tin!");
            return "redirect:/admin/category/index";
        }
        categoryDAO.save(category);
        redirectAttributes.addFlashAttribute("success", "Thêm danh mục thành công!");
        return "redirect:/admin/category/index";
    }
    
    @PostMapping("/update")
    public String update(@ModelAttribute Category category, RedirectAttributes redirectAttributes) {
        if (!categoryDAO.existsById(category.getId())) {
            redirectAttributes.addFlashAttribute("error", "Danh mục không tồn tại!");
            return "redirect:/admin/category/index";
        }

        if (category.getName().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng nhập tên danh mục!");
            return "redirect:/admin/category/index";
        }

        Category existingCategory = categoryDAO.findById(category.getId()).get();
        category.setId(existingCategory.getId()); // Không cho sửa ID
        categoryDAO.save(category);
        redirectAttributes.addFlashAttribute("success", "Cập nhật danh mục thành công!");
        return "redirect:/admin/category/index";
    }
    
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        if (!categoryDAO.existsById(id)) {
            redirectAttributes.addFlashAttribute("error", "Danh mục không tồn tại!");
            return "redirect:/admin/category/index";
        }
    
        try {
            categoryDAO.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Xóa danh mục thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể xóa danh mục do ràng buộc dữ liệu!");
        }
        return "redirect:/admin/category/index";
    }
    
}
