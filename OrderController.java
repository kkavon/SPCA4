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

  

    @GetMapping("/checkout")
    public String checkout(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        Cart cart = getCartFromSession(session);

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }
        if (!cart.getItems().isEmpty()) {
            session.setAttribute("lastOrderItem", cart.getItems().get(cart.getItems().size() - 1));
        }
        
        // Create a new order
        Orders order = new Orders();
        order.setUser(user);

        // Save the rder
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

        return "redirect:/rating?orderId=" + order.getId();

    }
    
    private Cart getCartFromSession(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }
    
    @GetMapping("/all-order-history")
    public String showAllOrderHistory(Model model) {
        List<Orders> orders = orderRepository.findAll();
        model.addAttribute("orders", orders);
        return "orders_admin";
    }
    
    
    @GetMapping("/rating")
    public String showRatingPage(@RequestParam("orderId") Long orderId, Model model) {
        Orders order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            List<OrderItem> orderItems = orderItemRepository.findByOrder(order);
            model.addAttribute("orderItems", orderItems);
        }
        return "rating";
    }
    @PostMapping("/submitRating")
    public String submitRating(@RequestParam("rating") int rating, @RequestParam("comment") String comment, @RequestParam("productId") Long productId, HttpSession session) {
        // Get the user ID from the session - need to allow for concurrency
        Long userId = (Long) session.getAttribute("userId");

        // get user using the user ID
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

        // Get the product using the productId
        Product product = productRepository.findById(productId).orElse(null);

        if (product != null) {
            review.setProduct(product);
        } else {
            // Error if null
            return "redirect:/error";
        }

        reviewRepository.save(review);

        return "redirect:/";
    }

}
