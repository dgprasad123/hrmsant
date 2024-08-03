/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.EquivalentPost;
import hrms.model.EquivalentPost.EquivalentPost;
import java.util.List;
/**
 *
 * @author Surendra
 */
public interface EquivalentPostDAO {
    
    public void saveEquivalentPost(EquivalentPost equivalpostForm, int notid);
    
    public List getEquivalentPostList(String empid);
    
    public EquivalentPost getempeditEquivalentData(String empid, int notId);
     
    public void updateEquivalentPost(EquivalentPost equivalpostForm);
    
    public void getCurrentEmpData(String empid, EquivalentPost equivalpostForm);
   
}
