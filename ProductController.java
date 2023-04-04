package net.codejava;

import java.util.List;
import java.util.stream.Collectors;

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
    
    
    @PostMapping("/products/addToCart/{id}")
    public String addToCart(@PathVariable("id") Long id, @ModelAttribute("cart") Cart cart, @RequestParam("quantity") int quantity, Model model) {
        Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        cart.addItem(product, quantity);
        return "redirect:/stock/customer";
    }
    
    @GetMapping("/cart")
    public String viewCart(@ModelAttribute("cart") Cart cart, Model model) {
        model.addAttribute("items", cart.getItems());
        return "cart_view";
    }
    
    @ModelAttribute("cart")
    public Cart createCart() {
        return new Cart();
    }


    @GetMapping("/stock/customer")
    public String listProductCustomer(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        System.out.println("Products" + products);
        return "products_display_customer";
    }

//
//    @GetMapping("/stock")
//    public String listProduct(Model model) {
//        List<Product> products = productRepository.findAll();
//        model.addAttribute("products", products);
//        System.out.println("Products" + products);
//        return "products_display";
//    }

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
    
    
    
    
    private List<Product> filterProducts(List<Product> products, String filter) {
        if (filter == null || filter.isEmpty()) {
            return products;
        }

        return products.stream()
            .filter(product -> product.getTitle().toLowerCase().contains(filter.toLowerCase()) ||
                    product.getManufacturer().toLowerCase().contains(filter.toLowerCase()) ||
                    product.getCategory().toLowerCase().contains(filter.toLowerCase()))
            .collect(Collectors.toList());
    }
    
    @GetMapping("/stock")
    public String listProduct(Model model, @RequestParam(required = false) String sortBy, @RequestParam(required = false) String filter) {
        List<Product> products = productRepository.findAll();
        
        if (filter != null && !filter.isEmpty()) {
            products = filterProducts(products, filter);
        }

        ProductSortingStrategy sortingStrategy;

        if (sortBy != null) {
            switch (sortBy) {
                case "title":
                    sortingStrategy = new SortByTitle();
                    break;
                case "manufacturer":
                    sortingStrategy = new SortByManufacturer();
                    break;
                case "price":
                    sortingStrategy = new SortByPrice();
                    break;
                default:
                    sortingStrategy = new SortByTitle();
            }
            products = sortingStrategy.sort(products);
        }

        model.addAttribute("products", products);
        return "products_display";
    }

}