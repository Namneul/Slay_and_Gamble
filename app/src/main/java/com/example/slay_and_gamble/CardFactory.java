package com.example.slay_and_gamble;

import static com.example.slay_and_gamble.TargetType.ENEMY;
import static com.example.slay_and_gamble.Type.*;

import java.util.*;

public class CardFactory {
    public static Card createBasicAttack(){
        return new Card(
                "타격",
                "피해를 6 줍니다",
                1,
                ATTACK,
                Arrays.asList(new AttackEffect(6)));
    }

    public static Card createBasicDefense(){
        return new Card(
                "수비",
                "방어도를 5 얻습니다.",
                1,
                DEFENSE,
                Arrays.asList(new GainArmorEffect(5)));
    }

    public static Card createBashAttack(){
        return new Card(
                "약점 강타",
                "피해를 8 줍니다. \n 취약을 2 부여합니다",
                2,
                ATTACK,
                Arrays.asList(new AttackEffect(8), new ApplyVulnerabilityEffect(2, ENEMY)));
    }

    public static Card createHeavyBlade(){
        return new Card(
                "대검",
                "피해를 14 줍니다. \n 힘의 효과가 2배로 적용됩니다.",
                2,
                ATTACK,
                Arrays.asList(new AttackEffect(8)));
    }

    public static Card createShieldAttack(){
        return new Card(
                "방패 타격",
                "피해를 5 줍니다. \n 방어도를 5 얻습니다.",
                1,
                ATTACK,
                Arrays.asList(new AttackEffect(5), new GainArmorEffect(5)));
    }


}
