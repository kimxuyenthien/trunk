/**
 * Created by: Hoang
 * Created on: 2013-11-18
 */

package de.gimik.app.allpresanapp.database;

import android.database.Cursor;
import android.util.Log;
import com.j256.ormlite.dao.GenericRawResults;
import de.gimik.app.allpresanapp.BaseActivity;
import de.gimik.app.allpresanapp.model.ProductRecommendationProduct;

import java.util.ArrayList;
import java.util.List;


public class ProductRecommendationProductDbAdapter extends DbAdapter{

    public ProductRecommendationProductDbAdapter(BaseActivity context, DBHelper dbHelper) {
        super(context, dbHelper);
    }

    public ProductRecommendationProductDbAdapter(BaseActivity context) {
        super(context);
    }

    public List<String> queryForAllName() {
    	List<String> result = null;
    	try {
        	GenericRawResults<String[]> rawResults =
        			dbHelper.getProductRecommendationProductDao().queryRaw("SELECT DISTINCT name FROM ProductRecommendationProduct");
        	result = new ArrayList<String>();
        		for (String[] resultColumns : rawResults) {
        		    result.add(resultColumns[0]);
        		}
        	
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PRPDbAdapter", "queryForAll error");
        }
        return result;
    }
    

    public List<ProductRecommendationProduct> queryAll() {
    	try {
        	return dbHelper.getProductRecommendationProductDao().queryForAll();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PRPDbAdapter", "queryAll error");
        	return new ArrayList<ProductRecommendationProduct>();
        }
    }

    public ProductRecommendationProduct queryForId(long id) {
        try {
            return dbHelper.getProductRecommendationProductDao().queryForId(id);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PRPDbAdapter", "queryForId error");
        }
        return null;
    }

    public boolean saveOrUpdate(ProductRecommendationProduct item) {
        try {
            dbHelper.getProductRecommendationProductDao().createOrUpdate(item);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PRPDbAdapter", "saveOrUpdate error");
        }
        return false;
    }
    
    public boolean deleteById(long id) {
        try {
            dbHelper.getProductRecommendationProductDao().deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PRPDbAdapter", "deleteById error");
        }
        return false;
    }

    public void deleteAll() {
        try {
        	dbHelper.getProductRecommendationProductDao().queryRaw("DELETE FROM ProductRecommendationProduct");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PRPDbAdapter", "deleteAll error");
        }
    }

    public boolean clProductRecommendationProduct() {
        try {
            // remove all
            dbHelper.getProductRecommendationProductDao().deleteBuilder().delete();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PRPDbAdapter", "deleteAll error");
        }
        return false;
    }
    public static ProductRecommendationProduct getDataFromCursor(Cursor c) {
        ProductRecommendationProduct data = new ProductRecommendationProduct();
        try {

            data
                    .setProductId(c.getLong(c.getColumnIndex("productId"))); //
            data
                    .setProductRecommendationId(c.getLong(c.getColumnIndex("productRecommendationId"))); //

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Ingredient", "getDataFromCursor");
        }

        return data;

    }
}
