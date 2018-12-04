package professional.service.corporate.com.atcleaningapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SessionManager extends SQLiteOpenHelper {
private static final int DATABASE_VERSION = 3;
//Database Name
private static final String DATABASE_NAME = "LoginDB_ATC";

private static  final String TABLE_NAME = "Login_session_ATC";
public static final String KEY_ID = "_id";
public static final String KEY_USER_ID = "user_id";
public static final String LOGIN_STATUS = "login_status";
public static final String LOGIN_EXPIRY = "login_expiry";
public SessionManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);    }

@Override
public void onCreate(SQLiteDatabase db) {
        String CREATE_H2_TABLE = "CREATE TABLE " + TABLE_NAME + "("
        + KEY_ID + " integer primary key autoincrement," + KEY_USER_ID+ " text not null,"  + LOGIN_STATUS + " INTEGER DEFAULT 0" + ")";
        db.execSQL(CREATE_H2_TABLE);
        }
@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        }
public void addUserLoginDetails(Context context, String user_id, String login_status) {
        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(KEY_USER_ID, user_cvid);
//        values.put(LOGIN_STATUS, login_status);
//       // values.put(LOGIN_EXPIRY, login_expiry);
//
//        return db.insert("Login_session", null, values);
        ContentValues initialValues = new ContentValues();
        initialValues.put("_id", 1); // the execution is different if _id is 2
        initialValues.put(KEY_USER_ID, user_id);
        initialValues.put(LOGIN_STATUS,login_status);

        int id = (int) db.insertWithOnConflict("Login_session_ATC", null, initialValues, SQLiteDatabase.CONFLICT_IGNORE);
        if (id == -1) {
        db.update("Login_session_ATC", initialValues, "_id=?", new String[] {"1"});  // number 1 is the _id here, update to variable for your code
        }
        // return true;
        }
public boolean updateUserLoginDetails(long rowId,  String login_status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(LOGIN_STATUS, login_status);


        return db.update(TABLE_NAME, args, KEY_ID + "=" + rowId, null) > 0;
        }
public boolean checkLoginStatus() {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select user_id, login_status from Login_session_ATC where login_status = 1";
        //Cursor cursor=db.query("login_metadata", null, "login_status=?", new String[]{LOGIN_STATUS}, null, null, null);
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
        cursor.close();
        return false;
        }
        cursor.moveToFirst();
        boolean status= cursor.getInt(cursor.getColumnIndex("login_status"))>0;
        cursor.close();
        return status;
        }
public boolean logout(long rowId,String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(LOGIN_STATUS, status);
        Log.i("Sourav, logout","status"+status);

        //args.put(KEY_TIME, time1);


        return db.update(TABLE_NAME, args, KEY_USER_ID + "=" + rowId, null) > 0;
        }
public List<LoginDBClass> getUserId() {

        String selectQuery = "SELECT user_id FROM " + TABLE_NAME + "";
        //where FOOD_TYPE like \"%type%\"";
        System.out.print(selectQuery);
        Log.i("Sourav, from database", "quyery" + selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<LoginDBClass> FoodList = new ArrayList<LoginDBClass>();
        if (cursor.moveToFirst()) {
        do {
        LoginDBClass list = new LoginDBClass();
//                list.setId((cursor.getString(0)));
//                Log.i("Sourav, Sl no","Sl no"+String.valueOf(cursor.getString(0)));
        list.setUser_id(cursor.getString(0));
//                Log.i("Sourav, Sl no","name"+String.valueOf(cursor.getString(1)));
//                list.setStatus(String.valueOf(cursor.getInt(2)));
//                Log.i("Sourav, Sl no","price"+String.valueOf(cursor.getString(2)));

        FoodList.add(list);
        Log.i("Sourav personal", "personalTask" + FoodList.toString());
        // list12.setrT(cursor.getString(6));
        // list12.setPrepMUp(cursor.getString(7));
        } while (cursor.moveToNext());
        } else {
        Log.i("Sourav Breakfast List", "No Data found");
        }

        return FoodList;
        }

        }