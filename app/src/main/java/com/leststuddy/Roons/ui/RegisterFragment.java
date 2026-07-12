package com.leststuddy.Roons.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.leststuddy.Roons.R;
import com.leststuddy.Roons.databinding.FragmentRegisterBinding;
import com.leststuddy.Roons.model.User;
import com.leststuddy.Roons.util.KeyboardUtils;
import com.leststuddy.Roons.util.ValidationUtils;
import com.leststuddy.Roons.viewmodel.UserViewModel;

public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;
    private UserViewModel userViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        binding.buttonToLogin.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_loginFragment)
        );

        binding.buttonRegister.setOnClickListener(v -> attemptRegistration());
    }

    private void attemptRegistration() {
        String fullName = binding.editFullName.getText().toString().trim();
        String email = binding.editEmail.getText().toString().trim();
        String phone = binding.editPhone.getText().toString().trim();
        String password = binding.editPassword.getText().toString();
        String confirmPassword = binding.editConfirmPassword.getText().toString();

        if (!validate(fullName, email, password, confirmPassword)) {
            return;
        }

        setLoading(true);
        userViewModel.getUserByEmail(email, existingUser -> {
            if (existingUser != null) {
                binding.layoutEmail.setError(getString(R.string.error_email_exists));
                setLoading(false);
            } else {
                User newUser = new User();
                newUser.fullName = fullName;
                newUser.email = email;
                newUser.phone = phone;
                newUser.passwordHash = password; // Storing as plain text for this project

                userViewModel.register(newUser, success -> {
                    setLoading(false);
                    if (success) {
                        KeyboardUtils.hideKeyboard(getActivity());
                        Snackbar.make(binding.getRoot(), R.string.success_registration, Snackbar.LENGTH_LONG).show();
                        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_registerFragment_to_loginFragment);
                    } else {
                        Snackbar.make(binding.getRoot(), R.string.error_registration_failed, Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private boolean validate(String name, String email, String password, String confirm) {
        boolean isValid = true;
        binding.layoutFullName.setError(null);
        binding.layoutEmail.setError(null);
        binding.layoutPassword.setError(null);
        binding.layoutConfirmPassword.setError(null);

        if (ValidationUtils.isEmpty(name)) {
            binding.layoutFullName.setError(getString(R.string.error_name_required));
            isValid = false;
        }
        if (!ValidationUtils.isValidEmail(email)) {
            binding.layoutEmail.setError(getString(R.string.error_invalid_email));
            isValid = false;
        }
        if (!ValidationUtils.isValidPassword(password)) {
            binding.layoutPassword.setError(getString(R.string.error_password_short));
            isValid = false;
        } else if (!ValidationUtils.doPasswordsMatch(password, confirm)) {
            binding.layoutConfirmPassword.setError(getString(R.string.error_passwords_dont_match));
            isValid = false;
        }

        return isValid;
    }

    private void setLoading(boolean isLoading) {
        binding.buttonRegister.setEnabled(!isLoading);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
