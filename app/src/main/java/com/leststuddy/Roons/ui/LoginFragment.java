package com.leststuddy.Roons.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.leststuddy.Roons.MainActivity;
import com.leststuddy.Roons.R;
import com.leststuddy.Roons.databinding.FragmentLoginBinding;
import com.leststuddy.Roons.util.KeyboardUtils;
import com.leststuddy.Roons.util.SessionManager;
import com.leststuddy.Roons.util.ValidationUtils;
import com.leststuddy.Roons.viewmodel.UserViewModel;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private UserViewModel userViewModel;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        sessionManager = new SessionManager(requireContext());

        binding.buttonLogin.setOnClickListener(v -> attemptLogin());

        binding.buttonToRegister.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment)
        );
    }

    private void attemptLogin() {
        String email = binding.editEmail.getText().toString().trim();
        String password = binding.editPassword.getText().toString();

        if (!validate(email, password)) {
            return;
        }

        setLoading(true);
        userViewModel.authenticate(email, password, user -> {
            if (user != null) {
                sessionManager.saveLogin(user.id);
                KeyboardUtils.hideKeyboard(getActivity());
                
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                if (getActivity() != null) {
                    getActivity().finish();
                }
            } else {
                setLoading(false);
                binding.layoutPassword.setError(getString(R.string.error_invalid_credentials));
            }
        });
    }

    private boolean validate(String email, String password) {
        boolean isValid = true;
        binding.layoutEmail.setError(null);
        binding.layoutPassword.setError(null);

        if (!ValidationUtils.isValidEmail(email)) {
            binding.layoutEmail.setError(getString(R.string.error_invalid_email));
            isValid = false;
        }
        if (ValidationUtils.isEmpty(password)) {
            binding.layoutPassword.setError(getString(R.string.error_password_required));
            isValid = false;
        }

        return isValid;
    }

    private void setLoading(boolean isLoading) {
        binding.buttonLogin.setEnabled(!isLoading);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
