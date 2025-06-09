package com.example.slay_and_gamble;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.slay_and_gamble.databinding.ActivityBattleBinding;

import java.util.List;

public class BattleActivity extends AppCompatActivity {

    private ActivityBattleBinding binding;
    private final String[] enemyImages = {"animal_monster", "skeleton_monster", "insect_monster"};
    CardAdapter adapter;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBattleBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        binding.endTurnBtn.setOnClickListener(v -> {
            GameManager.getInstance().playerEndTurn();
        });

        GameManager.getInstance().startBattle();
        Log.d("BattleActivity", "hand size: " +GameManager.getInstance().player.hand.size());

        binding.handView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );
        adapter = new CardAdapter(GameManager.getInstance().player.hand);
        binding.handView.setAdapter(adapter);
        binding.enemyIntent.setText(getIntentString(GameManager.getInstance().enemy.nextAction));




        // 1. 카드 클릭 시 바로 카드 사용
        adapter.setOnCardClickListener(card -> {
            GameManager gm = GameManager.getInstance();

            // 에너지 체크 & 카드 실제로 핸드에 남아있는지 확인
            if (!gm.player.hand.contains(card)) return;
            if (gm.player.energy < card.cost) {
                Toast.makeText(this, "에너지가 부족합니다!", Toast.LENGTH_SHORT).show();
                return;
            }

            gm.playerPlaysCard(card);

            // UI 갱신: 어댑터 데이터 변동, 상태뷰 등은 이벤트 리스너에서 자동 처리됨
            adapter.notifyDataSetChanged();
        });

        // 2. 상태 변경 시 UI 자동 갱신
        GameManager.getInstance().setEventListener(new GameEventListener() {
            @Override
            public void onStatsChanged() {
                runOnUiThread(() -> {
                    // 기존 상태들
                    binding.playerHp.setText("HP: " + GameManager.getInstance().player.hp);
                    binding.enemyHp.setText("HP: " + GameManager.getInstance().enemy.hp);
                    binding.playerEnergy.setText("에너지: " + GameManager.getInstance().player.energy);
                    binding.playerArmor.setText("방어도: " + GameManager.getInstance().player.armor);
                    binding.deckCount.setText(String.valueOf(GameManager.getInstance().player.deck.size()));
                    binding.discardPileCount.setText(String.valueOf(GameManager.getInstance().player.discardPile.size()));
                    // 디버프 추가!
                    binding.playerDebuff.setText("취약: " + GameManager.getInstance().player.vulnerable + " 힘" + GameManager.getInstance().player.strength);
                    binding.enemyDebuff.setText("취약: " + GameManager.getInstance().enemy.vulnerable + " 힘" + GameManager.getInstance().enemy.strength);
                    binding.enemyIntent.setText(getIntentString(GameManager.getInstance().enemy.nextAction));
                    binding.enemyArmor.setText("방어도" + GameManager.getInstance().enemy.armor);
                    adapter.notifyDataSetChanged();
                });
            }


            @Override
            public void onBattleEnded(BattleResult result) {
                runOnUiThread(() -> {
                    if (result == BattleResult.WIN) {
                        Toast.makeText(BattleActivity.this, "승리!", Toast.LENGTH_SHORT).show();
                        // 보상 카드 3장 띄우기
                        showRewardCards();
                    } else if (result == BattleResult.LOSE) {
                        Toast.makeText(BattleActivity.this, "패배!", Toast.LENGTH_SHORT).show();
                        showGameOverDialog();
                    }
                });
            }

            public void onEnemyAttacked() {
                animateEnemyHit();
            }
            public void onPlayerAttacked() {
                animatePlayerHit();
            }
        });



    }
    private String getIntentString(Enemy.Intent intent) {
        switch (intent) {
            case ATTACK: return "공격 예정 (" + (GameManager.getInstance().enemy.damage + GameManager.getInstance().enemy.strength) + " 데미지)";
            case BLOCK: return "방어 예정";
            case BUFF: return "강화 예정";
            case DEBUFF: return "디버프 예정";
            case ATTACK_DEBUFF: return "공격+디버프 예정 (" + (GameManager.getInstance().enemy.damage + GameManager.getInstance().enemy.strength) + " 데미지)";

            default: return "행동 없음";
        }
    }

    private void setRandomEnemyImage() {
        int idx = (int) (Math.random() * enemyImages.length);
        int resId = getResources().getIdentifier(enemyImages[idx], "drawable", getPackageName());
        Log.d("BattleActivity", "랜덤 적 이미지 idx=" + idx + ", name=" + enemyImages[idx] + ", resId=" + resId);
        binding.enemyImg.setImageResource(resId != 0 ? resId : R.drawable.animal_monster);
    }

    public void animateEnemyHit() {
        binding.enemyImg.setColorFilter(0x88FF0000, PorterDuff.Mode.SRC_ATOP);

        // 2. 흔들림 애니메이션도 동시에!
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        binding.enemyImg.startAnimation(shake);

        // 3. 180ms(흔들림 중간쯤) 뒤에 원래대로 복원
        binding.enemyImg.postDelayed(() -> {
            binding.enemyImg.clearColorFilter();
        }, 180);
    }
    public void animatePlayerHit() {
        binding.playerImg.setColorFilter(0x88FF0000, PorterDuff.Mode.SRC_ATOP);

        // 2. 흔들림 애니메이션도 동시에!
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        binding.playerImg.startAnimation(shake);

        // 3. 180ms(흔들림 중간쯤) 뒤에 원래대로 복원
        binding.playerImg.postDelayed(() -> {
            binding.playerImg.clearColorFilter();
        }, 180);
    }



    private void showRewardCards() {
        // 1. GameManager에서 보상 후보 3장 뽑기
        GameManager.getInstance().showReward();

        // 2. 레이아웃 클리어 & 보이게
        binding.rewardLayout.removeAllViews();
        binding.rewardGroup.setVisibility(View.VISIBLE);
        binding.rewardLayout.setVisibility(View.VISIBLE);

        // 3. 보상 카드 3장을 동적으로 생성
        List<Card> rewardCards = GameManager.getInstance().rewardCards;
        for (int i = 0; i < rewardCards.size(); i++) {
            Card card = rewardCards.get(i);

            // 카드 뷰(작은 레이아웃) inflate
            View cardView = getLayoutInflater().inflate(R.layout.item_card, binding.rewardLayout, false);

            // 카드 정보 세팅 (아이디, 이름 등 findViewById로)
            TextView name = cardView.findViewById(R.id.cardName);
            TextView cost = cardView.findViewById(R.id.cardCost);
            TextView desc = cardView.findViewById(R.id.cardDescription);
            ImageView illust = cardView.findViewById(R.id.card_illustration);
            ImageView bg = cardView.findViewById(R.id.card_bg);

            name.setText(card.name);
            cost.setText(String.valueOf(card.cost));
            desc.setText(card.description);
            int illuResId = getResources().getIdentifier(card.imageName, "drawable", getPackageName());
            illust.setImageResource(illuResId != 0 ? illuResId : R.drawable.back1);
            bg.setImageResource(R.drawable.card_background);

            int index = i; // final for lambda

            // 카드 클릭 시 해당 카드 덱에 추가 & 보상뷰/버튼 숨김
            cardView.setOnClickListener(v -> {
                GameManager.getInstance().selectRewardCard(index);
                Toast.makeText(this, card.name + " 카드가 덱에 추가되었습니다!", Toast.LENGTH_SHORT).show();
                binding.rewardGroup.setVisibility(View.GONE);
                GameManager.getInstance().stageNumber++;

                // 다음 전투 시작
                GameManager.getInstance().startBattle();
                setRandomEnemyImage();
            });

            // 레이아웃에 카드 추가
            binding.rewardLayout.addView(cardView);
        }
    }

    private void showGameOverDialog() {
        GameManager gm = GameManager.getInstance();

        // 다이얼로그 커스텀 레이아웃 inflate
        View dialogView = getLayoutInflater().inflate(R.layout.game_over_dialog, null);

        ImageView logoImg = dialogView.findViewById(R.id.gameOverImg);
        // (이미지 자동 할당, 필요시 logoImg.setImageResource(...)로 강제할당도 가능)

        TextView currentStreak = dialogView.findViewById(R.id.currentStreak);
        TextView bestStreak = dialogView.findViewById(R.id.bestStreak);

        currentStreak.setText("이번 연승: " + gm.winStreak + "회");
        bestStreak.setText("최고 연승: " + gm.bestStreak + "회");

        ImageButton retryBtn = dialogView.findViewById(R.id.retryBtn);

        AlertDialog dialog = new AlertDialog.Builder(this, R.style.DialogTheme)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        retryBtn.setOnClickListener(v -> {
            gm.resetStreaks();
            dialog.dismiss();
            gm.startBattle();
            GameManager.getInstance().stageNumber = 1;
            setRandomEnemyImage();
            GameManager.getInstance().resetGame();
            Intent intent = new Intent(BattleActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();


            // 필요하다면 UI 갱신 등 추가
        });

        dialog.show();
    }





}
