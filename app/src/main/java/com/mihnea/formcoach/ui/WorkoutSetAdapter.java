package com.mihnea.formcoach.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mihnea.formcoach.R;
import com.mihnea.formcoach.data.WorkoutSet;

import java.util.List;

public class WorkoutSetAdapter extends RecyclerView.Adapter<WorkoutSetAdapter.WorkoutSetViewHolder> {

    public interface OnSetClickListener {
        void onSetClick(WorkoutSet workoutSet);
    }

    private final List<WorkoutSet> sets;
    private final OnSetClickListener listener;

    public WorkoutSetAdapter(List<WorkoutSet> sets, OnSetClickListener listener) {
        this.sets = sets;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WorkoutSetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_workout_set, parent, false);
        return new WorkoutSetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutSetViewHolder holder, int position) {
        WorkoutSet workoutSet = sets.get(position);
        Context context = holder.itemView.getContext();
        holder.weightView.setText(context.getString(
                R.string.set_weight_format, String.valueOf(workoutSet.getWeightKg())));
        holder.repsView.setText(context.getString(
                R.string.set_reps_format, workoutSet.getReps()));
        holder.dateView.setText(workoutSet.getDate());
        holder.itemView.setOnClickListener(v -> listener.onSetClick(workoutSet));
    }

    @Override
    public int getItemCount() {
        return sets.size();
    }

    static class WorkoutSetViewHolder extends RecyclerView.ViewHolder {

        final TextView weightView;
        final TextView repsView;
        final TextView dateView;

        WorkoutSetViewHolder(@NonNull View itemView) {
            super(itemView);
            weightView = itemView.findViewById(R.id.set_weight);
            repsView = itemView.findViewById(R.id.set_reps);
            dateView = itemView.findViewById(R.id.set_date);
        }
    }
}
