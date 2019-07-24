package com.example.dts12_resfull_api;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dts12_resfull_api.helper.DBHelperBackend;

public class EditDataActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txt_id, txt_name, txt_position, txt_salary;
    private Button btn_submit, btn_delete;

    public static final String TAG_ID = "id";
    public static final String TAG_NAME = "name";
    public static final String TAG_POSITION = "position";
    public static final String TAG_SALARY = "salary";

    DBHelperBackend helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);

        txt_id = (EditText) findViewById(R.id.txt_id);
        txt_name = (EditText) findViewById(R.id.txt_name);
        txt_position = (EditText) findViewById(R.id.txt_position);
        txt_salary = (EditText) findViewById(R.id.txt_salary);

        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_delete = (Button) findViewById(R.id.btn_delete);

        btn_submit.setOnClickListener(this);
        btn_delete.setOnClickListener(this);

        Intent intent = getIntent();
        String id = intent.getStringExtra(TAG_ID);
        String name = intent.getStringExtra(TAG_NAME);
        String position = intent.getStringExtra(TAG_POSITION);
        String salary = intent.getStringExtra(TAG_SALARY);

        txt_id.setText(id);
        txt_name.setText(name);
        txt_position.setText(position);
        txt_salary.setText(salary);

        helper = new DBHelperBackend(null,this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_submit:
                String id = txt_id.getText().toString();
                String nama = txt_name.getText().toString();
                String posisi = txt_position.getText().toString();
                String salary = txt_salary.getText().toString();
                helper.updatePegawai(id, nama,posisi,salary);

                txt_id.setText("");
                txt_name.setText("");
                txt_position.setText("");
                txt_salary.setText("");


                startActivity(new Intent(EditDataActivity.this, MainActivity.class));
                finish();

                Toast.makeText(this, "Data updated", Toast.LENGTH_SHORT).show();

                break;

            case R.id.btn_delete:
                String id1 = txt_id.getText().toString();
                helper.hapusPegawai(id1);

                startActivity(new Intent(EditDataActivity.this, MainActivity.class));
                finish();

                Toast.makeText(this, "Data deleted", Toast.LENGTH_SHORT).show();


                break;
        }
    }
}
