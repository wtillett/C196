package com.wtillett.ClassTracker.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "mentor_table",
        foreignKeys = @ForeignKey(
                entity = Course.class,
                parentColumns = "id",
                childColumns = "courseId",
                onDelete = ForeignKey.RESTRICT),
        indices = {@Index("courseId")})
public class Mentor {

    private static Integer idSource = 0;

    @PrimaryKey
    @NonNull
    public Integer id;

    public Integer courseId;

    public String name;

    public String phone;

    public String email;

    public Mentor(Integer courseId, String name, String phone, String email) {
        this.id = idSource++;
        this.courseId = courseId;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    @Ignore
    public Mentor() {
        this.id = idSource++;
        this.courseId = null;
    }

    @NonNull
    @Override
    public String toString() {
        return this.name + " " + this.email + " " + this.phone;
    }
}
