package net.codejava;

import java.util.Comparator;
import java.util.List;

public class SortByPrice implements ProductSortingStrategy {
    @Override
    public List<Product> sort(List<Product> products, boolean ascending) {
        Comparator<Product> comparator = Comparator.comparing(Product::getPrice);
        products.sort(ascending ? comparator : comparator.reversed());
        return products;
    }
}