package com.wtillett.c196project.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.time.LocalDate;

@Entity(tableName = "term_table")
public class Term {

    private static Integer idSource = 0;

    @PrimaryKey
    @NonNull
    public Integer id;

    public String title;

    public LocalDate startDate;

    public LocalDate endDate;

    public Term(String title, LocalDate startDate, LocalDate endDate) {
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
        return this.title + "\n" + this.startDate + " - " + this.endDate;
    }
}
