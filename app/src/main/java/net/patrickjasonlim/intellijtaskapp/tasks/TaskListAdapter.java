package net.patrickjasonlim.intellijtaskapp.tasks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.patrickjasonlim.intellijtaskapp.R;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskListViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private Cursor mCursor;
    private TaskListEventListener mListener;

    public interface TaskListEventListener {
        void onItemClick(Task task, int position);
        void onDeleteClick(Task task, int position);
    }

    public TaskListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public TaskListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_task_item, parent, false);
        TaskListViewHolder vh = new TaskListViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(TaskListViewHolder holder, int position) {
        if (mCursor != null) {
            mCursor.moveToPosition(position);
            Task task = Task.getTaskByCursor(mCursor);
            holder.bindItem(task);
        }
    }

    @Override
    public int getItemCount() {
        if (mCursor != null) {
            return mCursor.getCount();
        } else {
            return 0;
        }
    }

    public void setListener(TaskListEventListener listener) {
        mListener = listener;
    }

    public void setCursor(Cursor cursor) {
        mCursor = cursor;
        this.notifyDataSetChanged();
    }

    class TaskListViewHolder extends RecyclerView.ViewHolder {

        private Task mTask;
        private TextView tvName;
        private ImageView ivDelete;

        public TaskListViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tv_task_name);
            ivDelete = (ImageView) itemView.findViewById(R.id.iv_delete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClick(mTask, getAdapterPosition());
                    }
                }
            });
            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItem();
                }
            });
        }

        private void deleteItem() {
            if (mListener != null) {
                mListener.onDeleteClick(mTask, getAdapterPosition());
            }

        }

        public void bindItem(Task task) {
            mTask = task;
            //
            tvName.setText(task.name);
        }
    }
}
