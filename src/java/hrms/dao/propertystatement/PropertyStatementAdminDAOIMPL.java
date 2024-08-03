/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.propertystatement;

import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.propertystatement.PropertyAdminSearchCriteria;
import hrms.model.propertystatement.PropertySearchResult;
import hrms.model.propertystatement.PropertyStatementAdminBean;
import hrms.model.propertystatement.PropertyStatementStatusBean;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Manisha
 */
public class PropertyStatementAdminDAOIMPL implements PropertyStatementAdminDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    @Resource(name = "repodataSource")
    protected DataSource repodataSource;

    public void setRepodataSource(DataSource repodataSource) {
        this.repodataSource = repodataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    private String uploadPath;

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    @Override
    public String unlockPropertyStatement(String loginempid, PropertyStatementAdminBean propertyStatementAdminBean) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String startTime = "";
        String msg = "Error Occured";
        try {
            String isPropertyActive = null;
            String spc = null;

            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT property_active,cur_spc FROM PROPERTY_STATEMENT_LIST  "
                    + "INNER JOIN emp_mast ON PROPERTY_STATEMENT_LIST.emp_id = emp_mast.emp_id "
                    + "INNER JOIN financial_year ON PROPERTY_STATEMENT_LIST.FINANCIAL_YEAR = financial_year.CY "
                    + "WHERE yearly_property_id = ?");
            pst.setInt(1, Integer.parseInt(propertyStatementAdminBean.getYearlyPropertyId()));
            rs = pst.executeQuery();
            if (rs.next()) {
                isPropertyActive = rs.getString("property_active");
                spc = rs.getString("cur_spc");
            }
            if (isPropertyActive != null && isPropertyActive.equals("Y")) {
                pst = con.prepareStatement("UPDATE PROPERTY_STATEMENT_LIST SET STATUS_ID=? WHERE yearly_property_id=?");
                pst.setInt(1, 0);
                pst.setInt(2, Integer.parseInt(propertyStatementAdminBean.getYearlyPropertyId()));
                pst.executeUpdate();
                msg = "Property Statement is Unlocked";

                Calendar cal = Calendar.getInstance();
                DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
                startTime = dateFormat.format(cal.getTime());

                pst = con.prepareStatement("INSERT INTO WORKFLOW_LOG(REF_ID,ACTION_TAKEN_BY,FORWARDED_SPC,FORWARD_TO,NOTE,SPC_ONTIME,TASK_ACTION_DATE,TASK_ID,TASK_STATUS_ID,WORKFLOW_TYPE,AUTHORITY_TYPE) VALUES(?,?,?,?,?,?,?,?,?,?,?)");
                pst.setInt(1, Integer.parseInt(propertyStatementAdminBean.getYearlyPropertyId()));
                pst.setString(2, loginempid);
                pst.setString(3, "");
                pst.setString(4, "");
                pst.setString(5, "");
                pst.setString(6, "");
                pst.setTimestamp(7, new Timestamp(dateFormat.parse(startTime).getTime()));
                pst.setInt(8, 0);
                pst.setInt(9, 0);
                pst.setString(10, "PROPERTY_UNLOCK");
                pst.setString(11, "PAR ADMIN");
                pst.executeUpdate();
            } else {
                msg = "Financial Year is Closed";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return msg;
    }

    /*@Override
     public PropertySearchResult getPropertyStatement(String fiscalyear, String searchCriteria, String searchString, int noofrows, int page) {
     List propertyList = new ArrayList();
     Connection con = null;
     PreparedStatement pstmt = null;
     PreparedStatement cntPstmt = null;
     ResultSet rs = null;
     PropertySearchResult propertySearchResult = new PropertySearchResult();
     try {
     con = dataSource.getConnection();
     int minlimit = noofrows * (page - 1);
     int maxlimit = noofrows;
     int total = 0;

     DataBaseFunctions.closeSqlObjects(pstmt);
     System.out.println("11111111111111111111");
     String cntsql = "SELECT count(*) as cnt FROM PROPERTY_STATEMENT_LIST"
     + " INNER JOIN EMP_MAST ON PROPERTY_STATEMENT_LIST.EMP_ID=EMP_MAST.EMP_ID"
     + " LEFT OUTER JOIN G_SPC ON PROPERTY_STATEMENT_LIST.SPC=G_SPC.SPC"
     + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE"
     + " LEFT OUTER JOIN G_CADRE ON PROPERTY_STATEMENT_LIST.CADRE_CODE=G_CADRE.CADRE_CODE "
     + " LEFT OUTER JOIN G_OFFICE ON EMP_MAST.CUR_OFF_CODE=G_OFFICE.OFF_CODE "
     + " WHERE PROPERTY_STATEMENT_LIST.FINANCIAL_YEAR=?";

     String sql = "SELECT IS_CLOSED,ARRAY_TO_STRING(ARRAY[INITIALS,F_NAME,M_NAME,L_NAME],' ') FULLNAME,PROPERTY_STATEMENT_LIST.EMP_ID,GPF_NO,DOB,OFF_EN,MOBILE,FINANCIAL_YEAR,STATUS_ID,FROM_DATE,TO_DATE,SUBMISSION_TYPE,G_CADRE.CADRE_NAME,POST_GROUP,POST,SUBMITTED_ON,yearly_property_id FROM PROPERTY_STATEMENT_LIST"
     + " INNER JOIN EMP_MAST ON PROPERTY_STATEMENT_LIST.EMP_ID=EMP_MAST.EMP_ID"
     + " LEFT OUTER JOIN G_SPC ON PROPERTY_STATEMENT_LIST.SPC=G_SPC.SPC"
     + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE"
     + " LEFT OUTER JOIN G_CADRE ON PROPERTY_STATEMENT_LIST.CADRE_CODE=G_CADRE.CADRE_CODE "
     + " LEFT OUTER JOIN G_OFFICE ON EMP_MAST.CUR_OFF_CODE=G_OFFICE.OFF_CODE "
     + " LEFT OUTER JOIN FINANCIAL_YEAR ON property_statement_list.financial_year=FINANCIAL_YEAR.CY "
     + " WHERE PROPERTY_STATEMENT_LIST.FINANCIAL_YEAR=?";

     String searchSQL = "";
     if (searchCriteria != null && !searchCriteria.equals("")) {
     if (searchCriteria.equalsIgnoreCase("empid")) {
     searchSQL = " AND EMP_MAST.EMP_ID=?";
     System.out.println("searchSQL is ______" +searchSQL);    
     } else if (searchCriteria.equalsIgnoreCase("gpfno")) {
     searchSQL = " AND GPF_NO=?";
     System.out.println("searchSQL gpfno is ______" +searchSQL);
     } else if (searchCriteria.equalsIgnoreCase("empname")) {
     searchSQL = " AND F_NAME LIKE ? OR M_NAME LIKE ? OR L_NAME LIKE ? OR ARRAY_TO_STRING(ARRAY[INITIALS,F_NAME,M_NAME,L_NAME],' ') LIKE ?";
     } else if (searchCriteria.equalsIgnoreCase("mobile")) {
     searchSQL = " AND MOBILE=?";
     System.out.println("searchSQL mobile is _________" +searchSQL);
     } else if (searchCriteria.equalsIgnoreCase("dob")) {
     searchSQL = " AND dob= to_date(?,'DD-Mon-yyyy')";
     System.out.println("searchSQL dob is __________" +searchSQL);
     }
     }
     if (searchSQL != null && !searchSQL.equals("")) {
     sql = sql + searchSQL;
     cntsql = cntsql + searchSQL;
     }
     sql = sql + " ORDER BY TRIM(F_NAME) ASC LIMIT ? OFFSET ?";
     System.out.println("sql is _________" +sql);
     pstmt = con.prepareStatement(sql);
     pstmt.setString(1, fiscalyear);
     cntPstmt = con.prepareStatement(cntsql);
     cntPstmt.setString(1, fiscalyear);
     if (searchSQL != null && !searchSQL.equals("")) {
     if (searchCriteria.equalsIgnoreCase("empid")) {
     pstmt.setString(2, searchString);
     pstmt.setInt(3, maxlimit);
     pstmt.setInt(4, minlimit);
     cntPstmt.setString(2, searchString);
     } else if (searchCriteria.equalsIgnoreCase("gpfno")) {
     pstmt.setString(2, searchString);
     pstmt.setInt(3, maxlimit);
     pstmt.setInt(4, minlimit);
     cntPstmt.setString(2, searchString);
     } else if (searchCriteria.equalsIgnoreCase("empname")) {
     pstmt.setString(2, "%" + searchString.toUpperCase() + "%");
     pstmt.setString(3, "%" + searchString.toUpperCase() + "%");
     pstmt.setString(4, "%" + searchString.toUpperCase() + "%");
     pstmt.setString(5, "%" + searchString.toUpperCase() + "%");
     pstmt.setInt(6, maxlimit);
     pstmt.setInt(7, minlimit);
     cntPstmt.setString(2, "%" + searchString.toUpperCase() + "%");
     cntPstmt.setString(3, "%" + searchString.toUpperCase() + "%");
     cntPstmt.setString(4, "%" + searchString.toUpperCase() + "%");
     cntPstmt.setString(5, "%" + searchString.toUpperCase() + "%");
     } else if (searchCriteria.equalsIgnoreCase("mobile")) {
     pstmt.setString(2, searchString);
     pstmt.setInt(3, maxlimit);
     pstmt.setInt(4, minlimit);
     cntPstmt.setString(2, searchString);
     } else if (searchCriteria.equalsIgnoreCase("dob")) {
     pstmt.setString(2, searchString);
     pstmt.setInt(3, maxlimit);
     pstmt.setInt(4, minlimit);
     cntPstmt.setString(2, searchString);
     }
     } else {
     pstmt.setInt(2, maxlimit);
     pstmt.setInt(3, minlimit);
     }
     rs = pstmt.executeQuery();
     int count = 0;
     while (rs.next()) {
     count++;
     PropertyStatementAdminBean propertyadmin = new PropertyStatementAdminBean();
     propertyadmin.setRow(((page - 1) * 20 + count));
     propertyadmin.setYearlyPropertyId(rs.getString("yearly_property_id"));
     propertyadmin.setIsClosed(rs.getString("IS_CLOSED"));
     propertyadmin.setEmpname(rs.getString("FULLNAME"));
     propertyadmin.setEmpid(rs.getString("EMP_ID"));
     System.out.println("rs.getString(\"EMP_ID\") _______" +rs.getString("EMP_ID"));
     propertyadmin.setGpfno(rs.getString("GPF_NO"));
     propertyadmin.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOB")));
     propertyadmin.setCurrentoffice(rs.getString("OFF_EN"));
     propertyadmin.setCadreName(StringUtils.defaultString(rs.getString("CADRE_NAME")));
     propertyadmin.setPostgroup(StringUtils.defaultString(rs.getString("POST_GROUP")));
     propertyadmin.setPost(StringUtils.defaultString(rs.getString("POST")));
     propertyadmin.setMobile(StringUtils.defaultString(rs.getString("MOBILE")));
     propertyadmin.setStatusId(rs.getInt("STATUS_ID"));
     if (rs.getInt("STATUS_ID") == 0) {
     propertyadmin.setStatus("NOT SUBMITTED");
     } else if (rs.getInt("STATUS_ID") == 1) {
     propertyadmin.setStatus("SUBMITTED");
     } else {
     propertyadmin.setStatus("NOT INITIATED");
     } 
     propertyadmin.setFromdt(CommonFunctions.getFormattedOutputDate1(rs.getDate("FROM_DATE")));
     propertyadmin.setTodt(CommonFunctions.getFormattedOutputDate1(rs.getDate("TO_DATE")));
     if (rs.getString("SUBMISSION_TYPE") != null && !rs.getString("SUBMISSION_TYPE").equals("")) {
     if (rs.getString("SUBMISSION_TYPE").equals("DS")) {
     propertyadmin.setSubmissionType("DOCUMENT SUBMISSION");
     } else if (rs.getString("SUBMISSION_TYPE").equals("FS")) {
     propertyadmin.setSubmissionType("FORM SUBMISSION");
     }
     } else {
     propertyadmin.setSubmissionType("");
     }
     propertyadmin.setSubmittedon(CommonFunctions.getFormattedOutputDate1(rs.getDate("SUBMITTED_ON")));
     propertyList.add(propertyadmin);
     }
     propertySearchResult.setPropertyList(propertyList);

     /*Count noof rows*/
    /*rs = cntPstmt.executeQuery();
     if (rs.next()) {
     propertySearchResult.setTotalPropertyFound(rs.getInt("cnt"));
     }*/
    /*Count noof rows*/
    /*} catch (Exception e) {
     e.printStackTrace();
     } finally {
     DataBaseFunctions.closeSqlObjects(rs);
     DataBaseFunctions.closeSqlObjects(cntPstmt);
     DataBaseFunctions.closeSqlObjects(pstmt);
     DataBaseFunctions.closeSqlObjects(con);
     }
     return propertySearchResult;
     }*/
    public PropertySearchResult getPropertyStatement(String fiscalyear, String searchCriteria, String searchString, int noofrows, int page) {
        List propertyList = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement cntPstmt = null;
        ResultSet rs = null;
        PropertySearchResult propertySearchResult = new PropertySearchResult();
        try {
            con = dataSource.getConnection();
            int minlimit = noofrows * (page - 1);
            int maxlimit = noofrows;
            int total = 0;
            String cntsql = "SELECT count(*) as cnt FROM "
                    + "(SELECT ARRAY_TO_STRING(ARRAY[INITIALS,F_NAME,M_NAME,L_NAME],' ') FULLNAME,EMP_ID,GPF_NO,DOB,MOBILE,CUR_OFF_CODE FROM EMP_MAST) EMP_MAST "
                    + "LEFT OUTER JOIN (SELECT CADRE_CODE,SPC,PROPERTY_STATEMENT_LIST.EMP_ID,FINANCIAL_YEAR,STATUS_ID,FROM_DATE,TO_DATE,SUBMISSION_TYPE,POST_GROUP,SUBMITTED_ON,yearly_property_id FROM PROPERTY_STATEMENT_LIST WHERE FINANCIAL_YEAR=?) PROPERTY_STATEMENT_LIST ON  EMP_MAST.EMP_ID = PROPERTY_STATEMENT_LIST.EMP_ID "
                    + "LEFT OUTER JOIN G_SPC ON PROPERTY_STATEMENT_LIST.SPC=G_SPC.SPC "
                    + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                    + "LEFT OUTER JOIN G_CADRE ON PROPERTY_STATEMENT_LIST.CADRE_CODE=G_CADRE.CADRE_CODE "
                    + "LEFT OUTER JOIN G_OFFICE ON EMP_MAST.CUR_OFF_CODE=G_OFFICE.OFF_CODE "
                    + "LEFT OUTER JOIN FINANCIAL_YEAR ON property_statement_list.financial_year=FINANCIAL_YEAR.CY ";

            String sql = "SELECT F_NAME,FULLNAME,G_OFFICE.OFF_CODE,IS_CLOSED,EMP_MAST.EMP_ID,GPF_NO,DOB,OFF_EN,MOBILE,FINANCIAL_YEAR,STATUS_ID,FROM_DATE,TO_DATE,SUBMISSION_TYPE,G_CADRE.CADRE_NAME,POST_GROUP,POST,SUBMITTED_ON,yearly_property_id FROM "
                    + "(SELECT F_NAME,ARRAY_TO_STRING(ARRAY[INITIALS,F_NAME,M_NAME,L_NAME],' ') FULLNAME,EMP_ID,GPF_NO,DOB,MOBILE,CUR_OFF_CODE FROM EMP_MAST) EMP_MAST "
                    + "LEFT OUTER JOIN (SELECT CADRE_CODE,SPC,PROPERTY_STATEMENT_LIST.EMP_ID,FINANCIAL_YEAR,STATUS_ID,FROM_DATE,TO_DATE,SUBMISSION_TYPE,POST_GROUP,SUBMITTED_ON,yearly_property_id FROM PROPERTY_STATEMENT_LIST WHERE FINANCIAL_YEAR=?) PROPERTY_STATEMENT_LIST ON  EMP_MAST.EMP_ID = PROPERTY_STATEMENT_LIST.EMP_ID "
                    + "LEFT OUTER JOIN G_SPC ON PROPERTY_STATEMENT_LIST.SPC=G_SPC.SPC "
                    + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                    + "LEFT OUTER JOIN G_CADRE ON PROPERTY_STATEMENT_LIST.CADRE_CODE=G_CADRE.CADRE_CODE "
                    + "LEFT OUTER JOIN G_OFFICE ON EMP_MAST.CUR_OFF_CODE=G_OFFICE.OFF_CODE "
                    + "LEFT OUTER JOIN FINANCIAL_YEAR ON property_statement_list.financial_year=FINANCIAL_YEAR.CY ";

            String searchSQL = "";
            if (searchCriteria != null && !searchCriteria.equals("")) {
                if (searchCriteria.equalsIgnoreCase("empid")) {
                    searchSQL = " where EMP_MAST.EMP_ID=?";
                } else if (searchCriteria.equalsIgnoreCase("gpfno")) {
                    searchSQL = " where GPF_NO=?";
                } else if (searchCriteria.equalsIgnoreCase("empname")) {
                    searchSQL = " where (F_NAME LIKE ? OR M_NAME LIKE ? OR L_NAME LIKE ? OR ARRAY_TO_STRING(ARRAY[INITIALS,F_NAME,M_NAME,L_NAME],' ') LIKE ?)";
                } else if (searchCriteria.equalsIgnoreCase("mobile")) {
                    searchSQL = " where MOBILE=?";
                } else if (searchCriteria.equalsIgnoreCase("dob")) {
                    searchSQL = " where dob= to_date(?,'DD-Mon-yyyy')";
                }
            }

            if (searchSQL != null && !searchSQL.equals("")) {
                sql = sql + searchSQL;
                cntsql = cntsql + searchSQL;
            }
            sql = sql + " ORDER BY TRIM(F_NAME) ASC LIMIT ? OFFSET ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, fiscalyear);
            cntPstmt = con.prepareStatement(cntsql);
            cntPstmt.setString(1, fiscalyear);
            if (searchSQL != null && !searchSQL.equals("")) {
                if (searchCriteria.equalsIgnoreCase("empid")) {
                    pstmt.setString(2, searchString);
                    pstmt.setInt(3, maxlimit);
                    pstmt.setInt(4, minlimit);
                    cntPstmt.setString(2, searchString);
                } else if (searchCriteria.equalsIgnoreCase("gpfno")) {
                    pstmt.setString(2, searchString);
                    pstmt.setInt(3, maxlimit);
                    pstmt.setInt(4, minlimit);
                    cntPstmt.setString(2, searchString);
                } else if (searchCriteria.equalsIgnoreCase("empname")) {
                    pstmt.setString(2, "%" + searchString.toUpperCase() + "%");
                    pstmt.setString(3, "%" + searchString.toUpperCase() + "%");
                    pstmt.setString(4, "%" + searchString.toUpperCase() + "%");
                    pstmt.setString(5, "%" + searchString.toUpperCase() + "%");
                    pstmt.setInt(6, maxlimit);
                    pstmt.setInt(7, minlimit);
                    cntPstmt.setString(2, "%" + searchString.toUpperCase() + "%");
                    cntPstmt.setString(3, "%" + searchString.toUpperCase() + "%");
                    cntPstmt.setString(4, "%" + searchString.toUpperCase() + "%");
                    cntPstmt.setString(5, "%" + searchString.toUpperCase() + "%");
                } else if (searchCriteria.equalsIgnoreCase("mobile")) {
                    pstmt.setString(2, searchString);
                    pstmt.setInt(3, maxlimit);
                    pstmt.setInt(4, minlimit);
                    cntPstmt.setString(2, searchString);
                } else if (searchCriteria.equalsIgnoreCase("dob")) {
                    pstmt.setString(2, searchString);
                    pstmt.setInt(3, maxlimit);
                    pstmt.setInt(4, minlimit);
                    cntPstmt.setString(2, searchString);
                }
            } else {
                pstmt.setInt(2, maxlimit);
                pstmt.setInt(3, minlimit);
            }
            rs = pstmt.executeQuery();
            int count = 0;
            while (rs.next()) {
                count++;
                PropertyStatementAdminBean propertyadmin = new PropertyStatementAdminBean();
                propertyadmin.setRow(((page - 1) * 20 + count));
                propertyadmin.setYearlyPropertyId(rs.getString("yearly_property_id"));
                propertyadmin.setIsClosed(rs.getString("IS_CLOSED"));
                propertyadmin.setOffCode(rs.getString("OFF_CODE"));
                propertyadmin.setEmpname(rs.getString("FULLNAME"));
                propertyadmin.setEmpid(rs.getString("EMP_ID"));
                propertyadmin.setGpfno(rs.getString("GPF_NO"));
                propertyadmin.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOB")));
                propertyadmin.setCurrentoffice(rs.getString("OFF_EN"));
                propertyadmin.setCadreName(StringUtils.defaultString(rs.getString("CADRE_NAME")));
                propertyadmin.setPostgroup(StringUtils.defaultString(rs.getString("POST_GROUP")));
                propertyadmin.setPost(StringUtils.defaultString(rs.getString("POST")));
                propertyadmin.setMobile(StringUtils.defaultString(rs.getString("MOBILE")));

                if (rs.getString("STATUS_ID") != null && !rs.getString("STATUS_ID").equals("")) {
                    if (rs.getInt("STATUS_ID") == 0) {
                        propertyadmin.setStatus("NOT SUBMITTED");
                    } else if (rs.getInt("STATUS_ID") == 1) {
                        propertyadmin.setStatus("SUBMITTED");
                    }
                } else {
                    propertyadmin.setStatus("PROPERTY NOT CREATED");
                }

                propertyadmin.setFromdt(CommonFunctions.getFormattedOutputDate1(rs.getDate("FROM_DATE")));
                propertyadmin.setTodt(CommonFunctions.getFormattedOutputDate1(rs.getDate("TO_DATE")));
                if (rs.getString("SUBMISSION_TYPE") != null && !rs.getString("SUBMISSION_TYPE").equals("")) {
                    if (rs.getString("SUBMISSION_TYPE").equals("DS")) {
                        propertyadmin.setSubmissionType("DOCUMENT SUBMISSION");
                    } else if (rs.getString("SUBMISSION_TYPE").equals("FS")) {
                        propertyadmin.setSubmissionType("FORM SUBMISSION");
                    }
                } else {
                    propertyadmin.setSubmissionType("");
                }
                if (rs.getString("SUBMITTED_ON") != null && !rs.getString("SUBMITTED_ON").equals("")) {
                    propertyadmin.setSubmittedon(CommonFunctions.getFormattedOutputDate1(rs.getDate("SUBMITTED_ON")));
                } else {
                    propertyadmin.setSubmittedon("");
                }
                propertyList.add(propertyadmin);
            }
            propertySearchResult.setPropertyList(propertyList);

            /*Count noof rows*/
            rs = cntPstmt.executeQuery();
            if (rs.next()) {
                propertySearchResult.setTotalPropertyFound(rs.getInt("cnt"));
            }
            /*Count noof rows*/
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(cntPstmt);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return propertySearchResult;
    }

    public PropertySearchResult getPropertyStatementForDDO(String fiscalyear, String offCode, String searchCriteria, String searchString, int noofrows, int page) {
        List propertyList = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement cntPstmt = null;
        ResultSet rs = null;
        PropertySearchResult propertySearchResult = new PropertySearchResult();
        try {
            con = dataSource.getConnection();
            int minlimit = noofrows * (page - 1);
            int maxlimit = noofrows;
            int total = 0;
            String cntsql = "SELECT count(*) as cnt FROM "
                    + "(SELECT ARRAY_TO_STRING(ARRAY[INITIALS,F_NAME,M_NAME,L_NAME],' ') FULLNAME,EMP_ID,GPF_NO,DOB,MOBILE,CUR_OFF_CODE FROM EMP_MAST) EMP_MAST "
                    + "LEFT OUTER JOIN (SELECT CADRE_CODE,SPC,PROPERTY_STATEMENT_LIST.EMP_ID,FINANCIAL_YEAR,STATUS_ID,FROM_DATE,TO_DATE,SUBMISSION_TYPE,POST_GROUP,SUBMITTED_ON,yearly_property_id FROM PROPERTY_STATEMENT_LIST WHERE FINANCIAL_YEAR=?) PROPERTY_STATEMENT_LIST ON  EMP_MAST.EMP_ID = PROPERTY_STATEMENT_LIST.EMP_ID "
                    + "LEFT OUTER JOIN G_SPC ON PROPERTY_STATEMENT_LIST.SPC=G_SPC.SPC "
                    + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                    + "LEFT OUTER JOIN G_CADRE ON PROPERTY_STATEMENT_LIST.CADRE_CODE=G_CADRE.CADRE_CODE "
                    + "LEFT OUTER JOIN G_OFFICE ON EMP_MAST.CUR_OFF_CODE=G_OFFICE.OFF_CODE "
                    + "LEFT OUTER JOIN FINANCIAL_YEAR ON property_statement_list.financial_year=FINANCIAL_YEAR.CY "
                    + "WHERE  CUR_OFF_CODE=? ";

            String sql = "SELECT F_NAME,FULLNAME,G_OFFICE.OFF_CODE,IS_CLOSED,EMP_MAST.EMP_ID,GPF_NO,DOB,OFF_EN,MOBILE,FINANCIAL_YEAR,STATUS_ID,FROM_DATE,TO_DATE,SUBMISSION_TYPE,G_CADRE.CADRE_NAME,POST_GROUP,POST,SUBMITTED_ON,yearly_property_id FROM "
                    + "(SELECT F_NAME,ARRAY_TO_STRING(ARRAY[INITIALS,F_NAME,M_NAME,L_NAME],' ') FULLNAME,EMP_ID,GPF_NO,DOB,MOBILE,CUR_OFF_CODE FROM EMP_MAST) EMP_MAST "
                    + "LEFT OUTER JOIN (SELECT CADRE_CODE,SPC,PROPERTY_STATEMENT_LIST.EMP_ID,FINANCIAL_YEAR,STATUS_ID,FROM_DATE,TO_DATE,SUBMISSION_TYPE,POST_GROUP,SUBMITTED_ON,yearly_property_id FROM PROPERTY_STATEMENT_LIST WHERE FINANCIAL_YEAR=?) PROPERTY_STATEMENT_LIST ON  EMP_MAST.EMP_ID = PROPERTY_STATEMENT_LIST.EMP_ID "
                    + "LEFT OUTER JOIN G_SPC ON PROPERTY_STATEMENT_LIST.SPC=G_SPC.SPC "
                    + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                    + "LEFT OUTER JOIN G_CADRE ON PROPERTY_STATEMENT_LIST.CADRE_CODE=G_CADRE.CADRE_CODE "
                    + "LEFT OUTER JOIN G_OFFICE ON EMP_MAST.CUR_OFF_CODE=G_OFFICE.OFF_CODE "
                    + "LEFT OUTER JOIN FINANCIAL_YEAR ON property_statement_list.financial_year=FINANCIAL_YEAR.CY "
                    + "WHERE CUR_OFF_CODE=? ";
            String searchSQL = "";
            if (searchCriteria != null && !searchCriteria.equals("")) {
                if (searchCriteria.equalsIgnoreCase("empid")) {
                    searchSQL = " AND EMP_MAST.EMP_ID=?";
                } else if (searchCriteria.equalsIgnoreCase("gpfno")) {
                    searchSQL = " AND GPF_NO=?";
                } else if (searchCriteria.equalsIgnoreCase("empname")) {
                    searchSQL = " AND (F_NAME LIKE ? OR M_NAME LIKE ? OR L_NAME LIKE ? OR ARRAY_TO_STRING(ARRAY[INITIALS,F_NAME,M_NAME,L_NAME],' ') LIKE ?)";
                } else if (searchCriteria.equalsIgnoreCase("mobile")) {
                    searchSQL = " AND MOBILE=?";
                } else if (searchCriteria.equalsIgnoreCase("dob")) {
                    searchSQL = " AND dob= to_date(?,'DD-Mon-yyyy')";
                }
            }

            if (searchSQL != null && !searchSQL.equals("")) {
                sql = sql + searchSQL;
                cntsql = cntsql + searchSQL;
            }
            sql = sql + " ORDER BY TRIM(F_NAME) ASC LIMIT ? OFFSET ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, fiscalyear);
            pstmt.setString(2, offCode);
            cntPstmt = con.prepareStatement(cntsql);
            cntPstmt.setString(1, fiscalyear);
            cntPstmt.setString(2, offCode);
            if (searchSQL != null && !searchSQL.equals("")) {
                if (searchCriteria.equalsIgnoreCase("empid")) {
                    pstmt.setString(3, searchString);
                    pstmt.setInt(4, maxlimit);
                    pstmt.setInt(5, minlimit);
                    cntPstmt.setString(3, searchString);
                } else if (searchCriteria.equalsIgnoreCase("gpfno")) {
                    pstmt.setString(3, searchString);
                    pstmt.setInt(4, maxlimit);
                    pstmt.setInt(5, minlimit);
                    cntPstmt.setString(3, searchString);
                } else if (searchCriteria.equalsIgnoreCase("empname")) {
                    pstmt.setString(3, "%" + searchString.toUpperCase() + "%");
                    pstmt.setString(4, "%" + searchString.toUpperCase() + "%");
                    pstmt.setString(5, "%" + searchString.toUpperCase() + "%");
                    pstmt.setString(6, "%" + searchString.toUpperCase() + "%");
                    pstmt.setInt(7, maxlimit);
                    pstmt.setInt(8, minlimit);
                    cntPstmt.setString(3, "%" + searchString.toUpperCase() + "%");
                    cntPstmt.setString(4, "%" + searchString.toUpperCase() + "%");
                    cntPstmt.setString(5, "%" + searchString.toUpperCase() + "%");
                    cntPstmt.setString(6, "%" + searchString.toUpperCase() + "%");
                } else if (searchCriteria.equalsIgnoreCase("mobile")) {
                    pstmt.setString(3, searchString);
                    pstmt.setInt(4, maxlimit);
                    pstmt.setInt(5, minlimit);
                    cntPstmt.setString(3, searchString);
                } else if (searchCriteria.equalsIgnoreCase("dob")) {
                    pstmt.setString(3, searchString);
                    pstmt.setInt(4, maxlimit);
                    pstmt.setInt(5, minlimit);
                    cntPstmt.setString(3, searchString);
                }
            } else {
                pstmt.setInt(3, maxlimit);
                pstmt.setInt(4, minlimit);
            }
            rs = pstmt.executeQuery();
            int count = 0;
            while (rs.next()) {
                count++;
                PropertyStatementAdminBean propertyadmin = new PropertyStatementAdminBean();
                propertyadmin.setRow(((page - 1) * 20 + count));
                propertyadmin.setYearlyPropertyId(rs.getString("yearly_property_id"));
                propertyadmin.setIsClosed(rs.getString("IS_CLOSED"));
                propertyadmin.setOffCode(rs.getString("OFF_CODE"));
                propertyadmin.setEmpname(rs.getString("FULLNAME"));
                propertyadmin.setEmpid(rs.getString("EMP_ID"));
                propertyadmin.setGpfno(rs.getString("GPF_NO"));
                propertyadmin.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOB")));
                propertyadmin.setCurrentoffice(rs.getString("OFF_EN"));
                propertyadmin.setCadreName(StringUtils.defaultString(rs.getString("CADRE_NAME")));
                propertyadmin.setPostgroup(StringUtils.defaultString(rs.getString("POST_GROUP")));
                propertyadmin.setPost(StringUtils.defaultString(rs.getString("POST")));
                propertyadmin.setMobile(StringUtils.defaultString(rs.getString("MOBILE")));

                if (rs.getString("STATUS_ID") != null && !rs.getString("STATUS_ID").equals("")) {
                    if (rs.getInt("STATUS_ID") == 0) {
                        propertyadmin.setStatus("NOT SUBMITTED");
                    } else if (rs.getInt("STATUS_ID") == 1) {
                        propertyadmin.setStatus("SUBMITTED");
                    }
                } else {
                    propertyadmin.setStatus("PROPERTY NOT CREATED");
                }

                propertyadmin.setFromdt(CommonFunctions.getFormattedOutputDate1(rs.getDate("FROM_DATE")));
                propertyadmin.setTodt(CommonFunctions.getFormattedOutputDate1(rs.getDate("TO_DATE")));
                if (rs.getString("SUBMISSION_TYPE") != null && !rs.getString("SUBMISSION_TYPE").equals("")) {
                    if (rs.getString("SUBMISSION_TYPE").equals("DS")) {
                        propertyadmin.setSubmissionType("DOCUMENT SUBMISSION");
                    } else if (rs.getString("SUBMISSION_TYPE").equals("FS")) {
                        propertyadmin.setSubmissionType("FORM SUBMISSION");
                    }
                } else {
                    propertyadmin.setSubmissionType("");
                }
                if (rs.getString("SUBMITTED_ON") != null && !rs.getString("SUBMITTED_ON").equals("")) {
                    propertyadmin.setSubmittedon(CommonFunctions.getFormattedOutputDate1(rs.getDate("SUBMITTED_ON")));
                } else {
                    propertyadmin.setSubmittedon("");
                }
                propertyList.add(propertyadmin);
            }
            propertySearchResult.setPropertyList(propertyList);

            /*Count noof rows*/
            rs = cntPstmt.executeQuery();
            if (rs.next()) {
                propertySearchResult.setTotalPropertyFound(rs.getInt("cnt"));
            }
            /*Count noof rows*/
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(cntPstmt);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return propertySearchResult;
    }

    public PropertySearchResult getPropertyStatementListBySearchString(PropertyAdminSearchCriteria propertyAdminSearchCriteria) {
        PropertySearchResult propertySearchResult = new PropertySearchResult();
        List propertyList = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement cntPstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            int minlimit = propertyAdminSearchCriteria.getRows() * (propertyAdminSearchCriteria.getPage() - 1);
            int maxlimit = propertyAdminSearchCriteria.getRows();
            int total = 0;

            /*Previously Written For getting Property of only Employee who have submitted 
            
             String cntsql = "SELECT count(*) as cnt FROM PROPERTY_STATEMENT_LIST"
             + " INNER JOIN EMP_MAST ON PROPERTY_STATEMENT_LIST.EMP_ID=EMP_MAST.EMP_ID"
             + " LEFT OUTER JOIN G_SPC ON PROPERTY_STATEMENT_LIST.SPC=G_SPC.SPC"
             + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE"
             + " LEFT OUTER JOIN G_CADRE ON EMP_MAST.CUR_CADRE_CODE=G_CADRE.CADRE_CODE "
             + " LEFT OUTER JOIN G_OFFICE ON EMP_MAST.CUR_OFF_CODE=G_OFFICE.OFF_CODE "
             + " WHERE PROPERTY_STATEMENT_LIST.FINANCIAL_YEAR=?";

             String sql = "SELECT ARRAY_TO_STRING(ARRAY[INITIALS,F_NAME,M_NAME,L_NAME],' ') FULLNAME,PROPERTY_STATEMENT_LIST.EMP_ID,GPF_NO,DOB,OFF_EN,MOBILE,FINANCIAL_YEAR,STATUS_ID,FROM_DATE,TO_DATE,SUBMISSION_TYPE,G_CADRE.CADRE_NAME,POST_GROUP,POST,SUBMITTED_ON,yearly_property_id FROM PROPERTY_STATEMENT_LIST"
             + " INNER JOIN EMP_MAST ON PROPERTY_STATEMENT_LIST.EMP_ID=EMP_MAST.EMP_ID"
             + " LEFT OUTER JOIN G_SPC ON PROPERTY_STATEMENT_LIST.SPC=G_SPC.SPC"
             + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE"
             + " LEFT OUTER JOIN G_CADRE ON EMP_MAST.CUR_CADRE_CODE=G_CADRE.CADRE_CODE "
             + " LEFT OUTER JOIN G_OFFICE ON EMP_MAST.CUR_OFF_CODE=G_OFFICE.OFF_CODE "
             + " WHERE PROPERTY_STATEMENT_LIST.FINANCIAL_YEAR=?"; */
            /* Previously Written to get cadre name from only property Statement List
             String cntsql = "SELECT count(*) as cnt FROM "
             + "(SELECT F_NAME,M_NAME,L_NAME,ARRAY_TO_STRING(ARRAY[INITIALS,F_NAME,M_NAME,L_NAME],' ') FULLNAME,EMP_ID,GPF_NO,DOB,MOBILE,CUR_OFF_CODE FROM EMP_MAST) EMP_MAST "
             + "LEFT OUTER JOIN (SELECT CADRE_CODE,SPC,PROPERTY_STATEMENT_LIST.EMP_ID,FINANCIAL_YEAR,STATUS_ID,FROM_DATE,TO_DATE,SUBMISSION_TYPE,POST_GROUP,SUBMITTED_ON,yearly_property_id FROM PROPERTY_STATEMENT_LIST WHERE FINANCIAL_YEAR=?) PROPERTY_STATEMENT_LIST ON  EMP_MAST.EMP_ID = PROPERTY_STATEMENT_LIST.EMP_ID "
             + "LEFT OUTER JOIN G_SPC ON PROPERTY_STATEMENT_LIST.SPC=G_SPC.SPC "
             + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
             + "LEFT OUTER JOIN G_CADRE ON PROPERTY_STATEMENT_LIST.CADRE_CODE=G_CADRE.CADRE_CODE "
             + "LEFT OUTER JOIN G_OFFICE ON EMP_MAST.CUR_OFF_CODE=G_OFFICE.OFF_CODE "
             + "LEFT OUTER JOIN FINANCIAL_YEAR ON property_statement_list.financial_year=FINANCIAL_YEAR.CY ";

             String sql = "SELECT F_NAME,M_NAME,L_NAME,FULLNAME,G_OFFICE.OFF_CODE,IS_CLOSED,EMP_MAST.EMP_ID,GPF_NO,DOB,OFF_EN,MOBILE,FINANCIAL_YEAR,STATUS_ID,FROM_DATE,TO_DATE,SUBMISSION_TYPE,G_CADRE.CADRE_NAME,POST_GROUP,POST,SUBMITTED_ON,yearly_property_id FROM "
             + "(SELECT F_NAME,M_NAME,L_NAME,ARRAY_TO_STRING(ARRAY[INITIALS,F_NAME,M_NAME,L_NAME],' ') FULLNAME,EMP_ID,GPF_NO,DOB,MOBILE,CUR_OFF_CODE FROM EMP_MAST) EMP_MAST "
             + "LEFT OUTER JOIN (SELECT CADRE_CODE,SPC,PROPERTY_STATEMENT_LIST.EMP_ID,FINANCIAL_YEAR,STATUS_ID,FROM_DATE,TO_DATE,SUBMISSION_TYPE,POST_GROUP,SUBMITTED_ON,yearly_property_id FROM PROPERTY_STATEMENT_LIST WHERE FINANCIAL_YEAR=?) PROPERTY_STATEMENT_LIST ON  EMP_MAST.EMP_ID = PROPERTY_STATEMENT_LIST.EMP_ID "
             + "LEFT OUTER JOIN G_SPC ON PROPERTY_STATEMENT_LIST.SPC=G_SPC.SPC "
             + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
             + "LEFT OUTER JOIN G_CADRE ON PROPERTY_STATEMENT_LIST.CADRE_CODE=G_CADRE.CADRE_CODE "
             + "LEFT OUTER JOIN G_OFFICE ON EMP_MAST.CUR_OFF_CODE=G_OFFICE.OFF_CODE "
             + "LEFT OUTER JOIN FINANCIAL_YEAR ON property_statement_list.financial_year=FINANCIAL_YEAR.CY ";*/
            String cntsql = "SELECT count(*) as cnt FROM "
                    + "(SELECT cur_spc,cur_cadre_code,F_NAME,M_NAME,L_NAME,ARRAY_TO_STRING(ARRAY[INITIALS,F_NAME,M_NAME,L_NAME],' ') FULLNAME,EMP_ID,GPF_NO,DOB,MOBILE,CUR_OFF_CODE FROM EMP_MAST) EMP_MAST "
                    + "LEFT OUTER JOIN (SELECT CADRE_CODE,SPC,PROPERTY_STATEMENT_LIST.EMP_ID,FINANCIAL_YEAR,STATUS_ID,FROM_DATE,TO_DATE,SUBMISSION_TYPE,POST_GROUP,SUBMITTED_ON,yearly_property_id FROM PROPERTY_STATEMENT_LIST WHERE FINANCIAL_YEAR=?) PROPERTY_STATEMENT_LIST ON  EMP_MAST.EMP_ID = PROPERTY_STATEMENT_LIST.EMP_ID "
                    + "LEFT OUTER JOIN G_SPC SPCPROPERTY ON PROPERTY_STATEMENT_LIST.SPC=SPCPROPERTY.SPC "
                    + "LEFT OUTER JOIN G_SPC SPCEMP ON EMP_MAST.cur_spc=SPCEMP.SPC "
                    + "LEFT OUTER JOIN G_POST POSTPRPTY ON SPCPROPERTY.GPC=POSTPRPTY.POST_CODE "
                    + "LEFT OUTER JOIN G_POST POSTEMP ON SPCEMP.GPC=POSTEMP.POST_CODE "
                    + "LEFT OUTER JOIN G_CADRE CADREPROPERTY ON PROPERTY_STATEMENT_LIST.cadre_code=CADREPROPERTY.CADRE_CODE "
                    + "LEFT OUTER JOIN G_CADRE CADREEMP ON EMP_MAST.cur_cadre_code=CADREEMP.CADRE_CODE "
                    + "LEFT OUTER JOIN G_OFFICE ON EMP_MAST.CUR_OFF_CODE=G_OFFICE.OFF_CODE "
                    + "LEFT OUTER JOIN FINANCIAL_YEAR ON property_statement_list.financial_year=FINANCIAL_YEAR.CY ";

            String sql = "SELECT POSTPRPTY.POST PRPPOSTNAME,POSTEMP.POST EMPPOSTNAME,CADREPROPERTY.CADRE_NAME PRPCADRENAME,CADREEMP.CADRE_NAME EMPCADRENAME,SPCPROPERTY.SPN,SPCEMP.SPN,F_NAME,M_NAME,L_NAME,FULLNAME,G_OFFICE.OFF_CODE,IS_CLOSED,EMP_MAST.EMP_ID,GPF_NO,DOB,OFF_EN,MOBILE,FINANCIAL_YEAR,STATUS_ID,FROM_DATE,TO_DATE,SUBMISSION_TYPE,POST_GROUP,SUBMITTED_ON,yearly_property_id FROM "
                    + "(SELECT cur_spc,cur_cadre_code,F_NAME,M_NAME,L_NAME,ARRAY_TO_STRING(ARRAY[INITIALS,F_NAME,M_NAME,L_NAME],' ') FULLNAME,EMP_ID,GPF_NO,DOB,MOBILE,CUR_OFF_CODE FROM EMP_MAST) EMP_MAST "
                    + "LEFT OUTER JOIN (SELECT CADRE_CODE,SPC,PROPERTY_STATEMENT_LIST.EMP_ID,FINANCIAL_YEAR,STATUS_ID,FROM_DATE,TO_DATE,SUBMISSION_TYPE,POST_GROUP,SUBMITTED_ON,yearly_property_id FROM PROPERTY_STATEMENT_LIST WHERE FINANCIAL_YEAR=?) "
                    + "PROPERTY_STATEMENT_LIST ON EMP_MAST.EMP_ID = PROPERTY_STATEMENT_LIST.EMP_ID "
                    + "LEFT OUTER JOIN G_SPC SPCPROPERTY ON PROPERTY_STATEMENT_LIST.SPC=SPCPROPERTY.SPC "
                    + "LEFT OUTER JOIN G_SPC SPCEMP ON EMP_MAST.cur_spc=SPCEMP.SPC "
                    + "LEFT OUTER JOIN G_POST POSTPRPTY ON SPCPROPERTY.GPC=POSTPRPTY.POST_CODE "
                    + "LEFT OUTER JOIN G_POST POSTEMP ON SPCEMP.GPC=POSTEMP.POST_CODE "
                    + "LEFT OUTER JOIN G_CADRE CADREPROPERTY ON PROPERTY_STATEMENT_LIST.cadre_code=CADREPROPERTY.CADRE_CODE "
                    + "LEFT OUTER JOIN G_CADRE CADREEMP ON EMP_MAST.cur_cadre_code=CADREEMP.CADRE_CODE "
                    + "LEFT OUTER JOIN G_OFFICE ON EMP_MAST.CUR_OFF_CODE=G_OFFICE.OFF_CODE "
                    + "LEFT OUTER JOIN FINANCIAL_YEAR ON property_statement_list.financial_year=FINANCIAL_YEAR.CY ";

            String searchSQL = "";
            if (propertyAdminSearchCriteria.getSearchCriteria() != null && !propertyAdminSearchCriteria.getSearchCriteria().equals("")) {
                if (propertyAdminSearchCriteria.getSearchCriteria().equalsIgnoreCase("F")) {
                    searchSQL = " where EMP_MAST.F_NAME LIKE ? OR EMP_MAST.M_NAME LIKE ? OR EMP_MAST.L_NAME LIKE ? OR ARRAY_TO_STRING(ARRAY[F_NAME,M_NAME,L_NAME],' ') LIKE ? ";
                } else if (propertyAdminSearchCriteria.getSearchCriteria().equalsIgnoreCase("G")) {
                    searchSQL = " where EMP_MAST.GPF_NO = ?";
                } else if (propertyAdminSearchCriteria.getSearchCriteria().equalsIgnoreCase("H")) {
                    searchSQL = " where EMP_MAST.EMP_ID = ?";
                } else if (propertyAdminSearchCriteria.getSearchCriteria().equalsIgnoreCase("D")) {
                    searchSQL = " where EMP_MAST.dob= to_date(?,'DD-Mon-yyyy')";
                }
            }

            if (searchSQL != null && !searchSQL.equals("")) {
                sql = sql + searchSQL;
                cntsql = cntsql + searchSQL;
            }
            sql = sql + " ORDER BY yearly_property_id ASC LIMIT ? OFFSET ?";

            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, propertyAdminSearchCriteria.getFiscalyear());
            cntPstmt = con.prepareStatement(cntsql);
            cntPstmt.setString(1, propertyAdminSearchCriteria.getFiscalyear());

            if (searchSQL != null && !searchSQL.equals("")) {
                if ((propertyAdminSearchCriteria.getSearchCriteria().equalsIgnoreCase("F") || propertyAdminSearchCriteria.getSearchCriteria().equalsIgnoreCase("G") || propertyAdminSearchCriteria.getSearchCriteria().equalsIgnoreCase("H") || propertyAdminSearchCriteria.getSearchCriteria().equalsIgnoreCase("D"))) {
                    if (propertyAdminSearchCriteria.getSearchCriteria().equalsIgnoreCase("F")) {
                        pstmt.setString(2, "%" + propertyAdminSearchCriteria.getSearchString().toUpperCase() + "%");
                        pstmt.setString(3, "%" + propertyAdminSearchCriteria.getSearchString().toUpperCase() + "%");
                        pstmt.setString(4, "%" + propertyAdminSearchCriteria.getSearchString().toUpperCase() + "%");
                        pstmt.setString(5, "%" + propertyAdminSearchCriteria.getSearchString().toUpperCase() + "%");
                        pstmt.setInt(6, maxlimit);
                        pstmt.setInt(7, minlimit);
                        cntPstmt.setString(2, "%" + propertyAdminSearchCriteria.getSearchString().toUpperCase() + "%");
                        cntPstmt.setString(3, "%" + propertyAdminSearchCriteria.getSearchString().toUpperCase() + "%");
                        cntPstmt.setString(4, "%" + propertyAdminSearchCriteria.getSearchString().toUpperCase() + "%");
                        cntPstmt.setString(5, "%" + propertyAdminSearchCriteria.getSearchString().toUpperCase() + "%");
                    } else if (propertyAdminSearchCriteria.getSearchCriteria().equalsIgnoreCase("G") || propertyAdminSearchCriteria.getSearchCriteria().equalsIgnoreCase("H") || propertyAdminSearchCriteria.getSearchCriteria().equalsIgnoreCase("D")) {
                        pstmt.setString(2, propertyAdminSearchCriteria.getSearchString());
                        pstmt.setInt(3, maxlimit);
                        pstmt.setInt(4, minlimit);
                        cntPstmt.setString(2, propertyAdminSearchCriteria.getSearchString());

                    }
                }
            } else {
                pstmt.setInt(2, maxlimit);
                pstmt.setInt(3, minlimit);
            }

            rs = pstmt.executeQuery();
            int count = 0;
            while (rs.next()) {
                count++;
                PropertyStatementAdminBean propertyadmin = new PropertyStatementAdminBean();
                propertyadmin.setRow(((propertyAdminSearchCriteria.getPage() - 1) * propertyAdminSearchCriteria.getRows() + count));
                if (rs.getString("yearly_property_id") != null && !rs.getString("yearly_property_id").equals("")) {
                    propertyadmin.setYearlyPropertyId(CommonFunctions.encodedTxt(rs.getString("yearly_property_id")));
                } else {
                    propertyadmin.setYearlyPropertyId((rs.getString("yearly_property_id")));
                }
                propertyadmin.setEmpname(rs.getString("FULLNAME"));
                propertyadmin.setEmpid(rs.getString("EMP_ID"));
                propertyadmin.setGpfno(rs.getString("GPF_NO"));
                propertyadmin.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOB")));
                propertyadmin.setCurrentoffice(rs.getString("OFF_EN"));
                //propertyadmin.setCadreName(StringUtils.defaultString(rs.getString("CADRE_NAME")));
                if (rs.getString("PRPCADRENAME") != null && !rs.getString("PRPCADRENAME").equals("")) {
                    propertyadmin.setCadreName(StringUtils.defaultString(rs.getString("PRPCADRENAME")));
                } else {
                    propertyadmin.setCadreName(StringUtils.defaultString(rs.getString("EMPCADRENAME")));
                }
                if (rs.getString("PRPPOSTNAME") != null && !rs.getString("PRPPOSTNAME").equals("")) {
                    propertyadmin.setPost(StringUtils.defaultString(rs.getString("PRPPOSTNAME")));
                } else {
                    propertyadmin.setPost(StringUtils.defaultString(rs.getString("EMPPOSTNAME")));
                }
                
                propertyadmin.setPostgroup(StringUtils.defaultString(rs.getString("POST_GROUP")));
                //propertyadmin.setPost(StringUtils.defaultString(rs.getString("POST")));
                propertyadmin.setMobile(StringUtils.defaultString(rs.getString("MOBILE")));
                if (rs.getString("STATUS_ID") != null && !rs.getString("STATUS_ID").equals("")) {
                    if (rs.getInt("STATUS_ID") == 0) {
                        propertyadmin.setStatus("NOT SUBMITTED");
                    } else if (rs.getInt("STATUS_ID") == 1) {
                        propertyadmin.setStatus("SUBMITTED");
                    }
                } else {
                    propertyadmin.setStatus("PROPERTY NOT CREATED");
                }
                propertyadmin.setFromdt(CommonFunctions.getFormattedOutputDate1(rs.getDate("FROM_DATE")));
                propertyadmin.setTodt(CommonFunctions.getFormattedOutputDate1(rs.getDate("TO_DATE")));
                if (rs.getString("SUBMISSION_TYPE") != null && !rs.getString("SUBMISSION_TYPE").equals("")) {
                    if (rs.getString("SUBMISSION_TYPE").equals("DS")) {
                        propertyadmin.setSubmissionType("DOCUMENT SUBMISSION");
                    } else if (rs.getString("SUBMISSION_TYPE").equals("FS")) {
                        propertyadmin.setSubmissionType("FORM SUBMISSION");
                    }
                } else {
                    propertyadmin.setSubmissionType("");
                }
                propertyadmin.setSubmittedon(CommonFunctions.getFormattedOutputDate1(rs.getDate("SUBMITTED_ON")));
                propertyList.add(propertyadmin);
            }
            propertySearchResult.setPropertyList(propertyList);

            /*Count noof rows*/
            rs = cntPstmt.executeQuery();
            if (rs.next()) {
                propertySearchResult.setTotalPropertyFound(rs.getInt("cnt"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(cntPstmt);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return propertySearchResult;
    }

    public PropertySearchResult getPropertyStatementListByOffice(PropertyAdminSearchCriteria propertyAdminSearchCriteria) {
        PropertySearchResult propertySearchResult = new PropertySearchResult();
        List propertyList = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement cntPstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            int minlimit = propertyAdminSearchCriteria.getRows() * (propertyAdminSearchCriteria.getPage() - 1);
            int maxlimit = propertyAdminSearchCriteria.getRows();
            int total = 0;

            /*Previously Written For getting Property of only Employee who have submitted
            
             String cntsql = "SELECT count(*) as cnt FROM PROPERTY_STATEMENT_LIST"
             + " INNER JOIN EMP_MAST ON PROPERTY_STATEMENT_LIST.EMP_ID=EMP_MAST.EMP_ID"
             + " LEFT OUTER JOIN G_SPC ON PROPERTY_STATEMENT_LIST.SPC=G_SPC.SPC"
             + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE"
             + " LEFT OUTER JOIN G_CADRE ON EMP_MAST.CUR_CADRE_CODE=G_CADRE.CADRE_CODE "
             + " LEFT OUTER JOIN G_OFFICE ON EMP_MAST.CUR_OFF_CODE=G_OFFICE.OFF_CODE "
             + " WHERE PROPERTY_STATEMENT_LIST.FINANCIAL_YEAR=?";

             String sql = "SELECT ARRAY_TO_STRING(ARRAY[INITIALS,F_NAME,M_NAME,L_NAME],' ') FULLNAME,PROPERTY_STATEMENT_LIST.EMP_ID,GPF_NO,DOB,MOBILE,FINANCIAL_YEAR,STATUS_ID,FROM_DATE,TO_DATE,SUBMISSION_TYPE,G_CADRE.CADRE_NAME,POST_GROUP,POST,SUBMITTED_ON,yearly_property_id FROM PROPERTY_STATEMENT_LIST"
             + " INNER JOIN EMP_MAST ON PROPERTY_STATEMENT_LIST.EMP_ID=EMP_MAST.EMP_ID"
             + " LEFT OUTER JOIN G_SPC ON PROPERTY_STATEMENT_LIST.SPC=G_SPC.SPC"
             + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE"
             + " LEFT OUTER JOIN G_CADRE ON EMP_MAST.CUR_CADRE_CODE=G_CADRE.CADRE_CODE "
             + " LEFT OUTER JOIN G_OFFICE ON EMP_MAST.CUR_OFF_CODE=G_OFFICE.OFF_CODE "
             + " WHERE PROPERTY_STATEMENT_LIST.FINANCIAL_YEAR=?";*/
            String cntsql = "SELECT count(*) as cnt FROM "
                    + "(SELECT ARRAY_TO_STRING(ARRAY[INITIALS,F_NAME,M_NAME,L_NAME],' ') FULLNAME,EMP_ID,GPF_NO,DOB,MOBILE,CUR_OFF_CODE,cur_cadre_code FROM EMP_MAST) EMP_MAST "
                    + "LEFT OUTER JOIN (SELECT CADRE_CODE,SPC,PROPERTY_STATEMENT_LIST.EMP_ID,FINANCIAL_YEAR,STATUS_ID,FROM_DATE,TO_DATE,SUBMISSION_TYPE,POST_GROUP,SUBMITTED_ON,yearly_property_id FROM PROPERTY_STATEMENT_LIST WHERE FINANCIAL_YEAR=?) PROPERTY_STATEMENT_LIST ON  EMP_MAST.EMP_ID = PROPERTY_STATEMENT_LIST.EMP_ID "
                    + "LEFT OUTER JOIN G_SPC ON PROPERTY_STATEMENT_LIST.SPC=G_SPC.SPC "
                    + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                    + "LEFT OUTER JOIN G_CADRE ON PROPERTY_STATEMENT_LIST.CADRE_CODE=G_CADRE.CADRE_CODE "
                    + "LEFT OUTER JOIN G_OFFICE ON EMP_MAST.CUR_OFF_CODE=G_OFFICE.OFF_CODE "
                    + "LEFT OUTER JOIN FINANCIAL_YEAR ON property_statement_list.financial_year=FINANCIAL_YEAR.CY ";

            String sql = "SELECT F_NAME,FULLNAME,G_OFFICE.OFF_CODE,IS_CLOSED,EMP_MAST.EMP_ID,GPF_NO,DOB,OFF_EN,MOBILE,FINANCIAL_YEAR,STATUS_ID,FROM_DATE,TO_DATE,SUBMISSION_TYPE,G_CADRE.CADRE_NAME,POST_GROUP,POST,SUBMITTED_ON,yearly_property_id,cur_cadre_code FROM "
                    + "(SELECT F_NAME,ARRAY_TO_STRING(ARRAY[INITIALS,F_NAME,M_NAME,L_NAME],' ') FULLNAME,EMP_ID,GPF_NO,DOB,MOBILE,CUR_OFF_CODE,cur_cadre_code FROM EMP_MAST) EMP_MAST "
                    + "LEFT OUTER JOIN (SELECT CADRE_CODE,SPC,PROPERTY_STATEMENT_LIST.EMP_ID,FINANCIAL_YEAR,STATUS_ID,FROM_DATE,TO_DATE,SUBMISSION_TYPE,POST_GROUP,SUBMITTED_ON,yearly_property_id FROM PROPERTY_STATEMENT_LIST WHERE FINANCIAL_YEAR=?) PROPERTY_STATEMENT_LIST ON  EMP_MAST.EMP_ID = PROPERTY_STATEMENT_LIST.EMP_ID "
                    + "LEFT OUTER JOIN G_SPC ON PROPERTY_STATEMENT_LIST.SPC=G_SPC.SPC "
                    + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                    + "LEFT OUTER JOIN G_CADRE ON PROPERTY_STATEMENT_LIST.CADRE_CODE=G_CADRE.CADRE_CODE "
                    + "LEFT OUTER JOIN G_OFFICE ON EMP_MAST.CUR_OFF_CODE=G_OFFICE.OFF_CODE "
                    + "LEFT OUTER JOIN FINANCIAL_YEAR ON property_statement_list.financial_year=FINANCIAL_YEAR.CY ";
            String searchSQL = "";
            if (propertyAdminSearchCriteria.getOffcode() != null && !propertyAdminSearchCriteria.getOffcode().equals("")) {
                searchSQL = " WHERE EMP_MAST.CUR_OFF_CODE = ?";

            }
            if (searchSQL != null && !searchSQL.equals("")) {
                sql = sql + searchSQL;
                cntsql = cntsql + searchSQL;
            }

            sql = sql + " ORDER BY yearly_property_id ASC LIMIT ? OFFSET ?";

            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, propertyAdminSearchCriteria.getFiscalyear());
            pstmt.setString(2, propertyAdminSearchCriteria.getOffcode());
            pstmt.setInt(3, maxlimit);
            pstmt.setInt(4, minlimit);
            cntPstmt = con.prepareStatement(cntsql);
            cntPstmt.setString(1, propertyAdminSearchCriteria.getFiscalyear());
            cntPstmt.setString(2, propertyAdminSearchCriteria.getOffcode());

            rs = pstmt.executeQuery();
            int count = 0;
            while (rs.next()) {
                count++;
                PropertyStatementAdminBean propertyadmin = new PropertyStatementAdminBean();
                propertyadmin.setRow(((propertyAdminSearchCriteria.getPage() - 1) * propertyAdminSearchCriteria.getRows() + count));
                if (rs.getString("yearly_property_id") != null && !rs.getString("yearly_property_id").equals("")) {
                    propertyadmin.setYearlyPropertyId(CommonFunctions.encodedTxt(rs.getString("yearly_property_id")));
                } else {
                    propertyadmin.setYearlyPropertyId((rs.getString("yearly_property_id")));
                }
                propertyadmin.setEmpname(rs.getString("FULLNAME"));
                propertyadmin.setEmpid(rs.getString("EMP_ID"));
                propertyadmin.setGpfno(rs.getString("GPF_NO"));
                propertyadmin.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOB")));
                // propertyadmin.setCurrentoffice(rs.getString("OFF_EN"));
                propertyadmin.setCadreName(StringUtils.defaultString(rs.getString("CADRE_NAME")));
                propertyadmin.setPostgroup(StringUtils.defaultString(rs.getString("POST_GROUP")));
                propertyadmin.setPost(StringUtils.defaultString(rs.getString("POST")));
                propertyadmin.setMobile(StringUtils.defaultString(rs.getString("MOBILE")));
                if (rs.getString("STATUS_ID") != null && !rs.getString("STATUS_ID").equals("")) {
                    if (rs.getInt("STATUS_ID") == 0) {
                        propertyadmin.setStatus("NOT SUBMITTED");
                    } else if (rs.getInt("STATUS_ID") == 1) {
                        propertyadmin.setStatus("SUBMITTED");
                    }
                } else {
                    propertyadmin.setStatus("PROPERTY NOT CREATED");
                }
                propertyadmin.setFromdt(CommonFunctions.getFormattedOutputDate1(rs.getDate("FROM_DATE")));
                propertyadmin.setTodt(CommonFunctions.getFormattedOutputDate1(rs.getDate("TO_DATE")));
                if (rs.getString("SUBMISSION_TYPE") != null && !rs.getString("SUBMISSION_TYPE").equals("")) {
                    if (rs.getString("SUBMISSION_TYPE").equals("DS")) {
                        propertyadmin.setSubmissionType("DOCUMENT SUBMISSION");
                    } else if (rs.getString("SUBMISSION_TYPE").equals("FS")) {
                        propertyadmin.setSubmissionType("FORM SUBMISSION");
                    }
                } else {
                    propertyadmin.setSubmissionType("");
                }
                propertyadmin.setSubmittedon(CommonFunctions.getFormattedOutputDate1(rs.getDate("SUBMITTED_ON")));
                propertyList.add(propertyadmin);
            }
            propertySearchResult.setPropertyList(propertyList);

            /*Count noof rows*/
            rs = cntPstmt.executeQuery();
            if (rs.next()) {
                propertySearchResult.setTotalPropertyFound(rs.getInt("cnt"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(cntPstmt);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return propertySearchResult;
    }

    public PropertySearchResult getPropertyStatementListByCadre(PropertyAdminSearchCriteria propertyAdminSearchCriteria) {
        PropertySearchResult propertySearchResult = new PropertySearchResult();
        List propertyList = new ArrayList();
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement cntPstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            int minlimit = propertyAdminSearchCriteria.getRows() * (propertyAdminSearchCriteria.getPage() - 1);
            int maxlimit = propertyAdminSearchCriteria.getRows();
            int total = 0;

            /* Previously Written For getting Property of only Employee who have submitted
            
             String cntsql = "SELECT count(*) as cnt FROM PROPERTY_STATEMENT_LIST"
             + " INNER JOIN EMP_MAST ON PROPERTY_STATEMENT_LIST.EMP_ID=EMP_MAST.EMP_ID"
             + " LEFT OUTER JOIN G_SPC ON PROPERTY_STATEMENT_LIST.SPC=G_SPC.SPC"
             + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE"
             + " LEFT OUTER JOIN G_CADRE ON EMP_MAST.CUR_CADRE_CODE=G_CADRE.CADRE_CODE "
             + " LEFT OUTER JOIN G_OFFICE ON EMP_MAST.CUR_OFF_CODE=G_OFFICE.OFF_CODE "
             + " WHERE PROPERTY_STATEMENT_LIST.FINANCIAL_YEAR=?";

             String sql = "SELECT ARRAY_TO_STRING(ARRAY[INITIALS,F_NAME,M_NAME,L_NAME],' ') FULLNAME,PROPERTY_STATEMENT_LIST.EMP_ID,GPF_NO,DOB,MOBILE,FINANCIAL_YEAR,STATUS_ID,FROM_DATE,TO_DATE,SUBMISSION_TYPE,G_CADRE.CADRE_NAME,POST_GROUP,POST,SUBMITTED_ON,yearly_property_id FROM PROPERTY_STATEMENT_LIST"
             + " INNER JOIN EMP_MAST ON PROPERTY_STATEMENT_LIST.EMP_ID=EMP_MAST.EMP_ID"
             + " LEFT OUTER JOIN G_SPC ON PROPERTY_STATEMENT_LIST.SPC=G_SPC.SPC"
             + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE"
             + " LEFT OUTER JOIN G_CADRE ON EMP_MAST.CUR_CADRE_CODE=G_CADRE.CADRE_CODE "
             + " LEFT OUTER JOIN G_OFFICE ON EMP_MAST.CUR_OFF_CODE=G_OFFICE.OFF_CODE "
             + " WHERE PROPERTY_STATEMENT_LIST.FINANCIAL_YEAR=?"; */
            String cntsql = "SELECT count(*) as cnt FROM "
                    + "(SELECT ARRAY_TO_STRING(ARRAY[INITIALS,F_NAME,M_NAME,L_NAME],' ') FULLNAME,EMP_ID,GPF_NO,DOB,MOBILE,CUR_OFF_CODE,cur_cadre_code FROM EMP_MAST) EMP_MAST "
                    + "LEFT OUTER JOIN (SELECT CADRE_CODE,SPC,PROPERTY_STATEMENT_LIST.EMP_ID,FINANCIAL_YEAR,STATUS_ID,FROM_DATE,TO_DATE,SUBMISSION_TYPE,POST_GROUP,SUBMITTED_ON,yearly_property_id FROM PROPERTY_STATEMENT_LIST WHERE FINANCIAL_YEAR=?) PROPERTY_STATEMENT_LIST ON  EMP_MAST.EMP_ID = PROPERTY_STATEMENT_LIST.EMP_ID "
                    + "LEFT OUTER JOIN G_SPC ON PROPERTY_STATEMENT_LIST.SPC=G_SPC.SPC "
                    + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                    + "LEFT OUTER JOIN G_CADRE ON PROPERTY_STATEMENT_LIST.CADRE_CODE=G_CADRE.CADRE_CODE "
                    + "LEFT OUTER JOIN G_OFFICE ON EMP_MAST.CUR_OFF_CODE=G_OFFICE.OFF_CODE "
                    + "LEFT OUTER JOIN FINANCIAL_YEAR ON property_statement_list.financial_year=FINANCIAL_YEAR.CY ";

            String sql = "SELECT F_NAME,FULLNAME,G_OFFICE.OFF_CODE,IS_CLOSED,EMP_MAST.EMP_ID,GPF_NO,DOB,OFF_EN,MOBILE,FINANCIAL_YEAR,STATUS_ID,FROM_DATE,TO_DATE,SUBMISSION_TYPE,G_CADRE.CADRE_NAME,POST_GROUP,POST,SUBMITTED_ON,yearly_property_id,CUR_CADRE_CODE FROM "
                    + "(SELECT F_NAME,ARRAY_TO_STRING(ARRAY[INITIALS,F_NAME,M_NAME,L_NAME],' ') FULLNAME,EMP_ID,GPF_NO,DOB,MOBILE,CUR_OFF_CODE,CUR_CADRE_CODE FROM EMP_MAST) EMP_MAST "
                    + "LEFT OUTER JOIN (SELECT CADRE_CODE,SPC,PROPERTY_STATEMENT_LIST.EMP_ID,FINANCIAL_YEAR,STATUS_ID,FROM_DATE,TO_DATE,SUBMISSION_TYPE,POST_GROUP,SUBMITTED_ON,yearly_property_id FROM PROPERTY_STATEMENT_LIST WHERE FINANCIAL_YEAR=?) PROPERTY_STATEMENT_LIST ON  EMP_MAST.EMP_ID = PROPERTY_STATEMENT_LIST.EMP_ID "
                    + "LEFT OUTER JOIN G_SPC ON PROPERTY_STATEMENT_LIST.SPC=G_SPC.SPC "
                    + "LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                    + "LEFT OUTER JOIN G_CADRE ON PROPERTY_STATEMENT_LIST.CADRE_CODE=G_CADRE.CADRE_CODE "
                    + "LEFT OUTER JOIN G_OFFICE ON EMP_MAST.CUR_OFF_CODE=G_OFFICE.OFF_CODE "
                    + "LEFT OUTER JOIN FINANCIAL_YEAR ON property_statement_list.financial_year=FINANCIAL_YEAR.CY ";
            String searchSQL = "";
            if (propertyAdminSearchCriteria.getCadre() != null && !propertyAdminSearchCriteria.getCadre().equals("")) {
                searchSQL = " WHERE EMP_MAST.CUR_CADRE_CODE = ?";
            }
            if (searchSQL != null && !searchSQL.equals("")) {
                sql = sql + searchSQL;
                cntsql = cntsql + searchSQL;
            }

            sql = sql + " ORDER BY yearly_property_id ASC LIMIT ? OFFSET ?";

            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, propertyAdminSearchCriteria.getFiscalyear());
            pstmt.setString(2, propertyAdminSearchCriteria.getCadre());
            pstmt.setInt(3, maxlimit);
            pstmt.setInt(4, minlimit);
            cntPstmt = con.prepareStatement(cntsql);
            cntPstmt.setString(1, propertyAdminSearchCriteria.getFiscalyear());
            cntPstmt.setString(2, propertyAdminSearchCriteria.getCadre());

            rs = pstmt.executeQuery();
            int count = 0;
            while (rs.next()) {
                count++;
                PropertyStatementAdminBean propertyadmin = new PropertyStatementAdminBean();
                propertyadmin.setRow(((propertyAdminSearchCriteria.getPage() - 1) * propertyAdminSearchCriteria.getRows() + count));
                if (rs.getString("yearly_property_id") != null && !rs.getString("yearly_property_id").equals("")) {
                    propertyadmin.setYearlyPropertyId(CommonFunctions.encodedTxt(rs.getString("yearly_property_id")));
                } else {
                    propertyadmin.setYearlyPropertyId((rs.getString("yearly_property_id")));
                }
                propertyadmin.setEmpname(rs.getString("FULLNAME"));
                propertyadmin.setEmpid(rs.getString("EMP_ID"));
                propertyadmin.setGpfno(rs.getString("GPF_NO"));
                propertyadmin.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOB")));
                // propertyadmin.setCurrentoffice(rs.getString("OFF_EN"));
                propertyadmin.setCadreName(StringUtils.defaultString(rs.getString("CADRE_NAME")));
                propertyadmin.setPostgroup(StringUtils.defaultString(rs.getString("POST_GROUP")));
                propertyadmin.setPost(StringUtils.defaultString(rs.getString("POST")));
                propertyadmin.setMobile(StringUtils.defaultString(rs.getString("MOBILE")));
                if (rs.getString("STATUS_ID") != null && !rs.getString("STATUS_ID").equals("")) {
                    if (rs.getInt("STATUS_ID") == 0) {
                        propertyadmin.setStatus("NOT SUBMITTED");
                    } else if (rs.getInt("STATUS_ID") == 1) {
                        propertyadmin.setStatus("SUBMITTED");
                    }
                } else {
                    propertyadmin.setStatus("PROPERTY NOT CREATED");
                }
                propertyadmin.setFromdt(CommonFunctions.getFormattedOutputDate1(rs.getDate("FROM_DATE")));
                propertyadmin.setTodt(CommonFunctions.getFormattedOutputDate1(rs.getDate("TO_DATE")));
                if (rs.getString("SUBMISSION_TYPE") != null && !rs.getString("SUBMISSION_TYPE").equals("")) {
                    if (rs.getString("SUBMISSION_TYPE").equals("DS")) {
                        propertyadmin.setSubmissionType("DOCUMENT SUBMISSION");
                    } else if (rs.getString("SUBMISSION_TYPE").equals("FS")) {
                        propertyadmin.setSubmissionType("FORM SUBMISSION");
                    }
                } else {
                    propertyadmin.setSubmissionType("");
                }
                propertyadmin.setSubmittedon(CommonFunctions.getFormattedOutputDate1(rs.getDate("SUBMITTED_ON")));
                propertyList.add(propertyadmin);
            }
            propertySearchResult.setPropertyList(propertyList);

            /*Count noof rows*/
            rs = cntPstmt.executeQuery();
            if (rs.next()) {
                propertySearchResult.setTotalPropertyFound(rs.getInt("cnt"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(cntPstmt);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return propertySearchResult;
    }

    @Override
    public PropertySearchResult getPropertyStatement(PropertyAdminSearchCriteria propertyAdminSearchCriteria) {
        PropertySearchResult propertySearchResult = new PropertySearchResult();
        if (propertyAdminSearchCriteria.getSearchby().equals("i")) {
            propertySearchResult = getPropertyStatementListBySearchString(propertyAdminSearchCriteria);
        } else if (propertyAdminSearchCriteria.getSearchby().equals("o")) {
            propertySearchResult = getPropertyStatementListByOffice(propertyAdminSearchCriteria);
        } else if (propertyAdminSearchCriteria.getSearchby().equals("c")) {
            propertySearchResult = getPropertyStatementListByCadre(propertyAdminSearchCriteria);
        }
        return propertySearchResult;
    }

    @Override
    public List getGroupWisePropertyStatus(String fiscalyear) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List propertyStatusList = new ArrayList();

        try {
            con = this.repodataSource.getConnection();
            String sql = " select t1.totalpostgrouptype,t1.totalpostgroupsubmitted,t1.noofemp,t1.pop_submit,t2.totalpostgroupsubmitted REGpostGrp,t2.noofemp REgNoEmp,t2.pop_submit RegPop_submitted "
                    + " from (select totalpostgroup.totalpostgrouptype,postgrouptypesubmitted.totalpostgroupsubmitted,totalpostgroup.noofemp,postgrouptypesubmitted.pop_submit from  "
                    + " (select post_grp_type totalpostgrouptype ,count(*)noofemp from emp_mast WHERE  (post_grp_type is not null and post_grp_type!='') group by post_grp_type)totalpostgroup "
                    + " left outer join (select temp.post_grp_type totalpostgroupsubmitted,count(*)pop_submit from (select emp_mast.* from emp_mast left outer join property_statement_list "
                    + " on emp_mast.emp_id=property_statement_list.emp_ID where (post_grp_type is not null and post_grp_type!='' ) and property_statement_list.financial_year=? and status_id=1 )temp "
                    + " group by temp.post_grp_type)postgrouptypesubmitted on totalpostgroup.totalpostgrouptype=postgrouptypesubmitted.totalpostgroupsubmitted )t1 "
                    + " left outer join (select totalpostgroup.totalpostgrouptype,postgrouptypesubmitted.totalpostgroupsubmitted,totalpostgroup.noofemp,postgrouptypesubmitted.pop_submit from "
                    + " (select post_grp_type totalpostgrouptype ,count(*)noofemp from emp_mast WHERE(post_grp_type is not null and post_grp_type!='' and is_regular='Y') "
                    + " group by post_grp_type)totalpostgroup "
                    + " left outer join(select temp.post_grp_type totalpostgroupsubmitted,count(*)pop_submit from (select emp_mast.* from emp_mast "
                    + " left outer join property_statement_list on emp_mast.emp_id=property_statement_list.emp_ID where "
                    + " (post_grp_type is not null and post_grp_type!='' and is_regular='Y') and property_statement_list.financial_year=? and status_id=1 )temp "
                    + " group by temp.post_grp_type)postgrouptypesubmitted "
                    + " on totalpostgroup.totalpostgrouptype=postgrouptypesubmitted.totalpostgroupsubmitted )t2 on t2.totalpostgrouptype=t1.totalpostgrouptype ";

            pst = con.prepareStatement(sql);
            pst.setString(1, fiscalyear);
            pst.setString(2, fiscalyear);
            rs = pst.executeQuery();

            while (rs.next()) {
                PropertyStatementAdminBean propertyStatementAdminBean = new PropertyStatementAdminBean();
                propertyStatementAdminBean.setTotalNopostgrouptype(rs.getString("totalpostgrouptype"));
                propertyStatementAdminBean.setTotalNumberEmployee(rs.getString("noofemp"));
                propertyStatementAdminBean.setTotalPropertySubmitted(rs.getString("pop_submit"));
                propertyStatementAdminBean.setTotalNumberRegularEmployee(rs.getString("regnoemp"));
                propertyStatementAdminBean.setTotalRegularempPropertySubmitted(rs.getString("regpop_submitted"));
                propertyStatusList.add(propertyStatementAdminBean);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return propertyStatusList;
    }

    @Override
    public void savePropertyStatementtatusDetailByPARAdmin(PropertyStatementStatusBean propertyStatementStatusBean) {
        String diskfileName = null;
        PreparedStatement pst = null;
        Connection con = null;
        String originalFileName = null;
        String contentType = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            if (!propertyStatementStatusBean.getPropertyStatusdocument().isEmpty()) {
                diskfileName = new Date().getTime() + "";
                originalFileName = propertyStatementStatusBean.getPropertyStatusdocument().getOriginalFilename();
                contentType = propertyStatementStatusBean.getPropertyStatusdocument().getContentType();
                byte[] bytes = propertyStatementStatusBean.getPropertyStatusdocument().getBytes();
                File dir = new File(this.uploadPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File serverFile = new File(dir.getAbsolutePath() + File.separator + diskfileName);
                FileOutputStream fout = new FileOutputStream(serverFile);
                fout.write(bytes);
                fout.close();

            }

            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String startTime = dateFormat.format(cal.getTime());
            Long curtime = new Date().getTime();

            pst = con.prepareStatement("INSERT INTO property_status_log(financial_year,change_on_date,property_period_extended_for,original_file_name_property_status,disk_file_name_property_status,file_type_property_status,file_path_property_status) VALUES(?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, propertyStatementStatusBean.getFiscalyear());
            pst.setTimestamp(2, new Timestamp(curtime));
            //pst.setTimestamp(7, new Timestamp(sdf.parse(parStatusBean.getParPeriodForAppraisee()).getTime()));
            pst.setTimestamp(3, new Timestamp(sdf.parse(propertyStatementStatusBean.getPropertyPeriodExtendedFor()).getTime()));
            pst.setString(4, originalFileName);
            pst.setString(5, diskfileName);
            pst.setString(6, contentType);
            pst.setString(7, this.uploadPath);
            pst.executeUpdate();

            pst = con.prepareStatement("UPDATE financial_year SET property_period_extended_for=? where cy=?");

            pst.setTimestamp(1, new Timestamp(sdf.parse(propertyStatementStatusBean.getPropertyPeriodExtendedFor()).getTime()));
            pst.setString(2, propertyStatementStatusBean.getFiscalyear());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public ArrayList getPropertyStatementtatusDetailByPARAdmin() {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList propertyStatusDetail = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select * from property_status_log");
            rs = pst.executeQuery();
            while (rs.next()) {
                PropertyStatementStatusBean propertyStatementStatusBean = new PropertyStatementStatusBean();
                propertyStatementStatusBean.setLogId(rs.getInt("log_id"));
                propertyStatementStatusBean.setFiscalyear(rs.getString("financial_year"));
                propertyStatementStatusBean.setPropertyStatusChangeOnDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("change_on_date")));
                propertyStatementStatusBean.setOriginalFileNameForpropertyStatus(rs.getString("original_file_name_property_status"));
                propertyStatementStatusBean.setDiskfileNameForpropertyStatus(rs.getString("disk_file_name_property_status"));
                propertyStatementStatusBean.setPropertyPeriodExtendedFor(CommonFunctions.getFormattedOutputDate1(rs.getDate("property_period_extended_for")));
                if (rs.getString("is_closed_property") != null && rs.getString("is_closed_property").equals("closeprop")) {
                    propertyStatementStatusBean.setIsClosedProperty("Closed");
                } else {
                    propertyStatementStatusBean.setIsClosedProperty("Open");
                }
                propertyStatusDetail.add(propertyStatementStatusBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return propertyStatusDetail;
    }

    public PropertyStatementStatusBean getAttachedFileforChangePropertyStatusByPARAdmin(int logId) {
        PropertyStatementStatusBean propertyStatementStatusBean = new PropertyStatementStatusBean();
        Connection con = null;
        Statement st = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select original_file_name_property_status,disk_file_name_property_status,file_type_property_status,file_path_property_status from property_status_log where log_id=?");
            pst.setInt(1, logId);
            rs = pst.executeQuery();
            String filepath = null;
            if (rs.next()) {
                propertyStatementStatusBean.setOriginalFileNameForpropertyStatus(rs.getString("original_file_name_property_status"));
                propertyStatementStatusBean.setDiskfileNameForpropertyStatus(rs.getString("disk_file_name_property_status"));
                propertyStatementStatusBean.setFileTypeForpropertyStatus("file_type_property_status");
                filepath = rs.getString("file_path_property_status");
            }
            File f = new File(filepath + File.separator + propertyStatementStatusBean.getDiskfileNameForpropertyStatus());
            propertyStatementStatusBean.setFilecontent(FileUtils.readFileToByteArray(f));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return propertyStatementStatusBean;
    }

    @Override
    public ArrayList getOfficeListForDDO(String empId) {
        Connection con = null;
        ArrayList officeListDDO = new ArrayList();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("select off_code,off_en,* from g_office where is_ddo='Y' and g_office.ddo_hrmsid=?");
            pstmt.setString(1, empId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                PropertyAdminSearchCriteria propertyAdminSearchCriteria = new PropertyAdminSearchCriteria();
                propertyAdminSearchCriteria.setOffcode(rs.getString("off_code"));
                propertyAdminSearchCriteria.setCurOfficeName(rs.getString("off_en"));
                officeListDDO.add(propertyAdminSearchCriteria);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return officeListDDO;
    }
}
