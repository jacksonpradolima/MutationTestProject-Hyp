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

import jmetal.util.JMException;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.solution.Solution;
import comparators.DominanceComparatorJM5;

/**
 *
 * @author Prado Lima
 */
public class LowLevelHeuristicJM5 extends LowLevelHeuristic {

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
    private static LowLevelHeuristicJM5 SLIDING_WINDOW_HEURISTIC[];

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
    private CrossoverOperator crossoverOperator;
    private MutationOperator mutationOperator;

    public LowLevelHeuristicJM5(HashMap<String, Object> parameters) {
        super(new HashMap());
        this.dominanceComparator = new DominanceComparatorJM5();

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
            this.crossoverOperator = (CrossoverOperator) parameters.get("crossover");
            // this.crossoverOperator.setParameter("probability", (double) 1);
        }

        if (parameters.containsKey("mutation")) {
            this.mutationOperator = (MutationOperator) parameters.get("mutation");
            if (this.mutationOperator != null) {
                //   this.mutationOperator.setParameter("probability", (double) 1);
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
                SLIDING_WINDOW_HEURISTIC = new LowLevelHeuristicJM5[W];
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
            this.elapsedTime += 1;
        }
    }

    public void updateRank(List<Solution> parents, Solution[] offsprings, HeuristicFunctionType heuristicFunction, List<LowLevelHeuristic> lowLevelHeuristics) {
            rank = 0;
            for (Solution parent : parents) {
                for (Solution offspring : offsprings) {
                    rank += ((double) dominanceComparator.compare(parent, offspring) + (double) 1) / (double) 2;
                }
            }
            rank /= ((double) parents.size() * (double) offsprings.length);

            
            if (HeuristicFunctionType.MultiArmedBandit == heuristicFunction) {
                creditAssignment(lowLevelHeuristics);
            }
        }

    public void updateElapseTime(List<LowLevelHeuristicJM5> lowLevelHeuristics, LowLevelHeuristicJM5 applyingHeuristic) {
        for (LowLevelHeuristicJM5 lowLevelHeuristic : lowLevelHeuristics) {
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

    public Object executeJM5(Object parents) throws JMException {

        List<Solution> offspring = (List<Solution>) crossoverOperator.execute(parents);

        if (mutationOperator != null) {
            for (Solution offSpringSolution : offspring) {
                mutationOperator.execute(offSpringSolution);
            }
        }

        executed();

        Solution[] s = new Solution[2];
        s[0] = offspring.get(0);
        s[1] = offspring.get(1);
        

        return s;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LowLevelHeuristicJM5 other = (LowLevelHeuristicJM5) obj;
        return Objects.equals(this.name, other.name);
    }

    private void updateReward() {
        double max = .0;
        int j = 0;
        LowLevelHeuristicJM5 h = SLIDING_WINDOW_HEURISTIC[j];
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

    private void reinitialize() {
        this.rank = 1;
        this.elapsedTime = 0;
        this.numberOfTimesApplied = 0;
        this.q = 1;
        this.r = 1;
        this.n = 1;
    }

    private static void reinitializeStatic() {
        SLIDING_WINDOW_HEURISTIC = new LowLevelHeuristicJM5[W];
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

    public String getName() {
        return name;
    }
     //</editor-fold>
}
