package DamageCalculators;

import Player.*;

public class AlhaithamCalculator extends DamageCalculator {
    public double Damage(Player player) {
        double totalHp = (player.baseHp * ((player.hpUp/100)+1)) + player.flatHp;
        double totalAttack = (player.baseAttack * ((player.attackUp/100)+1)) + player.flatAttack;
        double totalDefence = (player.baseDefence * ((player.defenceUp/100)+1)) + player.flatDefence;
        double skillMultiplier = player.skillMultiplier/100;
        double dualMultiplier = player.dualMultiplier/100;

        // Alhaitham passive
        double passive = player.elementalMastery * 0.1;
        if (passive > 100) {
            passive = 100;
        }

        double damageBonus = ((player.damageBonus+passive)/100)+1;
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
        
        return ((totalAttack * skillMultiplier) + (dualMultiplier * player.elementalMastery)) * damageBonus * defDamageReduction * resDamageReduction * reactionDamage;
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

    public double SpreadDamage(Player player) {
        // Alhaitham passive
        double passive = player.elementalMastery * 0.1;
        if (passive > 100) {
            passive = 100;
        }

        double damageBonus = ((player.damageBonus+passive)/100)+1;
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

        // Spread damage calculation
        double EM = player.elementalMastery;
        double spreadDamage = 1446.853458 * 1.25 * (1 + (5*EM)/(EM+1200));
        
        return Damage(player) + (spreadDamage * damageBonus * defDamageReduction * resDamageReduction) ;
    }

    public double SpreadCritDamage(Player player) {
        double critDamage = ((player.critDamage/100)+1);
        return critDamage * SpreadDamage(player);
    }

    public double SpreadAverageDamage(Player player) {
        double critRate = player.critRate;
        if (critRate >= 100) {
            critRate = 100;
        }
        critRate = critRate/100;
        double critDamage = ((player.critDamage/100)+1);
        
        return ((critRate * critDamage) + (1 - critRate)) * SpreadDamage(player);
    }

    public void printAll(Player player) {
        System.out.println("Damage(on crit): "+CritDamage(player));
        System.out.println("Damage(no crit): "+Damage(player));
        System.out.println("Average Damage: "+AverageDamage(player));
        System.out.println();
        System.out.println("Spread Damage(on crit): "+SpreadCritDamage(player));
        System.out.println("Spread Damage(no crit): "+SpreadDamage(player));
    }
}
