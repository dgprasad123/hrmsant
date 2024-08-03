package hrms.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import hrms.common.CommonFunctions;
import hrms.dao.master.TreasuryDAO;
import hrms.dao.payroll.billbrowser.BillBrowserDAO;
import hrms.dao.payroll.schedule.PayBillDMPDAO;
import hrms.dao.payroll.tpschedule.TPFScheduleDAO;
import hrms.model.common.CommonReportParamBean;
import hrms.model.master.Treasury;
import hrms.model.payroll.billbrowser.BillBean;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;

@Service
public class TPFScheduleService implements ServletContextAware{

    @Autowired
    public TPFScheduleDAO tpfScheduleDao;

    @Autowired
    public TreasuryDAO treasuryDao;

    @Autowired
    public BillBrowserDAO billBrowserDao;

    @Autowired
    public PayBillDMPDAO paybillDmpDao;

    private ServletContext context;

    @Override
    public void setServletContext(ServletContext sc) {
        this.context = sc;
    }

    public void createTPFSchedulePDF(int year, int month, String trcode) {
        String filepath = context.getInitParameter("TPFSchedulePDFPath");

        List tpfEmpList = null;
        List tpfAbstract = null;

        PdfWriter writer = null;

        try {
            List trlist = treasuryDao.getAGTreasuryList();

            Document document = null;

            Treasury treasury = null;
            for (int i = 0; i < trlist.size(); i++) {
                treasury = (Treasury) trlist.get(i);

                String foldername = filepath + "/" + year + "/" + CommonFunctions.getMonthAsString(month) + "/" + treasury.getAgtreasuryCode();
                File fileobj = new File(foldername);
                if (!fileobj.exists()) {
                    fileobj.mkdirs();
                }

                ArrayList billList = billBrowserDao.getVoucherListForCOA(year, month, treasury.getAgtreasuryCode());
                System.out.println("billList size is: "+billList.size());
                if (billList != null && billList.size() > 0) {
                    BillBean bb = null;
                    for (int j = 0; j < billList.size(); j++) {
                        bb = (BillBean) billList.get(j);

                        String billNo = bb.getBillno();

                        String innerfoldername = foldername + "/" + billNo + ".pdf";
                        File innerfileobj = new File(innerfoldername);
                        
                        document = new Document(PageSize.A4);
                        writer = PdfWriter.getInstance(document, new FileOutputStream(innerfileobj));

                        CommonReportParamBean crb = paybillDmpDao.getCommonReportParameter(billNo);
                        tpfEmpList = tpfScheduleDao.getEmployeeWiseTPFList(billNo, crb.getAqmonth(), crb.getAqyear());
                        tpfAbstract = tpfScheduleDao.getTPFAbstract(billNo, crb.getAqmonth(), crb.getAqyear());
                        tpfScheduleDao.generateTPFSchedulePDF(innerfoldername, document, billNo, tpfEmpList, tpfAbstract, crb);
                        document.close();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
