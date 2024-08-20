package farm.inventory.product;

import farm.inventory.product.data.Barcode;
import farm.inventory.product.data.Quality;

/**
 * Represents an Egg product in the inventory.
 */
public class Egg extends Product {

    private static final Barcode barcode = Barcode.EGG;

    /**
     * Constructs a new Egg product with the specified barcode and quality.
     */
    public Egg(Quality quality) {
        super(barcode, quality != null ? quality : Quality.REGULAR);
    }

    /**
     * Constructs a new Egg product with the specified quality.
     * If no quality is provided, it defaults to regular quality.
     */
    public Egg() {
        this(Quality.REGULAR);
    }
}