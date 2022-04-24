package edu.neu.madcourse.modernmath.teacher;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.neu.madcourse.modernmath.R;

public class AssignmentListHolder extends RecyclerView.ViewHolder {
    public TextView operators;
    public TextView title;
    public TextView difficulty;
    public TextView num_questions;
    public TextView time_limit;

    public AssignmentListHolder(@NonNull View itemView, AssignmentListClickListener clickListener) {
        super(itemView);

        this.operators = itemView.findViewById(R.id.operators);
        this.title = itemView.findViewById(R.id.assignment_name);
        this.difficulty = itemView.findViewById(R.id.assignment_difficulty);
        this.num_questions = itemView.findViewById(R.id.assignment_num_questions);
        this.time_limit = itemView.findViewById(R.id.assignment_time_limit);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener != null) {
                    int position = getLayoutPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        clickListener.onAssignmentClick(position);
                    }
                }
            }
        });
    }
}
