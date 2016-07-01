package de.gimik.app.allpresanapp.model;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Dang on 22.09.2015.
 */
@DatabaseTable(tableName = "service_question")
public class ServiceQuestion extends AuditableEntity {
    @SerializedName("link")
    @DatabaseField
    private String link;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
