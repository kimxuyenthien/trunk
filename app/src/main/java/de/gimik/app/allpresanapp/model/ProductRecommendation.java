package de.gimik.app.allpresanapp.model;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Dang on 22.09.2015.
 */
@DatabaseTable(tableName = "product_recommendation")
public class ProductRecommendation extends AuditableEntity {
    @SerializedName("name")
    @DatabaseField
    private String name;

    @SerializedName("image")
    @DatabaseField
    private String image;


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
}
