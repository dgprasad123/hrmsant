/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.punishment;

import hrms.model.punishment.PunishmentBean;
import java.util.List;

/**
 *
 * @author Manoj PC
 */
public interface PunishmentDAO {

    public List getPunishmentTypes();

    public void savePunishment(PunishmentBean pb, int notid);

    public List getPunishmentList(String empId);

    public PunishmentBean getEmpPunishmentData(String acId);

    public void updatePunishment(PunishmentBean pb);

    public void deletePunishment(PunishmentBean pb);
}
