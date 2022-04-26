package edu.neu.madcourse.modernmath.teacher;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        if (assignmentListItems.size() == 1 && assignmentListItems.get(position).getOperators() == null) {
            holder.operators.setText("");
            holder.difficulty.setText("");
            holder.time_limit.setText("");
            holder.num_questions.setText("");
            return;
        }
        StringJoiner joiner = new StringJoiner(" ");
        for (Operator op : assignmentListItems.get(position).getOperators())
        {
            joiner.add(String.valueOf(op.value));
        }
        holder.operators.setText("Operators: " + joiner);

        holder.difficulty.setText("Difficulty: " + assignmentListItems.get(position).getDifficulty());

        if (assignmentListItems.get(position).getTime_limit() == 0)
        {
            holder.time_limit.setText("Time Limit: Timer off");
        }
        else
        {
            holder.time_limit.setText("Time Limit: " + assignmentListItems.get(position).getTime_limit());
        }

        if (assignmentListItems.get(position).getNum_questions() == 0)
        {
            holder.num_questions.setText("Number of questions: No target");
        }
        else
        {
            holder.num_questions.setText("Number of questions: " + assignmentListItems.get(position).getNum_questions());
        }

        if (assignmentListItems.get(position).isCompletion_status()) {
            holder.itemView.setBackgroundColor(Color.parseColor("green"));
        }
    }

    @Override
    public int getItemCount() {
        return assignmentListItems.size();
    }
}
