package com.leststuddy.Roons.dao;

import androidx.room.*;
import com.leststuddy.Roons.model.User;
import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User getUserByEmail(String email);

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    User getUserById(int id);

    @Query("SELECT * FROM users WHERE email = :email AND passwordHash = :password LIMIT 1")
    User authenticate(String email, String password);

    @Query("SELECT * FROM users")
    List<User> getAllUsers();

    @Update
    void update(User user);

    @Delete
    void delete(User user);
}
