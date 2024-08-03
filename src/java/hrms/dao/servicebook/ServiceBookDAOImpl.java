package hrms.dao.servicebook;

import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.common.FileDownload;
import hrms.model.login.LoginUserBean;
import hrms.model.notification.EmployeeCadreSt;
import hrms.model.servicebook.EmpServiceBook;
import hrms.model.servicebook.EmpServiceHistory;
import hrms.model.servicebook.ServiceBookAnomali;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

public class ServiceBookDAOImpl implements ServiceBookDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    @Resource(name = "repodataSource")
    protected DataSource repodataSource;

    private String uploadPath;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public void setRepodataSource(DataSource repodataSource) {
        this.repodataSource = repodataSource;
    }

    public void setMiscellaneousData(EmpServiceHistory esh) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try {
            con = this.repodataSource.getConnection();
            pstmt = con.prepareStatement("SELECT HEADER,DESCRIPTION FROM EMP_MISC WHERE MISC_ID=? AND EMP_ID=?");
            pstmt.setInt(1, esh.getNoteId());
            pstmt.setString(2, esh.getEmpId());
            resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                if (resultSet.getString("HEADER") != null && !resultSet.getString("HEADER").equals("")) {
                    esh.setCategory(resultSet.getString("HEADER").trim() + ".");
                }
                if (resultSet.getString("DESCRIPTION") != null && !resultSet.getString("DESCRIPTION").equals("")) {
                    String miscllaneousData = resultSet.getString("DESCRIPTION").trim();
                    miscllaneousData = miscllaneousData.replaceAll("[\n\r\t]", "");
                    miscllaneousData = StringUtils.replace(miscllaneousData, "\"", "");
                    esh.setModuleNote(miscllaneousData.trim());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(resultSet, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void setRetiredMentData(EmpServiceHistory esh) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try {
            con = this.repodataSource.getConnection();
            String retType = null;
            pstmt = con.prepareStatement("SELECT RET_TYPE FROM EMP_RET_RES WHERE RET_ID=? AND EMP_ID=?");
            pstmt.setInt(1, esh.getNoteId());
            pstmt.setString(2, esh.getEmpId());
            resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                if (resultSet.getString("RET_TYPE") != null && !resultSet.getString("RET_TYPE").trim().equals("")) {
                    retType = resultSet.getString("RET_TYPE");
                }
            }
            if (retType != null) {
                if (retType.equalsIgnoreCase("VR")) {
                    esh.setCategory("VOLUNTARY RETIREMENT");
                } else if (retType.equalsIgnoreCase("SR")) {
                    esh.setCategory("RETIREMENT ON SUPERANNUATION");
                } else if (retType.equalsIgnoreCase("CR")) {
                    esh.setCategory("COMPULSORY RETIREMENT");
                } else if (retType.equalsIgnoreCase("RG")) {
                    esh.setCategory("RESIGNATION");
                } else if (retType.equalsIgnoreCase("TR")) {
                    esh.setCategory("TERMINATION");
                } else if (retType.equalsIgnoreCase("RT")) {
                    esh.setCategory("RETRENCHMENT");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(resultSet, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void setLoanTransactionType(EmpServiceHistory esh) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try {
            con = this.repodataSource.getConnection();
            pstmt = con.prepareStatement("SELECT TRAN_TYPE FROM EMP_LOAN_TRAN WHERE NOT_ID=? and EMP_ID=?");
            pstmt.setInt(1, esh.getNoteId());
            pstmt.setString(2, esh.getEmpId());
            resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                if (resultSet.getString("TRAN_TYPE") != null && !resultSet.getString("TRAN_TYPE").equals("")) {
                    if (resultSet.getString("TRAN_TYPE").equals("REPAYMENT")) {
                        esh.setCategory("REPAYMENT OF LOAN");
                    } else if (resultSet.getString("TRAN_TYPE").equals("RELEASE")) {
                        esh.setCategory("RELEASE OF LOAN");
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

    public void setEmployeeLeaveType(EmpServiceHistory esh) {
        String lsotId = null;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try {
            con = this.repodataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM EMP_LEAVE WHERE NOT_ID=? and EMP_ID=?");
            pstmt.setInt(1, esh.getNoteId());
            pstmt.setString(2, esh.getEmpId());
            resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                lsotId = resultSet.getString("LSOT_ID");
            }
            pstmt = con.prepareStatement("SELECT * FROM G_LSOT WHERE LSOT_ID=?");
            pstmt.setString(1, lsotId);
            resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                if (lsotId.equalsIgnoreCase("01")) {
                    esh.setCategory("LEAVE SANCTIONED");
                } else if (lsotId.equalsIgnoreCase("02")) {
                    esh.setCategory("LEAVE SURRENDERED");
                } else {
                    esh.setCategory(resultSet.getString("LSOT"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(resultSet, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void setEmployeeDeputationData(EmpServiceHistory esh) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try {
            con = this.repodataSource.getConnection();
            String ifExt = null;
            pstmt = con.prepareStatement("SELECT IF_EXTENSION FROM EMP_DEPUTATION WHERE NOT_TYPE='DEPUTATION' AND NOT_ID=? AND EMP_ID=?");
            pstmt.setInt(1, esh.getNoteId());
            pstmt.setString(2, esh.getEmpId());
            resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                if (resultSet.getString("IF_EXTENSION") != null) {
                    ifExt = resultSet.getString("IF_EXTENSION");
                }
            }
            if (ifExt != null && ifExt.equalsIgnoreCase("Y")) {
                esh.setCategory("EXTENSION OF DEPUTATION");
            } else if (ifExt == null || ifExt.equals("")) {
                esh.setCategory(esh.getTabname().toUpperCase());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(resultSet, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void setEmployeeAgDeputationData(EmpServiceHistory esh) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try {
            con = this.repodataSource.getConnection();
            pstmt = con.prepareStatement("SELECT IF_EXTENSION FROM EMP_DEPUTATION WHERE NOT_TYPE='DEPUTATION_AG' AND NOT_ID=? AND EMP_ID=?");
            pstmt.setInt(1, esh.getNoteId());
            pstmt.setString(2, esh.getEmpId());
            resultSet = pstmt.executeQuery();
            String ifExt = null;
            if (resultSet.next()) {
                if (resultSet.getString("IF_EXTENSION") != null) {
                    ifExt = resultSet.getString("IF_EXTENSION");
                }
            }
            if (ifExt != null && ifExt.equalsIgnoreCase("Y")) {
                esh.setCategory("ENDORSEMENT AG ON EXTENSION OF DEPUTATION");
            } else {
                esh.setCategory("ENDORSEMENT OF AG ON DEPUTATION");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(resultSet, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void setWefDate(EmpServiceHistory esh) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try {
            con = this.repodataSource.getConnection();
            String tabName = esh.getTabname();
            if (tabName.equalsIgnoreCase("INCREMENT") || tabName.equalsIgnoreCase("PAYREVISION") || tabName.equalsIgnoreCase("PAYFIXATION") || tabName.equalsIgnoreCase("STEPUP") || tabName.equalsIgnoreCase("PAY_ENTITLEMENT")) {
                pstmt = con.prepareStatement("SELECT WEF, pay, pay_scale, pay_level, pay_cell FROM EMP_PAY_RECORD WHERE NOT_ID = ?");
                pstmt.setInt(1, esh.getNoteId());
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
                pstmt.setInt(1, esh.getNoteId());
                resultSet = pstmt.executeQuery();
                if (resultSet.next()) {
                    if (resultSet.getDate("WEFD") != null && !resultSet.getString("WEFD").trim().equals("")) {
                        esh.setWefChange(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("WEFD")));
                    }
                }
            } else if (tabName.equalsIgnoreCase("wefPiInfo")) {
                pstmt = con.prepareStatement("SELECT WEF FROM EMP_PI_HISTORY WHERE NOT_ID = ?");
                pstmt.setInt(1, esh.getNoteId());
                resultSet = pstmt.executeQuery();
                if (resultSet.next()) {
                    if (resultSet.getDate("WEF") != null && !resultSet.getString("WEF").trim().equals("")) {
                        esh.setWefChange(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("WEF")));
                    }
                }
            } else if (tabName.equalsIgnoreCase("SUSPENSION") || tabName.equalsIgnoreCase("HEAD QUARTER FIXATION")) {
                pstmt = con.prepareStatement("SELECT WEFD FROM EMP_SUSPENSION WHERE SP_ID = ?");
                pstmt.setInt(1, esh.getNoteId());
                resultSet = pstmt.executeQuery();
                if (resultSet.next()) {
                    if (resultSet.getDate("WEFD") != null && !resultSet.getString("WEFD").trim().equals("")) {
                        esh.setWefChange(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("WEFD")));
                    }
                }
            } else if (tabName.equalsIgnoreCase("JOINING")) {
                String jWeft = getWefTime(esh);
                pstmt = con.prepareStatement("SELECT JOIN_DATE, getpostnamefromspc(spc)as post FROM EMP_JOIN WHERE NOT_ID = ?");
                pstmt.setInt(1, esh.getNoteId());
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
                pstmt.setInt(1, esh.getNoteId());
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

    public String getWefTime(EmpServiceHistory esh) throws Exception {
        String wefTime = null;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try {
            con = this.repodataSource.getConnection();
            if (esh.getTabname().equalsIgnoreCase("RELIEVE")) {
                pstmt = con.prepareStatement("SELECT RLV_TIME FROM EMP_RELIEVE WHERE NOT_ID=?");
                pstmt.setInt(1, esh.getNoteId());
                resultSet = pstmt.executeQuery();
                if (resultSet.getString("RLV_TIME") != null && !resultSet.getString("RLV_TIME").trim().equals("")) {
                    wefTime = resultSet.getString("RLV_TIME").trim();
                }
            } else if (esh.getTabname().equalsIgnoreCase("JOINING")) {
                pstmt = con.prepareStatement("SELECT JOIN_TIME FROM EMP_JOIN WHERE NOT_ID =?");
                pstmt.setInt(1, esh.getNoteId());
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

    public EmployeeCadreSt getCadreInfoOfNotification(String notid) {
        EmployeeCadreSt ec = null;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try {
            con = this.repodataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM EMP_CADRE WHERE NOT_ID=?");
            pstmt.setString(1, notid);
            resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                ec = new EmployeeCadreSt();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(resultSet, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return ec;
    }

    /*Cadre/Post/Pay*/
    public void setCadrePostPay(EmpServiceHistory esh) {
        String tabName = esh.getTabname();
        if (tabName.equalsIgnoreCase("ABSORPTION") || tabName.equalsIgnoreCase("DIRECT") || tabName.equalsIgnoreCase("REDEPLOYMENT") || tabName.equalsIgnoreCase("REHABILITATION") || tabName.equalsIgnoreCase("REGULARIZATION") || tabName.equalsIgnoreCase("SELECTION") || tabName.equalsIgnoreCase("VALIDATION") || tabName.equalsIgnoreCase("CONFIRMATION") || tabName.equalsIgnoreCase("PROMOTION") || tabName.equalsIgnoreCase("REPATRIATION") || tabName.equalsIgnoreCase("JOIN_CADRE") || tabName.equalsIgnoreCase("FIRST_APPOINTMENT") || tabName.equalsIgnoreCase("PAY_ENTITLEMENT") || tabName.equalsIgnoreCase("INCREMENT") || tabName.equalsIgnoreCase("PAYREVISION") || tabName.equalsIgnoreCase("PAYFIXATION") || tabName.equalsIgnoreCase("STEPUP")) {

        } else if (tabName.equalsIgnoreCase("JOINING")) {

        } else {
            esh.setCadre("N");
            esh.setPay("N");
        }
    }

    public void setTabName(EmpServiceHistory esh) {
        String tabName = esh.getTabname();
        if (tabName != null) {
            if (tabName.equalsIgnoreCase("DEPUTATION")) {
                setEmployeeDeputationData(esh);
            } else if (tabName.equalsIgnoreCase("DEPUTATION_AG")) {
                setEmployeeAgDeputationData(esh);
            } else if (tabName.equalsIgnoreCase("FIRST_APPOINTMENT")) {
                esh.setCategory("REGULAR RECRUITMENT DETAILS");
            } else if (tabName.equalsIgnoreCase("GREEN_CARD")) {
                esh.setCategory("GREEN CARD DETAILS");
            } else if (tabName.equalsIgnoreCase("JOIN_CADRE")) {
                esh.setCategory("JOINING IN CADRE");
            } else if (tabName.equalsIgnoreCase("INITIATION")) {
                esh.setCategory("DEPARTMENTAL PROCEEDING");
            } else if (tabName.equalsIgnoreCase("RELIEVE_CADRE")) {
                esh.setCategory("RELIEVE FROM CADRE");
            } else if (tabName.equalsIgnoreCase("PAY_ENTITLEMENT")) {
                esh.setCategory("PAY ENTITLEMENT");
            } else if (tabName.equalsIgnoreCase("TRANSFER")) {
                esh.setCategory("TRANSFER & POSTING");
            } else if (tabName.equalsIgnoreCase("EQ_STAT")) {
                esh.setCategory("EQUIVALENT TO POST");
            } else if (tabName.equalsIgnoreCase("EXAMINATION")) {
                esh.setCategory("DEPARTMENTAL EXAMINATION");
            } else if (tabName.equalsIgnoreCase("PAYREVISION")) {
                esh.setCategory("PAY REVISION");
            } else if (tabName.equalsIgnoreCase("PAYFIXATION")) {
                esh.setCategory("PAY FIXATION");
            } else if (tabName.equalsIgnoreCase("STEPUP")) {
                esh.setCategory("STEPUP OF PAY");
            } else if (tabName.equalsIgnoreCase("ADDITIONAL_CHARGE")) {
                esh.setCategory("ADDITIONAL CHARGE");
            } else if (tabName.equalsIgnoreCase("LT_TRAINING")) {
                esh.setCategory("LONG TERM/ MANDATORY TRAINING OR COURSE");
            } else if (tabName.equalsIgnoreCase("LEAVE")) {
                setEmployeeLeaveType(esh);
            } else if (tabName.equalsIgnoreCase("PI_CHANGE")) {
                esh.setCategory("CHANGE OF NAME/GPF A/C NO.");
            } else if (tabName.equalsIgnoreCase("LOAN_TRAN")) {
                setLoanTransactionType(esh);
            } else if (tabName.equalsIgnoreCase("LOAN_SANC")) {
                esh.setCategory("LOAN SANCTION");
            } else if (tabName.equalsIgnoreCase("RES_CAT")) {
                esh.setCategory("RESERVATION CATEGORY");
            } else if (tabName.equalsIgnoreCase("ALLOT_CADRE")) {
                esh.setCategory("ALLOTMENT TO CADRE");
            } else if (tabName.equalsIgnoreCase("ENROLLMENT")) {
                esh.setCategory("ENROLLMENT TO INSURANCE SCHEME");
            } else if (tabName.equalsIgnoreCase("REHABILITATION")) {
                esh.setCategory("REHABILITATION(FIRST APPOINTMENT)");
            } else if (tabName.equalsIgnoreCase("SERVICE_DISPOSAL")) {
                esh.setCategory("PLACEMENT OF SERVICE");
            } else if (tabName.equalsIgnoreCase("BRASS_ALLOT")) {
                esh.setCategory("BRASS NO. ALLOTMENT");
            } else if (tabName.equalsIgnoreCase("REGULARIZATION")) {
                esh.setCategory("REGULARIZATION OF SERVICE");
            } else if (tabName.equalsIgnoreCase("RETIREMENT")) {
                setRetiredMentData(esh);
            } else if (tabName.equalsIgnoreCase("MISC")) {
                setMiscellaneousData(esh);
            } else if (tabName.equalsIgnoreCase("DET_VAC")) {
                esh.setCategory("DETENTION OF VACATION");
            } else if (tabName.equalsIgnoreCase("OFFICIATE")) {
                esh.setCategory("ALLOWED TO OFFICIATE");
            } else if (tabName.equalsIgnoreCase("ADM_ACTION")) {
                esh.setCategory("ADMINISTRATIVE ACTION/PUNISHMENT");
            } else if (tabName.equalsIgnoreCase("SUSPENSION")) {
                esh.setCategory("SUSPENSION");
            } else if (tabName.equalsIgnoreCase("REINSTATEMENT")) {
                esh.setCategory("REINSTATEMENT");
            } else if (tabName.equalsIgnoreCase("REWARD")) {
                esh.setCategory("REWARD/APPRECIATION");
            } else if (tabName.equalsIgnoreCase("QTR_ALLOT")) {
                esh.setCategory("QUARTER ALLOTMENT");
            } else if (tabName.equalsIgnoreCase("QTR_SURRENDER")) {
                esh.setCategory("QUARTER SURRENDER");
            } else {
                esh.setCategory(tabName.toUpperCase());
            }
        }//
    }

    @Override
    public EmpServiceBook getSHReport(String empId) {
        Connection con = null;
        PreparedStatement pstSvRecord = null;
        ResultSet resultSet = null;
        List<EmpServiceHistory> serviceHistory = new ArrayList<>();
        EmpServiceBook eap = new EmpServiceBook();
        try {
            con = this.repodataSource.getConnection();
            pstSvRecord = con.prepareStatement("SELECT emp_notification.EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,emp_notification.NOT_ID ID,NOTE,ORDNO,ORDDT,emp_notification.NOT_TYPE TAB_NAME,DEPT_CODE,OFF_CODE, GETSPN(emp_notification.AUTH) AUTH ,ENT_DEPT,ENT_OFF, "
                    + "GETSPN(emp_notification.ENT_AUTH) ENT_AUTH,'1' TEMPORD,IS_LOCKED FROM emp_notification  WHERE emp_notification.emp_id=? AND IF_VISIBLE='Y' AND NOT_TYPE!='CHNG_STRUCTURE' AND NOT_TYPE!='LEAVE' AND NOT_TYPE!='RESULT' "
                    + "AND NOT_TYPE!='COMPLIANCE' AND NOT_TYPE!='SC_NOTICE' AND NOT_TYPE!='INCREMENT' and NOT_TYPE!='LOAN_SANC' and NOT_TYPE!='SUPERANNUATION' and NOT_TYPE!='REINSTATEMENT' and NOT_TYPE!='SUSPENSION' and (SB_DESCRIPTION is not null and SB_DESCRIPTION<>'')");
            pstSvRecord.setString(1, empId);
            resultSet = pstSvRecord.executeQuery();
            List<EmpServiceHistory> serviceHistory1 = new ArrayList<EmpServiceHistory>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                setTabName(esh);
                setWefDate(esh);
                serviceHistory1.add(esh);
            }

            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            /**
             * **************************************INCREMENT
             */
            pstSvRecord = con.prepareStatement("SELECT emp_notification.EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,emp_notification.NOT_ID ID,NOTE,ORDNO,ORDDT,emp_notification.NOT_TYPE TAB_NAME,DEPT_CODE,OFF_CODE, GETSPN(emp_notification.AUTH) AUTH,ENT_DEPT,ENT_OFF, "
                    + "GETSPN(emp_notification.ENT_AUTH) ENT_AUTH,'1' TEMPORD,IS_LOCKED  FROM (SELECT * FROM EMP_INCR WHERE emp_id=? AND PRID IS NULL) EMP_INCR inner join (select * from emp_notification where NOT_TYPE='INCREMENT' and emp_id=? and (if_visible='Y' or if_visible is null or if_visible='')) "
                    + "emp_notification ON EMP_INCR.NOT_ID = emp_notification.NOT_ID");
            pstSvRecord.setString(1, empId);
            pstSvRecord.setString(2, empId);
            resultSet = pstSvRecord.executeQuery();
            ArrayList<EmpServiceHistory> serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                setTabName(esh);
                setWefDate(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * **************************************LEAVE
             */
            pstSvRecord = con.prepareStatement("SELECT EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,NOT_ID ID,NOTE,ORDNO,ORDDT,NOT_TYPE TAB_NAME,DEPT_CODE,OFF_CODE,  GETSPN(emp_notification.AUTH) AUTH  ,ENT_DEPT,ENT_OFF,  GETSPN(emp_notification.ENT_AUTH) ENT_AUTH, "
                    + "'1' TEMPORD,IS_LOCKED FROM emp_notification where EMP_NOTIFICATION.NOT_ID IN (SELECT NOT_ID FROM EMP_LEAVE WHERE EMP_LEAVE.EMP_ID=? AND LSOT_ID='01') AND EMP_NOTIFICATION.EMP_ID=? AND NOT_TYPE='LEAVE' AND  "
                    + "STATUS = 'SANCTIONED AND AVAILED'");
            pstSvRecord.setString(1, empId);
            pstSvRecord.setString(2, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                setTabName(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * ************************************** LEAVE SANCTIONED
             */
            pstSvRecord = con.prepareStatement("SELECT EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,NOT_ID ID,NOTE,ORDNO,ORDDT,NOT_TYPE TAB_NAME,DEPT_CODE,OFF_CODE,  GETSPN(emp_notification.AUTH) AUTH  ,ENT_DEPT,ENT_OFF,  GETSPN(emp_notification.ENT_AUTH) ENT_AUTH  , "
                    + "	'1' TEMPORD,IS_LOCKED FROM emp_notification where EMP_NOTIFICATION.NOT_ID IN (SELECT NOT_ID FROM EMP_LEAVE WHERE EMP_LEAVE.EMP_ID=? AND LSOT_ID='02') AND EMP_NOTIFICATION.EMP_ID=? AND NOT_TYPE='LEAVE' "
                    + "	AND STATUS = 'SANCTIONED' AND EMP_NOTIFICATION.IF_VISIBLE='Y' ");
            pstSvRecord.setString(1, empId);
            pstSvRecord.setString(2, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                setTabName(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * **************************************
             */
            pstSvRecord = con.prepareStatement("SELECT EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,NOT_ID ID,NOTE,ORDNO,ORDDT,NOT_TYPE TAB_NAME,DEPT_CODE,OFF_CODE,  GETSPN(emp_notification.AUTH) AUTH  ,ENT_DEPT,ENT_OFF,  GETSPN(emp_notification.ENT_AUTH) ENT_AUTH,"
                    + "'1' TEMPORD,NULL IS_LOCKED FROM emp_notification where EMP_NOTIFICATION.NOT_ID IN (SELECT NOT_ID FROM EMP_LEAVE WHERE EMP_LEAVE.EMP_ID=? AND LSOT_ID!='01' AND LSOT_ID!='02') AND "
                    + "EMP_NOTIFICATION.EMP_ID=? AND NOT_TYPE='LEAVE' AND EMP_NOTIFICATION.IF_VISIBLE='Y' ");
            pstSvRecord.setString(1, empId);
            pstSvRecord.setString(2, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                setTabName(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * ************************************** LOAN SANCTION
             */
            pstSvRecord = con.prepareStatement("SELECT emp_notification.EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,emp_notification.NOT_ID ID,NOTE,ORDNO,ORDDT,emp_notification.NOT_TYPE  "
                    + " TAB_NAME,DEPT_CODE,OFF_CODE, GETSPN(emp_notification.AUTH) AUTH ,ENT_DEPT,ENT_OFF, "
                    + " GETSPN(emp_notification.ENT_AUTH) ENT_AUTH,'1' TEMPORD,IS_LOCKED FROM emp_notification  "
                    + " inner join emp_loan_sanc on emp_notification.not_id=emp_loan_sanc.not_id "
                    + " WHERE emp_notification.emp_id=? AND IF_VISIBLE='Y' "
                    + " AND  emp_loan_sanc.NOT_TYPE='LOAN_SANC' "
                    + " and (emp_loan_sanc.loan_tp='BICA' or emp_loan_sanc.loan_tp='CMPA' or emp_loan_sanc.loan_tp='MOPA' or emp_loan_sanc.loan_tp='MCA' or emp_loan_sanc.loan_tp='HBA' or emp_loan_sanc.loan_tp='VE' or emp_loan_sanc.loan_tp='SHBA') and (SB_DESCRIPTION is not null and SB_DESCRIPTION<>'')");
            pstSvRecord.setString(1, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                setTabName(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * ************************************** EXAMINATION
             */
            pstSvRecord = con.prepareStatement("SELECT EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,EX_ID ID,NOTE,NOT_NO ORDNO,NOT_DT ORDDT,'EXAMINATION' TAB_NAME,  DEPT_CODE,AUTH_OFF OFF_CODE,  GETSPN(emp_exam.AUTH) AUTH  ,ENT_DEPT,ENT_OFF,  "
                    + "GETSPN(emp_exam.ENT_AUTH) ENT_AUTH  ,NULL TEMPORD,NULL IS_LOCKED FROM emp_exam where IF_VISIBLE='Y' AND emp_id=?");
            pstSvRecord.setString(1, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                setTabName(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * *****************Training*********************
             */
            pstSvRecord = con.prepareStatement("SELECT EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,TRAINID ID,NOTE,ord_no ORDNO,ord_date ORDDT,'TRAINING' TAB_NAME,  NULL DEPT_CODE,NULL OFF_CODE,"
                    + "NULL AUTH,ENT_DEPT,ENT_OFF, GETSPN(emp_TRAIN.ENT_AUTH) ENT_AUTH  ,NULL TEMPORD,NULL IS_LOCKED FROM emp_TRAIN where IF_VISIBLE='Y' and emp_id=?");
            pstSvRecord.setString(1, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                setTabName(esh);
                serviceHistory2.add(esh);
            }

            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * *****************Service Verification*********************
             */
            pstSvRecord = con.prepareStatement("SELECT EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,SV_ID ID,NOTE,'' ORDNO,tdate ORDDT,'SERVICE VERIFICATION CERTIFICATE' TAB_NAME,  AUTH_DEPT DEPT_CODE,AUTH_OFF OFF_CODE,GETSPN(emp_SV.AUTH) AUTH,AUTH_DEPT ENT_DEPT, "
                    + "AUTH_OFF ENT_OFF,  GETSPN(emp_SV.AUTH) ENT_AUTH  ,NULL TEMPORD,NULL IS_LOCKED FROM emp_SV where emp_id=?");
            pstSvRecord.setString(1, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                setTabName(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * *****************RELIEVE*********************
             */
            /*pstSvRecord = con.prepareStatement("SELECT EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,NOT_ID ID,NOTE,MEMO_NO ORDNO,MEMO_DATE ORDDT,'RELIEVE' TAB_NAME,  NULL DEPT_CODE,NULL OFF_CODE,NULL AUTH,ENT_DEPT,ENT_OFF,  GETSPN(EMP_RELIEVE.ENT_AUTH) ENT_AUTH,"
             + "'2' TEMPORD,NULL IS_LOCKED FROM EMP_RELIEVE where IF_VISIBLE='Y' AND emp_id=? AND NOT_TYPE!='CHNG_STRUCTURE' AND NOT_TYPE!='SUSPENSION' and NOT_ID is not null ");*/
            pstSvRecord = con.prepareStatement("SELECT EMP_RELIEVE.EMP_ID,EMP_RELIEVE.DOE,EMP_RELIEVE.TOE,EMP_RELIEVE.SB_DESCRIPTION,EMP_RELIEVE.IF_ASSUMED,EMP_RELIEVE.NOT_ID ID,EMP_RELIEVE.NOTE,"
                    + " MEMO_NO ORDNO,MEMO_DATE ORDDT,'RELIEVE' TAB_NAME,  NULL DEPT_CODE,NULL OFF_CODE,NULL AUTH,EMP_RELIEVE.ENT_DEPT,EMP_RELIEVE.ENT_OFF,GETSPN(EMP_RELIEVE.ENT_AUTH) ENT_AUTH"
                    + " FROM EMP_NOTIFICATION NOTI"
                    + " LEFT OUTER JOIN (SELECT * FROM EMP_RELIEVE WHERE EMP_ID=? AND (JOIN_ID IS NULL OR (JOIN_ID::TEXT NOT IN"
                    + " (SELECT JOIN_ID::TEXT FROM EMP_JOIN WHERE EMP_ID=? AND IF_AD_CHARGE='Y'))))EMP_RELIEVE ON NOTI.NOT_ID=EMP_RELIEVE.NOT_ID"
                    + " WHERE EMP_RELIEVE.EMP_ID=? and (NOTI.not_type ='REHABILITATION' or"
                    + " NOTI.not_type='ABSORPTION' or NOTI.not_type='REDEPLOYMENT' or NOTI.not_type='VALIDATION' or NOTI.not_type='LT_TRAINING' or"
                    + " NOTI.not_type='DEPUTATION' or NOTI.not_type ='TRANSFER' or NOTI.not_type ='PROMOTION' or NOTI.not_type ='REDESIGNATION' or"
                    + " NOTI.not_type ='RESIGNATION' or NOTI.not_type ='DECEASED' or NOTI.not_type ='RETIREMENT' or NOTI.not_type='ALLOT_CADRE' or NOTI.not_type ='REAPPOINTMENT' or (NOTI.not_type='LEAVE'"
                    + " AND NOTI.NOT_ID IN (SELECT NOT_ID FROM EMP_LEAVE WHERE EMP_ID=? AND IF_LONGTERM='Y')))"
                    + " and EMP_RELIEVE.IF_VISIBLE='Y' AND EMP_RELIEVE.emp_id=? AND EMP_RELIEVE.NOT_TYPE!='CHNG_STRUCTURE' AND EMP_RELIEVE.NOT_TYPE!='SUSPENSION' and EMP_RELIEVE.NOT_ID is not null");
            pstSvRecord.setString(1, empId);
            pstSvRecord.setString(2, empId);
            pstSvRecord.setString(3, empId);
            pstSvRecord.setString(4, empId);
            pstSvRecord.setString(5, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                setTabName(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * *****************JOINING*********************
             */
            /*pstSvRecord = con.prepareStatement("SELECT EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,NOT_ID ID,NOTE,MEMO_NO ORDNO,MEMO_DATE ORDDT,'JOINING' TAB_NAME,  NULL DEPT_CODE,NULL OFF_CODE, NULL AUTH ,ENT_DEPT,ENT_OFF,  G_SPC.SPN ENT_AUTH  ,"
             + "'3' TEMPORD,NULL IS_LOCKED FROM EMP_JOIN left outer join g_spc on emp_join.ent_auth=g_spc.spc where IF_VISIBLE='Y'  AND emp_id=? AND NOT_TYPE!='CHNG_STRUCTURE' AND NOT_TYPE!='ADDITIONAL_CHARGE' and not_id is not null ");*/
            pstSvRecord = con.prepareStatement("SELECT joi.EMP_ID,joi.DOE,joi.TOE,joi.SB_DESCRIPTION,joi.NOT_ID ID,joi.NOTE,'JOINING' TAB_NAME, G_SPC.SPN ENT_AUTH,joi.MEMO_DATE ORDDT"
                    + " FROM emp_notification"
                    + " left outer join emp_relieve reli on emp_notification.emp_id=reli.emp_id and"
                    + " emp_notification.not_id=reli.not_id and reli.join_id not in (Select join_id from emp_join where EMP_ID=? AND if_ad_charge='Y')"
                    + " left outer join emp_join joi on emp_notification.emp_id=joi.emp_id and emp_notification.not_id=joi.not_id"
                    + " left outer join g_spc on joi.ent_auth=g_spc.spc where joi.IF_VISIBLE='Y' AND joi.emp_id=? AND joi.NOT_TYPE!='CHNG_STRUCTURE' AND joi.NOT_TYPE!='ADDITIONAL_CHARGE' and"
                    + " (emp_notification.not_type='FIRST_APPOINTMENT' or emp_notification.not_type='REHABILITATION' or emp_notification.not_type='ABSORPTION' or emp_notification.not_type='REDEPLOYMENT' or"
                    + " emp_notification.not_type='VALIDATION' or emp_notification.not_type='LT_TRAINING' or emp_notification.not_type='DEPUTATION' or emp_notification.not_type='POSTING' or emp_notification.not_type='TRANSFER'"
                    + " or emp_notification.not_type='PROMOTION' or emp_notification.not_type='ADDITIONAL_CHARGE' or emp_notification.not_type='ALLOT_CADRE' or emp_notification.not_type='REDESIGNATION' or"
                    + " emp_notification.not_type='CHNG_STRUCTURE')");
            pstSvRecord.setString(1, empId);
            pstSvRecord.setString(2, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                setTabName(esh);
                setWefDate(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * *****************REINSTATEMENT*********************
             */
            /*pstSvRecord = con.prepareStatement("SELECT EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,reinstatement_id ID,NOTE,ORDNO,ORDDT,'REINSTATEMENT' TAB_NAME,  AUTH_DEPT DEPT_CODE,AUTH_OFF OFF_CODE,  GETSPN(EMP_REINSTATEMENT.AUTH) AUTH  ,ENT_DEPT,ENT_OFF,  "
             + "GETSPN(EMP_REINSTATEMENT.ENT_AUTH) ENT_AUTH  ,NULL TEMPORD,NULL IS_LOCKED FROM EMP_REINSTATEMENT where emp_id=? ");*/
            pstSvRecord = con.prepareStatement("SELECT EMP_REINSTATEMENT.EMP_ID,EMP_REINSTATEMENT.DOE,EMP_REINSTATEMENT.TOE,EMP_NOTIFICATION.SB_DESCRIPTION,EMP_REINSTATEMENT.IF_ASSUMED,reinstatement_id ID,EMP_REINSTATEMENT.NOTE,EMP_REINSTATEMENT.ORDNO,EMP_REINSTATEMENT.ORDDT,'REINSTATEMENT' TAB_NAME,  EMP_REINSTATEMENT.AUTH_DEPT DEPT_CODE,EMP_REINSTATEMENT.AUTH_OFF OFF_CODE,  GETSPN(EMP_REINSTATEMENT.AUTH) AUTH ,EMP_REINSTATEMENT.ENT_DEPT,EMP_REINSTATEMENT.ENT_OFF,GETSPN(EMP_REINSTATEMENT.ENT_AUTH) ENT_AUTH  ,NULL TEMPORD,NULL IS_LOCKED FROM EMP_REINSTATEMENT"
                    + " EMP_REINSTATEMENT INNER JOIN EMP_SUSPENSION ON EMP_REINSTATEMENT.REINSTATEMENT_ID=EMP_SUSPENSION.SP_ID"
                    + " INNER JOIN EMP_NOTIFICATION ON EMP_REINSTATEMENT.NOT_ID=EMP_NOTIFICATION.NOT_ID"
                    + " where EMP_REINSTATEMENT.emp_id=?"
                    + " AND EMP_SUSPENSION.emp_id=? and EMP_NOTIFICATION.IF_VISIBLE='Y' ");
            pstSvRecord.setString(1, empId);
            pstSvRecord.setString(2, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                setTabName(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * *****************SUSPENSION*********************
             */
            /*pstSvRecord = con.prepareStatement("SELECT EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,SP_ID ID,NOTE,ORDNO,ORDDT,'SUSPENSION' TAB_NAME,  AUTH_DEPT DEPT_CODE,AUTH_OFF OFF_CODE,  GETSPN(EMP_SUSPENSION.AUTH) AUTH   ,ENT_DEPT,ENT_OFF,  "
             + "GETSPN(EMP_SUSPENSION.ENT_AUTH) ENT_AUTH  ,NULL TEMPORD,NULL IS_LOCKED FROM EMP_SUSPENSION where IF_VISIBLE='Y' AND emp_id=? AND  (HQ_FIX IS NULL OR HQ_FIX = '') ");*/
            pstSvRecord = con.prepareStatement("SELECT EMP_SUSPENSION.EMP_ID,EMP_SUSPENSION.DOE,EMP_SUSPENSION.TOE,EMP_SUSPENSION.SB_DESCRIPTION,EMP_SUSPENSION.IF_ASSUMED,SP_ID ID,EMP_SUSPENSION.NOTE,EMP_SUSPENSION.ORDNO,EMP_SUSPENSION.ORDDT,'SUSPENSION' TAB_NAME,  EMP_SUSPENSION.AUTH_DEPT DEPT_CODE,EMP_SUSPENSION.AUTH_OFF OFF_CODE,  GETSPN(EMP_SUSPENSION.AUTH) AUTH,EMP_SUSPENSION.ENT_DEPT,EMP_SUSPENSION.ENT_OFF,"
                    + " GETSPN(EMP_SUSPENSION.ENT_AUTH) ENT_AUTH  ,NULL TEMPORD,NULL IS_LOCKED FROM EMP_SUSPENSION"
                    + " INNER JOIN EMP_NOTIFICATION ON EMP_SUSPENSION.NOT_ID=EMP_NOTIFICATION.NOT_ID"
                    + " where EMP_SUSPENSION.IF_VISIBLE='Y' AND EMP_SUSPENSION.emp_id=? AND EMP_NOTIFICATION.emp_id=? AND (HQ_FIX IS NULL OR HQ_FIX = '')");
            pstSvRecord.setString(1, empId);
            pstSvRecord.setString(2, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                setTabName(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * *****************HEAD QUARTER FIXATION*********************
             */
            pstSvRecord = con.prepareStatement("SELECT EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,SP_ID ID,NOTE,ORDNO,ORDDT,'HEAD QUARTER FIXATION' TAB_NAME,  AUTH_DEPT DEPT_CODE,AUTH_OFF OFF_CODE,  GETSPN(EMP_SUSPENSION.AUTH) AUTH   ,ENT_DEPT,ENT_OFF,  "
                    + "GETSPN(EMP_SUSPENSION.ENT_AUTH) ENT_AUTH  ,NULL TEMPORD,NULL IS_LOCKED FROM EMP_SUSPENSION where IF_VISIBLE='Y' AND emp_id=? AND HQ_FIX IS NOT NULL ");
            pstSvRecord.setString(1, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                setTabName(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * *****************PERMISSION*********************
             */
            pstSvRecord = con.prepareStatement("SELECT EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,PER_ID ID,NOTE,ORD_NO ORDNO,ORD_DATE ORDDT,'PERMISSION' TAB_NAME,  DEPT_CODE,OFF_CODE,  GETSPN(EMP_PERMISSION.AUTH) AUTH   , ENT_DEPT,ENT_OFF,  "
                    + "GETSPN(EMP_PERMISSION.ENT_AUTH) ENT_AUTH  ,NULL TEMPORD,NULL IS_LOCKED FROM EMP_PERMISSION where emp_id=? AND IF_VISIBLE='Y' ");
            pstSvRecord.setString(1, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                setTabName(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * *****************EDUCATION*********************
             */
            pstSvRecord = con.prepareStatement("SELECT EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,QFN_ID ID,NOTE,'' ORDNO,NULL ORDDT,'EDUCATION' TAB_NAME,  NULL DEPT_CODE,NULL OFF_CODE,NULL AUTH,ENT_DEPT,ENT_OFF,  "
                    + "GETSPN(EMP_QUALIFICATION.ENT_AUTH) ENT_AUTH  ,NULL TEMPORD,NULL IS_LOCKED  FROM EMP_QUALIFICATION where emp_id=? AND DOE IS NOT NULL AND (SB_DESCRIPTION IS NOT NULL AND SB_DESCRIPTION<>'')");
            pstSvRecord.setString(1, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                setTabName(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * *****************`````````````````````MENT*********************
             */
            pstSvRecord = con.prepareStatement("SELECT EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,RET_ID ID,NOTE,ORD_NO ORDNO,ORD_DATE ORDDT,'RETIREMENT' TAB_NAME,  AUTH_DEPT DEPT_CODE,AUTH_OFF OFF_CODE,  GETSPN(EMP_RET_RES.AUTH) AUTH  ,ENT_DEPT,ENT_OFF,  "
                    + "GETSPN(EMP_RET_RES.ENT_AUTH) ENT_AUTH  ,NULL TEMPORD,NULL IS_LOCKED  FROM EMP_RET_RES where emp_id=?");
            pstSvRecord.setString(1, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                setTabName(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * *****************MISC*********************
             */
            /*pstSvRecord = con.prepareStatement("SELECT EMP_ID,DOE,TOE,'' SB_DESCRIPTION,IF_ASSUMED,MISC_ID ID,'' NOTE,'' ORDNO,NULL ORDDT,'MISC' TAB_NAME,  NULL DEPT_CODE,NULL OFF_CODE,NULL AUTH,NULL ENT_DEPT,NULL ENT_OFF,NULL ENT_AUTH,NULL TEMPORD,NULL IS_LOCKED "
             + "FROM EMP_MISC where emp_id=?");
             pstSvRecord.setString(1, empId);
             resultSet = pstSvRecord.executeQuery();
             serviceHistory2 = new ArrayList<>();
             while (resultSet.next()) {
             EmpServiceHistory esh = new EmpServiceHistory();
             esh.setEmpId(empId);
             esh.setNoteId(resultSet.getInt("ID"));
             esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
             esh.setToe(resultSet.getString("TOE"));
             esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
             esh.setTabname(resultSet.getString("TAB_NAME"));
             esh.setModuleNote(resultSet.getString("NOTE"));
             esh.setEntauth(resultSet.getString("ENT_AUTH"));
             esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
             setTabName(esh);
             serviceHistory2.add(esh);
             }
             serviceHistory1 = serviceHistory;
             serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);*/
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);
            /**
             * *****************SERVICE RECORD*********************
             */
            pstSvRecord = con.prepareStatement("SELECT EMP_ID,DOE,TOE,'' SB_DESCRIPTION,IF_ASSUMED,SR_ID ID,NOTE,'' ORDNO,NULL ORDDT,'SERVICE RECORD' TAB_NAME,  NULL DEPT_CODE,NULL OFF_CODE,NULL AUTH,ENT_DEPT,ENT_OFF,  GETSPN(EMP_SERVICERECORD.ENT_AUTH) ENT_AUTH, "
                    + "NULL TEMPORD,NULL IS_LOCKED FROM EMP_SERVICERECORD WHERE EMP_ID=?");
            pstSvRecord.setString(1, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                setTabName(esh);
                //serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * *****************SERVICE PAY RECORD*********************
             */
            pstSvRecord = con.prepareStatement("SELECT EMP_ID,DOE,TOE,'' SB_DESCRIPTION,IF_ASSUMED,SRP_ID ID,'' NOTE,'' ORDNO,NULL ORDDT,'SERVICE PAY RECORD' TAB_NAME,  NULL DEPT_CODE,NULL OFF_CODE,NULL AUTH,ENT_DEPT,ENT_OFF,  GETSPN(EMP_SR_PAY.ENT_AUTH) ENT_AUTH,"
                    + "NULL TEMPORD,NULL IS_LOCKED FROM EMP_SR_PAY WHERE EMP_ID=?");
            pstSvRecord.setString(1, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                setTabName(esh);
                //serviceHistory2.add(esh);
            }
            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * **************REGULARIZATION OF CONTRACTUAL SIX YEAR SERVICE TO
             * REGULAR***********************************
             */
            pstSvRecord = con.prepareStatement("select * from emp_regularization_contractual where emp_id=? and if_visible='Y' ");
            pstSvRecord.setString(1, empId);
            resultSet = pstSvRecord.executeQuery();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setCategory("REGULARIZATION OF CONTRACTUAL SIX YEAR SERVICE TO REGULAR");
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                serviceHistory2.add(esh);
            }
            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);
            
             /**
             * **************SUPERANNUATION**********************
             */
            pstSvRecord = con.prepareStatement("SELECT DOS,getpostnamefromspc(auth) retdFromdesignation,* FROM \n"
                    + "EMP_NOTIFICATION INNER JOIN EMP_MAST ON EMP_NOTIFICATION.EMP_ID=EMP_MAST.EMP_ID\n"
                    + "WHERE NOT_TYPE='SUPERANNUATION' AND IF_VISIBLE='Y' AND EMP_NOTIFICATION.EMP_ID=?");
            pstSvRecord.setString(1, empId);
            resultSet = pstSvRecord.executeQuery();
            if (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setCategory("SUPERANNUATION");
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setSpn(resultSet.getString("retdFromdesignation"));
                esh.setPay(resultSet.getString("ret_pay"));
                esh.setWefChange(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOS")));
                serviceHistory2.add(esh);
            }
            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            Collections.sort(serviceHistory, new Comparator<EmpServiceHistory>() {
                @Override
                public int compare(EmpServiceHistory esp1, EmpServiceHistory esp2) {
                    Date date1 = CommonFunctions.getDateFromString(esp1.getOrdDate(), "dd-MMM-yyyy");
                    Date date2 = CommonFunctions.getDateFromString(esp2.getOrdDate(), "dd-MMM-yyyy");
                    return date1.compareTo(date2);
                }
            });
            eap.setEmpsbrecord(serviceHistory);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return eap;
    }

    @Override
    public List getServiceBookAnnexureAData(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List annexureAList = new ArrayList();
        EmpServiceHistory esh = null;
        try {
            con = this.repodataSource.getConnection();

            String sql = "select join_date,rlv_date, post, emp_join.sv_id, off_en from emp_join"
                    + " left outer join emp_relieve on emp_join.join_id=emp_relieve.join_id"
                    + " inner join g_spc on emp_join.spc=g_spc.spc"
                    + " inner join g_post on g_spc.gpc=g_post.post_code"
                    + " inner join g_office on g_spc.off_code=g_office.off_code"
                    + " where emp_join.emp_id=? and emp_join.if_visible='Y' order by join_date";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            while (rs.next()) {
                esh = new EmpServiceHistory();
                esh.setFrmDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("join_date")));
                esh.setToDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("rlv_date")));
                esh.setSpn(rs.getString("post"));
                esh.setOffen(rs.getString("off_en"));
                esh.setIfSV(rs.getString("sv_id"));
                esh.setRemarksForNoSV("");
                annexureAList.add(esh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return annexureAList;
    }

    @Override
    public List getServiceBookAnnexureBData(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List annexureBList = new ArrayList();
        EmpServiceHistory esh = null;

        try {
            con = this.repodataSource.getConnection();
            Calendar cal = Calendar.getInstance();
            Date dt = null;
            DateFormat formatDate = new SimpleDateFormat("dd-MMM-yyyy");

            String sql = "select emp_pay_record.not_type, wef, off_en,pay_scale, pay, gp, (select cur_desg from aq_mast where emp_code=? and aq_month=extract(month from wef) and aq_year=extract(year from wef) limit 1 ) spn,increment_reason from emp_pay_record"
                    + " inner join emp_notification noti on emp_pay_record.not_id=noti.not_id"
                    + " left outer join g_office on noti.off_code=g_office.off_code"
                    + " where emp_pay_record.emp_id=?"
                    + " and emp_pay_record.not_type!='TRANSFER'"
                    + " and noti.if_visible='Y'"
                    + " order by emp_pay_record.wef";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setString(2, empid);
            rs = pst.executeQuery();
            while (rs.next()) {
                esh = new EmpServiceHistory();
                if (rs.getString("not_type") != null && !rs.getString("not_type").equals("")) {
                    if (rs.getString("not_type").equalsIgnoreCase("FIRST_APPOINTMENT")) {
                        esh.setTabname("REGULAR RECRUITMENT");
                    } else {
                        esh.setTabname(rs.getString("not_type"));
                    }
                }

                if (rs.getDate("wef") != null) {
                    esh.setWefChange(CommonFunctions.getFormattedOutputDate1(rs.getDate("wef")));
                    cal.setTime(rs.getDate("wef"));
                    cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1);
                    dt = cal.getTime();
                    esh.setNextwefChange(formatDate.format(dt));
                }
                esh.setOffen(rs.getString("off_en"));
                esh.setSpn(rs.getString("spn"));
                esh.setPayscale(rs.getString("pay_scale"));
                int pay = rs.getInt("pay");
                int gp = rs.getInt("gp");
                int paygp = pay + gp;
                esh.setPay(paygp + "");

                esh.setRemarksForNoSV(rs.getString("increment_reason"));

                annexureBList.add(esh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return annexureBList;
    }

    @Override
    public List getServiceBookAnnexureDData(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List annexureDList = new ArrayList();
        EmpServiceHistory esh = null;

        try {
            con = this.repodataSource.getConnection();

            String sql = "select emp_loan_sanc.loan_tp,loan_name,p_org_amt,p_instl_amt,p_cum_recovered,(p_org_amt-p_cum_recovered) balance from emp_loan_sanc"
                    + " inner join aq_dtls on emp_loan_sanc.loanid=aq_dtls.ad_ref_id"
                    + " inner join g_loan on emp_loan_sanc.loan_tp=g_loan.loan_tp where emp_id=? order by loan_name";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            while (rs.next()) {
                esh = new EmpServiceHistory();
                esh.setTypeOfAdvance(rs.getString("loan_name"));
                esh.setAdvanceAmt(rs.getString("p_org_amt"));
                esh.setAmtRepaid(rs.getString("p_instl_amt"));
                esh.setAmtOutstanding(rs.getString("balance"));
                annexureDList.add(esh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return annexureDList;
    }

    @Override
    public List getServiceBookAnnexureEData(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List annexureEList = new ArrayList();
        EmpServiceHistory esh = null;

        try {
            con = this.repodataSource.getConnection();

            String sql = "select cause,doi,off_en,dcr from emp_proceeding"
                    + " left outer join g_spc on emp_proceeding.cur_spc=g_spc.spc"
                    + " left outer join g_office on g_spc.off_code=g_office.off_code where emp_id=? order by doi";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            while (rs.next()) {
                esh = new EmpServiceHistory();
                esh.setDetailsDisciplinaryProceeding(rs.getString("cause"));
                esh.setDateOfInitiation(CommonFunctions.getFormattedOutputDate1(rs.getDate("doi")));
                esh.setOfficeDisciplinaryProceeding(rs.getString("off_en"));
                esh.setDateOfFinalization(CommonFunctions.getFormattedOutputDate1(rs.getDate("dcr")));
                annexureEList.add(esh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return annexureEList;
    }

    @Override
    public SelectOption getInitialJoiningData(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        SelectOption so = new SelectOption();
        try {
            con = this.repodataSource.getConnection();

            /*String sql = "select emp_id,post,off_en from emp_join"
             + " left outer join g_office on emp_join.auth_off=g_office.off_code"
             + " left outer join g_spc on emp_join.spc=g_spc.spc"
             + " left outer join g_post on g_spc.gpc=g_post.post_code"
             + " where emp_id=? and not_type='FIRST_APPOINTMENT'";*/
            String sql = "select emp_id,post,g_office.off_en,emp_join.auth_off,emp_join.spc,emp_join.organization_type,g_oth_spc.off_en goi_off_en,g_oth_postname.postname goipostname from emp_join"
                    + " left outer join g_office on emp_join.auth_off=g_office.off_code"
                    + " left outer join g_spc on emp_join.spc=g_spc.spc"
                    + " left outer join g_post on g_spc.gpc=g_post.post_code"
                    + " left outer join g_oth_spc on emp_join.auth_off=g_oth_spc.other_spc_id::TEXT"
                    + " left outer join g_oth_postname on g_oth_spc.other_spc_id=g_oth_postname.office_id"
                    + " where emp_id=? and not_type='FIRST_APPOINTMENT' and emp_join.not_id is not null";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getString("organization_type") != null && rs.getString("organization_type").equals("GOI")) {
                    so.setLabel(rs.getString("goi_off_en"));
                    so.setValue(rs.getString("goipostname"));
                } else {
                    so.setLabel(rs.getString("off_en"));
                    so.setValue(rs.getString("post"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return so;
    }

    @Override
    public String saveServiceBookAnomali(ServiceBookAnomali serviceBookAnomali) {

        String supportingDocument_disk_filename = null;
        String supportingDocumentoriginalfilename = null;
        String supportingDocument_file_type = null;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String msg = "Sucessfully Saved";
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("INSERT INTO servicebook_anomalies (emp_id,service_period_from,service_period_to,postingdeails,anomali_type,"
                    + "further_desc,status) VALUES (?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, serviceBookAnomali.getEmpId());
            if (serviceBookAnomali.getServicePeriodFrom() != null && !serviceBookAnomali.getServicePeriodFrom().equals("")) {
                pstmt.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(serviceBookAnomali.getServicePeriodFrom()).getTime()));
            } else {
                pstmt.setTimestamp(2, null);
            }
            if (serviceBookAnomali.getServicePeriodTo() != null && !serviceBookAnomali.getServicePeriodTo().equals("")) {
                pstmt.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(serviceBookAnomali.getServicePeriodTo()).getTime()));
            } else {
                pstmt.setTimestamp(3, null);
            }
            pstmt.setString(4, serviceBookAnomali.getPostingdeails());
            pstmt.setString(5, serviceBookAnomali.getAnomaliType());
            pstmt.setString(6, serviceBookAnomali.getFurtherDesc());
            pstmt.setString(7, "P");
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                serviceBookAnomali.setAnomaliId(rs.getInt(1));
            }
            if (serviceBookAnomali.getSupportingDocument() != null && !serviceBookAnomali.getSupportingDocument().isEmpty()) {
                supportingDocumentoriginalfilename = serviceBookAnomali.getSupportingDocument().getOriginalFilename();
                supportingDocument_disk_filename = new Date().getTime() + "";
                supportingDocument_file_type = serviceBookAnomali.getSupportingDocument().getContentType();
                byte[] bytes = serviceBookAnomali.getSupportingDocument().getBytes();
                File dir = new File(this.uploadPath + File.separator);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + supportingDocument_disk_filename);
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();
                pstmt = con.prepareStatement("UPDATE servicebook_anomalies SET document_disk_file_name=?, document_original_file_name = ?,document_original_file_type=? "
                        + "where anomali_id=?");
                pstmt.setString(1, supportingDocument_disk_filename);
                pstmt.setString(2, supportingDocumentoriginalfilename);
                pstmt.setString(3, supportingDocument_file_type);
                pstmt.setInt(4, serviceBookAnomali.getAnomaliId());
                pstmt.executeUpdate();
            }

        } catch (Exception e) {
            msg = "Error Occured";
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return msg;
    }

    @Override
    public List getAnomaliesListEmployee(String empId) {
        List anomaliesList = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = this.repodataSource.getConnection();
            pstmt = con.prepareStatement("SELECT emp_id,service_period_from,service_period_to,postingdeails,anomali_type,further_desc,"
                    + "document_original_file_name,document_original_file_type,status FROM servicebook_anomalies WHERE emp_id=?");
            pstmt.setString(1, empId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                ServiceBookAnomali serviceBookAnomali = new ServiceBookAnomali();
                serviceBookAnomali.setServicePeriodFrom(CommonFunctions.getFormattedOutputDate1(rs.getDate("service_period_from")));
                serviceBookAnomali.setServicePeriodTo(CommonFunctions.getFormattedOutputDate1(rs.getDate("service_period_to")));
                serviceBookAnomali.setPostingdeails(rs.getString("postingdeails"));
                serviceBookAnomali.setAnomaliType(rs.getString("anomali_type"));
                serviceBookAnomali.setFurtherDesc(rs.getString("further_desc"));
                serviceBookAnomali.setOriginalFileName(rs.getString("document_original_file_name"));
                serviceBookAnomali.setFileType(rs.getString("document_original_file_type"));
                serviceBookAnomali.setStatus(rs.getString("status"));
                anomaliesList.add(serviceBookAnomali);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return anomaliesList;
    }

    @Override
    public List getAnomaliesListOffice(String offcode) {
        List anomaliesList = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = this.repodataSource.getConnection();
            pstmt = con.prepareStatement("select anomali_id,emp_mast.emp_id,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMPNAME,"
                    + "service_period_from,service_period_to,postingdeails,anomali_type,further_desc,"
                    + "document_original_file_name,document_original_file_type,status from servicebook_anomalies "
                    + "inner join emp_mast on servicebook_anomalies.emp_id = emp_mast.emp_id "
                    + "where cur_off_code = ?");
            pstmt.setString(1, offcode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                ServiceBookAnomali serviceBookAnomali = new ServiceBookAnomali();
                serviceBookAnomali.setAnomaliId(rs.getInt("anomali_id"));
                serviceBookAnomali.setEmpId(rs.getString("emp_id"));
                serviceBookAnomali.setEmpname(rs.getString("EMPNAME"));
                serviceBookAnomali.setServicePeriodFrom(CommonFunctions.getFormattedOutputDate1(rs.getDate("service_period_from")));
                serviceBookAnomali.setServicePeriodTo(CommonFunctions.getFormattedOutputDate1(rs.getDate("service_period_to")));
                serviceBookAnomali.setPostingdeails(rs.getString("postingdeails"));
                serviceBookAnomali.setAnomaliType(rs.getString("anomali_type"));
                serviceBookAnomali.setFurtherDesc(rs.getString("further_desc"));
                serviceBookAnomali.setOriginalFileName(rs.getString("document_original_file_name"));
                serviceBookAnomali.setFileType(rs.getString("document_original_file_type"));
                serviceBookAnomali.setStatus(rs.getString("status"));
                anomaliesList.add(serviceBookAnomali);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return anomaliesList;
    }

    @Override
    public FileDownload getFileDownloadData(int anomaliId) {
        FileDownload fileDownload = new FileDownload();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = this.repodataSource.getConnection();
            pstmt = con.prepareStatement("SELECT document_disk_file_name, document_original_file_name,document_original_file_type "
                    + "from servicebook_anomalies where anomali_id=?");
            pstmt.setInt(1, anomaliId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                fileDownload.setOriginalfilename(rs.getString("document_original_file_name"));
                fileDownload.setFiletype(rs.getString("document_original_file_type"));
                File f = new File(this.uploadPath + File.separator + rs.getString("document_disk_file_name"));
                fileDownload.setFilecontent(FileUtils.readFileToByteArray(f));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt, con);
        }
        return fileDownload;
    }

    @Override
    public List getEmployeeList(String loginempid, String loginspc) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        SelectOption so = null;
        List emplist = new ArrayList();
        try {
            con = this.repodataSource.getConnection();

            String sql = "SELECT emp_id,gpf_no,ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,off_en FROM process_authorization "
                    + " inner join emp_mast on process_authorization.off_code=emp_mast.cur_off_code"
                    + " inner join g_office on process_authorization.off_code=g_office.off_code"
                    + " WHERE hrms_id=? and spc=? AND process_id=22 and is_regular in ('Y','C','G','W') and dep_code NOT IN('00', '08', '09', '10', '13', '14')"
                    + " order by f_name";

            pst = con.prepareStatement(sql);
            pst.setString(1, loginempid);
            pst.setString(2, loginspc);
            rs = pst.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("emp_id"));
                so.setLabel(rs.getString("EMP_NAME") + "(" + rs.getString("gpf_no") + ")" + " (" + rs.getString("off_en") + ")");
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

    @Override
    public EmpServiceBook getSHReportValidator(String empId) {

        Connection con = null;
        PreparedStatement pstSvRecord = null;
        ResultSet resultSet = null;
        List<EmpServiceHistory> serviceHistory = new ArrayList<>();
        EmpServiceBook eap = new EmpServiceBook();
        try {
            con = this.repodataSource.getConnection();
            pstSvRecord = con.prepareStatement("SELECT is_validated,emp_notification.EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,emp_notification.NOT_ID ID,NOTE, "
                    + " ORDNO,ORDDT,emp_notification.NOT_TYPE TAB_NAME,emp_notification.DEPT_CODE,emp_notification.OFF_CODE, SPC1.SPN AUTH , "
                    + " ENT_DEPT,ENT_OFF, SPC2.SPN ENT_AUTH,'1' TEMPORD,IS_LOCKED,ENTRY_TYPE FROM emp_notification "
                    + " LEFT OUTER JOIN G_SPC SPC1 ON emp_notification.AUTH=SPC1.SPC "
                    + " LEFT OUTER JOIN G_SPC SPC2 ON emp_notification.ENT_AUTH=SPC2.SPC "
                    + " WHERE emp_notification.emp_id=? AND IF_VISIBLE='Y' AND NOT_TYPE!='CHNG_STRUCTURE' AND NOT_TYPE!='LEAVE' AND NOT_TYPE!='RESULT' "
                    + " AND NOT_TYPE!='COMPLIANCE' AND NOT_TYPE!='SC_NOTICE' AND NOT_TYPE!='INCREMENT' and "
                    + " NOT_TYPE!='LOAN_SANC' and NOT_TYPE!='REINSTATEMENT' and NOT_TYPE!='SUSPENSION' and (SB_DESCRIPTION is not null and SB_DESCRIPTION<>'')");
            pstSvRecord.setString(1, empId);
            resultSet = pstSvRecord.executeQuery();
            List<EmpServiceHistory> serviceHistory1 = new ArrayList<EmpServiceHistory>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                esh.setIsValidated(resultSet.getString("is_validated"));
                if (resultSet.getString("ENTRY_TYPE") != null && !resultSet.getString("ENTRY_TYPE").equals("")) {
                    if (resultSet.getString("ENTRY_TYPE").equals("C")) {
                        esh.setEntryType("Current");
                    } else if (resultSet.getString("ENTRY_TYPE").equals("S")) {
                        esh.setEntryType("BackLog");
                    }
                }

                setTabName(esh);
                setWefDate(esh);
                serviceHistory1.add(esh);
            }

            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            /**
             * **************************************INCREMENT
             */
            pstSvRecord = con.prepareStatement("SELECT is_validated,emp_notification.EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,emp_notification.NOT_ID ID,NOTE,ORDNO,ORDDT,emp_notification.NOT_TYPE TAB_NAME,emp_notification.DEPT_CODE,emp_notification.OFF_CODE, "
                    + " SPC1.SPN AUTH,ENT_DEPT,ENT_OFF, "
                    + " SPC2.SPN ENT_AUTH,'1' TEMPORD,IS_LOCKED  FROM "
                    + " (SELECT * FROM EMP_INCR WHERE emp_id=? AND PRID IS NULL) EMP_INCR "
                    + " inner join (select * from emp_notification where NOT_TYPE='INCREMENT' and emp_id=? and (if_visible='Y' or if_visible is null or if_visible='')) "
                    + " emp_notification ON EMP_INCR.NOT_ID = emp_notification.NOT_ID "
                    + " LEFT OUTER JOIN G_SPC SPC1 ON emp_notification.AUTH=SPC1.SPC "
                    + " LEFT OUTER JOIN G_SPC SPC2 ON emp_notification.ENT_AUTH=SPC2.SPC ");
            pstSvRecord.setString(1, empId);
            pstSvRecord.setString(2, empId);
            resultSet = pstSvRecord.executeQuery();
            ArrayList<EmpServiceHistory> serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                esh.setIsValidated(resultSet.getString("is_validated"));
                setTabName(esh);
                setWefDate(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * **************************************LEAVE
             */
            pstSvRecord = con.prepareStatement("SELECT is_validated,EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,NOT_ID ID,NOTE,ORDNO,ORDDT,NOT_TYPE TAB_NAME,DEPT_CODE,OFF_CODE,  GETSPN(emp_notification.AUTH) AUTH  ,ENT_DEPT,ENT_OFF,  GETSPN(emp_notification.ENT_AUTH) ENT_AUTH, "
                    + "'1' TEMPORD,IS_LOCKED FROM emp_notification where EMP_NOTIFICATION.NOT_ID IN (SELECT NOT_ID FROM EMP_LEAVE WHERE EMP_LEAVE.EMP_ID=? AND LSOT_ID='01') AND EMP_NOTIFICATION.EMP_ID=? AND NOT_TYPE='LEAVE' AND  "
                    + "STATUS = 'SANCTIONED AND AVAILED'");
            pstSvRecord.setString(1, empId);
            pstSvRecord.setString(2, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                esh.setIsValidated(resultSet.getString("is_validated"));
                setTabName(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * ************************************** LEAVE SANCTIONED
             */
            pstSvRecord = con.prepareStatement("SELECT is_validated,EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,NOT_ID ID,NOTE,ORDNO,ORDDT,NOT_TYPE TAB_NAME,DEPT_CODE,OFF_CODE,  GETSPN(emp_notification.AUTH) AUTH  ,ENT_DEPT,ENT_OFF,  GETSPN(emp_notification.ENT_AUTH) ENT_AUTH  , "
                    + "	'1' TEMPORD,IS_LOCKED FROM emp_notification where EMP_NOTIFICATION.NOT_ID IN (SELECT NOT_ID FROM EMP_LEAVE WHERE EMP_LEAVE.EMP_ID=? AND LSOT_ID='02') AND EMP_NOTIFICATION.EMP_ID=? AND NOT_TYPE='LEAVE' "
                    + "	AND STATUS = 'SANCTIONED' AND EMP_NOTIFICATION.IF_VISIBLE='Y' ");
            pstSvRecord.setString(1, empId);
            pstSvRecord.setString(2, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                esh.setIsValidated(resultSet.getString("is_validated"));
                setTabName(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * **************************************
             */
            pstSvRecord = con.prepareStatement("SELECT is_validated,EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,NOT_ID ID,NOTE,ORDNO,ORDDT,NOT_TYPE TAB_NAME,DEPT_CODE,OFF_CODE,  GETSPN(emp_notification.AUTH) AUTH  ,ENT_DEPT,ENT_OFF,  GETSPN(emp_notification.ENT_AUTH) ENT_AUTH,"
                    + "'1' TEMPORD,NULL IS_LOCKED FROM emp_notification where EMP_NOTIFICATION.NOT_ID IN (SELECT NOT_ID FROM EMP_LEAVE WHERE EMP_LEAVE.EMP_ID=? AND LSOT_ID!='01' AND LSOT_ID!='02') AND EMP_NOTIFICATION.EMP_ID=? AND NOT_TYPE='LEAVE' AND EMP_NOTIFICATION.IF_VISIBLE='Y' ");
            pstSvRecord.setString(1, empId);
            pstSvRecord.setString(2, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                esh.setIsValidated(resultSet.getString("is_validated"));
                setTabName(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * ************************************** LOAN SANCTION
             */
            pstSvRecord = con.prepareStatement("SELECT is_validated,emp_notification.EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,emp_notification.NOT_ID ID,NOTE,ORDNO,ORDDT,emp_notification.NOT_TYPE  "
                    + " TAB_NAME,emp_notification.DEPT_CODE,emp_notification.OFF_CODE, SPC1.SPN AUTH ,ENT_DEPT,ENT_OFF, "
                    + " SPC2.SPN ENT_AUTH,'1' TEMPORD,IS_LOCKED FROM emp_notification  "
                    + " inner join emp_loan_sanc on emp_notification.not_id=emp_loan_sanc.not_id "
                    + " LEFT OUTER JOIN G_SPC SPC1 ON emp_notification.AUTH=SPC1.SPC "
                    + " LEFT OUTER JOIN G_SPC SPC2 ON emp_notification.ENT_AUTH=SPC2.SPC "
                    + " WHERE emp_notification.emp_id=? AND IF_VISIBLE='Y'  "
                    + " AND emp_loan_sanc.NOT_TYPE='LOAN_SANC'  "
                    + " and (emp_loan_sanc.loan_tp='BICA' or emp_loan_sanc.loan_tp='CMPA' or emp_loan_sanc.loan_tp='MOPA' or emp_loan_sanc.loan_tp='HBA' or emp_loan_sanc.loan_tp='MCA' or emp_loan_sanc.loan_tp='VE' or emp_loan_sanc.loan_tp='SHBA') and (SB_DESCRIPTION is not null and SB_DESCRIPTION<>'')");
            pstSvRecord.setString(1, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                esh.setIsValidated(resultSet.getString("is_validated"));
                setTabName(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * ************************************** EXAMINATION
             */
            pstSvRecord = con.prepareStatement("SELECT is_validated,EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,EX_ID ID,NOTE,NOT_NO ORDNO,NOT_DT ORDDT,'EXAMINATION' TAB_NAME,  DEPT_CODE,AUTH_OFF OFF_CODE,  GETSPN(emp_exam.AUTH) AUTH  ,ENT_DEPT,ENT_OFF,  "
                    + "GETSPN(emp_exam.ENT_AUTH) ENT_AUTH  ,NULL TEMPORD,NULL IS_LOCKED FROM emp_exam where IF_VISIBLE='Y' AND emp_id=?");
            pstSvRecord.setString(1, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                esh.setIsValidated(resultSet.getString("is_validated"));
                setTabName(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * *****************Training*********************
             */
            pstSvRecord = con.prepareStatement("SELECT is_validated,EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,TRAINID ID,NOTE,ord_no ORDNO,ord_date ORDDT,'TRAINING' TAB_NAME,  NULL DEPT_CODE,NULL OFF_CODE,"
                    + "NULL AUTH,ENT_DEPT,ENT_OFF, GETSPN(emp_TRAIN.ENT_AUTH) ENT_AUTH  ,NULL TEMPORD,NULL IS_LOCKED FROM emp_TRAIN where IF_VISIBLE='Y' and emp_id=?");
            pstSvRecord.setString(1, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                esh.setIsValidated(resultSet.getString("is_validated"));
                setTabName(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * *****************Service Verification*********************
             */
            pstSvRecord = con.prepareStatement("SELECT is_validated,EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,SV_ID ID,NOTE,'' ORDNO,tdate ORDDT,'SERVICE VERIFICATION CERTIFICATE' TAB_NAME,  AUTH_DEPT DEPT_CODE,AUTH_OFF OFF_CODE,GETSPN(emp_SV.AUTH) AUTH,AUTH_DEPT ENT_DEPT, "
                    + "AUTH_OFF ENT_OFF,  GETSPN(emp_SV.AUTH) ENT_AUTH  ,NULL TEMPORD,NULL IS_LOCKED FROM emp_SV where emp_id=?");
            pstSvRecord.setString(1, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                esh.setIsValidated(resultSet.getString("is_validated"));
                setTabName(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * *****************RELIEVE*********************
             */
            /*pstSvRecord = con.prepareStatement("SELECT is_validated,EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,NOT_ID ID,NOTE,MEMO_NO ORDNO,MEMO_DATE ORDDT,'RELIEVE' TAB_NAME,  NULL DEPT_CODE,NULL OFF_CODE,NULL AUTH,ENT_DEPT,ENT_OFF,  GETSPN(EMP_RELIEVE.ENT_AUTH) ENT_AUTH,"
             + "'2' TEMPORD,NULL IS_LOCKED FROM EMP_RELIEVE where IF_VISIBLE='Y' and NOT_ID is not null AND emp_id=? AND NOT_TYPE!='CHNG_STRUCTURE' AND NOT_TYPE!='SUSPENSION' ");*/
            pstSvRecord = con.prepareStatement("SELECT EMP_RELIEVE.is_validated,EMP_RELIEVE.EMP_ID,EMP_RELIEVE.DOE,EMP_RELIEVE.TOE,EMP_RELIEVE.SB_DESCRIPTION,EMP_RELIEVE.IF_ASSUMED,EMP_RELIEVE.NOT_ID ID,EMP_RELIEVE.NOTE,"
                    + " MEMO_NO ORDNO,MEMO_DATE ORDDT,'RELIEVE' TAB_NAME,  NULL DEPT_CODE,NULL OFF_CODE,NULL AUTH,EMP_RELIEVE.ENT_DEPT,EMP_RELIEVE.ENT_OFF,GETSPN(EMP_RELIEVE.ENT_AUTH) ENT_AUTH"
                    + " FROM EMP_NOTIFICATION NOTI"
                    + " LEFT OUTER JOIN (SELECT * FROM EMP_RELIEVE WHERE EMP_ID=? AND (JOIN_ID IS NULL OR (JOIN_ID::TEXT NOT IN"
                    + " (SELECT JOIN_ID::TEXT FROM EMP_JOIN WHERE EMP_ID=? AND IF_AD_CHARGE='Y'))))EMP_RELIEVE ON NOTI.NOT_ID=EMP_RELIEVE.NOT_ID"
                    + " WHERE EMP_RELIEVE.EMP_ID=? and (NOTI.not_type ='REHABILITATION' or"
                    + " NOTI.not_type='ABSORPTION' or NOTI.not_type='REDEPLOYMENT' or NOTI.not_type='VALIDATION' or NOTI.not_type='LT_TRAINING' or"
                    + " NOTI.not_type='DEPUTATION' or NOTI.not_type ='TRANSFER' or NOTI.not_type ='PROMOTION' or NOTI.not_type ='REDESIGNATION' or"
                    + " NOTI.not_type ='RESIGNATION' or NOTI.not_type ='DECEASED' or NOTI.not_type ='RETIREMENT' or NOTI.not_type='ALLOT_CADRE' or (NOTI.not_type='LEAVE'"
                    + " AND NOTI.NOT_ID IN (SELECT NOT_ID FROM EMP_LEAVE WHERE EMP_ID=? AND IF_LONGTERM='Y')))"
                    + " and EMP_RELIEVE.IF_VISIBLE='Y' AND EMP_RELIEVE.emp_id=? AND EMP_RELIEVE.NOT_TYPE!='CHNG_STRUCTURE' AND EMP_RELIEVE.NOT_TYPE!='SUSPENSION' and EMP_RELIEVE.NOT_ID is not null");
            pstSvRecord.setString(1, empId);
            pstSvRecord.setString(2, empId);
            pstSvRecord.setString(3, empId);
            pstSvRecord.setString(4, empId);
            pstSvRecord.setString(5, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                esh.setIsValidated(resultSet.getString("is_validated"));
                setTabName(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * *****************JOINING*********************
             */
            /*pstSvRecord = con.prepareStatement("SELECT EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,NOT_ID ID,NOTE,MEMO_NO ORDNO,MEMO_DATE ORDDT,'JOINING' TAB_NAME,  NULL DEPT_CODE,NULL OFF_CODE, NULL AUTH ,ENT_DEPT,ENT_OFF,  G_SPC.SPN ENT_AUTH  ,"
             + "'3' TEMPORD,NULL IS_LOCKED,is_validated FROM EMP_JOIN left outer join g_spc on emp_join.ent_auth=g_spc.spc where IF_VISIBLE='Y' and NOT_ID is not null and emp_id=? AND NOT_TYPE!='CHNG_STRUCTURE' AND NOT_TYPE!='ADDITIONAL_CHARGE' ");*/
            pstSvRecord = con.prepareStatement("SELECT joi.is_validated,joi.EMP_ID,joi.DOE,joi.TOE,joi.SB_DESCRIPTION,joi.NOT_ID ID,joi.NOTE,'JOINING' TAB_NAME, G_SPC.SPN ENT_AUTH,joi.MEMO_DATE ORDDT"
                    + " FROM emp_notification"
                    + " left outer join emp_relieve reli on emp_notification.emp_id=reli.emp_id and"
                    + " emp_notification.not_id=reli.not_id and reli.join_id not in (Select join_id from emp_join where EMP_ID=? AND if_ad_charge='Y')"
                    + " left outer join emp_join joi on emp_notification.emp_id=joi.emp_id and emp_notification.not_id=joi.not_id"
                    + " left outer join g_spc on joi.ent_auth=g_spc.spc where joi.IF_VISIBLE='Y' AND joi.emp_id=? AND joi.NOT_TYPE!='CHNG_STRUCTURE' AND joi.NOT_TYPE!='ADDITIONAL_CHARGE' and"
                    + " (emp_notification.not_type='FIRST_APPOINTMENT' or emp_notification.not_type='REHABILITATION' or emp_notification.not_type='ABSORPTION' or emp_notification.not_type='REDEPLOYMENT' or"
                    + " emp_notification.not_type='VALIDATION' or emp_notification.not_type='LT_TRAINING' or emp_notification.not_type='DEPUTATION' or emp_notification.not_type='POSTING' or emp_notification.not_type='TRANSFER'"
                    + " or emp_notification.not_type='PROMOTION' or emp_notification.not_type='ADDITIONAL_CHARGE' or emp_notification.not_type='ALLOT_CADRE' or emp_notification.not_type='REDESIGNATION' or"
                    + " emp_notification.not_type='CHNG_STRUCTURE')");
            pstSvRecord.setString(1, empId);
            pstSvRecord.setString(2, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                esh.setIsValidated(resultSet.getString("is_validated"));
                setTabName(esh);
                setWefDate(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * *****************REINSTATEMENT*********************
             */
//            pstSvRecord = con.prepareStatement("SELECT is_validated,EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,reinstatement_id ID,NOTE,ORDNO,ORDDT,'REINSTATEMENT' TAB_NAME,  AUTH_DEPT DEPT_CODE,AUTH_OFF OFF_CODE,  GETSPN(EMP_REINSTATEMENT.AUTH) AUTH  ,ENT_DEPT,ENT_OFF,  "
//                    + "GETSPN(EMP_REINSTATEMENT.ENT_AUTH) ENT_AUTH  ,NULL TEMPORD,NULL IS_LOCKED FROM EMP_REINSTATEMENT where emp_id=? ");
            pstSvRecord = con.prepareStatement("SELECT EMP_REINSTATEMENT.is_validated,EMP_REINSTATEMENT.EMP_ID,EMP_REINSTATEMENT.DOE,EMP_REINSTATEMENT.TOE,EMP_NOTIFICATION.SB_DESCRIPTION,EMP_REINSTATEMENT.IF_ASSUMED,reinstatement_id ID,EMP_REINSTATEMENT.NOTE,EMP_REINSTATEMENT.ORDNO,EMP_REINSTATEMENT.ORDDT,'REINSTATEMENT' TAB_NAME,  EMP_REINSTATEMENT.AUTH_DEPT DEPT_CODE,EMP_REINSTATEMENT.AUTH_OFF OFF_CODE,  GETSPN(EMP_REINSTATEMENT.AUTH) AUTH ,EMP_REINSTATEMENT.ENT_DEPT,EMP_REINSTATEMENT.ENT_OFF,GETSPN(EMP_REINSTATEMENT.ENT_AUTH) ENT_AUTH  ,NULL TEMPORD,NULL IS_LOCKED FROM EMP_REINSTATEMENT"
                    + " EMP_REINSTATEMENT INNER JOIN EMP_SUSPENSION ON EMP_REINSTATEMENT.REINSTATEMENT_ID=EMP_SUSPENSION.SP_ID"
                    + " INNER JOIN EMP_NOTIFICATION ON EMP_REINSTATEMENT.NOT_ID=EMP_NOTIFICATION.NOT_ID"
                    + " where EMP_REINSTATEMENT.emp_id=?"
                    + " AND EMP_SUSPENSION.emp_id=? AND EMP_NOTIFICATION.IF_VISIBLE='Y' ");
            pstSvRecord.setString(1, empId);
            pstSvRecord.setString(2, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                esh.setIsValidated(resultSet.getString("is_validated"));
                setTabName(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * *****************SUSPENSION*********************
             */
            /*pstSvRecord = con.prepareStatement("SELECT is_validated,EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,SP_ID ID,NOTE,ORDNO,ORDDT,'SUSPENSION' TAB_NAME,  AUTH_DEPT DEPT_CODE,AUTH_OFF OFF_CODE,  GETSPN(EMP_SUSPENSION.AUTH) AUTH   ,ENT_DEPT,ENT_OFF,  "
             + "GETSPN(EMP_SUSPENSION.ENT_AUTH) ENT_AUTH  ,NULL TEMPORD,NULL IS_LOCKED FROM EMP_SUSPENSION where IF_VISIBLE='Y' AND emp_id=? AND  (HQ_FIX IS NULL OR HQ_FIX = '') ");*/
            pstSvRecord = con.prepareStatement("SELECT EMP_SUSPENSION.is_validated,EMP_SUSPENSION.EMP_ID,EMP_SUSPENSION.DOE,EMP_SUSPENSION.TOE,EMP_SUSPENSION.SB_DESCRIPTION,EMP_SUSPENSION.IF_ASSUMED,SP_ID ID,EMP_SUSPENSION.NOTE,EMP_SUSPENSION.ORDNO,EMP_SUSPENSION.ORDDT,'SUSPENSION' TAB_NAME,  EMP_SUSPENSION.AUTH_DEPT DEPT_CODE,EMP_SUSPENSION.AUTH_OFF OFF_CODE,  GETSPN(EMP_SUSPENSION.AUTH) AUTH,EMP_SUSPENSION.ENT_DEPT,EMP_SUSPENSION.ENT_OFF,"
                    + " GETSPN(EMP_SUSPENSION.ENT_AUTH) ENT_AUTH  ,NULL TEMPORD,NULL IS_LOCKED FROM EMP_SUSPENSION"
                    + " INNER JOIN EMP_NOTIFICATION ON EMP_SUSPENSION.NOT_ID=EMP_NOTIFICATION.NOT_ID"
                    + " where EMP_SUSPENSION.IF_VISIBLE='Y' AND EMP_SUSPENSION.emp_id=? AND EMP_NOTIFICATION.emp_id=? AND (HQ_FIX IS NULL OR HQ_FIX = '')");
            pstSvRecord.setString(1, empId);
            pstSvRecord.setString(2, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                esh.setIsValidated(resultSet.getString("is_validated"));
                setTabName(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * *****************HEAD QUARTER FIXATION*********************
             */
            pstSvRecord = con.prepareStatement("SELECT EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,SP_ID ID,NOTE,ORDNO,ORDDT,'HEAD QUARTER FIXATION' TAB_NAME,  AUTH_DEPT DEPT_CODE,AUTH_OFF OFF_CODE,  GETSPN(EMP_SUSPENSION.AUTH) AUTH   ,ENT_DEPT,ENT_OFF,  "
                    + "GETSPN(EMP_SUSPENSION.ENT_AUTH) ENT_AUTH  ,NULL TEMPORD,NULL IS_LOCKED FROM EMP_SUSPENSION where IF_VISIBLE='Y' AND emp_id=? AND HQ_FIX IS NOT NULL ");
            pstSvRecord.setString(1, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                setTabName(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * *****************PERMISSION*********************
             */
            pstSvRecord = con.prepareStatement("SELECT is_validated,EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,PER_ID ID,NOTE,ORD_NO ORDNO,ORD_DATE ORDDT,'PERMISSION' TAB_NAME,  DEPT_CODE,OFF_CODE,  GETSPN(EMP_PERMISSION.AUTH) AUTH   , ENT_DEPT,ENT_OFF,  "
                    + "GETSPN(EMP_PERMISSION.ENT_AUTH) ENT_AUTH  ,NULL TEMPORD,NULL IS_LOCKED FROM EMP_PERMISSION where emp_id=? AND IF_VISIBLE='Y' ");
            pstSvRecord.setString(1, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                esh.setIsValidated(resultSet.getString("is_validated"));
                setTabName(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * *****************EDUCATION*********************
             */
            pstSvRecord = con.prepareStatement("SELECT is_validated,EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,QFN_ID ID,NOTE,'' ORDNO,NULL ORDDT,'EDUCATION' TAB_NAME,  NULL DEPT_CODE,NULL OFF_CODE,NULL AUTH,ENT_DEPT,ENT_OFF,  "
                    + "GETSPN(EMP_QUALIFICATION.ENT_AUTH) ENT_AUTH  ,NULL TEMPORD,NULL IS_LOCKED FROM EMP_QUALIFICATION where emp_id=? AND DOE IS NOT NULL AND (SB_DESCRIPTION IS NOT NULL AND SB_DESCRIPTION<>'')");
            pstSvRecord.setString(1, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                esh.setIsValidated(resultSet.getString("is_validated"));
                setTabName(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * *****************`````````````````````MENT*********************
             */
            pstSvRecord = con.prepareStatement("SELECT is_validated,EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,RET_ID ID,NOTE,ORD_NO ORDNO,ORD_DATE ORDDT,'RETIREMENT' TAB_NAME,  AUTH_DEPT DEPT_CODE,AUTH_OFF OFF_CODE,  GETSPN(EMP_RET_RES.AUTH) AUTH  ,ENT_DEPT,ENT_OFF,  "
                    + "GETSPN(EMP_RET_RES.ENT_AUTH) ENT_AUTH  ,NULL TEMPORD,NULL IS_LOCKED FROM EMP_RET_RES where emp_id=?");
            pstSvRecord.setString(1, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                esh.setIsValidated(resultSet.getString("is_validated"));
                setTabName(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * *****************MISC*********************
             */
            /*pstSvRecord = con.prepareStatement("SELECT is_validated,EMP_ID,DOE,TOE,'' SB_DESCRIPTION,IF_ASSUMED,MISC_ID ID,'' NOTE,'' ORDNO,NULL ORDDT,'MISC' TAB_NAME,  NULL DEPT_CODE,NULL OFF_CODE,NULL AUTH,NULL ENT_DEPT,NULL ENT_OFF,NULL ENT_AUTH,NULL TEMPORD,NULL IS_LOCKED "
             + "FROM EMP_MISC where emp_id=?");
             pstSvRecord.setString(1, empId);
             resultSet = pstSvRecord.executeQuery();
             serviceHistory2 = new ArrayList<>();
             while (resultSet.next()) {
             EmpServiceHistory esh = new EmpServiceHistory();
             esh.setEmpId(empId);
             esh.setNoteId(resultSet.getInt("ID"));
             esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
             esh.setToe(resultSet.getString("TOE"));
             esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
             esh.setTabname(resultSet.getString("TAB_NAME"));
             esh.setModuleNote(resultSet.getString("NOTE"));
             esh.setEntauth(resultSet.getString("ENT_AUTH"));
             esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
             esh.setIsValidated(resultSet.getString("is_validated"));
             setTabName(esh);
             serviceHistory2.add(esh);
             }
             serviceHistory1 = serviceHistory;
             serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);*/
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);
            /**
             * *****************SERVICE RECORD*********************
             */
            pstSvRecord = con.prepareStatement("SELECT EMP_ID,DOE,TOE,'' SB_DESCRIPTION,IF_ASSUMED,SR_ID ID,NOTE,'' ORDNO,NULL ORDDT,'SERVICE RECORD' TAB_NAME,  NULL DEPT_CODE,NULL OFF_CODE,NULL AUTH,ENT_DEPT,ENT_OFF,  GETSPN(EMP_SERVICERECORD.ENT_AUTH) ENT_AUTH, "
                    + "NULL TEMPORD,NULL IS_LOCKED FROM EMP_SERVICERECORD WHERE EMP_ID=?");
            pstSvRecord.setString(1, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                setTabName(esh);
                //serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * *****************SERVICE PAY RECORD*********************
             */
            pstSvRecord = con.prepareStatement("SELECT EMP_ID,DOE,TOE,'' SB_DESCRIPTION,IF_ASSUMED,SRP_ID ID,'' NOTE,'' ORDNO,NULL ORDDT,'SERVICE PAY RECORD' TAB_NAME,  NULL DEPT_CODE,NULL OFF_CODE,NULL AUTH,ENT_DEPT,ENT_OFF,  GETSPN(EMP_SR_PAY.ENT_AUTH) ENT_AUTH,"
                    + "NULL TEMPORD,NULL IS_LOCKED FROM EMP_SR_PAY WHERE EMP_ID=?");
            pstSvRecord.setString(1, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                setTabName(esh);
                //serviceHistory2.add(esh);
            }
            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);
            /**
             * **************REGULARIZATION OF CONTRACTUAL SIX YEAR SERVICE TO
             * REGULAR***********************************
             */

            pstSvRecord = con.prepareStatement("select * from emp_regularization_contractual where emp_id=? and if_visible='Y' ");
            pstSvRecord.setString(1, empId);
            resultSet = pstSvRecord.executeQuery();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setCategory("REGULARIZATION OF CONTRACTUAL SIX YEAR SERVICE TO REGULAR");
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                serviceHistory2.add(esh);

            }
            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * **************ENROLLMENT TO INSURANCE
             * ***********************************
             */
            pstSvRecord = con.prepareStatement("select * from emp_notification where emp_id=? and not_type='ENROLLMENT' and if_visible='Y' ");
            pstSvRecord.setString(1, empId);
            resultSet = pstSvRecord.executeQuery();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setTabname("ENROLLMENT");
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                //serviceHistory2.add(esh);
                setTabName(esh);

            }
            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * **************EQUIVALENT POST
             * ***********************************
             */
            pstSvRecord = con.prepareStatement("select * from emp_notification where emp_id=? and not_type='EQ_STAT' and if_visible='Y' ");
            pstSvRecord.setString(1, empId);
            resultSet = pstSvRecord.executeQuery();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setTabname("EQ_STAT");
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                //serviceHistory2.add(esh);
                setTabName(esh);

            }
            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            Collections.sort(serviceHistory, new Comparator<EmpServiceHistory>() {
                @Override
                public int compare(EmpServiceHistory esp1, EmpServiceHistory esp2) {
                    Date date1 = CommonFunctions.getDateFromString(esp1.getOrdDate(), "dd-MMM-yyyy");
                    Date date2 = CommonFunctions.getDateFromString(esp2.getOrdDate(), "dd-MMM-yyyy");
                    return date1.compareTo(date2);
                }
            });
            eap.setEmpsbrecord(serviceHistory);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return eap;

    }

    @Override
    public int validateServiceBookEntry(String empid, String id, LoginUserBean lub) {

        Connection con = null;

        PreparedStatement pst = null;
        int retVal = 0;

        try {
            con = this.dataSource.getConnection();

            String sql = "UPDATE EMP_NOTIFICATION SET IS_VALIDATED='Y' WHERE EMP_ID=? AND NOT_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setInt(2, Integer.parseInt(id));
            retVal = pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            if (retVal > 0) {
                sql = "INSERT INTO servicebook_validator_log(validated_by_hrmsid,validated_by_offcode,validated_on,not_id,transaction_table) VALUES(?,?,?,?,?)";
                pst = con.prepareStatement(sql);
                pst.setString(1, lub.getLoginempid());
                pst.setString(2, lub.getLoginoffcode());
                pst.setTimestamp(3, new Timestamp(new Date().getTime()));
                pst.setInt(4, Integer.parseInt(id));
                pst.setString(5, "emp_notification");
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
    public int validateNoNotificationServiceBookEntry(String empid, String id, String tabname, LoginUserBean lub) {
        Connection con = null;

        PreparedStatement pst = null;
        int retVal = 0;

        String transactiontable = "";

        try {
            con = this.dataSource.getConnection();

            if (tabname != null && !tabname.equals("")) {
                if (tabname.equals("JOINING")) {

                    String sql = "UPDATE EMP_JOIN SET IS_VALIDATED='Y' WHERE EMP_ID=? AND NOT_ID=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, empid);
                    pst.setInt(2, Integer.parseInt(id));
                    retVal = pst.executeUpdate();

                    transactiontable = "EMP_JOIN";
                } else if (tabname.equals("TRAINING")) {
                    String sql = "UPDATE EMP_TRAIN SET IS_VALIDATED='Y' WHERE EMP_ID=? AND TRAINID=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, empid);
                    pst.setInt(2, Integer.parseInt(id));
                    retVal = pst.executeUpdate();

                    transactiontable = "EMP_TRAIN";
                } else if (tabname.equals("SERVICE VERIFICATION CERTIFICATE")) {
                    String sql = "UPDATE EMP_SV SET IS_VALIDATED='Y' WHERE EMP_ID=? AND SV_ID=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, empid);
                    pst.setInt(2, Integer.parseInt(id));
                    retVal = pst.executeUpdate();

                    transactiontable = "EMP_SV";
                } else if (tabname.equals("RELIEVE")) {
                    String sql = "UPDATE EMP_RELIEVE SET IS_VALIDATED='Y' WHERE EMP_ID=? AND NOT_ID=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, empid);
                    pst.setInt(2, Integer.parseInt(id));
                    retVal = pst.executeUpdate();

                    transactiontable = "EMP_RELIEVE";
                } else if (tabname.equals("EXAMINATION")) {
                    String sql = "UPDATE EMP_EXAM SET IS_VALIDATED='Y' WHERE EMP_ID=? AND EX_ID=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, empid);
                    pst.setInt(2, Integer.parseInt(id));
                    retVal = pst.executeUpdate();

                    transactiontable = "EMP_EXAM";
                } else if (tabname.equals("REINSTATEMENT")) {
                    String sql = "UPDATE EMP_REINSTATEMENT SET IS_VALIDATED='Y' WHERE EMP_ID=? AND reinstatement_id=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, empid);
                    pst.setInt(2, Integer.parseInt(id));
                    retVal = pst.executeUpdate();

                    transactiontable = "EMP_REINSTATEMENT";
                } else if (tabname.equals("SUSPENSION")) {
                    String sql = "UPDATE EMP_SUSPENSION SET IS_VALIDATED='Y' WHERE EMP_ID=? AND SP_ID=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, empid);
                    pst.setInt(2, Integer.parseInt(id));
                    retVal = pst.executeUpdate();

                    transactiontable = "EMP_SUSPENSION";
                } else if (tabname.equals("PERMISSION")) {
                    String sql = "UPDATE EMP_PERMISSION SET IS_VALIDATED='Y' WHERE EMP_ID=? AND PER_ID=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, empid);
                    pst.setInt(2, Integer.parseInt(id));
                    retVal = pst.executeUpdate();

                    transactiontable = "EMP_PERMISSION";
                } else if (tabname.equals("EDUCATION")) {
                    String sql = "UPDATE EMP_QUALIFICATION SET IS_VALIDATED='Y' WHERE EMP_ID=? AND QFN_ID=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, empid);
                    pst.setInt(2, Integer.parseInt(id));
                    retVal = pst.executeUpdate();

                    transactiontable = "EMP_QUALIFICATION";
                } else if (tabname.equals("RETIREMENT")) {
                    String sql = "UPDATE EMP_RET_RES SET IS_VALIDATED='Y' WHERE EMP_ID=? AND RET_ID=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, empid);
                    pst.setInt(2, Integer.parseInt(id));
                    retVal = pst.executeUpdate();

                    transactiontable = "EMP_RET_RES";
                } else if (tabname.equals("MISC")) {
                    String sql = "UPDATE EMP_MISC SET IS_VALIDATED='Y' WHERE EMP_ID=? AND MISC_ID=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, empid);
                    pst.setInt(2, Integer.parseInt(id));
                    retVal = pst.executeUpdate();

                    transactiontable = "EMP_MISC";
                }
            }

            DataBaseFunctions.closeSqlObjects(pst);

            if (retVal > 0) {
                String sql = "INSERT INTO servicebook_validator_log(validated_by_hrmsid,validated_by_offcode,validated_on,not_id,transaction_table) VALUES(?,?,?,?,?)";
                pst = con.prepareStatement(sql);
                pst.setString(1, lub.getLoginempid());
                pst.setString(2, lub.getLoginoffcode());
                pst.setTimestamp(3, new Timestamp(new Date().getTime()));
                pst.setInt(4, Integer.parseInt(id));
                pst.setString(5, transactiontable);
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
    public int inValidateServiceBookEntry(String empid, String id) {
        Connection con = null;

        PreparedStatement pst = null;
        int retVal = 0;

        try {
            con = this.dataSource.getConnection();

            String sql = "UPDATE EMP_NOTIFICATION SET IS_VALIDATED='N' WHERE EMP_ID=? AND NOT_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setInt(2, Integer.parseInt(id));
            retVal = pst.executeUpdate();
            if (retVal != 0) {
                String sql1 = "UPDATE EMP_MAST SET IS_SB_UPDATE_COMPLETED='N' WHERE EMP_ID=? ";
                pst = con.prepareStatement(sql1);
                pst.setString(1, empid);
                pst.executeUpdate();
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
    public int inValidateNoNotificationServiceBookEntry(String empid, String id, String tabname) {
        Connection con = null;

        PreparedStatement pst = null;
        int retValue = 0;

        try {
            con = this.dataSource.getConnection();

            if (tabname != null && !tabname.equals("")) {
                if (tabname.equals("JOINING")) {

                    String sql = "UPDATE EMP_JOIN SET IS_VALIDATED='N' WHERE EMP_ID=? AND NOT_ID=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, empid);
                    pst.setInt(2, Integer.parseInt(id));
                    retValue = pst.executeUpdate();
                } else if (tabname.equals("TRAINING")) {
                    String sql = "UPDATE EMP_TRAIN SET IS_VALIDATED='N' WHERE EMP_ID=? AND TRAINID=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, empid);
                    pst.setInt(2, Integer.parseInt(id));
                    retValue = pst.executeUpdate();
                } else if (tabname.equals("SERVICE VERIFICATION CERTIFICATE")) {
                    String sql = "UPDATE EMP_SV SET IS_VALIDATED='N' WHERE EMP_ID=? AND SV_ID=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, empid);
                    pst.setInt(2, Integer.parseInt(id));
                    retValue = pst.executeUpdate();
                } else if (tabname.equals("RELIEVE")) {
                    String sql = "UPDATE EMP_RELIEVE SET IS_VALIDATED='N' WHERE EMP_ID=? AND NOT_ID=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, empid);
                    pst.setInt(2, Integer.parseInt(id));
                    retValue = pst.executeUpdate();
                } else if (tabname.equals("EXAMINATION")) {
                    String sql = "UPDATE EMP_EXAM SET IS_VALIDATED='N' WHERE EMP_ID=? AND EX_ID=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, empid);
                    pst.setInt(2, Integer.parseInt(id));
                    retValue = pst.executeUpdate();
                } else if (tabname.equals("REINSTATEMENT")) {
                    String sql = "UPDATE EMP_REINSTATEMENT SET IS_VALIDATED='N' WHERE EMP_ID=? AND reinstatement_id=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, empid);
                    pst.setInt(2, Integer.parseInt(id));
                    retValue = pst.executeUpdate();
                } else if (tabname.equals("SUSPENSION")) {
                    String sql = "UPDATE EMP_SUSPENSION SET IS_VALIDATED='N' WHERE EMP_ID=? AND SP_ID=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, empid);
                    pst.setInt(2, Integer.parseInt(id));
                    retValue = pst.executeUpdate();
                } else if (tabname.equals("PERMISSION")) {
                    String sql = "UPDATE EMP_PERMISSION SET IS_VALIDATED='N' WHERE EMP_ID=? AND PER_ID=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, empid);
                    pst.setInt(2, Integer.parseInt(id));
                    retValue = pst.executeUpdate();
                } else if (tabname.equals("EDUCATION")) {
                    String sql = "UPDATE EMP_QUALIFICATION SET IS_VALIDATED='N' WHERE EMP_ID=? AND QFN_ID=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, empid);
                    pst.setInt(2, Integer.parseInt(id));
                    retValue = pst.executeUpdate();
                } else if (tabname.equals("RETIREMENT")) {
                    String sql = "UPDATE EMP_RET_RES SET IS_VALIDATED='N' WHERE EMP_ID=? AND RET_ID=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, empid);
                    pst.setInt(2, Integer.parseInt(id));
                    retValue = pst.executeUpdate();
                } else if (tabname.equals("MISC")) {
                    String sql = "UPDATE EMP_MISC SET IS_VALIDATED='N' WHERE EMP_ID=? AND MISC_ID=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, empid);
                    pst.setInt(2, Integer.parseInt(id));
                    retValue = pst.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return retValue;
    }

    @Override
    public String inValidateSBEntryGroupWise(String checkedSBModule, String empid) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        String tabName = null;
        //ArrayList<String> module=new ArrayList<String>();
        int retValue = 0;
        //int retValue1 = 0;
        String modulename = "";
        String modname = "";
        try {
            con = this.dataSource.getConnection();
            List<String> module = Arrays.asList(checkedSBModule.split(","));
            int sizeofmodule = module.size();
            if (sizeofmodule > 0) {
                for (int i = 0; i < sizeofmodule; i++) {
                    tabName = module.get(i);
                    String[] chkValArr = tabName.split("~");
                    String sql = "UPDATE " + chkValArr[0] + " SET IS_VALIDATED='N' WHERE EMP_ID=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, empid);
                    retValue = pst.executeUpdate();
                    if (retValue > 0) {
                        modulename = modulename + chkValArr[1] + ",";
                        modname = modulename.substring(0, modulename.length() - 1);
                    }
                }
                if (retValue > 0) {
                    String query1 = "UPDATE EMP_MAST SET IS_SB_UPDATE_COMPLETED='N' WHERE EMP_ID=?";
                    pst = con.prepareStatement(query1);
                    pst.setString(1, empid);
                    pst.executeUpdate();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return modname;
    }

    @Override
    public int removeServiceBookEntryType(String empid, String id) {
        //boolean b = false;
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        int retVal = 0;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("UPDATE EMP_NOTIFICATION SET ENTRY_TYPE=NULL WHERE EMP_ID=? AND NOT_ID=?");
            ps.setString(1, empid);
            ps.setInt(2, Integer.parseInt(id));
            retVal = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return retVal;

    }

    @Override
    public EmpServiceBook getSHReportForAG(String empId) {

        Connection con = null;
        PreparedStatement pstSvRecord = null;
        ResultSet resultSet = null;
        List<EmpServiceHistory> serviceHistory = new ArrayList<>();
        EmpServiceBook eap = new EmpServiceBook();
        try {
            con = this.repodataSource.getConnection();
            pstSvRecord = con.prepareStatement("SELECT emp_notification.EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,emp_notification.NOT_ID ID,NOTE,ORDNO,ORDDT,emp_notification.NOT_TYPE TAB_NAME,DEPT_CODE,OFF_CODE, GETSPN(emp_notification.AUTH) AUTH ,ENT_DEPT,ENT_OFF, "
                    + "GETSPN(emp_notification.ENT_AUTH) ENT_AUTH,'1' TEMPORD,IS_LOCKED FROM emp_notification  WHERE emp_notification.emp_id=? AND IF_VISIBLE='Y' AND NOT_TYPE!='CHNG_STRUCTURE' AND NOT_TYPE!='LEAVE' AND NOT_TYPE!='RESULT' "
                    + "AND NOT_TYPE!='COMPLIANCE' AND NOT_TYPE!='SC_NOTICE' AND NOT_TYPE!='INCREMENT' and NOT_TYPE!='LOAN_SANC'  and NOT_TYPE!='SUPERANNUATION' and NOT_TYPE!='REINSTATEMENT' and NOT_TYPE!='SUSPENSION' and (SB_DESCRIPTION is not null and SB_DESCRIPTION<>'')");
            pstSvRecord.setString(1, empId);
            resultSet = pstSvRecord.executeQuery();
            List<EmpServiceHistory> serviceHistory1 = new ArrayList<EmpServiceHistory>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                setTabName(esh);
                setWefDate(esh);
                serviceHistory1.add(esh);
            }

            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            /**
             * **************************************INCREMENT
             */
            pstSvRecord = con.prepareStatement("SELECT emp_notification.EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,emp_notification.NOT_ID ID,NOTE,ORDNO,ORDDT,emp_notification.NOT_TYPE TAB_NAME,DEPT_CODE,OFF_CODE, GETSPN(emp_notification.AUTH) AUTH,ENT_DEPT,ENT_OFF, "
                    + "GETSPN(emp_notification.ENT_AUTH) ENT_AUTH,'1' TEMPORD,IS_LOCKED  FROM (SELECT * FROM EMP_INCR WHERE emp_id=? AND PRID IS NULL) EMP_INCR inner join (select * from emp_notification where NOT_TYPE='INCREMENT' and emp_id=? and (if_visible='Y' or if_visible is null or if_visible='')) "
                    + "emp_notification ON EMP_INCR.NOT_ID = emp_notification.NOT_ID");
            pstSvRecord.setString(1, empId);
            pstSvRecord.setString(2, empId);
            resultSet = pstSvRecord.executeQuery();
            ArrayList<EmpServiceHistory> serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                setTabName(esh);
                setWefDate(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * **************************************LEAVE
             */
            pstSvRecord = con.prepareStatement("SELECT EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,NOT_ID ID,NOTE,ORDNO,ORDDT,NOT_TYPE TAB_NAME,DEPT_CODE,OFF_CODE,  GETSPN(emp_notification.AUTH) AUTH  ,ENT_DEPT,ENT_OFF,  GETSPN(emp_notification.ENT_AUTH) ENT_AUTH, "
                    + "'1' TEMPORD,IS_LOCKED FROM emp_notification where EMP_NOTIFICATION.NOT_ID IN (SELECT NOT_ID FROM EMP_LEAVE WHERE EMP_LEAVE.EMP_ID=? AND LSOT_ID='01') AND EMP_NOTIFICATION.EMP_ID=? AND NOT_TYPE='LEAVE' AND  "
                    + "STATUS = 'SANCTIONED AND AVAILED'");
            pstSvRecord.setString(1, empId);
            pstSvRecord.setString(2, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                setTabName(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * ************************************** LEAVE SANCTIONED
             */
            pstSvRecord = con.prepareStatement("SELECT EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,NOT_ID ID,NOTE,ORDNO,ORDDT,NOT_TYPE TAB_NAME,DEPT_CODE,OFF_CODE,  GETSPN(emp_notification.AUTH) AUTH  ,ENT_DEPT,ENT_OFF,  GETSPN(emp_notification.ENT_AUTH) ENT_AUTH  , "
                    + "	'1' TEMPORD,IS_LOCKED FROM emp_notification where EMP_NOTIFICATION.NOT_ID IN (SELECT NOT_ID FROM EMP_LEAVE WHERE EMP_LEAVE.EMP_ID=? AND LSOT_ID='02') AND EMP_NOTIFICATION.EMP_ID=? AND NOT_TYPE='LEAVE' "
                    + "	AND STATUS = 'SANCTIONED' AND EMP_NOTIFICATION.IF_VISIBLE='Y' ");
            pstSvRecord.setString(1, empId);
            pstSvRecord.setString(2, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                setTabName(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * **************************************
             */
            pstSvRecord = con.prepareStatement("SELECT EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,NOT_ID ID,NOTE,ORDNO,ORDDT,NOT_TYPE TAB_NAME,DEPT_CODE,OFF_CODE,  GETSPN(emp_notification.AUTH) AUTH  ,ENT_DEPT,ENT_OFF,  GETSPN(emp_notification.ENT_AUTH) ENT_AUTH,"
                    + "'1' TEMPORD,NULL IS_LOCKED FROM emp_notification where EMP_NOTIFICATION.NOT_ID IN (SELECT NOT_ID FROM EMP_LEAVE WHERE EMP_LEAVE.EMP_ID=? AND LSOT_ID!='01' AND LSOT_ID!='02') AND "
                    + "EMP_NOTIFICATION.EMP_ID=? AND NOT_TYPE='LEAVE' AND EMP_NOTIFICATION.IF_VISIBLE='Y' ");
            pstSvRecord.setString(1, empId);
            pstSvRecord.setString(2, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                setTabName(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * ************************************** LOAN SANCTION
             */
            pstSvRecord = con.prepareStatement("SELECT emp_notification.EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,emp_notification.NOT_ID ID,NOTE,ORDNO,ORDDT,emp_notification.NOT_TYPE  "
                    + " TAB_NAME,DEPT_CODE,OFF_CODE, GETSPN(emp_notification.AUTH) AUTH ,ENT_DEPT,ENT_OFF, "
                    + " GETSPN(emp_notification.ENT_AUTH) ENT_AUTH,'1' TEMPORD,IS_LOCKED FROM emp_notification  "
                    + " inner join emp_loan_sanc on emp_notification.not_id=emp_loan_sanc.not_id "
                    + " WHERE emp_notification.emp_id=? AND IF_VISIBLE='Y' "
                    + " AND  emp_loan_sanc.NOT_TYPE='LOAN_SANC' "
                    + " and (emp_loan_sanc.loan_tp='BICA' or emp_loan_sanc.loan_tp='CMPA' or emp_loan_sanc.loan_tp='MOPA' or emp_loan_sanc.loan_tp='MCA' or emp_loan_sanc.loan_tp='HBA' or emp_loan_sanc.loan_tp='VE' or emp_loan_sanc.loan_tp='SHBA') and (SB_DESCRIPTION is not null and SB_DESCRIPTION<>'')");
            pstSvRecord.setString(1, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                setTabName(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * ************************************** EXAMINATION
             */
            pstSvRecord = con.prepareStatement("SELECT EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,EX_ID ID,NOTE,NOT_NO ORDNO,NOT_DT ORDDT,'EXAMINATION' TAB_NAME,  DEPT_CODE,AUTH_OFF OFF_CODE,  GETSPN(emp_exam.AUTH) AUTH  ,ENT_DEPT,ENT_OFF,  "
                    + "GETSPN(emp_exam.ENT_AUTH) ENT_AUTH  ,NULL TEMPORD,NULL IS_LOCKED FROM emp_exam where IF_VISIBLE='Y' AND emp_id=?");
            pstSvRecord.setString(1, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                setTabName(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * *****************Training*********************
             */
            pstSvRecord = con.prepareStatement("SELECT EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,TRAINID ID,NOTE,ord_no ORDNO,ord_date ORDDT,'TRAINING' TAB_NAME,  NULL DEPT_CODE,NULL OFF_CODE,"
                    + "NULL AUTH,ENT_DEPT,ENT_OFF, GETSPN(emp_TRAIN.ENT_AUTH) ENT_AUTH  ,NULL TEMPORD,NULL IS_LOCKED FROM emp_TRAIN where IF_VISIBLE='Y' and emp_id=?");
            pstSvRecord.setString(1, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                setTabName(esh);
                serviceHistory2.add(esh);
            }

            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * *****************Service Verification*********************
             */
            pstSvRecord = con.prepareStatement("SELECT EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,SV_ID ID,NOTE,'' ORDNO,tdate ORDDT,'SERVICE VERIFICATION CERTIFICATE' TAB_NAME,  AUTH_DEPT DEPT_CODE,AUTH_OFF OFF_CODE,GETSPN(emp_SV.AUTH) AUTH,AUTH_DEPT ENT_DEPT, "
                    + "AUTH_OFF ENT_OFF,  GETSPN(emp_SV.AUTH) ENT_AUTH  ,NULL TEMPORD,NULL IS_LOCKED FROM emp_SV where emp_id=?");
            pstSvRecord.setString(1, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                setTabName(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * *****************RELIEVE*********************
             */
            /*pstSvRecord = con.prepareStatement("SELECT EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,NOT_ID ID,NOTE,MEMO_NO ORDNO,MEMO_DATE ORDDT,'RELIEVE' TAB_NAME,  NULL DEPT_CODE,NULL OFF_CODE,NULL AUTH,ENT_DEPT,ENT_OFF,  GETSPN(EMP_RELIEVE.ENT_AUTH) ENT_AUTH,"
             + "'2' TEMPORD,NULL IS_LOCKED FROM EMP_RELIEVE where IF_VISIBLE='Y' AND emp_id=? AND NOT_TYPE!='CHNG_STRUCTURE' AND NOT_TYPE!='SUSPENSION' and NOT_ID is not null ");*/
            pstSvRecord = con.prepareStatement("SELECT EMP_RELIEVE.EMP_ID,EMP_RELIEVE.DOE,EMP_RELIEVE.TOE,EMP_RELIEVE.SB_DESCRIPTION,EMP_RELIEVE.IF_ASSUMED,EMP_RELIEVE.NOT_ID ID,EMP_RELIEVE.NOTE,"
                    + " MEMO_NO ORDNO,MEMO_DATE ORDDT,'RELIEVE' TAB_NAME,  NULL DEPT_CODE,NULL OFF_CODE,NULL AUTH,EMP_RELIEVE.ENT_DEPT,EMP_RELIEVE.ENT_OFF,GETSPN(EMP_RELIEVE.ENT_AUTH) ENT_AUTH"
                    + " FROM EMP_NOTIFICATION NOTI"
                    + " LEFT OUTER JOIN (SELECT * FROM EMP_RELIEVE WHERE EMP_ID=? AND (JOIN_ID IS NULL OR (JOIN_ID::TEXT NOT IN"
                    + " (SELECT JOIN_ID::TEXT FROM EMP_JOIN WHERE EMP_ID=? AND IF_AD_CHARGE='Y'))))EMP_RELIEVE ON NOTI.NOT_ID=EMP_RELIEVE.NOT_ID"
                    + " WHERE EMP_RELIEVE.EMP_ID=? and (NOTI.not_type ='REHABILITATION' or"
                    + " NOTI.not_type='ABSORPTION' or NOTI.not_type='REDEPLOYMENT' or NOTI.not_type='VALIDATION' or NOTI.not_type='LT_TRAINING' or"
                    + " NOTI.not_type='DEPUTATION' or NOTI.not_type ='TRANSFER' or NOTI.not_type ='PROMOTION' or NOTI.not_type ='REDESIGNATION' or"
                    + " NOTI.not_type ='RESIGNATION' or NOTI.not_type ='DECEASED' or NOTI.not_type ='RETIREMENT' or NOTI.not_type='ALLOT_CADRE' or (NOTI.not_type='LEAVE'"
                    + " AND NOTI.NOT_ID IN (SELECT NOT_ID FROM EMP_LEAVE WHERE EMP_ID=? AND IF_LONGTERM='Y')))"
                    + " and EMP_RELIEVE.IF_VISIBLE='Y' AND EMP_RELIEVE.emp_id=? AND EMP_RELIEVE.NOT_TYPE!='CHNG_STRUCTURE' AND EMP_RELIEVE.NOT_TYPE!='SUSPENSION' and EMP_RELIEVE.NOT_ID is not null");
            pstSvRecord.setString(1, empId);
            pstSvRecord.setString(2, empId);
            pstSvRecord.setString(3, empId);
            pstSvRecord.setString(4, empId);
            pstSvRecord.setString(5, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                setTabName(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * *****************JOINING*********************
             */
            /*pstSvRecord = con.prepareStatement("SELECT EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,NOT_ID ID,NOTE,MEMO_NO ORDNO,MEMO_DATE ORDDT,'JOINING' TAB_NAME,  NULL DEPT_CODE,NULL OFF_CODE, NULL AUTH ,ENT_DEPT,ENT_OFF,  G_SPC.SPN ENT_AUTH  ,"
             + "'3' TEMPORD,NULL IS_LOCKED FROM EMP_JOIN left outer join g_spc on emp_join.ent_auth=g_spc.spc where IF_VISIBLE='Y'  AND emp_id=? AND NOT_TYPE!='CHNG_STRUCTURE' AND NOT_TYPE!='ADDITIONAL_CHARGE' and not_id is not null ");*/
            pstSvRecord = con.prepareStatement("SELECT joi.EMP_ID,joi.DOE,joi.TOE,joi.SB_DESCRIPTION,joi.NOT_ID ID,joi.NOTE,'JOINING' TAB_NAME, G_SPC.SPN ENT_AUTH,joi.MEMO_DATE ORDDT"
                    + " FROM emp_notification"
                    + " left outer join emp_relieve reli on emp_notification.emp_id=reli.emp_id and"
                    + " emp_notification.not_id=reli.not_id and reli.join_id not in (Select join_id from emp_join where EMP_ID=? AND if_ad_charge='Y')"
                    + " left outer join emp_join joi on emp_notification.emp_id=joi.emp_id and emp_notification.not_id=joi.not_id"
                    + " left outer join g_spc on joi.ent_auth=g_spc.spc where joi.IF_VISIBLE='Y' AND joi.emp_id=? AND joi.NOT_TYPE!='CHNG_STRUCTURE' AND joi.NOT_TYPE!='ADDITIONAL_CHARGE' and"
                    + " (emp_notification.not_type='FIRST_APPOINTMENT' or emp_notification.not_type='REHABILITATION' or emp_notification.not_type='ABSORPTION' or emp_notification.not_type='REDEPLOYMENT' or"
                    + " emp_notification.not_type='VALIDATION' or emp_notification.not_type='LT_TRAINING' or emp_notification.not_type='DEPUTATION' or emp_notification.not_type='POSTING' or emp_notification.not_type='TRANSFER'"
                    + " or emp_notification.not_type='PROMOTION' or emp_notification.not_type='ADDITIONAL_CHARGE' or emp_notification.not_type='ALLOT_CADRE' or emp_notification.not_type='REDESIGNATION' or"
                    + " emp_notification.not_type='CHNG_STRUCTURE')");
            pstSvRecord.setString(1, empId);
            pstSvRecord.setString(2, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                setTabName(esh);
                setWefDate(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * *****************REINSTATEMENT*********************
             */
            /*pstSvRecord = con.prepareStatement("SELECT EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,reinstatement_id ID,NOTE,ORDNO,ORDDT,'REINSTATEMENT' TAB_NAME,  AUTH_DEPT DEPT_CODE,AUTH_OFF OFF_CODE,  GETSPN(EMP_REINSTATEMENT.AUTH) AUTH  ,ENT_DEPT,ENT_OFF,  "
             + "GETSPN(EMP_REINSTATEMENT.ENT_AUTH) ENT_AUTH  ,NULL TEMPORD,NULL IS_LOCKED FROM EMP_REINSTATEMENT where emp_id=? ");*/
            pstSvRecord = con.prepareStatement("SELECT EMP_REINSTATEMENT.EMP_ID,EMP_REINSTATEMENT.DOE,EMP_REINSTATEMENT.TOE,EMP_NOTIFICATION.SB_DESCRIPTION,EMP_REINSTATEMENT.IF_ASSUMED,reinstatement_id ID,EMP_REINSTATEMENT.NOTE,EMP_REINSTATEMENT.ORDNO,EMP_REINSTATEMENT.ORDDT,'REINSTATEMENT' TAB_NAME,  EMP_REINSTATEMENT.AUTH_DEPT DEPT_CODE,EMP_REINSTATEMENT.AUTH_OFF OFF_CODE,  GETSPN(EMP_REINSTATEMENT.AUTH) AUTH ,EMP_REINSTATEMENT.ENT_DEPT,EMP_REINSTATEMENT.ENT_OFF,GETSPN(EMP_REINSTATEMENT.ENT_AUTH) ENT_AUTH  ,NULL TEMPORD,NULL IS_LOCKED FROM EMP_REINSTATEMENT"
                    + " EMP_REINSTATEMENT INNER JOIN EMP_SUSPENSION ON EMP_REINSTATEMENT.REINSTATEMENT_ID=EMP_SUSPENSION.SP_ID"
                    + " INNER JOIN EMP_NOTIFICATION ON EMP_REINSTATEMENT.NOT_ID=EMP_NOTIFICATION.NOT_ID"
                    + " where EMP_REINSTATEMENT.emp_id=?"
                    + " AND EMP_SUSPENSION.emp_id=? and EMP_NOTIFICATION.IF_VISIBLE='Y' ");
            pstSvRecord.setString(1, empId);
            pstSvRecord.setString(2, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                setTabName(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * *****************SUSPENSION*********************
             */
            /*pstSvRecord = con.prepareStatement("SELECT EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,SP_ID ID,NOTE,ORDNO,ORDDT,'SUSPENSION' TAB_NAME,  AUTH_DEPT DEPT_CODE,AUTH_OFF OFF_CODE,  GETSPN(EMP_SUSPENSION.AUTH) AUTH   ,ENT_DEPT,ENT_OFF,  "
             + "GETSPN(EMP_SUSPENSION.ENT_AUTH) ENT_AUTH  ,NULL TEMPORD,NULL IS_LOCKED FROM EMP_SUSPENSION where IF_VISIBLE='Y' AND emp_id=? AND  (HQ_FIX IS NULL OR HQ_FIX = '') ");*/
            pstSvRecord = con.prepareStatement("SELECT EMP_SUSPENSION.EMP_ID,EMP_SUSPENSION.DOE,EMP_SUSPENSION.TOE,EMP_SUSPENSION.SB_DESCRIPTION,EMP_SUSPENSION.IF_ASSUMED,SP_ID ID,EMP_SUSPENSION.NOTE,EMP_SUSPENSION.ORDNO,EMP_SUSPENSION.ORDDT,'SUSPENSION' TAB_NAME,  EMP_SUSPENSION.AUTH_DEPT DEPT_CODE,EMP_SUSPENSION.AUTH_OFF OFF_CODE,  GETSPN(EMP_SUSPENSION.AUTH) AUTH,EMP_SUSPENSION.ENT_DEPT,EMP_SUSPENSION.ENT_OFF,"
                    + " GETSPN(EMP_SUSPENSION.ENT_AUTH) ENT_AUTH  ,NULL TEMPORD,NULL IS_LOCKED FROM EMP_SUSPENSION"
                    + " INNER JOIN EMP_NOTIFICATION ON EMP_SUSPENSION.NOT_ID=EMP_NOTIFICATION.NOT_ID"
                    + " where EMP_SUSPENSION.IF_VISIBLE='Y' AND EMP_SUSPENSION.emp_id=? AND EMP_NOTIFICATION.emp_id=? AND (HQ_FIX IS NULL OR HQ_FIX = '')");
            pstSvRecord.setString(1, empId);
            pstSvRecord.setString(2, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                setTabName(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * *****************HEAD QUARTER FIXATION*********************
             */
            pstSvRecord = con.prepareStatement("SELECT EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,SP_ID ID,NOTE,ORDNO,ORDDT,'HEAD QUARTER FIXATION' TAB_NAME,  AUTH_DEPT DEPT_CODE,AUTH_OFF OFF_CODE,  GETSPN(EMP_SUSPENSION.AUTH) AUTH   ,ENT_DEPT,ENT_OFF,  "
                    + "GETSPN(EMP_SUSPENSION.ENT_AUTH) ENT_AUTH  ,NULL TEMPORD,NULL IS_LOCKED FROM EMP_SUSPENSION where IF_VISIBLE='Y' AND emp_id=? AND HQ_FIX IS NOT NULL ");
            pstSvRecord.setString(1, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                setTabName(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * *****************PERMISSION*********************
             */
            pstSvRecord = con.prepareStatement("SELECT EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,PER_ID ID,NOTE,ORD_NO ORDNO,ORD_DATE ORDDT,'PERMISSION' TAB_NAME,  DEPT_CODE,OFF_CODE,  GETSPN(EMP_PERMISSION.AUTH) AUTH   , ENT_DEPT,ENT_OFF,  "
                    + "GETSPN(EMP_PERMISSION.ENT_AUTH) ENT_AUTH  ,NULL TEMPORD,NULL IS_LOCKED FROM EMP_PERMISSION where emp_id=? AND IF_VISIBLE='Y' ");
            pstSvRecord.setString(1, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                setTabName(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * *****************EDUCATION*********************
             */
            pstSvRecord = con.prepareStatement("SELECT EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,QFN_ID ID,NOTE,'' ORDNO,NULL ORDDT,'EDUCATION' TAB_NAME,  NULL DEPT_CODE,NULL OFF_CODE,NULL AUTH,ENT_DEPT,ENT_OFF,  "
                    + "GETSPN(EMP_QUALIFICATION.ENT_AUTH) ENT_AUTH  ,NULL TEMPORD,NULL IS_LOCKED  FROM EMP_QUALIFICATION where emp_id=? AND DOE IS NOT NULL AND (SB_DESCRIPTION IS NOT NULL AND SB_DESCRIPTION<>'')");
            pstSvRecord.setString(1, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                setTabName(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * *****************`````````````````````MENT*********************
             */
            pstSvRecord = con.prepareStatement("SELECT EMP_ID,DOE,TOE,SB_DESCRIPTION,IF_ASSUMED,RET_ID ID,NOTE,ORD_NO ORDNO,ORD_DATE ORDDT,'RETIREMENT' TAB_NAME,  AUTH_DEPT DEPT_CODE,AUTH_OFF OFF_CODE,  GETSPN(EMP_RET_RES.AUTH) AUTH  ,ENT_DEPT,ENT_OFF,  "
                    + "GETSPN(EMP_RET_RES.ENT_AUTH) ENT_AUTH  ,NULL TEMPORD,NULL IS_LOCKED  FROM EMP_RET_RES where emp_id=?");
            pstSvRecord.setString(1, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                setTabName(esh);
                serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * *****************MISC*********************
             */
            /*pstSvRecord = con.prepareStatement("SELECT EMP_ID,DOE,TOE,'' SB_DESCRIPTION,IF_ASSUMED,MISC_ID ID,'' NOTE,'' ORDNO,NULL ORDDT,'MISC' TAB_NAME,  NULL DEPT_CODE,NULL OFF_CODE,NULL AUTH,NULL ENT_DEPT,NULL ENT_OFF,NULL ENT_AUTH,NULL TEMPORD,NULL IS_LOCKED "
             + "FROM EMP_MISC where emp_id=?");
             pstSvRecord.setString(1, empId);
             resultSet = pstSvRecord.executeQuery();
             serviceHistory2 = new ArrayList<>();
             while (resultSet.next()) {
             EmpServiceHistory esh = new EmpServiceHistory();
             esh.setEmpId(empId);
             esh.setNoteId(resultSet.getInt("ID"));
             esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
             esh.setToe(resultSet.getString("TOE"));
             esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
             esh.setTabname(resultSet.getString("TAB_NAME"));
             esh.setModuleNote(resultSet.getString("NOTE"));
             esh.setEntauth(resultSet.getString("ENT_AUTH"));
             esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
             setTabName(esh);
             serviceHistory2.add(esh);
             }
             serviceHistory1 = serviceHistory;
             serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);*/
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);
            /**
             * *****************SERVICE RECORD*********************
             */
            pstSvRecord = con.prepareStatement("SELECT EMP_ID,DOE,TOE,'' SB_DESCRIPTION,IF_ASSUMED,SR_ID ID,NOTE,'' ORDNO,NULL ORDDT,'SERVICE RECORD' TAB_NAME,  NULL DEPT_CODE,NULL OFF_CODE,NULL AUTH,ENT_DEPT,ENT_OFF,  GETSPN(EMP_SERVICERECORD.ENT_AUTH) ENT_AUTH, "
                    + "NULL TEMPORD,NULL IS_LOCKED FROM EMP_SERVICERECORD WHERE EMP_ID=?");
            pstSvRecord.setString(1, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                setTabName(esh);
                //serviceHistory2.add(esh);
            }
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);

            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * *****************SERVICE PAY RECORD*********************
             */
            pstSvRecord = con.prepareStatement("SELECT EMP_ID,DOE,TOE,'' SB_DESCRIPTION,IF_ASSUMED,SRP_ID ID,'' NOTE,'' ORDNO,NULL ORDDT,'SERVICE PAY RECORD' TAB_NAME,  NULL DEPT_CODE,NULL OFF_CODE,NULL AUTH,ENT_DEPT,ENT_OFF,  GETSPN(EMP_SR_PAY.ENT_AUTH) ENT_AUTH,"
                    + "NULL TEMPORD,NULL IS_LOCKED FROM EMP_SR_PAY WHERE EMP_ID=?");
            pstSvRecord.setString(1, empId);
            resultSet = pstSvRecord.executeQuery();
            serviceHistory2 = new ArrayList<>();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setNoteId(resultSet.getInt("ID"));
                esh.setDoe(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOE")));
                esh.setToe(resultSet.getString("TOE"));
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setTabname(resultSet.getString("TAB_NAME"));
                esh.setModuleNote(resultSet.getString("NOTE"));
                esh.setEntauth(resultSet.getString("ENT_AUTH"));
                esh.setOrdDate(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("ORDDT")));
                setTabName(esh);
                //serviceHistory2.add(esh);
            }
            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * **************REGULARIZATION OF CONTRACTUAL SIX YEAR SERVICE TO
             * REGULAR***********************************
             */
            pstSvRecord = con.prepareStatement("select * from emp_regularization_contractual where emp_id=? and if_visible='Y' ");
            pstSvRecord.setString(1, empId);
            resultSet = pstSvRecord.executeQuery();
            while (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setCategory("REGULARIZATION OF CONTRACTUAL SIX YEAR SERVICE TO REGULAR");
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                serviceHistory2.add(esh);
            }
            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            /**
             * **************SUPERANNUATION**********************
             */
            pstSvRecord = con.prepareStatement("SELECT DOS,getpostnamefromspc(auth) retdFromdesignation,* FROM \n"
                    + "EMP_NOTIFICATION INNER JOIN EMP_MAST ON EMP_NOTIFICATION.EMP_ID=EMP_MAST.EMP_ID\n"
                    + "WHERE NOT_TYPE='SUPERANNUATION' AND IF_VISIBLE='Y' AND EMP_NOTIFICATION.EMP_ID=?");
            pstSvRecord.setString(1, empId);
            resultSet = pstSvRecord.executeQuery();
            if (resultSet.next()) {
                EmpServiceHistory esh = new EmpServiceHistory();
                esh.setEmpId(empId);
                esh.setCategory("SUPERANNUATION");
                esh.setSbdescription(resultSet.getString("SB_DESCRIPTION"));
                esh.setSpn(resultSet.getString("retdFromdesignation"));
                esh.setPay(resultSet.getString("ret_pay"));
                esh.setWefChange(CommonFunctions.getFormattedOutputDate1(resultSet.getDate("DOS")));
                serviceHistory2.add(esh);
            }
            serviceHistory1 = serviceHistory;
            serviceHistory = ListUtils.sum(serviceHistory1, serviceHistory2);

            Collections.sort(serviceHistory, new Comparator<EmpServiceHistory>() {
                @Override
                public int compare(EmpServiceHistory esp1, EmpServiceHistory esp2) {
                    Date date1 = CommonFunctions.getDateFromString(esp1.getWefChange(), "dd-MMM-yyyy");
                    Date date2 = CommonFunctions.getDateFromString(esp2.getWefChange(), "dd-MMM-yyyy");
                    return date1.compareTo(date2);
                }
            });
            eap.setEmpsbrecord(serviceHistory);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(resultSet, pstSvRecord);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return eap;

    }
    @Override
    public List getFinancialDetailsById(String empId) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        EmpServiceHistory empServiceHistory = null;
        List financialList = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            String sql = "SELECT emp_incr_parrecord.wef,nottype,pay,gp,FIRST_INCR,SECOND_INCR,THIRD_INCR, FOURTH_INCR,INCR_TYPE  from\n"
                    + "(select A.* from\n"
                    + "(SELECT E1.not_type nottype,E1.not_id linkid,E1.is_validated,E1.modified_by,E1.modified_on,E1.IF_VISIBLE,\n"
                    + "E1.SV_ID,E1.NOT_ID,E1.DOE, E1.ORDNO,\n"
                    + "E1.ORDDT,E1.AUTH,E1.OFF_CODE,E1.DEPT_CODE,WEF,WEFT,pay_scale,s_pay, p_pay,oth_pay,oth_desc,pay,gp, pay_level, pay_cell,\n"
                    + "INCRID::text,INCR::text,FIRST_INCR::text,SECOND_INCR::text,THIRD_INCR::text,FOURTH_INCR::text,INCR_TYPE FROM EMP_INCR\n"
                    + "LEFT OUTER JOIN EMP_NOTIFICATION E1 ON EMP_INCR.NOT_ID=E1.NOT_ID\n"
                    + "LEFT OUTER JOIN EMP_PAY_RECORD ON EMP_PAY_RECORD.NOT_ID=E1.NOT_ID\n"
                    + "WHERE EMP_PAY_RECORD.NOT_TYPE ='INCREMENT'\n"
                    + "AND EMP_PAY_RECORD.EMP_ID=? \n"
                    + "AND PRID IS NULL ORDER BY WEF DESC)A\n"
                    + "union\n"
                    + "select B.* from\n"
                    + "(SELECT E1.not_type nottype,E1.not_id linkid,E1.is_validated,E1.modified_by,E1.modified_on,E1.IF_VISIBLE,\n"
                    + "E1.SV_ID,E1.NOT_ID,E1.DOE, E1.ORDNO,\n"
                    + "E1.ORDDT,E1.AUTH,E1.OFF_CODE,E1.DEPT_CODE,WEF,WEFT,pay_scale,s_pay, p_pay,oth_pay,oth_desc,pay,gp, pay_level, pay_cell,\n"
                    + "'' INCRID,'' INCR,'' FIRST_INCR,'' SECOND_INCR,'' THIRD_INCR,'' FOURTH_INCR,'' INCR_TYPE FROM EMP_PAY_RECORD\n"
                    + "LEFT OUTER JOIN EMP_NOTIFICATION E1 ON EMP_PAY_RECORD.NOT_ID=E1.NOT_ID\n"
                    + "WHERE EMP_PAY_RECORD.NOT_TYPE in ('PROMOTION','PAYREVISION','FIRST_APPOINTMENT','PAYFIXATION') \n"
                    + "AND EMP_PAY_RECORD.EMP_ID=? ORDER BY WEF DESC)B) emp_incr_parrecord where nottype is not null \n"
                    + "order by wef";

            pst = con.prepareStatement(sql);
            pst.setString(1, empId);
            pst.setString(2, empId);
            rs = pst.executeQuery();
            while (rs.next()) {
                empServiceHistory = new EmpServiceHistory();
                String payString = rs.getString("pay");
                String gpString = rs.getString("gp");
                double pay = (payString != null) ? Double.parseDouble(payString) : 0.0;
                double gp = (gpString != null) ? Double.parseDouble(gpString) : 0.0;
                int roundedPay = (int) Math.round(pay);
                int roundedGp = (int) Math.round(gp);
                empServiceHistory.setOrdDate(CommonFunctions.getFormattedOutputDate1(rs.getTimestamp("wef")));
                empServiceHistory.setPay(String.valueOf(roundedPay));
                empServiceHistory.setGp(String.valueOf(roundedGp));
                empServiceHistory.setEntryType(rs.getString("nottype"));
                empServiceHistory.setIncrementType(rs.getString("INCR_TYPE"));

                financialList.add(empServiceHistory);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return financialList;
    }
}
