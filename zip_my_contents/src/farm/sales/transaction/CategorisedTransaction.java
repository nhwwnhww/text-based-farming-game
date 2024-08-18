package farm.sales.transaction;

import farm.customer.Customer;
import farm.inventory.product.Product;
import farm.inventory.product.data.Barcode;

import java.util.*;

/**
 * Represents a categorized transaction where products are grouped by their barcode.
 */
public class CategorisedTransaction extends Transaction {
    private Map<Barcode, List<Product>> purchasesByType;

    /**
     * Constructs a new CategorisedTransaction associated with the specified customer.
     *
     * @param customer The customer associated with this transaction.
     */
    public CategorisedTransaction(Customer customer) {
        super(customer);
        this.purchasesByType = new HashMap<>();
    }

    /**
     * Adds a product to the transaction's list of purchases, categorizing it by its barcode.
     *
     * @param product The product to be added.
     */
    @Override
    public void addProduct(Product product) {
        super.addProduct(product);
        Barcode barcode = product.getBarcode();
        purchasesByType.putIfAbsent(barcode, new ArrayList<>());
        purchasesByType.get(barcode).add(product);
    }

    /**
     * Returns a set of unique barcodes representing the types of products purchased.
     *
     * @return A set of barcodes.
     */
    public Set<Barcode> getPurchasedTypes() {
        return purchasesByType.keySet();
    }

    /**
     * Returns a map of products grouped by their barcode.
     *
     * @return A map of barcodes to lists of products.
     */
    public Map<Barcode, List<Product>> getPurchasesByType() {
        return new HashMap<>(purchasesByType);
    }

    /**
     * Returns the total quantity of products for a given barcode.
     *
     * @param barcode The barcode of the product.
     * @return The total quantity of products with the given barcode.
     */
    public int getPurchaseQuantity(Barcode barcode) {
        List<Product> products = purchasesByType.get(barcode);
        return products == null ? 0 : products.size();
    }

    /**
     * Returns the subtotal cost for all products of a given barcode.
     *
     * @param barcode The barcode of the product.
     * @return The subtotal cost of products with the given barcode.
     */
    public int getPurchaseSubtotal(Barcode barcode) {
        List<Product> products = purchasesByType.get(barcode);
        return products == null ? 0 : products.stream().mapToInt(Product::getBasePrice).sum();
    }
}
