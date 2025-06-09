package com.example.slay_and_gamble;

import java.util.ArrayList;
import java.util.Collections;

public class Player {
    int strength = 0;
    int vulnerable = 0;
    int maxHp = 70;
    int hp = maxHp;
    int maxEnergy = 3;
    int energy = maxEnergy;
    int armor = 0;
    boolean isHandFull = false;
    ArrayList<Card> deck;
    ArrayList<Card> hand;
    ArrayList<Card> discardPile;

    // ★★ 반드시 생성자에서 초기화! ★★
    public Player() {
        deck = new ArrayList<>();
        hand = new ArrayList<>();
        discardPile = new ArrayList<>();
        // 기본 덱 구성 예시 (원하는 대로 커스텀)
        for (int i = 0; i < 5; i++) deck.add(CardFactory.createBasicAttack());
        for (int i = 0; i < 4; i++) deck.add(CardFactory.createBasicDefense());
        deck.add(CardFactory.createBashAttack());
        deck.add(CardFactory.createShieldAttack());
        Collections.shuffle(deck);
    }

    void drawCard(int count) {
        for (int i = 0; i < count; i++) {
            // 1. 핸드가 가득 찼으면 그만
            if (hand.size() >= 8) {
                isHandFull = true;
                break;
            }
            // 2. 덱이 비어있으면 버린 더미에서 복구
            if (deck.isEmpty()) {
                if (discardPile.isEmpty()) break; // 더 이상 드로우 불가
                deck.addAll(discardPile);
                discardPile.clear();
                Collections.shuffle(deck);
            }
            // 3. 덱이 다시 비었으면 break
            if (deck.isEmpty()) break;
            // 4. 정상 드로우
            Card drawnCard = deck.remove(0);
            hand.add(drawnCard);
        }
    }

    void discardHand() {
        discardPile.addAll(hand);
        hand.clear();
    }
}
