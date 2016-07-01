package de.gimik.app.allpresanapp.model;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "product")
public class Product extends AuditableEntity {
    private static final long serialVersionUID = -1325531598075759936L;

    @SerializedName("name")
    @DatabaseField
    private String name;

    @SerializedName("title")
    @DatabaseField
    private String title;

    @SerializedName("title2")
    @DatabaseField
    private String title2;

    @SerializedName("label")
    @DatabaseField
    private String label;

    @SerializedName("description")
    @DatabaseField
    private String description;

    @SerializedName("composition")
    @DatabaseField
    private String composition;

    @SerializedName("image")
    @DatabaseField
    private String image;

    @SerializedName("icon")
    @DatabaseField
    private String icon;

    @SerializedName("productGroupId")
    @DatabaseField
    private int productGroupId;

    @SerializedName("technology")
    @DatabaseField
    private boolean technology;

    private ProductGroup productGroup;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getProductGroupId() {
        return productGroupId;
    }

    public void setProductGroupId(int productGroupId) {
        this.productGroupId = productGroupId;
    }

    public ProductGroup getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(ProductGroup productGroup) {
        this.productGroup = productGroup;
    }

    public boolean isTechnology() {
        return technology;
    }

    public void setTechnology(boolean technology) {
        this.technology = technology;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
