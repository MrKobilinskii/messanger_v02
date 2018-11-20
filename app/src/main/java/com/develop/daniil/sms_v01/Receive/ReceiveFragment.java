package com.develop.daniil.sms_v01.Receive;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.develop.daniil.sms_v01.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class ReceiveFragment extends Fragment {

    SQLiteDatabase database;
    String fromAddress, bodyMsg;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_receive, container,false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            bodyMsg = getArguments().getString("bodyMsg","Null");
            fromAddress = getArguments().getString("fromAddress","Null");
        }

        if(bodyMsg != null && fromAddress != null) {
            workWithDataBase();
            setupListview(view);
        }
        return view;
    }


    private void workWithDataBase() {
        if(getActivity() != null) {
            database = getActivity().openOrCreateDatabase("messages.db", MODE_PRIVATE, null);
        }

        database.execSQL("create table if not exists messages(id integer primary key, fromAddress text, bodyMsg text)");

        ContentValues contentValues = new ContentValues();
        contentValues.put("fromAddress", fromAddress);
        contentValues.put("bodyMsg", bodyMsg);
        database.insert("messages", null, contentValues);

        Cursor cursor = database.rawQuery("select * from messages", null);

        cursor.moveToFirst();

        fromAddress = cursor.getString(1);
        bodyMsg = cursor.getString(2);

        database.close();
    }


    private void setupListview(View view) {
        ListView listView = view.findViewById(R.id.ReceiveSms_ListView);
        ArrayList<String> messages = new ArrayList<>();
        messages.add(fromAddress + "\n" + bodyMsg);

        ArrayAdapter adapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_expandable_list_item_1, messages);
        listView.setAdapter(adapter);
    }

}
