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
import com.wtillett.c196project.database.Mentor;
import com.wtillett.c196project.database.Term;

import java.util.List;


public class GenericAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater inflater;
    private List<?> items;
    private final int TERM = 0, COURSE = 1, ASSESSMENT = 2, MENTOR = 3;

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
            case MENTOR:
                itemView = inflater.inflate(R.layout.mentorlist_item, parent, false);
                return new MentorViewHolder(itemView);
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
            case MENTOR:
                MentorViewHolder mentorViewHolder = (MentorViewHolder) viewHolder;
                mentorViewHolder.textView.setText(current.toString());
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
        else if (items.get(position) instanceof Mentor)
            return MENTOR;
        return -1;
    }

    void setDb(AppDatabase db) {
        AppDatabase db1 = db;
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
            intent.putExtra(TermDetailActivity.TERM_ID, term.id);
            context.startActivity(intent);
        }
    }

    private class CourseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView textView;

        CourseViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.course);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            Course course = (Course) items.get(position);
            Context context = v.getContext();
            Intent intent = new Intent(context, CourseDetailActivity.class);
            intent.putExtra(CourseDetailActivity.COURSE_ID, course.id);
            context.startActivity(intent);
        }
    }

    private class AssessmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView textView;

        AssessmentViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.assessment);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            Assessment assessment = (Assessment) items.get(position);
            Context context = v.getContext();
            Intent intent = new Intent(context, AssessmentDetailActivity.class);
            intent.putExtra(AssessmentDetailActivity.ASSESSMENT_ID, assessment.id);
            context.startActivity(intent);
        }
    }

    private class MentorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView textView;

        MentorViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.mentor);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            Mentor mentor = (Mentor) items.get(position);
            Context context = v.getContext();
            Intent intent = new Intent(context, MentorDetailActivity.class);
            intent.putExtra(MentorDetailActivity.MENTOR_ID, mentor.id);
            context.startActivity(intent);
        }
    }
}
