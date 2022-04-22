package edu.neu.madcourse.modernmath.teacher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.StringJoiner;

import edu.neu.madcourse.modernmath.R;
import edu.neu.madcourse.modernmath.assignments.Operator;

public class AssignmentListRVAdapter extends RecyclerView.Adapter<AssignmentListHolder> {
    final private ArrayList<AssignmentListItem> assignmentListItems;
    private AssignmentListClickListener listener;

    public AssignmentListRVAdapter(ArrayList<AssignmentListItem> assignmentListItems) {
        this.assignmentListItems = assignmentListItems;
    }

    public void setListener(AssignmentListClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public AssignmentListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_assignment_card, parent, false);
        return new AssignmentListHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentListHolder holder, int position) {
        holder.title.setText(assignmentListItems.get(position).getTitle());

        StringJoiner joiner = new StringJoiner(" ");
        for (Operator op : assignmentListItems.get(position).getOperators())
        {
            joiner.add(String.valueOf(op.value));
        }
        holder.operators.setText("Operators: " + joiner);

        holder.difficulty.setText("Difficulty: " + assignmentListItems.get(position).getDifficulty());
        holder.time_limit.setText("Time Limit: " + assignmentListItems.get(position).getTime_limit());
        holder.num_questions.setText("Number of questions: " + assignmentListItems.get(position).getNum_questions());
    }

    @Override
    public int getItemCount() {
        return assignmentListItems.size();
    }
}
