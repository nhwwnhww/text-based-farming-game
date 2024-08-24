package farm.sales.transaction;

import farm.customer.Customer;
import farm.inventory.product.Product;
import farm.inventory.product.data.Barcode;
import farm.inventory.product.data.Quality;
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
        List<Product> products = getPurchasesByType().get(type);
        return products == null ? 0 : products.size();
    }

    /**
     * Returns the subtotal cost for all products of a given barcode.
     *
     * @param barcode The barcode of the product.
     * @return The subtotal cost of products with the given barcode.
     */
    public int getPurchaseSubtotal(Barcode barcode) {
        int quality = getPurchaseQuantity(barcode);
        return quality * barcode.getBasePrice();
    }

    /**
     * Populates the purchasesByType map with the products from the current transaction.
     */
    private void populatePurchasesByType() {
        this.purchasesByType.clear();  // Clear previous data to avoid accumulation
        for (Product product : getPurchases()) {
            Barcode barcode = product.getBarcode();  // Get the barcode of the product

            // If the map already contains the barcode, add the product to the existing list
            if (purchasesByType.containsKey(barcode)) {
                purchasesByType.get(barcode).add(product);
            } else {
                // If the barcode is not present, create a new list and add the product to it
                List<Product> productList = new ArrayList<>();
                productList.add(product);
                purchasesByType.put(barcode, productList);
            }
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

        // Define the headings
        List<String> headings = List.of("Item", "Qty", "Price (ea.)", "Subtotal");

        // Create the list of entries
        List<List<String>> entries = new ArrayList<>();

        List<Barcode> sortedBarcode = new ArrayList<>(getPurchasedTypes());
        List<Barcode> predefinedOrder = Arrays.asList(Barcode.values());

        sortedBarcode.sort(Comparator.comparingInt(predefinedOrder::indexOf));

        for (Barcode barcode : sortedBarcode) {
            int quantity = getPurchaseQuantity(barcode);
            int pricePerItem = barcode.getBasePrice();
            int subtotal = getPurchaseSubtotal(barcode);

            String itemName = barcode.getDisplayName().toLowerCase();
            String qtyString = String.valueOf(quantity);
            String priceString = String.format("$%.2f", pricePerItem / 100.0);
            String subtotalString = String.format("$%.2f", subtotal / 100.0);

            entries.add(List.of(itemName, qtyString, priceString, subtotalString));
        }

        // Calculate the total price and format it
        String total = String.format("$%.2f", getTotal() / 100.0);

        // Get the customer's name
        String customerName = getAssociatedCustomer().getName();

        // Generate the formatted receipt using the ReceiptPrinter
        return ReceiptPrinter.createReceipt(headings, entries, total, customerName);
    }

}
