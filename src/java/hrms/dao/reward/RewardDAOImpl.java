/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.reward;

import hrms.SelectOption;
import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.model.common.FileAttribute;
import hrms.model.reward.Reward;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author lenovo
 */
public class RewardDAOImpl implements RewardDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected ServiceBookLanguageDAO sbDAO;

    public void setSbDAO(ServiceBookLanguageDAO sbDAO) {
        this.sbDAO = sbDAO;
    }

    @Override
    public List getRewardList(String empid) {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        List rewardlist = new ArrayList();
        Reward tbean = null;
        try {
            con = dataSource.getConnection();
            String sql = "select rwd_id,emp_notification.doe,emp_notification.not_id,emp_reward.not_type,ordno,orddt,off_en, if_visible from"
                    + " (select emp_id,doe,not_id,not_type,ordno,orddt,off_code from emp_notification where emp_id=? and not_type='REWARD')emp_notification"
                    + " left outer join emp_reward on emp_notification.not_id=emp_reward.not_id"
                    + " left outer join g_office on emp_notification.off_code=g_office.off_code";
            pst = con.prepareStatement(sql);
            pst.setString(1, empid);
            rs = pst.executeQuery();
            while (rs.next()) {
                tbean = new Reward();
                tbean.setRewardId(rs.getString("rwd_id"));
                tbean.setHnotid(rs.getInt("not_id"));
                tbean.setNotType(rs.getString("not_type"));
                tbean.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOE")));
                tbean.setOrdno(rs.getString("ordno"));
                tbean.setOrdt(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                tbean.setTransferToOffice(rs.getString("off_en"));
                if (rs.getString("if_visible") != null && rs.getString("if_visible").equals("N")) {
                    tbean.setChkNotSBPrint("Y");
                } else {
                    tbean.setChkNotSBPrint("N");
                }
                rewardlist.add(tbean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs,pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return rewardlist;
    }

    @Override
    public void saveReward(Reward rewardForm, int notid, String filepath) {
        Connection con = null;
        PreparedStatement pst = null;

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            con = dataSource.getConnection();
            String sql = "INSERT INTO emp_reward (NOT_ID, NOT_TYPE, EMP_ID, note, rwd_lvl, reward_type, if_visible, money_reward ) VALUES(?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setInt(1, notid);
            pst.setString(2, "REWARD");
            pst.setString(3, rewardForm.getEmpid());
            pst.setString(4, rewardForm.getNote());
            pst.setString(5, rewardForm.getRewardLevel());
            pst.setString(6, rewardForm.getRewardType());

            if (rewardForm.getChkNotSBPrint() != null && !rewardForm.getChkNotSBPrint().equals("")) {
                pst.setString(7, rewardForm.getChkNotSBPrint());
            } else {
                pst.setString(7, "Y");
            }
            pst.setString(8, rewardForm.getMoneyReward());
            pst.executeUpdate();

            String sbLang = sbDAO.getRewardDetails(rewardForm);

            pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION=?, IF_VISIBLE=? WHERE NOT_ID=?");
            pst.setString(1, sbLang);
            if (rewardForm.getChkNotSBPrint() != null && rewardForm.getChkNotSBPrint().equals("Y")) {
                pst.setString(2, "N");
            } else {
                pst.setString(2, "Y");
            }
            pst.setInt(3, notid);
            pst.execute();

            if (rewardForm.getAppreciationAttch() != null && !rewardForm.getAppreciationAttch().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = rewardForm.getAppreciationAttch().getInputStream();
                String diskfilename = System.currentTimeMillis() + "";
                File newFile1 = new File(filepath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_nomination_doc ( file_type, file_path, file_name, original_name, data_table_id,doc_name) values(?,?,?,?,?,?)";
                pst = con.prepareStatement(insFileQry);
                pst.setString(1, rewardForm.getAppreciationAttch().getContentType());
                pst.setString(2, filepath);
                pst.setString(3, diskfilename);
                pst.setString(4, rewardForm.getAppreciationAttch().getOriginalFilename());
                pst.setInt(5, notid);
                pst.setString(6, "NOTIAPR");
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateReward(Reward rewardForm, String filepath) {
        Connection con = null;
        PreparedStatement pst = null;

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            con = dataSource.getConnection();
            String sql = "UPDATE emp_reward SET  note=?, rwd_lvl=?, reward_type=?, if_visible=?, money_reward=? WHERE EMP_ID=? AND rwd_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, rewardForm.getNote());
            pst.setString(2, rewardForm.getRewardLevel());
            pst.setString(3, rewardForm.getRewardType());
            if (rewardForm.getChkNotSBPrint() != null && rewardForm.getChkNotSBPrint().equals("Y")) {
                pst.setString(4, "N");
            } else {
                pst.setString(4, "Y");
            }
            pst.setString(5, rewardForm.getMoneyReward());
            pst.setString(6, rewardForm.getEmpid());
            pst.setInt(7, Integer.parseInt(rewardForm.getRewardId()));
            pst.executeUpdate();

            String sbLang = sbDAO.getRewardDetails(rewardForm);

            pst = con.prepareStatement("UPDATE EMP_NOTIFICATION SET SB_DESCRIPTION=?, IF_VISIBLE=? WHERE NOT_ID=?");
            pst.setString(1, sbLang);
            if (rewardForm.getChkNotSBPrint() != null && !rewardForm.getChkNotSBPrint().equals("")) {
                pst.setString(2, rewardForm.getChkNotSBPrint());
            } else {
                pst.setString(2, "Y");
            }
            pst.setInt(3, rewardForm.getHnotid());
            pst.execute();

            if (rewardForm.getAppreciationAttch() != null && !rewardForm.getAppreciationAttch().isEmpty()) {
                int read = 0;
                byte[] bytes = new byte[1024];

                inputStream = rewardForm.getAppreciationAttch().getInputStream();
                String diskfilename = System.currentTimeMillis() + "";
                File newFile1 = new File(filepath + diskfilename);
                if (!newFile1.exists()) {
                    newFile1.createNewFile();
                }
                outputStream = new FileOutputStream(newFile1);

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                String insFileQry = "insert into police_nomination_doc ( file_type, file_path, file_name, original_name, data_table_id,doc_name) values(?,?,?,?,?,?)";
                pst = con.prepareStatement(insFileQry);
                pst.setString(1, rewardForm.getAppreciationAttch().getContentType());
                pst.setString(2, filepath);
                pst.setString(3, diskfilename);
                pst.setString(4, rewardForm.getAppreciationAttch().getOriginalFilename());
                pst.setInt(5, rewardForm.getHnotid());
                pst.setString(6, "NOTIAPR");
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void deleteReward(String rewardId) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            String sql = "DELETE FROM emp_reward WHERE rwd_id=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(rewardId));
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public Reward getEmpRewardData(String rewardId, int notificationId) throws SQLException {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        Reward rwd = new Reward();
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select organization_type,money_reward,emp_reward.emp_id,emp_reward.note,rwd_lvl,reward_type,emp_reward.not_type,ordno,emp_reward.if_visible, "
                    + "orddt,emp_notification.dept_code,emp_notification.off_code,emp_notification.auth from emp_reward  "
                    + "inner join emp_notification on emp_notification.not_id=emp_reward.not_id "
                    + "where rwd_id=?");
            pst.setInt(1, Integer.parseInt(rewardId));
            rs = pst.executeQuery();
            if (rs.next()) {
                rwd.setEmpid(rs.getString("emp_id"));
                rwd.setTxtNotOrdNo(rs.getString("ordno"));
                rwd.setTxtNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                //rwd.setAuthPostName(rs.getString("empname") + "," + rs.getString("post"));
                rwd.setAuthPostName(CommonFunctions.getSPN(con, rs.getString("auth")));
                rwd.setRewardLevel(rs.getString("rwd_lvl"));
                rwd.setRewardType(rs.getString("reward_type"));
                rwd.setNote(rs.getString("note"));
                rwd.setRadpostingauthtype(rs.getString("organization_type"));
                rwd.setHidAuthDeptCode(rs.getString("dept_code"));
                rwd.setHidAuthOffCode(rs.getString("off_code"));
                if(rwd.getRadpostingauthtype() != null && rwd.getRadpostingauthtype().equals("GOI")){
                    rwd.setAuthPostName(getOtherSpn(rs.getString("AUTH")));
                    rwd.setHidOthSpc(rs.getString("auth"));
                }else{
                    rwd.setAuthSpc(rs.getString("auth"));
                }
                if (rs.getString("if_visible") != null) {
                    if (rs.getString("if_visible").equals("") || rs.getString("if_visible").equals("Y")) {
                        rwd.setChkNotSBPrint("N");
                    }
                    if (rs.getString("if_visible").equals("N")) {
                        rwd.setChkNotSBPrint("Y");
                    }
                }
                rwd.setMoneyReward(rs.getString("money_reward"));

                pst = con.prepareStatement("select file_name, original_name, doc_name from police_nomination_doc where data_table_id=? and doc_name='NOTIAPR'");
                pst.setInt(1, notificationId);
                rs = pst.executeQuery();
                if (rs.next()) {
                    if (rs.getString("doc_name").equalsIgnoreCase("NOTIAPR")) {
                        rwd.setOriginalFileNameAppreciationAttch(rs.getString("original_name"));
                        rwd.setDiskFilenameAppreciationAttch(rs.getString("file_name"));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(con);
        }
        return rwd;
    }

    @Override
    public ArrayList empRewardType() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList empRewardTypeList = new ArrayList();
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT * FROM g_reward_type");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SelectOption so = new SelectOption();
                so.setValue(rs.getString("reward_type_id"));
                so.setLabel(rs.getString("reward_type"));
                empRewardTypeList.add(so);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return empRewardTypeList;
    }

    @Override
    public FileAttribute getAttachedDocument(String filePath, String docName, int attachId) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        File f = null;
        FileAttribute fa = new FileAttribute();
        try {
            con = this.dataSource.getConnection();
            String sql = "";
            if (docName != null && !docName.equals("")) {
                sql = "SELECT * FROM police_nomination_doc WHERE data_table_id=? and doc_name=? ";
                pst = con.prepareStatement(sql);
                pst.setInt(1, attachId);
                pst.setString(2, docName);
            } else {
                sql = "SELECT * FROM police_nomination_doc WHERE data_table_id=? ";
                pst = con.prepareStatement(sql);
                pst.setInt(1, attachId);

            }

            rs = pst.executeQuery();
            if (rs.next()) {
                String dirpath = filePath + rs.getString("file_name");

                f = new File(dirpath);
                if (f.exists()) {
                    fa.setDiskFileName(rs.getString("file_name"));
                    fa.setOriginalFileName(rs.getString("original_name"));
                    fa.setFileType(rs.getString("file_type"));
                    fa.setUploadAttachment(f);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return fa;
    }

    @Override
    public int deleteRewardAttachment(int notid, String filepath, FileAttribute fa) {

        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        File f = null;

        int deletestatus = 0;
        try {
            con = this.dataSource.getConnection();

            String dirpath = filepath + fa.getDiskFileName();

            f = new File(dirpath);
            if (f.exists()) {
                boolean isDeleted = f.delete();
                if (isDeleted == true) {
                    String sql = "DELETE FROM police_nomination_doc WHERE data_table_id=?";
                    pst = con.prepareStatement(sql);
                    pst.setInt(1, notid);
                    deletestatus = pst.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return deletestatus;
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
    
}
