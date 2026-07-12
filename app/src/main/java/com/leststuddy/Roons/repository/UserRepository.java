package com.leststuddy.Roons.repository;

import android.content.Context;
import com.leststuddy.Roons.dao.UserDao;
import com.leststuddy.Roons.database.AppDatabase;
import com.leststuddy.Roons.model.User;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.os.Handler;
import android.os.Looper;

public class UserRepository {
    private final UserDao userDao;
    private final ExecutorService executorService;
    private final Handler mainHandler;

    public interface ResultCallback<T> {
        void onResult(T result);
    }

    public UserRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        userDao = db.userDao();
        executorService = Executors.newFixedThreadPool(2);
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public void register(User user, ResultCallback<Boolean> callback) {
        executorService.execute(() -> {
            try {
                User existing = userDao.getUserByEmail(user.email);
                if (existing != null) {
                    mainHandler.post(() -> callback.onResult(false)); // Email exists
                } else {
                    userDao.insert(user);
                    mainHandler.post(() -> callback.onResult(true));
                }
            } catch (Exception e) {
                mainHandler.post(() -> callback.onResult(false));
            }
        });
    }

    public void authenticate(String email, String password, ResultCallback<User> callback) {
        executorService.execute(() -> {
            User user = userDao.authenticate(email, password);
            mainHandler.post(() -> callback.onResult(user));
        });
    }

    public void getUserByEmail(String email, ResultCallback<User> callback) {
        executorService.execute(() -> {
            User user = userDao.getUserByEmail(email);
            mainHandler.post(() -> callback.onResult(user));
        });
    }

    public void getUserById(int id, ResultCallback<User> callback) {
        executorService.execute(() -> {
            User user = userDao.getUserById(id);
            mainHandler.post(() -> callback.onResult(user));
        });
    }
}
