package id.rumahkoding.sekolahku.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import id.rumahkoding.sekolahku.R;
import id.rumahkoding.sekolahku.modul.Student;

public class StudentListAdapter extends ArrayAdapter<Student> {
    public StudentListAdapter(Context context, List<Student> objects) {
        super(context, R.layout.student_item, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item, null);
        }

        TextView textViewName = convertView.findViewById(R.id.textview_name);
        TextView textViewGender = convertView.findViewById(R.id.textview_gender);
        TextView textViewEducation = convertView.findViewById(R.id.textview_education);
        TextView textViewPhone = convertView.findViewById(R.id.textview_phone);

        Student student = getItem(position);

        textViewName.setText(String.format("%s %s", student.getFirstName(), student.getLastName()));
        textViewGender.setText(student.getGender());
        textViewEducation.setText(student.getEducation());
        textViewPhone.setText(student.getPhone());

        return convertView;
    }
}
