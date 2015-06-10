package algorithm.hhnsgaIII;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import lowlevelheuristic.HeuristicFunctionType;
import lowlevelheuristic.LowLevelHeuristic;
import lowlevelheuristic.LowLevelHeuristicJM5;

import org.uma.jmetal.algorithm.impl.AbstractGeneticAlgorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.EnvironmentalSelectionNSGAIII;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.ReferencePoint;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.solutionattribute.Ranking;
import org.uma.jmetal.util.solutionattribute.impl.DominanceRanking;

import comparators.ComparatorFactory;

@SuppressWarnings({"rawtypes", "unchecked"})
public class HHNSGAIII extends AbstractGeneticAlgorithm<Solution, List<Solution>> {

    private static final long serialVersionUID = -4625697567676758569L;

    protected int evaluations;

    protected int maxEvaluations;

    protected int populationSize;

    protected Problem problem;

    protected SolutionListEvaluator<Solution> evaluator;

    private Vector<Integer> numberOfDivisions;

    private List<ReferencePoint> referencePoints = new Vector<>();

    protected HeuristicFunctionType heuristicFunction;

    private List<LowLevelHeuristic> lowLevelHeuristics;

    public static HHNSGAIIIBuilder Builder;

    /**
     * Constructor
     */
    public HHNSGAIII() {
    }

    /**
     * Constructor
     */
    HHNSGAIII(HHNSGAIIIBuilder builder) {

        // can be created from the BuilderNSGAIII within the same package
        problem = builder.problem;
        maxEvaluations = builder.maxEvaluations;

        selectionOperator = builder.selectionOperator;

        // Get type of heuristic function
        heuristicFunction = builder.heuristicFunction;
        lowLevelHeuristics = builder.lowLevelHeuristics;

        evaluator = builder.evaluator;

        // / NSGAIII
        numberOfDivisions = new Vector<>(1);
        numberOfDivisions.add(builder.getDivisions()); // Default value for 3D
        // problems

        ReferencePoint.generateReferencePoints(referencePoints, problem.getNumberOfObjectives(), numberOfDivisions);

        populationSize = referencePoints.size();
        while (populationSize % 4 > 0) {
            populationSize++;
        }
    }

    @Override
    protected void initProgress() {
        evaluations = populationSize;
    }

    @Override
    protected void updateProgress() {
        evaluations += populationSize;
    }

    @Override
    protected boolean isStoppingConditionReached() {
        return evaluations >= maxEvaluations;
    }

