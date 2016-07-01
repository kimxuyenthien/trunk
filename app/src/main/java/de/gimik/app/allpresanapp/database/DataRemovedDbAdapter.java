package de.gimik.app.allpresanapp.database;

import android.util.Log;
import de.gimik.app.allpresanapp.BaseActivity;
import de.gimik.app.allpresanapp.model.DataRemoved;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dang on 25.05.2016.
 */
public class DataRemovedDbAdapter extends DbAdapter {
    public DataRemovedDbAdapter(BaseActivity context, DBHelper dbHelper) {
        super(context, dbHelper);
    }

    public DataRemovedDbAdapter(BaseActivity context) {
        super(context);
    }

    public List<DataRemoved> queryAll() {
        try {
            return dbHelper.getDataRemovedDbAdapter().queryBuilder().query();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProductWorldDbAdapter", "queryAll error");
        }
        return new ArrayList<DataRemoved>();
    }
}
