package edu.neu.madcourse.modernmath.assignments;

import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.neu.madcourse.modernmath.R;
import edu.neu.madcourse.modernmath.login.LoginClickListener;
import edu.neu.madcourse.modernmath.login.UserLoginCard;


public class StudentAssignmentRVAdaptor extends RecyclerView.Adapter<StudentAssignmentRVHolder> {

    private final ArrayList<StudentAssignmentCard> studentAssignmentList;
    private StudentAssignmentClickListener studentAssignmentClickListener;

    public StudentAssignmentRVAdaptor(ArrayList<StudentAssignmentCard> studentAssignmentList)
    {
        this.studentAssignmentList = studentAssignmentList;
    }

    public void setStudentAssignmentClickListener(StudentAssignmentClickListener listener)
    {
        this.studentAssignmentClickListener = listener;
    }

    @NonNull
    @Override
    public StudentAssignmentRVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_student_assignment_card, parent, false);
        return new StudentAssignmentRVHolder(view, this.studentAssignmentClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentAssignmentRVHolder viewHolder, int position) {
        if (studentAssignmentList.size() == 1 && studentAssignmentList.get(position).student_email == null) {
            viewHolder.student_email.setText("No Students");
            viewHolder.student_time_spent.setText("");
            viewHolder.student_num_correct.setText("");
            viewHolder.student_num_incorrect.setText("");
            return;
        }
        viewHolder.student_email.setText(studentAssignmentList.get(position).getEmail());

        int formatted_time_spent = studentAssignmentList.get(position).getTimeSpent() / 1000;

        viewHolder.student_time_spent.setText("Time spent: " + formatted_time_spent + " min");

        viewHolder.student_num_correct.setText("Number correct: " + Integer.toString(studentAssignmentList.get(position).getNumCorrect()));
        viewHolder.student_num_incorrect.setText("Number incorrect: " + Integer.toString(studentAssignmentList.get(position).getNumIncorrect()));
        if (studentAssignmentList.get(position).isComplete()) {
            viewHolder.itemView.setBackgroundColor(ActivityCompat.getColor(viewHolder.itemView.getContext(), R.color.teacher_complete));
            viewHolder.student_time_spent.setTextColor(Color.parseColor("black"));
            viewHolder.student_email.setTextColor(Color.parseColor("black"));
            viewHolder.student_num_correct.setTextColor(Color.parseColor("black"));
            viewHolder.student_num_incorrect.setTextColor(Color.parseColor("black"));
        } else {
            viewHolder.itemView.setBackgroundColor(ActivityCompat.getColor(viewHolder.itemView.getContext(), R.color.teacher_incomplete));
        }

    }

    @Override
    public int getItemCount()
    {
        return this.studentAssignmentList.size();
    }
}

