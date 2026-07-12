package com.leststuddy.Roons.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.leststuddy.Roons.R;
import com.leststuddy.Roons.databinding.ItemStudyRoomBinding;
import com.leststuddy.Roons.model.StudyRoom;

public class StudyRoomAdapter extends ListAdapter<StudyRoom, StudyRoomAdapter.StudyRoomViewHolder> {

    public interface OnRoomClickListener {
        void onRoomClick(int roomId);
    }

    private final OnRoomClickListener listener;

    public StudyRoomAdapter(OnRoomClickListener listener) {
        super(new DiffUtil.ItemCallback<StudyRoom>() {
            @Override
            public boolean areItemsTheSame(@NonNull StudyRoom oldItem, @NonNull StudyRoom newItem) {
                return oldItem.id == newItem.id;
            }

            @Override
            public boolean areContentsTheSame(@NonNull StudyRoom oldItem, @NonNull StudyRoom newItem) {
                return oldItem.name.equals(newItem.name) &&
                        oldItem.building.equals(newItem.building) &&
                        oldItem.capacity == newItem.capacity &&
                        oldItem.available == newItem.available;
            }
        });
        this.listener = listener;
    }

    @NonNull
    @Override
    public StudyRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemStudyRoomBinding binding = ItemStudyRoomBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new StudyRoomViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StudyRoomViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class StudyRoomViewHolder extends RecyclerView.ViewHolder {
        private final ItemStudyRoomBinding binding;

        StudyRoomViewHolder(ItemStudyRoomBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(StudyRoom room) {
            binding.textRoomName.setText(room.name);
            binding.textBuilding.setText(room.building);
            
            String capacityText = itemView.getContext().getString(R.string.room_capacity, room.capacity);
            binding.textCapacity.setText(capacityText);

            String statusText = room.available ? 
                    itemView.getContext().getString(R.string.status_available) : 
                    itemView.getContext().getString(R.string.status_unavailable);
            binding.textStatus.setText(statusText);

            binding.buttonDetails.setOnClickListener(v -> listener.onRoomClick(room.id));
            itemView.setOnClickListener(v -> listener.onRoomClick(room.id));
        }
    }
}
