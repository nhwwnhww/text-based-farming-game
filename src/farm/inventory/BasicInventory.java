package farm.inventory;

import farm.inventory.product.*;
import farm.inventory.product.data.Barcode;
import farm.inventory.product.data.Quality;
import farm.core.InvalidStockRequestException;
import farm.core.FailedTransactionException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A very basic inventory that stores and handles products individually.
 * Implements the Inventory interface.
 */
public class BasicInventory implements Inventory {

    private final List<Product> products;

    /**
     * Constructs a new BasicInventory instance with an empty product list.
     */
    public BasicInventory() {
        this.products = new ArrayList<>();
    }

    /**
     * Adds a new product with the given barcode and quality to the inventory.
     *
     * @param barcode the barcode of the product to add.
     * @param quality the quality of the product to add.
     */
    @Override
    public void addProduct(Barcode barcode, Quality quality) {
        Product product = switch (barcode) {
            case EGG -> new Egg(quality);
            case MILK -> new Milk(quality);
            case JAM -> new Jam(quality);
            case WOOL -> new Wool(quality);
            default -> throw new IllegalArgumentException("Unsupported product type: " + barcode);
        };
        this.products.add(product);
    }

    /**
     * Adds a certain number of products with the given barcode and quality to the inventory.
     *
     * @param barcode the barcode of the product to add.
     * @param quality the quality of the product to add.
     * @param quantity the number of products to add.
     * @throws InvalidStockRequestException if the quantity is greater than 1.
     */
    @Override
    public void addProduct(Barcode barcode,
                           Quality quality,
                           int quantity) throws InvalidStockRequestException {

        if (quantity < 1) {
            throw new InvalidStockRequestException("Quantity must be at least 1.");
        }

        if (quantity > 1) {
            throw new InvalidStockRequestException(
                    "Current inventory is not fancy enough. Please supply products one at a time.");
        }
        addProduct(barcode, quality);
    }

    /**
     * Determines if a product with the given barcode exists in the inventory.
     *
     * @param barcode the barcode of the product to check.
     * @return true if the product exists, false otherwise.
     */
    @Override
    public boolean existsProduct(Barcode barcode) {
        return products.stream().anyMatch(product -> product.getBarcode().equals(barcode));
    }

    /**
     * Retrieves the full stock currently held in the inventory.
     *
     * @return a list containing all products currently stored in the inventory.
     */
    @Override
    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    /**
     * Removes the first product with the given barcode from the inventory.
     *
     * @param barcode the barcode of the product to be removed.
     * @return a list containing the removed product if it exists, else an empty list.
     */
    @Override
    public List<Product> removeProduct(Barcode barcode) {
        List<Product> removedProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.getBarcode().equals(barcode)) {
                removedProducts.add(product);
                products.remove(product);
                break; // Remove only the first matching product
            }
        }
        return Collections.unmodifiableList(removedProducts); // Return an immutable list
    }

    /**
     * Removes a certain number of products with the given barcode from the inventory.
     *
     * @param barcode the barcode of the product to remove.
     * @param quantity the total amount of the product to remove from the inventory.
     * @return a list containing the removed products if they exist, else an empty list.
     * @throws FailedTransactionException if the quantity is greater than 1.
     */
    @Override
    public List<Product> removeProduct(
            Barcode barcode, int quantity) throws FailedTransactionException {
        if (quantity > 1) {
            throw new FailedTransactionException(
                    "Current inventory is not fancy enough."
                            +
                            " Please purchase products one at a time.");
        }
        return removeProduct(barcode);
    }
}