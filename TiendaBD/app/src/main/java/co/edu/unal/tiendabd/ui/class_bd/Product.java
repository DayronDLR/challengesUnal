package co.edu.unal.tiendabd.ui.class_bd;

public class Product {
    private String name;
    private String availability;
    private String id;
    private String category;
    private String imageProduct;
    private String key;

    public Product() {
    }

    public Product(String name, String availability, String id, String category, String imageProduct, String key) {
        this.name = name;
        this.availability = availability;
        this.id = id;
        this.category = category;
        this.imageProduct = imageProduct;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageProduct() {
        return imageProduct;
    }

    public void setImageProduct(String imageProduct) {
        this.imageProduct = imageProduct;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
