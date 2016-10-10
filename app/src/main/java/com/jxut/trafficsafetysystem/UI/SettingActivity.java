package com.jxut.trafficsafetysystem.UI;

import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.jxut.trafficsafetysystem.R;

/**
 * Created by Administrator on 2016/10/10.
 */

public class SettingActivity extends BaseActivity {

    private String frequencys;
    private String alarms;
    @Override
    public void initViews() {
        super.initViews();
        setContentView(R.layout.activity_setting);
        final Spinner frequency = (Spinner)findViewById(R.id.setting_frequency);
        Spinner alarm = (Spinner)findViewById(R.id.setting_alarm);
        frequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] frequencies = getResources().getStringArray(R.array.frequency);
                frequencys = frequencies[position];
                Toast.makeText(SettingActivity.this,frequencys,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        alarm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] alarmes = getResources().getStringArray(R.array.alarm);
                alarms = alarmes[position];
                Toast.makeText(SettingActivity.this,alarms,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final TextInputLayout high = (TextInputLayout)findViewById(R.id.setting_high);
        final TextInputLayout low = (TextInputLayout)findViewById(R.id.setting_low);
        final TextInputLayout electrode = (TextInputLayout)findViewById(R.id.setting_electrode);
        final TextInputLayout refesh = (TextInputLayout)findViewById(R.id.setting_refresh);
        final TextInputLayout alarmNum = (TextInputLayout)findViewById(R.id.setting_alarmNum);
        Button submit = (Button)findViewById(R.id.setting_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String highs = high.getEditText().getText().toString();
                String lows = low.getEditText().getText().toString();
                String electrods = electrode.getEditText().getText().toString();
                String refeshs = refesh.getEditText().getText().toString();
                String alarmNums = alarmNum.getEditText().getText().toString();
                Toast.makeText(SettingActivity.this,highs + ":" + lows + ":" + electrods + ":" + refeshs + ":" + alarmNums,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
