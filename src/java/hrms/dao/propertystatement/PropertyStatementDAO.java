/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.propertystatement;

import com.itextpdf.text.Document;
import hrms.model.propertystatement.PropertyDetail;
import hrms.model.propertystatement.PropertyStatement;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Manas Jena
 */
public interface PropertyStatementDAO {

    public ArrayList getPropertyList(String empid);

    public void savePropertyStmt(PropertyStatement psf);
    
    public void updatePropertyStmt(PropertyStatement psf);
    
    public boolean deletePropertyStmt(BigDecimal yearlyPropId, String empId);
    
    public boolean isCheckPropertyPeriod(String fiscalYear,Date fromdate, Date todate);
    
    public boolean isDuplicatePropertyPeriod(PropertyStatement propertyStatement);
    
    public PropertyStatement getPropertyStmt(BigDecimal yearlyPropId);
    
    public ArrayList getMovablePropertyDetailList(BigDecimal yearlyPropId);
    
    public ArrayList getImmovablePropertyDetailList(BigDecimal yearlyPropId);
    
    public int saveImmovableProperty(PropertyDetail prtDtl);
    
    public PropertyDetail getImmovableProperty(BigDecimal propertyDtlsId);
    
    public boolean deleteImmovableProperty(BigDecimal propertyDtlsId,String empId);
    
    public boolean deleteMovableProperty(BigDecimal propertyDtlsId,String empId);
    
    public boolean isPropertySubmittedDateClosed(BigDecimal yearlyPropId);
    
    public boolean submitPropertyStatement(BigDecimal yearlyPropId, String empId, String isImmovable,String isMovable, String qrCodePath, String Ip);
    
    public int saveMovableProperty(PropertyDetail prtDtl);
    
    public PropertyDetail getMovableProperty(BigDecimal propertyDtlsId);
    
    public List getPreviousFiscalYearPropertyStatementData(String empid);
    
    public List getPropertyStatementStatusData();
    
    public void viewPropertyStatementPDF(PropertyStatement propertyStatement,ArrayList movablePropertyDetailList,ArrayList immovablePropertyDetailList, Document document,String qrCodePath);
    
    public void generateQRCodeForPreviousYear(BigDecimal yearlyPropId,String qrCodePath);
}
