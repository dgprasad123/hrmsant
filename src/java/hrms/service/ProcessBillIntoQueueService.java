/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.service;

import hrms.common.DataBaseFunctions;
import hrms.model.payroll.billbrowser.BillBean;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Madhusmita
 */
@Service
public class ProcessBillIntoQueueService {

    @Autowired
    private ServletContext servletContext;

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void processIntoPaybiltask(int month, int year, String processValue, String billgroupid, int billno) {
        Connection con = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement stamt = null;
        List billgrpmappedOfcList = new ArrayList();

        try {
            con = this.dataSource.getConnection();
            if (processValue.equals("All")) {
                ps1 = con.prepareStatement("select paybill_task.task_id,t3.*,bill_mast.bill_no billmast_billno from\n"
                        + "(select bill_group_id,t1.off_code,t1.distname,t1.off_en,sum(noofEmp)cntemp,is_bill_verified,bill_no,getbillgrossamtDHE(bill_no,t1.off_code)Gross, \n"
                        + "getbilldedamtDHE(bill_no,t1.off_code)Ded,(getbillgrossamtDHE(bill_no,t1.off_code)-getbilldedamtDHE(bill_no,t1.off_code))Net from \n"
                        + " (select t2.*,aqm.noofEmp::integer,aqm.is_bill_verified,aqm.bill_no from  \n"
                        + " (select distinct bill_group_id,g_office.off_code,off_en,getdistname(g_office.dist_code)distname from \n"
                        + " (select * from bill_section_mapping where bill_group_id=?)bsm \n"
                        + " inner join g_section on bsm.section_id=g_section.section_id \n"
                        + " inner join g_office on g_section.off_code=g_office.off_code \n"
                        + " inner join (select section_id,count(*)cntemp from section_post_mapping group by section_id)spm  \n"
                        + " on spm.section_id=bsm.section_id)t2  \n"
                        + " left outer join (SELECT bill_no,off_code,AQ_GROUP,is_bill_verified,COUNT(*) noofEmp \n"
                        + " FROM AQ_MAST WHERE AQ_MONTH=? AND AQ_YEAR=? AND AQ_GROUP=?   \n"
                        + " GROUP BY off_code,AQ_GROUP,is_bill_verified,bill_no)aqm \n"
                        + " on aqm.AQ_GROUP=t2.bill_group_id AND aqm.OFF_CODE=t2.OFF_CODE \n"
                        + " order by is_bill_verified desc)t1 \n"
                        + " group by t1.off_code,off_en,is_bill_verified,bill_no,distname,bill_group_id)t3\n"
                        + "left outer join paybill_task on paybill_task.bill_group_id=t3.bill_group_id and paybill_task.off_code=t3.off_code\n"
                        + "left outer join bill_mast on bill_mast.bill_group_id=t3.bill_group_id and bill_mast.aq_month=? and bill_mast.aq_year=?\n"
                        + " order by distname,off_code");
                ps1.setBigDecimal(1, BigDecimal.valueOf(Double.parseDouble(billgroupid)));
                ps1.setInt(2, month);
                ps1.setInt(3, year);
                ps1.setBigDecimal(4, BigDecimal.valueOf(Double.parseDouble(billgroupid)));
                ps1.setInt(5, month);
                ps1.setInt(6, year);
                rs1 = ps1.executeQuery();
                while (rs1.next()) {
                    if ((rs1.getString("cntemp") == null || rs1.getString("cntemp").equals("")) && (rs1.getString("task_id") == null || rs1.getString("task_id").equals(""))) {
                        BillBean bb = new BillBean();
                        bb.setOfficeCode(rs1.getString("off_code"));
                        billgrpmappedOfcList.add(bb);
                    }
                }
            } else {
                BillBean bb1 = new BillBean();
                bb1.setOfficeCode(processValue);
                billgrpmappedOfcList.add(bb1);
            }
            System.out.println("size of billgroup:::" + billgrpmappedOfcList.size());

            for (int i = 0; i < billgrpmappedOfcList.size(); i++) {
                BillBean bb1 = (BillBean) billgrpmappedOfcList.get(i);
                System.out.println("bb1.getoffice:" + bb1.getOfficeCode() + ":::i:" + i);
                ps = con.prepareStatement("select COALESCE(max(task_id),0)+1 taskid FROM paybill_task");
                rs = ps.executeQuery();
                if (rs.next()) {
                    int newTaxid = rs.getInt("taskid");

                    stamt = con.prepareStatement("INSERT INTO PAYBILL_TASK (TASK_ID, OFF_CODE, BILL_ID, BILL_GROUP_ID, PRIORITY, bill_type, aq_month, aq_year) VALUES (?,?,?,?,?,?,?,?)");
                    stamt.setInt(1, newTaxid);
                    stamt.setString(2, bb1.getOfficeCode());
                    stamt.setInt(3, billno);
                    stamt.setBigDecimal(4, new BigDecimal(billgroupid));
                    stamt.setInt(5, 7);
                    stamt.setString(6, "PAY");
                    stamt.setInt(7, month);
                    stamt.setInt(8, year);
                    stamt.executeUpdate();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

}
