package de.gimik.app.allpresanapp.model;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "product_ingredient")
public class ProductIngredient{
    private static final long serialVersionUID = -1325531598075759936L;
    @DatabaseField(columnName = "id", generatedId = true)
    private Long id;

    @SerializedName("productId")
    @DatabaseField
    private long productId;

    @SerializedName("ingredientId")
    @DatabaseField
    private long ingredientId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(long ingredientId) {
        this.ingredientId = ingredientId;
    }
}
