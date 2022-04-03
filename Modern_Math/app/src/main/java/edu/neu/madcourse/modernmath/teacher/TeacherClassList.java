package edu.neu.madcourse.modernmath.teacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import edu.neu.madcourse.modernmath.R;

public class TeacherClassList extends AppCompatActivity {
    private FloatingActionButton addClass;
    private RecyclerView classListRV;
    private ClassListRVAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    final private ArrayList<ClassListItem> classList = new ArrayList<>();
    private DatabaseReference myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_class_list);
        TextView teacherName = findViewById(R.id.teacher_name);
        addClass = findViewById(R.id.addClass);

        // set teacher name
        String teacher = getIntent().getStringExtra("teacher_name");
        teacherName.setText(teacher);

        // populate list of classes
        this.myDatabase = FirebaseDatabase.getInstance().getReference();

        layoutManager = new LinearLayoutManager(this);
        classListRV = findViewById(R.id.class_list);
        classListRV.setHasFixedSize(true);

        // TODO: move this recyclerview stuff to a different thread
        // TODO: make the database call actually work
        DataSnapshot snapshot = null;
        for (DataSnapshot dataSnapshot : snapshot.getChildren())
        {
            String className = dataSnapshot.child("className").getValue().toString();
            String classPeriod = dataSnapshot.child("classPeriod").getValue().toString();
            String classCode = dataSnapshot.child("classCode").getValue().toString();
            int logoID = (int)dataSnapshot.child("logoID").getValue();

            classList.add(new ClassListItem(className, classPeriod, classCode, logoID));
        }

        adapter = new ClassListRVAdapter(classList);

        ClassListClickListener i = new ClassListClickListener() {
            @Override
            public void onClassClick(int position) {
                Intent intent = new Intent(TeacherClassList.this, TeacherListStudents.class );
                intent.putExtra("class_code", classList.get(position).getClassCode());
                startActivity(intent);
            }
        };
        adapter.setOnItemClickListener(i);

        classListRV.setAdapter(adapter);
        classListRV.setLayoutManager(layoutManager);

        // listener for adding a class
        addClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: open a new activity for this or something...
                String className = "";
                String classPeriod = "";
                String classCode = "";
                int logoID = 0;

                classList.add(new ClassListItem(className, classPeriod, classCode, logoID));
                adapter.notifyItemInserted(classList.size()-1);
            }
        });
    }
}