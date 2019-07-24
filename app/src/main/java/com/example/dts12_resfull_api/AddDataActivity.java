package com.example.dts12_resfull_api;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dts12_resfull_api.helper.DBHelperBackend;

public class AddDataActivity extends AppCompatActivity implements View.OnClickListener  {

    private EditText txt_id, txt_name, txt_position, txt_salary;
    private Button btn_submit, btn_cancel;

    DBHelperBackend helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        txt_id = (EditText) findViewById(R.id.txt_id);
        txt_name = (EditText) findViewById(R.id.txt_name);
        txt_position = (EditText) findViewById(R.id.txt_position);
        txt_salary = (EditText) findViewById(R.id.txt_salary);

        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);

        btn_submit.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        helper = new DBHelperBackend(null,this);



    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_submit:
                String nama = txt_name.getText().toString();
                String posisi = txt_position.getText().toString();
                String salary = txt_salary.getText().toString();
                helper.addPegawai(nama,posisi,salary);

                txt_name.setText("");
                txt_position.setText("");
                txt_salary.setText("");

                Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(AddDataActivity.this, MainActivity.class));
                finish();

                break;

            case R.id.btn_cancel:
                startActivity(new Intent(AddDataActivity.this, MainActivity.class));
                finish();
                break;
        }
    }
}
