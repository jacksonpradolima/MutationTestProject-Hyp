package algorithm.hhnsgaIII;

import java.util.List;

import lowlevelheuristic.HeuristicFunctionType;
import lowlevelheuristic.LowLevelHeuristicJM5;

import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

/**
 * Builder class
 */
@SuppressWarnings("rawtypes")
public class HHNSGAIIIBuilder implements AlgorithmBuilder {

    // no access modifier means access from classes within the same package
    Problem problem;
    int maxEvaluations;
    int populationSize;
    SelectionOperator selectionOperator;
    SolutionListEvaluator evaluator;
    int divisions;
    double beta;

    List<LowLevelHeuristicJM5> lowLevelHeuristics;

    HeuristicFunctionType heuristicFunction;

    /**
     * Builder constructor
     */
    public HHNSGAIIIBuilder(Problem problem) {
        this.problem = problem;
        maxEvaluations = 25000;
        populationSize = 100;
        evaluator = new SequentialSolutionListEvaluator();
    }

    public HHNSGAIIIBuilder setMaxEvaluations(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations;

        return this;
    }

    public HHNSGAIIIBuilder setPopulationSize(int populationSize) {
        this.populationSize = populationSize;

        return this;
    }

    public HHNSGAIIIBuilder setSelectionOperator(SelectionOperator selectionOperator) {
        this.selectionOperator = selectionOperator;
        return this;
    }

    public HHNSGAIIIBuilder setSolutionListEvaluator(SolutionListEvaluator evaluator) {
        this.evaluator = evaluator;
        return this;
    }

    public HHNSGAIIIBuilder setDivisions(int div) {
        this.divisions = div;
        return this;
    }

    public int getDivisions() {
        return this.divisions;
    }

    public HHNSGAIII build() {
        return new HHNSGAIII(this);
    }

    public HHNSGAIIIBuilder setLowLevelHeuristic(List<LowLevelHeuristicJM5> lowLevelHeuristics) {
        this.lowLevelHeuristics = lowLevelHeuristics;
        return this;
    }

    public HHNSGAIIIBuilder setHeuristicFunction(HeuristicFunctionType heuristicFunction) {
        this.heuristicFunction = heuristicFunction;
        return this;
    }

    public double getBeta() {
        return this.beta;
    }

    public HHNSGAIIIBuilder setBeta(double beta) {
        this.beta = beta;
        return this;
    }

}
