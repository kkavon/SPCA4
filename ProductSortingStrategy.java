package net.codejava;

import java.util.List;

public interface ProductSortingStrategy {
    List<Product> sort(List<Product> products);
}