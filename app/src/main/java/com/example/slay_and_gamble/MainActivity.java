package com.example.slay_and_gamble;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.slay_and_gamble.databinding.GameBinding;

public class MainActivity extends AppCompatActivity {

    private GameBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = GameBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        EdgeToEdge.enable(this);
        setContentView(view);

        binding.startBtn.setOnClickListener(v ->{
            Intent intent = new Intent(this, BattleActivity.class);
            startActivity(intent);
        });
    }
}