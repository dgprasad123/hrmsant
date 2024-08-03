/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.dao.common;

import hrms.model.common.DowanloadFile;

/**
 *
 * @author Manas
 */
public interface DownloadDAO {
    public DowanloadFile downloadGrievanceAttachment(int attachmentId);
}
