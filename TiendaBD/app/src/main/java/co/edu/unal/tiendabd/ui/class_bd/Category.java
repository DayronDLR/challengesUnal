package co.edu.unal.tiendabd.ui.class_bd;


public class Category {
    private String id;
    private String name;
    private String imageCategory;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String key;

    public Category() {
    }

    public Category(String id, String name, String imageCategory, String key) {
        this.id = id;
        this.name = name;
        this.imageCategory = imageCategory;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageCategory() {
        return imageCategory;
    }

    public void setImageCategory(String imageCategory) {
        this.imageCategory = imageCategory;
    }

    @Override
    public String toString() {
        return name;
    }
}
