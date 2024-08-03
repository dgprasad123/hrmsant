package hrms.common;

import java.io.Serializable;
import java.lang.reflect.Field;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

public class GeneratorType implements IdentifierGenerator {

    @Override
    public Serializable generate(SessionImplementor var1, Object object) throws HibernateException {
        int mcode = 0;
        String code = "";
        SessionFactory fact = null;
        try {
            fact = var1.getFactory();
            Session session = fact.openSession();
            Field[] f = object.getClass().getDeclaredFields();
            String id = f[0].getName();
            
            Query query = session.createQuery("select max(" + id + ") from " + object.getClass().getName());
            if(query.list().get(0) != null && !query.list().get(0).equals("")){
                mcode = (int) query.list().get(0);
                
                if (mcode > 0) {
                    mcode = mcode + 1;
                } else {
                    mcode = 1;
                }
            }else{
                mcode = 1;
            }
            session.flush();
            session.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mcode;

    }
    
    public static String getNextString(String previousString) {
        String nextString = "";
        long prevNum = 0;
        String zeroStr = "";
        if (previousString != null && !previousString.equals("")) {

            if (isNumeric(previousString) == true) {
                prevNum = Long.parseLong(previousString);
                prevNum++;
                nextString = String.valueOf(prevNum);
                if (nextString.length() < previousString.length()) {
                    for (int cnt = 0; cnt < (previousString.length() - nextString.length()); cnt++) {
                        zeroStr = zeroStr + "0";
                    }
                    nextString = zeroStr + nextString;
                }
            } else {
                if (isNumericChar(previousString.substring(0, 1)) == true) {

                    String tmp1 = previousString.substring(previousString.length() - 1, previousString.length());
                    char tmp = tmp1.charAt(0);
                    int asc = (int) tmp;
                    if (asc < 90) {
                        asc = asc + 1;
                        char[] newStr = {(char) asc};
                        
                        String finalStr = new String(newStr);
                        
                        
                        nextString = previousString.substring(0, previousString.length() - 1) + finalStr;
                    } else {
                        nextString = previousString + "A";
                    }

                } else {
                    int i = 2;
                    for (i = 0; i < previousString.length(); i++) {
                        if (isNumericChar(previousString.substring(i, i + 1)) == true) {
                            break;
                        }
                    }
                    prevNum = Long.parseLong(previousString.substring(i, previousString.length()));
                    prevNum++;
                    for (int cnt = 0; cnt < (previousString.length() - i) - String.valueOf(prevNum).length(); cnt++) {
                        zeroStr = zeroStr + "0";
                    }
                    nextString = zeroStr + nextString;
                    nextString = previousString.substring(0, i) + zeroStr + String.valueOf(prevNum);
                }
            }
        } else {
            nextString = "1";
        }
        return nextString;
    }

    public static boolean isNumeric(String inStr) {
        boolean result = false;
        try {
            long num = Long.parseLong(inStr);
            result = true;
        } catch (NumberFormatException nfe) {
            //nfe.printStackTrace();
            result = false;
        }
        return result;
    }

    public static boolean isNumericChar(String inStr) {
        boolean result = false;
        if (inStr.equals("0") || inStr.equals("1") || inStr.equals("2") || inStr.equals("3") || inStr.equals("4") || inStr.equals("5") || inStr.equals("6") || inStr.equals("7") || inStr.equals("8") || inStr.equals("9")) {
            result = true;
        }
        return result;
    }

}
