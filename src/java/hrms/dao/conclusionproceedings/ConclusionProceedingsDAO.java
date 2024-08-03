/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.conclusionproceedings;

import hrms.model.conclusionproceedings.ConclusionProceedings;
import hrms.model.conclusionproceedings.PunishmentDetails;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Manas
 */
public interface ConclusionProceedingsDAO {

    public ArrayList getEmpConcProcList(String empId);

    public ArrayList getEmpConcProcListOfficewise(String offcode);

    public List getConclusionProceedingEmpList(String offCode, String empid);

    public List getConclusionProceedingEmpListByGPFNo(String searby, String gpfno);

    public void saveConclusionProceedingEmpList(ConclusionProceedings conclusionProceedings);

    public ArrayList getSelectedEmpListForConclusionProceeding(String initiatedByempId);

    public ConclusionProceedings getConclusionProceedingData(int concprocid);

    public void saveInitiationDetailsForBacklog(ConclusionProceedings conclusionProceedings);

    public ConclusionProceedings getAttachedFile(int concprocid);

    public void saveInitiationDetails(ConclusionProceedings conclusionProceedings);

    public void deleteInitiationDetails(ConclusionProceedings conclusionProceedings);

    public void saveShowCauseDetails(ConclusionProceedings conclusionProceedings);

    public void saveComplianceDetails(ConclusionProceedings conclusionProceedings);

    public void saveResultDetails(ConclusionProceedings conclusionProceedings);

    public void deleteAttachmentDetail(ConclusionProceedings conclusionProceedings);

    public List getEmpPunishDetailsData(int concprocid, String pdtype);

    public PunishmentDetails getPunishmentTypeDetails(int punishmentdetailsid, String penltdesc);

    public boolean deletePunishmentTypeDetails(int punishmentdetailsid, String penltdesc);

    public PunishmentDetails savePunishmentFineRecovery(PunishmentDetails pnd);

    public PunishmentDetails savePunishmentWithholdIncrement(PunishmentDetails pnd);

    public PunishmentDetails savePunishmentWithholdPromotion(PunishmentDetails pnd);

    public PunishmentDetails savePunishmentSuspension(PunishmentDetails pnd);

    public PunishmentDetails savePunishmentReduction(PunishmentDetails pnd);

    public PunishmentDetails savePunishmentRetirement(PunishmentDetails pnd);

    public PunishmentDetails savePunishmentRemovalService(PunishmentDetails pnd);

    public PunishmentDetails savePunishmentDismissalService(PunishmentDetails pnd);

}
