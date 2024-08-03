/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.miscellaneous;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.miscellaneous.PostProposal;
import hrms.model.miscellaneous.PostProposalMaster;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author Surendra
 */
public class PostProposalDAOImpl implements PostProposalDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public int addProposal(PostProposal pp) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int proposalId = 0;
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            con = this.dataSource.getConnection();

            if (pp.getPorposalId() != 0) {
                ps = con.prepareStatement("UPDATE post_proposal SET order_no=?, order_date=? WHERE proposal_id=?");
                ps.setString(1, pp.getGoorderno());
                if(pp.getGodate() != null && !pp.getGodate().equals("")){
                    ps.setTimestamp(2, new Timestamp(dateFormat.parse(pp.getGodate()).getTime()));
                }else{
                    ps.setTimestamp(2, null);
                }
                ps.setInt(3, pp.getPorposalId());
                ps.executeUpdate();
            } else {
                ps = con.prepareStatement("INSERT INTO post_proposal (submitted_by_spc, submitted_by, submitted_to_spc, submitted_to, order_no, order_date ) VALUES(?, ?, ?, ?,?,? )", Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, pp.getSubmittedBySpc());
                ps.setString(2, pp.getSubmittedBy());
                ps.setString(3, pp.getSubmittedToSpc());
                ps.setString(4, pp.getSubmittedTo());
                ps.setString(5, pp.getGoorderno());
                ps.setTimestamp(6, new Timestamp(dateFormat.parse(pp.getGodate()).getTime()));
                ps.executeUpdate();

                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    proposalId = rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(rs, ps);
        }
        return proposalId;
    }

    @Override
    public int addProposalDetails(PostProposal ppd) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int proposalId = 0;
        try {

            con = this.dataSource.getConnection();

            if (ppd.getPorposalId() != 0) {
                ps = con.prepareStatement("UPDATE post_proposal_details SET post_name=?, category=?, pay_band=?, cadre=?, sanc_strength_non_plan=?, sanc_strength_plan=?, meninposition_plan=?, meninposition_non_plan=?, noof_contractual_plan=?, noof_contractual_non_plan=?, noof_conferred_plan=?, noof_conferred_non_plan=?, vacancy_plan=?, vacancy_non_plan=?, justification=?, remark=? WHERE proposal_id=?");
                ps.setString(1, ppd.getPostname());
                ps.setString(2, ppd.getCategory());
                ps.setString(3, ppd.getPayband());
                ps.setString(4, ppd.getCadre());
                ps.setInt(5, ppd.getSancstrengthnonplan());
                ps.setInt(6, ppd.getSancstrengthplan());
                ps.setInt(7, ppd.getMeninpositionplan());
                ps.setInt(8, ppd.getMeninpositionnonplan());
                ps.setInt(9, ppd.getNoofcontractualplan());
                ps.setInt(10, ppd.getNoofcontractualnonplan());
                ps.setInt(11, ppd.getNoofconferredplan());
                ps.setInt(12, ppd.getNoofconferrednonplan());
                ps.setInt(13, ppd.getNoofvacancyplan());
                ps.setInt(14, ppd.getNoofvacancynonplan());
                ps.setString(15, ppd.getJustification());
                ps.setString(16, ppd.getRemark());
                ps.setInt(17, ppd.getPorposalId());
                ps.executeUpdate();
            } else {
                ps = con.prepareStatement("INSERT INTO post_proposal_details ( proposal_id, post_name, category, pay_band, cadre, sanc_strength_non_plan, sanc_strength_plan, meninposition_plan, meninposition_non_plan, noof_contractual_plan, noof_contractual_non_plan, noof_conferred_plan, noof_conferred_non_plan, vacancy_plan, vacancy_non_plan, justification, remark)"
                        + " VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )", Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, ppd.getPorposalId());
                ps.setString(2, ppd.getPostname());
                ps.setString(3, ppd.getCategory());
                ps.setString(4, ppd.getPayband());
                ps.setString(5, ppd.getCadre());
                ps.setInt(6, ppd.getSancstrengthnonplan());
                ps.setInt(7, ppd.getSancstrengthplan());
                ps.setInt(8, ppd.getMeninpositionplan());
                ps.setInt(9, ppd.getMeninpositionnonplan());
                ps.setInt(10, ppd.getNoofcontractualplan());
                ps.setInt(11, ppd.getNoofcontractualnonplan());
                ps.setInt(12, ppd.getNoofconferredplan());
                ps.setInt(13, ppd.getNoofconferrednonplan());
                ps.setInt(14, ppd.getNoofvacancyplan());
                ps.setInt(15, ppd.getNoofvacancynonplan());
                ps.setString(16, ppd.getJustification());
                ps.setString(17, ppd.getRemark());
                ps.executeUpdate();

                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    proposalId = rs.getInt(1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(rs, ps);
        }
        return proposalId;
    }

    @Override
    public List getPostProposalList(String submiitedBysPC) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int proposalId = 0;
        List li = new ArrayList();
        try {

            con = this.dataSource.getConnection();
            ps = con.prepareStatement("SELECT proposal_id, submitted_by_spc, g_spc.spn submittedPost,  submitted_by, submitted_to_spc, submiited_to_gpc.spn submittedTOPost, submitted_to FROM post_proposal \n"
                    + " inner join g_spc on post_proposal.submitted_by_spc=g_spc.spc\n"
                    + " left outer join g_spc submiited_to_gpc on post_proposal.submitted_to_spc=submiited_to_gpc.spc WHERE submitted_by_spc=?");
            ps.setString(1, submiitedBysPC);
            rs = ps.executeQuery();
            while (rs.next()) {
                PostProposalMaster ppm = new PostProposalMaster();
                ppm.setPorposalId(rs.getInt("proposal_id") + "");
                //ppm.setSubmittedBy(rs.getString("submittedPost"));
                ppm.setSubmittedBySpc(rs.getString("submittedPost"));
                //ppm.setSubmittedTo("");
                ppm.setSubmittedToSpc(rs.getString("submittedTOPost"));
                li.add(ppm);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(rs, ps);
        }
        return li;
    }

    @Override
    public PostProposal getPostProposalDetails(int proposalId) {

        Connection con = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        PostProposal pp = new PostProposal();
        try {

            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select * from post_proposal,post_proposal_details where post_proposal.proposal_id=post_proposal_details.proposal_id and post_proposal.proposal_id=?");
            ps.setInt(1, proposalId);
            rs = ps.executeQuery();
            pp.setPorposalId(proposalId);
            while (rs.next()) {
                pp.setPostname(rs.getString("post_name"));
                pp.setCategory(rs.getString("category"));
                pp.setPayband(rs.getString("pay_band"));
                pp.setCadre(rs.getString("cadre"));
                pp.setGoorderno(rs.getString("order_no"));
                pp.setGodate(CommonFunctions.getFormattedOutputDate1(rs.getDate("order_date")));
                pp.setSancstrengthplan(rs.getInt("sanc_strength_plan"));
                pp.setSancstrengthnonplan(rs.getInt("sanc_strength_non_plan"));
                pp.setSancstrengthtotal(rs.getInt("sanc_strength_plan") + rs.getInt("sanc_strength_non_plan"));
                pp.setMeninpositionplan(rs.getInt("meninposition_plan"));
                pp.setMeninpositionnonplan(rs.getInt("meninposition_non_plan"));
                pp.setMeninpositiontotal(rs.getInt("meninposition_plan") + rs.getInt("meninposition_non_plan"));
                pp.setNoofcontractualnonplan(rs.getInt("noof_contractual_plan"));
                pp.setNoofcontractualnonplan(rs.getInt("noof_contractual_non_plan"));
                pp.setNoofcontractualtotal(rs.getInt("noof_contractual_plan") + rs.getInt("noof_contractual_non_plan"));
                pp.setNoofconferrednonplan(rs.getInt("noof_conferred_plan"));
                pp.setNoofconferrednonplan(rs.getInt("noof_conferred_non_plan"));
                pp.setNoofconferredtotal(rs.getInt("noof_conferred_plan") + rs.getInt("noof_conferred_non_plan"));
                pp.setNoofvacancyplan(rs.getInt("vacancy_plan"));
                pp.setNoofvacancynonplan(rs.getInt("vacancy_non_plan"));
                pp.setNoofvacancytotal(rs.getInt("vacancy_plan") + rs.getInt("vacancy_non_plan"));
                pp.setJustification(rs.getString("justification"));
                pp.setRemark(rs.getString("remark"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
            DataBaseFunctions.closeSqlObjects(rs, ps);
        }
        return pp;
    }

}
