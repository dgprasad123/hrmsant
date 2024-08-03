/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.model.master.DistrictWiseEsignBean;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Surendra
 */
public interface DistrictWiseEsignDAO {

    public ArrayList getEsignList(String distCode, int smonth, int syear);

    public List searchEsignbillDelete(DistrictWiseEsignBean districtWiseEsignBean);

    public DistrictWiseEsignBean getEsignbill(int billno,int esignId);
    
    public String esignFileRename(DistrictWiseEsignBean districtWiseEsignBean, String signedfilepath);    
    
    public int updateEsigndetails(DistrictWiseEsignBean districtWiseEsignBean,String newpdffilename);
    
    public void deleteEsignBill(int billno,int esignId);



}
