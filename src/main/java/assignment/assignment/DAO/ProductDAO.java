package assignment.assignment.DAO;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import assignment.assignment.entity.Product;

public interface ProductDAO extends JpaRepository<Product, Integer> {
    @Query(value = """
    SELECT TOP 8 * FROM Products 
    ORDER BY createdate DESC, id DESC
    """, nativeQuery = true)
    List<Product> findTop8NewestProductsSQL();

    // JQL: Lấy tất cả sản phẩm thuộc danh mục "Cây Thủy Sinh"
    @Query("SELECT p FROM Product p WHERE p.category.id = 'C007' ORDER BY p.createDate DESC, p.id DESC")
    List<Product> findAllThuySinhJQL();

    // JQL: Lấy tất cả sản phẩm thuộc danh mục "Xương Rồng Cảnh"
    @Query("SELECT p FROM Product p WHERE p.category.id = 'C008' ORDER BY p.createDate DESC, p.id DESC")
    List<Product> findAllXuongRongJQL();

    // List<Product> findByCategoryId(String categoryId);

    // Lấy sản phẩm theo danh mục có hỗ trợ phân trang
    @Query("SELECT p FROM Product p WHERE p.category.id = ?1 ORDER BY p.createDate DESC, p.id DESC")
    Page<Product> findByCategoryId(String categoryId, Pageable pageable);
 
    // Lấy tất cả sản phẩm có hỗ trợ phân trang
    @Query("SELECT p FROM Product p ORDER BY p.createDate DESC, p.id DESC")
    Page<Product> findAllProducts(Pageable pageable);

    // Tìm kiếm theo tên
   @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', ?1, '%')) ORDER BY p.createDate DESC, p.id")
   Page<Product> searchByName(String keyword, Pageable pageable);

   Page<Product> findAll(Pageable pageable);
}
