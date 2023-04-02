package net.codejava;

import java.util.Comparator;
import java.util.List;

public class SortByManufacturer implements ProductSortingStrategy {
    @Override
    public List<Product> sort(List<Product> products) {
        products.sort(Comparator.comparing(Product::getManufacturer));
        return products;
    }
}