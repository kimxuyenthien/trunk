package de.gimik.app.allpresanapp.model;

import android.database.Cursor;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "data_removed")
public class DataRemoved extends AuditableEntity {
    private static final long serialVersionUID = -1325531598075759936L;

    @SerializedName("data_id")
    @DatabaseField
    private Long data_id;

    @SerializedName("table_name")
    @DatabaseField
    private String table_name;

    public Long getData_id() {
        return data_id;
    }

    public void setData_id(Long data_id) {
        this.data_id = data_id;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }
    public static DataRemoved getDataFromCursor(Cursor c) {
        DataRemoved data = new DataRemoved();
        try {
            data.setTable_name(c.getString(c.getColumnIndex("table_name"))); //
            data.setData_id(c.getLong(c.getColumnIndex("data_id"))); //

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProductIngredientDB", "getDataFromCursor");
        }

        return data;

    }
}
