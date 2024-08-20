package farm.inventory.product;

import farm.inventory.product.data.Barcode;
import farm.inventory.product.data.Quality;

import java.util.Objects;

/**
 * Represents an abstract product in the inventory system.
 * This class should be extended by specific product types.
 */
public abstract class Product {
    private final Barcode barcode;
    private final Quality quality;

    /**
     * Constructs a new Product with the specified barcode and quality.
     *
     * @param barcode The barcode associated with the product.
     * @param quality The quality of the product.
     */
    public Product(Barcode barcode, Quality quality) {
        this.barcode = barcode;
        this.quality = quality;
    }

    /**
     * Returns the barcode of the product.
     *
     * @return The barcode of the product.
     */
    public Barcode getBarcode() {
        return barcode;
    }

    /**
     * Returns the display name of the product.
     *
     * @return The display name of the product.
     */
    public String getDisplayName() {
        return barcode.getDisplayName();
    }

    /**
     * Returns the quality of the product.
     *
     * @return The quality of the product.
     */
    public Quality getQuality() {
        return quality;
    }

    /**
     * Returns the base price of the product.
     * Subclasses must implement this to provide the specific price of the product.
     *
     * @return The base price of the product.
     */
    public int getBasePrice() {
        return barcode.getBasePrice();
    }

    /**
     * Returns a string representation of the product, including its display name and quality.
     *
     * @return A string representation of the product.
     */
    @Override
    public String toString() {
        return getDisplayName() + ": " + getBasePrice() + "c " + getQuality();
    }

    /**
     * Compares this product to the specified object. The result is true if and only if the argument
     * is not null and is a Product object that has the same barcode and quality as this object.
     *
     * @param o The object to compare this Product against.
     * @return true if the given object represents a Product equivalent to this product, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product product = (Product) o;
        return Objects.equals(barcode, product.barcode) && quality == product.quality;
    }

    /**
     * Returns a hash code value for the product based on its barcode and quality.
     *
     * @return A hash code value for this product.
     */
    @Override
    public int hashCode() {
        return Objects.hash(barcode, quality);
    }
}
