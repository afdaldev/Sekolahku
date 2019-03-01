package id.rumahkoding.sekolahku.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import id.rumahkoding.sekolahku.R;
import id.rumahkoding.sekolahku.modul.Student;

public class DetailActivity extends AppCompatActivity {
    private ImageView imageHeader, imageName;
    private TextView textview_name, textview_phone, textview_jenis_kelamin, textview_pendidikan,
            textview_hobby, textview_alamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (getSupportActionBar() != null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageHeader = findViewById(R.id.image_header);
        imageName = findViewById(R.id.image_name);
        textview_name = findViewById(R.id.textview_name);
        textview_phone = findViewById(R.id.textview_phone);
        textview_jenis_kelamin = findViewById(R.id.textview_jenis_kelamin);
        textview_pendidikan = findViewById(R.id.textview_pendidikan);
        textview_hobby = findViewById(R.id.textview_hobby);
        textview_alamat = findViewById(R.id.textview_alamat);

        Student student = getIntent().getParcelableExtra("student");
        textview_name.setText(String.format("%s %S", student.getFirstName(), student.getLastName()));
        textview_phone.setText(String.format(student.getPhone()));
        textview_jenis_kelamin.setText(String.format(student.getGender()));
        textview_pendidikan.setText(String.format(student.getEducation()));
        textview_hobby.setText(String.format(student.getHobby()));
        textview_alamat.setText(String.format(student.getAddress()));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
