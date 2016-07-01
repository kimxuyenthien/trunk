/**
 * Created by: Hoang
 * Created on: 2013-11-18
 */

package de.gimik.app.allpresanapp.database;

import android.database.Cursor;
import android.util.Log;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import de.gimik.app.allpresanapp.BaseActivity;
import de.gimik.app.allpresanapp.Utils.Constant;
import de.gimik.app.allpresanapp.Utils.Utils;
import de.gimik.app.allpresanapp.model.ProductGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ProductGroupDbAdapter extends DbAdapter{
    private static final String ACTIVE = "active";
    private static final String PRODUCT_WORLD_ID = "productWorldId";

    public ProductGroupDbAdapter(BaseActivity context, DBHelper dbHelper) {
        super(context, dbHelper);
    }

    public ProductGroupDbAdapter(BaseActivity context) {
        super(context);
    }

    public List<String> queryForAllName() {
    	List<String> result = null;
    	try {
        	GenericRawResults<String[]> rawResults =
        			dbHelper.getProductGroupDao().queryRaw("SELECT DISTINCT name FROM ProductGroup");
        	result = new ArrayList<String>();
        		for (String[] resultColumns : rawResults) {
        		    result.add(resultColumns[0]);
        		}
        	
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProductGroupDbAdapter", "queryForAllName error");
        }
        return result;
    }

    public List<ProductGroup> queryAll() {
        try {
            return dbHelper.getProductGroupDao().queryBuilder()
                    .where().eq(ACTIVE, true).query();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProductGroupDbAdapter", "queryAll error");
            return new ArrayList<ProductGroup>();
        }
    }

    public List<ProductGroup> queryByProductWorldId(long productWorldId) {
        try {
            return dbHelper.getProductGroupDao().queryBuilder()
                    .where().eq(ACTIVE, true).and().eq(PRODUCT_WORLD_ID, productWorldId).query();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProductGroupDbAdapter", "queryAll error");
            return new ArrayList<ProductGroup>();
        }
    }

    public ProductGroup queryForId(long id) {
        try {
            return dbHelper.getProductGroupDao().queryForId(id);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProductGroupDbAdapter", "queryForId error");
        }
        return null;
    }

    public boolean saveOrUpdate(ProductGroup item) {
        try {
            dbHelper.getProductGroupDao().createOrUpdate(item);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProductGroupDbAdapter", "saveOrUpdate error");
        }
        return false;
    }

    public boolean deleteById(long id) {
        try {
            Dao<ProductGroup, Long> dao = dbHelper.getProductGroupDao();
            ProductGroup productGroup = dao.queryForId(id);
            if (productGroup != null) {
                File fileDelete = new File(mContext.getDownloadBasePath(), productGroup.getLocalImage());
                if (fileDelete.exists()) {
                    fileDelete.delete();
                }
                dao.deleteById(id);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProductGroupDbAdapter", "deleteById error");
        }
        return false;
    }

    public void deleteAll() {
        try {
        	dbHelper.getProductGroupDao().queryRaw("DELETE FROM product_group");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProductGroupDbAdapter", "deleteAll error");
        }
    }

    public boolean clProductGroup() {
        try {
            // remove all
            dbHelper.getProductGroupDao().deleteBuilder().delete();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProductGroupDbAdapter", "clProductGroup error");
        }
        return false;
    }

    public static ProductGroup getDataFromCursor(Cursor c) {
        ProductGroup data = new ProductGroup();
        try {

            data
                    .setId(c.getLong(c.getColumnIndex("id"))); //

            String creation_time = c.getString(c.getColumnIndex("creation_time"));
            data
                    .setCreatedDate(Utils.formatStringToDate(creation_time, Constant.DATE_FORMAT)); // date

            boolean active = c.getInt(c.getColumnIndex("active"))>0;
            data.setActive(active);
            data.setCreatedBy(c.getString(c.getColumnIndex("created_by_user")));
            String modification_time = c.getString(c.getColumnIndex("modification_time"));
            data
                    .setCreatedDate(Utils.formatStringToDate(modification_time, Constant.DATE_FORMAT)); // date
            data.setLastModifiedBy(c.getString(c.getColumnIndex("modified_by_user")));
            data.setName(c.getString(c.getColumnIndex("name")));
            data.setDescription(c.getString(c.getColumnIndex("description")));
            data.setColor(c.getString(c.getColumnIndex("color")));
            data.setImage(c.getString(c.getColumnIndex("image")));
            data.setTitle(c.getString(c.getColumnIndex("title")));
            data.setProductWorldId(c.getInt(c.getColumnIndex("productWorldId")));


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Ingredient", "getDataFromCursor");
        }

        return data;

    }
}
