package edu.neu.madcourse.modernmath.teacher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.neu.madcourse.modernmath.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ClassListRVAdapter extends RecyclerView.Adapter<ClassListRVHolder> {
    final private ArrayList<ClassListItem> classListItems;
    private ClassListClickListener listener;

    public ClassListRVAdapter(ArrayList<ClassListItem> classListItems) {
        this.classListItems = classListItems;
    }

    @NonNull
    @Override
    public ClassListRVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_class_card, parent, false);
        return new ClassListRVHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassListRVHolder holder, int position) {
        ClassListItem item = classListItems.get(position);

        holder.classCode.setText("Class code: " + item.getClassCode());
        holder.classPeriod.setText("Class period:" + item.getClassPeriod());
        holder.className.setText("Class name: " + item.getClassName());
        int image = getImageResource(item.getLogoID());
        holder.logo.setImageResource(image);
    }

    private int getImageResource(int logoID) {
        return R.mipmap.ic_launcher_mm_round; //
    }

    @Override
    public int getItemCount() {
        return classListItems.size();
    }

    public void setOnItemClickListener(ClassListClickListener listener) {
        this.listener = listener;
    }
}
