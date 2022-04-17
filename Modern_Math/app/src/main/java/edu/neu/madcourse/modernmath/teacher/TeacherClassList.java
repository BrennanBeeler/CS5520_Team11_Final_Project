package edu.neu.madcourse.modernmath.teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
    private User teacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_class_list);
        TextView teacherName = findViewById(R.id.teacher_name);
        addClass = findViewById(R.id.addClass);

        setSupportActionBar(findViewById(R.id.class_list_toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setIcon(R.mipmap.ic_launcher_mm_round);
        }

        // set teacher name
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Toast.makeText(TeacherClassList.this,
                    "No teacher specified.", Toast.LENGTH_SHORT)
                    .show();
            Log.d("TeacherClass", "no teacher");
            finish();
        }
        Log.d("TeacherClass", "getting teacher name");
        teacher = extras.getParcelable("active_user");
        teacherName.setText(teacher.firstName + " " + teacher.lastName);

        // populate list of classes
        this.myDatabase = FirebaseDatabase.getInstance().getReference();

        layoutManager = new LinearLayoutManager(this);
        classListRV = findViewById(R.id.class_list);
        classListRV.setHasFixedSize(true);
        adapter = new ClassListRVAdapter(classList);
        classListRV.setAdapter(adapter);
        classListRV.setLayoutManager(layoutManager);


        // TODO: move this recyclerview stuff to a different thread
        // TODO: make the database call actually work
        this.myDatabase.child("classes").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                classList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("teacher_email").getValue().toString().equals(teacher.email)) {
                        String className = "Class: " +
                                dataSnapshot.child("class_title").getValue().toString();
                        String classPeriod = "Period: " +
                                dataSnapshot.child("class_period").getValue().toString();
                        String classCode = "Class Code: " + dataSnapshot.getKey();
                        int logoID = 1; // TODO: Either remove or make the option to select icons

                        classList.add(new ClassListItem(className, classPeriod, classCode, logoID));
                    }
                }

                adapter.notifyDataSetChanged();
                classListRV.scrollToPosition(0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Firebase Error", "Failed to get access ");
            }
        });

        ClassListClickListener i = new ClassListClickListener() {
            @Override
            public void onClassClick(int position) {
                Intent intent = new Intent(TeacherClassList.this, TeacherViewClassDetails.class );
                intent.putExtra("class_code", classList.get(position).getClassCode());
                intent.putExtra("active_user", teacher);
                startActivity(intent);
            }
        };
        adapter.setOnItemClickListener(i);



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