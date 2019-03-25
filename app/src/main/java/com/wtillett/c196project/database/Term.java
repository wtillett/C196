package com.wtillett.c196project.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity(tableName = "term_table")
public class Term implements Serializable {

    private static Integer idSource = 0;

    @PrimaryKey
    @NonNull
    public Integer id;

    public String title;

    public String startDate;

    public String endDate;

    public Term(String title, String startDate, String endDate) {
        this.id = idSource++;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Ignore
    public Term() {
        this.id = idSource++;
    }

    @Override
    public String toString() {
        return this.title + " " + this.startDate + " through " + this.endDate;
    }
}
