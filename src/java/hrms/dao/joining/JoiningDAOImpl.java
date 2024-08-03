package hrms.dao.joining;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.common.Message;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.model.joining.JoiningForm;
import hrms.model.joining.JoiningList;
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
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

public class JoiningDAOImpl implements JoiningDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    protected MaxJoiningIdDAO maxJoiningIdDAO;

    protected ServiceBookLanguageDAO sbDAO;

    public void setSbDAO(ServiceBookLanguageDAO sbDAO) {
        this.sbDAO = sbDAO;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setMaxJoiningIdDAO(MaxJoiningIdDAO maxJoiningIdDao) {
        this.maxJoiningIdDAO = maxJoiningIdDao;
    }

    @Override
    public List getJoiningList(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List jlist = new ArrayList();
        JoiningList jbean = null;
        try {
            con = this.dataSource.getConnection();
            String sql = "select * from (select TEMP.* from (select joi.is_validated,joi.modified_by,joi.modified_on,joi.if_visible,temp.sv_id,temp.ntype tntype,temp.notid tnid,temp.nordno tnordno,temp.nordt"
                    + " tnordt,temp.nauth tnauth,temp.notnote tnnote,"
                    + " temp.ndeptcode tndeptcode,temp.noffcode tnoffcode,temp.rid trid,temp.rmemono trmemono,"
                    + " temp.rmemodt trmemodt,temp.rdate trdate,temp.rtime trtime,temp.rdoj trdoj,temp.rtoj trtoj,joi.join_id joinid,joi.not_type jnotype,"
                    + " joi.not_id jnotid,joi.doe jdoe,joi.memo_no jmemono,joi.memo_date jmemodt,joi.join_date joindt,joi.join_time jointime,joi.spc jspc,"
                    + " joi.note jnote,joi.if_ad_charge jaddcharge,joi.lcr_id lcrid,joi.tr_data_type jointrtype,joi.sv_id svid from (SELECT noti.sv_id,"
                    + " noti.not_id notid,noti.not_type ntype,noti.emp_id nempid,"
                    + " noti.doe ndoe,noti.ordno nordno,noti.orddt nordt,noti.dept_code ndeptcode,noti.off_code noffcode,noti.auth nauth,noti.note notnote,"
                    + " reli.relieve_id rid,reli.doe rdoe,reli.memo_no rmemono,reli.memo_date rmemodt,reli.rlv_date rdate,reli.rlv_time rtime,"
                    + " reli.spc rspc,reli.due_doj rdoj,reli.due_toj rtoj FROM (SELECT * FROM emp_notification WHERE EMP_ID='" + empid + "') noti"
                    + " left outer join (Select * from emp_relieve where emp_id='" + empid + "') reli on noti.emp_id=reli.emp_id and"
                    + " noti.not_id=reli.not_id and reli.join_id not in (Select Join_id from emp_join where EMP_ID='" + empid + "' AND if_ad_charge='Y'))"
                    + " temp left outer join emp_join  joi on temp.nempid=joi.emp_id and temp.notid=joi.not_id WHERE temp.nempid='" + empid + "' and"
                    + " (temp.ntype='FIRST_APPOINTMENT' or temp.ntype='REHABILITATION' or temp.ntype='ABSORPTION' or temp.ntype='REDEPLOYMENT' or"
                    + " temp.ntype='VALIDATION' or temp.ntype='LT_TRAINING' or temp.ntype='DEPUTATION' or temp.ntype='POSTING' or temp.ntype='TRANSFER'"
                    + " or temp.ntype='PROMOTION' or temp.ntype='ALLOT_CADRE' or temp.ntype='REDESIGNATION' or"
                    + " temp.ntype='CHNG_STRUCTURE') and"
                    + " (temp.notid not in (select not_id from emp_cadre where emp_cadre.not_id=temp.notid and joined_assuch='Y' and EMP_ID='" + empid + "'))"
                    + " order by joindt desc) TEMP)jointemp order by tnordt desc ";

            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                jbean = new JoiningList();
                jbean.setNotid(rs.getString("tnid"));
                jbean.setNotType(rs.getString("tntype"));
                jbean.setNotOrdNo(rs.getString("tnordno"));
                if (rs.getDate("tnordt") != null && !rs.getDate("tnordt").equals("")) {
                    jbean.setNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("tnordt")));
                }
                if (rs.getString("trid") != null && !rs.getString("trid").equals("")) {
                    jbean.setRlvid(rs.getString("trid"));
                } else {
                    jbean.setRlvid("");
                }
                if (rs.getString("joinid") != null && !rs.getString("joinid").equals("")) {
                    jbean.setJoinid(rs.getString("joinid"));
                } else {
                    jbean.setJoinid("");
                }
                if (rs.getDate("trdoj") != null && !rs.getDate("trdoj").equals("")) {
                    jbean.setJoiningDueDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("trdoj")));
                }
                jbean.setJoiningDueTime(rs.getString("trtoj"));
                jbean.setJoiningOrdNo(rs.getString("jmemono"));
                if (rs.getDate("jmemodt") != null && !rs.getDate("jmemodt").equals("")) {
                    jbean.setJoiningOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("jmemodt")));
                }
                if (rs.getDate("joindt") != null && !rs.getDate("joindt").equals("")) {
                    jbean.setJoiningDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("joindt")));
                }
                jbean.setJoiningTime(rs.getString("jointime"));
                if (rs.getString("lcrid") != null && !rs.getString("lcrid").equals("")) {
                    jbean.setLcrid(rs.getString("lcrid"));
                } else {
                    jbean.setLcrid("");
                }
                if (rs.getString("jaddcharge") != null && !rs.getString("jaddcharge").equals("")) {
                    jbean.setAdditionalCharge(rs.getString("jaddcharge"));
                } else {
                    jbean.setAdditionalCharge("");
                }
                jbean.setPrintInServiceBook(rs.getString("if_visible"));

                jbean.setModifiedby(StringUtils.defaultString(rs.getString("modified_by")) + "<br />" + StringUtils.defaultString(CommonFunctions.getFormattedOutputDate1(rs.getDate("modified_on"))));

                jbean.setIsValidated(rs.getString("is_validated"));

                jlist.add(jbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return jlist;
    }

    public String getOtherSpn(String othSpc) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        String spn = "";
        try {
            con = this.dataSource.getConnection();

            if (othSpc != null && !othSpc.equals("")) {
                pst = con.prepareStatement("SELECT oth_spc, off_en FROM g_oth_spc WHERE other_spc_id=? and is_active='Y'");
                pst.setInt(1, Integer.parseInt(othSpc));
                rs = pst.executeQuery();
                if (rs.next()) {
                    spn = rs.getString("off_en");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return spn;
    }

    @Override
    public int getJoiningListCount(String empid) {

        Connection con = null;

        Statement stmt = null;
        ResultSet rs = null;

        int count = 0;
        try {
            con = this.dataSource.getConnection();

            String sql = "select count(*) cnt from (select * from (select TEMP.* from (select temp.sv_id,temp.ntype tntype,temp.notid tnid,temp.nordno tnordno,temp.nordt"
                    + " tnordt,temp.nauth tnauth,temp.notnote tnnote,"
                    + " temp.ndeptcode tndeptcode,temp.noffcode tnoffcode,temp.rid trid,temp.rmemono trmemono,"
                    + " temp.rmemodt trmemodt,temp.rdate trdate,temp.rtime trtime,temp.rdoj trdoj,temp.rtoj trtoj,joi.join_id joinid,joi.not_type jnotype,"
                    + " joi.not_id jnotid,joi.doe jdoe,joi.memo_no jmemono,joi.memo_date jmemodt,joi.join_date joindt,joi.join_time jointime,joi.spc jspc,"
                    + " joi.note jnote,joi.if_ad_charge jaddcharge,joi.lcr_id lcrid,joi.tr_data_type jointrtype,joi.sv_id svid from (SELECT noti.sv_id,"
                    + " noti.not_id notid,noti.not_type ntype,noti.emp_id nempid,"
                    + " noti.doe ndoe,noti.ordno nordno,noti.orddt nordt,noti.dept_code ndeptcode,noti.off_code noffcode,noti.auth nauth,noti.note notnote,"
                    + " reli.relieve_id rid,reli.doe rdoe,reli.memo_no rmemono,reli.memo_date rmemodt,reli.rlv_date rdate,reli.rlv_time rtime,"
                    + " reli.spc rspc,reli.due_doj rdoj,reli.due_toj rtoj FROM (SELECT * FROM emp_notification WHERE EMP_ID='" + empid + "') noti"
                    + " left outer join (Select * from emp_relieve where emp_id='" + empid + "') reli on noti.emp_id=reli.emp_id and"
                    + " noti.not_id=reli.not_id and reli.join_id not in (Select Join_id from emp_join where EMP_ID='" + empid + "' AND if_ad_charge='Y'))"
                    + " temp left outer join emp_join  joi on temp.nempid=joi.emp_id and temp.notid=joi.not_id WHERE temp.nempid='" + empid + "' and"
                    + " (temp.ntype='FIRST_APPOINTMENT' or temp.ntype='REHABILITATION' or temp.ntype='ABSORPTION' or temp.ntype='REDEPLOYMENT' or"
                    + " temp.ntype='VALIDATION' or temp.ntype='LT_TRAINING' or temp.ntype='DEPUTATION' or temp.ntype='POSTING' or temp.ntype='TRANSFER'"
                    + " or temp.ntype='PROMOTION' or temp.ntype='ADDITIONAL_CHARGE' or temp.ntype='ALLOT_CADRE' or temp.ntype='REDESIGNATION' or"
                    + " temp.ntype='CHNG_STRUCTURE') and"
                    + " (temp.notid not in (select not_id from emp_cadre where emp_cadre.not_id=temp.notid and joined_assuch='Y' and EMP_ID='" + empid + "'))"
                    + " order by joindt desc) TEMP)jointemp)temp";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                count = rs.getInt("cnt");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return count;
    }

    @Override
    public JoiningForm getJoiningData(String empid, int notid, String rlvid, String leaveid, String addl, String jid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String sql = "";

        JoiningForm jform = new JoiningForm();
        try {
            con = this.dataSource.getConnection();
            if (notid > 0) {
                sql = "SELECT NOT_ID,NOT_TYPE,ORDNO,EMP_ID,ORDDT,EMP_NOTIFICATION.DEPT_CODE,NOTE,EMP_NOTIFICATION.OFF_CODE,AUTH,DEPARTMENT_NAME,OFF_EN,SPN,ENTRY_TYPE FROM\n"
                        + "(SELECT NOT_ID,NOT_TYPE,ORDNO,EMP_ID,ORDDT,DEPT_CODE,NOTE,OFF_CODE,AUTH,ENTRY_TYPE FROM EMP_NOTIFICATION WHERE\n"
                        + "NOT_ID=? AND EMP_ID=?)EMP_NOTIFICATION\n"
                        + "LEFT OUTER JOIN G_DEPARTMENT ON EMP_NOTIFICATION.DEPT_CODE=G_DEPARTMENT.DEPARTMENT_CODE\n"
                        + "LEFT OUTER JOIN G_OFFICE ON EMP_NOTIFICATION.OFF_CODE=G_OFFICE.OFF_CODE\n"
                        + "LEFT OUTER JOIN G_SPC ON EMP_NOTIFICATION.AUTH=G_SPC.SPC";
                pst = con.prepareStatement(sql);
                pst.setInt(1, notid);
                pst.setString(2, empid);
                rs = pst.executeQuery();
                if (rs.next()) {
                    jform.setJempid(empid);
                    jform.setNotId(rs.getInt("NOT_ID"));
                    jform.setNotType(rs.getString("NOT_TYPE"));
                    jform.setNotOrdNo(rs.getString("ORDNO"));
                    if (rs.getDate("ORDDT") != null && !rs.getDate("ORDDT").equals("")) {
                        jform.setNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("ORDDT")));
                    }
                    jform.setNotiDeptName(rs.getString("DEPARTMENT_NAME"));
                    jform.setNotiOffName(rs.getString("OFF_EN"));
                    jform.setNotiSpn(rs.getString("SPN"));
                    jform.setEntryType(rs.getString("ENTRY_TYPE"));
                    jform.setRdTransaction("ENTRY_TYPE");

                    /////////// For Deputation Joining ////////
                    if (rs.getString("ENTRY_TYPE") != null && rs.getString("ENTRY_TYPE").equals("C") && rs.getString("NOT_TYPE").equals("DEPUTATION")) {
                        String sql1 = "SELECT EMP_NOTIFICATION.NOT_ID,EMP_NOTIFICATION.NOT_TYPE,emp_transfer.dept_code, emp_transfer.off_code,department_name,off_en, next_spc,spn,"
                                + "dist_name,G_OFFICE.dist_code,post,gpc  FROM\n"
                                + "(SELECT NOT_ID,NOT_TYPE,ORDNO,EMP_ID,ORDDT,DEPT_CODE,NOTE,OFF_CODE,AUTH,ENTRY_TYPE FROM EMP_NOTIFICATION WHERE\n"
                                + "NOT_ID=? AND EMP_ID=?)EMP_NOTIFICATION\n"
                                + "LEFT OUTER JOIN emp_transfer on EMP_NOTIFICATION.not_id=emp_transfer.not_id\n"
                                + "LEFT OUTER JOIN G_DEPARTMENT ON emp_transfer.DEPT_CODE=G_DEPARTMENT.DEPARTMENT_CODE\n"
                                + "LEFT OUTER JOIN G_OFFICE ON emp_transfer.OFF_CODE=G_OFFICE.OFF_CODE\n"
                                + "LEFT OUTER JOIN G_SPC ON emp_transfer.next_spc=G_SPC.SPC\n"
                                + "LEFT OUTER JOIN G_DISTRICT ON G_OFFICE.DIST_CODE=G_DISTRICT.DIST_CODE\n"
                                + "LEFT OUTER JOIN G_POST ON g_post.post_code=G_SPC.gpc";

                        pst = con.prepareStatement(sql1);
                        pst.setInt(1, notid);
                        pst.setString(2, empid);
                        rs = pst.executeQuery();
                        if (rs.next()) {
                            jform.setSpn(rs.getString("spn"));
                            jform.setHidPostCode(rs.getString("gpc"));
                            jform.setHidOffCode(rs.getString("off_code"));
                            jform.setHidDistCode(rs.getString("dist_code"));
                            jform.setHidDeptCode(rs.getString("dept_code"));
                            jform.setJspc(rs.getString("next_spc"));
                        }
                    }

                    //////////END//////////////////////////////
                }

            }

            if (rlvid != null && !rlvid.equals("")) {
                sql = "SELECT RELIEVE_ID,MEMO_NO,MEMO_DATE,RLV_DATE,RLV_TIME,DUE_DOJ,DUE_TOJ FROM EMP_RELIEVE WHERE RELIEVE_ID=? AND EMP_ID=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(rlvid));
                pst.setString(2, empid);
                rs = pst.executeQuery();
                if (rs.next()) {
                    jform.setRlvId(rs.getString("RELIEVE_ID"));
                    jform.setRlvOrdNo(rs.getString("MEMO_NO"));
                    if (rs.getDate("MEMO_DATE") != null && !rs.getDate("MEMO_DATE").equals("")) {
                        jform.setRlvOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("MEMO_DATE")));
                    }
                    if (rs.getDate("RLV_DATE") != null && !rs.getDate("RLV_DATE").equals("")) {
                        jform.setRlvDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("RLV_DATE")));
                    }
                    jform.setRlvTime(rs.getString("RLV_TIME"));
                    if (rs.getDate("DUE_DOJ") != null && !rs.getDate("DUE_DOJ").equals("")) {
                        jform.setJoiningDueDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("DUE_DOJ")));
                    }
                    jform.setJoiningDueTime(rs.getString("DUE_TOJ"));
                }
            }

            if (jid != null && !jid.equals("")) {
                //sql = "SELECT JOIN_ID,DOE,MEMO_NO,MEMO_DATE,JOIN_DATE,JOIN_TIME,EMP_JOIN.SPC,NOTE,IF_AD_CHARGE,AUTH_DEPT,AUTH_OFF,LCR_ID,IF_ASSUMED,IF_VISIBLE,ENT_OFF,ENT_DEPT,ENT_AUTH,GPC,field_off_code,SPN,wef FROM EMP_JOIN LEFT OUTER JOIN G_SPC ON EMP_JOIN.SPC=G_SPC.SPC WHERE JOIN_ID=? AND EMP_ID=?";
                /*sql = "SELECT JOIN_ID,DOE,MEMO_NO,MEMO_DATE,JOIN_DATE,JOIN_TIME,NOTE,IF_AD_CHARGE,AUTH_DEPT,AUTH_OFF,EMP_JOIN.SPC,SPN,LCR_ID,\n"
                 + "IF_ASSUMED,IF_VISIBLE,ENT_OFF,ENT_DEPT,ENT_AUTH,GPC,field_off_code,wef,dist_code,goi_spc,goi_offen,"
                 + "EMP_JOIN.organization_type FROM EMP_JOIN\n"
                 + "LEFT OUTER JOIN G_SPC ON EMP_JOIN.SPC=G_SPC.SPC\n"
                 + "LEFT OUTER JOIN G_OFFICE ON EMP_JOIN.AUTH_OFF=G_OFFICE.OFF_CODE\n"
                 + "left outer join (select CAST(g_oth_spc.other_spc_id AS TEXT)goi_spc,off_en goi_offen FROM g_oth_spc WHERE is_active='Y')gothspc \n"
                 + "on emp_join.spc=gothspc.goi_spc\n"
                 + "WHERE JOIN_ID=? AND EMP_ID=?";*/
                sql = "SELECT EMP_JOIN.NOT_TYPE,JOIN_ID,DOE,MEMO_NO,MEMO_DATE,JOIN_DATE,JOIN_TIME,NOTE,IF_AD_CHARGE,AUTH_DEPT,AUTH_OFF,EMP_JOIN.SPC,SPN,LCR_ID,"
                        + " IF_ASSUMED,IF_VISIBLE,ENT_OFF,ENT_DEPT,ENT_AUTH,GPC,field_off_code,wef,dist_code,goi_spc,goi_offen,"
                        + " EMP_JOIN.organization_type,g_oth_spc.off_en goi_off_en,g_oth_postname.postname goipostname FROM EMP_JOIN"
                        + " LEFT OUTER JOIN G_SPC ON EMP_JOIN.SPC=G_SPC.SPC"
                        + " LEFT OUTER JOIN G_OFFICE ON EMP_JOIN.AUTH_OFF=G_OFFICE.OFF_CODE"
                        + " left outer join (select CAST(g_oth_spc.other_spc_id AS TEXT)goi_spc,off_en goi_offen FROM g_oth_spc WHERE is_active='Y')gothspc"
                        + " on emp_join.spc=gothspc.goi_spc"
                        + " left outer join g_oth_spc on emp_join.auth_off=g_oth_spc.other_spc_id::TEXT"
                        + " left outer join g_oth_postname on g_oth_spc.other_spc_id=g_oth_postname.office_id"
                        + " WHERE JOIN_ID=? AND EMP_ID=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(jid));
                pst.setString(2, empid);
                rs = pst.executeQuery();
                if (rs.next()) {
                    jform.setJoinId(rs.getString("JOIN_ID"));
                    jform.setJoiningOrdNo(rs.getString("MEMO_NO"));
                    if (rs.getDate("MEMO_DATE") != null && !rs.getDate("MEMO_DATE").equals("")) {
                        jform.setJoiningOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("MEMO_DATE")));
                    }
                    if (rs.getDate("JOIN_DATE") != null && !rs.getDate("JOIN_DATE").equals("")) {
                        jform.setJoiningDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("JOIN_DATE")));
                    }
                    jform.setSltJoiningTime(rs.getString("JOIN_TIME"));
                    jform.setChkSBVisivle(rs.getString("IF_VISIBLE"));
                    //jform.setChkujt(rs.getString("IF_VISIBLE"));
                    jform.setHidDeptCode(rs.getString("AUTH_DEPT"));
                    jform.setHidDistCode(rs.getString("dist_code"));
                    jform.setHidOffCode(rs.getString("AUTH_OFF"));
                    //jform.setHidTempOffCode(rs.getString("AUTH_OFF"));
                    jform.setHidPostCode(rs.getString("GPC"));
                    //jform.setHidTempPostCode(rs.getString("GPC"));
                    jform.setJspc(rs.getString("SPC"));
                    jform.setSpn(rs.getString("SPN"));
                    jform.setHidPostedOthSpc(rs.getString("goi_spc"));
                    //jform.setHidTempSpc(rs.getString("SPC"));
                    jform.setHidLcrId(rs.getString("lcr_id"));
                    jform.setSltFieldOffice(rs.getString("field_off_code"));
                    //jform.setHidTempFieldOffCode(rs.getString("field_off_code"));
                    jform.setHidAddition(addl);
                    jform.setNote(rs.getString("NOTE"));
                    jform.setTxtWEFDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("wef")));
                    if (rs.getString("organization_type") != null && !rs.getString("organization_type").equals("")) {
                        if (rs.getString("organization_type").equals("GOO")) {
                            jform.setRadpostingauthtype("GOO");
                        } else if (rs.getString("organization_type").equals("GOI")) {
                            jform.setRadpostingauthtype("GOI");
                            if (rs.getString("NOT_TYPE") != null && rs.getString("NOT_TYPE").equals("FIRST_APPOINTMENT")) {
                                jform.setSpn(rs.getString("goipostname"));
                                jform.setHidGOIOffCode(rs.getString("AUTH_OFF"));
                                jform.setHidGOIPostedSPC(rs.getString("SPC"));
                            } else {
                                jform.setSpn(rs.getString("goi_offen"));
                            }
                        }
                    } else {
                        if ((rs.getString("SPC") != null && !rs.getString("SPC").equals(""))) {
                            jform.setRadpostingauthtype("GOO");
                        } else if (rs.getString("goi_spc") != null && !rs.getString("goi_spc").equals("")) {
                            jform.setRadpostingauthtype("GOI");
                        }
                    }

                }
                sql = "SELECT SP_FROM,SP_TO FROM EMP_LEAVE_CR WHERE LCR_ID='" + jform.getHidLcrId() + "'";
                pst = con.prepareStatement(sql);
                rs = pst.executeQuery();
                if (rs.next()) {
                    if (rs.getDate("SP_FROM") != null && !rs.getDate("SP_FROM").equals("")) {
                        jform.setUjtFrmDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("SP_FROM")));
                    }
                    if (rs.getDate("SP_TO") != null && !rs.getDate("SP_TO").equals("")) {
                        jform.setUjtToDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("SP_TO")));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return jform;
    }

    @Override
    public void saveJoining(JoiningForm jform, String entDeptCode, String entOffCode, String entSpc, String loginuserid
    ) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        boolean ret = false;

        try {
            con = this.dataSource.getConnection();

            String curDate = "";
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            curDate = dateFormat.format(cal.getTime());
            if (jform.getRadpostingauthtype() != null && jform.getRadpostingauthtype().equals("GOI")) {
                jform.setSpn(jform.getHidPostedOthSpc());
            }

            //String nexttoe = CommonFunctions.getNextToe(jform.getJempid(), curDate, con);
            if (jform.getJoinId() != null && !jform.getJoinId().equals("")) {
                String sql = "UPDATE EMP_JOIN SET MEMO_NO=?,MEMO_DATE=?,JOIN_DATE=?,JOIN_TIME=?,NOTE=?,NOT_ID=?,NOT_TYPE=?,IF_AD_CHARGE=?,AUTH_DEPT=?,AUTH_OFF=?,SPC=?,IF_ASSUMED=?,"
                        + "IF_VISIBLE=?,ENT_DEPT=?,ENT_OFF=?,ENT_AUTH=?,field_off_code=?,wef=?,organization_type=? "
                        + "WHERE JOIN_ID=? AND EMP_ID=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, jform.getJoiningOrdNo());
                if (jform.getJoiningOrdDt() != null && !jform.getJoiningOrdDt().equals("")) {
                    pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(jform.getJoiningOrdDt()).getTime()));
                } else {
                    pst.setTimestamp(2, null);
                }
                if (jform.getJoiningDt() != null && !jform.getJoiningDt().equals("")) {
                    pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(jform.getJoiningDt()).getTime()));
                } else {
                    pst.setTimestamp(3, null);
                }
                pst.setString(4, jform.getSltJoiningTime());
                pst.setString(5, jform.getNote());
                pst.setInt(6, jform.getNotId());
                pst.setString(7, jform.getNotType());
                pst.setString(8, jform.getHidAddition());

                pst.setString(12, "");
                if (jform.getChkSBVisivle() != null && !jform.getChkSBVisivle().equals("")) {
                    pst.setString(13, jform.getChkSBVisivle());
                } else {
                    pst.setString(13, "Y");
                }
                pst.setString(14, entDeptCode);
                pst.setString(15, entOffCode);
                pst.setString(16, entSpc);

                pst.setString(17, jform.getSltFieldOffice());
                if (jform.getTxtWEFDt() != null && !jform.getTxtWEFDt().equals("")) {
                    pst.setTimestamp(18, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(jform.getTxtWEFDt()).getTime()));
                } else {
                    pst.setTimestamp(18, null);
                }
                if (jform.getRadpostingauthtype() != null && jform.getRadpostingauthtype().equals("GOO")) {
                    pst.setString(9, jform.getHidDeptCode());
                    pst.setString(10, jform.getHidOffCode());
                    pst.setString(11, jform.getJspc());
                    pst.setString(19, "GOO");
                } else {
                    pst.setString(9, null);
                    if (jform.getNotType() != null && jform.getNotType().equals("FIRST_APPOINTMENT")) {
                        pst.setString(10, jform.getHidGOIOffCode());
                        pst.setString(11, jform.getHidGOIPostedSPC());
                    } else {
                        pst.setString(10, null);
                        pst.setString(11, jform.getHidPostedOthSpc());
                    }
                    pst.setString(19, "GOI");
                }
                pst.setInt(20, Integer.parseInt(jform.getJoinId()));
                pst.setString(21, jform.getJempid());
                pst.executeUpdate();

                //System.out.println("join updated");
                DataBaseFunctions.closeSqlObjects(pst);

                String sqlString = ToStringBuilder.reflectionToString(pst);
                pst = con.prepareStatement("UPDATE EMP_JOIN SET modified_by=?,modified_on=?,query_string=? WHERE join_id=? AND EMP_ID=?");
                pst.setString(1, loginuserid);
                pst.setTimestamp(2, new Timestamp(new Date().getTime()));
                pst.setString(3, sqlString);
                pst.setInt(4, Integer.parseInt(jform.getJoinId()));
                pst.setString(5, jform.getJempid());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                if (jform.getHidLcrId() != null && !jform.getHidLcrId().equals("")) {
                    if ((jform.getUjtFrmDt() != null && !jform.getUjtFrmDt().equalsIgnoreCase("")) || (jform.getUjtToDt() != null && !jform.getUjtToDt().equalsIgnoreCase(""))) {
                        pst = con.prepareStatement("UPDATE EMP_LEAVE_CR SET tol_id=?,cr_type=?,sp_from=?,sp_to=? WHERE emp_id=? and lcr_id=?");
                        pst.setString(1, "EL");
                        pst.setString(2, "U");
                        if (jform.getUjtFrmDt() != null && !jform.getUjtFrmDt().equalsIgnoreCase("")) {
                            pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(jform.getUjtFrmDt()).getTime()));
                        } else {
                            pst.setTimestamp(3, null);
                        }
                        if (jform.getUjtToDt() != null && !jform.getUjtToDt().equals("")) {
                            pst.setTimestamp(4, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(jform.getUjtToDt()).getTime()));
                        } else {
                            pst.setTimestamp(4, null);
                        }
                        pst.setString(5, jform.getJempid());
                        pst.setString(6, jform.getHidLcrId());
                        pst.executeUpdate();
                    } else {
                        pst = con.prepareStatement("delete from emp_leave_cr where emp_id=? and cr_type='U' and lcr_id=?");
                        pst.setString(1, jform.getJempid());
                        pst.setString(2, jform.getHidLcrId());
                        pst.executeUpdate();

                        pst = con.prepareStatement("UPDATE EMP_JOIN SET LCR_ID=? WHERE JOIN_ID=? AND EMP_ID=?");
                        pst.setString(1, null);
                        pst.setInt(2, Integer.parseInt(jform.getJoinId()));
                        pst.setString(3, jform.getJempid());
                        pst.executeUpdate();
                    }
                    DataBaseFunctions.closeSqlObjects(pst);
                } else {
                    if ((jform.getUjtFrmDt() != null && !jform.getUjtFrmDt().equalsIgnoreCase("")) || (jform.getUjtToDt() != null && !jform.getUjtToDt().equalsIgnoreCase(""))) {
                        String lcrCode = CommonFunctions.getMaxCode("EMP_LEAVE_CR", "LCR_ID", con);
                        pst = con.prepareStatement("INSERT INTO EMP_LEAVE_CR(LCR_ID,EMP_ID,TOL_ID,CR_TYPE,SP_FROM,SP_TO) values (?,?,?,?,?,?)");
                        pst.setString(1, lcrCode);
                        pst.setString(2, jform.getJempid());
                        pst.setString(3, "EL");
                        pst.setString(4, "U");
                        if (jform.getUjtFrmDt() != null && !jform.getUjtFrmDt().equals("")) {
                            pst.setTimestamp(5, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(jform.getUjtFrmDt()).getTime()));
                        } else {
                            pst.setTimestamp(5, null);
                        }
                        if (jform.getUjtToDt() != null && !jform.getUjtToDt().equals("")) {
                            pst.setTimestamp(6, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(jform.getUjtToDt()).getTime()));
                        } else {
                            pst.setTimestamp(6, null);
                        }
                        pst.executeUpdate();

                        pst = con.prepareStatement("UPDATE EMP_JOIN SET LCR_ID=? WHERE JOIN_ID=? AND EMP_ID=?");
                        pst.setString(1, lcrCode);
                        pst.setInt(2, Integer.parseInt(jform.getJoinId()));
                        pst.setString(3, jform.getJempid());
                        pst.executeUpdate();

                        DataBaseFunctions.closeSqlObjects(pst);
                    }
                }
            } else {
                String sql = "INSERT INTO EMP_JOIN(not_type, not_id, emp_id, doe,memo_no, memo_date, join_date, join_time, auth_dept, auth_off, spc,if_ad_charge, note, LCR_ID, TOE, IF_ASSUMED,IF_VISIBLE,ENT_DEPT,ENT_OFF,ENT_AUTH,field_off_code,wef,organization_type) "
                        + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                //jform.setJoinId(maxJoiningIdDAO.getMaxJoiningId());
                pst.setString(1, jform.getNotType());
                pst.setInt(2, jform.getNotId());
                pst.setString(3, jform.getJempid());
                pst.setTimestamp(4, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(curDate).getTime()));
                pst.setString(5, jform.getJoiningOrdNo());
                if (jform.getJoiningOrdDt() != null && !jform.getJoiningOrdDt().equals("")) {
                    pst.setTimestamp(6, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(jform.getJoiningOrdDt()).getTime()));
                } else {
                    pst.setTimestamp(6, null);
                }
                if (jform.getJoiningDt() != null && !jform.getJoiningDt().equals("")) {
                    pst.setTimestamp(7, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(jform.getJoiningDt()).getTime()));
                } else {
                    pst.setTimestamp(7, null);
                }
                pst.setString(8, jform.getSltJoiningTime());
                //pst.setString(9, jform.getHidDeptCode());
                //pst.setString(10, jform.getHidOffCode());
                //pst.setString(11, jform.getJspc());
                pst.setString(12, jform.getHidAddition());
                pst.setString(13, jform.getNote());
                pst.setString(14, jform.getHidLcrId());
                pst.setString(15, "");
                pst.setString(16, "");
                if (jform.getChkSBVisivle() != null && !jform.getChkSBVisivle().equals("")) {
                    pst.setString(17, jform.getChkSBVisivle());
                } else {
                    pst.setString(17, "Y");
                }
                pst.setString(18, entDeptCode);
                pst.setString(19, entOffCode);
                pst.setString(20, entSpc);
                pst.setString(21, jform.getSltFieldOffice());
                if (jform.getTxtWEFDt() != null && !jform.getTxtWEFDt().equals("")) {
                    pst.setTimestamp(22, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(jform.getTxtWEFDt()).getTime()));
                } else {
                    pst.setTimestamp(22, null);
                }
                if (jform.getRadpostingauthtype() != null && jform.getRadpostingauthtype().equals("GOO")) {
                    pst.setString(23, "GOO");
                    pst.setString(9, jform.getHidDeptCode());
                    pst.setString(10, jform.getHidOffCode());
                    pst.setString(11, jform.getJspc());
                } else if (jform.getRadpostingauthtype() != null && jform.getRadpostingauthtype().equals("GOI")) {
                    pst.setString(23, "GOI");
                    pst.setString(9, null);
                    if (jform.getNotType() != null && jform.getNotType().equals("FIRST_APPOINTMENT")) {
                        pst.setString(10, jform.getHidGOIOffCode());
                        pst.setString(11, jform.getHidGOIPostedSPC());
                    } else {
                        pst.setString(10, null);
                        pst.setString(11, jform.getHidPostedOthSpc());
                    }
                }
                pst.executeUpdate();

                rs = pst.getGeneratedKeys();
                rs.next();
                int joinid = rs.getInt("join_id");
                jform.setJoinId(joinid + "");

                DataBaseFunctions.closeSqlObjects(rs, pst);

                String sqlString = ToStringBuilder.reflectionToString(pst);
                pst = con.prepareStatement("UPDATE EMP_JOIN SET modified_by=?,modified_on=?,query_string=? WHERE join_id=? AND EMP_ID=?");
                pst.setString(1, loginuserid);
                pst.setTimestamp(2, new Timestamp(new Date().getTime()));
                pst.setString(3, sqlString);
                pst.setInt(4, joinid);
                pst.setString(5, jform.getJempid());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                if ((jform.getUjtFrmDt() != null && !jform.getUjtFrmDt().equals(""))
                        || (jform.getUjtToDt() != null && !jform.getUjtToDt().equals(""))) {
                    String lcrCode = CommonFunctions.getMaxCode("EMP_LEAVE_CR", "LCR_ID", con);
                    pst = con.prepareStatement("INSERT INTO EMP_LEAVE_CR(LCR_ID,EMP_ID,TOL_ID,CR_TYPE,SP_FROM,SP_TO) values (?,?,?,?,?,?)");
                    pst.setString(1, lcrCode);
                    pst.setString(2, jform.getJempid());
                    pst.setString(3, "EL");
                    pst.setString(4, "U");
                    if (jform.getUjtFrmDt() != null && !jform.getUjtFrmDt().equals("")) {
                        pst.setTimestamp(5, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(jform.getUjtFrmDt()).getTime()));
                    } else {
                        pst.setTimestamp(5, null);
                    }
                    if (jform.getUjtToDt() != null && !jform.getUjtToDt().equals("")) {
                        pst.setTimestamp(6, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(jform.getUjtToDt()).getTime()));
                    } else {
                        pst.setTimestamp(6, null);
                    }
                    pst.executeUpdate();

                    DataBaseFunctions.closeSqlObjects(pst);
                }
            }
            if (jform.getHidAddition() == null || jform.getHidAddition().equals("")) {
                /*if (curDate != null && !curDate.trim().equals("")) {
                 boolean updatepay = CommonFunctions.isupdatePayOrPostingInfo(jform.getJempid(), jform.getJoiningDt(), jform.getJoiningOrdDt(), "POSTING", con);
                 if (updatepay == true) {
                 ret = updatepay;
                 CommonFunctions.updateEmpPostingInfoOnDate(jform.getJempid(), curDate, con);
                 pst = con.prepareStatement("UPDATE EMP_MAST SET NEXT_OFFICE_CODE=? WHERE EMP_ID=?");
                 pst.setString(1, "");
                 pst.setString(2, jform.getJempid());
                 pst.execute();
                 } else {
                 ret = false;
                 }
                 }*/
                if (jform.getRdTransaction() != null && jform.getRdTransaction().equals("C")) {
                    if (jform.getNotType().equals("DEPUTATION")) {
                        String poffcode=null;
                        /*String sql1 = "select off_code from g_office where lvl='01' and department_code=(Select department_code from g_office where off_code='" + jform.getHidOffCode() + "')";
                        pst=con.prepareStatement(sql1);
                        rs=pst.executeQuery();
                        if(rs.next()){
                            poffcode=rs.getString("off_code");
                        }*/
                        pst = con.prepareStatement("UPDATE EMP_MAST SET DEPUTATION_OFFCODE=?,DEPUTATION_SPC=?,CUR_OFF_CODE=?,NEXT_OFFICE_CODE=? WHERE EMP_ID=?");
                        pst.setString(1, jform.getHidOffCode());
                        pst.setString(2, jform.getJspc());
                        pst.setString(3, jform.getHidOffCode());
                        //pst.setString(3, poffcode);
                        //pst.setString(4, jform.getJspc());
                        pst.setString(4, "");
                        pst.setString(5, jform.getJempid());
                        pst.execute();
                        CommonFunctions.modifyEmpCurStatus(jform.getJempid(), "ON DEPUTATION", con);

                    } else {
                        pst = con.prepareStatement("UPDATE EMP_MAST SET CUR_OFF_CODE=?,CUR_SPC=?,NEXT_OFFICE_CODE=? WHERE EMP_ID=?");
                        pst.setString(1, jform.getHidOffCode());
                        pst.setString(2, jform.getJspc());
                        pst.setString(3, "");
                        pst.setString(4, jform.getJempid());
                        pst.execute();
                        CommonFunctions.modifyEmpCurStatus(jform.getJempid(), "ON DUTY", con);
                    }

                    String sqlString = ToStringBuilder.reflectionToString(pst);
                    sbDAO.createTransactionLog(loginuserid, jform.getJempid(), "JOINING", sqlString);

                    DataBaseFunctions.closeSqlObjects(pst);
                }
            }

            if (jform.getChkNotSBPrint() != null && jform.getChkNotSBPrint().equals("Y")) {
                pst = con.prepareStatement("UPDATE EMP_JOIN SET IF_VISIBLE='N' WHERE JOIN_ID=?");
                pst.setInt(1, Integer.parseInt(jform.getJoinId()));
                pst.execute();
            } else if (jform.getChkNotSBPrint() == null || jform.getChkNotSBPrint().equals("")) {
                String sbLanguage = sbDAO.getJoinDetails(jform.getJoinId(), jform.getJempid());
                pst = con.prepareStatement("UPDATE EMP_JOIN SET SB_DESCRIPTION=?,IF_VISIBLE='Y' WHERE JOIN_ID=?");
                pst.setString(1, sbLanguage);
                pst.setInt(2, Integer.parseInt(jform.getJoinId()));
                pst.executeUpdate();
            }

            DataBaseFunctions.closeSqlObjects(pst);
            /**
             * ************************* Insert HRMS ID to the Quarter Table
             * *******************
             */
            // System.out.println("hrms===" + jform.getJempid());
            pst = con.prepareStatement("UPDATE consumer_ga SET transfer_id =? WHERE hrmsid=?");
            pst.setString(1, jform.getJempid());
            pst.setString(2, jform.getJempid());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void deleteJoining(JoiningForm jform
    ) {

        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveFieldOfficeData(String empid, String offcode
    ) {
        Connection con = null;
        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("Update emp_mast set field_off_code=? where emp_id=? and dep_code not in ('08','09','10')");
            pst.setString(1, offcode);
            pst.setString(2, empid);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public JoiningForm getFieldOfficeData(String empid
    ) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        JoiningForm jform = new JoiningForm();;

        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("select field_off_code,off_en,dist_name from emp_mast emp\n"
                    + "inner join g_office on emp.field_off_code=g_office.off_code\n"
                    + "inner join g_district on g_office.dist_code=g_district.dist_code\n"
                    + "where emp_id=?");
            pst.setString(1, empid);
            rs = pst.executeQuery();
            if (rs.next()) {
                jform.setHidTempFieldOffCode(rs.getString("field_off_code"));
                jform.setSltFieldOffice(rs.getString("off_en"));
                jform.setHidDistname(rs.getString("dist_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return jform;
    }

    @Override
    public String saveJoiningSBCorrection(JoiningForm jform, String entDeptCode, String entOffCode, String entSpc, String loginuserid, int sbcorrectionid
    ) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        boolean ret = false;

        String sbLanguage = "";

        try {
            con = this.dataSource.getConnection();

            String curDate = "";
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            curDate = dateFormat.format(cal.getTime());
            if (jform.getRadpostingauthtype() != null && jform.getRadpostingauthtype().equals("GOI")) {
                jform.setSpn(jform.getHidPostedOthSpc());
            }
            
            String sql = "DELETE FROM EMP_JOIN_SB_CORRECTION WHERE emp_id = ? and join_id = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, jform.getJempid());
            if(jform.getJoinId() != null && !jform.getJoinId().equals("")){
                pst.setInt(2, Integer.parseInt(jform.getJoinId()));                
            }else{
                pst.setInt(2, 0);
            }
            pst.executeUpdate();
            
            sql = "INSERT INTO EMP_JOIN_SB_CORRECTION(not_type, not_id, emp_id, doe,memo_no, memo_date, join_date, join_time, auth_dept, auth_off, spc,if_ad_charge, note, LCR_ID, TOE, IF_ASSUMED,IF_VISIBLE,ENT_DEPT,ENT_OFF,ENT_AUTH,field_off_code,wef,organization_type,ref_correction_id,join_id) "
                    + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            //jform.setJoinId(maxJoiningIdDAO.getMaxJoiningId());
            pst.setString(1, jform.getNotType());
            pst.setInt(2, jform.getNotId());
            pst.setString(3, jform.getJempid());
            pst.setTimestamp(4, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(curDate).getTime()));
            pst.setString(5, jform.getJoiningOrdNo());
            if (jform.getJoiningOrdDt() != null && !jform.getJoiningOrdDt().equals("")) {
                pst.setTimestamp(6, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(jform.getJoiningOrdDt()).getTime()));
            } else {
                pst.setTimestamp(6, null);
            }
            if (jform.getJoiningDt() != null && !jform.getJoiningDt().equals("")) {
                pst.setTimestamp(7, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(jform.getJoiningDt()).getTime()));
            } else {
                pst.setTimestamp(7, null);
            }
            pst.setString(8, jform.getSltJoiningTime());
            pst.setString(12, jform.getHidAddition());
            pst.setString(13, jform.getNote());
            pst.setString(14, jform.getHidLcrId());
            pst.setString(15, "");
            pst.setString(16, "");
            if (jform.getChkSBVisivle() != null && !jform.getChkSBVisivle().equals("")) {
                pst.setString(17, jform.getChkSBVisivle());
            } else {
                pst.setString(17, "Y");
            }
            pst.setString(18, entDeptCode);
            pst.setString(19, entOffCode);
            pst.setString(20, entSpc);
            pst.setString(21, jform.getSltFieldOffice());
            if (jform.getTxtWEFDt() != null && !jform.getTxtWEFDt().equals("")) {
                pst.setTimestamp(22, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(jform.getTxtWEFDt()).getTime()));
            } else {
                pst.setTimestamp(22, null);
            }
            if (jform.getRadpostingauthtype() != null && jform.getRadpostingauthtype().equals("GOO")) {
                pst.setString(23, "GOO");
                pst.setString(9, jform.getHidDeptCode());
                pst.setString(10, jform.getHidOffCode());
                pst.setString(11, jform.getJspc());
            } else if (jform.getRadpostingauthtype() != null && jform.getRadpostingauthtype().equals("GOI")) {
                pst.setString(23, "GOI");
                pst.setString(9, null);
                if (jform.getNotType() != null && jform.getNotType().equals("FIRST_APPOINTMENT")) {
                    pst.setString(10, jform.getHidGOIOffCode());
                    pst.setString(11, jform.getHidGOIPostedSPC());
                } else {
                    pst.setString(10, null);
                    pst.setString(11, jform.getHidPostedOthSpc());
                }
            }
            pst.setInt(24, sbcorrectionid);
            if(jform.getJoinId() != null && !jform.getJoinId().equals("")){
                pst.setInt(25, Integer.parseInt(jform.getJoinId()));
            }else{
                pst.setInt(25, 0);
            }
            pst.executeUpdate();

            rs = pst.getGeneratedKeys();
            rs.next();
            int logid = rs.getInt("log_id");

            DataBaseFunctions.closeSqlObjects(rs, pst);

            String sqlString = ToStringBuilder.reflectionToString(pst);
            pst = con.prepareStatement("UPDATE EMP_JOIN_SB_CORRECTION SET modified_by=?,modified_on=?,query_string=? WHERE log_id=? AND EMP_ID=?");
            pst.setString(1, loginuserid);
            pst.setTimestamp(2, new Timestamp(new Date().getTime()));
            pst.setString(3, sqlString);
            pst.setInt(4, logid);
            pst.setString(5, jform.getJempid());
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            if (jform.getChkNotSBPrint() != null && jform.getChkNotSBPrint().equals("Y")) {
                pst = con.prepareStatement("UPDATE EMP_JOIN_SB_CORRECTION SET IF_VISIBLE='N' WHERE JOIN_ID=?");
                pst.setInt(1, Integer.parseInt(jform.getJoinId()));
                pst.execute();
            } else if (jform.getChkNotSBPrint() == null || jform.getChkNotSBPrint().equals("")) {
                sbLanguage = sbDAO.getJoinDetails(jform.getJoinId(), jform.getJempid());
                pst = con.prepareStatement("UPDATE EMP_JOIN_SB_CORRECTION SET SB_DESCRIPTION=?,IF_VISIBLE='Y' WHERE JOIN_ID=?");
                pst.setString(1, sbLanguage);
                pst.setInt(2, Integer.parseInt(jform.getJoinId()));
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return sbLanguage;
    }

    
    public JoiningForm getJoiningDataSBCorrection(String empid, int notid, String rlvid, String leaveid, String addl, String jid,int correctionid) {
        
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        String sql = "";

        JoiningForm jform = new JoiningForm();
        try {
            con = this.dataSource.getConnection();
            if (notid > 0) {
                sql = "SELECT NOT_ID,NOT_TYPE,ORDNO,EMP_ID,ORDDT,EMP_NOTIFICATION.DEPT_CODE,NOTE,EMP_NOTIFICATION.OFF_CODE,AUTH,DEPARTMENT_NAME,OFF_EN,SPN FROM\n"
                        + "(SELECT NOT_ID,NOT_TYPE,ORDNO,EMP_ID,ORDDT,DEPT_CODE,NOTE,OFF_CODE,AUTH FROM EMP_NOTIFICATION WHERE\n"
                        + "NOT_ID=? AND EMP_ID=?)EMP_NOTIFICATION\n"
                        + "LEFT OUTER JOIN G_DEPARTMENT ON EMP_NOTIFICATION.DEPT_CODE=G_DEPARTMENT.DEPARTMENT_CODE\n"
                        + "LEFT OUTER JOIN G_OFFICE ON EMP_NOTIFICATION.OFF_CODE=G_OFFICE.OFF_CODE\n"
                        + "LEFT OUTER JOIN G_SPC ON EMP_NOTIFICATION.AUTH=G_SPC.SPC";
                pst = con.prepareStatement(sql);
                pst.setInt(1, notid);
                pst.setString(2, empid);
                rs = pst.executeQuery();
                if (rs.next()) {
                    jform.setJempid(empid);
                    jform.setNotId(rs.getInt("NOT_ID"));
                    jform.setNotType(rs.getString("NOT_TYPE"));
                    jform.setNotOrdNo(rs.getString("ORDNO"));
                    if (rs.getDate("ORDDT") != null && !rs.getDate("ORDDT").equals("")) {
                        jform.setNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("ORDDT")));
                    }
                    jform.setNotiDeptName(rs.getString("DEPARTMENT_NAME"));
                    jform.setNotiOffName(rs.getString("OFF_EN"));
                    jform.setNotiSpn(rs.getString("SPN"));
                }
            }

            if (rlvid != null && !rlvid.equals("")) {
                sql = "SELECT RELIEVE_ID,MEMO_NO,MEMO_DATE,RLV_DATE,RLV_TIME,DUE_DOJ,DUE_TOJ FROM EMP_RELIEVE WHERE RELIEVE_ID=? AND EMP_ID=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(rlvid));
                pst.setString(2, empid);
                rs = pst.executeQuery();
                if (rs.next()) {
                    jform.setRlvId(rs.getString("RELIEVE_ID"));
                    jform.setRlvOrdNo(rs.getString("MEMO_NO"));
                    if (rs.getDate("MEMO_DATE") != null && !rs.getDate("MEMO_DATE").equals("")) {
                        jform.setRlvOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("MEMO_DATE")));
                    }
                    if (rs.getDate("RLV_DATE") != null && !rs.getDate("RLV_DATE").equals("")) {
                        jform.setRlvDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("RLV_DATE")));
                    }
                    jform.setRlvTime(rs.getString("RLV_TIME"));
                    if (rs.getDate("DUE_DOJ") != null && !rs.getDate("DUE_DOJ").equals("")) {
                        jform.setJoiningDueDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("DUE_DOJ")));
                    }
                    jform.setJoiningDueTime(rs.getString("DUE_TOJ"));
                }
            }

            if (jid != null && !jid.equals("")) {
                sql = "SELECT EMP_JOIN.NOT_TYPE,JOIN_ID,DOE,MEMO_NO,MEMO_DATE,JOIN_DATE,JOIN_TIME,NOTE,IF_AD_CHARGE,AUTH_DEPT,AUTH_OFF,EMP_JOIN.SPC,SPN,LCR_ID,"
                        + " IF_ASSUMED,IF_VISIBLE,ENT_OFF,ENT_DEPT,ENT_AUTH,GPC,field_off_code,wef,dist_code,goi_spc,goi_offen,"
                        + " EMP_JOIN.organization_type,g_oth_spc.off_en goi_off_en,g_oth_postname.postname goipostname FROM EMP_JOIN_SB_CORRECTION EMP_JOIN"
                        + " LEFT OUTER JOIN G_SPC ON EMP_JOIN.SPC=G_SPC.SPC"
                        + " LEFT OUTER JOIN G_OFFICE ON EMP_JOIN.AUTH_OFF=G_OFFICE.OFF_CODE"
                        + " left outer join (select CAST(g_oth_spc.other_spc_id AS TEXT)goi_spc,off_en goi_offen FROM g_oth_spc WHERE is_active='Y')gothspc"
                        + " on emp_join.spc=gothspc.goi_spc"
                        + " left outer join g_oth_spc on emp_join.auth_off=g_oth_spc.other_spc_id::TEXT"
                        + " left outer join g_oth_postname on g_oth_spc.other_spc_id=g_oth_postname.office_id"
                        + " WHERE JOIN_ID=? AND EMP_ID=? AND EMP_JOIN.REF_CORRECTION_ID=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(jid));
                pst.setString(2, empid);
                pst.setInt(3, correctionid);
                rs = pst.executeQuery();
                if (rs.next()) {
                    jform.setJoinId(rs.getString("JOIN_ID"));
                    jform.setJoiningOrdNo(rs.getString("MEMO_NO"));
                    if (rs.getDate("MEMO_DATE") != null && !rs.getDate("MEMO_DATE").equals("")) {
                        jform.setJoiningOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("MEMO_DATE")));
                    }
                    if (rs.getDate("JOIN_DATE") != null && !rs.getDate("JOIN_DATE").equals("")) {
                        jform.setJoiningDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("JOIN_DATE")));
                    }
                    jform.setSltJoiningTime(rs.getString("JOIN_TIME"));
                    jform.setChkSBVisivle(rs.getString("IF_VISIBLE"));
                    jform.setHidDeptCode(rs.getString("AUTH_DEPT"));
                    jform.setHidDistCode(rs.getString("dist_code"));
                    jform.setHidOffCode(rs.getString("AUTH_OFF"));
                    jform.setHidPostCode(rs.getString("GPC"));
                    jform.setJspc(rs.getString("SPC"));
                    jform.setSpn(rs.getString("SPN"));
                    jform.setHidPostedOthSpc(rs.getString("goi_spc"));
                    jform.setHidLcrId(rs.getString("lcr_id"));
                    jform.setSltFieldOffice(rs.getString("field_off_code"));
                    jform.setHidAddition(addl);
                    jform.setNote(rs.getString("NOTE"));
                    jform.setTxtWEFDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("wef")));
                    if (rs.getString("organization_type") != null && !rs.getString("organization_type").equals("")) {
                        if (rs.getString("organization_type").equals("GOO")) {
                            jform.setRadpostingauthtype("GOO");
                        } else if (rs.getString("organization_type").equals("GOI")) {
                            jform.setRadpostingauthtype("GOI");
                            if (rs.getString("NOT_TYPE") != null && rs.getString("NOT_TYPE").equals("FIRST_APPOINTMENT")) {
                                jform.setSpn(rs.getString("goipostname"));
                                jform.setHidGOIOffCode(rs.getString("AUTH_OFF"));
                                jform.setHidGOIPostedSPC(rs.getString("SPC"));
                            } else {
                                jform.setSpn(rs.getString("goi_offen"));
                            }
                        }
                    } else {
                        if ((rs.getString("SPC") != null && !rs.getString("SPC").equals(""))) {
                            jform.setRadpostingauthtype("GOO");
                        } else if (rs.getString("goi_spc") != null && !rs.getString("goi_spc").equals("")) {
                            jform.setRadpostingauthtype("GOI");
                        }
                    }

                }
                sql = "SELECT SP_FROM,SP_TO FROM EMP_LEAVE_CR WHERE LCR_ID='" + jform.getHidLcrId() + "'";
                pst = con.prepareStatement(sql);
                rs = pst.executeQuery();
                if (rs.next()) {
                    if (rs.getDate("SP_FROM") != null && !rs.getDate("SP_FROM").equals("")) {
                        jform.setUjtFrmDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("SP_FROM")));
                    }
                    if (rs.getDate("SP_TO") != null && !rs.getDate("SP_TO").equals("")) {
                        jform.setUjtToDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("SP_TO")));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return jform;
    }

    @Override
    public void approveJoiningSBCorrection(JoiningForm jform, String entrydeptcode, String entryoffcode, String entryspc, String loginuserid
    ) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = this.dataSource.getConnection();

            if (jform.getJoinId() != null && !jform.getJoinId().equals("")) {
                String sql = "UPDATE EMP_JOIN_SB_CORRECTION SET MEMO_NO=?,MEMO_DATE=?,JOIN_DATE=?,JOIN_TIME=?,NOTE=?,NOT_ID=?,NOT_TYPE=?,IF_AD_CHARGE=?,AUTH_DEPT=?,AUTH_OFF=?,SPC=?,IF_ASSUMED=?,"
                        + "IF_VISIBLE=?,ENT_DEPT=?,ENT_OFF=?,ENT_AUTH=?,field_off_code=?,wef=?,organization_type=? "
                        + "WHERE JOIN_ID=? AND EMP_ID=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, jform.getJoiningOrdNo());
                if (jform.getJoiningOrdDt() != null && !jform.getJoiningOrdDt().equals("")) {
                    pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(jform.getJoiningOrdDt()).getTime()));
                } else {
                    pst.setTimestamp(2, null);
                }
                if (jform.getJoiningDt() != null && !jform.getJoiningDt().equals("")) {
                    pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(jform.getJoiningDt()).getTime()));
                } else {
                    pst.setTimestamp(3, null);
                }
                pst.setString(4, jform.getSltJoiningTime());
                pst.setString(5, jform.getNote());
                pst.setInt(6, jform.getNotId());
                pst.setString(7, jform.getNotType());
                pst.setString(8, jform.getHidAddition());
                pst.setString(12, "");
                if (jform.getChkSBVisivle() != null && !jform.getChkSBVisivle().equals("")) {
                    pst.setString(13, jform.getChkSBVisivle());
                } else {
                    pst.setString(13, "Y");
                }
                pst.setString(14, entrydeptcode);
                pst.setString(15, entryoffcode);
                pst.setString(16, entryspc);
                pst.setString(17, jform.getSltFieldOffice());
                if (jform.getTxtWEFDt() != null && !jform.getTxtWEFDt().equals("")) {
                    pst.setTimestamp(18, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(jform.getTxtWEFDt()).getTime()));
                } else {
                    pst.setTimestamp(18, null);
                }
                if (jform.getRadpostingauthtype() != null && jform.getRadpostingauthtype().equals("GOO")) {
                    pst.setString(9, jform.getHidDeptCode());
                    pst.setString(10, jform.getHidOffCode());
                    pst.setString(11, jform.getJspc());
                    pst.setString(19, "GOO");
                } else {
                    pst.setString(9, null);
                    if (jform.getNotType() != null && jform.getNotType().equals("FIRST_APPOINTMENT")) {
                        pst.setString(10, jform.getHidGOIOffCode());
                        pst.setString(11, jform.getHidGOIPostedSPC());
                    } else {
                        pst.setString(10, null);
                        pst.setString(11, jform.getHidPostedOthSpc());
                    }
                    pst.setString(19, "GOI");
                }
                pst.setInt(20, Integer.parseInt(jform.getJoinId()));
                pst.setString(21, jform.getJempid());
                pst.executeUpdate();

                DataBaseFunctions.closeSqlObjects(pst);

                String sqlString = ToStringBuilder.reflectionToString(pst);
                pst = con.prepareStatement("UPDATE EMP_JOIN_SB_CORRECTION SET modified_by=?,modified_on=?,query_string=? WHERE join_id=? AND EMP_ID=?");
                pst.setString(1, loginuserid);
                pst.setTimestamp(2, new Timestamp(new Date().getTime()));
                pst.setString(3, sqlString);
                pst.setInt(4, Integer.parseInt(jform.getJoinId()));
                pst.setString(5, jform.getJempid());
                pst.executeUpdate();

                sql = "UPDATE EMP_JOIN SET MEMO_NO=EMP_JOIN_SB_CORRECTION.MEMO_NO,MEMO_DATE=EMP_JOIN_SB_CORRECTION.MEMO_DATE,JOIN_DATE=EMP_JOIN_SB_CORRECTION.JOIN_DATE,JOIN_TIME=EMP_JOIN_SB_CORRECTION.JOIN_TIME,NOTE=EMP_JOIN_SB_CORRECTION.NOTE,AUTH_DEPT=EMP_JOIN_SB_CORRECTION.AUTH_DEPT,AUTH_OFF=EMP_JOIN_SB_CORRECTION.AUTH_OFF,SPC=EMP_JOIN_SB_CORRECTION.SPC,IF_ASSUMED=EMP_JOIN_SB_CORRECTION.IF_ASSUMED,"
                        + "IF_VISIBLE=EMP_JOIN_SB_CORRECTION.IF_VISIBLE,wef=EMP_JOIN_SB_CORRECTION.wef,organization_type=EMP_JOIN_SB_CORRECTION.organization_type FROM EMP_JOIN_SB_CORRECTION"
                        + " WHERE EMP_JOIN.JOIN_ID = EMP_JOIN_SB_CORRECTION.JOIN_ID AND EMP_JOIN_SB_CORRECTION.JOIN_ID=? AND EMP_JOIN_SB_CORRECTION.EMP_ID=?";
                pst = con.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(jform.getJoinId()));
                pst.setString(2, jform.getJempid());
                pst.executeUpdate();
                
                String sbLanguage = sbDAO.getJoinDetails(jform.getJoinId(), jform.getJempid());
                pst = con.prepareStatement("UPDATE EMP_JOIN SET SB_DESCRIPTION=?,IF_VISIBLE='Y' WHERE JOIN_ID=?");
                pst.setString(1, sbLanguage);
                pst.setInt(2, Integer.parseInt(jform.getJoinId()));
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }
}
