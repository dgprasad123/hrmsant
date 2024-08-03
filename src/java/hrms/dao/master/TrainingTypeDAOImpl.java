package hrms.dao.master;

import hrms.common.DataBaseFunctions;
import hrms.model.master.Training;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;

public class TrainingTypeDAOImpl implements TrainingTypeDAO {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List getTrainingType() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List trainingTypeList = new ArrayList();
        Training trn = null;
        try {
            con = dataSource.getConnection();
            pstmt = con.prepareStatement("SELECT TRAINING_TYPE_ID,TRAINING_TYPE FROM G_TRAINING_TYPE ORDER BY TRAINING_TYPE ASC");
            rs=pstmt.executeQuery();
            while (rs.next()) {
                trn = new Training();
                trn.setTrainingtypeid(rs.getString("TRAINING_TYPE_ID"));
                trn.setTrainingtype(rs.getString("TRAINING_TYPE"));
                trainingTypeList.add(trn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, pstmt);
            DataBaseFunctions.closeSqlObjects(con);
        }
        return trainingTypeList;
    }
}
