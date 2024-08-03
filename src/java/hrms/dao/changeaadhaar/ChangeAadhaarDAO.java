package hrms.dao.changeaadhaar;

import hrms.common.Message;

public interface ChangeAadhaarDAO {
    
      public Message changeAadhaar(String empid,String newaadhaar);
      public String decryptAadhaar(String encryptedAadhaar);
    
}
