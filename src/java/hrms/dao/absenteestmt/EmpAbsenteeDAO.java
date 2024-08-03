package hrms.dao.absenteestmt;

import hrms.model.absentee.Absentee;
import java.util.ArrayList;
import java.util.List;
import jxl.Workbook;


public interface EmpAbsenteeDAO {

    public ArrayList getAbseneteeList(String empid, int year, int month);

    public Absentee getAbsenteeDetail(int absid);

    public boolean saveAbsenteeData(Absentee abform);
    
    public boolean updateAbsenteeData(Absentee abform);
    
    public boolean deleteAbsEmploye(String empId, String absid);

    public ArrayList getAbseneteeYear(String empid);

    public void addExcelRowIntoDB(Workbook workbook, List li, int month, int year, String offCode);

    public boolean countMonthAttendance(String offCode, int month, int year);

    public ArrayList getAttendanceList(String offCode, int year, int month);

    public void deleteAttendanceList(String offCode, int year, int month);
    
    public int isBilledEmployee(String empid,int year,int month);
    
    public void deleteAbsenteeList(String empid,int absid);
    
    public int getnoofdaysInLeave(String empid,int year,int month);
    
    public int getnoofDaysTobeDelete(String empid,int absid);
    
}
