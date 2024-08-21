package farm.sales.transaction;

import farm.customer.Customer;
import farm.inventory.product.Product;
import farm.inventory.product.data.Barcode;
import farm.sales.ReceiptPrinter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a special sale transaction where discounts are applied to certain products.
 */
public class SpecialSaleTransaction extends CategorisedTransaction {
    private final Map<Barcode, Integer> discountMap;

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
     * Determines the total price for the provided product type within this transaction,
     * with any specified discount applied as an integer percentage taken from the usual subtotal.
     *
     * @param type The product type whose subtotal should be calculated.
     * @return The total (discounted) price for all instances of that product type within this transaction.
     */
    @Override
    public int getPurchaseSubtotal(Barcode type) {
        List<Product> products = getPurchasesByType().get(type);
        if (products == null || products.isEmpty()) {
            return 0;
        }

        int subtotal = products.stream()
                .mapToInt(Product::getBasePrice)
                .sum();

        int discountPercentage = getDiscountAmount(type);
        return subtotal - (subtotal * discountPercentage / 100);
    }

    /**
     * Calculates the total price (with discounts) of all the current products in the transaction.
     *
     * @return The total (discounted) price calculated.
     */
    @Override
    public int getTotal() {
        return getPurchasedTypes().stream()
                .mapToInt(this::getPurchaseSubtotal)
                .sum();
    }

    /**
     * Calculates how much the customer has saved from discounts.
     *
     * @return The numerical savings from discounts.
     */
    public int getTotalSaved() {
//        return getPurchasedTypes().stream()
//                .mapToInt(type -> {
//                    int subtotal = getPurchasesByType().get(type).stream()
//                            .mapToInt(Product::getBasePrice)
//                            .sum();
//                    int discountPercentage = getDiscountAmount(type);
//                    return subtotal * discountPercentage / 100;
//                })
//                .sum();

        return getPurchasesByType().keySet().stream()
                .mapToInt(type -> getPurchaseQuantity(type) - getPurchaseSubtotal(type))
                .sum();
    }

    /**
     * Returns a string representation of this transaction and its current state.
     *
     * @return The formatted string representation of the transaction.
     */
    @Override
    public String toString() {
        String productsString = getPurchases().stream()
                .map(Product::toString)
                .collect(Collectors.joining(", "));

        String discountsString = discountMap.toString();

        String status = isFinalised() ? "Finalised" : "Active";

        return "Transaction {Customer: "
                +
                getAssociatedCustomer().getName()
                +
                " | Phone Number: "
                +
                getAssociatedCustomer().getPhoneNumber()
                +
                " | Address: "
                +
                getAssociatedCustomer().getAddress()
                +
                ", Status: "
                +
                status
                +
                ", Associated Products: ["
                +
                productsString
                +
                "], Discounts: "
                +
                discountsString
                +
                '}';
    }

    /**
     * Converts the transaction into a formatted receipt for display, using the ReceiptPrinter.
     *
     * @return The styled receipt representation of this transaction.
     */
    @Override
    public String getReceipt() {
        if (!isFinalised()) {
            return ReceiptPrinter.createActiveReceipt();
        }

        List<String> headings = Arrays.asList("Item", "Qty", "Price (ea.)", "Subtotal");
        List<List<String>> entries = new ArrayList<>();
        int totalSavings = 0;

        for (Barcode barcode : Barcode.values()) {
            List<Product> products = getPurchasesByType().get(barcode);
            if (products != null && !products.isEmpty()) {
                int quantity = products.size();
                int pricePerItem = products.getFirst().getBasePrice();
                int subtotal = getPurchaseSubtotal(barcode);
                int discountAmount = getDiscountAmount(barcode);

                String itemName = barcode.getDisplayName().toLowerCase();
                String qtyString = String.valueOf(quantity);
                String priceString = String.format("$%.2f", pricePerItem / 100.0);
                String subtotalString = String.format("$%.2f", subtotal / 100.0);

                String entry = String
                        .format("%s    %s       %s       %s",
                                itemName, qtyString, priceString, subtotalString);

                if (discountAmount > 0) {
                    int originalSubtotal = getPurchaseSubtotal(barcode);
                    int savings = originalSubtotal - subtotal;
                    totalSavings += savings;
                    entry += String
                            .format("\nDiscount applied! %d%% off %s", discountAmount, itemName);
                }

                entries.add(Arrays.asList(itemName, qtyString, priceString, subtotalString));
            }
        }

        String total = String.format("$%.2f", getTotal() / 100.0);
        String savingsMessage = totalSavings > 0 ? String
                .format("***** TOTAL SAVINGS: $%.2f *****", totalSavings / 100.0) : "";

        String customerName = getAssociatedCustomer().getName();

        return ReceiptPrinter.createReceipt(headings, entries, total, customerName, savingsMessage);
    }
}
