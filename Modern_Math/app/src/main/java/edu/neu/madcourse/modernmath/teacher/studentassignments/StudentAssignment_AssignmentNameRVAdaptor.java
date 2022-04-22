package edu.neu.madcourse.modernmath.teacher.studentassignments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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
        // TODO: format time spent based on ultimate time format
        viewHolder.assignment_name.setText(studentAssignmentList.get(position).getAssignment_name());
        viewHolder.student_time_spent.setText("Time spent: " + studentAssignmentList.get(position).getTimeSpent());
        viewHolder.student_num_correct.setText("Number correct: " + Integer.toString(studentAssignmentList.get(position).getNumCorrect()));
        viewHolder.student_num_incorrect.setText("Number incorrect: " + Integer.toString(studentAssignmentList.get(position).getNumIncorrect()));
    }

    @Override
    public int getItemCount()
    {
        return this.studentAssignmentList.size();
    }
}

