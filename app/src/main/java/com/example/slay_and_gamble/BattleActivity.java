package com.example.slay_and_gamble;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slay_and_gamble.databinding.ActivityBattleBinding;

import java.util.List;

public class BattleActivity extends AppCompatActivity {

    private ActivityBattleBinding binding;
    CardAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBattleBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        GameManager gameManager = GameManager.getInstance();
        gameManager.startBattle();
        Player player = gameManager.player;

        binding.handView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );
        adapter = new CardAdapter(player.hand);
        binding.handView.setAdapter(adapter);

        gameManager.setEventListener(new GameEventListener() {
            @Override
            public void onStatsChanged() {
                // HP, 에너지 등 갱신
                runOnUiThread(() -> {
                    binding.playerHp.setText("HP: " + gameManager.player.hp);
                    binding.enemyHp.setText("HP: " + gameManager.enemy.hp);
                    binding.playerEnergy.setText("에너지: " + gameManager.player.energy);
                    binding.playerArmor.setText("방어도: " + gameManager.player.armor);
                    binding.deckCount.setText(GameManager.getInstance().player.deck.size());
                    adapter.notifyDataSetChanged();
                });
            }

            @Override
            public void onBattleEnded(BattleResult result) {
                runOnUiThread(() -> {
                    if (result == BattleResult.WIN) {
                        Toast.makeText(BattleActivity.this, "승리!", Toast.LENGTH_SHORT).show();
                    } else if (result == BattleResult.LOSE) {
                        Toast.makeText(BattleActivity.this, "패배!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
