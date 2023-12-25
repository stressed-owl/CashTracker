package com.nikolay.cashtracker.incomes;

import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nikolay.cashtracker.R;
import com.nikolay.cashtracker.db.DBHelper;
import com.nikolay.cashtracker.recyclerview.OnButtonClickListener;
import com.nikolay.cashtracker.recyclerview.UserDataAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class IncomesFragment extends Fragment implements OnButtonClickListener {

    private DBHelper dbHelper;
    private UserDataAdapter userDataAdapter;
    private EditText incomeValueEditText;
    private EditText incomeSourceEditText;
    private RecyclerView recyclerView;
    private TextView noDataFoundTxtView;
    private Cursor cursor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_incomes, container, false);

        noDataFoundTxtView = view.findViewById(R.id.noDataFoundTxtViewIncomes);
        recyclerView = view.findViewById(R.id.incomesRecyclerView);

        ArrayList<String> columnNames = new ArrayList<>();
        columnNames.add(DBHelper.KEY_INCOME_AMOUNT);
        columnNames.add(DBHelper.KEY_INCOME_SOURCE);
        columnNames.add(DBHelper.KEY_INCOME_DATE);

        cursor = dbHelper.readData(columnNames, DBHelper.INCOMES_TABLE_NAME);
        userDataAdapter = new UserDataAdapter(
                requireContext(),
                cursor,
                DBHelper.KEY_INCOME_AMOUNT,
                DBHelper.KEY_INCOME_DATE,
                DBHelper.KEY_INCOME_SOURCE,
                R.layout.incomes_list_item
        );
        userDataAdapter.setOnButtonClickListener(this);

        if(cursor.getCount() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            noDataFoundTxtView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            noDataFoundTxtView.setVisibility(View.VISIBLE);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(userDataAdapter);
        FloatingActionButton fab = view.findViewById(R.id.fabAddIncomes);

        fab.setOnClickListener(v -> showInputDialog());

        return view;
    }

    //    To set date when user added a record

    public String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void showInputDialog() {
        final TextView customAlertTitle = new TextView(requireActivity());
        Typeface poppins = ResourcesCompat.getFont(requireContext(), R.font.poppins_light);

        View incomesAlertView = getLayoutInflater().inflate(R.layout.custom_alert_incomes_view, null);
        incomeValueEditText = incomesAlertView.findViewById(R.id.incomeAmountEditText);
        incomeSourceEditText = incomesAlertView.findViewById(R.id.incomeSourceEditText);

        customAlertTitle.setText(getResources().getString(R.string.alert_incomes_title), null);
        customAlertTitle.setTextSize(22f);
        customAlertTitle.setTypeface(poppins);
        customAlertTitle.setTextColor(getResources().getColor(R.color.black, null));
        customAlertTitle.setPadding(64, 32, 16, 16);

        new MaterialAlertDialogBuilder(requireActivity(), R.style.AlertDialogTheme)
                .setTitle(getResources().getString(R.string.alert_incomes_title))
                .setView(incomesAlertView)
                .setNegativeButton(getResources().getString(R.string.cancel), (dialog, which) -> dialog.cancel())
                .setPositiveButton(getResources().getString(R.string.add), (dialog, which) -> {
                    String incomesAmount = incomeValueEditText.getText().toString();
                    String incomesSource = incomeSourceEditText.getText().toString();
                    String incomesDate = getCurrentDate();

//                    Map with users data (income, source and record date)
                    Map<String, Object> data = new HashMap<>();
                    data.put(DBHelper.KEY_INCOME_AMOUNT, incomesAmount);
                    data.put(DBHelper.KEY_INCOME_SOURCE, incomesSource);
                    data.put(DBHelper.KEY_INCOME_DATE, incomesDate);

                    boolean success = dbHelper.addData(data, DBHelper.INCOMES_TABLE_NAME);
                    if (success) {
                        updateRecyclerView();
                    } else {
                        Log.e("DBHELPER ERROR", "Something went wrong");
                    }
                })
                .show();
    }

    private void updateRecyclerView() {
        ArrayList<String> columnNames = new ArrayList<>();
        columnNames.add(DBHelper.KEY_INCOME_AMOUNT);
        columnNames.add(DBHelper.KEY_INCOME_SOURCE);
        columnNames.add(DBHelper.KEY_INCOME_DATE);

        Cursor updatedCursor = dbHelper.readData(columnNames, DBHelper.INCOMES_TABLE_NAME);

        // Close the old cursor and update the adapter with the new cursor
        if (cursor != null) {
            cursor.close();
        }

        cursor = updatedCursor;

        userDataAdapter.updateCursor(cursor);

        if (cursor != null && cursor.getCount() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            noDataFoundTxtView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            noDataFoundTxtView.setVisibility(View.VISIBLE);
        }

        // Notify the adapter that the data set has changed
        userDataAdapter.notifyDataSetChanged();
    }

    @Override
    public void onButtonClick(int position) {
        if(cursor != null && cursor.moveToPosition(position)) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.KEY_INCOME_ID));
            boolean success = dbHelper.deleteData(id, DBHelper.INCOMES_TABLE_NAME, DBHelper.KEY_INCOME_ID);

            if (success) {
                updateRecyclerView();
            } else {
                Log.e("DBHELPER ERROR", "Something went wrong");
            }
        }
    }
}