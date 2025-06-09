package com.example.slay_and_gamble;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

enum BattleResult {NONE, WIN, LOSE}

public class GameManager {
    private static GameManager instance;

    public static GameManager getInstance() {
        if (instance == null) instance = new GameManager();
        return instance;
    }

    public int winStreak = 0;      // 현재 연승 수
    public int bestStreak = 0;     // 최고 연승 수
    public int stageNumber = 1;
    private GameManager() {} // 외부에서 생성 불가
    BattleResult battleResult = BattleResult.NONE;
    Player player;
    Enemy enemy;
    int turnNumber;
    List<Card> rewardCards;

    public void startBattle(){
        if (player == null) {
            // 최초 1회만 Player 생성
            player = new Player();
            // Player 생성자에서 deck, hand, discardPile, maxEnergy 등 초기화
        } else {
            // Player를 새로 만들지 않고 상태만 리셋
            player.energy = player.maxEnergy;
            player.armor = 0;
            player.vulnerable = 0;
            player.strength = 0;
            player.deck.addAll(player.hand);
            player.deck.addAll(player.discardPile);
            player.hand.clear();
            player.discardPile.clear();
            Collections.shuffle(player.deck);
        }
        enemy = new Enemy(stageNumber);
        turnNumber = 1;
        player.drawCard(4); // 전투 시작 시 핸드 드로우
        if (eventListener != null) eventListener.onStatsChanged();
    }


    public void startPlayerTurn(){
        player.energy = player.maxEnergy;
        player.drawCard(4);
        player.armor = 0;
        if (player.vulnerable > 0){
            player.vulnerable--;
        }
        if (eventListener != null) eventListener.onStatsChanged();
    }

    public void playerPlaysCard(Card card){
        if (player.energy >= card.cost){
            player.energy -= card.cost;
            card.applyEffect(player, enemy);
            player.hand.remove(card);
            player.discardPile.add(card);
            endBattle();
            if (eventListener != null) eventListener.onStatsChanged();
        }
    }

    public void playerEndTurn(){
        player.discardHand();
        startEnemyTurn();
        if (eventListener != null) eventListener.onStatsChanged();
    }


    public void startEnemyTurn(){
        enemy.armor = 0;

        if (enemy.vulnerable > 0){
            enemy.vulnerable--;
        }
        enemy.takeAction(player);
        if (eventListener != null) eventListener.onStatsChanged();
        endBattle();
        endEnemyTurn();
    }

    public void endEnemyTurn(){
        turnNumber++;
        startPlayerTurn();
        enemy.decideNextAction();
    }

    public void endBattle() {
        if (player.hp <= 0) {
            // 플레이어 패배 처리
            battleResult = BattleResult.LOSE;
            if (winStreak > bestStreak) bestStreak = winStreak;
            System.out.println("패배! 게임 오버!");
            if (eventListener != null) eventListener.onBattleEnded(battleResult);
        } else if (enemy.hp <= 0) {
            // 플레이어 승리 처리
            battleResult = BattleResult.WIN;
            winStreak++;
            System.out.println("승리! 보상을 획득합니다.");
            if (eventListener != null) eventListener.onBattleEnded(battleResult);
        }
    }

    public void showReward(){
        List<Card> candidates = new ArrayList<>(CardFactory.getRewardCandidates());
        Collections.shuffle(candidates);
        rewardCards = candidates.subList(0, 3);

        System.out.println("보상 카드 선택하세요.");
        for (int i = 0; i < rewardCards.size(); i++) {
            Card card = rewardCards.get(i);
            System.out.printf("%d. %s - %s\n", i + 1, card.name, card.description);
        }


    }

    public void resetStreaks() {
        winStreak = 0;
        // bestStreak는 앱 재시작 전까지 유지 (원하면 여기서도 초기화 가능)
    }

    public void selectRewardCard(int index) {
        if (rewardCards != null && index >= 0 && index < rewardCards.size()) {
            Card chosen = rewardCards.get(index);
            player.deck.add(chosen);
            System.out.println(chosen.name + " 카드가 덱에 추가되었습니다.");
        }
    }
    public void resetGame() {
        // 모든 게임 상태, 플레이어, 적, 보상 등 전부 null로!
        player = null;
        enemy = null;
        winStreak = 0;
        battleResult = BattleResult.NONE;
        rewardCards = null;
        turnNumber = 0;
        // 필요하면 bestStreak도 0으로 (하지만 최고기록은 남겨도 됨)
    }



    private GameEventListener eventListener;

    public void setEventListener(GameEventListener eventListener){
        this.eventListener = eventListener;
    }

}
