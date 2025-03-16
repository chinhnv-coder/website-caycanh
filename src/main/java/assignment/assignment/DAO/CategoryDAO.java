package assignment.assignment.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import assignment.assignment.entity.Category;

public interface CategoryDAO extends JpaRepository<Category, String> {
    
}
