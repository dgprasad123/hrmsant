/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.payroll.services;

import hrms.common.CommonFunctions;
import hrms.common.DMPUtil;
import hrms.common.Numtowordconvertion;
import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.common.CommonReportParamBean;
import hrms.model.payroll.schedule.LicScheduleBean;
import hrms.model.payroll.schedule.LicSchedulePolicyBean;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 *
 * @author Surendra
 */
@Service
public class LICScheduleServices {
    public static final int characterPerLine = 87;
    public static final int lineperpage = 62;
    
    public ScheduleDAO comonScheduleDao;

    public void setComonScheduleDao(ScheduleDAO comonScheduleDao) {
        this.comonScheduleDao = comonScheduleDao;
    }
    
        
    private void printHeader(DMPUtil dmpUtil,CommonReportParamBean crb,String billdesc,String billdate,int pageNo)throws Exception{
        String officename=crb.getOfficeen();
        dmpUtil.writeToFile(StringUtils.center(officename,characterPerLine));
        dmpUtil.writeToFile(StringUtils.leftPad("Page : "+pageNo,characterPerLine));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile((char)27);
        dmpUtil.writeToFile((char)69);
        dmpUtil.writeToFile(StringUtils.rightPad("Bill No : "+billdesc+((char)27)+((char)70),characterPerLine," "));
        dmpUtil.writeToFile("8448-LIC Deduction Govt Employees");
        dmpUtil.writeToFile("BT SL NO-7100");
        dmpUtil.writeToFile("LIFE INSURANCE CORPORATION OF INDIA     SCHEDULE FORM 'C' P A CODE NO : "+StringUtils.defaultString(crb.getPacode()));
        dmpUtil.writeToFile("_________  DIVISION                     Designation of Drawing officer :");        
        dmpUtil.writeToFile(StringUtils.rightPad(" ",40)+StringUtils.defaultString(crb.getDdoname()));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("Orissa State Government servant policies     (This statement in triplicate should be");
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("statement showing deduction on account       completed everymonth. Two copies to be");
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("of premia towords Life Insurance Corporation  sent along with the paybill and other to");
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("of India policies from pay salary for :       be retained in Office along with the copy");
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile(CommonFunctions.getMonthAsString(crb.getAqmonth())+"-"+crb.getAqyear()+" of pay Bill)");
        dmpUtil.writeToFile("");
        
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile(StringUtils.rightPad(" ",45)+"Name & Address Of Institution: ");
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile(StringUtils.leftPad(officename,85));
        
        dmpUtil.writeToFile("---------------------------------------------------------------------------------------");
        dmpUtil.writeToFile(" Sl   Name of Policy Holder              Policy No  Month to    premium  Amt.  Remarks");
        dmpUtil.writeToFile(" No.                                                Which Reco-          Dedu-        ");
        dmpUtil.writeToFile("                                                    very Relate          cted         ");
        dmpUtil.writeToFile("---------------------------------------------------------------------------------------");
        dmpUtil.writeToFile("               1                           2            3          4      5       6");
        dmpUtil.writeToFile("---------------------------------------------------------------------------------------");
    }
    
