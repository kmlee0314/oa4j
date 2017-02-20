/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.rocworks.oa4j.base;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vogler
 */
public class JDpIdValueList {
    private final ArrayList<JDpVCItem> list = new ArrayList<>();
    
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("DpIdValueList: ").append(list.size()).append("\n");
        list.forEach((JDpVCItem item)-> { 
            s.append(item.toString()).append("\n");
        });
        return s.toString();
    }
    
    public void addItem(JDpVCItem item) {
        this.list.add(item);
    }
    
    public JDpVCItem getItem(int idx) {
        return list.get(idx);
    }
    
    public ArrayList<JDpVCItem> getItems() {
        return list;
    }
    
    public int getNumberOfItems() { // wincc oa api like
        return list.size();
    }
    
    public List<JDpVCItem> asList() {
        return list;
    }
        
    public void clear() {
        list.clear();
    }    
}