    @Override
    protected List<Solution> createInitialPopulation() {
        List<Solution> population = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            Solution newIndividual = problem.createSolution();
            population.add(newIndividual);
        }
        return population;
    }

    @Override
    protected List<Solution> evaluatePopulation(List<Solution> population) {
        population = evaluator.evaluate(population, problem);

        return population;
    }

    @Override
    protected List<Solution> selection(List<Solution> population) {
        List<Solution> matingPopulation = new ArrayList<>(population.size());
        for (int i = 0; i < populationSize; i++) {
            Solution solution = selectionOperator.execute(population);
            matingPopulation.add(solution);
        }

        return matingPopulation;
    }

    @Override
    protected List<Solution> reproduction(List<Solution> population) {
        // Define the heuristic function
        Comparator<LowLevelHeuristic> heuristicFunctionComparator = ComparatorFactory.createComparator(heuristicFunction);

        List<Solution> offspringPopulation = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i += 2) {
            List<Solution> parents = new ArrayList<>(2);
            parents.add(population.get(i));
            parents.add(population.get(i + 1));

            //Get the best hyperheuristics
            LowLevelHeuristicJM5 applyingHeuristic = (LowLevelHeuristicJM5) getApplyingHeuristic(heuristicFunctionComparator);

            Solution[] offSpring = null;

            try {
                offSpring = (Solution[]) applyingHeuristic.executeJM5(parents);
            } catch (JMException e) {
                e.printStackTrace();
            }
            
            //added to hyperheuristic works
            problem.evaluate(offSpring[0]);
            problem.evaluate(offSpring[1]);
            
            offspringPopulation.add(offSpring[0]);
            offspringPopulation.add(offSpring[1]);

            //Update rank
            applyingHeuristic.updateRank(parents, offSpring, heuristicFunction, getLowLevelHeuristics());

            //Update time elapsed from heuristics not executed
            applyingHeuristic.updateElapseTime(getLowLevelHeuristics(), applyingHeuristic);
        }
        return offspringPopulation;
    }

    private List<ReferencePoint> getReferencePointsCopy() {
        List<ReferencePoint> copy = new ArrayList<>();
        for (ReferencePoint r : this.referencePoints) {
            copy.add(new ReferencePoint(r));
        }
        return copy;
    }

    @Override
    protected List<Solution> replacement(List<Solution> population,
            List<Solution> offspringPopulation) {

        List<Solution> jointPopulation = new ArrayList<>();
        jointPopulation.addAll(population);
        jointPopulation.addAll(offspringPopulation);

        Ranking ranking = computeRanking(jointPopulation);

        // List<Solution> pop = crowdingDistanceSelection(ranking);
        List<Solution> pop = new ArrayList<>();
        List<List<Solution>> fronts = new ArrayList<>();
        int rankingIndex = 0;
        int candidateSolutions = 0;
        while (candidateSolutions < populationSize) {
            fronts.add(ranking.getSubfront(rankingIndex));
            candidateSolutions += ranking.getSubfront(rankingIndex).size();
            if ((pop.size() + ranking.getSubfront(rankingIndex).size()) <= populationSize) {
                addRankedSolutionsToPopulation(ranking, rankingIndex, pop);
            }
            rankingIndex++;
        }

		// A copy of the reference list should be used as parameter of the
        // environmental selection
        EnvironmentalSelectionNSGAIII selection = new EnvironmentalSelectionNSGAIII.Builder()
                .setNumberOfObjectives(problem.getNumberOfObjectives())
                .setFronts(fronts).setSolutionsToSelect(populationSize)
                .setReferencePoints(getReferencePointsCopy()).build();

        pop = selection.execute(pop);

        return pop;
    }

    @Override
    public List<Solution> getResult() {
        return getNonDominatedSolutions(getPopulation());
    }

    protected Ranking computeRanking(List<Solution> solutionList) {
        Ranking ranking = new DominanceRanking();
        ranking.computeRanking(solutionList);

        return ranking;
    }

    protected boolean populationIsNotFull(List<Solution> population) {
        return population.size() < populationSize;
    }

    protected boolean subfrontFillsIntoThePopulation(Ranking ranking, int rank,
            List<Solution> population) {
        return ranking.getSubfront(rank).size() < (populationSize - population
                .size());
    }

    protected void addRankedSolutionsToPopulation(Ranking ranking, int rank,
            List<Solution> population) {
        List<Solution> front;

        front = ranking.getSubfront(rank);

        for (int i = 0; i < front.size(); i++) {
            population.add(front.get(i));
        }
    }

    protected List<Solution> getNonDominatedSolutions(
            List<Solution> solutionList) {
        return SolutionListUtils.getNondominatedSolutions(solutionList);
    }

    public LowLevelHeuristic addLowLevelHeuristic(HashMap<String, Object> parameters) {
        LowLevelHeuristicJM5 lowLevelHeuristic = new LowLevelHeuristicJM5(parameters);
        if (!lowLevelHeuristics.contains(lowLevelHeuristic)) {
            lowLevelHeuristics.add(lowLevelHeuristic);
            return lowLevelHeuristic;
        } else {
            return null;
        }
    }

    public void clearLowLeverHeuristicsValues() {
        LowLevelHeuristic.clearAllStaticValues();
        for (LowLevelHeuristic lowLevelHeuristic : lowLevelHeuristics) {
            lowLevelHeuristic.clearAllValues();
        }
    }

    public List<LowLevelHeuristic> getLowLevelHeuristics() {
        return this.lowLevelHeuristics;
    }

    public int[] getLowLevelHeuristicsNumberOfTimesApplied() {
        int[] allTimesApplied = new int[lowLevelHeuristics.size()];
        for (int i = 0; i < lowLevelHeuristics.size(); i++) {
            LowLevelHeuristic lowLevelHeuristic = lowLevelHeuristics.get(i);
            allTimesApplied[i] = lowLevelHeuristic.getNumberOfTimesApplied();
        }
        return allTimesApplied;
    }

    public int getLowLevelHeuristicsSize() {
        return lowLevelHeuristics.size();
    }

    public void setLowLevelHeuristic(List<LowLevelHeuristic> lowLevelHeuristics) {
        this.lowLevelHeuristics = lowLevelHeuristics;
    }

    public LowLevelHeuristic getApplyingHeuristic(Comparator<LowLevelHeuristic> comparator) {
        if (heuristicFunction.name().equals(HeuristicFunctionType.Random.name()) || comparator == null) {
            return lowLevelHeuristics.get(PseudoRandom.randInt(0, lowLevelHeuristics.size() - 1));
        } else {
            List<LowLevelHeuristic> allLowLevelHeuristics = new ArrayList<>(lowLevelHeuristics);
            Collections.sort(allLowLevelHeuristics, comparator);
            List<LowLevelHeuristic> applyingHeuristics = new ArrayList<>();

            //Find the best tied heuristics
            Iterator<LowLevelHeuristic> iterator = allLowLevelHeuristics.iterator();
            LowLevelHeuristic heuristic;
            LowLevelHeuristic nextHeuristic = iterator.next();
            do {
                heuristic = nextHeuristic;
                applyingHeuristics.add(heuristic);
            } while (iterator.hasNext() && comparator.compare(heuristic, nextHeuristic = iterator.next()) == 0);

            return applyingHeuristics.get(PseudoRandom.randInt(0, applyingHeuristics.size() - 1));
        }
    }
}
