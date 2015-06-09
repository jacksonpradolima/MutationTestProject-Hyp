/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm.hhspea2;

import algorithm.HyperHeuristic;
import comparators.ComparatorFactory;
import java.util.Comparator;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.util.JMException;
import jmetal.util.Ranking;
import jmetal.util.Spea2Fitness;
import lowlevelheuristic.HeuristicFunctionType;
import lowlevelheuristic.LowLevelHeuristic;

/**
 *
 * @author Prado Lima
 */
/**
 * This class representing the SPEA2 algorithm using hyper heuristic
 */
public class HHSPEA2 extends HyperHeuristic {

    /**
     * Defines the number of tournaments for creating the mating pool
     */
    public static final int TOURNAMENTS_ROUNDS = 1;

    /**
     * Constructor. Create a new SPEA2 instance
     *
     * @param problem Problem to solve
     */
    public HHSPEA2(Problem problem) {
        super(problem);
    } // Spea2

    /**
     * Runs of the Spea2 algorithm.
     *
     * @return a <code>SolutionSet</code> that is a set of non dominated
     * solutions as a result of the algorithm execution
     * @throws JMException
     */
    public SolutionSet execute() throws JMException, ClassNotFoundException {
        int populationSize, archiveSize, maxEvaluations, evaluations;
        Operator selectionOperator;
        SolutionSet solutionSet, archive, offSpringSolutionSet;

        //Read the params
        populationSize = ((Integer) getInputParameter("populationSize")).intValue();
        archiveSize = ((Integer) getInputParameter("archiveSize")).intValue();
        maxEvaluations = ((Integer) getInputParameter("maxEvaluations")).intValue();

        //Read the operators
        selectionOperator = operators_.get("selection");

        // Get type of heuristic function
        HeuristicFunctionType heuristicFunction = (HeuristicFunctionType) getInputParameter("heuristicFunction");

        // Define the heuristic function
        Comparator<LowLevelHeuristic> heuristicFunctionComparator = ComparatorFactory.createComparator(heuristicFunction);

        //Initialize the variables
        solutionSet = new SolutionSet(populationSize);
        archive = new SolutionSet(archiveSize);
        evaluations = 0;

        //-> Create the initial solutionSet
        Solution newSolution;
        for (int i = 0; i < populationSize; i++) {
            newSolution = new Solution(problem_);
            problem_.evaluate(newSolution);
            problem_.evaluateConstraints(newSolution);
            evaluations++;
            solutionSet.add(newSolution);
        }

        while (evaluations < maxEvaluations) {
            SolutionSet union = ((SolutionSet) solutionSet).union(archive);
            Spea2Fitness spea = new Spea2Fitness(union);
            spea.fitnessAssign();
            archive = spea.environmentalSelection(archiveSize);

            // Create a new offspringPopulation
            offSpringSolutionSet = new SolutionSet(populationSize);
            Solution[] parents = new Solution[2];
            while (offSpringSolutionSet.size() < populationSize) {
                //Get the best hyperheuristics
                LowLevelHeuristic applyingHeuristic = getApplyingHeuristic(heuristicFunctionComparator);

                //obtain parents
                int j = 0;
                do {
                    j++;
                    parents[0] = (Solution) selectionOperator.execute(archive);
                } while (j < HHSPEA2.TOURNAMENTS_ROUNDS); // do-while                    

                int k = 0;
                do {
                    k++;
                    parents[1] = (Solution) selectionOperator.execute(archive);
                } while (k < HHSPEA2.TOURNAMENTS_ROUNDS); // do-while

                //make the crossover 
                Solution[] offSpring = (Solution[]) applyingHeuristic.execute(parents);

                problem_.evaluate(offSpring[0]);
                problem_.evaluateConstraints(offSpring[0]);
                offSpringSolutionSet.add(offSpring[0]);
                evaluations++;

                //Update rank
                applyingHeuristic.updateRank(parents, offSpring, heuristicFunction, getLowLevelHeuristics());

                //Update time elapsed from heuristics not executed
                applyingHeuristic.updateElapseTime(getLowLevelHeuristics(), applyingHeuristic);
            } // while
            // End Create a offSpring solutionSet
            solutionSet = offSpringSolutionSet;
        } // while

        Ranking ranking = new Ranking(archive);
        return ranking.getSubfront(0);
    } // execute    
} // SPEA2
