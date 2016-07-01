package de.gimik.app.allpresanapp.model;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "product_recommendation_product")
public class ProductRecommendationProduct {
    private static final long serialVersionUID = -1325531598075759936L;

    @DatabaseField(columnName = "id", generatedId = true)
    private Long id;

    @SerializedName("productId")
    @DatabaseField
    private Long productId;

    @SerializedName("productRecommendationId")
    @DatabaseField
    private Long productRecommendationId;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getProductRecommendationId() {
        return productRecommendationId;
    }

    public void setProductRecommendationId(Long productRecommendationId) {
        this.productRecommendationId = productRecommendationId;
    }

}
