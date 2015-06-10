package algorithm.hhspea2;

import algorithm.hhnsgaII.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.FileHandler;

import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.operators.selection.BinaryTournament;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.comparators.FitnessComparator;
import lowlevelheuristic.HeuristicFunctionType;
import problem.MTProblem;
import util.HyperHeuristicUtil;
import algorithm.HyperHeuristic;

public class HHSPEA2_main {

    protected static String[] crossovers = {
        "SinglePointCrossover",
        "UniformCrossoverBinary",
        "TwoPointsCrossoverBinary"
    };

    protected static String[] mutations = {
        "BitFlipMutation",
        "SwapMutationBinary",
        null
    };

    protected static String directory = "results/hhspea2/";

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void main(String[] args) throws JMException, IOException, ClassNotFoundException {
        // Logger object and file to store log messages
        Configuration.logger_.addHandler(new FileHandler(directory + "hhspea2.log"));

        // Problem
        Problem problem = new MTProblem("instances/find.txt");

        System.out.println("Problema: " + problem.getName());

        // Algorithm
        HyperHeuristic hh = new HHSPEA2(problem);

        System.out.println("Algoritmo: " + hh.getClass());

        /* Selection Operator */
        HashMap parameters = new HashMap();
        parameters.put("comparator", new FitnessComparator());

        // Algorithm parameters
        hh.setInputParameter("populationSize", 100);
        hh.setInputParameter("archiveSize", 100);
        hh.setInputParameter("maxEvaluations", 200000);
        hh.setInputParameter("heuristicFunction", HeuristicFunctionType.ChoiceFunction);
        // Add the operators to the algorithm
        hh.addOperator("selection", new BinaryTournament(parameters));
        hh.setLowLevelHeuristic(HyperHeuristicUtil.getLowLevelHeuristics(crossovers, mutations));

        // Execute the Algorithm
        long initTime = System.currentTimeMillis();
        SolutionSet population = hh.execute();
        long estimatedTime = System.currentTimeMillis() - initTime;

        // Print the results
        Configuration.logger_.info("Total execution time: " + estimatedTime + "ms");

        Configuration.logger_.info("Variables values have been writen to file VAR");
        population.printVariablesToFile(directory + "VAR");

        Configuration.logger_.info("Objectives values have been writen to file FUN");
        population.printObjectivesToFile(directory + "FUN");
    }
}
