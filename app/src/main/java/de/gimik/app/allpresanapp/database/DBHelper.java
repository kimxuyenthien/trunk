/**
 * Created by: QUYEN
 * Created on: 2013-11-18
 */

package de.gimik.app.allpresanapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import de.gimik.app.allpresanapp.model.*;

import java.sql.SQLException;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {
    public String TAG = DBHelper.class.getSimpleName();
    public static final int version = '1';
    protected Dao<Ean, Long> eanDao;
    protected Dao<Event, Long> eventDao;
    protected Dao<Ingredient, Long> ingredientDao;
    protected Dao<Member, Long> memberDao;
    protected Dao<Product, Long> productDao;
    protected Dao<ProductWorld, Long> productWorldDao;
    protected Dao<ProductGroup, Long> productGroupDao;
    protected Dao<ProductRecommendation, Long> productRecommendationDao;
    protected Dao<ProductRecommendationProduct, Long> productRecommendationProductDao;
    protected Dao<ServiceQuestion, Long> serviceQuestionDao;
    protected Dao<ProductIngredient, Long> productIngredientDao;
    protected Dao<DataRemoved, Long> dataRemovedDao;

    private static DBHelper helper = null;

    public DBHelper(Context context) {
        super(context, getDataBaseName(), null, version);
    }

    public DBHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
    }

    public static String getDataBaseName() {
        return "allpresan.sqlite";
    }

    public static synchronized DBHelper getHelper(Context context) {
        if (helper == null) {
            helper = new DBHelper(context);
        }
        return helper;
    }

    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(TAG, TAG + " onCreate");
            //NOTE: as we always copy db from the assets folder, the below code should not be necessary, just keep it for safe
            // however if we ever forget to add table in the db file in assets, we can't get existing data from there
            TableUtils.createTableIfNotExists(connectionSource, AuditableEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, Ean.class);
            TableUtils.createTableIfNotExists(connectionSource, Ean.class);
            TableUtils.createTableIfNotExists(connectionSource, Event.class);
            TableUtils.createTableIfNotExists(connectionSource, Ingredient.class);
            TableUtils.createTableIfNotExists(connectionSource, Member.class);
            TableUtils.createTableIfNotExists(connectionSource, Product.class);
            TableUtils.createTableIfNotExists(connectionSource, ProductRecommendationProduct.class);
            TableUtils.createTableIfNotExists(connectionSource, ProductWorld.class);
            TableUtils.createTableIfNotExists(connectionSource, ProductGroup.class);
            TableUtils.createTableIfNotExists(connectionSource, ProductRecommendation.class);
            TableUtils.createTableIfNotExists(connectionSource, ServiceQuestion.class);
            TableUtils.createTableIfNotExists(connectionSource, ProductIngredient.class);
        } catch (SQLException e) {
            Log.e(TAG, "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        Log.i(TAG, TAG + " onUpgrade, version from " + oldVersion + " to " + newVersion);
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
    }

    public Dao<Ean, Long> getEanDao() {
        if (eanDao == null) {
            try {
                eanDao = getDao(Ean.class);
            } catch (SQLException e) {
                Log.e(TAG, "Can't create eanDao", e);
            }
        }
        return eanDao;
    }

    public Dao<Event, Long> getEventDao() {
        if (eventDao == null) {
            try {
                eventDao = getDao(Event.class);
            } catch (SQLException e) {
                Log.e(TAG, "Can't create eventDao", e);
            }
        }
        return eventDao;
    }

    public Dao<Ingredient, Long> getIngredientDao() {
        if (ingredientDao == null) {
            try {
                ingredientDao = getDao(Ingredient.class);
            } catch (SQLException e) {
                Log.e(TAG, "Can't create ingredientDao", e);
            }
        }
        return ingredientDao;
    }

    public Dao<ProductIngredient, Long> getProductIngredientDao() {
        if (productIngredientDao == null) {
            try {
                productIngredientDao = getDao(ProductIngredient.class);
            } catch (SQLException e) {
                Log.e(TAG, "Can't create productIngredientDao", e);
            }
        }
        return productIngredientDao;
    }

    public Dao<Member, Long> getMemberDao() {
        if (memberDao == null) {
            try {
                memberDao = getDao(Member.class);
            } catch (SQLException e) {
                Log.e(TAG, "Can't create ingredientDao", e);
            }
        }
        return memberDao;
    }


    public Dao<Product, Long> getProductDao() {
        if (productDao == null) {
            try {
                productDao = getDao(Product.class);
            } catch (SQLException e) {
                Log.e(TAG, "Can't create productDao", e);
            }
        }
        return productDao;
    }

    public Dao<ProductGroup, Long> getProductGroupDao() {
        if (productGroupDao == null) {
            try {
                productGroupDao = getDao(ProductGroup.class);
            } catch (SQLException e) {
                Log.e(TAG, "Can't create productGroupDao", e);
            }
        }
        return productGroupDao;
    }

    public Dao<ProductRecommendation, Long> getProductRecommendationDao() {
        if (productRecommendationDao == null) {
            try {
                productRecommendationDao = getDao(ProductRecommendation.class);
            } catch (SQLException e) {
                Log.e(TAG, "Can't create productRecommendationDao", e);
            }
        }
        return productRecommendationDao;
    }

    public Dao<ProductRecommendationProduct, Long> getProductRecommendationProductDao() {
        if (productRecommendationProductDao == null) {
            try {
                productRecommendationProductDao = getDao(ProductRecommendationProduct.class);
            } catch (SQLException e) {
                Log.e(TAG, "Can't create ProductRecommendationProduct", e);
            }
        }
        return productRecommendationProductDao;
    }

    public Dao<ProductWorld, Long> getProductWorldDao() {
        if (productWorldDao == null) {
            try {
                productWorldDao = getDao(ProductWorld.class);
            } catch (SQLException e) {
                Log.e(TAG, "Can't create productWorldDao", e);
            }
        }
        return productWorldDao;
    }

    public Dao<ServiceQuestion, Long> getServiceQuestionDao() {
        if (serviceQuestionDao == null) {
            try {
                serviceQuestionDao = getDao(ServiceQuestion.class);
            } catch (SQLException e) {
                Log.e(TAG, "Can't create serviceQuestionDao", e);
            }
        }
        return serviceQuestionDao;
    }

    public Dao<DataRemoved, Long> getDataRemovedDbAdapter() {
        if (dataRemovedDao == null) {
            try {
                dataRemovedDao = getDao(DataRemoved.class);
            } catch (SQLException e) {
                Log.e(TAG, "Can't create DataRemoved", e);
            }
        }
        return dataRemovedDao;
    }
}
