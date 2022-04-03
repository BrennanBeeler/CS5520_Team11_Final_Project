package edu.neu.madcourse.modernmath.teacher;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import edu.neu.madcourse.modernmath.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ClassListRVHolder extends RecyclerView.ViewHolder {
    public TextView className;
    public TextView classPeriod;
    public TextView classCode;
    public ImageView logo;

    public ClassListRVHolder(@NonNull View itemView, ClassListClickListener clickListener) {
        super(itemView);
        className = itemView.findViewById(R.id.class_name);
        classPeriod = itemView.findViewById(R.id.class_period);
        classCode = itemView.findViewById(R.id.class_code);
        logo = itemView.findViewById(R.id.class_icon);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clickListener != null) {
                    int position = getLayoutPosition();
                    if(position != RecyclerView.NO_POSITION) {
                        clickListener.onClassClick(position);
                    }
                }
            }
        });
    }
}
