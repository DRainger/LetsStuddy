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

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.leststuddy.Roons.ui.adapter.StudyRoomAdapter;
import com.leststuddy.Roons.viewmodel.StudyRoomViewModel;

public class RoomListFragment extends Fragment {
    private FragmentRoomListBinding binding;
    private StudyRoomViewModel viewModel;
    private StudyRoomAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRoomListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView();
        setupViewModel();

        binding.buttonProfile.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.action_roomListFragment_to_profileFragment)
        );
    }

    private void setupRecyclerView() {
        adapter = new StudyRoomAdapter(roomId -> {
            Bundle args = new Bundle();
            args.putInt("roomId", roomId);
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_roomListFragment_to_roomDetailsFragment, args);
        });
        binding.recyclerRooms.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerRooms.setAdapter(adapter);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(StudyRoomViewModel.class);
        viewModel.insertInitialData();
        viewModel.getAllRooms().observe(getViewLifecycleOwner(), rooms -> {
            if (rooms == null || rooms.isEmpty()) {
                binding.recyclerRooms.setVisibility(View.GONE);
                binding.textEmpty.setVisibility(View.VISIBLE);
            } else {
                binding.recyclerRooms.setVisibility(View.VISIBLE);
                binding.textEmpty.setVisibility(View.GONE);
                adapter.submitList(rooms);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
