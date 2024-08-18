package farm.core;

/**
 * Exception thrown when attempting to add a duplicate customer to the AddressBook.
 */
public class DuplicateCustomerException extends Exception {

    /**
     * Constructs a new DuplicateCustomerException with no detail message.
     */
    public DuplicateCustomerException() {
        super();
    }

    /**
     * Constructs a new DuplicateCustomerException with the specified detail message.
     *
     * @param message The detail message.
     */
    public DuplicateCustomerException(String message) {
        super(message);
    }
}