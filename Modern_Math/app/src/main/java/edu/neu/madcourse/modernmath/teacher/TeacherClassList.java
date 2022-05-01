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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.neu.madcourse.modernmath.R;
import edu.neu.madcourse.modernmath.database.User;
import edu.neu.madcourse.modernmath.leadershipboard.LeadershipActivity;

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
        addClass = findViewById(R.id.addClass);

        setSupportActionBar(findViewById(R.id.class_list_toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            //actionBar.setIcon(R.mipmap.ic_launcher_mm_round);
            actionBar.setTitle("Class List");
            actionBar.setDisplayShowTitleEnabled(true);
        }

        // set teacher name
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Toast.makeText(TeacherClassList.this,
                    "No teacher specified.", Toast.LENGTH_SHORT)
                    .show();

            finish();
        }
        teacher = extras.getParcelable("active_user");

        // populate list of classes
        this.myDatabase = FirebaseDatabase.getInstance().getReference();

        layoutManager = new LinearLayoutManager(this);
        classListRV = findViewById(R.id.class_list);
        classListRV.setHasFixedSize(true);
        adapter = new ClassListRVAdapter(classList);
        classListRV.setAdapter(adapter);
        classListRV.setLayoutManager(layoutManager);

        this.myDatabase.child("classes").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                classList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("teacher_username").getValue().toString().equals(teacher.username)) {
                        String className = dataSnapshot.child("class_title").getValue().toString();
                        String classPeriod = dataSnapshot.child("class_period").getValue().toString();
                        String classCode = dataSnapshot.getKey();
                        int logoID = 1;

                        classList.add(new ClassListItem(className, classPeriod, classCode, logoID));
                    }
                }
                // Placeholder if no classes
                if (classList.size() == 0) {
                    String className = "No active classes";
                    classList.add(new ClassListItem(className, "", "", 0));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Firebase Error", "Failed to get access ");
            }
        });

        ClassListClickListener i = position -> {
            // Do nothing for placeholder
            if(classList.get(position).getClassName().equals("No active classes")) {
                return;
            }
            Intent intent = new Intent(TeacherClassList.this, TeacherViewClassDetails.class );
            intent.putExtra("class_code", classList.get(position).getClassCode());
            intent.putExtra("class_title", classList.get(position).getClassName());
            intent.putExtra("active_user", teacher);
            startActivity(intent);
        };
        adapter.setOnItemClickListener(i);



        addClass.setOnClickListener(view -> {
            Intent intent = new Intent(TeacherClassList.this, AddNewClass.class);
            intent.putExtra("active_user", teacher);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.leader_menu_item, menu);
        menu.findItem(R.id.name).setTitle(teacher.firstName);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.leader:
                Intent intent = new Intent(TeacherClassList.this, LeadershipActivity.class);
                intent.putExtra("active_user", this.teacher);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}