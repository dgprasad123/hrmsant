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
import hrms.model.payroll.schedule.PtScheduleBean;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 *
 * @author Surendra
 */

@Service
public class PTScheduleServices {
    public static final int characterPerLine = 87;
    public static final int lineperpage = 62;
    
    public ScheduleDAO comonScheduleDao;

    public void setComonScheduleDao(ScheduleDAO comonScheduleDao) {
        this.comonScheduleDao = comonScheduleDao;
    }
    
    
    

    private void printHeader(DMPUtil dmpUtil,CommonReportParamBean crb,String billdesc,String billdate,int pageNo)throws Exception{
        String deptname=crb.getOfficeen();
        dmpUtil.writeToFile(StringUtils.center("         Page : "+pageNo,characterPerLine," "));
        dmpUtil.writeToFile(" ");
        
        dmpUtil.writeToFile(StringUtils.center("SCHEDULE OF RECOVERY OF TAX ON PROFESSION",characterPerLine," "));
        dmpUtil.writeToFile(StringUtils.center("OF "+deptname,characterPerLine," ")); 
        dmpUtil.writeToFile(StringUtils.center("********",characterPerLine," "));
        
        dmpUtil.writeToFile(StringUtils.rightPad("0028-OTHER TAXES ON INCOME AND EXPENDITURE-107-TAXES ON PROFESSION",characterPerLine," "));
        dmpUtil.writeToFile(StringUtils.rightPad("TRADES, CALLING AND EMPLOYMENT-01045-TAXES ON PROFESSION",characterPerLine," "));
        dmpUtil.writeToFile(StringUtils.leftPad("Pay Bill No "+billdesc,characterPerLine," "));
        dmpUtil.writeToFile(StringUtils.leftPad("For the month of "+CommonFunctions.getMonthAsString(crb.getAqmonth())+"-"+crb.getAqyear(),characterPerLine," "));
        
        
        dmpUtil.writeToFile(StringUtils.repeat("-",characterPerLine));
        dmpUtil.writeToFile("  No.  Name & Designation                                      Salary      deducted");
        dmpUtil.writeToFile("       of employee                                            (in Rs.)     (in Rs.)");
        dmpUtil.writeToFile(StringUtils.repeat("-",characterPerLine));
        dmpUtil.writeToFile("  1        2                                                      3            4   ");
        dmpUtil.writeToFile(StringUtils.repeat("-",characterPerLine));
    }
    private void printPageFooter(DMPUtil dmpUtil,CommonReportParamBean crb,int pageTotal){
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile(StringUtils.repeat("-",characterPerLine));
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("* Page Total * :"+StringUtils.leftPad(pageTotal+"",65));
        dmpUtil.writeToFile(StringUtils.leftPad("In Words (Rupees "+StringUtils.upperCase(Numtowordconvertion.convertNumber(pageTotal)+" ) Only"),81));        
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile("");
        dmpUtil.writeToFile(StringUtils.defaultString(crb.getDdoname()));
    }
    public boolean write( String billno, String folderPath, String fileSeparator, CommonReportParamBean crb)throws Exception
    {
        String fileName = "PTSCHEDULE.txt";
        int year = 0;
        int month = 0;
        String billdesc = "";
        String billdate = "";
        boolean dataFound = false;
        boolean createFile=false;
        try{
            year = crb.getAqyear();
            month = crb.getAqmonth();
            billdesc = crb.getBilldesc();
            folderPath = folderPath + fileSeparator;
            billdate = crb.getBilldate();
            
            DMPUtil dmpUtil = null;
            
            
            int pageNo = 0;
            int basic = 0;
            int slno=0;
            int total = 0; 
            int cnt=0;
            int payBillMonth=0;
            int payBillYear=0;
            
            List<PtScheduleBean> ptlist=comonScheduleDao.getPTScheduleEmployeeList(billno,month,year);
            
            
           for (PtScheduleBean pts : ptlist) {
            
                
                if(pts.getEmpname() != null && !pts.getEmpname().equals("")){
                    if(createFile==false){
                       dmpUtil= new DMPUtil(folderPath, fileName);
                        createFile=true;
                    }
                    dataFound = true;
                    basic = pts.getBasicSal();  
                                 
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
                    }
                    total = total +Integer.parseInt(pts.getEmpTaxOnProffesion());       
                    slno++;
                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile(StringUtils.rightPad("  "+slno,6)+StringUtils.rightPad(pts.getEmpname(),55)+StringUtils.leftPad(""+pts.getEmpGrossSal(),8)+StringUtils.leftPad(pts.getEmpTaxOnProffesion(),12));                 
                    dmpUtil.writeToFile(StringUtils.rightPad("  ",6)+StringUtils.rightPad(pts.getEmpdesg(),characterPerLine));                    
                    cnt++;
                }
            }
            
            if(createFile==true){
                if(cnt>0){                
                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile(StringUtils.repeat("-",characterPerLine));
                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile(StringUtils.rightPad("* Grand Total * ",17)+StringUtils.leftPad(""+total,64));
                    dmpUtil.writeToFile(StringUtils.leftPad("In Words (Rupees "+StringUtils.upperCase(Numtowordconvertion.convertNumber(total)+" ) Only"),81));
                    dmpUtil.writeToFile("");
                    dmpUtil.writeToFile("");
                    if(crb.getAqmonth()<=10){
                        payBillMonth=crb.getAqmonth()+1;
                        payBillYear=year;
                    }else{
                        payBillMonth=0;
                        payBillYear=year+1;
                    }
                    dmpUtil.writeToFile(StringUtils.rightPad("For the month of "+CommonFunctions.getMonthAsString(crb.getAqmonth())+" payable on "+CommonFunctions.getMonthAsString(payBillMonth)+" "+payBillYear,characterPerLine," "));
                    dmpUtil.writeToFile(StringUtils.leftPad("Designation of the Drawing Officer: ",characterPerLine));      
                    dmpUtil.writeToFile(StringUtils.leftPad(StringUtils.defaultString(crb.getDdoname()),characterPerLine));
                }
                dmpUtil.writeToFile((char)12);
                dmpUtil.writeToFile((char)26);
            }
        }catch(Exception sqe){
            sqe.printStackTrace();
        }
        return dataFound;
    }

}
