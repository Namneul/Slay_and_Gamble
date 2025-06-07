package com.example.slay_and_gamble;

import java.util.List;

enum Type {ATTACK, DEFENSE, SKILL, POWER};

public class Card {
    String name;
    String description;
    int cost;
    Type type;
    List<Effect> effects;
    String imageName; // 생성자에 추가 ㄱㄱ

    public Card(String name, String description, int cost, Type type, List<Effect> effects) {
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.type = type;
        this.effects = effects;
    }


    void applyEffect(Player self, Enemy enemy){
        for (Effect effect: effects){
            effect.apply(self, enemy);
        }
    }
}
