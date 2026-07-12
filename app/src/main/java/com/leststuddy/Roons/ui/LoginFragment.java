package com.leststuddy.Roons.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.leststuddy.Roons.MainActivity;
import com.leststuddy.Roons.R;
import com.leststuddy.Roons.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonLogin.setOnClickListener(v -> {
            // TODO: Implement real authentication logic. 
            // This is temporary navigation to MainActivity.
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            if (getActivity() != null) {
                getActivity().finish();
            }
        });

        binding.buttonToRegister.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
