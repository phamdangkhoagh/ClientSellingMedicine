package com.example.clientsellingmedicine;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class IndividualUpdateScreenActivity extends AppCompatActivity {

    private TextView mDisplayData;

    private DatePickerDialog.OnDateSetListener mDataSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.individual_update_screen);

    }

    private void addControl() {
        mDisplayData = (TextView) findViewById(R.id.tvIndividualUpdateDate);
    }
    private void addEvents() {
        //handle the onclick event date
        mDisplayData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(IndividualUpdateScreenActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDataSetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        //show date
        mDataSetListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d("TAG","onDataSet: data "+ year + month + dayOfMonth);

                String date = month + "/" + dayOfMonth + "/" + year;
                mDisplayData.setText(date);
            }
        };

    }

}
