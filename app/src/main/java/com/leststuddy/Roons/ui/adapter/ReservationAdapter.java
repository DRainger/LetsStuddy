package com.leststuddy.Roons.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.leststuddy.Roons.databinding.ItemReservationBinding;
import com.leststuddy.Roons.model.Reservation;
import com.leststuddy.Roons.model.StudyRoom;
import com.leststuddy.Roons.viewmodel.StudyRoomViewModel;

public class ReservationAdapter extends ListAdapter<Reservation, ReservationAdapter.ReservationViewHolder> {

    public interface OnReservationActionListener {
        void onEdit(Reservation reservation);
        void onDelete(Reservation reservation);
    }

    private final OnReservationActionListener listener;
    private final StudyRoomViewModel studyRoomViewModel;

    public ReservationAdapter(OnReservationActionListener listener, StudyRoomViewModel studyRoomViewModel) {
        super(new DiffUtil.ItemCallback<Reservation>() {
            @Override
            public boolean areItemsTheSame(@NonNull Reservation oldItem, @NonNull Reservation newItem) {
                return oldItem.id == newItem.id;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Reservation oldItem, @NonNull Reservation newItem) {
                return oldItem.date.equals(newItem.date) &&
                        oldItem.startTime.equals(newItem.startTime) &&
                        oldItem.endTime.equals(newItem.endTime) &&
                        oldItem.roomId == newItem.roomId;
            }
        });
        this.listener = listener;
        this.studyRoomViewModel = studyRoomViewModel;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemReservationBinding binding = ItemReservationBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ReservationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class ReservationViewHolder extends RecyclerView.ViewHolder {
        private final ItemReservationBinding binding;

        ReservationViewHolder(ItemReservationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Reservation reservation) {
            binding.textDate.setText(reservation.date);
            binding.textTime.setText(reservation.startTime + " - " + reservation.endTime);

            // Fetch room details to display name and building
            studyRoomViewModel.getRoomById(reservation.roomId, room -> {
                if (room != null) {
                    binding.textRoomName.setText(room.name);
                    binding.textBuilding.setText(room.building);
                }
            });

            binding.buttonEdit.setOnClickListener(v -> listener.onEdit(reservation));
            binding.buttonDelete.setOnClickListener(v -> listener.onDelete(reservation));
        }
    }
}
