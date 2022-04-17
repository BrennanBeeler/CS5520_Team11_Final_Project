package edu.neu.madcourse.modernmath.teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.neu.madcourse.modernmath.MainActivity;
import edu.neu.madcourse.modernmath.R;
import edu.neu.madcourse.modernmath.database.User;

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
        Bundle extras = getIntent().getExtras();
        User teacher = extras.getParcelable("active_user");
        String teacher_name = teacher.firstName + " " + teacher.lastName;
        teacherName.setText(teacher_name);

        // populate list of classes
        this.myDatabase = FirebaseDatabase.getInstance().getReference();

        layoutManager = new LinearLayoutManager(this);
        classListRV = findViewById(R.id.class_list);
        classListRV.setHasFixedSize(true);
        adapter = new ClassListRVAdapter(classList);

        /*
        // TODO: move this recyclerview stuff to a different thread
        // TODO: make the database call actually work
        this.myDatabase.child("users").child(teacher.email).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                classList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String className = dataSnapshot.child("className").getValue().toString();
                    String classPeriod = dataSnapshot.child("classPeriod").getValue().toString();
                    String classCode = dataSnapshot.child("classCode").getValue().toString();
                    int logoID = (int) dataSnapshot.child("logoID").getValue();

                    classList.add(new ClassListItem(className, classPeriod, classCode, logoID));
                }

                adapter.notifyDataSetChanged();
                classListRV.scrollToPosition(classList.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Firebase Error", "Failed to get access ");
            }
        });
*/
        ClassListClickListener i = new ClassListClickListener() {
            @Override
            public void onClassClick(int position) {
                Intent intent = new Intent(TeacherClassList.this, TeacherListStudents.class );
                intent.putExtra("class_code", classList.get(position).getClassCode());
                intent.putExtra("teacher_name", teacher);
                startActivity(intent);
            }
        };
        adapter.setOnItemClickListener(i);

        classListRV.setAdapter(adapter);
        classListRV.setLayoutManager(layoutManager);


        addClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeacherClassList.this, AddNewClass.class);
                intent.putExtra("active_user", teacher);
                startActivity(intent);
            }
        });
    }
}