/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.placementofservice;

import hrms.model.placementofservice.PlacementOfServiceForm;
import java.util.List;

/**
 *
 * @author DurgaPrasad
 */
public interface PlacementOfServiceDAO {

    public List getPlacementOfServiceList(String empid);

    public List getAllotDescList(String notType);

    public void savePlacementOfService(PlacementOfServiceForm placementOfServiceForm, int notId, String loginempid, String sblanguage);

    public void updatePlacementOfService(PlacementOfServiceForm placementOfServiceForm, String loginempid);

    public PlacementOfServiceForm getPlacementOfServiceData(PlacementOfServiceForm placementOfServiceForm, int notId);

}
