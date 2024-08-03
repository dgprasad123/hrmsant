/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.model.master.Block;
import java.util.ArrayList;

/**
 *
 * @author lenovo pc
 */
public interface BlockDAO {

    public ArrayList getBlockList(String distCode);

    public Block getBlockDetails(String blockCode);
    
    public ArrayList getAllBlockList();
    
    public Block editBlock(Block block);
    
    public void saveNewBlock(Block block);
    
    public void updateNewBlock(Block block);

}
