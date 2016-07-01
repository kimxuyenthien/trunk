package de.gimik.app.allpresanapp.model;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Ingredient")
public class Ingredient extends AuditableEntity {
    private static final long serialVersionUID = -1325531598075759936L;

    @SerializedName("name")
    @DatabaseField
    private String name;

    @SerializedName("description")
    @DatabaseField
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
