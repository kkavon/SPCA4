package net.codejava;

public class SortingStrategyFactory {
    public static ProductSortingStrategy getSortingStrategy(String sortType) {
        switch (sortType) {
            case "title":
                return new SortByTitle();
            case "manufacturer":
                return new SortByManufacturer();
            case "price":
                return new SortByPrice();
            default:
                return new SortByTitle();
        }
    }
}
