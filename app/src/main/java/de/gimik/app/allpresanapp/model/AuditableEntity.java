package de.gimik.app.allpresanapp.model;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import de.gimik.app.allpresanapp.Utils.Utils;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Dang on 17.09.2015.
 */
@DatabaseTable(tableName = "AuditableEntity")
public class AuditableEntity implements Serializable {

    @SerializedName("id")
    @DatabaseField(generatedId = false, id = true)
    private Long id;

    @SerializedName("created_by_user")
    @DatabaseField(columnName = "created_by_user")
    private String createdByUser;

    @SerializedName("creation_time")
    @DatabaseField(columnName = "creation_time", format = "yyyy-MM-dd HH:mm:ss", dataType = DataType.DATE_STRING)
    private Date creationTime;

    @SerializedName("modified_by_user")
    @DatabaseField(columnName = "modified_by_user")
    private String modifiedByUser;

    @SerializedName("modification_time")
    @DatabaseField(columnName = "modification_time", format = "yyyy-MM-dd HH:mm:ss", dataType = DataType.DATE_STRING)
    private Date modificationTime;

    @SerializedName("active")
    @DatabaseField(columnDefinition = "TINYINT(1)  DEFAULT 1")
    private boolean active = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isNew() {
        return id == null;
    }

    public String getCreatedBy() {
        return createdByUser;
    }

    public void setCreatedBy(String createdBy) {
        this.createdByUser = createdBy;
    }

    public Date getCreatedDate() {
        return creationTime;
    }

    public void setCreatedDate(Date creationDate) {
        this.creationTime = creationDate;
    }

    public String getLastModifiedBy() {
        return modifiedByUser;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.modifiedByUser = lastModifiedBy;
    }

    public Date getLastModifiedDate() {
        return modificationTime;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.modificationTime = lastModifiedDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("ID=").append(this.getId()).append("|");

        sb.append("modifiedByUser=").append(modifiedByUser == null ? "" : modifiedByUser).append("|");
        sb.append("modificationTime=").append(modificationTime == null ? "" : modificationTime).append("|");

        return sb.toString();
    }

    public String getLocalIcon() {
        return getLocalBaseImageName(this, "icon");
    }

    public String getLocalImage() {
        return getLocalBaseImageName(this, "image");
    }

    public String getLocalLogo() {
        return getLocalBaseImageName(this, "logo");
    }

    public String getLocalGroupImage() {
        return getLocalBaseImageName(this, "group");
    }

    public static final String getLocalBaseImageName(final AuditableEntity entity, final String type) {
        if (Utils.isNullOrEmpty(type))
            return String.format("%s_%l", entity.getClass().getSimpleName(), entity.getId());
        return String.format("%s_%s_%d", entity.getClass().getSimpleName(), type, entity.getId());
    }
}
