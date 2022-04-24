package edu.neu.madcourse.modernmath.teacher.studentassignments;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import edu.neu.madcourse.modernmath.R;
import edu.neu.madcourse.modernmath.assignments.StudentAssignmentClickListener;

public class StudentAssignment_AssignmentNameRVHolder extends RecyclerView.ViewHolder {
    public TextView assignment_name;
    public TextView student_time_spent;
    public TextView student_num_correct;
    public TextView student_num_incorrect;

    public StudentAssignment_AssignmentNameRVHolder(View cardView)
    {
        super(cardView);
        this.assignment_name = cardView.findViewById(R.id.assignment_name);
        this.student_time_spent = cardView.findViewById(R.id.student_time_spent);
        this.student_num_correct = cardView.findViewById(R.id.student_num_correct);
        this.student_num_incorrect = cardView.findViewById(R.id.student_num_incorrect);
    }
}
