/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.model.AerAuthorization.AerAuthorizationBean;
import java.util.ArrayList;

/**
 *
 * @author manisha
 */
public interface AerAuthorizationDAO {

    public void saveAerauthorisationDetail(AerAuthorizationBean aerAuthorizationBean);

    public void saveAerauthorisationReviewerDetail(AerAuthorizationBean aerAuthorizationBean);

    public void saveAerauthorisationAcceptorDetail(AerAuthorizationBean aerAuthorizationBean);

    public void updateAerauthorisationReviewerDetail(AerAuthorizationBean aerAuthorizationBean);

    public void updateAerauthorisationAcceptorDetail(AerAuthorizationBean aerAuthorizationBean);

    public void saveEmpApproverAuth(AerAuthorizationBean aerAuthorizationBean);

    public ArrayList getProcessAuthorizationList(String financialyear, String offCode);

    public AerAuthorizationBean getProcessAuthorization(String financialyear, String offCode);

    public void updateAerauthorisationDetail(AerAuthorizationBean aerAuthorizationBean);

    public void updateEmpApproverAuth(AerAuthorizationBean aerAuthorizationBean);

    public void saveValidatorServiceBookAuth(AerAuthorizationBean aerAuthorizationBean);

    public void updateValidatorServiceBookAuth(AerAuthorizationBean aerAuthorizationBean);

    public void saveentryServiceBookAuth(AerAuthorizationBean aerAuthorizationBean);

    public void updateValidatorServiceBookentry(AerAuthorizationBean aerAuthorizationBean);

}
