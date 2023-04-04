package net.codejava;

import java.util.Comparator;
import java.util.List;

public class SortByManufacturer implements ProductSortingStrategy {
    @Override
    public List<Product> sort(List<Product> products, boolean ascending) {
        Comparator<Product> comparator = Comparator.comparing(Product::getManufacturer);
        products.sort(ascending ? comparator : comparator.reversed());
        return products;
    }
}