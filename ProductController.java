package net.codejava;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("cart")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;
    
    @GetMapping("/stock")
    public String listProduct(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        System.out.println("Products" + products);
        return "products_display";
    }

    @GetMapping("/products/create")
    public String createProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "products_create";
    }

    @PostMapping("/products")
    public String createproduct(@ModelAttribute Product product) {
        productRepository.save(product);
        return "redirect:/products";
    }


    @GetMapping("/products/edit/{id}")
    public String editProductForm(@PathVariable("id") Long id, Model model) {
        Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        return "products_edit";
    }
    @PostMapping("/products/update/{id}")
    public String updateProduct(@PathVariable("id") Long id, @ModelAttribute Product product) {
        productRepository.save(product);
        return "redirect:/stock";
    }
    
    
    
    @GetMapping("/products/search")
    public String searchProducts(@RequestParam("searchTerm") String searchTerm, Model model) {
        List<Product> searchResults = productRepository.search(searchTerm);
        model.addAttribute("products", searchResults);
        return "products_display";
    }
    

  
}