package com.leststuddy.Roons.dao;

import androidx.room.*;
import com.leststuddy.Roons.model.Reservation;
import java.util.List;

@Dao
public interface ReservationDao {
    @Insert
    void insert(Reservation reservation);

    @Query("SELECT * FROM reservations WHERE userId = :userId")
    List<Reservation> getReservationsForUser(int userId);

    @Delete
    void delete(Reservation reservation);
}
