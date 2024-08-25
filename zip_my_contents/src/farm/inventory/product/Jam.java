package farm.inventory.product;

import farm.inventory.product.data.Barcode;
import farm.inventory.product.data.Quality;

/**
 * Represents a Jam product in the inventory.
 */
public class Jam extends Product {

    private static final Barcode barcode = Barcode.JAM;

    /**
     * Constructs a new Egg product with the specified quality.
     * If no quality is provided, it defaults to regular quality.
     */
    public Jam() {
        super(barcode, Quality.REGULAR);
    }

    /**
     * Constructs a new Egg product with the specified barcode and quality.
     */
    public Jam(Quality quality) {
        super(barcode, quality != null ? quality : Quality.REGULAR);
    }

}