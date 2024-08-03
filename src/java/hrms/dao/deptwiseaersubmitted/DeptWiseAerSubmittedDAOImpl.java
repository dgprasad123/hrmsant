package hrms.dao.deptwiseaersubmitted;

import hrms.common.DataBaseFunctions;
import hrms.model.deptwiseaersubmitted.DeptWiseAerSubmitted;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class DeptWiseAerSubmittedDAOImpl implements DeptWiseAerSubmittedDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List getAerOffList(String deptCode, String fiscalYear) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList offList = new ArrayList();
        try {
            con = this.dataSource.getConnection();
            pstmt = con.prepareStatement("select g_office.off_code,off_en,status,aer_id from g_office  "
                    + "left outer join aer_report_submit on g_office.off_code=aer_report_submit.off_code "
                    + "where fy=? and department_code=?");
            pstmt.setString(1, fiscalYear);
            pstmt.setString(2, deptCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                DeptWiseAerSubmitted offListReport = new DeptWiseAerSubmitted();
                offListReport.setAerId(rs.getString("aer_id"));
                offListReport.setOffName(rs.getString("off_en"));
                if(rs.getString("status")!=null && rs.getString("status").equals("YES")){
                offListReport.setStatus("YES");
                }else if(rs.getString("status")!=null && rs.getString("status").equals("COMP")){
                  offListReport.setStatus("COMPLETED");  
                }else{
                    offListReport.setStatus("NO");
                }
                offList.add(offListReport);
            }
        } catch (Exception exe) {
            exe.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return offList;
    }

}
