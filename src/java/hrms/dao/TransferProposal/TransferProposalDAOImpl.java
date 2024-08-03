/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.TransferProposal;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.TransferProposal.TransferProposalBean;
import hrms.model.TransferProposal.TransferProposalDetail;
import hrms.model.eDespatch.eDespatchBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author Manoj PC
 */
public class TransferProposalDAOImpl implements TransferProposalDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ArrayList getEmpList(String cadreCode, int proposalId) {
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        ArrayList al = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select emp_id, cur_spc, ARRAY_TO_STRING(ARRAY[F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,spn from emp_mast"
                    + " left outer join g_spc on emp_mast.cur_spc=g_spc.spc where cur_cadre_code = ?"
                    + " AND cur_spc IS NOT NULL  AND (SELECT COUNT(*) FROM emp_transfer_proposal_detail ET "
                    + " INNER JOIN emp_transfer_proposal EP ON ET.proposal_id = EP.proposal_id "
                    + " WHERE emp_id = emp_mast.emp_id  AND is_approved = 'N') = 0 order by EMP_NAME");
            pst.setString(1, cadreCode);

            rs = pst.executeQuery();
            while (rs.next()) {
                TransferProposalBean tpf = new TransferProposalBean();
                tpf.setEmpName(rs.getString("emp_name"));
                tpf.setPostedPostName(rs.getString("spn"));
                tpf.setEmpId(rs.getString("emp_id"));
                tpf.setOldSpc(rs.getString("cur_spc"));
                al.add(tpf);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return al;
    }

    @Override
    public ArrayList getCadreGradeWiseEmpList(String cadreCode, String cadreGrade, int proposalId) {
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        ArrayList al = new ArrayList();
        try {
            con = dataSource.getConnection();
            if(cadreGrade.equals("")){
                pst = con.prepareStatement("select emp_id, cur_spc, ARRAY_TO_STRING(ARRAY[F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,spn from emp_mast"
                        + " left outer join g_spc on emp_mast.cur_spc=g_spc.spc where cur_cadre_code = ?"
                        + " AND cur_spc IS NOT NULL  AND (SELECT COUNT(*) FROM emp_transfer_proposal_detail ET "
                        + " INNER JOIN emp_transfer_proposal EP ON ET.proposal_id = EP.proposal_id "
                        + " WHERE emp_id = emp_mast.emp_id  AND is_approved = 'N') = 0 order by gradation_sl_no, EMP_NAME");
                pst.setString(1, cadreCode);                
            }else{
                pst = con.prepareStatement("select emp_id, cur_spc, ARRAY_TO_STRING(ARRAY[F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,spn from emp_mast"
                        + " left outer join g_spc on emp_mast.cur_spc=g_spc.spc where cur_cadre_code = ? and cadre_grade=?"
                        + " AND cur_spc IS NOT NULL  AND (SELECT COUNT(*) FROM emp_transfer_proposal_detail ET "
                        + " INNER JOIN emp_transfer_proposal EP ON ET.proposal_id = EP.proposal_id "
                        + " WHERE emp_id = emp_mast.emp_id  AND is_approved = 'N') = 0 order by gradation_sl_no, EMP_NAME");
                pst.setString(1, cadreCode);
                pst.setString(2, cadreGrade);
            }
            rs = pst.executeQuery();
            while (rs.next()) {
                TransferProposalBean tpf = new TransferProposalBean();
                tpf.setEmpName(rs.getString("emp_name"));
                tpf.setPostedPostName(rs.getString("spn"));
                tpf.setEmpId(rs.getString("emp_id"));
                tpf.setOldSpc(rs.getString("cur_spc"));
                al.add(tpf);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return al;
    }

    @Override
    public void saveOtherInputs(TransferProposalBean tpBean) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("UPDATE emp_transfer_proposal SET letterheader = ?,letterfooter = ?,"
                    + "authorityname = ?,authoritydesg = ? WHERE proposal_id=?");
            pst.setString(1, tpBean.getLetterheader());
            pst.setString(2, tpBean.getLetterfooter());
            pst.setString(3, tpBean.getAuthorityName());
            pst.setString(4, tpBean.getAuthorityDesg());
            pst.setInt(5, tpBean.getProposalId());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, con);
        }
    }

