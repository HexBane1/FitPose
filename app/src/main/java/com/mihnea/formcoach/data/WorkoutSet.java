package com.mihnea.formcoach.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "workout_sets")
public class WorkoutSet {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String exercise;
    private double weightKg;
    private int reps;
    private String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(double weightKg) {
        this.weightKg = weightKg;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
