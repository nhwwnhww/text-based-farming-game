package farm.inventory;

import farm.inventory.product.Product;
import farm.inventory.product.data.Barcode;
import farm.inventory.product.data.Quality;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents an abstract inventory that holds products.
 * This class should be extended by specific types of inventories.
 */
public abstract class Inventory {
    private final List<Product> products;

    /**
     * Constructs a new Inventory with an empty list of products.
     */
    public Inventory() {
        this.products = new ArrayList<>();
    }

    /**
     * Adds a product to the inventory based on the barcode and quality.
     * This method assumes a default quantity of 1.
     *
     * @param barcode The barcode of the product.
     * @param quality The quality of the product.
     */
    public void addProduct(Barcode barcode, Quality quality) {
        addProduct(barcode, quality, 1);
    }

    /**
     * Adds a specified quantity of a product to the inventory based on the barcode and quality.
     *
     * @param barcode  The barcode of the product.
     * @param quality  The quality of the product.
     * @param quantity The number of products to add.
     */
    public void addProduct(Barcode barcode, Quality quality, int quantity) {
        for (int i = 0; i < quantity; i++) {
            products.add(createProduct(barcode, quality));
        }
    }

    /**
     * Checks if a product with the specified barcode exists in the inventory.
     *
     * @param barcode The barcode of the product.
     * @return true if the product exists, false otherwise.
     */
    public boolean existsProduct(Barcode barcode) {
        return products.stream().anyMatch(product -> product.getBarcode().equals(barcode));
    }

    /**
     * Removes all products with the specified barcode from the inventory.
     *
     * @param barcode The barcode of the product to remove.
     * @return A list of removed products.
     */
    public List<Product> removeProduct(Barcode barcode) {
        List<Product> removedProducts = products.stream()
                .filter(product -> product.getBarcode().equals(barcode))
                .collect(Collectors.toList());

        products.removeIf(product -> product.getBarcode().equals(barcode));
        return removedProducts;
    }

    /**
     * Removes a specified quantity of products with the specified barcode from the inventory.
     *
     * @param barcode  The barcode of the product to remove.
     * @param quantity The number of products to remove.
     * @return A list of removed products.
     */
    public List<Product> removeProduct(Barcode barcode, int quantity) {
        List<Product> removedProducts = new ArrayList<>();
        int removedCount = 0;

        for (Product product : new ArrayList<>(products)) {
            if (product.getBarcode().equals(barcode) && removedCount < quantity) {
                products.remove(product);
                removedProducts.add(product);
                removedCount++;
            }
        }

        return removedProducts;
    }

    /**
     * Returns a list of all products in the inventory.
     *
     * @return A list of products.
     */
    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    /**
     * Creates a product based on the barcode and quality.
     * Subclasses must implement this method to return the appropriate product type.
     *
     * @param barcode The barcode of the product.
     * @param quality The quality of the product.
     * @return A new Product instance.
     */
    protected abstract Product createProduct(Barcode barcode, Quality quality);
}
