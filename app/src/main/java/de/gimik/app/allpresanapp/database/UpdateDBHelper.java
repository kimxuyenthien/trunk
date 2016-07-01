package de.gimik.app.allpresanapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Dang on 25.05.2016.
 */
public class UpdateDBHelper extends DBHelper {
    private static UpdateDBHelper helper = null;

    public UpdateDBHelper(Context context) {
        super(context, getDataBaseName(), null, version);
    }

    public static String getDataBaseName() {
        return "allpresan_update.sqlite";
    }

    public static synchronized UpdateDBHelper getHelper(Context context) {
        if (helper == null) {
            helper = new UpdateDBHelper(context);
        }
        return helper;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        try {
            db.execSQL("ALTER TABLE product_recommendation_product ADD COLUMN id INTEGER");
        } catch (Exception e) {
        }
        try {
            db.execSQL("ALTER TABLE product_ingredient ADD COLUMN id INTEGER");
        } catch (Exception e) {
        }
    }
}
