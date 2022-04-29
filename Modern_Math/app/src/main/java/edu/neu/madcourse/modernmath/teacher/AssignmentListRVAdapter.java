package edu.neu.madcourse.modernmath.teacher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.StringJoiner;

import edu.neu.madcourse.modernmath.R;
import edu.neu.madcourse.modernmath.assignments.Operator;

public class AssignmentListRVAdapter extends RecyclerView.Adapter<AssignmentListHolder> {
    final private ArrayList<AssignmentListItem> assignmentListItems;
    private AssignmentListClickListener listener;
    private boolean teacherpage;

    public AssignmentListRVAdapter(ArrayList<AssignmentListItem> assignmentListItems) {
        this.assignmentListItems = assignmentListItems;
        this.teacherpage = false;
    }

    public void setListener(AssignmentListClickListener listener) {
        this.listener = listener;
    }

    public void setTeacherpage(boolean teacherpage) {
        this.teacherpage = teacherpage;
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
            int min = (int) (assignmentListItems.get(position).getTime_limit() / 60000);
            int seconds = (int) ((assignmentListItems.get(position).getTime_limit() % 60000) / 1000);
            String time = "";
            if (min > 0) {
                time = String.valueOf(min) + " min ";
            }
            if (seconds > 0) {
                time += String.valueOf(seconds) + " seconds";
            }
            holder.time_limit.setText("Time Limit: " + time);
        }

        if (assignmentListItems.get(position).getNum_questions() == 0)
        {
            holder.num_questions.setText("Number of questions: No target");
        }
        else
        {
            holder.num_questions.setText("Number of questions: " + assignmentListItems.get(position).getNum_questions());
        }

        if (!assignmentListItems.get(position).isCompletion_status() && !teacherpage) {
            holder.itemView.setBackgroundColor( ActivityCompat.getColor(holder.itemView.getContext(), R.color.student_incomplete));;
        }
    }

    @Override
    public int getItemCount() {
        return assignmentListItems.size();
    }
}
