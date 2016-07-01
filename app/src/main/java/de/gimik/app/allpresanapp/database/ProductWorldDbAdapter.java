/**
 * Created by: Hoang
 * Created on: 2013-11-18
 */

package de.gimik.app.allpresanapp.database;

import android.database.Cursor;
import android.util.Log;
import com.j256.ormlite.dao.Dao;
import de.gimik.app.allpresanapp.BaseActivity;
import de.gimik.app.allpresanapp.Utils.Constant;
import de.gimik.app.allpresanapp.Utils.Utils;
import de.gimik.app.allpresanapp.model.ProductWorld;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ProductWorldDbAdapter extends DbAdapter{
    private static final String ACTIVE = "active";

    public ProductWorldDbAdapter(BaseActivity context, DBHelper dbHelper) {
        super(context, dbHelper);
    }

    public ProductWorldDbAdapter(BaseActivity context) {
        super(context);
    }

    public List<ProductWorld> queryAll() {
        try {
            return dbHelper.getProductWorldDao().queryBuilder()
                    .where().eq(ACTIVE, true).query();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProductWorldDbAdapter", "queryAll error");
            return new ArrayList<ProductWorld>();
        }
    }

    public ProductWorld queryForId(long id) {
        try {
            return dbHelper.getProductWorldDao().queryForId(id);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProductWorldDbAdapter", "queryForId error");
        }
        return null;
    }

    public boolean saveOrUpdate(ProductWorld item) {
        try {
            dbHelper.getProductWorldDao().createOrUpdate(item);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProductWorldDbAdapter", "saveOrUpdate error");
        }
        return false;
    }

    public boolean deleteById(long id) {
        try {
            Dao<ProductWorld, Long> dao = dbHelper.getProductWorldDao();
            ProductWorld productWorld = dao.queryForId(id);
            if (productWorld != null) {
                File fileDelete = new File(mContext.getDownloadBasePath(), productWorld.getLocalImage());
                if (fileDelete.exists()) {
                    fileDelete.delete();
                }
                fileDelete = new File(mContext.getDownloadBasePath(), productWorld.getLocalIcon());
                if (fileDelete.exists()) {
                    fileDelete.delete();
                }
                fileDelete = new File(mContext.getDownloadBasePath(), productWorld.getLocalGroupImage());
                if (fileDelete.exists()) {
                    fileDelete.delete();
                }
                fileDelete = new File(mContext.getDownloadBasePath(), productWorld.getLocalLogo());
                if (fileDelete.exists()) {
                    fileDelete.delete();
                }

                dao.deleteById(id);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProductWorldDbAdapter", "deleteById error");
        }
        return false;
    }

    public void deleteAll() {
        try {
            dbHelper.getProductWorldDao().queryRaw("DELETE FROM product_world");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProductWorldDbAdapter", "deleteAll error");
        }
    }

    public boolean clProductWorld() {
        try {
            // remove all
            dbHelper.getProductWorldDao().deleteBuilder().delete();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProductWorldDbAdapter", "clProductWorld error");
        }
        return false;
    }

    public static ProductWorld getDataFromCursor(Cursor c) {
        ProductWorld data = new ProductWorld();
        try {

            data
                    .setId(c.getLong(c.getColumnIndex("id"))); //

            String creation_time = c.getString(c.getColumnIndex("creation_time"));
            data
                    .setCreatedDate(Utils.formatStringToDate(creation_time, Constant.DATE_FORMAT)); // date

            boolean active = c.getInt(c.getColumnIndex("active")) > 0;
            data.setActive(active);
            data.setCreatedBy(c.getString(c.getColumnIndex("created_by_user")));
            String modification_time = c.getString(c.getColumnIndex("modification_time"));
            data
                    .setCreatedDate(Utils.formatStringToDate(modification_time, Constant.DATE_FORMAT)); // date
            data.setLastModifiedBy(c.getString(c.getColumnIndex("modified_by_user")));
            data.setName(c.getString(c.getColumnIndex("name")));
            data.setImage(c.getString(c.getColumnIndex("image")));
            data.setIcon(c.getString(c.getColumnIndex("icon")));
            data.setProductGroupTitle(c.getString(c.getColumnIndex("productGroupTitle")));
            data.setProductGroupImage(c.getString(c.getColumnIndex("productGroupImage")));
            data.setLogo(c.getString(c.getColumnIndex("logo")));
            data.setProductGroupSubTitle(c.getString(c.getColumnIndex("productGroupSubTitle")));

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Ingredient", "getDataFromCursor");
        }

        return data;

    }
}
