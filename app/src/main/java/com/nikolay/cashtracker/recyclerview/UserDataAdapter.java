package com.nikolay.cashtracker.recyclerview;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nikolay.cashtracker.R;
import com.nikolay.cashtracker.db.DBHelper;

public class UserDataAdapter extends RecyclerView.Adapter<UserDataAdapter.ViewHolder> {
    private final Context context;
    public Cursor cursor;
    private final String amountColumnName;
    private final String keyDate;
    private final String _catSo;

    private OnButtonClickListener onButtonClickListener;

    public void setOnButtonClickListener(OnButtonClickListener clickListener) {
        this.onButtonClickListener = clickListener;
    }

    public UserDataAdapter(Context context, Cursor cursor, String amountColumnName, String keyDate, String catSo) {
        this.context = context;
        this.cursor = cursor;
        this.amountColumnName = amountColumnName;
        this.keyDate = keyDate;
        this._catSo = catSo;
    }

    public void updateCursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close();
        }
        cursor = newCursor;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (cursor.moveToPosition(position)) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.KEY_EXPENSE_ID));
            int amount = cursor.getInt(cursor.getColumnIndexOrThrow(amountColumnName));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(keyDate));
            String catSo = cursor.getString(cursor.getColumnIndexOrThrow(_catSo));
            holder.userDataTextView.setText(String.valueOf(amount));
            holder.addedDataDateTextView.setText(date);
            holder.userCatSoTextView.setText(catSo);

            holder.deleteUserDataBtn.setOnClickListener(v -> {
                if (onButtonClickListener != null) {
                    onButtonClickListener.onButtonClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return (cursor == null) ? 0 : cursor.getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView userDataTextView;
        private final TextView addedDataDateTextView;
        private final TextView userCatSoTextView;
        private final ImageButton deleteUserDataBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userDataTextView = itemView.findViewById(R.id.userDataTextView);
            addedDataDateTextView = itemView.findViewById(R.id.addedDataDateTextView);
            userCatSoTextView = itemView.findViewById(R.id.userCatSoTextView);
            deleteUserDataBtn = itemView.findViewById(R.id.deleteUserDataBtn);
        }
    }
}
