/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparators;

import lowlevelheuristic.HeuristicFunctionType;
import java.util.Comparator;
import lowlevelheuristic.LowLevelHeuristicJM5;

/**
 *
 * @author Prado Lima
 */
public class ComparatorFactoryJM5 {

    public static Comparator<LowLevelHeuristicJM5> createComparator(HeuristicFunctionType name) {
        switch (name) {
            case ChoiceFunction:
                return new ChoiceFunctionComparatorJM5();
            case MultiArmedBandit:
                return new MultiArmedBanditComparatorJM5();
            case Random:
                return new RandomComparatorJM5();
            default:
                return null;
        }
    }

}
