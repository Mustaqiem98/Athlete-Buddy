package com.example.lab5authenticationiqbal;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class RegisterDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private static final String TAG="RegisterDetails";
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    String regID,name, phone, date,gender, sport;
    EditText  editTextFullName, editTextPhone;
    Button  buttonSubmit;
    Spinner spinnersport, spinnergender;
    DatabaseReference databaseRegister;


    public RegisterDetails(String regID, String mDisplayDate, String name, String phone, String gender, String sport)
    {
        this.regID = regID;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.sport = sport;
        this.date = mDisplayDate;
    }

    public String getName() {
        return name;
    }
    public String getPhone() {
        return phone;
    }
    public String getGender() {
        return gender;
    }
    public String getSport() {
        return sport;
    }
    public String getDate() {
        return date;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_details);
        FirebaseApp.initializeApp(this);
        databaseRegister = FirebaseDatabase.getInstance().getReference("RegDetails");
        editTextFullName = (EditText) findViewById(R.id.editTextFullName);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        buttonSubmit = (Button) findViewById(R.id.buttonDetails);
        spinnersport = (Spinner) findViewById(R.id.spinner1);
        spinnergender = (Spinner) findViewById(R.id.spinnergender);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRegisterDetails();
            }
        });


        ArrayAdapter<CharSequence> adaptersport = ArrayAdapter.createFromResource(this, R.array.sportcategory, android.R.layout.simple_spinner_item);
        adaptersport.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> adaptergender = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        adaptergender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnersport.setAdapter(adaptersport);
        spinnersport.setOnItemSelectedListener(this);
        String sport = spinnersport.getSelectedItem().toString();
        spinnergender.setAdapter(adaptergender);
        spinnergender.setOnItemSelectedListener(this);
        String gender = spinnersport.getSelectedItem().toString();

        mDisplayDate = (TextView) findViewById(R.id.tvDate);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        RegisterDetails.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyy: " + day + "/" + month + "/" + year);

                date = day + "/" + month + "/" + year;
                mDisplayDate.setText(date);
            }
        };

    }

    private void addRegisterDetails() {
        //get artistname and convert to string from editextname
        String name = editTextFullName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String sport = spinnersport.getSelectedItem().toString();
        String gender = spinnersport.getSelectedItem().toString();
        String date = String.valueOf(mDisplayDate);

        //check if the name is not empty
        if (!TextUtils.isEmpty(name) & !TextUtils.isEmpty(phone) & !TextUtils.isEmpty(sport)
        & !TextUtils.isEmpty(gender) & !TextUtils.isEmpty(date)) {
            //if exist push data to firebase database
            //store inside id in database
            //every time data stored the id will be unique
            String id = databaseRegister.push().getKey();
            //store
            RegisterDetails RegDetails = new RegisterDetails(id, name, phone, sport, gender, date);
            //store artist inside unique id
            databaseRegister.child(id).setValue(RegDetails);
            Toast.makeText(this, "Artist added", Toast.LENGTH_LONG).show();
        } else {
            //if the name is empty
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();

        }
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
