package farm.inventory;

import farm.core.FailedTransactionException;
import farm.core.InvalidStockRequestException;
import farm.inventory.product.*;
import farm.inventory.product.Product;
import farm.inventory.product.data.Barcode;
import farm.inventory.product.data.Quality;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FancyInventoryTest {
    @Test
    public void getStockedQuantityTest() throws InvalidStockRequestException {
        FancyInventory inventory = new FancyInventory();
        Barcode barcode = Barcode.EGG;
        Quality quality = Quality.REGULAR;

        // Add 10 eggs to the inventory
        inventory.addProduct(barcode, quality, 10);

        // Check if the stocked quantity is 10
        int expectedQuantity = 10;
        int actualQuantity = inventory.getStockedQuantity(barcode);
        Assert.assertEquals("Incorrect stocked quantity when 10 EGG's were added to the inventory", expectedQuantity, actualQuantity);
    }

    @Test
    public void removeProductOrderTest() {
        FancyInventory inventory = new FancyInventory();
        Barcode barcode = Barcode.JAM;

        // Add 10 REGULAR JAM and 1 SILVER JAM to the inventory
        for (int i = 0; i < 10; i++) {
            inventory.addProduct(barcode, Quality.REGULAR);
        }
        inventory.addProduct(barcode, Quality.SILVER);

        // Remove all products one by one
        List<Product> removedProducts = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            removedProducts.addAll(inventory.removeProduct(barcode));
        }

        // Check if the removal order is correct
        Assert.assertEquals("Incorrect product removal order.", List.of(
                new Jam(Quality.SILVER),
                new Jam(Quality.REGULAR),
                new Jam(Quality.REGULAR),
                new Jam(Quality.REGULAR),
                new Jam(Quality.REGULAR),
                new Jam(Quality.REGULAR),
                new Jam(Quality.REGULAR),
                new Jam(Quality.REGULAR),
                new Jam(Quality.REGULAR),
                new Jam(Quality.REGULAR),
                new Jam(Quality.REGULAR)
        ), removedProducts);
    }

    @Test
    public void removeProductQuantityNotEnoughTest() throws InvalidStockRequestException, FailedTransactionException {
        FancyInventory inventory = new FancyInventory();
        Barcode barcode = Barcode.EGG;
        Quality quality = Quality.REGULAR;

        // Add 5 eggs to the inventory
        inventory.addProduct(barcode, quality, 5);

        // Attempt to remove 10 eggs
        List<Product> removedProducts = inventory.removeProduct(barcode, 10);

        // Check if only 5 eggs were removed
        Assert.assertEquals("There should be 5 EGG's removed when the inventory contains only 5 EGG even though the wanted quantity was 10.",
                List.of(
                        new Egg(Quality.REGULAR),
                        new Egg(Quality.REGULAR),
                        new Egg(Quality.REGULAR),
                        new Egg(Quality.REGULAR),
                        new Egg(Quality.REGULAR)
                ), removedProducts);
    }

    @Test
    public void removeProductQuantityOrderTest() throws FailedTransactionException {
        FancyInventory inventory = new FancyInventory();
        Barcode barcode = Barcode.JAM;

        // Add 3 REGULAR JAM and 1 SILVER JAM to the inventory
        for (int i = 0; i < 3; i++) {
            inventory.addProduct(barcode, Quality.REGULAR);
        }
        inventory.addProduct(barcode, Quality.SILVER);

        // Attempt to remove 1 product
        List<Product> removedProducts = inventory.removeProduct(barcode, 1);

        // Check if the SILVER JAM was removed
        Assert.assertEquals("There should be 1 SILVER JAM removed when the inventory contains 4 JAM, 1 of which is SILVER, and 3 REGULAR.",
                List.of(new Jam(Quality.SILVER)), removedProducts);
    }

    @Test
    public void removeProductTest() throws FailedTransactionException, InvalidStockRequestException {
        FancyInventory inventory = new FancyInventory();
        Barcode barcode = Barcode.JAM;
        Quality quality = Quality.REGULAR;

        // Add 10 JAM to the inventory
        inventory.addProduct(barcode, quality, 10);

        // Attempt to remove all 10 JAM
        List<Product> removedProducts = inventory.removeProduct(barcode, 10);

        // Check if all 10 JAM were removed
        Assert.assertEquals("There should be 10 JAMs removed when the inventory contains 10 JAM.",
                List.of(
                        new Jam(Quality.REGULAR)
                ), removedProducts);
    }

}
