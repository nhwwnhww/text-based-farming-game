package farm.core;

import farm.core.FailedTransactionException;
import farm.core.InvalidStockRequestException;
import farm.customer.AddressBook;
import farm.customer.Customer;
import farm.inventory.BasicInventory;
import farm.inventory.FancyInventory;
import farm.inventory.Inventory;
import farm.inventory.product.*;
import farm.inventory.product.Product;
import farm.inventory.product.data.Barcode;
import farm.inventory.product.data.Quality;
import farm.sales.transaction.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FarmManagerTest {

    private final FancyInventory inventory = new FancyInventory();
    private final AddressBook addressBook = new AddressBook();
    private final Farm farm = new Farm(inventory, addressBook);
    private final Customer customer = new Customer("Ali", 33651111, "UQ");
    private final Transaction transaction = new Transaction(customer);
    private final FarmManager farmManager = new FarmManager(farm, new ShopFront());
//    @Test
//    public void addToInventoryQuantityMultipleTest() {
//        Inventory inventory = new FancyInventory();
//        FarmManager farmManager = new FarmManager(new Farm(inventory, new AddressBook()),new ShopFront());
//        String EGG = "EGG";
//        String MILK = "MILK";
//        String JAM = "JAM";
//        Quality regularQuality = Quality.REGULAR;
//
//        // Add 2 eggs, 2 milk, and 2 jam to the inventory
//        farmManager.addToInventory(EGG, 2);
//        System.out.println(inventory.getAllProducts());
//        farmManager.addToInventory(MILK, 2);
//        farmManager.addToInventory(JAM, 2);
//
//        // Retrieve all products from the inventory
//        List<Product> products = inventory.getAllProducts();
//
//        // Expected products in the inventory
//        List<Product> expectedProducts = List.of(
//                new Egg(regularQuality), new Egg(regularQuality),
//                new Milk(regularQuality), new Milk(regularQuality),
//                new Jam(regularQuality), new Jam(regularQuality)
//        );

//        // Verify that each expected product is found in the actual products list
//        for (Product expectedProduct : expectedProducts) {
//            Assert.assertTrue("Missing expected product: " + expectedProduct,
//                    products.contains(expectedProduct));
//        }
//
//        // Verify that the actual products list doesn't contain any extra products
//        Assert.assertEquals("Unexpected products found in inventory.", expectedProducts.size(), products.size());
//    }

    @Test
    public void addToInventoryQuantityNegativeQuantityTest() {
        String productName = "EGG";

        try {
            // Attempt to add a negative quantity of eggs
            farmManager.addToInventory(productName, -1);
            Assert.fail("Expected an IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            // Check if the correct error message is displayed
            assertEquals("The product add failure message was not displayed with the correct content when a negative quantity was specified.",
                    "Quantity must be at least 1.", e.getMessage());
        }
    }

}
