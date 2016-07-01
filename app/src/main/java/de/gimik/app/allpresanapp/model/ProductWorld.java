package de.gimik.app.allpresanapp.model;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "product_world")
public class ProductWorld extends AuditableEntity {
    private static final long serialVersionUID = -1325531598075759936L;

    @SerializedName("name")
    @DatabaseField
    private String name;

    @SerializedName("image")
    @DatabaseField
    private String image;

    @SerializedName("icon")
    @DatabaseField
    private String icon;

    @SerializedName("productGroupTitle")
    @DatabaseField
    private String productGroupTitle;

    @SerializedName("productGroupSubTitle")
    @DatabaseField
    private String productGroupSubTitle;

    @SerializedName("logo")
    @DatabaseField
    private String logo;

    @SerializedName("productGroupImage")
    @DatabaseField
    private String productGroupImage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getProductGroupTitle() {
        return productGroupTitle;
    }

    public void setProductGroupTitle(String productGroupTitle) {
        this.productGroupTitle = productGroupTitle;
    }

    public String getProductGroupSubTitle() {
        return productGroupSubTitle;
    }

    public void setProductGroupSubTitle(String productGroupSubTitle) {
        this.productGroupSubTitle = productGroupSubTitle;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getProductGroupImage() {
        return productGroupImage;
    }

    public void setProductGroupImage(String productGroupImage) {
        this.productGroupImage = productGroupImage;
    }

}
