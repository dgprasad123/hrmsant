/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.policemodule;

import jxl.write.WritableWorkbook;

/**
 *
 * @author Manisha
 */
public interface NominationRollRecommendCheckListDAO {

    public void downloadNominationCheckListForSI2Inspector(WritableWorkbook workbook, String currentPost, String nominatedForPost, String cadreCode, String fiscalyear);

    public void downloadNominationCheckListForHavildar2HavildarMajor(WritableWorkbook workbook, String currentPost, String nominatedForPost, String cadreCode, String fiscalyear);

    public void downloadNominationCheckListForInspector2DSP(WritableWorkbook workbook, String currentPost, String nominatedForPost, String cadreCode, String fiscalyear);

    public void downloadNominationCheckListForASI2SI(WritableWorkbook workbook, String currentPost, String nominatedForPost, String cadreCode, String fiscalyear);

    public void downloadNominationCheckListForSIArmed2InspectorArmedDistrictCadre(WritableWorkbook workbook, String currentPost, String nominatedForPost, String cadreCode, String fiscalyear);
    
    public void downloadNominationCheckListForSIArmed2InspectorArmedBatalionCadreOdiaCoy(WritableWorkbook workbook, String currentPost, String nominatedForPost, String cadreCode, String fiscalyear);
    
    public void downloadNominationCheckListForSIArmed2InspectorArmedBatalionCadreGurkhaCoy(WritableWorkbook workbook, String currentPost, String nominatedForPost, String cadreCode, String fiscalyear);
    
    public void downloadNominationCheckListForSIArmed2InspectorArmedGeneralCadre(WritableWorkbook workbook, String currentPost, String nominatedForPost, String cadreCode, String fiscalyear);
    
    public void downloadNominationCheckListForASIArmed2SubInspectorArmedDistrictCadre(WritableWorkbook workbook, String currentPost, String nominatedForPost, String cadreCode, String fiscalyear);
            
    public void downloadNominationCheckListForASIArmed2SubInspectorArmedBatalionCadreGurkhaCoy(WritableWorkbook workbook, String currentPost, String nominatedForPost, String cadreCode, String fiscalyear);
    
    public void downloadNominationCheckListForASIArmed2SubInspectorArmedBatalionCadreOdiaCoy(WritableWorkbook workbook, String currentPost, String nominatedForPost, String cadreCode, String fiscalyear);
    
   public void downloadNominationCheckListForConstableHavtoASI(WritableWorkbook workbook, String currentPost, String nominatedForPost, String cadreCode, String fiscalyear);
    
    public void downloadNominationCheckList(WritableWorkbook workbook, String currentPost, String nominatedForPost, String cadreCode, String fiscalyear);

    public void downloadNominationCheckListForInspectorArmed2AssistantCommandant(WritableWorkbook workbook, String currentPost, String nominatedForPost, String cadreCode, String fiscalyear);
    
    public void downloadNominationCheckListForJuniorClerk2SeniorClerk(WritableWorkbook workbook, String currentPost, String nominatedForPost, String cadreCode, String fiscalyear);
    
    public void downloadNominationCheckListForGroupD2JuniorClerk(WritableWorkbook workbook, String currentPost, String nominatedForPost, String cadreCode, String fiscalyear);
    
}
