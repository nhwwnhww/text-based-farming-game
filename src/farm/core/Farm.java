package farm.core;

import farm.customer.AddressBook;
import farm.customer.Customer;
import farm.inventory.FancyInventory;
import farm.inventory.Inventory;
import farm.inventory.product.*;
import farm.inventory.product.data.Quality;
import farm.sales.TransactionHistory;
import farm.sales.TransactionManager;
import farm.sales.transaction.Transaction;
import farm.inventory.product.data.Barcode;

import java.util.List;


/**
 * The Farm class represents a farm management system that handles inventory,
 * customer records, and transactions.
 */
public class Farm {
    private final Inventory inventory;
    private final AddressBook addressBook;
    private final TransactionManager transactionManager;
    private final TransactionHistory transactionHistory;

    /**
     * Creates a new Farm instance with the specified inventory and address book.
     *
     * @param inventory The inventory through which access to the farm's stock is provisioned.
     * @param addressBook The address book storing the farm's customer records.
     */
    public Farm(Inventory inventory, AddressBook addressBook) {
        this.inventory = inventory;
        this.addressBook = addressBook;
        this.transactionManager = new TransactionManager();
        this.transactionHistory = new TransactionHistory();
    }

    /**
     * Retrieves all customer records currently stored in the farm's address book.
     *
     * @return A list of all customers in the address book.
     * @ensures The returned list is a shallow copy and cannot modify the original address book.
     */
    public List<Customer> getAllCustomers() {
        // Return a shallow copy of the customers list
        return addressBook.getAllRecords();
    }

    /**
     * Retrieves all products currently stored in the farm's inventory.
     *
     * @return A list of all products in the inventory.
     * @ensures The returned list is a shallow copy and cannot modify the original inventory.
     */
    public List<Product> getAllStock() {
        return inventory.getAllProducts();
    }

    /**
     * Retrieves the farm's transaction manager.
     *
     * @return The farm's transaction manager.
     */
    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    /**
     * Retrieves the farm's transaction history.
     *
     * @return The farm's transaction history.
     */
    public TransactionHistory getTransactionHistory() {
        return transactionHistory;
    }

    /**
     * Saves the specified customer in the farm's address book.
     *
     * @param customer The customer to add to the address book.
     * @throws DuplicateCustomerException If the address book already contains this customer.
     */
    public void saveCustomer(Customer customer) throws DuplicateCustomerException {
        // Check if the customer already exists in the address book
        if (addressBook.containsCustomer(customer)) {
            throw new DuplicateCustomerException("Customer already exists in the address book.");
        }

        // Add the customer to the address book
        addressBook.addCustomer(customer);
    }

    /**
     * Adds a single product of the specified type and quality to the farm's inventory.
     *
     * @param barcode The product type to add to the inventory.
     * @param quality The quality of the product to add to the inventory.
     */
    public void stockProduct(Barcode barcode, Quality quality) {
        this.inventory.addProduct(barcode, quality);
    }

