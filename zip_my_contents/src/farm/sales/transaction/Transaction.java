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
    private int total;

    /**
     * Constructs a new Transaction associated with the specified customer.
     *
     * @param customer The customer associated with this transaction.
     */
    public Transaction(Customer customer) {
        this.associatedCustomer = customer;
        this.purchases = new ArrayList<>();
        this.finalised = false;
        this.total = 0;
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
     * Adds a product to the transaction's list of purchases.
     * This method can only be called if the transaction is not yet finalized.
     *
     * @param product The product to be added.
     * @throws IllegalStateException if the transaction has already been finalized.
     */
    public void addProduct(Product product) {
        if (finalised) {
            throw new IllegalStateException("Cannot add products to a finalized transaction.");
        }
        purchases.add(product);
        calculateTotal();
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
     * Calculates the total cost of the products in this transaction.
     */
    private void calculateTotal() {
        total = purchases.stream().mapToInt(Product::getBasePrice).sum();
    }

    /**
     * Returns the total cost of the transaction.
     *
     * @return The total cost.
     */
    public int getTotal() {
        return total;
    }

    /**
     * Returns a string representation of the transaction, including the associated customer and total cost.
     *
     * @return A string describing the transaction.
     */
    @Override
    public String toString() {
        return "Transaction for " + associatedCustomer.getName() + ": " + purchases.size() + " items, Total: " + total;
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
            receipt.append(product.getDisplayName()).append(" - ").append(product.getBasePrice()).append("\n");
        }
        receipt.append("Total: ").append(total);
        return receipt.toString();
    }
}
