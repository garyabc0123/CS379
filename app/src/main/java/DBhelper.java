import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBhelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "db";


    private static final String CATALOGDATABASE_DATABASENAME = "catalogTable";
    private static final String CATALOGDATABASE_CATALOGNAME = "catalogName";
    private static final String CATALOGDATABASE_USERNAME = "userName";
    private static final String CATALOGDATABASE_COLOR_R = "r";
    private static final String CATALOGDATABASE_COLOR_G = "g";
    private static final String CATALOGDATABASE_COLOR_B = "b";

    public DBhelper (Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase database){



        String command = "CREATE TABLE " + CATALOGDATABASE_DATABASENAME + " (" +
                CATALOGDATABASE_CATALOGNAME + "TEXT, " +
                CATALOGDATABASE_USERNAME + " TEXT, " +
                CATALOGDATABASE_COLOR_R + "INTEGER, " +
                CATALOGDATABASE_COLOR_G + "INTEGER, " +
                CATALOGDATABASE_COLOR_B + "INTEGER " +
                ")";
        database.execSQL(command);

    }

    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){

        database.execSQL("DROP TABLE IF EXISTS " + CATALOGDATABASE_DATABASENAME);
        onCreate(database);
    }

    public List<CatalogClass> getUserAllCatalog(String inputName){
        SQLiteDatabase db = this.getReadableDatabase();
        String command = "SELECT * from " + CATALOGDATABASE_DATABASENAME + " where " + CATALOGDATABASE_USERNAME + " = '" + inputName + "'";
        Cursor cursor = db.rawQuery(command,null);
        ArrayList<CatalogClass> ret = new ArrayList<>();
        while (cursor.moveToNext()){
            CatalogClass temp = new CatalogClass();
            temp.name = cursor.getString(0);
            temp.userName = cursor.getString(1);
            temp.r = cursor.getInt(2);
            temp.g = cursor.getInt(3);
            temp.b = cursor.getInt(4);
            ret.add(temp);
        }
        db.close();
        return ret;
    }
    public void addCatalog(CatalogClass input){
        SQLiteDatabase db = this.getWritableDatabase();
        String command = "SELECT * from " + CATALOGDATABASE_DATABASENAME + "where " + CATALOGDATABASE_USERNAME + " = '" + input.userName + "' AND " + CATALOGDATABASE_CATALOGNAME + " = '" + input.name +"'";
        Cursor cursor = db.rawQuery(command,null);
        if(cursor.getCount() != 0){
            return;
        }
        ContentValues val = new ContentValues();
        val.put(CATALOGDATABASE_CATALOGNAME,input.name);
        val.put(CATALOGDATABASE_USERNAME,input.userName);
        val.put(CATALOGDATABASE_COLOR_R,input.r);
        val.put(CATALOGDATABASE_COLOR_G,input.g);
        val.put(CATALOGDATABASE_COLOR_B,input.b);
        db.insert(CATALOGDATABASE_DATABASENAME,null,val);
        db.close();
    }

    public void deleteCatalog(String user,String catalogName){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CATALOGDATABASE_DATABASENAME,CATALOGDATABASE_CATALOGNAME + " = '" + catalogName + "' AND " + CATALOGDATABASE_USERNAME + " = '" + user + "'",null);
        db.close();
    }

}
