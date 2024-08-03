/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.model.master.SubDivision;
import java.util.List;

/**
 *
 * @author manisha
 */
public interface SubDivisionDAO {
    public List getSubDivisionList();
    public SubDivision getSubDivisionDetail(String subDivisionCode);
    
}
