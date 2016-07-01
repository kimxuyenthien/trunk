/**
 * Created by: Hoang
 * Created on: 2013-11-18
 */

package de.gimik.app.allpresanapp.database;

import android.util.Log;
import com.j256.ormlite.dao.GenericRawResults;
import de.gimik.app.allpresanapp.BaseActivity;
import de.gimik.app.allpresanapp.model.Member;

import java.util.ArrayList;
import java.util.List;


public class MemberDbAdapter extends DbAdapter {
    private static final String ACTIVE = "active";

    public MemberDbAdapter(BaseActivity context, DBHelper dbHelper) {
        super(context, dbHelper);
    }

    public MemberDbAdapter(BaseActivity context) {
        super(context);
    }

    public List<Member> queryForAll() {
        try {
            return dbHelper.getMemberDao().queryForAll();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MemberDbAdapter", "queryForAll error");
        }
        return new ArrayList<Member>();
    }

    public Member queryForFirst() {
        try {
            List<Member> memberList = dbHelper.getMemberDao().queryForAll();
            if (memberList != null && memberList.size() > 0)
                return memberList.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MemberDbAdapter", "queryForAll error");
        }
        return null;
    }

    public long count() {
        try {
            return dbHelper.getMemberDao().countOf();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MemberDbAdapter", "queryForAll error");
        }
        return 0;
    }

    public List<String> queryForAllName() {
        List<String> result = null;
        try {
            GenericRawResults<String[]> rawResults =
                    dbHelper.getMemberDao().queryRaw("SELECT DISTINCT id FROM Member");
            result = new ArrayList<String>();
            for (String[] resultColumns : rawResults) {
                result.add(resultColumns[0]);
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MemberDbAdapter", "queryForAllName error");
        }
        return result;
    }


    public List<Member> queryAll() {
        try {
            return dbHelper.getMemberDao().queryForAll();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MemberDbAdapter", "queryAll error");
            return new ArrayList<Member>();
        }
    }


    public Member queryForId(long id) {
        try {
            return dbHelper.getMemberDao().queryForId(id);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MemberDbAdapter", "queryForId error");
        }
        return null;
    }

    public boolean saveOrUpdate(Member item) {
        try {
            dbHelper.getMemberDao().createOrUpdate(item);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MemberDbAdapter", "queryForId error");
        }
        return false;
    }

    public boolean deleteById(long id) {
        try {
            dbHelper.getMemberDao().deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MemberDbAdapter", "deleteById error");
        }
        return false;
    }

    public void deleteAll() {
        try {
            dbHelper.getMemberDao().queryRaw("DELETE FROM Member");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MemberDbAdapter", "deleteAll error");
        }
    }

    public boolean clMember() {
        try {
            // remove all
            dbHelper.getMemberDao().deleteBuilder().delete();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MemberDbAdapter", "clMember error");
        }
        return false;
    }
}
