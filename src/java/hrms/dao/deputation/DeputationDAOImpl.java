package hrms.dao.deputation;

import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.common.Message;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.model.deputation.DeputationDataForm;
import hrms.model.deputation.DeputationListForm;
import hrms.model.joining.JoiningForm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class DeputationDAOImpl implements DeputationDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    protected MaxDeputationIdDAO maxDeputationIdDAO;

    protected ServiceBookLanguageDAO sbDAO;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setMaxDeputationIdDAO(MaxDeputationIdDAO maxDeputationIdDAO) {
        this.maxDeputationIdDAO = maxDeputationIdDAO;
    }

    public void setSbDAO(ServiceBookLanguageDAO sbDAO) {
        this.sbDAO = sbDAO;
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

    public String getInterstateSpn(String othSpc) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        String spn = "";
        try {
            con = this.dataSource.getConnection();

            if (othSpc != null && !othSpc.equals("")) {
                pst = con.prepareStatement("SELECT off_code, off_en FROM g_office WHERE off_code=?");
                pst.setString(1, othSpc);
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

    public String getForeignBodyPostName(String postcode) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        String postname = "";
        try {
            con = this.dataSource.getConnection();

            if (postcode != null && !postcode.equals("")) {
                pst = con.prepareStatement("SELECT post FROM g_post WHERE post_code=?");
                pst.setString(1, postcode);
                rs = pst.executeQuery();
                if (rs.next()) {
                    postname = rs.getString("post");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return postname;
    }

    @Override
    public List getCadreStatusList(String type) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        SelectOption so = null;

        List cslist = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT DISTINCT ACS,CADRE_STAT FROM G_CADRE_STAT WHERE TYPE=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, type);
            rs = pst.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("ACS"));
                so.setLabel(rs.getString("CADRE_STAT"));
                cslist.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return cslist;
    }

    @Override
    public List getSubCadreStatusList(String cadrestat) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        SelectOption so = null;

        List cslist = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            String sql = "SELECT DISTINCT ASCS,SUB_CADRE_STAT FROM G_CADRE_STAT WHERE ACS=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, cadrestat);
            rs = pst.executeQuery();
            while (rs.next()) {
                so = new SelectOption();
                so.setValue(rs.getString("ASCS"));
                so.setLabel(rs.getString("SUB_CADRE_STAT"));
                cslist.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return cslist;
    }

    @Override
    public void saveDeputation(DeputationDataForm deputationForm, int notid) {

        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "INSERT INTO EMP_DEPUTATION (NOT_TYPE, NOT_ID, EMP_ID, IF_EXTENSION, WEF_DATE, WEF_TIME, TILL_DATE, TILL_TIME) VALUES(?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, "DEPUTATION");
            pst.setInt(2, notid);
            pst.setString(3, deputationForm.getEmpid());
            pst.setString(4, deputationForm.getChkExtnDptnPrd());
            if (deputationForm.getTxtWEFrmDt() != null && !deputationForm.getTxtWEFrmDt().equals("")) {
                pst.setTimestamp(5, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(deputationForm.getTxtWEFrmDt()).getTime()));
            } else {
                pst.setTimestamp(5, null);
            }
            pst.setString(6, deputationForm.getSltWEFrmTime());
            if (deputationForm.getTxtTillDt() != null && !deputationForm.getTxtTillDt().equals("")) {
                pst.setTimestamp(7, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(deputationForm.getTxtTillDt()).getTime()));
            } else {
                pst.setTimestamp(7, null);
            }
            pst.setString(8, deputationForm.getSltTillTime());
            pst.executeUpdate();

            String sbLang = sbDAO.getDeputationDetails(deputationForm, "DEPUTATION", notid);

            pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION =?,entry_type=? WHERE NOT_ID=? ");
            pst.setString(1, sbLang);
            pst.setString(2, deputationForm.getRdTransaction());
            pst.setInt(3, notid);
            pst.execute();

            if (deputationForm.getRdTransaction() != null && deputationForm.getRdTransaction().equals("C")) {
                pst = con.prepareStatement("UPDATE EMP_MAST SET DEPUTATION_OFFCODE =?,DEPUTATION_SPC=? WHERE EMP_ID=?");
                pst.setString(1, deputationForm.getHidPostedOffCode());
                //pst.setString(2, deputationForm.getHidPostedOffCode());
                pst.setString(2, deputationForm.getHidPostedSpc());
                pst.setString(3, deputationForm.getEmpid());
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
    public void updateDeputation(DeputationDataForm deputationForm) {

        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();

            if (deputationForm.getDepId() != null && !deputationForm.getDepId().equals("")) {
                String sql = "UPDATE EMP_DEPUTATION SET IF_EXTENSION=?,WEF_DATE=?,WEF_TIME=?,TILL_DATE=?,TILL_TIME=? WHERE DEP_ID=? AND EMP_ID=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, deputationForm.getChkExtnDptnPrd());
                if (deputationForm.getTxtWEFrmDt() != null && !deputationForm.getTxtWEFrmDt().equals("")) {
                    pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(deputationForm.getTxtWEFrmDt()).getTime()));
                } else {
                    pst.setTimestamp(2, null);
                }
                pst.setString(3, deputationForm.getSltWEFrmTime());
                if (deputationForm.getTxtTillDt() != null && !deputationForm.getTxtTillDt().equals("")) {
                    pst.setTimestamp(4, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(deputationForm.getTxtTillDt()).getTime()));
                } else {
                    pst.setTimestamp(4, null);
                }
                pst.setString(5, deputationForm.getSltTillTime());
                pst.setInt(6, Integer.parseInt(deputationForm.getDepId()));
                pst.setString(7, deputationForm.getEmpid());
                pst.executeUpdate();

                String sbLang = sbDAO.getDeputationDetails(deputationForm, "DEPUTATION", deputationForm.getHidNotId());

                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION =?,entry_type=? WHERE NOT_ID=? ");
                pst.setString(1, sbLang);
                pst.setString(2, deputationForm.getRdTransaction());
                pst.setInt(3, deputationForm.getHidNotId());
                pst.execute();

                if (deputationForm.getRdTransaction() != null && deputationForm.getRdTransaction().equals("C")) {
                    pst = con.prepareStatement("UPDATE EMP_MAST SET DEPUTATION_OFFCODE =?,DEPUTATION_SPC=? WHERE EMP_ID=?");
                    pst.setString(1, deputationForm.getHidPostedOffCode());
                    //pst.setString(2, deputationForm.getHidPostedOffCode());
                    pst.setString(2, deputationForm.getHidPostedSpc());
                    pst.setString(3, deputationForm.getEmpid());
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

    @Override
    public List getDeputationList(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List deputationlist = new ArrayList();
        DeputationListForm dptBean = null;

        try {
            con = dataSource.getConnection();

            /*String sql = "select dep_id,doe,emp_notification.not_id,emp_deputation.not_type,ordno,orddt from" +
             " (select emp_id,doe,not_id,not_type,ordno,orddt from emp_notification where emp_id=?" +
             " and not_type=?)emp_notification" +
             " inner join emp_deputation on emp_notification.not_id=emp_deputation.not_id";*/
            String sql = "select g_oth_spc.off_en oth_off_en,emp_notification.is_validated,g_office.off_en,spn,dep_id,emp_notification.doe,emp_notification.not_id,emp_deputation.not_type,ordno,orddt,next_spc,WEF_DATE, TILL_DATE,relieve_id,organization_type,auth from"
                    + " (select is_validated,emp_id,doe,not_id,not_type,ordno,orddt,organization_type,auth from emp_notification where emp_id=?"
                    + " and not_type=?)emp_notification"
                    + " left outer join emp_deputation on emp_notification.not_id=emp_deputation.not_id"
                    + " left outer join emp_transfer on emp_notification.not_id=emp_transfer.not_id"
                    + " left outer join emp_relieve on emp_notification.not_id=emp_relieve.not_id"
                    + " left outer join g_spc on emp_transfer.next_spc=g_spc.spc"
                    + " left outer join g_office on g_spc.off_code=g_office.off_code"
                    + " left outer join g_oth_spc on emp_transfer.off_code=g_oth_spc.other_spc_id::TEXT";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setString(2, "DEPUTATION");
            rs = pst.executeQuery();
            while (rs.next()) {
                dptBean = new DeputationListForm();
                dptBean.setDeptId(rs.getString("dep_id"));
                dptBean.setNotId(rs.getString("not_id"));
                dptBean.setNotType(rs.getString("not_type"));
                dptBean.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe")));
                dptBean.setNotOrdNo(rs.getString("ordno"));
                dptBean.setNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                if (rs.getString("organization_type") != null && rs.getString("organization_type").equals("GOI")) {
                    dptBean.setPostingOffice(getOtherSpn(rs.getString("next_spc")));
                } else if (rs.getString("organization_type") != null && rs.getString("organization_type").equals("ISO")) {
                    dptBean.setPostingOffice(getInterstateSpn(rs.getString("next_spc")));
                } else {
                    if (rs.getString("oth_off_en") != null && !rs.getString("oth_off_en").equals("")) {
                        dptBean.setPostingOffice(rs.getString("oth_off_en"));
                    } else {
                        dptBean.setPostingOffice(rs.getString("off_en"));
                    }

                    //dptBean.setPostingOffice(getOtherSpn(rs.getString("next_spc")));
                    //dptBean.setPostingOffice(rs.getString("off_en"));
                    //dptBean.setPostingPost(rs.getString("spn"));
                    //dptBean.setPostingPost(getOtherSpn(rs.getString("spn")));
                }
                dptBean.setPeriodFrom(CommonFunctions.getFormattedOutputDate1(rs.getDate("WEF_DATE")));
                dptBean.setPeriodTo(CommonFunctions.getFormattedOutputDate1(rs.getDate("TILL_DATE")));
                dptBean.setRelieveId(rs.getString("relieve_id"));
                dptBean.setIsValidated(rs.getString("is_validated"));
                deputationlist.add(dptBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return deputationlist;
    }

    @Override
    public int getDeputationListCount(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        int count = 0;
        try {
            con = dataSource.getConnection();

            String sql = "select count(*) cnt from (select emp_id,doe,not_id,not_type,ordno,orddt from emp_notification where emp_id=?"
                    + " and not_type=?)temp";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setString(2, "DEPUTATION");
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
    public DeputationDataForm getEmpDeputationData(String empid, int notId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        DeputationDataForm deptnBean = new DeputationDataForm();
        try {
            con = this.dataSource.getConnection();

            String sql = "select emp_mast.next_office_code,g_office.state_code,g_oth_spc.category,emp_notification.if_visible,spc1.spn notSpn,spc2.spn transSpn,emp_notification.ACS,emp_notification.ASCS,emp_notification.not_id,emp_notification.not_type,ordno,orddt,emp_notification.dept_code notDeptCode,"
                    + " emp_notification.off_code notOffCode,auth,note,dep_id,if_extension,wef_date,wef_time,till_date,till_time,tr_id,emp_transfer.dept_code transDeptCode,"
                    + " emp_transfer.off_code transOffCode,emp_transfer.next_spc transSPC,emp_transfer.field_off_code fieldOffCode,organization_type,organization_type_posting from"
                    + " (SELECT emp_id,not_id,dep_id,if_extension,wef_date,wef_time,till_date,till_time FROM emp_deputation WHERE EMP_ID=?"
                    + " AND NOT_ID=?)emp_deputation"
                    + " inner join (select if_visible,not_id,not_type,ordno,orddt,dept_code,off_code,auth,note,ACS,ASCS, organization_type,organization_type_posting from emp_notification where EMP_ID=?"
                    + " AND NOT_ID=?)emp_notification"
                    + " on emp_deputation.not_id=emp_notification.not_id"
                    + " inner join emp_mast on emp_deputation.emp_id=emp_mast.emp_id"
                    + " left outer join emp_transfer on emp_notification.not_id=emp_transfer.not_id"
                    + " left outer join g_office on emp_transfer.next_spc=g_office.off_code"
                    + " left outer join g_oth_spc on emp_transfer.next_spc=g_oth_spc.other_spc_id::TEXT"
                    + " left outer join g_spc spc1 on emp_notification.auth=spc1.spc"
                    + " left outer join g_spc spc2 on emp_transfer.next_spc=spc2.spc"
                    + " where emp_notification.not_type='DEPUTATION'";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setInt(2, notId);
            pst.setString(3, empid);
            pst.setInt(4, notId);
            rs = pst.executeQuery();
            if (rs.next()) {
                deptnBean.setHidNotId(rs.getInt("not_id"));
                deptnBean.setHidNotifyingDeptCode(rs.getString("notDeptCode"));
                deptnBean.setHidNotifyingOffCode(rs.getString("notOffCode"));
                deptnBean.setHidNotiSpc(rs.getString("auth"));
                deptnBean.setNotifyingSpc(rs.getString("notSpn"));
                deptnBean.setTxtNotOrdNo(rs.getString("ordno"));
                deptnBean.setTxtNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                deptnBean.setNote(rs.getString("note"));

                deptnBean.setDepId(rs.getString("dep_id"));
                deptnBean.setRadpostingauthtype(rs.getString("organization_type"));
                deptnBean.setRadnotifyingauthtype(rs.getString("organization_type_posting"));
                deptnBean.setHidTransferId(rs.getString("tr_id"));
                deptnBean.setHidPostedDeptCode(rs.getString("transDeptCode"));
                deptnBean.setHidPostedOffCode(rs.getString("transOffCode"));
                deptnBean.setHidInterState(rs.getString("state_code"));
                deptnBean.setNextOfficeCode(rs.getString("next_office_code"));
                if (rs.getString("organization_type") != null && rs.getString("organization_type").equals("GOI")) {
                    deptnBean.setHidGOICategory(rs.getString("category"));
                    deptnBean.setHidPostedOthSpc(rs.getString("transSPC"));
                    deptnBean.setPostedSpc(getOtherSpn(rs.getString("transSPC")));
                } else if (rs.getString("organization_type") != null && rs.getString("organization_type").equals("ISO")) {
                    deptnBean.setHidInterStateSPC(rs.getString("transSPC"));
                    deptnBean.setPostedSpc(getInterstateSpn(rs.getString("transSPC")));
                } else {
                    deptnBean.setHidPostedSpc(rs.getString("transSPC"));
                    //deptnBean.setPostedSpc(rs.getString("transSpn"));
                    if (deptnBean.getNextOfficeCode() != null && !deptnBean.getNextOfficeCode().equals("")) {
                        deptnBean.setPostedSpc(getForeignBodyPostName(rs.getString("transSPC")));
                    } else {
                        deptnBean.setPostedSpc(rs.getString("transSpn"));
                    }
                }

                if (rs.getString("organization_type_posting") != null && rs.getString("organization_type_posting").equals("GOI")) {
                    deptnBean.setHidNotifyingOthSpc(rs.getString("auth"));
                    deptnBean.setNotifyingSpc(getOtherSpn(rs.getString("auth")));
                }

                //deptnBean.setPostedSpn(rs.getString("transSpn"));
                deptnBean.setSltFieldOffice(rs.getString("fieldOffCode"));
                //deptnBean.setHidFieldOffice(rs.getString("fieldOffCode"));
                deptnBean.setSltCadreStatus(rs.getString("ACS"));
                //deptnBean.setSltSubCadreStatus(rs.getString("ASCS"));
                deptnBean.setSltSubCadreStatus(rs.getString("ASCS"));
                deptnBean.setChkExtnDptnPrd(rs.getString("if_extension"));
                deptnBean.setTxtWEFrmDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("wef_date")));
                deptnBean.setSltWEFrmTime(rs.getString("wef_time"));
                deptnBean.setTxtTillDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("till_date")));
                deptnBean.setSltTillTime(rs.getString("till_time"));
                if (rs.getString("if_visible") != null && rs.getString("if_visible").equals("N")) {
                    deptnBean.setChkNotSBPrint("Y");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return deptnBean;
    }

    @Override
    public List getAGDeputationList(String empid) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        List deputationlist = new ArrayList();
        DeputationListForm dptBean = null;

        try {
            con = dataSource.getConnection();

            /*String sql = "select dep_id,doe,emp_notification.not_id,emp_deputation.not_type,ordno,orddt from" +
             " (select emp_id,doe,not_id,not_type,ordno,orddt from emp_notification where emp_id=?" +
             " and not_type=?)emp_notification" +
             " inner join emp_deputation on emp_notification.not_id=emp_deputation.not_id";*/
            String sql = "select dep_id,doe,emp_notification.not_id,emp_deputation.not_type,ordno,orddt,WEF_DATE, TILL_DATE from"
                    + " (select emp_id,doe,not_id,not_type,ordno,orddt from emp_notification where emp_id=?"
                    + " and not_type=?)emp_notification"
                    + "  inner join emp_deputation on emp_notification.not_id=emp_deputation.not_id ";
            //System.out.println("sql===="+sql);
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setString(2, "DEPUTATION_AG");
            rs = pst.executeQuery();
            while (rs.next()) {
                dptBean = new DeputationListForm();
                dptBean.setDeptId(rs.getString("dep_id"));
                dptBean.setNotId(rs.getString("not_id"));
                dptBean.setNotType(rs.getString("not_type"));
                dptBean.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe")));
                dptBean.setNotOrdNo(rs.getString("ordno"));
                dptBean.setNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                dptBean.setPeriodFrom(CommonFunctions.getFormattedOutputDate1(rs.getDate("WEF_DATE")));
                dptBean.setPeriodTo(CommonFunctions.getFormattedOutputDate1(rs.getDate("TILL_DATE")));
                deputationlist.add(dptBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return deputationlist;
    }

    @Override
    public void saveAGDeputation(DeputationDataForm deputationForm, int notid) {

        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "INSERT INTO EMP_DEPUTATION ( NOT_TYPE, NOT_ID, EMP_ID, IF_EXTENSION, WEF_DATE, WEF_TIME, TILL_DATE, TILL_TIME) VALUES(?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, "DEPUTATION_AG");
            pst.setInt(2, notid);
            pst.setString(3, deputationForm.getEmpid());
            pst.setString(4, deputationForm.getChkExtnDptnPrd());
            if (deputationForm.getTxtWEFrmDt() != null && !deputationForm.getTxtWEFrmDt().equals("")) {
                pst.setTimestamp(5, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(deputationForm.getTxtWEFrmDt()).getTime()));
            } else {
                pst.setTimestamp(5, null);
            }
            pst.setString(6, deputationForm.getSltWEFrmTime());
            if (deputationForm.getTxtWEFrmDt() != null && !deputationForm.getTxtWEFrmDt().equals("")) {
                pst.setTimestamp(7, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(deputationForm.getTxtTillDt()).getTime()));
            } else {
                pst.setTimestamp(7, null);
            }
            pst.setString(8, deputationForm.getSltTillTime());
            pst.executeUpdate();

            String sbLang = sbDAO.getDeputationAGLanguage(deputationForm);
            pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION=? WHERE NOT_ID=? ");
            pst.setString(1, sbLang);
            pst.setInt(2, notid);
            pst.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateAGDeputation(DeputationDataForm deputationForm) {

        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();

            if (deputationForm.getDepId() != null && !deputationForm.getDepId().equals("")) {
                String sql = "UPDATE EMP_DEPUTATION SET IF_EXTENSION=?,WEF_DATE=?,WEF_TIME=?,TILL_DATE=?,TILL_TIME=? WHERE DEP_ID=? AND EMP_ID=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, deputationForm.getChkExtnDptnPrd());
                if (deputationForm.getTxtWEFrmDt() != null && !deputationForm.getTxtWEFrmDt().equals("")) {
                    pst.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(deputationForm.getTxtWEFrmDt()).getTime()));
                } else {
                    pst.setTimestamp(2, null);
                }
                pst.setString(3, deputationForm.getSltWEFrmTime());
                if (deputationForm.getTxtTillDt() != null && !deputationForm.getTxtTillDt().equals("")) {
                    pst.setTimestamp(4, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(deputationForm.getTxtTillDt()).getTime()));
                } else {
                    pst.setTimestamp(4, null);
                }
                pst.setString(5, deputationForm.getSltTillTime());
                int deptId = Integer.parseInt(deputationForm.getDepId());
                pst.setInt(6, deptId);
                pst.setString(7, deputationForm.getEmpid());
                pst.executeUpdate();

                int notid = deputationForm.getHidNotId();
                String empid = deputationForm.getEmpid();

                String sbLang = sbDAO.getDeputationAGLanguage(deputationForm);
                // System.out.println("sbLang"+sbLang);
                pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION =? WHERE NOT_ID=? ");
                pst.setString(1, sbLang);
                pst.setInt(2, notid);
                pst.execute();

                //CommonFunctions.updateCadreStatus(deputationForm.getEmpid(), deputationForm.getSltCadreStatus(), deputationForm.getSltSubCadreStatus(), con);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public DeputationDataForm getEmpAGDeputationData(String empid, int notId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        DeputationDataForm deptnBean = new DeputationDataForm();
        try {
            con = this.dataSource.getConnection();

            String sql = "select organization_type,spc1.spn notSpn,emp_notification.if_visible,emp_notification.not_id,emp_notification.not_type,ordno,orddt,emp_notification.dept_code notDeptCode,"
                    + " emp_notification.off_code notOffCode,auth,note,dep_id,if_extension,wef_date,wef_time,till_date,till_time"
                    + " from (SELECT emp_id,not_id,dep_id,if_extension,wef_date,wef_time,till_date,till_time FROM emp_deputation WHERE EMP_ID=?"
                    + " AND NOT_ID=?)emp_deputation"
                    + " inner join (select organization_type,not_id,not_type,ordno,orddt,dept_code,off_code,auth,note,if_visible from emp_notification where EMP_ID=?"
                    + " AND NOT_ID=?)emp_notification"
                    + " on emp_deputation.not_id=emp_notification.not_id"
                    + "  inner join emp_mast on emp_deputation.emp_id=emp_mast.emp_id"
                    + "  left outer join g_spc spc1 on emp_notification.auth=spc1.spc"
                    + " where emp_notification.not_type='DEPUTATION_AG'";
            // System.out.println("sql:"+sql);
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setInt(2, notId);
            pst.setString(3, empid);
            pst.setInt(4, notId);
            rs = pst.executeQuery();
            if (rs.next()) {
                deptnBean.setHidNotId(rs.getInt("not_id"));
                deptnBean.setHidNotifyingDeptCode(rs.getString("notDeptCode"));
                deptnBean.setHidNotifyingOffCode(rs.getString("notOffCode"));
                deptnBean.setHidNotiSpc(rs.getString("auth"));
                //deptnBean.setHidPostedDeptCode(rs.getString("notSpn"));
                deptnBean.setNotifyingSpc(rs.getString("notSpn"));
                deptnBean.setTxtNotOrdNo(rs.getString("ordno"));
                deptnBean.setTxtNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                deptnBean.setNote(rs.getString("note"));

                deptnBean.setDepId(rs.getString("dep_id"));
                deptnBean.setChkExtnDptnPrd(rs.getString("if_extension"));
                deptnBean.setTxtWEFrmDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("wef_date")));
                deptnBean.setSltWEFrmTime(rs.getString("wef_time"));
                deptnBean.setTxtTillDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("till_date")));
                deptnBean.setSltTillTime(rs.getString("till_time"));
                if (rs.getString("if_visible") != null && rs.getString("if_visible").equals("N")) {
                    deptnBean.setChkNotSBPrint("Y");
                } else {
                    deptnBean.setChkNotSBPrint("N");
                }

                if (rs.getString("organization_type") != null && rs.getString("organization_type").equals("GOI")) {
                    deptnBean.setRadpostingauthtype("GOI");
                    deptnBean.setHidNotifyingOthSpc(rs.getString("auth"));
                    deptnBean.setNotifyingSpc(getOtherSpn(rs.getString("auth")));
                } else {
                    deptnBean.setRadpostingauthtype("GOO");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return deptnBean;
    }

    @Override
    public void saveAGLeaveContribution(DeputationDataForm deputationForm) {

        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();
            int notificationId = deputationForm.getHidNotId();
            String sql = "INSERT INTO EMP_CONTRIBUTION ( NOT_TYPE, NOT_ID, EMP_ID, CONT_TYPE, WEF_DATE, till_date, AMOUNT) VALUES(?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, "DEPUTATION_AG");
            pst.setInt(2, notificationId);
            pst.setString(3, deputationForm.getEmpid());
            pst.setString(4, deputationForm.getConType());
            if (deputationForm.getLeavewefDate() != null && !deputationForm.getLeavewefDate().equals("")) {
                pst.setTimestamp(5, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(deputationForm.getLeavewefDate()).getTime()));
            } else {
                pst.setTimestamp(5, null);
            }

            if (deputationForm.getLeavetillDate() != null && !deputationForm.getLeavetillDate().equals("")) {
                pst.setTimestamp(6, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(deputationForm.getLeavetillDate()).getTime()));
            } else {
                pst.setTimestamp(6, null);
            }
            String leave_amount = deputationForm.getLeaveAmount();
            String leave_amountVal = deputationForm.getLeaveAmount();
            if (leave_amount != null && !leave_amount.equals("")) {
                // leave_amount= String.format("%.2f", leave_amount);
                double roundAmount = Double.parseDouble(leave_amount);
                if ((roundAmount % 1) == 0) {
                    pst.setDouble(7, roundAmount);
                } else {
                    roundAmount = Math.round(roundAmount * 100.0) / 100.0;
                    pst.setDouble(7, roundAmount);
                }
                /*double roundAmount=Double.parseDouble(leave_amount);
                 DecimalFormat roundAmount1=new DecimalFormat("#.00"); 
                 pst.setDouble(7, Double.parseDouble(roundAmount1));*/

            } else {
                pst.setDouble(7, 0);
            }
            //pst.setString(8, deputationForm.getLeaveAmount());
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getEmpAGDeputationLeaveData(String empid, int notId, String type) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        List deputationlist = new ArrayList();
        DeputationDataForm deptnBean = null;
        try {
            con = this.dataSource.getConnection();

            String sql = "select *,to_char( amount, 'FM999999999.00') as amountFormat FROM emp_contribution WHERE EMP_ID=?  AND NOT_ID=? AND not_type=?";
            // System.out.println("empid==" + empid + "notid==" + notId);
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            pst.setInt(2, notId);
            pst.setString(3, "DEPUTATION_AG");

            rs = pst.executeQuery();
            while (rs.next()) {
                deptnBean = new DeputationDataForm();
                deptnBean.setHidNotId(rs.getInt("not_id"));
                deptnBean.setConType(rs.getString("cont_type"));
                deptnBean.setHidconId(rs.getString("cont_id"));
                deptnBean.setLeavewefDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("wef_date")));
                deptnBean.setLeavetillDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("till_date")));
                deptnBean.setLeaveAmount(rs.getString("amountFormat"));
                deputationlist.add(deptnBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return deputationlist;
    }

    @Override
    public void deleteDeputation(String empid, int notId) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();

            pst = con.prepareStatement("DELETE FROM emp_notification WHERE emp_id = ? AND not_id=?");
            pst.setString(1, empid);
            pst.setInt(2, notId);
            n = pst.executeUpdate();

            System.out.println(notId + "empid==" + empid);

            pst = con.prepareStatement("DELETE FROM EMP_TRANSFER WHERE emp_id = ? AND not_id=?");
            pst.setString(1, empid);
            pst.setInt(2, notId);
            n = pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void deleteAGDeputation(String empid, String cId, int notId) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        int cIdIntval = Integer.parseInt(cId);
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("DELETE FROM emp_contribution WHERE emp_id = ? AND cont_id=? AND not_id=?");
            pst.setString(1, empid);
            pst.setInt(2, cIdIntval);
            pst.setInt(3, notId);
            n = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public List getAllEmpDeputation(String deptCode, String offCode) {
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        List deputationlist = new ArrayList();
        DeputationDataForm deptnBean = null;
        try {
            con = dataSource.getConnection();

            String sql = "select EMP_ID,gpf_no,cur_off_code,getofficeen(cur_off_code)deputedFromOfc,NEXT_OFFICE_CODE,cur_spc,dep_code,\n"
                    + "ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') emp_name,deputation_spc,\n"
                    + "getpostnamefromspc(deputation_spc)deputationpost,deputation_offcode\n"
                    + "FROM EMP_MAST inner join\n"
                    + "(select distinct(a1.emp_id) deptempid\n"
                    + "from (select emp_transfer.dept_code,emp_transfer.off_code,emp_transfer.next_spc,\n"
                    + "emp_deputation.* from\n"
                    + "(emp_deputation inner join\n"
                    + "emp_transfer on emp_deputation.not_id=emp_transfer.not_id\n"
                    + "inner join emp_notification on emp_deputation.not_id=emp_notification.not_id ) where\n"
                    + "emp_transfer.dept_code=? and emp_deputation.not_type='DEPUTATION'\n"
                    + "and (extract(year from current_date)-extract(year from wef_date)=0\n"
                    + "or extract(year from current_date)-extract(year from wef_date)=1))a1)deptdetails\n"
                    + "on emp_mast.emp_id=deptdetails.deptempid  where "
                    + "cur_off_code=?  OR next_office_code=? ";

            pst = con.prepareStatement(sql);
            pst.setString(1, deptCode);
            pst.setString(2, offCode);
            pst.setString(3, offCode);
            rs = pst.executeQuery();
            while (rs.next()) {
                deptnBean = new DeputationDataForm();
                deptnBean.setEmpid(rs.getString("EMP_ID"));
                deptnBean.setGpfNo(rs.getString("gpf_no"));
                deptnBean.setEmpName(rs.getString("emp_name"));
                deptnBean.setNotifyingSpc(rs.getString("deputedFromOfc"));
                deptnBean.setPostedSpc(rs.getString("deputationpost"));
                deputationlist.add(deptnBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return deputationlist;
    }

    @Override
    public DeputationDataForm getEmpDeputation(String empId) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        DeputationDataForm deptnBean = null;

        try {
            con = dataSource.getConnection();
            String sql = "SELECT emp_id, gpf_no, array_to_string(array[initials,f_name,m_name,l_name],' ') AS full_name FROM emp_mast WHERE emp_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, empId);
            rs = pst.executeQuery();

            if (rs.next()) {
                deptnBean = new DeputationDataForm();
                deptnBean.setEmpid(rs.getString("emp_id"));
                deptnBean.setGpfNo(rs.getString("gpf_no"));
                deptnBean.setEmpName(rs.getString("full_name"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return deptnBean;
    }

}