    private void printPageFooter(DMPUtil dmpUtil,CommonReportParamBean crb,int pageTotal){
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("---------------------------------------------------------------------------------------");
        dmpUtil.writeToFile("* Page Total * :"+StringUtils.leftPad(pageTotal+"",60));
        dmpUtil.writeToFile("---------------------------------------------------------------------------------------");
        dmpUtil.writeToFile(StringUtils.leftPad("In Words (Rupees "+StringUtils.upperCase(Numtowordconvertion.convertNumber(pageTotal)+" ) Only"),76));
        dmpUtil.writeToFile("");        
    }
    private void printCarryForward(DMPUtil dmpUtil,int pageTotal,int pageNo){                
        dmpUtil.writeToFile(StringUtils.rightPad("CARRIED FROM PAGE :"+pageNo,22)+StringUtils.leftPad(""+pageTotal,54));
        dmpUtil.writeToFile("------------------------------------------------------------------------------------------");        
    }
    public boolean write(String billno, String folderPath, String fileSeparator, CommonReportParamBean crb)throws Exception
    {
        
        String fileName = "LICSCHEDULE.txt";
        int year = 0;
        int month = 0;
        String billdesc = "";
        String billdate = "";
        boolean dataFound = false;
        boolean createFile= false;
        try{
            year = crb.getAqyear();
            month = crb.getAqmonth();
            billdesc = crb.getBilldesc();
            folderPath = folderPath + fileSeparator;
            billdate = crb.getBilldate(); 
            
            
            DMPUtil dmpUtil = null;
            
            int pageNo = 0;
            int slno=0;
            int empcnt = 0;
            int total = 0; 
            int cnt=0;
            String query1="";
            String empCode="";
            List<LicScheduleBean> licList=comonScheduleDao.getLICScheduleEmpList("LIC",billno,month,year);
            
            for (LicScheduleBean lic : licList) {
                if(createFile==false){
                   dmpUtil= new DMPUtil(folderPath,fileName);
                    createFile=true;
                }
                empCode=lic.getEmpcode();
                dataFound = true;
                String aqslno = lic.getAqSlno();
                
                ArrayList<LicSchedulePolicyBean>  policyList= lic.getPremiumDetails();
                boolean employeePrinted = false;
        
                for (LicSchedulePolicyBean policyBean : policyList) {                
                    if(empCode!=null && !empCode.equals("")){                                                                      
                        if(slno==0 || slno%10 == 0){
                            pageNo ++;                        
                            if(pageNo == 1){   
                                dmpUtil.writeToFile((char)32);
                                dmpUtil.writeToFile((char)32);
                                dmpUtil.writeToFile((char)32);
                                dmpUtil.writeToFile((char)27);
                                dmpUtil.writeToFile((char)64);
                                dmpUtil.writeToFile((char)32);  
                                dmpUtil.writeToFile((char)32);                            
                                dmpUtil.writeToFile((char)18);                            
                                dmpUtil.writeToFile((char)27);
                                dmpUtil.writeToFile((char)120);
                                dmpUtil.writeToFile((char)48);
                            }else{
                                printPageFooter(dmpUtil,crb,total);     
                                dmpUtil.writeToFile((char)12);                                                        
                                dmpUtil.writeToFile((char)32);
                                dmpUtil.writeToFile((char)32);
                                dmpUtil.writeToFile((char)32);
                                dmpUtil.writeToFile((char)27);
                                dmpUtil.writeToFile((char)64);
                                dmpUtil.writeToFile((char)32);
                                dmpUtil.writeToFile((char)32);                            
                                dmpUtil.writeToFile((char)18);
                                dmpUtil.writeToFile((char)27);
                                dmpUtil.writeToFile((char)120);
                                dmpUtil.writeToFile((char)48);                            
                            }
                            printHeader(dmpUtil,crb,billdesc,billdate,pageNo);
                            if(pageNo > 1){
                                printCarryForward(dmpUtil,total,pageNo-1);
                            }
                        }
                    total = total + Integer.parseInt(policyBean.getAmount());
                    slno++;                
                    if(employeePrinted == false){
                        employeePrinted = true;
                        empcnt ++;
                        dmpUtil.writeToFile("");
                         dmpUtil.writeToFile(StringUtils.rightPad(StringUtils.defaultString(" "+empcnt),4)+StringUtils.rightPad(StringUtils.defaultString(lic.getEmpname()),31)+StringUtils.leftPad(StringUtils.defaultString(policyBean.getPolicyNo()).toUpperCase(),15)+StringUtils.leftPad(StringUtils.defaultString(policyBean.getRecoveryMonth()),10)+StringUtils.leftPad(StringUtils.defaultString(policyBean.getAmount()),9)+StringUtils.leftPad(StringUtils.defaultString(policyBean.getAmount()),7));
                        dmpUtil.writeToFile(StringUtils.rightPad(" ",4)+StringUtils.rightPad(lic.getEmpdesg(),characterPerLine)); 
                    }else{
                        dmpUtil.writeToFile(StringUtils.rightPad(" ",4)+StringUtils.rightPad("",31)+StringUtils.leftPad(StringUtils.defaultString(policyBean.getPolicyNo().toUpperCase()),15)+StringUtils.leftPad(StringUtils.defaultString(policyBean.getRecoveryMonth()),10)+StringUtils.leftPad(StringUtils.defaultString(policyBean.getAmount()),9)+StringUtils.leftPad(StringUtils.defaultString(policyBean.getAmount()),7));                    
                    }                     
                    cnt++;
                
                    }
            }
        }
            if(createFile==true){
                if(cnt>0){
                    
                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile("---------------------------------------------------------------------------------------");
                    dmpUtil.writeToFile("* Grand Total * :"+StringUtils.leftPad(total+"",59));
                    dmpUtil.writeToFile("---------------------------------------------------------------------------------------");
                    dmpUtil.writeToFile(StringUtils.leftPad("In Words (Rupees "+StringUtils.upperCase(Numtowordconvertion.convertNumber(total)+" ) Only"),76));
                    dmpUtil.writeToFile("Place : _________");
                    dmpUtil.writeToFile("Date : _________");
                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile(StringUtils.leftPad(StringUtils.defaultString(crb.getDdoname()),82));                
                    dmpUtil.writeToFile("---------------------------------------------------------------------------------------");
                    dmpUtil.writeToFile(StringUtils.center("FOR USE IN TREASURY/BANK",characterPerLine," "));
                    dmpUtil.writeToFile(" Name of Treasury/Bank :_________________________________________");
                    dmpUtil.writeToFile(" Amount remitted  : ____________________Challan No., Serial No :_____________");
                    dmpUtil.writeToFile(" Date : _________");
                    dmpUtil.writeToFile(StringUtils.leftPad("Treasury Officer/Bank Agent",80));               
                }
                dmpUtil.writeToFile((char)12);
                dmpUtil.writeToFile((char)26);  
            }
        }catch(Exception sqe){
            sqe.printStackTrace();
        } finally{
            
        }    
        return dataFound;
    }

}
