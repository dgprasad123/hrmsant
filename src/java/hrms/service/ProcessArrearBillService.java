/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.service;

import hrms.common.DataBaseFunctions;
import hrms.dao.payroll.arrear.ArrmastDAO;
import hrms.dao.payroll.billbrowser.BillGroupDAO;
import hrms.dao.payroll.billbrowser.SectionDefinationDAO;
import hrms.dao.payroll.billmast.BillMastDAO;
import hrms.model.payroll.arrear.ArrAqDtlsModel;
import hrms.model.payroll.arrear.ArrAqMastModel;
import hrms.model.payroll.arrear.PayRevisionIncrement;
import hrms.model.payroll.arrear.PayRevisionOption;
import hrms.model.payroll.billbrowser.BillGroup;
import hrms.model.payroll.billbrowser.SectionDefinition;
import hrms.model.payroll.billbrowser.SectionDtlSPCWiseEmp;
import hrms.model.payroll.billmast.BillMastModel;
import hrms.model.payroll.paybilltask.PaybillTask;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Manas
 */
@Service
public class ProcessArrearBillService {

    @Resource(name = "dataSource")
    protected DataSource dataSource;
    @Autowired
    BillGroupDAO billGroupDAO;
    @Autowired
    BillMastDAO billMastDAO;
    @Autowired
    SectionDefinationDAO sectionDefinationDAO;
    @Autowired
    ArrmastDAO arrmastDAO;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setBillGroupDAO(BillGroupDAO billGroupDAO) {
        this.billGroupDAO = billGroupDAO;
    }

    public void setBillMastDAO(BillMastDAO billMastDAO) {
        this.billMastDAO = billMastDAO;
    }

    public void setSectionDefinationDAO(SectionDefinationDAO sectionDefinationDAO) {
        this.sectionDefinationDAO = sectionDefinationDAO;
    }

    public void setArrmastDAO(ArrmastDAO arrmastDAO) {
        this.arrmastDAO = arrmastDAO;
    }

