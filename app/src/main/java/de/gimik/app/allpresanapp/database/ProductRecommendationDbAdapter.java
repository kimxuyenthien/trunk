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
import de.gimik.app.allpresanapp.model.ProductRecommendation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ProductRecommendationDbAdapter extends DbAdapter{
    private static final String ACTIVE = "active";

    public ProductRecommendationDbAdapter(BaseActivity context, DBHelper dbHelper) {
        super(context, dbHelper);
    }

    public ProductRecommendationDbAdapter(BaseActivity context) {
        super(context);
    }

    public List<ProductRecommendation> queryForAll() {
        try {
            return dbHelper.getProductRecommendationDao().queryForAll();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProductRecommendation", "queryForAll error");
        }
        return new ArrayList<ProductRecommendation>();
    }

    public List<String> queryForAllName() {
        List<String> result = null;
        try {
            GenericRawResults<String[]> rawResults =
                    dbHelper.getProductRecommendationDao().queryRaw("SELECT DISTINCT name FROM ProductRecommendation");
            result = new ArrayList<String>();
            for (String[] resultColumns : rawResults) {
                result.add(resultColumns[0]);
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProductRecommendation", "queryForAll error");
        }
        return result;
    }


    public List<ProductRecommendation> queryAll() {
        try {
            return dbHelper.getProductRecommendationDao().queryBuilder()
                    .where().eq(ACTIVE, true).query();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProductRecommendation", "queryAll error");
            return new ArrayList<ProductRecommendation>();
        }
    }

    public ProductRecommendation queryForId(long id) {
        try {
            return dbHelper.getProductRecommendationDao().queryForId(id);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProductRecommendation", "queryForId error");
        }
        return null;
    }

    public boolean saveOrUpdate(ProductRecommendation item) {
        try {
            dbHelper.getProductRecommendationDao().createOrUpdate(item);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProductRecommendation", "saveOrUpdate error");
        }
        return false;
    }

    public boolean deleteById(long id) {
        try {
            Dao<ProductRecommendation, Long> dao = dbHelper.getProductRecommendationDao();
            ProductRecommendation productRecommendation = dao.queryForId(id);
            if (productRecommendation != null) {
                File fileDelete = new File(mContext.getDownloadBasePath(), productRecommendation.getImage());
                if (fileDelete.exists()) {
                    fileDelete.delete();
                }

                dao.deleteById(id);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProductRecommendation", "deleteById error");
        }
        return false;
    }

    public void deleteAll() {
        try {
            dbHelper.getProductRecommendationDao().queryRaw("DELETE FROM ProductRecommendation");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProductRecommendation", "deleteAll error");
        }
    }

    public boolean clProductRecommendation() {
        try {
            // remove all
            dbHelper.getProductRecommendationDao().deleteBuilder().delete();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProductRecommendation", "deleteAll error");
        }
        return false;
    }

    public static ProductRecommendation getDataFromCursor(Cursor c) {
        ProductRecommendation data = new ProductRecommendation();
        try {

            data
                    .setId(c.getLong(c.getColumnIndex("id"))); //

            try {
                String creation_time = c.getString(c.getColumnIndex("creation_time"));
                data
                        .setCreatedDate(Utils.formatStringToDate(creation_time, Constant.DATE_FORMAT)); // date
            } catch (Exception e) {
            }

            boolean active = c.getInt(c.getColumnIndex("active")) > 0;
            data.setActive(active);
            data.setCreatedBy(c.getString(c.getColumnIndex("created_by_user")));
            try {
                String modification_time = c.getString(c.getColumnIndex("modification_time"));
                data
                        .setCreatedDate(Utils.formatStringToDate(modification_time, Constant.DATE_FORMAT)); // date
            } catch (Exception e) {
            }

            data.setLastModifiedBy(c.getString(c.getColumnIndex("modified_by_user")));
            data.setName(c.getString(c.getColumnIndex("name")));
            data.setImage(c.getString(c.getColumnIndex("image")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;

    }
}
