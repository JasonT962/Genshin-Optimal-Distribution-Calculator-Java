package OptimalDistributionCalculator;

import java.util.ArrayList;

import Player.*;
import Player.Artifact.mainstat;
import Player.Artifact.substat;

import DamageCalculators.*;

public class Optimizer {
    private Player player;
    private DamageCalculator calculator;

    // Artifact Roll Values
    private double rollHP = 4.95;
    private double rollFlatHP = 254;
    private double rollAttack = 4.95;
    private double rollFlatAttack = 16.5;
    private double rollDefence = 6.2;
    private double rollFlatDefence = 19.5;
    private double rollCR = 3.3;
    private double rollCD = 6.6;
    private double rollEM = 19.5;
    private double rollER = 5.5;

    // Set to 5 if you want artifacts to start with 4 substats, set to 4 if you want artifacts to start with 3 substats
    private double addRolls = 4;

    public Optimizer(DamageCalculator calculator) {
        player = new Player();
        this.calculator = calculator;
    }

    // Compares main stat combinations and returns the set with the highest average damage
    public Artifact[] SetMainStats() {
        Artifact flower = new Artifact(mainstat.FlatHealth, substat.None, substat.None, substat.None, substat.None);
        Artifact feather = new Artifact(mainstat.FlatAttack, substat.None, substat.None, substat.None, substat.None);
        Artifact sands = new Artifact(mainstat.None, substat.None, substat.None, substat.None, substat.None);
        Artifact goblet = new Artifact(mainstat.None, substat.None, substat.None, substat.None, substat.None);
        Artifact circlet = new Artifact(mainstat.None, substat.None, substat.None, substat.None, substat.None);

        // List of possible main stats for sands, goblets and circlets
        ArrayList<mainstat> sandsList = new ArrayList<mainstat>();
        sandsList.add(mainstat.Health);
        sandsList.add(mainstat.Attack);
        sandsList.add(mainstat.Defence);
        sandsList.add(mainstat.ElementalMastery);
        sandsList.add(mainstat.EnergyRecharge);

        ArrayList<mainstat> gobletList = new ArrayList<mainstat>();
        gobletList.add(mainstat.Health);
        gobletList.add(mainstat.Attack);
        gobletList.add(mainstat.Defence);
        gobletList.add(mainstat.ElementalMastery);

        if (player.elementalDamage == true) {
            gobletList.add(mainstat.EleDmgBonus);
        }
        else {
            gobletList.add(mainstat.PhysDmgBonus);
        }

        ArrayList<mainstat> circletList = new ArrayList<mainstat>();
        circletList.add(mainstat.Health);
        circletList.add(mainstat.Attack);
        circletList.add(mainstat.Defence);
        circletList.add(mainstat.ElementalMastery);
        circletList.add(mainstat.CritRate);
        circletList.add(mainstat.CritDamage);
        circletList.add(mainstat.HealingBonus);

        Artifact[] defaultSet = {flower,feather,sands,goblet,circlet};
        double defaultDamage = MaxDamage(defaultSet);

        
        // Filter out main stats that don't increase damage
        // Sands
        ArrayList<mainstat> toKeep = new ArrayList<mainstat>();
        for (int i = 0; i < sandsList.size(); i++) {
            sands = new Artifact(sandsList.get(i), substat.None, substat.None, substat.None, substat.None);
            Artifact[] temp = {flower,feather,sands,goblet,circlet};
            if (MaxDamage(temp) > defaultDamage) {
                toKeep.add(sandsList.get(i));
            }
        }
        sandsList = toKeep;
        sands = new Artifact(mainstat.None, substat.None, substat.None, substat.None, substat.None);

        // Goblet
        toKeep = new ArrayList<mainstat>();
        for (int i = 0; i < gobletList.size(); i++) {
            goblet = new Artifact(gobletList.get(i), substat.None, substat.None, substat.None, substat.None);
            Artifact[] temp = {flower,feather,sands,goblet,circlet};
            if (MaxDamage(temp) > defaultDamage) {
                toKeep.add(gobletList.get(i));
            }
        }
        gobletList = toKeep;
        goblet = new Artifact(mainstat.None, substat.None, substat.None, substat.None, substat.None);
        
        // Circlet
        toKeep = new ArrayList<mainstat>();
        for (int i = 0; i < circletList.size(); i++) {
            circlet = new Artifact(circletList.get(i), substat.None, substat.None, substat.None, substat.None);
            Artifact[] temp = {flower,feather,sands,goblet,circlet};
            if (MaxDamage(temp) > defaultDamage) {
                toKeep.add(circletList.get(i));
            }
        }
        circletList = toKeep;
        circlet = new Artifact(mainstat.None, substat.None, substat.None, substat.None, substat.None);

        double highestDamage = 0;
        double damage = 0;

        //The set we're going to change to compare to previous highest damage
        Artifact[] testSet = {flower,feather,sands,goblet,circlet};

        //The set which contains the current highest damage
        Artifact[] finalSet = {flower,feather,sands,goblet,circlet};

        // Loops through all possible main stat combinations
        for (int i = 0; i < sandsList.size(); i++) {
            for (int j = 0; j < gobletList.size(); j++) {
                for (int k = 0; k < circletList.size(); k++) {
                    sands = new Artifact(sandsList.get(i), substat.None, substat.None, substat.None, substat.None);
                    goblet = new Artifact(gobletList.get(j), substat.None, substat.None, substat.None, substat.None);
                    circlet = new Artifact(circletList.get(k), substat.None, substat.None, substat.None, substat.None);

                    testSet = new Artifact[] {flower,feather,sands,goblet,circlet};
                    damage = MaxDamage(SetSubStats(testSet)); // Applies best substats and damage the set can do

                    if (damage > highestDamage) {
                        highestDamage = damage;
                        finalSet = testSet;
                    }
                }
            }
        }
        return finalSet;
    }

