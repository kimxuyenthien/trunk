package de.gimik.app.allpresanapp.model;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

@DatabaseTable(tableName = "product_group")
public class ProductGroup extends AuditableEntity {
    private static final long serialVersionUID = -1325531598075759936L;

    @SerializedName("name")
    @DatabaseField
    private String name;

    @SerializedName("color")
    @DatabaseField
    private String color;

    @SerializedName("title")
    @DatabaseField
    private String title;

    @SerializedName("description")
    @DatabaseField
    private String description;

    @SerializedName("image")
    @DatabaseField
    private String image;

    @SerializedName("productWorldId")
    @DatabaseField
    private int productWorldId;

    private ProductWorld productWorld;
    private List<Product> products;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getProductWorldId() {
        return productWorldId;
    }

    public void setProductWorldId(int productWorldId) {
        this.productWorldId = productWorldId;
    }

    public ProductWorld getProductWorld() {
        return productWorld;
    }

    public void setProductWorld(ProductWorld productWorld) {
        this.productWorld = productWorld;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
