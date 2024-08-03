package hrms.dao.policemodule;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.master.District;
import hrms.model.policemodule.PoliceEmployee;
import hrms.model.policemodule.PoliceMISReportBean;
import hrms.model.policemodule.PoliceMISReportForm;
import hrms.model.policemodule.PoliceSearchResult;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.commons.lang.StringUtils;

public class PoliceMISReportDAOImpl implements PoliceMISReportDAO {

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
    public PoliceSearchResult LocatePolice(PoliceMISReportForm searchPolice) {

        Connection con = null;

        ResultSet rs = null;
        PreparedStatement st = null;
        PreparedStatement countst = null;

        ArrayList employeeList = new ArrayList();

        PoliceEmployee employee = null;

        PoliceSearchResult empSearchResult = new PoliceSearchResult();
        try {
            con = this.repodataSource.getConnection();

            if (searchPolice.getOffcode() == null || searchPolice.getOffcode().equals("")) {
                if (searchPolice.getCriteria() != null && !searchPolice.getCriteria().equals("") && searchPolice.getCriteria().equals("GPFNO")) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + "WHERE gpf_no = ? ");

                    countst.setString(1, StringUtils.upperCase(searchPolice.getSearchString()));
                    st = con.prepareStatement(" SELECT emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC, is_regular,doe_gov,"
                            + "(select ARRAY_TO_STRING(ARRAY[F_NAME, M_NAME,L_NAME], ' ') FATHER_NAME from emp_relation where emp_id=emp_mast.emp_id and relation='FATHER')father_name FROM emp_mast"
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc"
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE"
                            + " WHERE gpf_no = ? order by f_name");

                    st.setString(1, StringUtils.upperCase(searchPolice.getSearchString()));
                } else if (searchPolice.getCriteria() != null && !searchPolice.getCriteria().equals("") && searchPolice.getCriteria().equals("HRMSID")) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + "WHERE emp_id = ? ");
                    countst.setString(1, searchPolice.getSearchString());
                    st = con.prepareStatement(" SELECT doe_gov,emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,"
                            + "(select ARRAY_TO_STRING(ARRAY[F_NAME, M_NAME,L_NAME], ' ') FATHER_NAME from emp_relation where emp_id=emp_mast.emp_id and relation='FATHER')father_name FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE emp_id = ?  order by f_name ");
                    st.setString(1, searchPolice.getSearchString());
                } else if (searchPolice.getCriteria() != null && !searchPolice.getCriteria().equals("") && searchPolice.getCriteria().equals("MOBILE")) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + "WHERE MOBILE = ? ");
                    countst.setString(1, searchPolice.getSearchString());
                    st = con.prepareStatement(" SELECT doe_gov,emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,"
                            + "(select ARRAY_TO_STRING(ARRAY[F_NAME, M_NAME,L_NAME], ' ') FATHER_NAME from emp_relation where emp_id=emp_mast.emp_id and relation='FATHER')father_name FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE MOBILE = ? order by f_name");
                    st.setString(1, searchPolice.getSearchString());
                } else if (searchPolice.getCriteria() != null && !searchPolice.getCriteria().equals("") && searchPolice.getCriteria().equals("FNAME")) {
                    // if (searchEmployee.getOffcode() == null || searchEmployee.getOffcode().equals("")) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE F_name LIKE  ? ");
                    countst.setString(1, searchPolice.getSearchString());
                    /*st = con.prepareStatement(" SELECT doe_gov,emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,"
                     + "(select ARRAY_TO_STRING(ARRAY[F_NAME, M_NAME,L_NAME], ' ') FATHER_NAME from emp_relation where emp_id=emp_mast.emp_id and relation='FATHER')father_name FROM emp_mast "
                     + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                     + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                     + "  WHERE F_name LIKE  ? ORDER BY f_name, m_name,l_name ");*/
                    String sql = "SELECT doe_gov,emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,"
                            + "(select ARRAY_TO_STRING(ARRAY[F_NAME, M_NAME,L_NAME], ' ') FATHER_NAME from emp_relation where emp_id=emp_mast.emp_id and relation='FATHER')father_name FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + "  WHERE F_name LIKE ?";
                    if (searchPolice.getSltRank() != null && !searchPolice.getSltRank().equals("")) {
                        sql = sql + " and G_POST.post_code=?";
                    }
                    sql = sql + " ORDER BY f_name, m_name,l_name";
                    st = con.prepareStatement(sql);
                    st.setString(1, searchPolice.getSearchString().toUpperCase() + "%");
                    if (searchPolice.getSltRank() != null && !searchPolice.getSltRank().equals("")) {
                        st.setString(2, searchPolice.getSltRank());
                    }
                }

            } else if (searchPolice.getOffcode() != null) {
                if (searchPolice.getCriteria() == null || searchPolice.getCriteria().equals("")) {
                    countst = con.prepareStatement("SELECT COUNT(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE cur_off_code = ?");
                    countst.setString(1, searchPolice.getOffcode());
                    st = con.prepareStatement(" SELECT is_sb_update_completed,emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE cur_off_code = ? order by f_name ");
                    st.setString(1, searchPolice.getOffcode());
                    /*if deptname & offcode != null & searchcriteria = GPFNO */
                } else if (searchPolice.getCriteria() != null && !searchPolice.getCriteria().equals("") && searchPolice.getCriteria().equals("GPFNO")) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + "WHERE cur_off_code = ? and gpf_no = ? ");
                    countst.setString(1, searchPolice.getOffcode());
                    countst.setString(2, StringUtils.upperCase(searchPolice.getSearchString()));
                    st = con.prepareStatement(" SELECT is_sb_update_completed,emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE cur_off_code = ? and gpf_no = ? order by f_name");
                    st.setString(1, searchPolice.getOffcode());
                    st.setString(2, StringUtils.upperCase(searchPolice.getSearchString()));
                } else if (searchPolice.getCriteria() != null && !searchPolice.getCriteria().equals("") && searchPolice.getCriteria().equals("HRMSID") && searchPolice.getOffcode() != null) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + "WHERE cur_off_code = ? and emp_id = ? ");
                    countst.setString(1, searchPolice.getOffcode());
                    countst.setString(2, searchPolice.getSearchString());
                    st = con.prepareStatement(" SELECT is_sb_update_completed,emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE cur_off_code = ? and emp_id = ? order by f_name");
                    st.setString(1, searchPolice.getOffcode());
                    st.setString(2, searchPolice.getSearchString());
                } else if (searchPolice.getCriteria() != null && !searchPolice.getCriteria().equals("") && searchPolice.getCriteria().equals("FNAME") && searchPolice.getOffcode() != null) {
                    countst = con.prepareStatement("SELECT count(*) CNT FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + " WHERE cur_off_code = ? and F_name LIKE ? ");
                    countst.setString(1, searchPolice.getOffcode());
                    countst.setString(2, searchPolice.getSearchString());
                    st = con.prepareStatement(" SELECT is_sb_update_completed,emp_id, f_name, m_name,l_name,gpf_no,dob,POST,SPC,'' DDO_HRMSID, is_regular,has_office_privilage(SPC) as has_office_privilage FROM emp_mast "
                            + " LEFT OUTER JOIN G_SPC ON  emp_mast.cur_spc = G_SPC.spc "
                            + " LEFT OUTER JOIN G_POST ON G_SPC.GPC=G_POST.POST_CODE "
                            + "  WHERE cur_off_code = ? and F_name LIKE  ? ORDER BY f_name, m_name,l_name");
                    st.setString(1, searchPolice.getOffcode());
                    st.setString(2, searchPolice.getSearchString().toUpperCase() + "%");

                } else {
                    countst = con.prepareStatement("SELECT count(*) CNT  FROM emp_mast WHERE cur_off_code = ?");
                    countst.setString(1, searchPolice.getOffcode());
                    st = con.prepareStatement("SELECT EMPLIST.is_sb_update_completed,EMPLIST.emp_id, EMPLIST.f_name, EMPLIST.m_name,EMPLIST.l_name,EMPLIST.gpf_no,EMPLIST.dob,EMPLIST.cur_spc,EMPLIST.POST ,EMPLIST.SPC,EMPLIST.IS_REGULAR,GOFF.DDO_HRMSID FROM\n"
                            + "(SELECT EMPARR.is_sb_update_completed,EMPARR.emp_id, EMPARR.f_name, EMPARR.m_name,EMPARR.l_name,EMPARR.gpf_no,EMPARR.dob,EMPARR.cur_spc,EMPARR.POST ,EMPARR.is_regular,GMAP.SPC FROM\n"
                            + "(SELECT EMP.is_sb_update_completed,EMP.emp_id, EMP.f_name, EMP.m_name,EMP.l_name,EMP.gpf_no,EMP.dob,EMP.cur_spc,EMP.is_regular,G_POST.POST FROM\n"
                            + "(SELECT is_sb_update_completed,emp_id, f_name, m_name,l_name,gpf_no,dob,cur_spc,is_regular FROM emp_mast WHERE cur_off_code=?) EMP\n"
                            + "LEFT OUTER JOIN\n"
                            + "G_POST ON SUBSTR(EMP.CUR_SPC,14,6)=G_POST.POST_CODE )EMPARR\n"
                            + "LEFT OUTER JOIN\n"
                            + "(SELECT DISTINCT(SPC) FROM G_PRIVILEGE_MAP WHERE ROLE_ID='05') GMAP ON EMPARR.CUR_SPC=GMAP.SPC ORDER BY EMPARR.F_NAME) EMPLIST\n"
                            + "LEFT OUTER JOIN\n"
                            + "(SELECT * FROM G_OFFICE) GOFF ON\n"
                            + "SUBSTR(EMPLIST.CUR_SPC,1,13)=GOFF.OFF_CODE AND GOFF.DDO_HRMSID=EMPLIST.EMP_ID ");
                    st.setString(1, searchPolice.getOffcode());

                }
            }

            rs = countst.executeQuery();
            if (rs.next()) {
                empSearchResult.setTotalEmpFound(rs.getInt("CNT"));
            }
            rs = st.executeQuery();
            while (rs.next()) {
                employee = new PoliceEmployee();
                // employee.setPageNo(((Page - 1) * 20 + count) + "");
                employee.setEmpid(rs.getString("EMP_ID"));
                employee.setFname(rs.getString("F_NAME"));
                employee.setMname(rs.getString("M_NAME"));
                employee.setLname(rs.getString("L_NAME"));
                employee.setEmpName(StringUtils.defaultString(employee.getFname(), "") + " " + StringUtils.defaultString(employee.getMname(), "") + " " + StringUtils.defaultString(employee.getLname(), ""));
                employee.setGpfno(rs.getString("gpf_no"));
                employee.setPost(rs.getString("POST"));
                employee.setDob(CommonFunctions.getFormattedOutputDate4(rs.getDate("dob")));
                employee.setDoeGov(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe_gov")));
                employee.setFatherName(rs.getString("father_name"));
                employeeList.add(employee);
            }
            empSearchResult.setEmployeeList(employeeList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empSearchResult;
    }

    @Override
    public List policePostingDetails(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List postingdatalist = new ArrayList();
        PoliceMISReportBean misbean = null;
        try {
            con = this.repodataSource.getConnection();

            /*String sql = "select emp_join.*,spn from emp_join" +
             " left outer join g_spc on emp_join.spc=g_spc.spc where emp_id=? order by join_date";*/
            /*String sql = "select DIST_NAME,G_OFFICE.ddo_code,emp_join.*,post,spn from emp_join"
             + " left outer join g_spc on emp_join.spc=g_spc.spc"
             + " left outer join g_post on g_spc.gpc=g_post.post_code"
             + " LEFT OUTER JOIN G_OFFICE ON g_spc.OFF_CODE=G_OFFICE.OFF_CODE"
             + " LEFT OUTER JOIN G_DISTRICT ON G_OFFICE.DIST_CODE=G_DISTRICT.DIST_CODE"
             + " where emp_join.emp_id=? order by join_date";*/
            String sql = "select EMP_NOTIFICATION.NOT_TYPE,post,spn,DIST_NAME,G_OFFICE.ddo_code,emp_join.JOIN_DATE,RLV_DATE from emp_join"
                    + " INNER JOIN EMP_NOTIFICATION ON emp_join.NOT_ID = EMP_NOTIFICATION.NOT_ID"
                    + " left outer join g_spc on emp_join.spc=g_spc.spc"
                    + " left outer join g_post on g_spc.gpc=g_post.post_code"
                    + " LEFT OUTER JOIN G_OFFICE ON g_spc.OFF_CODE=G_OFFICE.OFF_CODE"
                    + " LEFT OUTER JOIN G_DISTRICT ON G_OFFICE.DIST_CODE=G_DISTRICT.DIST_CODE"
                    + " LEFT OUTER JOIN EMP_RELIEVE ON emp_join.JOIN_ID = EMP_RELIEVE.JOIN_ID"
                    + " where emp_join.emp_id=? and (post is not null and post <> '') order by join_date";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            while (rs.next()) {
                misbean = new PoliceMISReportBean();
                misbean.setPostingType(rs.getString("not_type"));
                //misbean.setPostingDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("join_date")));
                misbean.setPostingDesignation(rs.getString("post"));
                //misbean.setPostingDDOCode(rs.getString("ddo_code"));
                misbean.setPostingDistrict(rs.getString("DIST_NAME"));
                misbean.setPostingfromDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("join_date")));
                misbean.setPostingtoDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("RLV_DATE")));
                postingdatalist.add(misbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return postingdatalist;
    }

    @Override
    public List PolicePostingListSameDistrict(PoliceMISReportForm policeform) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List policelist = new ArrayList();
        PoliceMISReportBean misbean = null;
        try {
            con = this.repodataSource.getConnection();

            /*String sql = "select emp_id,gpf_no,f_name,ARRAY_TO_STRING(ARRAY[F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,dob,category,doe_gov,cur_spc,join_date,post from" +
             " (select emp_id,gpf_no,f_name,m_name,l_name,dob,category,doe_gov,cur_spc,join_date from" +
             " (select emp_mast.emp_id,gpf_no,f_name,m_name,l_name,dob,category,doe_gov,cur_spc,join_date,(DATE_PART('year',CURRENT_DATE) - DATE_PART('year',join_date::date))datediff from emp_mast" +
             " inner join emp_join on emp_mast.cur_spc=emp_join.spc" +
             " where cur_off_code='OLSHOM0010000')temp1 where datediff > 5)temp2" +
             " inner join g_spc on temp2.cur_spc=g_spc.spc" +
             " inner join g_post on g_spc.gpc=g_post.post_code order by f_name";*/
            String sql = "select emp_id,gpf_no,f_name,ARRAY_TO_STRING(ARRAY[F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,dob,temp2.category,doe_gov,cur_spc,cur_off_code,join_date,post from"
                    + " (select emp_id,gpf_no,f_name,m_name,l_name,dob,category,doe_gov,cur_spc,cur_off_code,join_date from"
                    + " (select emp_mast.emp_id,gpf_no,f_name,m_name,l_name,dob,emp_mast.category,doe_gov,cur_spc,cur_off_code,join_date,(DATE_PART('year',CURRENT_DATE) - DATE_PART('year',join_date::date))datediff from emp_mast"
                    + " inner join emp_join on emp_mast.emp_id=emp_join.emp_id and emp_mast.cur_spc=emp_join.spc)temp1 where datediff > 5 order by f_name)temp2"
                    + " inner join g_spc on temp2.cur_spc=g_spc.spc"
                    + " inner join g_post on g_spc.gpc=g_post.post_code"
                    + " inner join g_office on temp2.cur_off_code=g_office.off_code where g_office.department_code='14'";
            if (policeform.getSltRank() != null && !policeform.getSltRank().equals("")) {
                sql = sql + " and post_code=?";
            }
            if (policeform.getSltDistrict() != null && !policeform.getSltDistrict().equals("")) {
                sql = sql + " and g_office.dist_code=?";
            }
            sql = sql + " order by post";
            pst = con.prepareStatement(sql);
            if (policeform.getSltRank() != null && !policeform.getSltRank().equals("")) {
                pst.setString(1, policeform.getSltRank());
            }
            if (policeform.getSltDistrict() != null && !policeform.getSltDistrict().equals("")) {
                pst.setString(2, policeform.getSltDistrict());
            }
            rs = pst.executeQuery();
            while (rs.next()) {
                misbean = new PoliceMISReportBean();
                misbean.setEmpid(rs.getString("emp_id"));
                misbean.setGpfno(rs.getString("gpf_no"));
                misbean.setEmpname(rs.getString("EMP_NAME"));
                misbean.setCategory(rs.getString("category"));
                misbean.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                misbean.setDesignation(rs.getString("post"));
                misbean.setCurPostJoiningDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("join_date")));
                policelist.add(misbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return policelist;
    }

    @Override
    public List PolicePostingList(PoliceMISReportForm policeform) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List policelist = new ArrayList();
        PoliceMISReportBean misbean = null;
        try {
            con = this.repodataSource.getConnection();

            /*String sql = "select emp_id,gpf_no,f_name,ARRAY_TO_STRING(ARRAY[F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,dob,doe_gov,dos,cur_spc,cur_off_code,post,temp2.category,off_en from"
                    + " (select emp_mast.emp_id,gpf_no,f_name,m_name,l_name,dob,emp_mast.category,doe_gov,cur_spc,cur_off_code,dos from emp_mast)temp2"
                    + " inner join g_spc on temp2.cur_spc=g_spc.spc"
                    + " inner join g_post on g_spc.gpc=g_post.post_code"
                    + " inner join g_office on temp2.cur_off_code=g_office.off_code where g_office.department_code='14'";*/
            String sql = "select emp_id,gpf_no,f_name,ARRAY_TO_STRING(ARRAY[F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,dob,doe_gov,dos,cur_spc,cur_off_code,post,emp_mast.category,off_en," +
                        " (select off_en from emp_notification inner join g_office on emp_notification.ent_off=g_office.off_code where emp_id=emp_mast.emp_id and not_type='FIRST_APPOINTMENT')ent_off" +
                        " from emp_mast" +
                        " inner join g_spc on emp_mast.cur_spc=g_spc.spc" +
                        " inner join g_post on g_spc.gpc=g_post.post_code" +
                        " inner join g_office on emp_mast.cur_off_code=g_office.off_code where g_office.department_code='14'";
            if (policeform.getSltRank() != null && !policeform.getSltRank().equals("")) {
                sql = sql + " and post_code=?";
            }
            if (policeform.getSltDistrict() != null && !policeform.getSltDistrict().equals("")) {
                if (policeform.getSltDistrict().length() > 4) {
                    sql = sql + " and g_office.off_code=?";
                } else {
                    sql = sql + " and g_office.dist_code=?";
                }
            }
            sql = sql + " order by F_NAME";
            pst = con.prepareStatement(sql);
            if (policeform.getSltRank() != null && !policeform.getSltRank().equals("")) {
                pst.setString(1, policeform.getSltRank());
            }
            if (policeform.getSltDistrict() != null && !policeform.getSltDistrict().equals("")) {
                pst.setString(2, policeform.getSltDistrict());
            }
            rs = pst.executeQuery();
            while (rs.next()) {
                misbean = new PoliceMISReportBean();
                misbean.setEmpid(rs.getString("emp_id"));
                misbean.setGpfno(rs.getString("gpf_no"));
                misbean.setEmpname(rs.getString("EMP_NAME"));
                misbean.setDob(CommonFunctions.getFormattedOutputDate1(rs.getDate("dob")));
                misbean.setDoj(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe_gov")));
                misbean.setDos(CommonFunctions.getFormattedOutputDate1(rs.getDate("dos")));
                misbean.setDesignation(rs.getString("post"));
                misbean.setCategory(rs.getString("category"));
                misbean.setOffname(rs.getString("ent_off"));
                policelist.add(misbean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return policelist;
    }

    @Override
    public void generatePolicePostingListExcel(List policepostinglistdata, WritableWorkbook workbook, String fileName) {

        try {
            WritableSheet sheet = workbook.createSheet(fileName, 0);
            WritableFont headformat = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            WritableCellFormat headcell3 = new WritableCellFormat(headformat);
            headcell3.setAlignment(Alignment.LEFT);
            headcell3.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell3.setWrap(true);

            WritableCellFormat headcell = new WritableCellFormat(headformat);
            headcell.setAlignment(Alignment.LEFT);
            headcell.setVerticalAlignment(VerticalAlignment.CENTRE);
            headcell.setWrap(true);
            headcell.setBorder(Border.ALL, BorderLineStyle.DOUBLE);

            WritableCellFormat innercell = new WritableCellFormat(NumberFormats.INTEGER);
            innercell.setAlignment(Alignment.LEFT);
            innercell.setVerticalAlignment(VerticalAlignment.CENTRE);
            innercell.setWrap(true);

            int col = 0;
            int widthInChars = 15;

            Label label = null;
            jxl.write.Number num = null;

            label = new Label(0, 0, "HRMS ID", headcell);//column,row
            sheet.setColumnView(col, widthInChars);            
            sheet.addCell(label);
            
            col++;
            widthInChars = 20;
            label = new Label(1, 0, "GPF NO", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            sheet.addCell(label);
            
            col++;
            widthInChars = 30;            
            label = new Label(2, 0, "EMPLOYEE NAME", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            sheet.addCell(label);
            
            col++;
            widthInChars = 16;
            label = new Label(3, 0, "DOB", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            sheet.addCell(label);
            
            col++;
            widthInChars = 16;            
            label = new Label(4, 0, "DOJ", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            sheet.addCell(label);
            
            col++;
            widthInChars = 16;            
            label = new Label(5, 0, "DOS", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            sheet.addCell(label);
            
            col++;
            widthInChars = 40;            
            label = new Label(6, 0, "CURRENT POST", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            sheet.addCell(label);
            
            col++;
            widthInChars = 40;
            label = new Label(7, 0, "OFFICE", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            sheet.addCell(label);
            
            col++;
            widthInChars = 20;            
            label = new Label(8, 0, "CATEGORY", headcell);//column,row
            sheet.setColumnView(col, widthInChars);
            sheet.addCell(label);
            
            int row = 0;
            int slno = 0;

            col = 0;

            if (policepostinglistdata != null && policepostinglistdata.size() > 0) {
                PoliceMISReportBean pmisbean = null;
                for (int i = 0; i < policepostinglistdata.size(); i++) {
                    slno += 1;
                    row += 1;

                    pmisbean = (PoliceMISReportBean) policepostinglistdata.get(i);
                    
                    col++;
                    widthInChars = 15;
                    label = new Label(0, row, pmisbean.getEmpid(), innercell);//column,row
                    sheet.setColumnView(col, widthInChars);
                    sheet.addCell(label);
                    
                    col++;
                    widthInChars = 20;                    
                    label = new Label(1, row, pmisbean.getGpfno(), innercell);//column,row
                    sheet.setColumnView(col, widthInChars);
                    sheet.addCell(label);
                    
                    col++;
                    widthInChars = 30;                    
                    label = new Label(2, row, pmisbean.getEmpname(), innercell);//column,row
                    sheet.setColumnView(col, widthInChars);
                    sheet.addCell(label);
                    
                    col++;
                    widthInChars = 16;                    
                    label = new Label(3, row, pmisbean.getDob(), innercell);//column,row
                    sheet.setColumnView(col, widthInChars);
                    sheet.addCell(label);
                    
                    col++;
                    widthInChars = 16;                    
                    label = new Label(4, row, pmisbean.getDoj(), innercell);//column,row
                    sheet.setColumnView(col, widthInChars);
                    sheet.addCell(label);
                    
                    col++;
                    widthInChars = 16;                    
                    label = new Label(5, row, pmisbean.getDos(), innercell);//column,row
                    sheet.setColumnView(col, widthInChars);
                    sheet.addCell(label);
                    
                    col++;
                    widthInChars = 40;                    
                    label = new Label(6, row, pmisbean.getDesignation(), innercell);//column,row
                    sheet.setColumnView(col, widthInChars);
                    sheet.addCell(label);
                    
                    col++;
                    widthInChars = 40;                    
                    label = new Label(7, row, pmisbean.getOffname(), innercell);//column,row
                    sheet.setColumnView(col, widthInChars);
                    sheet.addCell(label);
                    
                    col++;
                    widthInChars = 20;                    
                    label = new Label(8, row, pmisbean.getCategory(), innercell);//column,row
                    sheet.setColumnView(col, widthInChars);
                    sheet.addCell(label);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List getPoliceEstablishmentOfficeList() {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        ArrayList establishmentList = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("select * from g_district where state_code='21' order by dist_name");
            rs = pst.executeQuery();
            while (rs.next()) {
                District district = new District();
                district.setDistCode(rs.getString("dist_code"));
                district.setDistName(rs.getString("dist_name"));
                establishmentList.add(district);
            }

            pst = con.prepareStatement("select FULL_NAME off_name,user_off_code off_code from user_master u inner join"
                    + " user_details d on u.user_id::VARCHAR=d.linkid where usertype='H' ORDER BY off_name");
            rs = pst.executeQuery();
            while (rs.next()) {
                District district = new District();
                district.setDistCode(rs.getString("off_code"));
                district.setDistName(rs.getString("off_name"));
                establishmentList.add(district);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return establishmentList;
    }
}