    /**
     * Adds a specified quantity of products of the specified type and quality to the farm's inventory.
     *
     * @param barcode The product type to add to the inventory.
     * @param quality The quality of the product to add to the inventory.
     * @param quantity The number of products to add to the inventory.
     * @throws IllegalArgumentException If quantity is less than 1.
     * @throws InvalidStockRequestException If the quantity is greater than 1 when a FancyInventory is not in use.
     */
    public void stockProduct(
            Barcode barcode, Quality quality, int quantity) throws InvalidStockRequestException {
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1.");
        }
        this.inventory.addProduct(barcode, quality, quantity);
    }

    /**
     * Sets the provided transaction as the current ongoing transaction.
     *
     * @param transaction The transaction to set as ongoing.
     * @throws FailedTransactionException If the transaction manager rejects the request to begin managing this transaction.
     * @requires The customer associated with the transaction exists in the farm's address book.
     */
    public void startTransaction(Transaction transaction) throws
            FailedTransactionException {

        // Set the transaction as the ongoing transaction in the transaction manager
        transactionManager.setOngoingTransaction(transaction);
    }

    /**
     * Attempts to add a single product of the given type to the customer's shopping cart.
     *
     * @param barcode The product type to add to the cart.
     * @return The number of products successfully added to the cart (0 or 1).
     * @throws FailedTransactionException If no transaction is ongoing.
     */
    public int addToCart(Barcode barcode) throws
            FailedTransactionException {

        if (!transactionManager.hasOngoingTransaction()) {
            throw new FailedTransactionException(
                    "Cannot add to cart when no customer has started shopping.");
        }

        // Check if the product exists in the inventory
        if (!inventory.existsProduct(barcode)) {
            return 0;
        }

        List<Product> getProducts = inventory.removeProduct(barcode);

        if (!getProducts.isEmpty()) {
            // Add the product to the customer's cart via the transaction manager
            transactionManager.registerPendingPurchase(getProducts.getFirst());
        }

        return 1;  // Successfully added one product to the cart
    }

    /**
     * Attempts to add the specified number of products of the given type to the customer's shopping cart.
     *
     * @param barcode The product type to add to the cart.
     * @param quantity The number of products to add to the cart.
     * @return The number of products successfully added to the cart.
     * @throws FailedTransactionException If no transaction is ongoing or if the quantity is invalid.
     * @throws IllegalArgumentException If quantity is less than 1.
     */
    public int addToCart(Barcode barcode, int quantity) throws
            FailedTransactionException {

        if (!transactionManager.hasOngoingTransaction()) {
            throw new FailedTransactionException(
                    "Cannot add to cart when no customer has started shopping.");
        }

        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1.");
        }

        // Check if the inventory is not FancyInventory and the quantity is greater than 1
        if (!(inventory instanceof FancyInventory) && quantity == 1) {
            throw new FailedTransactionException(
                    "Current inventory is not fancy enough. "
                            +
                            "Please purchase products one at a time.");
        }
        List<Product> getProducts = inventory.removeProduct(barcode, quantity);

        if (!getProducts.isEmpty()) {
            // Add the product to the customer's cart via the transaction manager
            for (Product product : getProducts) {
                transactionManager.registerPendingPurchase(product);
            }
        }

        return getProducts.size(); // Return the number of products successfully added to the cart
    }

    /**
     * Closes the ongoing transaction and records it in the farm's transaction history if products were purchased.
     *
     * @return true if the finalized transaction contained products; false otherwise.
     * @throws FailedTransactionException If the transaction cannot be closed.
     */
    public boolean checkout() throws FailedTransactionException {
        // Check if there is an ongoing transaction
        if (!transactionManager.hasOngoingTransaction()) {
            throw new FailedTransactionException("No ongoing transaction to checkout.");
        }

        // Finalize the current transaction
        Transaction transaction = transactionManager.closeCurrentTransaction();

        // Check if the transaction contained any products
        if (!transaction.getPurchases().isEmpty()) {
            // If the transaction had products, record it in the transaction history
            transactionHistory.recordTransaction(transaction);
            return true; // Indicate that the transaction contained products
        }

        // If the transaction was empty, return false
        return false;
    }

    /**
     * Retrieves the receipt associated with the most recent transaction.
     * This method fetches the receipt from the latest transaction recorded in the farm's history.
     *
     * @return The receipt associated with the most recent transaction, or an empty string if no transactions have occurred.
     */
    public String getLastReceipt() {
        // Retrieve the most recent transaction from the transaction history
        Transaction lastTransaction = transactionHistory.getLastTransaction();

        // Check if there is a transaction available
        if (lastTransaction == null) {
            return "No transactions available.";
        }

        // Return the receipt of the most recent transaction
        return lastTransaction.getReceipt();
    }

    /**
     * Retrieves a customer from the address book using the specified name and phone number.
     * This method searches the address book for a customer that matches the provided name and phone number.
     *
     * @param name The name of the customer to retrieve.
     * @param phoneNumber The phone number of the customer to retrieve.
     * @return The customer instance that matches the given name and phone number.
     * @throws CustomerNotFoundException If no customer with the given name and phone number exists in the address book.
     */
    public Customer getCustomer(String name, int phoneNumber) throws CustomerNotFoundException {
        // Search for the customer in the address book
        Customer customer = addressBook.getCustomer(name, phoneNumber);

        // Check if the customer was found
        if (customer == null) {
            throw new CustomerNotFoundException(
                    "No customer found with name: " + name + " and phone number: " + phoneNumber);
        }

        // Return the found customer
        return customer;
    }

}
