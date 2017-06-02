package net.patrickjasonlim.intellijtaskapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.patrickjasonlim.intellijtaskapp.tasks.AddUpdateTaskActivity;
import net.patrickjasonlim.intellijtaskapp.tasks.Task;
import net.patrickjasonlim.intellijtaskapp.tasks.TaskListAdapter;

import static net.patrickjasonlim.intellijtaskapp.tasks.AddUpdateTaskActivity.EXTRA_MODE;
import static net.patrickjasonlim.intellijtaskapp.tasks.AddUpdateTaskActivity.EXTRA_UPDATE_TASK_ID;
import static net.patrickjasonlim.intellijtaskapp.tasks.AddUpdateTaskActivity.MODE_ADD;
import static net.patrickjasonlim.intellijtaskapp.tasks.AddUpdateTaskActivity.MODE_UPDATE;

public class MainActivity extends AppCompatActivity implements TaskListAdapter.TaskListEventListener {

    private static final int LOADER_ID = 1;

    private RecyclerView rvTaskList;
    private FloatingActionButton fabAdd;
    private LinearLayoutManager mLayoutMgr;
    private TaskListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvTaskList = (RecyclerView) findViewById(R.id.rv_task_list);
        fabAdd = (FloatingActionButton) findViewById(R.id.fab_add);

        mAdapter = new TaskListAdapter(getApplicationContext());
        mAdapter.setListener(this);

        mLayoutMgr = new LinearLayoutManager(this);
        rvTaskList.setLayoutManager(mLayoutMgr);
        rvTaskList.setAdapter(mAdapter);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAddTask();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks();
    }

    private void loadTasks() {
        new AsyncTask<Void, Void, Cursor>() {

            @Override
            protected Cursor doInBackground(Void... params) {
                Cursor cursor = Task.getTaskListCursor(getApplicationContext());
                return cursor;
            }

            @Override
            protected void onPostExecute(Cursor cursor) {
                mAdapter.setCursor(cursor);
            }
        }.execute();
    }

    private void goToAddTask() {
        Intent i = new Intent(this, AddUpdateTaskActivity.class);
        i.putExtra(EXTRA_MODE, MODE_ADD);
        startActivity(i);
    }

    @Override
    public void onItemClick(Task task, int position) {
        Intent i = new Intent(this, AddUpdateTaskActivity.class);
        i.putExtra(EXTRA_MODE, MODE_UPDATE);
        i.putExtra(EXTRA_UPDATE_TASK_ID, task.id);
        startActivity(i);
    }

    @Override
    public void onDeleteClick(final Task task, int position) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Delete Task")
                .setMessage("Are you sure you want to delete the task?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        task.deleteTask(getApplicationContext());
                        loadTasks();
                    }
                })
                .setNegativeButton("No", null)
                .create();
        dialog.show();
    }
}
