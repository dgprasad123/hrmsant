/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.allowedToOfficiate;

import hrms.model.absorption.AbsorptionModel;
import hrms.model.allowedToOfficiate.AllowedToOfficiateModel;
import java.util.List;

/**
 *
 * @author Madhusmita
 */
public interface AllowedToOfficiateDAO {

    public List findallOfficiation(String empid);

    public int insertOfficiationData(AllowedToOfficiateModel officiation, int notId);

    public int updateOfficiationData(AllowedToOfficiateModel officiation, int notId);

    public AllowedToOfficiateModel editOfficiationData(int notId, String empid);

    public int deleteOfficiationData(AllowedToOfficiateModel officiation);

}
