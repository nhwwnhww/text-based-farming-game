package farm.sales;

import farm.inventory.product.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a shopping cart that holds products for purchase.
 */
public class Cart {
    private final List<Product> contents;

    /**
     * Constructs a new Cart with an empty list of products.
     */
    public Cart() {
        this.contents = new ArrayList<>();
    }

    /**
     * Adds a product to the cart.
     *
     * @param product The product to be added.
     */
    public void addProduct(Product product) {
        contents.add(product);
    }

    /**
     * Returns the list of products currently in the cart.
     *
     * @return A list of products in the cart.
     */
    public List<Product> getContents() {
        return new ArrayList<>(contents);
    }

    /**
     * Empties the cart, removing all products.
     */
    public void setEmpty() {
        contents.clear();
    }

    /**
     * Checks if the cart is empty.
     *
     * @return true if the cart is empty, false otherwise.
     */
    public boolean isEmpty() {
        return contents.isEmpty();
    }
}
