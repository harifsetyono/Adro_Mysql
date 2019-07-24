package com.example.dts12_resfull_api;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.dts12_resfull_api.adapter.Adapter;
import com.example.dts12_resfull_api.helper.DBHelperBackend;
import com.example.dts12_resfull_api.model.Data;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    private ListView listView;
    List<Data> items ;
    Adapter adapter;
    DBHelperBackend dbHelper;

    AlertDialog.Builder dialog;


    public static final String TAG_ID = "id";
    public static final String TAG_NAME = "name";
    public static final String TAG_POSITION = "position";
    public static final String TAG_SALARY = "salary";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ip_address", "192.168.100.115");
        editor.apply();

//        SharedPreferences sharedPreferences = getSharedPreferences("LoginPreference", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("ip_address", "192.168.100.115");
//        editor.commit();

        FloatingActionButton btnAdd = (FloatingActionButton) findViewById(R.id.fab);
        btnAdd.setOnClickListener(this);

        items = new ArrayList<>();
        adapter = new Adapter(this, items);

        listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        dbHelper = new DBHelperBackend(items,this);
        dbHelper.getAllPegawai();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String idx = items.get(i).getId();
                final String name = items.get(i).getName();
                final String position = items.get(i).getPosition();
                final String salary = items.get(i).getSalary();

                Intent intent = new Intent(MainActivity.this, EditDataActivity.class);
                intent.putExtra(TAG_ID, idx);
                intent.putExtra(TAG_NAME, name);
                intent.putExtra(TAG_POSITION, position);
                intent.putExtra(TAG_SALARY, salary);
                startActivity(intent);
            }
        });

//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                final String idx = items.get(i).getId();
//                final String name = items.get(i).getName();
//                final String position = items.get(i).getPosition();
//                final String salary = items.get(i).getSalary();
//
//                final CharSequence[] dialogItem = {"Edit","Delete"};
//                dialog = new AlertDialog.Builder(MainActivity.this);
//                dialog.setCancelable(true);
//                dialog.setItems(dialogItem, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        switch (i){
//                            case 0:
//                                Intent intent = new Intent(MainActivity.this, EditDataActivity.class);
//                                intent.putExtra(TAG_ID, idx);
//                                intent.putExtra(TAG_NAME, name);
//                                intent.putExtra(TAG_POSITION, position);
//                                intent.putExtra(TAG_SALARY, salary);
//                                startActivity(intent);
//                                break;
//                            case 1:
//                                //asjdgagdajdgadjagdajgdjag
//                                break;
//                        }
//                    }
//                });
//
//                return true;
//            }
//        });

    }

    public void ReloadList(){
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(MainActivity.this, AddDataActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =
                getMenuInflater();
        inflater.inflate(R.menu.menuoption,
                menu);
        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.itemSetting) {
            startActivity(new Intent(MainActivity.this, SetttingActivity.class));
//            finish();
        }

        return true;
    }
}
