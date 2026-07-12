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
import com.leststuddy.Roons.databinding.FragmentRoomListBinding;

public class RoomListFragment extends Fragment {
    private FragmentRoomListBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRoomListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonProfile.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.action_roomListFragment_to_profileFragment)
        );

        // TODO: This button_mic is used temporarily to navigate to RoomDetails 
        // since RecyclerView adapters are not yet implemented.
        binding.buttonMic.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.action_roomListFragment_to_roomDetailsFragment)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
