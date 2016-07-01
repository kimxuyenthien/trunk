package de.gimik.app.allpresanapp.model;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "ean")
public class Ean extends AuditableEntity {
    private static final long serialVersionUID = -1325531598075759936L;

    @SerializedName("name")
    @DatabaseField
    private String name;

    @SerializedName("size")
    @DatabaseField
    private String size;

    @SerializedName("productId")
    @DatabaseField
    private int productId;

    @SerializedName("item")
    @DatabaseField
    private String item;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
