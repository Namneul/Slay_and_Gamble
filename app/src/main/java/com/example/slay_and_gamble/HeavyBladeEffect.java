package com.example.slay_and_gamble;

public class HeavyBladeEffect implements Effect{

    int damage;
    public HeavyBladeEffect(int damage){this.damage = damage;}
    @Override
    public void apply(Player self, Enemy enemy) {
        int totalDamage = damage + self.strength*2;
        if (enemy.vulnerable > 0){
            enemy.hp -= (int)Math.ceil(totalDamage * 1.5);
        }else{
            enemy.hp -= totalDamage;
        }
    }
}
