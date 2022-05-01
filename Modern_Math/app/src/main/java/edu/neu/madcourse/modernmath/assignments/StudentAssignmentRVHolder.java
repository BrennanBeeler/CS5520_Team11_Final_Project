package edu.neu.madcourse.modernmath.assignments;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import edu.neu.madcourse.modernmath.R;

public class StudentAssignmentRVHolder extends RecyclerView.ViewHolder {
    public TextView student_username;
    public TextView student_time_spent;
    public TextView student_num_correct;
    public TextView student_num_incorrect;

    public StudentAssignmentRVHolder(View cardView, final StudentAssignmentClickListener studentAssignmentClickListener)
    {
        super(cardView);
        this.student_username = cardView.findViewById(R.id.student_username);
        this.student_time_spent = cardView.findViewById(R.id.student_time_spent);
        this.student_num_correct = cardView.findViewById(R.id.student_num_correct);
        this.student_num_incorrect = cardView.findViewById(R.id.student_num_incorrect);
    }
}
