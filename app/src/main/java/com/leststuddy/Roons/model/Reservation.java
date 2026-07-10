package com.leststuddy.Roons.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "reservations",
    foreignKeys = {
        @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "userId", onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = StudyRoom.class, parentColumns = "id", childColumns = "roomId", onDelete = ForeignKey.CASCADE)
    }
)
public class Reservation {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int userId;
    public int roomId;
    public String date;
    public String startTime;
    public String endTime;
}
