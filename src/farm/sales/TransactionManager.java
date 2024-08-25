package farm.sales;

import farm.core.FailedTransactionException;
import farm.inventory.product.Product;
import farm.sales.transaction.Transaction;

/**
 * The controlling class for all transactions.
 * <p>
 * This class is responsible for opening and closing transactions,
 * as well as ensuring that only one transaction
 * is active at any given time. It does not create transactions
 * but keeps track of the currently ongoing transaction
 * and the associated customer cart.
 * </p>
 * <p>
 * Component of Stage 2.
 * </p>
 */
public class TransactionManager {
    private Transaction ongoingTransaction;

    /**
     * Constructs a new TransactionManager object.
     */
    public TransactionManager() {
        // Constructor implementation
        this.ongoingTransaction = null;
    }

    /**
     * Determines whether a transaction is currently in progress.
     *
     * @return {@code true} if a transaction is in progress, else {@code false}.
     */
    public boolean hasOngoingTransaction() {
        // Method implementation
        return ongoingTransaction != null;
    }

    /**
     * Begins managing the specified transaction,
     * provided one is not already ongoing.
     * <p>
     * This method either sets the given transaction
     * to be the one currently in progress and manages it
     * through subsequent calls to the transaction manager,
     * or it rejects the request if a transaction is already ongoing.
     * </p>
     *
     * @param transaction the transaction to set as the manager's ongoing transaction.
     * @throws FailedTransactionException if a transaction is already in progress.
     */
    public void setOngoingTransaction(Transaction transaction) throws FailedTransactionException {
        // Method implementation
        if (hasOngoingTransaction()) {
            throw new FailedTransactionException("A transaction is already in progress.");
        }
        this.ongoingTransaction = transaction;
    }

    /**
     * Adds the given product to the cart of the customer associated
     * with the current transaction.
     * <p>
     * The product can only be added if there is currently an ongoing
     * transaction and that transaction has not already been finalised.
     * </p>
     *
     * @param product the product to add to the customer's cart.
     * @throws FailedTransactionException if there is no ongoing transaction
     * or the transaction has already been finalised.
     * @requires the provided product is known to be valid for purchase,
     * i.e., has been successfully retrieved from the farm's inventory.
     */
    public void registerPendingPurchase(Product product) throws FailedTransactionException {
        // Method implementation
        if (!hasOngoingTransaction()) {
            throw new FailedTransactionException("No ongoing transaction to register purchase.");
        }
        if (ongoingTransaction.isFinalised()) {
            throw new FailedTransactionException("The current transaction is already finalised.");
        }
        ongoingTransaction.getAssociatedCustomer().getCart().addProduct(product);
    }

    /**
     * Finalises the currently ongoing transaction and prepares the
     * TransactionManager to accept a new ongoing transaction.
     * <p>
     * The current transaction can only be closed if there is an actively
     * ongoing transaction. If the currently active transaction
     * is already finalised or contains no purchases, it should still be
     * finalised, and the manager readied to accept a new transaction.
     * </p>
     * <p>
     * When the current transaction is closed, the customer's cart must
     * also be emptied in preparation for their next shop.
     * </p>
     *
     * @return the finalised transaction.
     * @throws FailedTransactionException if there is no currently ongoing
     * transaction to close.
     */
    public Transaction closeCurrentTransaction() throws FailedTransactionException {
        // Method implementation
        if (!hasOngoingTransaction()) {
            throw new FailedTransactionException("No ongoing transaction to close.");
        }
        Transaction transactionToClose = ongoingTransaction;
        this.ongoingTransaction = null;
        transactionToClose.finalise();
        return transactionToClose;
    }
}
