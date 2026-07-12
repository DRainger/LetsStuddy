package com.leststuddy.Roons.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.leststuddy.Roons.AuthActivity;
import com.leststuddy.Roons.R;
import com.leststuddy.Roons.databinding.FragmentProfileBinding;
import com.leststuddy.Roons.model.User;
import com.leststuddy.Roons.util.KeyboardUtils;
import com.leststuddy.Roons.util.SessionManager;
import com.leststuddy.Roons.util.ValidationUtils;
import com.leststuddy.Roons.viewmodel.UserViewModel;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private UserViewModel userViewModel;
    private SessionManager sessionManager;
    private User currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        sessionManager = new SessionManager(requireContext());

        loadUserProfile();

        binding.buttonSave.setOnClickListener(v -> updateProfile());
        binding.buttonLogout.setOnClickListener(v -> showLogoutConfirmation());
        binding.buttonDeleteAccount.setOnClickListener(v -> showDeleteConfirmation());
    }

    private void loadUserProfile() {
        int userId = sessionManager.getUserId();
        if (userId == -1) {
            handleInvalidSession();
            return;
        }

        userViewModel.getUserById(userId, user -> {
            if (user == null) {
                handleInvalidSession();
            } else {
                currentUser = user;
                populateForm(user);
            }
        });
    }

    private void populateForm(User user) {
        if (binding == null) return;
        binding.editFullName.setText(user.fullName);
        binding.editEmail.setText(user.email);
        binding.editPhone.setText(user.phone);
    }

    private void updateProfile() {
        if (currentUser == null) return;

        String fullName = binding.editFullName.getText().toString().trim();
        String email = binding.editEmail.getText().toString().trim();
        String phone = binding.editPhone.getText().toString().trim();

        if (!validate(fullName, email)) return;

        setLoading(true);
        userViewModel.getUserByEmail(email, existingUser -> {
            if (existingUser != null && existingUser.id != currentUser.id) {
                binding.layoutEmail.setError(getString(R.string.error_email_exists));
                setLoading(false);
            } else {
                currentUser.fullName = fullName;
                currentUser.email = email;
                currentUser.phone = phone;

                userViewModel.update(currentUser, success -> {
                    setLoading(false);
                    if (success) {
                        KeyboardUtils.hideKeyboard(getActivity());
                        Snackbar.make(binding.getRoot(), R.string.success_profile_updated, Snackbar.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(binding.getRoot(), R.string.error_profile_update_failed, Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private boolean validate(String name, String email) {
        boolean isValid = true;
        binding.layoutFullName.setError(null);
        binding.layoutEmail.setError(null);

        if (ValidationUtils.isEmpty(name)) {
            binding.layoutFullName.setError(getString(R.string.error_name_required));
            isValid = false;
        }
        if (!ValidationUtils.isValidEmail(email)) {
            binding.layoutEmail.setError(getString(R.string.error_invalid_email));
            isValid = false;
        }

        return isValid;
    }

    private void showLogoutConfirmation() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.dialog_logout_title)
                .setMessage(R.string.dialog_logout_msg)
                .setNegativeButton(R.string.btn_cancel, null)
                .setPositiveButton(R.string.btn_logout, (dialog, which) -> logout())
                .show();
    }

    private void logout() {
        sessionManager.logout();
        navigateToAuth();
    }

    private void showDeleteConfirmation() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.dialog_delete_account_title)
                .setMessage(R.string.dialog_delete_account_msg)
                .setNegativeButton(R.string.btn_cancel, null)
                .setPositiveButton(R.string.btn_delete, (dialog, which) -> deleteAccount())
                .show();
    }

    private void deleteAccount() {
        if (currentUser == null) return;
        setLoading(true);
        userViewModel.delete(currentUser, success -> {
            if (success) {
                sessionManager.logout();
                Toast.makeText(getContext(), R.string.success_account_deleted, Toast.LENGTH_SHORT).show();
                navigateToAuth();
            } else {
                setLoading(false);
                Snackbar.make(binding.getRoot(), R.string.error_account_deletion_failed, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void handleInvalidSession() {
        Toast.makeText(getContext(), R.string.error_invalid_session, Toast.LENGTH_SHORT).show();
        sessionManager.logout();
        navigateToAuth();
    }

    private void navigateToAuth() {
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().finish();
        }
    }

    private void setLoading(boolean isLoading) {
        if (binding == null) return;
        binding.buttonSave.setEnabled(!isLoading);
        binding.buttonLogout.setEnabled(!isLoading);
        binding.buttonDeleteAccount.setEnabled(!isLoading);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