    @Override
    public int addTransferProposal(String empId, String cadreCode, String spc, String ownerId, String gproposalId, String hasAdditional, String proposalType) {
        Connection con = null;
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        ResultSet rs = null;
        int proposalId = 0;
        String sql = null;
        try {
            con = dataSource.getConnection();
            if (Integer.parseInt(gproposalId) == 0) {
                sql = "INSERT INTO emp_transfer_proposal (date_of_entry, owner_id, proposal_type) VALUES(CURRENT_TIMESTAMP, ?,?)";
                pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pst.setString(1, ownerId);
                pst.setString(2, proposalType);
                pst.executeUpdate();
                rs = pst.getGeneratedKeys();
                rs.next();
                proposalId = rs.getInt(1);
            } else {
                proposalId = Integer.parseInt(gproposalId);
            }
            //Now insert into details table            
            sql = "INSERT INTO emp_transfer_proposal_detail (proposal_id,emp_id,cadre_code, old_spc) VALUES(?, ?, ?, ?)";
            pst1 = con.prepareStatement(sql);
            pst1.setInt(1, proposalId);
            pst1.setString(2, empId);
            pst1.setString(3, cadreCode);
            pst1.setString(4, spc);
            pst1.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return proposalId;
    }

    @Override
    public int updateNewCadrePromotion(String cadreCode, String ownerId, int gproposalId, String cadreGrade) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        int proposalId = 0;
        String sql = null;
        try {
            con = dataSource.getConnection();
            if (gproposalId == 0) {
                sql = "INSERT INTO emp_transfer_proposal (date_of_entry, owner_id,proposal_type, cadre_code, cadre_grade) VALUES(CURRENT_TIMESTAMP, ?,'P',?,?)";
                pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pst.setString(1, ownerId);
                pst.setString(2, cadreCode);
                pst.setString(3, cadreGrade);
                pst.executeUpdate();
                rs = pst.getGeneratedKeys();
                rs.next();
                proposalId = rs.getInt(1);
            } else {
                proposalId = gproposalId;
                sql = "UPDATE emp_transfer_proposal SET cadre_code=?,cadre_grade=? WHERE proposal_id=?";
                pst = con.prepareStatement(sql);
                pst.setString(1, cadreCode);
                pst.setString(2, cadreGrade);
                pst.setInt(3, proposalId);
                pst.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return proposalId;
    }

    @Override
    public String deletePostingInfo(int detailPostingId) {
        String status = "S";
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("delete from emp_transfer_proposal_detail_posting where detail_posting_id=?");
            pst.setInt(1, detailPostingId);
            pst.executeUpdate();

        } catch (Exception ex) {
            status = "F";
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst, con);
        }
        return status;
    }

    @Override
    public ArrayList getTransferEmployeeList(int proposalId, int letterno, String fileno) {
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        ArrayList al = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT detail_id,emp_mast.emp_id, cur_spc, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,spn from "
                    + " emp_transfer_proposal_detail "
                    + " left outer join g_spc GS on emp_transfer_proposal_detail.old_spc=GS.spc "
                    + " inner join emp_mast on emp_mast.emp_id=emp_transfer_proposal_detail.emp_id where proposal_id=?");
            pst.setInt(1, proposalId);
            rs = pst.executeQuery();

            while (rs.next()) {
                TransferProposalBean tpf = new TransferProposalBean();
                tpf.setTransferProposalDetailId(rs.getInt("detail_id"));
                tpf.setEmpName(rs.getString("emp_name"));
                tpf.setPostedPostName(rs.getString("spn"));
                tpf.setEmpId(rs.getString("emp_id"));
                tpf.setOldSpc(rs.getString("cur_spc"));
                tpf.setTransferProposalDetail(getTransferProposalDetail(tpf.getTransferProposalDetailId(), letterno, fileno));
                al.add(tpf);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return al;
    }

