package id.rumahkoding.sekolahku.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.regex.Pattern;

import id.rumahkoding.sekolahku.R;
import id.rumahkoding.sekolahku.database.DatabaseManager;
import id.rumahkoding.sekolahku.modul.Student;

public class FormActivity extends AppCompatActivity {

    private EditText edtFirstName, edtLastName, edtPhone, edtEmail, edtAddress;
    private RadioGroup radioGroup;
    private Spinner spinnerStudy;
    private CheckBox checkBoxWriting, checkBoxReading, checkBoxCoding;
    private Button btnSave;
    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        edtFirstName = findViewById(R.id.edt_first_name);
        edtLastName = findViewById(R.id.edt_last_name);
        edtPhone = findViewById(R.id.edt_phone);
        edtEmail = findViewById(R.id.edt_email);
        radioGroup = findViewById(R.id.radio_group_gender);
        spinnerStudy = findViewById(R.id.spinner_study);
        checkBoxWriting = findViewById(R.id.checkbox_writing);
        checkBoxReading = findViewById(R.id.checkbox_reading);
        checkBoxCoding = findViewById(R.id.checkbox_coding);
        edtAddress = findViewById(R.id.edt_address);
        btnSave = findViewById(R.id.btn_save);

        databaseManager = new DatabaseManager(this);
        databaseManager.open();

        btnSave.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addStudent();
                    }
                }
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseManager.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_form, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.profile){
            Intent intent = new Intent(this, ListActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void addStudent(){
        boolean isValid = isValidateForm();
        String firstName = edtFirstName.getText().toString();
        String lastName = edtLastName.getText().toString();
        String phone = edtPhone.getText().toString();
        String email = edtEmail.getText().toString();
        String address = edtAddress.getText().toString();
        int checkedGenderId = radioGroup.getCheckedRadioButtonId();

        String gender = "";
        String hobby = "";
        String education = spinnerStudy.getSelectedItem().toString();

        if (checkedGenderId == R.id.radio_man){
            gender = "Pria";
        } else if (checkedGenderId == R.id.radio_woman){
            gender = "Wanita";
        } else {
            return;
        }

        if (checkBoxReading.isChecked()){
            hobby += "Membaca,";
        }

        if (checkBoxWriting.isChecked()){
            hobby += "Menulis,";
        }

        if (checkBoxCoding.isChecked()){
            hobby += "Coding";
        }

        Student student = new Student();
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setEmail(email);
        student.setPhone(phone);
        student.setGender(gender);
        student.setEducation(education);
        student.setHobby(hobby);
        student.setAddress(address);

        if (!isValid){
            isValidateForm();
        }else {
            long id = databaseManager.addStudent(student);

            if (id == -1){
                Toast.makeText(this, "Data gagal disimpan", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Data Berhasil Disimpan dengan id "+id, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private boolean isValidateForm(){

        boolean isValid = false;

        if (edtFirstName.getText().toString().isEmpty()){
            edtFirstName.setError("Nama Depan Harus Diisi");
            return false;
        }
        if (edtLastName.getText().toString().isEmpty()){
            edtLastName.setError("Nama Belakang Harus Diisi");
            return false;
        }
        if (!isValidName(edtFirstName.getText().toString())){
            edtFirstName.setError("Invalid First Name");
            return false;
        }
        if (!isValidName(edtLastName.getText().toString())){
            edtLastName.setError("Invalid Last Name");
            return false;
        }
        if (!isValidEmail(edtEmail.getText().toString())){
            edtEmail.setError("Invalid Email");
            return false;
        }
        if (!isValidPhone(edtPhone.getText().toString())){
            edtPhone.setError("Invalid Phone Number");
            return false;
        }

        isValid = true;
        return isValid;
    }

    private boolean isValidEmail(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPhone(String phone){
        return phone.length() < 10;
    }

    private boolean isValidName(String name){
        final String NAME_PATTERN = "[a-zA-Z.?]*";
        return Pattern.compile(NAME_PATTERN).matcher(name).matches();
    }
}