/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import algorithm.HyperHeuristic;
import algorithm.hhnsgaII.HHNSGAII;
import algorithm.hhnsgaIII.HHNSGAIII;
import algorithm.hhnsgaIII.HHNSGAIIIBuilder;
import experiment.HyperHeuristicType;
import static experiment.HyperHeuristicType.hhNSGAIII;
import experiment.Parameters;
import experiment.Settings;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import jmetal.util.JMException;
import jmetal.util.NonDominatedSolutionList;
import lowlevelheuristic.HeuristicFunctionType;
import lowlevelheuristic.LowLevelHeuristic;
import operators.crossover.UniformCrossoverBinary4NSGAIII;
import operators.mutation.SwapMutationBinary4NSGAIII;
import operators.selection.BinaryTournament24NSGAIII;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.NSGAIIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.SinglePointCrossover;
import org.uma.jmetal.operator.impl.mutation.BitFlipMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.archive.impl.NonDominatedSolutionListArchive;
import org.uma.jmetal.util.fileoutput.SolutionSetOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

/**
 *
 * @author thaina
 */
public class ExperimentUtil {

    public static Parameters verifyParameters(String[] args) {

        Parameters mutationParameters = new Parameters();
        //instance
        if (args[0] != null && !args[0].trim().equals("")) {
            mutationParameters.setInstance(args[0]);
        }

        //algorithm
        if (args[1] != null && !args[1].trim().equals("")) {
            mutationParameters.setAlgo(HyperHeuristicType.valueOf(args[1]));
        }

        //populationSize
        if (args[2] != null && !args[2].trim().equals("")) {
            try {
                mutationParameters.setPopulationSize(Integer.valueOf(args[2]));
            } catch (NumberFormatException ex) {
                System.out.println("Population size argument not integer.");
                System.exit(1);
            }
        }

        //generations
        if (args[3] != null && !args[3].trim().equals("")) {
            try {
                mutationParameters.setGenerations(Integer.valueOf(args[3]));
            } catch (NumberFormatException ex) {
                System.out.println("Generations argument not integer.");
                System.exit(1);
            }
        }

        //crossoverOperator
        if (args[4] != null && !args[4].trim().equals("")) {
            mutationParameters.setCrossoverOperator(args[4].split(","));
        }

        //mutationOperator
        if (args[5] != null && !args[5].trim().equals("")) {
            mutationParameters.setMutationOperator(("null," + args[5]).split(","));
        }

        //executions
        if (args[6] != null && !args[6].trim().equals("")) {
            try {
                mutationParameters.setExecutions(Integer.valueOf(args[6]));
            } catch (NumberFormatException ex) {
                System.out.println("Executions argument not double.");
                System.exit(1);
            }
        }

        //context
        if (args[7] != null && !args[7].trim().equals("")) {
            mutationParameters.setContext(args[7]);
        }

        //selection operator
        if (args[8] != null && !args[8].trim().equals("")) {
            mutationParameters.setSelectionOperator(args[8]);
        }

        return mutationParameters;
    }

    public static String getInstanceName(String path) {
        int end = path.indexOf(".txt");
        return path.substring(10, end);
    }

    //for jmetal 4.5
    public static void removeRepeated(NonDominatedSolutionList nonDominatedSolutions) {
        for (int i = 0; i < nonDominatedSolutions.size() - 1; i++) {
            String solucao = nonDominatedSolutions.get(i).getDecisionVariables()[0].toString();
            for (int j = i + 1; j < nonDominatedSolutions.size(); j++) {
                String solucaoB = nonDominatedSolutions.get(j).getDecisionVariables()[0].toString();
                if (solucao.equals(solucaoB)) {
                    nonDominatedSolutions.remove(j);
                    j--;
                }
            }
        }
    }

