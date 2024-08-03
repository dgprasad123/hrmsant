package hrms.dao.promotion;

import hrms.model.promotion.PromotionForm;
import java.util.List;

public interface PromotionDAO {

    public List getPromotionList(String empid);

    public List getAllotDescList(String notType);

    public void savePromotion(PromotionForm promotionForm, int notId, String loginempid, String loginuserid);

    public void updatePromotion(PromotionForm promotionForm, String loginempid, String loginuserid);

    public PromotionForm getEmpPromotionData(PromotionForm prform, int notId);

    public String getEmployeePostGroup(String empid);

    public void deletePromotion(PromotionForm promotionForm);

    public PromotionForm getEmpCurrentData(String empid);
    
    public String savePromotionDataSBCorrection(PromotionForm payfix, int notId, int refcorrectionid,String loginempid,String loginuserid);
    
    public PromotionForm editPromotionSBCorrectionDDO(PromotionForm prform, int notId,int correctionid);
    
    public void approvePromotionDataSBCorrection(PromotionForm payfix,String entrydeptcode,String entryoffcode,String entryspc,String loginuserid);
}
