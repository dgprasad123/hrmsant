package hrms.dao.GPHeadUpdate;

import hrms.model.GPHeadUpdate.GPHeadUpdateForm;

public interface GPHeadDAO {
    
    public GPHeadUpdateForm getBillDetails(GPHeadUpdateForm gpheadform);
    
    public int updateGPHead(GPHeadUpdateForm gpheadform);
}
