package hrms.dao.dpcconducted;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import hrms.common.DataBaseFunctions;
import hrms.model.parmast.DPCConductedBean;
import hrms.model.parmast.DPCConductedForm;
import hrms.model.parmast.NominationCategoryForm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class DPCConductedDAOImpl implements DPCConductedDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public DPCConductedForm getDPCconductedList(String offcode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        DPCConductedForm dpcbean = null;
        List dpclist = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            String sql = "select * from dpcs_conducted where off_code=? LIMIT 1";
            pst = con.prepareStatement(sql);
            pst.setString(1, offcode);
            rs = pst.executeQuery();
            while (rs.next()) {
                dpcbean = new DPCConductedForm();
                dpcbean.setOffCode(rs.getString("off_code"));
                dpcbean.setDlTotal(rs.getString("dl_total"));
                dpcbean.setDlDpcsDue(rs.getString("dl_dpcs_due"));
                dpcbean.setDlDpcs(rs.getString("dl_dpcs"));
                dpcbean.setDlGroupA(rs.getString("dl_group_a"));
                dpcbean.setDlGroupB(rs.getString("dl_group_b"));
                dpcbean.setDlGroupC(rs.getString("dl_group_c"));
                dpcbean.setDlGroupD(rs.getString("dl_group_d"));
                dpcbean.setDlDpcsNotConducted(rs.getString("dl_dpcs_not_conducted"));
                dpcbean.setDlReasonsThereof(rs.getString("dl_reasons_thereof"));
                dpcbean.setHdTotal(rs.getString("hd_total"));
                dpcbean.setHdDpcsDue(rs.getString("hd_dpcs_due"));
                dpcbean.setHdDpcs(rs.getString("hd_dpcs"));
                dpcbean.setHdGroupA(rs.getString("hd_group_a"));
                dpcbean.setHdGroupB(rs.getString("hd_group_b"));
                dpcbean.setHdGroupC(rs.getString("hd_group_c"));
                dpcbean.setHdGroupD(rs.getString("hd_group_d"));
                dpcbean.setHdDpcsNotConducted(rs.getString("hd_dpcs_not_conducted"));
                dpcbean.setHdReasonsThereof(rs.getString("hd_reasons_thereof"));
                dpcbean.setDsTotal(rs.getString("ds_total"));
                dpcbean.setDsDpcsDue(rs.getString("ds_dpcs_due"));
                dpcbean.setDsDpcs(rs.getString("ds_dpcs"));
                dpcbean.setDsGroupA(rs.getString("ds_group_a"));
                dpcbean.setDsGroupB(rs.getString("ds_group_b"));
                dpcbean.setDsGroupC(rs.getString("ds_group_c"));
                dpcbean.setDsGroupD(rs.getString("ds_group_d"));
                dpcbean.setDsDpcsNotConducted(rs.getString("ds_dpcs_not_conducted"));
                dpcbean.setDsReasonsThereof(rs.getString("ds_reasons_thereof"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return dpcbean;
    }

    @Override
    public void saveDPCConductedData(DPCConductedForm dpcform, String offcode, String offlevel) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List dpclist = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            int total = 0;
            String sql = "insert into dpcs_conducted(off_code, dl_dpcs_due,dl_dpcs,dl_group_a,dl_group_b,dl_group_c,dl_group_d,dl_total,dl_dpcs_not_conducted,dl_reasons_thereof,hd_dpcs_due,hd_dpcs,hd_group_a,hd_group_b,hd_group_c,hd_group_d,hd_total,hd_dpcs_not_conducted,hd_reasons_thereof,ds_dpcs_due,ds_dpcs,ds_group_a,ds_group_b,ds_group_c,ds_group_d,ds_total,ds_dpcs_not_conducted,ds_reasons_thereof,is_submitted) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            //System.out.println("DPC: "+dpcform.getHdDpcs());
            pst.setString(1, offcode);
            pst.setInt(2, Integer.parseInt(dpcform.getDlDpcsDue()));
            pst.setInt(3, Integer.parseInt(dpcform.getDlDpcs()));
            pst.setInt(4, Integer.parseInt(dpcform.getDlGroupA()));
            pst.setInt(5, Integer.parseInt(dpcform.getDlGroupB()));
            pst.setInt(6, Integer.parseInt(dpcform.getDlGroupC()));
            pst.setInt(7, Integer.parseInt(dpcform.getDlGroupD()));
            pst.setInt(8, Integer.parseInt(dpcform.getDlGroupA()) + Integer.parseInt(dpcform.getDlGroupB()) + Integer.parseInt(dpcform.getDlGroupC()) + Integer.parseInt(dpcform.getDlGroupD()));
            pst.setInt(9, Integer.parseInt(dpcform.getDlDpcsNotConducted()));
            pst.setString(10, dpcform.getDlReasonsThereof());
            pst.setInt(11, Integer.parseInt(dpcform.getHdDpcsDue()));
            pst.setInt(12, Integer.parseInt(dpcform.getHdDpcs()));
            pst.setInt(13, Integer.parseInt(dpcform.getHdGroupA()));
            pst.setInt(14, Integer.parseInt(dpcform.getHdGroupB()));
            pst.setInt(15, Integer.parseInt(dpcform.getHdGroupC()));
            pst.setInt(16, Integer.parseInt(dpcform.getHdGroupD()));
            pst.setInt(17, Integer.parseInt(dpcform.getHdGroupA()) + Integer.parseInt(dpcform.getHdGroupB()) + Integer.parseInt(dpcform.getHdGroupC()) + Integer.parseInt(dpcform.getHdGroupD()));
            pst.setInt(18, Integer.parseInt(dpcform.getHdDpcsNotConducted()));
            pst.setString(19, dpcform.getHdReasonsThereof());
            pst.setInt(20, Integer.parseInt(dpcform.getDsDpcsDue()));
            pst.setInt(21, Integer.parseInt(dpcform.getDsDpcs()));
            pst.setInt(22, Integer.parseInt(dpcform.getDsGroupA()));
            pst.setInt(23, Integer.parseInt(dpcform.getDsGroupB()));
            pst.setInt(24, Integer.parseInt(dpcform.getDsGroupC()));
            pst.setInt(25, Integer.parseInt(dpcform.getDsGroupD()));
            pst.setInt(26, Integer.parseInt(dpcform.getDsGroupA()) + Integer.parseInt(dpcform.getDsGroupB()) + Integer.parseInt(dpcform.getDsGroupC()) + Integer.parseInt(dpcform.getDsGroupD()));
            pst.setInt(27, Integer.parseInt(dpcform.getDsDpcsNotConducted()));
            pst.setString(28, dpcform.getDsReasonsThereof());
            pst.setString(29, "Y");
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public String getDpcSubmissionStatus(String offcode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String isSubmitted = "N";
        try {
            con = this.dataSource.getConnection();

            String sql = "select is_submitted from dpcs_conducted where off_code=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, offcode);
            rs = pst.executeQuery();
            if (rs.next()) {
                isSubmitted = rs.getString("is_submitted");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isSubmitted;
    }

    @Override
    public void saveNominationCategoryData(NominationCategoryForm nominationCategoryForm, String offcode, String offlevel) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            int prDeptGroupA = 0;
            int prDeptGroupB = 0;
            int prDeptGroupC = 0;
            int prDeptGroupD = 0;
            int prDeptGroupTotal = 0;

            int prHodGroupA = 0;
            int prHodGroupB = 0;
            int prHodGroupC = 0;
            int prHodGroupD = 0;
            int prHodGroupTotal = 0;

            int prDistGroupA = 0;
            int prDistGroupB = 0;
            int prDistGroupC = 0;
            int prDistGroupD = 0;
            int prDistGroupTotal = 0;

            if (nominationCategoryForm.getPrDeptGroupA() != null && !nominationCategoryForm.getPrDeptGroupA().equals("")) {
                prDeptGroupA = Integer.parseInt(nominationCategoryForm.getPrDeptGroupA());
            }
            if (nominationCategoryForm.getPrDeptGroupB() != null && !nominationCategoryForm.getPrDeptGroupB().equals("")) {
                prDeptGroupB = Integer.parseInt(nominationCategoryForm.getPrDeptGroupB());
            }
            if (nominationCategoryForm.getPrDeptGroupC() != null && !nominationCategoryForm.getPrDeptGroupC().equals("")) {
                prDeptGroupC = Integer.parseInt(nominationCategoryForm.getPrDeptGroupC());
            }
            if (nominationCategoryForm.getPrDeptGroupD() != null && !nominationCategoryForm.getPrDeptGroupD().equals("")) {
                prDeptGroupD = Integer.parseInt(nominationCategoryForm.getPrDeptGroupD());
            }
            prDeptGroupTotal = prDeptGroupA + prDeptGroupB + prDeptGroupC + prDeptGroupD;

            if (nominationCategoryForm.getPrHodGroupA() != null && !nominationCategoryForm.getPrHodGroupA().equals("")) {
                prHodGroupA = Integer.parseInt(nominationCategoryForm.getPrHodGroupA());
            }
            if (nominationCategoryForm.getPrHodGroupB() != null && !nominationCategoryForm.getPrHodGroupB().equals("")) {
                prHodGroupB = Integer.parseInt(nominationCategoryForm.getPrHodGroupB());
            }
            if (nominationCategoryForm.getPrHodGroupC() != null && !nominationCategoryForm.getPrHodGroupC().equals("")) {
                prHodGroupC = Integer.parseInt(nominationCategoryForm.getPrHodGroupC());
            }
            if (nominationCategoryForm.getPrHodGroupD() != null && !nominationCategoryForm.getPrHodGroupD().equals("")) {
                prHodGroupD = Integer.parseInt(nominationCategoryForm.getPrHodGroupD());
            }
            prHodGroupTotal = prHodGroupA + prHodGroupB + prHodGroupC + prHodGroupD;

            if (nominationCategoryForm.getPrDistGroupA() != null && !nominationCategoryForm.getPrDistGroupA().equals("")) {
                prDistGroupA = Integer.parseInt(nominationCategoryForm.getPrDistGroupA());
            }
            if (nominationCategoryForm.getPrDistGroupB() != null && !nominationCategoryForm.getPrDistGroupB().equals("")) {
                prDistGroupB = Integer.parseInt(nominationCategoryForm.getPrDistGroupB());
            }
            if (nominationCategoryForm.getPrDistGroupC() != null && !nominationCategoryForm.getPrDistGroupC().equals("")) {
                prDistGroupC = Integer.parseInt(nominationCategoryForm.getPrDistGroupC());
            }
            if (nominationCategoryForm.getPrDistGroupD() != null && !nominationCategoryForm.getPrDistGroupD().equals("")) {
                prDistGroupD = Integer.parseInt(nominationCategoryForm.getPrDistGroupD());
            }

            prDistGroupTotal = prDistGroupA + prDistGroupB + prDistGroupC + prDistGroupD;

            int otpwbDeptGroupA = 0;
            int otpwbDeptGroupB = 0;
            int otpwbDeptGroupC = 0;
            int otpwbDeptGroupD = 0;
            int otpwbDeptGroupTotal = 0;

            int otpwbHodGroupA = 0;
            int otpwbHodGroupB = 0;
            int otpwbHodGroupC = 0;
            int otpwbHodGroupD = 0;
            int otpwbHodGroupTotal = 0;

            int otpwbDistGroupA = 0;
            int otpwbDistGroupB = 0;
            int otpwbDistGroupC = 0;
            int otpwbDistGroupD = 0;
            int otpwbDistGroupTotal = 0;

            if (nominationCategoryForm.getOtpwbDeptGroupA() != null && !nominationCategoryForm.getOtpwbDeptGroupA().equals("")) {
                otpwbDeptGroupA = Integer.parseInt(nominationCategoryForm.getOtpwbDeptGroupA());
            }
            if (nominationCategoryForm.getOtpwbDeptGroupB() != null && !nominationCategoryForm.getOtpwbDeptGroupB().equals("")) {
                otpwbDeptGroupB = Integer.parseInt(nominationCategoryForm.getOtpwbDeptGroupB());
            }
            if (nominationCategoryForm.getOtpwbDeptGroupC() != null && !nominationCategoryForm.getOtpwbDeptGroupC().equals("")) {
                otpwbDeptGroupC = Integer.parseInt(nominationCategoryForm.getOtpwbDeptGroupC());
            }
            if (nominationCategoryForm.getOtpwbDeptGroupD() != null && !nominationCategoryForm.getOtpwbDeptGroupD().equals("")) {
                otpwbDeptGroupD = Integer.parseInt(nominationCategoryForm.getOtpwbDeptGroupD());
            }
            otpwbDeptGroupTotal = otpwbDeptGroupA + otpwbDeptGroupB + otpwbDeptGroupC + otpwbDeptGroupD;

            if (nominationCategoryForm.getOtpwbHodGroupA() != null && !nominationCategoryForm.getOtpwbHodGroupA().equals("")) {
                otpwbHodGroupA = Integer.parseInt(nominationCategoryForm.getOtpwbHodGroupA());
            }
            if (nominationCategoryForm.getOtpwbHodGroupB() != null && !nominationCategoryForm.getOtpwbHodGroupB().equals("")) {
                otpwbHodGroupB = Integer.parseInt(nominationCategoryForm.getOtpwbHodGroupB());
            }
            if (nominationCategoryForm.getOtpwbHodGroupC() != null && !nominationCategoryForm.getOtpwbHodGroupC().equals("")) {
                otpwbHodGroupC = Integer.parseInt(nominationCategoryForm.getOtpwbHodGroupC());
            }
            if (nominationCategoryForm.getOtpwbHodGroupD() != null && !nominationCategoryForm.getOtpwbHodGroupD().equals("")) {
                otpwbHodGroupD = Integer.parseInt(nominationCategoryForm.getOtpwbHodGroupD());
            }
            otpwbHodGroupTotal = otpwbHodGroupA + otpwbHodGroupB + otpwbHodGroupC + otpwbHodGroupD;

            if (nominationCategoryForm.getOtpwbDistGroupA() != null && !nominationCategoryForm.getOtpwbDistGroupA().equals("")) {
                otpwbDistGroupA = Integer.parseInt(nominationCategoryForm.getOtpwbDistGroupA());
            }
            if (nominationCategoryForm.getOtpwbDistGroupB() != null && !nominationCategoryForm.getOtpwbDistGroupB().equals("")) {
                otpwbDistGroupB = Integer.parseInt(nominationCategoryForm.getOtpwbDistGroupB());
            }
            if (nominationCategoryForm.getOtpwbDistGroupC() != null && !nominationCategoryForm.getOtpwbDistGroupC().equals("")) {
                otpwbDistGroupC = Integer.parseInt(nominationCategoryForm.getOtpwbDistGroupC());
            }
            if (nominationCategoryForm.getOtpwbDistGroupD() != null && !nominationCategoryForm.getOtpwbDistGroupD().equals("")) {
                otpwbDistGroupD = Integer.parseInt(nominationCategoryForm.getOtpwbDistGroupD());
            }
            otpwbDistGroupTotal = otpwbDistGroupA + otpwbDistGroupB + otpwbDistGroupC + otpwbDistGroupD;

            int otpabDeptGroupA = 0;
            int otpabDeptGroupB = 0;
            int otpabDeptGroupC = 0;
            int otpabDeptGroupD = 0;
            int otpabDeptGroupTotal = 0;

            int otpabHodGroupA = 0;
            int otpabHodGroupB = 0;
            int otpabHodGroupC = 0;
            int otpabHodGroupD = 0;
            int otpabHodGroupTotal = 0;

            int otpabDistGroupA = 0;
            int otpabDistGroupB = 0;
            int otpabDistGroupC = 0;
            int otpabDistGroupD = 0;
            int otpabDistGroupTotal = 0;

            if (nominationCategoryForm.getOtpabDeptGroupA() != null && !nominationCategoryForm.getOtpabDeptGroupA().equals("")) {
                otpabDeptGroupA = Integer.parseInt(nominationCategoryForm.getOtpabDeptGroupA());
            }
            if (nominationCategoryForm.getOtpabDeptGroupB() != null && !nominationCategoryForm.getOtpabDeptGroupB().equals("")) {
                otpabDeptGroupB = Integer.parseInt(nominationCategoryForm.getOtpabDeptGroupB());
            }
            if (nominationCategoryForm.getOtpabDeptGroupC() != null && !nominationCategoryForm.getOtpabDeptGroupC().equals("")) {
                otpabDeptGroupC = Integer.parseInt(nominationCategoryForm.getOtpabDeptGroupC());
            }
            if (nominationCategoryForm.getOtpabDeptGroupD() != null && !nominationCategoryForm.getOtpabDeptGroupD().equals("")) {
                otpabDeptGroupD = Integer.parseInt(nominationCategoryForm.getOtpabDeptGroupD());
            }
            otpabDeptGroupTotal = otpabDeptGroupA + otpabDeptGroupB + otpabDeptGroupC + otpabDeptGroupD;

            if (nominationCategoryForm.getOtpabHodGroupA() != null && !nominationCategoryForm.getOtpabHodGroupA().equals("")) {
                otpabHodGroupA = Integer.parseInt(nominationCategoryForm.getOtpabHodGroupA());
            }
            if (nominationCategoryForm.getOtpabHodGroupB() != null && !nominationCategoryForm.getOtpabHodGroupB().equals("")) {
                otpabHodGroupB = Integer.parseInt(nominationCategoryForm.getOtpabHodGroupB());
            }
            if (nominationCategoryForm.getOtpabHodGroupC() != null && !nominationCategoryForm.getOtpabHodGroupC().equals("")) {
                otpabHodGroupC = Integer.parseInt(nominationCategoryForm.getOtpabHodGroupC());
            }
            if (nominationCategoryForm.getOtpabHodGroupD() != null && !nominationCategoryForm.getOtpabHodGroupD().equals("")) {
                otpabHodGroupD = Integer.parseInt(nominationCategoryForm.getOtpabHodGroupD());
            }
            otpabHodGroupTotal = otpabHodGroupA + otpabHodGroupB + otpabHodGroupC + otpabHodGroupD;

            if (nominationCategoryForm.getOtpabDistGroupA() != null && !nominationCategoryForm.getOtpabDistGroupA().equals("")) {
                otpabDistGroupA = Integer.parseInt(nominationCategoryForm.getOtpabDistGroupA());
            }
            if (nominationCategoryForm.getOtpabDistGroupB() != null && !nominationCategoryForm.getOtpabDistGroupB().equals("")) {
                otpabDistGroupB = Integer.parseInt(nominationCategoryForm.getOtpabDistGroupB());
            }
            if (nominationCategoryForm.getOtpabDistGroupC() != null && !nominationCategoryForm.getOtpabDistGroupC().equals("")) {
                otpabDistGroupC = Integer.parseInt(nominationCategoryForm.getOtpabDistGroupC());
            }
            if (nominationCategoryForm.getOtpabDistGroupD() != null && !nominationCategoryForm.getOtpabDistGroupD().equals("")) {
                otpabDistGroupD = Integer.parseInt(nominationCategoryForm.getOtpabDistGroupD());
            }
            otpabDistGroupTotal = otpabDistGroupA + otpabDistGroupB + otpabDistGroupC + otpabDistGroupD;

            int incentiveDeptGroupA = 0;
            int incentiveDeptGroupB = 0;
            int incentiveDeptGroupC = 0;
            int incentiveDeptGroupD = 0;
            int incentiveDeptGroupTotal = 0;

            int incentiveHodGroupA = 0;
            int incentiveHodGroupB = 0;
            int incentiveHodGroupC = 0;
            int incentiveHodGroupD = 0;
            int incentiveHodGroupTotal = 0;

            int incentiveDistGroupA = 0;
            int incentiveDistGroupB = 0;
            int incentiveDistGroupC = 0;
            int incentiveDistGroupD = 0;
            int incentiveDistGroupTotal = 0;

            if (nominationCategoryForm.getIncentiveDeptGroupA() != null && !nominationCategoryForm.getIncentiveDeptGroupA().equals("")) {
                incentiveDeptGroupA = Integer.parseInt(nominationCategoryForm.getIncentiveDeptGroupA());
            }
            if (nominationCategoryForm.getIncentiveDeptGroupB() != null && !nominationCategoryForm.getIncentiveDeptGroupB().equals("")) {
                incentiveDeptGroupB = Integer.parseInt(nominationCategoryForm.getIncentiveDeptGroupB());
            }
            if (nominationCategoryForm.getIncentiveDeptGroupC() != null && !nominationCategoryForm.getIncentiveDeptGroupC().equals("")) {
                incentiveDeptGroupC = Integer.parseInt(nominationCategoryForm.getIncentiveDeptGroupC());
            }
            if (nominationCategoryForm.getIncentiveDeptGroupD() != null && !nominationCategoryForm.getIncentiveDeptGroupD().equals("")) {
                incentiveDeptGroupD = Integer.parseInt(nominationCategoryForm.getIncentiveDeptGroupD());
            }
            incentiveDeptGroupTotal = incentiveDeptGroupA + incentiveDeptGroupB + incentiveDeptGroupC + incentiveDeptGroupD;

            if (nominationCategoryForm.getIncentiveHodGroupA() != null && !nominationCategoryForm.getIncentiveHodGroupA().equals("")) {
                incentiveHodGroupA = Integer.parseInt(nominationCategoryForm.getIncentiveHodGroupA());
            }
            if (nominationCategoryForm.getIncentiveHodGroupB() != null && !nominationCategoryForm.getIncentiveHodGroupB().equals("")) {
                incentiveHodGroupB = Integer.parseInt(nominationCategoryForm.getIncentiveHodGroupB());
            }
            if (nominationCategoryForm.getIncentiveHodGroupC() != null && !nominationCategoryForm.getIncentiveHodGroupC().equals("")) {
                incentiveHodGroupC = Integer.parseInt(nominationCategoryForm.getIncentiveHodGroupC());
            }
            if (nominationCategoryForm.getIncentiveHodGroupD() != null && !nominationCategoryForm.getIncentiveHodGroupD().equals("")) {
                incentiveHodGroupD = Integer.parseInt(nominationCategoryForm.getIncentiveHodGroupD());
            }
            incentiveHodGroupTotal = incentiveHodGroupA + incentiveHodGroupB + incentiveHodGroupC + incentiveHodGroupD;

            if (nominationCategoryForm.getIncentiveDistGroupA() != null && !nominationCategoryForm.getIncentiveDistGroupA().equals("")) {
                incentiveDistGroupA = Integer.parseInt(nominationCategoryForm.getIncentiveDistGroupA());
            }
            if (nominationCategoryForm.getIncentiveDistGroupB() != null && !nominationCategoryForm.getIncentiveDistGroupB().equals("")) {
                incentiveDistGroupB = Integer.parseInt(nominationCategoryForm.getIncentiveDistGroupB());
            }
            if (nominationCategoryForm.getIncentiveDistGroupC() != null && !nominationCategoryForm.getIncentiveDistGroupC().equals("")) {
                incentiveDistGroupC = Integer.parseInt(nominationCategoryForm.getIncentiveDistGroupC());
            }
            if (nominationCategoryForm.getIncentiveDistGroupD() != null && !nominationCategoryForm.getIncentiveDistGroupD().equals("")) {
                incentiveDistGroupD = Integer.parseInt(nominationCategoryForm.getIncentiveDistGroupD());
            }
            incentiveDistGroupTotal = incentiveDistGroupA + incentiveDistGroupB + incentiveDistGroupC + incentiveDistGroupD;

            String sql = "insert into nomination_final(pr_dept_group_a,pr_dept_group_b,pr_dept_group_c,pr_dept_group_d,pr_dept_group_total,pr_hod_group_a,pr_hod_group_b,pr_hod_group_c,pr_hod_group_d,pr_hod_group_total,pr_dist_group_a,pr_dist_group_b,pr_dist_group_c,pr_dist_group_d,pr_dist_group_total,otpwb_dept_group_a,otpwb_dept_group_b,otpwb_dept_group_c,otpwb_dept_group_d,otpwb_dept_group_total,otpwb_hod_group_a,otpwb_hod_group_b,otpwb_hod_group_c,otpwb_hod_group_d,otpwb_hod_group_total,otpwb_dist_group_a,otpwb_dist_group_b,otpwb_dist_group_c,otpwb_dist_group_d,otpwb_dist_group_total,otpab_dept_group_a,otpab_dept_group_b,otpab_dept_group_c,otpab_dept_group_d,otpab_dept_group_total,otpab_hod_group_a,otpab_hod_group_b,otpab_hod_group_c,otpab_hod_group_d,otpab_hod_group_total,otpab_dist_group_a,otpab_dist_group_b,otpab_dist_group_c,otpab_dist_group_d,otpab_dist_group_total,incentive_dept_group_a,incentive_dept_group_b,incentive_dept_group_c,incentive_dept_group_d,incentive_dept_group_total,incentive_hod_group_a,incentive_hod_group_b,incentive_hod_group_c,incentive_hod_group_d,incentive_hod_group_total,incentive_dist_group_a,incentive_dist_group_b,incentive_dist_group_c,incentive_dist_group_d,incentive_dist_group_total,is_submitted,off_code) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            pst = con.prepareStatement(sql);
            pst.setInt(1, prDeptGroupA);
            pst.setInt(2, prDeptGroupB);
            pst.setInt(3, prDeptGroupC);
            pst.setInt(4, prDeptGroupD);
            pst.setInt(5, prDeptGroupTotal);
            pst.setInt(6, prHodGroupA);
            pst.setInt(7, prHodGroupB);
            pst.setInt(8, prHodGroupC);
            pst.setInt(9, prHodGroupD);
            pst.setInt(10, prHodGroupTotal);
            pst.setInt(11, prDistGroupA);
            pst.setInt(12, prDistGroupB);
            pst.setInt(13, prDistGroupC);
            pst.setInt(14, prDistGroupD);
            pst.setInt(15, prDistGroupTotal);

            pst.setInt(16, otpwbDeptGroupA);
            pst.setInt(17, otpwbDeptGroupB);
            pst.setInt(18, otpwbDeptGroupC);
            pst.setInt(19, otpwbDeptGroupD);
            pst.setInt(20, otpwbDeptGroupTotal);
            pst.setInt(21, otpwbHodGroupA);
            pst.setInt(22, otpwbHodGroupB);
            pst.setInt(23, otpwbHodGroupC);
            pst.setInt(24, otpwbHodGroupD);
            pst.setInt(25, otpwbHodGroupTotal);
            pst.setInt(26, otpwbDistGroupA);
            pst.setInt(27, otpwbDistGroupB);
            pst.setInt(28, otpwbDistGroupC);
            pst.setInt(29, otpwbDistGroupD);
            pst.setInt(30, otpwbDistGroupTotal);

            pst.setInt(31, otpabDeptGroupA);
            pst.setInt(32, otpabDeptGroupB);
            pst.setInt(33, otpabDeptGroupC);
            pst.setInt(34, otpabDeptGroupD);
            pst.setInt(35, otpabDeptGroupTotal);
            pst.setInt(36, otpabHodGroupA);
            pst.setInt(37, otpabHodGroupB);
            pst.setInt(38, otpabHodGroupC);
            pst.setInt(39, otpabHodGroupD);
            pst.setInt(40, otpabHodGroupTotal);
            pst.setInt(41, otpabDistGroupA);
            pst.setInt(42, otpabDistGroupB);
            pst.setInt(43, otpabDistGroupC);
            pst.setInt(44, otpabDistGroupD);
            pst.setInt(45, otpabDistGroupTotal);

            pst.setInt(46, incentiveDeptGroupA);
            pst.setInt(47, incentiveDeptGroupB);
            pst.setInt(48, incentiveDeptGroupC);
            pst.setInt(49, incentiveDeptGroupD);
            pst.setInt(50, incentiveDeptGroupTotal);
            pst.setInt(51, incentiveHodGroupA);
            pst.setInt(52, incentiveHodGroupB);
            pst.setInt(53, incentiveHodGroupC);
            pst.setInt(54, incentiveHodGroupD);
            pst.setInt(55, incentiveHodGroupTotal);
            pst.setInt(56, incentiveDistGroupA);
            pst.setInt(57, incentiveDistGroupB);
            pst.setInt(58, incentiveDistGroupC);
            pst.setInt(59, incentiveDistGroupD);
            pst.setInt(60, incentiveDistGroupTotal);

            pst.setString(61, "Y");
            pst.setString(62, offcode);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public NominationCategoryForm getNominationCategoryData(String offcode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        NominationCategoryForm nform = new NominationCategoryForm();

        try {
            con = this.dataSource.getConnection();

            String sql = "select * from nomination_final where off_code=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, offcode);
            rs = pst.executeQuery();
            if (rs.next()) {
                nform.setPrDeptGroupA(rs.getString("pr_dept_group_a"));
                nform.setPrDeptGroupB(rs.getString("pr_dept_group_b"));
                nform.setPrDeptGroupC(rs.getString("pr_dept_group_c"));
                nform.setPrDeptGroupD(rs.getString("pr_dept_group_d"));
                nform.setPrDeptGroupTotal(rs.getString("pr_dept_group_total"));
                nform.setPrHodGroupA(rs.getString("pr_hod_group_a"));
                nform.setPrHodGroupB(rs.getString("pr_hod_group_b"));
                nform.setPrHodGroupC(rs.getString("pr_hod_group_c"));
                nform.setPrHodGroupD(rs.getString("pr_hod_group_d"));
                nform.setPrHodGroupTotal(rs.getString("pr_hod_group_total"));
                nform.setPrDistGroupA(rs.getString("pr_dist_group_a"));
                nform.setPrDistGroupB(rs.getString("pr_dist_group_b"));
                nform.setPrDistGroupC(rs.getString("pr_dist_group_c"));
                nform.setPrDistGroupD(rs.getString("pr_dist_group_d"));
                nform.setPrDistGroupTotal(rs.getString("pr_dist_group_total"));

                nform.setOtpwbDeptGroupA(rs.getString("otpwb_dept_group_a"));
                nform.setOtpwbDeptGroupB(rs.getString("otpwb_dept_group_b"));
                nform.setOtpwbDeptGroupC(rs.getString("otpwb_dept_group_c"));
                nform.setOtpwbDeptGroupD(rs.getString("otpwb_dept_group_d"));
                nform.setOtpwbDeptGroupTotal(rs.getString("otpwb_dept_group_total"));
                nform.setOtpwbHodGroupA(rs.getString("otpwb_hod_group_a"));
                nform.setOtpwbHodGroupB(rs.getString("otpwb_hod_group_b"));
                nform.setOtpwbHodGroupC(rs.getString("otpwb_hod_group_c"));
                nform.setOtpwbHodGroupD(rs.getString("otpwb_hod_group_d"));
                nform.setOtpwbHodGroupTotal(rs.getString("otpwb_hod_group_total"));
                nform.setOtpwbDistGroupA(rs.getString("otpwb_dist_group_a"));
                nform.setOtpwbDistGroupB(rs.getString("otpwb_dist_group_b"));
                nform.setOtpwbDistGroupC(rs.getString("otpwb_dist_group_c"));
                nform.setOtpwbDistGroupD(rs.getString("otpwb_dist_group_d"));
                nform.setOtpwbDistGroupToal(rs.getString("otpwb_dist_group_total"));

                nform.setOtpabDeptGroupA(rs.getString("otpab_dept_group_a"));
                nform.setOtpabDeptGroupB(rs.getString("otpab_dept_group_b"));
                nform.setOtpabDeptGroupC(rs.getString("otpab_dept_group_c"));
                nform.setOtpabDeptGroupD(rs.getString("otpab_dept_group_d"));
                nform.setOtpabDeptGroupTotal(rs.getString("otpab_dept_group_total"));
                nform.setOtpabHodGroupA(rs.getString("otpab_hod_group_a"));
                nform.setOtpabHodGroupB(rs.getString("otpab_hod_group_b"));
                nform.setOtpabHodGroupC(rs.getString("otpab_hod_group_c"));
                nform.setOtpabHodGroupD(rs.getString("otpab_hod_group_d"));
                nform.setOtpabHodGroupTotal(rs.getString("otpab_hod_group_total"));
                nform.setOtpabDistGroupA(rs.getString("otpab_dist_group_a"));
                nform.setOtpabDistGroupB(rs.getString("otpab_dist_group_b"));
                nform.setOtpabDistGroupC(rs.getString("otpab_dist_group_c"));
                nform.setOtpabDistGroupD(rs.getString("otpab_dist_group_d"));
                nform.setOtpabDistGroupTotal(rs.getString("otpab_dist_group_total"));

                nform.setIncentiveDeptGroupA(rs.getString("incentive_dept_group_a"));
                nform.setIncentiveDeptGroupB(rs.getString("incentive_dept_group_b"));
                nform.setIncentiveDeptGroupC(rs.getString("incentive_dept_group_c"));
                nform.setIncentiveDeptGroupD(rs.getString("incentive_dept_group_d"));
                nform.setIncentiveDeptGroupTotal(rs.getString("incentive_dept_group_total"));
                nform.setIncentiveHodGroupA(rs.getString("incentive_hod_group_a"));
                nform.setIncentiveHodGroupB(rs.getString("incentive_hod_group_b"));
                nform.setIncentiveHodGroupC(rs.getString("incentive_hod_group_c"));
                nform.setIncentiveHodGroupD(rs.getString("incentive_hod_group_d"));
                nform.setIncentiveHodGroupTotal(rs.getString("incentive_hod_group_total"));
                nform.setIncentiveDistGroupA(rs.getString("incentive_dist_group_a"));
                nform.setIncentiveDistGroupB(rs.getString("incentive_dist_group_b"));
                nform.setIncentiveDistGroupC(rs.getString("incentive_dist_group_c"));
                nform.setIncentiveDistGroupD(rs.getString("incentive_dist_group_d"));
                nform.setIncentiveDistGroupTotal(rs.getString("incentive_dist_group_total"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return nform;
    }

    @Override
    public String getNominationCategorySubmissionStatus(String offcode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String isSubmitted = "N";
        try {
            con = this.dataSource.getConnection();

            String sql = "select is_submitted from nomination_final where off_code=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, offcode);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("is_submitted") != null && rs.getString("is_submitted").equals("Y")) {
                    isSubmitted = rs.getString("is_submitted");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isSubmitted;
    }

    @Override
    public List dpcMISReport() {
        Connection con = null;
        List dpclist = new ArrayList();
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("SELECT department_code,department_name,ao_off_code,nomination_final_id,dpcs_conducted_id from g_department "
                    + "left outer join nomination_final on g_department.ao_off_code = nomination_final.off_code "
                    + "left outer join dpcs_conducted on g_department.ao_off_code = dpcs_conducted.off_code "
                    + "where if_active='Y' order by department_name");
            rs = pst.executeQuery();
            while (rs.next()) {
                HashMap map = new HashMap();
                map.put("depcode", rs.getString("department_code"));
                map.put("depname", rs.getString("department_name"));
                map.put("aooffcode", rs.getString("ao_off_code"));
                if(rs.getInt("nomination_final_id") != 0){
                    map.put("isnominationsubmitted", "Y");
                }
                if(rs.getInt("dpcs_conducted_id") != 0){
                    map.put("isdpcsubmitted", "Y");
                }
                dpclist.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return dpclist;
    }

    @Override
    public void downloadNominationCategoryPDF(Document document, String offcode, String offname, NominationCategoryForm nominationCategoryForm) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");

            PdfPTable table = null;
            PdfPCell cell = null;

            table = new PdfPTable(8);
            table.setWidths(new float[]{0.5f, 1, 1, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("Premature Retirement, Out of turn promotion (within the batch and across the batch)\nIncentives ", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            cell.setColspan(8);
            cell.setFixedHeight(30);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setColspan(8);
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Name of the Department/HOD/District:", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            cell.setColspan(2);
            cell.setFixedHeight(30);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(offname, new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            cell.setColspan(6);
            cell.setFixedHeight(30);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Sl No", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            cell.setRowspan(2);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Category", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            cell.setRowspan(2);
            cell.setColspan(2);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("No of Cases Finalized", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            cell.setColspan(5);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Group-A", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            cell.setFixedHeight(20);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Group-B", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            cell.setFixedHeight(20);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Group-C", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            cell.setFixedHeight(20);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Group-D", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            cell.setFixedHeight(20);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Total", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            cell.setFixedHeight(20);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("1", f1));
            cell.setRowspan(3);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Premature Retirement", f1));
            cell.setRowspan(3);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Department Level", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getPrDeptGroupA(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getPrDeptGroupB(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getPrDeptGroupC(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getPrDeptGroupD(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getPrDeptGroupTotal(), f1));
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Heads of Department Level", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getPrHodGroupA(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getPrHodGroupB(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getPrHodGroupC(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getPrHodGroupD(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getPrHodGroupTotal(), f1));
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("District Level", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getPrDistGroupA(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getPrDistGroupB(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getPrDistGroupC(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getPrDistGroupD(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getPrDistGroupTotal(), f1));
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("2", f1));
            cell.setRowspan(3);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Out of turn promotion (within the batch)", f1));
            cell.setRowspan(3);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Department Level", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getOtpwbDeptGroupA(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getOtpwbDeptGroupB(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getOtpwbDeptGroupC(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getOtpwbDeptGroupD(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getOtpwbDeptGroupTotal(), f1));
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Heads of Department Level", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getOtpwbHodGroupA(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getOtpwbHodGroupB(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getOtpwbHodGroupC(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getOtpwbHodGroupD(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getOtpwbHodGroupTotal(), f1));
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("District Level", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getOtpwbDistGroupA(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getOtpwbDistGroupB(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getOtpwbDistGroupC(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getOtpwbDistGroupD(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getOtpwbDistGroupToal(), f1));
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("3", f1));
            cell.setRowspan(3);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Out of turn promotion (across the batch)", f1));
            cell.setRowspan(3);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Department Level", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getOtpabDeptGroupA(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getOtpabDeptGroupB(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getOtpabDeptGroupC(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getOtpabDeptGroupD(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getOtpabDeptGroupTotal(), f1));
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Heads of Department Level", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getOtpabHodGroupA(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getOtpabHodGroupB(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getOtpabHodGroupC(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getOtpabHodGroupD(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getOtpabHodGroupTotal(), f1));
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("District Level", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getOtpabDistGroupA(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getOtpabDistGroupB(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getOtpabDistGroupC(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getOtpabDistGroupD(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getOtpabDistGroupTotal(), f1));
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("4", f1));
            cell.setRowspan(3);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Incentives", f1));
            cell.setRowspan(3);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Department Level", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getIncentiveDeptGroupA(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getIncentiveDeptGroupB(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getIncentiveDeptGroupC(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getIncentiveDeptGroupD(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getIncentiveDeptGroupTotal(), f1));
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Heads of Department Level", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getIncentiveHodGroupA(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getIncentiveHodGroupB(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getIncentiveHodGroupC(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getIncentiveHodGroupD(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getIncentiveHodGroupTotal(), f1));
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("District Level", f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getIncentiveDistGroupA(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getIncentiveDistGroupB(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getIncentiveDistGroupC(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getIncentiveDistGroupD(), f1));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(nominationCategoryForm.getIncentiveDistGroupTotal(), f1));
            table.addCell(cell);

            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }
}
