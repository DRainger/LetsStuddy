package com.leststuddy.Roons.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.google.android.material.snackbar.Snackbar;
import com.leststuddy.Roons.R;
import com.leststuddy.Roons.databinding.FragmentReservationFormBinding;
import com.leststuddy.Roons.model.Reservation;
import com.leststuddy.Roons.util.SessionManager;
import com.leststuddy.Roons.viewmodel.ReservationViewModel;
import com.leststuddy.Roons.viewmodel.StudyRoomViewModel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ReservationFormFragment extends Fragment {
    private FragmentReservationFormBinding binding;
    private ReservationViewModel reservationViewModel;
    private StudyRoomViewModel studyRoomViewModel;
    private SessionManager sessionManager;

    private int roomId = -1;
    private int reservationId = -1;
    private Reservation existingReservation;
    private final Calendar calendar = Calendar.getInstance();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.US);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentReservationFormBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reservationViewModel = new ViewModelProvider(this).get(ReservationViewModel.class);
        studyRoomViewModel = new ViewModelProvider(this).get(StudyRoomViewModel.class);
        sessionManager = new SessionManager(requireContext());

        if (getArguments() != null) {
            roomId = getArguments().getInt("roomId", -1);
            reservationId = getArguments().getInt("reservationId", -1);
        }

        if (reservationId != -1) {
            loadExistingReservation();
        } else if (roomId != -1) {
            loadRoomDetails();
        }

        binding.editDate.setOnClickListener(v -> showDatePicker());
        binding.editStartTime.setOnClickListener(v -> showTimePicker(true));
        binding.editEndTime.setOnClickListener(v -> showTimePicker(false));
        binding.buttonSave.setOnClickListener(v -> saveReservation());
        binding.buttonCancel.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());
    }

    private void loadRoomDetails() {
        studyRoomViewModel.getRoomById(roomId, room -> {
            if (room != null) {
                binding.textRoomName.setText(room.name);
            }
        });
    }

    private void loadExistingReservation() {
        binding.textTitle.setText(R.string.title_edit_reservation);
        binding.buttonSave.setText(R.string.btn_update);
        reservationViewModel.getReservationById(reservationId, res -> {
            if (res != null) {
                existingReservation = res;
                roomId = res.roomId;
                binding.editDate.setText(res.date);
                binding.editStartTime.setText(res.startTime);
                binding.editEndTime.setText(res.endTime);
                loadRoomDetails();
            }
        });
    }

    private void showDatePicker() {
        new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            binding.editDate.setText(dateFormat.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePicker(boolean isStartTime) {
        new TimePickerDialog(requireContext(), (view, hourOfDay, minute) -> {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            String time = timeFormat.format(calendar.getTime());
            if (isStartTime) {
                binding.editStartTime.setText(time);
            } else {
                binding.editEndTime.setText(time);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    private void saveReservation() {
        String date = binding.editDate.getText().toString();
        String startTime = binding.editStartTime.getText().toString();
        String endTime = binding.editEndTime.getText().toString();

        if (date.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
            Snackbar.make(binding.getRoot(), "Please fill all fields", Snackbar.LENGTH_SHORT).show();
            return;
        }

        // Simple validation: end time after start time (HH:mm comparison)
        if (startTime.compareTo(endTime) >= 0) {
            Snackbar.make(binding.getRoot(), R.string.error_invalid_time_range, Snackbar.LENGTH_SHORT).show();
            return;
        }

        // Validate date is not in the past
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        if (calendar.before(today)) {
            Snackbar.make(binding.getRoot(), R.string.error_invalid_date, Snackbar.LENGTH_SHORT).show();
            return;
        }

        int userId = sessionManager.getUserId();
        Reservation res = (existingReservation != null) ? existingReservation : new Reservation();
        res.userId = userId;
        res.roomId = roomId;
        res.date = date;
        res.startTime = startTime;
        res.endTime = endTime;

        if (existingReservation != null) {
            reservationViewModel.update(res, this::handleResult);
        } else {
            reservationViewModel.insert(res, this::handleResult);
        }
    }

    private void handleResult(boolean success) {
        if (success) {
            Snackbar.make(binding.getRoot(), R.string.success_reservation_created, Snackbar.LENGTH_SHORT).show();
            Navigation.findNavController(requireView()).navigate(R.id.action_reservationFormFragment_to_reservationListFragment);
        } else {
            Snackbar.make(binding.getRoot(), R.string.error_overlap_detected, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
