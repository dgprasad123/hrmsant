/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.AGPension;

import com.itextpdf.text.Document;
import hrms.model.AGPension.PensionNOCBean;
import hrms.model.AGPension.SearchApplBean;
import java.util.List;

/**
 *
 * @author lenovo
 */
public interface PensionNOCDAO {

    public List PensionerNocList(String loginempid);

    public List NocList(String offcode,String loginempid);

    public List getEmployeeListForPension(String loginempid);

    public List getEmployeeListForPensionfromOtherOffice(String officecode);

    public int saveEmployeeListForPension(PensionNOCBean pensionNOCBean, String loginId);

    public void savePensionDetails(PensionNOCBean pensionNOCBean);

    public void requestForNoc(PensionNOCBean pensionNOCBean, String loginId);

    public List VigilancePensionerNocList(String NocStatus);

    public List VigilancePensionerNocListByDate(String NocStatus, String fromDate, String toDate);

    public List CBPensionerNocList(String NocStatus);

    public List CBPensionerNocListByDate(String NocStatus, String fromDate, String toDate);

    public List VigilanceNocUpload(int nocId, String hrmsid);

    public List CBNocUpload(int nocId, String hrmsid);

    public PensionNOCBean getAttachedFileforNOC(int nocId);

    public PensionNOCBean getAttachedFileforCB(int nocId);

    public void GenerateNoc(Document document, int nocId, String hrmsid, String logopath);

    public void GeneratevNoc(Document document, int nocId, String hrmsid, String logopath);

    public List CadreNocList(String spc, String offcode);

    public List cadreList(String spc);

    public List getCadreEmployeeListforPromotion(String cadreValue, String nocFor);

    public int saveCadreEmployeeListForPension(PensionNOCBean pensionNOCBean, String loginId, String spc);

    public List getPostList();

    public List SearchVigilancePensionerNocList(String NocStatus, SearchApplBean sab);

}
