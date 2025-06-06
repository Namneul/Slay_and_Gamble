package com.example.slay_and_gamble;

public class AttackEffect implements Effect {
    int value;

    public AttackEffect(int value) {this.value = value;}

    @Override
    public void apply(Player self, Enemy enemy) {
        enemy.hp -= value;
        if (enemy.hp <= 0) {
            enemy.hp = 0;
            enemy.die();
        }
    }
}
