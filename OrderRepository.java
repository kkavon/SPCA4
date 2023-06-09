package net.codejava;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
	 @Query("SELECT o FROM Orders o JOIN FETCH o.user")
	    List<Orders> findAllWithUsers();
	    Orders findByUser(User user);


	 
	 
}
