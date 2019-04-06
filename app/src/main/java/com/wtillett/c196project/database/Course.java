package com.wtillett.c196project.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity(tableName = "course_table",
        foreignKeys = @ForeignKey(
                entity = Term.class,
                parentColumns = "id",
                childColumns = "termId",
                onDelete = ForeignKey.RESTRICT),
        indices = {@Index("termId")})
public class Course implements Serializable {

    private static Integer idSource = 0;

    @PrimaryKey
    @NonNull
    public Integer id;

    public String title;

    public Integer termId;

    public String startDate;

    public String endDate;

    public String status;

    public String notes;

    public Course() {
        this.id = idSource++;
        this.termId = null;
        this.title = "";
        this.startDate = "";
        this.endDate = "";
        this.status = "";
        this.notes = "";
    }

    public Course(String title, String startDate, String endDate, String status, String notes) {
        this.id = idSource++;
        this.termId = null;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.notes = notes;
    }

    public Course(Integer termId, @NonNull String title, String startDate,
                  String endDate, String status, String notes) {
        this.id = idSource++;
        this.termId = (termId != -1) ? termId : null;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.notes = notes;
    }

    @Override
    public String toString() {
        return this.title + "\n" + this.status + " " + this.startDate + " - " + this.endDate;
    }
}
