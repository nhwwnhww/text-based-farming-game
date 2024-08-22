package farm.core;

/**
 * Thrown if a transaction failed.
 * <p>
 * This exception is raised when a transaction cannot be completed successfully. It indicates that an error
 * occurred during the transaction process.
 * </p>
 * <p>
 * Component of Stage 2.
 * </p>
 */
public class FailedTransactionException extends Exception {

    /**
     * Constructs a {@code FailedTransactionException} without any additional details.
     */
    public FailedTransactionException() {
        super();
    }

    /**
     * Constructs a {@code FailedTransactionException} with a message describing the exception.
     *
     * @param message the description of the exception.
     */
    public FailedTransactionException(String message) {
        super(message);
    }
}
