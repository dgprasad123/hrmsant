package hrms.dao.servicebook;

import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.servicebook.EmpServiceHistory;
import hrms.model.servicebook.SbCorrectionRequestModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class SbCorrectionRequestDAOImpl implements SbCorrectionRequestDAO {

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
    public List getServiceBookModuleNameList(String empHrmsId) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List sbModuleList = new ArrayList();
        SelectOption so = null;

        try {
            con = this.repodataSource.getConnection();
            String modNameSql = "SELECT NOT_TYPE FROM (SELECT NOT_TYPE FROM emp_notification WHERE emp_notification.emp_id=? AND IF_VISIBLE='Y' "
                    + "AND NOT_TYPE!='CHNG_STRUCTURE' AND NOT_TYPE!='LEAVE' AND NOT_TYPE!='RESULT' AND NOT_TYPE!='COMPLIANCE' AND NOT_TYPE!='SC_NOTICE' "
                    + "AND NOT_TYPE!='INCREMENT' and NOT_TYPE!='LOAN_SANC' and NOT_TYPE!='REINSTATEMENT' and NOT_TYPE!='SUSPENSION' and (SB_DESCRIPTION "
                    + "is not null and SB_DESCRIPTION<>'') group by NOT_TYPE "
                    + "UNION "
                    + "SELECT emp_notification.NOT_TYPE FROM (SELECT * FROM EMP_INCR WHERE emp_id=? AND PRID IS NULL) EMP_INCR "
                    + "inner join (select * from emp_notification where NOT_TYPE='INCREMENT' and emp_id=? and (if_visible='Y' or if_visible "
                    + "is null or if_visible='')) emp_notification ON EMP_INCR.NOT_ID = emp_notification.NOT_ID group by emp_notification.NOT_TYPE "
                    + "UNION SELECT NOT_TYPE FROM emp_notification where EMP_NOTIFICATION.NOT_ID IN (SELECT NOT_ID FROM EMP_LEAVE WHERE "
                    + "EMP_LEAVE.EMP_ID=? AND LSOT_ID='01') AND EMP_NOTIFICATION.EMP_ID=? AND NOT_TYPE='LEAVE' AND "
                    + "STATUS = 'SANCTIONED AND AVAILED' group by NOT_TYPE "
                    + "UNION SELECT NOT_TYPE FROM emp_notification where EMP_NOTIFICATION.NOT_ID IN (SELECT NOT_ID FROM EMP_LEAVE WHERE "
                    + "EMP_LEAVE.EMP_ID=? AND LSOT_ID='02') AND EMP_NOTIFICATION.EMP_ID=? AND NOT_TYPE='LEAVE' AND STATUS = 'SANCTIONED' "
                    + "AND EMP_NOTIFICATION.IF_VISIBLE='Y' group by NOT_TYPE "
                    + "UNION SELECT NOT_TYPE FROM emp_notification where EMP_NOTIFICATION.NOT_ID IN (SELECT NOT_ID FROM EMP_LEAVE WHERE EMP_LEAVE.EMP_ID=? "
                    + "AND LSOT_ID!='01' AND LSOT_ID!='02') AND EMP_NOTIFICATION.EMP_ID=? AND NOT_TYPE='LEAVE' AND EMP_NOTIFICATION.IF_VISIBLE='Y' "
                    + "group by NOT_TYPE "
                    + "UNION SELECT emp_notification.NOT_TYPE FROM emp_notification "
                    + "inner join emp_loan_sanc on emp_notification.not_id=emp_loan_sanc.not_id WHERE emp_notification.emp_id=? AND IF_VISIBLE='Y' "
                    + "AND  emp_loan_sanc.NOT_TYPE='LOAN_SANC' and (emp_loan_sanc.loan_tp='BICA' or emp_loan_sanc.loan_tp='CMPA' or emp_loan_sanc.loan_tp='MOPA' "
                    + "or emp_loan_sanc.loan_tp='MCA' or emp_loan_sanc.loan_tp='HBA' or emp_loan_sanc.loan_tp='VE' or emp_loan_sanc.loan_tp='SHBA') "
                    + "and (SB_DESCRIPTION is not null and SB_DESCRIPTION<>'') group by emp_notification.NOT_TYPE "
                    + "UNION SELECT 'EXAMINATION' NOT_TYPE FROM emp_exam where IF_VISIBLE='Y' AND emp_id=? group by NOT_TYPE "
                    + "UNION SELECT 'TRAINING' NOT_TYPE FROM emp_TRAIN where IF_VISIBLE='Y' and emp_id=? group by NOT_TYPE "
                    + "UNION SELECT 'SERVICE VERIFICATION CERTIFICATE' NOT_TYPE FROM EMP_SV where emp_id=? group by NOT_TYPE "
                    + "UNION SELECT 'RELIEVE' NOT_TYPE FROM EMP_NOTIFICATION NOTI LEFT OUTER JOIN (SELECT * FROM EMP_RELIEVE WHERE EMP_ID=? "
                    + "AND (JOIN_ID IS NULL OR (JOIN_ID::TEXT NOT IN (SELECT JOIN_ID::TEXT FROM EMP_JOIN WHERE EMP_ID=? AND IF_AD_CHARGE='Y'))))"
                    + "EMP_RELIEVE ON NOTI.NOT_ID=EMP_RELIEVE.NOT_ID WHERE EMP_RELIEVE.EMP_ID=? and (NOTI.not_type ='REHABILITATION' or "
                    + "NOTI.not_type='ABSORPTION' or NOTI.not_type='REDEPLOYMENT' or NOTI.not_type='VALIDATION' or NOTI.not_type='LT_TRAINING' or "
                    + "NOTI.not_type='DEPUTATION' or NOTI.not_type ='TRANSFER' or NOTI.not_type ='PROMOTION' or NOTI.not_type ='REDESIGNATION' or "
                    + "NOTI.not_type ='RESIGNATION' or NOTI.not_type ='DECEASED' or NOTI.not_type ='RETIREMENT' or NOTI.not_type='ALLOT_CADRE' or "
                    + "(NOTI.not_type='LEAVE' AND NOTI.NOT_ID IN (SELECT NOT_ID FROM EMP_LEAVE WHERE EMP_ID=? AND IF_LONGTERM='Y'))) "
                    + "and EMP_RELIEVE.IF_VISIBLE='Y' AND EMP_RELIEVE.emp_id=? AND EMP_RELIEVE.NOT_TYPE!='CHNG_STRUCTURE' AND "
                    + "EMP_RELIEVE.NOT_TYPE!='SUSPENSION' and EMP_RELIEVE.NOT_ID is not null group by EMP_RELIEVE.NOT_TYPE "
                    + "UNION SELECT 'JOINING' NOT_TYPE FROM emp_notification "
                    + "left outer join emp_relieve reli on emp_notification.emp_id=reli.emp_id and "
                    + "emp_notification.not_id=reli.not_id and reli.join_id not in (Select join_id from emp_join where EMP_ID=? AND if_ad_charge='Y') "
                    + "left outer join emp_join joi on emp_notification.emp_id=joi.emp_id and emp_notification.not_id=joi.not_id "
                    + "left outer join g_spc on joi.ent_auth=g_spc.spc where joi.IF_VISIBLE='Y' AND joi.emp_id=? AND joi.NOT_TYPE!='CHNG_STRUCTURE'"
                    + " AND joi.NOT_TYPE!='ADDITIONAL_CHARGE' and (emp_notification.not_type='FIRST_APPOINTMENT' or emp_notification.not_type='REHABILITATION' "
                    + "or emp_notification.not_type='ABSORPTION' or emp_notification.not_type='REDEPLOYMENT' or emp_notification.not_type='VALIDATION' "
                    + "or emp_notification.not_type='LT_TRAINING' or emp_notification.not_type='DEPUTATION' or emp_notification.not_type='POSTING' or "
                    + "emp_notification.not_type='TRANSFER' or emp_notification.not_type='PROMOTION' or emp_notification.not_type='ADDITIONAL_CHARGE' "
                    + "or emp_notification.not_type='ALLOT_CADRE' or emp_notification.not_type='REDESIGNATION' or emp_notification.not_type='CHNG_STRUCTURE')"
                    + " group by emp_notification.NOT_TYPE "
                    + "UNION SELECT 'REINSTATEMENT' NOT_TYPE FROM EMP_REINSTATEMENT "
                    + "INNER JOIN EMP_SUSPENSION ON EMP_REINSTATEMENT.REINSTATEMENT_ID=EMP_SUSPENSION.SP_ID "
                    + "INNER JOIN EMP_NOTIFICATION ON EMP_REINSTATEMENT.NOT_ID=EMP_NOTIFICATION.NOT_ID "
                    + "where EMP_REINSTATEMENT.emp_id=? AND EMP_SUSPENSION.emp_id=? and EMP_NOTIFICATION.IF_VISIBLE='Y' group by NOT_TYPE "
                    + "UNION SELECT 'SUSPENSION' NOT_TYPE FROM EMP_SUSPENSION "
                    + "INNER JOIN EMP_NOTIFICATION ON EMP_SUSPENSION.NOT_ID=EMP_NOTIFICATION.NOT_ID "
                    + "where EMP_SUSPENSION.IF_VISIBLE='Y' AND EMP_SUSPENSION.emp_id=? AND EMP_NOTIFICATION.emp_id=? AND "
                    + "(HQ_FIX IS NULL OR HQ_FIX = '') GROUP BY NOT_TYPE "
                    + "UNION SELECT 'HEAD QUARTER FIXATION' NOT_TYPE FROM EMP_SUSPENSION where IF_VISIBLE='Y' AND emp_id=? AND HQ_FIX IS NOT NULL "
                    + "GROUP BY NOT_TYPE  "
                    + "UNION SELECT 'PERMISSION' NOT_TYPE FROM EMP_PERMISSION where emp_id=? AND IF_VISIBLE='Y' GROUP BY NOT_TYPE "
                    + "UNION SELECT 'EDUCATION' NOT_TYPE FROM EMP_QUALIFICATION where emp_id=? AND DOE IS NOT NULL AND (SB_DESCRIPTION IS NOT NULL "
                    + "AND SB_DESCRIPTION<>'') GROUP BY NOT_TYPE "
                    + "UNION SELECT 'RETIREMENT' NOT_TYPE FROM EMP_RET_RES where emp_id=? GROUP BY NOT_TYPE "
                    + "UNION SELECT 'SERVICE RECORD' NOT_TYPE FROM EMP_SERVICERECORD WHERE EMP_ID=? GROUP BY NOT_TYPE "
                    + "UNION SELECT 'SERVICE PAY RECORD' NOT_TYPE FROM EMP_SR_PAY WHERE EMP_ID=? GROUP BY NOT_TYPE)temp ORDER BY NOT_TYPE";
            pst = con.prepareStatement(modNameSql);
            pst.setString(1, empHrmsId);
            pst.setString(2, empHrmsId);
            pst.setString(3, empHrmsId);
            pst.setString(4, empHrmsId);
            pst.setString(5, empHrmsId);
            pst.setString(6, empHrmsId);
            pst.setString(7, empHrmsId);
            pst.setString(8, empHrmsId);
            pst.setString(9, empHrmsId);
            pst.setString(10, empHrmsId);
            pst.setString(11, empHrmsId);
            pst.setString(12, empHrmsId);
            pst.setString(13, empHrmsId);
            pst.setString(14, empHrmsId);
            pst.setString(15, empHrmsId);
            pst.setString(16, empHrmsId);
            pst.setString(17, empHrmsId);
            pst.setString(18, empHrmsId);
            pst.setString(19, empHrmsId);
            pst.setString(20, empHrmsId);
            pst.setString(21, empHrmsId);
            pst.setString(22, empHrmsId);
            pst.setString(23, empHrmsId);
            pst.setString(24, empHrmsId);
            pst.setString(25, empHrmsId);
            pst.setString(26, empHrmsId);
            pst.setString(27, empHrmsId);
            pst.setString(28, empHrmsId);
            pst.setString(29, empHrmsId);
            pst.setString(30, empHrmsId);
            rs = pst.executeQuery();
            while (rs.next()) {
                so = new SelectOption();

                so.setLabel(rs.getString("NOT_TYPE"));
                so.setValue(rs.getString("NOT_TYPE"));
                sbModuleList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return sbModuleList;
    }

    @Override
    public List getServiceBookModuleDataList(String empHrmsId, String moduleName,String loginoffcode) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List sbModuleDataList = new ArrayList();
        SbCorrectionRequestModel sbReqBean = null;
        String moduleWiseQry = "";

        try {
            con = this.repodataSource.getConnection();
            switch (moduleName) {
                case "INCREMENT":
                    moduleWiseQry = "SELECT approved_by,rejected_by,approval_date,'' correctionloanid,service_book_correction.is_processed,service_book_correction.is_rejected,service_book_correction.correction_id,service_book_correction.is_submitted,'' refid,service_book_correction.entry_type,EMP_INCR.incrid mid,service_book_correction.requested_sb_language,DOE,SB_DESCRIPTION,emp_notification.NOT_ID ID,NOTE,ORDNO,ORDDT,"
                            + " emp_notification.NOT_TYPE TAB_NAME,ENT_AUTH FROM (SELECT * FROM EMP_INCR WHERE emp_id=? AND PRID IS NULL)"
                            + " EMP_INCR inner join (select * from emp_notification where NOT_TYPE=? and emp_id=? and (if_visible='Y' or if_visible"
                            + " is null or if_visible='')) emp_notification ON EMP_INCR.NOT_ID = emp_notification.NOT_ID"
                            + " left outer join service_book_correction on emp_notification.NOT_ID=service_book_correction.not_id and SB_DESCRIPTION is not null order by orddt";
                    pst = con.prepareStatement(moduleWiseQry);
                    pst.setString(1, empHrmsId);
                    pst.setString(2, moduleName);
                    pst.setString(3, empHrmsId);
                    break;
                case "PROMOTION":
                    moduleWiseQry = "select approved_by,rejected_by,approval_date,'' correctionloanid,service_book_correction.is_processed,service_book_correction.is_rejected,service_book_correction.correction_id,service_book_correction.is_submitted,'' refid,service_book_correction.entry_type,service_book_correction.requested_sb_language,pro_id mid,emp_notification.DOE,SB_DESCRIPTION,emp_notification.NOT_ID ID,NOTE,ORDNO,ORDDT,emp_notification.NOT_TYPE TAB_NAME,ENT_AUTH from"
                            + " (select ent_auth,NOTE,SB_DESCRIPTION,is_validated,if_visible,emp_id,doe,not_id,not_type,ordno,orddt from emp_notification where emp_id=? and not_type='PROMOTION')emp_notification"
                            + " left outer join emp_promotion on emp_notification.not_id=emp_promotion.not_id"
                            + " left outer join g_office on emp_promotion.off_code=g_office.off_code"
                            + " left outer join service_book_correction on emp_notification.NOT_ID=service_book_correction.not_id and SB_DESCRIPTION is not null"
                            + " order by emp_notification.orddt desc, emp_notification.doe desc";
                    pst = con.prepareStatement(moduleWiseQry);
                    pst.setString(1, empHrmsId);
                    break;
                case "LEAVE":
                    moduleWiseQry = "SELECT '' correctionloanid,ENT_AUTH,service_book_correction.requested_sb_language,DOE,SB_DESCRIPTION,emp_notification.NOT_ID ID,NOTE,ORDNO,ORDDT,emp_notification.NOT_TYPE TAB_NAME FROM emp_notification left outer join service_book_correction on emp_notification.NOT_ID=service_book_correction.not_id where EMP_NOTIFICATION.NOT_ID IN (SELECT NOT_ID FROM EMP_LEAVE WHERE EMP_LEAVE.EMP_ID=? AND LSOT_ID='01') AND EMP_NOTIFICATION.EMP_ID=? AND NOT_TYPE='LEAVE' AND"
                            + " STATUS = 'SANCTIONED AND AVAILED'"
                            + " UNION"
                            + " SELECT ENT_AUTH,service_book_correction.requested_sb_language,DOE,SB_DESCRIPTION,emp_notification.NOT_ID ID,NOTE,ORDNO,ORDDT,emp_notification.NOT_TYPE TAB_NAME FROM emp_notification left outer join service_book_correction on emp_notification.NOT_ID=service_book_correction.not_id where EMP_NOTIFICATION.NOT_ID IN (SELECT NOT_ID FROM EMP_LEAVE WHERE EMP_LEAVE.EMP_ID=? AND LSOT_ID='02') AND EMP_NOTIFICATION.EMP_ID=? AND NOT_TYPE='LEAVE'"
                            + " AND STATUS = 'SANCTIONED' AND EMP_NOTIFICATION.IF_VISIBLE='Y'"
                            + " UNION"
                            + " SELECT ENT_AUTH,service_book_correction.requested_sb_language,DOE,SB_DESCRIPTION,emp_notification.NOT_ID ID,NOTE,ORDNO,ORDDT,emp_notification.NOT_TYPE TAB_NAME FROM emp_notification left outer join service_book_correction on emp_notification.NOT_ID=service_book_correction.not_id where EMP_NOTIFICATION.NOT_ID IN (SELECT NOT_ID FROM EMP_LEAVE WHERE EMP_LEAVE.EMP_ID=? AND LSOT_ID!='01' AND LSOT_ID!='02') AND"
                            + " EMP_NOTIFICATION.EMP_ID=? AND NOT_TYPE='LEAVE' AND EMP_NOTIFICATION.IF_VISIBLE='Y'";
                    pst = con.prepareStatement(moduleWiseQry);
                    pst.setString(1, empHrmsId);
                    pst.setString(2, empHrmsId);
                    pst.setString(3, empHrmsId);
                    pst.setString(4, empHrmsId);
                    pst.setString(5, empHrmsId);
                    pst.setString(6, empHrmsId);
                    break;
                case "LOAN":
                    moduleWiseQry = "SELECT approved_by,rejected_by,approval_date,emp_loan_sanc_sb_correction.loanid correctionloanid,service_book_correction.is_processed,service_book_correction.is_rejected,service_book_correction.correction_id,service_book_correction.is_submitted,'' refid,service_book_correction.entry_type,service_book_correction.requested_sb_language,emp_loan_sanc.loanid mid,DOE,SB_DESCRIPTION,emp_notification.NOT_ID ID,NOTE,ORDNO,ORDDT,emp_notification.NOT_TYPE TAB_NAME,ENT_AUTH FROM emp_notification"
                            + " inner join emp_loan_sanc on emp_notification.not_id=emp_loan_sanc.not_id"
                            + " left outer join service_book_correction on emp_notification.NOT_ID=service_book_correction.not_id"
                            + " left outer join emp_loan_sanc_sb_correction on service_book_correction.correction_id=emp_loan_sanc_sb_correction.ref_correction_id"
                            + " WHERE emp_notification.emp_id=? AND IF_VISIBLE='Y'"
                            + " AND emp_loan_sanc.NOT_TYPE='LOAN_SANC'"
                            + " and (emp_loan_sanc.loan_tp='BICA' or emp_loan_sanc.loan_tp='CMPA' or emp_loan_sanc.loan_tp='MOPA' or emp_loan_sanc.loan_tp='MCA' or emp_loan_sanc.loan_tp='HBA' or emp_loan_sanc.loan_tp='VE' or emp_loan_sanc.loan_tp='SHBA') and (SB_DESCRIPTION is not null and SB_DESCRIPTION<>'')";
                    pst = con.prepareStatement(moduleWiseQry);
                    pst.setString(1, empHrmsId);
                    break;
                case "EXAMINATION":
                    moduleWiseQry = "SELECT '' correctionloanid,service_book_correction.requested_sb_language,EMP_ID,DOE,SB_DESCRIPTION,EX_ID ID,NOTE,NOT_NO ORDNO,NOT_DT ORDDT,'EXAMINATION' TAB_NAME,'' ENT_AUTH FROM emp_exam"
                            + " left outer join service_book_correction on emp_exam.EX_ID=service_book_correction.not_id where IF_VISIBLE='Y' AND emp_id=?";
                    pst = con.prepareStatement(moduleWiseQry);
                    pst.setString(1, empHrmsId);
                    break;
                case "TRAINING":
                    moduleWiseQry = "SELECT '' correctionloanid,service_book_correction.requested_sb_language,EMP_ID,DOE,SB_DESCRIPTION,TRAINID ID,NOTE,ord_no ORDNO,ord_date ORDDT,'TRAINING' TAB_NAME,'' ENT_AUTH FROM EMP_TRAIN"
                            + " left outer join service_book_correction on EMP_TRAIN.TRAINID=service_book_correction.not_id where IF_VISIBLE='Y' and emp_id=?";
                    pst = con.prepareStatement(moduleWiseQry);
                    pst.setString(1, empHrmsId);
                    break;
                case "SERVICE VERIFICATION CERTIFICATE":
                    moduleWiseQry = "SELECT approved_by,rejected_by,approval_date,'' correctionloanid,service_book_correction.is_processed,service_book_correction.is_rejected,service_book_correction.correction_id,requested_sb_language,'' ID,service_book_correction.is_submitted,'' refid,service_book_correction.entry_type,EMP_SV.EMP_ID,DOE,SB_DESCRIPTION,SV_ID mid,NOTE,'' ORDNO,tdate ORDDT,'SERVICE VERIFICATION CERTIFICATE' TAB_NAME,'' ENT_AUTH"
                            + " FROM EMP_SV left outer join service_book_correction on EMP_SV.SV_ID=service_book_correction.not_id where EMP_SV.emp_id=?";
                    pst = con.prepareStatement(moduleWiseQry);
                    pst.setString(1, empHrmsId);
                    break;
                case "RELIEVE":
                    /*moduleWiseQry = "SELECT service_book_correction.is_submitted,'' refid,service_book_correction.entry_type,EMP_RELIEVE.relieve_id mid,service_book_correction.requested_sb_language,NOTI.DOE,EMP_RELIEVE.SB_DESCRIPTION,NOTI.NOT_ID ID,"
                     + "NOTI.NOTE,NOTI.ORDNO,NOTI.ORDDT,'RELIEVE' TAB_NAME,NOTI.ENT_AUTH FROM EMP_NOTIFICATION NOTI"
                     + " LEFT OUTER JOIN (SELECT * FROM EMP_RELIEVE WHERE EMP_ID=? AND (JOIN_ID IS NULL OR (JOIN_ID::TEXT NOT IN"
                     + " (SELECT JOIN_ID::TEXT FROM EMP_JOIN WHERE EMP_ID=? AND IF_AD_CHARGE='Y'))))EMP_RELIEVE ON NOTI.NOT_ID=EMP_RELIEVE.NOT_ID"
                     + " left outer join service_book_correction on NOTI.NOT_ID=service_book_correction.not_id and NOTI.NOT_TYPE=service_book_correction.NOT_TYPE"
                     + " WHERE EMP_RELIEVE.EMP_ID=? and (NOTI.not_type ='REHABILITATION' or"
                     + " NOTI.not_type='ABSORPTION' or NOTI.not_type='REDEPLOYMENT' or NOTI.not_type='VALIDATION' or NOTI.not_type='LT_TRAINING' or"
                     + " NOTI.not_type='DEPUTATION' or NOTI.not_type ='TRANSFER' or NOTI.not_type ='PROMOTION' or NOTI.not_type ='REDESIGNATION' or"
                     + " NOTI.not_type ='RESIGNATION' or NOTI.not_type ='DECEASED' or NOTI.not_type ='RETIREMENT' or NOTI.not_type='ALLOT_CADRE' or (NOTI.not_type='LEAVE'"
                     + " AND NOTI.NOT_ID IN (SELECT NOT_ID FROM EMP_LEAVE WHERE EMP_ID=? AND IF_LONGTERM='Y')))"
                     + " and EMP_RELIEVE.IF_VISIBLE='Y' AND EMP_RELIEVE.emp_id=? AND EMP_RELIEVE.NOT_TYPE!='CHNG_STRUCTURE' AND EMP_RELIEVE.NOT_TYPE!='SUSPENSION' and EMP_RELIEVE.NOT_ID is not null";*/
                    moduleWiseQry = "select approved_by,rejected_by,approval_date,'' correctionloanid,service_book_correction.is_processed,service_book_correction.is_rejected,service_book_correction.correction_id,NOTI.not_id ID,service_book_correction.is_submitted,'' refid,service_book_correction.entry_type,EMP_RELIEVE.relieve_id mid,service_book_correction.requested_sb_language,NOTI.DOE,EMP_RELIEVE.SB_DESCRIPTION,"
                            + " NOTI.NOT_ID ID,NOTI.NOTE,NOTI.ORDNO,NOTI.ORDDT,'RELIEVE' TAB_NAME,NOTI.ENT_AUTH from emp_notification NOTI"
                            + " left outer join emp_relieve on NOTI.not_id=emp_relieve.not_id"
                            + " left outer join (select * from service_book_correction where not_type='RELIEVE')service_book_correction on emp_relieve.NOT_ID=service_book_correction.not_id"
                            + " WHERE EMP_RELIEVE.EMP_ID=? and EMP_RELIEVE.IF_VISIBLE='Y' AND EMP_RELIEVE.NOT_ID is not null";
                    pst = con.prepareStatement(moduleWiseQry);
                    pst.setString(1, empHrmsId);
                    break;
                case "JOINING":
                    /*moduleWiseQry = "SELECT service_book_correction.is_submitted,reli.relieve_id refid,service_book_correction.entry_type,joi.join_id mid,service_book_correction.requested_sb_language,joi.EMP_ID,joi.DOE,joi.SB_DESCRIPTION,joi.NOT_ID ID,joi.NOTE,'JOINING' TAB_NAME,joi.MEMO_DATE ORDDT,joi.ENT_AUTH"
                     + " FROM emp_notification"
                     + " left outer join service_book_correction on emp_notification.NOT_ID=service_book_correction.not_id and emp_notification.NOT_TYPE=service_book_correction.NOT_TYPE"
                     + " left outer join emp_relieve reli on emp_notification.emp_id=reli.emp_id and"
                     + " emp_notification.not_id=reli.not_id and reli.join_id not in (Select join_id from emp_join where EMP_ID=? AND if_ad_charge='Y')"
                     + " left outer join emp_join joi on emp_notification.emp_id=joi.emp_id and emp_notification.not_id=joi.not_id"
                     + " left outer join g_spc on joi.ent_auth=g_spc.spc where joi.IF_VISIBLE='Y' AND joi.emp_id=? AND joi.NOT_TYPE!='CHNG_STRUCTURE' AND joi.NOT_TYPE!='ADDITIONAL_CHARGE' and"
                     + " (emp_notification.not_type='FIRST_APPOINTMENT' or emp_notification.not_type='REHABILITATION' or emp_notification.not_type='ABSORPTION' or emp_notification.not_type='REDEPLOYMENT' or"
                     + " emp_notification.not_type='VALIDATION' or emp_notification.not_type='LT_TRAINING' or emp_notification.not_type='DEPUTATION' or emp_notification.not_type='POSTING' or emp_notification.not_type='TRANSFER'"
                     + " or emp_notification.not_type='PROMOTION' or emp_notification.not_type='ADDITIONAL_CHARGE' or emp_notification.not_type='ALLOT_CADRE' or emp_notification.not_type='REDESIGNATION' or"
                     + " emp_notification.not_type='CHNG_STRUCTURE')";*/
                    moduleWiseQry = "select approved_by,rejected_by,approval_date,'' correctionloanid,service_book_correction.is_processed,service_book_correction.is_rejected,service_book_correction.correction_id,emp_notification.not_id,service_book_correction.is_submitted,'' refid,service_book_correction.entry_type,EMP_JOIN.join_id mid,service_book_correction.requested_sb_language,emp_notification.DOE,EMP_JOIN.SB_DESCRIPTION,"
                            + " emp_notification.NOT_ID ID,emp_notification.NOTE,emp_notification.ORDNO,emp_notification.ORDDT,'JOINING' TAB_NAME,emp_notification.ENT_AUTH from emp_notification"
                            + " left outer join EMP_JOIN on emp_notification.not_id=EMP_JOIN.not_id"
                            + " left outer join (select * from service_book_correction where not_type='JOINING')service_book_correction on EMP_JOIN.NOT_ID=service_book_correction.not_id"
                            + " WHERE EMP_JOIN.EMP_ID=? and EMP_JOIN.IF_VISIBLE='Y' AND EMP_JOIN.NOT_ID is not null";
                    pst = con.prepareStatement(moduleWiseQry);
                    pst.setString(1, empHrmsId);
                    break;
                case "REINSTATEMENT":
                    moduleWiseQry = "SELECT '' correctionloanid,service_book_correction.requested_sb_language,EMP_REINSTATEMENT.EMP_ID,EMP_REINSTATEMENT.DOE,EMP_NOTIFICATION.SB_DESCRIPTION,reinstatement_id ID,EMP_REINSTATEMENT.NOTE,EMP_REINSTATEMENT.ORDNO,EMP_REINSTATEMENT.ORDDT,'REINSTATEMENT' TAB_NAME,ENT_AUTH FROM EMP_REINSTATEMENT"
                            + " EMP_REINSTATEMENT INNER JOIN EMP_SUSPENSION ON EMP_REINSTATEMENT.REINSTATEMENT_ID=EMP_SUSPENSION.SP_ID"
                            + " INNER JOIN EMP_NOTIFICATION ON EMP_REINSTATEMENT.NOT_ID=EMP_NOTIFICATION.NOT_ID"
                            + " left outer join service_book_correction on emp_notification.NOT_ID=service_book_correction.not_id"
                            + " where EMP_REINSTATEMENT.emp_id=?"
                            + " AND EMP_SUSPENSION.emp_id=? and EMP_NOTIFICATION.IF_VISIBLE='Y'";
                    pst = con.prepareStatement(moduleWiseQry);
                    pst.setString(1, empHrmsId);
                    pst.setString(2, empHrmsId);
                    break;
                case "SUSPENSION":
                    moduleWiseQry = "SELECT '' correctionloanid,service_book_correction.requested_sb_language,EMP_SUSPENSION.EMP_ID,EMP_SUSPENSION.DOE,EMP_SUSPENSION.SB_DESCRIPTION,SP_ID ID,EMP_SUSPENSION.NOTE,EMP_SUSPENSION.ORDNO,EMP_SUSPENSION.ORDDT,'SUSPENSION' TAB_NAME,ENT_AUTH FROM EMP_SUSPENSION"
                            + " INNER JOIN EMP_NOTIFICATION ON EMP_SUSPENSION.NOT_ID=EMP_NOTIFICATION.NOT_ID"
                            + " left outer join service_book_correction on emp_notification.NOT_ID=service_book_correction.not_id"
                            + " where EMP_SUSPENSION.IF_VISIBLE='Y' AND EMP_SUSPENSION.emp_id=? AND EMP_NOTIFICATION.emp_id=? AND (HQ_FIX IS NULL OR HQ_FIX = '')";
                    pst = con.prepareStatement(moduleWiseQry);
                    pst.setString(1, empHrmsId);
                    break;
                case "HEAD QUARTER FIXATION":
                    moduleWiseQry = "SELECT '' correctionloanid,service_book_correction.requested_sb_language,EMP_ID,DOE,SB_DESCRIPTION,SP_ID ID,NOTE,ORDNO,ORDDT,'HEAD QUARTER FIXATION' TAB_NAME,'' ENT_AUTH FROM EMP_SUSPENSION"
                            + " left outer join service_book_correction on EMP_SUSPENSION.SP_ID=service_book_correction.not_id where IF_VISIBLE='Y' AND emp_id=? AND HQ_FIX IS NOT NULL";
                    pst = con.prepareStatement(moduleWiseQry);
                    pst.setString(1, empHrmsId);
                    break;
                case "PERMISSION":
                    moduleWiseQry = "SELECT '' correctionloanid,service_book_correction.requested_sb_language,EMP_ID,DOE,SB_DESCRIPTION,PER_ID ID,NOTE,ORD_NO ORDNO,ORD_DATE ORDDT,'PERMISSION' TAB_NAME,'' ENT_AUTH FROM EMP_PERMISSION"
                            + " left outer join service_book_correction on EMP_PERMISSION.PER_ID=service_book_correction.not_id where emp_id=? AND IF_VISIBLE='Y'";
                    pst = con.prepareStatement(moduleWiseQry);
                    pst.setString(1, empHrmsId);
                    break;
                case "EDUCATION":
                    moduleWiseQry = "SELECT '' correctionloanid,service_book_correction.requested_sb_language,EMP_ID,DOE,SB_DESCRIPTION,QFN_ID ID,NOTE,'' ORDNO,NULL ORDDT,'EDUCATION' TAB_NAME,'' ENT_AUTH FROM EMP_QUALIFICATION"
                            + " left outer join service_book_correction on EMP_QUALIFICATION.QFN_ID=service_book_correction.not_id where emp_id=? AND DOE IS NOT NULL AND (SB_DESCRIPTION IS NOT NULL AND SB_DESCRIPTION<>'')";
                    pst = con.prepareStatement(moduleWiseQry);
                    pst.setString(1, empHrmsId);
                    break;
                case "RETIREMENT":
                    moduleWiseQry = "SELECT '' correctionloanid,service_book_correction.requested_sb_language,EMP_ID,DOE,SB_DESCRIPTION,RET_ID ID,NOTE,ORD_NO ORDNO,ORD_DATE ORDDT,'RETIREMENT' TAB_NAME,'' ENT_AUTH FROM EMP_RET_RES"
                            + " left outer join service_book_correction on EMP_RET_RES.RET_ID=service_book_correction.not_id where emp_id=?";
                    pst = con.prepareStatement(moduleWiseQry);
                    pst.setString(1, empHrmsId);
                    break;
                case "SERVICE RECORD":
                    moduleWiseQry = "SELECT '' correctionloanid,service_book_correction.requested_sb_language,EMP_ID,DOE,'' SB_DESCRIPTION,SR_ID ID,NOTE,'' ORDNO,NULL ORDDT,'SERVICE RECORD' TAB_NAME,'' ENT_AUTH FROM EMP_SERVICERECORD"
                            + " left outer join service_book_correction on EMP_SERVICERECORD.SR_ID=service_book_correction.not_id WHERE EMP_ID=?";
                    pst = con.prepareStatement(moduleWiseQry);
                    pst.setString(1, empHrmsId);
                    break;
                case "SERVICE PAY RECORD":
                    moduleWiseQry = "SELECT '' correctionloanid,service_book_correction.requested_sb_language,EMP_ID,DOE,'' SB_DESCRIPTION,SRP_ID ID,'' NOTE,'' ORDNO,NULL ORDDT,'SERVICE PAY RECORD' TAB_NAME,'' ENT_AUTH FROM EMP_SR_PAY"
                            + " left outer join service_book_correction on EMP_SR_PAY.SRP_ID=service_book_correction.not_id WHERE EMP_ID=?";
                    pst = con.prepareStatement(moduleWiseQry);
                    pst.setString(1, empHrmsId);
                    break;
                default:
                    moduleWiseQry = "SELECT approved_by,rejected_by,approval_date,'' correctionloanid,service_book_correction.is_processed,service_book_correction.is_rejected,service_book_correction.correction_id,service_book_correction.is_submitted,'' refid,service_book_correction.entry_type,'' mid,service_book_correction.requested_sb_language,emp_notification.EMP_ID,DOE,SB_DESCRIPTION,emp_notification.NOT_ID ID,NOTE,ORDNO,ORDDT,emp_notification.NOT_TYPE TAB_NAME,ENT_AUTH FROM emp_notification"
                            + " left outer join service_book_correction on emp_notification.NOT_ID=service_book_correction.not_id WHERE emp_notification.emp_id=? AND IF_VISIBLE='Y' AND emp_notification.NOT_TYPE=? and emp_notification.NOT_TYPE!='CHNG_STRUCTURE' AND emp_notification.NOT_TYPE!='LEAVE' AND emp_notification.NOT_TYPE!='RESULT'"
                            + " AND emp_notification.NOT_TYPE!='COMPLIANCE' AND emp_notification.NOT_TYPE!='SC_NOTICE' AND emp_notification.NOT_TYPE!='INCREMENT' and emp_notification.NOT_TYPE!='LOAN_SANC' and emp_notification.NOT_TYPE!='REINSTATEMENT' and emp_notification.NOT_TYPE!='SUSPENSION' and (SB_DESCRIPTION is not null and SB_DESCRIPTION<>'')";
                    pst = con.prepareStatement(moduleWiseQry);
                    pst.setString(1, empHrmsId);
                    pst.setString(2, moduleName);
                    break;
            }
            rs = pst.executeQuery();
            while (rs.next()) {
                sbReqBean = new SbCorrectionRequestModel();

                sbReqBean.setModuleid(rs.getString("mid"));
                sbReqBean.setEmpHrmsId(empHrmsId);
                sbReqBean.setNoteIdEnc(CommonFunctions.encodedTxt(rs.getString("ID")));
                sbReqBean.setNotId(rs.getString("ID"));
                sbReqBean.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOE")));
                sbReqBean.setSbDescription(rs.getString("SB_DESCRIPTION"));
                sbReqBean.setTabName(rs.getString("TAB_NAME"));
                sbReqBean.setModuleNote(rs.getString("NOTE"));
                sbReqBean.setEntryByAuth(rs.getString("ENT_AUTH"));
                sbReqBean.setOrderDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("ORDDT")));
                sbReqBean.setSbLanguageRequested(rs.getString("requested_sb_language"));
                setWefDate(sbReqBean);
                sbReqBean.setEntrytype(rs.getString("entry_type"));
                sbReqBean.setModuleRefId(rs.getString("refid"));
                sbReqBean.setIsSubmitted(rs.getString("is_submitted"));
                if(sbReqBean.getIsSubmitted() != null && sbReqBean.getIsSubmitted().equals("Y")){
                    sbReqBean.setIsSubmitted("Submitted to "+getDDOName(loginoffcode));
                }
                sbReqBean.setCorrectionid(rs.getString("correction_id"));
                if (sbReqBean.getCorrectionid() != null && !sbReqBean.getCorrectionid().equals("")) {
                    if (sbReqBean.getTabName() != null && sbReqBean.getTabName().equals("LOAN_SANC")) {
                        sbReqBean.setModuleid(rs.getString("correctionloanid"));
                    }
                }
                sbReqBean.setIsProcessed(rs.getString("is_processed"));                
                sbReqBean.setIsRejected(rs.getString("is_rejected"));
                if(sbReqBean.getIsProcessed() != null && sbReqBean.getIsProcessed().equals("Y")){
                    sbReqBean.setApprovalStatus(getApprovedRejectedByDetails(sbReqBean.getIsProcessed(),sbReqBean.getIsRejected(),rs.getString("approved_by"),rs.getDate("approval_date")));
                }else if(sbReqBean.getIsRejected() != null && sbReqBean.getIsRejected().equals("Y")){
                    sbReqBean.setApprovalStatus(getApprovedRejectedByDetails(sbReqBean.getIsProcessed(),sbReqBean.getIsRejected(),rs.getString("rejected_by"),rs.getDate("approval_date")));
                }
                sbModuleDataList.add(sbReqBean);
            }

            DataBaseFunctions.closeSqlObjects(rs, pst);

            moduleWiseQry = "SELECT * from service_book_correction WHERE EMP_ID=? AND NOT_TYPE=? and ENTRY_TYPE='NEW'";
            pst = con.prepareStatement(moduleWiseQry);
            pst.setString(1, empHrmsId);
            if (moduleName != null && moduleName.equals("LOAN")) {
                pst.setString(2, "LOAN_SANC");
            } else {
                pst.setString(2, moduleName);
            }
            rs = pst.executeQuery();
            while (rs.next()) {
                sbReqBean = new SbCorrectionRequestModel();

                sbReqBean.setEmpHrmsId(empHrmsId);
                sbReqBean.setNotId(rs.getString("NOT_ID"));
                sbReqBean.setSbLanguageRequested(rs.getString("requested_sb_language"));
                sbReqBean.setEntrytype(rs.getString("entry_type"));
                sbReqBean.setIsSubmitted(rs.getString("is_submitted"));
                sbModuleDataList.add(sbReqBean);
            }

            Collections.sort(sbModuleDataList, new Comparator<SbCorrectionRequestModel>() {
                @Override
                public int compare(SbCorrectionRequestModel o1, SbCorrectionRequestModel o2) {
                    Date date1 = CommonFunctions.getDateFromString(o1.getOrderDate(), "dd-MMM-yyyy");
                    Date date2 = CommonFunctions.getDateFromString(o2.getOrderDate(), "dd-MMM-yyyy");
                    return date1.compareTo(date2);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return sbModuleDataList;
    }

    @Override
    public SbCorrectionRequestModel getServiceBookModuleData(String empHrmsId, String moduleName, String noteId) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        SbCorrectionRequestModel sbReqBean = new SbCorrectionRequestModel();
        String moduleWiseQry = "";

        try {
            con = this.repodataSource.getConnection();
            switch (moduleName) {
                case "INCREMENT":
                    moduleWiseQry = "SELECT emp_notification.EMP_ID,DOE,ORDDT,NOTE,SB_DESCRIPTION,emp_notification.NOT_ID ID,"
                            + "emp_notification.NOT_TYPE TAB_NAME FROM (SELECT * FROM EMP_INCR WHERE emp_id=? AND PRID IS NULL) "
                            + "EMP_INCR inner join (select * from emp_notification where NOT_TYPE=? and emp_id=? and emp_notification.NOT_ID=? and (if_visible='Y' or if_visible "
                            + "is null or if_visible='')) emp_notification ON EMP_INCR.NOT_ID = emp_notification.NOT_ID and SB_DESCRIPTION is not null";
                    pst = con.prepareStatement(moduleWiseQry);
                    pst.setString(1, empHrmsId);
                    pst.setString(2, moduleName);
                    pst.setString(3, empHrmsId);
                    pst.setInt(4, Integer.parseInt(noteId));
                    break;
                case "LEAVE":
                    moduleWiseQry = "SELECT ENT_AUTH,service_book_correction.requested_sb_language,DOE,SB_DESCRIPTION,emp_notification.NOT_ID ID,NOTE,ORDNO,ORDDT,emp_notification.NOT_TYPE TAB_NAME FROM emp_notification left outer join service_book_correction on emp_notification.NOT_ID=service_book_correction.not_id where EMP_NOTIFICATION.NOT_ID IN (SELECT NOT_ID FROM EMP_LEAVE WHERE EMP_LEAVE.EMP_ID=? AND LSOT_ID='01') AND EMP_NOTIFICATION.EMP_ID=? AND NOT_TYPE='LEAVE' AND"
                            + " STATUS = 'SANCTIONED AND AVAILED' and emp_notification.NOT_ID=?"
                            + " UNION"
                            + " SELECT ENT_AUTH,service_book_correction.requested_sb_language,DOE,SB_DESCRIPTION,emp_notification.NOT_ID ID,NOTE,ORDNO,ORDDT,emp_notification.NOT_TYPE TAB_NAME FROM emp_notification left outer join service_book_correction on emp_notification.NOT_ID=service_book_correction.not_id where EMP_NOTIFICATION.NOT_ID IN (SELECT NOT_ID FROM EMP_LEAVE WHERE EMP_LEAVE.EMP_ID=? AND LSOT_ID='02') AND EMP_NOTIFICATION.EMP_ID=? AND NOT_TYPE='LEAVE'"
                            + " AND STATUS = 'SANCTIONED' AND EMP_NOTIFICATION.IF_VISIBLE='Y' and emp_notification.NOT_ID=?"
                            + " UNION"
                            + " SELECT ENT_AUTH,service_book_correction.requested_sb_language,DOE,SB_DESCRIPTION,emp_notification.NOT_ID ID,NOTE,ORDNO,ORDDT,emp_notification.NOT_TYPE TAB_NAME FROM emp_notification left outer join service_book_correction on emp_notification.NOT_ID=service_book_correction.not_id where EMP_NOTIFICATION.NOT_ID IN (SELECT NOT_ID FROM EMP_LEAVE WHERE EMP_LEAVE.EMP_ID=? AND LSOT_ID!='01' AND LSOT_ID!='02') AND"
                            + " EMP_NOTIFICATION.EMP_ID=? AND NOT_TYPE='LEAVE' AND EMP_NOTIFICATION.IF_VISIBLE='Y' and emp_notification.NOT_ID=?";
                    pst = con.prepareStatement(moduleWiseQry);
                    pst.setString(1, empHrmsId);
                    pst.setString(2, empHrmsId);
                    pst.setInt(3, Integer.parseInt(noteId));
                    pst.setString(4, empHrmsId);
                    pst.setString(5, empHrmsId);
                    pst.setInt(6, Integer.parseInt(noteId));
                    pst.setString(7, empHrmsId);
                    pst.setString(8, empHrmsId);
                    pst.setInt(9, Integer.parseInt(noteId));
                    break;
                case "LOAN":
                    moduleWiseQry = "SELECT service_book_correction.requested_sb_language,DOE,SB_DESCRIPTION,emp_notification.NOT_ID ID,NOTE,ORDNO,ORDDT,emp_notification.NOT_TYPE TAB_NAME FROM emp_notification"
                            + " inner join emp_loan_sanc on emp_notification.not_id=emp_loan_sanc.not_id"
                            + " left outer join service_book_correction on emp_notification.NOT_ID=service_book_correction.not_id"
                            + " WHERE emp_notification.emp_id=? and emp_notification.NOT_ID=? AND IF_VISIBLE='Y'"
                            + " AND emp_loan_sanc.NOT_TYPE='LOAN_SANC'"
                            + " and (emp_loan_sanc.loan_tp='BICA' or emp_loan_sanc.loan_tp='CMPA' or emp_loan_sanc.loan_tp='MOPA' or emp_loan_sanc.loan_tp='MCA' or emp_loan_sanc.loan_tp='HBA' or emp_loan_sanc.loan_tp='VE' or emp_loan_sanc.loan_tp='SHBA') and (SB_DESCRIPTION is not null and SB_DESCRIPTION<>'')";
                    pst = con.prepareStatement(moduleWiseQry);
                    pst.setString(1, empHrmsId);
                    pst.setInt(2, Integer.parseInt(noteId));
                    break;
                case "EXAMINATION":
                    moduleWiseQry = "SELECT service_book_correction.requested_sb_language,EMP_ID,DOE,SB_DESCRIPTION,EX_ID ID,NOTE,NOT_NO ORDNO,NOT_DT ORDDT,'EXAMINATION' TAB_NAME,'' ENT_AUTH FROM emp_exam"
                            + " left outer join service_book_correction on emp_exam.EX_ID=service_book_correction.not_id where IF_VISIBLE='Y' AND emp_id=? and EX_ID=?";
                    pst = con.prepareStatement(moduleWiseQry);
                    pst.setString(1, empHrmsId);
                    pst.setInt(2, Integer.parseInt(noteId));
                    break;
                case "TRAINING":
                    moduleWiseQry = "SELECT service_book_correction.requested_sb_language,EMP_ID,DOE,SB_DESCRIPTION,TRAINID ID,NOTE,ord_no ORDNO,ord_date ORDDT,'TRAINING' TAB_NAME,'' ENT_AUTH FROM EMP_TRAIN"
                            + " left outer join service_book_correction on EMP_TRAIN.TRAINID=service_book_correction.not_id where IF_VISIBLE='Y' and emp_id=? and TRAINID=?";
                    pst = con.prepareStatement(moduleWiseQry);
                    pst.setString(1, empHrmsId);
                    pst.setInt(2, Integer.parseInt(noteId));
                    break;
                case "SERVICE VERIFICATION CERTIFICATE":
                    moduleWiseQry = "SELECT service_book_correction.requested_sb_language,EMP_SV.EMP_ID,DOE,SB_DESCRIPTION,SV_ID ID,NOTE,'' ORDNO,tdate ORDDT,'SERVICE VERIFICATION CERTIFICATE' TAB_NAME,'' ENT_AUTH "
                            + " FROM EMP_SV left outer join service_book_correction on EMP_SV.SV_ID=service_book_correction.not_id where EMP_SV.emp_id=? and SV_ID=?";
                    pst = con.prepareStatement(moduleWiseQry);
                    pst.setString(1, empHrmsId);
                    pst.setInt(2, Integer.parseInt(noteId));
                    break;
                case "RELIEVE":
                    moduleWiseQry = "SELECT service_book_correction.requested_sb_language,EMP_RELIEVE.EMP_ID,EMP_RELIEVE.DOE,EMP_RELIEVE.SB_DESCRIPTION,EMP_RELIEVE.NOT_ID ID,EMP_RELIEVE.NOTE,"
                            + " MEMO_NO ORDNO,MEMO_DATE ORDDT,'RELIEVE' TAB_NAME,NOTI.ENT_AUTH FROM EMP_NOTIFICATION NOTI"
                            + " LEFT OUTER JOIN (SELECT * FROM EMP_RELIEVE WHERE EMP_ID=? AND (JOIN_ID IS NULL OR (JOIN_ID::TEXT NOT IN"
                            + " (SELECT JOIN_ID::TEXT FROM EMP_JOIN WHERE EMP_ID=? AND IF_AD_CHARGE='Y'))))EMP_RELIEVE ON NOTI.NOT_ID=EMP_RELIEVE.NOT_ID"
                            + " left outer join service_book_correction on NOTI.NOT_ID=service_book_correction.not_id"
                            + " WHERE EMP_RELIEVE.EMP_ID=? and NOTI.NOT_ID=? and (NOTI.not_type ='REHABILITATION' or"
                            + " NOTI.not_type='ABSORPTION' or NOTI.not_type='REDEPLOYMENT' or NOTI.not_type='VALIDATION' or NOTI.not_type='LT_TRAINING' or"
                            + " NOTI.not_type='DEPUTATION' or NOTI.not_type ='TRANSFER' or NOTI.not_type ='PROMOTION' or NOTI.not_type ='REDESIGNATION' or"
                            + " NOTI.not_type ='RESIGNATION' or NOTI.not_type ='DECEASED' or NOTI.not_type ='RETIREMENT' or NOTI.not_type='ALLOT_CADRE' or (NOTI.not_type='LEAVE'"
                            + " AND NOTI.NOT_ID IN (SELECT NOT_ID FROM EMP_LEAVE WHERE EMP_ID=? AND IF_LONGTERM='Y')))"
                            + " and EMP_RELIEVE.IF_VISIBLE='Y' AND EMP_RELIEVE.emp_id=? AND EMP_RELIEVE.NOT_TYPE!='CHNG_STRUCTURE' AND EMP_RELIEVE.NOT_TYPE!='SUSPENSION' and EMP_RELIEVE.NOT_ID is not null";
                    pst = con.prepareStatement(moduleWiseQry);
                    pst.setString(1, empHrmsId);
                    pst.setInt(2, Integer.parseInt(noteId));
                    pst.setString(3, empHrmsId);
                    pst.setString(4, empHrmsId);
                    pst.setString(5, empHrmsId);
                    pst.setString(6, empHrmsId);
                    break;
                case "JOINING":
                    moduleWiseQry = "SELECT service_book_correction.requested_sb_language,joi.EMP_ID,joi.DOE,joi.SB_DESCRIPTION,joi.NOT_ID ID,joi.NOTE,'JOINING' TAB_NAME,joi.MEMO_DATE ORDDT,joi.ENT_AUTH"
                            + " FROM emp_notification"
                            + " left outer join service_book_correction on emp_notification.NOT_ID=service_book_correction.not_id"
                            + " left outer join emp_relieve reli on emp_notification.emp_id=reli.emp_id and"
                            + " emp_notification.not_id=reli.not_id and reli.join_id not in (Select join_id from emp_join where EMP_ID=? AND if_ad_charge='Y')"
                            + " left outer join emp_join joi on emp_notification.emp_id=joi.emp_id and emp_notification.not_id=joi.not_id"
                            + " left outer join g_spc on joi.ent_auth=g_spc.spc where joi.IF_VISIBLE='Y' AND joi.emp_id=? AND emp_notification.NOT_ID=? AND joi.NOT_TYPE!='CHNG_STRUCTURE' AND joi.NOT_TYPE!='ADDITIONAL_CHARGE' and"
                            + " (emp_notification.not_type='FIRST_APPOINTMENT' or emp_notification.not_type='REHABILITATION' or emp_notification.not_type='ABSORPTION' or emp_notification.not_type='REDEPLOYMENT' or"
                            + " emp_notification.not_type='VALIDATION' or emp_notification.not_type='LT_TRAINING' or emp_notification.not_type='DEPUTATION' or emp_notification.not_type='POSTING' or emp_notification.not_type='TRANSFER'"
                            + " or emp_notification.not_type='PROMOTION' or emp_notification.not_type='ADDITIONAL_CHARGE' or emp_notification.not_type='ALLOT_CADRE' or emp_notification.not_type='REDESIGNATION' or"
                            + " emp_notification.not_type='CHNG_STRUCTURE')";
                    pst = con.prepareStatement(moduleWiseQry);
                    pst.setString(1, empHrmsId);
                    pst.setString(2, empHrmsId);
                    pst.setInt(3, Integer.parseInt(noteId));
                    break;
                case "REINSTATEMENT":
                    moduleWiseQry = "SELECT service_book_correction.requested_sb_language,EMP_REINSTATEMENT.EMP_ID,EMP_REINSTATEMENT.DOE,EMP_NOTIFICATION.SB_DESCRIPTION,reinstatement_id ID,EMP_REINSTATEMENT.NOTE,EMP_REINSTATEMENT.ORDNO,EMP_REINSTATEMENT.ORDDT,'REINSTATEMENT' TAB_NAME,ENT_AUTH FROM EMP_REINSTATEMENT"
                            + " EMP_REINSTATEMENT INNER JOIN EMP_SUSPENSION ON EMP_REINSTATEMENT.REINSTATEMENT_ID=EMP_SUSPENSION.SP_ID"
                            + " INNER JOIN EMP_NOTIFICATION ON EMP_REINSTATEMENT.NOT_ID=EMP_NOTIFICATION.NOT_ID"
                            + " left outer join service_book_correction on emp_notification.NOT_ID=service_book_correction.not_id"
                            + " where EMP_REINSTATEMENT.emp_id=?"
                            + " AND EMP_SUSPENSION.emp_id=? AND EMP_NOTIFICATION.NOT_ID=? and EMP_NOTIFICATION.IF_VISIBLE='Y'";
                    pst = con.prepareStatement(moduleWiseQry);
                    pst.setString(1, empHrmsId);
                    pst.setString(2, empHrmsId);
                    pst.setInt(3, Integer.parseInt(noteId));
                    break;
                case "SUSPENSION":
                    moduleWiseQry = "SELECT service_book_correction.requested_sb_language,EMP_SUSPENSION.EMP_ID,EMP_SUSPENSION.DOE,EMP_SUSPENSION.SB_DESCRIPTION,SP_ID ID,EMP_SUSPENSION.NOTE,EMP_SUSPENSION.ORDNO,EMP_SUSPENSION.ORDDT,'SUSPENSION' TAB_NAME,ENT_AUTH FROM EMP_SUSPENSION"
                            + " INNER JOIN EMP_NOTIFICATION ON EMP_SUSPENSION.NOT_ID=EMP_NOTIFICATION.NOT_ID"
                            + " left outer join service_book_correction on emp_notification.NOT_ID=service_book_correction.not_id"
                            + " where EMP_SUSPENSION.IF_VISIBLE='Y' AND EMP_SUSPENSION.emp_id=? AND EMP_NOTIFICATION.emp_id=? AND EMP_NOTIFICATION.NOT_ID=? AND (HQ_FIX IS NULL OR HQ_FIX = '')";
                    pst = con.prepareStatement(moduleWiseQry);
                    pst.setString(1, empHrmsId);
                    pst.setInt(2, Integer.parseInt(noteId));
                    break;
                case "HEAD QUARTER FIXATION":
                    moduleWiseQry = "SELECT service_book_correction.requested_sb_language,EMP_ID,DOE,SB_DESCRIPTION,SP_ID ID,NOTE,ORDNO,ORDDT,'HEAD QUARTER FIXATION' TAB_NAME,'' ENT_AUTH FROM EMP_SUSPENSION"
                            + " left outer join service_book_correction on EMP_SUSPENSION.SP_ID=service_book_correction.not_id where IF_VISIBLE='Y' AND emp_id=? AND EMP_SUSPENSION.SP_ID=? AND HQ_FIX IS NOT NULL";
                    pst = con.prepareStatement(moduleWiseQry);
                    pst.setString(1, empHrmsId);
                    pst.setInt(2, Integer.parseInt(noteId));
                    break;
                case "PERMISSION":
                    moduleWiseQry = "SELECT service_book_correction.requested_sb_language,EMP_ID,DOE,SB_DESCRIPTION,PER_ID ID,NOTE,ORD_NO ORDNO,ORD_DATE ORDDT,'PERMISSION' TAB_NAME,'' ENT_AUTH FROM EMP_PERMISSION"
                            + " left outer join service_book_correction on EMP_PERMISSION.PER_ID=service_book_correction.not_id where emp_id=? AND EMP_PERMISSION.PER_ID=? AND IF_VISIBLE='Y'";
                    pst = con.prepareStatement(moduleWiseQry);
                    pst.setString(1, empHrmsId);
                    pst.setInt(2, Integer.parseInt(noteId));
                    break;
                case "EDUCATION":
                    moduleWiseQry = "SELECT service_book_correction.requested_sb_language,EMP_ID,DOE,SB_DESCRIPTION,QFN_ID ID,NOTE,'' ORDNO,NULL ORDDT,'EDUCATION' TAB_NAME,'' ENT_AUTH FROM EMP_QUALIFICATION"
                            + " left outer join service_book_correction on EMP_QUALIFICATION.QFN_ID=service_book_correction.not_id where emp_id=? AND EMP_QUALIFICATION.QFN_ID=? AND DOE IS NOT NULL AND (SB_DESCRIPTION IS NOT NULL AND SB_DESCRIPTION<>'')";
                    pst = con.prepareStatement(moduleWiseQry);
                    pst.setString(1, empHrmsId);
                    pst.setInt(2, Integer.parseInt(noteId));
                    break;
                case "RETIREMENT":
                    moduleWiseQry = "SELECT service_book_correction.requested_sb_language,EMP_ID,DOE,SB_DESCRIPTION,RET_ID ID,NOTE,ORD_NO ORDNO,ORD_DATE ORDDT,'RETIREMENT' TAB_NAME,'' ENT_AUTH FROM EMP_RET_RES"
                            + " left outer join service_book_correction on EMP_RET_RES.RET_ID=service_book_correction.not_id where emp_id=? AND EMP_RET_RES.RET_ID=?";
                    pst = con.prepareStatement(moduleWiseQry);
                    pst.setString(1, empHrmsId);
                    pst.setInt(2, Integer.parseInt(noteId));
                    break;
                case "SERVICE RECORD":
                    moduleWiseQry = "SELECT service_book_correction.requested_sb_language,EMP_ID,DOE,'' SB_DESCRIPTION,SR_ID ID,NOTE,'' ORDNO,NULL ORDDT,'SERVICE RECORD' TAB_NAME,'' ENT_AUTH FROM EMP_SERVICERECORD"
                            + " left outer join service_book_correction on EMP_SERVICERECORD.SR_ID=service_book_correction.not_id WHERE EMP_ID=? AND EMP_SERVICERECORD.SR_ID=?";
                    pst = con.prepareStatement(moduleWiseQry);
                    pst.setString(1, empHrmsId);
                    pst.setInt(2, Integer.parseInt(noteId));
                    break;
                case "SERVICE PAY RECORD":
                    moduleWiseQry = "SELECT service_book_correction.requested_sb_language,EMP_ID,DOE,'' SB_DESCRIPTION,SRP_ID ID,'' NOTE,'' ORDNO,NULL ORDDT,'SERVICE PAY RECORD' TAB_NAME,'' ENT_AUTH FROM EMP_SR_PAY"
                            + " left outer join service_book_correction on EMP_SR_PAY.SRP_ID=service_book_correction.not_id WHERE EMP_ID=? AND EMP_SR_PAY.SRP_ID=?";
                    pst = con.prepareStatement(moduleWiseQry);
                    pst.setString(1, empHrmsId);
                    pst.setInt(2, Integer.parseInt(noteId));
                    break;
                default:
                    moduleWiseQry = "SELECT service_book_correction.requested_sb_language,emp_notification.EMP_ID,DOE,SB_DESCRIPTION,emp_notification.NOT_ID ID,NOTE,ORDNO,ORDDT,emp_notification.NOT_TYPE TAB_NAME,ENT_AUTH FROM emp_notification"
                            + " left outer join service_book_correction on emp_notification.NOT_ID=service_book_correction.not_id WHERE emp_notification.emp_id=? AND IF_VISIBLE='Y' AND emp_notification.NOT_TYPE=? AND EMP_NOTIFICATION.NOT_ID=? and emp_notification.NOT_TYPE!='CHNG_STRUCTURE' AND emp_notification.NOT_TYPE!='LEAVE' AND emp_notification.NOT_TYPE!='RESULT'"
                            + " AND emp_notification.NOT_TYPE!='COMPLIANCE' AND emp_notification.NOT_TYPE!='SC_NOTICE' AND emp_notification.NOT_TYPE!='INCREMENT' and emp_notification.NOT_TYPE!='LOAN_SANC' and emp_notification.NOT_TYPE!='REINSTATEMENT' and emp_notification.NOT_TYPE!='SUSPENSION' and (SB_DESCRIPTION is not null and SB_DESCRIPTION<>'')";
                    pst = con.prepareStatement(moduleWiseQry);
                    pst.setString(1, empHrmsId);
                    pst.setString(2, moduleName);
                    pst.setInt(3, Integer.parseInt(noteId));
                    break;
            }
            rs = pst.executeQuery();
            if (rs.next()) {
                sbReqBean.setEmpHrmsId(empHrmsId);
                sbReqBean.setNoteIdEnc(CommonFunctions.encodedTxt(rs.getString("ID")));
                sbReqBean.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOE")));
                sbReqBean.setSbDescription(rs.getString("SB_DESCRIPTION"));
                //String sblangEditable = sbReqBean.getSbDescription().replaceAll("[0-9]", "#");
                //sbReqBean.setSbLanguageRequested("ALLOWED ANNUAL INCREMENT @ Rs. 100/- RAISING PAY FROM Rs. 4200/- TO Rs. 4300/- PM IN THE SCALE OF PAY Rs. 4000-100-6000/- WEF 01-SEP-2001 VIDE FINANCE DEPARTMENT,GOVERNMENT OF ORISSA NOTIFICATION/ OFFICE ORDER NO. 47462/F DATED 14-SEP-2001. ");
                sbReqBean.setTabName(rs.getString("TAB_NAME"));
                sbReqBean.setModuleNote(rs.getString("NOTE"));
                //sbReqBean.setEntryByAuth(rs.getString("ENT_AUTH"));
                sbReqBean.setOrderDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("ORDDT")));
            } else {
                sbReqBean.setSbDescription("No Data Found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return sbReqBean;
    }

    @Override
    public int saveRequestedServiceBookLanguage(SbCorrectionRequestModel sbmodel) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int sbcorrectionid = 0;

        String previousSBLanguage = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "";

            if (sbmodel.getHidNotType() != null && !sbmodel.getHidNotType().equals("")) {
                if (sbmodel.getHidNotType().equals("JOINING")) {
                    sql = "SELECT SB_DESCRIPTION FROM EMP_JOIN WHERE EMP_ID=? AND NOT_ID=? AND JOIN_ID=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, sbmodel.getEmpHrmsId());
                    if (sbmodel.getHidNotId() != null && !sbmodel.getHidNotId().equals("")) {
                        pst.setInt(2, Integer.parseInt(sbmodel.getHidNotId()));
                    } else {
                        pst.setInt(2, 0);
                    }
                    if (sbmodel.getModuleid() != null && !sbmodel.getModuleid().equals("")) {
                        pst.setInt(3, Integer.parseInt(sbmodel.getModuleid()));
                    } else {
                        pst.setInt(3, 0);
                    }
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        previousSBLanguage = rs.getString("SB_DESCRIPTION");
                    }
                } else if (sbmodel.getHidNotType().equals("RELIEVE")) {
                    sql = "SELECT SB_DESCRIPTION FROM EMP_RELIEVE WHERE EMP_ID=? AND NOT_ID=? AND RELIEVE_ID=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, sbmodel.getEmpHrmsId());
                    if (sbmodel.getHidNotId() != null && !sbmodel.getHidNotId().equals("")) {
                        pst.setInt(2, Integer.parseInt(sbmodel.getHidNotId()));
                    } else {
                        pst.setInt(2, 0);
                    }
                    if (sbmodel.getModuleid() != null && !sbmodel.getModuleid().equals("")) {
                        pst.setInt(3, Integer.parseInt(sbmodel.getModuleid()));
                    } else {
                        pst.setInt(3, 0);
                    }
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        previousSBLanguage = rs.getString("SB_DESCRIPTION");
                    }
                } else if (sbmodel.getHidNotType().equals("SERVICE VERIFICATION CERTIFICATE")) {
                    sql = "SELECT SB_DESCRIPTION FROM EMP_SV WHERE EMP_ID=? AND SV_ID=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, sbmodel.getEmpHrmsId());
                    if (sbmodel.getModuleid() != null && !sbmodel.getModuleid().equals("")) {
                        pst.setInt(2, Integer.parseInt(sbmodel.getModuleid()));
                    } else {
                        pst.setInt(2, 0);
                    }
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        previousSBLanguage = rs.getString("SB_DESCRIPTION");
                    }
                } else {
                    sql = "SELECT SB_DESCRIPTION FROM EMP_NOTIFICATION WHERE EMP_ID=? AND NOT_ID=? AND NOT_TYPE=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, sbmodel.getEmpHrmsId());
                    if (sbmodel.getHidNotId() != null && !sbmodel.getHidNotId().equals("")) {
                        pst.setInt(2, Integer.parseInt(sbmodel.getHidNotId()));
                    } else {
                        pst.setInt(2, 0);
                    }
                    pst.setString(3, sbmodel.getHidNotType());
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        previousSBLanguage = rs.getString("SB_DESCRIPTION");
                    }
                }
            }
            //System.out.println("previousSBLanguage is: " + previousSBLanguage);
            sbmodel.setPreviousSBLanguage(previousSBLanguage);

            sql = "INSERT INTO service_book_correction(EMP_ID,NOT_ID,NOT_TYPE,PREVIOUS_SB_LANGUAGE,REQUESTED_SB_LANGUAGE,REQUEST_DATE,ENTRY_TYPE) VALUES(?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, sbmodel.getEmpHrmsId());
            if (sbmodel.getHidNotId() != null && !sbmodel.getHidNotId().equals("")) {
                pst.setInt(2, Integer.parseInt(sbmodel.getHidNotId()));
            } else {
                pst.setInt(2, 0);
            }
            pst.setString(3, sbmodel.getHidNotType());
            pst.setString(4, sbmodel.getPreviousSBLanguage());
            pst.setString(5, sbmodel.getSbLanguageRequested());
            pst.setTimestamp(6, new Timestamp(new Date().getTime()));
            pst.setString(7, sbmodel.getEntrytype());
            pst.executeUpdate();

            rs = pst.getGeneratedKeys();
            rs.next();
            sbcorrectionid = rs.getInt("correction_id");

            if (sbmodel.getEntrytype() != null && sbmodel.getEntrytype().equals("NEW")) {
                DataBaseFunctions.closeSqlObjects(rs, pst);

                sql = "UPDATE service_book_correction SET IS_SUBMITTED='Y' WHERE CORRECTION_ID=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, sbcorrectionid);
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return sbcorrectionid;
    }

    @Override
    public List getDDOEmployeeWiseServiceBookCorrectionData(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ArrayList sbcorrectionlist = new ArrayList();
        SbCorrectionRequestModel sbReqBean = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select * from service_book_correction where emp_id=? and is_submitted='Y' order by request_date";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            while (rs.next()) {
                sbReqBean = new SbCorrectionRequestModel();

                sbReqBean.setHidNotId(rs.getString("not_id"));
                sbReqBean.setNotId(rs.getString("not_id"));
                sbReqBean.setModuleid(getIncrementId(sbReqBean.getHidNotId()) + "");
                sbReqBean.setHidNotType(rs.getString("not_type"));
                sbReqBean.setTabName(rs.getString("not_type"));
                sbReqBean.setPreviousSBLanguage(rs.getString("previous_sb_language"));
                sbReqBean.setSbLanguageRequested(rs.getString("requested_sb_language"));
                sbReqBean.setRequestDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("request_date")));
                setWefDate(sbReqBean);
                sbReqBean.setCorrectionid(rs.getString("CORRECTION_ID"));
                sbReqBean.setIsRejected(rs.getString("IS_REJECTED"));
                sbReqBean.setIsProcessed(rs.getString("IS_PROCESSED"));
                sbReqBean.setEntrytype(rs.getString("ENTRY_TYPE"));
                sbReqBean.setEditLink(getEditLink(empid, sbReqBean.getTabName(), sbReqBean.getNotId(), sbReqBean.getCorrectionid(), sbReqBean.getEntrytype()));
                sbcorrectionlist.add(sbReqBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return sbcorrectionlist;
    }

    private int getIncrementId(String notid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int incrid = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "select * from emp_incr_log where not_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(notid));
            rs = pst.executeQuery();
            if (rs.next()) {
                incrid = rs.getInt("incrid");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return incrid;
    }

    private int getPromotionId(String notid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int promotionid = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "select * from emp_promotion where not_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(notid));
            rs = pst.executeQuery();
            if (rs.next()) {
                promotionid = rs.getInt("pro_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return promotionid;
    }

    private int getRelieveId(String notid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int relieveid = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "select * from emp_relieve where not_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(notid));
            rs = pst.executeQuery();
            if (rs.next()) {
                relieveid = rs.getInt("relieve_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return relieveid;
    }

    private int getJoiningId(String notid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int joinid = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "select * from emp_join where not_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(notid));
            rs = pst.executeQuery();
            if (rs.next()) {
                joinid = rs.getInt("join_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return joinid;
    }

    private String getLoanSanctionId(String notid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String loanid = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "select * from emp_loan_sanc_sb_correction where not_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(notid));
            rs = pst.executeQuery();
            if (rs.next()) {
                loanid = rs.getString("loanid");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return loanid;
    }

    public void setWefDate(SbCorrectionRequestModel esh) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try {
            con = this.repodataSource.getConnection();
            String tabName = esh.getTabName();
            if (tabName.equalsIgnoreCase("INCREMENT") || tabName.equalsIgnoreCase("PAYREVISION") || tabName.equalsIgnoreCase("PAYFIXATION") || tabName.equalsIgnoreCase("STEPUP") || tabName.equalsIgnoreCase("PAY_ENTITLEMENT")) {
                pstmt = con.prepareStatement("SELECT WEF, pay, pay_scale, pay_level, pay_cell FROM EMP_PAY_RECORD WHERE NOT_ID = ?");
                pstmt.setInt(1, Integer.parseInt(esh.getNotId()));
                resultSet = pstmt.executeQuery();
                if (resultSet.next()) {
                    if (resultSet.getDate("WEF") != null && !resultSet.getString("WEF").trim().equals("")) {
                        esh.setWefChange(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("WEF")));
                        esh.setPay(resultSet.getDouble("pay") + "");
                        if (resultSet.getString("pay_level") != null && !resultSet.getString("pay_level").equals("")) {
                            esh.setPayscale("IN CELL " + resultSet.getString("pay_cell") + " OF LEVEL " + resultSet.getString("pay_level"));
                        } else {
                            esh.setPayscale(resultSet.getString("pay_scale"));
                        }

                    }
                }
            } else if (tabName.equalsIgnoreCase("FIRST_APPOINTMENT") || tabName.equalsIgnoreCase("ABSORPTION") || tabName.equalsIgnoreCase("DIRECT")
                    || tabName.equalsIgnoreCase("REDEPLOYMENT") || tabName.equalsIgnoreCase("REHABILITATION")
                    || tabName.equalsIgnoreCase("SELECTION") || tabName.equalsIgnoreCase("VALIDATION") || tabName.equalsIgnoreCase("PROMOTION")
                    || tabName.equalsIgnoreCase("REPATRIATION") || tabName.equalsIgnoreCase("JOIN_CADRE") || tabName.equalsIgnoreCase("RELIEVE_CADRE")
                    || tabName.equalsIgnoreCase("OFFICIATE") || tabName.equalsIgnoreCase("CONFIRMATION") || tabName.equalsIgnoreCase("TRANSFER")
                    || tabName.equalsIgnoreCase("POSTING") || tabName.equalsIgnoreCase("SERVICE_DISPOSAL") || tabName.equalsIgnoreCase("ADDITIONAL_CHARGE")) {
                pstmt = con.prepareStatement("SELECT WEFD FROM EMP_CADRE WHERE NOT_ID = ?");
                pstmt.setInt(1, Integer.parseInt(esh.getNotId()));
                resultSet = pstmt.executeQuery();
                if (resultSet.next()) {
                    if (resultSet.getDate("WEFD") != null && !resultSet.getString("WEFD").trim().equals("")) {
                        esh.setWefChange(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("WEFD")));
                    }
                }
            } else if (tabName.equalsIgnoreCase("wefPiInfo")) {
                pstmt = con.prepareStatement("SELECT WEF FROM EMP_PI_HISTORY WHERE NOT_ID = ?");
                pstmt.setInt(1, Integer.parseInt(esh.getNotId()));
                resultSet = pstmt.executeQuery();
                if (resultSet.next()) {
                    if (resultSet.getDate("WEF") != null && !resultSet.getString("WEF").trim().equals("")) {
                        esh.setWefChange(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("WEF")));
                    }
                }
            } else if (tabName.equalsIgnoreCase("SUSPENSION") || tabName.equalsIgnoreCase("HEAD QUARTER FIXATION")) {
                pstmt = con.prepareStatement("SELECT WEFD FROM EMP_SUSPENSION WHERE SP_ID = ?");
                pstmt.setInt(1, Integer.parseInt(esh.getNotId()));
                resultSet = pstmt.executeQuery();
                if (resultSet.next()) {
                    if (resultSet.getDate("WEFD") != null && !resultSet.getString("WEFD").trim().equals("")) {
                        esh.setWefChange(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("WEFD")));
                    }
                }
            } else if (tabName.equalsIgnoreCase("JOINING")) {
                String jWeft = getWefTime(esh);
                pstmt = con.prepareStatement("SELECT JOIN_DATE, getpostnamefromspc(spc)as post FROM EMP_JOIN WHERE NOT_ID = ?");
                pstmt.setInt(1, Integer.parseInt(esh.getNotId()));
                resultSet = pstmt.executeQuery();
                if (resultSet.next()) {
                    if (resultSet.getDate("JOIN_DATE") != null && !resultSet.getString("JOIN_DATE").trim().equals("")) {
                        if (jWeft != null && !jWeft.equalsIgnoreCase("FN")) {
                            esh.setWefChange(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("JOIN_DATE")) + "(" + jWeft + ")");
                        } else {
                            esh.setWefChange(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("JOIN_DATE")));
                        }
                        esh.setSpn(resultSet.getString("post"));
                    }
                }
            } else if (tabName.equalsIgnoreCase("RELIEVE")) {
                String jWeft = getWefTime(esh);
                pstmt = con.prepareStatement("SELECT RLV_DATE FROM EMP_RELIEVE WHERE NOT_ID = ?");
                pstmt.setInt(1, Integer.parseInt(esh.getNotId()));
                resultSet = pstmt.executeQuery();
                if (resultSet.next()) {
                    if (resultSet.getDate("RLV_DATE") != null && !resultSet.getString("RLV_DATE").trim().equals("")) {
                        if (jWeft != null && !jWeft.equalsIgnoreCase("FN")) {
                            esh.setWefChange(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("RLV_DATE")) + "(" + jWeft + ")");
                        } else {
                            esh.setWefChange(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("RLV_DATE")));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(resultSet, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public String getWefTime(SbCorrectionRequestModel esh) throws Exception {
        String wefTime = null;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try {
            con = this.repodataSource.getConnection();
            if (esh.getTabName().equalsIgnoreCase("RELIEVE")) {
                pstmt = con.prepareStatement("SELECT RLV_TIME FROM EMP_RELIEVE WHERE NOT_ID=?");
                pstmt.setInt(1, Integer.parseInt(esh.getNotId()));
                resultSet = pstmt.executeQuery();
                if (resultSet.next()) {
                    if (resultSet.getString("RLV_TIME") != null && !resultSet.getString("RLV_TIME").trim().equals("")) {
                        wefTime = resultSet.getString("RLV_TIME").trim();
                    }
                }
            } else if (esh.getTabName().equalsIgnoreCase("JOINING")) {
                pstmt = con.prepareStatement("SELECT JOIN_TIME FROM EMP_JOIN WHERE NOT_ID =?");
                pstmt.setInt(1, Integer.parseInt(esh.getNotId()));
                resultSet = pstmt.executeQuery();
                if (resultSet.next()) {
                    if (resultSet.getString("JOIN_TIME") != null && !resultSet.getString("JOIN_TIME").trim().equals("")) {
                        wefTime = resultSet.getString("JOIN_TIME").trim();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(resultSet, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return wefTime;
    }

    private String getEditLink(String empid, String nottype, String notid, String correctionid, String entrytype) {
        String editlink = "";

        switch (nottype) {
            case "INCREMENT":
                int incrid = getIncrementId(notid);
                editlink = "EditIncrementSBCorrectionDDO.htm?mode=D&notId=" + notid + "&incrId=" + incrid + "&correctionid=" + correctionid + "&entrytype=" + entrytype + "&empid=" + empid;
                break;
            case "LEAVE":
                editlink = "editLeaveSanction.htm?notId=" + notid;
                break;
            case "LOAN_SANC":
                String loanid = getLoanSanctionId(notid);
                editlink = "EditLoanDataSBCorrectionDDO.htm?mode=D&loanid=" + loanid + "&correctionid=" + correctionid + "&entrytype=" + entrytype + "&empid=" + empid;
                break;
            case "EXAMINATION":
                editlink = "EditDepartmentalExam.htm?empid=" + empid + "&examid=" + notid;
                break;
            case "TRAINING":
                editlink = "gettrainingdata.htm?trainId=" + notid;
                break;
            case "SERVICE VERIFICATION CERTIFICATE":
                editlink = "EditServiceVerifySBCorrectionDDO.htm?mode=D&txtsvid=" + notid + "&txtempid=" + empid + "&correctionid=" + correctionid + "&entrytypeSBCorrection=" + entrytype + "&empid=" + empid;
                break;
            case "RELIEVE":
                int relieveid = getRelieveId(notid);
                editlink = "EditRelieveSBCorrectionDDO.htm?mode=D&notId=" + notid + "&rlvId=" + relieveid + "&empid=" + empid + "&correctionid=" + correctionid;
                break;
            case "JOINING":
                int relieveid1 = getRelieveId(notid);
                int joinid = getJoiningId(notid);
                editlink = "EditJoiningSBCorrectionDDO.htm?mode=D&notId=" + notid + "&rlvId=" + relieveid1 + "&leaveId=&jId=" + joinid + "&empid=" + empid + "&correctionid=" + correctionid;
                break;
            case "REINSTATEMENT":
                editlink = "reinstatementEntry.htm?spId=" + notid + "&empid=" + empid;
                break;
            case "SUSPENSION":
                editlink = "editSuspension.htm?susId=" + notid;
                break;
            case "HEAD QUARTER FIXATION":
                editlink = "";
                break;
            case "PERMISSION":
                editlink = "";
                break;
            case "EDUCATION":
                editlink = "";
                break;
            case "RETIREMENT":
                editlink = "";
                break;
            case "SERVICE RECORD":
                editlink = "editServiceRecord.htm?srid=" + notid;
                break;
            case "SERVICE PAY RECORD":
                editlink = "getPisrData.htm?srpId=" + notid;
                break;
            case "BRASS_ALLOT":
                editlink = "editBrassAllot.htm?notId=" + notid + "&mode=E";
                break;
            case "PAY_ENTITLEMENT":
                editlink = "editPayEntitlement.htm?notId=" + notid;
                break;
            case "ALLOWANCES":
                editlink = "editAllowance.htm?notId=" + notid;
                break;
            case "FIRST_APPOINTMENT":
                editlink = "editRecruitment.htm?notId=" + notid;
                break;
            case "REHABILITATION":
                editlink = "editRecruitment.htm?notId=" + notid;
                break;
            case "VALIDATION":
                editlink = "editRecruitment.htm?notId=" + notid;
                break;
            case "ABSORPTION":
                editlink = "editAbsorption.htm?notId=" + notid;
                break;
            case "REDEPLOYMENT":
                editlink = "editRedeployment.htm?notId=" + notid;
                break;
            case "REDESIGNATION":
                editlink = "editRedesignation.htm?notId=" + notid;
                break;
            case "REGULARIZATION":
                editlink = "editRegularization.htm?notId=" + notid;
                break;
            case "ADM_ACTION":
                editlink = "editPunishment.htm?acId=&notId=" + notid;
                break;
            case "REWARD":
                editlink = "editReward.htm?rewardId=&notId=" + notid;
                break;
            case "DET_VAC":
                editlink = "editDetention.htm?detentionId=" + notid;
                break;
            case "TRANSFER":
                editlink = "editTransfer.htm?transferId=&notId=" + notid;
                break;
            case "POSTING":
                editlink = "editPosting.htm?notId=" + notid;
                break;
            case "ADDITIONAL_CHARGE":
                editlink = "editadditionalCharge.htm?notId=" + notid;
                break;
            case "PROMOTION":
                int promotionid = getPromotionId(notid);
                editlink = "EditPromotionSBCorrectionDDO.htm?promotionId=" + promotionid + "&notId=" + notid + "&mode=D&entrytype=" + entrytype + "&correctionid=" + correctionid + "&empid=" + empid;
                break;
            case "SERVICE_DISPOSAL":
                editlink = "editPlacementOfService.htm?notId=" + notid;
                break;
            case "OFFICIATE":
                editlink = "editOfficiation.htm?notId=" + notid;
                break;
            case "LOAN_TRAN":
                editlink = "getloanreleasedata.htm?repid=&notId=" + notid;
                break;
            case "DEPUTATION":
                editlink = "editDeputation.htm?notId=" + notid;
                break;
            case "DEPUTATION_AG":
                editlink = "editAGDeputation.htm?notId=" + notid;
                break;
            case "REPATRIATION":
                editlink = "editRepatriation.htm?notId=" + notid;
                break;
            case "ALLOT_CADRE":
                editlink = "editAllotmentToCadre.htm?notId=" + notid;
                break;
            case "RELIEVE_CADRE":
                editlink = "editRelieveCadre.htm?notId=" + notid;
                break;
            case "JOIN_CADRE":
                editlink = "editJoiningCadre.htm?notId=" + notid;
                break;
            case "EQ_STAT":
                editlink = "editEquivalentPost.htm?notId=" + notid;
                break;
            case "MISCELLANEOUS":
                editlink = "editMiscInfo.htm?notId=" + notid;
                break;
            case "CONFIRMATION":
                editlink = "editConfirmationOfService.htm?notId=" + notid;
                break;
            case "LT_TRAINING":
                editlink = "getLtTrainingData.htm?notId=" + notid;
                break;
            case "FTC":
                editlink = "editFTCEntry.htm?notId=" + notid;
                break;
            case "LTC":
                editlink = "editLTCEntry.htm?notId=" + notid;
                break;
            case "PAYREVISION":
                editlink = "EditPayFixationSBCorrectionDDO.htm?notId=" + notid + "&mode=D&entrytype=" + entrytype + "&correctionid=" + correctionid + "&empid=" + empid;
                break;
            case "PAYFIXATION":
                editlink = "EditPayFixationSBCorrectionDDO.htm?notId=" + notid + "&mode=D&entrytype=" + entrytype + "&correctionid=" + correctionid + "&empid=" + empid;
                break;
            case "STEPUP":
                editlink = "EditPayFixationSBCorrectionDDO.htm?notId=" + notid + "&mode=D&entrytype=" + entrytype + "&correctionid=" + correctionid + "&empid=" + empid;
                break;
            default:
                editlink = "";
                break;
        }
        return editlink;
    }

    @Override
    public String getRequestedServiceBookLanguage(String empid, String nottype, String notid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String requestedSBLang = "";
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT * FROM SERVICE_BOOK_CORRECTION WHERE EMP_ID=? AND NOT_TYPE=? AND NOT_ID=? AND IS_REJECTED='N' AND is_processed='N'";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setString(2, nottype);
            pst.setInt(3, Integer.parseInt(notid));
            rs = pst.executeQuery();
            if (rs.next()) {
                requestedSBLang = rs.getString("REQUESTED_SB_LANGUAGE");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return requestedSBLang;
    }

    @Override
    public void rejectRequestedServiceBookLanguage(String empid, String nottype, String notid, String loginid) {

        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "UPDATE SERVICE_BOOK_CORRECTION SET IS_REJECTED='Y',rejected_by=?,approval_date=? WHERE EMP_ID=? AND NOT_TYPE=? AND NOT_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, loginid);
            pst.setTimestamp(2, new Timestamp(new Date().getTime()));
            pst.setString(3, empid);
            pst.setString(4, nottype);
            pst.setInt(5, Integer.parseInt(notid));
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateRequestedServiceBookFlag(String empid, String nottype, String notid, String correctionid, String loginid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            if (notid != null && !notid.equals("")) {
                String sql = "UPDATE SERVICE_BOOK_CORRECTION SET is_processed='Y',approved_by=?,approval_date=? WHERE EMP_ID=? AND NOT_TYPE=? AND NOT_ID=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, loginid);
                pst.setTimestamp(2, new Timestamp(new Date().getTime()));
                pst.setString(3, empid);
                pst.setString(4, nottype);
                pst.setInt(5, Integer.parseInt(notid));
                pst.executeUpdate();
            } else {
                String sql = "UPDATE SERVICE_BOOK_CORRECTION SET is_processed='Y',approved_by=?,approval_date=? WHERE EMP_ID=? AND NOT_TYPE=? AND CORRECTION_ID=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, loginid);
                pst.setTimestamp(2, new Timestamp(new Date().getTime()));
                pst.setString(3, empid);
                pst.setString(4, nottype);
                pst.setInt(5, Integer.parseInt(correctionid));
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
    public void updateRequestedServiceBookLanguage(int correctionid, String sblangrequested) {

        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "UPDATE SERVICE_BOOK_CORRECTION SET REQUESTED_SB_LANGUAGE=? WHERE CORRECTION_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, sblangrequested);
            pst.setInt(2, correctionid);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void deleteRequestedServiceBookLanguage(String empid, String nottype, String notid) {

        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "DELETE FROM SERVICE_BOOK_CORRECTION WHERE EMP_ID=? AND NOT_TYPE=? AND NOT_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setString(2, nottype);
            if (notid != null && !notid.equals("")) {
                pst.setInt(3, Integer.parseInt(notid));
            } else {
                pst.setInt(3, 0);
            }
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void submitRequestedServiceBookLanguage(String empid, int notid, String nottype) {

        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "UPDATE SERVICE_BOOK_CORRECTION SET IS_SUBMITTED='Y' WHERE EMP_ID=? AND NOT_ID=? AND NOT_TYPE=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setInt(2, notid);
            pst.setString(3, nottype);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getRequestedEmployeeListSBCorrection(String offcode) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List emplist = new ArrayList();
        SelectOption so = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT EMP_MAST.EMP_ID,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,F_NAME FROM service_book_correction"
                    + " INNER JOIN EMP_MAST ON service_book_correction.EMP_ID=EMP_MAST.EMP_ID WHERE EMP_MAST.CUR_OFF_CODE=? GROUP BY EMP_MAST.EMP_ID"
                    + " ORDER BY F_NAME";
            pst = con.prepareStatement(sql);
            pst.setString(1, offcode);
            rs = pst.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("EMP_ID"));
                so.setLabel(rs.getString("EMP_NAME"));
                emplist.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return emplist;
    }
    
    private String getApprovedRejectedByDetails(String isProcessed,String isRejected,String actiontakenby,Date actiontakendate){
        
        Connection con = null;
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        String approvaldetails = "";
        
        try{
            con = this.dataSource.getConnection();
            
            if(isProcessed != null && isProcessed.equals("Y")){
                approvaldetails = "Approved by ";
            }else if(isRejected != null && isRejected.equals("Y")){
                approvaldetails = "Rejected by ";
            }
            
            String sql = "SELECT EMP_ID,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME, L_NAME], ' ') EMPNAME FROM EMP_MAST WHERE EMP_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1,actiontakenby);
            rs = pst.executeQuery();
            if(rs.next()){                
                //approvaldetails = approvaldetails + rs.getString("EMPNAME") + " ON " + CommonFunctions.getFormattedOutputDate1(new SimpleDateFormat("yyyy-MMM-dd'T'HH:mm:ss").parse(actiontakendate));
                approvaldetails = approvaldetails + rs.getString("EMPNAME") + " ON " + CommonFunctions.getFormattedOutputDate1(actiontakendate);
            }            
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
      return approvaldetails;  
    }
    
    private String getDDOName(String offcode){
        Connection con = null;
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        String ddoname = "";
        
        try{
            con = this.dataSource.getConnection();
            
            String sql = "select ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME from g_office" +
                         " inner join emp_mast on g_office.ddo_hrmsid=emp_mast.emp_id" +
                         " where g_office.off_code=?";
            pst = con.prepareStatement(sql);
            pst.setString(1,offcode);
            rs = pst.executeQuery();
            if(rs.next()){
                ddoname = rs.getString("EMP_NAME");
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
       return ddoname; 
    }
}
