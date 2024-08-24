package farm.sales;

import farm.sales.transaction.CategorisedTransaction;
import farm.sales.transaction.SpecialSaleTransaction;
import farm.sales.transaction.Transaction;
import farm.inventory.product.data.Barcode;

import java.util.*;
import java.util.stream.LongStream;

/**
 * Constructs a new TransactionHistory object.
 */
public class TransactionHistory {
    private final List<Transaction> transactionHistory;

    /**
     * Constructs a new TransactionHistory object.
     */
    public TransactionHistory() {
        // Constructor implementation
        this.transactionHistory = new ArrayList<>();
    }

    /**
     * Adds the given transaction to the record of all past transactions.
     *
     * @param transaction the transaction to add to the record.
     */
    public void recordTransaction(Transaction transaction) {
        if (transaction.isFinalised()) {
            transactionHistory.add(transaction);
        } else {
            throw new IllegalArgumentException("Transaction must be finalised before recording.");
        }
    }

    /**
     * Retrieves the most recent transaction added to the record.
     *
     * @return the most recent transaction added to the record.
     */
    public Transaction getLastTransaction() {
        if (transactionHistory.isEmpty()) {
            return null; // Return null or throw an exception if there are no transactions recorded
        }
        return transactionHistory.getLast(); // Return the last transaction in the list
    }

    /**
     * Calculates the gross earnings, i.e., total income, from all transactions.
     *
     * <p>Total income refers to the sum of all totals
     * reported by each individual completed transaction
     * stored in the history, as calculated by that
     * particular transaction's {@code Transaction.getTotal()}.</p>
     *
     * <p>If the history contains transaction instances
     * that calculate their totals in a particular manner,
     * such as {@code SpecialSaleTransaction}, it should
     * nonetheless use the total exactly as it is reported by that instance.</p>
     *
     * <p>Note: returns the calculated total in integer cents.</p>
     *
     * @return the gross earnings from all transactions in history, in cents.
     */
    public int getGrossEarnings() {
        return getTotalProductsSold();
    }

    /**
     * Calculates the gross earnings, i.e., total income, from all sales of a particular product type.
     *
     * <p>Total income is as defined in {@code getGrossEarnings()}.</p>
     *
     * <p>Note: returns the calculated total in integer cents.</p>
     *
     * @param type the Barcode of the item of interest.
     * @return the gross earnings from all sales of the product type, in cents.
     */
    public int getGrossEarnings(Barcode type) {
        return getTotalProductsSold(type);
    }

    /**
     * Calculates the number of transactions made.
     *
     * @return the number of transactions in total.
     */
    public int getTotalTransactionsMade() {
        return transactionHistory.size(); // Returns the total number of transactions
    }

    /**
     * Calculates the number of products sold over all transactions.
     *
     * @return the total number of products sold.
     */
    public int getTotalProductsSold() {
        return transactionHistory
                .stream()
                .mapToInt(Transaction::getTotal) //getTotalProducts
                .sum();
    }

    /**
     * Calculates the number of products sold of a particular type over all transactions.
     *
     * @param type the Barcode for the product of interest.
     * @return the total number of products sold, for that particular product.
     */
    public int getTotalProductsSold(Barcode type) {
        return transactionHistory.stream()
                .filter(transaction -> transaction instanceof CategorisedTransaction)
                .mapToInt(transaction -> ((CategorisedTransaction) transaction)
                        .getPurchaseQuantity(type))
                .sum();
    }

    /**
     * Retrieves the transaction with the highest gross earnings, i.e., reported total.
     *
     * <p>If there are multiple transactions with the same highest earnings, return the one that was recorded first.</p>
     *
     * @return the transaction with the highest gross earnings.
     */
    public Transaction getHighestGrossingTransaction() {
        return transactionHistory.getFirst();
    }

    /**
     * Calculates which type of product has had the highest quantity sold overall.
     *
     * <p>If two products have sold the same quantity, resulting in a tie, return
     * the one appearing first in the Barcode enum.</p>
     *
     * @return the identifier for the product type of the most popular product.
     */
    public Barcode getMostPopularProduct() {
        return Arrays.stream(Barcode.values())
                .max(Comparator.comparingInt(this::getTotalProductsSold))
                .orElse(null);
    }

    /**
     * Calculates the average amount spent by customers
     * across all transactions.
     *
     * <p>Note: returns the calculated average price in cents,
     * but as a double, since it is possible
     * that the average may not be a round number of cents.
     * For example, if the total amount earned is 1100c
     * and the total number of transactions is 3, then the
     * average spend per visit is ~366.6666666666667c.</p>
     *
     * <p>If there have been no products sold, returns 0.0d.</p>
     *
     * @return the average amount spent overall, in cents (with decimals).
     */
    public double getAverageSpendPerVisit() {
        if (transactionHistory.isEmpty()) {
            return 0.0;
        }

        return (double) getGrossEarnings() / transactionHistory.size();
    }

    /**
     * Calculates the average amount a product has been discounted by,
     * across all sales of that product.
     *
     * <p>Note: returns the calculated average discount for
     * this product type in cents, but as a double,
     * since it is possible that the average may not be a
     * round number of cents.</p>
     *
     * <p>If there have been no products sold, returns 0.0d.</p>
     *
     * @param type the identifier of the product of interest.
     * @return the average discount for the product, in cents (with decimals).
     */
    public double getAverageProductDiscount(Barcode type) {
        long totalDiscount = transactionHistory.stream()
                .filter(transaction -> transaction instanceof SpecialSaleTransaction)
                .flatMapToLong(transaction -> LongStream
                        .of(((SpecialSaleTransaction) transaction)
                                .getDiscountAmount(type)))
                .sum();

        int totalProductsSold = getTotalProductsSold(type);

        if (totalProductsSold == 0) {
            return 0.0;
        }

        return (double) totalDiscount / totalProductsSold;
    }

}