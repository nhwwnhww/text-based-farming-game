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
     * Determines if the transaction is finalised (i.e. sale completed) or not.
     *
     * @return true if the transaction is over, else false.
     */
    public boolean isFinalised() {
        return finalised;
    }

    /**
     * Mark a transaction as finalised and update the transaction's internal state accordingly.
     * This locks in all pending purchases previously added,
     * such that they are now treated as final purchases and
     * no additional modification can be made,
     * and empties the customer's cart.
     */
    public void finalise() {
        this.finalised = true;
        this.purchases = associatedCustomer.getCart().getContents();
        this.associatedCustomer.getCart().setEmpty();
    }

    /**
     * Returns the list of products in this transaction.
     *
     * @return A list of products.
     */
    public List<Product> getPurchases() {
        if (!isFinalised()) {
            return new ArrayList<>(associatedCustomer.getCart().getContents());
        } else {
            return new ArrayList<>(purchases);
        }
    }

    /**
     * Returns the total cost of the transaction.
     *
     * @return The total cost.
     */
    public int getTotal() {
        int total = 0;
        for (Product product : getPurchases()) {
            total += product.getBasePrice();
        }
        return total;
    }

    /**
     * Returns a string representation of the transaction, including the associated customer and total cost.
     *
     * @return A string describing the transaction.
     */
    public String toString() {
        return "Transaction {Customer: "
                + associatedCustomer.getName()
                + " | Phone Number: "
                + associatedCustomer.getPhoneNumber()
                + " | Address: "
                + associatedCustomer.getAddress()
                + ", Status: "
                + (isFinalised() ? "Finalised" : "Active")
                + ", Associated Products: "
                + this.getPurchases()
                + "}";

    }

    /**
     * Returns a detailed receipt of the transaction.
     * used chatgpt to help
     *
     * @return A string representing the receipt of the transaction.
     */
    public String getReceipt() {
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