    // Returns set with substats that gives highest average damage
    public Artifact[] SetSubStats(Artifact[] set) {
        // Reset substats for all artifacts
        for (int i = 0; i < set.length; i++) {
            set[i].sub1 = substat.None;
            set[i].sub2 = substat.None;
            set[i].sub3 = substat.None;
            set[i].sub4 = substat.None;
        }

        // Loops through each artifact in the set
        for (int i = 0; i < set.length; i++) {
            ArrayList<substat> substatList = new ArrayList<substat>();
            substatList.add(substat.Health);
            substatList.add(substat.FlatHealth);
            substatList.add(substat.Attack);
            substatList.add(substat.FlatAttack);
            substatList.add(substat.Defence);
            substatList.add(substat.FlatDefence);
            substatList.add(substat.ElementalMastery);
            substatList.add(substat.EnergyRecharge);
            substatList.add(substat.CritRate);
            substatList.add(substat.CritDamage);

            // Removes substat from list that is the same as main stat
            for (int j = 0; j < substatList.size(); j++) {
                if (set[i].main.name().equals(substatList.get(j).name())) {
                    substatList.remove(j);
                    break;
                }
            }

            double highestDamage = 0;
            double damage = 0;
            substat bestSubstat = substatList.get(substatList.size()-1); // Set default best substat to last one in list (EnergyRecharge)

            // Substat 1
            for (int j = 0; j < substatList.size(); j++) {
                set[i].sub1 = substatList.get(j);
                damage = MaxDamage(set);

                if (damage > highestDamage) {
                    highestDamage = damage;
                    bestSubstat = substatList.get(j);
                }
            }
            set[i].sub1 = bestSubstat;
            substatList.remove(bestSubstat);
            bestSubstat = substatList.get(substatList.size()-1);

            // Substat 2
            for (int j = 0; j < substatList.size(); j++) {
                set[i].sub2 = substatList.get(j);
                damage = MaxDamage(set);

                if (damage > highestDamage) {
                    highestDamage = damage;
                    bestSubstat = substatList.get(j);
                }
            }
            set[i].sub2 = bestSubstat;
            substatList.remove(bestSubstat);
            bestSubstat = substatList.get(substatList.size()-1);

            // Substat 3
            for (int j = 0; j < substatList.size(); j++) {
                set[i].sub3 = substatList.get(j);
                damage = MaxDamage(set);

                if (damage > highestDamage) {
                    highestDamage = damage;
                    bestSubstat = substatList.get(j);
                }
            }
            set[i].sub3 = bestSubstat;
            substatList.remove(bestSubstat);
            bestSubstat = substatList.get(substatList.size()-1);

            // Substat 4
            for (int j = 0; j < substatList.size(); j++) {
                set[i].sub4 = substatList.get(j);
                damage = MaxDamage(set);

                if (damage > highestDamage) {
                    highestDamage = damage;
                    bestSubstat = substatList.get(j);
                }
            }
            set[i].sub4 = bestSubstat;
            substatList.remove(bestSubstat);
            bestSubstat = substatList.get(substatList.size()-1);
        }
        return set;
    }

