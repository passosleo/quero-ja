package com.leonardo.queroja.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leonardo.queroja.R;
import com.leonardo.queroja.entities.WishEntity;
import com.leonardo.queroja.enums.Status;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class WishAdapter extends RecyclerView.Adapter<WishAdapter.WishViewHolder> {

    private List<WishEntity> wishes;
    private final OnWishClickListener listener;

    public interface OnWishClickListener {
        void onDeleteClick(WishEntity wish);
        void onLinkClick(String url);
        void onItemClick(WishEntity wish);
        void onCheckClick(WishEntity wish);
    }

    public WishAdapter(List<WishEntity> wishes, OnWishClickListener listener) {
        this.wishes = wishes;
        this.listener = listener;
    }

    public void updateData(List<WishEntity> newWishes) {
        this.wishes = newWishes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_wish, parent, false);
        return new WishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishViewHolder holder, int position) {
        WishEntity wish = wishes.get(position);

        holder.titleTextView.setText(wish.getTitle());
        holder.descriptionTextView.setText(wish.getDescription());
        holder.priorityTextView.setText(wish.getPriority().getDescription());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        holder.createdAtTextView.setText(String.format("Criado em: %s", wish.getCreatedAt().minusHours(3).format(formatter)));
        holder.updatedAtTextView.setText(String.format("Atualizado em: %s", wish.getUpdatedAt().minusHours(3).format(formatter)));

        switch (wish.getPriority()) {
            case LOW:
                holder.priorityTextView.setBackgroundResource(R.drawable.tag_priority_low);
                holder.priorityTextView.setTextColor(holder.itemView.getResources().getColor(android.R.color.white));
                break;
            case MEDIUM:
                holder.priorityTextView.setBackgroundResource(R.drawable.tag_priority_medium);
                holder.priorityTextView.setTextColor(holder.itemView.getResources().getColor(android.R.color.white));
                break;
            case HIGH:
                holder.priorityTextView.setBackgroundResource(R.drawable.tag_priority_high);
                holder.priorityTextView.setTextColor(holder.itemView.getResources().getColor(android.R.color.white));
                break;
        }

        holder.completeImageView.setImageResource(wish.getStatus() == Status.OWNED ? R.drawable.ic_check : R.drawable.ic_undo);
        holder.completeImageView.setContentDescription(wish.getStatus() == Status.OWNED ?
                "Marcar como não concluído" : "Marcar como concluído");

        holder.linkImageView.setOnClickListener(v -> {
            if (listener != null) listener.onLinkClick(wish.getLink());
        });

        holder.deleteImageView.setOnClickListener(v -> {
            if (listener != null) listener.onDeleteClick(wish);
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(wish);
        });

        holder.completeImageView.setOnClickListener(v -> {
            if (listener != null) listener.onCheckClick(wish);
        });
    }

    @Override
    public int getItemCount() {
        return wishes != null ? wishes.size() : 0;
    }

    public static class WishViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView, priorityTextView, createdAtTextView, updatedAtTextView;
        ImageView linkImageView, deleteImageView, completeImageView;

        public WishViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.txt_wish_title);
            descriptionTextView = itemView.findViewById(R.id.txt_wish_description);
            priorityTextView = itemView.findViewById(R.id.txt_priority);
            createdAtTextView = itemView.findViewById(R.id.txt_created_at);
            updatedAtTextView = itemView.findViewById(R.id.txt_updated_at);
            linkImageView = itemView.findViewById(R.id.img_link);
            deleteImageView = itemView.findViewById(R.id.img_delete);
            completeImageView = itemView.findViewById(R.id.img_complete);
        }
    }
}