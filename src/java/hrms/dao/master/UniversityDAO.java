/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.master;

import java.util.ArrayList;
import hrms.model.master.University;
/**
 *
 * @author lenovo pc
 */
public interface UniversityDAO {
    public ArrayList getUniversityList();
    public University editUniversityBoard(University university);
    public University getUniversityBoardDetail(String boardCode);
    
    public void saveNewBoard(University  university);
    
    public void updateNewBoard(University  university);
    
    public void deleteBoardDetail(University  university);
}
