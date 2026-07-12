package com.leststuddy.Roons.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.leststuddy.Roons.model.Reservation;
import com.leststuddy.Roons.repository.ReservationRepository;
import com.leststuddy.Roons.repository.UserRepository;
import java.util.List;

public class ReservationViewModel extends AndroidViewModel {
    private final ReservationRepository repository;

    public ReservationViewModel(@NonNull Application application) {
        super(application);
        repository = new ReservationRepository(application);
    }

    public LiveData<List<Reservation>> getReservationsForUser(int userId) {
        return repository.getReservationsForUser(userId);
    }

    public void insert(Reservation reservation, UserRepository.ResultCallback<Boolean> callback) {
        repository.insert(reservation, callback);
    }

    public void update(Reservation reservation, UserRepository.ResultCallback<Boolean> callback) {
        repository.update(reservation, callback);
    }

    public void delete(Reservation reservation, UserRepository.ResultCallback<Boolean> callback) {
        repository.delete(reservation, callback);
    }

    public void getReservationById(int id, UserRepository.ResultCallback<Reservation> callback) {
        repository.getReservationById(id, callback);
    }
}
