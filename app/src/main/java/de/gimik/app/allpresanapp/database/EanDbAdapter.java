/**
 * Created by: Hoang
 * Created on: 2013-11-18
 */

package de.gimik.app.allpresanapp.database;

import android.database.Cursor;
import android.util.Log;
import com.j256.ormlite.dao.GenericRawResults;
import de.gimik.app.allpresanapp.BaseActivity;
import de.gimik.app.allpresanapp.Utils.Constant;
import de.gimik.app.allpresanapp.Utils.Utils;
import de.gimik.app.allpresanapp.model.Ean;
import de.gimik.app.allpresanapp.model.Product;

import java.util.ArrayList;
import java.util.List;


public class EanDbAdapter extends DbAdapter{
    private static final String ACTIVE = "active";

    public EanDbAdapter(BaseActivity context, DBHelper dbHelper) {
        super(context, dbHelper);
    }

    public EanDbAdapter(BaseActivity context) {
        super(context);
    }


    public List<Ean> queryAll() {
        try {

            return dbHelper.getEanDao().queryBuilder()
                    .where().eq(ACTIVE, true).query();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("EanDbAdapter", "queryForAll error");
        }
        return new ArrayList<Ean>();
    }

    public List<String> queryForAllName() {
    	List<String> result = null;
    	try {
        	GenericRawResults<String[]> rawResults =
        			dbHelper.getEanDao().queryRaw("SELECT DISTINCT name FROM Ean");
        	result = new ArrayList<String>();
        		for (String[] resultColumns : rawResults) {
        		    result.add(resultColumns[0]);
        		}
        	
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("EanDbAdapter", "queryForAllName error");
        }
        return result;
    }


    public Ean queryForId(long id) {
        try {
            return dbHelper.getEanDao().queryForId(id);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("EanDbAdapter", "queryForId error");
        }
        return null;
    }

    public boolean saveOrUpdate(Ean item) {
        try {
            dbHelper.getEanDao().createOrUpdate(item);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("EanDbAdapter", "saveOrUpdate error");
        }
        return false;
    }
    
    public boolean deleteById(long id) {
        try {
            dbHelper.getEanDao().deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("EanDbAdapter", "deleteById error");
        }
        return false;
    }

    public void deleteAll() {
        try {
        	dbHelper.getEanDao().queryRaw("DELETE FROM Ean");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("EanDbAdapter", "deleteAll error");
        }
    }

    public boolean clean() {
        try {
            // remove all
            dbHelper.getEanDao().deleteBuilder().delete();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("EanDbAdapter", "clean error");
        }
        return false;
    }

    public List<Ean> queryByProductId(long productId) {
        try {
            return dbHelper.getEanDao().queryBuilder()
                    .where().eq("productId", productId).query();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("EanDbAdapter", "queryAll by product ID error");
            return new ArrayList<Ean>();
        }
    }

    public List<Ean> queryByName (String name) {
        try {
            return dbHelper.getEanDao().queryBuilder()
                    .where().eq(ACTIVE, true).and().eq("name", name).query();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("EanDbAdapter", "queryByName error");
            return new ArrayList<Ean>();
        }
    }


    public static Ean getDataFromCursor(Cursor c) {
        Ean ean = new Ean();
        try {

            ean
                    .setId(c.getLong(c.getColumnIndex("id"))); //

            String creation_time = c.getString(c.getColumnIndex("creation_time"));
            ean
                    .setCreatedDate(Utils.formatStringToDate(creation_time, Constant.DATE_FORMAT)); // date

            boolean active = c.getInt(c.getColumnIndex(ACTIVE))>0;
            ean.setActive(active);
            ean.setCreatedBy(c.getString(c.getColumnIndex("created_by_user")));
            String modification_time = c.getString(c.getColumnIndex("modification_time"));
            ean
                    .setCreatedDate(Utils.formatStringToDate(modification_time, Constant.DATE_FORMAT)); // date
            ean.setLastModifiedBy(c.getString(c.getColumnIndex("modified_by_user")));
            ean.setName(c.getString(c.getColumnIndex("name")));
            ean.setSize(c.getString(c.getColumnIndex("size")));
            ean.setItem(c.getString(c.getColumnIndex("item")));
            ean.setProductId(c.getInt(c.getColumnIndex("productId")));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("EanDbAdapter", "getDataFromCursor");
        }

        return ean;

    }

}
