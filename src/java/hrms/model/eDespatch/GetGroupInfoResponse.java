/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.model.eDespatch;

import java.util.List;

/**
 *
 * @author Manas
 */
public class GetGroupInfoResponse {

    public List<GroupInfoResponse> groupInfoResponse;

    public List<GroupInfoResponse> getGroupInfoResponse() {
        return groupInfoResponse;
    }

    public void setGroupInfoResponse(List<GroupInfoResponse> groupInfoResponse) {
        this.groupInfoResponse = groupInfoResponse;
    }

}
