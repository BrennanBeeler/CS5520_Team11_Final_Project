package edu.neu.madcourse.modernmath.assignments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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
        viewHolder.student_email.setText(studentAssignmentList.get(position).getEmail());
        viewHolder.student_time_spent.setText(studentAssignmentList.get(position).getTimeSpent());
        viewHolder.student_num_correct.setText(Integer.toString(studentAssignmentList.get(position).getNumCorrect()));
        viewHolder.student_num_incorrect.setText(Integer.toString(studentAssignmentList.get(position).getNumIncorrect()));
    }

    @Override
    public int getItemCount()
    {
        return this.studentAssignmentList.size();
    }
}

