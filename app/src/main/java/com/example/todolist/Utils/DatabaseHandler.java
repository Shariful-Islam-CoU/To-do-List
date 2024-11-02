package com.example.todolist.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import androidx.annotation.Nullable;

import com.example.todolist.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int VERSION=1;
    private static final String  NAME="ToDoListDatabase";
    private static final String  TABLE_NAME="todo";
    private static final String  ID="id";
    private static final String  TASK="task";
    private static final String  STATUS="status";
    private static final String SELECT_ALL = "SELECT * FROM " + TABLE_NAME + " ";
    private static final String  CREATE_TABLE="CREATE TABLE "+TABLE_NAME+" ( "+ID+" INTEGER PRIMARY KEY AUTOINCREMENT,  "+TASK+" TEXT, "+STATUS+" INTEGER) ";
    private static final String DROP_TABLE="DROP TABLE IF EXISTS "+TABLE_NAME+"";
    Context context;
    private SQLiteDatabase sqLiteDatabase;
    public DatabaseHandler(@Nullable Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE);
        onCreate(sqLiteDatabase);
    }

    public void openDatabase(){
        sqLiteDatabase=this.getWritableDatabase();
    }

    public  void insertTask(ToDoModel task){
        ContentValues contentValues=new ContentValues();
        contentValues.put(TASK,task.getTask());
        contentValues.put(STATUS,0);
        sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
    }

    public List<ToDoModel> getAllTasks(){
        List<ToDoModel> taskList= new ArrayList<>();
        Cursor cursor=null;
        sqLiteDatabase.beginTransaction();

        try{
            cursor=sqLiteDatabase.rawQuery(SELECT_ALL,null);
            if(cursor!=null){
                if(cursor.moveToFirst()){
                    do{
                        ToDoModel toDoModel= new ToDoModel();
                        int a=cursor.getColumnIndex(ID);
                        int b=cursor.getColumnIndex(TASK);
                        int c=cursor.getColumnIndex(STATUS);
                        if(a>=0&&b>=0&&c>=0){
                            toDoModel.setId(cursor.getInt(a));
                            toDoModel.setTask(cursor.getString(b));
                            toDoModel.setStatus(cursor.getInt(c));
                        }
                        taskList.add(toDoModel);
                    } while ((cursor.moveToNext()));
                }
            }
        }
        finally {
            sqLiteDatabase.endTransaction();
            cursor.close();
        }
        return taskList;

    }

    public void upDateStatus(int id,int status){
        ContentValues contentValues=new ContentValues();
        contentValues.put(STATUS,status);
        sqLiteDatabase.update(TABLE_NAME,contentValues,ID+"=?", new String[]{String.valueOf(id)});

    }

    public void updateTask(int id,String task){
        ContentValues contentValues= new ContentValues();
        contentValues.put(TASK,task);
        sqLiteDatabase.update(TABLE_NAME,contentValues,ID+"=?",new String[]{String.valueOf(id)});
    }

    public  void deleteTask(int id){
        sqLiteDatabase.delete(TABLE_NAME,ID+"=?",new String[]{String.valueOf(id)});
    }
}
