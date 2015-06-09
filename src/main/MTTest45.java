package main;

import algorithm.hhnsgaII.hhNSGAII;
import experiment.Parameters;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.util.JMException;
import jmetal.util.NonDominatedSolutionList;
import lowlevelheuristic.HeuristicFunctionType;
import lowlevelheuristic.LowLevelHeuristic;
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

    public static void main(String[] args) throws JMException, ClassNotFoundException, IOException {

        Parameters mutationParameters = ExperimentUtil.verifyParameters(args);

        double crossoverProbability = 1;
        double mutationProbability = 1;

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

        // Alterar e usar a partir do arquivo de configuração
        algorithm.setInputParameter("heuristicFunction", HeuristicFunctionType.ChoiceFunction);

        HashMap parametersOperators;

        //Create low level heuristics
        int lowLevelHeuristicNumber = 1;
        String[] lowLevelHeuristicNames = new String[mutationParameters.getCrossoverOperator().length * mutationParameters.getMutationOperator().length + mutationParameters.getCrossoverOperator().length];
        for (String crossoverName : mutationParameters.getCrossoverOperator()) {
            for (String mutationName : mutationParameters.getMutationOperator()) {
                HashMap<String, Object> parameters = new HashMap<>();

                String name = "h" + lowLevelHeuristicNumber + " [" + crossoverName;
                if (mutationName != null) {
                    // Mutation operator
                    parametersOperators = new HashMap();
                    parametersOperators.put("probability", mutationProbability);
                    mutation = MutationFactory.getMutationOperator(mutationName, parametersOperators);

//                    mutation.setParameter("probability", mutationProbability);
                    parameters.put("mutation", mutation);

                    name += ", " + mutationName;
                }
                name += "]";
                lowLevelHeuristicNames[lowLevelHeuristicNumber - 1] = name;

                parameters.put("name", name);

                // Crossover operator
                parametersOperators = new HashMap();
                parametersOperators.put("probability", crossoverProbability);
                crossover = CrossoverFactory.getCrossoverOperator(crossoverName, parametersOperators);
                parameters.put("crossover", crossover);

                algorithm.addLowLevelHeuristic(parameters);

                lowLevelHeuristicNumber++;
            }
        }

        NonDominatedSolutionList nonDominatedSolutions = new NonDominatedSolutionList();
        String path = String.format("experiment/%s/%s/%s-%s", ExperimentUtil.getInstanceName(mutationParameters.getInstance()), mutationParameters.getAlgo(), mutationParameters.getPopulationSize(), mutationParameters.getGenerations());
        List<Integer> numberOfTimesAppliedAllRuns = new ArrayList<>();
        FileWriter fileWriter = new FileWriter(path + "/HHResults");

        /* Execute the Algorithm */
        for (int i = 0; i < mutationParameters.getExecutions(); i++) {
            NonDominatedSolutionList actualNonDominatedSolutions = new NonDominatedSolutionList();
            System.out.println("Run: " + i);
            long initTime = System.currentTimeMillis();
            SolutionSet population = algorithm.execute();
            long estimatedTime = System.currentTimeMillis() - initTime;
            System.out.println("Total time of execution: " + estimatedTime);
            /* Log messages */
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

            ExperimentUtil.printSingleHeuristicInformation(fileWriter, i, algorithm, numberOfTimesAppliedAllRuns);
        }

        ExperimentUtil.printAllHeuristicsInformation(fileWriter, numberOfTimesAppliedAllRuns);
        fileWriter.close();

        ExperimentUtil.printFinalSolutions(nonDominatedSolutions, mutationParameters);
    }

}
