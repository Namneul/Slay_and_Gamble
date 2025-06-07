package com.example.slay_and_gamble;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Player {
    int strength = 0;
    int vulnerable = 0;
    int maxHp = 70;
    int hp = maxHp;
    int maxEnergy = 3;
    int energy = 3;
    int armor = 0;
    boolean isHandFull;
    ArrayList<Card> deck;
    ArrayList<Card> hand;
    ArrayList<Card> discardPile;

    void drawCard(int count){
        // isHandFull, isEmpty 등등 확인
        for (int i = 0; i < count; i++){
        if (deck.isEmpty()){
            deck.addAll(discardPile);
            discardPile.clear();
            Collections.shuffle(deck);
        } else{
            return;
        }
        if (hand.size() >= 8){
            isHandFull = true;
            break;
        } else {
            Card drawnCard = deck.remove(0);
            hand.add(drawnCard);
            }
        }
    }
    void discardHand(){
        discardPile.addAll(hand);
        hand.clear();
    }
}
