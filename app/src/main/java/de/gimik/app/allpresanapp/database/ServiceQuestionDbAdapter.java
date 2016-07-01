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
import de.gimik.app.allpresanapp.model.ServiceQuestion;

import java.util.ArrayList;
import java.util.List;


public class ServiceQuestionDbAdapter extends DbAdapter{
    private static final String ACTIVE = "active";
    private static final String CREATION_TIME = "creation_time";
    private static final String MODIFIED_TIME = "modification_time";

    public ServiceQuestionDbAdapter(BaseActivity context, DBHelper dbHelper) {
        super(context, dbHelper);
    }

    public ServiceQuestionDbAdapter(BaseActivity context) {
        super(context);
    }

    public List<String> queryForAllName() {
    	List<String> result = null;
    	try {
        	GenericRawResults<String[]> rawResults =
        			dbHelper.getServiceQuestionDao().queryRaw("SELECT DISTINCT name FROM ServiceQuestion");
        	result = new ArrayList<String>();
        		for (String[] resultColumns : rawResults) {
        		    result.add(resultColumns[0]);
        		}
        	
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ServiceQuestionDb", "queryForAll error");
        }
        return result;
    }
    

    public List<ServiceQuestion> queryAll() {
    	try {
            return dbHelper.getServiceQuestionDao().queryBuilder()
                    .where().eq(ACTIVE, true).query();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ServiceQuestionDb", "queryForAll error");
        	return new ArrayList<ServiceQuestion>();
        }
    }
    public ServiceQuestion queryFirstRecord() {
        try {
            List<ServiceQuestion> result = dbHelper.getServiceQuestionDao().queryBuilder().orderBy(MODIFIED_TIME, false).orderBy(CREATION_TIME, false).limit(1)
                    .where().eq(ACTIVE, true).query();
            return  result!=null && result.size()>0?result.get(0):new ServiceQuestion();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ServiceQuestionDb", "queryFirstRecord error");
            Log.e("e", "queryFirstRecord error");
            return new ServiceQuestion();
        }
    }
    public ServiceQuestion queryForId(long id) {
        try {
            return dbHelper.getServiceQuestionDao().queryForId(id);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ServiceQuestionDb", "queryForId error");
        }
        return null;
    }

    public boolean saveOrUpdate(ServiceQuestion item) {
        try {
            dbHelper.getServiceQuestionDao().createOrUpdate(item);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ServiceQuestionDb", "saveOrUpdate error");
        }
        return false;
    }
    
    public boolean deleteById(long id) {
        try {
            dbHelper.getServiceQuestionDao().deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ServiceQuestionDb", "deleteById error");
        }
        return false;
    }

    public void deleteAll() {
        try {
        	dbHelper.getServiceQuestionDao().queryRaw("DELETE FROM ServiceQuestion");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ServiceQuestionDb", "deleteAll error");
        }
    }

    public boolean clServiceQuestion() {
        try {
            // remove all
            dbHelper.getServiceQuestionDao().deleteBuilder().delete();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ServiceQuestionDb", "clServiceQuestion error");
        }
        return false;
    }

    public static ServiceQuestion getDataFromCursor(Cursor c) {
        ServiceQuestion data = new ServiceQuestion();
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
            data.setLink(c.getString(c.getColumnIndex("link")));

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Ingredient", "getDataFromCursor");
        }

        return data;

    }
}
