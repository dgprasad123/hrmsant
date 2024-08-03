package hrms.common;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ChronologySerial {

    public static void addToSBOutputFromServiceBook(Connection con, String empid, String crslno, String notType, String notId) {

        PreparedStatement pst = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            String prevSlno = "";
            st = con.createStatement();
            rs = st.executeQuery("SELECT MAX(CHRONOLOGY_SERIAL_NO) CHRONOLOGY_SERIAL_NO FROM (  SELECT ROWNUM RN,CHRONOLOGY_SERIAL_NO,NOT_TYPE FROM SB_OUTPUT WHERE EMP_ID='" + empid + "' AND CHRONOLOGY_SERIAL_NO<" + crslno + " ORDER BY CHRONOLOGY_SERIAL_NO DESC) ");
            if (rs.next()) {
                prevSlno = rs.getString("CHRONOLOGY_SERIAL_NO");
            } else {
                prevSlno = "0";
            }

            BigDecimal bg1 = new BigDecimal(prevSlno);
            BigDecimal bg2 = new BigDecimal(crslno);
            String slno = ChronologySerial.generateSerialNumber(bg1, bg2);
            pst = con.prepareStatement("INSERT INTO SB_OUTPUT (CHRONOLOGY_SERIAL_NO,NOT_ID,NOT_TYPE,EMP_ID) VALUES(?,?,?,?)");
            pst.setString(1, slno);
            pst.setString(2, notId);
            pst.setString(3, notType);
            pst.setString(4, empid);
            pst.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(pst);
        }
    }

    public static String generateSerialNumber(BigDecimal bgsl1, BigDecimal bgsl2) {

        // =================                                    STEP -1             ===============================================================  
        BigDecimal diff = bgsl2.subtract(bgsl1);
            

        // =================                                    STEP -2         ===============================================================  
        String str = diff.toString();
        StringTokenizer stringtokenizer = new StringTokenizer(str, ".");
        int i = 0;
        ArrayList al = new ArrayList();
        while (stringtokenizer.hasMoreElements()) {
            al.add(stringtokenizer.nextToken());
        }

        int decimal = 0;

        if (al.size() > 1) {
            decimal = Integer.parseInt(al.get(1).toString());
        }
        int numeralDifference = 0;

        if (diff.intValue() < 1) {
            if (decimal > 0) {
                numeralDifference = firstDigit(decimal);
            } else {
                String numberStr = diff.toString();
                String fractionalStr = numberStr.substring(numberStr.indexOf('.') + 1);
                int fractional = Integer.parseInt(fractionalStr);
                numeralDifference = firstDigit(fractional);
            }
        } else if (diff.intValue() > 1) {
            numeralDifference = decimal;
        }

        // =================                                    STEP -3            ===============================================================  
        DecimalFormat df = new DecimalFormat(".###");
        String roundedDiff1 = df.format(diff);  //Math.round(diff*1000)/1000;

        double tenthPosition = 0;

        tenthPosition = Double.parseDouble(roundedDiff1) / numeralDifference;
        if (tenthPosition > 1) {
            tenthPosition = 1;
        }

        // =================                                    STEP -4                    ===============================================================  
        double roundedDiff = 0;
        double numToAdd = 0;
        if (numeralDifference > 1) {
            numToAdd = tenthPosition;
        } else {
            numToAdd = tenthPosition / 10;
        }

        // =================                                    STEP -5                  ===============================================================  
        
        BigDecimal newSl3 = new BigDecimal(numToAdd);

        newSl3 = newSl3.add(bgsl1);

        // =================                                    STEP -6                  ===============================================================  
        if (newSl3.subtract(bgsl1).intValue() >= 1) {
            newSl3 = newSl3.setScale(0, BigDecimal.ROUND_FLOOR);// Math.floor(newSl3);

        } else if (bgsl2.subtract(newSl3).intValue() >= 1) {
            newSl3 = newSl3.setScale(0, BigDecimal.ROUND_CEILING); // Math.round(newSl3);
        }

        DecimalFormat df2 = new DecimalFormat("0.##########");
        String formatted = df2.format(newSl3);
        
        return formatted;
    }

    public static int firstDigit(int n) {
        while (n < -9 || 9 < n) {
            n /= 10;

        }
        return Math.abs(n);
    }

    public static int addToSBOutput(Connection con, String empid, String notType, String notid, String ifvisible, String doe, String ordt) {

        PreparedStatement pst = null;
        Statement st = null;
        ResultSet rs = null;

        int slno = 0;
        int retVal = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            
            st = con.createStatement();
            String sql = "SELECT (MAX(CHRONOLOGY_SERIAL_NO)+1) CHRONOLOGY_SERIAL_NO FROM ("
                    + "SELECT CHRONOLOGY_SERIAL_NO,NOT_TYPE FROM SB_OUTPUT WHERE EMP_ID='" + empid + "' ORDER BY CHRONOLOGY_SERIAL_NO DESC)TEMP";
            rs = st.executeQuery(sql);
            if (rs.next()) {
                slno = rs.getInt("CHRONOLOGY_SERIAL_NO");
            }

            pst = con.prepareStatement("INSERT INTO SB_OUTPUT (CHRONOLOGY_SERIAL_NO,NOT_ID,NOT_TYPE,EMP_ID,IF_VISIBLE,DOE,ORDDT) VALUES(?,?,?,?,?,?,?)");
            pst.setInt(1, slno);
            pst.setString(2, notid);
            pst.setString(3, notType.toUpperCase());
            pst.setString(4, empid);
            pst.setString(5, ifvisible);
            if (doe != null && !doe.equals("")) {
                pst.setTimestamp(6, new Timestamp(sdf.parse(doe).getTime()));
            } else {
                pst.setTimestamp(6, null);
            }
            if (ordt != null && !ordt.equals("")) {
                pst.setTimestamp(7, new Timestamp(sdf.parse(ordt).getTime()));
            } else {
                pst.setTimestamp(7, null);
            }
            retVal = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(pst);
        }
        return retVal;
    }

    public static void createProcedure(Connection con, String notType, String nid) throws Exception {
        CallableStatement callSt = null;
        try {
            callSt = con.prepareCall("{call MANAGE_UNDO_REDO_TRAN(?,?,?)}");
            callSt.setString(1, nid);
            callSt.setString(2, notType);
            callSt.setString(3, "CREATE");
            callSt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    public static int updateSBOutput(Connection con, String empid, String notType, String notid, String doe, String ordt, String ifvisible) throws Exception {

        PreparedStatement pst = null;
        Statement st = null;
        ResultSet rs = null;
        int retVal = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            

            pst = con.prepareStatement("UPDATE SB_OUTPUT SET DOE=?,ORDDT=?,IF_VISIBLE=? WHERE EMP_ID=? AND NOT_ID=? AND NOT_TYPE=?");
            pst.setTimestamp(1, new Timestamp(sdf.parse(doe).getTime()));
            if (ordt != null && !ordt.equals("")) {
                pst.setTimestamp(2, new Timestamp(sdf.parse(ordt).getTime()));
            } else {
                pst.setTimestamp(2, null);
            }
            pst.setString(3, ifvisible);
            pst.setString(4, empid);
            pst.setString(5, notid);
            pst.setString(6, notType);
            retVal = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(rs, st);
            DataBaseFunctions.closeSqlObjects(pst);
        }
        return retVal;
    }

    public static int deletefromSBOutput(Connection con, String empid, String notType, String notId) throws Exception {

        PreparedStatement pst = null;
        int retVal = 0;
        try {
            pst = con.prepareStatement("DELETE FROM SB_OUTPUT WHERE EMP_ID=? AND NOT_ID=? AND NOT_TYPE=?");
            pst.setString(1, empid);
            pst.setString(2, notId);
            pst.setString(3, notType);
            retVal = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataBaseFunctions.closeSqlObjects(pst);
        }
        return retVal;
    }
}
