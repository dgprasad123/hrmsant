/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;
import hrms.model.master.OtherSPC;
import java.util.List;
/**
 *
 * @author lenovo
 */
public interface OtherSPCDAO {
     public List getOtherSPCList(String offType);
     
     public void saveOtherSPCList(OtherSPC ospc);
    
}
