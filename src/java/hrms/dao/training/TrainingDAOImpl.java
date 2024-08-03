package hrms.dao.training;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import static hrms.dao.master.TrainingTitleDAOImpl.getServerDoe;
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

public class TrainingDAOImpl implements TrainingDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    
    protected ServiceBookLanguageDAO sbDAO;

    public void setSbDAO(ServiceBookLanguageDAO sbDAO) {
        this.sbDAO = sbDAO;
    }

    public List getTrainingList(String empId) {
        Connection con = null;
        TrainingSchedule trainingSch = null;
        List ar = new ArrayList();
        ResultSet rs = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select * from (select is_validated,TRAINID, EMP_ID, DoE, ent_dept, ent_off, ent_auth, TOE, IF_ASSUMED, g_training_title.training_title, "
                    + " IF_UNDERWENT,TRNG_TYPE, "
                    + " S_DATE, C_DATE, DAYS, COORD, PLACE, NOTE from EMP_TRAIN "
                    + " left outer join g_training_title on g_training_title.trainingsl=EMP_TRAIN.trainingsl where EMP_ID=? ORDER BY DOE DESC) newtrain");
            pst.setString(1, empId);
            rs = pst.executeQuery();
            while (rs.next()) {
                trainingSch = new TrainingSchedule();
                trainingSch.setTrainId(rs.getString("TRAINID"));
                trainingSch.setEmpId(rs.getString("EMP_ID"));
                if (rs.getDate("DOE") != null) {
                    trainingSch.setTxtservice(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOE")));
                }
                if (rs.getString("TRNG_TYPE") == null) {
                    trainingSch.setSlttraintype("OTHER_TR");
                } else {
                    trainingSch.setSlttraintype(rs.getString("TRNG_TYPE"));
                }
                if (trainingSch.getSlttraintype().equals("OTHER_TR")) {
                   // trainingSch.setTxtTraingTitl(rs.getString("training_title"));
                    trainingSch.setSlttitle((rs.getString("training_title")));
                } else {
                    trainingSch.setSlttitle((rs.getString("training_title")));
                }
                if (rs.getDate("S_DATE") != null) {
                    trainingSch.setStartDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("S_DATE")));
                }
                if (rs.getString("IF_UNDERWENT") != null && !rs.getString("IF_UNDERWENT").equalsIgnoreCase("")) {
                    trainingSch.setUnderwent(rs.getString("IF_UNDERWENT"));
                }
                if (rs.getDate("C_DATE") != null) {
                    trainingSch.setComplDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("C_DATE")));
                }
                if (rs.getString("DAYS") != null
                        && !rs.getString("DAYS").equalsIgnoreCase("")) {
                    trainingSch.setTxttotday(rs.getString("DAYS").toUpperCase());
                }
                if (rs.getString("COORD") != null
                        && !rs.getString("COORD").equalsIgnoreCase("")) {
                    trainingSch.setTxtcoordinator(rs.getString("COORD").toUpperCase());
                }
                if (rs.getString("PLACE") != null
                        && !rs.getString("PLACE").equalsIgnoreCase("")) {
                    trainingSch.setTxttrplace(rs.getString("PLACE").toUpperCase());
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

    public void saveTrainingSchedule(TrainingSchedule training) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
   
            
            
            //String maxtrainId=CommonFunctions.getMaxCode("emp_train", "trainid", con);
            
            pstmt = con.prepareStatement("insert into emp_train(emp_id,doe,ent_dept,ent_off,ent_auth,toe,if_assumed,title,trainingsl,if_underwent,s_date,c_date,days,coord,place,note,sb_description,acs,ascs,trng_type,is_locked,source,ord_no,ord_date,if_visible)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setString(1, training.getEmpId());
            pstmt.setDate(2, new java.sql.Date(new SimpleDateFormat("dd-MMM-yyyy").parse(getServerDoe()).getTime()));
            pstmt.setString(3, training.getEntrydept());
            pstmt.setString(4, training.getEntryoff());
            pstmt.setString(5, training.getEntryauth());
            pstmt.setString(6, "");
            pstmt.setString(7, "N");
            String[] str = training.getSlttitle().split("-");
            pstmt.setString(8,str[0]);          
            pstmt.setInt(9, Integer.parseInt(str[1]));       
           // System.out.println("id:" + training.getSlttitle());
            pstmt.setString(10, training.getUnderwent());
            if(training.getStartDate() != null && !training.getStartDate().equals("")){
                pstmt.setTimestamp(11, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(training.getStartDate()).getTime()));
            }else{
                pstmt.setTimestamp(11,null);
            }
            if(training.getComplDate() != null && !training.getComplDate().equals("")){
                pstmt.setTimestamp(12, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(training.getComplDate()).getTime()));
            }else{
                pstmt.setTimestamp(12,null);
            }
            if(training.getTxttotday() != null && !training.getTxttotday().equals("")){
                pstmt.setDouble(13, Double.parseDouble(training.getTxttotday()));
            }else{
                pstmt.setDouble(13, 0);
            }
            pstmt.setString(14, training.getTxtcoordinator());
            pstmt.setString(15, training.getTxttrplace());
            pstmt.setString(16, training.getTxtnote());
            pstmt.setString(17, "");
            pstmt.setString(18, training.getNisb());
            pstmt.setString(19, "");
            pstmt.setString(20, training.getSlttraintype());
            pstmt.setString(21, "");
            pstmt.setString(22, "");
            pstmt.setString(23, training.getTxtOrdNo());
            pstmt.setTimestamp(24, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(training.getTxtOrdDt()).getTime()));
            pstmt.setString(25, training.getIfvisible());
            
            int maxtrainId = 0;
            int ret = pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            rs.next();
            maxtrainId = rs.getInt("trainid");

            String sbLang = sbDAO.getTrainingDetails(training);
            pstmt = con.prepareStatement("UPDATE EMP_TRAIN SET SB_DESCRIPTION='" + sbLang + "'  WHERE TRAINID='" + maxtrainId + "'");
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public void updateTrainingSchedule(TrainingSchedule training) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            String sql = "update emp_train set title=?,trainingsl=?,if_underwent=?,s_date=?,c_date=?,days=?,coord=?,place=?,note=?,trng_type=?,acs=?,ord_no=?,ord_date=?,if_visible=? where trainid=?";
            pstmt = con.prepareStatement(sql);
            
            String[] str = training.getSlttitle().split("-");
            pstmt.setString(1,str[0]);          
            pstmt.setInt(2, Integer.parseInt(str[1]));   
            
            //pstmt.setString(1, training.getSlttitle());
            pstmt.setString(3, training.getUnderwent());
            if(training.getStartDate() != null && !training.getStartDate().equals("")){
                pstmt.setTimestamp(4, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(training.getStartDate()).getTime()));
            }else{
                pstmt.setTimestamp(4, null);
            }
            if(training.getComplDate() != null && !training.getComplDate().equals("")){
                pstmt.setTimestamp(5, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(training.getComplDate()).getTime()));
            }else{
                pstmt.setTimestamp(5, null);
            }
            if(training.getTxttotday() != null && !training.getTxttotday().equals("")){
                pstmt.setDouble(6, Double.parseDouble(training.getTxttotday()));
            }else{
                pstmt.setDouble(6,0);
            }
            pstmt.setString(7, training.getTxtcoordinator());
            pstmt.setString(8, training.getTxttrplace());
            pstmt.setString(9, training.getTxtnote());
            pstmt.setString(10, training.getSlttraintype());
            pstmt.setString(11, training.getNisb());
             pstmt.setString(12, training.getTxtOrdNo());
            pstmt.setTimestamp(13, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(training.getTxtOrdDt()).getTime()));            
            pstmt.setString(14, training.getIfvisible());
            pstmt.setInt(15, Integer.parseInt(training.getTrainId()));
            pstmt.executeUpdate();
            
            
            String sbLang = sbDAO.getTrainingDetails(training);
            pstmt = con.prepareStatement("UPDATE EMP_TRAIN SET SB_DESCRIPTION='" + sbLang + "'  WHERE TRAINID='" + training.getTrainId() + "'");
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    public TrainingSchedule getTrainingData(String trainId) {
        Connection con = null;
        TrainingSchedule trainingSch = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        try {
            con = dataSource.getConnection();
            pst = con.prepareStatement("select * from (select TRAINID, EMP_ID, DoE,EMP_TRAIN.trainingsl, ent_dept, ent_off, ent_auth, TOE, IF_ASSUMED, EMP_TRAIN.TITLE,title.training_title title2,IF_UNDERWENT,TRNG_TYPE,\n"
                    + "S_DATE, C_DATE, DAYS, COORD, PLACE, NOTE,ACS,ord_no,ord_date,if_visible from EMP_TRAIN \n"
                    + "left outer join g_training_title title on title.trainingsl=EMP_TRAIN.trainingsl where TRAINID=? ORDER BY DOE DESC) newtrain");
            pst.setInt(1, Integer.parseInt(trainId));
            rs = pst.executeQuery();
            while (rs.next()) {
                trainingSch = new TrainingSchedule();
                trainingSch.setTrainId(rs.getString("TRAINID"));
                trainingSch.setEmpId(rs.getString("EMP_ID"));
                if (rs.getDate("DOE") != null) {
                    trainingSch.setTxtservice(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOE")));
                }
                trainingSch.setSlttraintype(rs.getString("TRNG_TYPE"));
     
                trainingSch.setSlttitle((rs.getString("title") +"-"+ rs.getString("trainingsl")));
                //System.out.println("title:" + rs.getString("title2"));
                if (rs.getDate("S_DATE") != null) {
                    trainingSch.setStartDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("S_DATE")));
                }
                if (rs.getString("IF_UNDERWENT") != null && !rs.getString("IF_UNDERWENT").equalsIgnoreCase("")) {
                    trainingSch.setUnderwent(rs.getString("IF_UNDERWENT"));
                }
                if (rs.getDate("C_DATE") != null) {
                    trainingSch.setComplDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("C_DATE")));
                }
                if (rs.getString("DAYS") != null
                        && !rs.getString("DAYS").equalsIgnoreCase("")) {
                    trainingSch.setTxttotday(rs.getString("DAYS").toUpperCase());
                }
                if (rs.getString("COORD") != null
                        && !rs.getString("COORD").equalsIgnoreCase("")) {
                    trainingSch.setTxtcoordinator(rs.getString("COORD").toUpperCase());
                }
                if (rs.getString("PLACE") != null
                        && !rs.getString("PLACE").equalsIgnoreCase("")) {
                    trainingSch.setTxttrplace(rs.getString("PLACE").toUpperCase());
                }
                trainingSch.setTxtnote(rs.getString("NOTE"));
                trainingSch.setNisb(rs.getString("ACS"));
                trainingSch.setTxtOrdNo(rs.getString("ord_no"));
                trainingSch.setTxtOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("ord_date")));
                if(rs.getString("if_visible")!=null && rs.getString("if_visible").equals("N")){
                    trainingSch.setChkNotSBPrint("Y");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return trainingSch;
    }

    public void deleteTraining(String trainId) {
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = this.dataSource.getConnection();
            pst = con.prepareStatement("DELETE FROM EMP_TRAIN WHERE TRAINID=?");
            pst.setInt(1, Integer.parseInt(trainId));
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

}
