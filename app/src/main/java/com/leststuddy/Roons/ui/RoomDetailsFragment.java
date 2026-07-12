package com.leststuddy.Roons.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.leststuddy.Roons.databinding.FragmentRoomDetailsBinding;

public class RoomDetailsFragment extends Fragment {
    private FragmentRoomDetailsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRoomDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int roomId = -1;
        if (getArguments() != null) {
            roomId = getArguments().getInt("roomId", -1);
        }

        // For now, just display the ID if available to confirm navigation
        if (roomId != -1) {
            binding.textRoomName.setText("Room ID: " + roomId);
        }

        binding.buttonBack.setOnClickListener(v -> 
            Navigation.findNavController(v).popBackStack()
        );

        binding.buttonReserve.setOnClickListener(v -> {
            // TODO: Implement reservation logic
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
