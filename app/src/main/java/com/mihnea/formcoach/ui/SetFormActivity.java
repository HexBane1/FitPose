package com.mihnea.formcoach.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mihnea.formcoach.R;
import com.mihnea.formcoach.data.AppDatabase;
import com.mihnea.formcoach.data.WorkoutSet;
import com.mihnea.formcoach.data.WorkoutSetDao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SetFormActivity extends AppCompatActivity {

    public static final String EXTRA_EXERCISE = "exercise";
    public static final String EXTRA_SET_ID = "set_id";

    private static final int NO_SET_ID = -1;

    private WorkoutSetDao dao;
    private WorkoutSet existingSet;
    private String exercise;
    private String date;
    private EditText weightInput;
    private EditText repsInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_set_form);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.set_form_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dao = AppDatabase.getDatabase(this).workoutSetDao();

        TextView titleView = findViewById(R.id.form_title);
        TextView dateView = findViewById(R.id.date_text);
        weightInput = findViewById(R.id.weight_input);
        repsInput = findViewById(R.id.reps_input);
        Button saveButton = findViewById(R.id.save_button);
        Button deleteButton = findViewById(R.id.delete_button);

        int setId = getIntent().getIntExtra(EXTRA_SET_ID, NO_SET_ID);
        if (setId == NO_SET_ID) {
            exercise = getIntent().getStringExtra(EXTRA_EXERCISE);
            date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
            titleView.setText(R.string.add_set);
        } else {
            existingSet = dao.getById(setId);
            if (existingSet == null) {
                finish();
                return;
            }
            date = existingSet.getDate();
            titleView.setText(R.string.edit_set);
            weightInput.setText(String.valueOf(existingSet.getWeightKg()));
            repsInput.setText(String.valueOf(existingSet.getReps()));
            deleteButton.setVisibility(View.VISIBLE);
            deleteButton.setOnClickListener(v -> {
                dao.delete(existingSet);
                finish();
            });
        }

        dateView.setText(date);
        saveButton.setOnClickListener(v -> save());
    }

    private void save() {
        String weightText = weightInput.getText().toString().trim();
        String repsText = repsInput.getText().toString().trim();
        boolean valid = true;
        double weight = 0;
        int reps = 0;

        if (weightText.isEmpty()) {
            weightInput.setError(getString(R.string.error_required));
            valid = false;
        } else {
            try {
                weight = Double.parseDouble(weightText);
            } catch (NumberFormatException e) {
                weight = 0;
            }
            if (weight <= 0) {
                weightInput.setError(getString(R.string.error_positive));
                valid = false;
            }
        }

        if (repsText.isEmpty()) {
            repsInput.setError(getString(R.string.error_required));
            valid = false;
        } else {
            try {
                reps = Integer.parseInt(repsText);
            } catch (NumberFormatException e) {
                reps = 0;
            }
            if (reps <= 0) {
                repsInput.setError(getString(R.string.error_positive));
                valid = false;
            }
        }

        if (!valid) {
            return;
        }

        if (existingSet == null) {
            WorkoutSet workoutSet = new WorkoutSet();
            workoutSet.setExercise(exercise);
            workoutSet.setWeightKg(weight);
            workoutSet.setReps(reps);
            workoutSet.setDate(date);
            dao.insert(workoutSet);
        } else {
            existingSet.setWeightKg(weight);
            existingSet.setReps(reps);
            dao.update(existingSet);
        }
        finish();
    }
}
