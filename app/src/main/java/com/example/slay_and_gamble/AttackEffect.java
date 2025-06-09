package com.example.slay_and_gamble;

public class AttackEffect implements Effect {
    int damage;

    public AttackEffect(int damage) {this.damage = damage;}

    @Override
    public void apply(Player self, Enemy enemy) {
        int totalDamage = damage + self.strength;
        if (enemy.vulnerable > 0){

            enemy.hp -= ((int)Math.ceil(totalDamage * 1.5) - enemy.armor);
        }else{
            enemy.hp -= (totalDamage - enemy.armor);

        }
        if (enemy.hp <= 0) {
            enemy.hp = 0;
            enemy.die();
        }
    }
}
