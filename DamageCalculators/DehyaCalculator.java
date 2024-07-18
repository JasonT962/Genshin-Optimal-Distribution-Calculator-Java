package DamageCalculators;

import Player.*;

public class DehyaCalculator extends DamageCalculator {
    public double Damage(Player player) {
        double totalHp = (player.baseHp * ((player.hpUp/100)+1)) + player.flatHp;
        double totalAttack = (player.baseAttack * ((player.attackUp/100)+1)) + player.flatAttack;
        double totalDefence = (player.baseDefence * ((player.defenceUp/100)+1)) + player.flatDefence;
        double skillMultiplier = player.skillMultiplier/100;
        double dualMultiplier = player.dualMultiplier/100;
        double damageBonus = (player.damageBonus/100)+1;
        double defDamageReduction = (player.characterLevel+100) / ((1-(player.defenceShred/100))*(1-(player.defenceIgnore/100))*(player.enemyLevel+100) + player.characterLevel + 100);

        // Calculations for resistance
        double resDamageReduction = (player.enemyResistance - player.resistanceShred) / 100;

        if (resDamageReduction < 0) {
            resDamageReduction = 1 - (resDamageReduction/2);
        }
        else if (resDamageReduction >= 0 && resDamageReduction < 0.75) {
            resDamageReduction = 1 - resDamageReduction;
        }
        else if (resDamageReduction >= 0.75) {
            resDamageReduction = 1 / (4*resDamageReduction+1);
        }
        else {
            resDamageReduction = 1;
        }

        // Calculations for reaction damage
        double reactionDamage = player.reactionDamage;
        if (reactionDamage <= 1) {
            reactionDamage = 1;
        }
        else {
            reactionDamage = reactionDamage * (1 + ((278 * (player.elementalMastery / (player.elementalMastery + 1400))) / 100));
        }
        
        return ((totalAttack * skillMultiplier) + (totalHp*dualMultiplier)) * damageBonus * defDamageReduction * resDamageReduction * reactionDamage;
    }

    public double CritDamage(Player player) {
        double critDamage = ((player.critDamage/100)+1);
        return critDamage * Damage(player);
    }

    public double AverageDamage(Player player) {
        double critRate = player.critRate;
        if (critRate >= 100) {
            critRate = 100;
        }
        critRate = critRate/100;
        double critDamage = ((player.critDamage/100)+1);
        
        return ((critRate * critDamage) + (1 - critRate)) * Damage(player);
    }
}
