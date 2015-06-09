/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package experiment;

import lowlevelheuristic.HeuristicFunctionType;

/**
 * Class that contains the configuration of Mutation Test
 *
 * @author Prado Lima
 */
public class Settings {

    public static final String[] INSTANCES = {
        //"instances/bisect.txt",
        //"instances/bub.txt",
        //"instances/find.txt",
        //"instances/fourballs.txt",
        //"instances/mid.txt",
        //"instances/trityp.txt",
        //"instances/guizzo_cas.txt",
        "instances/guizzo_james.txt",
        //"instances/guizzo_save.txt",
        //"instances/guizzo_weatherstation.txt"
    };

    public static final HyperHeuristicType[] ALGORITHMS = {
        HyperHeuristicType.hhNSGAII,
        HyperHeuristicType.hhIBEA,
        HyperHeuristicType.hhSPEA2,
        HyperHeuristicType.hhNSGAIII
    };

    public static final int[] POPULATION_SIZE = {
        200
    };

    public static final int[] GENERATIONS = {
        600
    };

    //public static final String CROSSOVER_OPERATORS = String.format("%s,%s,%s", "SinglePointCrossover", "UniformCrossoverBinary", "TwoPointsCrossoverBinary");
    //public static final String MUTATION_OPERATORS = String.format("%s,%s", "BitFlipMutation", "SwapMutationBinary");
    public static final String CROSSOVER_OPERATORS = String.format("%s,%s,%s", "SinglePointCrossover", "UniformCrossoverBinary4NSGAIII", "TwoPointsCrossoverBinary4NSGAIII");
    public static final String MUTATION_OPERATORS = String.format("%s,%s", "BitFlipMutation", "SwapMutationBinary4NSGAIII");
    public static final String[] SELECTION_OPERATORS = {
        "BinaryTournament2", //"RouletteWheel",
    //"LinearRanking"
    };
    
    public static final HeuristicFunctionType HEURISTIC_FUNCTION = HeuristicFunctionType.ChoiceFunction;

    public static final int EXECUTIONS = 30;
}
