package farm.sales.transaction;

import farm.customer.Customer;
import farm.inventory.product.Product;
import farm.sales.ReceiptPrinter;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a transaction in the system.
 * Each transaction is associated with a customer and contains a list of purchased products.
 */
public class Transaction {
    private final Customer associatedCustomer;
    private List<Product> purchases;
    private boolean finalised;

    /**
     * Constructs a new Transaction associated with the specified customer.
     *
     * @param customer The customer associated with this transaction.
     */
    public Transaction(Customer customer) {
        this.associatedCustomer = customer;
        this.purchases = customer.getCart().getContents();
        this.finalised = false;
    }

    /**
     * Returns the customer associated with this transaction.
     *
     * @return The associated customer.
     */
    public Customer getAssociatedCustomer() {
        return associatedCustomer;
    }

    /**
     * Returns whether this transaction has been finalized.
     *
     * @return true if the transaction is finalized, false otherwise.
     */
    public boolean isFinalised() {
        return finalised;
    }

    /**
     * Finalizes the transaction, preventing further modifications.
     */
    public void finalise() {
        this.finalised = true;
        purchases = associatedCustomer.getCart().getContents();
        associatedCustomer.getCart().setEmpty();
    }

    /**
     * Returns the list of products in this transaction.
     *
     * @return A list of products.
     */
    public List<Product> getPurchases() {
        if (!isFinalised()) {
            return associatedCustomer.getCart().getContents();
        }
        return new ArrayList<>(purchases);
    }

    /**
     * Returns the total cost of the transaction.
     *
     * @return The total cost.
     */
    public int getTotal() {
        if (!isFinalised()) {
            return associatedCustomer.getCart().getContents().stream().mapToInt(Product::getBasePrice).sum();
        }
        return purchases.stream().mapToInt(Product::getBasePrice).sum();

    }

    /**
     * Returns a string representation of the transaction, including the associated customer and total cost.
     *
     * @return A string describing the transaction.
     */
    @Override
    public String toString() {
        if (!isFinalised()) {
            return "Transaction {Customer: "
                    + associatedCustomer.getName()
                    + " | Phone Number: "
                    + associatedCustomer.getPhoneNumber()
                    + " | Address: "
                    + associatedCustomer.getAddress()
                    + ", Status: "
                    + (isFinalised() ? "Finalised" : "Active")
                    + ", Associated Products: "
                    + associatedCustomer.getCart().getContents()
                    + "}";
        }
        return "Transaction {Customer: "
                + associatedCustomer.getName()
                + " | Phone Number: "
                + associatedCustomer.getPhoneNumber()
                + " | Address: "
                + associatedCustomer.getAddress()
                + ", Status: "
                + (isFinalised() ? "Finalised" : "Active")
                + ", Associated Products: "
                + purchases
                + "}";

    }

    /**
     * Returns a detailed receipt of the transaction.
     *
     * @return A string representing the receipt of the transaction.
     */
    public String getReceipt() {
        StringBuilder receipt = new StringBuilder();

        if (!isFinalised()) {
            return ReceiptPrinter.createActiveReceipt();
        }

        //headings
        List<String> headings = List.of("Item", "Price");

        // Create the list of entries
        List<List<String>> entries = new ArrayList<>();
        for (Product product : getPurchases()) {
            String itemName = product.getDisplayName().toLowerCase();
            String itemPrice = String.format("$%.2f", product.getBasePrice() / 100.0);
            entries.add(List.of(itemName, itemPrice));
        }

        // Calculate the total price
        String total = String.format("$%.2f", getTotal() / 100.0);

        // Get the customer's name
        String customerName = getAssociatedCustomer().getName();

        // Generate the formatted receipt using the ReceiptPrinter
        return ReceiptPrinter.createReceipt(headings, entries, total, customerName);
    }
}
