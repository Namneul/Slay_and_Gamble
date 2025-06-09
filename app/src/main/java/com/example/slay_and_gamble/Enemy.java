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

    public Enemy(int stage) {
        maxHp = 50 + (stage - 1) * 10;   // 스테이지마다 +10
        hp = maxHp;
        damage = 5 + (stage - 1) * 1;    // 스테이지마다 +1
        decideNextAction();
    }

    public void takeAction(Player player){
        int totalDamage = damage + strength;
        int dealt;

        switch (nextAction){
            case ATTACK:
                dealt = calcEnemyDamageToPlayer(totalDamage, player.armor, player.vulnerable > 0);
                player.hp -= dealt;

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
                dealt = calcEnemyDamageToPlayer(totalDamage, player.armor, player.vulnerable > 0); // vulnerable은 공격에만 반영, 여기선 추가 옵션도 가능
                player.hp -= dealt;
                player.vulnerable += 2;
                break;
        }
        decideNextAction();
    }

    private int calcEnemyDamageToPlayer(int rawDamage, int armor, boolean isVulnerable) {
        double applied = rawDamage * (isVulnerable ? 1.5 : 1.0) - armor;
        int result = (int) Math.round(applied);
        return Math.max(result, 0);
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
