/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.heldup;

import hrms.model.heldup.RegularHeldUpBean;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 *
 * @author Manas
 */
public interface RegularHeldUpDAO {

    public ArrayList getyearList(String offcode);

    public ArrayList getmonthList();

    public ArrayList getBillGroupWiseEmployee(BigDecimal billGroupId);
    
    public void helduppay(RegularHeldUpBean regularHeldUpBean);
    
    public void releasepay(RegularHeldUpBean regularHeldUpBean);
    
    public RegularHeldUpBean getPayHeldupData(RegularHeldUpBean regularHeldUpBean);
}
