
package hrms.dao.master;

import hrms.model.master.PostOffice;
import java.util.ArrayList;

public interface PostOfficeDAO {
    public ArrayList getPostOfficeList(String distCode);
    
    public PostOffice  editPostOffice(PostOffice  postoff);
    
    public void saveNewPostOffice(PostOffice  postoff);
    
    public void updatePostOffice(PostOffice  postoff);
}
