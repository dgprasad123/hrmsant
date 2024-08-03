package hrms.dao.master;

import hrms.common.CommonFunctions;
import hrms.common.DataBaseFunctions;
import hrms.model.master.TrainingTitle;
import hrms.model.trainingschedule.TrainingSchedule;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class TrainingTitleDAOImpl implements TrainingTitleDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static String getServerDoe() {
        String currDate;
        Format formatter;
        formatter = new SimpleDateFormat("dd-MMM-yyyy");
        currDate = formatter.format(new Date());
        return currDate;
    }

    @Override
    public List getTrainingTitle() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List trainingTitleList = new ArrayList();
        TrainingTitle trainingTitle = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT training_title_id,training_title,trainingsl FROM g_training_title");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                trainingTitle = new TrainingTitle();
                trainingTitle.setTrainingTitleId(rs.getString("training_title_id"));
                trainingTitle.setTrainingTitle(rs.getString("training_title"));
                trainingTitle.setTrainingslId((rs.getString("training_title_id") +"-"+ rs.getString("trainingsl")));
                trainingTitleList.add(trainingTitle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return trainingTitleList;
    }

   

}
