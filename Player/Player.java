package Player;
public class Player {
    public double baseHp = 0;
    public double hpUp = 0;
    public double flatHp = 0;

    public double baseAttack = 0;
    public double attackUp = 0;
    public double flatAttack = 0;

    public double baseDefence = 0;
    public double defenceUp = 0;
    public double flatDefence = 0;

    public double critRate = 5;
    public double critDamage = 50;

    public double damageBonus =  0;
    public double elementalMastery = 0;
    public double energyRecharge = 0;
    public double healingBonus = 0;

    public double skillMultiplier = 0;
    public double dualMultiplier = 0;

    // Extra
    public double characterLevel = 90;
    public double enemyLevel = 90;

    public double enemyResistance = 10;
    public double resistanceShred = 0;

    public double defenceShred = 0;
    public double defenceIgnore = 0;

    // Pyro hitting Cryo / Hydro hitting Pyro = 2, Cryo hitting Pyro / Pyro hitting Hydro = 1.5
    public double reactionDamage = 0;

    public scaling damageScaling = scaling.Defence;
    public boolean elementalDamage = true;

    public enum scaling {
        Attack, Defence, Hp
    }
}
