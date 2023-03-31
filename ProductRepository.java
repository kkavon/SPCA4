package net.codejava;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	 @Query("SELECT p FROM Product p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
	    List<Product> search(@Param("searchTerm") String searchTerm);
}
