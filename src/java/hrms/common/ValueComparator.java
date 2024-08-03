/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.common;

import java.util.Comparator;
import java.util.Map;

/**
 *
 * @author DurgaPrasad
 */
public class ValueComparator  implements Comparator<String> {

    private Map<String, String> map;

    public ValueComparator(Map<String, String> map) {
        this.map = map;
    }

    public int compare(String a, String b) {
        return map.get(a).compareTo(map.get(b));
    }
    
}
