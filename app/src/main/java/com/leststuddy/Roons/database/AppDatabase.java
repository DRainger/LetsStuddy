package com.leststuddy.Roons.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.leststuddy.Roons.dao.*;
import com.leststuddy.Roons.model.*;

@Database(entities = {User.class, StudyRoom.class, Reservation.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract UserDao userDao();
    public abstract StudyRoomDao studyRoomDao();
    public abstract ReservationDao reservationDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "study_room_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
