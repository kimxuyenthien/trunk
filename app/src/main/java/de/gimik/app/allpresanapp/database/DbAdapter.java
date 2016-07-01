package de.gimik.app.allpresanapp.database;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import de.gimik.app.allpresanapp.BaseActivity;

/**
 * Created by Dang on 25.05.2016.
 */
public class DbAdapter {
    protected BaseActivity mContext;
    protected DBHelper dbHelper;


    public DbAdapter(BaseActivity context, DBHelper dbHelper) {
        this.dbHelper = dbHelper;
        mContext = context;
    }

    public DbAdapter(BaseActivity context) {
        dbHelper = OpenHelperManager.getHelper(context, DBHelper.class);
        mContext = context;
    }
}
