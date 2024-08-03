package hrms.dao.departmentalExam;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.departmentalExam.DepartmentalExamBean;
import hrms.model.departmentalExam.DepartmentalExamForm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class DepartmentalExamDAOImpl implements DepartmentalExamDAO{
    
    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    public List getDepartmentalExamList(String empid) {
        
        Connection con = null;
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        List deptexamlist = new ArrayList();
        DepartmentalExamBean deBean = null;
        try{
            con = this.dataSource.getConnection();
            
            String sql = "select * from emp_exam where emp_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1,empid);
            rs = pst.executeQuery();
            while(rs.next()){
                deBean = new DepartmentalExamBean();
                deBean.setExamId(rs.getString("ex_id"));
                deBean.setEmpid(rs.getString("emp_id"));
                deBean.setDateofentry(CommonFunctions.getFormattedOutputDate1(rs.getDate("doe")));
                deBean.setExaminationName(rs.getString("exam_name"));
                deBean.setFromDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("f_date")));
                deBean.setToDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("t_date")));
                deBean.setExaminationResult(rs.getString("result"));
                deptexamlist.add(deBean);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            DataBaseFunctions.closeSqlObjects(rs, pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
      return deptexamlist;  
    }

    @Override
    public void saveDepartmentalExam(DepartmentalExamForm deptExamForm,String logindept,String loginoffice,String loginpost,String loginempid,String sblang) {
        
        Connection con = null;
        
        PreparedStatement pst = null;
        
        try{
            con = this.dataSource.getConnection();
            
            String startTime = "";
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            startTime = dateFormat.format(cal.getTime());
            
            String sql = "insert into emp_exam(emp_id,doe,exam_name,inst_name,f_date,t_date,not_no,not_dt,not_dept,result,dept_code,auth_off,auth,ent_dept,ent_off,ent_auth,note,entry_taken_by,entry_taken_on,sb_description,if_visible) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(1,deptExamForm.getEmpid());
            pst.setTimestamp(2, new Timestamp(dateFormat.parse(startTime).getTime()));
            pst.setString(3,deptExamForm.getTxtExamName());
            pst.setString(4,deptExamForm.getTxtInstituteName());
            if(deptExamForm.getTxtFromDate() != null && !deptExamForm.getTxtFromDate().equals("")){
                pst.setTimestamp(5, new Timestamp(dateFormat.parse(deptExamForm.getTxtFromDate()).getTime()));
            }else{
                pst.setTimestamp(5, null);
            }
            if(deptExamForm.getTxtToDate() != null && !deptExamForm.getTxtToDate().equals("")){
                pst.setTimestamp(6, new Timestamp(dateFormat.parse(deptExamForm.getTxtToDate()).getTime()));
            }else{
                pst.setTimestamp(6, null);
            }
            pst.setString(7,deptExamForm.getTxtNotNo());
            if(deptExamForm.getTxtNotDate() != null && !deptExamForm.getTxtNotDate().equals("")){
                pst.setTimestamp(8, new Timestamp(dateFormat.parse(deptExamForm.getTxtNotDate()).getTime()));
            }else{
                pst.setTimestamp(8, null);
            }
            pst.setString(9,deptExamForm.getSltNotDept());
            pst.setString(10,deptExamForm.getRdExamResult());
            pst.setString(11,deptExamForm.getHidAuthDeptCode());
            pst.setString(12,deptExamForm.getHidAuthOffCode());
            pst.setString(13,deptExamForm.getAuthPost());
            pst.setString(14,logindept);
            pst.setString(15,loginoffice);
            pst.setString(16,loginpost);
            pst.setString(17,deptExamForm.getTxtNote());
            pst.setString(18,loginempid);
            pst.setTimestamp(19,new Timestamp(new Date().getTime()));
            pst.setString(20,sblang);
            pst.setString(21, deptExamForm.getIfvisible());
            pst.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public void updateDepartmentalExam(DepartmentalExamForm deptExamForm,String sblang) {
        
        Connection con = null;
        
        PreparedStatement pst = null;
        
        try{
            con = this.dataSource.getConnection();
            
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            
            String sql = "update emp_exam set exam_name=?,inst_name=?,f_date=?,t_date=?,not_no=?,not_dt=?,not_dept=?,result=?,dept_code=?,auth_off=?,auth=?,note=?,sb_description=?,if_visible=? where emp_id=? and ex_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1,deptExamForm.getTxtExamName());
            pst.setString(2,deptExamForm.getTxtInstituteName());
            if(deptExamForm.getTxtFromDate() != null && !deptExamForm.getTxtFromDate().equals("")){
                pst.setTimestamp(3, new Timestamp(dateFormat.parse(deptExamForm.getTxtFromDate()).getTime()));
            }else{
                pst.setTimestamp(3, null);
            }
            if(deptExamForm.getTxtToDate() != null && !deptExamForm.getTxtToDate().equals("")){
                pst.setTimestamp(4, new Timestamp(dateFormat.parse(deptExamForm.getTxtToDate()).getTime()));
            }else{
                pst.setTimestamp(4, null);
            }
            pst.setString(5,deptExamForm.getTxtNotNo());
            if(deptExamForm.getTxtNotDate() != null && !deptExamForm.getTxtNotDate().equals("")){
                pst.setTimestamp(6, new Timestamp(dateFormat.parse(deptExamForm.getTxtNotDate()).getTime()));
            }else{
                pst.setTimestamp(6, null);
            }
            pst.setString(7,deptExamForm.getSltNotDept());
            pst.setString(8,deptExamForm.getRdExamResult());
            pst.setString(9,deptExamForm.getHidAuthDeptCode());
            pst.setString(10,deptExamForm.getHidAuthOffCode());
            pst.setString(11,deptExamForm.getAuthPost());
            pst.setString(12,deptExamForm.getTxtNote());
            pst.setString(13,sblang);
            pst.setString(14, deptExamForm.getIfvisible());
            pst.setString(15,deptExamForm.getEmpid());
            pst.setInt(16,Integer.parseInt(deptExamForm.getExamId()));
            pst.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            DataBaseFunctions.closeSqlObjects(pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
    }

    @Override
    public DepartmentalExamForm editDepartmentalExam(String empid, String examid) {
        
        Connection con = null;
        
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        DepartmentalExamForm deptexamform = null;
        try{
            con = this.dataSource.getConnection();
            
            String sql = "select emp_exam.*,post,dist_code from emp_exam" +
                         " left outer join g_post on emp_exam.auth=g_post.post_code" +
                         " left outer join g_office on emp_exam.auth_off=g_office.off_code" +
                         " where emp_id=? and ex_id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1,empid);
            pst.setInt(2,Integer.parseInt(examid));
            rs = pst.executeQuery();
            if(rs.next()){
                deptexamform = new DepartmentalExamForm();
                deptexamform.setExamId(rs.getString("ex_id"));
                deptexamform.setEmpid(rs.getString("emp_id"));
                deptexamform.setTxtExamName(rs.getString("exam_name"));
                deptexamform.setTxtInstituteName(rs.getString("inst_name"));
                deptexamform.setTxtFromDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("f_date")));
                deptexamform.setTxtToDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("t_date")));
                deptexamform.setTxtNotNo(rs.getString("not_no"));
                deptexamform.setTxtNotDate(CommonFunctions.getFormattedOutputDate1(rs.getDate("not_dt")));
                deptexamform.setSltNotDept(rs.getString("not_dept"));
                deptexamform.setRdExamResult(rs.getString("result"));
                deptexamform.setHidAuthDeptCode(rs.getString("dept_code"));
                deptexamform.setHidAuthDistCode(rs.getString("dist_code"));
                deptexamform.setHidAuthOffCode(rs.getString("auth_off"));
                deptexamform.setAuthPost(rs.getString("auth"));
                deptexamform.setConductingPostName(rs.getString("post"));
                deptexamform.setTxtNote(rs.getString("note"));
                if(rs.getString("if_visible")!=null && rs.getString("if_visible").equals("N")){
                    deptexamform.setChkNotSBPrint("Y");
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            DataBaseFunctions.closeSqlObjects(rs,pst);
            DataBaseFunctions.closeSqlObjects(con);
        }
      return deptexamform;  
    }
}
