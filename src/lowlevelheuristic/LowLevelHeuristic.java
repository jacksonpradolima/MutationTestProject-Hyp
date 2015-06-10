/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lowlevelheuristic;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import jmetal.core.Solution;
import jmetal.operators.crossover.Crossover;
import jmetal.operators.mutation.Mutation;
import jmetal.util.JMException;
import jmetal.util.comparators.DominanceComparator;

/**
 *
 * @author Prado Lima
 */
public class LowLevelHeuristic {

    /**
     * Rank weight.
     */
    private double alpha = 1;

    /**
     * Elapsed time weight.
     */
    private double beta = 0.00005;

    /**
     * Sliding window size.
     */
    private static int W = 0;

    private static double c = 0;

    /**
     * Sliding window.
     */
    private static LowLevelHeuristic SLIDING_WINDOW_HEURISTIC[];

    /**
     * Sliding window improvement.
     */
    private static double SLIDING_WINDOW_IMPROVEMENT[];

    /**
     * Sliding window index.
     */
    private static int I = 0;

    /**
     * Total number of low level heuristics executions.
     */
    private static int IT = 0;
    private static int REBOOTS = 0;
    private static double SUM_N = 1;

    //Empirical Rewards.
    private double q = 1.0;
    private double r = 1.0;
    private double aux = 1.0;

    //Usage
    private final Comparator dominanceComparator;

    //Attributes
    private String name;
    private double rank;
    private double elapsedTime;
    private int numberOfTimesApplied;
    private int lastTimeApplied;
    private double n;

    //Aggregation
    private Crossover crossoverOperator;
    private Mutation mutationOperator;

    public LowLevelHeuristic(HashMap<String, Object> parameters) {
        this.dominanceComparator = new DominanceComparator();

        this.rank = 1;
        this.elapsedTime = 0;
        this.numberOfTimesApplied = 0;
        this.lastTimeApplied = 0;
        this.n = 1;
        this.q = 1;

        if (parameters.containsKey("name")) {
            this.name = (String) parameters.get("name");
        }

        if (parameters.containsKey("crossover")) {
            this.crossoverOperator = (Crossover) parameters.get("crossover");
            this.crossoverOperator.setParameter("probability", (double) 1);
        }

        if (parameters.containsKey("mutation")) {
            this.mutationOperator = (Mutation) parameters.get("mutation");
            if (this.mutationOperator != null) {
                this.mutationOperator.setParameter("probability", (double) 1);
            }
        }

        if (parameters.containsKey("alpha")) {
            alpha = (double) parameters.get("alpha");
        }

        if (parameters.containsKey("beta")) {
            beta = (double) parameters.get("beta");
        }

        if (parameters.containsKey("w")) {
            W = (int) parameters.get("w");
            if (SLIDING_WINDOW_HEURISTIC == null) {
                SLIDING_WINDOW_HEURISTIC = new LowLevelHeuristic[W];
                SLIDING_WINDOW_IMPROVEMENT = new double[W];
            }
        }

        if (parameters.containsKey("c")) {
            c = (double) parameters.get("c");
        }
    }

    /*
     Methods for updating values.
     */
    private void executed() {
        updateElapsedTime(true);
        this.numberOfTimesApplied++;
        IT++;
    }

    private void notExecuted() {
        updateElapsedTime(false);
    }

    private void updateElapsedTime(boolean executed) {
        if (executed) {
            this.elapsedTime = 0;
        } else {
            this.elapsedTime += 1D;
        }
    }

    public void updateRank(Solution[] parents, Solution[] offsprings, HeuristicFunctionType heuristicFunction, List<LowLevelHeuristic> lowLevelHeuristics) {
        rank = 0;
        for (Solution parent : parents) {
            for (Solution offspring : offsprings) {
                rank += ((double) dominanceComparator.compare(parent, offspring) + (double) 1) / (double) 2;
            }
        }
        rank /= ((double) parents.length * (double) offsprings.length);

        if (HeuristicFunctionType.MultiArmedBandit == heuristicFunction) {
            creditAssignment(lowLevelHeuristics);
        }
    }

    public void updateElapseTime(List<LowLevelHeuristic> lowLevelHeuristics, LowLevelHeuristic applyingHeuristic) {
        for (LowLevelHeuristic lowLevelHeuristic : lowLevelHeuristics) {
            if (!lowLevelHeuristic.equals(applyingHeuristic)) {
                lowLevelHeuristic.notExecuted();
            }
        }
    }

