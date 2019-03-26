package com.wtillett.c196project;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wtillett.c196project.database.AppDatabase;
import com.wtillett.c196project.database.Assessment;
import com.wtillett.c196project.database.Course;
import com.wtillett.c196project.database.Term;

import java.util.List;

import static com.wtillett.c196project.TermActivity.TERM_ID;

public class GenericAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private AppDatabase db;
    private LayoutInflater inflater;
    private List<?> items;
    private final int TERM = 0, COURSE = 1, ASSESSMENT = 2;

    GenericAdapter(Context context, List<?> items) {
        this.items = items;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case TERM:
                itemView = inflater.inflate(R.layout.termlist_item, parent, false);
                return new TermViewHolder(itemView);
            case COURSE:
                itemView = inflater.inflate(R.layout.courselist_item, parent, false);
                return new CourseViewHolder(itemView);
            case ASSESSMENT:
                itemView = inflater.inflate(R.layout.assessmentlist_item, parent, false);
                return new AssessmentViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        Object current = items.get(position);
        switch (viewHolder.getItemViewType()) {
            case TERM:
                TermViewHolder termViewHolder = (TermViewHolder) viewHolder;
                termViewHolder.textView.setText(current.toString());
                break;
            case COURSE:
                CourseViewHolder courseViewHolder = (CourseViewHolder) viewHolder;
                courseViewHolder.textView.setText(current.toString());
                break;
            case ASSESSMENT:
                AssessmentViewHolder assessmentViewHolder = (AssessmentViewHolder) viewHolder;
                assessmentViewHolder.textView.setText(current.toString());
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof Term)
            return TERM;
        else if (items.get(position) instanceof Course)
            return COURSE;
        else if (items.get(position) instanceof Assessment)
            return ASSESSMENT;
        return -1;
    }

    void setDb(AppDatabase db) {
        this.db = db;
    }

    class TermViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView textView;

        TermViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.term);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            Term term = (Term) items.get(position);
            Context context = v.getContext();
            Intent intent = new Intent(context, TermDetailActivity.class);
            intent.putExtra(TERM_ID, term.id);
            context.startActivity(intent);
        }
    }

    private class CourseViewHolder extends RecyclerView.ViewHolder {
        final TextView textView;

        CourseViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.course);
        }

        // TODO: Add OnClickListener to CourseViewHolder
    }

    private class AssessmentViewHolder extends RecyclerView.ViewHolder {
        final TextView textView;

        AssessmentViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.assessment);
        }

        // TODO: Add OnClickListener to AssessmentViewHolder
    }
}
