package com.example.slay_and_gamble;

public class GainArmorEffect implements Effect{

    int value;
    public GainArmorEffect(int value){this.value = value;}
    @Override
    public void apply(Player self, Enemy enemy) {
        self.armor += value;

    }
}