    public double getChoiceFunctionValue() {
        return (alpha * rank) + (beta * elapsedTime);
    }

    public double getMultiArmedBanditValue() {
        double a = Math.log(SUM_N);
        double b = Math.sqrt(2.0 * a);
        aux = b / n;
        return (double) q + c * aux;
    }

    public static void clearAllStaticValues() {
        reinitializeStatic();
        IT = 0;
        REBOOTS = 0;
    }

    public void clearAllValues() {
        reinitialize();
    }

    /*
     Override standard methods.
     */
    @Override
    public String toString() {
        return name + " - R: " + rank + ", T: " + elapsedTime + ", CH: " + getChoiceFunctionValue() + ", NA: " + numberOfTimesApplied;
    }

    public Object execute(Object object) throws JMException {
        Solution[] parents = (Solution[]) object;

        Solution[] offSpring = (Solution[]) crossoverOperator.execute(parents);
        if (mutationOperator != null) {
            for (Solution offSpringSolution : offSpring) {
                mutationOperator.execute(offSpringSolution);
            }
        }

        executed();

        return offSpring;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LowLevelHeuristic other = (LowLevelHeuristic) obj;
        return Objects.equals(this.name, other.name);
    }

    private void updateReward() {
        double max = .0;
        int j = 0;
        LowLevelHeuristic h = SLIDING_WINDOW_HEURISTIC[j];
        double im = SLIDING_WINDOW_IMPROVEMENT[j];
        for (; j < W && h != null; j++) {
            if (h.equals(this) && im > max) {
                max = im;
            }
            h = SLIDING_WINDOW_HEURISTIC[j];
            im = SLIDING_WINDOW_IMPROVEMENT[j];
        }
        r = max;
    }

    private void updateQ() {
        double e = (double) elapsedTime;
        double w = (double) W;
        q = q * (w / (w + e)) + r * (1.0 / (n + 1.0));
    }

    private void updateN() {
        double e = (double) elapsedTime;
        double w = (double) W;
        n = n * ((w / (w + e)) + (1.0 / (n + 1.0)));
    }

    protected void creditAssignment(List<LowLevelHeuristic> heuristics) {
        if (W != 0) {
            LowLevelHeuristic temp = SLIDING_WINDOW_HEURISTIC[I];
            SLIDING_WINDOW_HEURISTIC[I] = this;
            SLIDING_WINDOW_IMPROVEMENT[I] = this.rank;
            if (temp != null) {
                temp.updateReward();
            }
            this.updateReward();
            I++;
            I %= W;

            SUM_N = 0;
            for (LowLevelHeuristic heuristic : heuristics) {
                heuristic.updateQ();
                heuristic.updateN();
                SUM_N += heuristic.n;
            }
        }
    }

    private void reinitialize() {
        this.rank = 1;
        this.elapsedTime = 0;
        this.numberOfTimesApplied = 0;
        this.q = 1;
        this.r = 1;
        this.n = 1;
    }

    private static void reinitializeStatic() {
        SLIDING_WINDOW_HEURISTIC = new LowLevelHeuristic[W];
        SLIDING_WINDOW_IMPROVEMENT = new double[W];
        I = 0;
        IT = 0;
        REBOOTS++;
    }

    //<editor-fold defaultstate="collapsed" desc="Getters/Setters">
    /*
     Getters and setters.
     */
    public double getQ() {
        return q;
    }

    public double getAux() {
        return aux;
    }

    public static int getREBOOTS() {
        return REBOOTS;
    }

    public double getRank() {
        return rank;
    }

    public double getR() {
        return r;
    }

    public double getN() {
        return n;
    }

    public double getElapsedTime() {
        return elapsedTime;
    }

    public int getNumberOfTimesApplied() {
        return numberOfTimesApplied;
    }

    //used in the final results
    public void setNumberOfTimesApplied(int numberOfTimesApplied) {
        this.numberOfTimesApplied += numberOfTimesApplied;
    }

    public Crossover getCrossoverOperator() {
        return crossoverOperator;
    }

    public Mutation getMutationOperator() {
        return mutationOperator;
    }

    public String getName() {
        return name;
    }
     //</editor-fold>
}
