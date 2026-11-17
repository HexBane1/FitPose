package com.mihnea.formcoach.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mihnea.formcoach.R;
import com.mihnea.formcoach.data.AppDatabase;
import com.mihnea.formcoach.data.WorkoutSet;

import java.util.List;

public class SetListActivity extends AppCompatActivity {

    public static final String EXTRA_EXERCISE = "exercise";

    private String exercise;
    private RecyclerView recyclerView;
    private TextView emptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_set_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.set_list_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        exercise = getIntent().getStringExtra(EXTRA_EXERCISE);

        TextView titleView = findViewById(R.id.exercise_title);
        titleView.setText(exercise);

        emptyText = findViewById(R.id.empty_text);
        recyclerView = findViewById(R.id.set_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button analyzeButton = findViewById(R.id.analyze_form_button);
        analyzeButton.setOnClickListener(v -> {
            try {
                startActivity(new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA));
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, R.string.no_camera_app, Toast.LENGTH_SHORT).show();
            }
        });

        Button addButton = findViewById(R.id.add_set_button);
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, SetFormActivity.class);
            intent.putExtra(SetFormActivity.EXTRA_EXERCISE, exercise);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSets();
    }

    private void loadSets() {
        List<WorkoutSet> sets = AppDatabase.getDatabase(this).workoutSetDao().getByExercise(exercise);
        recyclerView.setAdapter(new WorkoutSetAdapter(sets, workoutSet -> {
            Intent intent = new Intent(this, SetFormActivity.class);
            intent.putExtra(SetFormActivity.EXTRA_SET_ID, workoutSet.getId());
            startActivity(intent);
        }));
        emptyText.setVisibility(sets.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
