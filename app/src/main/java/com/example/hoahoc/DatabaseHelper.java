package com.example.hoahoc; // Hoặc một package chung

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.hoahoc.model.baigiang;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "AppDatabase.db"; // Tên database chung
    private static final int DATABASE_VERSION = 3; // Tăng version khi thay đổi schema
    private static final String DB_PATH_SUFFIX = "/databases/";
    private final Context context;

    // Bảng bài giảng
    private static final String TABLE_BAI_GIANG = "tb_baigiang";
    private static final String KEY_BAI_GIANG_ID = "id";
    private static final String KEY_BAI_GIANG_TENCHUONG = "tenchuong";
    private static final String KEY_BAI_GIANG_LOP = "Lop";
    private static final String KEY_BAI_GIANG_THONGTIN = "thongtin";

    // Bảng kỳ thi
    private static final String TABLE_EXAMS = "exams";
    private static final String KEY_EXAM_ID = "id";
    private static final String KEY_EXAM_NUMBER = "exam_number";

    // Bảng câu hỏi
    private static final String TABLE_QUESTIONS = "questions";
    private static final String KEY_QUESTION_ID = "id";
    private static final String KEY_EXAM_ID_FK = "exam_id";
    private static final String KEY_QUESTION_TEXT = "question_text";
    private static final String KEY_OPTION_A = "option_a";
    private static final String KEY_OPTION_B = "option_b";
    private static final String KEY_OPTION_C = "option_c";
    private static final String KEY_OPTION_D = "option_d";
    private static final String KEY_CORRECT_ANSWER = "correct_answer";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        copyDatabaseFromAssets();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        // Tạo bảng bài giảng
//        String CREATE_BAI_GIANG_TABLE = "CREATE TABLE " + TABLE_BAI_GIANG + "("
//                + KEY_BAI_GIANG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//                + KEY_BAI_GIANG_TENCHUONG + " TEXT,"
//                + KEY_BAI_GIANG_LOP + " TEXT,"
//                + KEY_BAI_GIANG_THONGTIN + " TEXT" + ")";
////        db.execSQL(CREATE_BAI_GIANG_TABLE);
////
////        // Tạo bảng kỳ thi
//        String CREATE_EXAMS_TABLE = "CREATE TABLE " + TABLE_EXAMS + "("
//                + KEY_EXAM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//                + KEY_EXAM_NUMBER + " INTEGER" + ")";
//        db.execSQL(CREATE_EXAMS_TABLE);
//
//        // Tạo bảng câu hỏi
//        String CREATE_QUESTIONS_TABLE = "CREATE TABLE " + TABLE_QUESTIONS + "("
//                + KEY_QUESTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//                + KEY_EXAM_ID_FK + " INTEGER,"
//                + KEY_QUESTION_TEXT + " TEXT,"
//                + KEY_OPTION_A + " TEXT,"
//                + KEY_OPTION_B + " TEXT,"
//                + KEY_OPTION_C + " TEXT,"
//                + KEY_OPTION_D + " TEXT,"
//                + KEY_CORRECT_ANSWER + " TEXT,"
//                + "FOREIGN KEY(" + KEY_EXAM_ID_FK + ") REFERENCES " + TABLE_EXAMS + "(" + KEY_EXAM_ID + "))";
//        db.execSQL(CREATE_QUESTIONS_TABLE);

        // Thêm dữ liệu mặc định cho kỳ thi (nếu cần)
