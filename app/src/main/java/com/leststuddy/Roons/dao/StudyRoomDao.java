package com.leststuddy.Roons.dao;

import androidx.room.*;
import com.leststuddy.Roons.model.StudyRoom;
import java.util.List;

@Dao
public interface StudyRoomDao {
    @Insert
    void insert(StudyRoom room);

    @Query("SELECT * FROM study_rooms WHERE available = 1")
    List<StudyRoom> getAvailableRooms();

    @Query("SELECT * FROM study_rooms")
    List<StudyRoom> getAllRooms();
}
