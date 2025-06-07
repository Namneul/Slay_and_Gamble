package com.example.slay_and_gamble;

public interface GameEventListener {
    void onStatsChanged(); // HP, 에너지 등 바뀜
    void onBattleEnded(BattleResult result); // 승/패 판단됨
}
