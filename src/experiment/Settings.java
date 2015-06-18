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
        "instances/fourballs.txt", //"instances/mid.txt",
    //"instances/trityp.txt",
    //"instances/guizzo_cas.txt",
    //"instances/guizzo_james.txt",
    //"instances/guizzo_save.txt",
    //"instances/guizzo_weatherstation.txt"
    };

    public static final HyperHeuristicType[] ALGORITHMS = {
        //HyperHeuristicType.HHNSGAII,
        //HyperHeuristicType.HHIBEA,
        //HyperHeuristicType.HHSPEA2,
        HyperHeuristicType.HHNSGAIII,
        HyperHeuristicType.RHHNSGAIII,
        HyperHeuristicType.HHIBEA,
        HyperHeuristicType.RHHIBEA
    };

    public static final int[] POPULATION_SIZE = {
        200
    };

    public static final int[] GENERATIONS = {
        600
    };

    public static final String CROSSOVER_OPERATORS = String.format("%s,%s,%s", "SinglePointCrossover", "UniformCrossoverBinary", "TwoPointsCrossoverBinary");
    public static final String MUTATION_OPERATORS = String.format("%s,%s", "BitFlipMutation", "SwapMutationBinary");
    public static final String[] SELECTION_OPERATORS = {
        "BinaryTournament2", //"RouletteWheel",
    //"LinearRanking"
    };

    //public static final HeuristicFunctionType HEURISTIC_FUNCTION = HeuristicFunctionType.Random;
    public static final int EXECUTIONS = 2;

    public static final double[] BETA = {
        //0.5,
        //0.05,
        //0.005,
        //0.0005,
        0.00005,
        //0.000005,
        //0.0000005,
        0
    };
}
