package farm.inventory;

import farm.inventory.product.*;
import farm.inventory.product.data.Barcode;
import farm.inventory.product.data.Quality;
import farm.core.InvalidStockRequestException;
import farm.core.FailedTransactionException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A FancyInventory implementation that supports bulk operations and advanced features.
 * This inventory stores products in stacks, enabling quantity information and supports
 * operations on multiple products, such as adding or removing multiple products at once.
 */
public class FancyInventory implements Inventory {

    private final List<Product> products;

    /**
     * Constructs an empty FancyInventory.
     */
    public FancyInventory() {
        this.products = new ArrayList<>();
    }

    /**
     * Adds a new product with corresponding barcode to the inventory.
     *
     * @param barcode The barcode of the product to add.
     * @param quality The quality of the product to add.
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
        products.add(product);
    }

    /**
     * Adds multiple of the product with corresponding barcode to the inventory.
     *
     * @param barcode The barcode of the product to add.
     * @param quality The quality of the product to add.
     * @param quantity The amount of the product to add.
     * @throws InvalidStockRequestException If an invalid quantity is specified.
     */
    @Override
    public void addProduct(
            Barcode barcode, Quality quality, int quantity) throws InvalidStockRequestException {
        if (quantity < 1) {
            throw new InvalidStockRequestException("Quantity must be at least 1.");
        }
        for (int i = 0; i < quantity; i++) {
            addProduct(barcode, quality);
        }
    }

    /**
     * Determines if a product exists in the inventory with the given barcode.
     *
     * @param barcode The barcode of the product to check.
     * @return true if a product exists with the specified barcode, false otherwise.
     */
    @Override
    public boolean existsProduct(Barcode barcode) {
        return products.stream().anyMatch(product -> product.getBarcode().equals(barcode));
    }

    /**
     * Retrieves the full stock currently held in the inventory.
     *
     * @return A list containing all products currently stored in the inventory.
     */
    @Override
    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    /**
     * Removes the highest quality product with the corresponding barcode from the inventory.
     * The product with the highest quality is prioritized for removal.
     *
     * @param barcode The barcode of the product to be removed.
     * @return A list containing the removed product if it exists, else an empty list.
     */
    @Override
    public List<Product> removeProduct(Barcode barcode) {
        List<Product> removedProducts = new ArrayList<>();
        products.stream()
                .filter(product -> product.getBarcode().equals(barcode))
                .max(Comparator.comparing(Product::getQuality))
                .ifPresent(product -> {
                    removedProducts.add(product);
                    products.remove(product);
                });
        return removedProducts;
    }

    /**
     * Removes a given number of products with the corresponding barcode from the inventory,
     * choosing the highest quality products possible.
     * Removes the products with the highest quality first. If there are not enough of a given product
     * in the inventory, it will return as many of said product as possible.
     *
     * @param barcode The barcode of the product to remove.
     * @param quantity The total amount of the product to remove from the inventory.
     * @return A list containing the removed products if they exist, else an empty list.
     * @throws FailedTransactionException If an invalid quantity is specified.
     */
    @Override
    public List<Product> removeProduct(
            Barcode barcode, int quantity) throws FailedTransactionException {
        if (quantity < 1) {
            throw new FailedTransactionException("Quantity must be at least 1.");
        }
        List<Product> removedProducts = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            List<Product> productList = removeProduct(barcode);
            if (productList.isEmpty()) {
                break;
            }
            removedProducts.addAll(productList);
        }
        return removedProducts;
    }

    /**
     * Get the quantity of a specific product in the inventory.
     *
     * @param barcode The barcode of the product.
     * @return The total quantity of the specified product in the inventory.
     */
    public int getStockedQuantity(Barcode barcode) {
        return (int) products.stream()
                .filter(product -> product.getBarcode().equals(barcode))
                .count();
    }
}
