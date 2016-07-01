/**
 * Created by: Hoang
 * Created on: 2013-11-18
 */

package de.gimik.app.allpresanapp.database;

import android.database.Cursor;
import android.util.Log;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.QueryBuilder;
import de.gimik.app.allpresanapp.BaseActivity;
import de.gimik.app.allpresanapp.Utils.Constant;
import de.gimik.app.allpresanapp.Utils.Utils;
import de.gimik.app.allpresanapp.model.Event;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class EventDbAdapter extends DbAdapter{
    private static final String ACTIVE = "active";

    private static final String START_AT ="starts_at";

    public EventDbAdapter(BaseActivity context, DBHelper dbHelper) {
        super(context, dbHelper);
    }

    public EventDbAdapter(BaseActivity context) {
        super(context);
    }

    public List<String> queryForAllName() {
    	List<String> result = null;
    	try {
        	GenericRawResults<String[]> rawResults =
        			dbHelper.getEventDao().queryRaw("SELECT DISTINCT name FROM Event");
        	result = new ArrayList<String>();
        		for (String[] resultColumns : rawResults) {
        		    result.add(resultColumns[0]);
        		}

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("EventDbAdapter", "queryForAllName error");
        }
        return result;
    }


    public List<Event> queryAll() {
    	try {

            QueryBuilder<Event,Long> qd=dbHelper.getEventDao().queryBuilder();
            qd.where().eq(ACTIVE, true);
            qd.orderBy(START_AT, false);
            return qd.query();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("EventDbAdapter", "queryAll error");
        	return new ArrayList<Event>();
        }
    }

    public List<Event> queryForType(int type) {
        try {

            QueryBuilder<Event,Long> qd=dbHelper.getEventDao().queryBuilder();
            qd.where().eq(ACTIVE, true)
                        .and().eq("news", type);
            qd.orderBy(START_AT, false);
            return qd.query();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("EventDbAdapter", "queryAll error");
            return new ArrayList<Event>();
        }
    }

    public Event queryForId(long id) {
        try {
            return dbHelper.getEventDao().queryForId(id);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("EventDbAdapter", "queryForId error");
        }
        return null;
    }

    public boolean saveOrUpdate(Event item) {
        try {
            dbHelper.getEventDao().createOrUpdate(item);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("EventDbAdapter", "saveOrUpdate error");
        }
        return false;
    }
    
    public boolean deleteById(long id) {
        try {
            Dao<Event, Long> dao = dbHelper.getEventDao();
            Event event = dao.queryForId(id);
            if (event!=null){
                File fileDelete = new File(mContext.getDownloadBasePath(), event.getLocalImage());
                if (fileDelete.exists()) {
                    fileDelete.delete();
                }
                dao.deleteById(id);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("EventDbAdapter", "deleteById error");
        }
        return false;
    }

    public void deleteAll() {
        try {
        	dbHelper.getEventDao().queryRaw("DELETE FROM Event");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("EventDbAdapter", "deleteAll error");
        }
    }

    public boolean clEvent() {
        try {
            // remove all
            dbHelper.getEventDao().deleteBuilder().delete();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("EventDbAdapter", "clEvent error");
        }
        return false;
    }

    public static Event getDataFromCursor(Cursor c) {
        Event event = new Event();
        try {

            event
                    .setId(c.getLong(c.getColumnIndex("id"))); //

            String creation_time = c.getString(c.getColumnIndex("creation_time"));
            event
                    .setCreatedDate(Utils.formatStringToDate(creation_time, Constant.DATE_FORMAT)); // date

            boolean active = c.getInt(c.getColumnIndex("active"))>0;
            event.setActive(active);
            event.setCreatedBy(c.getString(c.getColumnIndex("created_by_user")));
            String modification_time = c.getString(c.getColumnIndex("modification_time"));
            event
                    .setCreatedDate(Utils.formatStringToDate(modification_time, Constant.DATE_FORMAT)); // date
            event.setLastModifiedBy(c.getString(c.getColumnIndex("modified_by_user")));
            event.setName(c.getString(c.getColumnIndex("name")));
            event.setDescription(c.getString(c.getColumnIndex("description")));
            event.setPlace(c.getString(c.getColumnIndex("place")));
            event.setImage(c.getString(c.getColumnIndex("image")));
            String starts_at = c.getString(c.getColumnIndex("starts_at"));
            if (starts_at != null)
                event.setStarts_at(Utils.formatStringToDate(starts_at, Constant.DATE_FORMAT)); // date

            String ends_at = c.getString(c.getColumnIndex("ends_at"));
            if (ends_at != null)
                event.setEnds_at(Utils.formatStringToDate(ends_at, Constant.DATE_FORMAT)); // date
            event.setDate(c.getString(c.getColumnIndex("date")));
            event.setPushText(c.getString(c.getColumnIndex("pushText")));
            event.setSummary(c.getString(c.getColumnIndex("summary")));
            event.setNews(c.getInt(c.getColumnIndex("news")));
            event.setPush(c.getInt(c.getColumnIndex("push")));

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("eventAdapter", "getDataFromCursor");
        }

        return event;

    }
}
