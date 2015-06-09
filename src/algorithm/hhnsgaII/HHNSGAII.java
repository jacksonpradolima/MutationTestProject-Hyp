//  hhNSGAII.java
//
//  Author:
//       Jackson Antonio do Prado Lima <japlima@ufpr.br>
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.
package algorithm.hhnsgaII;

import algorithm.HyperHeuristic;
import comparators.ComparatorFactory;
import java.util.Comparator;
import jmetal.core.*;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Distance;
import jmetal.util.JMException;
import jmetal.util.Ranking;
import jmetal.util.comparators.CrowdingComparator;
import lowlevelheuristic.LowLevelHeuristic;
import lowlevelheuristic.HeuristicFunctionType;

/**
 *
 * @author: Prado Lima
 */
public class HHNSGAII extends HyperHeuristic {

    /**
     * Constructor
     *
     * @param problem Problem to solve
     */
    public HHNSGAII(Problem problem) {
        super(problem);
    } // NSGAII

    /**
     * Runs the NSGA-II algorithm.
     *
     * @return a <code>SolutionSet</code> that is a set of non dominated
     * solutions as a result of the algorithm execution
     * @throws JMException
     */
    public SolutionSet execute() throws JMException, ClassNotFoundException {
        int populationSize;
        int maxEvaluations;
        int evaluations;

        QualityIndicator indicators; // QualityIndicator object
        int requiredEvaluations; // Use in the example of use of the
        // indicators object (see below)

        SolutionSet population;
        SolutionSet offspringPopulation;
        SolutionSet union;

        Operator selectionOperator;

        Distance distance = new Distance();

        //Read the parameters
        populationSize = (Integer) getInputParameter("populationSize");
        maxEvaluations = (Integer) getInputParameter("maxEvaluations");
        indicators = (QualityIndicator) getInputParameter("indicators");

        // Get type of heuristic function
        HeuristicFunctionType heuristicFunction = (HeuristicFunctionType) getInputParameter("heuristicFunction");

        // Define the heuristic function
        Comparator<LowLevelHeuristic> heuristicFunctionComparator = ComparatorFactory.createComparator(heuristicFunction);

        //Initialize the variables
        population = new SolutionSet(populationSize);
        evaluations = 0;

        requiredEvaluations = 0;

        //Read the operators
        selectionOperator = operators_.get("selection");

        // Create the initial solutionSet
        Solution newSolution;
        for (int i = 0; i < populationSize; i++) {
            newSolution = new Solution(problem_);
            problem_.evaluate(newSolution);
            problem_.evaluateConstraints(newSolution);
            evaluations++;
            population.add(newSolution);
        } //for       

        // I don't know where to use
        int generation = 0;

        // Generations 
        while (evaluations < maxEvaluations) {
            // We can use for something :D
            generation++;

            // Create the offSpring solutionSet      
            offspringPopulation = new SolutionSet(populationSize);
            Solution[] parents = new Solution[2];
            for (int i = 0; i < (populationSize / 2); i++) {
                //Get the best hyperheuristics
                LowLevelHeuristic applyingHeuristic = getApplyingHeuristic(heuristicFunctionComparator);

                //obtain parents
                parents[0] = (Solution) selectionOperator.execute(population);
                parents[1] = (Solution) selectionOperator.execute(population);

                Solution[] offSpring = (Solution[]) applyingHeuristic.execute(parents);

                problem_.evaluate(offSpring[0]);
                problem_.evaluateConstraints(offSpring[0]);
                problem_.evaluate(offSpring[1]);
                problem_.evaluateConstraints(offSpring[1]);

                offspringPopulation.add(offSpring[0]);
                offspringPopulation.add(offSpring[1]);

                evaluations += 2;

                //Update rank
                applyingHeuristic.updateRank(parents, offSpring, heuristicFunction, getLowLevelHeuristics());

                //Update time elapsed from heuristics not executed
                applyingHeuristic.updateElapseTime(getLowLevelHeuristics(), applyingHeuristic);
            } // for

            // Create the solutionSet union of solutionSet and offSpring
            union = ((SolutionSet) population).union(offspringPopulation);

            // Ranking the union
            Ranking ranking = new Ranking(union);

            int remain = populationSize;
            int index = 0;
            SolutionSet front = null;
            population.clear();

            // Obtain the next front
            front = ranking.getSubfront(index);

            while ((remain > 0) && (remain >= front.size())) {
                //Assign crowding distance to individuals
                distance.crowdingDistanceAssignment(front, problem_.getNumberOfObjectives());
                //Add the individuals of this front
                for (int k = 0; k < front.size(); k++) {
                    population.add(front.get(k));
                } // for

                //Decrement remain
                remain = remain - front.size();

                //Obtain the next front
                index++;
                if (remain > 0) {
                    front = ranking.getSubfront(index);
                } // if        
            } // while

            // Remain is less than front(index).size, insert only the best one
            if (remain > 0) {  // front contains individuals to insert                        
                distance.crowdingDistanceAssignment(front, problem_.getNumberOfObjectives());
                front.sort(new CrowdingComparator());
                for (int k = 0; k < remain; k++) {
                    population.add(front.get(k));
                } // for

                remain = 0;
            } // if                               

            // This piece of code shows how to use the indicator object into the code
            // of NSGA-II. In particular, it finds the number of evaluations required
            // by the algorithm to obtain a Pareto front with a hypervolume higher
            // than the hypervolume of the true Pareto front.
            if ((indicators != null)
                    && (requiredEvaluations == 0)) {
                double HV = indicators.getHypervolume(population);
                if (HV >= (0.98 * indicators.getTrueParetoFrontHypervolume())) {
                    requiredEvaluations = evaluations;
                } // if
            } // if
        } // while

        // Return as output parameter the required evaluations
        setOutputParameter("evaluations", requiredEvaluations);

        // Return the first non-dominated front
        Ranking ranking = new Ranking(population);
        ranking.getSubfront(0).printFeasibleFUN("FUN_NSGAII");

        return ranking.getSubfront(0);
    } // execute
} // NSGA-II
