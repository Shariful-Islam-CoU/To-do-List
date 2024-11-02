package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.todolist.Adapter.ToDoAdapter;
import com.example.todolist.Model.ToDoModel;
import com.example.todolist.Utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {

    private RecyclerView recyclerView;
    private ToDoAdapter taskAdapter;
    private List<ToDoModel> tasklist;
    private DatabaseHandler db;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    //    getSupportActionBar().hide();

        tasklist=new ArrayList<>();
        db=new DatabaseHandler(this);
        db.openDatabase();
        fab=findViewById(R.id.floatingID);



        recyclerView=findViewById(R.id.taskRecyclerViewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter=new ToDoAdapter(db,this);
        recyclerView.setAdapter(taskAdapter);

        ItemTouchHelper itemTouchHelper =new ItemTouchHelper(new RecyclerItemTouchHelper(taskAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);


        tasklist=db.getAllTasks();
        Collections.reverse(tasklist);
        taskAdapter.setTasks(tasklist);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewTask.newInstance().show(getSupportFragmentManager(),AddNewTask.TAG);
            }
        });



    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void handleDialogClose(DialogInterface dialogInterface) {
        tasklist=db.getAllTasks();
        Collections.reverse(tasklist);
        taskAdapter.setTasks(tasklist);
        taskAdapter.notifyDataSetChanged();
    }
}