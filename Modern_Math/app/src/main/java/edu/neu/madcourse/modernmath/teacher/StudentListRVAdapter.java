package edu.neu.madcourse.modernmath.teacher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.neu.madcourse.modernmath.R;

public class StudentListRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final private ArrayList<StudentListItem> studentListItems;
    private StudentClickListener listener;

    public StudentListRVAdapter(ArrayList<StudentListItem> studentListItems) {
        this.studentListItems = studentListItems;
    }

    public void setListener(StudentClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_student_card, parent, false);
        return new StudentListHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((StudentListHolder) holder).name.setText(studentListItems.get(position).getName());
        ((StudentListHolder) holder).username.setText(studentListItems.get(position).getUsername());
    }

    @Override
    public int getItemCount() {
        return studentListItems.size();
    }

    public class StudentListHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView username;

        public StudentListHolder(@NonNull View itemView, StudentClickListener clickListener) {
            super(itemView);

            name = itemView.findViewById(R.id.student_name_card);
            username = itemView.findViewById(R.id.student_username_card);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(clickListener != null) {
                        int position = getLayoutPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            clickListener.onStudentClick(position);
                        }
                    }
                }
            });
        }
    }
}
