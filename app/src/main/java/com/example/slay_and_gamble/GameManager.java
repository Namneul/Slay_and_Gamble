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

    private GameManager() {} // 외부에서 생성 불가
    BattleResult battleResult = BattleResult.NONE;
    Player player;
    Enemy enemy;
    int turnNumber;
    List<Card> rewardCards;

    public void startBattle(){
        if (player == null) {
            player = new Player();
            player.deck = new ArrayList<>();
            player.hand = new ArrayList<>();
            player.discardPile = new ArrayList<>();

            // 초반 덱 구성은 단 한 번만!
            for (int i = 0; i < 5; i++) player.deck.add(CardFactory.createBasicAttack());
            for (int i = 0; i < 4; i++) player.deck.add(CardFactory.createBasicDefense());
            player.deck.add(CardFactory.createBashAttack());
            player.deck.add(CardFactory.createShieldAttack());
            player.deck.add(CardFactory.createBulkUp());
            Collections.shuffle(player.deck);
        }
        else {
            player.energy = player.maxEnergy;
            player.armor = 0;
            player.hand.clear();
            player.discardPile.clear();
            // deck은 그대로 유지 (셔플은 필요시)
        }
        enemy = new Enemy();
        turnNumber = 1;
        startPlayerTurn();
        player.drawCard(4);
    }

    public void startPlayerTurn(){
        player.energy = player.maxEnergy;
        player.drawCard(1);
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
        startEnemyTurn();
        player.discardHand();
        if (eventListener != null) eventListener.onStatsChanged();
    }


    public void startEnemyTurn(){
        enemy.takeAction(player);
        if (eventListener != null) eventListener.onStatsChanged();
        endBattle();
        endEnemyTurn();
    }

    public void endEnemyTurn(){
        turnNumber++;
        startPlayerTurn();
    }

    public void endBattle() {
        if (player.hp <= 0) {
            // 플레이어 패배 처리
            battleResult = BattleResult.LOSE;
            System.out.println("패배! 게임 오버!");
            // 추가로: 게임 오버 화면, 리셋, 종료 등 로직
        } else if (enemy.hp <= 0) {
            // 플레이어 승리 처리
            battleResult = BattleResult.WIN;
            System.out.println("승리! 보상을 획득합니다.");
            // 추가로: 보상 화면, 다음 전투, 결과 저장 등 로직
        }
        // 전투 관련 상태 초기화 필요하면 여기서
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

    public void selectRewardCard(int index) {
        if (rewardCards != null && index >= 0 && index < rewardCards.size()) {
            Card chosen = rewardCards.get(index);
            player.deck.add(chosen);
            System.out.println(chosen.name + " 카드가 덱에 추가되었습니다.");
        }
    }


    private GameEventListener eventListener;

    public void setEventListener(GameEventListener eventListener){
        this.eventListener = eventListener;
    }

}
