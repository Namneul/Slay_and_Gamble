package com.example.slay_and_gamble;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        holder.cardCost.setText(card.cost);
        holder.cardDescription.setText(card.description);
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {
        TextView cardName, cardCost, cardDescription;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardName = itemView.findViewById(R.id.cardName);
            cardCost = itemView.findViewById(R.id.cardCost);
            cardDescription = itemView.findViewById(R.id.cardDescription);
        }
    }
}
