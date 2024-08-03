/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.common.LogMessage;
import hrms.common.Numtowordconvertion;
import hrms.common.ZipUtil;
import static hrms.common.ZipUtil.deleteDirectory;
import hrms.dao.master.TreasuryDAO;
import hrms.dao.payroll.billbrowser.AqReportDAO;
import hrms.dao.payroll.billbrowser.BillBrowserDAO;
import hrms.dao.payroll.schedule.LTAAGPDFHeaderFooter;
import hrms.dao.payroll.schedule.ScheduleDAOImpl;
import hrms.model.common.CommonReportParamBean;
import hrms.model.master.Treasury;
import hrms.model.payroll.aqreport.AqreportBean;
import hrms.model.payroll.billbrowser.BillBean;
import hrms.model.payroll.billbrowser.BillConfigObj;
import hrms.model.payroll.billbrowser.LongTermLoans;
import hrms.model.payroll.billbrowser.LtaBeanForAG;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Manas Jena
 */
@Service
public class LongTermAdvanceService {

    @Autowired
    public TreasuryDAO treasuryDao;
    @Autowired
    public BillBrowserDAO billBrowserDao;
    @Autowired
    public AqReportDAO aqReportDao;

    @Autowired
    public ScheduleDAOImpl comonScheduleDao;

    @Autowired
    private ServletContext servletContext;

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void createXMLFile(int month, int year, String schedule) {
        String agfilePath = servletContext.getInitParameter("agfilePath");
        try {

            File zipfile = new File(agfilePath + ".zip");
            if (zipfile.exists()) {
                //zipfile.delete();
            }
            File folder = new File(agfilePath);
            if (!folder.exists()) {
                folder.mkdir();
            } else {
                //ZipUtil.deleteDirectory(folder);
                if (!folder.exists()) {
                    folder.mkdir();
                }
            }

            ArrayList treasuryList = treasuryDao.getTreasuryList();
            for (int i = 0; i < treasuryList.size(); i++) {
                Treasury treasury = (Treasury) treasuryList.get(i);
                ArrayList payBillList = billBrowserDao.getPayBillList(year, month, treasury.getTreasuryCode());
                for (int j = 0; j < payBillList.size(); j++) {
                    BillBean bb = (BillBean) payBillList.get(j);

                    String aqDTLS = "AQ_DTLS";

                    aqDTLS = hrms.common.AqFunctionalities.getAQBillDtlsTable(bb.getBillMonth(), bb.getBillYear());
                    ArrayList longTermLoanDtls = aqReportDao.getLongTermLoanDtls(bb, aqDTLS);
                    LogMessage.setMessage(bb.getBillno());
                    if (longTermLoanDtls.size() > 0) {
                        LogMessage.setMessage("Data Found" + longTermLoanDtls.size());
                        LongTermLoans ltLoans = new LongTermLoans();
                        ltLoans.setLtaBeanForAG(longTermLoanDtls);
                        File file = new File(agfilePath + "/" + "file_" + bb.getBillno() + "_" + "LTA" + ".xml");
                        JAXBContext jaxbContext = JAXBContext.newInstance(LongTermLoans.class);
                        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
                        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                        jaxbMarshaller.marshal(ltLoans, file);
                    }
                }
            }
            /*
             ZipUtil.createZip(agfilePath);
            
             if (folder.exists()) {
             ZipUtil.deleteDirectory(folder);
             }
             */
        } catch (Exception e) {
            LogMessage.setMessage(e.getMessage());
            e.printStackTrace();
        }
    }

