package farm.inventory;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import farm.inventory.product.*;
import farm.inventory.product.data.Barcode;
import farm.inventory.product.data.Quality;
import farm.core.InvalidStockRequestException;
import farm.core.FailedTransactionException;

import java.util.ArrayList;
import java.util.List;

public class FancyInventoryTest {

    private FancyInventory inventory;

    @Before
    public void setUp() throws InvalidStockRequestException {
        inventory = new FancyInventory();

        // Add products to the inventory
//        inventory.addProduct(Barcode.EGG, Quality.GOLD, 5);
        inventory.addProduct(Barcode.EGG, Quality.REGULAR, 3);
//        inventory.addProduct(Barcode.JAM, Quality.SILVER, 6);
//        inventory.addProduct(Barcode.JAM, Quality.REGULAR, 6);
    }

    @Test
    public void removeProductQuantityEnoughTest() throws FailedTransactionException {
        // Test removing 2 eggs when there are enough in the inventory
        List<Product> removedProducts = inventory.removeProduct(Barcode.EGG, 10);
        System.out.println(removedProducts);
        assertEquals(5, removedProducts.size());
        assertEquals(Quality.GOLD, removedProducts.get(0).getQuality());
        assertEquals(Quality.REGULAR, removedProducts.get(1).getQuality());
    }

//    @Test
//    public void removeProductQuantityNotEnoughTest() throws FailedTransactionException {
//        // Test removing 10 eggs when there are only 5 in the inventory
//        List<Product> removedProducts = inventory.removeProduct(Barcode.EGG, 10);
//        assertEquals(3, removedProducts.size()); // Should only remove 3 eggs
//    }
//
//    @Test
//    public void removeProductQuantityOrderTest() throws FailedTransactionException {
//        // Test removing 3 JAMs, ensuring that the regular ones are removed before the silver one
//        List<Product> removedProducts = inventory.removeProduct(Barcode.JAM, 3);
//        assertEquals(3, removedProducts.size());
//        assertEquals(Quality.REGULAR, removedProducts.get(0).getQuality());
//        assertEquals(Quality.REGULAR, removedProducts.get(1).getQuality());
//        assertEquals(Quality.REGULAR, removedProducts.get(2).getQuality());
//    }
//
//    @Test(expected = FailedTransactionException.class)
//    public void removeProductThrowsExceptionTest() throws FailedTransactionException {
//        // Test if exception is thrown when trying to remove more products than available
//        inventory.removeProduct(Barcode.JAM, 5);
//    }
}
