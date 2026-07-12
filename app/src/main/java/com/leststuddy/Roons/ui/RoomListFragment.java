package com.leststuddy.Roons.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.leststuddy.Roons.R;
import com.leststuddy.Roons.databinding.FragmentRoomListBinding;

import android.text.Editable;
import android.text.TextWatcher;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.leststuddy.Roons.ui.adapter.StudyRoomAdapter;
import com.leststuddy.Roons.viewmodel.StudyRoomViewModel;

import java.util.ArrayList;

public class RoomListFragment extends Fragment {
    private FragmentRoomListBinding binding;
    private StudyRoomViewModel viewModel;
    private StudyRoomAdapter adapter;

    private final ActivityResultLauncher<Intent> voiceSearchLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    ArrayList<String> results = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (results != null && !results.isEmpty()) {
                        String query = results.get(0);
                        binding.editSearch.setText(query);
                    } else {
                        Snackbar.make(binding.getRoot(), R.string.error_no_speech_detected, Snackbar.LENGTH_SHORT).show();
                    }
                } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                    Snackbar.make(binding.getRoot(), R.string.error_voice_search_cancelled, Snackbar.LENGTH_SHORT).show();
                }
            }
    );

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
        setupSearch();

        binding.buttonProfile.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.action_roomListFragment_to_profileFragment)
        );

        binding.buttonReservations.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.action_roomListFragment_to_reservationListFragment)
        );

        binding.buttonMic.setOnClickListener(v -> startVoiceSearch());
    }

    private void startVoiceSearch() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.msg_speak_now));

        try {
            voiceSearchLauncher.launch(intent);
        } catch (Exception e) {
            Snackbar.make(binding.getRoot(), R.string.error_voice_search_unavailable, Snackbar.LENGTH_SHORT).show();
        }
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
        viewModel.getFilteredRooms().observe(getViewLifecycleOwner(), rooms -> {
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

    private void setupSearch() {
        binding.editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setSearchQuery(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
