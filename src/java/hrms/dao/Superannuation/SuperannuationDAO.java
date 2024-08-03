/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.dao.Superannuation;

import java.util.List;

/**
 *
 * @author shantanu
 */
public interface SuperannuationDAO {
    
    public List getSuperannuationList(String distCode, String supannYear, String supannMonth);
}
