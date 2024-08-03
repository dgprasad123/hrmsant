/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.policemodule;

import com.itextpdf.text.Document;
import hrms.model.policemodule.NominationForm;

/**
 *
 * @author Manisha
 */
public interface NominationRollPDFDAO {

    public NominationForm getEmployeeNominationData(int nominationMasterId, int nominationDetailId);

    //public void downloadNominationForm(Document document, int nominationMasterId, int nominationDetailId, String currentrank, String promoterank);

    public void downloadNominationFormForConstableHavtoASI(Document document, int nominationMasterId, int nominationDetailId, String currentrank, String promoterank);

    public void downloadNominationFormforASI2SubInspector(Document document, int nominationMasterId, int nominationDetailId, String currentrank, String promoterank);

    public void downloadNominationFormforASIArmed2SubInspectorArmed(Document document, int nominationMasterId, int nominationDetailId, String currentrank, String promoterank);
    
    public void downloadNominationFormforHavildar2HavildarMajor(Document document, int nominationMasterId, int nominationDetailId, String currentrank, String promoterank);

    public void downloadNominationFormforSIArmedtoInspectorArmed(Document document, int nominationMasterId, int nominationDetailId, String currentrank, String promoterank);

    public void downloadNominationFormforInspectorArmed2AssistantCommandant(Document document, int nominationMasterId, int nominationDetailId, String currentrank, String promoterank);

    public void downloadNominationFormforInspector2DSP(Document document, int nominationMasterId, int nominationDetailId, String currentrank, String promoterank);

    public void downloadNominationFormForJuniorClerkToSeniorClerk(Document document, int nominationMasterId, int nominationDetailId, String currentrank, String promoterank);
    
    public void downloadNominationFormForAsoToSo(Document document, int nominationMasterId, int nominationDetailId, String currentrank, String promoterank);
    
    public void downloadNominationFormforSI2Inspector(Document document, int nominationMasterId, int nominationDetailId, String currentrank, String promoterank);

    public void downloadNominationFormByRecommend(Document document, int nominationMasterId, int nominationDetailId, String currentrank, String promoterank);

    public void downloadNominationFormByRecommendSI2Inspector(Document document, int nominationMasterId, int nominationDetailId, String currentrank, String promoterank);

    public void downloadNominationFormByRecommendHavildarCons2ASI(Document document, int nominationMasterId, int nominationDetailId, String currentrank, String promoterank);

    public void downloadNominationFormByRecommendHavildar2MajorHavildar(Document document, int nominationMasterId, int nominationDetailId, String currentrank, String promoterank);

    public void downloadNominationFormByRecommendASI2SI(Document document, int nominationMasterId, int nominationDetailId, String currentrank, String promoterank);

    public void downloadNominationFormByRecommendInspector2DSP(Document document, int nominationMasterId, int nominationDetailId, String currentrank, String promoterank);

    public void downloadNominationFormByRecommendSIArmed2InspectorArmed(Document document, int nominationMasterId, int nominationDetailId, String currentrank, String promoterank);

    public void downloadNominationFormByRecommendInspectorArmed2AssistantCommandant(Document document, int nominationMasterId, int nominationDetailId, String currentrank, String promoterank);

    public void downloadNominationFormByRecommendAsiArmedtoSiArmed(Document document, int nominationMasterId, int nominationDetailId, String currentrank, String promoterank);

    public void downloadNominationFormByRecommendJuniorClerktoSeniorClerk(Document document, int nominationMasterId, int nominationDetailId, String currentrank, String promoterank);
    
    public void downloadNominationFormByRecommendAsoToSo(Document document, int nominationMasterId, int nominationDetailId, String currentrank, String promoterank);
    
    public void downloadAnnextureAForDSPRank(Document document, int nominationMasterId);
    

}
