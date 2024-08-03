package hrms.controller.payroll.aqreport;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.common.Numtowordconvertion;
import hrms.dao.esignBill.EsignBillDAO;
import hrms.dao.payroll.billbrowser.AqReportDAO;
import hrms.dao.payroll.schedule.ScheduleDAO;
import hrms.model.common.CommonReportParamBean;
import hrms.model.login.LoginUserBean;
import hrms.model.payroll.aqreport.AqreportBean;
import hrms.model.payroll.billbrowser.BillConfigObj;
import hrms.model.payroll.schedule.SectionWiseAqBean;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jxl.Workbook;
import jxl.write.WritableWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpOffice"})
public class AqBillController {

    @Autowired
    public AqReportDAO aqReportDAO;

    @Autowired
    ScheduleDAO comonScheduleDao;

    @Autowired
    EsignBillDAO esignBillDao;

    @RequestMapping(value = "aqbillreport.htm", method = RequestMethod.GET)
    // @RequestParam("billNo") String billNo

    public String aqBillReportHTML(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("CommonReportParamBean") CommonReportParamBean crb, @ModelAttribute("AqreportBean") AqreportBean aqreportFormBean,
               BindingResult result, HttpServletRequest request) {

        ArrayList aqlist = new ArrayList();
        String year = "";
        String month = "";
        String billdesc = "";
        String billdate = "";
        String empType = "";
        String billNo = aqreportFormBean.getBillNo();
        String format = aqreportFormBean.getFormat();
        int col19Tot = 0;
        int col20Tot = 0;
        int col191Tot = 0;
        int col201Tot = 0;
        int col202Tot = 0;
        String netPay = "";
        int ofcounter = 0;

        String column9NameList = null;
        String column10NameList = null;
        String column11NameList = null;
        String column12NameList = null;
        String column13NameList = null;
        String column14NameList = null;
        String column15NameList = null;
        String column16NameList = null;
        String column17NameList = "";
        String column18NameList = null;
        SectionWiseAqBean sbean=null;
       // String format = "f2";
//        if(request.getParameter("format") != null){
//            format = request.getParameter("format");
//        }else{
//            format=aqreportFormBean.getFormat();
//        }

        //Statement stmt = null;
        // ResultSet rs = null;
        try {
            //System.out.println("select empofc:;;;"+"loginoffice="+lub.getLoginoffcode());
            
            empType = aqReportDAO.getEmpType(billNo, month, year);

            // For DHE (Start)---            
            if (empType.equals("D")) {
                crb = aqReportDAO.getBillDetailsDHE(billNo);
            } //--End--
            else {
                System.out.println("billHtml");

                crb = aqReportDAO.getBillDetails(billNo);
            }

            year = crb.getAqyear() + "";
            month = crb.getAqmonth() + "";
            billdesc = crb.getBilldesc();
            billdate = crb.getBilldate();

            BillConfigObj billConfig = aqReportDAO.getBillConfig(billNo);

            Map<String, List> colNameList = aqReportDAO.getAllColumnNameList(billNo, month, year);

            if (colNameList.containsKey("9")) {

                Iterator itr = colNameList.get("9").iterator();
                while (itr.hasNext()) {
                    if (column9NameList != null && !column9NameList.equals("")) {
                        column9NameList = column9NameList + "/</br>" + (String) itr.next();
                    } else {
                        column9NameList = (String) itr.next();
                    }
                }
            } else {
                column9NameList = "LIC/<br/>PLI";
            }

            if (colNameList.containsKey("10")) {

                Iterator itr = colNameList.get("10").iterator();
                while (itr.hasNext()) {
                    if (column10NameList != null && !column10NameList.equals("")) {
                        column10NameList = column10NameList + "/</br>" + (String) itr.next();
                    } else {
                        column10NameList = (String) itr.next();
                    }
                }
            } else {
                column10NameList = "GPF/CPF/TPF<br/>DA-GPF<br/>RECOVERY";
            }

            if (colNameList.containsKey("11")) {

                Iterator itr = colNameList.get("11").iterator();
                while (itr.hasNext()) {
                    if (column11NameList != null && !column11NameList.equals("")) {
                        column11NameList = column11NameList + "/</br>" + (String) itr.next();
                    } else {
                        column11NameList = (String) itr.next();
                    }
                }
            } else {
                column11NameList = "P.TAX<br/>I.TAX";
            }

            if (colNameList.containsKey("12")) {

                Iterator itr = colNameList.get("12").iterator();
                while (itr.hasNext()) {
                    if (column12NameList != null && !column12NameList.equals("")) {
                        column12NameList = column12NameList + "/</br>" + (String) itr.next();
                    } else {
                        column12NameList = (String) itr.next();
                    }
                }
            } else {
                column12NameList = "HRR<br/>WATER TAX<br/>SWG<br/>HIRE CHG";
            }

            if (colNameList.containsKey("13")) {

                Iterator itr = colNameList.get("13").iterator();
                while (itr.hasNext()) {
                    if (column13NameList != null && !column13NameList.equals("")) {
                        column13NameList = column13NameList + "/</br>" + (String) itr.next();
                    } else {
                        column13NameList = (String) itr.next();
                    }
                }
            } else {
                column13NameList = "HB<br/> INT HB<br/>SPL HB<br/>INT SPL HB <br />";
            }

            if (colNameList.containsKey("14")) {

                Iterator itr = colNameList.get("14").iterator();
                while (itr.hasNext()) {
                    if (column14NameList != null && !column14NameList.equals("")) {
                        column14NameList = column14NameList + "/ </br>" + (String) itr.next();
                    } else {
                        column14NameList = (String) itr.next();
                    }
                }
            } else {
                column14NameList = "MC<br/>INT MC<br/>MC/MOP ADV<br/>INT MC/MOPED";
            }

            if (colNameList.containsKey("15")) {

                Iterator itr = colNameList.get("15").iterator();
                while (itr.hasNext()) {
                    if (column15NameList != null && !column15NameList.equals("")) {
                        column15NameList = column15NameList + "/ </br>" + (String) itr.next();
                    } else {
                        column15NameList = (String) itr.next();
                    }
                }
            } else {
                column15NameList = "CAR ADV<br/>INT CAR<br/>BI-CYCLE<br />INT CYCL";
            }

            if (colNameList.containsKey("16")) {

                Iterator itr = colNameList.get("16").iterator();
                while (itr.hasNext()) {
                    if (column16NameList != null && !column16NameList.equals("")) {
                        column16NameList = column16NameList + "/ </br>" + (String) itr.next();
                    } else {
                        column16NameList = (String) itr.next();
                    }
                }
            } else {
                column16NameList = "PAY ADV<br />MED ADV<br/>TRADE ADV<br/>OVDL";
            }

            if (colNameList.containsKey("17")) {

                Iterator itr = colNameList.get("17").iterator();
                while (itr.hasNext()) {
                    if (column17NameList != null && !column17NameList.equals("")) {
                        column17NameList = column17NameList + "/ </br>" + (String) itr.next();
                    } else {
                        column17NameList = (String) itr.next();
                    }
                }
            } else {
                column17NameList = "FEST<br/>NPS ARR.<br/>EX. PAY<br />RTI<br />AUDR";
            }

            if (colNameList.containsKey("18")) {

                Iterator itr = colNameList.get("18").iterator();
                while (itr.hasNext()) {
                    if (column18NameList != null && !column18NameList.equals("")) {
                        column18NameList = column18NameList + "/ </br>" + (String) itr.next();
                    } else {
                        column18NameList = (String) itr.next();
                    }
                }
            } else {
                column18NameList = "OTHER<br/>RECOVERY </br> GIS ADV </br>AIS GIS</br>COMP ADV";
            }

            List dedAbstractList = aqReportDAO.getDeductionGrandAbstract(billNo, month, year);

            if (format != null && format.equals("f1")) {
                //-- For DHE Condition is empType.equals("D")--
                if (empType.equals("D")) {
                    aqlist = aqReportDAO.getSectionWiseBillDtlsDHE(billNo, month, year, "f1", lub.getLoginoffcode(), billConfig, empType, column9NameList, column10NameList, column11NameList, column12NameList, column13NameList, column14NameList, column15NameList, column16NameList, column17NameList, column18NameList);
                } else {
                    aqlist = aqReportDAO.getSectionWiseBillDtls(billNo, month, year, "f1", billConfig, empType, column9NameList, column10NameList, column11NameList, column12NameList, column13NameList, column14NameList, column15NameList, column16NameList, column17NameList, column18NameList);
                }

            } else if (format != null && format.equals("f2")) {
                //-- For DHE Condition is empType.equals("D")----
                if (empType.equals("D")) {
                    aqlist = aqReportDAO.getSectionWiseBillDtlsDHE(billNo, month, year, "f2", lub.getLoginoffcode(), billConfig, empType, column9NameList, column10NameList, column11NameList, column12NameList, column13NameList, column14NameList, column15NameList, column16NameList, column17NameList, column18NameList);
                } else {
                    aqlist = aqReportDAO.getSectionWiseBillDtls(billNo, month, year, "f2", billConfig, empType, column9NameList, column10NameList, column11NameList, column12NameList, column13NameList, column14NameList, column15NameList, column16NameList, column17NameList, column18NameList);
                }

            }
            //System.out.println("size:" + aqlist.size());

            aqreportFormBean.setAqlist(aqlist);
            
            
            //------------------DHE----------------------
           /* ArrayList<String> offcodelist = new ArrayList();
            for (int i = 0; i < aqlist.size(); i++) {
                sbean = (SectionWiseAqBean) aqlist.get(i);
                offcodelist.add(sbean.getOffcode());

                //System.out.println("offcodelist:"+offcodelist.get(i)); 
            }
            List offIndexList=new ArrayList();
            for (String off : offcodelist) {
               
                if (!off.equals(offcodelist.get(0))) {
                    //System.out.println("off:" + off.concat(":T") + " 1stelement:" + offcodelist.indexOf(off));
                    sbean.setOffCount(offcodelist.indexOf(off));
                    offIndexList.add(sbean.getOffCount());
                    //System.out.println("offIndexList:::"+offIndexList.toString());
                    
                }
            }
            request.setAttribute("offIndexList", offIndexList);*/
            //---------------------DHE------------------------

            aqreportFormBean.setOffen(crb.getOfficeen());
            aqreportFormBean.setDept(crb.getDeptname());
            aqreportFormBean.setDistrict(crb.getDistrict());
            aqreportFormBean.setState(crb.getStatename());
            aqreportFormBean.setMonth(aqReportDAO.getMonth(Integer.parseInt(month)));
            aqreportFormBean.setYear(year);
            aqreportFormBean.setBilldesc(billdesc);
            aqreportFormBean.setBilldate(billdate);
            int col3Tot = aqReportDAO.getColGrandTotal(aqlist, "col3", "BASIC", null) + aqReportDAO.getColGrandTotal(aqlist, "col3", "SP", null) + aqReportDAO.getColGrandTotal(aqlist, "col3", "GP", null) + aqReportDAO.getColGrandTotal(aqlist, "col3", "IR", null);
            int col4Tot = aqReportDAO.getColGrandTotal(aqlist, "col4", "GP", null) + aqReportDAO.getColGrandTotal(aqlist, "col4", "PPAY", null);
            int col5Tot = aqReportDAO.getColGrandTotal(aqlist, "col5", 0, null) + aqReportDAO.getColGrandTotal(aqlist, "col5", 1, null);
            int col6Tot = aqReportDAO.getColGrandTotal(aqlist, "col6", "HRA", null) + aqReportDAO.getColGrandTotal(aqlist, "col6", "ADLHRA", null) + aqReportDAO.getColGrandTotal(aqlist, "col6", "LFQ", null) + aqReportDAO.getColGrandTotal(aqlist, "col6", "RAFAL", null);
            //int col7Tot = aqReportDAO.getColGrandTotal(aqlist, "col7", 0, null) + aqReportDAO.getColGrandTotal(aqlist, "col7", 1, null) + aqReportDAO.getColGrandTotal(aqlist, "col7", 2, null) + aqReportDAO.getColGrandTotal(aqlist, "col7", 3, null) + aqReportDAO.getColGrandTotal(aqlist, "col7", 4, null) + aqReportDAO.getColGrandTotal(aqlist, "col7", 5, null) + aqReportDAO.getColGrandTotal(aqlist, "col7", 6, null) + aqReportDAO.getColGrandTotal(aqlist, "col7", 7, null);
            int col8Tot = aqReportDAO.getColGrandTotal(aqlist, "col8", "GROSS PAY", null);
            int col9Tot = aqReportDAO.getColGrandTotal(aqlist, "col9");

            //aqReportDAO.getColGrandTotal(aqlist, "col9", "LIC", null) + aqReportDAO.getColGrandTotal(aqlist, "col9", "PLI", null);
            int col10Tot = aqReportDAO.getColGrandTotal(aqlist, "col10");

            //aqReportDAO.getColGrandTotal(aqlist, "col10", "CPF", null) + aqReportDAO.getColGrandTotal(aqlist, "col10", "GPF", null) + aqReportDAO.getColGrandTotal(aqlist, "col10", "TPF", null) + aqReportDAO.getColGrandTotal(aqlist, "col10", "GA", null) + aqReportDAO.getColGrandTotal(aqlist, "col10", "TPFGA", null) + aqReportDAO.getColGrandTotal(aqlist, "col10", "GPDD", null) + aqReportDAO.getColGrandTotal(aqlist, "col10", "GPIR", null);
            int col11Tot = aqReportDAO.getColGrandTotal(aqlist, "col11");

            //aqReportDAO.getColGrandTotal(aqlist, "col11", "PT", null) + aqReportDAO.getColGrandTotal(aqlist, "col11", "IT", null);
            int col12Tot = aqReportDAO.getColGrandTotal(aqlist, "col12");

            //aqReportDAO.getColGrandTotal(aqlist, "col12", "HRR", null) + aqReportDAO.getColGrandTotal(aqlist, "col12", "WRR", null) + aqReportDAO.getColGrandTotal(aqlist, "col12", "SWR", null) + aqReportDAO.getColGrandTotal(aqlist, "col12", "HC", null);
            int col13Tot = aqReportDAO.getColGrandTotal(aqlist, "col13");

            //aqReportDAO.getColGrandTotal(aqlist, "col13", "HBA", "P") + aqReportDAO.getColGrandTotal(aqlist, "col13", "HBA", "I") + aqReportDAO.getColGrandTotal(aqlist, "col13", "SHBA", "P") + aqReportDAO.getColGrandTotal(aqlist, "col13", "SHBA", "I");
            int col14Tot = aqReportDAO.getColGrandTotal(aqlist, "col14");

            //aqReportDAO.getColGrandTotal(aqlist, "col14", "MCA", "P") + aqReportDAO.getColGrandTotal(aqlist, "col14", "MCA", "I") + aqReportDAO.getColGrandTotal(aqlist, "col14", "MOPA", "P") + aqReportDAO.getColGrandTotal(aqlist, "col14", "MOPA", "I");
            int col15Tot = aqReportDAO.getColGrandTotal(aqlist, "col15");

            //aqReportDAO.getColGrandTotal(aqlist, "col15", "VE", "P") + aqReportDAO.getColGrandTotal(aqlist, "col15", "VE", "I") + aqReportDAO.getColGrandTotal(aqlist, "col15", "BI", "P") + aqReportDAO.getColGrandTotal(aqlist, "col15", "BI", "I");
            int col16Tot = aqReportDAO.getColGrandTotal(aqlist, "col16");

            //aqReportDAO.getColGrandTotal(aqlist, "col16", "PAY", null) + aqReportDAO.getColGrandTotal(aqlist, "col16", "MED", null) + aqReportDAO.getColGrandTotal(aqlist, "col16", "TRADE", null) + aqReportDAO.getColGrandTotal(aqlist, "col16", "OVDL", null) + aqReportDAO.getColGrandTotal(aqlist, "col16", "PA", null);
            int col17Tot = aqReportDAO.getColGrandTotal(aqlist, "col17");

            //int col17Tot = aqReportDAO.getColGrandTotal(aqlist, "col17", "FA", null) + aqReportDAO.getColGrandTotal(aqlist, "col17", "NPSL", null) + aqReportDAO.getColGrandTotal(aqlist, "col17", "EP", null) + +aqReportDAO.getColGrandTotal(aqlist, "col17", "AUDR", null) + aqReportDAO.getColGrandTotal(aqlist, "col17", "HRA_EP", null);
            int col18Tot = aqReportDAO.getColGrandTotal(aqlist, "col18");

            //aqReportDAO.getColGrandTotal(aqlist, "col18", "OR", null) + aqReportDAO.getColGrandTotal(aqlist, "col18", "GISA", "P") + aqReportDAO.getColGrandTotal(aqlist, "col18", "GIS", null) + aqReportDAO.getColGrandTotal(aqlist, "col18", "CMPA", null);
            if (format != null && format.equals("f1")) {
                col19Tot = aqReportDAO.getColGrandTotal(aqlist, "col19", "TOTDEN", null);
                col20Tot = aqReportDAO.getColGrandTotal(aqlist, "col20", "NETPAY", null);
                netPay = Numtowordconvertion.convertNumber(aqReportDAO.getColGrandTotal(aqlist, "col20", "NETPAY", null));
            } else if (format != null && format.equals("f2")) {
                col19Tot = aqReportDAO.getColGrandTotal(aqlist, "col19", "TOTDEN", null);
                col191Tot = aqReportDAO.getColGrandTotal(aqlist, "col19", "NETPAY", null);

                col20Tot = aqReportDAO.getColGrandTotal(aqlist, "col20", "PVTDED", null);
                col201Tot = aqReportDAO.getColGrandTotal(aqlist, "col20", "BANKLOAN", null);
                col202Tot = aqReportDAO.getColGrandTotal(aqlist, "col20", "NETBALANCE", null);
                netPay = Numtowordconvertion.convertNumber(aqReportDAO.getColGrandTotal(aqlist, "col20", "PVTDED", null) + aqReportDAO.getColGrandTotal(aqlist, "col20", "BANKLOAN", null) + aqReportDAO.getColGrandTotal(aqlist, "col20", "NETBALANCE", null));
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

            /*
            
             int totHrr = aqReportDAO.getColGrandTotal(aqlist, "col12", "HRR", null);
             int totHbaPri = aqReportDAO.getColGrandTotal(aqlist, "col13", "HBA", "P");
             int totShbaPri = aqReportDAO.getColGrandTotal(aqlist, "col13", "SHBA", "P");
             int totMcaPri = aqReportDAO.getColGrandTotal(aqlist, "col14", "MCA", "P") + aqReportDAO.getColGrandTotal(aqlist, "col14", "MOPA", "P");
             int totShbaInt = aqReportDAO.getColGrandTotal(aqlist, "col13", "SHBA", "I");
             int totBicycPri = aqReportDAO.getColGrandTotal(aqlist, "col15", "BI", "P");
             int totPt = aqReportDAO.getColGrandTotal(aqlist, "col11", "PT", null);
             int totGisaPri = aqReportDAO.getColGrandTotal(aqlist, "col18", "GISA", "P");
             int totIt = aqReportDAO.getColGrandTotal(aqlist, "col11", "IT", null);
             int totGpf = aqReportDAO.getColGrandTotal(aqlist, "col10", "GPF", null) + aqReportDAO.getColGrandTotal(aqlist, "col10", "GA", null) + aqReportDAO.getColGrandTotal(aqlist, "col10", "GPDD", null) + aqReportDAO.getColGrandTotal(aqlist, "col10", "GPIR", null);
             int totMopInt = aqReportDAO.getColGrandTotal(aqlist, "col14", "MCA", "I") + aqReportDAO.getColGrandTotal(aqlist, "col14", "MOPA", "I");
             int totHc = aqReportDAO.getColGrandTotal(aqlist, "col12", "HC", null);
             int totHbaInt = aqReportDAO.getColGrandTotal(aqlist, "col13", "HBA", "I");
             int totGis = aqReportDAO.getColGrandTotal(aqlist, "col18", "GIS", null);
             int totVehicleInt = aqReportDAO.getColGrandTotal(aqlist, "col15", "VE", "I");
             int totCpf = aqReportDAO.getColGrandTotal(aqlist, "col10", "CPF", null);
             int totPa = aqReportDAO.getColGrandTotal(aqlist, "col16", "PA", null);
             int totCgegis = aqReportDAO.getColGrandTotal(aqlist, "col18", "CGEGIS", null);
             int totVehiclePri = aqReportDAO.getColGrandTotal(aqlist, "col15", "VE", "P");
             int totTpf = aqReportDAO.getColGrandTotal(aqlist, "col10", "TPF", null);
             int totTfga = aqReportDAO.getColGrandTotal(aqlist, "col10", "TPFGA", null);
             int totTlci = aqReportDAO.getColGrandTotal(aqlist, "col9", "TLIC", null);
             int totWrr = aqReportDAO.getColGrandTotal(aqlist, "col12", "WRR", null);
             int totSwr = aqReportDAO.getColGrandTotal(aqlist, "col12", "SWR", null);
             int totCc = aqReportDAO.getColGrandTotal(aqlist, "col18", "CC", null);
             int totCmpa = aqReportDAO.getColGrandTotal(aqlist, "col18", "CMPA", null);
             int totOvdl = aqReportDAO.getColGrandTotal(aqlist, "col16", "OVDL", null);
             int totFestival = aqReportDAO.getColGrandTotal(aqlist, "col17", "FA", null);
             int totnpsarr = aqReportDAO.getColGrandTotal(aqlist, "col17", "NPSL", null);
             aqreportFormBean.setTotNpsArrear(totnpsarr);
             aqreportFormBean.setTotFestival(totFestival);
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
            
             */
            HashMap payAbstract = aqReportDAO.getPayAbstract(aqlist);
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
            String offname = aqreportFormBean.getOffen();
            String deptname = aqreportFormBean.getDept();
            String distname = aqreportFormBean.getDistrict();
            String statename = aqreportFormBean.getState();

            request.setAttribute("offname", offname);
            request.setAttribute("deptname", deptname);
            request.setAttribute("distname", distname);
            request.setAttribute("statename", statename);
            request.setAttribute("billdesc", billdesc);
            request.setAttribute("billdate", billdate);
            aqreportFormBean.setPay(pay);
            aqreportFormBean.setDp(dp);
            aqreportFormBean.setDa(da);
            aqreportFormBean.setHra(hra);
            aqreportFormBean.setOa(oa);
            int totAbstract = Integer.parseInt(pay) + Integer.parseInt(dp) + Integer.parseInt(da) + Integer.parseInt(hra) + Integer.parseInt(oa);
            aqreportFormBean.setTotAbstract(totAbstract);
            //oa=aqReportDAO.getOtherAllowance(billNo, month, year);
            //request.setAttribute("oa",oa+"");

            model.addAttribute("column9NameList", column9NameList);
            model.addAttribute("column10NameList", column10NameList);
            model.addAttribute("column11NameList", column11NameList);
            model.addAttribute("column12NameList", column12NameList);
            model.addAttribute("column13NameList", column13NameList);
            model.addAttribute("column14NameList", column14NameList);
            model.addAttribute("column15NameList", column15NameList);
            model.addAttribute("column16NameList", column16NameList);
            model.addAttribute("column17NameList", column17NameList);
            model.addAttribute("column18NameList", column18NameList);
            model.addAttribute("dedAbstractList", dedAbstractList);

            model.addAttribute("vchno", crb.getVchNo());
            model.addAttribute("vchdate", crb.getVchDate());

            request.setAttribute("aqBillReport", aqreportFormBean);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String path = "";

        if (empType.equals("R")||empType.equals("A")) {
            if (format != null && format.equals("f2")) {
                path = "payroll/AQReport/AQ_Bill1";
            } else if (format != null && format.equals("f1")) {
                path = "payroll/AQReport/AQ_Bill";
            }
        } else if (empType.equals("C") || empType.equals("B")) {
            if (format != null && format.equals("f1")) {
                path = "payroll/AQReport/AQ_Bill_Contractual";
            } else if (format != null && format.equals("f2")) {
                path = "payroll/AQReport/AQ_Bill_ContractualF2";
            }
        } else if (empType.equals("S")) {
            if (format != null && format.equals("f1")) {
                path = "payroll/AQReport/AQ_Bill_Contractual";
            } else if (format != null && format.equals("f2")) {
                path = "payroll/AQReport/AQ_Bill_ContractualF2";
            }
        } else if (empType.equals("D")) {
            if (format != null && format.equals("f2")) {
                path = "payroll/AQReport/AQ_Bill2";
            } else if (format != null && format.equals("f1")) {
                path = "payroll/AQReport/AQ_Bill";
            }
        }

        return path;

    }

    @RequestMapping(value = "AqReportExcel")
    public void AqReportExcel(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("billNo") String billNo
    ) {

        response.setContentType("application/vnd.ms-excel");
        OutputStream out = null;

        try {
            String fileName = "AQReport_" + billNo + ".xls";
            out = new BufferedOutputStream(response.getOutputStream());

            WritableWorkbook workbook = Workbook.createWorkbook(out);

            aqReportDAO.downloadAqReportExcel(out, fileName, workbook, billNo);

            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    @RequestMapping(value = "aqbillreportPdf.htm", method = RequestMethod.GET)
    public void aqBillReportPDF(HttpServletResponse response, ModelMap model,
            @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @RequestParam("billNo") String billNo,
            @RequestParam("format") String format,
            @ModelAttribute("AqreportBean") AqreportBean aqreportFormBean,
            @ModelAttribute("SelectedEmpOffice") String selectedEmpOffice,
            BindingResult result,
            HttpServletRequest request
    ) {

        String column9NameList = null;
        String column10NameList = null;
        String column11NameList = null;
        String column12NameList = null;
        String column13NameList = null;
        String column14NameList = null;
        String column15NameList = null;
        String column16NameList = null;
        String column17NameList = "";
        String column18NameList = null;

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
        response.setContentType("application/pdf");
        Document document = new Document(PageSize.A3);
        document.setMargins(10, 10, 10, 10);
        try {

            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            CommonReportParamBean crb = aqReportDAO.getBillDetails(billNo);

           Map<String, List> colNameList = aqReportDAO.getAllColumnNameList(billNo, Integer.toString(crb.getAqmonth()), Integer.toString(crb.getAqyear()));

            if (colNameList.containsKey("9")) {

                Iterator itr = colNameList.get("9").iterator();
                while (itr.hasNext()) {
                    if (column9NameList != null && !column9NameList.equals("")) {
                        column9NameList = column9NameList + "/</br>" + (String) itr.next();
                    } else {
                        column9NameList = (String) itr.next();
                    }
                }
            } else {
                column9NameList = "LIC/<br/>PLI";
            }

            if (colNameList.containsKey("10")) {

                Iterator itr = colNameList.get("10").iterator();
                while (itr.hasNext()) {
                    if (column10NameList != null && !column10NameList.equals("")) {
                        column10NameList = column10NameList + "/</br>" + (String) itr.next();
                    } else {
                        column10NameList = (String) itr.next();
                    }
                }
            } else {
                column10NameList = "GPF/CPF/TPF<br/>DA-GPF<br/>RECOVERY";
            }

            if (colNameList.containsKey("11")) {

                Iterator itr = colNameList.get("11").iterator();
                while (itr.hasNext()) {
                    if (column11NameList != null && !column11NameList.equals("")) {
                        column11NameList = column11NameList + "/</br>" + (String) itr.next();
                    } else {
                        column11NameList = (String) itr.next();
                    }
                }
            } else {
                column11NameList = "P.TAX<br/>I.TAX";
            }

            if (colNameList.containsKey("12")) {

                Iterator itr = colNameList.get("12").iterator();
                while (itr.hasNext()) {
                    if (column12NameList != null && !column12NameList.equals("")) {
                        column12NameList = column12NameList + "/</br>" + (String) itr.next();
                    } else {
                        column12NameList = (String) itr.next();
                    }
                }
            } else {
                column12NameList = "HRR<br/>WATER TAX<br/>SWG<br/>HIRE CHG";
            }

            if (colNameList.containsKey("13")) {

                Iterator itr = colNameList.get("13").iterator();
                while (itr.hasNext()) {
                    if (column13NameList != null && !column13NameList.equals("")) {
                        column13NameList = column13NameList + "/</br>" + (String) itr.next();
                    } else {
                        column13NameList = (String) itr.next();
                    }
                }
            } else {
                column13NameList = "HB<br/> INT HB<br/>SPL HB<br/>INT SPL HB <br />";
            }

            if (colNameList.containsKey("14")) {

                Iterator itr = colNameList.get("14").iterator();
                while (itr.hasNext()) {
                    if (column14NameList != null && !column14NameList.equals("")) {
                        column14NameList = column14NameList + "/ </br>" + (String) itr.next();
                    } else {
                        column14NameList = (String) itr.next();
                    }
                }
            } else {
                column14NameList = "MC<br/>INT MC<br/>MC/MOP ADV<br/>INT MC/MOPED";
            }

            if (colNameList.containsKey("15")) {

                Iterator itr = colNameList.get("15").iterator();
                while (itr.hasNext()) {
                    if (column15NameList != null && !column15NameList.equals("")) {
                        column15NameList = column15NameList + "/ </br>" + (String) itr.next();
                    } else {
                        column15NameList = (String) itr.next();
                    }
                }
            } else {
                column15NameList = "CAR ADV<br/>INT CAR<br/>BI-CYCLE<br />INT CYCL";
            }

            if (colNameList.containsKey("16")) {

                Iterator itr = colNameList.get("16").iterator();
                while (itr.hasNext()) {
                    if (column16NameList != null && !column16NameList.equals("")) {
                        column16NameList = column16NameList + "/ </br>" + (String) itr.next();
                    } else {
                        column16NameList = (String) itr.next();
                    }
                }
            } else {
                column16NameList = "PAY ADV<br />MED ADV<br/>TRADE ADV<br/>OVDL";
            }

            if (colNameList.containsKey("17")) {

                Iterator itr = colNameList.get("17").iterator();
                while (itr.hasNext()) {
                    if (column17NameList != null && !column17NameList.equals("")) {
                        column17NameList = column17NameList + "/ </br>" + (String) itr.next();
                    } else {
                        column17NameList = (String) itr.next();
                    }
                }
            } else {
                column17NameList = "FEST<br/>NPS ARR.<br/>EX. PAY<br />RTI<br />AUDR";
            }

            if (colNameList.containsKey("18")) {

                Iterator itr = colNameList.get("18").iterator();
                while (itr.hasNext()) {
                    if (column18NameList != null && !column18NameList.equals("")) {
                        column18NameList = column18NameList + "/ </br>" + (String) itr.next();
                    } else {
                        column18NameList = (String) itr.next();
                    }
                }
            } else {
                column18NameList = "OTHER<br/>RECOVERY </br> GIS ADV </br>AIS GIS</br>COMP ADV";
            }

            year = crb.getAqyear() + "";
            month = crb.getAqmonth() + "";
            billdesc = crb.getBilldesc();
            billdate = crb.getBilldate();
            empType = aqReportDAO.getEmpType(billNo, month, year);
            BillConfigObj billConfig = aqReportDAO.getBillConfig(billNo);
            if (format != null && format.equals("f1")) {
                aqlist = aqReportDAO.getSectionWiseBillDtls(billNo, month, year, "f1", billConfig, empType, column9NameList, column10NameList, column11NameList, column12NameList, column13NameList, column14NameList, column15NameList, column16NameList, column17NameList, column18NameList);
            } else if (format != null && format.equals("f2")) {
                aqlist = aqReportDAO.getSectionWiseBillDtls(billNo, month, year, "f2", billConfig, empType, column9NameList, column10NameList, column11NameList, column12NameList, column13NameList, column14NameList, column15NameList, column16NameList, column17NameList, column18NameList);
            }
            aqreportFormBean.setAqlist(aqlist);

            aqreportFormBean.setOffen(crb.getOfficeen());
            aqreportFormBean.setDept(crb.getDeptname());
            aqreportFormBean.setDistrict(crb.getDistrict());
            aqreportFormBean.setState(crb.getStatename());
            aqreportFormBean.setMonth(aqReportDAO.getMonth(Integer.parseInt(month)));
            aqreportFormBean.setYear(year);
            aqreportFormBean.setBilldesc(billdesc);
            aqreportFormBean.setBilldate(billdate);
            int col3Tot = aqReportDAO.getColGrandTotal(aqlist, "col3", "BASIC", null) + aqReportDAO.getColGrandTotal(aqlist, "col3", "SP", null) + aqReportDAO.getColGrandTotal(aqlist, "col3", "GP", null) + aqReportDAO.getColGrandTotal(aqlist, "col3", "IR", null);
            int col4Tot = aqReportDAO.getColGrandTotal(aqlist, "col4", "GP", null) + aqReportDAO.getColGrandTotal(aqlist, "col4", "PPAY", null);
            int col5Tot = aqReportDAO.getColGrandTotal(aqlist, "col5", 0, null) + aqReportDAO.getColGrandTotal(aqlist, "col5", 1, null);
            int col6Tot = aqReportDAO.getColGrandTotal(aqlist, "col6", "HRA", null) + aqReportDAO.getColGrandTotal(aqlist, "col6", "ADLHRA", null) + aqReportDAO.getColGrandTotal(aqlist, "col6", "LFQ", null) + aqReportDAO.getColGrandTotal(aqlist, "col6", "RAFAL", null);
            //int col7Tot = aqReportDAO.getColGrandTotal(aqlist, "col7", 0, null) + aqReportDAO.getColGrandTotal(aqlist, "col7", 1, null) + aqReportDAO.getColGrandTotal(aqlist, "col7", 2, null) + aqReportDAO.getColGrandTotal(aqlist, "col7", 3, null) + aqReportDAO.getColGrandTotal(aqlist, "col7", 4, null) + aqReportDAO.getColGrandTotal(aqlist, "col7", 5, null) + aqReportDAO.getColGrandTotal(aqlist, "col7", 6, null) + aqReportDAO.getColGrandTotal(aqlist, "col7", 7, null);
            int col8Tot = aqReportDAO.getColGrandTotal(aqlist, "col8", "GROSS PAY", null);
            int col9Tot = aqReportDAO.getColGrandTotal(aqlist, "col9", "LIC", null) + aqReportDAO.getColGrandTotal(aqlist, "col9", "PLI", null);
            int col10Tot = aqReportDAO.getColGrandTotal(aqlist, "col10", "CPF", null) + aqReportDAO.getColGrandTotal(aqlist, "col10", "GPF", null) + aqReportDAO.getColGrandTotal(aqlist, "col10", "TPF", null) + aqReportDAO.getColGrandTotal(aqlist, "col10", "GA", null) + aqReportDAO.getColGrandTotal(aqlist, "col10", "TPFGA", null) + aqReportDAO.getColGrandTotal(aqlist, "col10", "GPDD", null) + aqReportDAO.getColGrandTotal(aqlist, "col10", "GPIR", null);
            int col11Tot = aqReportDAO.getColGrandTotal(aqlist, "col11", "PT", null) + aqReportDAO.getColGrandTotal(aqlist, "col11", "IT", null);
            int col12Tot = aqReportDAO.getColGrandTotal(aqlist, "col12", "HRR", null) + aqReportDAO.getColGrandTotal(aqlist, "col12", "WRR", null) + aqReportDAO.getColGrandTotal(aqlist, "col12", "SWR", null) + aqReportDAO.getColGrandTotal(aqlist, "col12", "HC", null);
            int col13Tot = aqReportDAO.getColGrandTotal(aqlist, "col13", "HBA", "P") + aqReportDAO.getColGrandTotal(aqlist, "col13", "HBA", "I") + aqReportDAO.getColGrandTotal(aqlist, "col13", "SHBA", "P") + aqReportDAO.getColGrandTotal(aqlist, "col13", "SHBA", "I");
            int col14Tot = aqReportDAO.getColGrandTotal(aqlist, "col14", "MCA", "P") + aqReportDAO.getColGrandTotal(aqlist, "col14", "MCA", "I") + aqReportDAO.getColGrandTotal(aqlist, "col14", "MOPA", "P") + aqReportDAO.getColGrandTotal(aqlist, "col14", "MOPA", "I");
            int col15Tot = aqReportDAO.getColGrandTotal(aqlist, "col15", "VE", "P") + aqReportDAO.getColGrandTotal(aqlist, "col15", "VE", "I") + aqReportDAO.getColGrandTotal(aqlist, "col15", "BI", "P") + aqReportDAO.getColGrandTotal(aqlist, "col15", "BI", "I");
            int col16Tot = aqReportDAO.getColGrandTotal(aqlist, "col16", "PAY", null) + aqReportDAO.getColGrandTotal(aqlist, "col16", "MED", null) + aqReportDAO.getColGrandTotal(aqlist, "col16", "TRADE", null) + aqReportDAO.getColGrandTotal(aqlist, "col16", "OVDL", null);
            int col17Tot = aqReportDAO.getColGrandTotal(aqlist, "col17"); //aqReportDAO.getColGrandTotal(aqlist, "col17", "FA", null) + aqReportDAO.getColGrandTotal(aqlist, "col17", "NPSL", null) + aqReportDAO.getColGrandTotal(aqlist, "col17", "EP", null) + +aqReportDAO.getColGrandTotal(aqlist, "col17", "AUDR", null);
            int col18Tot = aqReportDAO.getColGrandTotal(aqlist, "col18", "OR", null) + aqReportDAO.getColGrandTotal(aqlist, "col18", "GISA", "P") + aqReportDAO.getColGrandTotal(aqlist, "col18", "GIS", null) + aqReportDAO.getColGrandTotal(aqlist, "col18", "CMPA", null);
            if (format != null && format.equals("f1")) {
                col19Tot = aqReportDAO.getColGrandTotal(aqlist, "col19", "TOTDEN", null);
                col20Tot = aqReportDAO.getColGrandTotal(aqlist, "col20", "NETPAY", null);
                netPay = Numtowordconvertion.convertNumber(aqReportDAO.getColGrandTotal(aqlist, "col20", "NETPAY", null));
            } else if (format != null && format.equals("f2")) {
                col19Tot = aqReportDAO.getColGrandTotal(aqlist, "col19", "TOTDEN", null);
                col191Tot = aqReportDAO.getColGrandTotal(aqlist, "col19", "NETPAY", null);

                col20Tot = aqReportDAO.getColGrandTotal(aqlist, "col20", "PVTDED", null);
                col201Tot = aqReportDAO.getColGrandTotal(aqlist, "col20", "BANKLOAN", null);
                col202Tot = aqReportDAO.getColGrandTotal(aqlist, "col20", "NETBALANCE", null);
                netPay = Numtowordconvertion.convertNumber(aqReportDAO.getColGrandTotal(aqlist, "col20", "PVTDED", null) + aqReportDAO.getColGrandTotal(aqlist, "col20", "BANKLOAN", null) + aqReportDAO.getColGrandTotal(aqlist, "col20", "NETBALANCE", null));
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
            int totHrr = aqReportDAO.getColGrandTotal(aqlist, "col12", "HRR", null);
            int totHbaPri = aqReportDAO.getColGrandTotal(aqlist, "col13", "HBA", "P");
            int totShbaPri = aqReportDAO.getColGrandTotal(aqlist, "col13", "SHBA", "P");
            int totMcaPri = aqReportDAO.getColGrandTotal(aqlist, "col14", "MCA", "P") + aqReportDAO.getColGrandTotal(aqlist, "col14", "MOPA", "P");
            int totShbaInt = aqReportDAO.getColGrandTotal(aqlist, "col13", "SHBA", "I");
            int totBicycPri = aqReportDAO.getColGrandTotal(aqlist, "col15", "BI", "P");
            int totPt = aqReportDAO.getColGrandTotal(aqlist, "col11", "PT", null);
            int totGisaPri = aqReportDAO.getColGrandTotal(aqlist, "col18", "GISA", "P");
            int totIt = aqReportDAO.getColGrandTotal(aqlist, "col11", "IT", null);
            int totGpf = aqReportDAO.getColGrandTotal(aqlist, "col10", "GPF", null) + aqReportDAO.getColGrandTotal(aqlist, "col10", "GA", null) + aqReportDAO.getColGrandTotal(aqlist, "col10", "GPDD", null) + aqReportDAO.getColGrandTotal(aqlist, "col10", "GPIR", null);
            int totMopInt = aqReportDAO.getColGrandTotal(aqlist, "col14", "MCA", "I") + aqReportDAO.getColGrandTotal(aqlist, "col14", "MOPA", "I");
            int totHc = aqReportDAO.getColGrandTotal(aqlist, "col12", "HC", null);
            int totHbaInt = aqReportDAO.getColGrandTotal(aqlist, "col13", "HBA", "I");
            int totGis = aqReportDAO.getColGrandTotal(aqlist, "col18", "GIS", null);
            int totVehicleInt = aqReportDAO.getColGrandTotal(aqlist, "col15", "VE", "I");
            int totCpf = aqReportDAO.getColGrandTotal(aqlist, "col10", "CPF", null);
            int totPa = aqReportDAO.getColGrandTotal(aqlist, "col16", "PA", null);
            int totCgegis = aqReportDAO.getColGrandTotal(aqlist, "col18", "CGEGIS", null);
            int totVehiclePri = aqReportDAO.getColGrandTotal(aqlist, "col15", "VE", "P");
            int totTpf = aqReportDAO.getColGrandTotal(aqlist, "col10", "TPF", null);
            int totTfga = aqReportDAO.getColGrandTotal(aqlist, "col10", "TPFGA", null);
            int totTlci = aqReportDAO.getColGrandTotal(aqlist, "col9", "TLIC", null);
            int totWrr = aqReportDAO.getColGrandTotal(aqlist, "col12", "WRR", null);
            int totSwr = aqReportDAO.getColGrandTotal(aqlist, "col12", "SWR", null);
            int totCc = aqReportDAO.getColGrandTotal(aqlist, "col18", "CC", null);
            int totCmpa = aqReportDAO.getColGrandTotal(aqlist, "col18", "CMPA", null);
            int totOvdl = aqReportDAO.getColGrandTotal(aqlist, "col16", "OVDL", null);
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
            HashMap payAbstract = aqReportDAO.getPayAbstract(aqlist);
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

            //String allowEsign = comonScheduleDao.allowedOfficeEsign(lub.getLoginoffcode());
            String allowEsign = comonScheduleDao.allowedOfficeEsign(selectedEmpOffice);

            String[] rowArray = allowEsign.split("\\|");
            String allowEsignStatus = rowArray[0];
            String createpathPDF = "";
            String unsignedPdfFilename = "";
            if (allowEsignStatus.equals("Y")) {
                createpathPDF = esignBillDao.GeneratePDFSignedPath(lub.getLoginoffcode(), crb.getAqyear(), crb.getAqmonth(), billNo);
                //   System.out.println("createpathPDF==="+createpathPDF);
                unsignedPdfFilename = "AQUITANCEREPORT_" + billNo + "_unsigned.pdf";
                PdfWriter.getInstance(document, new FileOutputStream(createpathPDF + File.separator + unsignedPdfFilename));
                document.open();
            }
            /* if (billNo != null && !aqreportFormBean.getSlNo().equals("") && allowEsignStatus.equals("Y")) {
             esignBillDao.savepdflog(unsignedPdfFilename, createpathPDF, aqreportFormBean.getSlNo(), aqreportFormBean.getBillNo(), crb.getAqmonth(), crb.getAqyear(), lub.getLoginempid(), lub.getLoginoffcode());
             }*/
            aqReportDAO.generateAqReportPDF(document, billNo, crb, aqreportFormBean);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

    }
    
   @RequestMapping(value = "aqbillreportOfcWise.htm", method = RequestMethod.GET)
    public String aqbillreportOfcWiseHTML(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, 
            @ModelAttribute("CommonReportParamBean") CommonReportParamBean crb, @ModelAttribute("AqreportBean") AqreportBean aqreportFormBean, 
            BindingResult result, HttpServletRequest request, Map<String, Object> modl)  throws IOException{

        ArrayList aqlist = new ArrayList();
        String year = "";
        String month = "";
        String billdesc = "";
        String billdate = "";
        String empType = "";
        String billNo = aqreportFormBean.getBillNo();
        String format = aqreportFormBean.getFormat();
        int col19Tot = 0;
        int col20Tot = 0;
        int col191Tot = 0;
        int col201Tot = 0;
        int col202Tot = 0;
        String netPay = "";
        int ofcounter = 0;

        String column9NameList = null;
        String column10NameList = null;
        String column11NameList = null;
        String column12NameList = null;
        String column13NameList = null;
        String column14NameList = null;
        String column15NameList = null;
        String column16NameList = null;
        String column17NameList = "";
        String column18NameList = null;
        SectionWiseAqBean sbean=null;

        try {
            
            
            empType = aqReportDAO.getEmpType(billNo, month, year);
            String selectOfficeCode=request.getParameter("offcode");

            // For DHE (Start)---            
            if (empType.equals("D")) {
                crb = aqReportDAO.getBillDetailsDHE(billNo);
            } //--End--
            else {
                System.out.println("billHtml");

                crb = aqReportDAO.getBillDetails(billNo);
            }

            year = crb.getAqyear() + "";
            month = crb.getAqmonth() + "";
            billdesc = crb.getBilldesc();
            billdate = crb.getBilldate();

            BillConfigObj billConfig = aqReportDAO.getBillConfig(billNo);

            Map<String, List> colNameList = aqReportDAO.getAllColumnNameList(billNo, month, year);

            if (colNameList.containsKey("9")) {

                Iterator itr = colNameList.get("9").iterator();
                while (itr.hasNext()) {
                    if (column9NameList != null && !column9NameList.equals("")) {
                        column9NameList = column9NameList + "/</br>" + (String) itr.next();
                    } else {
                        column9NameList = (String) itr.next();
                    }
                }
            } else {
                column9NameList = "LIC/<br/>PLI";
            }

            if (colNameList.containsKey("10")) {

                Iterator itr = colNameList.get("10").iterator();
                while (itr.hasNext()) {
                    if (column10NameList != null && !column10NameList.equals("")) {
                        column10NameList = column10NameList + "/</br>" + (String) itr.next();
                    } else {
                        column10NameList = (String) itr.next();
                    }
                }
            } else {
                column10NameList = "GPF/CPF/TPF<br/>DA-GPF<br/>RECOVERY";
            }

            if (colNameList.containsKey("11")) {

                Iterator itr = colNameList.get("11").iterator();
                while (itr.hasNext()) {
                    if (column11NameList != null && !column11NameList.equals("")) {
                        column11NameList = column11NameList + "/</br>" + (String) itr.next();
                    } else {
                        column11NameList = (String) itr.next();
                    }
                }
            } else {
                column11NameList = "P.TAX<br/>I.TAX";
            }

            if (colNameList.containsKey("12")) {

                Iterator itr = colNameList.get("12").iterator();
                while (itr.hasNext()) {
                    if (column12NameList != null && !column12NameList.equals("")) {
                        column12NameList = column12NameList + "/</br>" + (String) itr.next();
                    } else {
                        column12NameList = (String) itr.next();
                    }
                }
            } else {
                column12NameList = "HRR<br/>WATER TAX<br/>SWG<br/>HIRE CHG";
            }

            if (colNameList.containsKey("13")) {

                Iterator itr = colNameList.get("13").iterator();
                while (itr.hasNext()) {
                    if (column13NameList != null && !column13NameList.equals("")) {
                        column13NameList = column13NameList + "/</br>" + (String) itr.next();
                    } else {
                        column13NameList = (String) itr.next();
                    }
                }
            } else {
                column13NameList = "HB<br/> INT HB<br/>SPL HB<br/>INT SPL HB <br />";
            }

            if (colNameList.containsKey("14")) {

                Iterator itr = colNameList.get("14").iterator();
                while (itr.hasNext()) {
                    if (column14NameList != null && !column14NameList.equals("")) {
                        column14NameList = column14NameList + "/ </br>" + (String) itr.next();
                    } else {
                        column14NameList = (String) itr.next();
                    }
                }
            } else {
                column14NameList = "MC<br/>INT MC<br/>MC/MOP ADV<br/>INT MC/MOPED";
            }

            if (colNameList.containsKey("15")) {

                Iterator itr = colNameList.get("15").iterator();
                while (itr.hasNext()) {
                    if (column15NameList != null && !column15NameList.equals("")) {
                        column15NameList = column15NameList + "/ </br>" + (String) itr.next();
                    } else {
                        column15NameList = (String) itr.next();
                    }
                }
            } else {
                column15NameList = "CAR ADV<br/>INT CAR<br/>BI-CYCLE<br />INT CYCL";
            }

            if (colNameList.containsKey("16")) {

                Iterator itr = colNameList.get("16").iterator();
                while (itr.hasNext()) {
                    if (column16NameList != null && !column16NameList.equals("")) {
                        column16NameList = column16NameList + "/ </br>" + (String) itr.next();
                    } else {
                        column16NameList = (String) itr.next();
                    }
                }
            } else {
                column16NameList = "PAY ADV<br />MED ADV<br/>TRADE ADV<br/>OVDL";
            }

            if (colNameList.containsKey("17")) {

                Iterator itr = colNameList.get("17").iterator();
                while (itr.hasNext()) {
                    if (column17NameList != null && !column17NameList.equals("")) {
                        column17NameList = column17NameList + "/ </br>" + (String) itr.next();
                    } else {
                        column17NameList = (String) itr.next();
                    }
                }
            } else {
                column17NameList = "FEST<br/>NPS ARR.<br/>EX. PAY<br />RTI<br />AUDR";
            }

            if (colNameList.containsKey("18")) {

                Iterator itr = colNameList.get("18").iterator();
                while (itr.hasNext()) {
                    if (column18NameList != null && !column18NameList.equals("")) {
                        column18NameList = column18NameList + "/ </br>" + (String) itr.next();
                    } else {
                        column18NameList = (String) itr.next();
                    }
                }
            } else {
                column18NameList = "OTHER<br/>RECOVERY </br> GIS ADV </br>AIS GIS</br>COMP ADV";
            }

            List dedAbstractList = aqReportDAO.getDeductionGrandAbstract(billNo, month, year);

           if (format != null && format.equals("f2")) {
                //-- For DHE Condition is empType.equals("D")----
                if (empType.equals("D")) {
                    aqlist = aqReportDAO.getSectionWiseBillDtlsDHE(billNo, month, year, "f2",selectOfficeCode, billConfig, empType, column9NameList, column10NameList, column11NameList, column12NameList, column13NameList, column14NameList, column15NameList, column16NameList, column17NameList, column18NameList);
                }

            }
            //System.out.println("size:" + aqlist.size());

            aqreportFormBean.setAqlist(aqlist);
            
           
            //---------------------DHE------------------------

            aqreportFormBean.setOffen(crb.getOfficeen());
            aqreportFormBean.setDept(crb.getDeptname());
            aqreportFormBean.setDistrict(crb.getDistrict());
            aqreportFormBean.setState(crb.getStatename());
            aqreportFormBean.setMonth(aqReportDAO.getMonth(Integer.parseInt(month)));
            aqreportFormBean.setYear(year);
            aqreportFormBean.setBilldesc(billdesc);
            aqreportFormBean.setBilldate(billdate);
            int col3Tot = aqReportDAO.getColGrandTotal(aqlist, "col3", "BASIC", null) + aqReportDAO.getColGrandTotal(aqlist, "col3", "SP", null) + aqReportDAO.getColGrandTotal(aqlist, "col3", "GP", null) + aqReportDAO.getColGrandTotal(aqlist, "col3", "IR", null);
            int col4Tot = aqReportDAO.getColGrandTotal(aqlist, "col4", "GP", null) + aqReportDAO.getColGrandTotal(aqlist, "col4", "PPAY", null);
            int col5Tot = aqReportDAO.getColGrandTotal(aqlist, "col5", 0, null) + aqReportDAO.getColGrandTotal(aqlist, "col5", 1, null);
            int col6Tot = aqReportDAO.getColGrandTotal(aqlist, "col6", "HRA", null) + aqReportDAO.getColGrandTotal(aqlist, "col6", "ADLHRA", null) + aqReportDAO.getColGrandTotal(aqlist, "col6", "LFQ", null) + aqReportDAO.getColGrandTotal(aqlist, "col6", "RAFAL", null);
            //int col7Tot = aqReportDAO.getColGrandTotal(aqlist, "col7", 0, null) + aqReportDAO.getColGrandTotal(aqlist, "col7", 1, null) + aqReportDAO.getColGrandTotal(aqlist, "col7", 2, null) + aqReportDAO.getColGrandTotal(aqlist, "col7", 3, null) + aqReportDAO.getColGrandTotal(aqlist, "col7", 4, null) + aqReportDAO.getColGrandTotal(aqlist, "col7", 5, null) + aqReportDAO.getColGrandTotal(aqlist, "col7", 6, null) + aqReportDAO.getColGrandTotal(aqlist, "col7", 7, null);
            int col8Tot = aqReportDAO.getColGrandTotal(aqlist, "col8", "GROSS PAY", null);
            int col9Tot = aqReportDAO.getColGrandTotal(aqlist, "col9");

            //aqReportDAO.getColGrandTotal(aqlist, "col9", "LIC", null) + aqReportDAO.getColGrandTotal(aqlist, "col9", "PLI", null);
            int col10Tot = aqReportDAO.getColGrandTotal(aqlist, "col10");

            //aqReportDAO.getColGrandTotal(aqlist, "col10", "CPF", null) + aqReportDAO.getColGrandTotal(aqlist, "col10", "GPF", null) + aqReportDAO.getColGrandTotal(aqlist, "col10", "TPF", null) + aqReportDAO.getColGrandTotal(aqlist, "col10", "GA", null) + aqReportDAO.getColGrandTotal(aqlist, "col10", "TPFGA", null) + aqReportDAO.getColGrandTotal(aqlist, "col10", "GPDD", null) + aqReportDAO.getColGrandTotal(aqlist, "col10", "GPIR", null);
            int col11Tot = aqReportDAO.getColGrandTotal(aqlist, "col11");

            //aqReportDAO.getColGrandTotal(aqlist, "col11", "PT", null) + aqReportDAO.getColGrandTotal(aqlist, "col11", "IT", null);
            int col12Tot = aqReportDAO.getColGrandTotal(aqlist, "col12");

            //aqReportDAO.getColGrandTotal(aqlist, "col12", "HRR", null) + aqReportDAO.getColGrandTotal(aqlist, "col12", "WRR", null) + aqReportDAO.getColGrandTotal(aqlist, "col12", "SWR", null) + aqReportDAO.getColGrandTotal(aqlist, "col12", "HC", null);
            int col13Tot = aqReportDAO.getColGrandTotal(aqlist, "col13");

            //aqReportDAO.getColGrandTotal(aqlist, "col13", "HBA", "P") + aqReportDAO.getColGrandTotal(aqlist, "col13", "HBA", "I") + aqReportDAO.getColGrandTotal(aqlist, "col13", "SHBA", "P") + aqReportDAO.getColGrandTotal(aqlist, "col13", "SHBA", "I");
            int col14Tot = aqReportDAO.getColGrandTotal(aqlist, "col14");

            //aqReportDAO.getColGrandTotal(aqlist, "col14", "MCA", "P") + aqReportDAO.getColGrandTotal(aqlist, "col14", "MCA", "I") + aqReportDAO.getColGrandTotal(aqlist, "col14", "MOPA", "P") + aqReportDAO.getColGrandTotal(aqlist, "col14", "MOPA", "I");
            int col15Tot = aqReportDAO.getColGrandTotal(aqlist, "col15");

            //aqReportDAO.getColGrandTotal(aqlist, "col15", "VE", "P") + aqReportDAO.getColGrandTotal(aqlist, "col15", "VE", "I") + aqReportDAO.getColGrandTotal(aqlist, "col15", "BI", "P") + aqReportDAO.getColGrandTotal(aqlist, "col15", "BI", "I");
            int col16Tot = aqReportDAO.getColGrandTotal(aqlist, "col16");

            //aqReportDAO.getColGrandTotal(aqlist, "col16", "PAY", null) + aqReportDAO.getColGrandTotal(aqlist, "col16", "MED", null) + aqReportDAO.getColGrandTotal(aqlist, "col16", "TRADE", null) + aqReportDAO.getColGrandTotal(aqlist, "col16", "OVDL", null) + aqReportDAO.getColGrandTotal(aqlist, "col16", "PA", null);
            int col17Tot = aqReportDAO.getColGrandTotal(aqlist, "col17");

            //int col17Tot = aqReportDAO.getColGrandTotal(aqlist, "col17", "FA", null) + aqReportDAO.getColGrandTotal(aqlist, "col17", "NPSL", null) + aqReportDAO.getColGrandTotal(aqlist, "col17", "EP", null) + +aqReportDAO.getColGrandTotal(aqlist, "col17", "AUDR", null) + aqReportDAO.getColGrandTotal(aqlist, "col17", "HRA_EP", null);
            int col18Tot = aqReportDAO.getColGrandTotal(aqlist, "col18");

            //aqReportDAO.getColGrandTotal(aqlist, "col18", "OR", null) + aqReportDAO.getColGrandTotal(aqlist, "col18", "GISA", "P") + aqReportDAO.getColGrandTotal(aqlist, "col18", "GIS", null) + aqReportDAO.getColGrandTotal(aqlist, "col18", "CMPA", null);
            if (format != null && format.equals("f1")) {
                col19Tot = aqReportDAO.getColGrandTotal(aqlist, "col19", "TOTDEN", null);
                col20Tot = aqReportDAO.getColGrandTotal(aqlist, "col20", "NETPAY", null);
                netPay = Numtowordconvertion.convertNumber(aqReportDAO.getColGrandTotal(aqlist, "col20", "NETPAY", null));
            } else if (format != null && format.equals("f2")) {
                col19Tot = aqReportDAO.getColGrandTotal(aqlist, "col19", "TOTDEN", null);
                col191Tot = aqReportDAO.getColGrandTotal(aqlist, "col19", "NETPAY", null);

                col20Tot = aqReportDAO.getColGrandTotal(aqlist, "col20", "PVTDED", null);
                col201Tot = aqReportDAO.getColGrandTotal(aqlist, "col20", "BANKLOAN", null);
                col202Tot = aqReportDAO.getColGrandTotal(aqlist, "col20", "NETBALANCE", null);
                netPay = Numtowordconvertion.convertNumber(aqReportDAO.getColGrandTotal(aqlist, "col20", "PVTDED", null) + aqReportDAO.getColGrandTotal(aqlist, "col20", "BANKLOAN", null) + aqReportDAO.getColGrandTotal(aqlist, "col20", "NETBALANCE", null));
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

            HashMap payAbstract = aqReportDAO.getPayAbstract(aqlist);
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
            String offname = aqreportFormBean.getOffen();
            String deptname = aqreportFormBean.getDept();
            String distname = aqreportFormBean.getDistrict();
            String statename = aqreportFormBean.getState();

            request.setAttribute("offname", offname);
            request.setAttribute("deptname", deptname);
            request.setAttribute("distname", distname);
            request.setAttribute("statename", statename);
            request.setAttribute("billdesc", billdesc);
            request.setAttribute("billdate", billdate);
            aqreportFormBean.setPay(pay);
            aqreportFormBean.setDp(dp);
            aqreportFormBean.setDa(da);
            aqreportFormBean.setHra(hra);
            aqreportFormBean.setOa(oa);
            int totAbstract = Integer.parseInt(pay) + Integer.parseInt(dp) + Integer.parseInt(da) + Integer.parseInt(hra) + Integer.parseInt(oa);
            aqreportFormBean.setTotAbstract(totAbstract);
            //oa=aqReportDAO.getOtherAllowance(billNo, month, year);
            //request.setAttribute("oa",oa+"");

            model.addAttribute("column9NameList", column9NameList);
            model.addAttribute("column10NameList", column10NameList);
            model.addAttribute("column11NameList", column11NameList);
            model.addAttribute("column12NameList", column12NameList);
            model.addAttribute("column13NameList", column13NameList);
            model.addAttribute("column14NameList", column14NameList);
            model.addAttribute("column15NameList", column15NameList);
            model.addAttribute("column16NameList", column16NameList);
            model.addAttribute("column17NameList", column17NameList);
            model.addAttribute("column18NameList", column18NameList);
            model.addAttribute("dedAbstractList", dedAbstractList);

            model.addAttribute("vchno", crb.getVchNo());
            model.addAttribute("vchdate", crb.getVchDate());

            request.setAttribute("aqBillReport", aqreportFormBean);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String path = "";

       if (empType.equals("D")) {
            if (format != null && format.equals("f2")) {
                path = "payroll/AQReport/AQ_Bill2";
            } else if (format != null && format.equals("f1")) {
                path = "payroll/AQReport/AQ_Bill";
            }
        }

        return path;

    }

}
