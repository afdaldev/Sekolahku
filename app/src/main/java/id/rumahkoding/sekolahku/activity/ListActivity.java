package id.rumahkoding.sekolahku.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        studentListView = findViewById(R.id.listview_student);
        searchView = findViewById(R.id.search_view);

        registerForContextMenu(studentListView);

        studentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Student student = students.get(position);

                Intent intent = new Intent(ListActivity.this, DetailActivity.class);
                intent.putExtra("student", student);
                startActivity(intent);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String keyword) {
                students.clear();

                DatabaseManager dbManager = new DatabaseManager(ListActivity.this);
                dbManager.open();

                students.addAll(dbManager.searchStudentByName(keyword));

                adapter.notifyDataSetChanged();
                return false;
            }
        });
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();

        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final int position = menuInfo.position;

        final Student student = students.get(position);

        switch (id){
            case R.id.update_menu:
                Intent intent = new Intent(ListActivity.this, FormActivity.class);
                intent.putExtra("id", student.getId());
                startActivity(intent);

            break;

            case R.id.delete_menu:
            new AlertDialog.Builder(this)
                    .setTitle("Hapus Siswa")
                    .setMessage("Anda Yakin Ingin Menghapus ?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteSiswa(student, position);
                        }
                    })
                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();

            break;
        }

        return super.onContextItemSelected(item);
    }

    private void deleteSiswa(Student student, int position) {
        DatabaseManager dbManager = new DatabaseManager(ListActivity.this);
        dbManager.open();

        dbManager.deleteStudentById(student.getId());
        dbManager.close();
        students.remove(position);
        adapter.notifyDataSetChanged();
    }
}