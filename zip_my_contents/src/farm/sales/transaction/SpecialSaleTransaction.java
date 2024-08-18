package farm.sales.transaction;

import farm.customer.Customer;
import farm.inventory.product.Product;
import farm.inventory.product.data.Barcode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a special sale transaction where discounts are applied to certain products.
 */
public class SpecialSaleTransaction extends Transaction {
    private Map<Barcode, Integer> discountMap;

    /**
     * Constructs a new SpecialSaleTransaction associated with the specified customer.
     * This constructor initializes the transaction with no discounts.
     *
     * @param customer The customer associated with this transaction.
     */
    public SpecialSaleTransaction(Customer customer) {
        super(customer);
        this.discountMap = new HashMap<>();
    }

    /**
     * Constructs a new SpecialSaleTransaction associated with the specified customer and discount map.
     *
     * @param customer    The customer associated with this transaction.
     * @param discountMap A map associating barcodes with discount amounts.
     */
    public SpecialSaleTransaction(Customer customer, Map<Barcode, Integer> discountMap) {
        super(customer);
        this.discountMap = new HashMap<>(discountMap);
    }

    /**
     * Returns the discount amount for a specific barcode.
     *
     * @param barcode The barcode of the product.
     * @return The discount amount for the specified barcode, or 0 if no discount applies.
     */
    public int getDiscountAmount(Barcode barcode) {
        return discountMap.getOrDefault(barcode, 0);
    }

    /**
     * Calculates the total amount saved during this transaction by summing the discounts for all purchased products.
     *
     * @return The total amount saved during the transaction.
     */
    public int getTotalSaved() {
        int totalSaved = 0;
        for (Product product : getPurchases()) {
            Barcode barcode = product.getBarcode();
            int discount = getDiscountAmount(barcode);
            totalSaved += discount;
        }
        return totalSaved;
    }

    /**
     * Adds a product to the transaction's list of purchases, applying any relevant discount.
     *
     * @param product The product to be added.
     */
    @Override
    public void addProduct(Product product) {
        super.addProduct(product);
        // Apply discount to the total, if applicable
        int discount = getDiscountAmount(product.getBarcode());
        int total = getTotal();
        setTotal(total - discount);
    }

    /**
     * Sets the total cost of the transaction.
     *
     * @param total The new total cost of the transaction.
     */
    private void setTotal(int total) {
        // This method would adjust the internal total amount, potentially updating a superclass field.
        // Implement this depending on how the total is stored in the Transaction class.
    }
}
