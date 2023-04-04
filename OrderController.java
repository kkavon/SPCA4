package net.codejava;

import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    @ModelAttribute("cart")
    public Cart createCart() {
        return new Cart();
    }

    @GetMapping("/checkout")
    public String checkout(@ModelAttribute("cart") Cart cart, Model model, HttpSession session) {
        // Get the user ID from the session
        Long userId = (Long) session.getAttribute("userId");

        // Fetch the user using the user ID need to update this to allow for concurrency
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            // If the user is not found, redirect to the login page
            return "redirect:/login";
        }
        if (!cart.getItems().isEmpty()) {
            session.setAttribute("lastOrderItem", cart.getItems().get(cart.getItems().size() - 1));
        }
        
        // Create a new order
        Orders order = new Orders();
        order.setUser(user);

        // Save the order
        orderRepository.save(order);

        // Save the orer items
        for (OrderItem item : cart.getItems()) {
            item.setOrder(order);
            orderItemRepository.save(item);
        }

        // Clear the cart and retrn
        
        for (OrderItem item : cart.getItems()) {
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() - item.getQuantity());
            productRepository.save(product);
        }
        cart.getItems().clear();

        return "redirect:/rating";
    }
    
    
    @GetMapping("/all-order-history")
    public String showAllOrderHistory(Model model) {
        List<Orders> orders = orderRepository.findAll();
        model.addAttribute("orders", orders);
        return "orders_admin";
    }
    
    
    @GetMapping("/rating")
    public String showRatingPage() {
        return "rating";
    }
    
    @PostMapping("/submitRating")
    public String submitRating(@RequestParam("rating") int rating, @RequestParam("comment") String comment, HttpSession session) {
        // Get the user ID from the session - need to allow for concurrency
        Long userId = (Long) session.getAttribute("userId");

        // get  user using the user ID
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            // If the user is not found, redirect to the login page
            return "redirect:/login";
        }

        // Save the rating and review to the database - Need to allow a review for each item, not the whole order. 
        Review review = new Review();
        review.setUser(user);
        review.setRating(rating);
        review.setComment(comment);

        // Get order item from the session
        OrderItem lastOrderItem = (OrderItem) session.getAttribute("lastOrderItem");

        if (lastOrderItem != null) {
            review.setProduct(lastOrderItem.getProduct());
        } else {
            // Ierorr if null
            return "redirect:/error";
        }

        reviewRepository.save(review);

        return "redirect:/";
    }

}
