package hrms.dao.regularizationService;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.model.regularizeService.RegularizeServiceForm;
import hrms.model.regularizeService.RegularizeServiceList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class RegularizationServiceDAOImpl implements RegularizationServiceDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    protected MaxRegularizationServiceIDDAOImpl maxRegularizationServiceIDDAO;
    protected ServiceBookLanguageDAO sbDAO;
    protected NotificationDAO notificationDao;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setMaxRegularizationServiceIDDAO(MaxRegularizationServiceIDDAOImpl maxRegularizationServiceIDDAO) {
        this.maxRegularizationServiceIDDAO = maxRegularizationServiceIDDAO;
    }

    public void setSbDAO(ServiceBookLanguageDAO sbDAO) {
        this.sbDAO = sbDAO;
    }

    public void setNotificationDao(NotificationDAO notificationDao) {
        this.notificationDao = notificationDao;
    }
    

    @Override
    public void insertRegularizationData(RegularizeServiceForm regularizeService, int notId) {

        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "INSERT INTO emp_regularization(reg_id,emp_id,not_id,not_type,f_date,f_time,t_date,t_time) VALUES(?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, maxRegularizationServiceIDDAO.getMaxRegularizationServiceId());
            pst.setString(2, regularizeService.getEmpid());
            pst.setInt(3, notId);
            pst.setString(4, regularizeService.getHnotType());
            if (regularizeService.getTxtFrmDt() != null && !regularizeService.getTxtFrmDt().equals("")) {
                pst.setTimestamp(5, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(regularizeService.getTxtFrmDt()).getTime()));
            } else {
                pst.setTimestamp(5, null);
            }
            pst.setString(6, regularizeService.getSltFrmTime());
            if (regularizeService.getTxtToDt() != null && !regularizeService.getTxtToDt().equals("")) {
                pst.setTimestamp(7, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(regularizeService.getTxtToDt()).getTime()));
            } else {
                pst.setTimestamp(7, null);
            }
            pst.setString(8, regularizeService.getSltToTime());
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            sql = "UPDATE EMP_MAST SET IS_REGULAR='Y' WHERE EMP_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, regularizeService.getEmpid());
            pst.executeUpdate();
            String sbLang=sbDAO.getRegularizationLangDetails(regularizeService, notId);
            notificationDao.saveServiceBookLanguage(sbLang, notId, "REGULARIZATION");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }

    }

    @Override
    public void updateRegularizationData(RegularizeServiceForm regularizeService) {

        Connection con = null;

        PreparedStatement pst = null;

        try {
            con = this.dataSource.getConnection();

            String sql = "UPDATE emp_regularization SET f_date=?,f_time=?,t_date=?,t_time=? WHERE EMP_ID=? AND reg_id=?";
            pst = con.prepareStatement(sql);
            if (regularizeService.getTxtFrmDt() != null && !regularizeService.getTxtFrmDt().equals("")) {
                pst.setTimestamp(1, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(regularizeService.getTxtFrmDt()).getTime()));
            } else {
                pst.setTimestamp(1, null);
            }
            pst.setString(2, regularizeService.getSltFrmTime());
            if (regularizeService.getTxtToDt() != null && !regularizeService.getTxtToDt().equals("")) {
                pst.setTimestamp(3, new Timestamp(new SimpleDateFormat("dd-MMM-yyyy").parse(regularizeService.getTxtToDt()).getTime()));
            } else {
                pst.setTimestamp(3, null);
            }
            pst.setString(4, regularizeService.getSltToTime());
            pst.setString(5, regularizeService.getEmpid());
            pst.setString(6, regularizeService.getHregid());
            pst.executeUpdate();

            DataBaseFunctions.closeSqlObjects(pst);

            sql = "UPDATE EMP_MAST SET IS_REGULAR='Y' WHERE EMP_ID=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, regularizeService.getEmpid());
            pst.executeUpdate();
            String sbLang=sbDAO.getRegularizationLangDetails(regularizeService, regularizeService.getHnotid());
            notificationDao.saveServiceBookLanguage(sbLang, regularizeService.getHnotid(), "REGULARIZATION");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void deleteRegularizationData(String recruitId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public RegularizeServiceForm editRegularizationData(String empid, int notId) {
        
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;
        
        RegularizeServiceForm regularizeServiceForm = new RegularizeServiceForm();
        try{
            con = this.dataSource.getConnection();
            
            String sql = "select not_id,not_type,ordno,orddt,dept_code,off_code,auth,note,if_visible from emp_notification where emp_id=? and not_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1,empid);
            pst.setInt(2,notId);
            rs = pst.executeQuery();
            if(rs.next()){
                regularizeServiceForm.setHnotid(rs.getInt("not_id"));
                regularizeServiceForm.setTxtNotOrdNo(rs.getString("ordno"));
                regularizeServiceForm.setTxtNotOrdDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("orddt")));
                regularizeServiceForm.setHidNotifyingDeptCode(rs.getString("dept_code"));
                regularizeServiceForm.setHidNotifyingOffCode(rs.getString("off_code"));
                regularizeServiceForm.setNotifyingSpc(rs.getString("auth"));
                regularizeServiceForm.setNotifyingPostName(CommonFunctions.getSPN(con, rs.getString("auth")));
                regularizeServiceForm.setNote(rs.getString("note"));
                if(rs.getString("if_visible")!=null && rs.getString("if_visible").equals("N")){
                    regularizeServiceForm.setChkNotSBPrint("Y");
                }
            }
            
            DataBaseFunctions.closeSqlObjects(pst);
            
            sql = "select reg_id,f_date,f_time,t_date,t_time from emp_regularization where emp_id=? and not_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1,empid);
            pst.setInt(2,notId);
            rs = pst.executeQuery();
            if(rs.next()){
                regularizeServiceForm.setHregid(rs.getString("reg_id"));
                regularizeServiceForm.setTxtFrmDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("f_date")));
                regularizeServiceForm.setSltFrmTime(rs.getString("f_time"));
                regularizeServiceForm.setTxtToDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("t_date")));
                regularizeServiceForm.setSltToTime(rs.getString("t_time"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
      return regularizeServiceForm;  
    }

    @Override
    public List findAllRegularizationData(String empId) {
        
        Connection con = null;

        PreparedStatement pst = null;
        ResultSet rs = null;

        RegularizeServiceList regularizeServiceList = null;

        List list = new ArrayList();
        try {
            con = this.dataSource.getConnection();

            pst = con.prepareStatement("select emp_notification.doe,emp_notification.not_id,emp_notification.not_type,ordno,orddt,f_date,f_time,t_date,t_time from" +
                                       " (select emp_id,doe,not_id,not_type,ordno,orddt from emp_notification where emp_id=? and" +
                                       " not_type='REGULARIZATION')emp_notification" +
                                       " left outer join emp_regularization on emp_notification.not_id=emp_regularization.not_id");
            pst.setString(1, empId);
            rs = pst.executeQuery();
            while (rs.next()) {
                regularizeServiceList = new RegularizeServiceList();
                regularizeServiceList.setNotId(rs.getString("NOT_ID"));
                regularizeServiceList.setDoe(CommonFunctions.getFormattedOutputDate1(rs.getDate("DOE")));
                regularizeServiceList.setFromDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("f_date")));
                regularizeServiceList.setToDt(CommonFunctions.getFormattedOutputDate1(rs.getDate("t_date")));
                list.add(regularizeServiceList);
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
