package Player;
public class Artifact {
    public enum mainstat {
        Health, Attack, Defence, FlatHealth, FlatAttack, EnergyRecharge,
        ElementalMastery, CritRate, CritDamage, HealingBonus,
        EleDmgBonus, PhysDmgBonus, None
    }

    public enum substat {
        Health, Attack, Defence, FlatHealth, FlatAttack, FlatDefence,
        EnergyRecharge, ElementalMastery, CritRate, CritDamage, None
    }

    public mainstat main;
    public substat sub1;
    public substat sub2;
    public substat sub3;
    public substat sub4;

    public Artifact() {
        main = mainstat.None;
        sub1 = substat.None;
        sub2 = substat.None;
        sub3 = substat.None;
        sub4 = substat.None;
    }

    public Artifact(mainstat main, substat sub1, substat sub2, substat sub3, substat sub4) {
        this.main = main;
        this.sub1 = sub1;
        this.sub2 = sub2;
        this.sub3 = sub3;
        this.sub4 = sub4;
    }
}
