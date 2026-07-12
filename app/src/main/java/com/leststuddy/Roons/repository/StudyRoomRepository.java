package com.leststuddy.Roons.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.leststuddy.Roons.dao.StudyRoomDao;
import com.leststuddy.Roons.database.AppDatabase;
import com.leststuddy.Roons.model.StudyRoom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.os.Handler;
import android.os.Looper;

public class StudyRoomRepository {
    private final StudyRoomDao studyRoomDao;
    private final LiveData<List<StudyRoom>> allRooms;
    private final ExecutorService executorService;

    public StudyRoomRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        studyRoomDao = db.studyRoomDao();
        allRooms = studyRoomDao.getAllRooms();
        executorService = Executors.newFixedThreadPool(2);
    }

    public LiveData<List<StudyRoom>> getAllRooms() {
        return allRooms;
    }

    public void insert(StudyRoom room) {
        executorService.execute(() -> studyRoomDao.insert(room));
    }

    public void getRoomById(int id, UserRepository.ResultCallback<StudyRoom> callback) {
        executorService.execute(() -> {
            StudyRoom room = studyRoomDao.getRoomById(id);
            new Handler(Looper.getMainLooper()).post(() -> callback.onResult(room));
        });
    }

    public void insertInitialData() {
        executorService.execute(() -> {
            if (studyRoomDao.getCount() == 0) {
                List<StudyRoom> sampleRooms = new ArrayList<>();
                
                StudyRoom room1 = new StudyRoom();
                room1.name = "Library Study Room A";
                room1.building = "Main Library";
                room1.capacity = 6;
                room1.available = true;
                sampleRooms.add(room1);

                StudyRoom room2 = new StudyRoom();
                room2.name = "Computer Lab 201";
                room2.building = "Technology Building";
                room2.capacity = 24;
                room2.available = false;
                sampleRooms.add(room2);

                StudyRoom room3 = new StudyRoom();
                room3.name = "Quiet Room 105";
                room3.building = "Student Center";
                room3.capacity = 4;
                room3.available = true;
                sampleRooms.add(room3);

                StudyRoom room4 = new StudyRoom();
                room4.name = "Group Room 302";
                room4.building = "Science Building";
                room4.capacity = 10;
                room4.available = true;
                sampleRooms.add(room4);

                studyRoomDao.insertAll(sampleRooms);
            }
        });
    }
}
