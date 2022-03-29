package thundersharp.aigs.newsletter.core.helper;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import thundersharp.aigs.newsletter.core.model.NewsLetters;
import thundersharp.aigs.newsletter.core.utils.TimeUtils;


public class OfflineNewsHelper extends SQLiteOpenHelper {

    SQLiteDatabase database;
    private Context context;
    private static final String DATABASE_NAME = "spekter_database.db";
    private static final int DATABASE_VERSION = 4;

    private static final String DATABASE_TABLE_NAME = "spekter_news_data";


    private static final String AUTHOR = "news_author";
    private static final String TITLE = "news_title";
    private static final String DESCRIPTION = "news_description";
    private static final String URL = "news_url";
    private static final String URL_TO_IMAGE = "news_urlToImage";
    private static final String PUBLISHED_AT = "news_publishedAt";

    //private static final String SOURCE_ID = "source_id";
    private static final String SOURCE_NAME = "source_name";
    private static final String SHORT_DESCRIPTION = "source_description";
    private static final String DATABASE_TABLE_POSITION = "news_position";


    public OfflineNewsHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String Query_Table = " CREATE TABLE "
                +DATABASE_TABLE_NAME+"("
                +DATABASE_TABLE_POSITION+" INTEGER PRIMARY KEY, "
                +AUTHOR +" TEXT, "
                +TITLE+" TEXT, "
                +DESCRIPTION+" TEXT, "
                +URL+" TEXT, "
                +URL_TO_IMAGE+" TEXT, "
                +PUBLISHED_AT+" TEXT, "
                +SOURCE_NAME+" TEXT, "
                +SHORT_DESCRIPTION+" TEXT )";
        db.execSQL(Query_Table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE_NAME);
        onCreate(db);
    }

    // code to add a new contact
    public boolean addContact(NewsLetters newsModel,int pos) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DATABASE_TABLE_POSITION, pos);
        values.put(AUTHOR, newsModel.AUTHOR);
        values.put(TITLE, newsModel.TITLE);
        Log.e("TITLE",newsModel.TITLE);
        values.put(DESCRIPTION, newsModel.DESCRIPTION);
        values.put(URL, newsModel.URL);
        values.put(URL_TO_IMAGE, newsModel.URL_TO_IMAGE);
        values.put(PUBLISHED_AT, newsModel.PUBLISHED_AT);
        //values.put(SOURCE_ID, newsModel.SOURCE_ID);
        values.put(SOURCE_NAME, newsModel.SOURCE_NAME);
        values.put(SHORT_DESCRIPTION, newsModel.SHORT_DESCRIPTION);


        // Inserting Row
        db.insert(DATABASE_TABLE_NAME, null, values);
        saveCurrentUpdateTime();
        //db.close(); // Closing database connection
        return true;
    }


    NewsLetters getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(DATABASE_TABLE_NAME, new String[] { DATABASE_TABLE_POSITION,
                        AUTHOR,
                        TITLE,
                        DESCRIPTION,
                        URL,
                        URL_TO_IMAGE,
                        PUBLISHED_AT,
                        SOURCE_NAME,
                        SHORT_DESCRIPTION,
                }, DATABASE_TABLE_POSITION + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        NewsLetters newsModel = new NewsLetters();

        return newsModel;
    }

    public void saveCurrentUpdateTime(){
        SharedPreferences.Editor editor = context.getSharedPreferences(DATABASE_NAME,Context.MODE_PRIVATE).edit();
        //editor.putLong("timestamp",System.currentTimeMillis());
        editor.putLong("timestamp", TimeUtils.getTimeStampOfOriginToday());
        editor.apply();
    }

    public long getLastUpdateTime(){
        return context.getSharedPreferences(DATABASE_NAME,Context.MODE_PRIVATE).getLong("timestamp",000000000);
    }

    public Long getLastUpdateTimePos(int itemPosition) throws Exception{
        List<NewsLetters> newsModels = getAllNews();
        if (newsModels != null){

            if (!newsModels.isEmpty()){

                if (newsModels.size() > itemPosition){
                    String selectQuery = "SELECT "+DATABASE_TABLE_POSITION+" FROM " + DATABASE_TABLE_NAME+" WHERE "+DATABASE_TABLE_POSITION+" ='"+itemPosition+"'";

                    SQLiteDatabase db = this.getWritableDatabase();
                    Cursor cursor = db.rawQuery(selectQuery, null);
                    if (cursor != null) {
                        cursor.move(1);
                        return cursor.getLong(14);

                    }else{
                        throw new Exception("Cursor didn't moved");
                    }

                }else {
                    throw new Exception("Position do not exists in the database !");
                }

            }else {
                throw new Exception("Got empty response from database !");
            }

        }else{
            throw new Exception("Got null response from the database !");
        }

    }

    // code to get all contacts in a list
    public List<NewsLetters> getAllNews() {
        List<NewsLetters> contactList = new ArrayList<NewsLetters>();
        String selectQuery = "SELECT  * FROM " + DATABASE_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                NewsLetters newsModel = new NewsLetters(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8));
                //newsModel.setNews_position(Integer.parseInt(cursor.getString(0)));


                contactList.add(newsModel);
            } while (cursor.moveToNext());
        }

        return contactList;
    }

    // Getting news Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + DATABASE_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    //drop table externally
    public boolean dropTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE_NAME);
        onCreate(db);
        return true;
    }
}
