package hrms.dao.posting;

import hrms.model.posting.PostingForm;
import java.sql.SQLException;
import java.util.List;

public interface PostingDAO {
    
    public List findAllPosting(String empid);
    
    public void savePosting(PostingForm postingform, int notid);
    
    public void updatePosting(PostingForm postingform);
    
    public void deletePosting(PostingForm postingform);
    
    public PostingForm getPostingData(PostingForm postingform, int notificationId) throws SQLException;
    
}
