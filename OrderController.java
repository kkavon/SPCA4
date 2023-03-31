package net.codejava;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("cart")
public class OrderController {

	
	 @Autowired
	    private OrderRepository orderRepository;

	    @GetMapping("/checkout")
	    public String checkout(@ModelAttribute("cart") Cart cart, Model model) {
	        //need to  implement the checkout process here
	        //save order and order items to the database etc
	        return "redirect:/";
	    }
}
