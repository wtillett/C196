package com.wtillett.ClassTracker.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.time.LocalDate;

@Entity(tableName = "assessment_table",
        foreignKeys = @ForeignKey(
                entity = Course.class,
                parentColumns = "id",
                childColumns = "courseId",
                onDelete = ForeignKey.RESTRICT),
        indices = {@Index("courseId")})
public class Assessment {

    private static Integer idSource = 0;

    @PrimaryKey
    @NonNull
    public Integer id;

    public Integer courseId;

    public String title;

    public LocalDate goalDate;

    public boolean isObjective;

    public Assessment(Integer courseId, String title, LocalDate goalDate, boolean isObjective) {
        this.id = idSource++;
        this.courseId = courseId;
        this.title = title;
        this.goalDate = goalDate;
        this.isObjective = isObjective;
    }

    public Assessment(String title, LocalDate goalDate, boolean isObjective) {
        this.id = idSource++;
        this.courseId = null;
        this.title = title;
        this.goalDate = goalDate;
        this.isObjective = isObjective;
    }

    public Assessment() {
        this.id = idSource++;
        this.courseId = null;
        this.title = "";
        this.goalDate = null;
        this.isObjective = true;
    }

    @Override
    public String toString() {
        return this.title + " " + this.goalDate + "\n" +
                (this.isObjective ? "Objective" : "Performance") + " Assessment";
    }
}
