package main;

import algorithm.hhNSGAII;
import experiment.Parameters;
import java.util.HashMap;
import java.util.Iterator;
import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.util.JMException;
import jmetal.util.NonDominatedSolutionList;
import problem.MTProblem;
import util.ExperimentUtil;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Main class
 *
 * @author thiagodnf
 */
//experimental class for jmetal 4.5
public class MTTest45 {

    public static void main(String[] args) throws JMException, ClassNotFoundException {

        Parameters mutationParameters = ExperimentUtil.verifyParameters(args);

        //print parameters
        mutationParameters.PrintParameters();

        //select problem
        Problem problem = new MTProblem(mutationParameters.getInstance());

        Operator crossover;         // Crossover operator
        Operator mutation;         // Mutation operator
        Operator selection;         // Selection operator

        // Selection Operator 
        selection = SelectionFactory.getSelectionOperator(mutationParameters.getSelectionOperator(), null);

        //select algorithm
        hhNSGAII algorithm = new hhNSGAII(problem);

        // Algorithm params
        algorithm.setInputParameter("populationSize", mutationParameters.getPopulationSize());
        algorithm.setInputParameter("maxEvaluations", mutationParameters.getPopulationSize() * mutationParameters.getGenerations());

        //Parameter for SPEA and IBEA
        algorithm.setInputParameter("archiveSize", mutationParameters.getPopulationSize());

        /* Add the operators to the algorithm*/
        algorithm.addOperator("selection", selection);

        //Create low level heuristics
        int lowLevelHeuristicNumber = 1;
        String[] lowLevelHeuristicNames = new String[mutationParameters.getCrossoverOperator().length * mutationParameters.getMutationOperator().length + mutationParameters.getCrossoverOperator().length];
        for (String crossoverName : mutationParameters.getCrossoverOperator()) {
            for (String mutationName : mutationParameters.getMutationOperator()) {
                HashMap<String, Object> parameters = new HashMap<>();

                String name = "h" + lowLevelHeuristicNumber + " [" + crossoverName;
                if (mutationName != null) {
                    mutation = MutationFactory.getMutationOperator(mutationName, null);
                    mutation.setParameter("probability", 1);
                    parameters.put("mutation", mutation);

                    name += ", " + mutationName;
                }
                name += "]";
                lowLevelHeuristicNames[lowLevelHeuristicNumber - 1] = name;

                parameters.put("name", name);

                crossover = CrossoverFactory.getCrossoverOperator(crossoverName, null);
                crossover.setParameter("probability", 1);
                parameters.put("crossover", crossover);

                algorithm.addLowLevelHeuristic(parameters);

                lowLevelHeuristicNumber++;
            }
        }

        NonDominatedSolutionList nonDominatedSolutions = new NonDominatedSolutionList();
        String path = "";

        /* Execute the Algorithm */
        for (int i = 0; i < mutationParameters.getExecutions(); i++) {
            NonDominatedSolutionList actualNonDominatedSolutions = new NonDominatedSolutionList();
            System.out.println("Run: " + i);
            long initTime = System.currentTimeMillis();
            SolutionSet population = algorithm.execute();
            long estimatedTime = System.currentTimeMillis() - initTime;
            System.out.println("Total time of execution: " + estimatedTime);
            /* Log messages */
            path = String.format("experiment/%s/%s/%s", ExperimentUtil.getInstanceName(mutationParameters.getInstance()), mutationParameters.getAlgo(), mutationParameters.getContext());
            String pathFun = String.format("%s/FUN_%s", path, i);
            String pathVar = String.format("%s/VAR_%s", path, i);

            for (Iterator<Solution> iterator = population.iterator(); iterator.hasNext();) {
                Solution solution = iterator.next();
                nonDominatedSolutions.add(solution);
                actualNonDominatedSolutions.add(solution);
            }
            ExperimentUtil.removeRepeated(actualNonDominatedSolutions);

            System.out.println("Objectives values have been writen to file " + pathFun);
            actualNonDominatedSolutions.printObjectivesToFile(pathFun);

            System.out.println("Variables values have been writen to file " + pathVar);
            actualNonDominatedSolutions.printVariablesToFile(pathVar);
        }

        ExperimentUtil.printFinalSolutions(nonDominatedSolutions, mutationParameters);
    }
}
