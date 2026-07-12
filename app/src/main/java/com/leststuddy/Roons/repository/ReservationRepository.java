package com.leststuddy.Roons.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import androidx.lifecycle.LiveData;
import com.leststuddy.Roons.dao.ReservationDao;
import com.leststuddy.Roons.database.AppDatabase;
import com.leststuddy.Roons.model.Reservation;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReservationRepository {
    private final ReservationDao reservationDao;
    private final ExecutorService executorService;
    private final Handler mainHandler;

    public ReservationRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        reservationDao = db.reservationDao();
        executorService = Executors.newFixedThreadPool(2);
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public LiveData<List<Reservation>> getReservationsForUser(int userId) {
        return reservationDao.getReservationsForUser(userId);
    }

    public void insert(Reservation reservation, UserRepository.ResultCallback<Boolean> callback) {
        executorService.execute(() -> {
            boolean hasOverlap = checkOverlap(reservation);
            if (hasOverlap) {
                mainHandler.post(() -> callback.onResult(false));
            } else {
                reservationDao.insert(reservation);
                mainHandler.post(() -> callback.onResult(true));
            }
        });
    }

    public void update(Reservation reservation, UserRepository.ResultCallback<Boolean> callback) {
        executorService.execute(() -> {
            boolean hasOverlap = checkOverlap(reservation);
            if (hasOverlap) {
                mainHandler.post(() -> callback.onResult(false));
            } else {
                reservationDao.update(reservation);
                mainHandler.post(() -> callback.onResult(true));
            }
        });
    }

    public void delete(Reservation reservation, UserRepository.ResultCallback<Boolean> callback) {
        executorService.execute(() -> {
            reservationDao.delete(reservation);
            mainHandler.post(() -> callback.onResult(true));
        });
    }

    public void getReservationById(int id, UserRepository.ResultCallback<Reservation> callback) {
        executorService.execute(() -> {
            Reservation res = reservationDao.getReservationById(id);
            mainHandler.post(() -> callback.onResult(res));
        });
    }

    private boolean checkOverlap(Reservation reservation) {
        List<Reservation> overlaps = reservationDao.findOverlappingReservations(
                reservation.roomId,
                reservation.date,
                reservation.startTime,
                reservation.endTime,
                reservation.id
        );
        return !overlaps.isEmpty();
    }
}
