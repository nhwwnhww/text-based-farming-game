package farm.core;

import farm.customer.AddressBook;
import farm.customer.Customer;
import farm.inventory.product.data.Barcode;
import farm.inventory.product.data.Quality;
import farm.sales.transaction.Transaction;
import org.junit.Test;
import static org.junit.Assert.*;
import farm.core.Farm;
import farm.core.FailedTransactionException;
import farm.inventory.Inventory;
import farm.inventory.FancyInventory;
import farm.inventory.product.*;
import farm.core.FarmManager;

public class FarmTest {

    private final FancyInventory inventory = new FancyInventory();
    private final AddressBook addressBook = new AddressBook();
    private final Farm farm = new Farm(inventory, addressBook);
    private final Customer customer = new Customer("Ali", 33651111, "UQ");
    private final Transaction transaction = new Transaction(customer);

//    @Test
//    public void addFancyQtyWithFancyInventorySufficientStockTest() throws InvalidStockRequestException, DuplicateCustomerException {
//        addressBook.addCustomer(customer);
//
//        farm.stockProduct(Barcode.MILK, Quality.GOLD,10); // Adding 10 units of a fancy product to the inventory
//
//        try {
//            farm.startTransaction(transaction); // Starting a transaction
//            farm.addToCart(Barcode.MILK, 5); // Adding 5 units of the product to the cart
//            farm.checkout(); // Checkout to complete the transaction
//            assertTrue(true); // If no exception, the test passes
//        } catch (FailedTransactionException e) {
//            fail("Test failed due to fancy inventory issues: " + e.getMessage());
//        }
//    }
//
    @Test
    public void addToCartNoOngoingFailureMessageTest() {
        try {
            farm.addToCart(Barcode.MILK, 1); // Trying to add to cart without starting a transaction
            fail("A failed transaction exception should have been thrown.");
        } catch (FailedTransactionException e) {
            assertEquals("No ongoing transaction. Cannot add to cart.", e.getMessage());
        }
    }

    @Test
    public void addToCartNoOngoingFailureTest() {
        try {
            farm.addToCart(Barcode.MILK, 1); // Trying to add to cart without starting a transaction
            fail("Expected exception: FailedTransactionException");
        } catch (FailedTransactionException e) {
            // Test passes as the exception is expected
        }
    }
//
//    @Test
//    public void addToCartNoStockTest() throws FailedTransactionException {
//        farm.startTransaction(transaction); // Starting a transaction
//
//        try {
//
//            farm.addToCart(Barcode.valueOf("YES"), 1); // Trying to add a non-existent product to the cart
//            fail("FailedTransactionException thrown when it should not have been.");
//        } catch (FailedTransactionException e) {
//            assertEquals("Product not found in inventory.", e.getMessage());
//        }
//    }
//
//    @Test
//    public void addToCartSuccessTest() throws FailedTransactionException {
//        farm.addToCart(Barcode.MILK, 10); // Adding product to inventory
//        farm.startTransaction(transaction); // Starting a transaction
//
//        try {
//            farm.addToCart(Barcode.MILK, 1); // Adding 1 unit of the product to the cart
//            farm.checkout(); // Checkout to complete the transaction
//            assertTrue(true); // If no exception, the test passes
//        } catch (FailedTransactionException e) {
//            fail("FailedTransactionException thrown when it should not have been.");
//        }
//    }
//
//    @Test
//    public void checkoutWithPurchasesSuccessTest() throws FailedTransactionException {
//        farm.addToCart(Barcode.WOOL, 10); // Adding fancy product to inventory
//        farm.startTransaction(transaction); // Starting a transaction
//
//        try {
//            farm.addToCart(Barcode.WOOL, 1); // Adding fancy product to the cart
//            assertTrue(farm.checkout()); // Checking out and expecting a successful transaction
//        } catch (FailedTransactionException e) {
//            fail("FailedTransactionException thrown during checkout: " + e.getMessage());
//        }
//    }

//    @Test
//    public void getLastReceiptTest() {
//        try {
//            // Start transactions and add products
//            farm.startTransaction(transaction);
//            transaction.getAssociatedCustomer().getCart().addProduct(new Egg(Quality.GOLD)); // Example product
//            transaction.getAssociatedCustomer().getCart().addProduct(new Milk(Quality.SILVER)); // Example product
//            transaction.getAssociatedCustomer().getCart().addProduct(new Wool(Quality.REGULAR)); // Example product
//
//            farm.checkout();
//
//            farm.startTransaction(transaction);
//            transaction.getAssociatedCustomer().getCart().addProduct(new Jam(Quality.REGULAR)); // Example product
//            String receipt2 = transaction.getReceipt();
//            farm.checkout();
//            System.out.println(farm.getTransactionHistory());
//
//            // Retrieve the last receipt
//            String lastReceipt = farm.getLastReceipt();
//            System.out.println(lastReceipt);
//
//            // Assert that the last receipt matches the expected one
//            assertNotNull(lastReceipt);
//            assertEquals(receipt2, lastReceipt);
//
//        } catch (FailedTransactionException e) {
//            fail("Transaction failed unexpectedly: " + e.getMessage());
//        }
//    }
}

