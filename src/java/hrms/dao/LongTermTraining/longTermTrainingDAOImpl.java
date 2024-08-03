/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.LongTermTraining;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import static hrms.dao.master.TrainingTitleDAOImpl.getServerDoe;
import hrms.dao.notification.NotificationDAO;
import hrms.dao.transfer.TransferDAO;
import hrms.dao.transfer.TransferDAOImpl;
import hrms.model.notification.NotificationBean;
//import hrms.model.LongTermTrainingForm.longTermTrainingForm;
import hrms.model.trainingschedule.TrainingSchedule;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @author Madhusmita
 */
public class longTermTrainingDAOImpl implements longTermTrainingDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    protected TransferDAOImpl transferIdDao;
    protected ServiceBookLanguageDAO sbDAO;
    protected NotificationDAO notificationDao;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setSbDAO(ServiceBookLanguageDAO sbDAO) {
        this.sbDAO = sbDAO;
    }

    public void setNotificationDao(NotificationDAO notificationDao) {
        this.notificationDao = notificationDao;
    }

    public void setTransferIdDao(TransferDAOImpl transferIdDao) {
        this.transferIdDao = transferIdDao;
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
    public List getLtTrainingList(String empId) {
        Connection con = null;
        TrainingSchedule trainingSch = null;
        List ar = new ArrayList();
        ResultSet rs = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select doe,empnt.not_id,ordno,fdate,tdate,days,training_title_id,training_title,coordinator,\n"
                    + "place,if_underwent,coordinator,place,is_validated from\n"
                    + "(select * from emp_notification where not_type='LT_TRAINING')empnt\n"
                    + "inner join \n"
                    + "(select * from emp_lt_training )emplt\n"
                    + "on empnt.not_id=emplt.not_id\n"
                    + "left outer join g_training_title trngType\n"
                    + "on trngType.training_title_id=emplt.training_name\n"
                    + "where empnt.not_type='LT_TRAINING' and empnt.emp_id=?");
            pst.setString(1, empId);
            rs = pst.executeQuery();
            while (rs.next()) {
                trainingSch = new TrainingSchedule();
                //trainingSch.setTrainId(rs.getString("TRAINID"));
                //trainingSch.setEmpId(rs.getString("EMP_ID"));
                trainingSch.setNotId(rs.getString("not_id"));
                if (rs.getDate("DOE") != null) {
                    trainingSch.setTxtservice(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOE")));
                }
                if (rs.getString("ordno") != null && !rs.getString("ordno").equals("")) {
                    trainingSch.setTxtOrdNo(rs.getString("ordno"));
                }

                if (rs.getDate("fdate") != null) {
                    trainingSch.setStartDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("fdate")));
                }
                if (rs.getString("if_underwent") != null && !rs.getString("if_underwent").equalsIgnoreCase("")) {
                    trainingSch.setUnderwent(rs.getString("if_underwent"));
                }
                if (rs.getDate("tdate") != null) {
                    trainingSch.setComplDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("tdate")));
                }
                if (rs.getString("days") != null
                        && !rs.getString("days").equalsIgnoreCase("")) {
                    trainingSch.setTxttotday(rs.getString("days").toUpperCase());
                }

                trainingSch.setTrainingName(rs.getString("training_title"));

                if (rs.getString("coordinator") != null
                        && !rs.getString("coordinator").equalsIgnoreCase("")) {
                    trainingSch.setTxtcoordinator(rs.getString("coordinator").toUpperCase());
                }
                if (rs.getString("place") != null
                        && !rs.getString("place").equalsIgnoreCase("")) {
                    trainingSch.setTxttrplace(rs.getString("place").toUpperCase());
                }
                trainingSch.setIsValidated(rs.getString("is_validated"));
                ar.add(trainingSch);
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {

            DataBaseFunctions.closeSqlObjects(rs);
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return ar;

    }

    @Override
    public TrainingSchedule getLtTrainingData(int notId) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        TrainingSchedule trSchedule = new TrainingSchedule();
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("select t1.*,go.dist_code posted_disrict from\n"
                    + "(select empnt.emp_id,empnt.not_id,ordno,orddt,empnt.dept_code notdept,empnt.off_code notofc,auth notspc,g_spc.spn notpostname,\n"
                    + "note,fdate,tdate,days,training_title_id,coordinator,if_underwent,place,organization_type_posting,\n"
                    + "if_visible,et.next_spc postedspc,g1.spn postedspn,substr(et.next_spc,14,6) gpc,et.off_code postedofc,et.dept_code posteddept from\n"
                    + "(select * from emp_notification where not_id=? and not_type='LT_TRAINING')empnt\n"
                    + "inner join emp_lt_training emplt\n"
                    + "on empnt.not_id=emplt.not_id\n"
                    + "left outer join g_training_title trngTitle on\n"
                    + "trngTitle.training_title_id=emplt.training_name\n"
                    + "left outer join g_spc on g_spc.spc=empnt.auth\n"
                    + "left outer join g_office on g_office.off_code=empnt.off_code\n"
                    + "left outer join (select * from emp_transfer where not_type='LT_TRAINING')et\n"
                    + "on et.not_id=empnt.not_id\n"
                    + "left outer join g_spc g1 on g1.spc=et.next_spc)t1\n"
                    + "left outer join g_office go on t1.postedofc=go.off_code");
            ps.setInt(1, notId);
            rs = ps.executeQuery();
            if (rs.next()) {
                trSchedule.setNotId("not_id");
                trSchedule.setTxtOrdNo(rs.getString("ordno"));
                trSchedule.setTxtOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                trSchedule.setHidNotifyingDeptCode(rs.getString("notdept"));
                trSchedule.setHidNotifyingOffCode(rs.getString("notofc"));
                trSchedule.setNotifyingPostName(rs.getString("notpostname"));
                trSchedule.setNotifyingSpc(rs.getString("notspc"));
                trSchedule.setTxtnote(rs.getString("note"));
                trSchedule.setStartDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("fdate")));
                trSchedule.setComplDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("tdate")));
                trSchedule.setTxttotday(rs.getString("days"));
                trSchedule.setSlttitle(rs.getString("training_title_id"));
                trSchedule.setTxtcoordinator(rs.getString("coordinator"));
                trSchedule.setUnderwent(rs.getString("if_underwent"));
                trSchedule.setTxttrplace(rs.getString("place"));
                trSchedule.setHidPostedDistCode(rs.getString("posted_disrict"));
                if (rs.getString("gpc") != null && !rs.getString("gpc").equals("")) {
                    trSchedule.setGenericpostPosted(rs.getString("gpc"));
                }
                trSchedule.setHidNotId(notId);
                if (rs.getString("If_visible") != null && rs.getString("if_visible").equals("N")) {
                    trSchedule.setChkNotSBPrint("Y");
                }
                if (rs.getString("organization_type_posting") != null && rs.getString("organization_type_posting").equals("GOI")) {
                    trSchedule.setHidPostedOthSpc(rs.getString("postedspc"));
                    trSchedule.setPostedPostName(getOtherSpn(rs.getString("postedspc")));
                    trSchedule.setRdTrainingauthtype("GOI");

                } else {
                    trSchedule.setPostedspc(rs.getString("postedspc"));
                    //trSchedule.setHidPostedOthSpc(rs.getString("postedspc"));
                    trSchedule.setPostedPostName(rs.getString("notpostname"));
                    trSchedule.setHidPostedDeptCode(rs.getString("posteddept"));
                    trSchedule.setHidPostedOffCode(rs.getString("postedofc"));
                    trSchedule.setPostedPostName(rs.getString("postedspn"));
                    trSchedule.setRdTrainingauthtype("GOO");
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return trSchedule;
    }

    @Override
    public void deleteLtTraining(String trainId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void saveLtTraining(TrainingSchedule lttraining, int notId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();

            //String maxtrainId=CommonFunctions.getMaxCode("emp_train", "trainid", con);
            pstmt = con.prepareStatement("insert into emp_lt_training(emp_id,not_type,training_name,fdate,tdate,"
                    + "days,place,if_underwent,coordinator,if_long_term,not_id)values(?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, lttraining.getEmpId());
            pstmt.setString(2, "LT_TRAINING");
            pstmt.setString(3, lttraining.getSlttitle());
            if (lttraining.getStartDate() != null && !lttraining.getStartDate().equals("")) {
                pstmt.setTimestamp(4, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(lttraining.getStartDate()).getTime()));
            } else {
                pstmt.setTimestamp(4, null);
            }
            if (lttraining.getComplDate() != null && !lttraining.getComplDate().equals("")) {
                pstmt.setTimestamp(5, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(lttraining.getComplDate()).getTime()));
            } else {
                pstmt.setTimestamp(5, null);
            }
            if (lttraining.getTxttotday() != null && !lttraining.getTxttotday().equals("")) {
                pstmt.setDouble(6, Double.parseDouble(lttraining.getTxttotday()));
            } else {
                pstmt.setDouble(6, 0);
            }
            pstmt.setString(7, lttraining.getTxttrplace());
            pstmt.setString(8, lttraining.getUnderwent());

            pstmt.setString(9, lttraining.getTxtcoordinator());
            pstmt.setString(10, "Y");
            pstmt.setInt(11, notId);

            pstmt.executeUpdate();
            if (lttraining.getRdTransaction() != null && lttraining.getRdTransaction().equals("C")) {
                String sql = "UPDATE EMP_MAST SET NEXT_OFFICE_CODE=? WHERE EMP_ID=?";
                pstmt = con.prepareStatement(sql);
                pstmt.setString(1, lttraining.getHidPostedOffCode());
                pstmt.setString(2, lttraining.getEmpId());
                pstmt.executeUpdate();
            }

            String sql = "INSERT INTO EMP_TRANSFER (NOT_ID, NOT_TYPE, EMP_ID,DEPT_CODE,OFF_CODE, NEXT_SPC)VALUES(?,?,?,?,?,?)";
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, notId);
            pstmt.setString(2, "LT_TRAINING");
            pstmt.setString(3, lttraining.getEmpId());
            if (lttraining.getRdTrainingauthtype() != null && lttraining.getRdTrainingauthtype().equals("GOI")) {
                pstmt.setString(4, null);
                pstmt.setString(5, null);
                pstmt.setString(6, lttraining.getHidPostedOthSpc());
                //lttraining.setPostedPostName(lttraining.getHidPostedOthSpc());
            } else {
                pstmt.setString(4, lttraining.getHidPostedDeptCode());
                pstmt.setString(5, lttraining.getHidPostedOffCode());
                pstmt.setString(6, lttraining.getPostedspc());
            }
            pstmt.executeUpdate();
            String sbLang = sbDAO.getLongTermTrainingDetails(lttraining, notId);
            notificationDao.saveServiceBookLanguage(sbLang, notId, "LT_TRAINING");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateLtTraining(TrainingSchedule lttraining, int notId) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            con = this.dataSource.getConnection();
            ps = con.prepareStatement("update emp_lt_training set fdate=?,tdate=?,days=?,place=?,if_underwent=?,coordinator=?,training_name=? where not_id=?");
            if (lttraining.getStartDate() != null && !lttraining.getStartDate().equals("")) {
                ps.setTimestamp(1, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(lttraining.getStartDate()).getTime()));
            } else {
                ps.setTimestamp(1, null);
            }
            if (lttraining.getComplDate() != null && !lttraining.getComplDate().equals("")) {
                ps.setTimestamp(2, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(lttraining.getComplDate()).getTime()));
            } else {
                ps.setTimestamp(2, null);
            }
            if (lttraining.getTxttotday() != null && !lttraining.getTxttotday().equals("")) {
                ps.setDouble(3, Double.parseDouble(lttraining.getTxttotday()));
            } else {
                ps.setDouble(3, 0);
            }
            ps.setString(4, lttraining.getTxttrplace());
            ps.setString(5, lttraining.getUnderwent());
            ps.setString(6, lttraining.getTxtcoordinator());
            ps.setString(7, lttraining.getSlttitle());
            ps.setInt(8, notId);
            ps.executeUpdate();
            String sql = "UPDATE EMP_TRANSFER SET DEPT_CODE=?,OFF_CODE=?, NEXT_SPC=? WHERE NOT_ID=?";
            ps = con.prepareStatement(sql);
            if (lttraining.getRdTrainingauthtype() != null && lttraining.getRdTrainingauthtype().equals("GOO")) {
                ps.setString(1, lttraining.getHidPostedDeptCode());
                ps.setString(2, lttraining.getHidPostedOffCode());
                ps.setString(3, lttraining.getPostedspc());
            } else {
                ps.setString(1, null);
                ps.setString(2, null);
                ps.setString(3, lttraining.getHidPostedOthSpc());
            }            
            ps.setInt(4, notId);
            ps.executeUpdate();
            String sql1 = "UPDATE EMP_NOTIFICATION SET organization_type_posting=?,organization_type=? WHERE NOT_ID=?";
            ps = con.prepareStatement(sql1);
            if (lttraining.getRdTrainingauthtype() != null && !lttraining.getRdTrainingauthtype().equals("")) {
                ps.setString(1, lttraining.getRdTrainingauthtype());
                ps.setString(2, lttraining.getRdTrainingauthtype());
                ps.setInt(3, notId);
            }
            ps.executeUpdate();
            if (lttraining.getRdTransaction().equals("C") && lttraining.getRdTransaction() != null && lttraining.getRdTrainingauthtype().equals("GOO")) {
                String sql2 = "UPDATE EMP_MAST SET NEXT_OFFICE_CODE=? WHERE EMP_ID=?";
                ps = con.prepareStatement(sql2);
                ps.setString(1, lttraining.getHidPostedOffCode());
                ps.setString(2, lttraining.getEmpId());
                ps.executeUpdate();
            }
            String sbLang = sbDAO.getLongTermTrainingDetails(lttraining, notId);
            notificationDao.saveServiceBookLanguage(sbLang, notId, "LT_TRAINING");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, ps);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }
}