//        for (int i = 1; i <= 9; i++) {
//            ContentValues examValues = new ContentValues();
//            examValues.put(KEY_EXAM_NUMBER, i);
//            long examId = db.insert(TABLE_EXAMS, null, examValues);
//
//            for (int j = 1; j <= 40; j++) {
//                ContentValues questionValues = new ContentValues();
//                questionValues.put(KEY_EXAM_ID_FK, examId);
//                questionValues.put(KEY_QUESTION_TEXT, "Câu hỏi mặc định " + j + " của đề " + i);
//                questionValues.put(KEY_OPTION_A, "A. Mặc định A");
//                questionValues.put(KEY_OPTION_B, "B. Mặc định B");
//                questionValues.put(KEY_OPTION_C, "C. Mặc định C");
//                questionValues.put(KEY_OPTION_D, "D. Mặc định D");
//                questionValues.put(KEY_CORRECT_ANSWER, "A");
//                db.insert(TABLE_QUESTIONS, null, questionValues);
//            }
//        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BAI_GIANG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXAMS);
        onCreate(db);
    }

    // Các phương thức truy vấn cho bảng bài giảng
    public ArrayList<baigiang> getDataByLop(String lop) {
        ArrayList<baigiang> listbaigiang = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        try{
            Cursor cursor = db.query(TABLE_BAI_GIANG, null, KEY_BAI_GIANG_LOP + "=?", new String[]{lop}, null, null, null);
            if (cursor.moveToFirst()) {
                do {

                    baigiang bg = new baigiang(
                            cursor.getString(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3)
                    );
                    listbaigiang.add(bg);


                }
                while (cursor.moveToNext());
            }
            cursor.close();
        }catch (Exception e){

            }
        return listbaigiang;
    }

    // Các phương thức truy vấn cho bảng kỳ thi
    public Cursor getAllExams() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_EXAMS, null);
    }

    // Các phương thức truy vấn cho bảng câu hỏi
    public Cursor getQuestionsByExamId(int examId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_QUESTIONS + " WHERE " + KEY_EXAM_ID_FK + "=?", new String[]{String.valueOf(examId)});
    }

    public boolean updateQuestion(int examId, int questionNumber, String questionText, String optionA, String optionB, String optionC, String optionD, String correctAnswer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_QUESTION_TEXT, questionText);
        values.put(KEY_OPTION_A, optionA);
        values.put(KEY_OPTION_B, optionB);
        values.put(KEY_OPTION_C, optionC);
        values.put(KEY_OPTION_D, optionD);
        values.put(KEY_CORRECT_ANSWER, correctAnswer);

        Cursor cursor = db.rawQuery("SELECT " + KEY_QUESTION_ID + " FROM " + TABLE_QUESTIONS + " WHERE " + KEY_EXAM_ID_FK + "=? LIMIT ?,1", new String[]{String.valueOf(examId), String.valueOf(questionNumber - 1)});
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") int questionId = cursor.getInt(cursor.getColumnIndex(KEY_QUESTION_ID));
            cursor.close();
            int rowsAffected = db.update(TABLE_QUESTIONS, values, KEY_QUESTION_ID + "=?", new String[]{String.valueOf(questionId)});
            return rowsAffected > 0;
        }
        cursor.close();
        return false;
    }

    public void importQuestionsFromCSV() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets().open("questions.csv")));
            String line;
            br.readLine(); // Bỏ qua dòng tiêu đề
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 8) {
                    int examId = Integer.parseInt(data[0]);
                    int questionNumber = Integer.parseInt(data[1]);
                    String questionText = data[2];
                    String optionA = data[3];
                    String optionB = data[4];
                    String optionC = data[5];
                    String optionD = data[6];
                    String correctAnswer = data[7];
                    updateQuestion(examId, questionNumber, questionText, optionA, optionB, optionC, optionD, correctAnswer);
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void copyDatabaseFromAssets() {
        File dbFile = context.getDatabasePath(DATABASE_NAME);

        if (dbFile.exists()) {
            SQLiteDatabase checkDB = null;
            try {
                checkDB = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
                Cursor cursor = checkDB.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
                // Kiểm tra xem cả hai bảng có tồn tại không
                boolean baiGiangExists = false;
                boolean examsExists = false;
                boolean questionsExists = false;
                while (cursor.moveToNext()) {
                    @SuppressLint("Range") String tableName = cursor.getString(cursor.getColumnIndex("name"));
                    if (tableName.equals(TABLE_BAI_GIANG)) baiGiangExists = true;
                    if (tableName.equals(TABLE_EXAMS)) examsExists = true;
                    if (tableName.equals(TABLE_QUESTIONS)) questionsExists = true;
                }
                cursor.close();
                checkDB.close();

                if (!baiGiangExists || !examsExists || !questionsExists) {
                    Log.e("DatabaseHelper", "❌ Một hoặc nhiều bảng bị thiếu! Xóa và copy lại.");
                    dbFile.delete();
                }

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
}