    //Returns the highest average damage an artifact set could do
    public double MaxDamage(Artifact[] set) {
        // Refresh player so previous stats added get reset
        player = new Player();

        // Roll Limits for each substat
        double hpUpLimit = 0;
        double flatHPLimit = 0;
        double attackUpLimit = 0;
        double flatAttackLimit = 0;
        double defenceUpLimit = 0;
        double flatDefenceLimit = 0;
        double crLimit = 0;
        double cdLimit = 0;
        double emLimit = 0;
        double erLimit = 0;

        // Apply main stats and sub stats
        for (int i = 0; i < set.length; i++) {
            switch(set[i].main) {
                case Health:
                    player.hpUp += 46.6;
                    break;
                case FlatHealth:
                    player.flatHp += 4780;
                    break;
                case Attack:
                    player.attackUp += 46.6;
                    break;
                case FlatAttack:
                    player.flatAttack += 311;
                    break;
                case Defence:
                    player.defenceUp += 58.3;
                    break;
                case ElementalMastery:
                    player.elementalMastery += 187;
                    break;
                case EnergyRecharge:
                    player.energyRecharge += 51.8;
                    break;
                case EleDmgBonus:
                    player.damageBonus += 46.6;
                    break;
                case PhysDmgBonus:
                    player.damageBonus += 58.3;
                    break;
                case CritRate:
                    player.critRate += 31.1;
                    break;
                case CritDamage:
                    player.critDamage += 62.2;
                    break;
                case HealingBonus:
                    player.healingBonus += 35.9;
                    break;
                case None:
                    break;
                default:
                    break;
            }

            switch(set[i].sub1) {
                case Health:
                    player.hpUp += rollHP;
                    hpUpLimit += addRolls;
                    break;
                case FlatHealth:
                    player.flatHp += rollFlatHP;
                    flatHPLimit += addRolls;
                    break;
                case Attack:
                    player.attackUp += rollAttack;
                    attackUpLimit += addRolls;
                    break;
                case FlatAttack:
                    player.flatAttack += rollFlatAttack;
                    flatAttackLimit += addRolls;
                    break;
                case Defence:
                    player.defenceUp += rollDefence;
                    defenceUpLimit += addRolls;
                    break;
                case FlatDefence:
                    player.flatDefence += rollFlatDefence;
                    flatDefenceLimit += addRolls;
                    break;
                case ElementalMastery:
                    player.elementalMastery += rollEM;
                    emLimit += addRolls;
                    break;
                case EnergyRecharge:
                    player.energyRecharge += rollER;
                    erLimit += addRolls;
                    break;
                case CritRate:
                    player.critRate += rollCR;
                    crLimit += addRolls;
                    break;
                case CritDamage:
                    player.critDamage += rollCD;
                    cdLimit += addRolls;
                    break;
                case None:
                    break;
                default:
                    break;
            }

            switch(set[i].sub2) {
                case Health:
                    player.hpUp += rollHP;
                    hpUpLimit += addRolls;
                    break;
                case FlatHealth:
                    player.flatHp += rollFlatHP;
                    flatHPLimit += addRolls;
                    break;
                case Attack:
                    player.attackUp += rollAttack;
                    attackUpLimit += addRolls;
                    break;
                case FlatAttack:
                    player.flatAttack += rollFlatAttack;
                    flatAttackLimit += addRolls;
                    break;
                case Defence:
                    player.defenceUp += rollDefence;
                    defenceUpLimit += addRolls;
                    break;
                case FlatDefence:
                    player.flatDefence += rollFlatDefence;
                    flatDefenceLimit += addRolls;
                    break;
                case ElementalMastery:
                    player.elementalMastery += rollEM;
                    emLimit += addRolls;
                    break;
                case EnergyRecharge:
                    player.energyRecharge += rollER;
                    erLimit += addRolls;
                    break;
                case CritRate:
                    player.critRate += rollCR;
                    crLimit += addRolls;
                    break;
                case CritDamage:
                    player.critDamage += rollCD;
                    cdLimit += addRolls;
                    break;
                case None:
                    break;
                default:
                    break;
            }

            switch(set[i].sub3) {
                case Health:
                    player.hpUp += rollHP;
                    hpUpLimit += addRolls;
                    break;
                case FlatHealth:
                    player.flatHp += rollFlatHP;
                    flatHPLimit += addRolls;
                    break;
                case Attack:
                    player.attackUp += rollAttack;
                    attackUpLimit += addRolls;
                    break;
                case FlatAttack:
                    player.flatAttack += rollFlatAttack;
                    flatAttackLimit += addRolls;
                    break;
                case Defence:
                    player.defenceUp += rollDefence;
                    defenceUpLimit += addRolls;
                    break;
                case FlatDefence:
                    player.flatDefence += rollFlatDefence;
                    flatDefenceLimit += addRolls;
                    break;
                case ElementalMastery:
                    player.elementalMastery += rollEM;
                    emLimit += addRolls;
                    break;
                case EnergyRecharge:
                    player.energyRecharge += rollER;
                    erLimit += addRolls;
                    break;
                case CritRate:
                    player.critRate += rollCR;
                    crLimit += addRolls;
                    break;
                case CritDamage:
                    player.critDamage += rollCD;
                    cdLimit += addRolls;
                    break;
                case None:
                    break;
                default:
                    break;
            }

            switch(set[i].sub4) {
                case Health:
                    player.hpUp += rollHP;
                    hpUpLimit += addRolls;
                    break;
                case FlatHealth:
                    player.flatHp += rollFlatHP;
                    flatHPLimit += addRolls;
                    break;
                case Attack:
                    player.attackUp += rollAttack;
                    attackUpLimit += addRolls;
                    break;
                case FlatAttack:
                    player.flatAttack += rollFlatAttack;
                    flatAttackLimit += addRolls;
                    break;
                case Defence:
                    player.defenceUp += rollDefence;
                    defenceUpLimit += addRolls;
                    break;
                case FlatDefence:
                    player.flatDefence += rollFlatDefence;
                    flatDefenceLimit += addRolls;
                    break;
                case ElementalMastery:
                    player.elementalMastery += rollEM;
                    emLimit += addRolls;
                    break;
                case EnergyRecharge:
                    player.energyRecharge += rollER;
                    erLimit += addRolls;
                    break;
                case CritRate:
                    player.critRate += rollCR;
                    crLimit += addRolls;
                    break;
                case CritDamage:
                    player.critDamage += rollCD;
                    cdLimit += addRolls;
                    break;
                case None:
                    break;
                default:
                    break;
            }
        }

        // Find which substat roll gives the highest average damage then add it
        for (int i = 0; i < addRolls * set.length; i++) {
            player.hpUp += rollHP;
            double damage1 = calculator.AverageDamage(player);
            player.hpUp -= rollHP;

            player.flatHp += rollFlatHP;
            double damage2 = calculator.AverageDamage(player);
            player.flatHp -= rollFlatHP;

            player.attackUp += rollAttack;
            double damage3 = calculator.AverageDamage(player);
            player.attackUp -= rollAttack;

            player.flatAttack += rollFlatAttack;
            double damage4 = calculator.AverageDamage(player);
            player.flatAttack -= rollFlatAttack;

            player.defenceUp += rollDefence;
            double damage5 = calculator.AverageDamage(player);
            player.defenceUp -= rollDefence;

            player.flatDefence += rollFlatDefence;
            double damage6 = calculator.AverageDamage(player);
            player.flatDefence -= rollFlatDefence;

            player.critRate += rollCR;
            double damage7 = calculator.AverageDamage(player);
            player.critRate -= rollCR;

            player.critDamage += rollCD;
            double damage8 = calculator.AverageDamage(player);
            player.critDamage -= rollCD;

            player.elementalMastery += rollEM;
            double damage9 = calculator.AverageDamage(player);
            player.elementalMastery -= rollEM;

            player.energyRecharge += rollER;
            double damage10 = calculator.AverageDamage(player);
            player.energyRecharge -= rollER;

            // If substat has no rolls left, then damage for that roll is void
            if (hpUpLimit == 0) {
                damage1 = 0;
            }
            if (flatHPLimit == 0) {
                damage2 = 0;
            }
            if (attackUpLimit == 0) {
                damage3 = 0;
            }
            if (flatAttackLimit == 0) {
                damage4 = 0;
            }
            if (defenceUpLimit == 0) {
                damage5 = 0;
            }
            if (flatDefenceLimit == 0) {
                damage6 = 0;
            }
            if (crLimit == 0) {
                damage7 = 0;
            }
            if (cdLimit == 0) {
                damage8 = 0;
            }
            if (emLimit == 0) {
                damage9 = 0;
            }
            if (erLimit == 0) {
                damage10 = 0;
            }

            if (damage1 >= max(damage2,damage3,damage4,damage5,damage6,damage7,damage8,damage9,damage10)) {
                player.hpUp += rollHP;
                hpUpLimit -= 1;
            }
            else if (damage2 >= max(damage1,damage3,damage4,damage5,damage6,damage7,damage8,damage9,damage10)) {
                player.flatHp += rollFlatHP;
                flatHPLimit -= 1;
            }
            else if (damage3 >= max(damage1,damage2,damage4,damage5,damage6,damage7,damage8,damage9,damage10)) {
                player.attackUp += rollAttack;
                attackUpLimit -= 1;
            }
            else if (damage4 >= max(damage1,damage2,damage3,damage5,damage6,damage7,damage8,damage9,damage10)) {
                player.flatAttack += rollFlatAttack;
                flatAttackLimit -= 1;
            }
            else if (damage5 >= max(damage1,damage2,damage3,damage4,damage6,damage7,damage8,damage9,damage10)) {
                player.defenceUp += rollDefence;
                defenceUpLimit -= 1;
            }
            else if (damage6 >= max(damage1,damage2,damage3,damage4,damage5,damage7,damage8,damage9,damage10)) {
                player.flatDefence += rollFlatDefence;
                flatDefenceLimit -= 1;
            }
            else if (damage7 >= max(damage1,damage2,damage3,damage4,damage5,damage6,damage8,damage9,damage10)) {
                player.critRate += rollCR;
                crLimit -= 1;
            }
            else if (damage8 >= max(damage1,damage2,damage3,damage4,damage5,damage6,damage7,damage9,damage10)) {
                player.critDamage += rollCD;
                cdLimit -= 1;
            }
            else if (damage9 >= max(damage1,damage2,damage3,damage4,damage5,damage6,damage7,damage8,damage10)) {
                player.elementalMastery += rollEM;
                emLimit -= 1;
            }
            else if (damage10 >= max(damage1,damage2,damage3,damage4,damage5,damage6,damage7,damage8,damage9)) {
                player.energyRecharge += rollER;
                erLimit -= 1;
            }
            else {
                System.out.println("Error: Problem with checking next best substat roll");
            }
        }
        return calculator.AverageDamage(player);
    }