    @Override
    public ArrayList getTransferEmployeeList(int proposalId) {
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        ArrayList al = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("SELECT detail_id,emp_mast.emp_id, cur_spc, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,spn from "
                    + " emp_transfer_proposal_detail "
                    + " left outer join g_spc GS on emp_transfer_proposal_detail.old_spc=GS.spc "
                    + " inner join emp_mast on emp_mast.emp_id=emp_transfer_proposal_detail.emp_id where proposal_id=?");
            pst.setInt(1, proposalId);
            rs = pst.executeQuery();

            while (rs.next()) {
                TransferProposalBean tpf = new TransferProposalBean();
                tpf.setTransferProposalDetailId(rs.getInt("detail_id"));
                tpf.setEmpName(rs.getString("emp_name"));
                tpf.setPostedPostName(rs.getString("spn"));
                tpf.setEmpId(rs.getString("emp_id"));
                tpf.setOldSpc(rs.getString("cur_spc"));
                tpf.setTransferProposalDetail(getTransferProposalDetail(tpf.getTransferProposalDetailId()));
                al.add(tpf);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return al;
    }

    public ArrayList getTransferProposalDetail(int detailId, int letterno, String fileno) {
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        ArrayList al = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select detail_posting_id,detail_id,spn,is_additional,emp_transfer_proposal_detail_posting.new_spc from emp_transfer_proposal_detail_posting "
                    + "left outer join g_spc GS on emp_transfer_proposal_detail_posting.new_spc=GS.spc "
                    + "where detail_id=? order by is_additional");
            pst.setInt(1, detailId);
            rs = pst.executeQuery();
            while (rs.next()) {
                TransferProposalDetail transferProposalDetail = new TransferProposalDetail();
                transferProposalDetail.setDetailId(rs.getInt("detail_id"));
                transferProposalDetail.setDetailPostingId(rs.getInt("detail_posting_id"));
                transferProposalDetail.setNewspc(rs.getString("new_spc"));
                transferProposalDetail.setNewpost(rs.getString("spn"));
                transferProposalDetail.setIsadditional(rs.getString("is_additional"));
                transferProposalDetail.setLetterno(fileno + "/" + (letterno++));
                al.add(transferProposalDetail);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return al;
    }

    public ArrayList getTransferProposalDetail(int detailId) {
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        ArrayList al = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select detail_posting_id,detail_id,spn,is_additional,emp_transfer_proposal_detail_posting.new_spc from emp_transfer_proposal_detail_posting "
                    + "left outer join g_spc GS on emp_transfer_proposal_detail_posting.new_spc=GS.spc "
                    + "where detail_id=? order by is_additional");
            pst.setInt(1, detailId);
            rs = pst.executeQuery();
            while (rs.next()) {
                TransferProposalDetail transferProposalDetail = new TransferProposalDetail();
                transferProposalDetail.setDetailId(rs.getInt("detail_id"));
                transferProposalDetail.setDetailPostingId(rs.getInt("detail_posting_id"));
                transferProposalDetail.setNewspc(rs.getString("new_spc"));
                transferProposalDetail.setNewpost(rs.getString("spn"));
                transferProposalDetail.setIsadditional(rs.getString("is_additional"));
                al.add(transferProposalDetail);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return al;
    }

    @Override
    public String getTransferEmployee(int proposalId) {
        String content = "";
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        ArrayList al = new ArrayList();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select TD.detail_id, EM.emp_id, cur_spc, ARRAY_TO_STRING(ARRAY[INITIALS, F_NAME, M_NAME,L_NAME], ' ') EMP_NAME,spn from emp_mast EM"
                    + " left outer join g_spc GS on EM.cur_spc=GS.spc"
                    + " inner join emp_transfer_proposal_detail TD on EM.emp_id=TD.emp_id"
                    + " INNER JOIN emp_transfer_proposal TP ON TP.proposal_id = TD.proposal_id"
                    + " where TD.proposal_id = ?");
            pst.setInt(1, proposalId);
            rs = pst.executeQuery();
            content = "<table class=\"table table-striped table-bordered\" width=\"100%\" cellspacing=\"0\" style=\"margin-top:80px;\">"
                    + "                                            <tr style=\"font-weight:bold;background:#035F8B;color:#FFFFFF;font-size:13pt;\">"
                    + "                                                <td colspan=\"2\">Transfer Detail</td>"
                    + "                                            </tr>";

            while (rs.next()) {
                TransferProposalBean tpf = new TransferProposalBean();
                tpf.setEmpName(rs.getString("emp_name"));
                tpf.setPostedPostName(rs.getString("spn"));
                tpf.setEmpId(rs.getString("emp_id"));
                tpf.setOldSpc(rs.getString("cur_spc"));
                tpf.setTransferProposalId(rs.getInt("detail_id"));
                al.add(tpf);
                content += "                                            <tr>"
                        + "                                                <td>" + rs.getString("emp_name") + "<br /><span style='font-size:9pt;color:#008900;'>" + rs.getString("spn") + "</span></td>"
                        + "                                                <td width=\"10%\"><input type=\"button\" value=\"Posting\" style=\"background:#0379B4\" class=\"btn btn-success\"  /></td>\n"
                        + "                                            </tr>";
            }
            content += "                                        </table>";
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return content;
    }

