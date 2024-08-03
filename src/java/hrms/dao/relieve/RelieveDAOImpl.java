package hrms.dao.relieve;

import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.model.relieve.RelieveBean;
import hrms.model.relieve.RelieveForm;
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
import org.apache.commons.lang.builder.ToStringBuilder;

public class RelieveDAOImpl implements RelieveDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    protected MaxRelieveIdDAOImpl maxRelieveIdDao;

    protected ServiceBookLanguageDAO sbDAO;

    public void setSbDAO(ServiceBookLanguageDAO sbDAO) {
        this.sbDAO = sbDAO;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setMaxRelieveIdDao(MaxRelieveIdDAOImpl maxRelieveIdDao) {
        this.maxRelieveIdDao = maxRelieveIdDao;
    }

    @Override
    public List getRelieveList(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        RelieveBean rform = null;
        ArrayList alist = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            String sql = "select username,* from (select TEMP.* from (SELECT RE.entry_taken_by,RE.ENTRY_TAKEN_ON,RE.IF_VISIBLE,NOTI.NOT_TYPE AS NOTIFYTYPE, NOTI.NOT_ID AS NOTIFYID,NOTI.DOE AS NOTIYDOE,NOTI.ORDNO"
                    + " AS NOTIYORDNO,NOTI.ORDDT"
                    + " AS NOTIYORDDT,NOTI.DEPT_CODE AS NOTIYCODE,NOTI.OFF_CODE AS NOTIOFFCODE,NOTI.AUTH AS NOTIYAUTH ,RE.RELIEVE_ID AS RLID,RE.DOE AS"
                    + " RLDOE,RE.MEMO_NO AS RLMEMONO,RE.MEMO_DATE AS RLMEMODT,RE.RLV_DATE AS RLDATE,RE.RLV_TIME AS RLTIME,RE.SPC AS RLAUTH,RE.DUE_DOJ AS"
                    + " RLDUEDATE,RE.DUE_TOJ AS RLDUETIME,RE.LPC_FILE_PATH,RE.IF_RLNQ AS RLNQ,RE.TR_DATA_TYPE TRTYPE,RE.SV_ID FROM (SELECT * FROM EMP_NOTIFICATION WHERE"
                    + " EMP_ID=?) NOTI"
                    + " LEFT OUTER JOIN (SELECT EMP_RELIEVE.entry_taken_by,EMP_RELIEVE.ENTRY_TAKEN_ON,EMP_RELIEVE.IF_VISIBLE,EMP_RELIEVE.LPC_FILE_PATH,EMP_RELIEVE.RELIEVE_ID,EMP_RELIEVE.NOT_TYPE,EMP_RELIEVE.NOT_ID,EMP_RELIEVE.EMP_ID,EMP_RELIEVE.DOE,"
                    + " EMP_RELIEVE.MEMO_NO,EMP_RELIEVE.MEMO_DATE,EMP_RELIEVE.RLV_DATE,EMP_RELIEVE.RLV_TIME,EMP_RELIEVE.SPC,EMP_RELIEVE.DUE_DOJ,"
                    + " EMP_RELIEVE.DUE_TOJ, EMP_RELIEVE.NOTE, EMP_RELIEVE.JOIN_ID,EMP_RELIEVE.IF_RLNQ,EMP_RELIEVE.TR_DATA_TYPE,EMP_RELIEVE.SV_ID"
                    + " FROM (SELECT * FROM EMP_RELIEVE WHERE"
                    + " EMP_ID=? AND (JOIN_ID IS NULL OR (JOIN_ID::TEXT NOT IN (SELECT JOIN_ID::TEXT FROM EMP_JOIN WHERE EMP_ID=? AND IF_AD_CHARGE='Y'))))"
                    + " EMP_RELIEVE LEFT OUTER JOIN (SELECT * FROM EMP_JOIN WHERE EMP_ID=?) EMP_JOIN ON"
                    + " (EMP_RELIEVE.JOIN_ID=EMP_JOIN.JOIN_ID) AND"
                    + " if_ad_charge='N') RE ON NOTI.NOT_ID=RE.NOT_ID WHERE NOTI.EMP_ID=? and (NOTI.not_type ='REHABILITATION' or"
                    + " NOTI.not_type='ABSORPTION' or NOTI.not_type='REDEPLOYMENT' or NOTI.not_type='VALIDATION' or NOTI.not_type='LT_TRAINING' or"
                    + " NOTI.not_type='DEPUTATION' or NOTI.not_type ='TRANSFER' or NOTI.not_type ='PROMOTION' or NOTI.not_type ='REDESIGNATION' or"
                    + " NOTI.not_type ='RESIGNATION' or NOTI.not_type ='DECEASED' or NOTI.not_type ='RETIREMENT' or"
                    + " NOTI.not_type='ALLOT_CADRE' or (NOTI.not_type='LEAVE' AND NOTI.NOT_ID IN (SELECT NOT_ID FROM EMP_LEAVE WHERE EMP_ID=?"
                    + " AND IF_LONGTERM='Y'))) and (NOTI.not_id not in (select not_id from emp_cadre where emp_cadre.not_id=NOTI.not_id and"
                    + " joined_assuch='Y' and EMP_ID=?)) ORDER BY RLDATE DESC)TEMP)relievetemp "
                    + " left outer join user_details on relievetemp.entry_taken_by=user_details.linkid order by notiyorddt desc, notiydoe desc";

            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setString(2, empid);
            pst.setString(3, empid);
            pst.setString(4, empid);
            pst.setString(5, empid);
            pst.setString(6, empid);
            pst.setString(7, empid);

            rs = pst.executeQuery();
            while (rs.next()) {
                rform = new RelieveBean();
                rform.setRlvid(rs.getString("RLID"));
                rform.setNotid(rs.getString("NOTIFYID"));
                rform.setNotType(rs.getString("NOTIFYTYPE"));
                rform.setLpCFilePath(rs.getString("LPC_FILE_PATH"));
                if (rs.getDate("NOTIYDOE") != null && !rs.getDate("NOTIYDOE").equals("")) {
                    rform.setNotdoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("NOTIYDOE")));
                }
                rform.setNotordno(rs.getString("NOTIYORDNO"));
                if (rs.getDate("NOTIYORDDT") != null && !rs.getDate("NOTIYORDDT").equals("")) {
                    rform.setNotorddt(CommonFunctions.getFormattedOutputDate1(rs.getDate("NOTIYORDDT")));
                }
                rform.setRlvordno(rs.getString("RLMEMONO"));
                if (rs.getDate("RLMEMODT") != null && !rs.getDate("RLMEMODT").equals("")) {
                    rform.setRlvorddt(CommonFunctions.getFormattedOutputDate1(rs.getDate("RLMEMODT")));
                }
                if (rs.getDate("RLDATE") != null && !rs.getDate("RLDATE").equals("")) {
                    rform.setRlvondt(CommonFunctions.getFormattedOutputDate1(rs.getDate("RLDATE")));
                }
                rform.setRlvontime(rs.getString("RLTIME"));
                if (rs.getDate("RLDUEDATE") != null && !rs.getDate("RLDUEDATE").equals("")) {
                    rform.setRlvduedt(CommonFunctions.getFormattedOutputDate1(rs.getDate("RLDUEDATE")));
                }
                rform.setRlvduetime(rs.getString("RLDUETIME"));

                rform.setPrintInServiceBook(rs.getString("IF_VISIBLE"));

                if (rs.getString("username") != null && !rs.getString("username").equals("")) {
                    rform.setModifiedby(rs.getString("username") + "<br />" + CommonFunctions.getFormattedOutputDate1(rs.getDate("ENTRY_TAKEN_ON")));
                } else {
                    rform.setModifiedby("");
                }

                alist.add(rform);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return alist;
    }

    @Override
    public int getRelieveListCount(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int count = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "select count(*) cnt from (select * from (select TEMP.* from (SELECT NOTI.NOT_TYPE AS NOTIFYTYPE, NOTI.NOT_ID AS NOTIFYID,NOTI.DOE AS NOTIYDOE,NOTI.ORDNO"
                    + " AS NOTIYORDNO,NOTI.ORDDT"
                    + " AS NOTIYORDDT,NOTI.DEPT_CODE AS NOTIYCODE,NOTI.OFF_CODE AS NOTIOFFCODE,NOTI.AUTH AS NOTIYAUTH ,RE.RELIEVE_ID AS RLID,RE.DOE AS"
                    + " RLDOE,RE.MEMO_NO AS RLMEMONO,RE.MEMO_DATE AS RLMEMODT,RE.RLV_DATE AS RLDATE,RE.RLV_TIME AS RLTIME,RE.SPC AS RLAUTH,RE.DUE_DOJ AS"
                    + " RLDUEDATE,RE.DUE_TOJ AS RLDUETIME,RE.IF_RLNQ AS RLNQ,RE.TR_DATA_TYPE TRTYPE,RE.SV_ID FROM (SELECT * FROM EMP_NOTIFICATION WHERE"
                    + " EMP_ID=?) NOTI"
                    + " LEFT OUTER JOIN (SELECT EMP_RELIEVE.RELIEVE_ID,EMP_RELIEVE.NOT_TYPE,EMP_RELIEVE.NOT_ID,EMP_RELIEVE.EMP_ID,EMP_RELIEVE.DOE,"
                    + " EMP_RELIEVE.MEMO_NO,EMP_RELIEVE.MEMO_DATE,EMP_RELIEVE.RLV_DATE,EMP_RELIEVE.RLV_TIME,EMP_RELIEVE.SPC,EMP_RELIEVE.DUE_DOJ,"
                    + " EMP_RELIEVE.DUE_TOJ, EMP_RELIEVE.NOTE, EMP_RELIEVE.JOIN_ID,EMP_RELIEVE.IF_RLNQ,EMP_RELIEVE.TR_DATA_TYPE,EMP_RELIEVE.SV_ID"
                    + " FROM (SELECT * FROM EMP_RELIEVE WHERE"
                    + " EMP_ID=? AND (JOIN_ID IS NULL OR (JOIN_ID::TEXT NOT IN (SELECT JOIN_ID::TEXT FROM EMP_JOIN WHERE EMP_ID=? AND IF_AD_CHARGE='Y'))))"
                    + " EMP_RELIEVE LEFT OUTER JOIN (SELECT * FROM EMP_JOIN WHERE EMP_ID=?) EMP_JOIN ON"
                    + " (EMP_RELIEVE.JOIN_ID=EMP_JOIN.JOIN_ID) AND"
                    + " if_ad_charge='N') RE ON NOTI.NOT_ID=RE.NOT_ID WHERE NOTI.EMP_ID=? and (NOTI.not_type ='REHABILITATION' or"
                    + " NOTI.not_type='ABSORPTION' or NOTI.not_type='REDEPLOYMENT' or NOTI.not_type='VALIDATION' or NOTI.not_type='LT_TRAINING' or"
                    + " NOTI.not_type='DEPUTATION' or NOTI.not_type ='TRANSFER' or NOTI.not_type ='PROMOTION' or NOTI.not_type ='REDESIGNATION' or"
                    + " NOTI.not_type ='RESIGNATION' or NOTI.not_type ='DECEASED' or NOTI.not_type ='RETIREMENT' or"
                    + " NOTI.not_type='ALLOT_CADRE' or (NOTI.not_type='LEAVE' AND NOTI.NOT_ID IN (SELECT NOT_ID FROM EMP_LEAVE WHERE EMP_ID=?"
                    + " AND IF_LONGTERM='Y'))) and (NOTI.not_id not in (select not_id from emp_cadre where emp_cadre.not_id=NOTI.not_id and"
                    + " joined_assuch='Y' and EMP_ID=?)) ORDER BY RLDATE DESC)TEMP)relievetemp)temp";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setString(2, empid);
            pst.setString(3, empid);
            pst.setString(4, empid);
            pst.setString(5, empid);
            pst.setString(6, empid);
            pst.setString(7, empid);
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
    public RelieveForm getRelieveData(String empid, int notid, String rlvid) {

        Connection con = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        PreparedStatement joinpst = null;
        ResultSet joinrs = null;

        RelieveForm el = new RelieveForm();
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT EMP_NOTIFICATION.AUTH,EMP_NOTIFICATION.OFF_CODE,EMP_NOTIFICATION.DEPT_CODE,NOT_ID,NOT_TYPE,ORDNO,ORDDT,DEPARTMENT_NAME,OFF_EN,SPN FROM EMP_NOTIFICATION "
                    + "LEFT OUTER JOIN G_DEPARTMENT ON EMP_NOTIFICATION.DEPT_CODE =  G_DEPARTMENT.DEPARTMENT_CODE "
                    + "LEFT OUTER JOIN G_OFFICE ON EMP_NOTIFICATION.OFF_CODE =  G_OFFICE.OFF_CODE "
                    + "LEFT OUTER JOIN G_SPC ON EMP_NOTIFICATION.AUTH =  G_SPC.SPC WHERE NOT_ID=? AND EMP_ID=?");
            pstmt.setInt(1, notid);
            pstmt.setString(2, empid);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                //el = new RelieveForm();
                el.setEmpId(empid);
                el.setHidNotId(rs.getInt("NOT_ID"));
                el.setHidNotType(rs.getString("NOT_TYPE").toUpperCase());
                el.setOrdNo(rs.getString("ORDNO"));
                if (rs.getDate("ORDDT") != null && !rs.getDate("ORDDT").equals("")) {
                    el.setOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("ORDDT")).toUpperCase());
                }
                if (rs.getString("DEPT_CODE") != null && !rs.getString("DEPT_CODE").trim().equals("")) {
                    el.setDeptname(rs.getString("DEPARTMENT_NAME"));
                }
                if (rs.getString("OFF_CODE") != null && !rs.getString("OFF_CODE").trim().equals("")) {
                    el.setOffname(rs.getString("OFF_EN"));
                }
                if (rs.getString("AUTH") != null && !rs.getString("AUTH").trim().equals("")) {
                    el.setPostname(rs.getString("SPN"));
                }
            }
            if (el.getEmpId() != null && !el.getEmpId().equals("")) {
                getTransactionStatus(el);
            }
            if (el.getHidNotType() != null && !el.getHidNotType().equals("")) {
                if (el.getHidNotType().equals("RETIREMENT") || el.getHidNotType().equals("RESIGNATION") || el.getHidNotType().equals("DECEASED")) {
                    el.setTransactionStatus("C");
                }
            }
            if (rlvid != null && !rlvid.equals("")) {
                //pstmt = con.prepareStatement("SELECT * FROM EMP_RELIEVE WHERE RELIEVE_ID=? AND EMP_ID=?");
                pstmt = con.prepareStatement("SELECT EMP_RELIEVE.*,G_OFFICE.DEPARTMENT_CODE,DIST_CODE,G_SPC.OFF_CODE,GPC,POST,EMP_RELIEVE.SPC FROM EMP_RELIEVE"
                        + " LEFT OUTER JOIN G_SPC ON EMP_RELIEVE.SPC=G_SPC.SPC"
                        + " LEFT OUTER JOIN G_OFFICE ON G_SPC.OFF_CODE=G_OFFICE.OFF_CODE"
                        + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE"
                        + " WHERE RELIEVE_ID=? AND EMP_ID=?");
                pstmt.setInt(1, Integer.parseInt(rlvid));
                pstmt.setString(2, empid);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    el.setRlvId(rlvid);
                    if (rs.getString("if_visible") != null && rs.getString("if_visible").equals("N")) {
                        el.setChkNotSBPrint("Y");
                    } else {
                        el.setChkNotSBPrint("N");
                    }
                    el.setTxtRlvOrdNo(rs.getString("MEMO_NO"));
                    if (rs.getDate("MEMO_DATE") != null && !rs.getDate("MEMO_DATE").equals("")) {
                        el.setTxtRlvOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("MEMO_DATE")).toUpperCase());
                    }
                    if (rs.getDate("RLV_DATE") != null && !rs.getDate("RLV_DATE").equals("")) {
                        el.setTxtRlvDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("RLV_DATE")).toUpperCase());
                    }
                    el.setSltRlvTime(rs.getString("RLV_TIME"));
                    el.setChkSBPrint(rs.getString("IF_VISIBLE"));
                    if (rs.getDate("DUE_DOJ") != null && !rs.getDate("DUE_DOJ").equals("")) {
                        el.setTxtJoinDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("DUE_DOJ")).toUpperCase());
                    }
                    el.setSltJoinTime(rs.getString("DUE_TOJ"));
                    el.setRdRqRl(rs.getString("if_rlnq"));
                    if (rs.getString("SPC") != null && !rs.getString("SPC").equals("")) {
                        //if (el.getTransactionStatus() != null && el.getTransactionStatus().equals("C")) {
                        el.setHidRlvSpc(rs.getString("SPC"));
                        el.setSltRlvPost(rs.getString("SPC") + "@" + rs.getString("JOIN_ID"));
                        /*} else if (el.getTransactionStatus() != null && el.getTransactionStatus().equals("S")) {
                         el.setAuthHidDeptCode(rs.getString("DEPARTMENT_CODE"));
                         el.setAuthHidDistCode(rs.getString("DIST_CODE"));
                         el.setAuthHidOffCode(rs.getString("OFF_CODE"));
                         el.setAuthHidGPC(rs.getString("GPC"));
                         el.setAuthPostName(rs.getString("POST"));
                         }*/
                    }
                }
            }

            pstmt = con.prepareStatement("SELECT LAST_AQMONTH,LAST_AQYEAR FROM EMP_MAST WHERE EMP_ID=?");
            pstmt.setString(1, empid);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                el.setHidAqMonth(rs.getString("LAST_AQMONTH"));
                el.setHidAqYear(rs.getString("LAST_AQYEAR"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return el;
    }

    @Override
    public List getRelievedPostList(String empid, String rlvid, String transactionStatus, String rlvspc) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List li = new ArrayList();
        SelectOption so = null;
        try {
            con = this.dataSource.getConnection();

            if (transactionStatus != null && !transactionStatus.equals("")) {
                if (transactionStatus.equals("S")) {
                    String sql = "SELECT EMP_JOIN.SPC,JOIN_ID,POST,JOIN_DATE FROM EMP_JOIN"
                            + " INNER JOIN G_SPC ON EMP_JOIN.SPC=G_SPC.SPC"
                            + " INNER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE WHERE emp_id=? and (EMP_JOIN.SPC is not null and EMP_JOIN.SPC<>'') and if_ad_charge is null ORDER BY POST";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, empid);
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        so = new SelectOption();
                        String post = rs.getString("post");
                        if (post != null && !post.equals("")) {
                            String joindate = CommonFunctions.getFormattedOutputDate1(rs.getDate("JOIN_DATE"));
                            so.setValue(rs.getString("SPC") + "@" + rs.getString("JOIN_ID"));
                            so.setLabel(post + "(Joined on - " + joindate + ")");
                            li.add(so);
                        }
                    }
                } else if (transactionStatus.equals("C")) {
                    if (rlvid != null && !rlvid.equals("")) {
                        String sql = "SELECT EMP_JOIN.SPC,JOIN_ID,POST,JOIN_DATE FROM EMP_JOIN"
                                + " INNER JOIN G_SPC ON EMP_JOIN.SPC=G_SPC.SPC"
                                + " INNER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE WHERE emp_id=? and EMP_JOIN.SPC=? and if_ad_charge != 'Y' order by POST";
                        pst = con.prepareStatement(sql);
                        pst.setString(1, empid);
                        pst.setString(2, rlvspc);
                        rs = pst.executeQuery();
                        while (rs.next()) {
                            so = new SelectOption();
                            String post = rs.getString("post");
                            if (post != null && !post.equals("")) {
                                so.setValue(rs.getString("SPC") + "@" + rs.getString("JOIN_ID"));
                                so.setLabel(post);
                                li.add(so);
                            }
                        }
                    } else {
                        String sql = "SELECT emp_join.SPC,JOIN_ID,post,JOIN_DATE,emp_join.if_ad_charge FROM EMP_JOIN"
                                + " inner join emp_mast on emp_join.spc=emp_mast.cur_spc"
                                + " inner join g_spc on emp_join.spc=g_spc.spc"
                                + " inner join g_post on g_spc.gpc=g_post.post_code"
                                + " WHERE emp_join.emp_id=? and emp_mast.emp_id=? and (emp_join.SPC is not null and emp_join.SPC<>'') and if_ad_charge != 'Y' ORDER BY POST";
                        pst = con.prepareStatement(sql);
                        pst.setString(1, empid);
                        pst.setString(2, empid);
                        rs = pst.executeQuery();
                        while (rs.next()) {
                            so = new SelectOption();
                            String post = rs.getString("post");
                            if (post != null && !post.equals("")) {
                                String joindate = CommonFunctions.getFormattedOutputDate1(rs.getDate("JOIN_DATE"));
                                so.setValue(rs.getString("SPC") + "@" + rs.getString("JOIN_ID"));
                                if (rs.getString("if_ad_charge") != null && rs.getString("if_ad_charge").equals("Y")) {
                                    so.setLabel(post + "(Joined on - " + joindate + ")(Additional Charge)");
                                } else {
                                    so.setLabel(post + "(Joined on - " + joindate + ")");
                                }
                                li.add(so);
                            }
                        }
                    }

                    if (li.size() == 0) {
                        String sql = "SELECT emp_mast.cur_spc SPC,post,0 join_id FROM emp_mast"
                                + " inner join g_spc on emp_mast.cur_spc=g_spc.spc"
                                + " inner join g_post on g_spc.gpc=g_post.post_code"
                                + " WHERE emp_mast.emp_id=? and (emp_mast.cur_spc is not null and emp_mast.cur_spc<>'') ORDER BY POST";
                        pst = con.prepareStatement(sql);
                        pst.setString(1, empid);
                        rs = pst.executeQuery();
                        while (rs.next()) {
                            so = new SelectOption();
                            String post = rs.getString("post");
                            if (post != null && !post.equals("")) {
                                so.setValue(rs.getString("SPC") + "@" + rs.getString("JOIN_ID"));
                                so.setLabel(post);
                                li.add(so);
                            }
                        }
                    }

                }
            }

            /*String sql = "SELECT SPC,JOIN_ID FROM EMP_JOIN WHERE emp_id=? and if_ad_charge='N' and JOIN_ID NOT IN"
             + " (select join_id from emp_relieve where emp_id=? and join_id is not null)"
             + " UNION"
             + " SELECT CUR_SPC,'NA' FROM  EMP_MAST WHERE EMP_ID=?";*/
            /*
             if (rlvid != null && !rlvid.equals("")) {
             String sql = "select tab.spc,post,join_id from ("
             + " select spc,join_id from emp_join where if_ad_charge='Y' and emp_id=?"
             + " UNION"
             + " SELECT SPC,0 join_id FROM EMP_RELIEVE WHERE RELIEVE_ID=?)tab"
             + " inner join g_spc on tab.spc=g_spc.spc"
             + " inner join g_post on g_spc.gpc=g_post.post_code"
             + " order by join_id";
             pst = con.prepareStatement(sql);
             pst.setString(1, empid);
             pst.setInt(2, Integer.parseInt(rlvid));
             rs = pst.executeQuery();
             while (rs.next()) {
             so = new SelectOption();
             String post = rs.getString("post");
             if (post != null && !post.equals("")) {
             if (rs.getInt("JOIN_ID") > 0) {
             so.setValue(rs.getString("SPC"));
             so.setLabel(post + " (ADDITIONAL CHARGE)");
             } else {
             so.setValue(rs.getString("SPC"));
             so.setLabel(post);
             }
             li.add(so);
             }
             li.add(so);
             }
             } else {
             String sql = "select tab.spc,post,join_id from ("
             + " select spc,join_id from emp_join where if_ad_charge='Y' and emp_id=?"
             + " UNION"
             + " SELECT CUR_SPC,0 join_id FROM  EMP_MAST WHERE EMP_ID=? and (cur_spc is not null or cur_spc<>''))tab"
             + " inner join g_spc on tab.spc=g_spc.spc"
             + " inner join g_post on g_spc.gpc=g_post.post_code"
             + " order by join_id";
             pst = con.prepareStatement(sql);
             pst.setString(1, empid);
             pst.setString(2, empid);
             rs = pst.executeQuery();
             while (rs.next()) {
             so = new SelectOption();
             String post = rs.getString("post");
             if (post != null && !post.equals("")) {
             if (rs.getInt("JOIN_ID") > 0) {
             so.setValue(rs.getString("SPC"));
             so.setLabel(post + " (ADDITIONAL CHARGE)");
             } else {
             so.setValue(rs.getString("SPC"));
             so.setLabel(post);
             }
             li.add(so);
             }
             }
             }
             */
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return li;
    }

    @Override
    public boolean saveRelieve(RelieveForm relieve, String entDeptCode, String entOffCode, String entSpc, String loginempid, String loginusername) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ResultSet rlvidrs = null;

        PreparedStatement pst1 = null;

        boolean updatePost = false;
        int retVal = 0;
        String spc = "";
        int joinid = 0;

        boolean updatestatus = true;
        try {
            con = this.dataSource.getConnection();

            /*if (relieve.getTransactionStatus() != null && relieve.getTransactionStatus().equals("C")) {
             spc = relieve.getSltRlvPost();
             } else if (relieve.getTransactionStatus() != null && relieve.getTransactionStatus().equals("S")) {
             spc = getSPC(relieve.getAuthHidGPC());
             }*/
            if (relieve.getSltRlvPost() != null && !relieve.getSltRlvPost().equals("")) {
                String[] rlvPostArr = relieve.getSltRlvPost().split("@");
                spc = rlvPostArr[0];
                joinid = Integer.parseInt(rlvPostArr[1]);
            } else if (relieve.getSltAddlCharge() != null && !relieve.getSltAddlCharge().equals("")) {
                String[] rlvPostArr = relieve.getSltAddlCharge().split("@");
                spc = rlvPostArr[0];
                joinid = Integer.parseInt(rlvPostArr[1]);
            }

            if (relieve.getRlvId() != null && !relieve.getRlvId().equals("")) {
                pst = con.prepareStatement("UPDATE emp_relieve SET memo_no=?,memo_date=?,rlv_date=?,rlv_time=?,spc=?,due_doj=?,due_toj=?,note=?,join_id=?,if_rlnq=?,toe=?,if_assumed=?,if_visible=?,ENT_DEPT=?,ENT_OFF=?,ENT_AUTH=?,entry_taken_by=?,entry_taken_on=? WHERE relieve_id=? AND EMP_ID=?");
                pst.setString(1, relieve.getTxtRlvOrdNo());
                if (relieve.getTxtRlvOrdDt() != null && !relieve.getTxtRlvOrdDt().equals("")) {
                    pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(relieve.getTxtRlvOrdDt()).getTime()));
                } else {
                    pst.setTimestamp(2, null);
                }
                if (relieve.getTxtRlvDt() != null && !relieve.getTxtRlvDt().equals("")) {
                    pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(relieve.getTxtRlvDt()).getTime()));
                } else {
                    pst.setTimestamp(3, null);
                }
                pst.setString(4, relieve.getSltRlvTime());
                //pst.setString(5, relieve.getSpc1());
                pst.setString(5, spc);
                if (relieve.getTxtJoinDt() != null && !relieve.getTxtJoinDt().equals("")) {
                    pst.setTimestamp(6, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(relieve.getTxtJoinDt()).getTime()));
                } else {
                    pst.setTimestamp(6, null);
                }
                pst.setString(7, relieve.getSltJoinTime());
                pst.setString(8, relieve.getNote());
                pst.setInt(9, joinid);
                pst.setString(10, relieve.getRdRqRl());
                //pst.setString(11, next);
                pst.setString(11, "");
                pst.setString(12, "");
                if (relieve.getChkSBPrint() != null && !relieve.getChkSBPrint().equals("")) {
                    pst.setString(13, relieve.getChkSBPrint());
                } else {
                    pst.setString(13, "Y");
                }
                pst.setString(14, entDeptCode);
                pst.setString(15, entOffCode);
                pst.setString(16, entSpc);
                pst.setString(17, loginempid);
                pst.setTimestamp(18, new Timestamp(new Date().getTime()));
                pst.setInt(19, Integer.parseInt(relieve.getRlvId()));
                pst.setString(20, relieve.getEmpId());
                retVal = pst.executeUpdate();

                String sqlString = ToStringBuilder.reflectionToString(pst);
                pst = con.prepareStatement("UPDATE emp_relieve SET query_string=? WHERE relieve_id=? AND EMP_ID=?");
                pst.setString(1, sqlString);
                pst.setInt(2, Integer.parseInt(relieve.getRlvId()));
                pst.setString(3, relieve.getEmpId());
                pst.executeUpdate();
            } else {
                String startTime = "";
                Calendar cal = Calendar.getInstance();
                DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
                startTime = dateFormat.format(cal.getTime());

                pst = con.prepareStatement("insert into emp_relieve(NOT_TYPE, NOT_ID, EMP_ID, DOE, MEMO_NO, MEMO_DATE, RLV_DATE, RLV_TIME, SPC, DUE_DOJ, DUE_TOJ, NOTE, JOIN_ID, IF_RLNQ, TOE, IF_ASSUMED,IF_VISIBLE,ENT_DEPT,ENT_OFF,ENT_AUTH,entry_taken_by,entry_taken_on) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                //relieve.setRlvId(maxRelieveIdDao.getMaxRelieveId());
                //pst.setString(1, relieve.getRlvId());
                pst.setString(1, relieve.getHidNotType());
                pst.setInt(2, relieve.getHidNotId());
                pst.setString(3, relieve.getEmpId());
                pst.setTimestamp(4, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(startTime).getTime()));
                pst.setString(5, relieve.getTxtRlvOrdNo());
                if (relieve.getTxtRlvOrdDt() != null && !relieve.getTxtRlvOrdDt().equals("")) {
                    pst.setTimestamp(6, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(relieve.getTxtRlvOrdDt()).getTime()));
                } else {
                    pst.setTimestamp(6, null);
                }
                if (relieve.getTxtRlvDt() != null && !relieve.getTxtRlvDt().equals("")) {
                    pst.setTimestamp(7, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(relieve.getTxtRlvDt()).getTime()));
                } else {
                    pst.setTimestamp(7, null);
                }
                pst.setString(8, relieve.getSltRlvTime());
                pst.setString(9, spc);
                if (relieve.getTxtJoinDt() != null && !relieve.getTxtJoinDt().equals("")) {
                    pst.setTimestamp(10, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(relieve.getTxtJoinDt()).getTime()));
                } else {
                    pst.setTimestamp(10, null);
                }
                pst.setString(11, relieve.getSltJoinTime());
                pst.setString(12, relieve.getNote());
                pst.setInt(13, joinid);
                pst.setString(14, relieve.getRdRqRl());
                //pst.setString(16, next);
                pst.setString(15, "");
                pst.setString(16, "");
                if (relieve.getChkSBPrint() != null && !relieve.getChkSBPrint().equals("")) {
                    pst.setString(17, relieve.getChkSBPrint());
                } else {
                    pst.setString(17, "Y");
                }
                pst.setString(18, entDeptCode);
                pst.setString(19, entOffCode);
                pst.setString(20, entSpc);
                pst.setString(21, loginempid);
                pst.setTimestamp(22, new Timestamp(new Date().getTime()));
                retVal = pst.executeUpdate();

                rlvidrs = pst.getGeneratedKeys();
                rlvidrs.next();
                int rlvid = rlvidrs.getInt("relieve_id");
                relieve.setRlvId(rlvid + "");

                String sqlString = ToStringBuilder.reflectionToString(pst);
                pst = con.prepareStatement("UPDATE emp_relieve SET query_string=? WHERE relieve_id=? AND EMP_ID=?");
                pst.setString(1, sqlString);
                pst.setInt(2, rlvid);
                pst.setString(3, relieve.getEmpId());
                pst.executeUpdate();
            }

            //if (relieve.getRdTransaction() != null && relieve.getRdTransaction().equals("C")) {
            if (relieve.getTransactionStatus() != null && relieve.getTransactionStatus().equals("C")) {
                pst = con.prepareStatement("UPDATE EMP_MAST SET CUR_SPC=?, POST_ORDER_DATE=?, CURR_POST_DOJ=?,NEXT_OFFICE_CODE=? WHERE EMP_ID=?");
                pst.setString(1, null);
                if (relieve.getTxtRlvDt() != null && !relieve.getTxtRlvDt().equals("")) {
                    pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(relieve.getTxtRlvDt()).getTime()));
                } else {
                    pst.setTimestamp(2, null);
                }
                if (relieve.getTxtRlvOrdDt() != null && !relieve.getTxtRlvOrdDt().equals("")) {
                    pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(relieve.getTxtRlvOrdDt()).getTime()));
                } else {
                    pst.setTimestamp(3, null);
                }
                pst.setString(4, relieve.getHidNextOfficeCode());
                pst.setString(5, relieve.getEmpId());
                pst.executeUpdate();

                String sqlString = ToStringBuilder.reflectionToString(pst);
                sbDAO.createTransactionLog(loginusername, relieve.getEmpId(), "RELIEVE", sqlString);

                if (relieve.getHidNotType() != null && relieve.getHidNotType().equals("LEAVE")) {
                    CommonFunctions.modifyEmpCurStatus(relieve.getEmpId(), "ON LEAVE", con);
                } else if (relieve.getHidNotType() != null && relieve.getHidNotType().equals("RETIREMENT")) {
                    CommonFunctions.modifyEmpCurStatus(relieve.getEmpId(), "SUPERANNUATED", con);
                } else {
                    CommonFunctions.modifyEmpCurStatus(relieve.getEmpId(), "ON TRANSIT", con);
                }
            }
            //}

            if (relieve.getChkNotSBPrint() != null && relieve.getChkNotSBPrint().equals("Y")) {
                pst = con.prepareStatement("UPDATE EMP_RELIEVE SET if_visible='N' WHERE RELIEVE_ID=?");
                pst.setInt(1, Integer.parseInt(relieve.getRlvId()));
                pst.executeUpdate();
            } else {
                String sbLanguage = sbDAO.getRelieveDetails(relieve.getHidNotId(), relieve.getEmpId());
                pst = con.prepareStatement("UPDATE EMP_RELIEVE SET SB_DESCRIPTION =?,if_visible='Y' WHERE RELIEVE_ID=?");
                pst.setString(1, sbLanguage);
                pst.setInt(2, Integer.parseInt(relieve.getRlvId()));
                pst.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(pst1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return updatestatus;
    }

    @Override
    public void deleteRelieve(String empid, RelieveForm erelieve) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String joiningId = "";
        String joining_offCode = "";
        String joining_spc = "";

        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("SELECT OFF_CODE,EMP_JOIN.SPC,EMP_RELIEVE.JOIN_ID FROM (SELECT JOIN_ID FROM EMP_RELIEVE WHERE RELIEVE_ID=?)EMP_RELIEVE"
                    + " INNER JOIN EMP_JOIN ON EMP_RELIEVE.JOIN_ID=EMP_JOIN.JOIN_ID"
                    + " INNER JOIN G_SPC ON EMP_JOIN.SPC=G_SPC.SPC");
            pst.setInt(1, Integer.parseInt(erelieve.getRlvId()));
            rs = pst.executeQuery();
            if (rs.next()) {
                joiningId = rs.getString("JOIN_ID");
                joining_offCode = rs.getString("OFF_CODE");
                joining_spc = rs.getString("SPC");
            }

            if (joiningId != null && !joiningId.equals("")) {
                pst = con.prepareStatement("UPDATE EMP_MAST SET CUR_OFF_CODE=?,CUR_SPC=? WHERE EMP_ID=?");
                pst.setString(1, joining_offCode);
                pst.setString(2, joining_spc);
                pst.setString(3, empid);
                pst.executeUpdate();
            }

            pst = con.prepareStatement("DELETE FROM EMP_RELIEVE WHERE relieve_id=?");
            pst.setInt(1, Integer.parseInt(erelieve.getRlvId()));
            pst.executeUpdate();

            CommonFunctions.modifyEmpCurStatus(erelieve.getEmpId(), "ON DUTY", con);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getAddlChargeList(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List li = new ArrayList();
        SelectOption so = null;
        try {
            con = this.dataSource.getConnection();

            //String sql = "SELECT SPC,JOIN_ID FROM EMP_JOIN WHERE emp_id=? and if_ad_charge='Y' and JOIN_ID NOT IN (select join_id from emp_relieve where emp_id=? and join_id is not null)";
            /*String sql = "SELECT EMP_JOIN.SPC,SPN,JOIN_ID FROM (SELECT SPC,JOIN_ID FROM EMP_JOIN WHERE emp_id=? and if_ad_charge='Y' and JOIN_ID NOT IN"
             + " (select join_id from emp_relieve where emp_id=? and join_id is not null))emp_join"
             + " left outer join g_spc on emp_join.spc=g_spc.spn";*/
            String sql = "SELECT EMP_JOIN.SPC,SPN,JOIN_ID FROM (SELECT SPC,JOIN_ID FROM EMP_JOIN WHERE emp_id=? and if_ad_charge='Y' and JOIN_ID::TEXT NOT IN"
                    + " (select join_id::TEXT from emp_relieve where emp_id=? and join_id::TEXT is not null))emp_join"
                    + " left outer join g_spc on emp_join.spc=g_spc.spc";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setString(2, empid);
            rs = pst.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("SPC") + "@" + rs.getString("JOIN_ID"));
                so.setLabel(rs.getString("SPN"));
                li.add(so);
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
    public boolean getUpdateStatus(String empid, int notid, String nottype) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String nextofficecode = "";
        boolean updatestatus = true;
        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("select NEXT_OFFICE_CODE from EMP_MAST where EMP_ID=?");
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                nextofficecode = rs.getString("NEXT_OFFICE_CODE");
            }
            if (nextofficecode == null || nextofficecode.equals("")) {
                updatestatus = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return updatestatus;
    }

    private String getSPC(String gpc) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String spc = "";
        try {
            con = this.dataSource.getConnection();

            if (gpc != null && !gpc.equals("")) {
                String sql = "select spc,gpc,spn from g_spc where gpc=? and is_sanctioned='Y' limit 1";
                pst = con.prepareStatement(sql);
                pst.setString(1, gpc);
                rs = pst.executeQuery();
                if (rs.next()) {
                    spc = rs.getString("spc");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return spc;
    }

    private RelieveForm getTransactionStatus(RelieveForm erelieve) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String nextofficecode = "";

        String updatestatus = "";
        try {
            con = this.dataSource.getConnection();

            String empid = erelieve.getEmpId();

            pst = con.prepareStatement("select NEXT_OFFICE_CODE from emp_mast where EMP_ID=?");
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                nextofficecode = rs.getString("NEXT_OFFICE_CODE");
            }

            erelieve.setHidNextOfficeCode(nextofficecode);

            if (erelieve.getHidNextOfficeCode() != null && !erelieve.getHidNextOfficeCode().equals("")) {
                updatestatus = "C";
            } else {
                updatestatus = "S";
            }
            erelieve.setTransactionStatus(updatestatus);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return erelieve;
    }

    @Override
    public String saveRelieveSBCorrection(RelieveForm relieve, String entDeptCode, String entOffCode, String entSpc, String loginempid, String loginusername, int refcorrectionid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ResultSet rlvidrs = null;

        PreparedStatement pst1 = null;

        String spc = "";
        int joinid = 0;

        boolean updatestatus = true;
        int retVal = 0;

        String sbLanguage = "";
        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("DELETE FROM EMP_RELIEVE_SB_CORRECTION WHERE relieve_id=? AND EMP_ID=?");
            pst.setInt(1, Integer.parseInt(relieve.getRlvId()));
            pst.setString(2, relieve.getEmpId());
            pst.executeUpdate();

            if (relieve.getSltRlvPost() != null && !relieve.getSltRlvPost().equals("")) {
                String[] rlvPostArr = relieve.getSltRlvPost().split("@");
                spc = rlvPostArr[0];
                joinid = Integer.parseInt(rlvPostArr[1]);
            } else if (relieve.getSltAddlCharge() != null && !relieve.getSltAddlCharge().equals("")) {
                String[] rlvPostArr = relieve.getSltAddlCharge().split("@");
                spc = rlvPostArr[0];
                joinid = Integer.parseInt(rlvPostArr[1]);
            }

            String startTime = "";
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            startTime = dateFormat.format(cal.getTime());

            pst = con.prepareStatement("insert into EMP_RELIEVE_SB_CORRECTION(NOT_TYPE, NOT_ID, EMP_ID, DOE, MEMO_NO, MEMO_DATE, RLV_DATE, RLV_TIME, SPC, DUE_DOJ, DUE_TOJ, NOTE, JOIN_ID, IF_RLNQ, TOE, IF_ASSUMED,IF_VISIBLE,ENT_DEPT,ENT_OFF,ENT_AUTH,entry_taken_by,entry_taken_on,ref_correction_id,relieve_id) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, relieve.getHidNotType());
            pst.setInt(2, relieve.getHidNotId());
            pst.setString(3, relieve.getEmpId());
            pst.setTimestamp(4, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(startTime).getTime()));
            pst.setString(5, relieve.getTxtRlvOrdNo());
            if (relieve.getTxtRlvOrdDt() != null && !relieve.getTxtRlvOrdDt().equals("")) {
                pst.setTimestamp(6, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(relieve.getTxtRlvOrdDt()).getTime()));
            } else {
                pst.setTimestamp(6, null);
            }
            if (relieve.getTxtRlvDt() != null && !relieve.getTxtRlvDt().equals("")) {
                pst.setTimestamp(7, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(relieve.getTxtRlvDt()).getTime()));
            } else {
                pst.setTimestamp(7, null);
            }
            pst.setString(8, relieve.getSltRlvTime());
            pst.setString(9, spc);
            if (relieve.getTxtJoinDt() != null && !relieve.getTxtJoinDt().equals("")) {
                pst.setTimestamp(10, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(relieve.getTxtJoinDt()).getTime()));
            } else {
                pst.setTimestamp(10, null);
            }
            pst.setString(11, relieve.getSltJoinTime());
            pst.setString(12, relieve.getNote());
            pst.setInt(13, joinid);
            pst.setString(14, relieve.getRdRqRl());
            pst.setString(15, "");
            pst.setString(16, "");
            if (relieve.getChkSBPrint() != null && !relieve.getChkSBPrint().equals("")) {
                pst.setString(17, relieve.getChkSBPrint());
            } else {
                pst.setString(17, "Y");
            }
            pst.setString(18, entDeptCode);
            pst.setString(19, entOffCode);
            pst.setString(20, entSpc);
            pst.setString(21, loginempid);
            pst.setTimestamp(22, new Timestamp(new Date().getTime()));
            pst.setInt(23, refcorrectionid);
            pst.setInt(24, Integer.parseInt(relieve.getRlvId()));
            retVal = pst.executeUpdate();

            rlvidrs = pst.getGeneratedKeys();
            rlvidrs.next();
            int rlvid = rlvidrs.getInt("relieve_id");
            relieve.setRlvId(rlvid + "");

            String sqlString = ToStringBuilder.reflectionToString(pst);
            pst = con.prepareStatement("UPDATE EMP_RELIEVE_SB_CORRECTION SET query_string=? WHERE relieve_id=? AND EMP_ID=?");
            pst.setString(1, sqlString);
            pst.setInt(2, rlvid);
            pst.setString(3, relieve.getEmpId());
            pst.executeUpdate();

            if (relieve.getChkNotSBPrint() != null && relieve.getChkNotSBPrint().equals("Y")) {
                pst = con.prepareStatement("UPDATE EMP_RELIEVE_SB_CORRECTION SET if_visible='N' WHERE RELIEVE_ID=?");
                pst.setInt(1, Integer.parseInt(relieve.getRlvId()));
                pst.executeUpdate();
            } else {
                sbLanguage = sbDAO.getRelieveDetails(relieve.getHidNotId(), relieve.getEmpId());
                pst = con.prepareStatement("UPDATE EMP_RELIEVE_SB_CORRECTION SET SB_DESCRIPTION =?,if_visible='Y' WHERE RELIEVE_ID=?");
                pst.setString(1, sbLanguage);
                pst.setInt(2, Integer.parseInt(relieve.getRlvId()));
                pst.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(pst1);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return sbLanguage;
    }

    @Override
    public RelieveForm getRelieveDataSBCorrectionDDO(String empid, int notid, String rlvid,int correctionid) {

        Connection con = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        RelieveForm el = null;
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT EMP_NOTIFICATION.AUTH,EMP_NOTIFICATION.OFF_CODE,EMP_NOTIFICATION.DEPT_CODE,NOT_ID,NOT_TYPE,ORDNO,ORDDT,DEPARTMENT_NAME,OFF_EN,SPN FROM EMP_NOTIFICATION "
                    + "LEFT OUTER JOIN G_DEPARTMENT ON EMP_NOTIFICATION.DEPT_CODE =  G_DEPARTMENT.DEPARTMENT_CODE "
                    + "LEFT OUTER JOIN G_OFFICE ON EMP_NOTIFICATION.OFF_CODE =  G_OFFICE.OFF_CODE "
                    + "LEFT OUTER JOIN G_SPC ON EMP_NOTIFICATION.AUTH =  G_SPC.SPC WHERE NOT_ID=? AND EMP_ID=?");
            pstmt.setInt(1, notid);
            pstmt.setString(2, empid);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                el = new RelieveForm();
                el.setEmpId(empid);
                el.setHidNotId(rs.getInt("NOT_ID"));
                el.setHidNotType(rs.getString("NOT_TYPE").toUpperCase());
                el.setOrdNo(rs.getString("ORDNO"));
                if (rs.getDate("ORDDT") != null && !rs.getDate("ORDDT").equals("")) {
                    el.setOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("ORDDT")).toUpperCase());
                }
                if (rs.getString("DEPT_CODE") != null && !rs.getString("DEPT_CODE").trim().equals("")) {
                    el.setDeptname(rs.getString("DEPARTMENT_NAME"));
                }
                if (rs.getString("OFF_CODE") != null && !rs.getString("OFF_CODE").trim().equals("")) {
                    el.setOffname(rs.getString("OFF_EN"));
                }
                if (rs.getString("AUTH") != null && !rs.getString("AUTH").trim().equals("")) {
                    el.setPostname(rs.getString("SPN"));
                }
            }
            if (el.getEmpId() != null && !el.getEmpId().equals("")) {
                getTransactionStatus(el);
            }
            if (el.getHidNotType() != null && !el.getHidNotType().equals("")) {
                if (el.getHidNotType().equals("RETIREMENT") || el.getHidNotType().equals("RESIGNATION") || el.getHidNotType().equals("DECEASED")) {
                    el.setTransactionStatus("C");
                }
            }
            if (rlvid != null && !rlvid.equals("")) {
                pstmt = con.prepareStatement("SELECT EMP_RELIEVE.*,G_OFFICE.DEPARTMENT_CODE,DIST_CODE,G_SPC.OFF_CODE,GPC,POST,EMP_RELIEVE.SPC FROM EMP_RELIEVE_SB_CORRECTION EMP_RELIEVE"
                        + " LEFT OUTER JOIN G_SPC ON EMP_RELIEVE.SPC=G_SPC.SPC"
                        + " LEFT OUTER JOIN G_OFFICE ON G_SPC.OFF_CODE=G_OFFICE.OFF_CODE"
                        + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE"
                        + " WHERE RELIEVE_ID=? AND EMP_ID=? AND EMP_RELIEVE.REF_CORRECTION_ID=?");
                pstmt.setInt(1, Integer.parseInt(rlvid));
                pstmt.setString(2, empid);
                pstmt.setInt(3, correctionid);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    el.setRlvId(rlvid);
                    if (rs.getString("if_visible") != null && rs.getString("if_visible").equals("N")) {
                        el.setChkNotSBPrint("Y");
                    } else {
                        el.setChkNotSBPrint("N");
                    }
                    el.setTxtRlvOrdNo(rs.getString("MEMO_NO"));
                    if (rs.getDate("MEMO_DATE") != null && !rs.getDate("MEMO_DATE").equals("")) {
                        el.setTxtRlvOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("MEMO_DATE")).toUpperCase());
                    }
                    if (rs.getDate("RLV_DATE") != null && !rs.getDate("RLV_DATE").equals("")) {
                        el.setTxtRlvDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("RLV_DATE")).toUpperCase());
                    }
                    el.setSltRlvTime(rs.getString("RLV_TIME"));
                    el.setChkSBPrint(rs.getString("IF_VISIBLE"));
                    if (rs.getDate("DUE_DOJ") != null && !rs.getDate("DUE_DOJ").equals("")) {
                        el.setTxtJoinDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("DUE_DOJ")).toUpperCase());
                    }
                    el.setSltJoinTime(rs.getString("DUE_TOJ"));
                    el.setRdRqRl(rs.getString("if_rlnq"));
                    if (rs.getString("SPC") != null && !rs.getString("SPC").equals("")) {
                        el.setHidRlvSpc(rs.getString("SPC"));
                        el.setSltRlvPost(rs.getString("SPC") + "@" + rs.getString("JOIN_ID"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return el;
    }

    @Override
    public void approveRelieveDataSBCorrection(RelieveForm relieve, String entDeptCode, String entOffCode, String entSpc, String loginempid, String loginusername) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        String spc = "";
        int joinid = 0;

        ResultSet rlvidrs = null;
        try {
            con = this.dataSource.getConnection();

            /*if (relieve.getEntrytypeSBCorrection() != null && relieve.getEntrytypeSBCorrection().equals("NEW")) {
             if (relieve.getSltRlvPost() != null && !relieve.getSltRlvPost().equals("")) {
             String[] rlvPostArr = relieve.getSltRlvPost().split("@");
             spc = rlvPostArr[0];
             joinid = Integer.parseInt(rlvPostArr[1]);
             } else if (relieve.getSltAddlCharge() != null && !relieve.getSltAddlCharge().equals("")) {
             String[] rlvPostArr = relieve.getSltAddlCharge().split("@");
             spc = rlvPostArr[0];
             joinid = Integer.parseInt(rlvPostArr[1]);
             }

             String startTime = "";
             Calendar cal = Calendar.getInstance();
             DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
             startTime = dateFormat.format(cal.getTime());

             pst = con.prepareStatement("insert into emp_relieve(NOT_TYPE, NOT_ID, EMP_ID, DOE, MEMO_NO, MEMO_DATE, RLV_DATE, RLV_TIME, SPC, DUE_DOJ, DUE_TOJ, NOTE, JOIN_ID, IF_RLNQ, TOE, IF_ASSUMED,IF_VISIBLE,ENT_DEPT,ENT_OFF,ENT_AUTH,entry_taken_by,entry_taken_on) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
             pst.setString(1, relieve.getHidNotType());
             pst.setInt(2, relieve.getHidNotId());
             pst.setString(3, relieve.getEmpId());
             pst.setTimestamp(4, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(startTime).getTime()));
             pst.setString(5, relieve.getTxtRlvOrdNo());
             if (relieve.getTxtRlvOrdDt() != null && !relieve.getTxtRlvOrdDt().equals("")) {
             pst.setTimestamp(6, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(relieve.getTxtRlvOrdDt()).getTime()));
             } else {
             pst.setTimestamp(6, null);
             }
             if (relieve.getTxtRlvDt() != null && !relieve.getTxtRlvDt().equals("")) {
             pst.setTimestamp(7, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(relieve.getTxtRlvDt()).getTime()));
             } else {
             pst.setTimestamp(7, null);
             }
             pst.setString(8, relieve.getSltRlvTime());
             pst.setString(9, spc);
             if (relieve.getTxtJoinDt() != null && !relieve.getTxtJoinDt().equals("")) {
             pst.setTimestamp(10, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(relieve.getTxtJoinDt()).getTime()));
             } else {
             pst.setTimestamp(10, null);
             }
             pst.setString(11, relieve.getSltJoinTime());
             pst.setString(12, relieve.getNote());
             pst.setInt(13, joinid);
             pst.setString(14, relieve.getRdRqRl());
             pst.setString(15, "");
             pst.setString(16, "");
             if (relieve.getChkSBPrint() != null && !relieve.getChkSBPrint().equals("")) {
             pst.setString(17, relieve.getChkSBPrint());
             } else {
             pst.setString(17, "Y");
             }
             pst.setString(18, entDeptCode);
             pst.setString(19, entOffCode);
             pst.setString(20, entSpc);
             pst.setString(21, loginempid);
             pst.setTimestamp(22, new Timestamp(new Date().getTime()));
             pst.executeUpdate();

             rlvidrs = pst.getGeneratedKeys();
             rlvidrs.next();
             int rlvid = rlvidrs.getInt("relieve_id");
             relieve.setRlvId(rlvid + "");

             String sqlString = ToStringBuilder.reflectionToString(pst);
             pst = con.prepareStatement("UPDATE emp_relieve SET query_string=? WHERE relieve_id=? AND EMP_ID=?");
             pst.setString(1, sqlString);
             pst.setInt(2, rlvid);
             pst.setString(3, relieve.getEmpId());
             pst.executeUpdate();

             if (relieve.getTransactionStatus() != null && relieve.getTransactionStatus().equals("C")) {
             pst = con.prepareStatement("UPDATE EMP_MAST SET CUR_SPC=?, POST_ORDER_DATE=?, CURR_POST_DOJ=?,NEXT_OFFICE_CODE=? WHERE EMP_ID=?");
             pst.setString(1, null);
             if (relieve.getTxtRlvDt() != null && !relieve.getTxtRlvDt().equals("")) {
             pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(relieve.getTxtRlvDt()).getTime()));
             } else {
             pst.setTimestamp(2, null);
             }
             if (relieve.getTxtRlvOrdDt() != null && !relieve.getTxtRlvOrdDt().equals("")) {
             pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(relieve.getTxtRlvOrdDt()).getTime()));
             } else {
             pst.setTimestamp(3, null);
             }
             pst.setString(4, relieve.getHidNextOfficeCode());
             pst.setString(5, relieve.getEmpId());
             pst.executeUpdate();

             sqlString = ToStringBuilder.reflectionToString(pst);
             sbDAO.createTransactionLog(loginusername, relieve.getEmpId(), "RELIEVE", sqlString);

             if (relieve.getHidNotType() != null && relieve.getHidNotType().equals("LEAVE")) {
             CommonFunctions.modifyEmpCurStatus(relieve.getEmpId(), "ON LEAVE", con);
             } else if (relieve.getHidNotType() != null && relieve.getHidNotType().equals("RETIREMENT")) {
             CommonFunctions.modifyEmpCurStatus(relieve.getEmpId(), "SUPERANNUATED", con);
             } else {
             CommonFunctions.modifyEmpCurStatus(relieve.getEmpId(), "ON TRANSIT", con);
             }
             }

             if (relieve.getChkNotSBPrint() != null && relieve.getChkNotSBPrint().equals("Y")) {
             pst = con.prepareStatement("UPDATE EMP_RELIEVE SET if_visible='N' WHERE RELIEVE_ID=?");
             pst.setInt(1, Integer.parseInt(relieve.getRlvId()));
             pst.executeUpdate();
             } else {
             String sbLanguage = sbDAO.getRelieveDetails(relieve.getHidNotId(), relieve.getEmpId());
             pst = con.prepareStatement("UPDATE EMP_RELIEVE SET SB_DESCRIPTION =?,if_visible='Y' WHERE RELIEVE_ID=?");
             pst.setString(1, sbLanguage);
             pst.setInt(2, Integer.parseInt(relieve.getRlvId()));
             pst.executeUpdate();
             }
             } else {*/
            if (relieve.getRlvId() != null && !relieve.getRlvId().equals("")) {
                pst = con.prepareStatement("UPDATE EMP_RELIEVE_SB_CORRECTION SET memo_no=?,memo_date=?,rlv_date=?,rlv_time=?,spc=?,due_doj=?,due_toj=?,note=?,join_id=?,if_rlnq=?,toe=?,if_assumed=?,if_visible=?,ENT_DEPT=?,ENT_OFF=?,ENT_AUTH=?,entry_taken_by=?,entry_taken_on=? WHERE relieve_id=? AND EMP_ID=?");
                pst.setString(1, relieve.getTxtRlvOrdNo());
                if (relieve.getTxtRlvOrdDt() != null && !relieve.getTxtRlvOrdDt().equals("")) {
                    pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(relieve.getTxtRlvOrdDt()).getTime()));
                } else {
                    pst.setTimestamp(2, null);
                }
                if (relieve.getTxtRlvDt() != null && !relieve.getTxtRlvDt().equals("")) {
                    pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(relieve.getTxtRlvDt()).getTime()));
                } else {
                    pst.setTimestamp(3, null);
                }
                pst.setString(4, relieve.getSltRlvTime());
                //pst.setString(5, relieve.getSpc1());
                pst.setString(5, spc);
                if (relieve.getTxtJoinDt() != null && !relieve.getTxtJoinDt().equals("")) {
                    pst.setTimestamp(6, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(relieve.getTxtJoinDt()).getTime()));
                } else {
                    pst.setTimestamp(6, null);
                }
                pst.setString(7, relieve.getSltJoinTime());
                pst.setString(8, relieve.getNote());
                pst.setInt(9, joinid);
                pst.setString(10, relieve.getRdRqRl());
                //pst.setString(11, next);
                pst.setString(11, "");
                pst.setString(12, "");
                if (relieve.getChkSBPrint() != null && !relieve.getChkSBPrint().equals("")) {
                    pst.setString(13, relieve.getChkSBPrint());
                } else {
                    pst.setString(13, "Y");
                }
                pst.setString(14, entDeptCode);
                pst.setString(15, entOffCode);
                pst.setString(16, entSpc);
                pst.setString(17, loginempid);
                pst.setTimestamp(18, new Timestamp(new Date().getTime()));
                pst.setInt(19, Integer.parseInt(relieve.getRlvId()));
                pst.setString(20, relieve.getEmpId());
                pst.executeUpdate();

                String sql = "UPDATE EMP_RELIEVE SET memo_no=EMP_RELIEVE_SB_CORRECTION.memo_no,memo_date=EMP_RELIEVE_SB_CORRECTION.memo_date,rlv_date=EMP_RELIEVE_SB_CORRECTION.rlv_date,rlv_time=EMP_RELIEVE_SB_CORRECTION.rlv_time,spc=EMP_RELIEVE_SB_CORRECTION.spc,due_doj=EMP_RELIEVE_SB_CORRECTION.due_doj,due_toj=EMP_RELIEVE_SB_CORRECTION.due_toj,note=EMP_RELIEVE_SB_CORRECTION.note,join_id=EMP_RELIEVE_SB_CORRECTION.join_id,if_rlnq=EMP_RELIEVE_SB_CORRECTION.if_rlnq,if_assumed=EMP_RELIEVE_SB_CORRECTION.if_assumed,if_visible=EMP_RELIEVE_SB_CORRECTION.if_visible,entry_taken_by=EMP_RELIEVE_SB_CORRECTION.entry_taken_by,entry_taken_on=EMP_RELIEVE_SB_CORRECTION.entry_taken_on FROM EMP_RELIEVE_SB_CORRECTION WHERE EMP_RELIEVE.relieve_id = EMP_RELIEVE_SB_CORRECTION.relieve_id AND EMP_RELIEVE_SB_CORRECTION.relieve_id=? AND EMP_RELIEVE_SB_CORRECTION.EMP_ID=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(relieve.getRlvId()));
                pst.setString(2, relieve.getEmpId());
                pst.executeUpdate();
                
                String sbLanguage = sbDAO.getRelieveDetails(relieve.getHidNotId(), relieve.getEmpId());
                pst = con.prepareStatement("UPDATE EMP_RELIEVE SET SB_DESCRIPTION=?,if_visible='Y' WHERE RELIEVE_ID=?");
                pst.setString(1, sbLanguage);
                pst.setInt(2, Integer.parseInt(relieve.getRlvId()));
                pst.executeUpdate();
            }
            //}
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }
}
