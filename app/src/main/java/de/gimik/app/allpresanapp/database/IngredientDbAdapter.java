/**
 * Created by: Hoang
 * Created on: 2013-11-18
 */

package de.gimik.app.allpresanapp.database;

import android.database.Cursor;
import android.util.Log;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RawRowMapper;
import de.gimik.app.allpresanapp.BaseActivity;
import de.gimik.app.allpresanapp.Utils.Constant;
import de.gimik.app.allpresanapp.Utils.Utils;
import de.gimik.app.allpresanapp.model.Ingredient;

import java.util.ArrayList;
import java.util.List;


public class IngredientDbAdapter extends DbAdapter {
    private static final String ACTIVE = "active";
    private static final String NAME = "name";

    public IngredientDbAdapter(BaseActivity context, DBHelper dbHelper) {
        super(context, dbHelper);
    }

    public IngredientDbAdapter(BaseActivity context) {
        super(context);
    }

    public List<Ingredient> queryAll() {
        List<Ingredient> list = new ArrayList();
        try {
            String sql = "SELECT Ingredient.* FROM Ingredient " +
                    " WHERE Ingredient.active=1" +
                    " ORDER BY Ingredient.name COLLATE NOCASE; ";
            Dao<Ingredient, Long> dao = dbHelper.getIngredientDao();
            RawRowMapper<Ingredient> rawRowMapper = dao.getRawRowMapper();
            GenericRawResults<Ingredient> result = dao.queryRaw(sql, rawRowMapper, new String[]{});
            for (Ingredient ingredient : result)
                list.add(ingredient);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Ingredient> queryAll(String name) {
        List<Ingredient> list = new ArrayList();
        try {
            String sql = "SELECT Ingredient.* FROM Ingredient " +
                    " WHERE Ingredient.active=1 AND Ingredient.name LIKE '%" + name + "%'" +
                    " ORDER BY Ingredient.name COLLATE NOCASE; ";
            Dao<Ingredient, Long> dao = dbHelper.getIngredientDao();
            RawRowMapper<Ingredient> rawRowMapper = dao.getRawRowMapper();
            GenericRawResults<Ingredient> result = dao.queryRaw(sql, rawRowMapper, new String[]{});
            for (Ingredient ingredient : result)
                list.add(ingredient);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> queryForAllName() {
        List<String> result = null;
        try {
            GenericRawResults<String[]> rawResults =
                    dbHelper.getIngredientDao().queryRaw("SELECT DISTINCT name FROM Ingredient");
            result = new ArrayList();
            for (String[] resultColumns : rawResults) {
                result.add(resultColumns[0]);
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("IngredientDbAdapter", "queryForAllName error");
        }
        return result;
    }

    public List<Ingredient> queryByProductId(long productId) {
        List<Ingredient> result = null;
        try {
            GenericRawResults<String[]> rawResults =
                    dbHelper.getProductRecommendationDao().queryRaw("SELECT DISTINCT Ingredient.id, name, description FROM Ingredient " +
                            "  INNER JOIN product_ingredient ON product_ingredient.ingredientId=Ingredient.id WHERE product_ingredient.productId=" + productId + " ORDER BY name  ");
            result = new ArrayList<Ingredient>();
            for (String[] resultColumns : rawResults) {
                Ingredient ingredient = new Ingredient();
                ingredient.setId(Long.parseLong(resultColumns[0]));
                ingredient.setName(resultColumns[1]);
                ingredient.setDescription(resultColumns[2]);
                result.add(ingredient);
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProductDbAdapter", "queryByProductRecommend error");
        }
        return result;
    }

    public Ingredient queryForId(long id) {
        try {
            return dbHelper.getIngredientDao().queryForId(id);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("IngredientDbAdapter", "queryForId error");
        }
        return null;
    }

    public boolean saveOrUpdate(Ingredient item) {
        try {
            dbHelper.getIngredientDao().createOrUpdate(item);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("IngredientDbAdapter", "saveOrUpdate error");
        }
        return false;
    }

    public boolean deleteById(long id) {
        try {
            dbHelper.getIngredientDao().deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("IngredientDbAdapter", "deleteById error");
        }
        return false;
    }

    public void deleteAll() {
        try {
            dbHelper.getIngredientDao().queryRaw("DELETE FROM Ingredient");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("IngredientDbAdapter", "deleteAll error");
        }
    }

    public boolean clIngredient() {
        try {
            // remove all
            dbHelper.getIngredientDao().deleteBuilder().delete();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("IngredientDbAdapter", "clIngredient error");
        }
        return false;
    }

    public static Ingredient getDataFromCursor(Cursor c) {
        Ingredient data = new Ingredient();
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

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Ingredient", "getDataFromCursor");
        }

        return data;

    }
}
