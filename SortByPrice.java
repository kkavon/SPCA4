package net.codejava;

import java.util.Comparator;
import java.util.List;

public class SortByPrice implements ProductSortingStrategy {
    @Override
    public List<Product> sort(List<Product> products) {
        products.sort(Comparator.comparing(Product::getPrice));
        return products;
    }
}