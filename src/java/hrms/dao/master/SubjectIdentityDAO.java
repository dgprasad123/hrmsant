/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.model.master.SubjectIdentityBean;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Surendra
 */
public interface SubjectIdentityDAO {

    public ArrayList getsubjectIdentityList();

    public void saveSubjectDetails(SubjectIdentityBean subjectIdentityBean);

    public SubjectIdentityBean editSubject(int subjectSlno);

    public void updateSubjectDetails(SubjectIdentityBean subjectIdentityBean);

    public void deleteDegree(int subjectSlno);

}
