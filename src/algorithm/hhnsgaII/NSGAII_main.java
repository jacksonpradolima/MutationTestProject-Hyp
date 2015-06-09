//  IBEA_main.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
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

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.FileHandler;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.metaheuristics.ibea.IBEA;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.BinaryTournament;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.comparators.FitnessComparator;
import problem.MTProblem;

/**
 * Class for configuring and running the DENSEA algorithm
 */
public class NSGAII_main {

    protected static String directory = "results/nsgaII";
    
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void main(String[] args) throws JMException, IOException, ClassNotFoundException {
        // Logger object and file to store log messages
        Configuration.logger_.addHandler(new FileHandler(String.format("%s/%s", directory, "nsgaII.log")));

        Problem problem = new MTProblem("instances/guizzo_cas.txt");

        Algorithm algorithm = new IBEA(problem);

        // Algorithm parameters
        algorithm.setInputParameter("populationSize", 100);
        algorithm.setInputParameter("archiveSize", 100);
        algorithm.setInputParameter("maxEvaluations", 200000);

        // Mutation and Crossover for Real codification
        HashMap parameters = new HashMap();
        parameters.put("probability", 0.9);
        Operator crossover = CrossoverFactory.getCrossoverOperator("SinglePointCrossover", parameters);

        parameters = new HashMap();
        parameters.put("probability", 0.01);
        Operator mutation = MutationFactory.getMutationOperator("BitFlipMutation", parameters);

        /* Selection Operator */
        parameters = new HashMap();
        parameters.put("comparator", new FitnessComparator());
        Operator selection = new BinaryTournament(parameters);

        // Add the operators to the algorithm
        algorithm.addOperator("crossover", crossover);
        algorithm.addOperator("mutation", mutation);
        algorithm.addOperator("selection", selection);

        // Execute the Algorithm
        long initTime = System.currentTimeMillis();
        SolutionSet population = algorithm.execute();
        long estimatedTime = System.currentTimeMillis() - initTime;

        // Print the results
        Configuration.logger_.info("Total execution time: " + estimatedTime + "ms");

        Configuration.logger_.info("Variables values have been writen to file VAR");
        population.printVariablesToFile(String.format("%s/%s", directory, "VAR"));

        Configuration.logger_.info("Objectives values have been writen to file FUN");
        population.printObjectivesToFile(String.format("%s/%s", directory, "FUN"));
    }
}
