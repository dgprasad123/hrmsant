/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.model.master.QualificationBean;
import java.util.ArrayList;

/**
 *
 * @author Surendra
 */
public interface QualificationMasterDAO {

    public ArrayList getQualificationList();

    public QualificationBean editQualification(int slno);

    public void saveUserDetails(QualificationBean qualificationBean);

    public void updateUserDetails(QualificationBean qualificationBean);

    public void deleteQualification(int slno);

}
