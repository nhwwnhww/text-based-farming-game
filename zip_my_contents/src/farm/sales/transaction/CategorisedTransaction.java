package farm.sales.transaction;

import farm.customer.Customer;
import farm.inventory.product.Product;
import farm.inventory.product.data.Barcode;
import farm.sales.ReceiptPrinter;

import java.util.*;

/**
 * Represents a categorized transaction where products are grouped by their barcode.
 */
public class CategorisedTransaction extends Transaction {
    private final Map<Barcode, List<Product>> purchasesByType;

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
     * Returns a set of unique barcodes representing the types of products purchased.
     *
     * @return A set of barcodes.
     */
    public Set<Barcode> getPurchasedTypes() {
        populatePurchasesByType();
        return purchasesByType.keySet();
    }

    /**
     * Returns a map of products grouped by their barcode.
     *
     * @return A map of barcodes to lists of products.
     */
    public Map<Barcode, List<Product>> getPurchasesByType() {
        populatePurchasesByType();
        return new HashMap<>(purchasesByType);

    }

    /**
     * Returns the total quantity of products for a given barcode.
     *
     * @param type The barcode of the product.
     * @return The total quantity of products with the given barcode.
     */
    public int getPurchaseQuantity(Barcode type) {
        // Ensure purchasesByType is up-to-date
        populatePurchasesByType();

        List<Product> products = purchasesByType.get(type);
        return products == null ? 0 : products.size();
    }

    /**
     * Returns the subtotal cost for all products of a given barcode.
     *
     * @param barcode The barcode of the product.
     * @return The subtotal cost of products with the given barcode.
     */
    public int getPurchaseSubtotal(Barcode barcode) {
        // Ensure purchasesByType is up-to-date
        populatePurchasesByType();
        List<Product> products = purchasesByType.get(barcode);
        if (products == null) {
            return 0;
        }

        return products
                .stream()
                .mapToInt(Product::getBasePrice)
                .sum();
    }

    /**
     * Populates the purchasesByType map with the products from the current transaction.
     */
    private void populatePurchasesByType() {
        purchasesByType.clear();  // Clear previous data to avoid accumulation

        for (Product product : getPurchases()) {
            Barcode barcode = product.getBarcode();
            purchasesByType.computeIfAbsent(barcode, k -> new ArrayList<>()).add(product);
        }
    }

    /**
     * Converts the transaction into a formatted receipt for display.
     *
     * @return The styled receipt representation of this transaction.
     */
    @Override
    public String getReceipt() {
        // If the transaction is not finalized, return an active receipt
        if (!isFinalised()) {
            return ReceiptPrinter.createActiveReceipt();
        }

        // Define the headings as specified
        List<String> headings = List.of("Item", "Qty", "Price (ea.)", "Subtotal");

        // Create the list of entries
        List<List<String>> entries = new ArrayList<>();
        getPurchasedTypes();
        for (Barcode barcode : Barcode.values()) {
            List<Product> products = getPurchasesByType().get(barcode);
            if (products != null && !products.isEmpty()) {
                int quantity = products.size();
                int pricePerItem = products.getFirst().getBasePrice();
                int subtotal = getPurchaseSubtotal(barcode);

                String itemName = barcode.getDisplayName().toLowerCase();
                String qtyString = String.valueOf(quantity);
                String priceString = String.format("$%.2f", pricePerItem / 100.0);
                String subtotalString = String.format("$%.2f", subtotal / 100.0);

                entries.add(List.of(itemName, qtyString, priceString, subtotalString));
            }
        }

        // Calculate the total price and format it
        String total = String.format("$%.2f", getTotal() / 100.0);

        // Get the customer's name
        String customerName = getAssociatedCustomer().getName();

        // Generate the formatted receipt using the ReceiptPrinter
        return ReceiptPrinter.createReceipt(headings, entries, total, customerName);
    }

}
