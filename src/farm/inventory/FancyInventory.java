package farm.inventory;

import farm.inventory.product.*;
import farm.inventory.product.data.Barcode;
import farm.inventory.product.data.Quality;
import farm.core.InvalidStockRequestException;
import farm.core.FailedTransactionException;

import java.util.*;

/**
 * A FancyInventory implementation that supports bulk operations and advanced features.
 * This inventory stores products in stacks, enabling quantity information and supports
 * operations on multiple products, such as adding or removing multiple products at once.
 */
public class FancyInventory implements Inventory {

    private static List<Product> products;

    /**
     * Constructs an empty FancyInventory.
     */
    public FancyInventory() {
        products = new ArrayList<>();
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
            default -> throw new IllegalArgumentException("IllegalArgument barcode: " + barcode);
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
            throw new IllegalArgumentException("Quantity must be at least 1.");
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
     * Removes the highest quality product with the corresponding barcode from the inventory.
     * The product with the highest quality is prioritized for removal.
     * used chatgpt to help
     *
     * @param barcode The barcode of the product to be removed.
     * @return A list containing the removed product if it exists, else an empty list.
     */
    @Override
    public List<Product> removeProduct(Barcode barcode) {
        List<Product> removedProducts = new ArrayList<>();

        Map<Quality, Integer> qualityOrderMap = getQualityOrderMap();

        Optional<Product> productToRemove = products.stream()
                .filter(product -> product.getBarcode().equals(barcode))
                .max(Comparator.comparing((Product product) ->
                        qualityOrderMap.get(product.getQuality())));

        productToRemove.ifPresent(product -> {
            removedProducts.add(product);   // Add the product to the list of removed list
            products.remove(product);       // Remove the product from the inventory
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
            if (!existsProduct(barcode)) {
                break; // No more products to remove
            }
            List<Product> productList = removeProduct(barcode);
            removedProducts.addAll(productList);
        }
        return removedProducts;
    }

    /**
     * Retrieves the full stock currently held in the inventory.
     *
     * @return A list containing all products currently stored in the inventory.
     */
    @Override
    public List<Product> getAllProducts() {
        // Create a new list from the products to avoid modifying the original list
        List<Product> sortedProducts = new ArrayList<>(products);

        Map<Barcode, Integer> barcodeOrderMap = getBarcodeOrderMap();

        // Sort the products based on the order defined in the Barcode enum
        sortedProducts.sort(Comparator.comparing(
                product -> barcodeOrderMap.get(product.getBarcode())));

        return sortedProducts;
    }

    /**
     * Get the quantity of a specific product in the inventory.
     * used chatgpt to help
     *
     * @return The total quantity of the specified product in the inventory.
     */
    public int getStockedQuantity(Barcode barcode) {
        return (int) products.stream()
                .filter(product -> product.getBarcode().equals(barcode))
                .count();
    }

    private Map<Barcode, Integer> getBarcodeOrderMap() {
        // Create a map of Barcode to its ordinal position
        Map<Barcode, Integer> barcodeOrderMap = new HashMap<>();
        Barcode[] barcodes = Barcode.values();
        for (int i = 0; i < barcodes.length; i++) {
            barcodeOrderMap.put(barcodes[i], i);
        }
        return barcodeOrderMap;
    }

    private Map<Quality, Integer> getQualityOrderMap() {
        // Create a map of Barcode to its ordinal position
        Map<Quality, Integer> qualityOrderMap = new HashMap<>();
        Quality[] qualities = Quality.values();
        for (int i = 0; i < qualities.length; i++) {
            qualityOrderMap.put(qualities[i], i);
        }
        return qualityOrderMap;
    }
}
