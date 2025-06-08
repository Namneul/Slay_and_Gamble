package com.example.slay_and_gamble;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.*;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    private List<Card> cards;

    public CardAdapter(List<Card> cards) {
        this.cards = cards;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Card card = cards.get(position);
        holder.cardName.setText(card.name);
        holder.cardCost.setText(String.valueOf(card.cost));
        holder.cardDescription.setText(card.description);
        int resId = holder.itemView.getContext().getResources().getIdentifier(
                card.imageName, "drawable", holder.itemView.getContext().getPackageName()
        );
        if (resId != 0){
            holder.cardImage.setImageResource(resId);
        } else {
            holder.cardImage.setImageResource(R.drawable.card_background); // 기본 이미지
            Log.e("CardAdapter", "이미지 리소스를 찾을 수 없음: " + card.imageName);
        }
        Log.d("CardAdapter", "카드: " + card.name + ", 이미지: " + card.imageName);
        Log.d("CardAdapter", "리소스 ID: " + resId);

    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {
        TextView cardName, cardCost, cardDescription;
        ImageView cardImage;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardName = itemView.findViewById(R.id.cardName);
            cardCost = itemView.findViewById(R.id.cardCost);
            cardDescription = itemView.findViewById(R.id.cardDescription);
            cardImage = itemView.findViewById(R.id.card_image);
        }
    }
}
