/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparators;

import lowlevelheuristic.HeuristicFunctionType;
import java.util.Comparator;
import lowlevelheuristic.LowLevelHeuristic;

/**
 *
 * @author Prado Lima
 */
public class ComparatorFactory {

    public static Comparator<LowLevelHeuristic> createComparator(HeuristicFunctionType name) {
        switch (name) {
            case ChoiceFunction:
                return new ChoiceFunctionComparator();
            case MultiArmedBandit:
                return new MultiArmedBanditComparator();
            case Random:
                return new RandomComparator();
            default:
                return null;
        }
    }

}
