package farm.core;

/**
 * Exception thrown when a customer is not found in the AddressBook.
 */
public class CustomerNotFoundException extends Exception {

    /**
     * Constructs a new CustomerNotFoundException with no detail message.
     */
    public CustomerNotFoundException() {
        super();
    }

    /**
     * Constructs a new CustomerNotFoundException with the specified detail message.
     *
     * @param message The detail message.
     */
    public CustomerNotFoundException(String message) {
        super(message);
    }
}