package OptimalDistributionCalculator;

import Player.*;
import DamageCalculators.*;

public class Main {
    public static void main(String[] args) {
        Player player = new Player();
        DamageCalculator calc = new DamageCalculator();
        //calc.printStats(player);

        Optimizer optimizer = new Optimizer(new DamageCalculator());
        optimizer.Optimize();
    }
}
