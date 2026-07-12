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
import com.leststuddy.Roons.R;
import com.leststuddy.Roons.databinding.FragmentRoomDetailsBinding;
import com.leststuddy.Roons.viewmodel.StudyRoomViewModel;

public class RoomDetailsFragment extends Fragment {
    private FragmentRoomDetailsBinding binding;
    private StudyRoomViewModel studyRoomViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRoomDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        studyRoomViewModel = new ViewModelProvider(this).get(StudyRoomViewModel.class);

        int roomId = -1;
        if (getArguments() != null) {
            roomId = getArguments().getInt("roomId", -1);
        }

        if (roomId != -1) {
            loadRoomDetails(roomId);
        }

        binding.buttonBack.setOnClickListener(v -> 
            Navigation.findNavController(v).popBackStack()
        );

        int finalRoomId = roomId;
        binding.buttonReserve.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putInt("roomId", finalRoomId);
            Navigation.findNavController(v).navigate(R.id.action_roomDetailsFragment_to_reservationFormFragment, args);
        });
    }

    private void loadRoomDetails(int roomId) {
        studyRoomViewModel.getRoomById(roomId, room -> {
            if (room != null && binding != null) {
                binding.textRoomName.setText(room.name);
                binding.textBuilding.setText(room.building);
                binding.textCapacity.setText(getString(R.string.room_capacity, room.capacity));
                
                String statusText = room.available ? 
                        getString(R.string.status_available) : 
                        getString(R.string.status_unavailable);
                binding.textStatus.setText(statusText);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
