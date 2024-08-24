package farm.inventory;

import farm.core.FailedTransactionException;
import farm.core.InvalidStockRequestException;
import farm.inventory.product.Product;
import farm.inventory.product.data.Quality;
import farm.inventory.product.data.Barcode;


import java.util.List;

/**
 * An interface representing the base requirements for an Inventory.
 * Handles adding and removing items from storage.
 * Implementations of this interface must provide support for managing individual and bulk product operations.
 * Component of Stage 2.
 */
public interface Inventory {

    /**
     * Adds a new product with the specified barcode and quality to the inventory.
     *
     * @param barcode the barcode of the product to add.
     * @param quality the quality of the product to add.
     */
    void addProduct(Barcode barcode, Quality quality);

    /**
     * Adds the specified quantity of the product with the given barcode to the inventory.
     * This method allows for bulk additions if the implementing inventory supports it.
     *
     * @param barcode the barcode of the product to add.
     * @param quality the quality of the product to add.
     * @param quantity the amount of the product to add.
     * @throws InvalidStockRequestException if the implementing inventory does not support adding multiple products at once.
     */
    void addProduct(Barcode barcode, Quality quality, int quantity) throws InvalidStockRequestException;

    /**
     * Determines if a product with the given barcode exists in the inventory.
     *
     * @param barcode the barcode of the product to check.
     * @return true if the product exists, false otherwise.
     */
    boolean existsProduct(Barcode barcode);

    /**
     * Retrieves the full stock currently held in the inventory.
     *
     * @return a list containing all products currently stored in the inventory.
     */
    List<Product> getAllProducts();

    /**
     * Removes the first product with the specified barcode from the inventory.
     *
     * @param barcode the barcode of the product to be removed.
     * @return a list containing the removed product if it exists, else an empty list.
     */
    List<Product> removeProduct(Barcode barcode);

    /**
     * Removes the specified number of products with the given barcode from the inventory.
     * This method allows for bulk removals if the implementing inventory supports it.
     *
     * @param barcode the barcode of the product to remove.
     * @param quantity the total amount of the product to remove from the inventory.
     * @return a list containing the removed products if they exist, else an empty list.
     * @throws FailedTransactionException if the implementing inventory does not support removing multiple products at once.
     */
    List<Product> removeProduct(Barcode barcode, int quantity) throws FailedTransactionException;
}