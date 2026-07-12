package com.leststuddy.Roons.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.leststuddy.Roons.model.StudyRoom;
import com.leststuddy.Roons.repository.StudyRoomRepository;
import com.leststuddy.Roons.repository.UserRepository;
import java.util.List;

public class StudyRoomViewModel extends AndroidViewModel {
    private final StudyRoomRepository repository;
    private final LiveData<List<StudyRoom>> allRooms;

    public StudyRoomViewModel(@NonNull Application application) {
        super(application);
        repository = new StudyRoomRepository(application);
        allRooms = repository.getAllRooms();
    }

    public LiveData<List<StudyRoom>> getAllRooms() {
        return allRooms;
    }

    public void insertInitialData() {
        repository.insertInitialData();
    }

    public void getRoomById(int id, UserRepository.ResultCallback<StudyRoom> callback) {
        repository.getRoomById(id, callback);
    }
}
