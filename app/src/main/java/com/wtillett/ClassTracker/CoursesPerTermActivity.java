package com.wtillett.ClassTracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.wtillett.ClassTracker.database.AppDatabase;
import com.wtillett.ClassTracker.database.Course;
import com.wtillett.ClassTracker.database.Term;

import java.util.ArrayList;

public class CoursesPerTermActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses_per_term);

        TextView selectTerm = findViewById(R.id.selectTerm);
        TextView noTermsFound = findViewById(R.id.noTermsFound);
        Spinner spinner = findViewById(R.id.termSpinner);

        db = AppDatabase.getDatabase(getApplicationContext());
        ArrayList<Term> terms = new ArrayList<>(db.appDao().getAllTerms());

        if (terms.size() == 0) {
            selectTerm.setVisibility(View.INVISIBLE);
            noTermsFound.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.INVISIBLE);
        } else {
            ArrayList<String> termTitles = new ArrayList<String>();
            terms.forEach(t -> termTitles.add(t.title));

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_dropdown_item, termTitles);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(this);

            ArrayList<Course> filteredCourses = getFilteredCourses(termTitles.get(0));
            setRecyclerView(filteredCourses);
        }
    }

    private void setRecyclerView(ArrayList<Course> filteredCourses) {
        GenericAdapter adapter = new GenericAdapter(this, filteredCourses);
        adapter.setDb(db);
        RecyclerView recyclerView = findViewById(R.id.cptRecyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private ArrayList<Course> getFilteredCourses(String termTitle) {
        return new ArrayList<>(db.appDao().getCourses(db.appDao().getTerm(termTitle).id));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String termTitle = adapterView.getItemAtPosition(i).toString();
        ArrayList<Course> courses = getFilteredCourses(termTitle);
        setRecyclerView(getFilteredCourses(termTitle));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