    //Prints player's stats and artifact main stats
    public void printStats(Artifact[] set) {
        double totalHP = (player.baseHp * ((player.hpUp/100)+1)) + player.flatHp;
        double totalAttack = (player.baseAttack * ((player.attackUp/100)+1)) + player.flatAttack;
        double totalDefence = (player.baseDefence * ((player.defenceUp/100)+1)) + player.flatDefence;
        System.out.println("HP: "+totalHP);
        System.out.println("Attack: "+totalAttack);
        System.out.println("Defence: "+totalDefence);
        System.out.println("Crit Rate: "+player.critRate);
        System.out.println("Crit Damage: "+player.critDamage);
        System.out.println("Damage Bonus: "+player.damageBonus);
        System.out.println("Elemental Mastery: "+player.elementalMastery);
        System.out.println("Energy Recharge: "+player.energyRecharge);
        System.out.println();
        System.out.println("Damage(on crit): "+calculator.CritDamage(player));
        System.out.println("Damage(no crit): "+calculator.Damage(player));
        System.out.println("Average Damage: "+calculator.AverageDamage(player));
        System.out.println();
        System.out.println("Sands: "+set[2].main.name());
        System.out.println("Goblet: "+set[3].main.name());
        System.out.println("Circlet: "+set[4].main.name());
    }

    // Combines other methods to show you optimal artifact main stats and stats
    public void Optimize() {
        Artifact[] set = SetSubStats(SetMainStats());
        MaxDamage(set);
        printStats(set);
    }

    // Simple method that returns the highest value from any number of parameters
    public double max(double... values) {
        double highestVal = 0;
        for (double val : values) {
            if (val > highestVal) {
                highestVal = val;
            }
        }
        return highestVal;
    }
}