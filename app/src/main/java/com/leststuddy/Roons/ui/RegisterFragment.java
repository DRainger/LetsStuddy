package com.leststuddy.Roons.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.leststuddy.Roons.R;
import com.leststuddy.Roons.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonToLogin.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_loginFragment)
        );

        binding.buttonRegister.setOnClickListener(v -> {
            // TODO: Implement registration logic
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