    //for jmetal 5.0
    public static void removeRepeated(NonDominatedSolutionListArchive nonDominatedSolutions) {
        List<Solution> listSolutions = nonDominatedSolutions.getSolutionList();
        for (int i = 0; i < listSolutions.size() - 1; i++) {
            String solucao = listSolutions.get(i).getVariableValue(0).toString();
            for (int j = i + 1; j < listSolutions.size(); j++) {
                String solucaoB = listSolutions.get(j).getVariableValue(0).toString();
                if (solucao.equals(solucaoB)) {
                    listSolutions.remove(j);
                    j--;
                }
            }
        }
    }

    public static Algorithm algorithmBuilder(Parameters mutationParameters, Problem problem, String[] crossovers, String[] mutations, HeuristicFunctionType heuristicFunctiontype) throws JMException {

        Algorithm algorithm = null;

        CrossoverOperator crossover = getCrossoverOperator(mutationParameters);

        MutationOperator mutation = getMutationOperator(mutationParameters);

        SelectionOperator selection = getSelectionOperator(mutationParameters);

        int maxEvaluations = mutationParameters.getPopulationSize() * mutationParameters.getGenerations();

        if (mutationParameters.getAlgo().name().equalsIgnoreCase("NSGAIII")) {
            algorithm = new NSGAIIIBuilder(problem)
                    .setCrossoverOperator(crossover)
                    .setMutationOperator(mutation)
                    .setSelectionOperator(selection)
                    .setPopulationSize(mutationParameters.getPopulationSize())
                    .setMaxEvaluations(maxEvaluations)
                    .setDivisions(mutationParameters.getPopulationSize())
                    .build();
        } else if (mutationParameters.getAlgo().name().equalsIgnoreCase("HHNSGAIII")) {
            algorithm = new HHNSGAIIIBuilder(problem)
                    .setPopulationSize(mutationParameters.getPopulationSize())
                    .setSelectionOperator(selection)
                    .setLowLevelHeuristic(HyperHeuristicUtilJM5.getLowLevelHeuristics(crossovers, mutations))
                    .setHeuristicFunction(heuristicFunctiontype)
                    .setMaxEvaluations(maxEvaluations)
                    .setDivisions(mutationParameters.getPopulationSize())
                    .build();
        }
        return algorithm;
    }

    public static SelectionOperator getSelectionOperator(Parameters mutationParameters) throws JMException {
        SelectionOperator selection;
        selection = selectSelectionOperator(mutationParameters);
        return selection;
    }

    public static MutationOperator getMutationOperator(Parameters mutationParameters) throws JMException {
        MutationOperator mutation;
        mutation = selectMutationOperator(mutationParameters);
        return mutation;
    }

    public static CrossoverOperator getCrossoverOperator(Parameters mutationParameters) throws JMException {
        CrossoverOperator crossover;
        HashMap parameters = new HashMap();
        parameters.put("probability", 1);
        crossover = selectCrossoverOperator(parameters, mutationParameters);
        return crossover;
    }

    public static void printFinalSolutions(NonDominatedSolutionList nonDominatedSolutions, Parameters mutationParameters) {
        String path = String.format("experiment/%s/%s/%s/%s-%s", ExperimentUtil.getInstanceName(mutationParameters.getInstance()), mutationParameters.getAlgo(), Settings.HEURISTIC_FUNCTION, mutationParameters.getPopulationSize(), mutationParameters.getGenerations());
        ExperimentUtil.removeRepeated(nonDominatedSolutions);
        String pathFunAll = path + "/FUN_All";
        String pathVarAll = path + "/VAR_All";
        nonDominatedSolutions.printObjectivesToFile(pathFunAll);
        nonDominatedSolutions.printVariablesToFile(pathVarAll);
    }

    public static void printFinalSolutions(NonDominatedSolutionListArchive nonDominatedSolutions, Parameters mutationParameters) {
        String path = String.format("experiment/%s/%s/%s/%s-%s", ExperimentUtil.getInstanceName(mutationParameters.getInstance()), mutationParameters.getAlgo(), Settings.HEURISTIC_FUNCTION, mutationParameters.getPopulationSize(), mutationParameters.getGenerations());
        ExperimentUtil.removeRepeated(nonDominatedSolutions);
        String pathFunAll = path + "/FUN_All";
        String pathVarAll = path + "/VAR_All";
        new SolutionSetOutput.Printer(nonDominatedSolutions.getSolutionList())
                .setSeparator("\t")
                .setVarFileOutputContext(new DefaultFileOutputContext(pathVarAll))
                .setFunFileOutputContext(new DefaultFileOutputContext(pathFunAll))
                .print();
    }

