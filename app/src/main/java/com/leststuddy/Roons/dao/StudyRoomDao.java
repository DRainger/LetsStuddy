package com.leststuddy.Roons.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.leststuddy.Roons.model.StudyRoom;
import java.util.List;

@Dao
public interface StudyRoomDao {
    @Insert
    void insert(StudyRoom room);

    @Insert
    void insertAll(List<StudyRoom> rooms);

    @Query("SELECT * FROM study_rooms WHERE available = 1")
    LiveData<List<StudyRoom>> getAvailableRooms();

    @Query("SELECT * FROM study_rooms")
    LiveData<List<StudyRoom>> getAllRooms();

    @Query("SELECT COUNT(*) FROM study_rooms")
    int getCount();
}
