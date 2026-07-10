package com.leststuddy.Roons.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "study_rooms")
public class StudyRoom {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String building;
    public int capacity;
    public boolean available;
}
