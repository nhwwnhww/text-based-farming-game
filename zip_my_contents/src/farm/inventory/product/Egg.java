package farm.inventory.product;

import farm.inventory.product.data.Barcode;
import farm.inventory.product.data.Quality;

/**
 * Represents an Egg product in the inventory.
 */
public class Egg extends Product {

    private static final Barcode barcode = Barcode.EGG;
    private static final Quality quality = Quality.REGULAR;

    /**
     * Constructs a new Egg product with the specified barcode and quality.
     */
    public Egg() {
        super(barcode, quality);
    }

    @Override
    public String getDisplayName() {
        return "Egg";
    }

    @Override
    public int getBasePrice() {
        // Define the base price for Egg, could vary by quality
        return 5;
    }
}