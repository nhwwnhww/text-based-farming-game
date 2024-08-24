package farm.sales.transaction;

import farm.customer.Customer;
import farm.inventory.Inventory;
import farm.inventory.product.Product;
import farm.inventory.product.data.Barcode;
import farm.sales.ReceiptPrinter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a special sale transaction where discounts are applied to certain products.
 */
public class SpecialSaleTransaction extends CategorisedTransaction {
    private static Map<Barcode, Integer> discountMap = Map.of();

    /**
     * Constructs a new SpecialSaleTransaction associated with the specified customer.
     * This constructor initializes the transaction with no discounts.
     *
     * @param customer The customer associated with this transaction.
     */
    public SpecialSaleTransaction(Customer customer) {
        super(customer);
        discountMap = new HashMap<>();
    }

    /**
     * Constructs a new SpecialSaleTransaction associated with the specified customer and discount map.
     *
     * @param customer    The customer associated with this transaction.
     * @param discountMap A map associating barcodes with discount amounts.
     */
    public SpecialSaleTransaction(Customer customer, Map<Barcode, Integer> discountMap) {
        super(customer);
        SpecialSaleTransaction.discountMap = new HashMap<>(discountMap);
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
        return new HashSet<>(getPurchasedTypes()).stream()
                .mapToInt(this::getPurchaseSubtotal)
                .sum();
    }

    /**
     * Calculates how much the customer has saved from discounts.
     *
     * @return The numerical savings from discounts.
     */
    public int getTotalSaved() {
        return getPurchasesByType().keySet().stream()
                .mapToInt(type -> {
                    int originalSubtotal = getPurchasesByType().get(type).stream()
                            .mapToInt(Product::getBasePrice)
                            .sum();
                    int discountedSubtotal = getPurchaseSubtotal(type);
                    return originalSubtotal - discountedSubtotal;
                })
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

        // Generate the receipt string
        String receipt;

        // Create the list of entries
        List<List<String>> entries = new ArrayList<>();

        // Define the headings
        List<String> headings = List.of("Item", "Qty", "Price (ea.)", "Subtotal");

        List<Barcode> sortedBarcode = new ArrayList<>(getPurchasedTypes());
        List<Barcode> predefinedOrder = Arrays.asList(Barcode.values());

        sortedBarcode.sort(Comparator.comparingInt(predefinedOrder::indexOf));

        for (Barcode barcode : sortedBarcode) {
            int quantity = getPurchaseQuantity(barcode);
            int pricePerItem = barcode.getBasePrice();
            int subtotal = getPurchaseSubtotal(barcode);
            int discountAmount = getDiscountAmount(barcode);

            String itemName = barcode.getDisplayName().toLowerCase();
            String qtyString = String.valueOf(quantity);
            String priceString = String.format("$%.2f", pricePerItem / 100.0);
            String subtotalString = String.format("$%.2f", subtotal / 100.0);

            List<String> line = List.of(itemName, qtyString, priceString, subtotalString);
            entries.add(line);

            if (discountAmount > 0) {
                discountMap.put(barcode, discountAmount);
            }

        }

        String total = String.format("$%.2f", getTotal() / 100.0);
        String customerName = getAssociatedCustomer().getName();

        // Add total savings at the bottom of the receipt
        if (getTotalSaved() > 0) {
            String totalSaved = String.format(
                    "$%.2f", getTotalSaved() / 100.0);

            List<String> listReceipt = buildPurchasesSectionEntries(
                            generateReceiptLines(
                                    headings, entries, total, customerName, totalSaved));

            StringBuilder sb = new StringBuilder();
            for (String string : listReceipt) {
                sb.append(string).append("\n");
            }
            receipt = sb.toString();
        } else {
            receipt = ReceiptPrinter.createReceipt(headings, entries, total, customerName);
        }

        return receipt;
    }

    private List<String> generateReceiptLines(
            List<String> headings,
            List<List<String>> entries,
            String total, String customerName, String totalSaved) {
        String fullReceipt = ReceiptPrinter.createReceipt(
                headings, entries, total, customerName, totalSaved);
        return Arrays.asList(fullReceipt.split("\n"));
    }

    private static List<String> buildPurchasesSectionEntries(
            List<String> lines) {
        // Create a set of valid Barcode names for quick lookup
        Set<String> validBarcodeNames = Arrays.stream(Barcode.values())
                .map(Enum::name)
                .collect(Collectors.toSet());

        List<String> updatedLines = new ArrayList<>(lines);

        int index = 1;
        for (String entry : lines) {
            String[] itemSplit = entry.split(" ");
            String item = itemSplit[0].toUpperCase(); // Convert to upper case for matching

            if (validBarcodeNames.contains(item)) {
                Barcode barcode = Barcode.valueOf(item);
                if (discountMap.containsKey(barcode) && discountMap.get(barcode) != 0) {
                    String discountMessage = String.format(
                            "Discount applied! %d%% off %s",
                            discountMap.get(barcode), item.toLowerCase());
                    updatedLines.add(index, discountMessage);
                    index++;
                }
            }
            index++;
        }
        return updatedLines;


        }

}