    @Override
    public ArrayList getTransferProposalList(String empId, String proposalType) {
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        ArrayList al = new ArrayList();
        if (proposalType.equals("A")) {
            proposalType = "";
        }
        try {
            con = dataSource.getConnection();
            String sql = "SELECT order_number, order_date, proposal_id, is_approved, date_of_entry,proposal_type, (SELECT COUNT(*) FROM "
                    + " emp_transfer_proposal_detail WHERE ET.proposal_id = proposal_id) as num_emp "
                    + " FROM emp_transfer_proposal ET WHERE owner_id = ?";
            if (!proposalType.equals("")) {
                sql += " AND is_approved = ?";
            }
            sql += " ORDER BY date_of_entry DESC";
            pst = con.prepareStatement(sql);
            pst.setString(1, empId);
            if (!proposalType.equals("")) {
                pst.setString(2, proposalType);
            }
            rs = pst.executeQuery();
            while (rs.next()) {
                TransferProposalBean tpf = new TransferProposalBean();
                tpf.setProposalId(rs.getInt("proposal_id"));
                tpf.setDateofEntry(CommonFunctions.getFormattedOutputDate1(rs.getDate("date_of_entry")));
                tpf.setNumEmps(Integer.parseInt(rs.getString("num_emp")));
                tpf.setIsApproved(rs.getString("is_approved"));
                tpf.setOrderNumber(rs.getString("order_number"));
                tpf.setOrderDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("order_date")));
                tpf.setProposalType(rs.getString("proposal_type"));
                al.add(tpf);
            }
            /*
             sql = "SELECT order_number, order_date, proposal_id, is_approved, date_of_entry,proposal_type, (SELECT COUNT(*) FROM "
             + " emp_promotion_proposal_detail WHERE ET.proposal_id = proposal_id) as num_emp "
             + " FROM emp_transfer_proposal ET WHERE owner_id = ? and proposal_type='P'";
             if (!proposalType.equals("")) {
             sql += " AND is_approved = ?";
             }
             sql += " ORDER BY date_of_entry DESC";
             pst = con.prepareStatement(sql);
             pst.setString(1, empId);
             if (!proposalType.equals("")) {
             pst.setString(2, proposalType);
             }
             rs = pst.executeQuery();
             while (rs.next()) {
             TransferProposalBean tpf = new TransferProposalBean();
             tpf.setProposalId(rs.getString("proposal_id"));
             tpf.setDateofEntry(CommonFunctions.getFormattedOutputDate1(rs.getDate("date_of_entry")));
             tpf.setNumEmps(Integer.parseInt(rs.getString("num_emp")));
             tpf.setIsApproved(rs.getString("is_approved"));
             tpf.setOrderNumber(rs.getString("order_number"));
             tpf.setOrderDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("order_date")));
             tpf.setProposalType(rs.getString("proposal_type"));
             al.add(tpf);
             }*/
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return al;
    }

    @Override
    public TransferProposalBean getTransferProposalListDetail(int proposalId) {
        TransferProposalBean tpf = new TransferProposalBean();
        ResultSet rs = null;
        PreparedStatement pst = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();

            String sql = "SELECT file_no,order_number, order_date, proposal_id, is_approved, date_of_entry, proposal_type, cadre_name,cadre_grade,pay_level, "
                    + "letterheader, letterfooter, authorityname, authoritydesg FROM emp_transfer_proposal "
                    + "left outer join g_cadre on emp_transfer_proposal.cadre_code = g_cadre.cadre_code WHERE proposal_id=?";

            pst = con.prepareStatement(sql);
            pst.setInt(1, proposalId);
            rs = pst.executeQuery();
            while (rs.next()) {
                tpf.setProposalId(rs.getInt("proposal_id"));
                tpf.setDateofEntry(CommonFunctions.getFormattedOutputDate1(rs.getDate("date_of_entry")));
                tpf.setIsApproved(rs.getString("is_approved"));
                tpf.setOrderNumber(rs.getString("file_no") + "/" + rs.getString("order_number"));
                tpf.setFileno(rs.getString("file_no"));
                tpf.setLetterno(rs.getInt("order_number"));
                tpf.setOrderDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("order_date")));
                tpf.setProposalType(rs.getString("proposal_type"));
                tpf.setCadreName(rs.getString("cadre_name"));
                tpf.setCadreGrade(rs.getString("cadre_grade"));
                tpf.setPay(rs.getString("pay_level"));
                tpf.setLetterheader(rs.getString("letterheader"));
                tpf.setLetterfooter(rs.getString("letterfooter"));
                tpf.setAuthorityName(rs.getString("authorityname"));
                tpf.setAuthorityDesg(rs.getString("authoritydesg"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

        return tpf;
    }

    @Override
    public void deleteEmployee(int transferProposalDetailId) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            String sql = "DELETE FROM emp_transfer_proposal_detail WHERE detail_id = ?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, transferProposalDetailId);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateNewSpc(String oldSpc, String newspc, int transferProposalDetailId, String transferType, String empId, String cadreCode) {
        Connection con = null;
        PreparedStatement pst = null;
        String sql = null;
        try {
            con = dataSource.getConnection();
            if (transferType.equals("N")) {
                pst = con.prepareStatement("delete from emp_transfer_proposal_detail_posting where detail_id=? and is_additional='N'");
                pst.setInt(1, transferProposalDetailId);
                pst.executeUpdate();
            }
            sql = "INSERT INTO emp_transfer_proposal_detail_posting (detail_id, new_spc, is_additional) VALUES(?, ?, ?)";
            pst = con.prepareStatement(sql);
            pst.setInt(1, transferProposalDetailId);
            pst.setString(2, newspc);
            pst.setString(3, transferType);
            pst.executeUpdate();

            /*if (transferType.equals("ADDITIONAL")) {
             sql = "UPDATE emp_transfer_proposal_detail SET additional_spc = ?, is_additional = 'Y' WHERE detail_id = ?";
             } else {
             sql = "UPDATE emp_transfer_proposal_detail SET new_spc = ? WHERE detail_id = ?";
             }
             pst = con.prepareStatement(sql);
             pst.setString(1, spc);
             pst.setInt(2, transferProposalId);
             pst.executeUpdate();*/
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void deleteTransferProposal(int proposalId) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            String sql = "DELETE FROM emp_transfer_proposal WHERE proposal_id = ?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, proposalId);
            pst.executeUpdate();
            sql = "DELETE FROM emp_transfer_proposal_detail WHERE proposal_id = ?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, proposalId);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void saveApproval(int proposalId, String orderNumber, String orderDate) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            con = dataSource.getConnection();
            String sql = "UPDATE emp_transfer_proposal SET order_number = ?, order_date = ?, is_approved = 'Y'"
                    + " WHERE proposal_id = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, orderNumber);
            pst.setTimestamp(2, new Timestamp(sdf.parse(orderDate).getTime()));
            pst.setInt(3, proposalId);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void sendLetterInEdespatch(eDespatchBean tpBean) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            pst = con.prepareStatement("UPDATE emp_transfer_proposal SET order_number = ?, order_date = ?, is_approved = 'Y',file_no=? WHERE proposal_id = ?");
            pst.setString(1, tpBean.getLetterno());
            pst.setTimestamp(2, new Timestamp(sdf.parse(tpBean.getLetterdate()).getTime()));
            pst.setString(3, tpBean.getFileno());
            pst.setInt(4, tpBean.getProposalId());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

}
