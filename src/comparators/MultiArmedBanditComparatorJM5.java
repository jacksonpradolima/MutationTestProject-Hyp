/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comparators;

import lowlevelheuristic.LowLevelHeuristic;
import java.util.Comparator;
import lowlevelheuristic.LowLevelHeuristicJM5;

/**
 *
 * @author gian
 */
public class MultiArmedBanditComparatorJM5  implements Comparator<LowLevelHeuristicJM5> {

    @Override
    public int compare(LowLevelHeuristicJM5 o1, LowLevelHeuristicJM5 o2) {
        if (o1.getMultiArmedBanditValue() > o2.getMultiArmedBanditValue()) {
            return -1;
        } else if (o1.getMultiArmedBanditValue() < o2.getMultiArmedBanditValue()) {
            return 1;
        } else {
            return 0;
        }
    }
    
}
