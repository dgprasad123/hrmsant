/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.model.master.State;
import java.util.List;

/**
 *
 * @author Manas Jena
 */
public interface StateDAO {
    public List getStateList();
    public State getStateDetails(String stateCode);
    
    public void saveNewState(State  state);
    
    public void updateNewState(State  state);
    
    public State editState(State  state);

}
