package net.codejava;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<OrderItem> items;

    public Cart() {
        items = new ArrayList<>();
    }

    public void addItem(Product product, int quantity) {
        for (OrderItem item : items) {
            if (item.getProduct().getId().equals(product.getId())) {
                int newQuantity = item.getQuantity() + quantity;
                if (newQuantity <= product.getQuantity()) {
                    item.setQuantity(newQuantity);
                } else {
                    item.setQuantity(product.getQuantity());
                }
                return;
            }
        }
        items.add(new OrderItem(product, quantity));
    }


    public List<OrderItem> getItems() {
        return items;
    }
}
