package id.rumahkoding.sekolahku.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.List;

import id.rumahkoding.sekolahku.R;
import id.rumahkoding.sekolahku.adapter.StudentListAdapter;
import id.rumahkoding.sekolahku.database.DatabaseManager;
import id.rumahkoding.sekolahku.modul.Student;

public class ListActivity extends AppCompatActivity {

    private ListView studentListView;
    private List<Student>students;
    private StudentListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        studentListView = findViewById(R.id.listview_student);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getStudentData();
    }

    private void getStudentData() {
        DatabaseManager dbManager = new DatabaseManager(this);
        dbManager.open();

        students = dbManager.getAllStudent();
        adapter = new StudentListAdapter(this, students);
        studentListView.setAdapter(adapter);

        dbManager.close();
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
            Intent intent = new Intent(this, FormActivity.class);
            startActivity(intent);
        }

        if (id == R.id.delete){
            new AlertDialog.Builder(this)
                    .setTitle("Hapus Semua Data")
                    .setMessage("Anda Yakin ?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DatabaseManager dbManager = new DatabaseManager(ListActivity.this);
                            dbManager.open();
                            dbManager.deleteAll();
                            dbManager.close();
                            students.clear();
                            adapter.notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }
}