    public static MutationOperator selectMutationOperator(Parameters mutationParameters) {
        if (mutationParameters.getMutationOperator().equals("SwapMutationBinary")) {
            return new SwapMutationBinary4NSGAIII(1);
        } else if (mutationParameters.getMutationOperator().equals("BitFlipMutation")) {
            return new BitFlipMutation(1);
        }
        return null;
    }

    public static CrossoverOperator selectCrossoverOperator(HashMap parameters, Parameters crossoverParameters) {
        if (crossoverParameters.getCrossoverOperator().equals("UniformCrossoverBinary")) {
            return new UniformCrossoverBinary4NSGAIII(parameters);
        } else if (crossoverParameters.getCrossoverOperator().equals("SinglePointCrossover")) {
            return new SinglePointCrossover(1);
        }
        return null;
    }

    public static SelectionOperator selectSelectionOperator(Parameters mutationParameters) {
        if (mutationParameters.getSelectionOperator().equals("BinaryTournament2")) {
            return new BinaryTournament24NSGAIII();
        }
        return null;
    }

    public static void printSingleHeuristicInformation(FileWriter fileWriter, int i, HyperHeuristic algorithm, List<Integer> numberOfTimesAppliedAllRuns) throws IOException {
        fileWriter.write("Run: " + i + "\n");
        List<LowLevelHeuristic> lowLevelHeuristics = algorithm.getLowLevelHeuristics();
        printHeuristicInformation(lowLevelHeuristics, i, numberOfTimesAppliedAllRuns, fileWriter);
        algorithm.clearLowLeverHeuristicsValues();
    }

    public static void printSingleHeuristicInformation(FileWriter fileWriter, int i, HHNSGAIII algorithm, List<Integer> numberOfTimesAppliedAllRuns) throws IOException {
        fileWriter.write("Run: " + i + "\n");
        List<LowLevelHeuristic> lowLevelHeuristics = algorithm.getLowLevelHeuristics();
        printHeuristicInformation(lowLevelHeuristics, i, numberOfTimesAppliedAllRuns, fileWriter);
        algorithm.clearLowLeverHeuristicsValues();
    }

    private static void printHeuristicInformation(List<LowLevelHeuristic> lowLevelHeuristics, int i, List<Integer> numberOfTimesAppliedAllRuns, FileWriter fileWriter) throws IOException {
        for (int j = 0; j < lowLevelHeuristics.size(); j++) {
            if (i == 0) {
                numberOfTimesAppliedAllRuns.add(lowLevelHeuristics.get(j).getNumberOfTimesApplied());
            } else {
                int numberOfTimesApplied = lowLevelHeuristics.get(j).getNumberOfTimesApplied();
                numberOfTimesAppliedAllRuns.set(j, numberOfTimesAppliedAllRuns.get(j) + numberOfTimesApplied);
            }
            fileWriter.write("Low Level Heuristic " + lowLevelHeuristics.get(j).getName() + " applied " + lowLevelHeuristics.get(j).getNumberOfTimesApplied() + " times\n");
        }
    }

    public static void printAllHeuristicsInformation(FileWriter fileWriter, List<Integer> numberOfTimesAppliedAllRuns) throws IOException {
        fileWriter.write("-------------------------------------------------------\n");
        fileWriter.write("All Runs\n");
        for (int i = 0; i < numberOfTimesAppliedAllRuns.size(); i++) {
            fileWriter.write("Low Level Heuristic h" + (i + 1) + " applied " + numberOfTimesAppliedAllRuns.get(i) + " times\n");
        }
    }

}
