package com.example.hoahoc;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.hoahoc.model.baigiang;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    Context context;

    private static final String DATABASE_NAME = "Danhsach_Chuong.db"; // Tên database
    private static final int DATABASE_VERSION = 1; // Phiên bản database
    private static final String DB_PATH_SUFFIX = "/databases/";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        copyDatabaseFromAssets();
    }
    public ArrayList<baigiang> getDataByLop(String lop) {
        ArrayList<baigiang> listbaigiang = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try{

            // get du lieu by id
            Cursor cursor = db.query("tb_baigiang", null, "Lop = ?", new String[]{lop}, null, null, null, null);
            if(cursor.getCount() > 0){
                while(cursor.moveToNext()){
                    String id = cursor.getString(0);
                    String tenchuong = cursor.getString(1);
                    String Lop = cursor.getString(2);
                    String thongtin = cursor.getString(3);
                    listbaigiang.add(new baigiang(id, tenchuong, Lop, thongtin));
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            db.endTransaction();

        }
        return listbaigiang;

    }

    private void copyDatabaseFromAssets() {
        File dbFile = context.getDatabasePath(DATABASE_NAME);

        if (dbFile.exists()) {
            SQLiteDatabase checkDB = null;
            try {
                checkDB = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
                Cursor cursor = checkDB.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
                if (cursor.getCount() == 0) {
                    Log.e("DatabaseHelper", "❌ Database bị lỗi! Xóa và copy lại.");
                    dbFile.delete();
                }
                cursor.close();
                checkDB.close();
            } catch (Exception e) {
                Log.e("DatabaseHelper", "❌ Không thể mở database, xóa và copy lại.");
                dbFile.delete();
            }
        }

        if (!dbFile.exists()) {
            try {
                InputStream myInput = context.getAssets().open(DATABASE_NAME);
                String outFileName = context.getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
                File dbDir = new File(context.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
                if (!dbDir.exists()) dbDir.mkdirs();

                OutputStream myOutput = new FileOutputStream(outFileName);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }
                myOutput.flush();
                myOutput.close();
                myInput.close();

                Log.d("DatabaseHelper", "✅ Database copied successfully!");
            } catch (IOException e) {
                Log.e("DatabaseHelper", "❌ Copy database failed! " + e.getMessage());
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
