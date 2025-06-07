package com.example.slay_and_gamble;

public class Enemy {
    int strength = 0;
    int vulnerable = 0;

    int hp, maxHp;
    String name;
    public enum Intent {ATTACK, BLOCK, BUFF, DEBUFF, ATTACK_DEBUFF};
    int damage = 5;
    int armor = 0;

    Intent nextAction;

    public void takeAction(Player player){
        switch (nextAction){
            case ATTACK:
                player.hp -= (damage + strength);
                break;
            case BLOCK:
                armor += 5;
                break;
            case BUFF:
                strength += 1;
                break;
            case DEBUFF:
                player.vulnerable += 2;
                break;
            case ATTACK_DEBUFF:
                player.hp -= (damage + strength);
                player.vulnerable += 2;
                break;
        }
        decideNextAction();
    }

    public void decideNextAction(){
        int rand = (int)(Math.random()*5);
        switch (rand){
            case 0:
                nextAction = Intent.ATTACK;
                break;
            case 1:
                nextAction = Intent.BLOCK;
                break;
            case 2:
                nextAction = Intent.BUFF;
                break;
            case 3:
                nextAction = Intent.DEBUFF;
                break;
            case 4:
                nextAction = Intent.ATTACK_DEBUFF;
                break;
        }
    }

    public void die(){

    }
}
