package net.codejava;

import java.util.List;
import javax.servlet.http.HttpSession;
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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    @ModelAttribute("cart")
    public Cart createCart() {
        return new Cart();
    }

    @GetMapping("/checkout")
    public String checkout(@ModelAttribute("cart") Cart cart, Model model, HttpSession session) {
        // Get the user ID from the session
        Long userId = (Long) session.getAttribute("userId");

        // Fetch the user using the user ID
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            // If the user is not found, redirect to the login page
            return "redirect:/login";
        }

        // Create a new order
        Orders order = new Orders();
        order.setUser(user);

        // Save the order
        orderRepository.save(order);

        // Save the order items
        for (OrderItem item : cart.getItems()) {
            item.setOrder(order);
            orderItemRepository.save(item);
        }

        // Clear the cart and return
        cart.getItems().clear();
        return "redirect:/";
    }
}
