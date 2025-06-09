package com.example.slay_and_gamble;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private List<Card> cards;

    // 1. 카드 클릭 리스너 인터페이스 선언
    public interface OnCardClickListener {
        void onCardClick(Card card);
    }

    private OnCardClickListener cardClickListener;

    // 2. 리스너 세터
    public void setOnCardClickListener(OnCardClickListener listener) {
        this.cardClickListener = listener;
    }

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

        holder.cardName.setText(card.name != null ? card.name : "이름 없음");
        holder.cardCost.setText(String.valueOf(card.cost));
        holder.cardDescription.setText(card.description != null ? card.description : "");

        int illuResId = holder.itemView.getContext().getResources()
                .getIdentifier(card.imageName, "drawable", holder.itemView.getContext().getPackageName());
        holder.cardIllustration.setImageResource(illuResId != 0 ? illuResId : R.drawable.back1);
        holder.cardBg.setImageResource(R.drawable.card_background);

        // 3. 클릭 이벤트에 리스너 연결 (카드 객체만 넘김)
        holder.itemView.setOnClickListener(v -> {
            if (cardClickListener != null) {
                cardClickListener.onCardClick(card);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {
        ImageView cardBg, cardIllustration;
        TextView cardName, cardCost, cardDescription;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardBg = itemView.findViewById(R.id.card_bg);
            cardIllustration = itemView.findViewById(R.id.card_illustration);
            cardName = itemView.findViewById(R.id.cardName);
            cardCost = itemView.findViewById(R.id.cardCost);
            cardDescription = itemView.findViewById(R.id.cardDescription);
        }
    }
}
