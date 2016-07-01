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
import de.gimik.app.allpresanapp.model.Product;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ProductDbAdapter extends DbAdapter{
    private static final String ACTIVE = "active";
    private static final String PRODUCT_GROUP_ID = "productGroupId";

    public ProductDbAdapter(BaseActivity context, DBHelper dbHelper) {
        super(context, dbHelper);
    }

    public ProductDbAdapter(BaseActivity context) {
        super(context);
    }

    public List<Product> queryByProductRecommend(long productRecommendId) {
        List<Product> result = null;
        try {
            GenericRawResults<String[]> rawResults =
                    dbHelper.getProductRecommendationDao().queryRaw("SELECT PRODUCT.id, PRODUCT.name FROM PRODUCT " +
                            "  INNER JOIN product_recommendation_product ON product_recommendation_product.productId=PRODUCT.id" +
                            " AND product_recommendation_product.productRecommendationId=" + productRecommendId);
            result = new ArrayList<Product>();
            for (String[] resultColumns : rawResults) {
                Product product = new Product();
                product.setId(Long.parseLong(resultColumns[0]));
                product.setName(resultColumns[1]);
                result.add(product);
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProductDbAdapter", "queryByProductRecommend error");
        }
        return result;
    }

    public List<Product> queryByProductGroupdId(long productGroupId) {
        try {
            return dbHelper.getProductDao().queryBuilder()
                    .where().eq(ACTIVE, true).and().eq(PRODUCT_GROUP_ID, productGroupId).query();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProductDbAdapter", "queryAll error");
            return new ArrayList<Product>();
        }
    }

    public List<String> queryForAllName() {
        List<String> result = null;
        try {
            GenericRawResults<String[]> rawResults =
                    dbHelper.getProductDao().queryRaw("SELECT DISTINCT name FROM Product");
            result = new ArrayList<String>();
            for (String[] resultColumns : rawResults) {
                result.add(resultColumns[0]);
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProductDbAdapter", "queryForAllName error");
        }
        return result;
    }

    public List<Product> queryAll() {
        try {
            return dbHelper.getProductDao().queryBuilder()
                    .where().eq(ACTIVE, true).query();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProductDbAdapter", "queryAll error");
            return new ArrayList<Product>();
        }
    }

    public Product queryForId(long id) {
        try {
            return dbHelper.getProductDao().queryForId(id);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProductDbAdapter", "queryAll error");
        }
        return null;
    }

    public boolean saveOrUpdate(Product item) {
        try {
            dbHelper.getProductDao().createOrUpdate(item);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProductDbAdapter", "saveOrUpdate error");
        }
        return false;
    }

    public boolean deleteById(long id) {
        try {
            Dao<Product, Long> dao = dbHelper.getProductDao();
            Product product = dao.queryForId(id);
            if (product != null) {
                File fileDelete = new File(mContext.getDownloadBasePath(), product.getLocalImage());
                if (fileDelete.exists()) {
                    fileDelete.delete();
                }
                fileDelete = new File(mContext.getDownloadBasePath(), product.getIcon());
                if (fileDelete.exists()) {
                    fileDelete.delete();
                }
            }

            dbHelper.getProductDao().deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProductDbAdapter", "deleteById error");
        }
        return false;
    }

    public void deleteAll() {
        try {
            dbHelper.getProductDao().queryRaw("DELETE FROM Product");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProductDbAdapter", "deleteAll error");
        }
    }

    public boolean clProduct() {
        try {
            // remove all
            dbHelper.getProductDao().deleteBuilder().delete();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProductDbAdapter", "deleteAll error");
        }
        return false;
    }

    public static Product getDataFromCursor(Cursor c) {
        Product data = new Product();
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
            data.setDescription(c.getString(c.getColumnIndex("description")));
            data.setComposition(c.getString(c.getColumnIndex("composition")));
            data.setImage(c.getString(c.getColumnIndex("image")));
            data.setLabel(c.getString(c.getColumnIndex("label")));
            data.setTitle(c.getString(c.getColumnIndex("title")));
            data.setTitle2(c.getString(c.getColumnIndex("title2")));
            data
                    .setProductGroupId(c.getInt(c.getColumnIndex("productGroupId")));
            data
                    .setIcon(c.getString(c.getColumnIndex("icon")));
            boolean technology = c.getInt(c.getColumnIndex("technology")) == 1;
            data.setTechnology(technology);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Ingredient", "getDataFromCursor");
        }

        return data;

    }
}
