package com.leststuddy.Roons.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.leststuddy.Roons.model.Reservation;
import java.util.List;

@Dao
public interface ReservationDao {
    @Insert
    long insert(Reservation reservation);

    @Update
    void update(Reservation reservation);

    @Delete
    void delete(Reservation reservation);

    @Query("SELECT * FROM reservations WHERE id = :id LIMIT 1")
    Reservation getReservationById(int id);

    @Query("SELECT * FROM reservations WHERE userId = :userId ORDER BY date DESC, startTime DESC")
    LiveData<List<Reservation>> getReservationsForUser(int userId);

    @Query("SELECT * FROM reservations WHERE roomId = :roomId AND date = :date AND " +
           "((startTime < :endTime AND endTime > :startTime)) AND id != :excludeId")
    List<Reservation> findOverlappingReservations(int roomId, String date, String startTime, String endTime, int excludeId);
}