    public void createTXTFile(int month, int year, String schedule) {
        String agfilePath = servletContext.getInitParameter("agfilePath");
        try {
            String zipPath = agfilePath + "LTA_" + month + "_" + year;
            File zipfile = new File(zipPath + ".zip");
            if (zipfile.exists()) {
                //zipfile.delete();
            }
            File folder = new File(zipPath);
            if (!folder.exists()) {
                folder.mkdir();
            } else {
                //ZipUtil.deleteDirectory(folder);
                if (!folder.exists()) {
                    folder.mkdir();
                }
            }

            ArrayList treasuryList = treasuryDao.getTreasuryList();
            int ji = 1;
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < treasuryList.size(); i++) {
                Treasury treasury = (Treasury) treasuryList.get(i);
                ArrayList payBillList = billBrowserDao.getPayBillList(year, month, treasury.getTreasuryCode());
                for (int j = 0; j < payBillList.size(); j++) {
                    BillBean bb = (BillBean) payBillList.get(j);

                    String aqDTLS = "AQ_DTLS";

                    aqDTLS = hrms.common.AqFunctionalities.getAQBillDtlsTable(bb.getBillMonth(), bb.getBillYear());

                    ArrayList longTermLoanDtls = aqReportDao.getLongTermLoanDtls(bb, aqDTLS);
                    LogMessage.setMessage(bb.getBillno());
                    if (longTermLoanDtls.size() > 0) {
                        for (int fi = 0; fi < longTermLoanDtls.size(); fi++) {
                            LtaBeanForAG lta = (LtaBeanForAG) longTermLoanDtls.get(fi);
                            ji++;
                            sb.append(" INSERT INTO AG_LT (LOAN_TYP, LOAN_STYP, SNC_NO, SNC_DT, FIN_YR, ACC_MN, ACC_TYP, "
                                    + "  SEC_NM, SAL_MN, ADJ_MN, PI_FLG, RECV_ORD_NO, RECV_ORD_DT, RECV_TYP, PAY_MODE, "
                                    + "  TR_FIN_YR, TR_MN, TR_CD, TVTC_NO, TVTC_DT, TVTC_AMT, DDO_CD, INST_NO, AMT_PAID, "
                                    + "  BASE_HD_CD, REF_UP_TE_NO, REF_ROWID, WHO_USER, WHO_DT, AMT_TYP, LN_IDNO, R_MJH_CD, "
                                    + "  R_SMJH_CD, R_MIH_CD, R_SBH_CD, R_DTLH_CD, R_SDTLH_CD, MJH_CD, UNIT_NO, YR_TA, "
                                    + "  MJH_CD_ACT, LOP_CAC, LTA_POST_AMT, AD_REF_ID, IS_VERIFIED, IMPORT_STATUS) "
                                    + "  VALUES "
                                    + "  ('" + lta.getLOAN_TYP() + "','" + lta.getLOAN_STYP() + "','" + lta.getSNC_NO() + "','" + lta.getSNC_DT() + "','" + lta.getFIN_YR() + "','" + lta.getACC_MN() + "','" + lta.getACC_TYP() + "', "
                                    + "   '" + lta.getSEC_NM() + "','" + lta.getSAL_MN() + "','" + lta.getADJ_MN() + "','" + lta.getPI_FLG() + "','" + lta.getRECV_ORD_NO() + "','" + lta.getRECV_ORD_DT() + "','" + lta.getRECV_TYP() + "','" + lta.getPAY_MODE() + "',"
                                    + "   '" + lta.getTR_FIN_YR() + "','" + lta.getTR_MN() + "','" + lta.getTR_CD() + "','" + lta.getTVTC_NO() + "','" + lta.getTVTC_DT() + "','" + lta.getTVTC_AMT() + "','" + lta.getDDO_CD() + "','" + lta.getINST_NO() + "','" + lta.getAMT_PAID() + "',"
                                    + "   '" + lta.getBASE_HD_CD() + "', '" + lta.getREF_UP_TE_NO() + "', '" + lta.getREF_ROWID() + "', '" + lta.getWHO_USER() + "','" + lta.getWHO_DT() + "','" + lta.getAMT_TYP() + "','" + lta.getLN_IDNO() + "','" + lta.getRMJHCD() + "', "
                                    + "   '" + lta.getRSMJHCD() + "','" + lta.getRMIHCD() + "','" + lta.getRSBHCD() + "', '" + lta.getRDTLHCD() + "','" + lta.getRSDTLHCD() + "','" + lta.getBASE_HD_CD() + "','" + lta.getUNITNO() + "','" + lta.getFIN_YR() + "', "
                                    + "   '" + lta.getBASE_HD_CD() + "','" + lta.getLOPCAC() + "','" + lta.getLOANWISESUM() + "','" + lta.getAD_REF_ID() + "','" + lta.getIS_VERIFIED() + "','N' ) ;\n");
                        }
                    }
                }
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(zipPath + ".txt"));
            writer.write(sb.toString());
            writer.close();

            ZipUtil.createZip(zipPath);

            if (folder.exists()) {
                ZipUtil.deleteDirectory(folder);
            }

        } catch (Exception e) {
            LogMessage.setMessage(e.getMessage());
            e.printStackTrace();
        }
    }

    public void createPDFFile(int month, int year, String schedule) {

        String agfilePath = servletContext.getInitParameter("agfilePath");

        Document document = new Document(PageSize.A4);

        try {
            ArrayList treasuryList = treasuryDao.getTreasuryList();
            for (int i = 0; i < treasuryList.size(); i++) {
                //for (int i = 0; i < 2; i++) {
                Treasury treasury = (Treasury) treasuryList.get(i);

                String folderName = agfilePath + year;

                File folder = new File(folderName);
                //file.getParentFile().mkdirs();
                if (!folder.exists()) {
                    folder.mkdirs();

                    File innerfile = new File(folderName + "/" + treasury.getAgtreasuryCode() + "_" + schedule + "_" + (month + 1) + "_" + year + ".pdf");

                    PdfWriter.getInstance(document, new FileOutputStream(innerfile));
                    document.open();
                } else {
                    File innerfile = new File(folderName + "/" + treasury.getAgtreasuryCode() + "_" + schedule + "_" + (month + 1) + "_" + year + ".pdf");

                    PdfWriter.getInstance(document, new FileOutputStream(innerfile));
                    document.open();
                }

                ArrayList billList = billBrowserDao.getPayBillListForAG(year, month, treasury.getTreasuryCode());
                comonScheduleDao.generateSchedulePDFReportsForAG(document, schedule, billList);
                /*for(int j = 0; j < billList.size(); j++){
                 //List aquitancelist = aqReportDao.getAquitanceDataForAG()
                 BillBean bb = (BillBean) billList.get(j);
                 }*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    public void createLTAScheduleBulkPDFFileForAG(int month, int year, String schedule) throws EOFException {

        String agfilePath = servletContext.getInitParameter("agdownloadPDFPath");

        Document document = new Document(PageSize.A4);

        PdfWriter writer = null;

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();

            SelectOption so = comonScheduleDao.getBTPIHeaderClassification(schedule);

            String folderName = agfilePath + year;

            File folder = new File(folderName);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            List parentTrList = treasuryDao.getAGTreasuryList();

            Treasury parentTreasury = null;
            for (int i = 0; i < parentTrList.size(); i++) {
                //for (int i = 0; i < 5; i++) {
                parentTreasury = (Treasury) parentTrList.get(i);

                //String innerFolderName = agfilePath + year + "/" + parentTreasury.getAgtreasuryCode();
                String innerFolderName = agfilePath + year + "/" + CommonFunctions.getMonthAsString(month) + "/unsigned";

                File innerFolder = new File(innerFolderName);
                if (!innerFolder.exists()) {
                    innerFolder.mkdirs();
                }

                File innerfile = new File(innerFolder + "/" + parentTreasury.getAgtreasuryCode() + "_" + schedule + "_" + (month + 1) + "_" + year + ".pdf");

                document = new Document(PageSize.A4);

                writer = PdfWriter.getInstance(document, new FileOutputStream(innerfile));
                writer.setPageEvent(new LTAAGPDFHeaderFooter(so.getLabel(), so.getValue()));
                document.open();

                //ArrayList treasuryList = treasuryDao.getTreasuryListAG(parentTreasury.getAgtreasuryCode());
                ArrayList billList = billBrowserDao.getVoucherListForAG(year, month, parentTreasury.getAgtreasuryCode());
                if (schedule.equals("GIS")) {
                    comonScheduleDao.generateGISSchedulePDFReportsForAG(document, schedule, billList);
                } else {
                    comonScheduleDao.generateSchedulePDFReportsForAG(document, schedule, billList);
                }
                //comonScheduleDao.generateSchedulePDFReportsForAGKRD(document, schedule, billList);
                document.close();
            }
            String sql = "insert into ag_lta_pdf_generation_log(year,month,loan_type) values(?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setInt(1, year);
            pst.setInt(2, month);
            pst.setString(3, schedule);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    public void createLTAAQReportPDFFileForAG(int vchmonth, int vchyear, String agTrCode) throws EOFException {

        String agfilePath = servletContext.getInitParameter("agdownloadPDFPath");

        Document document = new Document(PageSize.A4);

        PdfWriter writer = null;

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ArrayList aqlist = new ArrayList();
        String year = "";
        String month = "";
        String billdesc = "";
        String billdate = "";
        String empType = "";
        int col19Tot = 0;
        int col20Tot = 0;
        int col191Tot = 0;
        int col201Tot = 0;
        int col202Tot = 0;
        String netPay = "";

        document.setMargins(10, 10, 10, 10);

        CommonReportParamBean crb = null;

        String format = "f1";

        AqreportBean aqreportFormBean = new AqreportBean();
        try {
            con = this.dataSource.getConnection();

            String folderName = agfilePath + "/Aquitance" + "/" + vchyear;

            File folder = new File(folderName);
            //file.getParentFile().mkdirs();
            if (!folder.exists()) {
                folder.mkdirs();
            }

            ArrayList billList = billBrowserDao.getAquitanceVoucherListForAG(vchyear, vchmonth, agTrCode);

            if (billList != null && billList.size() > 0) {
                for (int i = 0; i < billList.size(); i++) {
                    //for (int i = 0; i < 1; i++) {
                    BillBean bb = (BillBean) billList.get(i);

                    String billNo = bb.getBillno();
                    year = bb.getBillYear() + "";
                    month = bb.getBillMonth() + "";

                    //String innerFolderName = folderName + "/" + (CommonFunctions.getMonthAsString(vchmonth));
                    String innerFolderName = folderName + "/" + (CommonFunctions.getMonthAsString(vchmonth)) + "/unsigned";

                    File innerFolder = new File(innerFolderName);
                    if (!innerFolder.exists()) {
                        innerFolder.mkdirs();
                    }

                    File innerfile = new File(innerFolder + "/" + billNo + ".pdf");

                    document = new Document(PageSize.A3);
                    document.setMargins(10, 10, 10, 10);

                    writer = PdfWriter.getInstance(document, new FileOutputStream(innerfile));
                    writer.setPageEvent(new LTAAGPDFHeaderFooter("", ""));
                    document.open();

                    crb = aqReportDao.getBillDetails(bb.getBillno());

                    billdesc = crb.getBilldesc();
                    billdate = crb.getBilldate();
                    empType = aqReportDao.getEmpType(billNo, month, year);
                    BillConfigObj billConfig = aqReportDao.getBillConfig(billNo);
                    if (format != null && format.equals("f1")) {
                        //aqlist = aqReportDao.getSectionWiseBillDtls(billNo, month, year, "f1", billConfig, empType);
                    } else if (format != null && format.equals("f2")) {
                        //aqlist = aqReportDao.getSectionWiseBillDtls(billNo, month, year, "f2", billConfig, empType);
                    }
                    aqreportFormBean.setAqlist(aqlist);

                    aqreportFormBean.setOffen(crb.getOfficeen());
                    aqreportFormBean.setDept(crb.getDeptname());
                    aqreportFormBean.setDistrict(crb.getDistrict());
                    aqreportFormBean.setState(crb.getStatename());
                    aqreportFormBean.setMonth(aqReportDao.getMonth(Integer.parseInt(month)));
                    aqreportFormBean.setYear(year);
                    aqreportFormBean.setBilldesc(billdesc);
                    aqreportFormBean.setBilldate(billdate);
                    int col3Tot = aqReportDao.getColGrandTotal(aqlist, "col3", "BASIC", null) + aqReportDao.getColGrandTotal(aqlist, "col3", "SP", null) + aqReportDao.getColGrandTotal(aqlist, "col3", "GP", null) + aqReportDao.getColGrandTotal(aqlist, "col3", "IR", null);
                    int col4Tot = aqReportDao.getColGrandTotal(aqlist, "col4", "GP", null) + aqReportDao.getColGrandTotal(aqlist, "col4", "PPAY", null);
                    int col5Tot = aqReportDao.getColGrandTotal(aqlist, "col5", 0, null) + aqReportDao.getColGrandTotal(aqlist, "col5", 1, null);
                    int col6Tot = aqReportDao.getColGrandTotal(aqlist, "col6", "HRA", null) + aqReportDao.getColGrandTotal(aqlist, "col6", "ADLHRA", null) + aqReportDao.getColGrandTotal(aqlist, "col6", "LFQ", null) + aqReportDao.getColGrandTotal(aqlist, "col6", "RAFAL", null);
                    //int col7Tot = aqReportDAO.getColGrandTotal(aqlist, "col7", 0, null) + aqReportDAO.getColGrandTotal(aqlist, "col7", 1, null) + aqReportDAO.getColGrandTotal(aqlist, "col7", 2, null) + aqReportDAO.getColGrandTotal(aqlist, "col7", 3, null) + aqReportDAO.getColGrandTotal(aqlist, "col7", 4, null) + aqReportDAO.getColGrandTotal(aqlist, "col7", 5, null) + aqReportDAO.getColGrandTotal(aqlist, "col7", 6, null) + aqReportDAO.getColGrandTotal(aqlist, "col7", 7, null);
                    int col8Tot = aqReportDao.getColGrandTotal(aqlist, "col8", "GROSS PAY", null);
                    int col9Tot = aqReportDao.getColGrandTotal(aqlist, "col9", "LIC", null) + aqReportDao.getColGrandTotal(aqlist, "col9", "PLI", null);
                    int col10Tot = aqReportDao.getColGrandTotal(aqlist, "col10", "CPF", null) + aqReportDao.getColGrandTotal(aqlist, "col10", "GPF", null) + aqReportDao.getColGrandTotal(aqlist, "col10", "TPF", null) + aqReportDao.getColGrandTotal(aqlist, "col10", "GA", null) + aqReportDao.getColGrandTotal(aqlist, "col10", "TPFGA", null) + aqReportDao.getColGrandTotal(aqlist, "col10", "GPDD", null) + aqReportDao.getColGrandTotal(aqlist, "col10", "GPIR", null);
                    int col11Tot = aqReportDao.getColGrandTotal(aqlist, "col11", "PT", null) + aqReportDao.getColGrandTotal(aqlist, "col11", "IT", null);
                    int col12Tot = aqReportDao.getColGrandTotal(aqlist, "col12", "HRR", null) + aqReportDao.getColGrandTotal(aqlist, "col12", "WRR", null) + aqReportDao.getColGrandTotal(aqlist, "col12", "SWR", null) + aqReportDao.getColGrandTotal(aqlist, "col12", "HC", null);
                    int col13Tot = aqReportDao.getColGrandTotal(aqlist, "col13", "HBA", "P") + aqReportDao.getColGrandTotal(aqlist, "col13", "HBA", "I") + aqReportDao.getColGrandTotal(aqlist, "col13", "SHBA", "P") + aqReportDao.getColGrandTotal(aqlist, "col13", "SHBA", "I");
                    int col14Tot = aqReportDao.getColGrandTotal(aqlist, "col14", "MCA", "P") + aqReportDao.getColGrandTotal(aqlist, "col14", "MCA", "I") + aqReportDao.getColGrandTotal(aqlist, "col14", "MOPA", "P") + aqReportDao.getColGrandTotal(aqlist, "col14", "MOPA", "I");
                    int col15Tot = aqReportDao.getColGrandTotal(aqlist, "col15", "VE", "P") + aqReportDao.getColGrandTotal(aqlist, "col15", "VE", "I") + aqReportDao.getColGrandTotal(aqlist, "col15", "BI", "P") + aqReportDao.getColGrandTotal(aqlist, "col15", "BI", "I");
                    int col16Tot = aqReportDao.getColGrandTotal(aqlist, "col16", "PAY", null) + aqReportDao.getColGrandTotal(aqlist, "col16", "MED", null) + aqReportDao.getColGrandTotal(aqlist, "col16", "TRADE", null) + aqReportDao.getColGrandTotal(aqlist, "col16", "OVDL", null);
                    int col17Tot = aqReportDao.getColGrandTotal(aqlist, "col17", "FA", null) + aqReportDao.getColGrandTotal(aqlist, "col17", "NPSL", null) + aqReportDao.getColGrandTotal(aqlist, "col17", "EP", null) + +aqReportDao.getColGrandTotal(aqlist, "col17", "AUDR", null);
                    int col18Tot = aqReportDao.getColGrandTotal(aqlist, "col18", "OR", null) + aqReportDao.getColGrandTotal(aqlist, "col18", "GISA", "P") + aqReportDao.getColGrandTotal(aqlist, "col18", "GIS", null) + aqReportDao.getColGrandTotal(aqlist, "col18", "CMPA", null);
                    if (format != null && format.equals("f1")) {
                        col19Tot = aqReportDao.getColGrandTotal(aqlist, "col19", "TOTDEN", null);
                        col20Tot = aqReportDao.getColGrandTotal(aqlist, "col20", "NETPAY", null);
                        netPay = Numtowordconvertion.convertNumber(aqReportDao.getColGrandTotal(aqlist, "col20", "NETPAY", null));
                    } else if (format != null && format.equals("f2")) {
                        col19Tot = aqReportDao.getColGrandTotal(aqlist, "col19", "TOTDEN", null);
                        col191Tot = aqReportDao.getColGrandTotal(aqlist, "col19", "NETPAY", null);

                        col20Tot = aqReportDao.getColGrandTotal(aqlist, "col20", "PVTDED", null);
                        col201Tot = aqReportDao.getColGrandTotal(aqlist, "col20", "BANKLOAN", null);
                        col202Tot = aqReportDao.getColGrandTotal(aqlist, "col20", "NETBALANCE", null);
                        netPay = Numtowordconvertion.convertNumber(aqReportDao.getColGrandTotal(aqlist, "col20", "PVTDED", null) + aqReportDao.getColGrandTotal(aqlist, "col20", "BANKLOAN", null) + aqReportDao.getColGrandTotal(aqlist, "col20", "NETBALANCE", null));
                    }
                    aqreportFormBean.setCol3Tot(col3Tot);
                    aqreportFormBean.setCol4Tot(col4Tot);
                    aqreportFormBean.setCol5Tot(col5Tot);
                    aqreportFormBean.setCol6Tot(col6Tot);
                    //aqreportFormBean.setCol7Tot(col7Tot);
                    aqreportFormBean.setCol8Tot(col8Tot);
                    aqreportFormBean.setCol9Tot(col9Tot);
                    aqreportFormBean.setCol10Tot(col10Tot);
                    aqreportFormBean.setCol11Tot(col11Tot);
                    aqreportFormBean.setCol12Tot(col12Tot);
                    aqreportFormBean.setCol13Tot(col13Tot);
                    aqreportFormBean.setCol14Tot(col14Tot);
                    aqreportFormBean.setCol15Tot(col15Tot);
                    aqreportFormBean.setCol16Tot(col16Tot);
                    aqreportFormBean.setCol17Tot(col17Tot);
                    aqreportFormBean.setCol18Tot(col18Tot);
                    aqreportFormBean.setCol19Tot(col19Tot);
                    aqreportFormBean.setCol191Tot(col191Tot);
                    aqreportFormBean.setNetPay(netPay);
                    aqreportFormBean.setCol20Tot(col20Tot);
                    aqreportFormBean.setCol201Tot(col201Tot);
                    aqreportFormBean.setCol202Tot(col202Tot);
                    int totHrr = aqReportDao.getColGrandTotal(aqlist, "col12", "HRR", null);
                    int totHbaPri = aqReportDao.getColGrandTotal(aqlist, "col13", "HBA", "P");
                    int totShbaPri = aqReportDao.getColGrandTotal(aqlist, "col13", "SHBA", "P");
                    int totMcaPri = aqReportDao.getColGrandTotal(aqlist, "col14", "MCA", "P") + aqReportDao.getColGrandTotal(aqlist, "col14", "MOPA", "P");
                    int totShbaInt = aqReportDao.getColGrandTotal(aqlist, "col13", "SHBA", "I");
                    int totBicycPri = aqReportDao.getColGrandTotal(aqlist, "col15", "BI", "P");
                    int totPt = aqReportDao.getColGrandTotal(aqlist, "col11", "PT", null);
                    int totGisaPri = aqReportDao.getColGrandTotal(aqlist, "col18", "GISA", "P");
                    int totIt = aqReportDao.getColGrandTotal(aqlist, "col11", "IT", null);
                    int totGpf = aqReportDao.getColGrandTotal(aqlist, "col10", "GPF", null) + aqReportDao.getColGrandTotal(aqlist, "col10", "GA", null) + aqReportDao.getColGrandTotal(aqlist, "col10", "GPDD", null) + aqReportDao.getColGrandTotal(aqlist, "col10", "GPIR", null);
                    int totMopInt = aqReportDao.getColGrandTotal(aqlist, "col14", "MCA", "I") + aqReportDao.getColGrandTotal(aqlist, "col14", "MOPA", "I");
                    int totHc = aqReportDao.getColGrandTotal(aqlist, "col12", "HC", null);
                    int totHbaInt = aqReportDao.getColGrandTotal(aqlist, "col13", "HBA", "I");
                    int totGis = aqReportDao.getColGrandTotal(aqlist, "col18", "GIS", null);
                    int totVehicleInt = aqReportDao.getColGrandTotal(aqlist, "col15", "VE", "I");
                    int totCpf = aqReportDao.getColGrandTotal(aqlist, "col10", "CPF", null);
                    int totPa = aqReportDao.getColGrandTotal(aqlist, "col16", "PA", null);
                    int totCgegis = aqReportDao.getColGrandTotal(aqlist, "col18", "CGEGIS", null);
                    int totVehiclePri = aqReportDao.getColGrandTotal(aqlist, "col15", "VE", "P");
                    int totTpf = aqReportDao.getColGrandTotal(aqlist, "col10", "TPF", null);
                    int totTfga = aqReportDao.getColGrandTotal(aqlist, "col10", "TPFGA", null);
                    int totTlci = aqReportDao.getColGrandTotal(aqlist, "col9", "TLIC", null);
                    int totWrr = aqReportDao.getColGrandTotal(aqlist, "col12", "WRR", null);
                    int totSwr = aqReportDao.getColGrandTotal(aqlist, "col12", "SWR", null);
                    int totCc = aqReportDao.getColGrandTotal(aqlist, "col18", "CC", null);
                    int totCmpa = aqReportDao.getColGrandTotal(aqlist, "col18", "CMPA", null);
                    int totOvdl = aqReportDao.getColGrandTotal(aqlist, "col16", "OVDL", null);
                    aqreportFormBean.setTotHrr(totHrr);
                    aqreportFormBean.setTotHbaPri(totHbaPri);
                    aqreportFormBean.setTotShbaPri(totShbaPri);
                    aqreportFormBean.setTotMcaPri(totMcaPri);
                    aqreportFormBean.setTotShbaInt(totShbaInt);
                    aqreportFormBean.setTotBicycPri(totBicycPri);
                    aqreportFormBean.setTotPt(totPt);
                    aqreportFormBean.setTotGisaPri(totGisaPri);
                    aqreportFormBean.setTotIt(totIt);
                    aqreportFormBean.setTotGpf(totGpf);
                    aqreportFormBean.setTotMopInt(totMopInt);
                    aqreportFormBean.setTotHc(totHc);
                    aqreportFormBean.setTotHbaInt(totHbaInt);
                    aqreportFormBean.setTotGis(totGis);
                    aqreportFormBean.setTotVehicleInt(totVehicleInt);
                    aqreportFormBean.setTotCpf(totCpf);
                    aqreportFormBean.setTotPa(totPa);
                    aqreportFormBean.setTotCgegis(totCgegis);
                    aqreportFormBean.setTotVehiclePri(totVehiclePri);
                    aqreportFormBean.setTotTpf(totTpf);
                    aqreportFormBean.setTotTfga(totTfga);
                    aqreportFormBean.setTotTlci(totTlci);
                    aqreportFormBean.setTotWrr(totWrr);
                    aqreportFormBean.setTotSwr(totSwr);
                    aqreportFormBean.setTotCc(totCc);
                    aqreportFormBean.setTotCmpa(totCmpa);
                    aqreportFormBean.setTotOvdl(totOvdl);
                    HashMap payAbstract = aqReportDao.getPayAbstract(aqlist);
                    String pay = payAbstract.get("pay").toString();
                    String dp = payAbstract.get("dp").toString();
                    String da = payAbstract.get("da").toString();
                    String hra = payAbstract.get("hra").toString();
                    String oa = payAbstract.get("oa").toString();
                    int col7Tot = 0;
                    if (oa != null && !oa.equals("")) {
                        col7Tot = Integer.parseInt(oa);
                        aqreportFormBean.setCol7Tot(col7Tot);
                    }
                    aqreportFormBean.setPay(pay);
                    aqreportFormBean.setDp(dp);
                    aqreportFormBean.setDa(da);
                    aqreportFormBean.setHra(hra);
                    aqreportFormBean.setOa(oa);
                    int totAbstract = Integer.parseInt(pay) + Integer.parseInt(dp) + Integer.parseInt(da) + Integer.parseInt(hra) + Integer.parseInt(oa);
                    aqreportFormBean.setTotAbstract(totAbstract);

                    aqReportDao.generateAqReportPDF(document, billNo, crb, aqreportFormBean);

                    document.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void DownloadZIPFiles(String type, int vchyear, int vchmonth) {
        String url = "https://apps.hrmsodisha.gov.in/DownloadFilesForAG.htm?year=" + vchyear + "&month=" + vchmonth + "&type=" + type;

        String foldername = "";
        String filename = "";
        String dirpath = "";
        try {
            if (type.equals("S")) {
                foldername = "Schedule";
                filename = vchyear + "_" + CommonFunctions.getMonthAsString(vchmonth - 1) + ".zip";
            } else if (type.equals("A")) {
                foldername = "Aquitance";
                filename = vchyear + "_" + CommonFunctions.getMonthAsString(vchmonth - 1) + ".zip";
            } else if (type.equals("L")) {
                foldername = "LTA";
                filename = "LTA_" + CommonFunctions.getMonthAsString(vchmonth - 1) + "_" + vchyear + ".zip";
            }
            //folderpath = "D:\\LTA\\Test\\"+foldername+"\\"+vchyear+"_"+vchmonth+".zip";
            dirpath = "D:\\LTA\\Test\\" + foldername;
            File folder = new File(dirpath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            dirpath = dirpath + "\\" + filename;
            downloadUsingStream(url, dirpath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void downloadUsingStream(String urlStr, String file) throws IOException {
        URL url = new URL(urlStr);
        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        FileOutputStream fis = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int count = 0;
        while ((count = bis.read(buffer, 0, 1024)) != -1) {
            fis.write(buffer, 0, count);
        }
        fis.close();
        bis.close();
    }
}
