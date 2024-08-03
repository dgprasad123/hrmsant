/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.report.annualestablishment;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.master.Office;
import hrms.model.report.annualestablishmentreport.AERFlowList;
import hrms.model.report.annualestablishmentreport.AerListBox;
import hrms.model.report.annualestablishmentreport.AnnualEstablishment;
import hrms.model.report.annualestablishmentreport.AnnualEstablishmentReportPTDeptList;
import hrms.model.report.annualestablishmentreport.AnnualEstablishmentReportPostTerminatonBean;
import hrms.model.report.annualestablishmentreport.AnnualEstablishmentReportPostTerminatonForm;
import hrms.model.report.annualestablishmentreport.DepartmentWiseAerStatus;
import hrms.model.report.annualestablishmentreport.ProformaEmployeeDataBean;
import hrms.model.report.annualestablishmentreport.ScheduleIIBean;
import hrms.model.workflowrouting.WorkflowRouting;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.sql.DataSource;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Surendra
 */
public class AnnuaiEstablishmentReportDAOImpl implements AnnuaiEstablishmentReportDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    @Resource(name = "repodataSource")
    protected DataSource repodataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setRepodataSource(DataSource repodataSource) {
        this.repodataSource = repodataSource;
    }

    @Override
    public List getAerReportListFinancialYearWise(String offCode, String financialYear, String roleType, String loginSpc) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<AnnualEstablishment> li = new ArrayList<AnnualEstablishment>();
        AnnualEstablishment ae = null;
        int i = 0;
        try {
            con = this.dataSource.getConnection();
            if (roleType != null && !roleType.equals("")) {
                if (roleType.equalsIgnoreCase("OP") || roleType.equalsIgnoreCase("BO")) {
                    ps = con.prepareStatement(" select co_code,co_off_code,aer_id, aer.off_code, controlling_spc, spn, status, fy, submitted_on,is_operator_submitted, operator_spc,spn, is_approver_submitted,revert_reason, (select count(*) cnt from annual_establish_report where aer_id= aer.aer_id) totaer_report from aer_report_submit aer LEFT OUTER JOIN g_spc "
                            + " g_spc ON aer.controlling_spc=g_spc.spc "
                            + " LEFT OUTER JOIN G_OFFICE ON aer.co_off_code=G_OFFICE.off_code"
                            + " where aer.off_code=? AND FY=? ");
                    /*ps = con.prepareStatement("select co_code,co_off_code,aer_id, aer.off_code, controlling_spc, spn, status, fy, submitted_on,is_operator_submitted, operator_spc,spn, is_approver_submitted,revert_reason, (select count(*) cnt from annual_establish_report where aer_id= aer.aer_id) totaer_report from aer_report_submit aer" +
                                             " LEFT OUTER JOIN g_spc g_spc ON aer.controlling_spc=g_spc.spc" +
                                             " LEFT OUTER JOIN G_OFFICE ON aer.co_off_code=G_OFFICE.off_code" +
                                             " where aer.off_code=? AND FY=?" +
                                             " UNION" +
                                             " select co_code,co_off_code,aer_id, aer.off_code, controlling_spc, spn, status, fy, submitted_on,is_operator_submitted, operator_spc,spn, is_approver_submitted,revert_reason, (select count(*) cnt from annual_establish_report where aer_id= aer.aer_id) totaer_report from aer_report_submit aer" +
                                             " LEFT OUTER JOIN g_spc g_spc ON aer.controlling_spc=g_spc.spc" +
                                             " LEFT OUTER JOIN G_OFFICE ON aer.co_off_code=G_OFFICE.off_code" +
                                             " where aer.off_code=(SELECT OFF_CODE FROM PROCESS_AUTHORIZATION WHERE HRMS_ID=(SELECT EMP_ID FROM EMP_JOIN WHERE IF_AD_CHARGE='Y' order by join_date desc limit 1)) AND FY='"+financialYear+"'");*/
                    ps.setString(1, offCode);
                    ps.setString(2, financialYear);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        i++;
                        ae = new AnnualEstablishment();
                        ae.setTotpostinAerreport(rs.getInt("totaer_report"));
                        ae.setSerialno(i);
                        ae.setAerId(rs.getInt("aer_id"));
                        ae.setOffCode(rs.getString("off_code"));
                        ae.setControllingSpc(rs.getString("spn"));
                        ae.setStatus(rs.getString("status"));
                        ae.setFy(rs.getString("fy"));
                        ae.setSubmittedDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("submitted_on")));
                        ae.setRevertReason(rs.getString("revert_reason"));
                        if (roleType.equalsIgnoreCase("BO")) {
                            if (rs.getString("is_operator_submitted") != null && !rs.getString("is_operator_submitted").equals("") && rs.getString("is_operator_submitted").equals("Y")) {
                                if (rs.getString("is_approver_submitted") != null && !rs.getString("is_approver_submitted").equals("") && rs.getString("is_approver_submitted").equals("Y")) {
                                    ae.setShowApproveLink("N");
                                } else {
                                    ae.setShowApproveLink("Y");
                                }
                            } else {
                                ae.setShowApproveLink("N");
                            }

                        } else {
                            ae.setShowApproveLink("N");
                        }
                        ae.setCoOffCode(rs.getString("co_off_code"));
                        ae.setCoCode(rs.getString("co_code"));
                        li.add(ae);
                    }
                } else if (roleType.equalsIgnoreCase("AP")) {
                    /*ps = con.prepareStatement(" select aer_id, aer.fy, submitted_on, is_operator_submitted, operator_spc,spn, is_approver_submitted, approver_spc, is_reviewer_submitted, "
                     + "  is_verifier_submitted, is_acceptor_submitted , (select count(*) cnt from annual_establish_report where aer_id= aer.aer_id) totaer_report from aer_report_submit aer "
                     + " INNER JOIN process_authorization ON aer.off_code=process_authorization.off_code "
                     + " inner join g_spc on aer.operator_spc=g_spc.spc "
                     + " where FY=? AND process_authorization.SPC=? and process_authorization.process_id=? and role_type='APPROVER'");*/
                    ps = con.prepareStatement("select g_office2.co_code,aer.co_off_code,g_office1.lvl,aer_report_submit_at_co.co_aer_id,dreviewer_emp_id,dverifier_emp_id,aer_report_submit_at_co.acceptor_emp_id,aer_id, aer.fy, submitted_on, is_operator_submitted, operator_spc,spn, is_approver_submitted, approver_spc, is_reviewer_submitted,"
                            + " is_verifier_submitted, is_acceptor_submitted , (select count(*) cnt from annual_establish_report where aer_id= aer.aer_id) totaer_report from aer_report_submit aer"
                            + " INNER JOIN process_authorization ON aer.off_code=process_authorization.off_code"
                            + " inner join g_office g_office1 on aer.off_code=g_office1.off_code"
                            + " left outer join g_office g_office2 on aer.co_off_code=g_office2.off_code"
                            + " inner join g_spc on aer.operator_spc=g_spc.spc"
                            + " left outer join aer_report_submit_at_co on aer.co_aer_id=aer_report_submit_at_co.co_aer_id"
                            + " where aer.FY=? AND process_authorization.SPC=? and process_authorization.process_id=? and role_type='APPROVER'");

                    ps.setString(1, financialYear);
                    ps.setString(2, loginSpc);
                    ps.setInt(3, 13);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        i++;

                        ae = new AnnualEstablishment();

                        ae.setCoOffCode(rs.getString("co_off_code"));
                        ae.setCoCode(rs.getString("co_code"));

                        ae.setTotpostinAerreport(rs.getInt("totaer_report"));
                        ae.setSerialno(i);
                        ae.setAerId(rs.getInt("aer_id"));
                        //ae.setOffCode(rs.getString("off_code"));
                        ae.setControllingSpc(rs.getString("spn"));
                        //ae.setFileName(rs.getString("file_name"));
                        if (rs.getString("is_operator_submitted").equalsIgnoreCase("N")) {
                            ae.setStatus("PENDING AT OPERATOR END");
                        } else if (rs.getString("is_operator_submitted").equalsIgnoreCase("Y")) {
                            if (rs.getString("is_approver_submitted") != null && rs.getString("is_approver_submitted").equalsIgnoreCase("N")) {
                                ae.setStatus("PENDING AT SELF");
                            } else if (rs.getString("is_approver_submitted").equalsIgnoreCase("Y")) {
                                if (rs.getString("is_reviewer_submitted").equalsIgnoreCase("N")) {
                                    ae.setStatus("PENDING AT REVIEWER END");
                                } else if (rs.getString("is_reviewer_submitted").equalsIgnoreCase("Y")) {
                                    if (rs.getString("is_verifier_submitted").equalsIgnoreCase("N")) {
                                        ae.setStatus("PENDING AT VERIFIER END");
                                    } else if (rs.getString("is_verifier_submitted").equalsIgnoreCase("Y")) {
                                        if (rs.getString("lvl") != null && rs.getString("lvl").equals("02")) {
                                            if (rs.getString("dreviewer_emp_id") == null || rs.getString("dreviewer_emp_id").equals("")) {
                                                ae.setStatus("PENDING AT DEPARTMENTAL REVIEWER END");
                                            } else if (rs.getString("dverifier_emp_id") == null || rs.getString("dverifier_emp_id").equals("")) {
                                                ae.setStatus("PENDING AT DEPARTMENTAL VERIFIER END");
                                            } else if (rs.getString("acceptor_emp_id") == null || rs.getString("acceptor_emp_id").equals("")) {
                                                ae.setStatus("PENDING AT ACCEPTOR END");
                                            }
                                        } else {
                                            if (rs.getString("acceptor_emp_id") == null || rs.getString("acceptor_emp_id").equals("")) {
                                                ae.setStatus("PENDING AT ACCEPTOR END");
                                            }
                                        }
                                    }
                                }
                            } else {
                                ae.setStatus("");
                            }
                        }

                        if (rs.getString("is_approver_submitted") != null && !rs.getString("is_approver_submitted").equals("") && rs.getString("is_approver_submitted").equals("Y")) {
                            ae.setShowApproveLink("N");
                        } else {
                            ae.setShowApproveLink("Y");
                        }

                        ae.setFy(rs.getString("fy"));
                        ae.setSubmittedDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("submitted_on")));

                        li.add(ae);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getAerReportListFinancialYearWiseForCOLevel(String offCode, String financialYear, String roleType, String loginSpc) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<AnnualEstablishment> li = new ArrayList<AnnualEstablishment>();
        AnnualEstablishment ae = null;
        int i = 0;
        try {
            con = this.dataSource.getConnection();
            if (roleType != null && !roleType.equals("")) {
                if (roleType.equalsIgnoreCase("REVIEWER") || roleType.equalsIgnoreCase("BO")) {

                    ps = con.prepareStatement("SELECT ddo_code, aer_id, fy, approver_submitted_on, is_operator_submitted, approver_spc,spn,"
                            + " is_approver_submitted, approver_spc, is_reviewer_submitted,verifier_revert_reason, "
                            + " off_en, is_verifier_submitted, is_acceptor_submitted FROM process_authorization "
                            + " INNER JOIN aer_report_submit ON process_authorization.OFF_CODE = aer_report_submit.CO_OFF_CODE "
                            + " INNER JOIN  g_spc on aer_report_submit.approver_spc=g_spc.spc "
                            + " inner join g_office on aer_report_submit.OFF_CODE=g_office.off_code "
                            + " WHERE process_authorization.SPC=? AND PROCESS_ID=? AND aer_report_submit.FY=? "
                            + " AND ROLE_TYPE='REVIEWER' "
                            + " order by reviewer_submitted_on");
                    ps.setString(1, loginSpc);
                    ps.setInt(2, 13);
                    ps.setString(3, financialYear);
                    //ps.setString(4, financialYear);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        i++;

                        ae = new AnnualEstablishment();
                        ae.setSerialno(i);
                        ae.setAerId(rs.getInt("aer_id"));
                        ae.setPostname(rs.getString("spn"));
                        ae.setRevertReason(rs.getString("verifier_revert_reason"));
                        ae.setControllingSpc(rs.getString("spn"));
                        if (rs.getString("is_reviewer_submitted").equalsIgnoreCase("N")) {
                            ae.setStatus("PENDING AT SELF");
                        } else if (rs.getString("is_verifier_submitted").equalsIgnoreCase("N")) {
                            ae.setStatus("PENDING AT VERIFIER END");
                        } else if (rs.getString("is_acceptor_submitted").equalsIgnoreCase("N")) {
                            ae.setStatus("PENDING AT ACCEPTOR END");
                        } else if (rs.getString("is_acceptor_submitted").equalsIgnoreCase("Y")) {
                            ae.setStatus("ACCEPTED");
                        }
                        if (rs.getString("is_reviewer_submitted") != null && !rs.getString("is_reviewer_submitted").equals("") && rs.getString("is_reviewer_submitted").equals("Y")) {
                            ae.setShowApproveLink("N");
                        } else {
                            ae.setShowApproveLink("Y");
                        }
                        if (roleType.equalsIgnoreCase("BO")) {
                            if (rs.getString("is_reviewer_submitted") != null && !rs.getString("is_reviewer_submitted").equals("") && rs.getString("is_reviewer_submitted").equals("Y")) {
                                if (rs.getString("is_verifier_submitted") != null && !rs.getString("is_verifier_submitted").equals("") && rs.getString("is_verifier_submitted").equals("Y")) {
                                    ae.setShowApproveLink("N");
                                } else {
                                    ae.setShowApproveLink("Y");
                                    ae.setStatus("PENDING AT SELF");
                                }

                            } else {
                                ae.setShowApproveLink("Y");
                            }
                        }
                        ae.setDdoCode(rs.getString("ddo_code"));
                        ae.setOperatoroffName(rs.getString("off_en"));
                        ae.setFy(rs.getString("fy"));
                        ae.setSubmittedDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("approver_submitted_on")));

                        li.add(ae);
                    }
                } else if (roleType.equalsIgnoreCase("VERIFIER")) {
                    /*
                     ps = con.prepareStatement("	select ddo_code, aer_id, aer.fy, approver_submitted_on, is_operator_submitted, approver_spc,spn, is_approver_submitted, approver_spc, is_reviewer_submitted, off_en, "
                     + "	is_verifier_submitted,reviewer_spc,reviewer_submitted_on,verifier_submitted_on, is_acceptor_submitted, acceptor_submitted_on from aer_report_submit aer "
                     + "	INNER JOIN process_authorization ON aer.co_off_code=process_authorization.off_code "
                     + "	inner join g_spc on aer.verifier_spc=g_spc.spc "
                     + " inner join g_office on aer.off_code=g_office.off_code "
                     + "	where FY=? AND process_authorization.SPC=? "
                     + "	and process_authorization.process_id=? and process_authorization.role_type='VERIFIER' and is_reviewer_submitted='Y' order by reviewer_submitted_on");
                   
                     */

                    ps = con.prepareStatement(" SELECT * FROM process_authorization "
                            + " INNER JOIN aer_report_submit ON process_authorization.OFF_CODE = aer_report_submit.CO_OFF_CODE "
                            //+ " INNER JOIN  g_spc on aer_report_submit.reviewer_spc=g_spc.spc "
                            + " inner join g_office on aer_report_submit.OFF_CODE=g_office.off_code "
                            + " WHERE process_authorization.SPC=? AND PROCESS_ID=13 AND aer_report_submit.FY=?"
                            + " AND ROLE_TYPE='VERIFIER'"
                            + " and is_reviewer_submitted='Y' order by reviewer_submitted_on ");

                    ps.setString(1, loginSpc);
                    ps.setString(2, financialYear);
                    //ps.setString(3, financialYear);

                    rs = ps.executeQuery();
                    while (rs.next()) {
                        i++;

                        ae = new AnnualEstablishment();
                        ae.setSerialno(i);
                        ae.setAerId(rs.getInt("aer_id"));
                        //ae.setPostname(rs.getString("spn"));
                        //ae.setControllingSpc(rs.getString("spn"));
                        ae.setSubmittedDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("reviewer_submitted_on")));
                        if (rs.getString("is_verifier_submitted").equalsIgnoreCase("N")) {

                            ae.setStatus("PENDING AT SELF");
                        } else if (rs.getString("is_verifier_submitted").equalsIgnoreCase("Y") && (rs.getString("co_aer_id") == null || rs.getString("co_aer_id").equals(""))) {
                            ae.setSubmittedDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("verifier_submitted_on")));
                            ae.setStatus("APPROVED BY VERIFIER");
                        } else if (rs.getString("is_acceptor_submitted").equalsIgnoreCase("N") && rs.getInt("co_aer_id") > 0) {
                            ae.setSubmittedDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("verifier_submitted_on")));
                            ae.setStatus("PENDING AT ACCEPTOR END");
                        } else if (rs.getString("is_acceptor_submitted").equalsIgnoreCase("Y")) {
                            ae.setStatus("ACCEPTED");
                            ae.setSubmittedDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("acceptor_submitted_on")));
                        }
                        if (rs.getString("is_verifier_submitted") != null && !rs.getString("is_verifier_submitted").equals("") && rs.getString("is_verifier_submitted").equals("Y")) {
                            ae.setShowApproveLink("N");
                        } else {
                            ae.setShowApproveLink("Y");
                        }
                        ae.setDdoCode(rs.getString("ddo_code"));
                        ae.setOperatoroffName(rs.getString("off_en"));
                        ae.setFy(rs.getString("fy"));

                        li.add(ae);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getAerReportListFinancialYearWiseForAOLevel(String offCode, String financialYear, String roleType, String loginSpc) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<AnnualEstablishment> li = new ArrayList<AnnualEstablishment>();
        AnnualEstablishment ae = null;
        int i = 0;
        try {
            con = this.dataSource.getConnection();
            if (roleType.equals("ACCEPTOR")) {
                ps = con.prepareStatement("select co_code,co_off_code,co_aer_id, aer.fy, verifier_submitted_on, spn, verifier_spc, off_en,acceptor_emp_id, dreviewer_emp_id, dverifier_emp_id,LVL "
                        + " from aer_report_submit_at_co aer  "
                        + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code  "
                        + " inner join g_spc on aer.verifier_spc=g_spc.spc  "
                        + " inner join g_office on aer.co_off_code=g_office.off_code  "
                        + " where FY=? AND process_authorization.SPC=?  "
                        + " and process_authorization.process_id=? and process_authorization.role_type='ACCEPTOR' ");
            } else if (roleType.equals("DREVIEWER")) {
                ps = con.prepareStatement("select co_code,co_off_code,co_aer_id, aer.fy, verifier_submitted_on, spn, verifier_spc, off_en,dreviewer_emp_id,dverifier_emp_id,acceptor_emp_id,LVL "
                        + " from aer_report_submit_at_co aer  "
                        + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code  "
                        + " inner join g_spc on aer.verifier_spc=g_spc.spc  "
                        + " inner join g_office on aer.co_off_code=g_office.off_code  "
                        + " where FY=? AND process_authorization.SPC=?  "
                        + " and process_authorization.process_id=? and process_authorization.role_type='DREVIEWER' AND LVL != '01' ");
            } else if (roleType.equals("DVERIFIER")) {
                ps = con.prepareStatement("select co_code,co_off_code,co_aer_id, aer.fy, verifier_submitted_on, spn, verifier_spc, off_en,dreviewer_emp_id,dverifier_emp_id,acceptor_emp_id,LVL "
                        + " from aer_report_submit_at_co aer  "
                        + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code  "
                        + " inner join g_spc on aer.verifier_spc=g_spc.spc  "
                        + " inner join g_office on aer.co_off_code=g_office.off_code  "
                        + " where FY=? AND process_authorization.SPC=?  "
                        + " and process_authorization.process_id=? and process_authorization.role_type='DVERIFIER'  AND LVL != '01' ");
            }
            if (roleType != null && !roleType.equals("")) {
                ps.setString(1, financialYear);
                ps.setString(2, loginSpc);
                ps.setInt(3, 13);

                rs = ps.executeQuery();
                while (rs.next()) {
                    i++;

                    ae = new AnnualEstablishment();
                    ae.setSerialno(i);
                    ae.setAerId(rs.getInt("co_aer_id"));
                    ae.setPostname(rs.getString("spn"));
                    ae.setControllingSpc(rs.getString("verifier_spc"));

                    ae.setCoOffCode(rs.getString("co_off_code"));
                    ae.setCoCode(rs.getString("co_code"));

                    ae.setOperatoroffName(rs.getString("off_en"));
                    ae.setFy(rs.getString("fy"));
                    ae.setSubmittedDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("verifier_submitted_on")));
                    if (roleType.equals("DREVIEWER")) {
                        if (rs.getString("dreviewer_emp_id") == null || rs.getString("dreviewer_emp_id").equals("")) {
                            ae.setShowApproveLink("Y");
                            ae.setShowDisApproveLink("N");
                        } else {
                            ae.setShowApproveLink("N");
                            ae.setShowDisApproveLink("Y");
                        }
                    } else if (roleType.equals("DVERIFIER")) {
                        if (rs.getString("dreviewer_emp_id") != null && (rs.getString("dverifier_emp_id") == null || rs.getString("dverifier_emp_id").equals(""))) {
                            ae.setShowApproveLink("Y");
                            ae.setShowDisApproveLink("N");
                        } else if ((rs.getString("dreviewer_emp_id") == null || rs.getString("dreviewer_emp_id").equals("")) && (rs.getString("dverifier_emp_id") == null || rs.getString("dverifier_emp_id").equals(""))) {
                            ae.setShowApproveLink("N");
                            ae.setShowDisApproveLink("N");
                        } else {
                            ae.setShowApproveLink("N");
                            ae.setShowDisApproveLink("Y");
                        }
                    } else if (roleType.equals("ACCEPTOR")) {
                        if (rs.getString("LVL").equals("01")) {
                            if (rs.getString("acceptor_emp_id") != null && !rs.getString("acceptor_emp_id").equals("")) {
                                ae.setShowApproveLink("N");
                                ae.setShowDisApproveLink("Y");
                            } else {
                                ae.setShowApproveLink("Y");
                                ae.setShowDisApproveLink("N");
                                ae.setStatus("PENDING AT SELF");
                            }
                        } else {
                            if (rs.getString("dverifier_emp_id") != null && rs.getString("dreviewer_emp_id") != null && !rs.getString("dreviewer_emp_id").equals("") && (rs.getString("acceptor_emp_id") == null || rs.getString("acceptor_emp_id").equals(""))) {
                                ae.setShowApproveLink("Y");
                            } else if (rs.getString("acceptor_emp_id") != null && !rs.getString("acceptor_emp_id").equals("")) {
                                ae.setShowDisApproveLink("Y");
                            } else {
                                ae.setShowApproveLink("N");
                                ae.setShowDisApproveLink("N");
                            }
                        }
                    }

                    li.add(ae);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public void saveAERSanctionedPostData(String offCode, AnnualEstablishment bean, String ddocode) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement pst2 = null;
        PreparedStatement pst3 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;

        PreparedStatement pst4 = null;
        ResultSet rs4 = null;

        boolean found = false;
        try {
            con = this.dataSource.getConnection();

            if (bean.getAerId() > 0) {
                String sql = "UPDATE aer_report_submit SET fy=?,sancnosgrp_a=?,sancnosgrp_b=?,sancnosgrp_c=?,sancnosgrp_d=?,grant_in_aid=? WHERE aer_id=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, bean.getFinancialYear());
                if (bean.getGroupAData() != null && !bean.getGroupAData().equals("")) {
                    pst.setInt(2, Integer.parseInt(bean.getGroupAData()));
                } else {
                    pst.setInt(2, 0);
                }
                if (bean.getGroupBData() != null && !bean.getGroupBData().equals("")) {
                    pst.setInt(3, Integer.parseInt(bean.getGroupBData()));
                } else {
                    pst.setInt(3, 0);
                }
                if (bean.getGroupCData() != null && !bean.getGroupCData().equals("")) {
                    pst.setInt(4, Integer.parseInt(bean.getGroupCData()));
                } else {
                    pst.setInt(4, 0);
                }
                if (bean.getGroupDData() != null && !bean.getGroupDData().equals("")) {
                    pst.setInt(5, Integer.parseInt(bean.getGroupDData()));
                } else {
                    pst.setInt(5, 0);
                }
                if (bean.getGrantInAid() != null && !bean.getGrantInAid().equals("")) {
                    pst.setInt(6, Integer.parseInt(bean.getGrantInAid()));
                } else {
                    pst.setInt(6, 0);
                }
                pst.setInt(7, bean.getAerId());
                pst.executeUpdate();

                pst2 = con.prepareStatement("DELETE FROM aer2_co_mapping WHERE aer_id=?");
                pst2.setInt(1, bean.getAerId());
                pst2.execute();

                pst2 = con.prepareStatement("INSERT INTO aer2_co_mapping (bill_group_id, aer_id, off_code, fy) VALUES (?,?,?,?) ");

                pst3 = con.prepareStatement("select bill_group_id from g_section "
                        + " inner join bill_section_mapping on g_section.section_id=bill_section_mapping.section_id where bill_type='REGULAR' and off_code=? ");

                String subofficecode = "";
                pst4 = con.prepareStatement("select * from g_office where ddo_code=? and online_bill_submission='Y'");
                pst4.setString(1, ddocode);
                rs4 = pst4.executeQuery();
                while (rs4.next()) {
                    subofficecode = rs4.getString("off_code");

                    pst3.setString(1, subofficecode);
                    rs2 = pst3.executeQuery();
                    while (rs2.next()) {
                        pst2.setBigDecimal(1, rs2.getBigDecimal("bill_group_id"));
                        pst2.setInt(2, bean.getAerId());
                        pst2.setString(3, offCode);
                        pst2.setString(4, bean.getFinancialYear());
                        pst2.executeUpdate();
                    }
                }

            } else {

                String sql = "INSERT INTO aer_report_submit(off_code,controlling_spc,file_name,status,fy,sancnosgrp_a,sancnosgrp_b ,sancnosgrp_c ,sancnosgrp_d,grant_in_aid, is_singleco) values(?,?,?,?,?,?,?,?,?,?,?)";
                pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pst.setString(1, offCode);
                pst.setString(2, "");
                pst.setString(3, "");
                pst.setString(4, "");
                pst.setString(5, bean.getFinancialYear());
                if (bean.getGroupAData() != null && !bean.getGroupAData().equals("")) {
                    pst.setInt(6, Integer.parseInt(bean.getGroupAData()));
                } else {
                    pst.setInt(6, 0);
                }
                if (bean.getGroupBData() != null && !bean.getGroupBData().equals("")) {
                    pst.setInt(7, Integer.parseInt(bean.getGroupBData()));
                } else {
                    pst.setInt(7, 0);
                }
                if (bean.getGroupCData() != null && !bean.getGroupCData().equals("")) {
                    pst.setInt(8, Integer.parseInt(bean.getGroupCData()));
                } else {
                    pst.setInt(8, 0);
                }
                if (bean.getGroupDData() != null && !bean.getGroupDData().equals("")) {
                    pst.setInt(9, Integer.parseInt(bean.getGroupDData()));
                } else {
                    pst.setInt(9, 0);
                }
                if (bean.getGrantInAid() != null && !bean.getGrantInAid().equals("")) {
                    pst.setInt(10, Integer.parseInt(bean.getGrantInAid()));
                } else {
                    pst.setInt(10, 0);
                }

                if (bean.getSingleCO().equalsIgnoreCase("Y")) {
                    pst.setInt(11, 1);
                } else {
                    pst.setInt(11, 0);
                }

                pst.execute();
                ResultSet insrs = pst.getGeneratedKeys();
                int aerId = 0;
                if (insrs.next()) {
                    aerId = insrs.getInt(1);
                }

                //PreparedStatement psRet = con.prepareStatement("SELECT off_code, fy, bill_group_id from  aer2_co_mapping where off_code=? and bill_group_id=? and fy=?");
                pst2 = con.prepareStatement("INSERT INTO aer2_co_mapping (bill_group_id, aer_id, off_code, fy) VALUES (?,?,?,?) ");

                pst3 = con.prepareStatement("select bill_group_id from g_section "
                        + " inner join bill_section_mapping on g_section.section_id=bill_section_mapping.section_id where bill_type='REGULAR' and off_code=?");

                if (bean.getBillGrpId() != null) {
                    for (int i = 0; i < bean.getBillGrpId().length; i++) {
                        pst2.setBigDecimal(1, bean.getBillGrpId()[i]);
                        pst2.setInt(2, aerId);
                        pst2.setString(3, offCode);
                        pst2.setString(4, bean.getFinancialYear());
                        pst2.executeUpdate();
                    }
                } else {
                    String subofficecode = "";
                    pst4 = con.prepareStatement("select * from g_office where ddo_code=? and online_bill_submission='Y'");
                    pst4.setString(1, ddocode);
                    rs4 = pst4.executeQuery();
                    while (rs4.next()) {
                        subofficecode = rs4.getString("off_code");

                        pst3.setString(1, subofficecode);
                        rs2 = pst3.executeQuery();
                        while (rs2.next()) {
                            pst2.setBigDecimal(1, rs2.getBigDecimal("bill_group_id"));
                            pst2.setInt(2, aerId);
                            pst2.setString(3, offCode);
                            pst2.setString(4, bean.getFinancialYear());
                            pst2.executeUpdate();
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(rs2, pst3);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public AnnualEstablishment geteditAerdata(int aerid) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String status = "";
        AnnualEstablishment aer = new AnnualEstablishment();
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT aer_id, fy, sancnosgrp_a, sancnosgrp_b , sancnosgrp_c , sancnosgrp_d, grant_in_aid, is_singleco FROM aer_report_submit WHERE aer_id=?");
            ps.setInt(1, aerid);
            rs = ps.executeQuery();
            if (rs.next()) {
                aer.setAerId(rs.getInt("aer_id"));
                aer.setGroupAData(rs.getInt("sancnosgrp_a") + "");
                aer.setGroupBData(rs.getInt("sancnosgrp_b") + "");
                aer.setGroupCData(rs.getInt("sancnosgrp_c") + "");
                aer.setGroupDData(rs.getInt("sancnosgrp_d") + "");
                aer.setGrantInAid(rs.getInt("grant_in_aid") + "");
                aer.setFinancialYear(rs.getString("fy"));
                aer.setSingleCO(rs.getInt("is_singleco") + "");

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return aer;
    }

    @Override
    public AnnualEstablishment submittedforCurrentFinancialYear(String offcode, AnnualEstablishment ae) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String status = "";
        AnnualEstablishment aer = new AnnualEstablishment();
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT status,taskid FROM aer_report_submit WHERE  off_code=? and aer_id=?");

            ps.setString(1, offcode);
            ps.setInt(2, ae.getAerId());
            rs = ps.executeQuery();
            if (rs.next()) {
                status = rs.getString("status");
                aer.setStatus(status);
                aer.setTaskid(rs.getInt("taskid"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return aer;
    }

    @Override
    public List getAerReportList(String offcode, int aerId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<AnnualEstablishment> li = new ArrayList<AnnualEstablishment>();
        AnnualEstablishment ae = null;
        int i = 0;
        try {
            con = this.dataSource.getConnection();

            ps = con.prepareStatement("SELECT aer_id, a.off_code, spn controlling_spc, file_name,  fy, submitted_on, taskid, d.status_name \n"
                    + "FROM aer_report_submit a, G_SPC b, task_master c, g_process_status d WHERE a.taskid=c.task_id and  a.controlling_spc=b.spc and c.status_id=d.status_id and  a.off_code=?  and aer_id=? ");
            ps.setString(1, offcode);
            ps.setInt(2, aerId);
            rs = ps.executeQuery();
            while (rs.next()) {
                i++;
                ae = new AnnualEstablishment();
                ae.setSerialno(i);
                ae.setOffCode(rs.getString("off_code"));
                ae.setControllingSpc(rs.getString("controlling_spc"));
                ae.setFileName(rs.getString("file_name"));
                ae.setStatus(rs.getString("status_name"));
                ae.setFy(rs.getString("fy"));
                ae.setTaskid(rs.getInt("taskid"));
                ae.setSubmittedDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("submitted_on")));

                li.add(ae);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List<AnnualEstablishment> getAnnualEstablistmentReportListFromAuthLogin(String taskId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<AnnualEstablishment> li = new ArrayList<AnnualEstablishment>();
        AnnualEstablishment ae = null;
        int i = 0;
        try {
            con = this.dataSource.getConnection();

            ps = con.prepareStatement("select (meninposition(gpc,pay_scale, post_grp, pay_scale_7th_pay , gp , pay_gp_level , off_code,'NT','NP' ) + meninposition(gpc,pay_scale, post_grp, pay_scale_7th_pay , gp , pay_gp_level , off_code,'NT','P' )) meninPosition, "
                    + "	post, sanction_strength, gpc, pay_scale, post_grp, pay_scale_7th_pay, GP, pay_gp_level, off_code from ( "
                    + " select COUNT(*) sanction_strength,gpc, pay_scale, post_grp,pay_scale_7th_pay,GP,pay_gp_level, a.off_code  from g_spc a, aer_report_submit b  "
                    + " where a.off_code=b.off_code and taskid=? group by gpc, pay_scale, post_grp,pay_scale_7th_pay,GP,pay_gp_level, a.off_code) g_spc "
                    + " inner join g_post on g_spc.gpc=g_post.post_code order by post ");
            ps.setInt(1, Integer.parseInt(taskId));
            rs = ps.executeQuery();
            while (rs.next()) {
                i++;
                ae = new AnnualEstablishment();
                ae.setSerialno(i);
                ae.setGpc(rs.getString("gpc"));
                ae.setOffCode(rs.getString("off_code"));
                ae.setPostname(rs.getString("post"));
                ae.setGroup(rs.getString("post_grp"));
                ae.setScaleofPay(rs.getString("pay_scale"));
                ae.setGp(rs.getString("GP"));
                ae.setScaleofPay7th(rs.getString("pay_scale_7th_pay"));
                ae.setLevel(rs.getString("pay_gp_level"));
                ae.setSanctionedStrength(rs.getInt("sanction_strength"));
                int meninPosition = rs.getInt("meninPosition");
                int vacant = ae.getSanctionedStrength() - meninPosition;
                ae.setMeninPosition(meninPosition);
                ae.setVacancyPosition(vacant);

                li.add(ae);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public AerListBox getAnnualEstablistmentReportList(String offcode, int aerid, int isSingle) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<AnnualEstablishment> li = new ArrayList<AnnualEstablishment>();
        AnnualEstablishment ae = null;
        AerListBox aebox = new AerListBox();
        int postA = 0;
        int postB = 0;
        int postC = 0;
        int postD = 0;
        int grant = 0;
        int i = 0;

        try {
            con = this.dataSource.getConnection();

            /*
             isSingleCo ===
             This column is used for multiple Co 2 submit after some data inserted before going to implement this Logic
             */
            if (isSingle == 1) {
                ps = con.prepareStatement("select meninposition(gpc,pay_scale, post_grp, pay_scale_7th_pay , gp , pay_gp_level , off_code, is_teaching_post,IS_PLAN ) meninPosition, post, sanction_strength, gpc, pay_scale, post_grp, pay_scale_7th_pay, GP, pay_gp_level, off_code from ( \n"
                        + "SELECT COUNT(*) sanction_strength,gpc, pay_scale, post_grp,pay_scale_7th_pay,GP,pay_gp_level, off_code,is_teaching_post,IS_PLAN \n"
                        + "from g_spc where  off_code=? group by gpc, pay_scale, post_grp,pay_scale_7th_pay,GP,pay_gp_level, off_code,is_teaching_post,IS_PLAN) g_spc\n"
                        + "inner join g_post on g_spc.gpc=g_post.post_code order by post_grp, pay_scale desc, pay_scale_7th_pay desc, post ");
                ps.setString(1, offcode);
            } else {
                ps = con.prepareStatement("select meninposition(gpc,pay_scale, post_grp, pay_scale_7th_pay , gp , pay_gp_level , off_code, is_teaching_post, IS_PLAN ) meninPosition, post, sanction_strength, gpc, pay_scale, post_grp, pay_scale_7th_pay, GP, pay_gp_level, off_code from\n"
                        + " (SELECT COUNT(*) sanction_strength,gpc, pay_scale, post_grp,pay_scale_7th_pay,GP,pay_gp_level, g_spc.off_code,is_teaching_post,IS_PLAN FROM aer2_co_mapping \n"
                        + " INNER JOIN bill_section_mapping ON aer2_co_mapping.bill_group_id=bill_section_mapping.bill_group_id\n"
                        + " INNER JOIN section_post_mapping ON bill_section_mapping.section_id = section_post_mapping.section_id\n"
                        + " INNER JOIN g_spc ON section_post_mapping.SPC = g_spc.SPC\n"
                        + " WHERE AER_ID=? group by gpc, pay_scale, post_grp,pay_scale_7th_pay,GP,pay_gp_level, g_spc.off_code,is_teaching_post,IS_PLAN)g_spc\n"
                        + " inner join g_post on g_spc.gpc=g_post.post_code order by post_grp, pay_scale desc, pay_scale_7th_pay desc, post");

                ps.setInt(1, aerid);
            }

            rs = ps.executeQuery();
            while (rs.next()) {
                i++;
                ae = new AnnualEstablishment();
                ae.setSerialno(i);
                ae.setGpc(rs.getString("gpc"));
                ae.setOffCode(rs.getString("off_code"));
                ae.setPostname(rs.getString("post"));
                ae.setGroup(rs.getString("post_grp"));

                if (ae.getGroup() != null && !ae.getGroup().equals("")) {
                    if (ae.getGroup().equalsIgnoreCase("A")) {
                        postA = postA + rs.getInt("sanction_strength");
                    } else if (ae.getGroup().equalsIgnoreCase("B")) {
                        postB = postB + rs.getInt("sanction_strength");
                    } else if (ae.getGroup().equalsIgnoreCase("C")) {
                        postC = postC + rs.getInt("sanction_strength");
                    } else if (ae.getGroup().equalsIgnoreCase("D")) {
                        postD = postD + rs.getInt("sanction_strength");
                    }
                }

                ae.setScaleofPay(rs.getString("pay_scale"));
                ae.setGp(rs.getString("GP"));
                ae.setScaleofPay7th(rs.getString("pay_scale_7th_pay"));
                ae.setLevel(rs.getString("pay_gp_level"));
                ae.setSanctionedStrength(rs.getInt("sanction_strength"));
                int meninPosition = rs.getInt("meninPosition");
                int vacant = ae.getSanctionedStrength() - meninPosition;
                ae.setMeninPosition(meninPosition);
                ae.setVacancyPosition(vacant);

                li.add(ae);
            }
            aebox.setLi(li);
            aebox.setGroupADataSystem(postA + "");
            aebox.setGroupBDataSystem(postB + "");
            aebox.setGroupCDataSystem(postC + "");
            aebox.setGroupDDataSystem(postD + "");
            aebox.setGrantInAidSystem(grant + "");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return aebox;
    }

    @Override
    public void addAERMaster(AnnualEstablishment ae, String empId, int aerId) {
        Connection con = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps4 = null;
        ResultSet rs2 = null;
        int mcode = 0;
        try {

            con = this.dataSource.getConnection();

            String startTime = "";
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            startTime = dateFormat.format(cal.getTime());

            ps2 = con.prepareStatement("update aer_report_submit set controlling_spc=?, file_name=?, status=?, taskid=?, submitted_on=? where off_code=? and fy=? and aer_id=?");
            ps2.setString(1, ae.getControllingSpc());
            ps2.setString(2, "AER_" + ae.getOffCode() + "_" + aerId + ".pdf");
            ps2.setString(3, "YES");
            ps2.setInt(4, mcode);
            ps2.setTimestamp(5, new Timestamp(dateFormat.parse(startTime).getTime()));
            ps2.setString(6, ae.getOffCode());
            ps2.setString(7, ae.getFy());
            ps2.setInt(8, aerId);
            ps2.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs2);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public boolean privewDataInserted(int aerId) {
        Connection con = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        boolean foundData = false;
        try {

            con = this.dataSource.getConnection();
            ps2 = con.prepareStatement("select count(*) cnt from annual_establish_report WHERE aer_id=?");
            ps2.setInt(1, aerId);
            rs = ps2.executeQuery();
            if (rs.next()) {
                if (rs.getInt("cnt") > 0) {
                    foundData = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps2);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return foundData;
    }

    @Override
    public void submitEstablishmentReportAsOperator(int aerId, String offCode, String empId, String spc) {
        Connection con = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps4 = null;
        ResultSet rs2 = null;
        int mcode = 0;
        String sql = "";
        try {

            con = this.dataSource.getConnection();

            String startTime = "";
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            startTime = dateFormat.format(cal.getTime());

            ps2 = con.prepareStatement("update aer_report_submit set is_operator_submitted=?, operator_emp_id=?, operator_spc=?, file_name=?, submitted_on=?, status=?, revert_reason=? where off_code=? and aer_id=?");
            ps2.setString(1, "Y");
            ps2.setString(2, empId);
            ps2.setString(3, spc);
            ps2.setString(4, "AER_" + offCode + "_" + aerId + ".pdf");
            ps2.setTimestamp(5, new Timestamp(dateFormat.parse(startTime).getTime()));
            ps2.setString(6, "PENDING AT APPROVER");
            ps2.setString(7, "");
            ps2.setString(8, offCode);
            ps2.setInt(9, aerId);

            ps2.execute();

            DataBaseFunctions.closeSqlObjects(ps2);
            /*
             sql = "DELETE FROM annual_establish_report WHERE aer_id=?";
             ps2 = con.prepareStatement(sql);
             ps2.setInt(1, aerId);
             ps2.execute();
             */

            sql = "INSERT INTO aer_comm_log(aer_id,action_date,action_taken_by_empid,action_taken_by_spc,message_if_any,user_type) values(?,?,?,?,?,?)";
            ps2 = con.prepareStatement(sql);
            ps2.setInt(1, aerId);
            ps2.setTimestamp(2, new Timestamp(dateFormat.parse(startTime).getTime()));
            ps2.setString(3, empId);
            ps2.setString(4, spc);
            ps2.setString(5, null);
            ps2.setString(6, "OPERATOR");
            ps2.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs2);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void addAEReportDataPartA(int aerId, String postGroup, String cadreCode) {
        Connection con = null;
        PreparedStatement ps4 = null;
        try {
            con = this.dataSource.getConnection();

            if (postGroup.equals("A") && cadreCode != null && !cadreCode.equals("")) {

                ps4 = con.prepareStatement("insert into annual_establish_report (off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength,menin_position,vacancy_position,gp,aer_id,aer_part_type,cadre_code) \n"
                        + " SELECT OFF_CODE,gpc,post, POST_GRP,level_7thpay,pay_scale,sanction_strength, meninposition(gpc,pay_scale,post_grp,level_7thpay::TEXT,GP,OFF_CODE,'" + cadreCode + "'," + aerId + ",'REGULAR') person_in_position,0, GP," + aerId + ",'PART-A' aer_part_type,'" + cadreCode + "' FROM(\n"
                        + " SELECT COUNT(*) sanction_strength,GPC,pay_scale,post_grp,level_7thpay, G_SPC.GP,bill_group_master.OFF_CODE FROM aer2_co_mapping\n"
                        + " inner join bill_section_mapping on aer2_co_mapping.BILL_GROUP_ID=bill_section_mapping.BILL_GROUP_ID\n"
                        + " inner join bill_group_master on bill_section_mapping.BILL_GROUP_ID=bill_group_master.BILL_GROUP_ID "
                        + " inner join g_section on bill_section_mapping.section_id=g_section.section_id\n"
                        + " INNER JOIN SECTION_POST_MAPPING ON BILL_SECTION_MAPPING.SECTION_ID = SECTION_POST_MAPPING.SECTION_ID\n"
                        + " INNER JOIN G_SPC ON SECTION_POST_MAPPING.SPC = G_SPC.SPC\n"
                        + " LEFT OUTER JOIN EMP_MAST ON SECTION_POST_MAPPING.SPC = EMP_MAST.CUR_SPC "
                        + " INNER JOIN G_CADRE ON EMP_MAST.CUR_CADRE_CODE=G_CADRE.CADRE_CODE "
                        + " WHERE AER_ID=? and (g_section.bill_type='REGULAR' OR g_section.bill_type='SP_CATGORY') and bill_group_master.bill_type = '42' "
                        + " and bill_group_master.sub_minor_head2 not in ('02002','03003','78012','02003','03001','03002','41078','02001','09001','02004')"
                        + " AND POST_GRP=? AND cadre_type='" + cadreCode + "'"
                        + " group by gpc, pay_scale, post_grp,level_7thpay,G_SPC.GP, bill_group_master.OFF_CODE) G_SPC "
                        + " INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE order by post_grp, level_7thpay desc,pay_scale desc, post");

            } else {
                if (postGroup.equals("D")) {
                    ps4 = con.prepareStatement("insert into annual_establish_report (off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength,menin_position,vacancy_position,gp,aer_id,aer_part_type,cadre_code) \n"
                            + " SELECT OFF_CODE,gpc,post, POST_GRP,level_7thpay,pay_scale,sanction_strength, meninposition(gpc,pay_scale,post_grp,level_7thpay::TEXT,GP,OFF_CODE,'N'," + aerId + ",'REGULAR') person_in_position,0, GP," + aerId + ",'PART-A' aer_part_type,'' FROM(\n"
                            + " SELECT COUNT(*) sanction_strength,GPC,pay_scale,post_grp,level_7thpay, G_SPC.GP,bill_group_master.OFF_CODE FROM aer2_co_mapping\n"
                            + " inner join bill_section_mapping on aer2_co_mapping.BILL_GROUP_ID=bill_section_mapping.BILL_GROUP_ID"
                            + " inner join bill_group_master on bill_section_mapping.BILL_GROUP_ID=bill_group_master.BILL_GROUP_ID "
                            + " inner join g_section on bill_section_mapping.section_id=g_section.section_id\n"
                            + " INNER JOIN SECTION_POST_MAPPING ON BILL_SECTION_MAPPING.SECTION_ID = SECTION_POST_MAPPING.SECTION_ID\n"
                            + " INNER JOIN G_SPC ON SECTION_POST_MAPPING.SPC = G_SPC.SPC\n"
                            + " LEFT OUTER JOIN EMP_MAST ON SECTION_POST_MAPPING.SPC = EMP_MAST.CUR_SPC "
                            + " LEFT OUTER JOIN G_CADRE ON EMP_MAST.CUR_CADRE_CODE=G_CADRE.CADRE_CODE "
                            + " WHERE AER_ID=? and (g_section.bill_type='REGULAR' or g_section.bill_type='CONT6_REG' or g_section.bill_type='SP_CATGORY') and bill_group_master.bill_type = '42' "
                            + " and bill_group_master.sub_minor_head2 not in ('02002','03003','78012','02003','03001','03002','41078','02001','09001','02004')"
                            + " AND (POST_GRP=? or POST_GRP is null) AND is_sanctioned='Y' AND ifuclean!='Y' AND (((cadre_type<>'AIS' AND cadre_type<>'UGC' AND cadre_type<>'OJS') or cadre_type is null) OR CUR_CADRE_CODE IS NULL ) \n"
                            + " group by gpc, pay_scale, post_grp,level_7thpay,G_SPC.GP, bill_group_master.OFF_CODE) G_SPC\n"
                            + " INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE order by post_grp, level_7thpay desc,pay_scale desc, post");
                } else {
                    ps4 = con.prepareStatement("insert into annual_establish_report (off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength,menin_position,vacancy_position,gp,aer_id,aer_part_type,cadre_code) \n"
                            + " SELECT OFF_CODE,gpc,post, POST_GRP,level_7thpay,pay_scale,sanction_strength, meninposition(gpc,pay_scale,post_grp,level_7thpay::TEXT,GP,OFF_CODE,'N'," + aerId + ",'REGULAR') person_in_position,0, GP," + aerId + ",'PART-A' aer_part_type,'' FROM(\n"
                            + " SELECT COUNT(*) sanction_strength,GPC,pay_scale,post_grp,level_7thpay, G_SPC.GP,bill_group_master.OFF_CODE FROM aer2_co_mapping\n"
                            + " inner join bill_section_mapping on aer2_co_mapping.BILL_GROUP_ID=bill_section_mapping.BILL_GROUP_ID"
                            + " inner join bill_group_master on bill_section_mapping.BILL_GROUP_ID=bill_group_master.BILL_GROUP_ID "
                            + " inner join g_section on bill_section_mapping.section_id=g_section.section_id\n"
                            + " INNER JOIN SECTION_POST_MAPPING ON BILL_SECTION_MAPPING.SECTION_ID = SECTION_POST_MAPPING.SECTION_ID\n"
                            + " INNER JOIN G_SPC ON SECTION_POST_MAPPING.SPC = G_SPC.SPC\n"
                            + " LEFT OUTER JOIN EMP_MAST ON SECTION_POST_MAPPING.SPC = EMP_MAST.CUR_SPC "
                            + " LEFT OUTER JOIN G_CADRE ON EMP_MAST.CUR_CADRE_CODE=G_CADRE.CADRE_CODE "
                            + " WHERE AER_ID=? and (g_section.bill_type='REGULAR' or g_section.bill_type='CONT6_REG' or g_section.bill_type='SP_CATGORY') and bill_group_master.bill_type = '42' "
                            + " and bill_group_master.sub_minor_head2 not in ('02002','03003','78012','02003','03001','03002','41078','02001','09001','02004')"
                            + " AND POST_GRP=? AND is_sanctioned='Y' AND ifuclean!='Y' AND (((cadre_type<>'AIS' AND cadre_type<>'UGC' AND cadre_type<>'OJS') or cadre_type is null) OR CUR_CADRE_CODE IS NULL ) \n"
                            + " group by gpc, pay_scale, post_grp,level_7thpay,G_SPC.GP, bill_group_master.OFF_CODE) G_SPC\n"
                            + " INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE order by post_grp, level_7thpay desc,pay_scale desc, post");
                }
            }
            ps4.setInt(1, aerId);
            ps4.setString(2, postGroup);
            ps4.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps4);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void addAEReportDataPartB(int aerId, String postGroup, String cadreCode) {
        Connection con = null;
        PreparedStatement ps4 = null;
        try {
            con = this.dataSource.getConnection();

            if (postGroup.equals("A") && cadreCode != null && !cadreCode.equals("")) {

                ps4 = con.prepareStatement("insert into annual_establish_report (off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength,menin_position,vacancy_position,gp,aer_id,aer_part_type,cadre_code) \n"
                        + " SELECT OFF_CODE,gpc,post, POST_GRP,level_7thpay,pay_scale,sanction_strength, meninposition(gpc,pay_scale,post_grp,level_7thpay::TEXT,GP,OFF_CODE,'" + cadreCode + "'," + aerId + ",'GIA') person_in_position,0, GP," + aerId + ",'PART-B' aer_part_type,'" + cadreCode + "' FROM(\n"
                        + " SELECT COUNT(*) sanction_strength,GPC,pay_scale,post_grp,level_7thpay, G_SPC.GP,bill_group_master.OFF_CODE FROM aer2_co_mapping\n"
                        + " inner join bill_section_mapping on aer2_co_mapping.BILL_GROUP_ID=bill_section_mapping.BILL_GROUP_ID\n"
                        + " inner join bill_group_master on bill_section_mapping.BILL_GROUP_ID=bill_group_master.BILL_GROUP_ID "
                        + " inner join g_section on bill_section_mapping.section_id=g_section.section_id\n"
                        + " INNER JOIN SECTION_POST_MAPPING ON BILL_SECTION_MAPPING.SECTION_ID = SECTION_POST_MAPPING.SECTION_ID\n"
                        + " INNER JOIN G_SPC ON SECTION_POST_MAPPING.SPC = G_SPC.SPC\n"
                        + " LEFT OUTER JOIN EMP_MAST ON SECTION_POST_MAPPING.SPC = EMP_MAST.CUR_SPC "
                        + " INNER JOIN G_CADRE ON EMP_MAST.CUR_CADRE_CODE=G_CADRE.CADRE_CODE "
                        + " WHERE AER_ID=? and (g_section.bill_type='REGULAR' OR g_section.bill_type='SP_CATGORY') and (bill_group_master.bill_type = '21' or bill_group_master.bill_type = '69') "
                        + " and bill_group_master.sub_minor_head2 not in ('02002','03003','78012','02003','03001','03002','41078','02001','09001','02004')"
                        + " AND POST_GRP=? AND cadre_type='" + cadreCode + "'"
                        + " group by gpc, pay_scale, post_grp,level_7thpay,G_SPC.GP, bill_group_master.OFF_CODE) G_SPC "
                        + " INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE order by post_grp, level_7thpay desc,pay_scale desc, post");

            } else {
                ps4 = con.prepareStatement("insert into annual_establish_report (off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength,menin_position,vacancy_position,gp,aer_id,aer_part_type,cadre_code) \n"
                        + " SELECT OFF_CODE,gpc,post, POST_GRP,level_7thpay,pay_scale,sanction_strength, meninposition(gpc,pay_scale,post_grp,level_7thpay::TEXT,GP,OFF_CODE,'N'," + aerId + ",'GIA') person_in_position,0, GP," + aerId + ",'PART-B' aer_part_type,'' FROM(\n"
                        + " SELECT COUNT(*) sanction_strength,GPC,pay_scale,post_grp,level_7thpay, G_SPC.GP,bill_group_master.OFF_CODE FROM aer2_co_mapping\n"
                        + " inner join bill_section_mapping on aer2_co_mapping.BILL_GROUP_ID=bill_section_mapping.BILL_GROUP_ID "
                        + " inner join bill_group_master on bill_section_mapping.BILL_GROUP_ID=bill_group_master.BILL_GROUP_ID "
                        + " inner join g_section on bill_section_mapping.section_id=g_section.section_id\n"
                        + " INNER JOIN SECTION_POST_MAPPING ON BILL_SECTION_MAPPING.SECTION_ID = SECTION_POST_MAPPING.SECTION_ID\n"
                        + " INNER JOIN G_SPC ON SECTION_POST_MAPPING.SPC = G_SPC.SPC\n"
                        + " LEFT OUTER JOIN EMP_MAST ON SECTION_POST_MAPPING.SPC = EMP_MAST.CUR_SPC "
                        + " LEFT OUTER JOIN G_CADRE ON EMP_MAST.CUR_CADRE_CODE=G_CADRE.CADRE_CODE "
                        + " WHERE AER_ID=? and (g_section.bill_type='REGULAR' OR g_section.bill_type='SP_CATGORY') and (bill_group_master.bill_type = '21' or bill_group_master.bill_type = '69') "
                        + " and bill_group_master.sub_minor_head2 not in ('02002','03003','78012','02003','03001','03002','41078','02001','09001','02004')"
                        + " AND (POST_GRP=? or POST_GRP is null) AND (((cadre_type<>'AIS' AND cadre_type<>'UGC' AND cadre_type<>'OJS') or cadre_type is null) OR CUR_CADRE_CODE IS NULL ) \n"
                        + " group by gpc, pay_scale, post_grp,level_7thpay,G_SPC.GP, bill_group_master.OFF_CODE) G_SPC\n"
                        + " INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE order by post_grp, level_7thpay desc,pay_scale desc, post");
            }
            ps4.setInt(1, aerId);
            ps4.setString(2, postGroup);
            ps4.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps4);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void addAEReportDataPartC(int aerId) {
        Connection con = null;
        PreparedStatement ps4 = null;
        try {
            con = this.dataSource.getConnection();

            ps4 = con.prepareStatement("insert into annual_establish_report (off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength,menin_position,vacancy_position,gp,aer_id,aer_part_type,cadre_code) \n"
                    + " SELECT OFF_CODE,gpc,post, POST_GRP,level_7thpay,pay_scale,sanction_strength, meninposition(gpc,pay_scale,post_grp,level_7thpay::TEXT,GP,OFF_CODE,'N'," + aerId + ",'CONT_6') person_in_position,0, GP," + aerId + ",'PART-C' aer_part_type,'' FROM(\n"
                    + " SELECT COUNT(*) sanction_strength,GPC,pay_scale,post_grp,level_7thpay, G_SPC.GP,g_section.OFF_CODE FROM aer2_co_mapping\n"
                    + " inner join bill_section_mapping on aer2_co_mapping.BILL_GROUP_ID=bill_section_mapping.BILL_GROUP_ID\n"
                    + " inner join g_section on bill_section_mapping.section_id=g_section.section_id\n"
                    + " INNER JOIN SECTION_POST_MAPPING ON BILL_SECTION_MAPPING.SECTION_ID = SECTION_POST_MAPPING.SECTION_ID\n"
                    + " INNER JOIN G_SPC ON SECTION_POST_MAPPING.SPC = G_SPC.SPC\n"
                    + " LEFT OUTER JOIN EMP_MAST ON SECTION_POST_MAPPING.SPC = EMP_MAST.CUR_SPC\n"
                    + " WHERE AER_ID=? and g_section.bill_type='CONT6_REG'\n"
                    + " group by gpc, pay_scale, post_grp,level_7thpay,G_SPC.GP, g_section.OFF_CODE) G_SPC\n"
                    + " INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE order by post_grp, level_7thpay desc,pay_scale desc, post");

            ps4.setInt(1, aerId);
            ps4.execute();

            ps4 = con.prepareStatement("update annual_establish_report set vacancy_position= sanction_strength-menin_position where aer_id=?");
            ps4.setInt(1, aerId);
            ps4.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps4);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateGspcForAERReport(String offcode) {
        Connection con = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps4 = null;
        PreparedStatement ps6 = null;
        PreparedStatement ps8 = null;
        ResultSet rs2 = null;
        ResultSet rs4 = null;
        ResultSet rs8 = null;
        String sql = "";
        String sql2 = "";
        try {
            con = this.dataSource.getConnection();
            sql = "SELECT cur_spc, emp_id FROM emp_mast WHERE cur_off_code=? ";
            ps2 = con.prepareStatement(sql);
            ps2.setString(1, offcode);

            sql2 = "SELECT previous_pay_scale,previous_gp,payrev_fitted_level,PAYSCALE FROM PAY_REVISION_OPTION \n"
                    + "LEFT OUTER JOIN (SELECT DISTINCT PAYSCALE, GP_LEVEL FROM PAY_MATRIX_2017 )  PAY_MATRIX_2017  ON PAY_REVISION_OPTION.payrev_fitted_level=PAY_MATRIX_2017.GP_LEVEL::text\n"
                    + "WHERE emp_id=? and IS_APPROVED_CHECKING_AUTH='Y'";
            ps4 = con.prepareStatement(sql2);

            ps6 = con.prepareStatement(" UPDATE G_SPC SET pay_gp_level=?, pay_scale=?, GP=?, pay_scale_7th_pay=? WHERE spc=? ");

            ps8 = con.prepareStatement("select  min(amt) entry_level from PAY_MATRIX_2017 where PAYSCALE=? and gp=? and GP_LEVEL=? \n"
                    + "UNION\n"
                    + "select  max(amt) entry_level from PAY_MATRIX_2017 where PAYSCALE=? and gp=? and GP_LEVEL=?  ");

            rs2 = ps2.executeQuery();
            while (rs2.next()) {
                ps4.setString(1, rs2.getString("emp_id"));
                rs4 = ps4.executeQuery();
                if (rs4.next()) {

                    String payScale7th = "";

                    ps8.setString(1, rs4.getString("PAYSCALE"));
                    ps8.setInt(2, rs4.getInt("previous_gp"));
                    if (rs4.getString("payrev_fitted_level") != null && !rs4.getString("payrev_fitted_level").equals("")) {
                        ps8.setInt(3, Integer.parseInt(rs4.getString("payrev_fitted_level")));
                    } else {
                        ps8.setInt(3, 0);
                    }

                    ps8.setString(4, rs4.getString("PAYSCALE"));
                    ps8.setInt(5, rs4.getInt("previous_gp"));
                    if (rs4.getString("payrev_fitted_level") != null && !rs4.getString("payrev_fitted_level").equals("")) {
                        ps8.setInt(6, Integer.parseInt(rs4.getString("payrev_fitted_level")));
                    } else {
                        ps8.setInt(6, 0);
                    }

                    rs8 = ps8.executeQuery();
                    while (rs8.next()) {

                        if (payScale7th != null && !payScale7th.equals("")) {
                            payScale7th = payScale7th + "-" + rs8.getString("entry_level");
                        } else {
                            payScale7th = rs8.getString("entry_level");
                        }

                    }

                    ps6.setString(1, rs4.getString("payrev_fitted_level"));
                    ps6.setString(2, rs4.getString("previous_pay_scale"));
                    ps6.setInt(3, rs4.getInt("previous_gp"));
                    ps6.setString(4, payScale7th);
                    ps6.setString(5, rs2.getString("cur_spc"));
                    ps6.execute();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List<AnnualEstablishment> getSubmittedReportList(String offcode, String fy, int aerId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<AnnualEstablishment> li = new ArrayList<AnnualEstablishment>();
        AnnualEstablishment ae = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT off_code ,gpc ,postname ,post_group ,pay_scale_7th_pay ,gp,"
                    + "pay_scale_6th_pay ,level ,sanction_strength ,menin_position ,vacancy_position ,financial_year ,submit_status ,file_path from annual_establish_report where off_code=? and financial_year=? and aer_id=?");

            ps.setString(1, offcode);
            ps.setString(2, fy);
            ps.setInt(3, aerId);
            rs = ps.executeQuery();
            int i = 0;
            while (rs.next()) {
                i++;
                ae = new AnnualEstablishment();
                ae.setSerialno(i);
                ae.setGpc(rs.getString("gpc"));
                ae.setOffCode(rs.getString("off_code"));
                ae.setPostname(rs.getString("postname"));
                ae.setGroup(rs.getString("post_group"));
                ae.setScaleofPay(rs.getString("pay_scale_6th_pay"));
                ae.setGp(rs.getString("GP"));
                ae.setScaleofPay7th(rs.getString("pay_scale_7th_pay"));
                ae.setLevel(rs.getString("level"));
                ae.setSanctionedStrength(rs.getInt("sanction_strength"));
                ae.setMeninPosition(rs.getInt("menin_position"));
                ae.setVacancyPosition(rs.getInt("vacancy_position"));

                li.add(ae);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public Map<String, String> getAuthorityList(String offcode) {
        Map<String, String> map = new HashMap<String, String>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement(" SELECT b.spc,d.post,ARRAY_TO_STRING(ARRAY[c.INITIALS, c.F_NAME, c.M_NAME, c.L_NAME], ' ') EMPNAME from g_office a, g_spc b, emp_mast c, g_post d "
                    + "  where a.co_ddo_code=b.off_code and b.spc=c.cur_spc and b.gpc=d.post_code and a.off_code=? and d.isauthority='Y'");
            ps.setString(1, offcode);
            rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("spc"), rs.getString("post") + ", (" + rs.getString("EMPNAME") + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return map;
    }

    @Override
    public void approvedAER(int taskId) {
        Connection con = null;
        PreparedStatement pst1 = null;
        String sql = "";
        try {
            con = this.dataSource.getConnection();

            pst1 = con.prepareStatement("UPDATE TASK_MASTER SET STATUS_ID=?, APPLY_TO=? WHERE TASK_ID=?");

            pst1.setInt(1, 67);//Completed
            pst1.setString(2, null);
            pst1.setInt(3, taskId);
            pst1.executeUpdate();
            DataBaseFunctions.closeSqlObjects(pst1);

            sql = "UPDATE aer_report_submit SET status=? WHERE taskid=?";
            pst1 = con.prepareStatement(sql);
            pst1.setString(1, "COMP");
            pst1.setInt(2, taskId);
            pst1.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst1);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public String getAERStatus(int taskId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String submitted = "N";
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT STATUS_ID FROM TASK_MASTER WHERE TASK_ID=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, taskId);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getInt("STATUS_ID") == 68) {
                    submitted = "R";
                } else if (rs.getInt("STATUS_ID") == 67) {
                    submitted = "Y";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return submitted;
    }

    @Override
    public void revertAER(int taskId, String serverfilePath, String fileName, String revertReason) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String offCode = "";
        String fy = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT off_code,fy FROM aer_report_submit WHERE taskid=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, taskId);
            rs = pst.executeQuery();
            if (rs.next()) {
                offCode = rs.getString("off_code");
                fy = rs.getString("fy");
            }

            DataBaseFunctions.closeSqlObjects(pst);

            sql = "UPDATE TASK_MASTER SET STATUS_ID=?, PENDING_AT=?,APPLY_TO=?,PENDING_SPC=? WHERE TASK_ID=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, 68);
            pst.setString(2, null);
            pst.setString(3, null);
            pst.setString(4, null);
            pst.setInt(5, taskId);
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            sql = "UPDATE aer_report_submit SET status=?,revert_reason=? WHERE taskid=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, "REV");
            pst.setString(2, revertReason);
            pst.setInt(3, taskId);
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            sql = "DELETE FROM annual_establish_report WHERE off_code=? and financial_year=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, offCode);
            pst.setString(2, fy);
            pst.executeUpdate();

            Path file = Paths.get(serverfilePath, fileName);
            if (Files.exists(file)) {
                Files.deleteIfExists(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public List getScheduleIIData(int aerId, String offCode, int issingleCo) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List data = new ArrayList();

        ScheduleIIBean scbean = null;
        try {
            con = this.dataSource.getConnection();

            if (issingleCo == 1) {
                String sql = "select PAY_SCALE, post_grp from g_spc where off_code=? GROUP BY PAY_SCALE, post_grp order by post_grp, PAY_SCALE desc";

                pst = con.prepareStatement(sql);
                pst.setString(1, offCode);
            } else {
                String sql = "select PAY_SCALE, post_grp from ( "
                        + " select bill_group_id from aer_report_submit ae, aer2_co_mapping co where ae.aer_id=co.aer_id and ae.aer_id=? ) tab "
                        + " left outer join bill_section_mapping bill on tab.bill_group_id=bill.bill_group_id "
                        + " left outer join section_post_mapping sec on bill.section_id=sec.section_id "
                        + " left outer join g_spc on sec .spc=g_spc.spc GROUP BY PAY_SCALE, post_grp order by post_grp, PAY_SCALE desc";

                pst = con.prepareStatement(sql);
                pst.setInt(1, aerId);
            }
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getString("PAY_SCALE") != null && !rs.getString("PAY_SCALE").equals("")) {
                    scbean = new ScheduleIIBean();
                    scbean.setPayscale(rs.getString("PAY_SCALE"));
                    scbean.setTeacherSanctionedStrengthPlan(getSanctionedStrength(aerId, rs.getString("PAY_SCALE"), "T", "P", rs.getString("post_grp")));
                    scbean.setTeacherSanctionedStrengthNonPlan(getSanctionedStrength(aerId, rs.getString("PAY_SCALE"), "T", "NP", rs.getString("post_grp")));
                    scbean.setTeacherSanctionedStrengthTotal(scbean.getTeacherSanctionedStrengthPlan() + scbean.getTeacherSanctionedStrengthNonPlan());
                    scbean.setOthersSanctionedStrengthPlan(getSanctionedStrength(aerId, rs.getString("PAY_SCALE"), "NT", "P", rs.getString("post_grp")));
                    scbean.setOthersSanctionedStrengthNonPlan(getSanctionedStrength(aerId, rs.getString("PAY_SCALE"), "NT", "NP", rs.getString("post_grp")));
                    scbean.setOthersSanctionedStrengthTotal(scbean.getOthersSanctionedStrengthPlan() + scbean.getOthersSanctionedStrengthNonPlan());
                    scbean.setTotalPlan(scbean.getTeacherSanctionedStrengthPlan() + scbean.getOthersSanctionedStrengthPlan());
                    scbean.setTotalNonPlan(scbean.getTeacherSanctionedStrengthNonPlan() + scbean.getOthersSanctionedStrengthNonPlan());
                    scbean.setTotalSanctionedStrength(scbean.getTeacherSanctionedStrengthTotal() + scbean.getOthersSanctionedStrengthTotal());

                    int teacherMenInPositionPlan = getMenInPosition(aerId, rs.getString("PAY_SCALE"), "T", "P", rs.getString("post_grp"));
                    int teacherMenInPositionNonPlan = getMenInPosition(aerId, rs.getString("PAY_SCALE"), "T", "NP", rs.getString("post_grp"));
                    int othersMenInPositionPlan = getMenInPosition(aerId, rs.getString("PAY_SCALE"), "NT", "P", rs.getString("post_grp"));
                    int othersMenInPositionNonPlan = getMenInPosition(aerId, rs.getString("PAY_SCALE"), "NT", "NP", rs.getString("post_grp"));

                    scbean.setTeacherVacancyPositionPlan(scbean.getTeacherSanctionedStrengthPlan() - teacherMenInPositionPlan);
                    scbean.setTeacherVacancyPositionNonPlan(scbean.getTeacherSanctionedStrengthNonPlan() - teacherMenInPositionNonPlan);
                    scbean.setOthersVacancyPositionPlan(scbean.getOthersSanctionedStrengthPlan() - othersMenInPositionPlan);
                    scbean.setOthersVacancyPositionNonPlan(scbean.getOthersSanctionedStrengthNonPlan() - othersMenInPositionNonPlan);
                    scbean.setTotalTeacherVacancyPosition(scbean.getTeacherVacancyPositionPlan() + scbean.getTeacherVacancyPositionNonPlan());
                    scbean.setTotalOthersVacancyPosition(scbean.getOthersVacancyPositionPlan() + scbean.getOthersVacancyPositionNonPlan());

                    data.add(scbean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return data;
    }

    private int getSanctionedStrength(int aerid, String payscale, String isTeacher, String isPlan, String postgroup) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int count = 0;
        try {
            con = this.dataSource.getConnection();
            String sql = "";
            if (isPlan.equals("P")) {
                sql = "select count(*) cnt from ( \n"
                        + "select bill_group_id from aer_report_submit ae, aer2_co_mapping co where ae.aer_id=co.aer_id and  ae.aer_id=?) tab \n"
                        + "left outer join bill_section_mapping bill on tab.bill_group_id=bill.bill_group_id \n"
                        + "left outer join section_post_mapping sec on bill.section_id=sec.section_id \n"
                        + "left outer join g_spc on sec.spc=g_spc.spc where PAY_SCALE is not null and pay_scale=? and (IFUCLEAN!='Y' OR IFUCLEAN IS NULL) \n"
                        + " and is_teaching_post=? and is_plan=? and g_spc.post_grp=? GROUP BY PAY_SCALE order by PAY_SCALE asc";
                pst = con.prepareStatement(sql);
                pst.setInt(1, aerid);
                pst.setString(2, payscale);
                pst.setString(3, isTeacher);
                pst.setString(4, isPlan);
                pst.setString(5, postgroup);
            } else {
                sql = "select count(*) cnt from ( \n"
                        + "select bill_group_id from aer_report_submit ae, aer2_co_mapping co where ae.aer_id=co.aer_id and  ae.aer_id=?) tab \n"
                        + "left outer join bill_section_mapping bill on tab.bill_group_id=bill.bill_group_id \n"
                        + "left outer join section_post_mapping sec on bill.section_id=sec.section_id \n"
                        + "left outer join g_spc on sec .spc=g_spc.spc where PAY_SCALE is not null and pay_scale=? and (IFUCLEAN!='Y' OR IFUCLEAN IS NULL)\n"
                        + " and is_teaching_post=? and (is_plan='NP' or is_plan is null) and g_spc.post_grp=? GROUP BY PAY_SCALE order by PAY_SCALE asc";
                pst = con.prepareStatement(sql);
                pst.setInt(1, aerid);
                pst.setString(2, payscale);
                pst.setString(3, isTeacher);
                pst.setString(4, postgroup);
            }

            rs = pst.executeQuery();
            if (rs.next()) {
                count = rs.getInt("cnt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return count;
    }

    private int getMenInPosition(int aerId, String payscale, String isTeacher, String isPlan, String postgroup) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int count = 0;
        try {
            con = this.dataSource.getConnection();
            String sql = "";
            if (isPlan.equals("P")) {
                sql = " select count(*) cnt  from (select bill_group_id from aer_report_submit ae, aer2_co_mapping co where ae.aer_id=co.aer_id and  ae.aer_id=? ) tab \n"
                        + "left outer join bill_section_mapping bill on tab.bill_group_id=bill.bill_group_id \n"
                        + "left outer join section_post_mapping sec on bill.section_id=sec.section_id \n"
                        + "left outer join g_spc on sec.spc=g_spc.spc \n"
                        + "inner join emp_mast on  g_spc.spc=emp_mast.cur_spc  where  emp_mast.emp_id is not null   and  g_spc.pay_scale=? and is_teaching_post=? and is_plan=? and (IFUCLEAN!='Y' OR IFUCLEAN IS NULL) and g_spc.post_grp=? ";

                pst = con.prepareStatement(sql);
                pst.setInt(1, aerId);
                pst.setString(2, payscale);
                pst.setString(3, isTeacher);
                pst.setString(4, isPlan);
                pst.setString(5, postgroup);
            } else {
                sql = " select count(*) cnt from (select bill_group_id from aer_report_submit ae, aer2_co_mapping co where ae.aer_id=co.aer_id and  ae.aer_id=? ) tab \n"
                        + " left outer join bill_section_mapping bill on tab.bill_group_id=bill.bill_group_id \n"
                        + " left outer join section_post_mapping sec on bill.section_id=sec.section_id \n"
                        + " left outer join g_spc on sec.spc=g_spc.spc \n"
                        + " inner join emp_mast on  g_spc.spc=emp_mast.cur_spc  where  emp_mast.emp_id is not null   and  g_spc.pay_scale=? and is_teaching_post=? and (is_plan='NP' or is_plan is null) and (IFUCLEAN!='Y' OR IFUCLEAN IS NULL) and g_spc.post_grp=? ";
                pst = con.prepareStatement(sql);
                pst.setInt(1, aerId);
                pst.setString(2, payscale);
                pst.setString(3, isTeacher);
                pst.setString(4, postgroup);
            }
            rs = pst.executeQuery();
            if (rs.next()) {
                count = rs.getInt("cnt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return count;
    }

    @Override
    public List departmentWiseAerStatus(String finYear) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        DepartmentWiseAerStatus dwas = null;
        List deptWiseAerStatus = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("SELECT * FROM G_DEPARTMENT WHERE IF_ACTIVE='Y'");
            rs = pst.executeQuery();
            while (rs.next()) {
                dwas = new DepartmentWiseAerStatus();
                dwas.setDeptName(rs.getString("DEPARTMENT_NAME"));
                dwas.setDeptCode(rs.getString("DEPARTMENT_CODE"));
                dwas.setFinYear(finYear);
                dwas.setNoOfDDO(getNoOfDdo(rs.getString("DEPARTMENT_CODE")));;
                dwas.setNoAerSubmitted(getSubmittedAER(rs.getString("DEPARTMENT_CODE"), finYear));
                dwas.setNoOfAerAproved(getApprovedAER(rs.getString("DEPARTMENT_CODE"), finYear));
                deptWiseAerStatus.add(dwas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return deptWiseAerStatus;

    }

    public int getSubmittedAER(String deptCode, String finYear) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        int noOfAreSubmitted = 0;
        try {

            con = this.dataSource.getConnection();
            pst = con.prepareStatement("select count(department_code) cnt from (select off_code from aer_report_submit where  status='YES' and FY=?) tab "
                    + "inner join g_office on tab.off_code=g_office.off_code where department_code=?  group by department_code");
            pst.setString(1, finYear);
            pst.setString(2, deptCode);
            rs = pst.executeQuery();
            if (rs.next()) {
                noOfAreSubmitted = rs.getInt("cnt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return noOfAreSubmitted;
    }

    public int getApprovedAER(String deptCode, String finYear) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        int noOfApprovedAer = 0;
        try {

            con = this.dataSource.getConnection();
            String sql = "select count(department_code) cnt from (select off_code from aer_report_submit where  status='COMP' and FY=?) tab "
                    + "inner join g_office on tab.off_code=g_office.off_code where department_code=?  group by department_code";
            pst = con.prepareStatement(sql);
            pst.setString(1, finYear);
            pst.setString(2, deptCode);
            rs = pst.executeQuery();
            if (rs.next()) {
                noOfApprovedAer = rs.getInt("cnt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return noOfApprovedAer;
    }

    public int getNoOfDdo(String deptCode) {
        int noOfDdo = 0;
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            String sql = "SELECT COUNT(*)NOOFDDO FROM G_OFFICE WHERE DEPARTMENT_CODE=? AND IS_DDO='Y'";
            pst = con.prepareStatement(sql);
            pst.setString(1, deptCode);
            rs = pst.executeQuery();
            if (rs.next()) {
                noOfDdo = rs.getInt("NOOFDDO");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return noOfDdo;
    }

    @Override
    public List aerSubmittedOfficeList(String deptCode, String finYear) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        DepartmentWiseAerStatus dwas = null;
        List offList = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            String sql = "SELECT OFF_EN, AER_ID FROM(SELECT OFF_CODE,OFF_EN FROM G_OFFICE WHERE DEPARTMENT_CODE=? ) OFFICE INNER JOIN "
                    + "(SELECT DISTINCT OFF_CODE, AER_ID FROM AER_REPORT_SUBMIT WHERE FY=? AND (STATUS='YES' OR STATUS='COMP'))AERREPORT ON OFFICE.OFF_CODE=AERREPORT.OFF_CODE";
            pst = con.prepareStatement(sql);
            pst.setString(1, deptCode);
            pst.setString(2, finYear);
            rs = pst.executeQuery();
            while (rs.next()) {
                dwas = new DepartmentWiseAerStatus();
                dwas.setOffName(rs.getString("OFF_EN"));
                dwas.setAerId(rs.getInt("AER_ID"));
                offList.add(dwas);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return offList;
    }

    @Override
    public List aerApprovedOfficeList(String deptCode, String finYear) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        DepartmentWiseAerStatus dwas = null;
        List offList = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            String sql = "SELECT OFF_EN, AER_ID FROM(SELECT OFF_CODE,OFF_EN FROM G_OFFICE WHERE DEPARTMENT_CODE=? ) OFFICE INNER JOIN "
                    + "(SELECT DISTINCT OFF_CODE, AER_ID FROM AER_REPORT_SUBMIT WHERE FY=? AND  STATUS='COMP')AERREPORT ON OFFICE.OFF_CODE=AERREPORT.OFF_CODE";
            pst = con.prepareStatement(sql);
            pst.setString(1, deptCode);
            pst.setString(2, finYear);
            rs = pst.executeQuery();
            while (rs.next()) {
                dwas = new DepartmentWiseAerStatus();
                dwas.setOffName(rs.getString("OFF_EN"));
                dwas.setAerId(rs.getInt("AER_ID"));
                offList.add(dwas);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return offList;
    }

    @Override
    public List getFinancialYearList() {
        List li = new ArrayList();
        try {
            int fystartyear = 0;
            String currentFinancialYear = "";
            int year = Calendar.getInstance().get(Calendar.YEAR);

            int month = Calendar.getInstance().get(Calendar.MONTH) + 1;

            if (month <= 3) {
                currentFinancialYear = (year - 1) + "-" + Integer.toString(year).substring(2, 4);
                fystartyear = (year - 1);
            } else {
                currentFinancialYear = year + "-" + Integer.toString(year + 1).substring(2, 4);
                fystartyear = year;
            }
            li.add(currentFinancialYear);
            currentFinancialYear = (fystartyear - 1) + "-" + Integer.toString(fystartyear).substring(2, 4);
            li.add(currentFinancialYear);

            currentFinancialYear = (fystartyear - 2) + "-" + Integer.toString(fystartyear - 1).substring(2, 4);
            li.add(currentFinancialYear);

            currentFinancialYear = (fystartyear - 3) + "-" + Integer.toString(fystartyear - 2).substring(2, 4);
            li.add(currentFinancialYear);

            currentFinancialYear = (fystartyear - 4) + "-" + Integer.toString(fystartyear - 3).substring(2, 4);
            li.add(currentFinancialYear);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return li;
    }

    @Override
    public List getAERAuthorityList(String offCode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        SelectOption so = null;
        List authList = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT b.spc,d.post,ARRAY_TO_STRING(ARRAY[c.INITIALS, c.F_NAME, c.M_NAME, c.L_NAME], ' ') EMPNAME from g_office a, g_spc b, emp_mast c, g_post d "
                    + "  where a.off_code=? and b.spc=c.cur_spc and b.gpc=d.post_code and c.cur_off_code=? and d.isauthority='Y'";
            pst = con.prepareStatement(sql);
            pst.setString(1, offCode);
            pst.setString(2, offCode);
            rs = pst.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("spc"));
                so.setLabel(rs.getString("post") + ", (" + rs.getString("EMPNAME") + ")");
                authList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return authList;
    }

    @Override
    public int getisSingleCo(int aerId) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        int singleco = 0;
        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("SELECT is_singleco from aer_report_submit where aer_id=?");
            pst.setInt(1, aerId);
            rs = pst.executeQuery();
            if (rs.next()) {
                singleco = rs.getInt("is_singleco");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return singleco;
    }

    @Override
    public boolean duplicateCreateAER(String offCode, String financialYear) {
        Connection con = null;
        boolean status = false;
        PreparedStatement pst = null;
        ResultSet rs = null;
        int singleco = 0;
        boolean found = false;
        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("SELECT IS_SINGLECO FROM aer_report_submit WHERE off_code=? AND fy=?");
            pst.setString(1, offCode);
            pst.setString(2, financialYear);
            rs = pst.executeQuery();
            if (rs.next()) {
                singleco = rs.getInt("IS_SINGLECO");
                found = true;
            }
            DataBaseFunctions.closeSqlObjects(rs, pst);
            if (found) {
                if (singleco == 1) {
                    status = true;
                } else {
                    pst = con.prepareStatement("select aercnt, bgcount from ( "
                            + "select count(*) aercnt, off_code  from aer2_co_mapping  where  off_code=? AND fy=? group by off_code) tab1 "
                            + "left outer join (select count(*) bgcount, off_code  from bill_group_master where off_code=? AND (IS_DELETED='N' OR IS_DELETED IS NULL) group by off_code) tab2 "
                            + "on tab1.off_code=tab2.off_code");
                    pst.setString(1, offCode);
                    pst.setString(2, financialYear);
                    pst.setString(3, offCode);
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        if (rs.getInt("bgcount") == rs.getInt("aercnt")) {
                            status = true;
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return status;
    }

    @Override
    public String getCoType(String offCode, String financialYear) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        String singleco = "";
        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("SELECT is_singleco from aer_report_submit WHERE off_code=? AND fy=?");
            pst.setString(1, offCode);
            pst.setString(2, financialYear);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getInt("is_singleco") == 0) {
                    singleco = "N";
                } else {
                    singleco = "Y";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return singleco;
    }

    @Override
    public String getAERRevertReason(String aerid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String revertReason = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "select revert_reason from aer_report_submit where aer_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(aerid));
            rs = pst.executeQuery();
            if (rs.next()) {
                revertReason = rs.getString("revert_reason");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return revertReason;
    }

    @Override
    public String getAerFileName(String aerId) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String fileName = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "select file_name from aer_report_submit where aer_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(aerId));
            rs = pst.executeQuery();
            if (rs.next()) {
                fileName = rs.getString("file_name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return fileName;
    }

    @Override
    public List getProformaGIAEmployeeData(String offcode, String postgrptype) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ProformaEmployeeDataBean pedbean = null;
        List emplist = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            String sql = "select section_post_mapping.spc,emp_mast.post_grp_type,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,F_NAME, post,cur_basic_salary from bill_group_master,bill_section_mapping,section_post_mapping,emp_mast,g_spc,g_post"
                    + " where bill_group_master.bill_group_id=bill_section_mapping.bill_group_id"
                    + " and bill_section_mapping.section_id=section_post_mapping.section_id"
                    + " and section_post_mapping.spc=emp_mast.cur_spc"
                    + " and section_post_mapping.spc=g_spc.spc"
                    + " and g_spc.gpc=g_post.post_code"
                    + " and bill_type='21' and bill_group_master.off_code=? and emp_mast.post_grp_type=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, offcode);
            pst.setString(2, postgrptype);
            rs = pst.executeQuery();
            while (rs.next()) {
                pedbean = new ProformaEmployeeDataBean();
                pedbean.setEmpname(rs.getString("EMPNAME"));
                pedbean.setPost(rs.getString("post"));
                pedbean.setBasicsalary(rs.getString("cur_basic_salary"));
                emplist.add(pedbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emplist;
    }

    @Override
    public int getGIACount(String offcode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int count = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "select grant_in_aid from aer_report_submit where off_code=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, offcode);
            rs = pst.executeQuery();
            if (rs.next()) {
                count = rs.getInt("grant_in_aid");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return count;
    }

    @Override
    public void addHierarchy(WorkflowRouting wr) {

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("insert into g_workflow_routing (office_code, gpc, process_id, reporting_gpc, reporting_office_code) values(?,?,?,?,?) ");
            ps.setString(1, wr.getLoginOffcode());
            ps.setString(2, wr.getGpc());
            ps.setInt(3, Integer.parseInt(wr.getProcessId()));
            ps.setString(4, wr.getReportingGpc());
            ps.setString(5, wr.getOfficeCode());
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void removeHierarchy(WorkflowRouting wr) {

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = this.dataSource.getConnection();
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("delete from g_workflow_routing where  workflow_routing_id=? ");
            ps.setInt(1, wr.getWorkflowRoutingId());
            ps.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getmappedPostList(String postcode, String processId) {
        List li = new ArrayList();
        WorkflowRouting wr = new WorkflowRouting();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select workflow_routing_id, process_attribute1, getofficeen(reporting_office_code) offName, office_code, gpc, post, process_id, reporting_gpc, level_id "
                    + "from g_workflow_routing a, g_post b where a.reporting_gpc=b.post_code and  gpc=? and process_id=? order by post");
            ps.setString(1, postcode);
            ps.setInt(2, Integer.parseInt(processId));
            rs = ps.executeQuery();
            while (rs.next()) {
                wr = new WorkflowRouting();
                wr.setWorkflowRoutingId(rs.getInt("workflow_routing_id"));
                wr.setPostName(rs.getString("post") + ", " + rs.getString("offName"));
                li.add(wr);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public List getPostListAuthorityWise(String offcode, String postcode, String processId) {
        List li = new ArrayList();
        WorkflowRouting wr = new WorkflowRouting();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select DISTINCT post_code,post from ( \n"
                    + " select post_code,post from g_post where  isauthority='Y' and post_code not in ( \n"
                    + " select reporting_gpc from g_workflow_routing where gpc=? and process_id=?) ) tab \n"
                    + " left outer join g_spc on tab.post_code=g_spc.gpc where off_code=?  order by post");

            ps.setString(1, postcode);
            ps.setInt(2, Integer.parseInt(processId));
            ps.setString(3, offcode);
            rs = ps.executeQuery();
            while (rs.next()) {
                wr = new WorkflowRouting();
                wr.setReportingGpc(rs.getString("post_code") + "");
                wr.setPostName(rs.getString("post"));
                li.add(wr);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public void createOtherEst(AnnualEstablishment bean) {
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        Connection con = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            int mcode = CommonFunctions.getMaxCodeInteger("aer_other_establishment", "aer_other_id", con);
            pst = con.prepareStatement("INSERT INTO aer_other_establishment (aer_other_id,aer_id,category,post, sanc_strength, vacancy, payscale_6th, payscale_7th, remarks,fyear,group_name,part_type) values (?,?,?,?,?,?,?,?,?,?,?,?)");
            pst.setInt(1, mcode);
            pst.setInt(2, bean.getAerId());
            pst.setString(3, bean.getOtherCategory());
            pst.setString(4, bean.getOtherPost());
            pst.setInt(5, bean.getOtherSS());
            pst.setInt(6, bean.getOtherVacancy());
            pst.setString(7, bean.getOther6thPay());
            pst.setString(8, bean.getOther7thPay());
            pst.setString(9, bean.getOtherRemarks());
            pst.setString(10, bean.getFinancialYear());
            pst.setString(11, bean.getGroup());
            pst.setString(12, bean.getPartType());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pst1);
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public AnnualEstablishment[] getOtherEst(int aerid, String PartType) {
        List<AnnualEstablishment> aeList = new ArrayList<>();
        String SQL = "SELECT * FROM aer_other_establishment WHERE aer_id=? AND part_type=?";
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = dataSource.getConnection();
            statement = conn.prepareStatement(SQL);
            statement.setInt(1, aerid);
            statement.setString(2, PartType);
            result = statement.executeQuery();
            while (result.next()) {
                AnnualEstablishment aer = new AnnualEstablishment();
                aer.setOtherCategory(result.getString("category"));
                aer.setOtherPost(result.getString("post"));
                aer.setScaleofPay(result.getString("payscale_6th"));
                aer.setOther7thPay(result.getString("payscale_7th"));
                aer.setOtherRemarks(result.getString("remarks"));
                aer.setOtherSS(result.getInt("sanc_strength"));
                aer.setOtherVacancy(result.getInt("vacancy"));
                aer.setMeninPosition((result.getInt("sanc_strength") - result.getInt("vacancy")));
                aer.setAerId(result.getInt("aer_id"));
                aer.setAerOtherId(result.getInt("aer_other_id"));
                aer.setFinancialYear(result.getString("fyear"));
                aer.setGroup(result.getString("group_name"));
                aer.setPartType(result.getString("part_type"));
                aer.setGp(result.getString("gp"));

                // aer.setAddressId(result.getInt("ADDRESS_ID"));
                aeList.add(aer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        AnnualEstablishment addrarray[] = aeList.toArray(new AnnualEstablishment[aeList.size()]);
        return addrarray;
    }

    @Override
    public int deleteOtherEst(int aerOtherId) {
        int n = 0;
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("DELETE FROM aer_other_establishment WHERE aer_other_id =?");
            pst.setInt(1, aerOtherId);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    @Override
    public AnnualEstablishment geOtherEstDetals(int aerOtherId) {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        AnnualEstablishment aer = new AnnualEstablishment();

        String SQL = "SELECT aer_other_id,aer_id,category,post,sanc_strength,vacancy,remarks,payscale_6th,payscale_7th,fyear,group_name,part_type from aer_other_establishment WHERE aer_other_id=? ";
        try {
            conn = dataSource.getConnection();
            statement = conn.prepareStatement(SQL);
            statement.setInt(1, aerOtherId);
            result = statement.executeQuery();
            if (result.next()) {
                aer.setOtherCategory(result.getString("category"));
                aer.setOtherPost(result.getString("post"));
                aer.setOther6thPay(result.getString("payscale_6th"));
                aer.setOther7thPay(result.getString("payscale_7th"));
                aer.setOtherRemarks(result.getString("remarks"));
                aer.setOtherSS(result.getInt("sanc_strength"));
                aer.setOtherVacancy(result.getInt("vacancy"));
                aer.setAerId(result.getInt("aer_id"));
                aer.setAerOtherId(result.getInt("aer_other_id"));
                aer.setFinancialYear(result.getString("fyear"));
                aer.setGroup(result.getString("group_name"));
                aer.setPartType(result.getString("part_type"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return aer;
    }

    @Override
    public void saveOtherEst(AnnualEstablishment bean) {
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        Connection con = null;
        //  SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("UPDATE aer_other_establishment SET category=?,post=?, sanc_strength=?, vacancy=?, payscale_6th=?, payscale_7th=?, remarks=?,group_name=? WHERE aer_other_id=?");
            pst.setString(1, bean.getOtherCategory());
            pst.setString(2, bean.getOtherPost());
            pst.setInt(3, bean.getOtherSS());
            pst.setInt(4, bean.getOtherVacancy());
            pst.setString(5, bean.getOther6thPay());
            pst.setString(6, bean.getOther7thPay());
            pst.setString(7, bean.getOtherRemarks());
            pst.setString(8, bean.getGroup());
            pst.setInt(9, bean.getAerOtherId());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pst1);
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getGroupAlistBeforeSubmission(String partType, String offCode, String postGroup, int aerId, String cadreCode) {

        List li = new ArrayList();
        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        boolean viewStatus = true;
        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement("SELECT count(*) cnt FROM annual_establish_report where aer_id=?");
            pst.setInt(1, aerId);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getInt("cnt") > 0) {
                    viewStatus = false;
                }
            }
            if (viewStatus == true) {

                if (partType != null && partType.equals("PART-A")) {
                    if (postGroup.equals("A") && cadreCode != null && !cadreCode.equals("")) {
                        pst = con.prepareStatement("SELECT post, sanction_strength, meninposition(gpc,pay_scale,post_grp,level_7thpay::TEXT,GP,OFF_CODE,'" + cadreCode + "'," + aerId + ",'REGULAR') person_in_position, gpc, pay_scale, POST_GRP, level_7thpay, GP,OFF_CODE FROM( \n"
                                + " SELECT COUNT(*) sanction_strength,GPC,pay_scale,post_grp,level_7thpay, G_SPC.GP,bill_group_master.OFF_CODE FROM aer2_co_mapping\n"
                                + " inner join bill_section_mapping on aer2_co_mapping.BILL_GROUP_ID=bill_section_mapping.BILL_GROUP_ID "
                                + " inner join bill_group_master on bill_section_mapping.BILL_GROUP_ID=bill_group_master.BILL_GROUP_ID "
                                + " inner join g_section on bill_section_mapping.section_id=g_section.section_id "
                                + " INNER JOIN SECTION_POST_MAPPING ON BILL_SECTION_MAPPING.SECTION_ID = SECTION_POST_MAPPING.SECTION_ID "
                                + " INNER JOIN G_SPC ON SECTION_POST_MAPPING.SPC = G_SPC.SPC "
                                + " LEFT OUTER JOIN EMP_MAST ON SECTION_POST_MAPPING.SPC = EMP_MAST.CUR_SPC "
                                + " INNER JOIN G_CADRE ON EMP_MAST.CUR_CADRE_CODE=G_CADRE.CADRE_CODE "
                                + " WHERE AER_ID=? and aer2_co_mapping.off_code=? and bill_group_master.bill_type = '42' "
                                + " and bill_group_master.sub_minor_head2 not in ('02002','03003','78012','02003','03001','03002','41078','02001','09001','02004')"
                                + " and (g_section.bill_type='REGULAR' OR g_section.bill_type='SP_CATGORY') AND POST_GRP=? AND is_sanctioned='Y' AND ifuclean!='Y' AND cadre_type='" + cadreCode + "'  "
                                + " group by gpc, pay_scale, post_grp,level_7thpay,G_SPC.GP, bill_group_master.OFF_CODE) G_SPC "
                                + " INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE order by post_grp, level_7thpay::Integer desc,pay_scale desc, post ");

                    } else {
                        if (!postGroup.equals("D")) {
                            pst = con.prepareStatement("SELECT post, sanction_strength, meninposition(gpc,pay_scale,post_grp,level_7thpay::TEXT,GP,OFF_CODE,'N'," + aerId + ",'REGULAR') person_in_position, gpc, pay_scale, POST_GRP, level_7thpay, GP,OFF_CODE FROM( "
                                    + " SELECT COUNT(*) sanction_strength,GPC,pay_scale,post_grp,level_7thpay, G_SPC.GP,bill_group_master.OFF_CODE FROM aer2_co_mapping"
                                    + " inner join bill_section_mapping on aer2_co_mapping.BILL_GROUP_ID=bill_section_mapping.BILL_GROUP_ID "
                                    + " inner join bill_group_master on bill_section_mapping.BILL_GROUP_ID=bill_group_master.BILL_GROUP_ID "
                                    + " inner join g_section on bill_section_mapping.section_id=g_section.section_id "
                                    + " INNER JOIN SECTION_POST_MAPPING ON BILL_SECTION_MAPPING.SECTION_ID = SECTION_POST_MAPPING.SECTION_ID "
                                    + " INNER JOIN G_SPC ON SECTION_POST_MAPPING.SPC = G_SPC.SPC "
                                    + " LEFT OUTER JOIN EMP_MAST ON SECTION_POST_MAPPING.SPC = EMP_MAST.CUR_SPC "
                                    + " LEFT OUTER JOIN G_CADRE ON EMP_MAST.CUR_CADRE_CODE=G_CADRE.CADRE_CODE "
                                    + " WHERE AER_ID=? and aer2_co_mapping.off_code=? and bill_group_master.bill_type = '42' "
                                    + " and bill_group_master.sub_minor_head2 not in ('02002','03003','78012','02003','03001','03002','41078','02001','09001','02004')"
                                    + " and (g_section.bill_type='REGULAR' or g_section.bill_type='CONT6_REG' or g_section.bill_type='SP_CATGORY') AND POST_GRP=? and is_sanctioned='Y' AND ifuclean!='Y' AND (((cadre_type<>'AIS' AND cadre_type<>'UGC' AND cadre_type<>'OJS') or cadre_type is null) OR CUR_CADRE_CODE IS NULL ) "
                                    + " group by gpc, pay_scale, post_grp,level_7thpay,G_SPC.GP, bill_group_master.OFF_CODE) G_SPC "
                                    + " INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE order by post_grp, level_7thpay::Integer desc,pay_scale desc, post ");
                        } else {
                            pst = con.prepareStatement("SELECT post, sanction_strength, meninposition(gpc,pay_scale,post_grp,level_7thpay::TEXT,GP,OFF_CODE,'N'," + aerId + ",'REGULAR') person_in_position, gpc, pay_scale, POST_GRP, level_7thpay, GP,OFF_CODE FROM( "
                                    + " SELECT COUNT(*) sanction_strength,GPC,pay_scale,post_grp,level_7thpay, G_SPC.GP,bill_group_master.OFF_CODE FROM aer2_co_mapping"
                                    + " inner join bill_section_mapping on aer2_co_mapping.BILL_GROUP_ID=bill_section_mapping.BILL_GROUP_ID "
                                    + " inner join bill_group_master on bill_section_mapping.BILL_GROUP_ID=bill_group_master.BILL_GROUP_ID "
                                    + " inner join g_section on bill_section_mapping.section_id=g_section.section_id "
                                    + " INNER JOIN SECTION_POST_MAPPING ON BILL_SECTION_MAPPING.SECTION_ID = SECTION_POST_MAPPING.SECTION_ID "
                                    + " INNER JOIN G_SPC ON SECTION_POST_MAPPING.SPC = G_SPC.SPC "
                                    + " LEFT OUTER JOIN EMP_MAST ON SECTION_POST_MAPPING.SPC = EMP_MAST.CUR_SPC "
                                    + " WHERE AER_ID=? and aer2_co_mapping.off_code=? and bill_group_master.bill_type = '42' "
                                    + " and bill_group_master.sub_minor_head2 not in ('02002','03003','78012','02003','03001','03002','41078','02001','09001','02004')"
                                    + " and (g_section.bill_type='REGULAR' or g_section.bill_type='CONT6_REG' or g_section.bill_type='SP_CATGORY') AND ( POST_GRP=? or POST_GRP is null) AND is_sanctioned='Y' AND ifuclean!='Y'"
                                    + " group by gpc, pay_scale, post_grp,level_7thpay,G_SPC.GP, bill_group_master.OFF_CODE) G_SPC "
                                    + " INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE order by post_grp, level_7thpay::Integer desc,pay_scale desc, post ");
                        }

                    }
                    pst.setInt(1, aerId);
                    pst.setString(2, offCode);
                    pst.setString(3, postGroup);
                } else if (partType != null && partType.equals("PART-B")) {
                    if (postGroup.equals("A") && cadreCode != null && !cadreCode.equals("")) {
                        pst = con.prepareStatement("SELECT post, sanction_strength, meninposition(gpc,pay_scale,post_grp,level_7thpay::TEXT,GP,OFF_CODE,'" + cadreCode + "'," + aerId + ",'GIA') person_in_position, gpc, pay_scale, POST_GRP, level_7thpay, GP,OFF_CODE FROM( \n"
                                + " SELECT COUNT(*) sanction_strength,GPC,pay_scale,post_grp,level_7thpay, G_SPC.GP,bill_group_master.OFF_CODE FROM aer2_co_mapping\n"
                                + " inner join bill_section_mapping on aer2_co_mapping.BILL_GROUP_ID=bill_section_mapping.BILL_GROUP_ID "
                                + " inner join bill_group_master on bill_section_mapping.BILL_GROUP_ID=bill_group_master.BILL_GROUP_ID "
                                + " inner join g_section on bill_section_mapping.section_id=g_section.section_id "
                                + " INNER JOIN SECTION_POST_MAPPING ON BILL_SECTION_MAPPING.SECTION_ID = SECTION_POST_MAPPING.SECTION_ID "
                                + " INNER JOIN G_SPC ON SECTION_POST_MAPPING.SPC = G_SPC.SPC "
                                + " LEFT OUTER JOIN EMP_MAST ON SECTION_POST_MAPPING.SPC = EMP_MAST.CUR_SPC "
                                + " INNER JOIN G_CADRE ON EMP_MAST.CUR_CADRE_CODE=G_CADRE.CADRE_CODE "
                                + " WHERE AER_ID=? and aer2_co_mapping.off_code=? and (bill_group_master.bill_type = '21' or bill_group_master.bill_type = '69') "
                                + " and bill_group_master.sub_minor_head2 not in ('02002','03003','78012','02003','03001','03002','41078','02001','09001','02004')"
                                + " and (g_section.bill_type='REGULAR' OR g_section.bill_type='SP_CATGORY') AND POST_GRP=? AND cadre_type='" + cadreCode + "'  "
                                + " group by gpc, pay_scale, post_grp,level_7thpay,G_SPC.GP, bill_group_master.OFF_CODE) G_SPC "
                                + " INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE order by post_grp, level_7thpay::Integer desc,pay_scale desc, post ");

                    } else {
                        if (!postGroup.equals("D")) {
                            pst = con.prepareStatement("SELECT post, sanction_strength, meninposition(gpc,pay_scale,post_grp,level_7thpay::TEXT,GP,OFF_CODE,'N'," + aerId + ",'GIA') person_in_position, gpc, pay_scale, POST_GRP, level_7thpay, GP,OFF_CODE FROM( "
                                    + " SELECT COUNT(*) sanction_strength,GPC,pay_scale,post_grp,level_7thpay, G_SPC.GP,bill_group_master.OFF_CODE FROM aer2_co_mapping"
                                    + " inner join bill_section_mapping on aer2_co_mapping.BILL_GROUP_ID=bill_section_mapping.BILL_GROUP_ID "
                                    + " inner join bill_group_master on bill_section_mapping.BILL_GROUP_ID=bill_group_master.BILL_GROUP_ID "
                                    + " inner join g_section on bill_section_mapping.section_id=g_section.section_id "
                                    + " INNER JOIN SECTION_POST_MAPPING ON BILL_SECTION_MAPPING.SECTION_ID = SECTION_POST_MAPPING.SECTION_ID "
                                    + " INNER JOIN G_SPC ON SECTION_POST_MAPPING.SPC = G_SPC.SPC "
                                    + " LEFT OUTER JOIN EMP_MAST ON SECTION_POST_MAPPING.SPC = EMP_MAST.CUR_SPC "
                                    + " LEFT OUTER JOIN G_CADRE ON EMP_MAST.CUR_CADRE_CODE=G_CADRE.CADRE_CODE "
                                    + " WHERE AER_ID=? and aer2_co_mapping.off_code=? and (bill_group_master.bill_type = '21' or bill_group_master.bill_type = '69') "
                                    + " and bill_group_master.sub_minor_head2 not in ('02002','03003','78012','02003','03001','03002','41078','02001','09001','02004')"
                                    + " and (g_section.bill_type='REGULAR' OR g_section.bill_type='SP_CATGORY') AND POST_GRP=? AND (((cadre_type<>'AIS' AND cadre_type<>'UGC' AND cadre_type<>'OJS') or cadre_type is null) OR CUR_CADRE_CODE IS NULL ) "
                                    + " group by gpc, pay_scale, post_grp,level_7thpay,G_SPC.GP, bill_group_master.OFF_CODE) G_SPC "
                                    + " INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE order by post_grp, level_7thpay::Integer desc,pay_scale desc, post ");
                        } else {
                            pst = con.prepareStatement("SELECT post, sanction_strength, meninposition(gpc,pay_scale,post_grp,level_7thpay::TEXT,GP,OFF_CODE,'N'," + aerId + ",'GIA') person_in_position, gpc, pay_scale, POST_GRP, level_7thpay, GP,OFF_CODE FROM( "
                                    + " SELECT COUNT(*) sanction_strength,GPC,pay_scale,post_grp,level_7thpay, G_SPC.GP,bill_group_master.OFF_CODE FROM aer2_co_mapping"
                                    + " inner join bill_section_mapping on aer2_co_mapping.BILL_GROUP_ID=bill_section_mapping.BILL_GROUP_ID "
                                    + " inner join bill_group_master on bill_section_mapping.BILL_GROUP_ID=bill_group_master.BILL_GROUP_ID "
                                    + " inner join g_section on bill_section_mapping.section_id=g_section.section_id "
                                    + " INNER JOIN SECTION_POST_MAPPING ON BILL_SECTION_MAPPING.SECTION_ID = SECTION_POST_MAPPING.SECTION_ID "
                                    + " INNER JOIN G_SPC ON SECTION_POST_MAPPING.SPC = G_SPC.SPC "
                                    + " LEFT OUTER JOIN EMP_MAST ON SECTION_POST_MAPPING.SPC = EMP_MAST.CUR_SPC "
                                    + " WHERE AER_ID=? and aer2_co_mapping.off_code=? and (bill_group_master.bill_type = '21' or bill_group_master.bill_type = '69') "
                                    + " and bill_group_master.sub_minor_head2 not in ('02002','03003','78012','02003','03001','03002','41078','02001','09001','02004')"
                                    + " and (g_section.bill_type='REGULAR' OR g_section.bill_type='SP_CATGORY') AND ( POST_GRP=? or POST_GRP is null) "
                                    + " group by gpc, pay_scale, post_grp,level_7thpay,G_SPC.GP, bill_group_master.OFF_CODE) G_SPC "
                                    + " INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE order by post_grp, level_7thpay::Integer desc,pay_scale desc, post ");
                        }

                    }
                    pst.setInt(1, aerId);
                    pst.setString(2, offCode);
                    pst.setString(3, postGroup);

                } else if (partType != null && partType.equals("PART-C")) {
                    pst = con.prepareStatement("SELECT post, sanction_strength, meninposition(gpc,pay_scale,post_grp,level_7thpay::TEXT,GP,OFF_CODE,'N'," + aerId + ",'CONT_6') person_in_position, gpc, pay_scale, POST_GRP, level_7thpay , GP,OFF_CODE FROM( "
                            + " SELECT COUNT(*) sanction_strength,GPC,pay_scale,post_grp,level_7thpay, G_SPC.GP,bill_group_master.OFF_CODE FROM aer2_co_mapping"
                            + " inner join bill_section_mapping on aer2_co_mapping.BILL_GROUP_ID=bill_section_mapping.BILL_GROUP_ID "
                            + " inner join bill_group_master on bill_section_mapping.BILL_GROUP_ID=bill_group_master.BILL_GROUP_ID "
                            + " inner join g_section on bill_section_mapping.section_id=g_section.section_id"
                            + " INNER JOIN SECTION_POST_MAPPING ON BILL_SECTION_MAPPING.SECTION_ID = SECTION_POST_MAPPING.SECTION_ID "
                            + " INNER JOIN G_SPC ON SECTION_POST_MAPPING.SPC = G_SPC.SPC "
                            + " LEFT OUTER JOIN EMP_MAST ON SECTION_POST_MAPPING.SPC = EMP_MAST.CUR_SPC "
                            + " WHERE AER_ID=? and aer2_co_mapping.off_code=? and (bill_group_master.bill_type = '42' OR bill_group_master.bill_type = '21' or bill_group_master.bill_type = '69') and g_section.bill_type='CONT6_REG' "
                            + " group by gpc, pay_scale, post_grp,level_7thpay,G_SPC.GP, bill_group_master.OFF_CODE) G_SPC "
                            + " INNER JOIN G_POST ON G_SPC.GPC = G_POST.POST_CODE order by post_grp, level_7thpay::Integer desc,pay_scale desc, post ");
                    pst.setInt(1, aerId);
                    pst.setString(2, offCode);
                }

                rs = pst.executeQuery();
                while (rs.next()) {
                    AnnualEstablishment ae = new AnnualEstablishment();
                    ae.setGroup(rs.getString("POST_GRP"));
                    ae.setGpc(rs.getString("post"));
                    ae.setPostname(rs.getString("gpc"));
                    ae.setScaleofPay(rs.getString("pay_scale"));
                    ae.setGp(rs.getString("GP"));
                    ae.setOffCode(rs.getString("OFF_CODE"));
                    ae.setScaleofPay7th(rs.getString("level_7thpay"));
                    ae.setSanctionedStrength(rs.getInt("sanction_strength"));
                    ae.setMeninPosition(rs.getInt("person_in_position"));
                    ae.setVacancyPosition(ae.getSanctionedStrength() - ae.getMeninPosition());
                    li.add(ae);
                }
            } else {
                if (partType != null && partType.equals("PART-A")) {

                    if (postGroup.equals("A") && cadreCode != null && !cadreCode.equals("")) {
                        pst = con.prepareStatement("SELECT report_id,off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength,menin_position,vacancy_position,gp,post_group, remarks FROM annual_establish_report "
                                + "  WHERE aer_id=?  and post_group=? and aer_part_type='PART-A' AND CADRE_CODE='" + cadreCode + "' order by post_group, pay_scale_7th_pay::Integer desc,pay_scale_6th_pay desc, postname");
                    } else {
                        if (postGroup.equals("D")) {
                            pst = con.prepareStatement("SELECT report_id,off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength,menin_position,vacancy_position,gp,post_group, remarks FROM annual_establish_report "
                                    + "  WHERE aer_id=? and (post_group=? or post_group is null) and aer_part_type='PART-A' AND (CADRE_CODE='' OR CADRE_CODE is null) order by post_group, pay_scale_7th_pay::Integer desc,pay_scale_6th_pay desc, postname");
                        } else {
                            pst = con.prepareStatement("SELECT report_id,off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength,menin_position,vacancy_position,gp,post_group, remarks FROM annual_establish_report "
                                    + "  WHERE aer_id=? and post_group=? and aer_part_type='PART-A' AND (CADRE_CODE='' OR CADRE_CODE is null) order by post_group, pay_scale_7th_pay::Integer desc,pay_scale_6th_pay desc, postname");
                        }
                    }
                    pst.setInt(1, aerId);
                    pst.setString(2, postGroup);
                    //pst.setString(2, offCode);
                } else if (partType != null && partType.equals("PART-B")) {

                    if (postGroup.equals("A") && cadreCode != null && !cadreCode.equals("")) {
                        pst = con.prepareStatement("SELECT report_id,off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength,menin_position,vacancy_position,gp,post_group, remarks FROM annual_establish_report "
                                + "  WHERE aer_id=?  and post_group=? and aer_part_type='PART-B' AND CADRE_CODE='" + cadreCode + "' order by post_group, pay_scale_7th_pay desc,pay_scale_6th_pay desc, postname");
                    } else {
                        pst = con.prepareStatement("SELECT report_id,off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength,menin_position,vacancy_position,gp,post_group, remarks FROM annual_establish_report "
                                + "  WHERE aer_id=?  and post_group=? and aer_part_type='PART-B' AND (CADRE_CODE='' OR CADRE_CODE is null) order by post_group, pay_scale_7th_pay desc,pay_scale_6th_pay desc, postname");
                    }
                    pst.setInt(1, aerId);
                    pst.setString(2, postGroup);
                    //pst.setString(2, offCode);

                } else if (partType != null && partType.equals("PART-C")) {
                    pst = con.prepareStatement("SELECT report_id,off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength,menin_position,vacancy_position,gp, remarks FROM annual_establish_report "
                            + "  WHERE aer_id=? and aer_part_type='PART-C' order by post_group, pay_scale_7th_pay desc,pay_scale_6th_pay desc, postname");
                    pst.setInt(1, aerId);
                    //pst.setString(2, offCode);
                }

                rs = pst.executeQuery();
                while (rs.next()) {
                    AnnualEstablishment ae = new AnnualEstablishment();
                    ae.setReportId(rs.getInt("report_id"));
                    ae.setGroup(rs.getString("post_group"));
                    ae.setGpc(rs.getString("postname"));
                    ae.setPostname(rs.getString("gpc"));
                    ae.setScaleofPay(rs.getString("pay_scale_6th_pay"));
                    ae.setGp(rs.getString("GP"));
                    ae.setOffCode(rs.getString("off_code"));
                    ae.setScaleofPay7th(rs.getString("pay_scale_7th_pay"));
                    ae.setSanctionedStrength(rs.getInt("sanction_strength"));
                    ae.setMeninPosition(rs.getInt("menin_position"));
                    ae.setVacancyPosition(rs.getInt("vacancy_position"));
                    ae.setOtherRemarks(rs.getString("remarks"));
                    li.add(ae);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public void deleteAerData(int aerId, String OffCode) {

        PreparedStatement pst = null;
        Connection con = null;

        try {
            con = dataSource.getConnection();

            if (OffCode != null && !OffCode.equals("")) {

                pst = con.prepareStatement("DELETE FROM aer2_co_mapping WHERE aer_id=? and off_code=?");
                pst.setInt(1, aerId);
                pst.setString(2, OffCode);
                pst.execute();

                pst = con.prepareStatement("DELETE FROM aer_report_submit WHERE aer_id=? and off_code=?");
                pst.setInt(1, aerId);
                pst.setString(2, OffCode);
                pst.execute();

                pst = con.prepareStatement("DELETE FROM aer_other_establishment WHERE aer_id=? ");
                pst.setInt(1, aerId);
                pst.execute();

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void deleteAerReportData(int aerId, String ddocode) {

        Connection con = null;

        PreparedStatement offpst = null;
        ResultSet offrs = null;

        PreparedStatement pst = null;
        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("DELETE FROM annual_establish_report WHERE aer_id=? and off_code=?");

            if (ddocode != null && !ddocode.equals("")) {

                String offsql = "select * from g_office where ddo_code=? and online_bill_submission='Y'";
                offpst = con.prepareStatement(offsql);
                offpst.setString(1, ddocode);
                offrs = offpst.executeQuery();
                while (offrs.next()) {
                    String offcode = offrs.getString("off_code");

                    pst.setInt(1, aerId);
                    pst.setString(2, offcode);
                    pst.execute();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public boolean getAerSubmittedStatus(int aerId) {

        PreparedStatement pst = null;
        Connection con = null;
        ResultSet rs = null;
        boolean viewStatus = true;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT submitted_on FROM aer_report_submit where aer_id=? and submitted_on is not null");
            pst.setInt(1, aerId);
            rs = pst.executeQuery();
            if (rs.next()) {
                viewStatus = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return viewStatus;
    }

    @Override
    public int updatePostInformation(String offcode, String gpc, String scaleOfPay6th, String scaleOfPay7th, String postgroup, String hidPostgroup, String paylevel, String gp) {

        Connection con = null;

        PreparedStatement pst = null;

        int retVal = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "UPDATE G_SPC SET pay_scale=?,post_grp=?,level_7thpay=?,gp=? WHERE OFF_CODE=? AND GPC=? and (post_grp=? or post_grp is null or post_grp='')";
            pst = con.prepareStatement(sql);
            pst.setString(1, scaleOfPay6th);
            pst.setString(2, postgroup);
            if (paylevel != null && !paylevel.equals("")) {
                pst.setInt(3, Integer.parseInt(paylevel));
            } else {
                pst.setInt(3, 0);
            }
            if (gp != null && !gp.equals("")) {
                pst.setInt(4, Integer.parseInt(gp));
            } else {
                pst.setInt(4, 0);
            }
            pst.setString(5, offcode);
            pst.setString(6, gpc);
            pst.setString(7, hidPostgroup);
            retVal = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return retVal;
    }

    @Override
    public int updatePartCPostInformation(String offcode, String gpc, String scaleOfPay6th, String postgroup, String paylevel, String gp) {

        Connection con = null;

        PreparedStatement pst = null;

        int retVal = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "UPDATE G_SPC SET pay_scale=?,post_grp=?,level_7thpay=?,gp=? WHERE OFF_CODE=? AND GPC=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, scaleOfPay6th);
            pst.setString(2, postgroup);
            if (paylevel != null && !paylevel.equals("")) {
                pst.setInt(3, Integer.parseInt(paylevel));
            } else {
                pst.setInt(3, 0);
            }
            if (gp != null && !gp.equals("")) {
                pst.setInt(4, Integer.parseInt(gp));
            } else {
                pst.setInt(4, 0);
            }
            pst.setString(5, offcode);
            pst.setString(6, gpc);
            retVal = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return retVal;
    }

    @Override
    public void approveAER(int aerid, String empid, String spc, String offcode, String selectedOffCode, String roleType) {

        Connection con = null;

        PreparedStatement ps2 = null;
        ResultSet rs = null;
        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();

            if (roleType.equals("BO")) {
                ps2 = con.prepareStatement("SELECT * FROM aer_report_submit WHERE aer_id=?");
                ps2.setInt(1, aerid);
                rs = ps2.executeQuery();
                if (rs.next()) {
                    if (rs.getString("is_reviewer_submitted").equalsIgnoreCase("N")) {
                        roleType = "REVIEWER";
                    } else if (rs.getString("is_verifier_submitted").equalsIgnoreCase("N")) {
                        roleType = "VERIFIER";
                    }
                }
            }

            String startTime = "";
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            startTime = dateFormat.format(cal.getTime());
            if (roleType != null && roleType.equals("APPROVER")) {
                pst = con.prepareStatement("update aer_report_submit set is_approver_submitted=?, approver_emp_id=?, approver_spc=?, approver_submitted_on=?, status=?,co_off_code=? where  aer_id=?");
                pst.setString(1, "Y");
                pst.setString(2, empid);
                pst.setString(3, spc);
                pst.setTimestamp(4, new Timestamp(dateFormat.parse(startTime).getTime()));
                pst.setString(5, "APPROVED");
                pst.setString(6, selectedOffCode);
                //pst.setString(7, offcode);
                pst.setInt(7, aerid);
                pst.execute();
            } else if (roleType != null && roleType.equals("REVIEWER")) {
                pst = con.prepareStatement("update aer_report_submit set is_reviewer_submitted=?, reviewer_emp_id=?, reviewer_spc=?, reviewer_submitted_on=?, status=?,verifier_revert_reason=? where  aer_id=?");
                pst.setString(1, "Y");
                pst.setString(2, empid);
                pst.setString(3, spc);
                pst.setTimestamp(4, new Timestamp(dateFormat.parse(startTime).getTime()));
                pst.setString(5, "REVIEWED");
                pst.setString(6, "");
                pst.setInt(7, aerid);
                pst.execute();
            } else if (roleType != null && roleType.equals("VERIFIER")) {
                pst = con.prepareStatement("update aer_report_submit set is_verifier_submitted=?, verifier_emp_id=?, verifier_spc=?, verifier_submitted_on=?, status=? where aer_id=?");
                pst.setString(1, "Y");
                pst.setString(2, empid);
                pst.setString(3, spc);
                pst.setTimestamp(4, new Timestamp(dateFormat.parse(startTime).getTime()));
                pst.setString(5, "VERIFIED");
                //pst.setString(6, offcode);
                pst.setInt(6, aerid);
                pst.execute();
            } else if (roleType != null && roleType.equals("DREVIEWER")) {
                pst = con.prepareStatement("update aer_report_submit_at_co set dreviewer_emp_id=?, dreviewer_spc=?, dreviewer_submitted_on=? where co_aer_id=?");
                pst.setString(1, empid);
                pst.setString(2, spc);
                pst.setTimestamp(3, new Timestamp(dateFormat.parse(startTime).getTime()));
                pst.setInt(4, aerid);
                pst.execute();
            } else if (roleType != null && roleType.equals("DVERIFIER")) {
                pst = con.prepareStatement("update aer_report_submit_at_co set dverifier_emp_id=?, dverifier_spc=?, dverifier_submitted_on=? where co_aer_id=?");
                pst.setString(1, empid);
                pst.setString(2, spc);
                pst.setTimestamp(3, new Timestamp(dateFormat.parse(startTime).getTime()));
                pst.setInt(4, aerid);
                pst.execute();
            } else if (roleType != null && roleType.equals("ACCEPTOR")) {

                pst = con.prepareStatement("update aer_report_submit set is_acceptor_submitted=?, acceptor_emp_id=?, acceptor_spc=?, acceptor_submitted_on=?, status=? where co_aer_id=?");
                pst.setString(1, "Y");
                pst.setString(2, empid);
                pst.setString(3, spc);
                pst.setTimestamp(4, new Timestamp(dateFormat.parse(startTime).getTime()));
                pst.setString(5, "ACCEPTED");
                pst.setInt(6, aerid);
                pst.execute();

                pst = con.prepareStatement("update aer_report_submit_at_co set acceptor_emp_id=?, acceptor_spc=?, acceptor_submitted_on=? where co_aer_id=?");
                pst.setString(1, empid);
                pst.setString(2, spc);
                pst.setTimestamp(3, new Timestamp(dateFormat.parse(startTime).getTime()));
                pst.setInt(4, aerid);
                pst.execute();
            }
            DataBaseFunctions.closeSqlObjects(pst);

            String sql = "INSERT INTO aer_comm_log(aer_id,action_date,action_taken_by_empid,action_taken_by_spc,message_if_any,user_type) values(?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setInt(1, aerid);
            pst.setTimestamp(2, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.setString(3, empid);
            pst.setString(4, spc);
            pst.setString(5, "APPROVE");
            pst.setString(6, roleType);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public String checkOperator(String offcode, String spc) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String isOperator = "N";
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT hrms_id FROM process_authorization WHERE spc=? AND process_id='13' AND role_type='OPERATOR'";
            pst = con.prepareStatement(sql);
            pst.setString(1, spc);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("hrms_id") != null && !rs.getString("hrms_id").equals("")) {
                    isOperator = "Y";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isOperator;
    }

    @Override
    public String checkApprover(String offcode, String spc) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String isApprover = "N";
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT hrms_id FROM process_authorization WHERE spc=? AND process_id='13' AND role_type='APPROVER'";
            pst = con.prepareStatement(sql);
            pst.setString(1, spc);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("hrms_id") != null && !rs.getString("hrms_id").equals("")) {
                    isApprover = "Y";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isApprover;

    }

    @Override
    public String checkReviewer(String offcode, String spc) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String isApprover = "N";
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT hrms_id FROM process_authorization WHERE spc=? AND process_id='13' AND role_type='REVIEWER'";
            pst = con.prepareStatement(sql);
            pst.setString(1, spc);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("hrms_id") != null && !rs.getString("hrms_id").equals("")) {
                    isApprover = "Y";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isApprover;

    }

    @Override
    public String checkVerifier(String offcode, String spc) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String isApprover = "N";
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT hrms_id FROM process_authorization WHERE spc=? AND process_id='13' AND role_type='VERIFIER'";
            pst = con.prepareStatement(sql);
            pst.setString(1, spc);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("hrms_id") != null && !rs.getString("hrms_id").equals("")) {
                    isApprover = "Y";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isApprover;

    }

    @Override
    public void saveAerGiaData(AnnualEstablishment bean) {
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            int mcode = CommonFunctions.getMaxCodeInteger("aer_other_establishment", "aer_other_id", con);
            pst = con.prepareStatement("INSERT INTO aer_other_establishment (aer_other_id,aer_id,group_name,post, sanc_strength, vacancy, payscale_6th, payscale_7th, remarks,fyear,part_type,gp,off_code) values (?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pst.setInt(1, mcode);
            pst.setInt(2, bean.getAerId());
            pst.setString(3, bean.getGroup());
            pst.setString(4, bean.getPostname());
            pst.setInt(5, bean.getSanctionedStrength());
            pst.setInt(6, bean.getVacancyPosition());
            pst.setString(7, bean.getScaleofPay());
            pst.setString(8, bean.getScaleofPay7th());
            pst.setString(9, bean.getOtherRemarks());
            pst.setString(10, "2017-2018");
            // pst.setString(11, bean.getGroup());
            pst.setString(11, "B");
            pst.setString(12, bean.getGp());
            pst.setString(13, bean.getOffCode());
            if (bean.getAerId() > 0) {
                pst.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public AnnualEstablishment getGiaEstDetals(int aerOtherId, String partType, int aerid) {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        AnnualEstablishment aer = new AnnualEstablishment();

        String SQL = "SELECT aer_other_id,aer_id,category,post,sanc_strength,vacancy,remarks,payscale_6th,payscale_7th,fyear,group_name,part_type,gp from aer_other_establishment WHERE aer_other_id=? and part_type=? and aer_id=?";
        try {
            conn = dataSource.getConnection();
            statement = conn.prepareStatement(SQL);
            statement.setInt(1, aerOtherId);
            statement.setString(2, partType);
            statement.setInt(3, aerid);
            result = statement.executeQuery();
            if (result.next()) {
                aer.setPostname(result.getString("post"));
                aer.setGp(result.getString("gp"));
                aer.setScaleofPay(result.getString("payscale_6th"));
                aer.setScaleofPay7th(result.getString("payscale_7th"));
                aer.setOtherRemarks(result.getString("remarks"));
                aer.setSanctionedStrength(result.getInt("sanc_strength"));
                aer.setMeninPosition(result.getInt("sanc_strength") - result.getInt("vacancy"));
                aer.setVacancyPosition(result.getInt("vacancy"));
                aer.setAerId(result.getInt("aer_id"));
                aer.setAerOtherId(result.getInt("aer_other_id"));
                aer.setFinancialYear(result.getString("fyear"));
                aer.setGroup(result.getString("group_name"));
                aer.setPartType(result.getString("part_type"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return aer;
    }

    @Override
    public void updateAerGiaData(AnnualEstablishment bean) {
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        Connection con = null;
        //  SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("UPDATE aer_other_establishment SET post=?, sanc_strength=?, vacancy=?, payscale_6th=?, payscale_7th=?, remarks=?,group_name=?,gp=? WHERE aer_other_id=? and part_type=? and aer_id=?");

            pst.setString(1, bean.getPostname());
            pst.setInt(2, bean.getSanctionedStrength());
            pst.setInt(3, bean.getVacancyPosition());
            pst.setString(4, bean.getScaleofPay());
            pst.setString(5, bean.getScaleofPay7th());
            pst.setString(6, bean.getOtherRemarks());
            pst.setString(7, bean.getGroup());
            pst.setString(8, bean.getGp());
            pst.setInt(9, bean.getAerOtherId());
            pst.setString(10, bean.getPartType());
            pst.setInt(11, bean.getAerId());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, pst1);
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void deleteGiaEst(int aerOtherId, String partType, int aerid) {
        int n = 0;
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("DELETE FROM aer_other_establishment WHERE aer_other_id =? and part_type=? and aer_id=?");
            pst.setInt(1, aerOtherId);
            pst.setString(2, partType);
            pst.setInt(3, aerid);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void DownloadaerPDFReport(Document document, String aerId, String offCode) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        String[] an = {"A", "B", "C", "D"};

        int totSanctionedStrength = 0;
        int totMenInPosition = 0;
        int totVacancy = 0;
        try {
            con = this.dataSource.getConnection();

            Font hdrTextFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFont1 = new Font(Font.FontFamily.HELVETICA, 5, Font.BOLD, BaseColor.BLACK);
            Font dataHdrFont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
            Font dataValFont = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK);

            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");
            PdfPTable table = null;
            PdfPTable innertable = null;
            PdfPCell cell = null;
            PdfPCell innercell = null;

            table = new PdfPTable(1);
            table.setWidths(new int[]{2});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("SCHEDULE-I", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Relalting to Head of Office including the Head of Department and Administrative Department in respect of their own establishment", new Font(Font.FontFamily.TIMES_ROMAN, 7)));
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("ANNUAL ESTABLISHMENT REVIEW TO BE FURNISHED BY HEAD OF OFFICE TO HEAD OF THE DEPARTMENT BY END OF JANUARY EACH YEAR", new Font(Font.FontFamily.TIMES_ROMAN, 7)));
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("PART-A (Regular Establishment)", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD)));
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("CERTIFICATE", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD)));
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("The sanctioned strength of the establishment as on 1st January  is given below. I have reviewed the staff requirement having regard to the prescribed yardstricks, wherever applicable. I certify that continuance of all the posts as considered necessary.", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            document.add(table);

            PdfPTable table1 = new PdfPTable(9);
            table1.setWidths(new int[]{8, 12, 10, 5, 5, 5, 5, 5, 7});
            table1.setWidthPercentage(100);

            PdfPCell datacell;

            datacell = new PdfPCell(new Phrase("Category of employee ", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setRowspan(2);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Despription of the Posts ", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setRowspan(2);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Pay Scale", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setColspan(2);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("As per 7th Pay ", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setRowspan(2);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Sanctioned  Strength ", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setRowspan(2);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Persons-in-Position", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setRowspan(2);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Vacancy Position", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setRowspan(2);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Remarks if any", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setRowspan(2);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("As per 6th Pay Commission", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setColspan(1);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("GP", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setColspan(1);
            table1.addCell(datacell);

            for (int i = 0; i < an.length; i++) {
                String ans = an[i];
                String groupType = "GROUP " + ans;

                if (ans.equals("A")) {
                    datacell = new PdfPCell(new Phrase(groupType, hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);
                    datacell = new PdfPCell(new Phrase("Other than UGC /Judiciary/ All India Services", new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK)));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    datacell.setColspan(8);
                    table1.addCell(datacell);

                    pst = con.prepareStatement("SELECT remarks,off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength,menin_position,vacancy_position,gp,post_group FROM annual_establish_report "
                            + "  WHERE aer_id=? and post_group=? and aer_part_type='PART-A' and (cadre_code='' or cadre_code is null) order by post_group, pay_scale_7th_pay desc,pay_scale_6th_pay desc, postname");
                    pst.setInt(1, Integer.parseInt(aerId));
                    //pst.setString(2, offCode);
                    pst.setString(2, ans);
                    rs = pst.executeQuery();
                    while (rs.next()) {

                        datacell = new PdfPCell(new Phrase(" ", hdrTextFont1));
                        datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_6th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("GP"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_7th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        totSanctionedStrength = totSanctionedStrength + rs.getInt("sanction_strength");

                        datacell = new PdfPCell(new Phrase(rs.getString("sanction_strength"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        totMenInPosition = totMenInPosition + rs.getInt("menin_position");

                        datacell = new PdfPCell(new Phrase(rs.getString("menin_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        totVacancy = totVacancy + rs.getInt("vacancy_position");

                        datacell = new PdfPCell(new Phrase(rs.getString("vacancy_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("remarks"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                    }

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);
                    datacell = new PdfPCell(new Phrase("All India Services", new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK)));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    datacell.setColspan(8);
                    table1.addCell(datacell);

                    pst = con.prepareStatement("SELECT remarks,off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength,menin_position,vacancy_position,gp,post_group FROM annual_establish_report "
                            + "  WHERE aer_id=? and post_group=? and aer_part_type='PART-A' and cadre_code='AIS' order by post_group, pay_scale_7th_pay desc,pay_scale_6th_pay desc, postname");
                    pst.setInt(1, Integer.parseInt(aerId));
                    //pst.setString(2, offCode);
                    pst.setString(2, ans);
                    rs = pst.executeQuery();
                    while (rs.next()) {

                        datacell = new PdfPCell(new Phrase(" ", hdrTextFont1));
                        datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_6th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("GP"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_7th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);
                        totSanctionedStrength = totSanctionedStrength + rs.getInt("sanction_strength");
                        datacell = new PdfPCell(new Phrase(rs.getString("sanction_strength"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);
                        totMenInPosition = totMenInPosition + rs.getInt("menin_position");
                        datacell = new PdfPCell(new Phrase(rs.getString("menin_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);
                        totVacancy = totVacancy + rs.getInt("vacancy_position");
                        datacell = new PdfPCell(new Phrase(rs.getString("vacancy_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("remarks"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                    }

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);
                    datacell = new PdfPCell(new Phrase("UGC", new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK)));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    datacell.setColspan(8);
                    table1.addCell(datacell);

                    pst = con.prepareStatement("SELECT remarks,off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength,menin_position,vacancy_position,gp,post_group FROM annual_establish_report "
                            + "  WHERE aer_id=? and post_group=? and aer_part_type='PART-A' and cadre_code='UGC' order by post_group, pay_scale_7th_pay desc,pay_scale_6th_pay desc, postname");
                    pst.setInt(1, Integer.parseInt(aerId));
                    //pst.setString(2, offCode);
                    pst.setString(2, ans);
                    rs = pst.executeQuery();
                    while (rs.next()) {

                        datacell = new PdfPCell(new Phrase(" ", hdrTextFont1));
                        datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_6th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("GP"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_7th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("sanction_strength"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("menin_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("vacancy_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("remarks"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        totSanctionedStrength = totSanctionedStrength + rs.getInt("sanction_strength");
                        totMenInPosition = totMenInPosition + rs.getInt("menin_position");
                        totVacancy = totVacancy + rs.getInt("vacancy_position");

                    }

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);
                    datacell = new PdfPCell(new Phrase("Judiciary", new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK)));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    datacell.setColspan(8);
                    table1.addCell(datacell);

                    pst = con.prepareStatement("SELECT remarks,off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength,menin_position,vacancy_position,gp,post_group FROM annual_establish_report "
                            + "  WHERE aer_id=? and post_group=? and aer_part_type='PART-A' and cadre_code='OJS' order by post_group, pay_scale_7th_pay desc,pay_scale_6th_pay desc, postname");
                    pst.setInt(1, Integer.parseInt(aerId));
                    //pst.setString(2, offCode);
                    pst.setString(2, ans);
                    rs = pst.executeQuery();
                    while (rs.next()) {

                        datacell = new PdfPCell(new Phrase(" ", hdrTextFont1));
                        datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_6th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("GP"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_7th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("sanction_strength"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("menin_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("vacancy_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("remarks"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        totSanctionedStrength = totSanctionedStrength + rs.getInt("sanction_strength");
                        totMenInPosition = totMenInPosition + rs.getInt("menin_position");
                        totVacancy = totVacancy + rs.getInt("vacancy_position");

                    }

                    /*Total*/
                    datacell = new PdfPCell(new Phrase("TOTAL", hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totSanctionedStrength + "", hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totMenInPosition + "", hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totVacancy + "", hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);
                    /*Total*/
                } else {
                    datacell = new PdfPCell(new Phrase(groupType, hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    datacell.setColspan(9);
                    table1.addCell(datacell);

                    totSanctionedStrength = 0;
                    totMenInPosition = 0;
                    totVacancy = 0;

                    if (ans.equals("D")) {
                        pst = con.prepareStatement("SELECT remarks,off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength,menin_position,vacancy_position,gp,post_group FROM annual_establish_report "
                                + "  WHERE aer_id=? and (post_group=? or post_group is null) and aer_part_type='PART-A' and (cadre_code='' or cadre_code is null) order by post_group, pay_scale_7th_pay desc,pay_scale_6th_pay desc, postname");
                    } else {
                        pst = con.prepareStatement("SELECT remarks,off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength,menin_position,vacancy_position,gp,post_group FROM annual_establish_report "
                                + "  WHERE aer_id=? and post_group=? and aer_part_type='PART-A' and (cadre_code='' or cadre_code is null) order by post_group, pay_scale_7th_pay desc,pay_scale_6th_pay desc, postname");
                    }
                    pst.setInt(1, Integer.parseInt(aerId));
                    //pst.setString(2, offCode);
                    pst.setString(2, ans);
                    rs = pst.executeQuery();
                    while (rs.next()) {

                        datacell = new PdfPCell(new Phrase(" ", hdrTextFont1));
                        datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_6th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("GP"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_7th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("sanction_strength"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("menin_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("vacancy_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("remarks"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        totSanctionedStrength = totSanctionedStrength + rs.getInt("sanction_strength");
                        totMenInPosition = totMenInPosition + rs.getInt("menin_position");
                        totVacancy = totVacancy + rs.getInt("vacancy_position");

                    }

                    /*Total*/
                    datacell = new PdfPCell(new Phrase("TOTAL", hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totSanctionedStrength + "", hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totMenInPosition + "", hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totVacancy + "", hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);
                    /*Total*/
                }

            }
            document.add(table1);

            /**
             * ************************************* Form B
             * *************************************
             */
            PdfPTable table5 = new PdfPTable(9);
            table5.setWidths(new int[]{8, 12, 10, 5, 5, 5, 5, 5, 7});
            table5.setWidthPercentage(100);

            PdfPCell datacellB;

            datacellB = new PdfPCell(new Phrase("PART-B ( G-I-A Establishment Drawing From Treasury)", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setColspan(9);
            table5.addCell(datacellB);

            datacellB = new PdfPCell(new Phrase("Category of employee ", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setRowspan(2);
            table5.addCell(datacellB);

            datacellB = new PdfPCell(new Phrase("Despription of the Posts ", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setRowspan(2);
            table5.addCell(datacellB);

            datacellB = new PdfPCell(new Phrase("Pay Scale", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setColspan(2);
            table5.addCell(datacellB);

            datacellB = new PdfPCell(new Phrase("As per 7th Pay ", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setRowspan(2);
            table5.addCell(datacellB);

            datacellB = new PdfPCell(new Phrase("Sanctioned  Strength ", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setRowspan(2);
            table5.addCell(datacellB);

            datacellB = new PdfPCell(new Phrase("Persons-in-Position", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setRowspan(2);
            table5.addCell(datacellB);

            datacellB = new PdfPCell(new Phrase("Vacancy Position", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setRowspan(2);
            table5.addCell(datacellB);

            datacellB = new PdfPCell(new Phrase("Remarks if any", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setRowspan(2);
            table5.addCell(datacellB);
            datacellB = new PdfPCell(new Phrase("As per 6th Pay ", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setColspan(1);
            table5.addCell(datacellB);
            datacellB = new PdfPCell(new Phrase("GP", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setColspan(1);
            table5.addCell(datacellB);

            datacellB = new PdfPCell(new Phrase("", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setColspan(1);
            table5.addCell(datacellB);

            PdfPCell datacellAB;

            totSanctionedStrength = 0;
            totMenInPosition = 0;
            totVacancy = 0;

            for (int i = 0; i < an.length; i++) {
                String ans = an[i];
                String groupType = "GROUP " + ans;

                if (ans.equals("A")) {
                    datacellAB = new PdfPCell(new Phrase(groupType, hdrTextFont1));
                    datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table5.addCell(datacellAB);

                    datacellAB = new PdfPCell(new Phrase("Other than UGC /Judiciary/ All India Services", new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK)));
                    datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                    datacellAB.setColspan(8);
                    table5.addCell(datacellAB);

                    totSanctionedStrength = 0;
                    totMenInPosition = 0;
                    totVacancy = 0;

                    pst = con.prepareStatement("SELECT remarks,off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength,menin_position,vacancy_position,gp,post_group FROM annual_establish_report "
                            + "  WHERE aer_id=? and post_group=? and aer_part_type='PART-B' and cadre_code='' order by post_group, pay_scale_7th_pay desc,pay_scale_6th_pay desc, postname");
                    pst.setInt(1, Integer.parseInt(aerId));
                    //pst.setString(2, offCode);
                    pst.setString(2, ans);
                    rs = pst.executeQuery();
                    while (rs.next()) {

                        datacellAB = new PdfPCell(new Phrase(" ", hdrTextFont1));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table5.addCell(datacellAB);

                        datacellAB = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellAB);

                        datacellAB = new PdfPCell(new Phrase(rs.getString("pay_scale_6th_pay"), dataValFont));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellAB);

                        datacellAB = new PdfPCell(new Phrase(rs.getString("GP"), dataValFont));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellAB);

                        datacellAB = new PdfPCell(new Phrase(rs.getString("pay_scale_7th_pay"), dataValFont));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellAB);

                        datacellAB = new PdfPCell(new Phrase(rs.getString("sanction_strength"), dataValFont));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellAB);

                        datacellAB = new PdfPCell(new Phrase(rs.getString("menin_position"), dataValFont));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellAB);

                        datacellAB = new PdfPCell(new Phrase(rs.getString("vacancy_position"), dataValFont));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellAB);

                        datacellAB = new PdfPCell(new Phrase(rs.getString("remarks"), dataValFont));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellAB);

                        totSanctionedStrength = totSanctionedStrength + rs.getInt("sanction_strength");
                        totMenInPosition = totMenInPosition + rs.getInt("menin_position");
                        totVacancy = totVacancy + rs.getInt("vacancy_position");

                    }

                    datacellAB = new PdfPCell();
                    datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table5.addCell(datacellAB);

                    datacellAB = new PdfPCell(new Phrase("All India Services", new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK)));
                    datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                    datacellAB.setColspan(8);
                    table5.addCell(datacellAB);

                    pst = con.prepareStatement("SELECT remarks,off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength,menin_position,vacancy_position,gp,post_group FROM annual_establish_report "
                            + "  WHERE aer_id=? and post_group=? and aer_part_type='PART-B' and cadre_code='AIS' order by post_group, pay_scale_7th_pay desc,pay_scale_6th_pay desc, postname");
                    pst.setInt(1, Integer.parseInt(aerId));
                    pst.setString(2, ans);
                    rs = pst.executeQuery();
                    while (rs.next()) {

                        datacellAB = new PdfPCell(new Phrase(" ", hdrTextFont1));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table5.addCell(datacellAB);

                        datacellAB = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellAB);

                        datacellAB = new PdfPCell(new Phrase(rs.getString("pay_scale_6th_pay"), dataValFont));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellAB);

                        datacellAB = new PdfPCell(new Phrase(rs.getString("GP"), dataValFont));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellAB);

                        datacellAB = new PdfPCell(new Phrase(rs.getString("pay_scale_7th_pay"), dataValFont));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellAB);

                        datacellAB = new PdfPCell(new Phrase(rs.getString("sanction_strength"), dataValFont));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellAB);

                        datacellAB = new PdfPCell(new Phrase(rs.getString("menin_position"), dataValFont));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellAB);

                        datacellAB = new PdfPCell(new Phrase(rs.getString("vacancy_position"), dataValFont));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellAB);

                        datacellAB = new PdfPCell(new Phrase(rs.getString("remarks"), dataValFont));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellAB);

                        totSanctionedStrength = totSanctionedStrength + rs.getInt("sanction_strength");
                        totMenInPosition = totMenInPosition + rs.getInt("menin_position");
                        totVacancy = totVacancy + rs.getInt("vacancy_position");

                    }

                    datacellAB = new PdfPCell();
                    datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table5.addCell(datacellAB);

                    datacellAB = new PdfPCell(new Phrase("UGC", new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK)));
                    datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                    datacellAB.setColspan(8);
                    table5.addCell(datacellAB);

                    pst = con.prepareStatement("SELECT remarks,off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength,menin_position,vacancy_position,gp,post_group FROM annual_establish_report "
                            + "  WHERE aer_id=? and post_group=? and aer_part_type='PART-B' and cadre_code='UGC' order by post_group, pay_scale_7th_pay desc,pay_scale_6th_pay desc, postname");
                    pst.setInt(1, Integer.parseInt(aerId));
                    //pst.setString(2, offCode);
                    pst.setString(2, ans);
                    rs = pst.executeQuery();
                    while (rs.next()) {

                        datacellAB = new PdfPCell(new Phrase(" ", hdrTextFont1));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table5.addCell(datacellAB);

                        datacellAB = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellAB);

                        datacellAB = new PdfPCell(new Phrase(rs.getString("pay_scale_6th_pay"), dataValFont));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellAB);

                        datacellAB = new PdfPCell(new Phrase(rs.getString("GP"), dataValFont));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellAB);

                        datacellAB = new PdfPCell(new Phrase(rs.getString("pay_scale_7th_pay"), dataValFont));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellAB);

                        datacellAB = new PdfPCell(new Phrase(rs.getString("sanction_strength"), dataValFont));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellAB);

                        datacellAB = new PdfPCell(new Phrase(rs.getString("menin_position"), dataValFont));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellAB);

                        datacellAB = new PdfPCell(new Phrase(rs.getString("vacancy_position"), dataValFont));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellAB);

                        datacellAB = new PdfPCell(new Phrase(rs.getString("remarks"), dataValFont));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellAB);

                        totSanctionedStrength = totSanctionedStrength + rs.getInt("sanction_strength");
                        totMenInPosition = totMenInPosition + rs.getInt("menin_position");
                        totVacancy = totVacancy + rs.getInt("vacancy_position");

                    }

                    datacellAB = new PdfPCell();
                    datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table5.addCell(datacellAB);

                    datacellAB = new PdfPCell(new Phrase("Judiciary", new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK)));
                    datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                    datacellAB.setColspan(8);
                    table5.addCell(datacellAB);

                    pst = con.prepareStatement("SELECT remarks,off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength,menin_position,vacancy_position,gp,post_group FROM annual_establish_report "
                            + "  WHERE aer_id=? and post_group=? and aer_part_type='PART-B' and cadre_code='OJS' order by post_group, pay_scale_7th_pay desc,pay_scale_6th_pay desc, postname");
                    pst.setInt(1, Integer.parseInt(aerId));
                    //pst.setString(2, offCode);
                    pst.setString(2, ans);
                    rs = pst.executeQuery();
                    while (rs.next()) {

                        datacellAB = new PdfPCell(new Phrase(" ", hdrTextFont1));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table5.addCell(datacellAB);

                        datacellAB = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellAB);

                        datacellAB = new PdfPCell(new Phrase(rs.getString("pay_scale_6th_pay"), dataValFont));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellAB);

                        datacellAB = new PdfPCell(new Phrase(rs.getString("GP"), dataValFont));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellAB);

                        datacellAB = new PdfPCell(new Phrase(rs.getString("pay_scale_7th_pay"), dataValFont));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellAB);

                        datacellAB = new PdfPCell(new Phrase(rs.getString("sanction_strength"), dataValFont));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellAB);

                        datacellAB = new PdfPCell(new Phrase(rs.getString("menin_position"), dataValFont));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellAB);

                        datacellAB = new PdfPCell(new Phrase(rs.getString("vacancy_position"), dataValFont));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellAB);

                        datacellAB = new PdfPCell(new Phrase(rs.getString("remarks"), dataValFont));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellAB);

                        totSanctionedStrength = totSanctionedStrength + rs.getInt("sanction_strength");
                        totMenInPosition = totMenInPosition + rs.getInt("menin_position");
                        totVacancy = totVacancy + rs.getInt("vacancy_position");

                    }

                    /*Total*/
                    datacell = new PdfPCell(new Phrase("TOTAL", hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totSanctionedStrength + "", hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totMenInPosition + "", hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totVacancy + "", hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);
                    /*Total*/
                } else {
                    datacellAB = new PdfPCell(new Phrase(groupType, hdrTextFont1));
                    datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                    datacellAB.setColspan(9);
                    table5.addCell(datacellAB);

                    totSanctionedStrength = 0;
                    totMenInPosition = 0;
                    totVacancy = 0;

                    pst = con.prepareStatement("SELECT remarks,off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength,menin_position,vacancy_position,gp,post_group FROM annual_establish_report "
                            + "  WHERE aer_id=? and post_group=? and aer_part_type='PART-B' and cadre_code='' order by post_group, pay_scale_7th_pay desc,pay_scale_6th_pay desc, postname");
                    pst.setInt(1, Integer.parseInt(aerId));
                    //pst.setString(2, offCode);
                    pst.setString(2, ans);
                    rs = pst.executeQuery();
                    while (rs.next()) {

                        datacellAB = new PdfPCell(new Phrase(" ", hdrTextFont1));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table5.addCell(datacellAB);

                        datacellAB = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellAB);

                        datacellAB = new PdfPCell(new Phrase(rs.getString("pay_scale_6th_pay"), dataValFont));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellAB);

                        datacellAB = new PdfPCell(new Phrase(rs.getString("GP"), dataValFont));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellAB);

                        datacellAB = new PdfPCell(new Phrase(rs.getString("pay_scale_7th_pay"), dataValFont));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellAB);

                        datacellAB = new PdfPCell(new Phrase(rs.getString("sanction_strength"), dataValFont));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellAB);

                        datacellAB = new PdfPCell(new Phrase(rs.getString("menin_position"), dataValFont));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellAB);

                        datacellAB = new PdfPCell(new Phrase(rs.getString("vacancy_position"), dataValFont));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellAB);

                        datacellAB = new PdfPCell(new Phrase(rs.getString("remarks"), dataValFont));
                        datacellAB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellAB);

                        totSanctionedStrength = totSanctionedStrength + rs.getInt("sanction_strength");
                        totMenInPosition = totMenInPosition + rs.getInt("menin_position");
                        totVacancy = totVacancy + rs.getInt("vacancy_position");

                    }

                    /*Total*/
                    datacell = new PdfPCell(new Phrase("TOTAL", hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totSanctionedStrength + "", hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totMenInPosition + "", hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totVacancy + "", hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);
                    /*Total*/
                }

            }

            datacellB = new PdfPCell(new Phrase("PART-B ( G-I-A Establishment)", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setColspan(9);
            table5.addCell(datacellB);

            datacellB = new PdfPCell(new Phrase("Category of employee ", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setRowspan(2);
            table5.addCell(datacellB);

            datacellB = new PdfPCell(new Phrase("Despription of the Posts ", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setRowspan(2);
            table5.addCell(datacellB);

            datacellB = new PdfPCell(new Phrase("Pay Scale", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setColspan(2);
            table5.addCell(datacellB);

            datacellB = new PdfPCell(new Phrase("As per 7th Pay ", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setRowspan(2);
            table5.addCell(datacellB);

            datacellB = new PdfPCell(new Phrase("Sanctioned  Strength ", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setRowspan(2);
            table5.addCell(datacellB);

            datacellB = new PdfPCell(new Phrase("Persons-in-Position", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setRowspan(2);
            table5.addCell(datacellB);

            datacellB = new PdfPCell(new Phrase("Vacancy Position", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setRowspan(2);
            table5.addCell(datacellB);

            datacellB = new PdfPCell(new Phrase("Remarks if any", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setRowspan(2);
            table5.addCell(datacellB);
            datacellB = new PdfPCell(new Phrase("As per 6th Pay ", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setColspan(1);
            table5.addCell(datacellB);
            datacellB = new PdfPCell(new Phrase("GP", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setColspan(1);
            table5.addCell(datacellB);

            datacellB = new PdfPCell(new Phrase("", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setColspan(1);
            table5.addCell(datacellB);

            String[] anGIA = {"A", "B", "C", "D", "R"};

            String sql = "select temp.off_code,off_en from (select off_code from aer_other_establishment where aer_id=? and part_type='B' group by off_code)temp"
                    + " inner join g_office on temp.off_code=g_office.off_code order by off_en";
            pst1 = con.prepareStatement(sql);
            pst1.setInt(1, Integer.parseInt(aerId));
            rs1 = pst1.executeQuery();
            while (rs1.next()) {
                //String offname = getOfficeName(con, rs1.getString("off_code"));
                String offname = rs1.getString("off_en");

                datacellB = new PdfPCell(new Phrase(offname, hdrTextFont1));
                datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
                datacellB.setColspan(9);
                table5.addCell(datacellB);

                for (int k = 0; k < anGIA.length; k++) {
                    String ans = anGIA[k];
                    String groupType = "GROUP " + ans;
                    if (ans.equals("R")) {
                        groupType = "Consolidated Renumeration";
                    }
                    datacellB = new PdfPCell(new Phrase(groupType, hdrTextFont1));
                    datacellB.setHorizontalAlignment(Element.ALIGN_LEFT);
                    datacellB.setColspan(9);
                    table5.addCell(datacellB);

                    totSanctionedStrength = 0;
                    totMenInPosition = 0;
                    totVacancy = 0;

                    pst = con.prepareStatement("SELECT * FROM aer_other_establishment "
                            + "  WHERE aer_id=? and group_name=? and  part_type=? and off_code=?");
                    pst.setInt(1, Integer.parseInt(aerId));
                    pst.setString(2, ans);
                    pst.setString(3, "B");
                    pst.setString(4, rs1.getString("off_code"));
                    rs = pst.executeQuery();
                    while (rs.next()) {

                        datacellB = new PdfPCell(new Phrase(" ", hdrTextFont1));
                        datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table5.addCell(datacellB);

                        datacellB = new PdfPCell(new Phrase(rs.getString("post"), dataValFont));
                        datacellB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellB);

                        datacellB = new PdfPCell(new Phrase(rs.getString("payscale_6th"), dataValFont));
                        datacellB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellB);

                        datacellB = new PdfPCell(new Phrase(rs.getString("gp"), dataValFont));
                        datacellB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellB);

                        datacellB = new PdfPCell(new Phrase(rs.getString("payscale_7th"), dataValFont));
                        datacellB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellB);

                        datacellB = new PdfPCell(new Phrase(rs.getString("sanc_strength"), dataValFont));
                        datacellB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellB);

                        //datacellB = new PdfPCell(new Phrase((rs.getInt("sanc_strength") - rs.getInt("vacancy"))+"", dataValFont));
                        datacellB = new PdfPCell(new Phrase((rs.getInt("sanc_strength") - rs.getInt("vacancy")) + "", dataValFont));
                        datacellB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellB);

                        datacellB = new PdfPCell(new Phrase(rs.getString("vacancy"), dataValFont));
                        datacellB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellB);

                        datacellB = new PdfPCell(new Phrase(rs.getString("remarks"), dataValFont));
                        datacellB.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table5.addCell(datacellB);

                        totSanctionedStrength = totSanctionedStrength + rs.getInt("sanc_strength");
                        totMenInPosition = totMenInPosition + (rs.getInt("sanc_strength") - rs.getInt("vacancy"));
                        totVacancy = totVacancy + rs.getInt("vacancy");

                    }

                    /*Total*/
                    datacell = new PdfPCell(new Phrase("TOTAL", hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totSanctionedStrength + "", hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totMenInPosition + "", hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totVacancy + "", hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);
                    /*Total*/

                }
            }

            document.add(table5);

            /**
             * ******************************************************** Form-C
             * *****************************
             */
            PdfPTable table2 = new PdfPTable(5);
            table2.setWidths(new int[]{10, 15, 10, 10, 10});
            table2.setWidthPercentage(100);

            PdfPCell datacellC;

            datacellC = new PdfPCell(new Phrase("PART-C ( Non-Regular Establishment)", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            datacellC.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellC.setColspan(5);
            table2.addCell(datacellC);

            datacellC = new PdfPCell(new Phrase("Category", hdrTextFont1));
            datacellC.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(datacellC);

            datacellC = new PdfPCell(new Phrase("Description of the Posts", hdrTextFont1));
            datacellC.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(datacellC);

            datacellC = new PdfPCell(new Phrase("As per 7th Pay", hdrTextFont1));
            datacellC.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(datacellC);

            datacellC = new PdfPCell(new Phrase("Persons-in-Position", hdrTextFont1));
            datacellC.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(datacellC);

            datacellC = new PdfPCell(new Phrase("Remarks if any", hdrTextFont1));
            datacellC.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(datacellC);

            datacellC = new PdfPCell(new Phrase("Contractual in lieu of regular post (As per GA & PG Department Resolution)/ Other Contractual Junior Engineer", dataValFont));
            datacellC.setHorizontalAlignment(Element.ALIGN_LEFT);
            datacellC.setColspan(5);
            table2.addCell(datacellC);

            pst = con.prepareStatement("SELECT off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength,menin_position,vacancy_position,gp,post_group FROM annual_establish_report "
                    + "  WHERE aer_id=? and aer_part_type='PART-C' order by post_group, pay_scale_7th_pay desc,pay_scale_6th_pay desc, postname");
            pst.setInt(1, Integer.parseInt(aerId));
            rs = pst.executeQuery();
            while (rs.next()) {
                datacellC = new PdfPCell(new Phrase(" ", hdrTextFont1));
                datacellC.setHorizontalAlignment(Element.ALIGN_CENTER);
                table2.addCell(datacellC);

                datacellC = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                datacellC.setHorizontalAlignment(Element.ALIGN_LEFT);
                table2.addCell(datacellC);

                datacellC = new PdfPCell(new Phrase(rs.getString("pay_scale_7th_pay"), dataValFont));
                datacellC.setHorizontalAlignment(Element.ALIGN_LEFT);
                table2.addCell(datacellC);

                datacellC = new PdfPCell(new Phrase(rs.getString("menin_position"), dataValFont));
                datacellC.setHorizontalAlignment(Element.ALIGN_LEFT);
                table2.addCell(datacellC);

                datacellC = new PdfPCell(new Phrase("", dataValFont));
                datacellC.setHorizontalAlignment(Element.ALIGN_LEFT);
                table2.addCell(datacellC);
            }
            document.add(table2);

            /**
             * ***************************************** Part D
             * *******************************
             */
            PdfPTable table3 = new PdfPTable(4);
            table3.setWidths(new int[]{20, 15, 10, 10});
            table3.setWidthPercentage(100);

            PdfPCell datacellD;

            datacellD = new PdfPCell(new Phrase("PART-D ( Other Establishment)", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            datacellD.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellD.setColspan(4);
            table3.addCell(datacellD);

            datacellD = new PdfPCell(new Phrase("Category", hdrTextFont1));
            datacellD.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(datacellD);

            datacellD = new PdfPCell(new Phrase("Number", hdrTextFont1));
            datacellD.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(datacellD);

            datacellD = new PdfPCell(new Phrase("Group", hdrTextFont1));
            datacellD.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(datacellD);

            datacellD = new PdfPCell(new Phrase("Remarks if any", hdrTextFont1));
            datacellD.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(datacellD);
            pst = con.prepareStatement("SELECT category,sanc_strength,group_name,remarks FROM aer_other_establishment  WHERE aer_id=? and part_type=?");
            pst.setInt(1, Integer.parseInt(aerId));
            pst.setString(2, "D");
            rs = pst.executeQuery();
            while (rs.next()) {
                datacellD = new PdfPCell(new Phrase(rs.getString("category"), dataValFont));
                datacellD.setHorizontalAlignment(Element.ALIGN_LEFT);
                table3.addCell(datacellD);

                datacellD = new PdfPCell(new Phrase(rs.getString("sanc_strength"), dataValFont));
                datacellD.setHorizontalAlignment(Element.ALIGN_LEFT);
                table3.addCell(datacellD);

                datacellD = new PdfPCell(new Phrase(rs.getString("group_name"), dataValFont));
                datacellD.setHorizontalAlignment(Element.ALIGN_LEFT);
                table3.addCell(datacellD);

                datacellD = new PdfPCell(new Phrase(rs.getString("remarks"), dataValFont));
                datacellD.setHorizontalAlignment(Element.ALIGN_LEFT);
                table3.addCell(datacellD);

            }
            document.add(table3);

            /**
             * **************************** FORM E
             * *************************************
             */
            PdfPTable table4 = new PdfPTable(3);
            table4.setWidths(new int[]{20, 15, 10});
            table4.setWidthPercentage(100);

            PdfPCell datacellE;

            datacellE = new PdfPCell(new Phrase("PART-E ( Outsourced / on Contract)", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            datacellE.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellE.setColspan(3);
            table4.addCell(datacellE);

            datacellE = new PdfPCell(new Phrase("Category", hdrTextFont1));
            datacellE.setHorizontalAlignment(Element.ALIGN_CENTER);
            table4.addCell(datacellE);

            datacellE = new PdfPCell(new Phrase("Number", hdrTextFont1));
            datacellE.setHorizontalAlignment(Element.ALIGN_CENTER);
            table4.addCell(datacellE);

            datacellE = new PdfPCell(new Phrase("Remarks if any", hdrTextFont1));
            datacellE.setHorizontalAlignment(Element.ALIGN_CENTER);
            table4.addCell(datacellE);

            pst = con.prepareStatement("SELECT category,sanc_strength,group_name,remarks FROM aer_other_establishment  WHERE aer_id=? and part_type=?");
            pst.setInt(1, Integer.parseInt(aerId));
            pst.setString(2, "E");
            rs = pst.executeQuery();
            while (rs.next()) {
                datacellE = new PdfPCell(new Phrase(rs.getString("category"), dataValFont));
                datacellE.setHorizontalAlignment(Element.ALIGN_LEFT);
                table4.addCell(datacellE);

                datacellE = new PdfPCell(new Phrase(rs.getString("sanc_strength"), dataValFont));
                datacellE.setHorizontalAlignment(Element.ALIGN_LEFT);
                table4.addCell(datacellE);

                datacellE = new PdfPCell(new Phrase(rs.getString("remarks"), dataValFont));
                datacellE.setHorizontalAlignment(Element.ALIGN_LEFT);
                table4.addCell(datacellE);

            }
            document.add(table4);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void downloadaerPDFReportScheduleII(Document document, String aerId, String offCode) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String[] an = {"A", "B", "C", "D"};

        int totSanctionedStrength = 0;
        int totMenInPosition = 0;
        int totVacancy = 0;
        try {
            con = this.dataSource.getConnection();

            Font hdrTextFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFont1 = new Font(Font.FontFamily.HELVETICA, 5, Font.BOLD, BaseColor.BLACK);
            Font dataHdrFont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
            Font dataValFont = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK);

            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");
            PdfPTable table = null;
            PdfPTable innertable = null;
            PdfPCell cell = null;
            PdfPCell innercell = null;

            table = new PdfPTable(1);
            table.setWidths(new int[]{2});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("SCHEDULE-II", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Relalting to Head of Office including the Head of Department and Administrative Department in respect of their own establishment", new Font(Font.FontFamily.TIMES_ROMAN, 7)));
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("ANNUAL ESTABLISHMENT REVIEW TO BE FURNISHED BY HEAD OF OFFICE TO HEAD OF THE DEPARTMENT BY END OF JANUARY EACH YEAR", new Font(Font.FontFamily.TIMES_ROMAN, 7)));
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("PART-A (Regular Establishment)", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD)));
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("CERTIFICATE", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD)));
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("The sanctioned strength of the establishment as on 1st January  is given below. I have reviewed the staff requirement having regard to the prescribed yardstricks, wherever applicable. I certify that continuance of all the posts as considered necessary.", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            document.add(table);

            PdfPTable table1 = new PdfPTable(9);
            table1.setWidths(new int[]{8, 12, 10, 5, 5, 5, 5, 5, 7});
            table1.setWidthPercentage(100);

            PdfPCell datacell;

            datacell = new PdfPCell(new Phrase("Category of employee ", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setRowspan(2);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Despription of the Posts ", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setRowspan(2);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Pay Scale", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setColspan(2);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("As per 7th Pay ", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setRowspan(2);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Sanctioned  Strength ", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setRowspan(2);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Persons-in-Position", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setRowspan(2);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Vacancy Position", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setRowspan(2);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Remarks if any", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setRowspan(2);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("As per 6th Pay Commission", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setColspan(1);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("GP", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setColspan(1);
            table1.addCell(datacell);

            for (int i = 0; i < an.length; i++) {
                String ans = an[i];
                String groupType = "GROUP " + ans;

                if (ans.equals("A")) {
                    datacell = new PdfPCell(new Phrase(groupType, hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);
                    datacell = new PdfPCell(new Phrase("Other than UGC /Judiciary/ All India Services", new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK)));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    datacell.setColspan(8);
                    table1.addCell(datacell);

                    totSanctionedStrength = 0;
                    totMenInPosition = 0;
                    totVacancy = 0;

                    pst = con.prepareStatement("SELECT remarks, off_code, gpc, postname, post_group, pay_scale_7th_pay, pay_scale_6th_pay, sanction_strength, "
                            + "  menin_position, vacancy_position, gp, post_group FROM annual_establish_report_at_co "
                            + "  WHERE co_aer_id=? and post_group=? and aer_part_type='PART-A' and (cadre_code='' or cadre_code is null) "
                            + "  order by post_group, pay_scale_7th_pay desc,pay_scale_6th_pay desc, postname");
                    pst.setInt(1, Integer.parseInt(aerId));
                    //pst.setString(2, offCode);
                    pst.setString(2, ans);
                    rs = pst.executeQuery();
                    while (rs.next()) {

                        datacell = new PdfPCell(new Phrase(" ", hdrTextFont1));
                        datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_6th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("GP"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_7th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("sanction_strength"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("menin_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("vacancy_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("remarks"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        totSanctionedStrength = totSanctionedStrength + rs.getInt("sanction_strength");
                        totMenInPosition = totMenInPosition + rs.getInt("menin_position");
                        totVacancy = totVacancy + rs.getInt("vacancy_position");

                    }

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);
                    datacell = new PdfPCell(new Phrase("All India Services", new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK)));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    datacell.setColspan(8);
                    table1.addCell(datacell);

                    pst = con.prepareStatement("SELECT remarks, off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength, "
                            + "  menin_position,vacancy_position,gp,post_group FROM annual_establish_report_at_co "
                            + "  WHERE co_aer_id=? and post_group=? and aer_part_type='PART-A' and cadre_code='AIS' "
                            + " order by post_group, pay_scale_7th_pay desc,pay_scale_6th_pay desc, postname");
                    pst.setInt(1, Integer.parseInt(aerId));
                    //pst.setString(2, offCode);
                    pst.setString(2, ans);
                    rs = pst.executeQuery();
                    while (rs.next()) {

                        datacell = new PdfPCell(new Phrase(" ", hdrTextFont1));
                        datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_6th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("GP"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_7th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("sanction_strength"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("menin_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("vacancy_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("remarks"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        totSanctionedStrength = totSanctionedStrength + rs.getInt("sanction_strength");
                        totMenInPosition = totMenInPosition + rs.getInt("menin_position");
                        totVacancy = totVacancy + rs.getInt("vacancy_position");

                    }

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);
                    datacell = new PdfPCell(new Phrase("UGC", new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK)));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    datacell.setColspan(8);
                    table1.addCell(datacell);

                    pst = con.prepareStatement("SELECT remarks,off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength, "
                            + "  menin_position,vacancy_position,gp,post_group FROM annual_establish_report_at_co "
                            + "  WHERE co_aer_id=? and post_group=? and aer_part_type='PART-A' and cadre_code='UGC' "
                            + "  order by post_group, pay_scale_7th_pay desc,pay_scale_6th_pay desc, postname");
                    pst.setInt(1, Integer.parseInt(aerId));
                    //pst.setString(2, offCode);
                    pst.setString(2, ans);
                    rs = pst.executeQuery();
                    while (rs.next()) {

                        datacell = new PdfPCell(new Phrase(" ", hdrTextFont1));
                        datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_6th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("GP"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_7th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("sanction_strength"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("menin_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("vacancy_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("remarks"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        totSanctionedStrength = totSanctionedStrength + rs.getInt("sanction_strength");
                        totMenInPosition = totMenInPosition + rs.getInt("menin_position");
                        totVacancy = totVacancy + rs.getInt("vacancy_position");

                    }

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);
                    datacell = new PdfPCell(new Phrase("Judiciary", new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK)));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    datacell.setColspan(8);
                    table1.addCell(datacell);

                    pst = con.prepareStatement("SELECT remarks,off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength, "
                            + "  menin_position,vacancy_position,gp,post_group FROM annual_establish_report_at_co "
                            + "  WHERE co_aer_id=? and post_group=? and aer_part_type='PART-A' and cadre_code='OJS' "
                            + "  order by post_group, pay_scale_7th_pay desc,pay_scale_6th_pay desc, postname");
                    pst.setInt(1, Integer.parseInt(aerId));
                    //pst.setString(2, offCode);
                    pst.setString(2, ans);
                    rs = pst.executeQuery();
                    while (rs.next()) {

                        datacell = new PdfPCell(new Phrase(" ", hdrTextFont1));
                        datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_6th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("GP"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_7th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("sanction_strength"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("menin_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("vacancy_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("remarks"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        totSanctionedStrength = totSanctionedStrength + rs.getInt("sanction_strength");
                        totMenInPosition = totMenInPosition + rs.getInt("menin_position");
                        totVacancy = totVacancy + rs.getInt("vacancy_position");

                    }

                    /*Total*/
                    datacell = new PdfPCell(new Phrase("TOTAL", hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totSanctionedStrength + "", hdrTextFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totMenInPosition + "", hdrTextFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totVacancy + "", hdrTextFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);
                    /*Total*/
                } else {
                    datacell = new PdfPCell(new Phrase(groupType, hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    datacell.setColspan(9);
                    table1.addCell(datacell);

                    totSanctionedStrength = 0;
                    totMenInPosition = 0;
                    totVacancy = 0;

                    pst = con.prepareStatement("SELECT remarks,off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength, "
                            + "  menin_position,vacancy_position,gp,post_group FROM annual_establish_report_at_co "
                            + "  WHERE co_aer_id=? and post_group=? and aer_part_type='PART-A' and cadre_code='' "
                            + "  order by post_group, pay_scale_7th_pay desc,pay_scale_6th_pay desc, postname");
                    pst.setInt(1, Integer.parseInt(aerId));
                    //pst.setString(2, offCode);
                    pst.setString(2, ans);
                    rs = pst.executeQuery();
                    while (rs.next()) {

                        datacell = new PdfPCell(new Phrase(" ", hdrTextFont1));
                        datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_6th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("GP"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_7th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("sanction_strength"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("menin_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("vacancy_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("remarks"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        totSanctionedStrength = totSanctionedStrength + rs.getInt("sanction_strength");
                        totMenInPosition = totMenInPosition + rs.getInt("menin_position");
                        totVacancy = totVacancy + rs.getInt("vacancy_position");

                    }

                    /*Total*/
                    datacell = new PdfPCell(new Phrase("TOTAL", hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totSanctionedStrength + "", hdrTextFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totMenInPosition + "", hdrTextFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totVacancy + "", hdrTextFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);
                    /*Total*/
                }

            }
            document.add(table1);

            /**
             * ************************************* Form B
             * *************************************
             */
            table1 = new PdfPTable(9);
            table1.setWidths(new int[]{8, 12, 10, 5, 5, 5, 5, 5, 7});
            table1.setWidthPercentage(100);

            datacell = new PdfPCell();

            datacell = new PdfPCell(new Phrase("PART-B (G-I-A Establishment Drawing from Treasury )", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD)));
            datacell.setColspan(9);
            datacell.setFixedHeight(20);
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Category of employee ", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setRowspan(2);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Despription of the Posts ", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setRowspan(2);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Pay Scale", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setColspan(2);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("As per 7th Pay ", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setRowspan(2);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Sanctioned  Strength ", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setRowspan(2);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Persons-in-Position", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setRowspan(2);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Vacancy Position", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setRowspan(2);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Remarks if any", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setRowspan(2);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("As per 6th Pay Commission", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setColspan(1);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("GP", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setColspan(1);
            table1.addCell(datacell);

            for (int i = 0; i < an.length; i++) {
                String ans = an[i];
                String groupType = "GROUP " + ans;

                if (ans.equals("A")) {
                    datacell = new PdfPCell(new Phrase(groupType, hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);
                    datacell = new PdfPCell(new Phrase("Other than UGC /Judiciary/ All India Services", new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK)));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    datacell.setColspan(8);
                    table1.addCell(datacell);

                    totSanctionedStrength = 0;
                    totMenInPosition = 0;
                    totVacancy = 0;

                    pst = con.prepareStatement("SELECT remarks, off_code, gpc, postname, post_group, pay_scale_7th_pay, pay_scale_6th_pay, sanction_strength, "
                            + "  menin_position, vacancy_position, gp, post_group FROM annual_establish_report_at_co "
                            + "  WHERE co_aer_id=? and post_group=? and aer_part_type='PART-B' and cadre_code='' "
                            + "  order by post_group, pay_scale_7th_pay desc,pay_scale_6th_pay desc, postname");
                    pst.setInt(1, Integer.parseInt(aerId));
                    //pst.setString(2, offCode);
                    pst.setString(2, ans);
                    rs = pst.executeQuery();
                    while (rs.next()) {

                        datacell = new PdfPCell(new Phrase(" ", hdrTextFont1));
                        datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_6th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("GP"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_7th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("sanction_strength"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("menin_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("vacancy_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("remarks"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        totSanctionedStrength = totSanctionedStrength + rs.getInt("sanction_strength");
                        totMenInPosition = totMenInPosition + rs.getInt("menin_position");
                        totVacancy = totVacancy + rs.getInt("vacancy_position");

                    }

                    /*Total*/
                    datacell = new PdfPCell(new Phrase("TOTAL", hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totSanctionedStrength + "", dataValFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totMenInPosition + "", dataValFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totVacancy + "", dataValFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);
                    /*Total*/

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);
                    datacell = new PdfPCell(new Phrase("All India Services", new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK)));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    datacell.setColspan(8);
                    table1.addCell(datacell);

                    totSanctionedStrength = 0;
                    totMenInPosition = 0;
                    totVacancy = 0;

                    pst = con.prepareStatement("SELECT remarks, off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength, "
                            + "  menin_position,vacancy_position,gp,post_group FROM annual_establish_report_at_co "
                            + "  WHERE co_aer_id=? and post_group=? and aer_part_type='PART-B' and cadre_code='AIS' "
                            + " order by post_group, pay_scale_7th_pay desc,pay_scale_6th_pay desc, postname");
                    pst.setInt(1, Integer.parseInt(aerId));
                    //pst.setString(2, offCode);
                    pst.setString(2, ans);
                    rs = pst.executeQuery();
                    while (rs.next()) {

                        datacell = new PdfPCell(new Phrase(" ", hdrTextFont1));
                        datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_6th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("GP"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_7th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("sanction_strength"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("menin_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("vacancy_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("remarks"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        totSanctionedStrength = totSanctionedStrength + rs.getInt("sanction_strength");
                        totMenInPosition = totMenInPosition + rs.getInt("menin_position");
                        totVacancy = totVacancy + rs.getInt("vacancy_position");

                    }

                    /*Total*/
                    datacell = new PdfPCell(new Phrase("TOTAL", hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totSanctionedStrength + "", dataValFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totMenInPosition + "", dataValFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totVacancy + "", dataValFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);
                    /*Total*/

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);
                    datacell = new PdfPCell(new Phrase("UGC", new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK)));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    datacell.setColspan(8);
                    table1.addCell(datacell);

                    totSanctionedStrength = 0;
                    totMenInPosition = 0;
                    totVacancy = 0;

                    pst = con.prepareStatement("SELECT remarks,off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength, "
                            + "  menin_position,vacancy_position,gp,post_group FROM annual_establish_report_at_co "
                            + "  WHERE co_aer_id=? and post_group=? and aer_part_type='PART-B' and cadre_code='UGC' "
                            + "  order by post_group, pay_scale_7th_pay desc,pay_scale_6th_pay desc, postname");
                    pst.setInt(1, Integer.parseInt(aerId));
                    //pst.setString(2, offCode);
                    pst.setString(2, ans);
                    rs = pst.executeQuery();
                    while (rs.next()) {

                        datacell = new PdfPCell(new Phrase(" ", hdrTextFont1));
                        datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_6th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("GP"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_7th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("sanction_strength"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("menin_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("vacancy_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("remarks"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        totSanctionedStrength = totSanctionedStrength + rs.getInt("sanction_strength");
                        totMenInPosition = totMenInPosition + rs.getInt("menin_position");
                        totVacancy = totVacancy + rs.getInt("vacancy_position");

                    }

                    /*Total*/
                    datacell = new PdfPCell(new Phrase("TOTAL", hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totSanctionedStrength + "", dataValFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totMenInPosition + "", dataValFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totVacancy + "", dataValFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);
                    /*Total*/

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);
                    datacell = new PdfPCell(new Phrase("Judiciary", new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK)));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    datacell.setColspan(8);
                    table1.addCell(datacell);

                    totSanctionedStrength = 0;
                    totMenInPosition = 0;
                    totVacancy = 0;

                    pst = con.prepareStatement("SELECT remarks,off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength, "
                            + "  menin_position,vacancy_position,gp,post_group FROM annual_establish_report_at_co "
                            + "  WHERE co_aer_id=? and post_group=? and aer_part_type='PART-B' and cadre_code='OJS' "
                            + "  order by post_group, pay_scale_7th_pay desc,pay_scale_6th_pay desc, postname");
                    pst.setInt(1, Integer.parseInt(aerId));
                    //pst.setString(2, offCode);
                    pst.setString(2, ans);
                    rs = pst.executeQuery();
                    while (rs.next()) {

                        datacell = new PdfPCell(new Phrase(" ", hdrTextFont1));
                        datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_6th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("GP"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_7th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("sanction_strength"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("menin_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("vacancy_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("remarks"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        totSanctionedStrength = totSanctionedStrength + rs.getInt("sanction_strength");
                        totMenInPosition = totMenInPosition + rs.getInt("menin_position");
                        totVacancy = totVacancy + rs.getInt("vacancy_position");

                    }

                    /*Total*/
                    datacell = new PdfPCell(new Phrase("TOTAL", hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totSanctionedStrength + "", dataValFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totMenInPosition + "", dataValFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totVacancy + "", dataValFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);
                    /*Total*/
                } else {
                    datacell = new PdfPCell(new Phrase(groupType, hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    datacell.setColspan(9);
                    table1.addCell(datacell);

                    totSanctionedStrength = 0;
                    totMenInPosition = 0;
                    totVacancy = 0;

                    pst = con.prepareStatement("SELECT remarks,off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength, "
                            + "  menin_position,vacancy_position,gp,post_group FROM annual_establish_report_at_co "
                            + "  WHERE co_aer_id=? and post_group=? and aer_part_type='PART-B' and cadre_code='' "
                            + "  order by post_group, pay_scale_7th_pay desc,pay_scale_6th_pay desc, postname");
                    pst.setInt(1, Integer.parseInt(aerId));
                    //pst.setString(2, offCode);
                    pst.setString(2, ans);
                    rs = pst.executeQuery();
                    while (rs.next()) {

                        datacell = new PdfPCell(new Phrase(" ", hdrTextFont1));
                        datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_6th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("GP"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_7th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("sanction_strength"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("menin_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("vacancy_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("remarks"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        totSanctionedStrength = totSanctionedStrength + rs.getInt("sanction_strength");
                        totMenInPosition = totMenInPosition + rs.getInt("menin_position");
                        totVacancy = totVacancy + rs.getInt("vacancy_position");

                    }

                    /*Total*/
                    datacell = new PdfPCell(new Phrase("TOTAL", hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totSanctionedStrength + "", dataValFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totMenInPosition + "", dataValFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totVacancy + "", dataValFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);
                    /*Total*/
                }

            }
            document.add(table1);

            PdfPTable table5 = new PdfPTable(9);
            table5.setWidths(new int[]{8, 12, 10, 5, 5, 5, 5, 5, 7});
            table5.setWidthPercentage(100);

            PdfPCell datacellB;

            datacellB = new PdfPCell(new Phrase("PART-B ( G-I-A Establishment)", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setColspan(9);
            table5.addCell(datacellB);

            datacellB = new PdfPCell(new Phrase("Category of employee ", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setRowspan(2);
            table5.addCell(datacellB);

            datacellB = new PdfPCell(new Phrase("Despription of the Posts ", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setRowspan(2);
            table5.addCell(datacellB);

            datacellB = new PdfPCell(new Phrase("Pay Scale", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setColspan(2);
            table5.addCell(datacellB);

            datacellB = new PdfPCell(new Phrase("As per 7th Pay ", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setRowspan(2);
            table5.addCell(datacellB);

            datacellB = new PdfPCell(new Phrase("Sanctioned  Strength ", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setRowspan(2);
            table5.addCell(datacellB);

            datacellB = new PdfPCell(new Phrase("Persons-in-Position", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setRowspan(2);
            table5.addCell(datacellB);

            datacellB = new PdfPCell(new Phrase("Vacancy Position", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setRowspan(2);
            table5.addCell(datacellB);

            datacellB = new PdfPCell(new Phrase("Remarks if any", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setRowspan(2);
            table5.addCell(datacellB);
            datacellB = new PdfPCell(new Phrase("", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setColspan(1);
            table5.addCell(datacellB);
            datacellB = new PdfPCell(new Phrase("As per 6th Pay Commission", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setColspan(1);
            table5.addCell(datacellB);

            datacellB = new PdfPCell(new Phrase("GP", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setColspan(1);
            table5.addCell(datacellB);

            String[] anGIA = {"A", "B", "C", "D", "R"};

            for (int k = 0; k < anGIA.length; k++) {
                String ans = anGIA[k];
                String groupType = "GROUP " + ans;
                if (ans.equals("R")) {
                    groupType = "Consolidated Renumeration";
                }
                datacellB = new PdfPCell(new Phrase(groupType, hdrTextFont1));
                datacellB.setHorizontalAlignment(Element.ALIGN_LEFT);
                datacellB.setColspan(9);
                table5.addCell(datacellB);

                totSanctionedStrength = 0;
                totMenInPosition = 0;
                totVacancy = 0;

                pst = con.prepareStatement("SELECT remarks,off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength, "
                        + "  menin_position,vacancy_position,gp,post_group FROM annual_establish_report_at_co "
                        + "  WHERE co_aer_id=? and post_group=? and aer_part_type=? and (cadre_code='' or cadre_code is null) "
                        + "  order by post_group, pay_scale_7th_pay desc,pay_scale_6th_pay desc, postname ");
                pst.setInt(1, Integer.parseInt(aerId));
                pst.setString(2, ans);
                pst.setString(3, "B");
                rs = pst.executeQuery();
                while (rs.next()) {

                    datacellB = new PdfPCell(new Phrase(" ", hdrTextFont1));
                    datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table5.addCell(datacellB);

                    datacellB = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                    datacellB.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table5.addCell(datacellB);

                    datacellB = new PdfPCell(new Phrase(rs.getString("pay_scale_6th_pay"), dataValFont));
                    datacellB.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table5.addCell(datacellB);

                    datacellB = new PdfPCell(new Phrase(rs.getString("gp"), dataValFont));
                    datacellB.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table5.addCell(datacellB);

                    datacellB = new PdfPCell(new Phrase(rs.getString("pay_scale_7th_pay"), dataValFont));
                    datacellB.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table5.addCell(datacellB);

                    datacellB = new PdfPCell(new Phrase(rs.getString("sanction_strength"), dataValFont));
                    datacellB.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table5.addCell(datacellB);

                    //datacellB = new PdfPCell(new Phrase(rs.getString("menin_position"), dataValFont));
                    datacellB = new PdfPCell(new Phrase((rs.getInt("sanction_strength") - rs.getInt("vacancy_position")) + "", dataValFont));
                    datacellB.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table5.addCell(datacellB);

                    datacellB = new PdfPCell(new Phrase(rs.getString("vacancy_position"), dataValFont));
                    datacellB.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table5.addCell(datacellB);

                    datacellB = new PdfPCell(new Phrase(rs.getString("remarks"), dataValFont));
                    datacellB.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table5.addCell(datacellB);

                    totSanctionedStrength = totSanctionedStrength + rs.getInt("sanction_strength");
                    totMenInPosition = totMenInPosition + (rs.getInt("sanction_strength") - rs.getInt("vacancy_position"));
                    totVacancy = totVacancy + rs.getInt("vacancy_position");

                }

                /*Total*/
                datacell = new PdfPCell(new Phrase("TOTAL", hdrTextFont1));
                datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table5.addCell(datacell);

                datacell = new PdfPCell();
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table5.addCell(datacell);

                datacell = new PdfPCell();
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table5.addCell(datacell);

                datacell = new PdfPCell();
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table5.addCell(datacell);

                datacell = new PdfPCell();
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table5.addCell(datacell);

                datacell = new PdfPCell(new Phrase(totSanctionedStrength + "", dataValFont));
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table5.addCell(datacell);

                datacell = new PdfPCell(new Phrase(totMenInPosition + "", dataValFont));
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table5.addCell(datacell);

                datacell = new PdfPCell(new Phrase(totVacancy + "", dataValFont));
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table5.addCell(datacell);

                datacell = new PdfPCell();
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table5.addCell(datacell);
                /*Total*/

            }

            document.add(table5);

            /**
             * ******************************************************** Form-C
             * *****************************
             */
            PdfPTable table2 = new PdfPTable(5);
            table2.setWidths(new int[]{10, 15, 10, 10, 10});
            table2.setWidthPercentage(100);

            PdfPCell datacellC;

            datacellC = new PdfPCell(new Phrase("PART-C ( Non-Regular Establishment)", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            datacellC.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellC.setColspan(5);
            table2.addCell(datacellC);

            datacellC = new PdfPCell(new Phrase("Category", hdrTextFont1));
            datacellC.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(datacellC);

            datacellC = new PdfPCell(new Phrase("Description of the Posts", hdrTextFont1));
            datacellC.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(datacellC);

            datacellC = new PdfPCell(new Phrase("As per 7th Pay", hdrTextFont1));
            datacellC.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(datacellC);

            datacellC = new PdfPCell(new Phrase("Persons-in-Position", hdrTextFont1));
            datacellC.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(datacellC);

            datacellC = new PdfPCell(new Phrase("Remarks if any", hdrTextFont1));
            datacellC.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(datacellC);

            datacellC = new PdfPCell(new Phrase("Contractual in lieu of regular post (As per GA & PG Department Resolution)/ Other Contractual Junior Engineer ", dataValFont));
            datacellC.setHorizontalAlignment(Element.ALIGN_LEFT);
            datacellC.setColspan(5);
            table2.addCell(datacellC);

            pst = con.prepareStatement("SELECT off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength,menin_position, "
                    + "  vacancy_position,gp,post_group FROM annual_establish_report_at_co "
                    + "  WHERE co_aer_id=? and aer_part_type='PART-C' order by post_group, pay_scale_7th_pay desc,pay_scale_6th_pay desc, postname ");
            pst.setInt(1, Integer.parseInt(aerId));
            rs = pst.executeQuery();
            while (rs.next()) {
                datacellC = new PdfPCell(new Phrase(" ", hdrTextFont1));
                datacellC.setHorizontalAlignment(Element.ALIGN_CENTER);
                table2.addCell(datacellC);

                datacellC = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                datacellC.setHorizontalAlignment(Element.ALIGN_LEFT);
                table2.addCell(datacellC);

                datacellC = new PdfPCell(new Phrase(rs.getString("pay_scale_7th_pay"), dataValFont));
                datacellC.setHorizontalAlignment(Element.ALIGN_LEFT);
                table2.addCell(datacellC);

                datacellC = new PdfPCell(new Phrase(rs.getString("menin_position"), dataValFont));
                datacellC.setHorizontalAlignment(Element.ALIGN_LEFT);
                table2.addCell(datacellC);

                datacellC = new PdfPCell(new Phrase("", dataValFont));
                datacellC.setHorizontalAlignment(Element.ALIGN_LEFT);
                table2.addCell(datacellC);
            }
            document.add(table2);

            /**
             * ***************************************** Part D
             * *******************************
             */
            PdfPTable table3 = new PdfPTable(4);
            table3.setWidths(new int[]{20, 15, 10, 10});
            table3.setWidthPercentage(100);

            PdfPCell datacellD;

            datacellD = new PdfPCell(new Phrase("PART-D ( Other Establishment)", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            datacellD.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellD.setColspan(4);
            table3.addCell(datacellD);

            datacellD = new PdfPCell(new Phrase("Category", hdrTextFont1));
            datacellD.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(datacellD);

            datacellD = new PdfPCell(new Phrase("Number", hdrTextFont1));
            datacellD.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(datacellD);

            datacellD = new PdfPCell(new Phrase("Group", hdrTextFont1));
            datacellD.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(datacellD);

            datacellD = new PdfPCell(new Phrase("Remarks if any", hdrTextFont1));
            datacellD.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(datacellD);
            pst = con.prepareStatement("SELECT remarks,off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength, "
                    + "  menin_position,vacancy_position,gp,post_group FROM annual_establish_report_at_co "
                    + "  WHERE co_aer_id=? and aer_part_type=?"
                    + "  order by postname ");

            pst.setInt(1, Integer.parseInt(aerId));
            pst.setString(2, "D");
            rs = pst.executeQuery();
            while (rs.next()) {
                datacellD = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                datacellD.setHorizontalAlignment(Element.ALIGN_LEFT);
                table3.addCell(datacellD);

                datacellD = new PdfPCell(new Phrase(rs.getString("sanction_strength"), dataValFont));
                datacellD.setHorizontalAlignment(Element.ALIGN_LEFT);
                table3.addCell(datacellD);

                datacellD = new PdfPCell(new Phrase(rs.getString("post_group"), dataValFont));
                datacellD.setHorizontalAlignment(Element.ALIGN_LEFT);
                table3.addCell(datacellD);

                datacellD = new PdfPCell(new Phrase(rs.getString("remarks"), dataValFont));
                datacellD.setHorizontalAlignment(Element.ALIGN_LEFT);
                table3.addCell(datacellD);

            }
            document.add(table3);

            /**
             * **************************** FORM E
             * *************************************
             */
            PdfPTable table4 = new PdfPTable(3);
            table4.setWidths(new int[]{20, 15, 10});
            table4.setWidthPercentage(100);

            PdfPCell datacellE;

            datacellE = new PdfPCell(new Phrase("PART-E ( Outsourced / on Contract)", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            datacellE.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellE.setColspan(3);
            table4.addCell(datacellE);

            datacellE = new PdfPCell(new Phrase("Category", hdrTextFont1));
            datacellE.setHorizontalAlignment(Element.ALIGN_CENTER);
            table4.addCell(datacellE);

            datacellE = new PdfPCell(new Phrase("Number", hdrTextFont1));
            datacellE.setHorizontalAlignment(Element.ALIGN_CENTER);
            table4.addCell(datacellE);

            datacellE = new PdfPCell(new Phrase("Remarks if any", hdrTextFont1));
            datacellE.setHorizontalAlignment(Element.ALIGN_CENTER);
            table4.addCell(datacellE);

            pst = con.prepareStatement("SELECT remarks,off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength, "
                    + "  menin_position,vacancy_position,gp,post_group FROM annual_establish_report_at_co "
                    + "  WHERE co_aer_id=? and aer_part_type=? "
                    + "  order by postname ");
            pst.setInt(1, Integer.parseInt(aerId));
            pst.setString(2, "E");
            rs = pst.executeQuery();
            while (rs.next()) {
                datacellE = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                datacellE.setHorizontalAlignment(Element.ALIGN_LEFT);
                table4.addCell(datacellE);

                datacellE = new PdfPCell(new Phrase(rs.getString("sanction_strength"), dataValFont));
                datacellE.setHorizontalAlignment(Element.ALIGN_LEFT);
                table4.addCell(datacellE);

                datacellE = new PdfPCell(new Phrase(rs.getString("remarks"), dataValFont));
                datacellE.setHorizontalAlignment(Element.ALIGN_LEFT);
                table4.addCell(datacellE);

            }
            document.add(table4);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void declineAER(int aerid, String empid, String spc, String offcode, String revertreason, String roleType) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            if (roleType.equals("BO")) {
                ps2 = con.prepareStatement("SELECT * FROM aer_report_submit WHERE aer_id=?");
                ps2.setInt(1, aerid);
                rs = ps2.executeQuery();
                if (rs.next()) {
                    if (rs.getString("is_reviewer_submitted").equalsIgnoreCase("N")) {
                        roleType = "REVIEWER";
                    } else if (rs.getString("is_verifier_submitted").equalsIgnoreCase("N")) {
                        roleType = "VERIFIER";
                    }
                } else {
                    ps2 = con.prepareStatement("SELECT * FROM aer_report_submit_at_co WHERE co_aer_id=?");
                    ps2.setInt(1, aerid);
                    rs = ps2.executeQuery();
                    if (rs.next()) {
                        if (rs.getString("dreviewer_emp_id") == null || rs.getString("dreviewer_emp_id").equals("")) {
                            roleType = "DREVIEWER";
                        } else if (rs.getString("dverifier_emp_id") == null || rs.getString("dverifier_emp_id").equals("")) {
                            roleType = "DVERIFIER";
                        } else if (rs.getString("acceptor_emp_id") == null || rs.getString("acceptor_emp_id").equals("")) {
                            roleType = "ACCEPTOR";
                        }
                    }
                }
            }

            String sql = "";
            String startTime = "";
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            startTime = dateFormat.format(cal.getTime());
            if (roleType.equals("APPROVER")) {
                pst = con.prepareStatement("update aer_report_submit set is_operator_submitted=?, operator_emp_id=?, operator_spc=?, file_name=?, submitted_on=?, status=?,revert_reason=? where aer_id=?");
                pst.setString(1, "N");
                pst.setString(2, null);
                pst.setString(3, null);
                pst.setString(4, null);
                pst.setTimestamp(5, null);
                pst.setString(6, null);
                pst.setString(7, revertreason);
                pst.setInt(8, aerid);
                pst.executeUpdate();
            } else if (roleType.equals("REVIEWER")) {
                pst = con.prepareStatement("update aer_report_submit set  file_name=?, submitted_on=?, status=?, is_operator_submitted=?, operator_emp_id=?, operator_spc=?, is_approver_submitted=?, approver_emp_id=?, approver_spc=?, approver_submitted_on=? , co_off_code=?,revert_reason=?,verifier_revert_reason=? where aer_id=?");
                pst.setString(1, null);
                pst.setTimestamp(2, null);
                pst.setString(3, null);
                pst.setString(4, "N");
                pst.setString(5, null);
                pst.setString(6, null);
                pst.setString(7, "N");
                pst.setString(8, null);
                pst.setString(9, null);
                pst.setTimestamp(10, null);
                pst.setString(11, null);
                pst.setString(12, revertreason);
                pst.setString(13, "");
                pst.setInt(14, aerid);
                pst.executeUpdate();
            } else if (roleType.equals("VERIFIER")) {
                //pst = con.prepareStatement("update aer_report_submit set  file_name=?, submitted_on=?, status=?, is_operator_submitted=?, operator_emp_id=?, operator_spc=?, is_approver_submitted=?, approver_emp_id=?, approver_spc=?, approver_submitted_on=? , co_off_code=?, revert_reason=?, is_reviewer_submitted=?, reviewer_emp_id=?, reviewer_spc=?, reviewer_submitted_on=?  where aer_id=?");
                pst = con.prepareStatement("update aer_report_submit set is_reviewer_submitted=?, reviewer_emp_id=?, reviewer_spc=?, reviewer_submitted_on=?,verifier_revert_reason=?   where aer_id=?");
                pst.setString(1, "N");
                pst.setString(2, null);
                pst.setString(3, null);
                pst.setTimestamp(4, null);
                pst.setString(5, revertreason);
                pst.setInt(6, aerid);
                pst.executeUpdate();
            } else if (roleType.equals("ACCEPTOR") || roleType.equals("DREVIEWER") || roleType.equals("DVERIFIER")) {

                pst = con.prepareStatement("delete from annual_establish_report_at_co where co_aer_id=?");
                pst.setInt(1, aerid);
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                pst = con.prepareStatement("delete from aer_report_submit_at_co where co_aer_id=?");
                pst.setInt(1, aerid);
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                pst = con.prepareStatement("update aer_report_submit set co_aer_id=0 , is_verifier_submitted='N' , verifier_emp_id=null, verifier_spc=null, verifier_submitted_on=null,acceptor_revert_reason=? where co_aer_id=?");
                pst.setString(1, revertreason);
                pst.setInt(2, aerid);
                pst.executeUpdate();
            }
            DataBaseFunctions.closeSqlObjects(pst);

            sql = "INSERT INTO aer_comm_log(aer_id,action_date,action_taken_by_empid,action_taken_by_spc,message_if_any,user_type) values(?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setInt(1, aerid);
            pst.setTimestamp(2, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.setString(3, empid);
            pst.setString(4, spc);
            pst.setString(5, revertreason);
            pst.setString(6, roleType);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public AnnualEstablishment[] getGiaEst(int aerId, String offCode, String partType) {
        List<AnnualEstablishment> aeList = new ArrayList<>();
        String SQL = "SELECT * FROM aer_other_establishment WHERE aer_id=? and off_code=? AND part_type=? order by post";
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = dataSource.getConnection();
            statement = conn.prepareStatement(SQL);
            statement.setInt(1, aerId);
            statement.setString(2, offCode);
            statement.setString(3, partType);
            result = statement.executeQuery();
            while (result.next()) {
                AnnualEstablishment aer = new AnnualEstablishment();
                aer.setOffCode(offCode);
                aer.setOtherCategory(result.getString("category"));
                aer.setOtherPost(result.getString("post"));
                aer.setScaleofPay(result.getString("payscale_6th"));
                aer.setOther7thPay(result.getString("payscale_7th"));
                aer.setOtherRemarks(result.getString("remarks"));
                aer.setOtherSS(result.getInt("sanc_strength"));
                aer.setOtherVacancy(result.getInt("vacancy"));
                aer.setMeninPosition((result.getInt("sanc_strength") - result.getInt("vacancy")));
                aer.setAerId(result.getInt("aer_id"));
                aer.setAerOtherId(result.getInt("aer_other_id"));
                aer.setFinancialYear(result.getString("fyear"));
                aer.setGroup(result.getString("group_name"));
                aer.setPartType(result.getString("part_type"));
                aer.setGp(result.getString("gp"));

                // aer.setAddressId(result.getInt("ADDRESS_ID"));
                aeList.add(aer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        AnnualEstablishment addrarray[] = aeList.toArray(new AnnualEstablishment[aeList.size()]);
        return addrarray;
    }

    @Override
    public void updateRemarks(int aerid, String gpc, String remarks) {

        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "UPDATE annual_establish_report SET remarks=? WHERE aer_id=? AND gpc=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, remarks);
            pst.setInt(2, aerid);
            pst.setString(3, gpc);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getAllGrantinAidOfficesByAerId(int aerId) {
        List<AnnualEstablishment> aeList = new ArrayList<>();
        String SQL = "SELECT * FROM aer_other_establishment WHERE aer_id=? AND part_type=? order by off_code, group_name";
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            conn = dataSource.getConnection();
            statement = conn.prepareStatement(SQL);
            statement.setInt(1, aerId);
            statement.setString(2, "B");
            result = statement.executeQuery();
            while (result.next()) {
                AnnualEstablishment aer = new AnnualEstablishment();
                aer.setOffCode(result.getString("off_code"));
                aer.setOtherCategory(result.getString("category"));
                aer.setOtherPost(result.getString("post"));
                aer.setScaleofPay(result.getString("payscale_6th"));
                aer.setOther7thPay(result.getString("payscale_7th"));
                aer.setOtherRemarks(result.getString("remarks"));
                aer.setOtherSS(result.getInt("sanc_strength"));
                aer.setOtherVacancy(result.getInt("vacancy"));
                aer.setMeninPosition((result.getInt("sanc_strength") - result.getInt("vacancy")));
                aer.setAerId(result.getInt("aer_id"));
                aer.setAerOtherId(result.getInt("aer_other_id"));
                aer.setFinancialYear(result.getString("fyear"));
                aer.setGroup(result.getString("group_name"));
                aer.setPartType(result.getString("part_type"));
                aer.setGp(result.getString("gp"));

                // aer.setAddressId(result.getInt("ADDRESS_ID"));
                aeList.add(aer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(result, statement);
            DataBaseFunctions.closeSqlObjects(conn);
        }
        return aeList;
    }

    @Override
    public String getRemarks(int aerid, String gpc) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String remarks = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT remarks FROM annual_establish_report WHERE aer_id=? AND gpc=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, aerid);
            pst.setString(2, gpc);
            rs = pst.executeQuery();
            if (rs.next()) {
                remarks = rs.getString("remarks");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return remarks;
    }

    @Override
    public List getAerViewForCO(String fy, String spc, String coOffCode, String postgrp, String cadreType, String partTYpe, String roletype) {
        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        List<AnnualEstablishment> aeList = new ArrayList<>();
        boolean isdataInserted = false;
        int coAerId = 0;
        String sql = "";
        try {
            con = this.dataSource.getConnection();

            ps2 = con.prepareStatement(" SELECT co_aer_id FROM aer_report_submit_at_co where co_off_code=? and verifier_spc=? and fy=? ");
            ps2.setString(1, coOffCode);
            ps2.setString(2, spc);
            ps2.setString(3, fy);
            rs = ps2.executeQuery();
            if (rs.next()) {
                coAerId = rs.getInt("co_aer_id");
                if (coAerId > 0) {
                    //isdataInserted = true;
                }
            }
            DataBaseFunctions.closeSqlObjects(rs, ps2);

            if (isdataInserted) {
                /*
                 sql = "";

                 if (partTYpe.equalsIgnoreCase("PART-A")) {
                 if (cadreType != null && !cadreType.equals("")) {

                 pst = con.prepareStatement(sql);
                 pst.setInt(1, coAerId);
                 pst.setString(2, cadreType);
                 pst.setString(3, postgrp);
                 pst.setString(4, partTYpe);

                 } else if (!postgrp.equals("D")) {
                 pst = con.prepareStatement(sql);
                 pst.setInt(1, coAerId);
                 pst.setString(2, cadreType);
                 pst.setString(3, postgrp);
                 pst.setString(4, partTYpe);

                 } else if (postgrp.equals("D")) {
                 pst = con.prepareStatement(sql);
                 pst.setInt(1, coAerId);
                 pst.setString(2, cadreType);
                 pst.setString(3, postgrp);
                 pst.setString(4, partTYpe);

                 }
                 } else if (partTYpe.equalsIgnoreCase("PART-C")) {
                 pst = con.prepareStatement(sql);
                 pst.setInt(1, coAerId);
                 pst.setString(2, partTYpe);
                 }

                 rs = pst.executeQuery();
                 while (rs.next()) {
                 AnnualEstablishment aer = new AnnualEstablishment();
                 aer.setSanctionedStrength(rs.getInt("sanction_strength"));
                 aer.setMeninPosition(rs.getInt("menin_position"));
                 aer.setVacancyPosition(rs.getInt("vacancy_position"));
                 aer.setGpc(rs.getString("gpc"));
                 aer.setPostname(rs.getString("postname"));
                 aer.setPostgrp(rs.getString("post_group"));
                 aer.setScaleofPay(rs.getString("pay_scale_6th_pay"));
                 aer.setScaleofPay7th(rs.getString("pay_scale_7th_pay"));
                 aer.setGp(rs.getString("gp"));
                 aer.setCadreType(rs.getString("cadre_code"));
                 aer.setPartType(rs.getString("aer_part_type"));
                 aeList.add(aer);
                 }
                 */
            } else {
                if (roletype.equalsIgnoreCase("REVIEWER")) {
                    if (partTYpe.equalsIgnoreCase("PART-A")) {
                        if (cadreType != null && !cadreType.equals("")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position, "
                                    + " postname, post_group, annual_establish_report.pay_scale_7th_pay, pay_scale_6th_pay, "
                                    + " annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  from aer_report_submit aer "
                                    + " INNER JOIN process_authorization ON aer.co_off_code=process_authorization.off_code "
                                    + " inner join g_spc on aer.approver_spc=g_spc.spc "
                                    + " inner join annual_establish_report on aer.aer_id=annual_establish_report.aer_id "
                                    + " where FY=? "
                                    + " AND process_authorization.SPC=? "
                                    + " and annual_establish_report.cadre_code=? "
                                    + " and post_group=? "
                                    + " and aer_part_type=? "
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='REVIEWER' and is_approver_submitted='Y' and is_reviewer_submitted='Y' "
                                    + " group by postname, post_group, annual_establish_report.pay_scale_7th_pay, "
                                    + " annual_establish_report.pay_scale_6th_pay, annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  order by annual_establish_report.pay_scale_7th_pay desc ";
                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, cadreType);
                            pst.setString(4, postgrp);
                            pst.setString(5, partTYpe);

                        } else if (!postgrp.equals("D")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position, "
                                    + " postname, post_group, annual_establish_report.pay_scale_7th_pay, pay_scale_6th_pay, "
                                    + " annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  from aer_report_submit aer "
                                    + " INNER JOIN process_authorization ON aer.co_off_code=process_authorization.off_code "
                                    + " inner join g_spc on aer.approver_spc=g_spc.spc "
                                    + " inner join annual_establish_report on aer.aer_id=annual_establish_report.aer_id "
                                    + " where FY=? "
                                    + " AND process_authorization.SPC=? "
                                    + " and ((annual_establish_report.cadre_code<>'AIS' and annual_establish_report.cadre_code<>'UGC' and annual_establish_report.cadre_code<>'OJS') or annual_establish_report.cadre_code is null) "
                                    + " and post_group=? "
                                    + " and aer_part_type=? "
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='REVIEWER' "
                                    + " and is_approver_submitted='Y'  and is_reviewer_submitted='Y' "
                                    + " group by  postname, post_group, annual_establish_report.pay_scale_7th_pay, "
                                    + " annual_establish_report.pay_scale_6th_pay, annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type "
                                    + " ORDER BY annual_establish_report.pay_scale_7th_pay DESC ";

                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, postgrp);
                            pst.setString(4, partTYpe);

                        } else if (postgrp.equals("D")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position, "
                                    + " postname, post_group, annual_establish_report.pay_scale_7th_pay, pay_scale_6th_pay, "
                                    + " annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  from aer_report_submit aer "
                                    + " INNER JOIN process_authorization ON aer.co_off_code=process_authorization.off_code "
                                    + " inner join g_spc on aer.approver_spc=g_spc.spc "
                                    + " inner join annual_establish_report on aer.aer_id=annual_establish_report.aer_id "
                                    + " where FY=? AND process_authorization.SPC=? "
                                    + " and ((annual_establish_report.cadre_code<>'AIS' and annual_establish_report.cadre_code<>'UGC' and annual_establish_report.cadre_code<>'OJS') or annual_establish_report.cadre_code is null) "
                                    + " and (post_group=? or post_group is null) and aer_part_type=? "
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='REVIEWER' and is_approver_submitted='Y'  and is_reviewer_submitted='Y' "
                                    + " group by postname, post_group, annual_establish_report.pay_scale_7th_pay, "
                                    + " annual_establish_report.pay_scale_6th_pay, annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  order by annual_establish_report.pay_scale_7th_pay desc ";
                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, postgrp);
                            pst.setString(4, partTYpe);
                        }
                    } else if (partTYpe.equalsIgnoreCase("PART-B")) {
                        if (cadreType != null && !cadreType.equals("")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position, "
                                    + " postname, post_group, annual_establish_report.pay_scale_7th_pay, pay_scale_6th_pay, "
                                    + " annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  from aer_report_submit aer "
                                    + " INNER JOIN process_authorization ON aer.co_off_code=process_authorization.off_code "
                                    + " inner join g_spc on aer.approver_spc=g_spc.spc "
                                    + " inner join annual_establish_report on aer.aer_id=annual_establish_report.aer_id "
                                    + " where FY=? "
                                    + " AND process_authorization.SPC=? "
                                    + " and annual_establish_report.cadre_code=? "
                                    + " and post_group=? "
                                    + " and aer_part_type=? "
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='REVIEWER' and is_approver_submitted='Y' and is_reviewer_submitted='Y' "
                                    + " group by  postname, post_group, annual_establish_report.pay_scale_7th_pay, "
                                    + " annual_establish_report.pay_scale_6th_pay, annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  order by annual_establish_report.pay_scale_7th_pay desc ";
                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, cadreType);
                            pst.setString(4, postgrp);
                            pst.setString(5, partTYpe);

                        } else if (!postgrp.equals("D")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position, "
                                    + " postname, post_group, annual_establish_report.pay_scale_7th_pay, pay_scale_6th_pay, "
                                    + " annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  from aer_report_submit aer "
                                    + " INNER JOIN process_authorization ON aer.co_off_code=process_authorization.off_code "
                                    + " inner join g_spc on aer.approver_spc=g_spc.spc "
                                    + " inner join annual_establish_report on aer.aer_id=annual_establish_report.aer_id "
                                    + " where FY=? "
                                    + " AND process_authorization.SPC=? "
                                    + " and ((annual_establish_report.cadre_code<>'AIS' and annual_establish_report.cadre_code<>'UGC' and annual_establish_report.cadre_code<>'OJS') or annual_establish_report.cadre_code is null) "
                                    + " and post_group=? "
                                    + " and aer_part_type=? "
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='REVIEWER' and is_approver_submitted='Y'  and is_reviewer_submitted='Y' "
                                    + " group by  postname, post_group, annual_establish_report.pay_scale_7th_pay, "
                                    + " annual_establish_report.pay_scale_6th_pay, annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  order by annual_establish_report.pay_scale_7th_pay desc ";

                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, postgrp);
                            pst.setString(4, partTYpe);

                        } else if (postgrp.equals("D")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position, "
                                    + " postname, post_group, annual_establish_report.pay_scale_7th_pay, pay_scale_6th_pay, "
                                    + " annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  from aer_report_submit aer "
                                    + " INNER JOIN process_authorization ON aer.co_off_code=process_authorization.off_code "
                                    + " inner join g_spc on aer.approver_spc=g_spc.spc "
                                    + " inner join annual_establish_report on aer.aer_id=annual_establish_report.aer_id "
                                    + " where FY=? AND process_authorization.SPC=? "
                                    + " and ((annual_establish_report.cadre_code<>'AIS' and annual_establish_report.cadre_code<>'UGC' and annual_establish_report.cadre_code<>'OJS') or annual_establish_report.cadre_code is null) "
                                    + " and (post_group=? or post_group is null) and aer_part_type=? "
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='REVIEWER' and is_approver_submitted='Y'  and is_reviewer_submitted='Y' "
                                    + " group by postname, post_group, annual_establish_report.pay_scale_7th_pay, "
                                    + " annual_establish_report.pay_scale_6th_pay, annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  order by annual_establish_report.pay_scale_7th_pay desc ";
                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, postgrp);
                            pst.setString(4, partTYpe);
                        }
                    } else if (partTYpe.equalsIgnoreCase("PART-C")) {
                        sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position, "
                                + " postname, post_group, annual_establish_report.pay_scale_7th_pay, pay_scale_6th_pay, "
                                + " annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  from aer_report_submit aer "
                                + " INNER JOIN process_authorization ON aer.co_off_code=process_authorization.off_code "
                                + " inner join g_spc on aer.approver_spc=g_spc.spc "
                                + " inner join annual_establish_report on aer.aer_id=annual_establish_report.aer_id "
                                + " where FY=? "
                                + " AND process_authorization.SPC=? "
                                + " and aer_part_type=? "
                                + " and process_authorization.process_id=13 and process_authorization.role_type='REVIEWER' and is_approver_submitted='Y'  and is_reviewer_submitted='Y' "
                                + " group by  postname, post_group, annual_establish_report.pay_scale_7th_pay, "
                                + " annual_establish_report.pay_scale_6th_pay, annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  order by annual_establish_report.pay_scale_7th_pay desc ";
                        pst = con.prepareStatement(sql);
                        pst.setString(1, fy);
                        pst.setString(2, spc);
                        pst.setString(3, partTYpe);
                    }
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        AnnualEstablishment aer = new AnnualEstablishment();
                        aer.setSanctionedStrength(rs.getInt("sanction_strength"));
                        aer.setMeninPosition(rs.getInt("menin_position"));
                        aer.setVacancyPosition(rs.getInt("vacancy_position"));
                        //aer.setGpc(rs.getString("gpc"));
                        aer.setPostname(rs.getString("postname"));
                        aer.setPostgrp(rs.getString("post_group"));
                        aer.setScaleofPay(rs.getString("pay_scale_6th_pay"));
                        aer.setScaleofPay7th(rs.getString("pay_scale_7th_pay"));
                        aer.setGp(rs.getString("gp"));
                        aer.setCadreType(rs.getString("cadre_code"));
                        aer.setPartType(rs.getString("aer_part_type"));
                        aeList.add(aer);
                    }
                } else if (roletype.equalsIgnoreCase("VERIFIER")) {
                    if (partTYpe.equalsIgnoreCase("PART-A")) {
                        if (cadreType != null && !cadreType.equals("")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position, "
                                    + " postname, post_group, annual_establish_report.pay_scale_7th_pay, pay_scale_6th_pay, "
                                    + " annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  from aer_report_submit aer "
                                    + " INNER JOIN process_authorization ON aer.co_off_code=process_authorization.off_code "
                                    + " inner join g_spc on aer.approver_spc=g_spc.spc "
                                    + " inner join annual_establish_report on aer.aer_id=annual_establish_report.aer_id "
                                    + " where FY=? "
                                    + " AND process_authorization.SPC=? "
                                    + " and annual_establish_report.cadre_code=? "
                                    + " and post_group=? "
                                    + " and aer_part_type=? "
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='VERIFIER' and is_reviewer_submitted='Y' and is_verifier_submitted='Y' "
                                    + " group by postname, post_group, annual_establish_report.pay_scale_7th_pay, "
                                    + " annual_establish_report.pay_scale_6th_pay, annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  order by annual_establish_report.pay_scale_7th_pay desc ";
                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, cadreType);
                            pst.setString(4, postgrp);
                            pst.setString(5, partTYpe);

                        } else if (!postgrp.equals("D")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position, "
                                    + " postname, post_group, annual_establish_report.pay_scale_7th_pay, pay_scale_6th_pay, "
                                    + " annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  from aer_report_submit aer "
                                    + " INNER JOIN process_authorization ON aer.co_off_code=process_authorization.off_code "
                                    + " inner join g_spc on aer.approver_spc=g_spc.spc "
                                    + " inner join annual_establish_report on aer.aer_id=annual_establish_report.aer_id "
                                    + " where FY=? "
                                    + " AND process_authorization.SPC=? "
                                    + " and ((annual_establish_report.cadre_code<>'AIS' and annual_establish_report.cadre_code<>'UGC' and annual_establish_report.cadre_code<>'OJS') or annual_establish_report.cadre_code is null) "
                                    + " and post_group=? "
                                    + " and aer_part_type=? "
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='VERIFIER' and is_reviewer_submitted='Y' and is_verifier_submitted='Y' "
                                    + " group by postname, post_group, annual_establish_report.pay_scale_7th_pay, "
                                    + " annual_establish_report.pay_scale_6th_pay, annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  order by annual_establish_report.pay_scale_7th_pay desc ";

                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, postgrp);
                            pst.setString(4, partTYpe);

                        } else if (postgrp.equals("D")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position, "
                                    + " postname, post_group, annual_establish_report.pay_scale_7th_pay, pay_scale_6th_pay, "
                                    + " annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  from aer_report_submit aer "
                                    + " INNER JOIN process_authorization ON aer.co_off_code=process_authorization.off_code "
                                    + " inner join g_spc on aer.approver_spc=g_spc.spc "
                                    + " inner join annual_establish_report on aer.aer_id=annual_establish_report.aer_id "
                                    + " where FY=? AND process_authorization.SPC=? "
                                    + " and ((annual_establish_report.cadre_code<>'AIS' and annual_establish_report.cadre_code<>'UGC' and annual_establish_report.cadre_code<>'OJS') or annual_establish_report.cadre_code is null) "
                                    + " and (post_group=? or post_group is null) and aer_part_type=? "
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='VERIFIER' and is_reviewer_submitted='Y' and is_verifier_submitted='Y' "
                                    + " group by postname, post_group, annual_establish_report.pay_scale_7th_pay, "
                                    + " annual_establish_report.pay_scale_6th_pay, annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  order by annual_establish_report.pay_scale_7th_pay desc ";
                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, postgrp);
                            pst.setString(4, partTYpe);
                        }
                    } else if (partTYpe.equalsIgnoreCase("PART-B")) {
                        if (cadreType != null && !cadreType.equals("")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position, "
                                    + " postname, post_group, annual_establish_report.pay_scale_7th_pay, pay_scale_6th_pay, "
                                    + " annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  from aer_report_submit aer "
                                    + " INNER JOIN process_authorization ON aer.co_off_code=process_authorization.off_code "
                                    + " inner join g_spc on aer.approver_spc=g_spc.spc "
                                    + " inner join annual_establish_report on aer.aer_id=annual_establish_report.aer_id "
                                    + " where FY=? "
                                    + " AND process_authorization.SPC=? "
                                    + " and annual_establish_report.cadre_code=? "
                                    + " and post_group=? "
                                    + " and aer_part_type=? "
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='VERIFIER' and is_reviewer_submitted='Y' and is_verifier_submitted='Y' "
                                    + " group by postname, post_group, annual_establish_report.pay_scale_7th_pay, "
                                    + " annual_establish_report.pay_scale_6th_pay, annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  order by annual_establish_report.pay_scale_7th_pay desc ";
                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, cadreType);
                            pst.setString(4, postgrp);
                            pst.setString(5, partTYpe);

                        } else if (!postgrp.equals("D")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position, "
                                    + " postname, post_group, annual_establish_report.pay_scale_7th_pay, pay_scale_6th_pay, "
                                    + " annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  from aer_report_submit aer "
                                    + " INNER JOIN process_authorization ON aer.co_off_code=process_authorization.off_code "
                                    + " inner join g_spc on aer.approver_spc=g_spc.spc "
                                    + " inner join annual_establish_report on aer.aer_id=annual_establish_report.aer_id "
                                    + " where FY=? "
                                    + " AND process_authorization.SPC=? "
                                    + " and ((annual_establish_report.cadre_code<>'AIS' and annual_establish_report.cadre_code<>'UGC' and annual_establish_report.cadre_code<>'OJS') or annual_establish_report.cadre_code is null) "
                                    + " and post_group=? "
                                    + " and aer_part_type=? "
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='VERIFIER' and is_reviewer_submitted='Y' and is_verifier_submitted='Y' "
                                    + " group by postname, post_group, annual_establish_report.pay_scale_7th_pay, "
                                    + " annual_establish_report.pay_scale_6th_pay, annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  order by annual_establish_report.pay_scale_7th_pay desc ";

                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, postgrp);
                            pst.setString(4, partTYpe);

                        } else if (postgrp.equals("D")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position, "
                                    + " postname, post_group, annual_establish_report.pay_scale_7th_pay, pay_scale_6th_pay, "
                                    + " annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  from aer_report_submit aer "
                                    + " INNER JOIN process_authorization ON aer.co_off_code=process_authorization.off_code "
                                    + " inner join g_spc on aer.approver_spc=g_spc.spc "
                                    + " inner join annual_establish_report on aer.aer_id=annual_establish_report.aer_id "
                                    + " where FY=? AND process_authorization.SPC=? "
                                    + " and ((annual_establish_report.cadre_code<>'AIS' and annual_establish_report.cadre_code<>'UGC' and annual_establish_report.cadre_code<>'OJS') or annual_establish_report.cadre_code is null) "
                                    + " and (post_group=? or post_group is null) and aer_part_type=? "
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='VERIFIER' and is_reviewer_submitted='Y' and is_verifier_submitted='Y' "
                                    + " group by postname, post_group, annual_establish_report.pay_scale_7th_pay, "
                                    + " annual_establish_report.pay_scale_6th_pay, annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  order by annual_establish_report.pay_scale_7th_pay desc ";
                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, postgrp);
                            pst.setString(4, partTYpe);
                        }
                    } else if (partTYpe.equalsIgnoreCase("PART-C")) {
                        sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position, "
                                + " postname, post_group, annual_establish_report.pay_scale_7th_pay, pay_scale_6th_pay, "
                                + " annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  from aer_report_submit aer "
                                + " INNER JOIN process_authorization ON aer.co_off_code=process_authorization.off_code "
                                + " inner join g_spc on aer.approver_spc=g_spc.spc "
                                + " inner join annual_establish_report on aer.aer_id=annual_establish_report.aer_id "
                                + " where FY=? "
                                + " AND process_authorization.SPC=? "
                                + " and aer_part_type=? "
                                + " and process_authorization.process_id=13 and process_authorization.role_type='VERIFIER' and is_reviewer_submitted='Y' and is_verifier_submitted='Y' "
                                + " group by postname, post_group, annual_establish_report.pay_scale_7th_pay, "
                                + " annual_establish_report.pay_scale_6th_pay, annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  order by annual_establish_report.pay_scale_7th_pay desc ";
                        pst = con.prepareStatement(sql);
                        pst.setString(1, fy);
                        pst.setString(2, spc);
                        pst.setString(3, partTYpe);
                    }

                    rs = pst.executeQuery();
                    while (rs.next()) {
                        AnnualEstablishment aer = new AnnualEstablishment();
                        aer.setSanctionedStrength(rs.getInt("sanction_strength"));
                        aer.setMeninPosition(rs.getInt("menin_position"));
                        aer.setVacancyPosition(rs.getInt("vacancy_position"));
                        //aer.setGpc(rs.getString("gpc"));
                        aer.setPostname(rs.getString("postname"));
                        aer.setPostgrp(rs.getString("post_group"));
                        aer.setScaleofPay(rs.getString("pay_scale_6th_pay"));
                        aer.setScaleofPay7th(rs.getString("pay_scale_7th_pay"));
                        aer.setGp(rs.getString("gp"));
                        aer.setCadreType(rs.getString("cadre_code"));
                        aer.setPartType(rs.getString("aer_part_type"));
                        aeList.add(aer);
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return aeList;
    }

    @Override
    public void addAEReportDataOther(String offcode, int aerId, String type) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "insert into annual_establish_report (off_code,aer_id,aer_part_type) values(?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, offcode);
            pst.setInt(2, aerId);
            pst.setString(3, type);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList DeptWiseAERStatus(String fiscalyear) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs1, rs2 = null;
        ArrayList outList = new ArrayList();
        String stm, stm1 = null;
        //String fyYear = "2021-22";
        String fyYear = fiscalyear;
        try {
            con = repodataSource.getConnection();
            stm = ("SELECT count(g_office.*) as  totalDdo ,department_name,g_department.department_code FROM g_department INNER JOIN g_office "
                    + " ON g_department.department_code=g_office.department_code WHERE 	g_department.if_active='Y' AND 	g_office.is_ddo='Y' GROUP BY  g_department.department_code ORDER BY department_name");
            ps = con.prepareStatement(stm);
            rs1 = ps.executeQuery();
            while (rs1.next()) {
                AnnualEstablishment aer = new AnnualEstablishment();
                aer.setDepartmentname(rs1.getString("department_name"));
                aer.setTotalDDO(rs1.getInt("totalDdo"));
                String deptCode = rs1.getString("department_code");
                aer.setDeptCode(deptCode);

                stm1 = ("SELECT (SELECT count(DISTINCT aer_report_submit.off_code) FROM aer_report_submit,g_office WHERE aer_report_submit.off_code=g_office.off_code AND  department_code = ? AND is_operator_submitted=? AND fy=? AND	g_office.is_ddo='Y' ) as operator_submitted,"
                        + "(SELECT count(DISTINCT aer_report_submit.off_code) FROM aer_report_submit,g_office WHERE aer_report_submit.off_code=g_office.off_code AND  department_code = ? AND is_reviewer_submitted=? AND fy=? AND g_office.is_ddo='Y') as reviewer_submitted,"
                        + "(SELECT count(DISTINCT aer_report_submit.off_code) FROM aer_report_submit,g_office WHERE aer_report_submit.off_code=g_office.off_code AND  department_code = ? AND is_verifier_submitted=? AND fy=? AND g_office.is_ddo='Y') as verifier_submitted,"
                        + "(SELECT count(DISTINCT aer_report_submit.co_off_code) FROM aer_report_submit,g_office WHERE aer_report_submit.co_off_code=g_office.off_code AND  department_code = ? AND is_acceptor_submitted=? AND fy=? AND g_office.is_ddo='Y') as acceptor_submitted,"
                        + "(SELECT count(DISTINCT aer_report_submit.off_code) FROM aer_report_submit,g_office WHERE aer_report_submit.off_code=g_office.off_code AND  department_code = ? AND is_approver_submitted=? AND fy=? AND g_office.is_ddo='Y') as approver_submitted");
                ps1 = con.prepareStatement(stm1);
                ps1.setString(1, deptCode);
                ps1.setString(2, "Y");
                ps1.setString(3, fyYear);
                ps1.setString(4, deptCode);
                ps1.setString(5, "Y");
                ps1.setString(6, fyYear);
                ps1.setString(7, deptCode);
                ps1.setString(8, "Y");
                ps1.setString(9, fyYear);
                ps1.setString(10, deptCode);
                ps1.setString(11, "Y");
                ps1.setString(12, fyYear);
                ps1.setString(13, deptCode);
                ps1.setString(14, "Y");
                ps1.setString(15, fyYear);
                rs2 = ps1.executeQuery();
                while (rs2.next()) {
                    aer.setOperatorSubmitted(rs2.getInt("operator_submitted"));
                    aer.setApproverSubmitted(rs2.getInt("approver_submitted"));
                    aer.setReviewerSubmitted(rs2.getInt("reviewer_submitted"));
                    aer.setVerifierSubmitted(rs2.getInt("verifier_submitted"));
                    aer.setAcceptorSubmitted(rs2.getInt("acceptor_submitted"));
                }

                outList.add(aer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps, ps1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outList;
    }

    @Override
    public ArrayList DDOWiseAERStatus(String dcode) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs1, rs2 = null;
        ArrayList outList = new ArrayList();
        String stm, stm1 = null;
        String fyYear = "2022-23";
        try {
            con = repodataSource.getConnection();
            stm = ("SELECT g_office.off_en ,g_office.off_code,g_department.department_code FROM g_department INNER JOIN g_office  ON g_department.department_code=g_office.department_code WHERE  "
                    + " g_department.if_active='Y' AND 	g_office.is_ddo='Y' AND g_office.department_code=? ORDER BY g_office.off_en");
            ps = con.prepareStatement(stm);
            ps.setString(1, dcode);
            rs1 = ps.executeQuery();
            while (rs1.next()) {
                AnnualEstablishment aer = new AnnualEstablishment();
                aer.setDepartmentname(rs1.getString("off_en"));
                String deptCode = rs1.getString("department_code");
                String offCode = rs1.getString("off_code");
                aer.setDeptCode(deptCode);
                aer.setOffCode(offCode);

                stm1 = ("SELECT (SELECT count(DISTINCT aer_report_submit.off_code) FROM aer_report_submit,g_office WHERE aer_report_submit.off_code=g_office.off_code AND  department_code = ? AND is_operator_submitted=? AND g_office.off_code=? AND fy=? AND	g_office.is_ddo='Y') as operator_submitted,"
                        + "(SELECT count(DISTINCT aer_report_submit.off_code) FROM aer_report_submit,g_office WHERE aer_report_submit.off_code=g_office.off_code AND  department_code = ? AND is_reviewer_submitted=? AND g_office.off_code=? AND fy=? AND	g_office.is_ddo='Y' ) as reviewer_submitted,"
                        + "(SELECT count(DISTINCT aer_report_submit.off_code) FROM aer_report_submit,g_office WHERE aer_report_submit.off_code=g_office.off_code AND  department_code = ? AND is_verifier_submitted=? AND g_office.off_code=? AND fy=? AND	g_office.is_ddo='Y') as verifier_submitted,"
                        + "(SELECT count(DISTINCT aer_report_submit.off_code) FROM aer_report_submit,g_office WHERE aer_report_submit.off_code=g_office.off_code AND  department_code = ? AND is_acceptor_submitted=? AND g_office.off_code=? AND fy=? AND	g_office.is_ddo='Y') as acceptor_submitted,"
                        + "(SELECT count(DISTINCT aer_report_submit.off_code) FROM aer_report_submit,g_office WHERE aer_report_submit.off_code=g_office.off_code AND  department_code = ? AND is_approver_submitted=? AND g_office.off_code=? AND fy=? AND	g_office.is_ddo='Y') as approver_submitted");
                ps1 = con.prepareStatement(stm1);
                ps1.setString(1, deptCode);
                ps1.setString(2, "Y");
                ps1.setString(3, offCode);
                ps1.setString(4, fyYear);
                ps1.setString(5, deptCode);
                ps1.setString(6, "Y");
                ps1.setString(7, offCode);
                ps1.setString(8, fyYear);
                ps1.setString(9, deptCode);
                ps1.setString(10, "Y");
                ps1.setString(11, offCode);
                ps1.setString(12, fyYear);
                ps1.setString(13, deptCode);
                ps1.setString(14, "Y");
                ps1.setString(15, offCode);
                ps1.setString(16, fyYear);
                ps1.setString(17, deptCode);
                ps1.setString(18, "Y");
                ps1.setString(19, offCode);
                ps1.setString(20, fyYear);
                rs2 = ps1.executeQuery();
                while (rs2.next()) {
                    aer.setOperatorSubmitted(rs2.getInt("operator_submitted"));
                    aer.setApproverSubmitted(rs2.getInt("approver_submitted"));
                    aer.setReviewerSubmitted(rs2.getInt("reviewer_submitted"));
                    aer.setVerifierSubmitted(rs2.getInt("verifier_submitted"));
                    aer.setAcceptorSubmitted(rs2.getInt("acceptor_submitted"));
                }

                outList.add(aer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps, ps1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outList;
    }

    @Override
    public ArrayList DistWiseAERReport(String fy) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs1, rs2 = null;
        ArrayList outList = new ArrayList();
        String stm, stm1 = null;
        String fyYear = fy;
        try {
            con = repodataSource.getConnection();
            stm = ("SELECT count(g_office.*) as  totalDdo ,g_district.dist_code,dist_name FROM g_district INNER JOIN g_office "
                    + " ON g_district.dist_code=g_office.dist_code WHERE g_office.is_ddo='Y' GROUP BY  g_district.dist_code ORDER BY dist_name");
            ps = con.prepareStatement(stm);
            rs1 = ps.executeQuery();
            while (rs1.next()) {
                AnnualEstablishment aer = new AnnualEstablishment();
                aer.setDepartmentname(rs1.getString("dist_name"));
                aer.setTotalDDO(rs1.getInt("totalDdo"));
                String distCode = rs1.getString("dist_code");
                aer.setDeptCode(distCode);

                stm1 = ("SELECT (SELECT count(DISTINCT aer_report_submit.off_code) FROM aer_report_submit,g_office WHERE aer_report_submit.off_code=g_office.off_code AND  g_office.dist_code = ? AND is_operator_submitted=? AND fy=? AND g_office.is_ddo='Y') as operator_submitted,"
                        + "(SELECT count(DISTINCT aer_report_submit.off_code) FROM aer_report_submit,g_office WHERE aer_report_submit.off_code=g_office.off_code AND  g_office.dist_code = ? AND is_reviewer_submitted=? AND fy=? AND g_office.is_ddo='Y') as reviewer_submitted,"
                        + "(SELECT count(DISTINCT aer_report_submit.off_code) FROM aer_report_submit,g_office WHERE aer_report_submit.off_code=g_office.off_code AND  g_office.dist_code = ? AND is_verifier_submitted=? AND fy=? AND g_office.is_ddo='Y') as verifier_submitted,"
                        + "(SELECT count(DISTINCT aer_report_submit.off_code) FROM aer_report_submit,g_office WHERE aer_report_submit.off_code=g_office.off_code AND  g_office.dist_code = ? AND is_acceptor_submitted=? AND fy=? AND g_office.is_ddo='Y') as acceptor_submitted,"
                        + "(SELECT count(DISTINCT aer_report_submit.off_code) FROM aer_report_submit,g_office WHERE aer_report_submit.off_code=g_office.off_code AND  g_office.dist_code = ? AND is_approver_submitted=? AND fy=? AND g_office.is_ddo='Y') as approver_submitted");
                ps1 = con.prepareStatement(stm1);
                ps1.setString(1, distCode);
                ps1.setString(2, "Y");
                ps1.setString(3, fyYear);
                ps1.setString(4, distCode);
                ps1.setString(5, "Y");
                ps1.setString(6, fyYear);
                ps1.setString(7, distCode);
                ps1.setString(8, "Y");
                ps1.setString(9, fyYear);
                ps1.setString(10, distCode);
                ps1.setString(11, "Y");
                ps1.setString(12, fyYear);
                ps1.setString(13, distCode);
                ps1.setString(14, "Y");
                ps1.setString(15, fyYear);
                rs2 = ps1.executeQuery();
                while (rs2.next()) {
                    aer.setOperatorSubmitted(rs2.getInt("operator_submitted"));
                    aer.setApproverSubmitted(rs2.getInt("approver_submitted"));
                    aer.setReviewerSubmitted(rs2.getInt("reviewer_submitted"));
                    aer.setVerifierSubmitted(rs2.getInt("verifier_submitted"));
                    aer.setAcceptorSubmitted(rs2.getInt("acceptor_submitted"));
                }

                outList.add(aer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps, ps1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outList;
    }

    @Override
    public ArrayList DistWiseOfficeAERReport(String distcode) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement pst3 = null;
        ResultSet rs1, rs2, rs3 = null;
        ArrayList outList = new ArrayList();
        String stm, stm1 = null;
        String fyYear = "2022-23";
        try {
            con = repodataSource.getConnection();
            stm = ("SELECT g_office.off_en ,g_office.off_code ,g_district.dist_code,dist_name FROM g_district INNER JOIN g_office  "
                    + " ON g_district.dist_code=g_office.dist_code AND  g_office.is_ddo='Y' AND g_office.dist_code=? ORDER BY g_office.off_en");
            ps = con.prepareStatement(stm);
            ps.setString(1, distcode);
            rs1 = ps.executeQuery();
            while (rs1.next()) {
                AnnualEstablishment aer = new AnnualEstablishment();
                aer.setDepartmentname(rs1.getString("off_en"));
                String dist_code = rs1.getString("dist_code");
                String offCode = rs1.getString("off_code");
                aer.setOffCode(offCode);

                stm1 = ("SELECT (SELECT count(DISTINCT aer_report_submit.off_code) FROM aer_report_submit,g_office WHERE aer_report_submit.off_code=g_office.off_code AND  dist_code = ? AND is_operator_submitted=? AND g_office.off_code=? AND fy=? AND g_office.is_ddo='Y') as operator_submitted,"
                        + "(SELECT count(DISTINCT aer_report_submit.off_code) FROM aer_report_submit,g_office WHERE aer_report_submit.off_code=g_office.off_code AND  dist_code = ? AND is_reviewer_submitted=? AND g_office.off_code=? AND fy=? AND g_office.is_ddo='Y') as reviewer_submitted,"
                        + "(SELECT count(DISTINCT aer_report_submit.off_code) FROM aer_report_submit,g_office WHERE aer_report_submit.off_code=g_office.off_code AND  dist_code = ? AND is_verifier_submitted=? AND g_office.off_code=? AND fy=? AND g_office.is_ddo='Y') as verifier_submitted,"
                        + "(SELECT count(DISTINCT aer_report_submit.off_code) FROM aer_report_submit,g_office WHERE aer_report_submit.off_code=g_office.off_code AND  dist_code = ? AND is_acceptor_submitted=? AND g_office.off_code=? AND fy=? AND g_office.is_ddo='Y') as acceptor_submitted,"
                        + "(SELECT count(DISTINCT aer_report_submit.off_code) FROM aer_report_submit,g_office WHERE aer_report_submit.off_code=g_office.off_code AND  dist_code = ? AND is_approver_submitted=? AND g_office.off_code=? AND fy=? AND g_office.is_ddo='Y') as approver_submitted");
                ps1 = con.prepareStatement(stm1);
                ps1.setString(1, dist_code);
                ps1.setString(2, "Y");
                ps1.setString(3, offCode);
                ps1.setString(4, fyYear);
                ps1.setString(5, dist_code);
                ps1.setString(6, "Y");
                ps1.setString(7, offCode);
                ps1.setString(8, fyYear);
                ps1.setString(9, dist_code);
                ps1.setString(10, "Y");
                ps1.setString(11, offCode);
                ps1.setString(12, fyYear);
                ps1.setString(13, dist_code);
                ps1.setString(14, "Y");
                ps1.setString(15, offCode);
                ps1.setString(16, fyYear);
                ps1.setString(17, dist_code);
                ps1.setString(18, "Y");
                ps1.setString(19, offCode);
                ps1.setString(20, fyYear);
                rs2 = ps1.executeQuery();
                while (rs2.next()) {
                    aer.setOperatorSubmitted(rs2.getInt("operator_submitted"));
                    aer.setApproverSubmitted(rs2.getInt("approver_submitted"));
                    aer.setReviewerSubmitted(rs2.getInt("reviewer_submitted"));
                    aer.setVerifierSubmitted(rs2.getInt("verifier_submitted"));
                    aer.setAcceptorSubmitted(rs2.getInt("acceptor_submitted"));
                }
                
                pst3 = con.prepareStatement("SELECT aer_id from aer_report_submit where off_code=? and fy=?");
                pst3.setString(1,offCode);
                pst3.setString(2,fyYear);
                rs3 = pst3.executeQuery();
                if(rs3.next()){
                    aer.setAerId(rs3.getInt("aer_id"));
                }
                
                outList.add(aer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps, ps1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outList;
    }

    @Override
    public ArrayList getAERProcessUserNameList(int aerid) {

        Connection con = null;

        PreparedStatement pst1 = null;
        ResultSet rs1 = null;

        PreparedStatement pst2 = null;
        ResultSet rs2 = null;

        SelectOption so = null;

        ArrayList userlist = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            String sql1 = "select * from aer_report_submit where aer_id=?";
            pst1 = con.prepareStatement(sql1);
            pst1.setInt(1, aerid);
            rs1 = pst1.executeQuery();
            if (rs1.next()) {

                String sql2 = "select hrms_id,spc from aer_report_submit"
                        + " inner join process_authorization on aer_report_submit.off_code=process_authorization.off_code where aer_id=? and role_type='OPERATOR' and process_id=13";
                pst2 = con.prepareStatement(sql2);
                pst2.setInt(1, aerid);
                rs2 = pst2.executeQuery();
                if (rs2.next()) {
                    so = new SelectOption();
                    so.setLabel("OPERATOR");
                    so.setValue(getRoleName(rs2.getString("hrms_id"), rs2.getString("spc")));
                    userlist.add(so);
                }

                DataBaseFunctions.closeSqlObjects(rs2, pst2);

                sql2 = "select hrms_id,spc from aer_report_submit"
                        + " inner join process_authorization on aer_report_submit.off_code=process_authorization.off_code where aer_id=? and role_type='APPROVER' and process_id=13";
                pst2 = con.prepareStatement(sql2);
                pst2.setInt(1, aerid);
                rs2 = pst2.executeQuery();
                if (rs2.next()) {
                    so = new SelectOption();
                    so.setLabel("APPROVER");
                    so.setValue(getRoleName(rs2.getString("hrms_id"), rs2.getString("spc")));
                    userlist.add(so);
                }

                DataBaseFunctions.closeSqlObjects(rs2, pst2);

                so = new SelectOption();
                so.setLabel("CO OFFICE NAME");
                so.setValue(getCOOfficeName(rs1.getString("co_off_code")));
                userlist.add(so);

                sql2 = "select hrms_id,spc from aer_report_submit"
                        + " inner join process_authorization on aer_report_submit.co_off_code=process_authorization.off_code where aer_id=? and role_type='REVIEWER' and process_id=13";
                pst2 = con.prepareStatement(sql2);
                pst2.setInt(1, aerid);
                rs2 = pst2.executeQuery();
                while (rs2.next()) {
                    so = new SelectOption();
                    so.setLabel("REVIEWER");
                    so.setValue(getRoleName(rs2.getString("hrms_id"), rs2.getString("spc")));
                    userlist.add(so);
                }

                DataBaseFunctions.closeSqlObjects(rs2, pst2);

                sql2 = "select hrms_id,spc from aer_report_submit"
                        + " inner join process_authorization on aer_report_submit.co_off_code=process_authorization.off_code where aer_id=? and role_type='VERIFIER' and process_id=13";
                pst2 = con.prepareStatement(sql2);
                pst2.setInt(1, aerid);
                rs2 = pst2.executeQuery();
                while (rs2.next()) {
                    so = new SelectOption();
                    so.setLabel("VERIFIER");
                    so.setValue(getRoleName(rs2.getString("hrms_id"), rs2.getString("spc")));
                    userlist.add(so);
                }

                DataBaseFunctions.closeSqlObjects(rs2, pst2);

                sql2 = "select hrms_id,spc from aer_report_submit"
                        + " inner join aer_report_submit_at_co on aer_report_submit.co_aer_id=aer_report_submit_at_co.co_aer_id"
                        + " inner join process_authorization on aer_report_submit_at_co.ao_off_code=process_authorization.off_code where aer_id=? and role_type='DREVIEWER' and process_id=13";
                pst2 = con.prepareStatement(sql2);
                pst2.setInt(1, aerid);
                rs2 = pst2.executeQuery();
                while (rs2.next()) {
                    so = new SelectOption();
                    so.setLabel("DREVIEWER");
                    so.setValue(getRoleName(rs2.getString("hrms_id"), rs2.getString("spc")));
                    userlist.add(so);
                }

                DataBaseFunctions.closeSqlObjects(rs2, pst2);

                sql2 = "select hrms_id,spc from aer_report_submit"
                        + " inner join aer_report_submit_at_co on aer_report_submit.co_aer_id=aer_report_submit_at_co.co_aer_id"
                        + " inner join process_authorization on aer_report_submit_at_co.ao_off_code=process_authorization.off_code where aer_id=? and role_type='DVERIFIER' and process_id=13";
                pst2 = con.prepareStatement(sql2);
                pst2.setInt(1, aerid);
                rs2 = pst2.executeQuery();
                while (rs2.next()) {
                    so = new SelectOption();
                    so.setLabel("DVERIFIER");
                    so.setValue(getRoleName(rs2.getString("hrms_id"), rs2.getString("spc")));
                    userlist.add(so);
                }

                DataBaseFunctions.closeSqlObjects(rs2, pst2);

                /*sql2 = "select hrms_id,spc from aer_report_submit"
                 + " inner join process_authorization on aer_report_submit.co_off_code=process_authorization.off_code where aer_id=? and role_type='ACCEPTOR' and process_id=13";*/
                sql2 = "select hrms_id,spc from aer_report_submit"
                        + " inner join aer_report_submit_at_co on aer_report_submit.co_aer_id=aer_report_submit_at_co.co_aer_id"
                        + " inner join process_authorization on aer_report_submit_at_co.ao_off_code=process_authorization.off_code where aer_id=? and role_type='ACCEPTOR' and process_id=13";
                pst2 = con.prepareStatement(sql2);
                pst2.setInt(1, aerid);
                rs2 = pst2.executeQuery();
                while (rs2.next()) {
                    so = new SelectOption();
                    so.setLabel("ACCEPTOR");
                    so.setValue(getRoleName(rs2.getString("hrms_id"), rs2.getString("spc")));
                    userlist.add(so);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs1, pst1);
            DataBaseFunctions.closeSqlObjects(rs2, pst2);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return userlist;
    }

    private String getRoleName(String empid, String spc) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String empname = "";
        try {
            con = this.repodataSource.getConnection();

            String sql = "select ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') FULL_NAME,spn, mobile from emp_mast"
                    + " left outer join g_spc on emp_mast.cur_spc=g_spc.spc where emp_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                empname = rs.getString("FULL_NAME") + "(" + rs.getString("spn") + ") Mobile No- " + rs.getString("mobile");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empname;
    }

    private String getCOOfficeName(String offCode) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String offname = "";
        try {
            con = this.repodataSource.getConnection();

            String sql = "select off_en from g_office where off_code=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, offCode);
            rs = pst.executeQuery();
            if (rs.next()) {
                offname = rs.getString("off_en");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return offname;
    }

    @Override
    public AnnualEstablishment[] getAerViewOtherPartForCO(String fy, String spc, String partTYpe, String roletype) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List<AnnualEstablishment> aeList = new ArrayList<>();
        try {
            con = this.dataSource.getConnection();

            /*String sql = "select aer_other_establishment.* from aer_report_submit aer"
             + " INNER JOIN process_authorization ON aer.co_off_code=process_authorization.off_code"
             + " inner join g_spc on aer.approver_spc=g_spc.spc"
             + " inner join aer_other_establishment on aer.aer_id=aer_other_establishment.aer_id"
             + " where FY=?"
             + " AND process_authorization.SPC=?"
             + " and part_type=?"
             + " and process_authorization.process_id=13 and process_authorization.role_type='REVIEWER' and is_approver_submitted='Y' and is_reviewer_submitted='Y'";*/
            String sql = "select sum(sanc_strength) sanc_strength,sum(men_position) men_position,sum(vacancy) vacancy,aer_other_establishment.category,aer_other_establishment.post,aer_other_establishment.payscale_6th,aer_other_establishment.payscale_7th,"
                    + " aer_other_establishment.gp,aer_other_establishment.group_name,aer_other_establishment.remarks from aer_report_submit aer"
                    + " INNER JOIN process_authorization ON aer.co_off_code=process_authorization.off_code"
                    + " inner join g_spc on aer.approver_spc=g_spc.spc"
                    + " inner join aer_other_establishment on aer.aer_id=aer_other_establishment.aer_id"
                    + " where FY=?"
                    + " AND process_authorization.SPC=? and role_type=?"
                    + " and part_type=?"
                    + " and process_authorization.process_id=13 and is_approver_submitted='Y' and is_reviewer_submitted='Y' and is_verifier_submitted='Y' group by"
                    + " aer_other_establishment.category,aer_other_establishment.post,aer_other_establishment.payscale_6th,aer_other_establishment.payscale_7th,aer_other_establishment.gp,aer_other_establishment.group_name,aer_other_establishment.remarks";
            pst = con.prepareStatement(sql);
            pst.setString(1, fy);
            pst.setString(2, spc);
            pst.setString(3, roletype);
            pst.setString(4, partTYpe);
            rs = pst.executeQuery();
            while (rs.next()) {
                AnnualEstablishment aer = new AnnualEstablishment();
                aer.setOtherCategory(rs.getString("category"));
                aer.setOtherPost(rs.getString("post"));
                aer.setScaleofPay(rs.getString("payscale_6th"));
                aer.setOther7thPay(rs.getString("payscale_7th"));
                aer.setOtherRemarks(rs.getString("remarks"));
                aer.setOtherSS(rs.getInt("sanc_strength"));
                aer.setOtherVacancy(rs.getInt("vacancy"));
                aer.setMeninPosition((rs.getInt("sanc_strength") - rs.getInt("vacancy")));
                //aer.setAerId(rs.getInt("aer_id"));
                //aer.setAerOtherId(rs.getInt("aer_other_id"));
                //aer.setFinancialYear(rs.getString("fyear"));
                aer.setGroup(rs.getString("group_name"));
                //aer.setPartType(rs.getString("part_type"));
                aer.setGp(rs.getString("gp"));
                aeList.add(aer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        AnnualEstablishment addrarray[] = aeList.toArray(new AnnualEstablishment[aeList.size()]);
        return addrarray;
    }

    @Override
    public String getAERSubmittedOfficeName(int aerid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String officename = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "select ddo_code,off_en from aer_report_submit"
                    + " inner join g_office on aer_report_submit.off_code=g_office.off_code"
                    + " where aer_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, aerid);
            rs = pst.executeQuery();
            if (rs.next()) {
                officename = rs.getString("off_en") + "(" + rs.getString("ddo_code") + ")";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return officename;
    }

    @Override
    public List getTotalGranteeOfficeListWithAerdata(String poffcode, int aerId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        List officelist = new ArrayList();
        Office office = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT off_code,off_en,ddo_code from g_office where off_bill_status='GA' AND p_off_code=?  order by off_en asc");
            pstmt.setString(1, poffcode);
            rs = pstmt.executeQuery();

            pstmt2 = con.prepareStatement("SELECT * FROM aer_other_establishment WHERE aer_id=? and off_code=? AND part_type=? order by group_name");

            while (rs.next()) {
                office = new Office();
                office.setOffCode(rs.getString("off_code"));
                office.setOffName(rs.getString("off_en"));
                office.setDdoCode(rs.getString("ddo_code"));
                ArrayList li = new ArrayList();
                pstmt2.setInt(1, aerId);
                pstmt2.setString(2, office.getOffCode());
                pstmt2.setString(3, "B");
                rs2 = pstmt2.executeQuery();
                while (rs2.next()) {
                    AnnualEstablishment aer = new AnnualEstablishment();
                    aer.setOffCode(office.getOffCode());
                    aer.setOtherCategory(rs2.getString("category"));
                    aer.setOtherPost(rs2.getString("post"));
                    aer.setScaleofPay(rs2.getString("payscale_6th"));
                    aer.setOther7thPay(rs2.getString("payscale_7th"));
                    aer.setOtherRemarks(rs2.getString("remarks"));
                    aer.setOtherSS(rs2.getInt("sanc_strength"));
                    aer.setOtherVacancy(rs2.getInt("vacancy"));
                    aer.setMeninPosition((rs2.getInt("sanc_strength") - rs2.getInt("vacancy")));
                    aer.setAerId(rs2.getInt("aer_id"));
                    aer.setAerOtherId(rs2.getInt("aer_other_id"));
                    aer.setFinancialYear(rs2.getString("fyear"));
                    aer.setGroup(rs2.getString("group_name"));
                    aer.setPartType(rs2.getString("part_type"));
                    aer.setGp(rs2.getString("gp"));

                    // aer.setAddressId(result.getInt("ADDRESS_ID"));
                    li.add(aer);
                }
                office.setAerPostData(li);
                officelist.add(office);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return officelist;
    }

    @Override
    public int submitAerReportAsVerifier(String empId, String spc, String cooffCode, String aoOffCode, String fy) {
        Connection con = null;
        int coAerId = 0;
        PreparedStatement pst = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;

        String sql = "";

        String startTime = "";
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        startTime = dateFormat.format(cal.getTime());
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("insert into aer_report_submit_at_co (co_off_code,verifier_emp_id, verifier_spc, verifier_submitted_on, ao_off_code, fy) values(?,?,?,?,?,?) ", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, cooffCode);
            pst.setString(2, empId);
            pst.setString(3, spc);
            pst.setTimestamp(4, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.setString(5, aoOffCode);
            pst.setString(6, fy);
            pst.execute();
            rs = pst.getGeneratedKeys();
            if (rs.next()) {
                coAerId = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return coAerId;
    }

    @Override
    public void updateAerCOAerId(String empId, String spc, String cooffCode, String fy, int coAerId) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("UPDATE aer_report_submit SET co_aer_id =? WHERE co_off_code=? AND verifier_emp_id=? AND verifier_spc=? AND fy=?");
            pst.setInt(1, coAerId);
            pst.setString(2, cooffCode);
            pst.setString(3, empId);
            pst.setString(4, spc);
            pst.setString(5, fy);
            pst.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void insertConsolidatedDataForAOPartABFORGIAandC(int coAerId, String fy, String spc, String cooffCode, String cadreType, String postgrp, String partTYpe) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;

        String sql = "";
        try {
            con = this.dataSource.getConnection();

            if (partTYpe.equalsIgnoreCase("PART-A") || partTYpe.equalsIgnoreCase("PART-B")) {
                if (cadreType != null && !cadreType.equals("")) {
                    sql = "insert into annual_establish_report_at_co(co_aer_id, off_code, sanction_strength,menin_position,vacancy_position,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,gp,cadre_code,aer_part_type)"
                            + " select " + coAerId + ", '" + cooffCode + "' , sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                            + " postname, post_group, annual_establish_report.pay_scale_7th_pay, pay_scale_6th_pay,"
                            + " annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  from aer_report_submit aer"
                            + " INNER JOIN process_authorization ON aer.co_off_code=process_authorization.off_code"
                            + " inner join g_spc on aer.approver_spc=g_spc.spc"
                            + " inner join annual_establish_report on aer.aer_id=annual_establish_report.aer_id"
                            + " where FY=?"
                            + " AND process_authorization.SPC=?"
                            + " and annual_establish_report.cadre_code=?"
                            + " and post_group=?"
                            + " and aer_part_type=?"
                            + " and process_authorization.process_id=13 and process_authorization.role_type='VERIFIER' and is_approver_submitted='Y' and is_verifier_submitted='Y'"
                            + " group by  postname, post_group, annual_establish_report.pay_scale_7th_pay,"
                            + " annual_establish_report.pay_scale_6th_pay, annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, fy);
                    pst.setString(2, spc);
                    pst.setString(3, cadreType);
                    pst.setString(4, postgrp);
                    pst.setString(5, partTYpe);
                    pst.executeUpdate();
                } else if (!postgrp.equals("D")) {
                    sql = "insert into annual_establish_report_at_co(co_aer_id, off_code,  sanction_strength,menin_position,vacancy_position,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,gp,cadre_code,aer_part_type)"
                            + " select " + coAerId + ", '" + cooffCode + "' , sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                            + " postname, post_group, annual_establish_report.pay_scale_7th_pay, pay_scale_6th_pay,"
                            + " annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  from aer_report_submit aer"
                            + " INNER JOIN process_authorization ON aer.co_off_code=process_authorization.off_code"
                            + " inner join g_spc on aer.approver_spc=g_spc.spc"
                            + " inner join annual_establish_report on aer.aer_id=annual_establish_report.aer_id"
                            + " where FY=?"
                            + " AND process_authorization.SPC=?"
                            + " and ((annual_establish_report.cadre_code<>'AIS' and annual_establish_report.cadre_code<>'UGC' and annual_establish_report.cadre_code<>'OJS') or annual_establish_report.cadre_code is null) "
                            + " and post_group=?"
                            + " and aer_part_type=?"
                            + " and process_authorization.process_id=13 and process_authorization.role_type='VERIFIER' and is_approver_submitted='Y' and is_verifier_submitted='Y'"
                            + " group by postname, post_group, annual_establish_report.pay_scale_7th_pay,"
                            + " annual_establish_report.pay_scale_6th_pay, annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, fy);
                    pst.setString(2, spc);
                    pst.setString(3, postgrp);
                    pst.setString(4, partTYpe);
                    pst.executeUpdate();
                } else if (postgrp.equals("D")) {
                    sql = "insert into annual_establish_report_at_co(co_aer_id, off_code, sanction_strength,menin_position,vacancy_position,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,gp,cadre_code,aer_part_type)"
                            + " select " + coAerId + ", '" + cooffCode + "' , sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                            + " postname, post_group, annual_establish_report.pay_scale_7th_pay, pay_scale_6th_pay,"
                            + " annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  from aer_report_submit aer"
                            + " INNER JOIN process_authorization ON aer.co_off_code=process_authorization.off_code"
                            + " inner join g_spc on aer.approver_spc=g_spc.spc"
                            + " inner join annual_establish_report on aer.aer_id=annual_establish_report.aer_id"
                            + " where FY=?"
                            + " AND process_authorization.SPC=?"
                            + " and ((annual_establish_report.cadre_code<>'AIS' and annual_establish_report.cadre_code<>'UGC' and annual_establish_report.cadre_code<>'OJS') or annual_establish_report.cadre_code is null) "
                            + " and (post_group=? or post_group is null)"
                            + " and aer_part_type=?"
                            + " and process_authorization.process_id=13 and process_authorization.role_type='VERIFIER' and is_approver_submitted='Y' and is_verifier_submitted='Y'"
                            + " group by  postname, post_group, annual_establish_report.pay_scale_7th_pay,"
                            + " annual_establish_report.pay_scale_6th_pay, annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, fy);
                    pst.setString(2, spc);
                    pst.setString(3, postgrp);
                    pst.setString(4, partTYpe);
                    pst.executeUpdate();
                }
            }
            if (partTYpe.equalsIgnoreCase("PART-C")) {
                sql = "insert into annual_establish_report_at_co(co_aer_id, off_code, sanction_strength,menin_position,vacancy_position,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,gp,cadre_code,aer_part_type)"
                        + " select " + coAerId + ", '" + cooffCode + "'  , sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                        + " postname, post_group, annual_establish_report.pay_scale_7th_pay, pay_scale_6th_pay,"
                        + " annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  from aer_report_submit aer"
                        + " INNER JOIN process_authorization ON aer.co_off_code=process_authorization.off_code"
                        + " inner join g_spc on aer.approver_spc=g_spc.spc"
                        + " inner join annual_establish_report on aer.aer_id=annual_establish_report.aer_id"
                        + " where FY=?"
                        + " AND process_authorization.SPC=? "
                        + " and aer_part_type=? "
                        + " and process_authorization.process_id=13 and process_authorization.role_type='VERIFIER' and is_approver_submitted='Y'  and is_verifier_submitted='Y' "
                        + " group by postname, post_group, annual_establish_report.pay_scale_7th_pay, "
                        + " annual_establish_report.pay_scale_6th_pay, annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  ";
                pst = con.prepareStatement(sql);
                pst.setString(1, fy);
                pst.setString(2, spc);
                pst.setString(3, partTYpe);
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void insertConsolidatedDataForAOPartBDE(String fy, String spc, String partType, int coAerId, String cooffCode) {

        Connection con = null;

        PreparedStatement pst = null;

        ResultSet rs = null;

        String sql = "";

        try {
            con = this.dataSource.getConnection();

            sql = "insert into annual_establish_report_at_co(co_aer_id, off_code, sanction_strength,menin_position,vacancy_position,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,gp,aer_part_type) "
                    + " select " + coAerId + ", '" + cooffCode + "' , sum(sanc_strength) sanc_strength,sum(men_position) men_position,sum(vacancy) vacancy,aer_other_establishment.post,aer_other_establishment.group_name,aer_other_establishment.payscale_7th,aer_other_establishment.payscale_6th, "
                    + " aer_other_establishment.gp,part_type from aer_report_submit aer "
                    + " INNER JOIN process_authorization ON aer.co_off_code=process_authorization.off_code "
                    + " inner join g_spc on aer.approver_spc=g_spc.spc"
                    + " inner join aer_other_establishment on aer.aer_id=aer_other_establishment.aer_id "
                    + " where FY=?"
                    + " AND process_authorization.SPC=?"
                    + " and part_type=?"
                    + " and process_authorization.process_id=13 and process_authorization.role_type='VERIFIER' and is_approver_submitted='Y' and is_verifier_submitted='Y'"
                    + " group by "
                    + " aer_other_establishment.post,aer_other_establishment.group_name,aer_other_establishment.payscale_7th,aer_other_establishment.payscale_6th, "
                    + " aer_other_establishment.gp,part_type";
            pst = con.prepareStatement(sql);
            pst.setString(1, fy);
            pst.setString(2, spc);
            pst.setString(3, partType);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public String getcoOffCodeasVerifier(String empId, String fy) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String offCode = "";
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("SELECT off_code FROM process_authorization where hrms_id=? and role_type='VERIFIER' ");
            pst.setString(1, empId);
            //pst.setString(2, fy);
            rs = pst.executeQuery();
            if (rs.next()) {
                offCode = rs.getString("off_code");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return offCode;
    }

    public String getAoOffCode(String deptCode) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String offCode = "";
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("SELECT ao_off_code FROM g_department WHERE department_code=? ");
            pst.setString(1, deptCode);
            rs = pst.executeQuery();
            if (rs.next()) {
                offCode = rs.getString("ao_off_code");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return offCode;
    }

    @Override
    public String getSubmitStatusForReviewer(String empid, String spc, String fy, String cooffcode) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String status = "Y";
        try {
            con = this.dataSource.getConnection();

            String sql = "select co_off_code from aer_report_submit"
                    + " inner join process_authorization on aer_report_submit.co_off_code=process_authorization.off_code where is_reviewer_submitted='Y'"
                    + " and role_type='VERIFIER' and hrms_id=? and spc=? and fy=? and (co_aer_id = 0 or co_aer_id is null) group by co_off_code";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setString(2, spc);
            pst.setString(3, fy);
            rs = pst.executeQuery();
            if (rs.next()) {
                status = "N";
                cooffcode = rs.getString("co_off_code");
            }

            pst = con.prepareStatement(" SELECT co_aer_id FROM aer_report_submit_at_co where co_off_code=? and verifier_spc=? and fy=? ");
            pst.setString(1, cooffcode);
            pst.setString(2, spc);
            pst.setString(3, fy);
            rs = pst.executeQuery();
            if (rs.next()) {
                status = "Y";
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return status;
    }

    @Override
    public String getRoleTypeForReviewerAndVerifierOfAO(String empid, String spc, String fy, String cooffcode) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String roleType = "";
        try {
            con = this.dataSource.getConnection();
            String sql = "select role_type from process_authorization "
                    + " where (role_type='ACCEPTOR' or role_type='DREVIEWER' or role_type='DVERIFIER') and hrms_id=? and spc=? "
                    + " and off_code=? order by role_type ";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setString(2, spc);
            pst.setString(3, cooffcode);
            //pst.setString(4, fy);
            rs = pst.executeQuery();
            if (rs.next()) {
                roleType = rs.getString("role_type");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return roleType;
    }

    @Override
    public String getRoleTypeForReviewerAndVerifierOfAODisapprove(String empid, String spc, String fy, String cooffcode) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String roleType = "";
        try {
            con = this.dataSource.getConnection();
            String sql = "select role_type from process_authorization "
                    + " where (role_type='ACCEPTOR' or role_type='DREVIEWER' or role_type='DVERIFIER') and hrms_id=? and spc=? "
                    + " and off_code=? order by role_type ";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setString(2, spc);
            pst.setString(3, cooffcode);
            //pst.setString(4, fy);
            rs = pst.executeQuery();
            while (rs.next()) {
                if (!roleType.equals("")) {
                    roleType = roleType
                            + "_" + rs.getString("role_type");
                } else {
                    roleType = rs.getString("role_type");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return roleType;
    }

    public String getRoleTypeForReviewerAndVerifier(String empid, String spc, String fy, String cooffcode) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String roleType = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "select role_type from process_authorization "
                    + " where (role_type='VERIFIER' or role_type='REVIEWER') and hrms_id=? and spc=? "
                    + " order by role_type ";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setString(2, spc);
            //pst.setString(3, cooffcode);
            //pst.setString(3, fy);
            rs = pst.executeQuery();
            while (rs.next()) {
                if (roleType != null && !roleType.equals("")) {

                    if (rs.getString("role_type").equals("VERIFIER") && roleType.equals("REVIEWER")) {
                        roleType = "BO";
                    } else {
                        roleType = rs.getString("role_type");
                    }
                } else {
                    roleType = rs.getString("role_type");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return roleType;
    }

    @Override
    public ArrayList COWiseAERStatus() {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs1, rs2 = null;
        ArrayList outList = new ArrayList();
        String stm, stm1 = null;
        try {
            con = repodataSource.getConnection();
            stm = "SELECT off_code,off_en,(SELECT count(*) FROM aer_report_submit WHERE co_off_code=g_office.off_code AND fy=?) as cnt FROM g_office WHERE (lvl=? OR lvl=?)  order by off_en ";
            ps = con.prepareStatement(stm);
            ps.setString(1, "2022-23");
            ps.setString(2, "01");
            ps.setString(3, "02");

            rs1 = ps.executeQuery();
            while (rs1.next()) {
                AnnualEstablishment aer = new AnnualEstablishment();
                aer.setOffCode(rs1.getString("off_code"));
                aer.setOffName(rs1.getString("off_en"));
                aer.setTotalDDO(rs1.getInt("cnt"));
                String offCode = rs1.getString("off_code");

                stm1 = "SELECT count(*) as coStatus FROM aer_report_submit_at_co WHERE co_off_code=? AND  ao_off_code is NOT NULL  ";
                ps1 = con.prepareStatement(stm1);
                ps1.setString(1, offCode);
                rs2 = ps1.executeQuery();
                String Status = "N";
                while (rs2.next()) {
                    if (rs2.getInt("coStatus") > 0) {
                        Status = "Y";
                    }

                }
                aer.setCoStatus(Status);

                outList.add(aer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps, ps1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outList;
    }

    @Override
    public ArrayList getAERBillGroupNameList(int aerid) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList billgrouplist = new ArrayList();
        SelectOption so = null;
        try {
            con = this.dataSource.getConnection();
            String sql = "select aer2_co_mapping.bill_group_id,description from aer2_co_mapping"
                    + " inner join bill_group_master on aer2_co_mapping.bill_group_id=bill_group_master.bill_group_id where aer_id=? order by description";
            pst = con.prepareStatement(sql);
            pst.setInt(1, aerid);
            rs = pst.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("bill_group_id"));
                so.setLabel(rs.getString("description"));
                billgrouplist.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return billgrouplist;
    }

    @Override
    public void deleteAERBillGroupNameList(int aerid, BigDecimal billgroupid) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = this.dataSource.getConnection();
            String sql = "DELETE FROM aer2_co_mapping WHERE aer_id=? AND bill_group_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, aerid);
            pst.setBigDecimal(2, billgroupid);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public int getCoAerid(String empid, String spc, String fy, String cooffcode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        int coaerId = 0;
        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement(" SELECT co_aer_id FROM aer_report_submit_at_co where co_off_code=? and verifier_spc=? and fy=? ");
            pst.setString(1, cooffcode);
            pst.setString(2, spc);
            pst.setString(3, fy);
            rs = pst.executeQuery();
            if (rs.next()) {
                coaerId = rs.getInt("co_aer_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return coaerId;
    }

    @Override
    public ArrayList TreasuryWiseAERStatus(String fiscalyear) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs1, rs2 = null;
        ArrayList outList = new ArrayList();
        String stm, stm1 = null;
        try {
            con = repodataSource.getConnection();
            stm = "SELECT count(a.tr_code) as cnt,a.tr_code,b.tr_name FROM g_office a,g_treasury b WHERE a.tr_code=b.tr_code AND a.is_ddo ='Y' AND  a.tr_code IS NOT NULL GROUP BY a.tr_code,b.tr_name  ORDER BY b.tr_name ";
            ps = con.prepareStatement(stm);
            rs1 = ps.executeQuery();
            // int trCode=0;
            while (rs1.next()) {
                AnnualEstablishment aer = new AnnualEstablishment();
                String trCode = rs1.getString("tr_code");
                aer.setTrCode(rs1.getString("tr_code"));
                aer.setTrName(rs1.getString("tr_name"));
                aer.setTotalDDO(rs1.getInt("cnt"));
                stm1 = "SELECT count(*) as Totalcnt FROM ( SELECT b.off_code  FROM g_office a,aer_report_submit b WHERE a.off_code=b.off_code AND a.tr_code=? AND fy=? GROUP BY b.off_code) AS c";
                ps1 = con.prepareStatement(stm1);
                ps1.setString(1, trCode);
                ps1.setString(2, fiscalyear);
                rs2 = ps1.executeQuery();
                int TotalOffice = 0;
                while (rs2.next()) {
                    aer.setTotalOffice(rs2.getInt("Totalcnt"));
                }

                outList.add(aer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps, ps1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outList;
    }

    @Override
    public ArrayList TreasuryWiseOfficeAERReport(String trCode) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs1, rs2 = null;
        ArrayList outList = new ArrayList();
        String stm, stm1 = null;
        try {
            con = repodataSource.getConnection();
            stm = ("SELECT g_office.off_en ,g_office.off_code  FROM  g_office WHERE  g_office.tr_code=? AND is_ddo ='Y' ORDER BY g_office.off_en  ");
            ps = con.prepareStatement(stm);
            ps.setString(1, trCode);
            rs1 = ps.executeQuery();
            while (rs1.next()) {
                AnnualEstablishment aer = new AnnualEstablishment();
                aer.setDepartmentname(rs1.getString("off_en"));
                //   String dist_code = rs1.getString("dist_code");
                String offCode = rs1.getString("off_code");
                aer.setOffCode(offCode);

                stm1 = ("SELECT (SELECT count(DISTINCT aer_report_submit.off_code) FROM aer_report_submit,g_office WHERE aer_report_submit.off_code=g_office.off_code AND  fy = ? AND is_operator_submitted=? AND g_office.off_code=?) as operator_submitted,"
                        + "(SELECT count(DISTINCT aer_report_submit.off_code) FROM aer_report_submit,g_office WHERE aer_report_submit.off_code=g_office.off_code AND  fy = ? AND is_reviewer_submitted=? AND g_office.off_code=?) as reviewer_submitted,"
                        + "(SELECT count(DISTINCT aer_report_submit.off_code) FROM aer_report_submit,g_office WHERE aer_report_submit.off_code=g_office.off_code AND  fy = ? AND is_verifier_submitted=? AND g_office.off_code=?) as verifier_submitted,"
                        + "(SELECT count(DISTINCT aer_report_submit.off_code) FROM aer_report_submit,g_office WHERE aer_report_submit.off_code=g_office.off_code AND  fy = ? AND is_acceptor_submitted=? AND g_office.off_code=?) as acceptor_submitted,"
                        + "(SELECT count(DISTINCT aer_report_submit.off_code) FROM aer_report_submit,g_office WHERE aer_report_submit.off_code=g_office.off_code AND  fy = ? AND is_approver_submitted=? AND g_office.off_code=?) as approver_submitted");
                ps1 = con.prepareStatement(stm1);
                ps1.setString(1, "2022-23");
                ps1.setString(2, "Y");
                ps1.setString(3, offCode);
                ps1.setString(4, "2022-23");
                ps1.setString(5, "Y");
                ps1.setString(6, offCode);
                ps1.setString(7, "2022-23");
                ps1.setString(8, "Y");
                ps1.setString(9, offCode);
                ps1.setString(10, "2022-23");
                ps1.setString(11, "Y");
                ps1.setString(12, offCode);
                ps1.setString(13, "2022-23");
                ps1.setString(14, "Y");
                ps1.setString(15, "2022-23");
                rs2 = ps1.executeQuery();
                while (rs2.next()) {
                    aer.setOperatorSubmitted(rs2.getInt("operator_submitted"));
                    aer.setApproverSubmitted(rs2.getInt("approver_submitted"));
                    aer.setReviewerSubmitted(rs2.getInt("reviewer_submitted"));
                    aer.setVerifierSubmitted(rs2.getInt("verifier_submitted"));
                    aer.setAcceptorSubmitted(rs2.getInt("acceptor_submitted"));
                }

                outList.add(aer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps, ps1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outList;
    }

    @Override
    public int getSchedule2AerId(String loginSpc, String empId, String fy) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        String offCode = "";
        int coaerid = 0;
        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("select off_code from process_authorization where ROLE_TYPE in ('REVIEWER','VERIFIER') and hrms_id=? and spc=? ");
            pst.setString(1, empId);
            pst.setString(2, loginSpc);
            rs = pst.executeQuery();
            if (rs.next()) {
                offCode = rs.getString("off_code");
            }

            String sql = "select co_aer_id from aer_report_submit_at_co where fy=? and co_off_code=? ";
            pst = con.prepareStatement(sql);

            pst.setString(1, fy);
            pst.setString(2, offCode);
            rs = pst.executeQuery();
            if (rs.next()) {
                coaerid = rs.getInt("co_aer_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return coaerid;
    }

    @Override
    public List getAerViewForAO(String fy, String spc, String aoOffCode, String postgrp, String cadreType, String partTYpe, String roletype) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List<AnnualEstablishment> aerList = new ArrayList<>();
        boolean isdataInserted = false;

        String sql = "";
        try {
            con = this.dataSource.getConnection();

            if (isdataInserted) {
                /*
                 sql = "";

                 if (partTYpe.equalsIgnoreCase("PART-A")) {
                 if (cadreType != null && !cadreType.equals("")) {

                 pst = con.prepareStatement(sql);
                 pst.setInt(1, coAerId);
                 pst.setString(2, cadreType);
                 pst.setString(3, postgrp);
                 pst.setString(4, partTYpe);

                 } else if (!postgrp.equals("D")) {
                 pst = con.prepareStatement(sql);
                 pst.setInt(1, coAerId);
                 pst.setString(2, cadreType);
                 pst.setString(3, postgrp);
                 pst.setString(4, partTYpe);

                 } else if (postgrp.equals("D")) {
                 pst = con.prepareStatement(sql);
                 pst.setInt(1, coAerId);
                 pst.setString(2, cadreType);
                 pst.setString(3, postgrp);
                 pst.setString(4, partTYpe);

                 }
                 } else if (partTYpe.equalsIgnoreCase("PART-C")) {
                 pst = con.prepareStatement(sql);
                 pst.setInt(1, coAerId);
                 pst.setString(2, partTYpe);
                 }

                 rs = pst.executeQuery();
                 while (rs.next()) {
                 AnnualEstablishment aer = new AnnualEstablishment();
                 aer.setSanctionedStrength(rs.getInt("sanction_strength"));
                 aer.setMeninPosition(rs.getInt("menin_position"));
                 aer.setVacancyPosition(rs.getInt("vacancy_position"));
                 aer.setGpc(rs.getString("gpc"));
                 aer.setPostname(rs.getString("postname"));
                 aer.setPostgrp(rs.getString("post_group"));
                 aer.setScaleofPay(rs.getString("pay_scale_6th_pay"));
                 aer.setScaleofPay7th(rs.getString("pay_scale_7th_pay"));
                 aer.setGp(rs.getString("gp"));
                 aer.setCadreType(rs.getString("cadre_code"));
                 aer.setPartType(rs.getString("aer_part_type"));
                 aeList.add(aer);
                 }
                 */
            } else {
                if (roletype.equalsIgnoreCase("DREVIEWER")) {
                    if (partTYpe.equalsIgnoreCase("PART-A")) {
                        if (cadreType != null && !cadreType.equals("")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                                    + " postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay, pay_scale_6th_pay,"
                                    + " annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type  from aer_report_submit_at_co aer"
                                    + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code"
                                    + /*" inner join g_spc on aer.dreviewer_spc=g_spc.spc" +*/ " inner join annual_establish_report_at_co on aer.co_aer_id=annual_establish_report_at_co.co_aer_id"
                                    + " where FY=?"
                                    + " AND process_authorization.SPC=?"
                                    + " and annual_establish_report_at_co.cadre_code=?"
                                    + " and post_group=?"
                                    + " and aer_part_type=?"
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='DREVIEWER'"
                                    + " group by postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay,"
                                    + " annual_establish_report_at_co.pay_scale_6th_pay, annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type order by pay_scale_7th_pay desc";
                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, cadreType);
                            pst.setString(4, postgrp);
                            pst.setString(5, partTYpe);

                        } else if (!postgrp.equals("D")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                                    + "  postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay, pay_scale_6th_pay,"
                                    + " annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type  from aer_report_submit_at_co aer"
                                    + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code"
                                    + /*" inner join g_spc on aer.dreviewer_spc=g_spc.spc" +*/ " inner join annual_establish_report_at_co on aer.co_aer_id=annual_establish_report_at_co.co_aer_id"
                                    + " where FY=?"
                                    + " AND process_authorization.SPC=?"
                                    + " and ((annual_establish_report_at_co.cadre_code<>'AIS' and annual_establish_report_at_co.cadre_code<>'UGC' and annual_establish_report_at_co.cadre_code<>'OJS') or annual_establish_report_at_co.cadre_code is null)"
                                    + " and post_group=?"
                                    + " and aer_part_type=?"
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='DREVIEWER'"
                                    + " group by postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay,"
                                    + " annual_establish_report_at_co.pay_scale_6th_pay, annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type order by pay_scale_7th_pay desc";

                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, postgrp);
                            pst.setString(4, partTYpe);
                        } else if (postgrp.equals("D")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                                    + "  postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay, pay_scale_6th_pay,"
                                    + " annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type  from aer_report_submit_at_co aer"
                                    + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code"
                                    + /*" inner join g_spc on aer.dreviewer_spc=g_spc.spc" +*/ " inner join annual_establish_report_at_co on aer.co_aer_id=annual_establish_report_at_co.co_aer_id"
                                    + " where FY=?"
                                    + " AND process_authorization.SPC=?"
                                    + " and ((annual_establish_report_at_co.cadre_code<>'AIS' and annual_establish_report_at_co.cadre_code<>'UGC' and annual_establish_report_at_co.cadre_code<>'OJS') or annual_establish_report_at_co.cadre_code is null)"
                                    + " and (post_group=? or post_group is null)"
                                    + " and aer_part_type=?"
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='DREVIEWER'"
                                    + " group by postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay,"
                                    + " annual_establish_report_at_co.pay_scale_6th_pay, annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type order by pay_scale_7th_pay desc";
                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, postgrp);
                            pst.setString(4, partTYpe);
                        }
                    } else if (partTYpe.equalsIgnoreCase("PART-B")) {
                        if (cadreType != null && !cadreType.equals("")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                                    + " postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay, pay_scale_6th_pay,"
                                    + " annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type  from aer_report_submit_at_co aer"
                                    + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code"
                                    + /*" inner join g_spc on aer.dreviewer_spc=g_spc.spc" +*/ " inner join annual_establish_report_at_co on aer.co_aer_id=annual_establish_report_at_co.co_aer_id"
                                    + " where FY=?"
                                    + " AND process_authorization.SPC=?"
                                    + " and annual_establish_report_at_co.cadre_code=?"
                                    + " and post_group=?"
                                    + " and aer_part_type=?"
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='DREVIEWER'"
                                    + " group by postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay,"
                                    + " annual_establish_report_at_co.pay_scale_6th_pay, annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type order by pay_scale_7th_pay desc";
                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, cadreType);
                            pst.setString(4, postgrp);
                            pst.setString(5, partTYpe);

                        } else if (!postgrp.equals("D")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                                    + " postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay, pay_scale_6th_pay,"
                                    + " annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type  from aer_report_submit_at_co aer"
                                    + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code"
                                    + /*" inner join g_spc on aer.dreviewer_spc=g_spc.spc" +*/ " inner join annual_establish_report_at_co on aer.co_aer_id=annual_establish_report_at_co.co_aer_id"
                                    + " where FY=?"
                                    + " AND process_authorization.SPC=?"
                                    + " and post_group=?"
                                    + " and aer_part_type=?"
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='DREVIEWER'"
                                    + " group by postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay,"
                                    + " annual_establish_report_at_co.pay_scale_6th_pay, annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type order by pay_scale_7th_pay desc";

                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, postgrp);
                            pst.setString(4, partTYpe);

                        } else if (postgrp.equals("D")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                                    + " postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay, pay_scale_6th_pay,"
                                    + " annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type  from aer_report_submit_at_co aer"
                                    + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code"
                                    + /*" inner join g_spc on aer.dreviewer_spc=g_spc.spc" +*/ " inner join annual_establish_report_at_co on aer.co_aer_id=annual_establish_report_at_co.co_aer_id"
                                    + " where FY=?"
                                    + " AND process_authorization.SPC=?"
                                    + " and (post_group=? or post_group is null)"
                                    + " and aer_part_type=?"
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='DREVIEWER'"
                                    + " group by postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay,"
                                    + " annual_establish_report_at_co.pay_scale_6th_pay, annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type order by pay_scale_7th_pay desc";
                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, postgrp);
                            pst.setString(4, partTYpe);
                        }
                    } else if (partTYpe.equalsIgnoreCase("PART-C")) {
                        sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                                + " postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay, pay_scale_6th_pay,"
                                + " annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type  from aer_report_submit_at_co aer"
                                + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code"
                                + /*" inner join g_spc on aer.dreviewer_spc=g_spc.spc" +*/ " inner join annual_establish_report_at_co on aer.co_aer_id=annual_establish_report_at_co.co_aer_id"
                                + " where FY=?"
                                + " AND process_authorization.SPC=?"
                                + " and aer_part_type=?"
                                + " and process_authorization.process_id=13 and process_authorization.role_type='DREVIEWER'"
                                + " group by postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay,"
                                + " annual_establish_report_at_co.pay_scale_6th_pay, annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type order by pay_scale_7th_pay desc";
                        pst = con.prepareStatement(sql);
                        pst.setString(1, fy);
                        pst.setString(2, spc);
                        pst.setString(3, partTYpe);
                    }
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        AnnualEstablishment aer = new AnnualEstablishment();
                        aer.setSanctionedStrength(rs.getInt("sanction_strength"));
                        aer.setMeninPosition(rs.getInt("menin_position"));
                        aer.setVacancyPosition(rs.getInt("vacancy_position"));
                        //aer.setGpc(rs.getString("gpc"));
                        aer.setPostname(rs.getString("postname"));
                        aer.setPostgrp(rs.getString("post_group"));
                        aer.setScaleofPay(rs.getString("pay_scale_6th_pay"));
                        aer.setScaleofPay7th(rs.getString("pay_scale_7th_pay"));
                        aer.setGp(rs.getString("gp"));
                        aer.setCadreType(rs.getString("cadre_code"));
                        aer.setPartType(rs.getString("aer_part_type"));
                        aerList.add(aer);
                    }
                } else if (roletype.equalsIgnoreCase("DVERIFIER")) {
                    if (partTYpe.equalsIgnoreCase("PART-A")) {
                        if (cadreType != null && !cadreType.equals("")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                                    + " postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay, pay_scale_6th_pay,"
                                    + " annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type  from aer_report_submit_at_co aer"
                                    + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code"
                                    + " inner join g_spc on aer.dreviewer_spc=g_spc.spc"
                                    + " inner join annual_establish_report_at_co on aer.co_aer_id=annual_establish_report_at_co.co_aer_id"
                                    + " where FY=?"
                                    + " AND process_authorization.SPC=?"
                                    + " and annual_establish_report_at_co.cadre_code=?"
                                    + " and post_group=?"
                                    + " and aer_part_type=?"
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='DVERIFIER' and dreviewer_emp_id is not null"
                                    + " group by postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay,"
                                    + " annual_establish_report_at_co.pay_scale_6th_pay, annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type order by pay_scale_7th_pay desc";
                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, cadreType);
                            pst.setString(4, postgrp);
                            pst.setString(5, partTYpe);

                        } else if (!postgrp.equals("D")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                                    + " postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay, pay_scale_6th_pay,"
                                    + " annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type  from aer_report_submit_at_co aer"
                                    + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code"
                                    + " inner join g_spc on aer.dreviewer_spc=g_spc.spc"
                                    + " inner join annual_establish_report_at_co on aer.co_aer_id=annual_establish_report_at_co.co_aer_id"
                                    + " where FY=?"
                                    + " AND process_authorization.SPC=?"
                                    + " and ((annual_establish_report_at_co.cadre_code<>'AIS' and annual_establish_report_at_co.cadre_code<>'UGC' and annual_establish_report_at_co.cadre_code<>'OJS') or annual_establish_report_at_co.cadre_code is null)"
                                    + " and post_group=?"
                                    + " and aer_part_type=?"
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='DVERIFIER' and dreviewer_emp_id is not null"
                                    + " group by postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay,"
                                    + " annual_establish_report_at_co.pay_scale_6th_pay, annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type order by pay_scale_7th_pay desc";

                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, postgrp);
                            pst.setString(4, partTYpe);

                        } else if (postgrp.equals("D")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                                    + " postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay, pay_scale_6th_pay,"
                                    + " annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type  from aer_report_submit_at_co aer"
                                    + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code"
                                    + " inner join g_spc on aer.dreviewer_spc=g_spc.spc"
                                    + " inner join annual_establish_report_at_co on aer.co_aer_id=annual_establish_report_at_co.co_aer_id"
                                    + " where FY=?"
                                    + " AND process_authorization.SPC=?"
                                    + " and ((annual_establish_report_at_co.cadre_code<>'AIS' and annual_establish_report_at_co.cadre_code<>'UGC' and annual_establish_report_at_co.cadre_code<>'OJS') or annual_establish_report_at_co.cadre_code is null)"
                                    + " and (post_group=? or post_group is null)"
                                    + " and aer_part_type=?"
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='DVERIFIER' and dreviewer_emp_id is not null"
                                    + " group by postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay,"
                                    + " annual_establish_report_at_co.pay_scale_6th_pay, annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type order by pay_scale_7th_pay desc";
                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, postgrp);
                            pst.setString(4, partTYpe);
                        }
                    } else if (partTYpe.equalsIgnoreCase("PART-B")) {
                        if (cadreType != null && !cadreType.equals("")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                                    + " postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay, pay_scale_6th_pay,"
                                    + " annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type  from aer_report_submit_at_co aer"
                                    + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code"
                                    + " inner join g_spc on aer.dreviewer_spc=g_spc.spc"
                                    + " inner join annual_establish_report_at_co on aer.co_aer_id=annual_establish_report_at_co.co_aer_id"
                                    + " where FY=?"
                                    + " AND process_authorization.SPC=?"
                                    + " and annual_establish_report_at_co.cadre_code=?"
                                    + " and post_group=?"
                                    + " and aer_part_type=?"
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='DVERIFIER' and dreviewer_emp_id is not null"
                                    + " group by postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay,"
                                    + " annual_establish_report_at_co.pay_scale_6th_pay, annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type order by pay_scale_7th_pay desc";
                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, cadreType);
                            pst.setString(4, postgrp);
                            pst.setString(5, partTYpe);

                        } else if (!postgrp.equals("D")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                                    + " postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay, pay_scale_6th_pay,"
                                    + " annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type  from aer_report_submit_at_co aer"
                                    + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code"
                                    + " inner join g_spc on aer.dreviewer_spc=g_spc.spc"
                                    + " inner join annual_establish_report_at_co on aer.co_aer_id=annual_establish_report_at_co.co_aer_id"
                                    + " where FY=?"
                                    + " AND process_authorization.SPC=?"
                                    + " and ((annual_establish_report_at_co.cadre_code<>'AIS' and annual_establish_report_at_co.cadre_code<>'UGC' and annual_establish_report_at_co.cadre_code<>'OJS') or annual_establish_report_at_co.cadre_code is null)"
                                    + " and post_group=?"
                                    + " and aer_part_type=?"
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='DVERIFIER' and dreviewer_emp_id is not null"
                                    + " group by postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay,"
                                    + " annual_establish_report_at_co.pay_scale_6th_pay, annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type order by pay_scale_7th_pay desc";

                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, postgrp);
                            pst.setString(4, partTYpe);

                        } else if (postgrp.equals("D")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                                    + " postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay, pay_scale_6th_pay,"
                                    + " annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type  from aer_report_submit_at_co aer"
                                    + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code"
                                    + " inner join g_spc on aer.dreviewer_spc=g_spc.spc"
                                    + " inner join annual_establish_report_at_co on aer.co_aer_id=annual_establish_report_at_co.co_aer_id"
                                    + " where FY=?"
                                    + " AND process_authorization.SPC=?"
                                    + " and ((annual_establish_report_at_co.cadre_code<>'AIS' and annual_establish_report_at_co.cadre_code<>'UGC' and annual_establish_report_at_co.cadre_code<>'OJS') or annual_establish_report_at_co.cadre_code is null)"
                                    + " and (post_group=? or post_group is null)"
                                    + " and aer_part_type=?"
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='DVERIFIER' and dreviewer_emp_id is not null"
                                    + " group by postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay,"
                                    + " annual_establish_report_at_co.pay_scale_6th_pay, annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type order by pay_scale_7th_pay desc";
                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, postgrp);
                            pst.setString(4, partTYpe);
                        }
                    } else if (partTYpe.equalsIgnoreCase("PART-C")) {
                        sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                                + " postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay, pay_scale_6th_pay,"
                                + " annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type  from aer_report_submit_at_co aer"
                                + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code"
                                + /*" inner join g_spc on aer.dreviewer_spc=g_spc.spc" +*/ " inner join annual_establish_report_at_co on aer.co_aer_id=annual_establish_report_at_co.co_aer_id"
                                + " where FY=?"
                                + " AND process_authorization.SPC=?"
                                + " and aer_part_type=?"
                                + " and process_authorization.process_id=13 and process_authorization.role_type='DVERIFIER'"
                                + " group by postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay,"
                                + " annual_establish_report_at_co.pay_scale_6th_pay, annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type order by pay_scale_7th_pay desc";
                        pst = con.prepareStatement(sql);
                        pst.setString(1, fy);
                        pst.setString(2, spc);
                        pst.setString(3, partTYpe);
                    }

                    rs = pst.executeQuery();
                    while (rs.next()) {
                        AnnualEstablishment aer = new AnnualEstablishment();
                        aer.setSanctionedStrength(rs.getInt("sanction_strength"));
                        aer.setMeninPosition(rs.getInt("menin_position"));
                        aer.setVacancyPosition(rs.getInt("vacancy_position"));
                        //aer.setGpc(rs.getString("gpc"));
                        aer.setPostname(rs.getString("postname"));
                        aer.setPostgrp(rs.getString("post_group"));
                        aer.setScaleofPay(rs.getString("pay_scale_6th_pay"));
                        aer.setScaleofPay7th(rs.getString("pay_scale_7th_pay"));
                        aer.setGp(rs.getString("gp"));
                        aer.setCadreType(rs.getString("cadre_code"));
                        aer.setPartType(rs.getString("aer_part_type"));
                        aerList.add(aer);
                    }
                } else if (roletype.equalsIgnoreCase("ACCEPTOR")) {
                    if (partTYpe.equalsIgnoreCase("PART-A")) {
                        if (cadreType != null && !cadreType.equals("")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                                    + " postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay, pay_scale_6th_pay,"
                                    + " annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type  from aer_report_submit_at_co aer"
                                    + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code"
                                    /*+ " inner join g_spc on aer.dreviewer_spc=g_spc.spc"*/
                                    + " inner join annual_establish_report_at_co on aer.co_aer_id=annual_establish_report_at_co.co_aer_id"
                                    + " where FY=?"
                                    + " AND process_authorization.SPC=?"
                                    + " and annual_establish_report_at_co.cadre_code=?"
                                    + " and post_group=?"
                                    + " and aer_part_type=?"
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='ACCEPTOR'"
                                    + " group by postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay,"
                                    + " annual_establish_report_at_co.pay_scale_6th_pay, annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type order by pay_scale_7th_pay desc";
                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, cadreType);
                            pst.setString(4, postgrp);
                            pst.setString(5, partTYpe);

                        } else if (!postgrp.equals("D")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                                    + " postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay, pay_scale_6th_pay,"
                                    + " annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type  from aer_report_submit_at_co aer"
                                    + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code"
                                    /*+ " inner join g_spc on aer.dreviewer_spc=g_spc.spc"*/
                                    + " inner join annual_establish_report_at_co on aer.co_aer_id=annual_establish_report_at_co.co_aer_id"
                                    + " where FY=?"
                                    + " AND process_authorization.SPC=?"
                                    + " and ((annual_establish_report_at_co.cadre_code<>'AIS' and annual_establish_report_at_co.cadre_code<>'UGC' and annual_establish_report_at_co.cadre_code<>'OJS') or annual_establish_report_at_co.cadre_code is null)"
                                    + " and post_group=?"
                                    + " and aer_part_type=?"
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='ACCEPTOR'"
                                    + " group by postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay,"
                                    + " annual_establish_report_at_co.pay_scale_6th_pay, annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type order by pay_scale_7th_pay desc";
                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, postgrp);
                            pst.setString(4, partTYpe);

                        } else if (postgrp.equals("D")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                                    + " postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay, pay_scale_6th_pay,"
                                    + " annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type  from aer_report_submit_at_co aer"
                                    + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code"
                                    /* + " inner join g_spc on aer.dreviewer_spc=g_spc.spc"*/
                                    + " inner join annual_establish_report_at_co on aer.co_aer_id=annual_establish_report_at_co.co_aer_id"
                                    + " where FY=?"
                                    + " AND process_authorization.SPC=?"
                                    + " and ((annual_establish_report_at_co.cadre_code<>'AIS' and annual_establish_report_at_co.cadre_code<>'UGC' and annual_establish_report_at_co.cadre_code<>'OJS') or annual_establish_report_at_co.cadre_code is null)"
                                    + " and (post_group=? or post_group is null)"
                                    + " and aer_part_type=?"
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='ACCEPTOR'"
                                    + " group by postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay,"
                                    + " annual_establish_report_at_co.pay_scale_6th_pay, annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type order by pay_scale_7th_pay desc";
                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, postgrp);
                            pst.setString(4, partTYpe);
                        }
                    } else if (partTYpe.equalsIgnoreCase("PART-B")) {
                        if (cadreType != null && !cadreType.equals("")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                                    + " postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay, pay_scale_6th_pay,"
                                    + " annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type  from aer_report_submit_at_co aer"
                                    + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code"
                                    /*+ " inner join g_spc on aer.dreviewer_spc=g_spc.spc"*/
                                    + " inner join annual_establish_report_at_co on aer.co_aer_id=annual_establish_report_at_co.co_aer_id"
                                    + " where FY=?"
                                    + " AND process_authorization.SPC=?"
                                    + " and annual_establish_report_at_co.cadre_code=?"
                                    + " and post_group=?"
                                    + " and aer_part_type=?"
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='ACCEPTOR'"
                                    + " group by postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay,"
                                    + " annual_establish_report_at_co.pay_scale_6th_pay, annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type order by pay_scale_7th_pay desc";
                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, cadreType);
                            pst.setString(4, postgrp);
                            pst.setString(5, partTYpe);

                        } else if (!postgrp.equals("D")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                                    + " postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay, pay_scale_6th_pay,"
                                    + " annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type  from aer_report_submit_at_co aer"
                                    + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code"
                                    /*+ " inner join g_spc on aer.dreviewer_spc=g_spc.spc"*/
                                    + " inner join annual_establish_report_at_co on aer.co_aer_id=annual_establish_report_at_co.co_aer_id"
                                    + " where FY=?"
                                    + " AND process_authorization.SPC=?"
                                    + " and ((annual_establish_report_at_co.cadre_code<>'AIS' and annual_establish_report_at_co.cadre_code<>'UGC' and annual_establish_report_at_co.cadre_code<>'OJS') or annual_establish_report_at_co.cadre_code is null)"
                                    + " and post_group=?"
                                    + " and aer_part_type=?"
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='ACCEPTOR'"
                                    + " group by postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay,"
                                    + " annual_establish_report_at_co.pay_scale_6th_pay, annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type order by pay_scale_7th_pay desc";
                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, postgrp);
                            pst.setString(4, partTYpe);

                        } else if (postgrp.equals("D")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                                    + " postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay, pay_scale_6th_pay,"
                                    + " annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type  from aer_report_submit_at_co aer"
                                    + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code"
                                    /*+ " inner join g_spc on aer.dreviewer_spc=g_spc.spc"*/
                                    + " inner join annual_establish_report_at_co on aer.co_aer_id=annual_establish_report_at_co.co_aer_id"
                                    + " where FY=?"
                                    + " AND process_authorization.SPC=?"
                                    + " and ((annual_establish_report_at_co.cadre_code<>'AIS' and annual_establish_report_at_co.cadre_code<>'UGC' and annual_establish_report_at_co.cadre_code<>'OJS') or annual_establish_report_at_co.cadre_code is null)"
                                    + " and (post_group=? or post_group is null)"
                                    + " and aer_part_type=?"
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='ACCEPTOR'"
                                    + " group by postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay,"
                                    + " annual_establish_report_at_co.pay_scale_6th_pay, annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type order by pay_scale_7th_pay desc";
                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, postgrp);
                            pst.setString(4, partTYpe);
                        }
                    } else if (partTYpe.equalsIgnoreCase("PART-C")) {
                        sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                                + " postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay, pay_scale_6th_pay,"
                                + " annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type  from aer_report_submit_at_co aer"
                                + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code"
                                + /*" inner join g_spc on aer.dreviewer_spc=g_spc.spc" +*/ " inner join annual_establish_report_at_co on aer.co_aer_id=annual_establish_report_at_co.co_aer_id"
                                + " where FY=?"
                                + " AND process_authorization.SPC=?"
                                + " and aer_part_type=?"
                                + " and process_authorization.process_id=13 and process_authorization.role_type='ACCEPTOR'"
                                + " group by postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay,"
                                + " annual_establish_report_at_co.pay_scale_6th_pay, annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type order by pay_scale_7th_pay desc";
                        pst = con.prepareStatement(sql);
                        pst.setString(1, fy);
                        pst.setString(2, spc);
                        pst.setString(3, partTYpe);
                    }

                    rs = pst.executeQuery();
                    while (rs.next()) {
                        AnnualEstablishment aer = new AnnualEstablishment();
                        aer.setSanctionedStrength(rs.getInt("sanction_strength"));
                        aer.setMeninPosition(rs.getInt("menin_position"));
                        aer.setVacancyPosition(rs.getInt("vacancy_position"));
                        //aer.setGpc(rs.getString("gpc"));
                        aer.setPostname(rs.getString("postname"));
                        aer.setPostgrp(rs.getString("post_group"));
                        aer.setScaleofPay(rs.getString("pay_scale_6th_pay"));
                        aer.setScaleofPay7th(rs.getString("pay_scale_7th_pay"));
                        aer.setGp(rs.getString("gp"));
                        aer.setCadreType(rs.getString("cadre_code"));
                        aer.setPartType(rs.getString("aer_part_type"));
                        aerList.add(aer);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return aerList;
    }

    @Override
    public void insertConsolidatedDataAfterAcceptorSubmit(int aoAerId, String fy, String spc, String aooffCode, String cadreType, String postgrp, String partTYpe) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String sql = "";
        try {
            con = this.dataSource.getConnection();

            if (partTYpe.equalsIgnoreCase("PART-A") || partTYpe.equalsIgnoreCase("PART-B")) {
                if (cadreType != null && !cadreType.equals("")) {
                    sql = "insert into annual_establish_report_at_ao(ao_aer_id, off_code, sanction_strength,menin_position,vacancy_position,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,gp,cadre_code,aer_part_type)"
                            + " select " + aoAerId + ", '" + aooffCode + "' ,sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                            + " '' gpc, postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay, pay_scale_6th_pay,"
                            + " annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type from aer_report_submit_at_co aer"
                            + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code"
                            + " inner join annual_establish_report_at_co on aer.co_aer_id=annual_establish_report_at_co.co_aer_id"
                            + " where FY=?"
                            + " AND process_authorization.SPC=?"
                            + " and annual_establish_report_at_co.cadre_code=?"
                            + " and post_group=?"
                            + " and aer_part_type=?"
                            + " and process_authorization.process_id=13 and process_authorization.role_type='ACCEPTOR'"
                            + " group by postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay,"
                            + " annual_establish_report_at_co.pay_scale_6th_pay, annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, fy);
                    pst.setString(2, spc);
                    pst.setString(3, cadreType);
                    pst.setString(4, postgrp);
                    pst.setString(5, partTYpe);
                    pst.executeUpdate();
                } else if (!postgrp.equals("D")) {
                    sql = "insert into annual_establish_report_at_ao(ao_aer_id, off_code, sanction_strength,menin_position,vacancy_position,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,gp,cadre_code,aer_part_type)"
                            + " select " + aoAerId + ", '" + aooffCode + "' ,sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                            + " '' gpc, postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay, pay_scale_6th_pay,"
                            + " annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type  from aer_report_submit_at_co aer"
                            + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code"
                            + " inner join annual_establish_report_at_co on aer.co_aer_id=annual_establish_report_at_co.co_aer_id"
                            + " where FY=?"
                            + " AND process_authorization.SPC=?"
                            + " and ((annual_establish_report_at_co.cadre_code<>'AIS' and annual_establish_report_at_co.cadre_code<>'UGC' and annual_establish_report_at_co.cadre_code<>'OJS') or annual_establish_report_at_co.cadre_code is null)"
                            + " and post_group=?"
                            + " and aer_part_type=?"
                            + " and process_authorization.process_id=13 and process_authorization.role_type='ACCEPTOR'"
                            + " group by postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay,"
                            + " annual_establish_report_at_co.pay_scale_6th_pay, annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, fy);
                    pst.setString(2, spc);
                    pst.setString(3, postgrp);
                    pst.setString(4, partTYpe);
                    pst.executeUpdate();
                } else if (postgrp.equals("D")) {
                    sql = "insert into annual_establish_report_at_ao(ao_aer_id, off_code, sanction_strength,menin_position,vacancy_position,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,gp,cadre_code,aer_part_type)"
                            + " select " + aoAerId + ", '" + aooffCode + "' ,sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                            + " '' gpc, postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay, pay_scale_6th_pay,"
                            + " annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type  from aer_report_submit_at_co aer"
                            + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code"
                            + " inner join annual_establish_report_at_co on aer.co_aer_id=annual_establish_report_at_co.co_aer_id"
                            + " where FY=?"
                            + " AND process_authorization.SPC=?"
                            + " and ((annual_establish_report_at_co.cadre_code<>'AIS' and annual_establish_report_at_co.cadre_code<>'UGC' and annual_establish_report_at_co.cadre_code<>'OJS') or annual_establish_report_at_co.cadre_code is null)"
                            + " and (post_group=? or post_group is null)"
                            + " and aer_part_type=?"
                            + " and process_authorization.process_id=13 and process_authorization.role_type='ACCEPTOR'"
                            + " group by postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay,"
                            + " annual_establish_report_at_co.pay_scale_6th_pay, annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, fy);
                    pst.setString(2, spc);
                    pst.setString(3, postgrp);
                    pst.setString(4, partTYpe);
                    pst.executeUpdate();
                }
            } else if (partTYpe.equalsIgnoreCase("PART-C")) {
                sql = "insert into annual_establish_report_at_ao(ao_aer_id, off_code, sanction_strength,menin_position,vacancy_position,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,gp,cadre_code,aer_part_type)"
                        + " select " + aoAerId + ", '" + aooffCode + "' ,sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                        + " '' gpc, postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay, pay_scale_6th_pay,"
                        + " annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type  from aer_report_submit_at_co aer"
                        + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code"
                        + " inner join annual_establish_report_at_co on aer.co_aer_id=annual_establish_report_at_co.co_aer_id"
                        + " where FY=?"
                        + " AND process_authorization.SPC=?"
                        + " and ((annual_establish_report_at_co.cadre_code<>'AIS' and annual_establish_report_at_co.cadre_code<>'UGC' and annual_establish_report_at_co.cadre_code<>'OJS') or annual_establish_report_at_co.cadre_code is null)"
                        + " and aer_part_type=?"
                        + " and process_authorization.process_id=13 and process_authorization.role_type='ACCEPTOR'"
                        + " group by postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay,"
                        + " annual_establish_report_at_co.pay_scale_6th_pay, annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type";
                pst = con.prepareStatement(sql);
                pst.setString(1, fy);
                pst.setString(2, spc);
                pst.setString(3, partTYpe);
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void downloadPDFReportScheduleIII(Document document, String aerId, String offCode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String[] an = {"A", "B", "C", "D"};

        int totSanctionedStrength = 0;
        int totMenInPosition = 0;
        int totVacancy = 0;
        try {
            con = this.dataSource.getConnection();

            Font hdrTextFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.BLACK);
            Font hdrTextFont1 = new Font(Font.FontFamily.HELVETICA, 5, Font.BOLD, BaseColor.BLACK);
            Font dataHdrFont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
            Font dataValFont = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK);

            Font f1 = new Font();
            f1.setSize(10);
            f1.setFamily("Times New Roman");
            PdfPTable table = null;
            PdfPTable innertable = null;
            PdfPCell cell = null;
            PdfPCell innercell = null;

            table = new PdfPTable(1);
            table.setWidths(new int[]{2});
            table.setWidthPercentage(100);

            cell = new PdfPCell(new Phrase("SCHEDULE-III", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Relalting to Head of Office including the Head of Department and Administrative Department in respect of their own establishment", new Font(Font.FontFamily.TIMES_ROMAN, 7)));
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("ANNUAL ESTABLISHMENT REVIEW TO BE FURNISHED BY HEAD OF OFFICE TO HEAD OF THE DEPARTMENT BY END OF JANUARY EACH YEAR", new Font(Font.FontFamily.TIMES_ROMAN, 7)));
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("PART-A (Regular Establishment)", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD)));
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("CERTIFICATE", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD)));
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("The sanctioned strength of the establishment as on 1st January  is given below. I have reviewed the staff requirement having regard to the prescribed yardstricks, wherever applicable. I certify that continuance of all the posts as considered necessary.", new Font(Font.FontFamily.TIMES_ROMAN, 8)));
            cell.setColspan(4);
            cell.setFixedHeight(20);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            document.add(table);

            PdfPTable table1 = new PdfPTable(9);
            table1.setWidths(new int[]{8, 12, 10, 5, 5, 5, 5, 5, 7});
            table1.setWidthPercentage(100);

            PdfPCell datacell;

            datacell = new PdfPCell(new Phrase("Category of employee ", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setRowspan(2);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Despription of the Posts ", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setRowspan(2);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Pay Scale", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setColspan(2);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("As per 7th Pay ", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setRowspan(2);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Sanctioned  Strength ", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setRowspan(2);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Persons-in-Position", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setRowspan(2);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Vacancy Position", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setRowspan(2);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Remarks if any", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setRowspan(2);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("As per 6th Pay Commission", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setColspan(1);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("GP", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setColspan(1);
            table1.addCell(datacell);

            for (int i = 0; i < an.length; i++) {
                String ans = an[i];
                String groupType = "GROUP " + ans;

                if (ans.equals("A")) {
                    datacell = new PdfPCell(new Phrase(groupType, hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);
                    datacell = new PdfPCell(new Phrase("Other than UGC /Judiciary/ All India Services", new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK)));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    datacell.setColspan(8);
                    table1.addCell(datacell);

                    totSanctionedStrength = 0;
                    totMenInPosition = 0;
                    totVacancy = 0;

                    pst = con.prepareStatement("SELECT remarks, off_code, gpc, postname, post_group, pay_scale_7th_pay, pay_scale_6th_pay, sanction_strength,"
                            + " menin_position, vacancy_position, gp, post_group FROM annual_establish_report_at_ao"
                            + " WHERE ao_aer_id=? and post_group=? and aer_part_type='PART-A' and (cadre_code='' or cadre_code is null)"
                            + " order by post_group, pay_scale_7th_pay desc,pay_scale_6th_pay desc, postname");
                    pst.setInt(1, Integer.parseInt(aerId));
                    //pst.setString(2, offCode);
                    pst.setString(2, ans);
                    rs = pst.executeQuery();
                    while (rs.next()) {

                        datacell = new PdfPCell(new Phrase(" ", hdrTextFont1));
                        datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_6th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("GP"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_7th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("sanction_strength"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("menin_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("vacancy_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("remarks"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        totSanctionedStrength = totSanctionedStrength + rs.getInt("sanction_strength");
                        totMenInPosition = totMenInPosition + rs.getInt("menin_position");
                        totVacancy = totVacancy + rs.getInt("vacancy_position");

                    }

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);
                    datacell = new PdfPCell(new Phrase("All India Services", new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK)));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    datacell.setColspan(8);
                    table1.addCell(datacell);

                    pst = con.prepareStatement("SELECT remarks, off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength,"
                            + "  menin_position,vacancy_position,gp,post_group FROM annual_establish_report_at_ao"
                            + "  WHERE ao_aer_id=? and post_group=? and aer_part_type='PART-A' and cadre_code='AIS'"
                            + "  order by post_group, pay_scale_7th_pay desc,pay_scale_6th_pay desc, postname");
                    pst.setInt(1, Integer.parseInt(aerId));
                    //pst.setString(2, offCode);
                    pst.setString(2, ans);
                    rs = pst.executeQuery();
                    while (rs.next()) {

                        datacell = new PdfPCell(new Phrase(" ", hdrTextFont1));
                        datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_6th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("GP"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_7th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("sanction_strength"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("menin_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("vacancy_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("remarks"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        totSanctionedStrength = totSanctionedStrength + rs.getInt("sanction_strength");
                        totMenInPosition = totMenInPosition + rs.getInt("menin_position");
                        totVacancy = totVacancy + rs.getInt("vacancy_position");

                    }

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);
                    datacell = new PdfPCell(new Phrase("UGC", new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK)));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    datacell.setColspan(8);
                    table1.addCell(datacell);

                    pst = con.prepareStatement("SELECT remarks,off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength,"
                            + " menin_position,vacancy_position,gp,post_group FROM annual_establish_report_at_ao"
                            + " WHERE ao_aer_id=? and post_group=? and aer_part_type='PART-A' and cadre_code='UGC'"
                            + " order by post_group, pay_scale_7th_pay desc,pay_scale_6th_pay desc, postname");
                    pst.setInt(1, Integer.parseInt(aerId));
                    //pst.setString(2, offCode);
                    pst.setString(2, ans);
                    rs = pst.executeQuery();
                    while (rs.next()) {

                        datacell = new PdfPCell(new Phrase(" ", hdrTextFont1));
                        datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_6th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("GP"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_7th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("sanction_strength"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("menin_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("vacancy_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("remarks"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        totSanctionedStrength = totSanctionedStrength + rs.getInt("sanction_strength");
                        totMenInPosition = totMenInPosition + rs.getInt("menin_position");
                        totVacancy = totVacancy + rs.getInt("vacancy_position");

                    }

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);
                    datacell = new PdfPCell(new Phrase("Judiciary", new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK)));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    datacell.setColspan(8);
                    table1.addCell(datacell);

                    pst = con.prepareStatement("SELECT remarks,off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength,"
                            + " menin_position,vacancy_position,gp,post_group FROM annual_establish_report_at_ao"
                            + " WHERE ao_aer_id=? and post_group=? and aer_part_type='PART-A' and cadre_code='OJS'"
                            + " order by post_group, pay_scale_7th_pay desc,pay_scale_6th_pay desc, postname");
                    pst.setInt(1, Integer.parseInt(aerId));
                    //pst.setString(2, offCode);
                    pst.setString(2, ans);
                    rs = pst.executeQuery();
                    while (rs.next()) {

                        datacell = new PdfPCell(new Phrase(" ", hdrTextFont1));
                        datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_6th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("GP"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_7th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("sanction_strength"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("menin_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("vacancy_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("remarks"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        totSanctionedStrength = totSanctionedStrength + rs.getInt("sanction_strength");
                        totMenInPosition = totMenInPosition + rs.getInt("menin_position");
                        totVacancy = totVacancy + rs.getInt("vacancy_position");

                    }

                    /*Total*/
                    datacell = new PdfPCell(new Phrase("TOTAL", hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totSanctionedStrength + "", hdrTextFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totMenInPosition + "", hdrTextFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totVacancy + "", hdrTextFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);
                    /*Total*/
                } else {
                    datacell = new PdfPCell(new Phrase(groupType, hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    datacell.setColspan(9);
                    table1.addCell(datacell);

                    totSanctionedStrength = 0;
                    totMenInPosition = 0;
                    totVacancy = 0;

                    pst = con.prepareStatement("SELECT remarks,off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength,"
                            + " menin_position,vacancy_position,gp,post_group FROM annual_establish_report_at_ao"
                            + " WHERE ao_aer_id=? and post_group=? and aer_part_type='PART-A' and (cadre_code='' or cadre_code is null)"
                            + " order by post_group, pay_scale_7th_pay desc,pay_scale_6th_pay desc, postname");
                    pst.setInt(1, Integer.parseInt(aerId));
                    //pst.setString(2, offCode);
                    pst.setString(2, ans);
                    rs = pst.executeQuery();
                    while (rs.next()) {

                        datacell = new PdfPCell(new Phrase(" ", hdrTextFont1));
                        datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_6th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("GP"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_7th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("sanction_strength"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("menin_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("vacancy_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("remarks"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        totSanctionedStrength = totSanctionedStrength + rs.getInt("sanction_strength");
                        totMenInPosition = totMenInPosition + rs.getInt("menin_position");
                        totVacancy = totVacancy + rs.getInt("vacancy_position");

                    }
                    /*Total*/
                    datacell = new PdfPCell(new Phrase("TOTAL", hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totSanctionedStrength + "", hdrTextFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totMenInPosition + "", hdrTextFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totVacancy + "", hdrTextFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);
                    /*Total*/
                }

            }
            document.add(table1);

            /**
             * ************************************* Form B
             * *************************************
             */
            table1 = new PdfPTable(9);
            table1.setWidths(new int[]{8, 12, 10, 5, 5, 5, 5, 5, 7});
            table1.setWidthPercentage(100);

            datacell = new PdfPCell();

            datacell = new PdfPCell(new Phrase("PART-B (G-I-A Establishment Drawing from Treasury )", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD)));
            datacell.setColspan(9);
            datacell.setFixedHeight(20);
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("Category of employee ", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setRowspan(2);
            table1.addCell(datacell);
            datacell = new PdfPCell(new Phrase("Despription of the Posts ", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setRowspan(2);
            table1.addCell(datacell);
            datacell = new PdfPCell(new Phrase("Pay Scale", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setColspan(2);
            table1.addCell(datacell);
            datacell = new PdfPCell(new Phrase("As per 7th Pay ", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setRowspan(2);
            table1.addCell(datacell);
            datacell = new PdfPCell(new Phrase("Sanctioned  Strength ", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setRowspan(2);
            table1.addCell(datacell);
            datacell = new PdfPCell(new Phrase("Persons-in-Position", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setRowspan(2);
            table1.addCell(datacell);
            datacell = new PdfPCell(new Phrase("Vacancy Position", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setRowspan(2);
            table1.addCell(datacell);
            datacell = new PdfPCell(new Phrase("Remarks if any", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setRowspan(2);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("As per 6th Pay Commission", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setColspan(1);
            table1.addCell(datacell);

            datacell = new PdfPCell(new Phrase("GP", hdrTextFont1));
            datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setColspan(1);
            table1.addCell(datacell);

            for (int i = 0; i < an.length; i++) {
                String ans = an[i];
                String groupType = "GROUP " + ans;

                if (ans.equals("A")) {
                    datacell = new PdfPCell(new Phrase(groupType, hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);
                    datacell = new PdfPCell(new Phrase("Other than UGC /Judiciary/ All India Services", new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK)));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    datacell.setColspan(8);
                    table1.addCell(datacell);

                    totSanctionedStrength = 0;
                    totMenInPosition = 0;
                    totVacancy = 0;

                    pst = con.prepareStatement("SELECT remarks, off_code, gpc, postname, post_group, pay_scale_7th_pay, pay_scale_6th_pay, sanction_strength,"
                            + " menin_position, vacancy_position, gp, post_group FROM annual_establish_report_at_ao"
                            + " WHERE ao_aer_id=? and post_group=? and aer_part_type='PART-B' and (cadre_code='' or cadre_code is null)"
                            + " order by post_group, pay_scale_7th_pay desc,pay_scale_6th_pay desc, postname");
                    pst.setInt(1, Integer.parseInt(aerId));
                    //pst.setString(2, offCode);
                    pst.setString(2, ans);
                    rs = pst.executeQuery();
                    while (rs.next()) {

                        datacell = new PdfPCell(new Phrase(" ", hdrTextFont1));
                        datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_6th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("GP"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_7th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("sanction_strength"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("menin_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("vacancy_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("remarks"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        totSanctionedStrength = totSanctionedStrength + rs.getInt("sanction_strength");
                        totMenInPosition = totMenInPosition + rs.getInt("menin_position");
                        totVacancy = totVacancy + rs.getInt("vacancy_position");

                    }

                    /*Total*/
                    datacell = new PdfPCell(new Phrase("TOTAL", hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totSanctionedStrength + "", dataValFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totMenInPosition + "", dataValFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totVacancy + "", dataValFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);
                    /*Total*/

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);
                    datacell = new PdfPCell(new Phrase("All India Services", new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK)));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    datacell.setColspan(8);
                    table1.addCell(datacell);

                    totSanctionedStrength = 0;
                    totMenInPosition = 0;
                    totVacancy = 0;

                    pst = con.prepareStatement("SELECT remarks, off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength,"
                            + " menin_position,vacancy_position,gp,post_group FROM annual_establish_report_at_ao"
                            + " WHERE ao_aer_id=? and post_group=? and aer_part_type='PART-B' and cadre_code='AIS'"
                            + " order by post_group, pay_scale_7th_pay desc,pay_scale_6th_pay desc, postname");
                    pst.setInt(1, Integer.parseInt(aerId));
                    //pst.setString(2, offCode);
                    pst.setString(2, ans);
                    rs = pst.executeQuery();
                    while (rs.next()) {

                        datacell = new PdfPCell(new Phrase(" ", hdrTextFont1));
                        datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_6th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("GP"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_7th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("sanction_strength"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("menin_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("vacancy_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("remarks"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        totSanctionedStrength = totSanctionedStrength + rs.getInt("sanction_strength");
                        totMenInPosition = totMenInPosition + rs.getInt("menin_position");
                        totVacancy = totVacancy + rs.getInt("vacancy_position");

                    }

                    /*Total*/
                    datacell = new PdfPCell(new Phrase("TOTAL", hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totSanctionedStrength + "", dataValFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totMenInPosition + "", dataValFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totVacancy + "", dataValFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);
                    /*Total*/

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);
                    datacell = new PdfPCell(new Phrase("UGC", new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK)));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    datacell.setColspan(8);
                    table1.addCell(datacell);

                    totSanctionedStrength = 0;
                    totMenInPosition = 0;
                    totVacancy = 0;

                    pst = con.prepareStatement("SELECT remarks,off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength,"
                            + " menin_position,vacancy_position,gp,post_group FROM annual_establish_report_at_ao"
                            + " WHERE ao_aer_id=? and post_group=? and aer_part_type='PART-B' and cadre_code='UGC'"
                            + " order by post_group, pay_scale_7th_pay desc,pay_scale_6th_pay desc, postname");
                    pst.setInt(1, Integer.parseInt(aerId));
                    //pst.setString(2, offCode);
                    pst.setString(2, ans);
                    rs = pst.executeQuery();
                    while (rs.next()) {

                        datacell = new PdfPCell(new Phrase(" ", hdrTextFont1));
                        datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_6th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("GP"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_7th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("sanction_strength"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("menin_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("vacancy_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("remarks"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        totSanctionedStrength = totSanctionedStrength + rs.getInt("sanction_strength");
                        totMenInPosition = totMenInPosition + rs.getInt("menin_position");
                        totVacancy = totVacancy + rs.getInt("vacancy_position");

                    }

                    /*Total*/
                    datacell = new PdfPCell(new Phrase("TOTAL", hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totSanctionedStrength + "", dataValFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totMenInPosition + "", dataValFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totVacancy + "", dataValFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);
                    /*Total*/

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);
                    datacell = new PdfPCell(new Phrase("Judiciary", new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK)));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    datacell.setColspan(8);
                    table1.addCell(datacell);

                    totSanctionedStrength = 0;
                    totMenInPosition = 0;
                    totVacancy = 0;

                    pst = con.prepareStatement("SELECT remarks,off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength,"
                            + " menin_position,vacancy_position,gp,post_group FROM annual_establish_report_at_ao"
                            + " WHERE ao_aer_id=? and post_group=? and aer_part_type='PART-B' and cadre_code='OJS'"
                            + " order by post_group, pay_scale_7th_pay desc,pay_scale_6th_pay desc, postname");
                    pst.setInt(1, Integer.parseInt(aerId));
                    //pst.setString(2, offCode);
                    pst.setString(2, ans);
                    rs = pst.executeQuery();
                    while (rs.next()) {

                        datacell = new PdfPCell(new Phrase(" ", hdrTextFont1));
                        datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_6th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("GP"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_7th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("sanction_strength"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("menin_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("vacancy_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("remarks"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        totSanctionedStrength = totSanctionedStrength + rs.getInt("sanction_strength");
                        totMenInPosition = totMenInPosition + rs.getInt("menin_position");
                        totVacancy = totVacancy + rs.getInt("vacancy_position");

                    }

                    /*Total*/
                    datacell = new PdfPCell(new Phrase("TOTAL", hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totSanctionedStrength + "", dataValFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totMenInPosition + "", dataValFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totVacancy + "", dataValFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);
                    /*Total*/
                } else {
                    datacell = new PdfPCell(new Phrase(groupType, hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    datacell.setColspan(9);
                    table1.addCell(datacell);

                    totSanctionedStrength = 0;
                    totMenInPosition = 0;
                    totVacancy = 0;

                    pst = con.prepareStatement("SELECT remarks,off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength,"
                            + " menin_position,vacancy_position,gp,post_group FROM annual_establish_report_at_ao"
                            + " WHERE ao_aer_id=? and post_group=? and aer_part_type='PART-B' and (cadre_code='' or cadre_code is null)"
                            + " order by post_group, pay_scale_7th_pay desc,pay_scale_6th_pay desc, postname");
                    pst.setInt(1, Integer.parseInt(aerId));
                    //pst.setString(2, offCode);
                    pst.setString(2, ans);
                    rs = pst.executeQuery();
                    while (rs.next()) {

                        datacell = new PdfPCell(new Phrase(" ", hdrTextFont1));
                        datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_6th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("GP"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("pay_scale_7th_pay"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("sanction_strength"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("menin_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("vacancy_position"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        datacell = new PdfPCell(new Phrase(rs.getString("remarks"), dataValFont));
                        datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table1.addCell(datacell);

                        totSanctionedStrength = totSanctionedStrength + rs.getInt("sanction_strength");
                        totMenInPosition = totMenInPosition + rs.getInt("menin_position");
                        totVacancy = totVacancy + rs.getInt("vacancy_position");

                    }

                    /*Total*/
                    datacell = new PdfPCell(new Phrase("TOTAL", hdrTextFont1));
                    datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totSanctionedStrength + "", dataValFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totMenInPosition + "", dataValFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell(new Phrase(totVacancy + "", dataValFont));
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);

                    datacell = new PdfPCell();
                    datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table1.addCell(datacell);
                    /*Total*/
                }

            }
            document.add(table1);

            PdfPTable table5 = new PdfPTable(9);
            table5.setWidths(new int[]{8, 12, 10, 5, 5, 5, 5, 5, 7});
            table5.setWidthPercentage(100);

            PdfPCell datacellB;

            datacellB = new PdfPCell(new Phrase("PART-B ( G-I-A Establishment)", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setColspan(9);
            table5.addCell(datacellB);

            datacellB = new PdfPCell(new Phrase("Category of employee ", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setRowspan(2);
            table5.addCell(datacellB);

            datacellB = new PdfPCell(new Phrase("Despription of the Posts ", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setRowspan(2);
            table5.addCell(datacellB);

            datacellB = new PdfPCell(new Phrase("Pay Scale", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setColspan(2);
            table5.addCell(datacellB);

            datacellB = new PdfPCell(new Phrase("As per 7th Pay ", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setRowspan(2);
            table5.addCell(datacellB);

            datacellB = new PdfPCell(new Phrase("Sanctioned  Strength ", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacell.setRowspan(2);
            table5.addCell(datacellB);

            datacellB = new PdfPCell(new Phrase("Persons-in-Position", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setRowspan(2);
            table5.addCell(datacellB);

            datacellB = new PdfPCell(new Phrase("Vacancy Position", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setRowspan(2);
            table5.addCell(datacellB);

            datacellB = new PdfPCell(new Phrase("Remarks if any", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setRowspan(2);
            table5.addCell(datacellB);
            datacellB = new PdfPCell(new Phrase("", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setColspan(1);
            table5.addCell(datacellB);
            datacellB = new PdfPCell(new Phrase("As per 6th Pay Commission", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setColspan(1);
            table5.addCell(datacellB);

            datacellB = new PdfPCell(new Phrase("GP", hdrTextFont1));
            datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellB.setColspan(1);
            table5.addCell(datacellB);

            String[] anGIA = {"A", "B", "C", "D", "R"};

            for (int k = 0; k < anGIA.length; k++) {
                String ans = anGIA[k];
                String groupType = "GROUP " + ans;
                if (ans.equals("R")) {
                    groupType = "Consolidated Renumeration";
                }
                datacellB = new PdfPCell(new Phrase(groupType, hdrTextFont1));
                datacellB.setHorizontalAlignment(Element.ALIGN_LEFT);
                datacellB.setColspan(9);
                table5.addCell(datacellB);

                totSanctionedStrength = 0;
                totMenInPosition = 0;
                totVacancy = 0;

                pst = con.prepareStatement("SELECT remarks,off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength,"
                        + " menin_position,vacancy_position,gp,post_group FROM annual_establish_report_at_ao"
                        + " WHERE ao_aer_id=? and post_group=? and aer_part_type=? and (cadre_code='' or cadre_code is null)"
                        + " order by post_group, pay_scale_7th_pay desc,pay_scale_6th_pay desc, postname ");
                pst.setInt(1, Integer.parseInt(aerId));
                pst.setString(2, ans);
                pst.setString(3, "B");
                rs = pst.executeQuery();
                while (rs.next()) {

                    datacellB = new PdfPCell(new Phrase(" ", hdrTextFont1));
                    datacellB.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table5.addCell(datacellB);

                    datacellB = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                    datacellB.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table5.addCell(datacellB);

                    datacellB = new PdfPCell(new Phrase(rs.getString("pay_scale_6th_pay"), dataValFont));
                    datacellB.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table5.addCell(datacellB);

                    datacellB = new PdfPCell(new Phrase(rs.getString("gp"), dataValFont));
                    datacellB.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table5.addCell(datacellB);

                    datacellB = new PdfPCell(new Phrase(rs.getString("pay_scale_7th_pay"), dataValFont));
                    datacellB.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table5.addCell(datacellB);

                    datacellB = new PdfPCell(new Phrase(rs.getString("sanction_strength"), dataValFont));
                    datacellB.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table5.addCell(datacellB);

                    datacellB = new PdfPCell(new Phrase(rs.getString("menin_position"), dataValFont));
                    datacellB.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table5.addCell(datacellB);

                    datacellB = new PdfPCell(new Phrase(rs.getString("vacancy_position"), dataValFont));
                    datacellB.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table5.addCell(datacellB);

                    datacellB = new PdfPCell(new Phrase(rs.getString("remarks"), dataValFont));
                    datacellB.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table5.addCell(datacellB);

                    totSanctionedStrength = totSanctionedStrength + rs.getInt("sanction_strength");
                    totMenInPosition = totMenInPosition + rs.getInt("menin_position");
                    totVacancy = totVacancy + rs.getInt("vacancy_position");

                }

                /*Total*/
                datacell = new PdfPCell(new Phrase("TOTAL", hdrTextFont1));
                datacell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table5.addCell(datacell);

                datacell = new PdfPCell();
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table5.addCell(datacell);

                datacell = new PdfPCell();
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table5.addCell(datacell);

                datacell = new PdfPCell();
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table5.addCell(datacell);

                datacell = new PdfPCell();
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table5.addCell(datacell);

                datacell = new PdfPCell(new Phrase(totSanctionedStrength + "", dataValFont));
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table5.addCell(datacell);

                datacell = new PdfPCell(new Phrase(totMenInPosition + "", dataValFont));
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table5.addCell(datacell);

                datacell = new PdfPCell(new Phrase(totVacancy + "", dataValFont));
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table5.addCell(datacell);

                datacell = new PdfPCell();
                datacell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table5.addCell(datacell);
                /*Total*/

            }

            document.add(table5);

            /**
             * ******************************************************** Form-C
             * *****************************
             */
            PdfPTable table2 = new PdfPTable(5);
            table2.setWidths(new int[]{10, 15, 10, 10, 10});
            table2.setWidthPercentage(100);

            PdfPCell datacellC;

            datacellC = new PdfPCell(new Phrase("PART-C ( Non-Regular Establishment)", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            datacellC.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellC.setColspan(5);
            table2.addCell(datacellC);

            datacellC = new PdfPCell(new Phrase("Category", hdrTextFont1));
            datacellC.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(datacellC);

            datacellC = new PdfPCell(new Phrase("Description of the Posts", hdrTextFont1));
            datacellC.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(datacellC);

            datacellC = new PdfPCell(new Phrase("As per 7th Pay", hdrTextFont1));
            datacellC.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(datacellC);

            datacellC = new PdfPCell(new Phrase("Persons-in-Position", hdrTextFont1));
            datacellC.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(datacellC);

            datacellC = new PdfPCell(new Phrase("Remarks if any", hdrTextFont1));
            datacellC.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(datacellC);

            datacellC = new PdfPCell(new Phrase("Contractual in lieu of regular post (As per GA & PG Department Resolution)/ Other Contractual Junior Engineer ", dataValFont));
            datacellC.setHorizontalAlignment(Element.ALIGN_LEFT);
            datacellC.setColspan(5);
            table2.addCell(datacellC);

            pst = con.prepareStatement("SELECT off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength,menin_position,"
                    + " vacancy_position,gp,post_group FROM annual_establish_report_at_ao"
                    + " WHERE ao_aer_id=? and aer_part_type='PART-C' order by post_group, pay_scale_7th_pay desc,pay_scale_6th_pay desc, postname");
            pst.setInt(1, Integer.parseInt(aerId));
            rs = pst.executeQuery();
            while (rs.next()) {
                datacellC = new PdfPCell(new Phrase(" ", hdrTextFont1));
                datacellC.setHorizontalAlignment(Element.ALIGN_CENTER);
                table2.addCell(datacellC);

                datacellC = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                datacellC.setHorizontalAlignment(Element.ALIGN_LEFT);
                table2.addCell(datacellC);

                datacellC = new PdfPCell(new Phrase(rs.getString("pay_scale_7th_pay"), dataValFont));
                datacellC.setHorizontalAlignment(Element.ALIGN_LEFT);
                table2.addCell(datacellC);

                datacellC = new PdfPCell(new Phrase(rs.getString("menin_position"), dataValFont));
                datacellC.setHorizontalAlignment(Element.ALIGN_LEFT);
                table2.addCell(datacellC);

                datacellC = new PdfPCell(new Phrase("", dataValFont));
                datacellC.setHorizontalAlignment(Element.ALIGN_LEFT);
                table2.addCell(datacellC);
            }
            document.add(table2);

            /**
             * ***************************************** Part D
             * *******************************
             */
            PdfPTable table3 = new PdfPTable(4);
            table3.setWidths(new int[]{20, 15, 10, 10});
            table3.setWidthPercentage(100);

            PdfPCell datacellD;

            datacellD = new PdfPCell(new Phrase("PART-D ( Other Establishment)", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            datacellD.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellD.setColspan(4);
            table3.addCell(datacellD);

            datacellD = new PdfPCell(new Phrase("Category", hdrTextFont1));
            datacellD.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(datacellD);

            datacellD = new PdfPCell(new Phrase("Number", hdrTextFont1));
            datacellD.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(datacellD);

            datacellD = new PdfPCell(new Phrase("Group", hdrTextFont1));
            datacellD.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(datacellD);

            datacellD = new PdfPCell(new Phrase("Remarks if any", hdrTextFont1));
            datacellD.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(datacellD);
            pst = con.prepareStatement("SELECT remarks,off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength,"
                    + " menin_position,vacancy_position,gp,post_group FROM annual_establish_report_at_ao"
                    + " WHERE ao_aer_id=? and aer_part_type=? order by postname");

            pst.setInt(1, Integer.parseInt(aerId));
            pst.setString(2, "D");
            rs = pst.executeQuery();
            while (rs.next()) {
                datacellD = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                datacellD.setHorizontalAlignment(Element.ALIGN_LEFT);
                table3.addCell(datacellD);

                datacellD = new PdfPCell(new Phrase(rs.getString("sanction_strength"), dataValFont));
                datacellD.setHorizontalAlignment(Element.ALIGN_LEFT);
                table3.addCell(datacellD);

                datacellD = new PdfPCell(new Phrase(rs.getString("post_group"), dataValFont));
                datacellD.setHorizontalAlignment(Element.ALIGN_LEFT);
                table3.addCell(datacellD);

                datacellD = new PdfPCell(new Phrase(rs.getString("remarks"), dataValFont));
                datacellD.setHorizontalAlignment(Element.ALIGN_LEFT);
                table3.addCell(datacellD);

            }
            document.add(table3);

            /**
             * **************************** FORM E
             * *************************************
             */
            PdfPTable table4 = new PdfPTable(3);
            table4.setWidths(new int[]{20, 15, 10});
            table4.setWidthPercentage(100);

            PdfPCell datacellE;

            datacellE = new PdfPCell(new Phrase("PART-E ( Outsourced / on Contract)", new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD)));
            datacellE.setHorizontalAlignment(Element.ALIGN_CENTER);
            datacellE.setColspan(3);
            table4.addCell(datacellE);

            datacellE = new PdfPCell(new Phrase("Category", hdrTextFont1));
            datacellE.setHorizontalAlignment(Element.ALIGN_CENTER);
            table4.addCell(datacellE);

            datacellE = new PdfPCell(new Phrase("Number", hdrTextFont1));
            datacellE.setHorizontalAlignment(Element.ALIGN_CENTER);
            table4.addCell(datacellE);

            datacellE = new PdfPCell(new Phrase("Remarks if any", hdrTextFont1));
            datacellE.setHorizontalAlignment(Element.ALIGN_CENTER);
            table4.addCell(datacellE);

            pst = con.prepareStatement("SELECT remarks,off_code,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,sanction_strength,"
                    + " menin_position,vacancy_position,gp,post_group FROM annual_establish_report_at_ao"
                    + " WHERE ao_aer_id=? and aer_part_type=? order by postname");
            pst.setInt(1, Integer.parseInt(aerId));
            pst.setString(2, "E");
            rs = pst.executeQuery();
            while (rs.next()) {
                datacellE = new PdfPCell(new Phrase(rs.getString("postname"), dataValFont));
                datacellE.setHorizontalAlignment(Element.ALIGN_LEFT);
                table4.addCell(datacellE);

                datacellE = new PdfPCell(new Phrase(rs.getString("sanction_strength"), dataValFont));
                datacellE.setHorizontalAlignment(Element.ALIGN_LEFT);
                table4.addCell(datacellE);

                datacellE = new PdfPCell(new Phrase(rs.getString("remarks"), dataValFont));
                datacellE.setHorizontalAlignment(Element.ALIGN_LEFT);
                table4.addCell(datacellE);

            }
            document.add(table4);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public String getSubmitStatusForAcceptor(String empid, String spc, String fy, String aooffcode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String status = "N";
        try {
            con = this.dataSource.getConnection();

            String sql = "select * from aer_report_submit_at_co"
                    + " inner join process_authorization on aer_report_submit_at_co.ao_off_code=process_authorization.off_code where"
                    + " role_type='ACCEPTOR' and hrms_id=? and spc=? and ao_off_code=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setString(2, spc);
            pst.setString(3, aooffcode);
            //pst.setString(4, fy);
            rs = pst.executeQuery();
            if (rs.next()) {
                status = "Y";
            }

            /*pst = con.prepareStatement("SELECT co_aer_id FROM aer_report_submit_at_co where ao_off_code='OLSFIN0010000' and acceptor_spc='OLSFIN00100000701060004' and fy='2018-19'");
             pst.setString(1, aooffcode);
             pst.setString(2, spc);
             pst.setString(3, fy);
             rs = pst.executeQuery();
             if (rs.next()) {
             status = "Y";
             }*/
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return status;
    }

    @Override
    public int submitAerReportAsAcceptor(String empId, String spc, String aoOffCode, String fy) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;

        int aoAerId = 0;

        String startTime = "";
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        startTime = dateFormat.format(cal.getTime());
        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("insert into aer_report_submit_at_ao (ao_off_code, fy) values(?,?) ", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, aoOffCode);
            pst.setString(2, fy);
            pst.execute();
            rs = pst.getGeneratedKeys();
            if (rs.next()) {
                aoAerId = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return aoAerId;
    }

    @Override
    public int getSchedule3AerId(String loginSpc, String empId, String fy) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        String offCode = "";
        int aoaerid = 0;
        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("select off_code from process_authorization where ROLE_TYPE in ('DREVIEWER','DVERIFIER','ACCEPTOR') and hrms_id=? and spc=? order by off_code");
            pst.setString(1, empId);
            pst.setString(2, loginSpc);
            rs = pst.executeQuery();
            if (rs.next()) {
                offCode = rs.getString("off_code");
            }

            String sql = "select ao_aer_id from aer_report_submit_at_ao where fy=? and ao_off_code=? ";
            pst = con.prepareStatement(sql);

            pst.setString(1, fy);
            pst.setString(2, offCode);
            rs = pst.executeQuery();
            if (rs.next()) {
                aoaerid = rs.getInt("ao_aer_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return aoaerid;
    }

    @Override
    public void updateAerAOAerId(String empId, String spc, String aooffCode, String fy, int aoAerId) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("UPDATE aer_report_submit_at_co SET ao_aer_id =? WHERE ao_off_code=? AND acceptor_emp_id=? AND acceptor_spc=? AND fy=?");
            pst.setInt(1, aoAerId);
            pst.setString(2, aooffCode);
            pst.setString(3, empId);
            pst.setString(4, spc);
            pst.setString(5, fy);
            pst.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public ArrayList DeptWiseGroupAERStatus() {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement cntStatement = null;
        ResultSet rs1, rs2, rs3 = null;
        ArrayList outList = new ArrayList();
        String stm, stm1, stm2 = null;
        String fyYear = "2022-23";
        try {
            con = repodataSource.getConnection();
            String stmt1 = "SELECT count(DISTINCT aer_report_submit.off_code) as NO_AER_SUBMITTED FROM aer_report_submit,g_office WHERE aer_report_submit.off_code=g_office.off_code AND  department_code = ? AND is_approver_submitted='Y' AND fy=? AND g_office.is_ddo='Y' ";
            cntStatement = con.prepareStatement(stmt1);
            stm = ("SELECT count(g_office.*) as  totalDdo ,department_name,g_department.department_code,demand_no FROM g_department INNER JOIN g_office "
                    + " ON g_department.department_code=g_office.department_code WHERE 	g_department.if_active='Y' AND 	g_office.is_ddo='Y' GROUP BY  g_department.department_code ORDER BY demand_no");
            ps = con.prepareStatement(stm);
            rs1 = ps.executeQuery();
            while (rs1.next()) {
                int GroupA = 0;
                int GroupB = 0;
                int GroupC = 0;
                int GroupD = 0;

                int grantidaid = 0;
                int TotalGroup = 0;

                AnnualEstablishment aer = new AnnualEstablishment();
                aer.setDepartmentname(rs1.getString("department_name"));
                aer.setTotalDDO(rs1.getInt("totalDdo"));
                aer.setDemandNo(rs1.getInt("demand_no"));
                String deptCode = rs1.getString("department_code");
                aer.setDeptCode(deptCode);

                cntStatement.setString(1, deptCode);
                cntStatement.setString(2, fyYear);
                rs2 = cntStatement.executeQuery();
                if (rs2.next()) {
                    aer.setApproverSubmitted(rs2.getInt("NO_AER_SUBMITTED"));
                }
                stm1 = "select post_group, SUM(menin_position) as cnt from aer_report_submit ARS "
                        + " INNER JOIN annual_establish_report AER ON ARS.aer_id = AER.aer_id INNER JOIN g_office GO ON GO.off_code = ARS.off_code"
                        + " WHERE ARS.is_approver_submitted='Y' AND GO.department_code = ? AND ARS.fy=? and post_group is not null AND aer_part_type = 'PART-A' GROUP BY post_group";
                ps1 = con.prepareStatement(stm1);
                ps1.setString(1, deptCode);
                ps1.setString(2, fyYear);

                rs2 = ps1.executeQuery();
                while (rs2.next()) {
                    if (rs2.getString("post_group").equals("A")) {
                        GroupA = rs2.getInt("cnt");
                    }
                    if (rs2.getString("post_group").equals("B")) {
                        GroupB = rs2.getInt("cnt");
                    }
                    if (rs2.getString("post_group").equals("C")) {
                        GroupC = rs2.getInt("cnt");
                    }
                    if (rs2.getString("post_group").equals("D")) {
                        GroupD = rs2.getInt("cnt");
                    }

                    aer.setTotalACnt(GroupA);
                    aer.setTotalBCnt(GroupB);
                    aer.setTotalCCnt(GroupC);
                    aer.setTotalDCnt(GroupD);

                    TotalGroup = GroupA + GroupB + GroupC + GroupD;
                    aer.setTotalallCnt(TotalGroup);

                }

                stm2 = "select sum(sanc_strength)-sum(vacancy) as cnt from aer_report_submit ARS"
                        + " INNER JOIN aer_other_establishment AER ON ARS.aer_id = AER.aer_id "
                        + " INNER JOIN g_office GO ON GO.off_code = ARS.off_code WHERE "
                        + "  ARS.is_approver_submitted='Y'  AND GO.department_code = ? and "
                        + " group_name is not null and part_type = 'B' AND fy = ?";
                ps2 = con.prepareStatement(stm2);
                ps2.setString(1, deptCode);
                ps2.setString(2, fyYear);
                rs3 = ps2.executeQuery();
                while (rs3.next()) {
                    grantidaid = rs3.getInt("cnt");

                }

                aer.setGrantinAid(grantidaid);
                int grandTotal = grantidaid + TotalGroup;
                aer.setGrandTotal(grandTotal);
                outList.add(aer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps, ps1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outList;
    }

    @Override
    public String checkAODReviewer(String offcode, String spc) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String isDReviewer = "N";
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT hrms_id FROM process_authorization WHERE spc=? AND process_id='13' AND role_type='DREVIEWER'";
            pst = con.prepareStatement(sql);
            pst.setString(1, spc);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("hrms_id") != null && !rs.getString("hrms_id").equals("")) {
                    isDReviewer = "Y";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isDReviewer;
    }

    @Override
    public String checkAODVerifier(String offcode, String spc) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String isDVerifier = "N";
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT hrms_id FROM process_authorization WHERE spc=? AND process_id='13' AND role_type='DVERIFIER'";
            pst = con.prepareStatement(sql);
            pst.setString(1, spc);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("hrms_id") != null && !rs.getString("hrms_id").equals("")) {
                    isDVerifier = "Y";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isDVerifier;
    }

    @Override
    public String checkAOAcceptor(String offcode, String spc) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String isAcceptor = "N";
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT hrms_id FROM process_authorization WHERE spc=? AND process_id='13' AND role_type='ACCEPTOR'";
            pst = con.prepareStatement(sql);
            pst.setString(1, spc);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("hrms_id") != null && !rs.getString("hrms_id").equals("")) {
                    isAcceptor = "Y";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return isAcceptor;
    }

    @Override
    public ArrayList getAERFlowList(int aerid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        AERFlowList aflist = null;

        ArrayList flowlist = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            String sql = "select * from aer_comm_log where aer_id=? order by action_date desc";
            pst = con.prepareStatement(sql);
            pst.setInt(1, aerid);
            rs = pst.executeQuery();
            while (rs.next()) {
                aflist = new AERFlowList();

                Timestamp originalDate = rs.getTimestamp("action_date");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");

                String modifiedDate = simpleDateFormat.format(originalDate);

                String actiondate = modifiedDate;
                String usertype = rs.getString("user_type");
                String message = rs.getString("message_if_any");
                aflist.setActionDate(actiondate);
                aflist.setActionBy(usertype);
                aflist.setMessage(message);
                flowlist.add(aflist);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return flowlist;
    }

    @Override
    public int updatePartAPostInformationAtReviewerLevel(int aerid, String gpc, String scaleOfPay6th, String postgroup, String hidPostgroup, String paylevel, String gp) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int retVal = 0;

        String aerOffCode = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "UPDATE annual_establish_report SET pay_scale_6th_pay=?,post_group=?,pay_scale_7th_pay=?,gp=? WHERE aer_id=? AND gpc=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, scaleOfPay6th);
            pst.setString(2, postgroup);
            pst.setString(3, paylevel);
            if (gp != null && !gp.equals("")) {
                pst.setInt(4, Integer.parseInt(gp));
            } else {
                pst.setInt(4, 0);
            }
            pst.setInt(5, aerid);
            pst.setString(6, gpc);
            retVal = pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            sql = "select off_code from aer_report_submit where aer_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, aerid);
            rs = pst.executeQuery();
            if (rs.next()) {
                aerOffCode = rs.getString("off_code");
            }
            if (aerOffCode != null && !aerOffCode.equals("")) {
                sql = "UPDATE g_spc SET pay_scale=?,post_grp=?,level_7thpay=?,gp=? WHERE off_code=? AND gpc=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, scaleOfPay6th);
                pst.setString(2, postgroup);
                pst.setInt(3, Integer.parseInt(paylevel));
                if (gp != null && !gp.equals("")) {
                    pst.setInt(4, Integer.parseInt(gp));
                } else {
                    pst.setInt(4, 0);
                }
                pst.setString(5, aerOffCode);
                pst.setString(6, gpc);
                retVal = pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return retVal;
    }

    @Override
    public ArrayList DeptWiseGroupAERSan() {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement cntStatement = null;
        ResultSet rs1, rs2, rs3 = null;
        ArrayList outList = new ArrayList();
        String stm, stm1, stm2 = null;
        String fyYear = "2022-23";
        try {
            con = repodataSource.getConnection();
            String stmt1 = "SELECT count(DISTINCT aer_report_submit.off_code) as NO_AER_SUBMITTED FROM aer_report_submit,g_office WHERE aer_report_submit.off_code=g_office.off_code AND  department_code = ? AND is_approver_submitted='Y' AND fy=? AND g_office.is_ddo='Y' ";
            cntStatement = con.prepareStatement(stmt1);
            stm = ("SELECT count(g_office.*) as  totalDdo ,department_name,g_department.department_code,demand_no FROM g_department INNER JOIN g_office "
                    + " ON g_department.department_code=g_office.department_code WHERE 	g_department.if_active='Y' AND 	g_office.is_ddo='Y' GROUP BY  g_department.department_code ORDER BY demand_no");
            ps = con.prepareStatement(stm);
            rs1 = ps.executeQuery();
            while (rs1.next()) {
                int GroupA = 0;
                int GroupB = 0;
                int GroupC = 0;
                int GroupD = 0;

                int grantidaid = 0;
                int TotalGroup = 0;

                AnnualEstablishment aer = new AnnualEstablishment();
                aer.setDepartmentname(rs1.getString("department_name"));
                aer.setDemandNo(rs1.getInt("demand_no"));
                aer.setTotalDDO(rs1.getInt("totalDdo"));
                String deptCode = rs1.getString("department_code");
                aer.setDeptCode(deptCode);

                cntStatement.setString(1, deptCode);
                cntStatement.setString(2, fyYear);
                rs2 = cntStatement.executeQuery();
                if (rs2.next()) {
                    aer.setApproverSubmitted(rs2.getInt("NO_AER_SUBMITTED"));
                }
                stm1 = "select post_group, SUM(sanction_strength) as cnt from aer_report_submit ARS "
                        + " INNER JOIN annual_establish_report AER ON ARS.aer_id = AER.aer_id INNER JOIN g_office GO ON GO.off_code = ARS.off_code"
                        + " WHERE ARS.is_approver_submitted='Y' AND GO.department_code = ? AND ARS.fy=? and post_group is not null AND aer_part_type = 'PART-A' GROUP BY post_group";
                ps1 = con.prepareStatement(stm1);
                ps1.setString(1, deptCode);
                ps1.setString(2, fyYear);

                rs2 = ps1.executeQuery();
                while (rs2.next()) {
                    if (rs2.getString("post_group").equals("A")) {
                        GroupA = rs2.getInt("cnt");
                    }
                    if (rs2.getString("post_group").equals("B")) {
                        GroupB = rs2.getInt("cnt");
                    }
                    if (rs2.getString("post_group").equals("C")) {
                        GroupC = rs2.getInt("cnt");
                    }
                    if (rs2.getString("post_group").equals("D")) {
                        GroupD = rs2.getInt("cnt");
                    }

                    aer.setTotalACnt(GroupA);
                    aer.setTotalBCnt(GroupB);
                    aer.setTotalCCnt(GroupC);
                    aer.setTotalDCnt(GroupD);

                    TotalGroup = GroupA + GroupB + GroupC + GroupD;
                    aer.setTotalallCnt(TotalGroup);

                }

                stm2 = "select sum(sanc_strength) as cnt from aer_report_submit ARS"
                        + " INNER JOIN aer_other_establishment AER ON ARS.aer_id = AER.aer_id "
                        + " INNER JOIN g_office GO ON GO.off_code = ARS.off_code WHERE "
                        + "  ARS.is_approver_submitted='Y'  AND GO.department_code = ? and "
                        + " group_name is not null and part_type = 'B' AND fy = ?";
                ps2 = con.prepareStatement(stm2);
                ps2.setString(1, deptCode);
                ps2.setString(2, fyYear);
                rs3 = ps2.executeQuery();
                while (rs3.next()) {
                    grantidaid = rs3.getInt("cnt");

                }

                aer.setGrantinAid(grantidaid);
                int grandTotal = grantidaid + TotalGroup;
                aer.setGrandTotal(grandTotal);
                outList.add(aer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps, ps1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outList;
    }

    @Override
    public ArrayList COWiseDeptAERStatus(String fy) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        ResultSet rs1, rs2, rs3 = null;
        ArrayList outList = new ArrayList();

        String stm = null;
        String stm1 = null;
        String sql2 = null;
        String fyYear = fy;
        try {
            con = repodataSource.getConnection();
            sql2 = "SELECT co_code,off_code,off_en,(SELECT count(*) FROM aer_report_submit WHERE co_off_code=g_office.off_code AND fy=? AND g_office.department_code=?) as cnt FROM g_office WHERE (lvl=? OR lvl=?) AND 	g_office.is_ddo='Y'  AND g_office.department_code=?  order by off_en ";

            stm = ("SELECT count(g_office.*) as  totalDdo ,department_name,g_department.department_code,g_department.demand_no,( select off_en from g_office inner join \n"
                    + "(SELECT ao_off_code FROM aer_report_submit_at_ao WHERE  fy=? GROUP BY ao_off_code)temp\n"
                    + "on g_office.off_code=temp.ao_off_code WHERE department_code=g_department.department_code ) is_dept_submitted FROM g_department INNER JOIN g_office "
                    + " ON g_department.department_code=g_office.department_code WHERE 	g_department.if_active='Y' AND 	g_office.is_ddo='Y'  AND (g_office.lvl='02' OR  g_office.lvl='01') GROUP BY  g_department.department_code ORDER BY department_name");
            ps = con.prepareStatement(stm);
            ps.setString(1, fyYear);
            rs1 = ps.executeQuery();
            while (rs1.next()) {
                AnnualEstablishment aer = new AnnualEstablishment();
                aer.setDepartmentname(rs1.getString("department_name"));
                aer.setTotalDDO(rs1.getInt("totalDdo"));
                aer.setStatus(rs1.getString("is_dept_submitted"));
                String deptCode = rs1.getString("department_code");

                ArrayList inList = new ArrayList();

                ps1 = con.prepareStatement(sql2);
                ps1.setString(1, fyYear);
                ps1.setString(2, deptCode);
                ps1.setString(3, "01");
                ps1.setString(4, "02");
                ps1.setString(5, deptCode);
                rs2 = ps1.executeQuery();
                int totalCosubmitted = 0;
                while (rs2.next()) {
                    AnnualEstablishment aer2 = new AnnualEstablishment();
                    aer2.setOffCode(rs2.getString("off_code"));
                    aer2.setCoCode(rs2.getString("co_code"));
                    aer2.setOffName(rs2.getString("off_en"));
                    aer2.setTotalDDO(rs2.getInt("cnt"));
                    String offCode = rs2.getString("off_code");

                    stm1 = "SELECT count(*) as coStatus FROM aer_report_submit_at_co WHERE co_off_code=? AND fy=? AND  ao_off_code is NOT NULL  ";
                    ps2 = con.prepareStatement(stm1);
                    ps2.setString(1, offCode);
                    ps2.setString(2, fyYear);
                    rs3 = ps2.executeQuery();
                    String Status = "N";

                    while (rs3.next()) {
                        if (rs3.getInt("coStatus") > 0) {
                            Status = "Y";
                            totalCosubmitted = totalCosubmitted + 1;
                        }

                    }
                    aer2.setCoStatus(Status);

                    inList.add(aer2);
                }
                aer.setAerWiseDDO(inList);
                aer.setTotal(totalCosubmitted);
                aer.setDeptCode(deptCode);
                outList.add(aer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps, ps1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outList;
    }

    @Override
    public void insertConsolidatedDataAfterAcceptorSubmitForBDE(int aoAerId, String fy, String spc, String aooffCode, String postgrp, String partTYpe) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "insert into annual_establish_report_at_ao(ao_aer_id, off_code, sanction_strength,menin_position,vacancy_position,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,gp,aer_part_type)"
                    + " select  " + aoAerId + ",'" + aooffCode + "',sum(sanction_strength) sanc_strength,sum(menin_position) men_position,sum(vacancy_position) vacancy,annual_establish_report_at_co.postname,annual_establish_report_at_co.post_group,"
                    + " annual_establish_report_at_co.pay_scale_7th_pay,annual_establish_report_at_co.pay_scale_6th_pay,"
                    + " annual_establish_report_at_co.gp,aer_part_type from aer_report_submit_at_co aer"
                    + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code"
                    + " inner join annual_establish_report_at_co on aer.co_aer_id=annual_establish_report_at_co.co_aer_id"
                    + " where FY=?"
                    + " AND process_authorization.SPC=?"
                    + " and aer_part_type=?"
                    + " and process_authorization.process_id=13 and process_authorization.role_type='ACCEPTOR'"
                    + " group by"
                    + " annual_establish_report_at_co.postname,annual_establish_report_at_co.post_group,annual_establish_report_at_co.pay_scale_7th_pay,annual_establish_report_at_co.pay_scale_6th_pay,"
                    + " annual_establish_report_at_co.gp,aer_part_type";
            pst = con.prepareStatement(sql);
            pst.setString(1, fy);
            pst.setString(2, spc);
            pst.setString(3, partTYpe);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList DeptWiseGroupAERSanCoWise(String fyYear) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement cntStatement = null;
        ResultSet rs1, rs2, rs3 = null;
        ArrayList outList = new ArrayList();
        String stm, stm1, stm2 = null;
        //   String fyYear = "2019-20";
        try {
            con = repodataSource.getConnection();
            String stmt1 = "SELECT count(DISTINCT aer_report_submit.off_code) as NO_AER_SUBMITTED FROM aer_report_submit,g_office WHERE aer_report_submit.co_off_code=g_office.off_code AND  department_code = ? AND is_approver_submitted='Y' AND fy=? AND (co_aer_id=0 OR co_aer_id::text='' or co_aer_id::text is null)";
            cntStatement = con.prepareStatement(stmt1);
            stm = ("SELECT count(g_office.*) as  totalDdo ,department_name,g_department.department_code,demand_no FROM g_department INNER JOIN g_office "
                    + " ON g_department.department_code=g_office.department_code INNER  JOIN aer_report_submit_at_ao ON g_office.off_code=aer_report_submit_at_ao.ao_off_code  AND fy=? GROUP BY  g_department.department_code ORDER BY demand_no");
            ps = con.prepareStatement(stm);
            ps.setString(1, fyYear);
            rs1 = ps.executeQuery();
            while (rs1.next()) {
                int GroupA = 0;
                int GroupB = 0;
                int GroupC = 0;
                int GroupD = 0;

                int grantidaid = 0;
                int TotalGroup = 0;

                AnnualEstablishment aer = new AnnualEstablishment();
                aer.setDepartmentname(rs1.getString("department_name"));
                aer.setDemandNo(rs1.getInt("demand_no"));
                aer.setTotalDDO(rs1.getInt("totalDdo"));
                String deptCode = rs1.getString("department_code");
                aer.setDeptCode(deptCode);

                cntStatement.setString(1, deptCode);
                cntStatement.setString(2, fyYear);
                rs2 = cntStatement.executeQuery();
                if (rs2.next()) {
                    aer.setApproverSubmitted(rs2.getInt("NO_AER_SUBMITTED"));
                }
                stm1 = "select post_group, SUM(sanction_strength) as cnt from aer_report_submit_at_co  "
                        + " INNER JOIN annual_establish_report_at_co AER ON aer_report_submit_at_co.co_aer_id = AER.co_aer_id INNER JOIN g_office GO ON aer_report_submit_at_co.co_off_code=GO.off_code"
                        + " WHERE  GO.department_code = ? AND fy=? and post_group is not null AND aer_part_type = 'PART-A'  GROUP BY post_group";
                ps1 = con.prepareStatement(stm1);
                ps1.setString(1, deptCode);
                ps1.setString(2, fyYear);

                rs2 = ps1.executeQuery();
                while (rs2.next()) {
                    if (rs2.getString("post_group").equals("A")) {
                        GroupA = rs2.getInt("cnt");
                    }
                    if (rs2.getString("post_group").equals("B")) {
                        GroupB = rs2.getInt("cnt");
                    }
                    if (rs2.getString("post_group").equals("C")) {
                        GroupC = rs2.getInt("cnt");
                    }
                    if (rs2.getString("post_group").equals("D")) {
                        GroupD = rs2.getInt("cnt");
                    }

                    aer.setTotalACnt(GroupA);
                    aer.setTotalBCnt(GroupB);
                    aer.setTotalCCnt(GroupC);
                    aer.setTotalDCnt(GroupD);
                    aer.setFinancialYear(fyYear);

                    TotalGroup = GroupA + GroupB + GroupC + GroupD;
                    aer.setTotalallCnt(TotalGroup);

                }

                stm2 = "select sum(sanction_strength) as cnt from aer_report_submit_at_co ARS"
                        + " INNER JOIN annual_establish_report_at_co AER ON ARS.co_aer_id = AER.co_aer_id "
                        + " INNER JOIN g_office GO ON GO.off_code = ARS.co_off_code WHERE "
                        + "   GO.department_code = ? and "
                        + " post_group is not null and (aer_part_type = 'PART-B' OR aer_part_type = 'B') AND fy = ?";
                ps2 = con.prepareStatement(stm2);
                ps2.setString(1, deptCode);
                ps2.setString(2, fyYear);
                rs3 = ps2.executeQuery();
                while (rs3.next()) {
                    grantidaid = rs3.getInt("cnt");

                }

                aer.setGrantinAid(grantidaid);
                int grandTotal = grantidaid + TotalGroup;
                aer.setGrandTotal(grandTotal);
                aer.setFinancialYear(fyYear);
                outList.add(aer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps, ps1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outList;
    }

    @Override
    public ArrayList DeptWiseGroupAERMIPCoWise(String fyYear) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement cntStatement = null;
        ResultSet rs1, rs2, rs3 = null;
        ArrayList outList = new ArrayList();
        String stm, stm1, stm2 = null;
        //String fyYear = "2019-20";
        try {
            con = repodataSource.getConnection();
            String stmt1 = "SELECT count(DISTINCT aer_report_submit.off_code) as NO_AER_SUBMITTED FROM aer_report_submit,g_office WHERE aer_report_submit.co_off_code=g_office.off_code AND  department_code = ? AND is_approver_submitted='Y' AND fy=? AND (co_aer_id=0 OR co_aer_id::text='' or co_aer_id::text is null)";
            cntStatement = con.prepareStatement(stmt1);
            stm = ("SELECT count(g_office.*) as  totalDdo ,department_name,g_department.department_code,demand_no FROM g_department INNER JOIN g_office "
                    + " ON g_department.department_code=g_office.department_code INNER  JOIN aer_report_submit_at_ao ON g_office.off_code=aer_report_submit_at_ao.ao_off_code WHERE 	g_department.if_active='Y' AND 	g_office.is_ddo='Y' AND fy=? GROUP BY  g_department.department_code ORDER BY demand_no");
            ps = con.prepareStatement(stm);
            ps.setString(1, fyYear);
            rs1 = ps.executeQuery();
            while (rs1.next()) {
                int GroupA = 0;
                int GroupB = 0;
                int GroupC = 0;
                int GroupD = 0;

                int grantidaid = 0;
                int TotalGroup = 0;

                AnnualEstablishment aer = new AnnualEstablishment();
                aer.setDepartmentname(rs1.getString("department_name"));
                aer.setDemandNo(rs1.getInt("demand_no"));
                aer.setTotalDDO(rs1.getInt("totalDdo"));
                String deptCode = rs1.getString("department_code");
                aer.setDeptCode(deptCode);

                cntStatement.setString(1, deptCode);
                cntStatement.setString(2, fyYear);
                rs2 = cntStatement.executeQuery();
                if (rs2.next()) {
                    aer.setApproverSubmitted(rs2.getInt("NO_AER_SUBMITTED"));
                }
                stm1 = "select post_group, SUM(menin_position) as cnt from aer_report_submit_at_co  "
                        + " INNER JOIN annual_establish_report_at_co AER ON aer_report_submit_at_co.co_aer_id = AER.co_aer_id INNER JOIN g_office GO ON aer_report_submit_at_co.co_off_code=GO.off_code"
                        + " WHERE  GO.department_code = ? AND fy=? and post_group is not null AND aer_part_type = 'PART-A'  GROUP BY post_group";
                ps1 = con.prepareStatement(stm1);
                ps1.setString(1, deptCode);
                ps1.setString(2, fyYear);

                rs2 = ps1.executeQuery();
                while (rs2.next()) {
                    if (rs2.getString("post_group").equals("A")) {
                        GroupA = rs2.getInt("cnt");
                    }
                    if (rs2.getString("post_group").equals("B")) {
                        GroupB = rs2.getInt("cnt");
                    }
                    if (rs2.getString("post_group").equals("C")) {
                        GroupC = rs2.getInt("cnt");
                    }
                    if (rs2.getString("post_group").equals("D")) {
                        GroupD = rs2.getInt("cnt");
                    }

                    aer.setTotalACnt(GroupA);
                    aer.setTotalBCnt(GroupB);
                    aer.setTotalCCnt(GroupC);
                    aer.setTotalDCnt(GroupD);

                    TotalGroup = GroupA + GroupB + GroupC + GroupD;
                    aer.setTotalallCnt(TotalGroup);

                }

                /*stm2 = "select sum(menin_position) as cnt from aer_report_submit_at_co ARS"
                 + " INNER JOIN annual_establish_report_at_co AER ON ARS.co_aer_id = AER.co_aer_id "
                 + " INNER JOIN g_office GO ON GO.off_code = ARS.co_off_code WHERE "
                 + "   GO.department_code = ? and "
                 + " post_group is not null and (aer_part_type = 'PART-B' OR aer_part_type = 'B') AND fy = ?";*/
                stm2 = "select (sum(sanction_strength) - sum(vacancy_position)) as cnt from aer_report_submit_at_co ARS"
                        + " INNER JOIN annual_establish_report_at_co AER ON ARS.co_aer_id = AER.co_aer_id "
                        + " INNER JOIN g_office GO ON GO.off_code = ARS.co_off_code WHERE "
                        + "   GO.department_code = ? and "
                        + " post_group is not null and (aer_part_type = 'PART-B' OR aer_part_type = 'B') AND fy = ?";
                ps2 = con.prepareStatement(stm2);
                ps2.setString(1, deptCode);
                ps2.setString(2, fyYear);
                rs3 = ps2.executeQuery();
                while (rs3.next()) {
                    grantidaid = rs3.getInt("cnt");

                }

                aer.setGrantinAid(grantidaid);
                int grandTotal = grantidaid + TotalGroup;
                aer.setGrandTotal(grandTotal);
                aer.setFinancialYear(fyYear);
                outList.add(aer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps, ps1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outList;
    }

    @Override
    public ArrayList DeptWiseGroupAERVacancyCoWise(String fyYear) {
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement cntStatement = null;
        ResultSet rs1, rs2, rs3 = null;
        ArrayList outList = new ArrayList();
        String stm, stm1, stm2 = null;
        //String fyYear = "2019-20";
        try {
            con = repodataSource.getConnection();
            String stmt1 = "SELECT count(DISTINCT aer_report_submit.off_code) as NO_AER_SUBMITTED FROM aer_report_submit,g_office WHERE aer_report_submit.co_off_code=g_office.off_code AND  department_code = ? AND is_approver_submitted='Y' AND fy=? AND (co_aer_id=0 OR co_aer_id::text='' or co_aer_id::text is null)";
            cntStatement = con.prepareStatement(stmt1);
            stm = ("SELECT count(g_office.*) as  totalDdo ,department_name,g_department.department_code,demand_no FROM g_department INNER JOIN g_office "
                    + " ON g_department.department_code=g_office.department_code INNER  JOIN aer_report_submit_at_ao ON g_office.off_code=aer_report_submit_at_ao.ao_off_code WHERE 	g_department.if_active='Y' AND 	g_office.is_ddo='Y'  AND fy=?  GROUP BY  g_department.department_code ORDER BY demand_no");
            ps = con.prepareStatement(stm);
            ps.setString(1, fyYear);
            rs1 = ps.executeQuery();
            while (rs1.next()) {
                int GroupA = 0;
                int GroupB = 0;
                int GroupC = 0;
                int GroupD = 0;

                int grantidaid = 0;
                int TotalGroup = 0;

                AnnualEstablishment aer = new AnnualEstablishment();
                aer.setDepartmentname(rs1.getString("department_name"));
                aer.setDemandNo(rs1.getInt("demand_no"));
                aer.setTotalDDO(rs1.getInt("totalDdo"));
                String deptCode = rs1.getString("department_code");
                aer.setDeptCode(deptCode);

                cntStatement.setString(1, deptCode);
                cntStatement.setString(2, fyYear);
                rs2 = cntStatement.executeQuery();
                if (rs2.next()) {
                    aer.setApproverSubmitted(rs2.getInt("NO_AER_SUBMITTED"));
                }
                stm1 = "select post_group, SUM(sanction_strength) as cnt,SUM(menin_position) as cnt1 from aer_report_submit_at_co  "
                        + " INNER JOIN annual_establish_report_at_co AER ON aer_report_submit_at_co.co_aer_id = AER.co_aer_id INNER JOIN g_office GO ON aer_report_submit_at_co.co_off_code=GO.off_code"
                        + " WHERE  GO.department_code = ? AND fy=? and post_group is not null AND aer_part_type = 'PART-A'  GROUP BY post_group";
                ps1 = con.prepareStatement(stm1);
                ps1.setString(1, deptCode);
                ps1.setString(2, fyYear);

                rs2 = ps1.executeQuery();
                while (rs2.next()) {
                    if (rs2.getString("post_group").equals("A")) {
                        GroupA = rs2.getInt("cnt") - rs2.getInt("cnt1");
                    }
                    if (rs2.getString("post_group").equals("B")) {
                        GroupB = rs2.getInt("cnt") - rs2.getInt("cnt1");
                    }
                    if (rs2.getString("post_group").equals("C")) {
                        GroupC = rs2.getInt("cnt") - rs2.getInt("cnt1");
                    }
                    if (rs2.getString("post_group").equals("D")) {
                        GroupD = rs2.getInt("cnt") - rs2.getInt("cnt1");
                    }

                    aer.setTotalACnt(GroupA);
                    aer.setTotalBCnt(GroupB);
                    aer.setTotalCCnt(GroupC);
                    aer.setTotalDCnt(GroupD);

                    TotalGroup = GroupA + GroupB + GroupC + GroupD;
                    aer.setTotalallCnt(TotalGroup);

                }

                stm2 = "select sum(sanction_strength) as cnt,SUM(menin_position) as cnt1,SUM(vacancy_position) as cnt2 from aer_report_submit_at_co ARS"
                        + " INNER JOIN annual_establish_report_at_co AER ON ARS.co_aer_id = AER.co_aer_id "
                        + " INNER JOIN g_office GO ON GO.off_code = ARS.co_off_code WHERE "
                        + "   GO.department_code = ? and "
                        + " post_group is not null and (aer_part_type = 'PART-B' OR aer_part_type = 'B') AND fy = ?";
                ps2 = con.prepareStatement(stm2);
                ps2.setString(1, deptCode);
                ps2.setString(2, fyYear);
                rs3 = ps2.executeQuery();
                while (rs3.next()) {
                    /*if(fyYear != null && (fyYear.equals("2018-19") || fyYear.equals("2019-20") || fyYear.equals("2020-21"))){
                     grantidaid = rs3.getInt("cnt") - rs3.getInt("cnt1");
                     }else{
                     grantidaid = rs3.getInt("cnt") - rs3.getInt("cnt2");
                     }*/
                    grantidaid = rs3.getInt("cnt2");
                }

                aer.setGrantinAid(grantidaid);
                int grandTotal = grantidaid + TotalGroup;
                aer.setGrandTotal(grandTotal);
                outList.add(aer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps, ps1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outList;
    }

    @Override
    public ArrayList viewCODeptWise(String dcode, String fy) {

        Connection con = null;

        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;

        ResultSet rs1 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;

        ArrayList outList = new ArrayList();

        String stm2, stm3 = null;

        try {
            con = this.repodataSource.getConnection();

            String sql2 = "SELECT off_code,off_en FROM g_office WHERE (lvl=? OR lvl=?) AND g_office.is_ddo='Y' AND g_office.department_code=? order by off_en ";
            ps1 = con.prepareStatement(sql2);
            ps1.setString(1, "01");
            ps1.setString(2, "02");
            ps1.setString(3, dcode);
            rs2 = ps1.executeQuery();
            while (rs2.next()) {
                AnnualEstablishment aer2 = new AnnualEstablishment();
                aer2.setOffCode(rs2.getString("off_code"));
                aer2.setOffName(rs2.getString("off_en"));
                String offCode = rs2.getString("off_code");

                int groupA = 0;
                int groupB = 0;
                int groupC = 0;
                int groupD = 0;

                int partD = 0;
                int partE = 0;

                int grantidaid = 0;
                int totalGroup = 0;

                stm2 = "SELECT co_aer_id FROM aer_report_submit_at_co WHERE co_off_code=? AND fy=? AND ao_off_code is NOT NULL";
                ps2 = con.prepareStatement(stm2);
                ps2.setString(1, offCode);
                ps2.setString(2, fy);
                rs3 = ps2.executeQuery();
                String Status = "N";
                int aerId = 0;
                if (rs3.next()) {
                    if (rs3.getInt("co_aer_id") > 0) {
                        Status = "Y";
                    }
                    aer2.setCoStatus(Status);
                    aerId = rs3.getInt("co_aer_id");
                    aer2.setAerId(aerId);

                    stm3 = "select post_group,SUM(sanction_strength) sancstrength from annual_establish_report_at_co where co_aer_id=? and aer_part_type = 'PART-A' group by post_group";
                    ps3 = con.prepareStatement(stm3);
                    ps3.setInt(1, aerId);
                    rs3 = ps3.executeQuery();
                    while (rs3.next()) {
                        if (rs3.getString("post_group") != null && rs3.getString("post_group").equals("A")) {
                            groupA = rs3.getInt("sancstrength");
                        }
                        if (rs3.getString("post_group") != null && rs3.getString("post_group").equals("B")) {
                            groupB = rs3.getInt("sancstrength");
                        }
                        if (rs3.getString("post_group") != null && rs3.getString("post_group").equals("C")) {
                            groupC = rs3.getInt("sancstrength");
                        }
                        if (rs3.getString("post_group") != null && rs3.getString("post_group").equals("D")) {
                            groupD = rs3.getInt("sancstrength");
                        }

                        aer2.setTotalACnt(groupA);
                        aer2.setTotalBCnt(groupB);
                        aer2.setTotalCCnt(groupC);
                        aer2.setTotalDCnt(groupD);

                        totalGroup = groupA + groupB + groupC + groupD;
                        aer2.setTotalallCnt(totalGroup);
                    }

                    DataBaseFunctions.closeSqlObjects(rs3, ps3);

                    stm3 = "select SUM(sanction_strength) sancstrength from aer_report_submit_at_co ARS"
                            + " INNER JOIN annual_establish_report_at_co AER ON ARS.co_aer_id = AER.co_aer_id "
                            + " WHERE ARS.co_aer_id=? AND post_group is not null and (aer_part_type = 'PART-B' OR aer_part_type = 'B')";
                    ps3 = con.prepareStatement(stm3);
                    ps3.setInt(1, aerId);
                    rs3 = ps3.executeQuery();
                    if (rs3.next()) {
                        grantidaid = rs3.getInt("sancstrength");
                    }
                    aer2.setGrantinAid(grantidaid);

                    DataBaseFunctions.closeSqlObjects(rs3, ps3);

                    stm3 = "select SUM(sanction_strength) sancstrength from aer_report_submit_at_co ARS"
                            + " INNER JOIN annual_establish_report_at_co AER ON ARS.co_aer_id = AER.co_aer_id "
                            + " WHERE ARS.co_aer_id=? AND post_group is not null and (aer_part_type = 'PART-D' OR aer_part_type = 'D')";
                    ps3 = con.prepareStatement(stm3);
                    ps3.setInt(1, aerId);
                    rs3 = ps3.executeQuery();
                    if (rs3.next()) {
                        partD = rs3.getInt("sancstrength");
                    }
                    aer2.setPartD(partD);

                    DataBaseFunctions.closeSqlObjects(rs3, ps3);

                    stm3 = "select SUM(sanction_strength) sancstrength from aer_report_submit_at_co ARS"
                            + " INNER JOIN annual_establish_report_at_co AER ON ARS.co_aer_id = AER.co_aer_id "
                            + " WHERE ARS.co_aer_id=? AND post_group is not null and (aer_part_type = 'PART-E' OR aer_part_type = 'E')";
                    ps3 = con.prepareStatement(stm3);
                    ps3.setInt(1, aerId);
                    rs3 = ps3.executeQuery();
                    if (rs3.next()) {
                        partE = rs3.getInt("sancstrength");
                    }
                    aer2.setPartE(partE);
                }
                outList.add(aer2);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs1, rs2, rs3);
            DataBaseFunctions.closeSqlObjects(ps1, ps2, ps3);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outList;
    }

    @Override
    public ArrayList viewCODeptWiseMIP(String dcode, String fy) {

        Connection con = null;

        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;

        ResultSet rs1 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;

        ArrayList outList = new ArrayList();

        String stm2, stm3 = null;

        try {
            con = this.repodataSource.getConnection();

            String sql2 = "SELECT off_code,off_en FROM g_office WHERE (lvl=? OR lvl=?) AND g_office.is_ddo='Y' AND g_office.department_code=? order by off_en ";
            ps1 = con.prepareStatement(sql2);
            ps1.setString(1, "01");
            ps1.setString(2, "02");
            ps1.setString(3, dcode);
            rs2 = ps1.executeQuery();
            while (rs2.next()) {
                AnnualEstablishment aer2 = new AnnualEstablishment();
                aer2.setOffCode(rs2.getString("off_code"));
                aer2.setOffName(rs2.getString("off_en"));
                String offCode = rs2.getString("off_code");

                int groupA = 0;
                int groupB = 0;
                int groupC = 0;
                int groupD = 0;

                int partD = 0;
                int partE = 0;

                int grantidaid = 0;
                int totalGroup = 0;

                stm2 = "SELECT co_aer_id FROM aer_report_submit_at_co WHERE co_off_code=? AND fy=? AND ao_off_code is NOT NULL";
                ps2 = con.prepareStatement(stm2);
                ps2.setString(1, offCode);
                ps2.setString(2, fy);
                rs3 = ps2.executeQuery();
                String Status = "N";
                int aerId = 0;
                if (rs3.next()) {
                    if (rs3.getInt("co_aer_id") > 0) {
                        Status = "Y";
                    }
                    aer2.setCoStatus(Status);
                    aerId = rs3.getInt("co_aer_id");
                    aer2.setAerId(aerId);

                    stm3 = "select post_group,SUM(menin_position) meninposition from annual_establish_report_at_co where co_aer_id=? and aer_part_type = 'PART-A' group by post_group";
                    ps3 = con.prepareStatement(stm3);
                    ps3.setInt(1, aerId);
                    rs3 = ps3.executeQuery();
                    while (rs3.next()) {
                        if (rs3.getString("post_group") != null && rs3.getString("post_group").equals("A")) {
                            groupA = rs3.getInt("meninposition");
                        }
                        if (rs3.getString("post_group") != null && rs3.getString("post_group").equals("B")) {
                            groupB = rs3.getInt("meninposition");
                        }
                        if (rs3.getString("post_group") != null && rs3.getString("post_group").equals("C")) {
                            groupC = rs3.getInt("meninposition");
                        }
                        if (rs3.getString("post_group") != null && rs3.getString("post_group").equals("D")) {
                            groupD = rs3.getInt("meninposition");
                        }

                        aer2.setTotalACnt(groupA);
                        aer2.setTotalBCnt(groupB);
                        aer2.setTotalCCnt(groupC);
                        aer2.setTotalDCnt(groupD);

                        totalGroup = groupA + groupB + groupC + groupD;
                        aer2.setTotalallCnt(totalGroup);
                    }

                    DataBaseFunctions.closeSqlObjects(rs3, ps3);

                    stm3 = "select SUM(menin_position) meninposition from aer_report_submit_at_co ARS"
                            + " INNER JOIN annual_establish_report_at_co AER ON ARS.co_aer_id = AER.co_aer_id "
                            + " WHERE ARS.co_aer_id=? AND post_group is not null and (aer_part_type = 'PART-B' OR aer_part_type = 'B')";
                    ps3 = con.prepareStatement(stm3);
                    ps3.setInt(1, aerId);
                    rs3 = ps3.executeQuery();
                    if (rs3.next()) {
                        grantidaid = rs3.getInt("meninposition");
                    }
                    aer2.setGrantinAid(grantidaid);

                    DataBaseFunctions.closeSqlObjects(rs3, ps3);

                    stm3 = "select SUM(menin_position) meninposition from aer_report_submit_at_co ARS"
                            + " INNER JOIN annual_establish_report_at_co AER ON ARS.co_aer_id = AER.co_aer_id "
                            + " WHERE ARS.co_aer_id=? AND post_group is not null and (aer_part_type = 'PART-D' OR aer_part_type = 'D')";
                    ps3 = con.prepareStatement(stm3);
                    ps3.setInt(1, aerId);
                    rs3 = ps3.executeQuery();
                    if (rs3.next()) {
                        partD = rs3.getInt("meninposition");
                    }
                    aer2.setPartD(partD);

                    DataBaseFunctions.closeSqlObjects(rs3, ps3);

                    stm3 = "select SUM(menin_position) meninposition from aer_report_submit_at_co ARS"
                            + " INNER JOIN annual_establish_report_at_co AER ON ARS.co_aer_id = AER.co_aer_id "
                            + " WHERE ARS.co_aer_id=? AND post_group is not null and (aer_part_type = 'PART-E' OR aer_part_type = 'E')";
                    ps3 = con.prepareStatement(stm3);
                    ps3.setInt(1, aerId);
                    rs3 = ps3.executeQuery();
                    if (rs3.next()) {
                        partE = rs3.getInt("meninposition");
                    }
                    aer2.setPartE(partE);
                }
                outList.add(aer2);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs1, rs2, rs3);
            DataBaseFunctions.closeSqlObjects(ps1, ps2, ps3);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outList;
    }

    @Override
    public ArrayList getDDOtoCOMappingList(String spc) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ArrayList colist = new ArrayList();
        SelectOption so = null;
        try {
            con = this.dataSource.getConnection();

            /*String sql = "select co_off_code,off_en from ddo_to_co_mapping"
             + " inner join g_office on ddo_to_co_mapping.co_off_code=g_office.off_code where ddo_off_code=? and is_active='Y' order by off_en";*/
            String sql = "select distinct co_off_code,off_en from ddo_to_co_mapping"
                    + " inner join g_office on ddo_to_co_mapping.co_off_code=g_office.off_code"
                    + " inner join process_authorization on ddo_to_co_mapping.ddo_off_code=process_authorization.off_code where is_active='Y' and spc=? and role_type='APPROVER' order by off_en";
            pst = con.prepareStatement(sql);
            pst.setString(1, spc);
            rs = pst.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("co_off_code"));
                so.setLabel(rs.getString("off_en"));
                colist.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return colist;
    }

    @Override
    public void changeCadre(int aerid, String gpc, String cadreType) {

        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "UPDATE annual_establish_report SET cadre_code=? WHERE report_id=? AND gpc=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, cadreType);
            pst.setInt(2, aerid);
            pst.setString(3, gpc);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void insertGranteeOfficeData(int aerid, String pOffcode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        PreparedStatement granteepst1 = null;
        ResultSet granteers1 = null;

        PreparedStatement granteepst2 = null;
        ResultSet granteers2 = null;

        PreparedStatement granteeinsertpst = null;

        String fy = "";

        String prevFinYear = "";

        Office office = null;
        try {
            con = this.dataSource.getConnection();

            String insertsql = "insert into aer_other_establishment(aer_id,category,post,vacancy,payscale_6th,payscale_7th,remarks,sanc_strength,fyear,group_name,part_type,men_position,gp,off_code) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            granteeinsertpst = con.prepareStatement(insertsql);

            String sql = "select fy from aer_report_submit where aer_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, aerid);
            rs = pst.executeQuery();
            if (rs.next()) {
                fy = rs.getString("fy");
            }
            if (fy != null && !fy.equals("")) {
                String[] curFinYearArr = fy.split("-");
                int curFinYear0 = Integer.parseInt(curFinYearArr[0]);
                int curFinYear1 = Integer.parseInt(curFinYearArr[1]);

                int prevFinYear0 = curFinYear0 - 1;
                int prevFinYear1 = curFinYear1 - 1;

                prevFinYear = prevFinYear0 + "-" + prevFinYear1;
            }
            if (prevFinYear != null && !prevFinYear.equals("")) {

                DataBaseFunctions.closeSqlObjects(rs, pst);

                sql = "select aer_report_submit.aer_id from aer_other_establishment"
                        + " inner join aer_report_submit on aer_other_establishment.aer_id=aer_report_submit.aer_id where fy=? and aer_report_submit.off_code=? AND part_type='B' group by aer_report_submit.aer_id";
                pst = con.prepareStatement(sql);
                pst.setString(1, prevFinYear);
                pst.setString(2, pOffcode);
                rs = pst.executeQuery();
                while (rs.next()) {
                    int prevAerId = rs.getInt("aer_id");

                    granteepst1 = con.prepareStatement("SELECT off_code,off_en,ddo_code from g_office where off_bill_status='GA' AND p_off_code=? order by off_en asc");
                    granteepst1.setString(1, pOffcode);
                    granteers1 = granteepst1.executeQuery();

                    granteepst2 = con.prepareStatement("SELECT * FROM aer_other_establishment WHERE aer_id=? and off_code=? AND part_type=? order by group_name");

                    while (granteers1.next()) {
                        office = new Office();
                        office.setOffCode(granteers1.getString("off_code"));
                        office.setOffName(granteers1.getString("off_en"));
                        office.setDdoCode(granteers1.getString("ddo_code"));

                        granteepst2.setInt(1, prevAerId);
                        granteepst2.setString(2, office.getOffCode());
                        granteepst2.setString(3, "B");
                        granteers2 = granteepst2.executeQuery();
                        while (granteers2.next()) {
                            AnnualEstablishment aer = new AnnualEstablishment();
                            aer.setOffCode(office.getOffCode());
                            aer.setOtherCategory(granteers2.getString("category"));
                            aer.setOtherPost(granteers2.getString("post"));
                            aer.setScaleofPay(granteers2.getString("payscale_6th"));
                            aer.setOther7thPay(granteers2.getString("payscale_7th"));
                            aer.setOtherRemarks(granteers2.getString("remarks"));
                            aer.setOtherSS(granteers2.getInt("sanc_strength"));
                            aer.setMeninPosition(granteers2.getInt("men_position"));
                            aer.setOtherVacancy(aer.getOtherSS() - aer.getMeninPosition());
                            aer.setAerId(granteers2.getInt("aer_id"));
                            aer.setAerOtherId(granteers2.getInt("aer_other_id"));
                            aer.setFinancialYear(granteers2.getString("fyear"));
                            aer.setGroup(granteers2.getString("group_name"));
                            aer.setPartType(granteers2.getString("part_type"));
                            aer.setGp(granteers2.getString("gp"));

                            String isDuplicate = isDuplicateData(con, aer.getOffCode(), aerid, aer.getGroup(), aer.getOtherPost(), aer.getScaleofPay(), aer.getGp(), aer.getOther7thPay(), aer.getOtherSS(), aer.getMeninPosition(), aer.getOtherVacancy(), aer.getOtherRemarks());

                            if (isDuplicate.equals("N")) {
                                granteeinsertpst.setInt(1, aerid);
                                granteeinsertpst.setString(2, aer.getOtherCategory());
                                granteeinsertpst.setString(3, aer.getOtherPost());
                                granteeinsertpst.setInt(4, aer.getOtherVacancy());
                                granteeinsertpst.setString(5, aer.getScaleofPay());
                                granteeinsertpst.setString(6, aer.getOther7thPay());
                                granteeinsertpst.setString(7, aer.getOtherRemarks());
                                granteeinsertpst.setInt(8, aer.getOtherSS());
                                granteeinsertpst.setString(9, fy);
                                granteeinsertpst.setString(10, aer.getGroup());
                                granteeinsertpst.setString(11, "B");
                                granteeinsertpst.setInt(12, aer.getMeninPosition());
                                granteeinsertpst.setString(13, aer.getGp());
                                granteeinsertpst.setString(14, aer.getOffCode());
                                granteeinsertpst.executeUpdate();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    private String isDuplicateData(Connection con, String offcode, int curAerId, String group, String postname, String payscale_6th, String gp, String level7thPay, int sanctionedStrength, int menInPosition, int vacancy, String remarks) {

        PreparedStatement pst = null;
        ResultSet rs = null;

        String isDuplicate = "N";
        try {
            String sql = "select * from aer_other_establishment where aer_id=? and off_code=? AND part_type=? and group_name=? and post=? and payscale_6th=? and gp=? and payscale_7th=? and sanc_strength=? and men_position=? and vacancy=? and remarks=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, curAerId);
            pst.setString(2, offcode);
            pst.setString(3, "B");
            pst.setString(4, group);
            pst.setString(5, postname);
            pst.setString(6, payscale_6th);
            pst.setString(7, gp);
            pst.setString(8, level7thPay);
            pst.setInt(9, sanctionedStrength);
            pst.setInt(10, menInPosition);
            pst.setInt(11, vacancy);
            pst.setString(12, remarks);
            rs = pst.executeQuery();
            if (rs.next()) {
                isDuplicate = "Y";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
        }
        return isDuplicate;
    }

    @Override
    public int getCOAuthorityApprovedOperatorList(String loginoffcode, String fy, String roletype, String loginspc) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int count = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT count(is_verifier_submitted) cnt FROM process_authorization"
                    + " INNER JOIN aer_report_submit ON process_authorization.OFF_CODE = aer_report_submit.CO_OFF_CODE"
                    + " INNER JOIN g_spc on aer_report_submit.reviewer_spc=g_spc.spc"
                    + " inner join g_office on aer_report_submit.OFF_CODE=g_office.off_code"
                    + " WHERE process_authorization.SPC=? AND PROCESS_ID=13 AND aer_report_submit.FY=?"
                    + " AND ROLE_TYPE='VERIFIER'"
                    + " and is_reviewer_submitted='Y' and is_verifier_submitted != 'Y'";
            pst = con.prepareStatement(sql);
            pst.setString(1, loginspc);
            pst.setString(2, fy);
            rs = pst.executeQuery();
            if (rs.next()) {
                count = rs.getInt("cnt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return count;
    }

    @Override
    public String getAERSubmittedCOOfficeName(int coAerid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String officename = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "select ddo_code,off_en from aer_report_submit_at_co"
                    + " inner join g_office on aer_report_submit_at_co.co_off_code=g_office.off_code"
                    + " where co_aer_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, coAerid);
            rs = pst.executeQuery();
            if (rs.next()) {
                officename = rs.getString("off_en") + "(" + rs.getString("ddo_code") + ")";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return officename;
    }

    @Override
    public String getAERSubmittedAOOfficeName(int aoAerid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String officename = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "select ddo_code,off_en from aer_report_submit_at_ao"
                    + " inner join g_office on aer_report_submit_at_ao.ao_off_code=g_office.off_code"
                    + " where ao_aer_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, aoAerid);
            rs = pst.executeQuery();
            if (rs.next()) {
                officename = rs.getString("off_en") + "(" + rs.getString("ddo_code") + ")";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return officename;
    }

    @Override
    public ArrayList COWiseDDONotSubmited(String dcode) {
        Connection con = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps1 = null;
        ResultSet rs1, rs2, rs3 = null;
        ArrayList outList = new ArrayList();
        String stm, stm1 = null;
        String fy = "2022-23";

        try {
            con = repodataSource.getConnection();
            String sql2 = "SELECT g_office.off_code,g_office.off_en FROM g_office INNER JOIN ddo_to_co_mapping ON ddo_to_co_mapping.ddo_off_code=g_office.off_code WHERE  ddo_to_co_mapping.co_off_code=?  order by off_en ";
            ps1 = con.prepareStatement(sql2);
            ps1.setString(1, dcode);
            rs2 = ps1.executeQuery();
            while (rs2.next()) {
                AnnualEstablishment aer2 = new AnnualEstablishment();
                aer2.setOffCode(rs2.getString("off_code"));
                aer2.setOffName(rs2.getString("off_en"));
                String offCode = rs2.getString("off_code");

                stm1 = "SELECT  aer_id FROM aer_report_submit WHERE off_code=? AND fy=? AND  co_off_code is NOT NULL LIMIT 1  ";
                ps2 = con.prepareStatement(stm1);
                ps2.setString(1, offCode);
                ps2.setString(2, fy);
                rs3 = ps2.executeQuery();
                String Status = "N";
                aer2.setCoStatus(Status);
                int aerId = 0;
                while (rs3.next()) {
                    if (rs3.getInt("aer_id") > 0) {
                        Status = "Y";
                    }
                    aer2.setCoStatus(Status);
                    Status = "N";
                }

                outList.add(aer2);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps2);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outList;
    }

    @Override
    public AnnualEstablishment[] getAerViewOtherPartForAO(String fy, String spc, String partTYpe, String roletype) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List<AnnualEstablishment> aeList = new ArrayList<>();
        try {
            con = this.dataSource.getConnection();

            String sql = " select sum(sanction_strength) sanc_strength,sum(menin_position) men_position,sum(vacancy_position) vacancy,aer_other_establishment.postname,"
                    + " aer_other_establishment.gp,aer_other_establishment.remarks from aer_report_submit_at_co"
                    + " INNER JOIN process_authorization ON aer_report_submit_at_co.ao_off_code=process_authorization.off_code"
                    + " inner join annual_establish_report_at_co aer_other_establishment on aer_report_submit_at_co.co_aer_id=aer_other_establishment.co_aer_id"
                    + " where FY=?"
                    + " AND process_authorization.SPC=? and role_type=?"
                    + " and aer_part_type=?"
                    + " and process_authorization.process_id=13 group by"
                    + " aer_other_establishment.postname,"
                    + " aer_other_establishment.gp,aer_other_establishment.remarks";
            pst = con.prepareStatement(sql);
            pst.setString(1, fy);
            pst.setString(2, spc);
            pst.setString(3, roletype);
            pst.setString(4, partTYpe);
            rs = pst.executeQuery();
            while (rs.next()) {
                AnnualEstablishment aer = new AnnualEstablishment();
                aer.setOtherPost(rs.getString("postname"));
                aer.setOtherRemarks(rs.getString("remarks"));
                aer.setOtherSS(rs.getInt("sanc_strength"));
                aer.setMeninPosition(rs.getInt("men_position"));
                aer.setOtherVacancy((rs.getInt("sanc_strength") - rs.getInt("men_position")));
                aer.setGp(rs.getString("gp"));
                aeList.add(aer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        AnnualEstablishment addrarray[] = aeList.toArray(new AnnualEstablishment[aeList.size()]);
        return addrarray;

    }

    @Override
    public List getVerifierCOOfficeList(String empid, String spc, String fy) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List officelist = new ArrayList();

        AnnualEstablishment ae = new AnnualEstablishment();
        try {
            con = this.dataSource.getConnection();

            String sql = "select process_authorization.off_code,ddo_code,off_en,aer_report_submit_at_co.co_aer_id,aer_report_submit_at_co.verifier_submitted_on from process_authorization"
                    + " inner join g_office on process_authorization.off_code=g_office.off_code"
                    + " left outer join (select * from aer_report_submit_at_co where fy=?)aer_report_submit_at_co on process_authorization.off_code=aer_report_submit_at_co.co_off_code"
                    + " where hrms_id=? and spc=? and role_type='VERIFIER'";
            pst = con.prepareStatement(sql);
            pst.setString(1, fy);
            pst.setString(2, empid);
            pst.setString(3, spc);
            rs = pst.executeQuery();
            while (rs.next()) {
                ae = new AnnualEstablishment();
                ae.setOffCode(rs.getString("off_code"));
                ae.setDdoCode(rs.getString("ddo_code"));
                ae.setOffName(rs.getString("off_en"));

                ae.setCoaerid(rs.getString("co_aer_id"));
                ae.setSubmittedDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("verifier_submitted_on")));

                officelist.add(ae);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return officelist;
    }

    @Override
    public List getAerReportListFinancialYearWiseForCOLevelForMultipleCOOffice(String cooffCode, String financialYear, String roleType, String loginSpc) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<AnnualEstablishment> li = new ArrayList<AnnualEstablishment>();
        AnnualEstablishment ae = null;
        int i = 0;
        try {
            con = this.dataSource.getConnection();
            //  System.out.println("roleType is: " + roleType);
            if (roleType != null && !roleType.equals("")) {
                if (roleType.equalsIgnoreCase("VERIFIER")) {
                    ps = con.prepareStatement(" SELECT * FROM process_authorization "
                            + " INNER JOIN aer_report_submit ON process_authorization.OFF_CODE = aer_report_submit.CO_OFF_CODE "
                            + " inner join g_office on aer_report_submit.OFF_CODE=g_office.off_code "
                            + " WHERE process_authorization.SPC=? AND PROCESS_ID=13 AND aer_report_submit.FY=?"
                            + " AND ROLE_TYPE='VERIFIER'"
                            + " and is_reviewer_submitted='Y' and aer_report_submit.CO_OFF_CODE=? order by reviewer_submitted_on ");
                    ps.setString(1, loginSpc);
                    ps.setString(2, financialYear);
                    ps.setString(3, cooffCode);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        i++;

                        ae = new AnnualEstablishment();
                        ae.setSerialno(i);
                        ae.setAerId(rs.getInt("aer_id"));
                        //ae.setPostname(rs.getString("spn"));
                        //ae.setControllingSpc(rs.getString("spn"));
                        ae.setSubmittedDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("reviewer_submitted_on")));
                        if (rs.getString("is_verifier_submitted").equalsIgnoreCase("N")) {

                            ae.setStatus("PENDING AT SELF");
                        } else if (rs.getString("is_verifier_submitted").equalsIgnoreCase("Y") && (rs.getString("co_aer_id") == null || rs.getString("co_aer_id").equals(""))) {
                            ae.setSubmittedDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("verifier_submitted_on")));
                            ae.setStatus("APPROVED BY VERIFIER");
                        } else if (rs.getString("is_acceptor_submitted").equalsIgnoreCase("N") && rs.getInt("co_aer_id") > 0) {
                            ae.setSubmittedDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("verifier_submitted_on")));
                            ae.setStatus("PENDING AT ACCEPTOR END");
                        } else if (rs.getString("is_acceptor_submitted").equalsIgnoreCase("Y")) {
                            ae.setStatus("ACCEPTED");
                            ae.setSubmittedDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("acceptor_submitted_on")));
                        }
                        if (rs.getString("is_verifier_submitted") != null && !rs.getString("is_verifier_submitted").equals("") && rs.getString("is_verifier_submitted").equals("Y")) {
                            ae.setShowApproveLink("N");
                        } else {
                            ae.setShowApproveLink("Y");
                        }
                        ae.setDdoCode(rs.getString("ddo_code"));
                        ae.setOperatoroffName(rs.getString("off_en"));
                        ae.setFy(rs.getString("fy"));
                        ae.setRevertReason("");

                        li.add(ae);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public int getSchedule2AerIdForMultipleCOOffice(String loginSpc, String empId, String fy, String cooffcode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        String offCode = "";
        int coaerid = 0;
        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("select off_code from process_authorization where ROLE_TYPE='VERIFIER' and hrms_id=? and spc=? and off_code=?");
            pst.setString(1, empId);
            pst.setString(2, loginSpc);
            pst.setString(3, cooffcode);
            rs = pst.executeQuery();
            if (rs.next()) {
                offCode = rs.getString("off_code");
            }

            String sql = "select co_aer_id from aer_report_submit_at_co where fy=? and co_off_code=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, fy);
            pst.setString(2, offCode);
            rs = pst.executeQuery();
            if (rs.next()) {
                if(rs.getInt("co_aer_id") > 0){              
                    coaerid = rs.getInt("co_aer_id");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return coaerid;
    }

    @Override
    public List getAerViewForCOMultiple(String fy, String spc, String coOffCode, String postgrp, String cadreType, String partTYpe, String roletype) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        List<AnnualEstablishment> aeList = new ArrayList<>();
        boolean isdataInserted = false;
        int coAerId = 0;
        String sql = "";
        try {
            con = this.dataSource.getConnection();

            ps2 = con.prepareStatement(" SELECT co_aer_id FROM aer_report_submit_at_co where co_off_code=? and verifier_spc=? and fy=? ");
            ps2.setString(1, coOffCode);
            ps2.setString(2, spc);
            ps2.setString(3, fy);
            rs = ps2.executeQuery();
            if (rs.next()) {
                coAerId = rs.getInt("co_aer_id");
                if (coAerId > 0) {
                    //isdataInserted = true;
                }
            }
            DataBaseFunctions.closeSqlObjects(rs, ps2);

            if (isdataInserted) {

            } else {
                if (roletype.equalsIgnoreCase("VERIFIER")) {
                    if (partTYpe.equalsIgnoreCase("PART-A")) {
                        if (cadreType != null && !cadreType.equals("")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position, "
                                    + " postname, post_group, annual_establish_report.pay_scale_7th_pay, pay_scale_6th_pay, "
                                    + " annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  from aer_report_submit aer "
                                    + " INNER JOIN process_authorization ON aer.co_off_code=process_authorization.off_code "
                                    + " inner join g_spc on aer.approver_spc=g_spc.spc "
                                    + " inner join annual_establish_report on aer.aer_id=annual_establish_report.aer_id "
                                    + " where FY=? "
                                    + " AND process_authorization.SPC=? "
                                    + " and annual_establish_report.cadre_code=? "
                                    + " and post_group=? "
                                    + " and aer_part_type=? "
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='VERIFIER' and is_reviewer_submitted='Y' and is_verifier_submitted='Y' "
                                    + " and aer.co_off_code=?"
                                    + " group by postname, post_group, annual_establish_report.pay_scale_7th_pay, "
                                    + " annual_establish_report.pay_scale_6th_pay, annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  order by annual_establish_report.pay_scale_7th_pay desc ";
                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, cadreType);
                            pst.setString(4, postgrp);
                            pst.setString(5, partTYpe);
                            pst.setString(6, coOffCode);
                        } else if (!postgrp.equals("D")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position, "
                                    + " postname, post_group, annual_establish_report.pay_scale_7th_pay, pay_scale_6th_pay, "
                                    + " annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  from aer_report_submit aer "
                                    + " INNER JOIN process_authorization ON aer.co_off_code=process_authorization.off_code "
                                    + " inner join g_spc on aer.approver_spc=g_spc.spc "
                                    + " inner join annual_establish_report on aer.aer_id=annual_establish_report.aer_id "
                                    + " where FY=? "
                                    + " AND process_authorization.SPC=? "
                                    + " and ((annual_establish_report.cadre_code<>'AIS' and annual_establish_report.cadre_code<>'UGC' and annual_establish_report.cadre_code<>'OJS') or annual_establish_report.cadre_code is null) "
                                    + " and post_group=? "
                                    + " and aer_part_type=? "
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='VERIFIER' and is_reviewer_submitted='Y' and is_verifier_submitted='Y' "
                                    + " and aer.co_off_code=?"
                                    + " group by postname, post_group, annual_establish_report.pay_scale_7th_pay, "
                                    + " annual_establish_report.pay_scale_6th_pay, annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  order by annual_establish_report.pay_scale_7th_pay desc ";

                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, postgrp);
                            pst.setString(4, partTYpe);
                            pst.setString(5, coOffCode);
                        } else if (postgrp.equals("D")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position, "
                                    + " postname, post_group, annual_establish_report.pay_scale_7th_pay, pay_scale_6th_pay, "
                                    + " annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  from aer_report_submit aer "
                                    + " INNER JOIN process_authorization ON aer.co_off_code=process_authorization.off_code "
                                    + " inner join g_spc on aer.approver_spc=g_spc.spc "
                                    + " inner join annual_establish_report on aer.aer_id=annual_establish_report.aer_id "
                                    + " where FY=? AND process_authorization.SPC=? "
                                    + " and ((annual_establish_report.cadre_code<>'AIS' and annual_establish_report.cadre_code<>'UGC' and annual_establish_report.cadre_code<>'OJS') or annual_establish_report.cadre_code is null) "
                                    + " and (post_group=? or post_group is null) and aer_part_type=? "
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='VERIFIER' and is_reviewer_submitted='Y' and is_verifier_submitted='Y' "
                                    + " and aer.co_off_code=?"
                                    + " group by postname, post_group, annual_establish_report.pay_scale_7th_pay, "
                                    + " annual_establish_report.pay_scale_6th_pay, annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  order by annual_establish_report.pay_scale_7th_pay desc ";
                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, postgrp);
                            pst.setString(4, partTYpe);
                            pst.setString(5, coOffCode);
                        }
                    } else if (partTYpe.equalsIgnoreCase("PART-B")) {
                        if (cadreType != null && !cadreType.equals("")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position, "
                                    + " postname, post_group, annual_establish_report.pay_scale_7th_pay, pay_scale_6th_pay, "
                                    + " annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  from aer_report_submit aer "
                                    + " INNER JOIN process_authorization ON aer.co_off_code=process_authorization.off_code "
                                    + " inner join g_spc on aer.approver_spc=g_spc.spc "
                                    + " inner join annual_establish_report on aer.aer_id=annual_establish_report.aer_id "
                                    + " where FY=? "
                                    + " AND process_authorization.SPC=? "
                                    + " and annual_establish_report.cadre_code=? "
                                    + " and post_group=? "
                                    + " and aer_part_type=? "
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='VERIFIER' and is_reviewer_submitted='Y' and is_verifier_submitted='Y' "
                                    + " and aer.co_off_code=?"
                                    + " group by postname, post_group, annual_establish_report.pay_scale_7th_pay, "
                                    + " annual_establish_report.pay_scale_6th_pay, annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  order by annual_establish_report.pay_scale_7th_pay desc ";
                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, cadreType);
                            pst.setString(4, postgrp);
                            pst.setString(5, partTYpe);
                            pst.setString(6, coOffCode);
                        } else if (!postgrp.equals("D")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position, "
                                    + " postname, post_group, annual_establish_report.pay_scale_7th_pay, pay_scale_6th_pay, "
                                    + " annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  from aer_report_submit aer "
                                    + " INNER JOIN process_authorization ON aer.co_off_code=process_authorization.off_code "
                                    + " inner join g_spc on aer.approver_spc=g_spc.spc "
                                    + " inner join annual_establish_report on aer.aer_id=annual_establish_report.aer_id "
                                    + " where FY=? "
                                    + " AND process_authorization.SPC=? "
                                    + " and ((annual_establish_report.cadre_code<>'AIS' and annual_establish_report.cadre_code<>'UGC' and annual_establish_report.cadre_code<>'OJS') or annual_establish_report.cadre_code is null) "
                                    + " and post_group=? "
                                    + " and aer_part_type=? "
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='VERIFIER' and is_reviewer_submitted='Y' and is_verifier_submitted='Y' "
                                    + " and aer.co_off_code=?"
                                    + " group by postname, post_group, annual_establish_report.pay_scale_7th_pay, "
                                    + " annual_establish_report.pay_scale_6th_pay, annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  order by annual_establish_report.pay_scale_7th_pay desc ";

                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, postgrp);
                            pst.setString(4, partTYpe);
                            pst.setString(5, coOffCode);
                        } else if (postgrp.equals("D")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position, "
                                    + " postname, post_group, annual_establish_report.pay_scale_7th_pay, pay_scale_6th_pay, "
                                    + " annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  from aer_report_submit aer "
                                    + " INNER JOIN process_authorization ON aer.co_off_code=process_authorization.off_code "
                                    + " inner join g_spc on aer.approver_spc=g_spc.spc "
                                    + " inner join annual_establish_report on aer.aer_id=annual_establish_report.aer_id "
                                    + " where FY=? AND process_authorization.SPC=? "
                                    + " and ((annual_establish_report.cadre_code<>'AIS' and annual_establish_report.cadre_code<>'UGC' and annual_establish_report.cadre_code<>'OJS') or annual_establish_report.cadre_code is null) "
                                    + " and (post_group=? or post_group is null) and aer_part_type=? "
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='VERIFIER' and is_reviewer_submitted='Y' and is_verifier_submitted='Y' "
                                    + " and aer.co_off_code=?"
                                    + " group by postname, post_group, annual_establish_report.pay_scale_7th_pay, "
                                    + " annual_establish_report.pay_scale_6th_pay, annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  order by annual_establish_report.pay_scale_7th_pay desc ";
                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, postgrp);
                            pst.setString(4, partTYpe);
                            pst.setString(5, coOffCode);
                        }
                    } else if (partTYpe.equalsIgnoreCase("PART-C")) {
                        sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position, "
                                + " postname, post_group, annual_establish_report.pay_scale_7th_pay, pay_scale_6th_pay, "
                                + " annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  from aer_report_submit aer "
                                + " INNER JOIN process_authorization ON aer.co_off_code=process_authorization.off_code "
                                + " inner join g_spc on aer.approver_spc=g_spc.spc "
                                + " inner join annual_establish_report on aer.aer_id=annual_establish_report.aer_id "
                                + " where FY=? "
                                + " AND process_authorization.SPC=? "
                                + " and aer_part_type=? "
                                + " and process_authorization.process_id=13 and process_authorization.role_type='VERIFIER' and is_reviewer_submitted='Y' and is_verifier_submitted='Y' "
                                + " and aer.co_off_code=?"
                                + " group by postname, post_group, annual_establish_report.pay_scale_7th_pay, "
                                + " annual_establish_report.pay_scale_6th_pay, annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  order by annual_establish_report.pay_scale_7th_pay desc ";
                        pst = con.prepareStatement(sql);
                        pst.setString(1, fy);
                        pst.setString(2, spc);
                        pst.setString(3, partTYpe);
                        pst.setString(4, coOffCode);
                    }

                    rs = pst.executeQuery();
                    while (rs.next()) {
                        AnnualEstablishment aer = new AnnualEstablishment();
                        aer.setSanctionedStrength(rs.getInt("sanction_strength"));
                        aer.setMeninPosition(rs.getInt("menin_position"));
                        aer.setVacancyPosition(rs.getInt("vacancy_position"));
                        //aer.setGpc(rs.getString("gpc"));
                        aer.setPostname(rs.getString("postname"));
                        aer.setPostgrp(rs.getString("post_group"));
                        aer.setScaleofPay(rs.getString("pay_scale_6th_pay"));
                        aer.setScaleofPay7th(rs.getString("pay_scale_7th_pay"));
                        aer.setGp(rs.getString("gp"));
                        aer.setCadreType(rs.getString("cadre_code"));
                        aer.setPartType(rs.getString("aer_part_type"));
                        aeList.add(aer);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return aeList;
    }

    @Override
    public AnnualEstablishment[] getAerViewOtherPartForCOMultiple(String fy, String spc, String partTYpe, String roletype, String cooffcode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List<AnnualEstablishment> aeList = new ArrayList<>();
        try {
            con = this.dataSource.getConnection();

            String sql = "select sum(sanc_strength) sanc_strength,sum(men_position) men_position,sum(vacancy) vacancy,aer_other_establishment.category,aer_other_establishment.post,aer_other_establishment.payscale_6th,aer_other_establishment.payscale_7th,"
                    + " aer_other_establishment.gp,aer_other_establishment.group_name,aer_other_establishment.remarks from aer_report_submit aer"
                    + " INNER JOIN process_authorization ON aer.co_off_code=process_authorization.off_code"
                    + " inner join g_spc on aer.approver_spc=g_spc.spc"
                    + " inner join aer_other_establishment on aer.aer_id=aer_other_establishment.aer_id"
                    + " where FY=?"
                    + " AND process_authorization.SPC=? and role_type=?"
                    + " and part_type=?"
                    + " and process_authorization.process_id=13 and is_approver_submitted='Y' and is_reviewer_submitted='Y' and is_verifier_submitted='Y' "
                    + " and aer.co_off_code=? group by"
                    + " aer_other_establishment.category,aer_other_establishment.post,aer_other_establishment.payscale_6th,aer_other_establishment.payscale_7th,aer_other_establishment.gp,aer_other_establishment.group_name,aer_other_establishment.remarks";
            pst = con.prepareStatement(sql);
            pst.setString(1, fy);
            pst.setString(2, spc);
            pst.setString(3, roletype);
            pst.setString(4, partTYpe);
            pst.setString(5, cooffcode);
            rs = pst.executeQuery();
            while (rs.next()) {
                AnnualEstablishment aer = new AnnualEstablishment();
                aer.setOtherCategory(rs.getString("category"));
                aer.setOtherPost(rs.getString("post"));
                aer.setScaleofPay(rs.getString("payscale_6th"));
                aer.setOther7thPay(rs.getString("payscale_7th"));
                aer.setOtherRemarks(rs.getString("remarks"));
                aer.setOtherSS(rs.getInt("sanc_strength"));
                aer.setOtherVacancy(rs.getInt("vacancy"));
                aer.setMeninPosition((rs.getInt("sanc_strength") - rs.getInt("vacancy")));
                aer.setGroup(rs.getString("group_name"));
                aer.setGp(rs.getString("gp"));
                aeList.add(aer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        AnnualEstablishment addrarray[] = aeList.toArray(new AnnualEstablishment[aeList.size()]);
        return addrarray;
    }

    @Override
    public void insertConsolidatedDataForAOPartABFORGIAandCMultipleCO(int coAerId, String fy, String spc, String cooffCode, String cadreType, String postgrp, String partTYpe) {

        Connection con = null;

        PreparedStatement pst = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;

        String sql = "";
        try {
            con = this.dataSource.getConnection();

            if (partTYpe.equalsIgnoreCase("PART-A") || partTYpe.equalsIgnoreCase("PART-B")) {
                if (cadreType != null && !cadreType.equals("")) {
                    sql = "insert into annual_establish_report_at_co(co_aer_id, off_code, sanction_strength,menin_position,vacancy_position,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,gp,cadre_code,aer_part_type)"
                            + " select " + coAerId + ", '" + cooffCode + "' , sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                            + " postname, post_group, annual_establish_report.pay_scale_7th_pay, pay_scale_6th_pay,"
                            + " annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  from aer_report_submit aer"
                            + " INNER JOIN process_authorization ON aer.co_off_code=process_authorization.off_code"
                            + " inner join g_spc on aer.approver_spc=g_spc.spc"
                            + " inner join annual_establish_report on aer.aer_id=annual_establish_report.aer_id"
                            + " where FY=?"
                            + " AND process_authorization.SPC=?"
                            + " and annual_establish_report.cadre_code=?"
                            + " and post_group=?"
                            + " and aer_part_type=?"
                            + " and process_authorization.process_id=13 and process_authorization.role_type='VERIFIER' and is_approver_submitted='Y' and is_verifier_submitted='Y'"
                            + " and aer.co_off_code=?"
                            + " group by postname, post_group, annual_establish_report.pay_scale_7th_pay,"
                            + " annual_establish_report.pay_scale_6th_pay, annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, fy);
                    pst.setString(2, spc);
                    pst.setString(3, cadreType);
                    pst.setString(4, postgrp);
                    pst.setString(5, partTYpe);
                    pst.setString(6, cooffCode);
                    pst.executeUpdate();
                } else if (!postgrp.equals("D")) {
                    sql = "insert into annual_establish_report_at_co(co_aer_id, off_code,  sanction_strength,menin_position,vacancy_position,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,gp,cadre_code,aer_part_type)"
                            + " select " + coAerId + ", '" + cooffCode + "' , sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                            + " postname, post_group, annual_establish_report.pay_scale_7th_pay, pay_scale_6th_pay,"
                            + " annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  from aer_report_submit aer"
                            + " INNER JOIN process_authorization ON aer.co_off_code=process_authorization.off_code"
                            + " inner join g_spc on aer.approver_spc=g_spc.spc"
                            + " inner join annual_establish_report on aer.aer_id=annual_establish_report.aer_id"
                            + " where FY=?"
                            + " AND process_authorization.SPC=?"
                            + " and ((annual_establish_report.cadre_code<>'AIS' and annual_establish_report.cadre_code<>'UGC' and annual_establish_report.cadre_code<>'OJS') or annual_establish_report.cadre_code is null) "
                            + " and post_group=?"
                            + " and aer_part_type=?"
                            + " and process_authorization.process_id=13 and process_authorization.role_type='VERIFIER' and is_approver_submitted='Y' and is_verifier_submitted='Y'"
                            + " and aer.co_off_code=?"
                            + " group by postname, post_group, annual_establish_report.pay_scale_7th_pay,"
                            + " annual_establish_report.pay_scale_6th_pay, annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, fy);
                    pst.setString(2, spc);
                    pst.setString(3, postgrp);
                    pst.setString(4, partTYpe);
                    pst.setString(5, cooffCode);
                    pst.executeUpdate();
                } else if (postgrp.equals("D")) {
                    sql = "insert into annual_establish_report_at_co(co_aer_id, off_code, sanction_strength,menin_position,vacancy_position,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,gp,cadre_code,aer_part_type)"
                            + " select " + coAerId + ", '" + cooffCode + "' , sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                            + " postname, post_group, annual_establish_report.pay_scale_7th_pay, pay_scale_6th_pay,"
                            + " annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type from aer_report_submit aer"
                            + " INNER JOIN process_authorization ON aer.co_off_code=process_authorization.off_code"
                            + " inner join g_spc on aer.approver_spc=g_spc.spc"
                            + " inner join annual_establish_report on aer.aer_id=annual_establish_report.aer_id"
                            + " where FY=?"
                            + " AND process_authorization.SPC=?"
                            + " and ((annual_establish_report.cadre_code<>'AIS' and annual_establish_report.cadre_code<>'UGC' and annual_establish_report.cadre_code<>'OJS') or annual_establish_report.cadre_code is null) "
                            + " and (post_group=? or post_group is null)"
                            + " and aer_part_type=?"
                            + " and process_authorization.process_id=13 and process_authorization.role_type='VERIFIER' and is_approver_submitted='Y' and is_verifier_submitted='Y'"
                            + " and aer.co_off_code=?"
                            + " group by  postname, post_group, annual_establish_report.pay_scale_7th_pay,"
                            + " annual_establish_report.pay_scale_6th_pay, annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, fy);
                    pst.setString(2, spc);
                    pst.setString(3, postgrp);
                    pst.setString(4, partTYpe);
                    pst.setString(5, cooffCode);
                    pst.executeUpdate();
                }
            }
            if (partTYpe.equalsIgnoreCase("PART-C")) {
                sql = "insert into annual_establish_report_at_co(co_aer_id, off_code, sanction_strength,menin_position,vacancy_position,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,gp,cadre_code,aer_part_type)"
                        + " select " + coAerId + ", '" + cooffCode + "'  , sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                        + " postname, post_group, annual_establish_report.pay_scale_7th_pay, pay_scale_6th_pay,"
                        + " annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  from aer_report_submit aer"
                        + " INNER JOIN process_authorization ON aer.co_off_code=process_authorization.off_code"
                        + " inner join g_spc on aer.approver_spc=g_spc.spc"
                        + " inner join annual_establish_report on aer.aer_id=annual_establish_report.aer_id"
                        + " where FY=?"
                        + " AND process_authorization.SPC=? "
                        + " and aer_part_type=? "
                        + " and process_authorization.process_id=13 and process_authorization.role_type='VERIFIER' and is_approver_submitted='Y'  and is_verifier_submitted='Y' "
                        + " and aer.co_off_code=?"
                        + " group by postname, post_group, annual_establish_report.pay_scale_7th_pay, "
                        + " annual_establish_report.pay_scale_6th_pay, annual_establish_report.gp, annual_establish_report.cadre_code, aer_part_type  ";
                pst = con.prepareStatement(sql);
                pst.setString(1, fy);
                pst.setString(2, spc);
                pst.setString(3, partTYpe);
                pst.setString(4, cooffCode);
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void insertConsolidatedDataForAOPartBDEMultipleCO(String fy, String spc, String partType, int coAerId, String cooffCode) {

        Connection con = null;

        PreparedStatement pst = null;

        ResultSet rs = null;

        String sql = "";

        try {
            con = this.dataSource.getConnection();

            sql = "insert into annual_establish_report_at_co(co_aer_id, off_code, sanction_strength,menin_position,vacancy_position,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,gp,aer_part_type) "
                    + " select " + coAerId + ", '" + cooffCode + "' , sum(sanc_strength) sanc_strength,sum(men_position) men_position,sum(vacancy) vacancy,aer_other_establishment.post,aer_other_establishment.group_name,aer_other_establishment.payscale_7th,aer_other_establishment.payscale_6th, "
                    + " aer_other_establishment.gp,part_type from aer_report_submit aer "
                    + " INNER JOIN process_authorization ON aer.co_off_code=process_authorization.off_code "
                    + " inner join g_spc on aer.approver_spc=g_spc.spc"
                    + " inner join aer_other_establishment on aer.aer_id=aer_other_establishment.aer_id "
                    + " where FY=?"
                    + " AND process_authorization.SPC=?"
                    + " and part_type=?"
                    + " and process_authorization.process_id=13 and process_authorization.role_type='VERIFIER' and is_approver_submitted='Y' and is_verifier_submitted='Y'"
                    + " and aer.co_off_code=?"
                    + " group by "
                    + " aer_other_establishment.post,aer_other_establishment.group_name,aer_other_establishment.payscale_7th,aer_other_establishment.payscale_6th, "
                    + " aer_other_establishment.gp,part_type";
            pst = con.prepareStatement(sql);
            pst.setString(1, fy);
            pst.setString(2, spc);
            pst.setString(3, partType);
            pst.setString(4, cooffCode);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getAcceptorAOOfficeList(String empid, String spc, String fy) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List officelist = new ArrayList();

        AnnualEstablishment ae = new AnnualEstablishment();
        try {
            con = this.dataSource.getConnection();

            String sql = "select process_authorization.off_code,ddo_code,off_en,aer_report_submit_at_ao.ao_aer_id from process_authorization"
                    + " inner join g_office on process_authorization.off_code=g_office.off_code"
                    + " left outer join (select * from aer_report_submit_at_ao where fy=?)aer_report_submit_at_ao on process_authorization.off_code=aer_report_submit_at_ao.ao_off_code"
                    + " where hrms_id=? and spc=? and role_type='ACCEPTOR'";
            pst = con.prepareStatement(sql);
            pst.setString(1, fy);
            pst.setString(2, empid);
            pst.setString(3, spc);
            rs = pst.executeQuery();
            while (rs.next()) {
                ae = new AnnualEstablishment();
                ae.setOffCode(rs.getString("off_code"));
                ae.setDdoCode(rs.getString("ddo_code"));
                ae.setOffName(rs.getString("off_en"));

                ae.setAoaerid(rs.getString("ao_aer_id"));

                officelist.add(ae);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return officelist;
    }

    @Override
    public String getAcceptorAssignedOffice(String empid, String spc) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String offcode = "";

        try {
            con = this.dataSource.getConnection();

            String sql = "select process_authorization.off_code from process_authorization where hrms_id=? and spc=? and (role_type='ACCEPTOR' or role_type='DREVIEWER' or role_type='DVERIFIER')";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setString(2, spc);
            rs = pst.executeQuery();
            if (rs.next()) {
                offcode = rs.getString("off_code");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return offcode;
    }

    @Override
    public List getAerViewForMultipleAO(String fy, String spc, String aoOffCode, String postgrp, String cadreType, String partTYpe, String roletype) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List<AnnualEstablishment> aerList = new ArrayList<>();
        boolean isdataInserted = false;

        String sql = "";
        try {
            con = this.dataSource.getConnection();

            if (isdataInserted) {
            } else {
                if (roletype.equalsIgnoreCase("ACCEPTOR")) {
                    if (partTYpe.equalsIgnoreCase("PART-A")) {
                        if (cadreType != null && !cadreType.equals("")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                                    + " postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay, pay_scale_6th_pay,"
                                    + " annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type  from aer_report_submit_at_co aer"
                                    + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code"
                                    /*+ " inner join g_spc on aer.dreviewer_spc=g_spc.spc"*/
                                    + " inner join annual_establish_report_at_co on aer.co_aer_id=annual_establish_report_at_co.co_aer_id"
                                    + " where FY=?"
                                    + " AND process_authorization.SPC=?"
                                    + " and annual_establish_report_at_co.cadre_code=?"
                                    + " and post_group=?"
                                    + " and aer_part_type=?"
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='ACCEPTOR'"
                                    + " and aer.ao_off_code=?"
                                    + " group by postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay,"
                                    + " annual_establish_report_at_co.pay_scale_6th_pay, annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type order by pay_scale_7th_pay desc";
                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, cadreType);
                            pst.setString(4, postgrp);
                            pst.setString(5, partTYpe);
                            pst.setString(6, aoOffCode);
                        } else if (!postgrp.equals("D")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                                    + " postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay, pay_scale_6th_pay,"
                                    + " annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type  from aer_report_submit_at_co aer"
                                    + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code"
                                    /*+ " inner join g_spc on aer.dreviewer_spc=g_spc.spc"*/
                                    + " inner join annual_establish_report_at_co on aer.co_aer_id=annual_establish_report_at_co.co_aer_id"
                                    + " where FY=?"
                                    + " AND process_authorization.SPC=?"
                                    + " and ((annual_establish_report_at_co.cadre_code<>'AIS' and annual_establish_report_at_co.cadre_code<>'UGC' and annual_establish_report_at_co.cadre_code<>'OJS') or annual_establish_report_at_co.cadre_code is null)"
                                    + " and post_group=?"
                                    + " and aer_part_type=?"
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='ACCEPTOR'"
                                    + " and aer.ao_off_code=?"
                                    + " group by postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay,"
                                    + " annual_establish_report_at_co.pay_scale_6th_pay, annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type order by pay_scale_7th_pay desc";
                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, postgrp);
                            pst.setString(4, partTYpe);
                            pst.setString(5, aoOffCode);
                        } else if (postgrp.equals("D")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                                    + " postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay, pay_scale_6th_pay,"
                                    + " annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type  from aer_report_submit_at_co aer"
                                    + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code"
                                    /* + " inner join g_spc on aer.dreviewer_spc=g_spc.spc"*/
                                    + " inner join annual_establish_report_at_co on aer.co_aer_id=annual_establish_report_at_co.co_aer_id"
                                    + " where FY=?"
                                    + " AND process_authorization.SPC=?"
                                    + " and ((annual_establish_report_at_co.cadre_code<>'AIS' and annual_establish_report_at_co.cadre_code<>'UGC' and annual_establish_report_at_co.cadre_code<>'OJS') or annual_establish_report_at_co.cadre_code is null)"
                                    + " and (post_group=? or post_group is null)"
                                    + " and aer_part_type=?"
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='ACCEPTOR'"
                                    + " and aer.ao_off_code=?"
                                    + " group by postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay,"
                                    + " annual_establish_report_at_co.pay_scale_6th_pay, annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type order by pay_scale_7th_pay desc";
                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, postgrp);
                            pst.setString(4, partTYpe);
                            pst.setString(5, aoOffCode);
                        }
                    } else if (partTYpe.equalsIgnoreCase("PART-B")) {
                        if (cadreType != null && !cadreType.equals("")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                                    + " postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay, pay_scale_6th_pay,"
                                    + " annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type  from aer_report_submit_at_co aer"
                                    + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code"
                                    /*+ " inner join g_spc on aer.dreviewer_spc=g_spc.spc"*/
                                    + " inner join annual_establish_report_at_co on aer.co_aer_id=annual_establish_report_at_co.co_aer_id"
                                    + " where FY=?"
                                    + " AND process_authorization.SPC=?"
                                    + " and annual_establish_report_at_co.cadre_code=?"
                                    + " and post_group=?"
                                    + " and aer_part_type=?"
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='ACCEPTOR'"
                                    + " and aer.ao_off_code=?"
                                    + " group by postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay,"
                                    + " annual_establish_report_at_co.pay_scale_6th_pay, annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type order by pay_scale_7th_pay desc";
                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, cadreType);
                            pst.setString(4, postgrp);
                            pst.setString(5, partTYpe);
                            pst.setString(6, aoOffCode);
                        } else if (!postgrp.equals("D")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                                    + " postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay, pay_scale_6th_pay,"
                                    + " annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type  from aer_report_submit_at_co aer"
                                    + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code"
                                    /*+ " inner join g_spc on aer.dreviewer_spc=g_spc.spc"*/
                                    + " inner join annual_establish_report_at_co on aer.co_aer_id=annual_establish_report_at_co.co_aer_id"
                                    + " where FY=?"
                                    + " AND process_authorization.SPC=?"
                                    + " and ((annual_establish_report_at_co.cadre_code<>'AIS' and annual_establish_report_at_co.cadre_code<>'UGC' and annual_establish_report_at_co.cadre_code<>'OJS') or annual_establish_report_at_co.cadre_code is null)"
                                    + " and post_group=?"
                                    + " and aer_part_type=?"
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='ACCEPTOR'"
                                    + " and aer.ao_off_code=?"
                                    + " group by postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay,"
                                    + " annual_establish_report_at_co.pay_scale_6th_pay, annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type order by pay_scale_7th_pay desc";
                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, postgrp);
                            pst.setString(4, partTYpe);
                            pst.setString(5, aoOffCode);
                        } else if (postgrp.equals("D")) {
                            sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                                    + " postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay, pay_scale_6th_pay,"
                                    + " annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type  from aer_report_submit_at_co aer"
                                    + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code"
                                    /*+ " inner join g_spc on aer.dreviewer_spc=g_spc.spc"*/
                                    + " inner join annual_establish_report_at_co on aer.co_aer_id=annual_establish_report_at_co.co_aer_id"
                                    + " where FY=?"
                                    + " AND process_authorization.SPC=?"
                                    + " and ((annual_establish_report_at_co.cadre_code<>'AIS' and annual_establish_report_at_co.cadre_code<>'UGC' and annual_establish_report_at_co.cadre_code<>'OJS') or annual_establish_report_at_co.cadre_code is null)"
                                    + " and (post_group=? or post_group is null)"
                                    + " and aer_part_type=?"
                                    + " and process_authorization.process_id=13 and process_authorization.role_type='ACCEPTOR'"
                                    + " and aer.ao_off_code=?"
                                    + " group by postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay,"
                                    + " annual_establish_report_at_co.pay_scale_6th_pay, annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type order by pay_scale_7th_pay desc";
                            pst = con.prepareStatement(sql);
                            pst.setString(1, fy);
                            pst.setString(2, spc);
                            pst.setString(3, postgrp);
                            pst.setString(4, partTYpe);
                            pst.setString(5, aoOffCode);
                        }
                    } else if (partTYpe.equalsIgnoreCase("PART-C")) {
                        sql = "select sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                                + " postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay, pay_scale_6th_pay,"
                                + " annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type from aer_report_submit_at_co aer"
                                + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code"
                                + /*" inner join g_spc on aer.dreviewer_spc=g_spc.spc" +*/ " inner join annual_establish_report_at_co on aer.co_aer_id=annual_establish_report_at_co.co_aer_id"
                                + " where FY=?"
                                + " AND process_authorization.SPC=?"
                                + " and aer_part_type=?"
                                + " and process_authorization.process_id=13 and process_authorization.role_type='ACCEPTOR'"
                                + " and aer.ao_off_code=?"
                                + " group by postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay,"
                                + " annual_establish_report_at_co.pay_scale_6th_pay, annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type order by pay_scale_7th_pay desc";
                        pst = con.prepareStatement(sql);
                        pst.setString(1, fy);
                        pst.setString(2, spc);
                        pst.setString(3, partTYpe);
                        pst.setString(4, aoOffCode);
                    }

                    rs = pst.executeQuery();
                    while (rs.next()) {
                        AnnualEstablishment aer = new AnnualEstablishment();
                        aer.setSanctionedStrength(rs.getInt("sanction_strength"));
                        aer.setMeninPosition(rs.getInt("menin_position"));
                        aer.setVacancyPosition(rs.getInt("vacancy_position"));
                        //aer.setGpc(rs.getString("gpc"));
                        aer.setPostname(rs.getString("postname"));
                        aer.setPostgrp(rs.getString("post_group"));
                        aer.setScaleofPay(rs.getString("pay_scale_6th_pay"));
                        aer.setScaleofPay7th(rs.getString("pay_scale_7th_pay"));
                        aer.setGp(rs.getString("gp"));
                        aer.setCadreType(rs.getString("cadre_code"));
                        aer.setPartType(rs.getString("aer_part_type"));
                        aerList.add(aer);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return aerList;
    }

    @Override
    public AnnualEstablishment[] getAerViewOtherPartForMultipleAO(String fy, String spc, String partTYpe, String roletype, String aooffcode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List<AnnualEstablishment> aeList = new ArrayList<>();
        try {
            con = this.dataSource.getConnection();

            /*String sql = "select sum(sanc_strength) sanc_strength,sum(men_position) men_position,sum(vacancy) vacancy,aer_other_establishment.category,aer_other_establishment.post,aer_other_establishment.payscale_6th,aer_other_establishment.payscale_7th,"
             + " aer_other_establishment.gp,aer_other_establishment.group_name,aer_other_establishment.remarks from aer_report_submit aer"
             + " INNER JOIN process_authorization ON aer.co_off_code=process_authorization.off_code"
             + " inner join g_spc on aer.approver_spc=g_spc.spc"
             + " inner join aer_other_establishment on aer.aer_id=aer_other_establishment.aer_id"
             + " where FY=?"
             + " AND process_authorization.SPC=? and role_type=?"
             + " and part_type=?"
             + " and process_authorization.process_id=13 and is_approver_submitted='Y' and is_reviewer_submitted='Y' and is_verifier_submitted='Y' "
             + " and aer.co_off_code=? group by"
             + " aer_other_establishment.category,aer_other_establishment.post,aer_other_establishment.payscale_6th,aer_other_establishment.payscale_7th,aer_other_establishment.gp,aer_other_establishment.group_name,aer_other_establishment.remarks";
             pst = con.prepareStatement(sql);
             pst.setString(1, fy);
             pst.setString(2, spc);
             pst.setString(3, roletype);
             pst.setString(4, partTYpe);
             pst.setString(5, aooffcode);
             rs = pst.executeQuery();
             while (rs.next()) {
             AnnualEstablishment aer = new AnnualEstablishment();
             aer.setOtherCategory(rs.getString("category"));
             aer.setOtherPost(rs.getString("post"));
             aer.setScaleofPay(rs.getString("payscale_6th"));
             aer.setOther7thPay(rs.getString("payscale_7th"));
             aer.setOtherRemarks(rs.getString("remarks"));
             aer.setOtherSS(rs.getInt("sanc_strength"));
             aer.setOtherVacancy(rs.getInt("vacancy"));
             aer.setMeninPosition((rs.getInt("sanc_strength") - rs.getInt("vacancy")));
             aer.setGroup(rs.getString("group_name"));
             aer.setGp(rs.getString("gp"));
             aeList.add(aer);
             }*/
            String sql = "select sum(sanction_strength) sanc_strength,sum(menin_position) men_position,sum(vacancy_position) vacancy,"
                    + " annual_establish_report_at_co.postname,annual_establish_report_at_co.post_group,annual_establish_report_at_co.pay_scale_7th_pay,annual_establish_report_at_co.pay_scale_6th_pay,"
                    + " annual_establish_report_at_co.gp,annual_establish_report_at_co.remarks from aer_report_submit_at_co aer"
                    + " INNER JOIN process_authorization ON aer.co_off_code=process_authorization.off_code"
                    + " inner join g_spc on aer.acceptor_spc=g_spc.spc"
                    + " inner join annual_establish_report_at_co on aer.co_aer_id=annual_establish_report_at_co.co_aer_id"
                    + " where FY=?"
                    + " AND process_authorization.SPC=? and role_type=?"
                    + " and aer_part_type=?"
                    + " and process_authorization.process_id=13 and verifier_emp_id is not null and verifier_submitted_on is not null"
                    + " and aer.ao_off_code=?"
                    + " group by"
                    + " annual_establish_report_at_co.postname,annual_establish_report_at_co.post_group,annual_establish_report_at_co.pay_scale_7th_pay,annual_establish_report_at_co.pay_scale_6th_pay,"
                    + " annual_establish_report_at_co.gp,annual_establish_report_at_co.remarks";
            pst = con.prepareStatement(sql);
            pst.setString(1, fy);
            pst.setString(2, spc);
            pst.setString(3, roletype);
            pst.setString(4, partTYpe);
            pst.setString(5, aooffcode);
            rs = pst.executeQuery();
            while (rs.next()) {
                AnnualEstablishment aer = new AnnualEstablishment();
                aer.setOtherCategory(rs.getString("postname"));
                aer.setOtherSS(rs.getInt("sanc_strength"));
                //aer.setMeninPosition(rs.getInt("men_position"));
                //aer.setOtherVacancy((rs.getInt("sanc_strength") - rs.getInt("men_position")));
                aer.setGroup(rs.getString("post_group"));
                aer.setGp(rs.getString("gp"));
                aer.setOtherRemarks(rs.getString("remarks"));
                aeList.add(aer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        AnnualEstablishment addrarray[] = aeList.toArray(new AnnualEstablishment[aeList.size()]);
        return addrarray;
    }

    @Override
    public AnnualEstablishment[] getAerViewOtherPartForGIAMultipleAO(String fy, String spc, String partTYpe, String roletype, String aooffcode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List<AnnualEstablishment> aeList = new ArrayList<>();
        try {
            con = this.dataSource.getConnection();

            String sql = " select sum(sanction_strength) sanc_strength,sum(menin_position) men_position,sum(vacancy_position) vacancy,aer_other_establishment.postname,"
                    + " aer_other_establishment.gp,aer_other_establishment.remarks from aer_report_submit_at_co"
                    + " INNER JOIN process_authorization ON aer_report_submit_at_co.ao_off_code=process_authorization.off_code"
                    + " inner join annual_establish_report_at_co aer_other_establishment on aer_report_submit_at_co.co_aer_id=aer_other_establishment.co_aer_id"
                    + " where FY=?"
                    + " AND process_authorization.SPC=? and role_type=?"
                    + " and aer_part_type=?"
                    + " and process_authorization.process_id=13 "
                    + " and aer_report_submit_at_co.ao_off_code=? group by"
                    + " aer_other_establishment.postname,"
                    + " aer_other_establishment.gp,aer_other_establishment.remarks";
            pst = con.prepareStatement(sql);
            pst.setString(1, fy);
            pst.setString(2, spc);
            pst.setString(3, roletype);
            pst.setString(4, partTYpe);
            pst.setString(5, aooffcode);
            rs = pst.executeQuery();
            while (rs.next()) {
                AnnualEstablishment aer = new AnnualEstablishment();
                aer.setOtherPost(rs.getString("postname"));
                aer.setOtherRemarks(rs.getString("remarks"));
                aer.setOtherSS(rs.getInt("sanc_strength"));
                aer.setMeninPosition(rs.getInt("men_position"));
                aer.setOtherVacancy((rs.getInt("sanc_strength") - rs.getInt("men_position")));
                aer.setGp(rs.getString("gp"));
                aeList.add(aer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        AnnualEstablishment addrarray[] = aeList.toArray(new AnnualEstablishment[aeList.size()]);
        return addrarray;
    }

    @Override
    public void insertConsolidatedDataAfterAcceptorSubmitMultipleAO(int aoAerId, String fy, String spc, String aooffCode, String cadreType, String postgrp, String partTYpe) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String sql = "";
        try {
            con = this.dataSource.getConnection();

            if (partTYpe.equalsIgnoreCase("PART-A") || partTYpe.equalsIgnoreCase("PART-B")) {
                if (cadreType != null && !cadreType.equals("")) {
                    sql = "insert into annual_establish_report_at_ao(ao_aer_id, off_code, sanction_strength,menin_position,vacancy_position,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,gp,cadre_code,aer_part_type)"
                            + " select " + aoAerId + ", '" + aooffCode + "' ,sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                            + " '' gpc, postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay, pay_scale_6th_pay,"
                            + " annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type from aer_report_submit_at_co aer"
                            + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code"
                            + " inner join annual_establish_report_at_co on aer.co_aer_id=annual_establish_report_at_co.co_aer_id"
                            + " where FY=?"
                            + " AND process_authorization.SPC=?"
                            + " and annual_establish_report_at_co.cadre_code=?"
                            + " and post_group=?"
                            + " and aer_part_type=?"
                            + " and process_authorization.process_id=13 and process_authorization.role_type='ACCEPTOR'"
                            + " and aer.ao_off_code=?"
                            + " group by postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay,"
                            + " annual_establish_report_at_co.pay_scale_6th_pay, annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, fy);
                    pst.setString(2, spc);
                    pst.setString(3, cadreType);
                    pst.setString(4, postgrp);
                    pst.setString(5, partTYpe);
                    pst.setString(6, aooffCode);
                    pst.executeUpdate();
                } else if (!postgrp.equals("D")) {
                    sql = "insert into annual_establish_report_at_ao(ao_aer_id, off_code, sanction_strength,menin_position,vacancy_position,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,gp,cadre_code,aer_part_type)"
                            + " select " + aoAerId + ", '" + aooffCode + "' ,sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                            + " '' gpc, postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay, pay_scale_6th_pay,"
                            + " annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type  from aer_report_submit_at_co aer"
                            + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code"
                            + " inner join annual_establish_report_at_co on aer.co_aer_id=annual_establish_report_at_co.co_aer_id"
                            + " where FY=?"
                            + " AND process_authorization.SPC=?"
                            + " and ((annual_establish_report_at_co.cadre_code<>'AIS' and annual_establish_report_at_co.cadre_code<>'UGC' and annual_establish_report_at_co.cadre_code<>'OJS') or annual_establish_report_at_co.cadre_code is null)"
                            + " and post_group=?"
                            + " and aer_part_type=?"
                            + " and process_authorization.process_id=13 and process_authorization.role_type='ACCEPTOR'"
                            + " and aer.ao_off_code=?"
                            + " group by postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay,"
                            + " annual_establish_report_at_co.pay_scale_6th_pay, annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, fy);
                    pst.setString(2, spc);
                    pst.setString(3, postgrp);
                    pst.setString(4, partTYpe);
                    pst.setString(5, aooffCode);
                    pst.executeUpdate();
                } else if (postgrp.equals("D")) {
                    sql = "insert into annual_establish_report_at_ao(ao_aer_id, off_code, sanction_strength,menin_position,vacancy_position,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,gp,cadre_code,aer_part_type)"
                            + " select " + aoAerId + ", '" + aooffCode + "' ,sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                            + " '' gpc, postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay, pay_scale_6th_pay,"
                            + " annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type  from aer_report_submit_at_co aer"
                            + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code"
                            + " inner join annual_establish_report_at_co on aer.co_aer_id=annual_establish_report_at_co.co_aer_id"
                            + " where FY=?"
                            + " AND process_authorization.SPC=?"
                            + " and ((annual_establish_report_at_co.cadre_code<>'AIS' and annual_establish_report_at_co.cadre_code<>'UGC' and annual_establish_report_at_co.cadre_code<>'OJS') or annual_establish_report_at_co.cadre_code is null)"
                            + " and (post_group=? or post_group is null)"
                            + " and aer_part_type=?"
                            + " and process_authorization.process_id=13 and process_authorization.role_type='ACCEPTOR'"
                            + " and aer.ao_off_code=?"
                            + " group by postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay,"
                            + " annual_establish_report_at_co.pay_scale_6th_pay, annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, fy);
                    pst.setString(2, spc);
                    pst.setString(3, postgrp);
                    pst.setString(4, partTYpe);
                    pst.setString(5, aooffCode);
                    pst.executeUpdate();
                }
            } else if (partTYpe.equalsIgnoreCase("PART-C")) {
                sql = "insert into annual_establish_report_at_ao(ao_aer_id, off_code, sanction_strength,menin_position,vacancy_position,gpc,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,gp,cadre_code,aer_part_type)"
                        + " select " + aoAerId + ", '" + aooffCode + "' ,sum(sanction_strength) sanction_strength, sum(menin_position) menin_position, sum(vacancy_position) vacancy_position,"
                        + " '' gpc, postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay, pay_scale_6th_pay,"
                        + " annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type  from aer_report_submit_at_co aer"
                        + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code"
                        + " inner join annual_establish_report_at_co on aer.co_aer_id=annual_establish_report_at_co.co_aer_id"
                        + " where FY=?"
                        + " AND process_authorization.SPC=?"
                        + " and ((annual_establish_report_at_co.cadre_code<>'AIS' and annual_establish_report_at_co.cadre_code<>'UGC' and annual_establish_report_at_co.cadre_code<>'OJS') or annual_establish_report_at_co.cadre_code is null)"
                        + " and aer_part_type=?"
                        + " and process_authorization.process_id=13 and process_authorization.role_type='ACCEPTOR'"
                        + " and aer.ao_off_code=?"
                        + " group by postname, post_group, annual_establish_report_at_co.pay_scale_7th_pay,"
                        + " annual_establish_report_at_co.pay_scale_6th_pay, annual_establish_report_at_co.gp, annual_establish_report_at_co.cadre_code, aer_part_type";
                pst = con.prepareStatement(sql);
                pst.setString(1, fy);
                pst.setString(2, spc);
                pst.setString(3, partTYpe);
                pst.setString(4, aooffCode);
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void insertConsolidatedDataAfterAcceptorSubmitForBDEMultipleAO(int aoAerId, String fy, String spc, String aooffCode, String postgrp, String partTYpe) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "insert into annual_establish_report_at_ao(ao_aer_id, off_code, sanction_strength,menin_position,vacancy_position,postname,post_group,pay_scale_7th_pay,pay_scale_6th_pay,gp,aer_part_type)"
                    + " select  " + aoAerId + ",'" + aooffCode + "',sum(sanction_strength) sanc_strength,sum(menin_position) men_position,sum(vacancy_position) vacancy,annual_establish_report_at_co.postname,annual_establish_report_at_co.post_group,"
                    + " annual_establish_report_at_co.pay_scale_7th_pay,annual_establish_report_at_co.pay_scale_6th_pay,"
                    + " annual_establish_report_at_co.gp,aer_part_type from aer_report_submit_at_co aer"
                    + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code"
                    + " inner join annual_establish_report_at_co on aer.co_aer_id=annual_establish_report_at_co.co_aer_id"
                    + " where FY=?"
                    + " AND process_authorization.SPC=?"
                    + " and aer_part_type=?"
                    + " and process_authorization.process_id=13 and process_authorization.role_type='ACCEPTOR'"
                    + " and aer.ao_off_code=?"
                    + " group by"
                    + " annual_establish_report_at_co.postname,annual_establish_report_at_co.post_group,annual_establish_report_at_co.pay_scale_7th_pay,annual_establish_report_at_co.pay_scale_6th_pay,"
                    + " annual_establish_report_at_co.gp,aer_part_type";
            pst = con.prepareStatement(sql);
            pst.setString(1, fy);
            pst.setString(2, spc);
            pst.setString(3, partTYpe);
            pst.setString(4, aooffCode);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getAerReportListFinancialYearWiseForMultipleAOLevel(String aoffCode, String financialYear, String roleType, String loginSpc) {

        Connection con = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        List<AnnualEstablishment> li = new ArrayList<AnnualEstablishment>();
        AnnualEstablishment ae = null;
        int i = 0;
        try {
            con = this.dataSource.getConnection();
            if (roleType.equals("ACCEPTOR")) {
                ps = con.prepareStatement("select co_aer_id, aer.fy, verifier_submitted_on, spn, verifier_spc, off_en,acceptor_emp_id, dreviewer_emp_id, dverifier_emp_id,LVL "
                        + " from aer_report_submit_at_co aer  "
                        + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code  "
                        + " inner join g_spc on aer.verifier_spc=g_spc.spc  "
                        + " inner join g_office on aer.co_off_code=g_office.off_code  "
                        + " where FY=? AND process_authorization.SPC=?  "
                        + " and process_authorization.process_id=? and process_authorization.role_type='ACCEPTOR' and aer.ao_off_code=?");
            } else if (roleType.equals("DREVIEWER")) {
                ps = con.prepareStatement("select co_aer_id, aer.fy, verifier_submitted_on, spn, verifier_spc, off_en,dreviewer_emp_id,dverifier_emp_id,acceptor_emp_id,LVL "
                        + " from aer_report_submit_at_co aer  "
                        + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code  "
                        + " inner join g_spc on aer.verifier_spc=g_spc.spc  "
                        + " inner join g_office on aer.co_off_code=g_office.off_code  "
                        + " where FY=? AND process_authorization.SPC=?  "
                        + " and process_authorization.process_id=? and process_authorization.role_type='DREVIEWER' AND LVL != '01' ");
            } else if (roleType.equals("DVERIFIER")) {
                ps = con.prepareStatement("select co_aer_id, aer.fy, verifier_submitted_on, spn, verifier_spc, off_en,dreviewer_emp_id,dverifier_emp_id,acceptor_emp_id,LVL "
                        + " from aer_report_submit_at_co aer  "
                        + " INNER JOIN process_authorization ON aer.ao_off_code=process_authorization.off_code  "
                        + " inner join g_spc on aer.verifier_spc=g_spc.spc  "
                        + " inner join g_office on aer.co_off_code=g_office.off_code  "
                        + " where FY=? AND process_authorization.SPC=?  "
                        + " and process_authorization.process_id=? and process_authorization.role_type='DVERIFIER'  AND LVL != '01' ");
            }
            if (roleType != null && !roleType.equals("")) {
                ps.setString(1, financialYear);
                ps.setString(2, loginSpc);
                ps.setInt(3, 13);
                ps.setString(4, aoffCode);
                rs = ps.executeQuery();
                while (rs.next()) {
                    i++;

                    ae = new AnnualEstablishment();
                    ae.setSerialno(i);
                    ae.setAerId(rs.getInt("co_aer_id"));
                    ae.setPostname(rs.getString("spn"));
                    ae.setControllingSpc(rs.getString("verifier_spc"));

                    ae.setOperatoroffName(rs.getString("off_en"));
                    ae.setFy(rs.getString("fy"));
                    ae.setSubmittedDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("verifier_submitted_on")));
                    if (roleType.equals("DREVIEWER")) {
                        if (rs.getString("dreviewer_emp_id") == null || rs.getString("dreviewer_emp_id").equals("")) {
                            ae.setShowApproveLink("Y");
                        } else {
                            ae.setShowApproveLink("N");
                        }
                    } else if (roleType.equals("DVERIFIER")) {
                        if (rs.getString("dreviewer_emp_id") != null && (rs.getString("dverifier_emp_id") == null || rs.getString("dverifier_emp_id").equals(""))) {
                            ae.setShowApproveLink("Y");
                        } else {
                            ae.setShowApproveLink("N");
                        }
                    } else if (roleType.equals("ACCEPTOR")) {
                        if (rs.getString("LVL").equals("01")) {
                            if (rs.getString("acceptor_emp_id") != null && !rs.getString("acceptor_emp_id").equals("")) {
                                ae.setShowApproveLink("N");
                            } else {
                                ae.setShowApproveLink("Y");
                                ae.setStatus("PENDING AT SELF");
                            }
                        } else {
                            if (rs.getString("dverifier_emp_id") != null && rs.getString("dreviewer_emp_id") != null && !rs.getString("dreviewer_emp_id").equals("") && (rs.getString("acceptor_emp_id") == null || rs.getString("acceptor_emp_id").equals(""))) {
                                ae.setShowApproveLink("Y");
                            } else {
                                ae.setShowApproveLink("N");
                            }
                        }
                    }
                    li.add(ae);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public int getSchedule3AerIdMultipleAO(String loginSpc, String empId, String fy, String aoffCode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int aoaerid = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "select ao_aer_id from aer_report_submit_at_ao where fy=? and ao_off_code=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, fy);
            pst.setString(2, aoffCode);
            rs = pst.executeQuery();
            if (rs.next()) {
                aoaerid = rs.getInt("ao_aer_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return aoaerid;
    }

    @Override
    public void downloadAERSanctionedStrengthExcel(WritableWorkbook workbook, ArrayList aerdetails, String fyear) {

        int row = 0;
        int slno = 0;

        int totalGroupA = 0;
        int totalGroupB = 0;
        int totalGroupC = 0;
        int totalGroupD = 0;
        int groupTotal = 0;
        int grantInAidTotal = 0;
        int grandTotal = 0;
        try {
            WritableSheet sheet = workbook.createSheet("SANCTIONED_STRENGTH", 0);

            WritableFont headformat = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
            WritableCellFormat headcell = new WritableCellFormat(headformat);
            headcell.setAlignment(Alignment.CENTRE);
            headcell.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell.setWrap(true);
            headcell.setBorder(Border.ALL, BorderLineStyle.THIN);

            WritableFont innerformat = new WritableFont(WritableFont.ARIAL, 12, WritableFont.NO_BOLD);
            WritableCellFormat innercell = new WritableCellFormat(innerformat);
            innercell.setWrap(true);
            innercell.setBorder(Border.ALL, BorderLineStyle.THIN);

            Label label = null;
            Number num = null;

            label = new Label(0, row, "AER SANCTIONED STRENGTH " + fyear, headcell);
            sheet.mergeCells(0, row, 9, row);
            sheet.addCell(label);

            row = row + 1;

            label = new Label(0, row, "SL No", headcell);
            sheet.addCell(label);
            label = new Label(1, row, "Demand\nNo", headcell);
            sheet.setColumnView(1, 12);
            sheet.addCell(label);
            label = new Label(2, row, "Department", headcell);
            sheet.setColumnView(2, 30);
            sheet.addCell(label);
            label = new Label(3, row, "Group A", headcell);
            sheet.addCell(label);
            label = new Label(4, row, "Group B", headcell);
            sheet.addCell(label);
            label = new Label(5, row, "Group C", headcell);
            sheet.addCell(label);
            label = new Label(6, row, "Group D", headcell);
            sheet.addCell(label);
            label = new Label(7, row, "Total(A+B+C+D)", headcell);
            sheet.addCell(label);
            label = new Label(8, row, "Grant in Aid", headcell);
            sheet.addCell(label);
            label = new Label(9, row, "Grant Total", headcell);
            sheet.addCell(label);

            if (aerdetails != null && aerdetails.size() > 0) {
                for (int i = 0; i < aerdetails.size(); i++) {

                    AnnualEstablishment aer = (AnnualEstablishment) aerdetails.get(i);

                    row = row + 1;
                    slno = slno + 1;

                    label = new Label(0, row, slno + "", innercell);
                    sheet.addCell(label);
                    num = new Number(1, row, aer.getDemandNo(), innercell);
                    sheet.setColumnView(1, 12);
                    sheet.addCell(num);
                    label = new Label(2, row, aer.getDepartmentname(), innercell);
                    sheet.setColumnView(2, 30);
                    sheet.addCell(label);
                    num = new Number(3, row, aer.getTotalACnt(), innercell);
                    sheet.addCell(num);
                    num = new Number(4, row, aer.getTotalBCnt(), innercell);
                    sheet.addCell(num);
                    num = new Number(5, row, aer.getTotalCCnt(), innercell);
                    sheet.addCell(num);
                    num = new Number(6, row, aer.getTotalDCnt(), innercell);
                    sheet.addCell(num);
                    num = new Number(7, row, aer.getTotalallCnt(), innercell);
                    sheet.addCell(num);
                    num = new Number(8, row, aer.getGrantinAid(), innercell);
                    sheet.addCell(num);
                    num = new Number(9, row, aer.getGrandTotal(), innercell);
                    sheet.addCell(num);

                    totalGroupA = totalGroupA + aer.getTotalACnt();
                    totalGroupB = totalGroupB + aer.getTotalBCnt();
                    totalGroupC = totalGroupC + aer.getTotalCCnt();
                    totalGroupD = totalGroupD + aer.getTotalDCnt();
                    groupTotal = totalGroupA + totalGroupB + totalGroupC + totalGroupD;
                    grantInAidTotal = grantInAidTotal + aer.getGrantinAid();
                    grandTotal = groupTotal + grantInAidTotal;
                }

                row = row + 1;

                label = new Label(0, row, "", headcell);
                sheet.addCell(label);
                label = new Label(1, row, "", headcell);
                sheet.setColumnView(1, 12);
                sheet.addCell(label);
                label = new Label(2, row, "", headcell);
                sheet.setColumnView(2, 30);
                sheet.addCell(label);
                num = new Number(3, row, totalGroupA, headcell);
                sheet.addCell(num);
                num = new Number(4, row, totalGroupB, headcell);
                sheet.addCell(num);
                num = new Number(5, row, totalGroupC, headcell);
                sheet.addCell(num);
                num = new Number(6, row, totalGroupD, headcell);
                sheet.addCell(num);
                num = new Number(7, row, groupTotal, headcell);
                sheet.addCell(num);
                num = new Number(8, row, grantInAidTotal, headcell);
                sheet.addCell(num);
                num = new Number(9, row, grandTotal, headcell);
                sheet.addCell(num);

            }
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void downloadAERMenInPositionExcel(WritableWorkbook workbook, ArrayList aerdetails, String fyear) {

        int row = 0;
        int slno = 0;

        int totalGroupA = 0;
        int totalGroupB = 0;
        int totalGroupC = 0;
        int totalGroupD = 0;
        int groupTotal = 0;
        int grantInAidTotal = 0;
        int grandTotal = 0;
        try {
            WritableSheet sheet = workbook.createSheet("MEN_IN_POSITION", 0);

            WritableFont headformat = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
            WritableCellFormat headcell = new WritableCellFormat(headformat);
            headcell.setAlignment(Alignment.CENTRE);
            headcell.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell.setWrap(true);
            headcell.setBorder(Border.ALL, BorderLineStyle.THIN);

            WritableFont innerformat = new WritableFont(WritableFont.ARIAL, 12, WritableFont.NO_BOLD);
            WritableCellFormat innercell = new WritableCellFormat(innerformat);
            innercell.setWrap(true);
            innercell.setBorder(Border.ALL, BorderLineStyle.THIN);

            Label label = null;
            Number num = null;

            label = new Label(0, row, "AER MEN IN POSITION " + fyear, headcell);
            sheet.mergeCells(0, row, 9, row);
            sheet.addCell(label);

            row = row + 1;

            label = new Label(0, row, "SL No", headcell);
            sheet.addCell(label);
            label = new Label(1, row, "Demand\nNo", headcell);
            sheet.setColumnView(1, 12);
            sheet.addCell(label);
            label = new Label(2, row, "Department", headcell);
            sheet.setColumnView(2, 30);
            sheet.addCell(label);
            label = new Label(3, row, "Group A", headcell);
            sheet.addCell(label);
            label = new Label(4, row, "Group B", headcell);
            sheet.addCell(label);
            label = new Label(5, row, "Group C", headcell);
            sheet.addCell(label);
            label = new Label(6, row, "Group D", headcell);
            sheet.addCell(label);
            label = new Label(7, row, "Total(A+B+C+D)", headcell);
            sheet.addCell(label);
            label = new Label(8, row, "Grant in Aid", headcell);
            sheet.addCell(label);
            label = new Label(9, row, "Grant Total", headcell);
            sheet.addCell(label);

            if (aerdetails != null && aerdetails.size() > 0) {
                for (int i = 0; i < aerdetails.size(); i++) {

                    AnnualEstablishment aer = (AnnualEstablishment) aerdetails.get(i);

                    row = row + 1;
                    slno = slno + 1;

                    label = new Label(0, row, slno + "", innercell);
                    sheet.addCell(label);
                    num = new Number(1, row, aer.getDemandNo(), innercell);
                    sheet.setColumnView(1, 12);
                    sheet.addCell(num);
                    label = new Label(2, row, aer.getDepartmentname(), innercell);
                    sheet.setColumnView(2, 30);
                    sheet.addCell(label);
                    num = new Number(3, row, aer.getTotalACnt(), innercell);
                    sheet.addCell(num);
                    num = new Number(4, row, aer.getTotalBCnt(), innercell);
                    sheet.addCell(num);
                    num = new Number(5, row, aer.getTotalCCnt(), innercell);
                    sheet.addCell(num);
                    num = new Number(6, row, aer.getTotalDCnt(), innercell);
                    sheet.addCell(num);
                    num = new Number(7, row, aer.getTotalallCnt(), innercell);
                    sheet.addCell(num);
                    num = new Number(8, row, aer.getGrantinAid(), innercell);
                    sheet.addCell(num);
                    num = new Number(9, row, aer.getGrandTotal(), innercell);
                    sheet.addCell(num);

                    totalGroupA = totalGroupA + aer.getTotalACnt();
                    totalGroupB = totalGroupB + aer.getTotalBCnt();
                    totalGroupC = totalGroupC + aer.getTotalCCnt();
                    totalGroupD = totalGroupD + aer.getTotalDCnt();
                    groupTotal = totalGroupA + totalGroupB + totalGroupC + totalGroupD;
                    grantInAidTotal = grantInAidTotal + aer.getGrantinAid();
                    grandTotal = groupTotal + grantInAidTotal;
                }

                row = row + 1;

                label = new Label(0, row, "", headcell);
                sheet.addCell(label);
                label = new Label(1, row, "", headcell);
                sheet.setColumnView(1, 12);
                sheet.addCell(label);
                label = new Label(2, row, "", headcell);
                sheet.setColumnView(2, 30);
                sheet.addCell(label);
                num = new Number(3, row, totalGroupA, headcell);
                sheet.addCell(num);
                num = new Number(4, row, totalGroupB, headcell);
                sheet.addCell(num);
                num = new Number(5, row, totalGroupC, headcell);
                sheet.addCell(num);
                num = new Number(6, row, totalGroupD, headcell);
                sheet.addCell(num);
                num = new Number(7, row, groupTotal, headcell);
                sheet.addCell(num);
                num = new Number(8, row, grantInAidTotal, headcell);
                sheet.addCell(num);
                num = new Number(9, row, grandTotal, headcell);
                sheet.addCell(num);

            }
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void downloadAERVacancyExcel(WritableWorkbook workbook, ArrayList aerdetails, String fyear) {

        int row = 0;
        int slno = 0;

        int totalGroupA = 0;
        int totalGroupB = 0;
        int totalGroupC = 0;
        int totalGroupD = 0;
        int groupTotal = 0;
        int grantInAidTotal = 0;
        int grandTotal = 0;
        try {
            WritableSheet sheet = workbook.createSheet("VACANCY", 0);

            WritableFont headformat = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
            WritableCellFormat headcell = new WritableCellFormat(headformat);
            headcell.setAlignment(Alignment.CENTRE);
            headcell.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell.setWrap(true);
            headcell.setBorder(Border.ALL, BorderLineStyle.THIN);

            WritableFont innerformat = new WritableFont(WritableFont.ARIAL, 12, WritableFont.NO_BOLD);
            WritableCellFormat innercell = new WritableCellFormat(innerformat);
            innercell.setWrap(true);
            innercell.setBorder(Border.ALL, BorderLineStyle.THIN);

            Label label = null;
            Number num = null;

            label = new Label(0, row, "AER VACANCY " + fyear, headcell);
            sheet.mergeCells(0, row, 9, row);
            sheet.addCell(label);

            row = row + 1;

            label = new Label(0, row, "SL No", headcell);
            sheet.addCell(label);
            label = new Label(1, row, "Demand\nNo", headcell);
            sheet.setColumnView(1, 12);
            sheet.addCell(label);
            label = new Label(2, row, "Department", headcell);
            sheet.setColumnView(2, 30);
            sheet.addCell(label);
            label = new Label(3, row, "Group A", headcell);
            sheet.addCell(label);
            label = new Label(4, row, "Group B", headcell);
            sheet.addCell(label);
            label = new Label(5, row, "Group C", headcell);
            sheet.addCell(label);
            label = new Label(6, row, "Group D", headcell);
            sheet.addCell(label);
            label = new Label(7, row, "Total(A+B+C+D)", headcell);
            sheet.addCell(label);
            label = new Label(8, row, "Grant in Aid", headcell);
            sheet.addCell(label);
            label = new Label(9, row, "Grant Total", headcell);
            sheet.addCell(label);

            if (aerdetails != null && aerdetails.size() > 0) {
                for (int i = 0; i < aerdetails.size(); i++) {

                    AnnualEstablishment aer = (AnnualEstablishment) aerdetails.get(i);

                    row = row + 1;
                    slno = slno + 1;

                    label = new Label(0, row, slno + "", innercell);
                    sheet.addCell(label);
                    num = new Number(1, row, aer.getDemandNo(), innercell);
                    sheet.setColumnView(1, 12);
                    sheet.addCell(num);
                    label = new Label(2, row, aer.getDepartmentname(), innercell);
                    sheet.setColumnView(2, 30);
                    sheet.addCell(label);
                    num = new Number(3, row, aer.getTotalACnt(), innercell);
                    sheet.addCell(num);
                    num = new Number(4, row, aer.getTotalBCnt(), innercell);
                    sheet.addCell(num);
                    num = new Number(5, row, aer.getTotalCCnt(), innercell);
                    sheet.addCell(num);
                    num = new Number(6, row, aer.getTotalDCnt(), innercell);
                    sheet.addCell(num);
                    num = new Number(7, row, aer.getTotalallCnt(), innercell);
                    sheet.addCell(num);
                    num = new Number(8, row, aer.getGrantinAid(), innercell);
                    sheet.addCell(num);
                    num = new Number(9, row, aer.getGrandTotal(), innercell);
                    sheet.addCell(num);

                    totalGroupA = totalGroupA + aer.getTotalACnt();
                    totalGroupB = totalGroupB + aer.getTotalBCnt();
                    totalGroupC = totalGroupC + aer.getTotalCCnt();
                    totalGroupD = totalGroupD + aer.getTotalDCnt();
                    groupTotal = totalGroupA + totalGroupB + totalGroupC + totalGroupD;
                    grantInAidTotal = grantInAidTotal + aer.getGrantinAid();
                    grandTotal = groupTotal + grantInAidTotal;
                }

                row = row + 1;

                label = new Label(0, row, "", headcell);
                sheet.addCell(label);
                label = new Label(1, row, "", headcell);
                sheet.setColumnView(1, 12);
                sheet.addCell(label);
                label = new Label(2, row, "", headcell);
                sheet.setColumnView(2, 30);
                sheet.addCell(label);
                num = new Number(3, row, totalGroupA, headcell);
                sheet.addCell(num);
                num = new Number(4, row, totalGroupB, headcell);
                sheet.addCell(num);
                num = new Number(5, row, totalGroupC, headcell);
                sheet.addCell(num);
                num = new Number(6, row, totalGroupD, headcell);
                sheet.addCell(num);
                num = new Number(7, row, groupTotal, headcell);
                sheet.addCell(num);
                num = new Number(8, row, grantInAidTotal, headcell);
                sheet.addCell(num);
                num = new Number(9, row, grandTotal, headcell);
                sheet.addCell(num);

            }
            workbook.write();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void disApproveAER(int aerid, String empid, String spc, String offcode, String selectedOffCode, String roleType) {

        Connection con = null;

        PreparedStatement ps2 = null;
        ResultSet rs = null;
        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();

            if (roleType.equals("BO")) {
                ps2 = con.prepareStatement("SELECT * FROM aer_report_submit WHERE aer_id=?");
                ps2.setInt(1, aerid);
                rs = ps2.executeQuery();
                if (rs.next()) {
                    if (rs.getString("is_reviewer_submitted").equalsIgnoreCase("N")) {
                        roleType = "REVIEWER";
                    } else if (rs.getString("is_verifier_submitted").equalsIgnoreCase("N")) {
                        roleType = "VERIFIER";
                    } else if (rs.getString("is_reviewer_submitted").equalsIgnoreCase("Y") && rs.getString("is_verifier_submitted").equalsIgnoreCase("N")) {
                        roleType = "VERIFIER";
                    } else if (rs.getString("is_reviewer_submitted").equalsIgnoreCase("Y") && rs.getString("is_verifier_submitted").equalsIgnoreCase("Y")) {
                        roleType = "VERIFIER";
                    }
                }
            } else if (roleType.equals("AOTR") || roleType.equals("AOBO")) {
                ps2 = con.prepareStatement("SELECT * FROM aer_report_submit_at_co WHERE co_aer_id=?");
                ps2.setInt(1, aerid);
                rs = ps2.executeQuery();
                if (rs.next()) {
                    if ((rs.getString("dreviewer_emp_id") != null && !rs.getString("dreviewer_emp_id").equals("")) && (rs.getString("dverifier_emp_id") == null || rs.getString("dverifier_emp_id").equals(""))) {
                        roleType = "DREVIEWER";
                    } else if (rs.getString("dverifier_emp_id") != null && !rs.getString("dverifier_emp_id").equals("")) {
                        roleType = "DVERIFIER";
                    } else if ((rs.getString("dreviewer_emp_id") != null && !rs.getString("dreviewer_emp_id").equals("")) && (rs.getString("dverifier_emp_id") != null && !rs.getString("dverifier_emp_id").equals(""))) {
                        roleType = "ACCEPTOR";
                    }
                }
            }

            String startTime = "";
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            startTime = dateFormat.format(cal.getTime());
            if (roleType != null && roleType.equals("APPROVER")) {
                pst = con.prepareStatement("update aer_report_submit set is_approver_submitted=?, approver_emp_id=?, approver_spc=?, approver_submitted_on=?, status=?,co_off_code=? where  aer_id=?");
                pst.setString(1, "N");
                pst.setString(2, null);
                pst.setString(3, null);
                pst.setTimestamp(4, null);
                pst.setString(5, "PENDING AT OPERATOR");
                pst.setString(6, null);
                //pst.setString(7, offcode);
                pst.setInt(7, aerid);
                pst.execute();
            } else if (roleType != null && roleType.equals("REVIEWER")) {
                pst = con.prepareStatement("update aer_report_submit set is_reviewer_submitted=?, reviewer_emp_id=?, reviewer_spc=?, reviewer_submitted_on=?, status=? where  aer_id=?");
                pst.setString(1, "N");
                pst.setString(2, null);
                pst.setString(3, null);
                pst.setTimestamp(4, null);
                pst.setString(5, "APPROVED");
                //pst.setString(6, offcode);
                pst.setInt(6, aerid);
                pst.execute();
            } else if (roleType != null && roleType.equals("VERIFIER")) {
                pst = con.prepareStatement("update aer_report_submit set is_verifier_submitted=?, verifier_emp_id=?, verifier_spc=?, verifier_submitted_on=?, status=? where aer_id=?");
                pst.setString(1, "N");
                pst.setString(2, null);
                pst.setString(3, null);
                pst.setTimestamp(4, null);
                pst.setString(5, "REVIEWED");
                //pst.setString(6, offcode);
                pst.setInt(6, aerid);
                pst.execute();
            } else if (roleType != null && roleType.equals("DREVIEWER")) {
                pst = con.prepareStatement("update aer_report_submit_at_co set dreviewer_emp_id=?, dreviewer_spc=?, dreviewer_submitted_on=? where co_aer_id=?");
                pst.setString(1, null);
                pst.setString(2, null);
                pst.setTimestamp(3, null);
                pst.setInt(4, aerid);
                pst.execute();
            } else if (roleType != null && roleType.equals("DVERIFIER")) {
                pst = con.prepareStatement("update aer_report_submit_at_co set dverifier_emp_id=?, dverifier_spc=?, dverifier_submitted_on=? where co_aer_id=?");
                pst.setString(1, null);
                pst.setString(2, null);
                pst.setTimestamp(3, null);
                pst.setInt(4, aerid);
                pst.execute();
            } else if (roleType != null && roleType.equals("ACCEPTOR")) {

                pst = con.prepareStatement("update aer_report_submit set is_acceptor_submitted=?, acceptor_emp_id=?, acceptor_spc=?, acceptor_submitted_on=?, status=? where co_aer_id=?");
                pst.setString(1, "N");
                pst.setString(2, null);
                pst.setString(3, null);
                pst.setTimestamp(4, null);
                pst.setString(5, "PENDING AT ACCEPTOR");
                pst.setInt(6, aerid);
                pst.execute();

                pst = con.prepareStatement("update aer_report_submit_at_co set acceptor_emp_id=?, acceptor_spc=?, acceptor_submitted_on=? where co_aer_id=?");
                pst.setString(1, null);
                pst.setString(2, null);
                pst.setTimestamp(3, null);
                pst.setInt(4, aerid);
                pst.execute();
            }
            DataBaseFunctions.closeSqlObjects(pst);

            String sql = "INSERT INTO aer_comm_log(aer_id,action_date,action_taken_by_empid,action_taken_by_spc,message_if_any,user_type) values(?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setInt(1, aerid);
            pst.setTimestamp(2, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.setString(3, empid);
            pst.setString(4, spc);
            pst.setString(5, "DISAPPROVED");
            pst.setString(6, roleType);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(ps2);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public List getAERListStatus(String offcode, String fy) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List aerstatuslist = new ArrayList();

        AnnualEstablishment ae = null;

        try {
            con = this.repodataSource.getConnection();

            String sql = "select aer_id,status from aer_report_submit where off_code=? and fy=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, offcode);
            pst.setString(2, fy);
            rs = pst.executeQuery();
            while (rs.next()) {
                ae = new AnnualEstablishment();
                ae.setAerId(rs.getInt("aer_id"));
                if (rs.getString("status") != null && !rs.getString("status").equals("")) {
                    ae.setStatus(rs.getString("status"));
                } else {
                    ae.setStatus("NOT SUBMITTED");
                }
                aerstatuslist.add(ae);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return aerstatuslist;
    }

    @Override
    public List getAerScheduleIListForAOLevel(int coaerid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List<AnnualEstablishment> aerlist = new ArrayList<AnnualEstablishment>();
        AnnualEstablishment ae = null;
        int i = 0;

        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("SELECT * from aer_report_submit"
                    + " inner join g_office on aer_report_submit.off_code=g_office.off_code"
                    + " WHERE co_aer_id=?");
            pst.setInt(1, coaerid);
            rs = pst.executeQuery();
            while (rs.next()) {
                i++;

                ae = new AnnualEstablishment();
                ae.setSerialno(i);
                ae.setAerId(rs.getInt("aer_id"));
                ae.setSubmittedDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("reviewer_submitted_on")));
                if (rs.getString("is_verifier_submitted").equalsIgnoreCase("N")) {
                    ae.setStatus("PENDING AT SELF");
                } else if (rs.getString("is_verifier_submitted").equalsIgnoreCase("Y") && (rs.getString("co_aer_id") == null || rs.getString("co_aer_id").equals(""))) {
                    ae.setSubmittedDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("verifier_submitted_on")));
                    ae.setStatus("APPROVED BY VERIFIER");
                } else if (rs.getString("is_acceptor_submitted").equalsIgnoreCase("N") && rs.getInt("co_aer_id") > 0) {
                    ae.setSubmittedDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("verifier_submitted_on")));
                    ae.setStatus("PENDING AT ACCEPTOR END");
                } else if (rs.getString("is_acceptor_submitted").equalsIgnoreCase("Y")) {
                    ae.setStatus("ACCEPTED");
                    ae.setSubmittedDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("acceptor_submitted_on")));
                }
                ae.setDdoCode(rs.getString("ddo_code"));
                ae.setOperatoroffName(rs.getString("off_en"));
                ae.setFy(rs.getString("fy"));

                aerlist.add(ae);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return aerlist;
    }

    @Override
    public ArrayList viewCODeptWiseVacancy(String dcode, String fy) {

        Connection con = null;

        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;

        ResultSet rs1 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;

        ArrayList outList = new ArrayList();

        String stm2, stm3 = null;

        try {
            con = this.repodataSource.getConnection();

            String sql = "SELECT off_code,off_en FROM g_office WHERE (lvl=? OR lvl=?) AND g_office.is_ddo='Y' AND g_office.department_code=? order by off_en ";
            ps1 = con.prepareStatement(sql);
            ps1.setString(1, "01");
            ps1.setString(2, "02");
            ps1.setString(3, dcode);
            rs1 = ps1.executeQuery();
            while (rs1.next()) {
                AnnualEstablishment aer2 = new AnnualEstablishment();
                aer2.setOffCode(rs1.getString("off_code"));
                aer2.setOffName(rs1.getString("off_en"));
                String offCode = rs1.getString("off_code");

                int groupA = 0;
                int groupB = 0;
                int groupC = 0;
                int groupD = 0;

                int partD = 0;
                int partE = 0;

                int grantidaid = 0;
                int totalGroup = 0;

                stm2 = "SELECT co_aer_id FROM aer_report_submit_at_co WHERE co_off_code=? AND fy=? AND ao_off_code is NOT NULL  ";
                ps2 = con.prepareStatement(stm2);
                ps2.setString(1, offCode);
                ps2.setString(2, fy);
                rs2 = ps2.executeQuery();
                String Status = "N";
                int aerId = 0;
                if (rs2.next()) {
                    if (rs2.getInt("co_aer_id") > 0) {
                        Status = "Y";
                    }
                    aer2.setCoStatus(Status);
                    aerId = rs2.getInt("co_aer_id");
                    aer2.setAerId(aerId);

                    stm3 = "select post_group,SUM(sanction_strength) sancstrength,SUM(menin_position) meninposition,SUM(vacancy_position) vacancy_position from annual_establish_report_at_co where co_aer_id=? and aer_part_type = 'PART-A' group by post_group";
                    ps3 = con.prepareStatement(stm3);
                    ps3.setInt(1, aerId);
                    rs3 = ps3.executeQuery();
                    while (rs3.next()) {
                        if (rs3.getString("post_group") != null && rs3.getString("post_group").equals("A")) {
                            groupA = rs3.getInt("vacancy_position");
                        }
                        if (rs3.getString("post_group") != null && rs3.getString("post_group").equals("B")) {
                            groupB = rs3.getInt("vacancy_position");
                        }
                        if (rs3.getString("post_group") != null && rs3.getString("post_group").equals("C")) {
                            groupC = rs3.getInt("vacancy_position");
                        }
                        if (rs3.getString("post_group") != null && rs3.getString("post_group").equals("D")) {
                            groupD = rs3.getInt("vacancy_position");
                        }

                        aer2.setTotalACnt(groupA);
                        aer2.setTotalBCnt(groupB);
                        aer2.setTotalCCnt(groupC);
                        aer2.setTotalDCnt(groupD);

                        totalGroup = groupA + groupB + groupC + groupD;
                        aer2.setTotalallCnt(totalGroup);
                    }

                    DataBaseFunctions.closeSqlObjects(rs3, ps3);

                    stm3 = "select SUM(sanction_strength) sancstrength,SUM(menin_position) meninposition,SUM(vacancy_position) vacancy_position from aer_report_submit_at_co ARS"
                            + " INNER JOIN annual_establish_report_at_co AER ON ARS.co_aer_id = AER.co_aer_id "
                            + " WHERE ARS.co_aer_id=? AND post_group is not null and (aer_part_type = 'PART-B' OR aer_part_type = 'B')";
                    ps3 = con.prepareStatement(stm3);
                    ps3.setInt(1, aerId);
                    rs3 = ps3.executeQuery();
                    if (rs3.next()) {
                        grantidaid = rs3.getInt("vacancy_position");
                    }
                    aer2.setGrantinAid(grantidaid);

                    DataBaseFunctions.closeSqlObjects(rs3, ps3);

                    stm3 = "select SUM(sanction_strength) sancstrength,SUM(menin_position) meninposition,SUM(vacancy_position) vacancy_position from aer_report_submit_at_co ARS"
                            + " INNER JOIN annual_establish_report_at_co AER ON ARS.co_aer_id = AER.co_aer_id "
                            + " WHERE ARS.co_aer_id=? AND post_group is not null and (aer_part_type = 'PART-D' OR aer_part_type = 'D')";
                    ps3 = con.prepareStatement(stm3);
                    ps3.setInt(1, aerId);
                    rs3 = ps3.executeQuery();
                    if (rs3.next()) {
                        partD = rs3.getInt("vacancy_position");
                    }
                    aer2.setPartD(partD);

                    DataBaseFunctions.closeSqlObjects(rs3, ps3);

                    stm3 = "select SUM(sanction_strength) sancstrength,SUM(menin_position) meninposition,SUM(vacancy_position) vacancy_position from aer_report_submit_at_co ARS"
                            + " INNER JOIN annual_establish_report_at_co AER ON ARS.co_aer_id = AER.co_aer_id "
                            + " WHERE ARS.co_aer_id=? AND post_group is not null and (aer_part_type = 'PART-E' OR aer_part_type = 'E')";
                    ps3 = con.prepareStatement(stm3);
                    ps3.setInt(1, aerId);
                    rs3 = ps3.executeQuery();
                    if (rs3.next()) {
                        partE = rs3.getInt("vacancy_position");
                    }
                    aer2.setPartE(partE);
                }
                int grandTotal = grantidaid + totalGroup + partE + partD;
                aer2.setGrandTotal(grandTotal);

                outList.add(aer2);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs1, rs2, rs3);
            DataBaseFunctions.closeSqlObjects(ps1, ps2, ps3);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outList;
    }

    @Override
    public ArrayList viewDDODeptWiseVacancy(String offcode, String coaerid) {

        Connection con = null;

        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;

        ResultSet rs1 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;

        ArrayList outList = new ArrayList();

        String stm2, stm3 = null;

        try {
            con = this.repodataSource.getConnection();

            String sql = "SELECT off_code,off_en FROM ddo_to_co_mapping inner join g_office on ddo_to_co_mapping.ddo_off_code=g_office.off_code"
                    + " WHERE ddo_to_co_mapping.co_off_code=?";
            ps1 = con.prepareStatement(sql);
            ps1.setString(1, offcode);
            rs1 = ps1.executeQuery();
            while (rs1.next()) {
                AnnualEstablishment aer2 = new AnnualEstablishment();
                aer2.setOffCode(rs1.getString("off_code"));
                aer2.setOffName(rs1.getString("off_en"));
                String offCode = rs1.getString("off_code");

                int groupA = 0;
                int groupB = 0;
                int groupC = 0;
                int groupD = 0;

                int partD = 0;
                int partE = 0;

                int grantidaid = 0;
                int totalGroup = 0;

                stm2 = "SELECT aer_id FROM aer_report_submit WHERE off_code=? AND co_aer_id=? AND co_off_code is NOT NULL";
                ps2 = con.prepareStatement(stm2);
                ps2.setString(1, offCode);
                ps2.setInt(2, Integer.parseInt(coaerid));
                rs2 = ps2.executeQuery();
                String Status = "N";
                int aerId = 0;
                if (rs2.next()) {
                    if (rs2.getInt("aer_id") > 0) {
                        Status = "Y";
                    }
                    aer2.setCoStatus(Status);
                    aerId = rs2.getInt("aer_id");
                    aer2.setAerId(aerId);

                    stm3 = "select post_group,SUM(sanction_strength) sancstrength,SUM(menin_position) meninposition,SUM(vacancy_position) vacancy_position from annual_establish_report where aer_id=? and aer_part_type = 'PART-A' group by post_group";
                    ps3 = con.prepareStatement(stm3);
                    ps3.setInt(1, aerId);
                    rs3 = ps3.executeQuery();
                    while (rs3.next()) {
                        if (rs3.getString("post_group") != null && rs3.getString("post_group").equals("A")) {
                            groupA = rs3.getInt("vacancy_position");
                        }
                        if (rs3.getString("post_group") != null && rs3.getString("post_group").equals("B")) {
                            groupB = rs3.getInt("vacancy_position");
                        }
                        if (rs3.getString("post_group") != null && rs3.getString("post_group").equals("C")) {
                            groupC = rs3.getInt("vacancy_position");
                        }
                        if (rs3.getString("post_group") != null && rs3.getString("post_group").equals("D")) {
                            groupD = rs3.getInt("vacancy_position");
                        }

                        aer2.setTotalACnt(groupA);
                        aer2.setTotalBCnt(groupB);
                        aer2.setTotalCCnt(groupC);
                        aer2.setTotalDCnt(groupD);

                        totalGroup = groupA + groupB + groupC + groupD;
                        aer2.setTotalallCnt(totalGroup);
                    }

                    DataBaseFunctions.closeSqlObjects(rs3, ps3);

                    stm3 = "select SUM(sanction_strength) sancstrength,SUM(menin_position) meninposition,SUM(vacancy_position) vacancy_position from aer_report_submit ARS"
                            + " INNER JOIN annual_establish_report AER ON ARS.aer_id = AER.aer_id "
                            + " WHERE ARS.aer_id=? AND post_group is not null and (aer_part_type = 'PART-B' OR aer_part_type = 'B')";
                    ps3 = con.prepareStatement(stm3);
                    ps3.setInt(1, aerId);
                    rs3 = ps3.executeQuery();
                    if (rs3.next()) {
                        grantidaid = rs3.getInt("vacancy_position");
                    }
                    aer2.setGrantinAid(grantidaid);

                    DataBaseFunctions.closeSqlObjects(rs3, ps3);

                    stm3 = "select SUM(sanction_strength) sancstrength,SUM(menin_position) meninposition,SUM(vacancy_position) vacancy_position from aer_report_submit ARS"
                            + " INNER JOIN annual_establish_report AER ON ARS.aer_id = AER.aer_id "
                            + " WHERE ARS.aer_id=? AND post_group is not null and (aer_part_type = 'PART-D' OR aer_part_type = 'D')";
                    ps3 = con.prepareStatement(stm3);
                    ps3.setInt(1, aerId);
                    rs3 = ps3.executeQuery();
                    if (rs3.next()) {
                        partD = rs3.getInt("vacancy_position");
                    }
                    aer2.setPartD(partD);

                    DataBaseFunctions.closeSqlObjects(rs3, ps3);

                    stm3 = "select SUM(sanction_strength) sancstrength,SUM(menin_position) meninposition,SUM(vacancy_position) vacancy_position from aer_report_submit ARS"
                            + " INNER JOIN annual_establish_report AER ON ARS.aer_id = AER.aer_id "
                            + " WHERE ARS.aer_id=? AND post_group is not null and (aer_part_type = 'PART-E' OR aer_part_type = 'E')";
                    ps3 = con.prepareStatement(stm3);
                    ps3.setInt(1, aerId);
                    rs3 = ps3.executeQuery();
                    if (rs3.next()) {
                        partE = rs3.getInt("vacancy_position");
                    }
                    aer2.setPartE(partE);
                }
                int grandTotal = grantidaid + totalGroup + partE + partD;
                aer2.setGrandTotal(grandTotal);

                outList.add(aer2);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs1, rs2, rs3);
            DataBaseFunctions.closeSqlObjects(ps1, ps2, ps3);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outList;
    }

    @Override
    public ArrayList viewDDODeptWiseVacancyPostList(String postgroup, String parttype, String aerid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        AnnualEstablishment aer = null;

        ArrayList postList = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            String sql = "select postname,vacancy_position from annual_establish_report where post_group=? and aer_part_type=? and aer_id=? ORDER BY postname";
            pst = con.prepareStatement(sql);
            pst.setString(1, postgroup);
            pst.setString(2, "PART-" + parttype);
            pst.setInt(3, Integer.parseInt(aerid));
            rs = pst.executeQuery();
            while (rs.next()) {
                aer = new AnnualEstablishment();
                aer.setPostname(rs.getString("postname"));
                aer.setTotalEmp(rs.getInt("vacancy_position"));
                postList.add(aer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return postList;
    }

    @Override
    public ArrayList viewDDODeptWiseSancStrength(String offcode, String coaerid) {

        Connection con = null;

        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;

        ResultSet rs1 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;

        ArrayList outList = new ArrayList();

        String stm2, stm3 = null;

        try {
            con = this.repodataSource.getConnection();

            String sql = "SELECT off_code,off_en FROM ddo_to_co_mapping inner join g_office on ddo_to_co_mapping.ddo_off_code=g_office.off_code"
                    + " WHERE ddo_to_co_mapping.co_off_code=?";
            ps1 = con.prepareStatement(sql);
            ps1.setString(1, offcode);
            rs1 = ps1.executeQuery();
            while (rs1.next()) {
                AnnualEstablishment aer2 = new AnnualEstablishment();
                aer2.setOffCode(rs1.getString("off_code"));
                aer2.setOffName(rs1.getString("off_en"));
                String offCode = rs1.getString("off_code");

                int groupA = 0;
                int groupB = 0;
                int groupC = 0;
                int groupD = 0;

                int partD = 0;
                int partE = 0;

                int grantidaid = 0;
                int totalGroup = 0;

                stm2 = "SELECT aer_id FROM aer_report_submit WHERE off_code=? AND co_aer_id=? AND co_off_code is NOT NULL";
                ps2 = con.prepareStatement(stm2);
                ps2.setString(1, offCode);
                ps2.setInt(2, Integer.parseInt(coaerid));
                rs2 = ps2.executeQuery();
                String Status = "N";
                int aerId = 0;
                if (rs2.next()) {
                    if (rs2.getInt("aer_id") > 0) {
                        Status = "Y";
                    }
                    aer2.setCoStatus(Status);
                    aerId = rs2.getInt("aer_id");
                    aer2.setAerId(aerId);

                    stm3 = "select post_group,SUM(sanction_strength) sancstrength from annual_establish_report where aer_id=? and aer_part_type = 'PART-A' group by post_group";
                    ps3 = con.prepareStatement(stm3);
                    ps3.setInt(1, aerId);
                    rs3 = ps3.executeQuery();
                    while (rs3.next()) {
                        if (rs3.getString("post_group") != null && rs3.getString("post_group").equals("A")) {
                            groupA = rs3.getInt("sancstrength");
                        }
                        if (rs3.getString("post_group") != null && rs3.getString("post_group").equals("B")) {
                            groupB = rs3.getInt("sancstrength");
                        }
                        if (rs3.getString("post_group") != null && rs3.getString("post_group").equals("C")) {
                            groupC = rs3.getInt("sancstrength");
                        }
                        if (rs3.getString("post_group") != null && rs3.getString("post_group").equals("D")) {
                            groupD = rs3.getInt("sancstrength");
                        }

                        aer2.setTotalACnt(groupA);
                        aer2.setTotalBCnt(groupB);
                        aer2.setTotalCCnt(groupC);
                        aer2.setTotalDCnt(groupD);

                        totalGroup = groupA + groupB + groupC + groupD;
                        aer2.setTotalallCnt(totalGroup);
                    }

                    DataBaseFunctions.closeSqlObjects(rs3, ps3);

                    stm3 = "select SUM(sanction_strength) sancstrength from aer_report_submit ARS"
                            + " INNER JOIN annual_establish_report AER ON ARS.aer_id = AER.aer_id "
                            + " WHERE ARS.aer_id=? AND post_group is not null and (aer_part_type = 'PART-B' OR aer_part_type = 'B')";
                    ps3 = con.prepareStatement(stm3);
                    ps3.setInt(1, aerId);
                    rs3 = ps3.executeQuery();
                    if (rs3.next()) {
                        grantidaid = rs3.getInt("sancstrength");
                    }
                    aer2.setGrantinAid(grantidaid);

                    DataBaseFunctions.closeSqlObjects(rs3, ps3);

                    stm3 = "select SUM(sanction_strength) sancstrength from aer_report_submit ARS"
                            + " INNER JOIN annual_establish_report AER ON ARS.aer_id = AER.aer_id "
                            + " WHERE ARS.aer_id=? AND post_group is not null and (aer_part_type = 'PART-D' OR aer_part_type = 'D')";
                    ps3 = con.prepareStatement(stm3);
                    ps3.setInt(1, aerId);
                    rs3 = ps3.executeQuery();
                    if (rs3.next()) {
                        partD = rs3.getInt("sancstrength");
                    }
                    aer2.setPartD(partD);

                    DataBaseFunctions.closeSqlObjects(rs3, ps3);

                    stm3 = "select SUM(sanction_strength) sancstrength from aer_report_submit ARS"
                            + " INNER JOIN annual_establish_report AER ON ARS.aer_id = AER.aer_id "
                            + " WHERE ARS.aer_id=? AND post_group is not null and (aer_part_type = 'PART-E' OR aer_part_type = 'E')";
                    ps3 = con.prepareStatement(stm3);
                    ps3.setInt(1, aerId);
                    rs3 = ps3.executeQuery();
                    if (rs3.next()) {
                        partE = rs3.getInt("sancstrength");
                    }
                    aer2.setPartE(partE);
                }
                int grandTotal = grantidaid + totalGroup + partE + partD;
                aer2.setGrandTotal(grandTotal);

                outList.add(aer2);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs1, rs2, rs3);
            DataBaseFunctions.closeSqlObjects(ps1, ps2, ps3);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outList;
    }

    @Override
    public ArrayList viewDDODeptWiseSancStrengthPostList(String postgroup, String parttype, String aerid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        AnnualEstablishment aer = null;

        ArrayList postList = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            String sql = "select postname,sanction_strength from annual_establish_report where post_group=? and aer_part_type=? and aer_id=? ORDER BY postname";
            pst = con.prepareStatement(sql);
            pst.setString(1, postgroup);
            pst.setString(2, "PART-" + parttype);
            pst.setInt(3, Integer.parseInt(aerid));
            rs = pst.executeQuery();
            while (rs.next()) {
                aer = new AnnualEstablishment();
                aer.setPostname(rs.getString("postname"));
                aer.setTotalEmp(rs.getInt("sanction_strength"));
                postList.add(aer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return postList;
    }

    @Override
    public ArrayList viewDDODeptWiseMIP(String offcode, String coaerid) {

        Connection con = null;

        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;

        ResultSet rs1 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;

        ArrayList outList = new ArrayList();

        String stm2, stm3 = null;

        try {
            con = this.repodataSource.getConnection();

            String sql = "SELECT off_code,off_en FROM ddo_to_co_mapping inner join g_office on ddo_to_co_mapping.ddo_off_code=g_office.off_code"
                    + " WHERE ddo_to_co_mapping.co_off_code=?";
            ps1 = con.prepareStatement(sql);
            ps1.setString(1, offcode);
            rs1 = ps1.executeQuery();
            while (rs1.next()) {
                AnnualEstablishment aer2 = new AnnualEstablishment();
                aer2.setOffCode(rs1.getString("off_code"));
                aer2.setOffName(rs1.getString("off_en"));
                String offCode = rs1.getString("off_code");

                int groupA = 0;
                int groupB = 0;
                int groupC = 0;
                int groupD = 0;

                int partD = 0;
                int partE = 0;

                int grantidaid = 0;
                int totalGroup = 0;

                stm2 = "SELECT aer_id FROM aer_report_submit WHERE off_code=? AND co_aer_id=? AND co_off_code is NOT NULL";
                ps2 = con.prepareStatement(stm2);
                ps2.setString(1, offCode);
                ps2.setInt(2, Integer.parseInt(coaerid));
                rs2 = ps2.executeQuery();
                String Status = "N";
                int aerId = 0;
                if (rs2.next()) {
                    if (rs2.getInt("aer_id") > 0) {
                        Status = "Y";
                    }
                    aer2.setCoStatus(Status);
                    aerId = rs2.getInt("aer_id");
                    aer2.setAerId(aerId);

                    stm3 = "select post_group,SUM(menin_position) meninposition from annual_establish_report where aer_id=? and aer_part_type = 'PART-A' group by post_group";
                    ps3 = con.prepareStatement(stm3);
                    ps3.setInt(1, aerId);
                    rs3 = ps3.executeQuery();
                    while (rs3.next()) {
                        if (rs3.getString("post_group") != null && rs3.getString("post_group").equals("A")) {
                            groupA = rs3.getInt("meninposition");
                        }
                        if (rs3.getString("post_group") != null && rs3.getString("post_group").equals("B")) {
                            groupB = rs3.getInt("meninposition");
                        }
                        if (rs3.getString("post_group") != null && rs3.getString("post_group").equals("C")) {
                            groupC = rs3.getInt("meninposition");
                        }
                        if (rs3.getString("post_group") != null && rs3.getString("post_group").equals("D")) {
                            groupD = rs3.getInt("meninposition");
                        }

                        aer2.setTotalACnt(groupA);
                        aer2.setTotalBCnt(groupB);
                        aer2.setTotalCCnt(groupC);
                        aer2.setTotalDCnt(groupD);

                        totalGroup = groupA + groupB + groupC + groupD;
                        aer2.setTotalallCnt(totalGroup);
                    }

                    DataBaseFunctions.closeSqlObjects(rs3, ps3);

                    stm3 = "select SUM(menin_position) meninposition from aer_report_submit ARS"
                            + " INNER JOIN annual_establish_report AER ON ARS.aer_id = AER.aer_id "
                            + " WHERE ARS.aer_id=? AND post_group is not null and (aer_part_type = 'PART-B' OR aer_part_type = 'B')";
                    ps3 = con.prepareStatement(stm3);
                    ps3.setInt(1, aerId);
                    rs3 = ps3.executeQuery();
                    if (rs3.next()) {
                        grantidaid = rs3.getInt("meninposition");
                    }
                    aer2.setGrantinAid(grantidaid);

                    DataBaseFunctions.closeSqlObjects(rs3, ps3);

                    stm3 = "select SUM(menin_position) meninposition from aer_report_submit ARS"
                            + " INNER JOIN annual_establish_report AER ON ARS.aer_id = AER.aer_id "
                            + " WHERE ARS.aer_id=? AND post_group is not null and (aer_part_type = 'PART-D' OR aer_part_type = 'D')";
                    ps3 = con.prepareStatement(stm3);
                    ps3.setInt(1, aerId);
                    rs3 = ps3.executeQuery();
                    if (rs3.next()) {
                        partD = rs3.getInt("meninposition");
                    }
                    aer2.setPartD(partD);

                    DataBaseFunctions.closeSqlObjects(rs3, ps3);

                    stm3 = "select SUM(menin_position) meninposition from aer_report_submit ARS"
                            + " INNER JOIN annual_establish_report AER ON ARS.aer_id = AER.aer_id "
                            + " WHERE ARS.aer_id=? AND post_group is not null and (aer_part_type = 'PART-E' OR aer_part_type = 'E')";
                    ps3 = con.prepareStatement(stm3);
                    ps3.setInt(1, aerId);
                    rs3 = ps3.executeQuery();
                    if (rs3.next()) {
                        partE = rs3.getInt("meninposition");
                    }
                    aer2.setPartE(partE);
                }
                int grandTotal = grantidaid + totalGroup + partE + partD;
                aer2.setGrandTotal(grandTotal);

                outList.add(aer2);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs1, rs2, rs3);
            DataBaseFunctions.closeSqlObjects(ps1, ps2, ps3);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outList;
    }

    @Override
    public ArrayList viewDDODeptWiseMIPPostList(String postgroup, String parttype, String aerid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        AnnualEstablishment aer = null;

        ArrayList postList = new ArrayList();
        try {
            con = this.repodataSource.getConnection();

            String sql = "select postname,menin_position from annual_establish_report where post_group=? and aer_part_type=? and aer_id=? ORDER BY postname";
            pst = con.prepareStatement(sql);
            pst.setString(1, postgroup);
            pst.setString(2, "PART-" + parttype);
            pst.setInt(3, Integer.parseInt(aerid));
            rs = pst.executeQuery();
            while (rs.next()) {
                aer = new AnnualEstablishment();
                aer.setPostname(rs.getString("postname"));
                aer.setTotalEmp(rs.getInt("menin_position"));
                postList.add(aer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return postList;
    }

    ///---GenericPostWiseStrength For DDO Office---
    @Override
    public List getGenericPostList(String offcode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List genericPostList = new ArrayList();
        AnnualEstablishment aer = null;
        String dupGpc = null;
        String dupPostName = null;
        try {
            con = this.dataSource.getConnection();

            ps = con.prepareStatement("select t2.gpc,t2.postname,sum(ddosanPost)sanPost, sum(ddomenInPos)menInPos,sum(ddovacantPost)vacantPost from \n"
                    + " (select t1.gpc,t1.postName,t1.off_code,count(t1.gpc)ddosanPost,getcntMeninpositionGpc(t1.off_code,gpc)ddomenInPos,getcntVacantGpc(t1.off_code,gpc)ddovacantPost from \n"
                    + " (SELECT getCntMappingSpc(g_spc.spc)cntPost,getpostnamefromspc(g_spc.spc)postName,getgpcfromspc(g_spc.spc)gpc,g_spc.spc,pay_scale, \n"
                    + " post_grp,level_7thpay,gp,bsm.*,bgm.* from   \n"
                    + " (SELECT * FROM G_SECTION where off_code=?) gs \n"
                    + " INNER JOIN bill_section_mapping  bsm \n"
                    + " on gs.section_id=bsm.section_id \n"
                    + " inner join bill_group_master bgm on bsm.BILL_GROUP_ID=bgm.BILL_GROUP_ID \n"
                    + " INNER JOIN SECTION_POST_MAPPING ON bsm.SECTION_ID = SECTION_POST_MAPPING.SECTION_ID \n"
                    + " INNER JOIN G_SPC ON SECTION_POST_MAPPING.SPC = G_SPC.SPC \n"
                    + " where (gs.bill_type='REGULAR' or gs.bill_type='CONT6_REG') and bgm.bill_type = '42' order by off_code )t1 \n"
                    + " group by t1.gpc,t1.postName,t1.off_code )t2  \n"
                    + " group by t2.gpc,t2.postname  order by t2.postName");
            ps.setString(1, offcode);
            rs = ps.executeQuery();
            while (rs.next()) {
                aer = new AnnualEstablishment();
                aer.setPostname(rs.getString("postName"));
                aer.setGpc(rs.getString("gpc"));
                aer.setMeninPosition(rs.getInt("menInPos"));
                aer.setVacancyPosition(rs.getInt("vacantPost"));
                aer.setSanctionedStrength(rs.getInt("sanPost"));
                if (rs.getInt("sanPost") < (rs.getInt("vacantPost") + rs.getInt("menInPos"))) {
                    aer.setDupGpc(rs.getString("gpc"));
                    aer.setDupPostname(rs.getString("postName"));
                }
                genericPostList.add(aer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return genericPostList;
    }

    @Override
    public void downloadExcelGenericPostWiseStrength(OutputStream out, WritableWorkbook workbook, String fileName, String offCode) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        AnnualEstablishment aer = null;
        List genericPostList = new ArrayList();
        int totalSancStrength = 0;
        int totMenInPosition = 0;
        int totVacancy = 0;
        try {
            con = this.dataSource.getConnection();

            WritableSheet sheet = workbook.createSheet("GenericPostStrength", 0);
            WritableFont headformat = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
            WritableCellFormat headcell = new WritableCellFormat(headformat);
            headcell.setAlignment(Alignment.CENTRE);
            WritableCellFormat headcell1 = new WritableCellFormat(headformat);
            headcell1.setAlignment(Alignment.LEFT);

            WritableFont cellformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
            WritableCellFormat innercell = new WritableCellFormat(cellformat);
            innercell.setAlignment(Alignment.CENTRE);

            WritableCellFormat innercell1 = new WritableCellFormat(cellformat);
            innercell1.setAlignment(Alignment.LEFT);

            WritableCellFormat innercell2 = new WritableCellFormat(cellformat);
            innercell2.setAlignment(Alignment.CENTRE);
            innercell2.setBackground(Colour.GREY_25_PERCENT);

            WritableCellFormat innercell3 = new WritableCellFormat(cellformat);
            innercell3.setAlignment(Alignment.LEFT);
            innercell3.setBackground(Colour.GREY_25_PERCENT);

            WritableCellFormat colorheadercell = new WritableCellFormat(headformat);
            colorheadercell.setBorder(Border.RIGHT, BorderLineStyle.MEDIUM_DASHED, Colour.RED);

            WritableCellFormat colorcell = new WritableCellFormat(cellformat);
            colorcell.setBorder(Border.RIGHT, BorderLineStyle.MEDIUM_DASHED, Colour.RED);

            int widthInChars = 10;

            Label label = new Label(0, 0, "Sl No", headcell);//column,row
            sheet.addCell(label);
            label = new Label(1, 0, "Post Name", headcell);
            sheet.addCell(label);
            sheet.setColumnView(1, widthInChars);
            label = new Label(2, 0, "Sanctioned Post", headcell);
            sheet.addCell(label);
            label = new Label(3, 0, "Men In Position", headcell);
            sheet.addCell(label);
            label = new Label(4, 0, "Vacancy", headcell);
            sheet.addCell(label);

            // ----Post List Other Than HODs---
            ps = con.prepareStatement("select t1.gpc,t1.postName,t1.off_code,count(t1.gpc)sanPost,getcntMeninpositionGpc(t1.off_code,gpc)menInPos,getcntVacantGpc(t1.off_code,gpc)vacantPost from\n"
                    + "(SELECT getCntMappingSpc(g_spc.spc)cntPost,getpostnamefromspc(g_spc.spc)postName,getgpcfromspc(g_spc.spc)gpc,g_spc.spc,pay_scale,post_grp,level_7thpay,gp,bsm.*,bgm.* from\n"
                    + "(SELECT * FROM G_SECTION WHERE OFF_CODE=?)gs\n"
                    + "INNER JOIN bill_section_mapping  bsm\n"
                    + "on gs.section_id=bsm.section_id\n"
                    + "inner join bill_group_master bgm on bsm.BILL_GROUP_ID=bgm.BILL_GROUP_ID\n"
                    + "INNER JOIN SECTION_POST_MAPPING ON bsm.SECTION_ID = SECTION_POST_MAPPING.SECTION_ID\n"
                    + "INNER JOIN G_SPC ON SECTION_POST_MAPPING.SPC = G_SPC.SPC\n"
                    + "where (gs.bill_type='REGULAR' or gs.bill_type='CONT6_REG') and bgm.bill_type = '42' )t1 \n"
                    + "group by t1.gpc,t1.postName,t1.off_code order by t1.postName ");

            ps.setString(1, offCode);
            rs = ps.executeQuery();
            int row = 0;
            while (rs.next()) {
                row++;
                if (rs.getInt("sanPost") < rs.getInt("menInPos") + rs.getInt("vacantPost")) {
                    label = new Label(0, row, Integer.toString(row), innercell2);
                    sheet.addCell(label);

                    label = new Label(1, row, rs.getString("postName"), innercell3);
                    sheet.addCell(label);
                    //set column width
                    sheet.setColumnView(1, 80);
                    label = new Label(2, row, Integer.toString(rs.getInt("sanPost")), innercell2);
                    sheet.addCell(label);
                    sheet.setColumnView(2, 20);

                    label = new Label(3, row, Integer.toString(rs.getInt("menInPos")), innercell2);
                    sheet.addCell(label);
                    sheet.setColumnView(3, 20);
                    label = new Label(4, row, Integer.toString(rs.getInt("vacantPost")), innercell2);
                    sheet.addCell(label);

                } else {
                    label = new Label(0, row, Integer.toString(row), innercell);
                    sheet.addCell(label);

                    label = new Label(1, row, rs.getString("postName"), innercell1);
                    sheet.addCell(label);
                    //set column width
                    sheet.setColumnView(1, 80);
                    label = new Label(2, row, Integer.toString(rs.getInt("sanPost")), innercell);
                    sheet.addCell(label);
                    sheet.setColumnView(2, 20);

                    label = new Label(3, row, Integer.toString(rs.getInt("menInPos")), innercell);
                    sheet.addCell(label);
                    sheet.setColumnView(3, 20);
                    label = new Label(4, row, Integer.toString(rs.getInt("vacantPost")), innercell);
                    sheet.addCell(label);
                }
                totalSancStrength = totalSancStrength + rs.getInt("sanPost");
                totMenInPosition = totMenInPosition + rs.getInt("menInPos");
                totVacancy = totVacancy + rs.getInt("vacantPost");
            }

            label = new Label(0, row + 1, "", headcell);
            sheet.addCell(label);

            label = new Label(1, row + 1, "", headcell);
            sheet.addCell(label);
            label = new Label(2, row + 1, Integer.toString(totalSancStrength), headcell);
            sheet.addCell(label);
            label = new Label(3, row + 1, Integer.toString(totMenInPosition), headcell);
            sheet.addCell(label);
            label = new Label(4, row + 1, Integer.toString(totVacancy), headcell);
            sheet.addCell(label);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List gpcWiseEmployeeMapped(String offCode, String gpc) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List empList = new ArrayList();
        AnnualEstablishment aer = null;

        try {
            con = this.dataSource.getConnection();

            ps = con.prepareStatement("SELECT EMP_ID,ARRAY_TO_STRING(ARRAY[initials,f_name,m_name,l_name],' ')empName,getCntMappingSpc(g_spc.spc)cntPost,\n"
                    + "getpostnamefromspc(g_spc.spc)postName,section_name,getgpcfromspc(g_spc.spc)gpc,g_spc.spc,bgm.description billName,g_office.ddo_code,off_en from\n"
                    + "(SELECT * FROM G_SECTION where off_code=?)gs\n"
                    + "inner join g_office on gs.off_code=g_office.off_code\n"
                    + "INNER JOIN bill_section_mapping  bsm\n"
                    + "on gs.section_id=bsm.section_id\n"
                    + "inner join bill_group_master bgm on bsm.BILL_GROUP_ID=bgm.BILL_GROUP_ID\n"
                    + "INNER JOIN SECTION_POST_MAPPING ON bsm.SECTION_ID = SECTION_POST_MAPPING.SECTION_ID\n"
                    + "INNER JOIN G_SPC ON SECTION_POST_MAPPING.SPC = G_SPC.SPC\n"
                    + "INNER JOIN EMP_MAST ON EMP_MAST.CUR_SPC=G_SPC.SPC\n"
                    + "where (gs.bill_type='REGULAR' or gs.bill_type='CONT6_REG') and bgm.bill_type = '42'  and gpc=?\n"
                    + "and getCntMappingSpc(g_spc.spc)<>0 order by off_en");
            ps.setString(1, offCode);
            ps.setString(2, gpc);
            rs = ps.executeQuery();
            while (rs.next()) {
                aer = new AnnualEstablishment();
                aer.setEmpId(rs.getString("EMP_ID"));
                aer.setEmpName(rs.getString("empName"));
                aer.setEmpPost(rs.getString("postName"));
                aer.setSectionName(rs.getString("section_name"));
                aer.setBillGrpName(rs.getString("billName"));
                aer.setOffName(rs.getString("off_en") + " (" + rs.getString("ddo_code") + ")");
                empList.add(aer);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);

        }
        return empList;
    }

    @Override
    public List getMultipleEmpInOneSPC(String gpc, String offCode) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List emplist = new ArrayList();
        AnnualEstablishment aer = null;
        try {
            con = this.dataSource.getConnection();
            System.out.println("String offcode, String gpc::" + gpc);

            ps = con.prepareStatement("SELECT EMP_ID,GPF_NO,getpostnamefromspc(emp.CUR_SPC)postName,getoffnamefromspc(emp.CUR_SPC)offName,\n"
                    + "ARRAY_TO_STRING(ARRAY[INITIALS,F_NAME,M_NAME,L_NAME],' ')EMPNAME, g_office.ddo_code from\n"
                    + "(select cur_spc,count(*)cnt from emp_mast where getgpcfromspc(CUR_SPC)=? and \n"
                    + "cur_off_code=? group by cur_spc\n"
                    + "having count(cur_spc)>1 )dupemp\n"
                    + "inner join emp_mast emp on dupemp.cur_spc=emp.cur_spc\n"
                    + "INNER JOIN SECTION_POST_MAPPING SPM ON EMP.CUR_SPC=SPM.SPC\n"
                    + "inner join g_office on emp.cur_off_code=g_office.off_code");
            ps.setString(1, gpc);
            ps.setString(2, offCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                aer = new AnnualEstablishment();
                aer.setEmpName(rs.getString("EMPNAME"));
                aer.setEmpId(rs.getString("EMP_ID"));
                aer.setGpfNo(rs.getString("GPF_NO"));
                aer.setEmpPost(rs.getString("postName"));
                aer.setOffName(rs.getString("offName") + " (" + rs.getString("ddo_code") + ")");
                emplist.add(aer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emplist;
    }

    /////----GenericPostWiseStrength For CO Office -------
    @Override
    public List getGenericPostListForCO(String offcode, String offLvl) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List genericPostList = new ArrayList();
        AnnualEstablishment aer = null;
        String dupGpc = null;
        String dupPostName = null;
        try {
            con = this.dataSource.getConnection();
            if (offLvl != null && offLvl.equals("02")) {
                // ----Post List for HODs---
                ps = con.prepareStatement("select t2.gpc,t2.postname,sum(ddosanPost)sanPost, sum(ddomenInPos)menInPos,sum(ddovacantPost)vacantPost from\n"
                        + "(select t1.gpc,t1.postName,t1.off_code,count(t1.gpc)ddosanPost,getcntMeninpositionGpc(t1.off_code,gpc)ddomenInPos,getcntVacantGpc(t1.off_code,gpc)ddovacantPost from\n"
                        + "(SELECT ddoCoMap.CO_off_code,getCntMappingSpc(g_spc.spc)cntPost,getpostnamefromspc(g_spc.spc)postName,getgpcfromspc(g_spc.spc)gpc,g_spc.spc,pay_scale,\n"
                        + "post_grp,level_7thpay,gp,bsm.*,bgm.* from\n"
                        + "(select * from ddo_to_co_mapping where CO_off_code=?)ddoCoMap\n"
                        + "inner join \n"
                        + "(SELECT * FROM G_SECTION) gs \n"
                        + " on ddoCoMap.ddo_off_code=gs.off_code\n"
                        + "INNER JOIN bill_section_mapping  bsm\n"
                        + "on gs.section_id=bsm.section_id\n"
                        + "inner join bill_group_master bgm on bsm.BILL_GROUP_ID=bgm.BILL_GROUP_ID\n"
                        + "INNER JOIN SECTION_POST_MAPPING ON bsm.SECTION_ID = SECTION_POST_MAPPING.SECTION_ID\n"
                        + "INNER JOIN G_SPC ON SECTION_POST_MAPPING.SPC = G_SPC.SPC\n"
                        + "where (gs.bill_type='REGULAR' or gs.bill_type='CONT6_REG') and bgm.bill_type = '42' order by off_code )t1\n"
                        + "group by t1.gpc,t1.postName,t1.off_code )t2 \n"
                        + "group by t2.gpc,t2.postname  order by t2.postName");
            }

            ps.setString(1, offcode);
            rs = ps.executeQuery();
            while (rs.next()) {
                aer = new AnnualEstablishment();
                aer.setPostname(rs.getString("postName"));
                aer.setGpc(rs.getString("gpc"));
                aer.setMeninPosition(rs.getInt("menInPos"));
                aer.setVacancyPosition(rs.getInt("vacantPost"));
                aer.setSanctionedStrength(rs.getInt("sanPost"));
                if (rs.getInt("sanPost") < (rs.getInt("vacantPost") + rs.getInt("menInPos"))) {
                    aer.setDupGpc(rs.getString("gpc"));
                    aer.setDupPostname(rs.getString("postName"));
                }
                genericPostList.add(aer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return genericPostList;
    }

    @Override
    public void downloadExcelGenericPostWiseStrengthForCO(OutputStream out, WritableWorkbook workbook, String fileName, String offCode, String offLvl) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        AnnualEstablishment aer = null;
        List genericPostList = new ArrayList();
        int totalSancStrength = 0;
        int totMenInPosition = 0;
        int totVacancy = 0;
        try {
            con = this.dataSource.getConnection();

            WritableSheet sheet = workbook.createSheet("GenericPostStrength", 0);
            WritableFont headformat = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
            WritableCellFormat headcell = new WritableCellFormat(headformat);
            headcell.setAlignment(Alignment.CENTRE);
            WritableCellFormat headcell1 = new WritableCellFormat(headformat);
            headcell1.setAlignment(Alignment.LEFT);

            WritableFont cellformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
            WritableCellFormat innercell = new WritableCellFormat(cellformat);
            innercell.setAlignment(Alignment.CENTRE);

            WritableCellFormat innercell1 = new WritableCellFormat(cellformat);
            innercell1.setAlignment(Alignment.LEFT);

            WritableCellFormat innercell2 = new WritableCellFormat(cellformat);
            innercell2.setAlignment(Alignment.CENTRE);
            innercell2.setBackground(Colour.GREY_25_PERCENT);

            WritableCellFormat innercell3 = new WritableCellFormat(cellformat);
            innercell3.setAlignment(Alignment.LEFT);
            innercell3.setBackground(Colour.GREY_25_PERCENT);

            WritableCellFormat colorheadercell = new WritableCellFormat(headformat);
            colorheadercell.setBorder(Border.RIGHT, BorderLineStyle.MEDIUM_DASHED, Colour.RED);

            WritableCellFormat colorcell = new WritableCellFormat(cellformat);
            colorcell.setBorder(Border.RIGHT, BorderLineStyle.MEDIUM_DASHED, Colour.RED);

            int widthInChars = 10;

            Label label = new Label(0, 0, "Sl No", headcell);//column,row
            sheet.addCell(label);
            label = new Label(1, 0, "Post Name", headcell);
            sheet.addCell(label);
            sheet.setColumnView(1, widthInChars);
            label = new Label(2, 0, "Sanctioned Post", headcell);
            sheet.addCell(label);
            label = new Label(3, 0, "Men In Position", headcell);
            sheet.addCell(label);
            label = new Label(4, 0, "Vacancy", headcell);
            sheet.addCell(label);

            if (offLvl != null && offLvl.equals("02")) {
                // ----Post List for HODs---
                ps = con.prepareStatement("select t2.gpc,t2.postname,sum(ddosanPost)sanPost, sum(ddomenInPos)menInPos,sum(ddovacantPost)vacantPost from\n"
                        + "(select t1.gpc,t1.postName,t1.off_code,count(t1.gpc)ddosanPost,getcntMeninpositionGpc(t1.off_code,gpc)ddomenInPos,getcntVacantGpc(t1.off_code,gpc)ddovacantPost from\n"
                        + "(SELECT ddoCoMap.CO_off_code,getCntMappingSpc(g_spc.spc)cntPost,getpostnamefromspc(g_spc.spc)postName,getgpcfromspc(g_spc.spc)gpc,g_spc.spc,pay_scale,\n"
                        + "post_grp,level_7thpay,gp,bsm.*,bgm.* from\n"
                        + "(select * from ddo_to_co_mapping where CO_off_code=?)ddoCoMap\n"
                        + "inner join \n"
                        + "(SELECT * FROM G_SECTION) gs \n"
                        + " on ddoCoMap.ddo_off_code=gs.off_code\n"
                        + "INNER JOIN bill_section_mapping  bsm\n"
                        + "on gs.section_id=bsm.section_id\n"
                        + "inner join bill_group_master bgm on bsm.BILL_GROUP_ID=bgm.BILL_GROUP_ID\n"
                        + "INNER JOIN SECTION_POST_MAPPING ON bsm.SECTION_ID = SECTION_POST_MAPPING.SECTION_ID\n"
                        + "INNER JOIN G_SPC ON SECTION_POST_MAPPING.SPC = G_SPC.SPC\n"
                        + "where (gs.bill_type='REGULAR' or gs.bill_type='CONT6_REG') and bgm.bill_type = '42' order by off_code )t1\n"
                        + "group by t1.gpc,t1.postName,t1.off_code )t2 \n"
                        + "group by t2.gpc,t2.postname  order by t2.postName");
            }
            ps.setString(1, offCode);
            rs = ps.executeQuery();
            int row = 0;
            while (rs.next()) {
                row++;
                if (rs.getInt("sanPost") < rs.getInt("menInPos") + rs.getInt("vacantPost")) {
                    label = new Label(0, row, Integer.toString(row), innercell2);
                    sheet.addCell(label);

                    label = new Label(1, row, rs.getString("postName"), innercell3);
                    sheet.addCell(label);
                    //set column width
                    sheet.setColumnView(1, 80);
                    label = new Label(2, row, Integer.toString(rs.getInt("sanPost")), innercell2);
                    sheet.addCell(label);
                    sheet.setColumnView(2, 20);

                    label = new Label(3, row, Integer.toString(rs.getInt("menInPos")), innercell2);
                    sheet.addCell(label);
                    sheet.setColumnView(3, 20);
                    label = new Label(4, row, Integer.toString(rs.getInt("vacantPost")), innercell2);
                    sheet.addCell(label);

                } else {
                    label = new Label(0, row, Integer.toString(row), innercell);
                    sheet.addCell(label);

                    label = new Label(1, row, rs.getString("postName"), innercell1);
                    sheet.addCell(label);
                    //set column width
                    sheet.setColumnView(1, 80);
                    label = new Label(2, row, Integer.toString(rs.getInt("sanPost")), innercell);
                    sheet.addCell(label);
                    sheet.setColumnView(2, 20);

                    label = new Label(3, row, Integer.toString(rs.getInt("menInPos")), innercell);
                    sheet.addCell(label);
                    sheet.setColumnView(3, 20);
                    label = new Label(4, row, Integer.toString(rs.getInt("vacantPost")), innercell);
                    sheet.addCell(label);
                }
                totalSancStrength = totalSancStrength + rs.getInt("sanPost");
                totMenInPosition = totMenInPosition + rs.getInt("menInPos");
                totVacancy = totVacancy + rs.getInt("vacantPost");
            }

            label = new Label(0, row + 1, "", headcell);
            sheet.addCell(label);

            label = new Label(1, row + 1, "", headcell);
            sheet.addCell(label);
            label = new Label(2, row + 1, Integer.toString(totalSancStrength), headcell);
            sheet.addCell(label);
            label = new Label(3, row + 1, Integer.toString(totMenInPosition), headcell);
            sheet.addCell(label);
            label = new Label(4, row + 1, Integer.toString(totVacancy), headcell);
            sheet.addCell(label);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List gpcWiseEmployeeMappedForCO(String offCode, String gpc, String offLvl) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List empList = new ArrayList();
        AnnualEstablishment aer = null;

        try {
            con = this.dataSource.getConnection();
            if (offLvl != null && offLvl.equals("02")) {
                ps = con.prepareStatement("SELECT EMP_ID,ARRAY_TO_STRING(ARRAY[initials,f_name,m_name,l_name],' ')empName,getCntMappingSpc(g_spc.spc)cntPost,\n"
                        + "getpostnamefromspc(g_spc.spc)postName,section_name,getgpcfromspc(g_spc.spc)gpc,g_spc.spc,bgm.description billName,getofficeen(ddo_off_code)off_en,g_office.ddo_code from\n"
                        + "(select * from ddo_to_co_mapping where CO_off_code=?)ddoCoMap \n"
                        + "INNER JOIN\n"
                        + "(SELECT * FROM G_SECTION)gs\n"
                        + "ON ddoCoMap.ddo_off_code=gs.off_code\n"
                        + "inner join g_office on gs.off_code=g_office.off_code\n"
                        + "INNER JOIN bill_section_mapping  bsm\n"
                        + "on gs.section_id=bsm.section_id\n"
                        + "inner join bill_group_master bgm on bsm.BILL_GROUP_ID=bgm.BILL_GROUP_ID\n"
                        + "INNER JOIN SECTION_POST_MAPPING ON bsm.SECTION_ID = SECTION_POST_MAPPING.SECTION_ID\n"
                        + "INNER JOIN G_SPC ON SECTION_POST_MAPPING.SPC = G_SPC.SPC\n"
                        + "INNER JOIN EMP_MAST ON EMP_MAST.CUR_SPC=G_SPC.SPC\n"
                        + "where (gs.bill_type='REGULAR' or gs.bill_type='CONT6_REG') and bgm.bill_type = '42'  and gpc=?\n"
                        + "and getCntMappingSpc(g_spc.spc)<>0 order by off_en");
            }
            ps.setString(1, offCode);
            ps.setString(2, gpc);
            rs = ps.executeQuery();
            while (rs.next()) {
                aer = new AnnualEstablishment();
                aer.setEmpId(rs.getString("EMP_ID"));
                aer.setEmpName(rs.getString("empName"));
                aer.setEmpPost(rs.getString("postName"));
                aer.setSectionName(rs.getString("section_name"));
                aer.setBillGrpName(rs.getString("billName"));
                aer.setOffName(rs.getString("off_en") + " (" + rs.getString("ddo_code") + ")");
                empList.add(aer);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);

        }
        return empList;
    }

    @Override
    public List getMultipleEmpInOneSPCForCO(String gpc, String offCode, String offLvl) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        List emplist = new ArrayList();
        AnnualEstablishment aer = null;
        try {
            con = this.dataSource.getConnection();

            if (offLvl != null && offLvl.equals("02")) {

                ps = con.prepareStatement("SELECT EMP_ID,GPF_NO,getpostnamefromspc(emp.CUR_SPC)postName,getoffnamefromspc(emp.CUR_SPC)offName,\n"
                        + "ARRAY_TO_STRING(ARRAY[INITIALS,F_NAME,M_NAME,L_NAME],' ')EMPNAME,g_office.ddo_code from\n"
                        + "(select cur_spc,count(*)cnt from emp_mast where getgpcfromspc(CUR_SPC)=? and \n"
                        + "cur_off_code in (select ddo_off_code from ddo_to_co_mapping  where co_off_code=?) group by cur_spc\n"
                        + "having count(cur_spc)>1 )dupemp\n"
                        + "inner join emp_mast emp on dupemp.cur_spc=emp.cur_spc\n"
                        + "INNER JOIN SECTION_POST_MAPPING SPM ON EMP.CUR_SPC=SPM.SPC\n"
                        + "inner join g_office on emp.cur_off_code=g_office.off_code");
            }
            ps.setString(1, gpc);
            ps.setString(2, offCode);
            rs = ps.executeQuery();
            while (rs.next()) {
                aer = new AnnualEstablishment();
                aer.setEmpName(rs.getString("EMPNAME"));
                aer.setEmpId(rs.getString("EMP_ID"));
                aer.setGpfNo(rs.getString("GPF_NO"));
                aer.setEmpPost(rs.getString("postName"));
                aer.setOffName(rs.getString("offName") + " (" + rs.getString("ddo_code") + ")");
                emplist.add(aer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emplist;
    }

    @Override
    public String getAERRevertReasonVerifier(String aerid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String revertReason = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "select verifier_revert_reason from aer_report_submit where aer_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(aerid));
            rs = pst.executeQuery();
            if (rs.next()) {
                revertReason = rs.getString("verifier_revert_reason");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return revertReason;
    }

    @Override
    public List viewMappedSectionAgainstGPC(String offCode, String postcode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        SelectOption so = null;

        ArrayList sectionnamelist = new ArrayList();
        try {
            con = this.repodataSource.getConnection();

            String sql = "select section_name from g_section"
                    + " left outer join section_post_mapping on g_section.section_id=section_post_mapping.section_id"
                    + " left outer join g_spc on section_post_mapping.spc=g_spc.spc"
                    + " left outer join g_post on g_spc.gpc=g_post.post_code"
                    + " where g_spc.off_code=? and g_post.post_code=?"
                    + " group by section_name order by section_name";
            pst = con.prepareStatement(sql);
            pst.setString(1, offCode);
            pst.setString(2, postcode);
            rs = pst.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setLabel(rs.getString("section_name"));
                so.setValue(rs.getString("section_name"));
                sectionnamelist.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return sectionnamelist;
    }

    @Override
    public ArrayList DistWiseOfficeAERReportDC(String fy, String distcode) {
        
        Connection con = null;
        
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement pst3 = null;
        
        ResultSet rs1, rs2, rs3 = null;
        ArrayList outList = new ArrayList();
        String stm, stm1 = null;
        String fyYear = fy;
        try {
            con = this.repodataSource.getConnection();
            
            stm = ("SELECT g_office.off_en ,g_office.off_code ,g_district.dist_code,dist_name FROM g_district INNER JOIN g_office  "
                    + " ON g_district.dist_code=g_office.dist_code AND  g_office.is_ddo='Y' AND g_office.dist_code=? ORDER BY g_office.off_en");
            ps = con.prepareStatement(stm);
            ps.setString(1, distcode);
            rs1 = ps.executeQuery();
            while (rs1.next()) {
                AnnualEstablishment aer = new AnnualEstablishment();
                aer.setDepartmentname(rs1.getString("off_en"));
                String dist_code = rs1.getString("dist_code");
                String offCode = rs1.getString("off_code");
                aer.setOffCode(offCode);

                stm1 = ("SELECT (SELECT count(DISTINCT aer_report_submit.off_code) FROM aer_report_submit,g_office WHERE aer_report_submit.off_code=g_office.off_code AND  dist_code = ? AND is_operator_submitted=? AND g_office.off_code=? AND fy=? AND g_office.is_ddo='Y') as operator_submitted,"
                        + "(SELECT count(DISTINCT aer_report_submit.off_code) FROM aer_report_submit,g_office WHERE aer_report_submit.off_code=g_office.off_code AND  dist_code = ? AND is_reviewer_submitted=? AND g_office.off_code=? AND fy=? AND g_office.is_ddo='Y') as reviewer_submitted,"
                        + "(SELECT count(DISTINCT aer_report_submit.off_code) FROM aer_report_submit,g_office WHERE aer_report_submit.off_code=g_office.off_code AND  dist_code = ? AND is_verifier_submitted=? AND g_office.off_code=? AND fy=? AND g_office.is_ddo='Y') as verifier_submitted,"
                        + "(SELECT count(DISTINCT aer_report_submit.off_code) FROM aer_report_submit,g_office WHERE aer_report_submit.off_code=g_office.off_code AND  dist_code = ? AND is_acceptor_submitted=? AND g_office.off_code=? AND fy=? AND g_office.is_ddo='Y') as acceptor_submitted,"
                        + "(SELECT count(DISTINCT aer_report_submit.off_code) FROM aer_report_submit,g_office WHERE aer_report_submit.off_code=g_office.off_code AND  dist_code = ? AND is_approver_submitted=? AND g_office.off_code=? AND fy=? AND g_office.is_ddo='Y') as approver_submitted");
                ps1 = con.prepareStatement(stm1);
                ps1.setString(1, dist_code);
                ps1.setString(2, "Y");
                ps1.setString(3, offCode);
                ps1.setString(4, fyYear);
                ps1.setString(5, dist_code);
                ps1.setString(6, "Y");
                ps1.setString(7, offCode);
                ps1.setString(8, fyYear);
                ps1.setString(9, dist_code);
                ps1.setString(10, "Y");
                ps1.setString(11, offCode);
                ps1.setString(12, fyYear);
                ps1.setString(13, dist_code);
                ps1.setString(14, "Y");
                ps1.setString(15, offCode);
                ps1.setString(16, fyYear);
                ps1.setString(17, dist_code);
                ps1.setString(18, "Y");
                ps1.setString(19, offCode);
                ps1.setString(20, fyYear);
                rs2 = ps1.executeQuery();
                if (rs2.next()) {
                    aer.setOperatorSubmitted(rs2.getInt("operator_submitted"));
                    aer.setApproverSubmitted(rs2.getInt("approver_submitted"));
                    aer.setReviewerSubmitted(rs2.getInt("reviewer_submitted"));
                    aer.setVerifierSubmitted(rs2.getInt("verifier_submitted"));
                    aer.setAcceptorSubmitted(rs2.getInt("acceptor_submitted"));
                }
                
                pst3 = con.prepareStatement("SELECT aer_id from aer_report_submit where off_code=? and fy=?");
                pst3.setString(1,offCode);
                pst3.setString(2,fyYear);
                rs3 = pst3.executeQuery();
                if(rs3.next()){
                    aer.setAerId(rs3.getInt("aer_id"));
                }
                
                outList.add(aer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(ps, ps1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return outList;
    }
}
