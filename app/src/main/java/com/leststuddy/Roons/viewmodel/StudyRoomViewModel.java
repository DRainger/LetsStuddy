package com.leststuddy.Roons.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import com.leststuddy.Roons.model.StudyRoom;
import com.leststuddy.Roons.repository.StudyRoomRepository;
import com.leststuddy.Roons.repository.UserRepository;
import java.util.List;

public class StudyRoomViewModel extends AndroidViewModel {
    private final StudyRoomRepository repository;
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>("");
    private final LiveData<List<StudyRoom>> filteredRooms;

    public StudyRoomViewModel(@NonNull Application application) {
        super(application);
        repository = new StudyRoomRepository(application);
        
        filteredRooms = Transformations.switchMap(searchQuery, query -> {
            if (query == null || query.trim().isEmpty()) {
                return repository.getAllRooms();
            } else {
                return repository.searchRooms(query.trim());
            }
        });
    }

    public LiveData<List<StudyRoom>> getFilteredRooms() {
        return filteredRooms;
    }

    public void setSearchQuery(String query) {
        if (query != null && !query.equals(searchQuery.getValue())) {
            searchQuery.setValue(query);
        }
    }

    public void insertInitialData() {
        repository.insertInitialData();
    }

    public void getRoomById(int id, UserRepository.ResultCallback<StudyRoom> callback) {
        repository.getRoomById(id, callback);
    }
}
