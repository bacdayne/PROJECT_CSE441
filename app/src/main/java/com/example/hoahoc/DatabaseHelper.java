package com.example.hoahoc;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.hoahoc.model.ExamHistory;
import com.example.hoahoc.model.Lesson;
import com.example.hoahoc.model.Question;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "AppDatabase.db";
    private static final int DATABASE_VERSION = 4;
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

    // Bảng lịch sử bài thi
    private static final String TABLE_EXAM_HISTORY = "exam_history";
    private static final String KEY_HISTORY_ID = "history_id";
    private static final String KEY_EXAM_ID_HISTORY = "exam_id";
    private static final String KEY_COMPLETION_TIME = "completion_time";
    private static final String KEY_TOTAL_CORRECT = "total_correct";
    private static final String KEY_SCORE = "score";
    private static final String KEY_QUESTION_IDS = "question_ids";
    private static final String KEY_USER_ANSWERS = "user_answers";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        copyDatabaseFromAssets();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BAI_GIANG_TABLE = "CREATE TABLE " + TABLE_BAI_GIANG + "("
                + KEY_BAI_GIANG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_BAI_GIANG_TENCHUONG + " TEXT,"
                + KEY_BAI_GIANG_LOP + " TEXT,"
                + KEY_BAI_GIANG_THONGTIN + " TEXT" + ")";
        db.execSQL(CREATE_BAI_GIANG_TABLE);

        String CREATE_EXAMS_TABLE = "CREATE TABLE " + TABLE_EXAMS + "("
                + KEY_EXAM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_EXAM_NUMBER + " INTEGER" + ")";
        db.execSQL(CREATE_EXAMS_TABLE);

        String CREATE_QUESTIONS_TABLE = "CREATE TABLE " + TABLE_QUESTIONS + "("
                + KEY_QUESTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_EXAM_ID_FK + " INTEGER,"
                + KEY_QUESTION_TEXT + " TEXT,"
                + KEY_OPTION_A + " TEXT,"
                + KEY_OPTION_B + " TEXT,"
                + KEY_OPTION_C + " TEXT,"
                + KEY_OPTION_D + " TEXT,"
                + KEY_CORRECT_ANSWER + " TEXT,"
                + "FOREIGN KEY(" + KEY_EXAM_ID_FK + ") REFERENCES " + TABLE_EXAMS + "(" + KEY_EXAM_ID + "))";
        db.execSQL(CREATE_QUESTIONS_TABLE);

        String CREATE_EXAM_HISTORY_TABLE = "CREATE TABLE " + TABLE_EXAM_HISTORY + "("
                + KEY_HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_EXAM_ID_HISTORY + " INTEGER,"
                + KEY_COMPLETION_TIME + " TEXT,"
                + KEY_TOTAL_CORRECT + " INTEGER,"
                + KEY_SCORE + " REAL,"
                + KEY_QUESTION_IDS + " TEXT,"
                + KEY_USER_ANSWERS + " TEXT,"
                + "FOREIGN KEY(" + KEY_EXAM_ID_HISTORY + ") REFERENCES " + TABLE_EXAMS + "(" + KEY_EXAM_ID + "))";
        db.execSQL(CREATE_EXAM_HISTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 4) {
            String CREATE_EXAM_HISTORY_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_EXAM_HISTORY + "("
                    + KEY_HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + KEY_EXAM_ID_HISTORY + " INTEGER,"
                    + KEY_COMPLETION_TIME + " TEXT,"
                    + KEY_TOTAL_CORRECT + " INTEGER,"
                    + KEY_SCORE + " REAL,"
                    + KEY_QUESTION_IDS + " TEXT,"
                    + KEY_USER_ANSWERS + " TEXT,"
                    + "FOREIGN KEY(" + KEY_EXAM_ID_HISTORY + ") REFERENCES " + TABLE_EXAMS + "(" + KEY_EXAM_ID + "))";
            db.execSQL(CREATE_EXAM_HISTORY_TABLE);
        }
    }

    // Thêm câu hỏi
    public long addQuestion(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_EXAM_ID_FK, question.getExamId());
        values.put(KEY_QUESTION_TEXT, question.getQuestionText());
        values.put(KEY_OPTION_A, question.getOptionA());
        values.put(KEY_OPTION_B, question.getOptionB());
        values.put(KEY_OPTION_C, question.getOptionC());
        values.put(KEY_OPTION_D, question.getOptionD());
        values.put(KEY_CORRECT_ANSWER, question.getCorrectAnswer());

        long result = db.insert(TABLE_QUESTIONS, null, values);
        db.close();
        return result;
    }

    // Sửa câu hỏi
    public int updateQuestion(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_EXAM_ID_FK, question.getExamId());
        values.put(KEY_QUESTION_TEXT, question.getQuestionText());
        values.put(KEY_OPTION_A, question.getOptionA());
        values.put(KEY_OPTION_B, question.getOptionB());
        values.put(KEY_OPTION_C, question.getOptionC());
        values.put(KEY_OPTION_D, question.getOptionD());
        values.put(KEY_CORRECT_ANSWER, question.getCorrectAnswer());

        int result = db.update(TABLE_QUESTIONS, values, KEY_QUESTION_ID + "=?", new String[]{String.valueOf(question.getId())});
        db.close();
        return result;
    }

    // Xóa câu hỏi
    public void deleteQuestion(int questionId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_QUESTIONS, KEY_QUESTION_ID + "=?", new String[]{String.valueOf(questionId)});
        db.close();
    }

    // Lấy tất cả câu hỏi theo examId
    public List<Question> getQuestionsByExamIdList(int examId) {
        List<Question> questions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_QUESTIONS + " WHERE " + KEY_EXAM_ID_FK + "=?", new String[]{String.valueOf(examId)});
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") Question question = new Question(
                        cursor.getInt(cursor.getColumnIndex(KEY_QUESTION_ID)),
                        cursor.getInt(cursor.getColumnIndex(KEY_EXAM_ID_FK)),
                        cursor.getString(cursor.getColumnIndex(KEY_QUESTION_TEXT)),
                        cursor.getString(cursor.getColumnIndex(KEY_OPTION_A)),
                        cursor.getString(cursor.getColumnIndex(KEY_OPTION_B)),
                        cursor.getString(cursor.getColumnIndex(KEY_OPTION_C)),
                        cursor.getString(cursor.getColumnIndex(KEY_OPTION_D)),
                        cursor.getString(cursor.getColumnIndex(KEY_CORRECT_ANSWER))
                );
                questions.add(question);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return questions;
    }

    // Lưu lịch sử bài thi
    public long saveExamHistory(ExamHistory history) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_EXAM_ID_HISTORY, history.getExamId());
        values.put(KEY_COMPLETION_TIME, history.getCompletionTime());
        values.put(KEY_TOTAL_CORRECT, history.getTotalCorrect());
        values.put(KEY_SCORE, history.getScore());
        values.put(KEY_QUESTION_IDS, history.getQuestionIds());
        values.put(KEY_USER_ANSWERS, history.getUserAnswers());

        long result = db.insert(TABLE_EXAM_HISTORY, null, values);
        db.close();
        return result;
    }

    // Lấy tất cả lịch sử bài thi
    public List<ExamHistory> getAllExamHistory() {
        List<ExamHistory> historyList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EXAM_HISTORY, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int historyId = cursor.getInt(cursor.getColumnIndex(KEY_HISTORY_ID));
                @SuppressLint("Range") int examId = cursor.getInt(cursor.getColumnIndex(KEY_EXAM_ID_HISTORY));
                @SuppressLint("Range") String completionTime = cursor.getString(cursor.getColumnIndex(KEY_COMPLETION_TIME));
                @SuppressLint("Range") int totalCorrect = cursor.getInt(cursor.getColumnIndex(KEY_TOTAL_CORRECT));
                @SuppressLint("Range") double score = cursor.getDouble(cursor.getColumnIndex(KEY_SCORE));
                @SuppressLint("Range") String questionIds = cursor.getString(cursor.getColumnIndex(KEY_QUESTION_IDS));
                @SuppressLint("Range") String userAnswers = cursor.getString(cursor.getColumnIndex(KEY_USER_ANSWERS));

                ExamHistory history = new ExamHistory(historyId, examId, completionTime, totalCorrect, score, questionIds, userAnswers);
                historyList.add(history);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return historyList;
    }

    // Xóa lịch sử bài thi
    public void deleteExamHistory(int historyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXAM_HISTORY, KEY_HISTORY_ID + "=?", new String[]{String.valueOf(historyId)});
        db.close();
    }

    // Các phương thức truy vấn cho bảng bài giảng
    public ArrayList<Lesson> getDataByLop(String lop) {
        ArrayList<Lesson> listbaigiang = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.query(TABLE_BAI_GIANG, null, KEY_BAI_GIANG_LOP + "=?", new String[]{lop}, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    Lesson bg = new Lesson(
                            cursor.getString(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3)
                    );
                    listbaigiang.add(bg);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
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

    public void updateSavedStatus(int lessonId, boolean isSaved) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("trangthai", isSaved ? 1 : 0);
        db.update("tb_baigiang", values, "id = ?", new String[]{String.valueOf(lessonId)});
        db.close();
    }

    public List<Lesson> getSavedLessons() {
        List<Lesson> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tb_baigiang WHERE trangthai = 1", null);

        if (cursor.moveToFirst()) {
            do {
                Lesson lesson = new Lesson(
                        cursor.getString(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("tenchuong")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Lop")),
                        cursor.getString(cursor.getColumnIndexOrThrow("thongtin"))
                );
                lesson.setSaved(true);
                list.add(lesson);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Lesson> getAllLessons() {
        List<Lesson> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tb_baigiang", null);
        if (cursor.moveToFirst()) {
            do {
                Lesson lesson = new Lesson(
                        cursor.getString(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("tenchuong")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Lop")),
                        cursor.getString(cursor.getColumnIndexOrThrow("thongtin"))
                );
                int colIndex = cursor.getColumnIndex("trangthai");
                if (colIndex != -1) {
                    int saved = cursor.getInt(colIndex);
                    lesson.setSaved(saved == 1);
                }
                list.add(lesson);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public long insertLesson(Lesson lesson) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tenchuong", lesson.getTenchuong());
        values.put("Lop", lesson.getLop());
        values.put("thongtin", lesson.getThongtin());
        values.put("trangthai", lesson.isSaved() ? 1 : 0);
        long result = db.insert("tb_baigiang", null, values);
        db.close();
        return result;
    }

    public int updateLesson(Lesson lesson) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tenchuong", lesson.getTenchuong());
        values.put("Lop", lesson.getLop());
        values.put("thongtin", lesson.getThongtin());
        values.put("trangthai", lesson.isSaved() ? 1 : 0);

        int lessonId = lesson.getId();
        int result = db.update("tb_baigiang", values, "id = ?", new String[]{String.valueOf(lessonId)});
        db.close();
        return result;
    }

    public void deleteLesson(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("tb_baigiang", "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public int getNextLessonId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(id) FROM tb_baigiang", null);
        int nextId = 1;
        if (cursor.moveToFirst()) {
            nextId = cursor.getInt(0) + 1;
        }
        cursor.close();
        return nextId;
    }
}