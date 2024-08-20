package farm.sales.transaction;

import farm.customer.Customer;
import farm.inventory.product.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a transaction in the system.
 * Each transaction is associated with a customer and contains a list of purchased products.
 */
public class Transaction {
    private final Customer associatedCustomer;
    private final List<Product> purchases;
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
    }

    /**
     * Returns the list of products in this transaction.
     *
     * @return A list of products.
     */
    public List<Product> getPurchases() {
        return new ArrayList<>(purchases);
    }

    /**
     * Returns the total cost of the transaction.
     *
     * @return The total cost.
     */
    public int getTotal() {
        return purchases.size();
    }

    /**
     * Returns a string representation of the transaction, including the associated customer and total cost.
     *
     * @return A string describing the transaction.
     */
    @Override
    public String toString() {
        return "Transaction for "
                + associatedCustomer.getName()
                + ": " + purchases.size()
                + " items, Total: "
                + getTotal();
    }

    /**
     * Returns a detailed receipt of the transaction.
     *
     * @return A string representing the receipt of the transaction.
     */
    public String getReceipt() {
        StringBuilder receipt = new StringBuilder();
        receipt.append("Receipt for ").append(associatedCustomer.getName()).append(":\n");
        for (Product product : purchases) {
            receipt.append(product.toString()).append("\n");
        }
        receipt.append("Total: ").append(getTotal());
        return receipt.toString();
    }
}
