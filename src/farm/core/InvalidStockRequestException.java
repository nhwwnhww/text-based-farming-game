package farm.core;

/**
 * Thrown if attempting to perform a stock operation with invalid parameters.
 * <p>
 * This exception is raised when an invalid stock request is made, indicating that the operation cannot proceed
 * due to incorrect or inappropriate parameters.
 * </p>
 * <p>
 * Component of Stage 2.
 * </p>
 */
public class InvalidStockRequestException extends Exception {

    /**
     * Constructs an {@code InvalidStockRequestException} in response to an invalid stock request with no additional details.
     */
    public InvalidStockRequestException() {
        super();
    }

    /**
     * Constructs an {@code InvalidStockRequestException} in response to an invalid stock request
     * with a message describing the exception.
     *
     * @param message the description of the exception.
     */
    public InvalidStockRequestException(String message) {
        super(message);
    }
}
