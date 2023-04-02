package net.codejava;

import java.util.Comparator;
import java.util.List;

public class SortByTitle implements ProductSortingStrategy {
    @Override
    public List<Product> sort(List<Product> products) {
        products.sort(Comparator.comparing(Product::getTitle));
        return products;
    }
}