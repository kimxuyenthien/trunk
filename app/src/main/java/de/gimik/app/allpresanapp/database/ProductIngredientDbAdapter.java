/**
 * Created by: Hoang
 * Created on: 2013-11-18
 */

package de.gimik.app.allpresanapp.database;

import android.database.Cursor;
import android.util.Log;
import de.gimik.app.allpresanapp.BaseActivity;
import de.gimik.app.allpresanapp.model.ProductIngredient;

import java.util.ArrayList;
import java.util.List;


public class ProductIngredientDbAdapter extends DbAdapter{

    private static final String ACTIVE = "active";
    private static final String NAME="name";

    public ProductIngredientDbAdapter(BaseActivity context, DBHelper dbHelper) {
        super(context, dbHelper);
    }

    public ProductIngredientDbAdapter(BaseActivity context) {
        super(context);
    }

    public List<ProductIngredient> queryAll() {
        try {
            return dbHelper.getProductIngredientDao().queryForAll();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProductIngredientDb", "queryAll error");
            return new ArrayList<ProductIngredient>();
        }
    }



    public ProductIngredient queryForId(long id) {
        try {
            return dbHelper.getProductIngredientDao().queryForId(id);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProductIngredientDb", "queryForId error");
        }
        return null;
    }

    public boolean saveOrUpdate(ProductIngredient item) {
        try {
            dbHelper.getProductIngredientDao().createOrUpdate(item);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProductIngredientDb", "saveOrUpdate error");
        }
        return false;
    }
    
    public boolean deleteById(long id) {
        try {
            dbHelper.getProductIngredientDao().deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProductIngredientDb", "deleteById error");
        }
        return false;
    }

    public void deleteAll() {
        try {
        	dbHelper.getProductIngredientDao().queryRaw("DELETE FROM product_ingredient");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("IngredientDbAdapter", "deleteAll error");
        }
    }

    public boolean clIngredient() {
        try {
            // remove all
            dbHelper.getProductIngredientDao().deleteBuilder().delete();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProductIngredientDB", "clIngredient error");
        }
        return false;
    }

    public static ProductIngredient getDataFromCursor(Cursor c) {
        ProductIngredient data = new ProductIngredient();
        try {
            data.setProductId(c.getLong(c.getColumnIndex("productId"))); //
            data.setIngredientId(c.getLong(c.getColumnIndex("ingredientId"))); //

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProductIngredientDB", "getDataFromCursor");
        }

        return data;

    }
}
