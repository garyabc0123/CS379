import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "db";
    private static final String USERDATABASE_DATABASENAME = "userTable";
    private static final String USERDATABASE_ACCOUNT = "account";
    private static final String USERDATAVASE_PASSWORD = "password";

    private static final String CATALOGDATABASE_DATABASENAME = "catalogTable";
    private static final String CATALOGDATABASE_CATALOGNAME = "catalogName";
    private static final String CATALOGDATABASE_COLOR_R = "r";
    private static final String CATALOGDATABASE_COLOR_G = "g";
    private static final String CATALOGDATABASE_COLOR_B = "b";

    public DBhelper (Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase database){
        String command = "CREATE TABLE " + USERDATABASE_DATABASENAME + " (" +
                USERDATABASE_ACCOUNT + " TEXT, " +
                USERDATAVASE_PASSWORD + " TEXT " +
                " )";
        database.execSQL(command);

        command = "CREATE TABLE " + CATALOGDATABASE_DATABASENAME + " (" +
                CATALOGDATABASE_CATALOGNAME + "TEXT, " +
                CATALOGDATABASE_COLOR_R + "INTEGER, " +
                CATALOGDATABASE_COLOR_G + "INTEGER, " +
                CATALOGDATABASE_COLOR_B + "INTEGER " +
                ")";
        database.execSQL(command);

    }

    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
        database.execSQL("DROP TABLE IF EXISTS " + USERDATABASE_DATABASENAME);
        database.execSQL("DROP TABLE IF EXISTS " + CATALOGDATABASE_DATABASENAME);
        onCreate(database);
    }


}