    public void updateThreadStatus(int status) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("UPDATE PAYBILL_SERVICE_NAME SET STATUS = ? WHERE SERVICE_ID=9");
            pstmt.setInt(1, status);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public String processPayFixationArrearBill(PaybillTask paybillTask) {
        String status = "S";
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("delete from arr_mast where bill_no=?");
            System.out.println("+++++++++ Billid:" + paybillTask.getBillid());
            pstmt.setInt(1, paybillTask.getBillid());
            pstmt.executeUpdate();

            //1st date of the month, 
            System.out.println(paybillTask.getBillgroupid() + ")()()()()()()()()()()()(" + paybillTask.getOffcode());
            Calendar myCalendar = new GregorianCalendar(paybillTask.getAqyear(), paybillTask.getAqmonth() + 1, 1);
            Date startDate = myCalendar.getTime();
            //1st date of the month
            myCalendar.set(Calendar.DAY_OF_MONTH, myCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            int daysInMonth = myCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            Date endDate = myCalendar.getTime();
            //End Date of Month
            BillMastModel billMastModel = billMastDAO.getBillMastDetails(paybillTask.getBillid());
            BillGroup billGroup = billGroupDAO.getBillGroupDetails(paybillTask.getBillgroupid());
            ArrayList sections = sectionDefinationDAO.getBillGroupWiseSectionList(paybillTask.getBillgroupid());
            int slno = 0;
            System.out.println("Bill Group Id:" + paybillTask.getBillgroupid());
            for (int j = 0; j < sections.size(); j++) {
                SectionDefinition secDef = (SectionDefinition) sections.get(j);
                if (secDef.getBillType().equals("CONTRACTUAL")) {
                    ArrayList spcwiseemplist = sectionDefinationDAO.getSPCWiseEmpOnlyInSection(secDef.getSectionId());
                    for (int k = 0; k < spcwiseemplist.size(); k++) {
                        SectionDtlSPCWiseEmp sdswe = (SectionDtlSPCWiseEmp) spcwiseemplist.get(k);
                        slno++;
                        Calendar choiceCal = Calendar.getInstance();
                        choiceCal.setTime(sdswe.getOrderdate());
                        int choiceYear = choiceCal.get(Calendar.YEAR);
                        int choiceMonth = choiceCal.get(Calendar.MONTH);

                        ArrAqMastModel arrAqMastModel = new ArrAqMastModel();
                        arrAqMastModel.setSlno(slno);
                        arrAqMastModel.setAqGroup(paybillTask.getBillgroupid());
                        arrAqMastModel.setEmpCode(sdswe.getEmpid());
                        arrAqMastModel.setEmpName(sdswe.getEmpname());
                        arrAqMastModel.setGpfAccNo(sdswe.getGpfaccno());
                        arrAqMastModel.setCurDesg(sdswe.getPostname());
                        arrAqMastModel.setCurBasic(sdswe.getCurBasicSalary());
                        arrAqMastModel.setGpfType(getGPFSeries(sdswe.getGpfaccno()));
                        arrAqMastModel.setChoiceDate(sdswe.getOrderdate());
                        arrAqMastModel.setBillNo(billMastModel.getBillNo());
                        arrAqMastModel.setPayMonth(paybillTask.getAqmonth());
                        arrAqMastModel.setPayYear(paybillTask.getAqyear());
                        int[] contractualMatrix = arrmastDAO.getPayMatrix(sdswe.getGp());
                        String aqslno = arrmastDAO.saveArrmastdata(arrAqMastModel);
                        System.out.println(sdswe.getEmpid() + "aqslno======1111111111=======:" + aqslno);

                        int year = choiceYear;
                        choiceMonth = choiceMonth - 1;
                        for (int month = choiceMonth; (month <= 11 && year == 2016) || (month <= 7 && year == 2017); month++) {
                            int dapercentage = 0;
                            if (month <= 5 && year == 2016) {
                                dapercentage = 0;
                            } else if (month > 5 && month <= 11 && year == 2016) {
                                dapercentage = 2;
                            } else if (month <= 5 && year == 2017) {
                                dapercentage = 4;
                            } else if (month > 5 && month <= 11 && year == 2017) {
                                dapercentage = 5;
                            }

                            System.out.println("month=" + month + "    year=" + year + "    dapercentage=" + dapercentage);

                            ArrAqDtlsModel[] arrAqDtlsModels = arrmastDAO.getAqDtlsModelFromAllowanceListForContractual(sdswe.getOrderdate(), month, year, sdswe.getEmpid(), dapercentage, aqslno, sdswe.getGp());
                            if (month == 11) {
                                month = -1;
                                year = 2017;
                            }
                            int calcUniqueNo = 0;
                            arrmastDAO.saveArrdtlsdata(arrAqDtlsModels,calcUniqueNo);
                        }
                        pstmt = con.prepareStatement("update arr_mast set full_arrear_pay = getgross_arrear(aqsl_no) WHERE aqsl_no=?");
                        pstmt.setString(1, aqslno);
                        pstmt.executeUpdate();
                        pstmt = con.prepareStatement("update arr_mast set arrear_pay = round(full_arrear_pay*0.4) WHERE aqsl_no=?");
                        pstmt.setString(1, aqslno);
                        pstmt.executeUpdate();
                    }
                } else {
                    System.out.println("****" + secDef.getSectionId());
                    ArrayList spcwiseemplist = sectionDefinationDAO.getSPCWiseEmpOnlyInSection(secDef.getSectionId());
                    for (int k = 0; k < spcwiseemplist.size(); k++) {

                        SectionDtlSPCWiseEmp sdswe = (SectionDtlSPCWiseEmp) spcwiseemplist.get(k);
                        if (sdswe.getEmpid() == null) {
                            //ArrAqMastModel
                        } else {
                            PayRevisionOption po = arrmastDAO.getChoiceDate(sdswe.getEmpid());
                            try {
                                if (po.getChoiceDate() != null) {
                                    slno++;
                                    Calendar choiceCal = Calendar.getInstance();
                                    choiceCal.setTime(po.getChoiceDate());
                                    int choiceYear = choiceCal.get(Calendar.YEAR);
                                    int choiceMonth = choiceCal.get(Calendar.MONTH);

                                    System.out.println("choiceDate:" + po.getChoiceDate());
                                    ArrAqMastModel arrAqMastModel = new ArrAqMastModel();
                                    arrAqMastModel.setSlno(slno);
                                    arrAqMastModel.setAqGroup(paybillTask.getBillgroupid());
                                    arrAqMastModel.setEmpCode(sdswe.getEmpid());
                                    arrAqMastModel.setEmpName(sdswe.getEmpname());
                                    arrAqMastModel.setGpfAccNo(sdswe.getGpfaccno());
                                    arrAqMastModel.setCurDesg(sdswe.getPostname());
                                    arrAqMastModel.setCurBasic(sdswe.getCurBasicSalary());
                                    arrAqMastModel.setGpfType(getGPFSeries(sdswe.getGpfaccno()));
                                    arrAqMastModel.setChoiceDate(po.getChoiceDate());
                                    arrAqMastModel.setBillNo(billMastModel.getBillNo());
                                    arrAqMastModel.setPayMonth(paybillTask.getAqmonth());
                                    arrAqMastModel.setPayYear(paybillTask.getAqyear());

                                    String aqslno = arrmastDAO.saveArrmastdata(arrAqMastModel);
                                    System.out.println(sdswe.getEmpid() + "aqslno======1111111111=======:" + aqslno);
                                    int year = choiceYear;
                                    choiceMonth = choiceMonth - 1;
                                    for (int month = choiceMonth; (month <= 11 && year == 2016) || (month <= 7 && year == 2017); month++) {
                                        int dapercentage = 0;
                                        if (month <= 5 && year == 2016) {
                                            dapercentage = 0;
                                        } else if (month > 5 && month <= 11 && year == 2016) {
                                            dapercentage = 2;
                                        } else if (month <= 5 && year == 2017) {
                                            dapercentage = 4;
                                        } else if (month > 5 && month <= 11 && year == 2017) {
                                            dapercentage = 5;
                                        }

                                        System.out.println("month=" + month + "    year=" + year + "    dapercentage=" + dapercentage);

                                        ArrAqDtlsModel[] arrAqDtlsModels = arrmastDAO.getAqDtlsModelFromAllowanceList(po, month, year, sdswe.getEmpid(), dapercentage, aqslno);
                                        if (month == 11) {
                                            month = -1;
                                            year = 2017;
                                        }
                                        int calcUniqueNo = 0;
                                        arrmastDAO.saveArrdtlsdata(arrAqDtlsModels,calcUniqueNo);
                                    }
                                    pstmt = con.prepareStatement("update arr_mast set full_arrear_pay = getgross_arrear(aqsl_no) WHERE aqsl_no=?");
                                    pstmt.setString(1, aqslno);
                                    pstmt.executeUpdate();
                                    pstmt = con.prepareStatement("update arr_mast set arrear_pay = round(full_arrear_pay*0.4) WHERE aqsl_no=?");
                                    pstmt.setString(1, aqslno);
                                    pstmt.executeUpdate();
                                    System.out.println("***********************************");
                                }
                            } catch (Exception exo) {
                                exo.printStackTrace();
                            }
                        }
                    }

                }

            }
            billMastDAO.markBillAsPrepared(paybillTask.getBillid());
            deletePayBillTask(paybillTask.getBillid());
        } catch (SQLException e) {
            status = "F";
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return status;
    }

    public void deletePayBillTask(int billId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("DELETE FROM PAYBILL_TASK WHERE BILL_ID=?");
            pstmt.setInt(1, billId);
            pstmt.executeUpdate();
        } catch (SQLException exe) {
            exe.printStackTrace();
        } finally {
            try {
                pstmt.close();
                con.close();
            } catch (SQLException exe) {
                exe.printStackTrace();
            }
        }
    }

    public String getGPFSeries(String gpfaccno) {
        String gpfseries = "";
        if (gpfaccno != null) {
            gpfaccno.replaceAll("[^A-Z]", "");
        }
        return gpfseries;
    }
}
