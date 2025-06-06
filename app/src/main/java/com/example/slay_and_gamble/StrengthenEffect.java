package com.example.slay_and_gamble;

public class StrengthenEffect implements Effect {
    int strength;
    TargetType target;
    public StrengthenEffect(int strength, TargetType target){
        this.strength = strength;
        this.target = target;
    }

    @Override
    public void apply(Player self, Enemy enemy) {
        switch (target){
            case PLAYER:
                self.strength += strength;
                break;
            case ENEMY:
                enemy.strength += strength;
        }
    }
}
