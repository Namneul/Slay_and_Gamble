package com.example.slay_and_gamble;

enum Type {ATTACK, DEFENSE, SKILL, POWER};

public class Card {
    String name;
    String description;
    int cost;
    int value;

    Type type;

    void applyEffect(Player self, Enemy enemy){
        switch (this.type){
            case ATTACK:
                enemy.hp -= value;
                if (enemy.hp <= 0){
                    enemy.hp = 0;
                    enemy.die();
                }
                break;
            case DEFENSE:
                self.armor += value;
                break;
            case SKILL:

                break;
            case POWER:

                break;

        }
    }
}
