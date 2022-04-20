package edu.neu.madcourse.modernmath.teacher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.neu.madcourse.modernmath.R;

public class AssignmentListRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_assignment_card, parent, false);
        return new AssignmentListHolder(view, listener);    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (!assignmentListItems.get(position).isAdd()) {
            ((AssignmentListHolder) holder).add.setText("");
        }
        if (!assignmentListItems.get(position).isSubtract()) {
            ((AssignmentListHolder) holder).subtract.setText("");
        }
        if (!assignmentListItems.get(position).isMultiply()) {
            ((AssignmentListHolder) holder).multiply.setText("");
        }
        if (!assignmentListItems.get(position).isDivide()) {
            ((AssignmentListHolder) holder).divide.setText("");
        }

        ((AssignmentListHolder) holder).title.setText(assignmentListItems.get(position).getTitle());
        ((AssignmentListHolder) holder).numQuestions.setText(assignmentListItems.get(position).getNumQuestions());
        ((AssignmentListHolder) holder).difficulty.setText(assignmentListItems.get(position).getDifficulty());
    }

    @Override
    public int getItemCount() {
        return assignmentListItems.size();
    }

    public class AssignmentListHolder extends RecyclerView.ViewHolder {
        public TextView add;
        public TextView subtract;
        public TextView multiply;
        public TextView divide;
        public TextView title;
        public TextView difficulty;
        public TextView numQuestions;

        public AssignmentListHolder(@NonNull View itemView, AssignmentListClickListener clickListener) {
            super(itemView);

            add = itemView.findViewById(R.id.assignment_add);
            subtract = itemView.findViewById(R.id.assignment_subtract);
            multiply = itemView.findViewById(R.id.assignment_multiply);
            divide = itemView.findViewById(R.id.assignment_divide);
            title = itemView.findViewById(R.id.assignment_title);
            difficulty = itemView.findViewById(R.id.assignment_difficulty);
            numQuestions = itemView.findViewById(R.id.assignment_num_questions);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(clickListener != null) {
                        int position = getLayoutPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            clickListener.onAssignmentClick(position);
                        }
                    }
                }
            });
        }
    }
}
