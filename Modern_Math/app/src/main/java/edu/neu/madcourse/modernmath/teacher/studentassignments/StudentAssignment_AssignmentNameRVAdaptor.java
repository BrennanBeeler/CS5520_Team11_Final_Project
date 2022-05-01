package edu.neu.madcourse.modernmath.teacher.studentassignments;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.neu.madcourse.modernmath.R;
import edu.neu.madcourse.modernmath.assignments.StudentAssignmentCard;
import edu.neu.madcourse.modernmath.assignments.StudentAssignmentClickListener;
import edu.neu.madcourse.modernmath.assignments.StudentAssignmentRVHolder;


public class StudentAssignment_AssignmentNameRVAdaptor extends RecyclerView.Adapter<StudentAssignment_AssignmentNameRVHolder> {

    private final ArrayList<StudentAssignmentCard_AssignmentName> studentAssignmentList;

    public StudentAssignment_AssignmentNameRVAdaptor(ArrayList<StudentAssignmentCard_AssignmentName> studentAssignmentList)
    {
        this.studentAssignmentList = studentAssignmentList;
    }

    @NonNull
    @Override
    public StudentAssignment_AssignmentNameRVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_student_assignment_card_assignment_name, parent, false);
        return new StudentAssignment_AssignmentNameRVHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentAssignment_AssignmentNameRVHolder viewHolder, int position) {
        if (studentAssignmentList.size() == 1 && studentAssignmentList.get(position).getAssignment_name() == null) {
            viewHolder.assignment_name.setText("No assignments yet!");
            viewHolder.student_time_spent.setText("");
            viewHolder.student_num_correct.setText("");
            viewHolder.student_num_incorrect.setText("");
            return;
        }
        viewHolder.assignment_name.setText(studentAssignmentList.get(position).getAssignment_name());

        int formatted_time_spent = studentAssignmentList.get(position).getTimeSpent() / 1000 / 60;

        viewHolder.student_time_spent.setText("Time spent: " + formatted_time_spent + " min");
        viewHolder.student_num_correct.setText("Number correct: " + studentAssignmentList.get(position).getNumCorrect());
        viewHolder.student_num_incorrect.setText("Number incorrect: " + studentAssignmentList.get(position).getNumIncorrect());

        if (studentAssignmentList.get(position).isCompletion_status()) {
            viewHolder.itemView.setBackgroundColor(ActivityCompat.getColor(viewHolder.itemView.getContext(), R.color.teacher_complete));
            viewHolder.student_time_spent.setTextColor(Color.parseColor("black"));
            viewHolder.assignment_name.setTextColor(Color.parseColor("black"));
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

