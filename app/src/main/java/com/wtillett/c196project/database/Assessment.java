package com.wtillett.c196project.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity(tableName = "assessment_table",
        foreignKeys = @ForeignKey(
                entity = Course.class,
                parentColumns = "id",
                childColumns = "courseId",
                onDelete = ForeignKey.RESTRICT),
        indices = {@Index("courseId")})
public class Assessment implements Serializable {

    private static Integer idSource = 0;

    @PrimaryKey
    @NonNull
    public Integer id;

    public Integer courseId;

    public String title;

    public String goalDate;

    public boolean isObjective;

    public Assessment(Integer courseId, String title, String goalDate, boolean isObjective) {
        this.id = idSource++;
        this.courseId = courseId;
        this.title = title;
        this.goalDate = goalDate;
        this.isObjective = isObjective;
    }

    public Assessment(String title, String goalDate, boolean isObjective) {
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
        this.goalDate = "";
        this.isObjective = true;
    }

    @Override
    public String toString() {
        return this.title + " " + (this.isObjective ? "OA" : "PA");
    }
}
