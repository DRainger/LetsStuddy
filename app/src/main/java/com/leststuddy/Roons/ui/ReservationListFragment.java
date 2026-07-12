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
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.leststuddy.Roons.R;
import com.leststuddy.Roons.databinding.FragmentReservationListBinding;
import com.leststuddy.Roons.model.Reservation;
import com.leststuddy.Roons.ui.adapter.ReservationAdapter;
import com.leststuddy.Roons.util.SessionManager;
import com.leststuddy.Roons.viewmodel.ReservationViewModel;
import com.leststuddy.Roons.viewmodel.StudyRoomViewModel;

public class ReservationListFragment extends Fragment {
    private FragmentReservationListBinding binding;
    private ReservationViewModel reservationViewModel;
    private ReservationAdapter adapter;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentReservationListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reservationViewModel = new ViewModelProvider(this).get(ReservationViewModel.class);
        StudyRoomViewModel studyRoomViewModel = new ViewModelProvider(this).get(StudyRoomViewModel.class);
        sessionManager = new SessionManager(requireContext());

        setupRecyclerView(studyRoomViewModel);
        observeReservations();
    }

    private void setupRecyclerView(StudyRoomViewModel studyRoomViewModel) {
        adapter = new ReservationAdapter(new ReservationAdapter.OnReservationActionListener() {
            @Override
            public void onEdit(Reservation reservation) {
                Bundle args = new Bundle();
                args.putInt("reservationId", reservation.id);
                Navigation.findNavController(requireView()).navigate(R.id.action_reservationListFragment_to_reservationFormFragment, args);
            }

            @Override
            public void onDelete(Reservation reservation) {
                showDeleteConfirmation(reservation);
            }
        }, studyRoomViewModel);
        binding.recyclerReservations.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerReservations.setAdapter(adapter);
    }

    private void observeReservations() {
        int userId = sessionManager.getUserId();
        reservationViewModel.getReservationsForUser(userId).observe(getViewLifecycleOwner(), list -> {
            if (list == null || list.isEmpty()) {
                binding.recyclerReservations.setVisibility(View.GONE);
                binding.textEmpty.setVisibility(View.VISIBLE);
            } else {
                binding.recyclerReservations.setVisibility(View.VISIBLE);
                binding.textEmpty.setVisibility(View.GONE);
                adapter.submitList(list);
            }
        });
    }

    private void showDeleteConfirmation(Reservation reservation) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.dialog_delete_reservation_title)
                .setMessage(R.string.dialog_delete_reservation_msg)
                .setNegativeButton(R.string.btn_cancel, null)
                .setPositiveButton(R.string.btn_delete, (dialog, which) -> {
                    reservationViewModel.delete(reservation, success -> {
                        if (success) {
                            Snackbar.make(binding.getRoot(), R.string.success_reservation_deleted, Snackbar.LENGTH_SHORT).show();
                        }
                    });
                })
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
