/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.reinstatement;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.model.reinstatement.Reinstatement;
import hrms.model.reinstatement.ReinstatementList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author lenovo pc
 */
public class ReinstatementDAOImpl implements ReinstatementDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    protected ServiceBookLanguageDAO sbDAO;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setSbDAO(ServiceBookLanguageDAO sbDAO) {
        this.sbDAO = sbDAO;
    }

    public int insertReinstatementData(Reinstatement reinstate) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            Date outputDate = null;
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("INSERT INTO EMP_REINSTATEMENT(reinstatement_id, EMP_ID, DOE, TOE, IF_ASSUMED, ORDNO, ORDDT, AUTH_DEPT, AUTH_OFF, AUTH, NOTE,  ENT_DEPT, ENT_OFF, ENT_AUTH, NOT_ID,wefdate,weftime) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pst.setInt(1, Integer.parseInt(reinstate.getSpId()));
            pst.setString(2, reinstate.getEmpid());
            pst.setTimestamp(3, new Timestamp(new Date().getTime()));
            pst.setString(4, reinstate.getToe());
            pst.setString(5, reinstate.getIfAssumed());
            pst.setString(6, reinstate.getTxtNotOrdNo());
            if (reinstate.getTxtNotOrdDt() != null && !reinstate.getTxtNotOrdDt().equals("")) {
                outputDate = dateFormat.parse(reinstate.getTxtNotOrdDt());
                pst.setTimestamp(7, new Timestamp(outputDate.getTime()));
            } else {
                pst.setTimestamp(7, null);
            }

            pst.setString(8, reinstate.getSltDept());
            pst.setString(9, reinstate.getSltOffice());
            pst.setString(10, reinstate.getNotifyingPostName());

            pst.setString(11, reinstate.getNote());
            //pst.setString(12, reinstate.getIfVisible());
            pst.setString(12, reinstate.getEntDept());
            pst.setString(13, reinstate.getEntOffice());
            pst.setString(14, reinstate.getEntAuth());
            pst.setInt(15, reinstate.getHnotid());
            if (reinstate.getWefdate() != null && !reinstate.getWefdate().equals("")) {
                Date wefDate = dateFormat.parse(reinstate.getWefdate());
                pst.setTimestamp(16, new Timestamp(wefDate.getTime()));
            } else {
                pst.setTimestamp(16, null);
            }
            pst.setString(17, reinstate.getSltweftime());
            pst.executeUpdate();

            if (reinstate.getRdTransaction() != null && !reinstate.getRdTransaction().equals("") && reinstate.getRdTransaction().equals("C")) {
                pst = con.prepareStatement("UPDATE EMP_MAST SET DEP_CODE='02' WHERE EMP_ID=?");
                pst.setString(1, reinstate.getEmpid());
                pst.execute();
            }

            /*
             * Updating the Service Book Language
             */
            String sbLang = sbDAO.getReinstateLanguage(reinstate);
            pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION =? WHERE NOT_ID=? ");
            pst.setString(1, sbLang);
            pst.setInt(2, reinstate.getHnotid());
            pst.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    public int updateReinstatementData(Reinstatement reinstate) {
        int n = 0;
        PreparedStatement pst = null;
        Connection con = null;
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            Date outputDate = null;

            con = dataSource.getConnection();
            pst = con.prepareStatement("UPDATE EMP_REINSTATEMENT SET EMP_ID=?, DOE=?, TOE=?, IF_ASSUMED=?, ORDNO=?, ORDDT=?, AUTH_DEPT=?, AUTH_OFF=?, AUTH=?, NOTE=?, ENT_DEPT=?, ENT_OFF=?, ENT_AUTH=?,wefdate=?,weftime=? WHERE reinstatement_id=?");
            pst.setString(1, reinstate.getEmpid());
            pst.setTimestamp(2, new Timestamp(new Date().getTime()));
            pst.setString(3, reinstate.getToe());
            pst.setString(4, reinstate.getIfAssumed());
            pst.setString(5, reinstate.getTxtNotOrdNo());
            if (reinstate.getTxtNotOrdDt() != null && !reinstate.getTxtNotOrdDt().equals("")) {
                outputDate = dateFormat.parse(reinstate.getTxtNotOrdDt());
                pst.setTimestamp(6, new Timestamp(outputDate.getTime()));
            } else {
                pst.setTimestamp(6, null);
            }

            pst.setString(7, reinstate.getSltDept());
            pst.setString(8, reinstate.getSltOffice());
            pst.setString(9, reinstate.getNotifyingPostName());
            pst.setString(10, reinstate.getNote());
            pst.setString(11, reinstate.getEntDept());
            pst.setString(12, reinstate.getEntOffice());
            pst.setString(13, reinstate.getEntAuth());
            if (reinstate.getWefdate() != null && !reinstate.getWefdate().equals("")) {
                Date wefDate = dateFormat.parse(reinstate.getWefdate());
                pst.setTimestamp(14, new Timestamp(wefDate.getTime()));
            } else {
                pst.setTimestamp(14, null);
            }
            pst.setString(15, reinstate.getSltweftime());
            pst.setInt(16, Integer.parseInt(reinstate.getSpId()));
            n = pst.executeUpdate();

            if (reinstate.getRdTransaction() != null && !reinstate.getRdTransaction().equals("") && reinstate.getRdTransaction().equals("C")) {
                pst = con.prepareStatement("UPDATE EMP_MAST SET DEP_CODE='02' WHERE EMP_ID=?");
                pst.setString(1, reinstate.getEmpid());
                pst.execute();
            }

            /*
             * Updating the Service Book Language
             */
            String sbLang = sbDAO.getReinstateLanguage(reinstate);
            pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION =? WHERE NOT_ID=? ");
            pst.setString(1, sbLang);
            pst.setInt(2, reinstate.getHnotid());
            pst.execute();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    public int deleteReinstatement(String reinstateId) {
        int n = 0;
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("DELETE FROM EMP_REINSTATEMENT WHERE reinstatement_id=?");
            pst.setInt(1, Integer.parseInt(reinstateId));
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return n;
    }

    public Reinstatement editReinstatement(String reinstateId) {

        Reinstatement reinstate = new Reinstatement();

        Connection con = null;

        ResultSet rs = null;
        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("SELECT empres.NOT_ID, reinstatement_id, empres.EMP_ID, empres.DOE, empres.TOE, empres.IF_ASSUMED, empres.ORDNO, empres.ORDDT, \n"
                    + "AUTH_DEPT, AUTH_OFF, empres.AUTH,empres.NOTE, empres.ENT_DEPT,\n"
                    + "empres.ENT_OFF, empres.ENT_AUTH,wefdate,weftime,en.if_visible FROM \n"
                    + "(select * from EMP_REINSTATEMENT)empres\n"
                    + "inner join emp_notification en\n"
                    + "on empres.not_id=en.not_id\n"
                    + "WHERE reinstatement_id=?");
            pst.setInt(1, Integer.parseInt(reinstateId));
            rs = pst.executeQuery();
            if (rs.next()) {
                reinstate.setHnotid(rs.getInt("NOT_ID"));
                reinstate.setSpId(rs.getInt("reinstatement_id") + "");
                reinstate.setEmpid(rs.getString("EMP_ID"));
                reinstate.setDoe(rs.getString("DOE"));
                reinstate.setToe(rs.getString("TOE"));
                reinstate.setIfAssumed(rs.getString("IF_ASSUMED"));
                reinstate.setTxtNotOrdNo(rs.getString("ORDNO"));
                reinstate.setTxtNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("ORDDT")));
                reinstate.setSltDept(rs.getString("AUTH_DEPT"));
                reinstate.setSltOffice(rs.getString("AUTH_OFF"));
                reinstate.setHidTempAuthSpc(CommonFunctions.getSPN(con, rs.getString("AUTH")));
                reinstate.setNotifyingPostName(rs.getString("AUTH"));

                reinstate.setNote(rs.getString("NOTE"));
                reinstate.setEntDept(rs.getString("ENT_DEPT"));
                reinstate.setEntOffice(rs.getString("ENT_OFF"));
                reinstate.setEntAuth(rs.getString("ENT_AUTH"));

                reinstate.setWefdate(CommonFunctions.getFormattedOutputDate1(rs.getDate("wefdate")));
                reinstate.setSltweftime(rs.getString("weftime"));
                if(rs.getString("if_visible")!=null && rs.getString("if_visible").equals("N")){
                  reinstate.setChkNotSBPrint("Y");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return reinstate;
    }

    public List findAllReinstatement(String empId) {
        ReinstatementList reinstList = null;
        List list = null;
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        list = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT sus.emp_id, SUS.SP_ID AS SUSPID,SUS.DOE AS SDOE,SUS.ORDNO AS SORDNO,SUS.ORDDT AS SORDDT,SUS.AUTH AS SAUTH,"
                    + " SUS.WEFD AS SWEFD,SUS.WEFT AS SWEFT,RE.SP_ID AS RSPID,RE.DOE AS RDOE,RE.ORDNO AS RORDNO,RE.ORDDT AS RORDDT,"
                    + " RE.AUTH_DEPT AS RDEPT,RE.AUTH_OFF AS ROFFICE,RE.AUTH AS RAUTH,RE.SV_ID AS RSVID "
                    + " FROM (SELECT * FROM EMP_SUSPENSION WHERE EMP_ID=? AND HQ_FIX IS NULL OR HQ_FIX = '' OR HQ_FIX='N' ) "
                    + " SUS LEFT OUTER JOIN EMP_REINSTATEMENT RE ON SUS.SP_ID=RE.reinstatement_id ");
            pst.setString(1, empId);
            rs = pst.executeQuery();
            while (rs.next()) {
                reinstList = new ReinstatementList();
                reinstList.setSusdoe(rs.getString("SDOE"));
                reinstList.setSusordno(rs.getString("SORDNO"));
                reinstList.setSusordDate(rs.getString("SORDDT"));
                reinstList.setWefd(rs.getString("SWEFD"));
                list.add(reinstList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return list;
    }
}
