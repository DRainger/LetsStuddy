package com.leststuddy.Roons.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.leststuddy.Roons.model.User;
import com.leststuddy.Roons.repository.UserRepository;

public class UserViewModel extends AndroidViewModel {
    private final UserRepository repository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
    }

    public void register(User user, UserRepository.ResultCallback<Boolean> callback) {
        repository.register(user, callback);
    }

    public void authenticate(String email, String password, UserRepository.ResultCallback<User> callback) {
        repository.authenticate(email, password, callback);
    }

    public void getUserByEmail(String email, UserRepository.ResultCallback<User> callback) {
        repository.getUserByEmail(email, callback);
    }

    public void getUserById(int id, UserRepository.ResultCallback<User> callback) {
        repository.getUserById(id, callback);
    }

    public void update(User user, UserRepository.ResultCallback<Boolean> callback) {
        repository.update(user, callback);
    }

    public void delete(User user, UserRepository.ResultCallback<Boolean> callback) {
        repository.delete(user, callback);
    }
}
