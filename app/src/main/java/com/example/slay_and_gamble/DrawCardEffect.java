package com.example.slay_and_gamble;

public class DrawCardEffect implements Effect{

    int count;
    public DrawCardEffect(int count){this.count = count;}
    @Override
    public void apply(Player self, Enemy enemy) {
        self.drawCard(count);
    }
}
