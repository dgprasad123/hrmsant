/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.dao.reservationcategory;


import hrms.model.reservationcategory.ReservationCategoryInformation;
import java.util.List;

/**
 *
 * @author HP
 */
public interface ReservationCategoryInformationDAO {
   
    public void saveReservationCategoryInformation(ReservationCategoryInformation rci);
    public void updateReservationCategoryInformation(ReservationCategoryInformation rci);
    public List getReservationCategoryInformation(String EmpId);
    public ReservationCategoryInformation editReservationCategory(String notid );
}
