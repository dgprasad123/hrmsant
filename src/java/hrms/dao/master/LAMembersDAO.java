/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import hrms.model.lamembers.LAMembers;
import java.util.List;

/**
 *
 * @author Manas
 */
public interface LAMembersDAO {

    public List getLAMembersList();

    public List getLAMembersList(String officiating);

    public LAMembers getLAMemberDetail(int lmid);

    public void saveLAMember(LAMembers lamembers);

    public void inActivateMember(int lmid);

    public void activateMember(int lmid);
}
