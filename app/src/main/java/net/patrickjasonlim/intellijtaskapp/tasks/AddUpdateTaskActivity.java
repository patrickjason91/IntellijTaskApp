package net.patrickjasonlim.intellijtaskapp.tasks;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.patrickjasonlim.intellijtaskapp.R;

public class AddUpdateTaskActivity extends AppCompatActivity {

    public static final String EXTRA_MODE = "mode";
    public static final String EXTRA_UPDATE_TASK_ID = "update_task_id";

    public static final int MODE_ADD = 1;
    public static final int MODE_UPDATE = 2;

    private EditText etTaskName;
    private Button btnSave;

    private Task mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_task);

        etTaskName = (EditText) findViewById(R.id.et_task_name);
        btnSave = (Button) findViewById(R.id.btn_save);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        mTask = new Task();
        loadUiAndData();
    }

    private int getMode() {
        return getIntent().getIntExtra(EXTRA_MODE, MODE_ADD);
    }

    private void loadUiAndData() {
        if (getMode() == MODE_UPDATE) {
            long taskId = getIntent().getLongExtra(EXTRA_UPDATE_TASK_ID, -1);
            if (taskId != -1) {
                new AsyncTask<Long, Void, Task>() {

                    @Override
                    protected Task doInBackground(Long... params) {
                        Long taskId = params[0];
                        Task task = Task.getTaskById(getApplicationContext(), taskId);
                        return task;
                    }

                    @Override
                    protected void onPostExecute(Task task) {
                        loadTaskToUi(task);
                    }
                }.execute(taskId);
            }
        }
    }

    private void loadTaskToUi(Task task){
        mTask = task;
        etTaskName.setText(task.name);
    }

    private void save() {
        mTask.name = etTaskName.getText().toString();

        if (getMode() == MODE_UPDATE) {
            mTask.updateTask(this);
        } else {
            mTask.name = etTaskName.getText().toString();
            mTask.timestamp = System.currentTimeMillis();

            mTask.saveTask(this);
        }
        //
        finish();
    }
}
