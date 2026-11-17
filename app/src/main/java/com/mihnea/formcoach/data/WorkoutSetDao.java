package com.mihnea.formcoach.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WorkoutSetDao {

    @Insert
    void insert(WorkoutSet workoutSet);

    @Update
    void update(WorkoutSet workoutSet);

    @Delete
    void delete(WorkoutSet workoutSet);

    @Query("SELECT * FROM workout_sets WHERE exercise = :exercise ORDER BY id DESC")
    List<WorkoutSet> getByExercise(String exercise);

    @Query("SELECT * FROM workout_sets WHERE id = :id")
    WorkoutSet getById(int id);
}
