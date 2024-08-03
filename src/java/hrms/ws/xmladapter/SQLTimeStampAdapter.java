/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.ws.xmladapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author DurgaPrasad
 */
public class SQLTimeStampAdapter extends XmlAdapter <java.util.Date,java.sql.Timestamp>{
    @Override
    public java.util.Date marshal(java.sql.Timestamp sqlDate) throws Exception {
        if(null == sqlDate) {
            return null;
        }
        return new java.util.Date(sqlDate.getTime());
    }

    @Override
    public java.sql.Timestamp unmarshal(java.util.Date utilDate) throws Exception {
        if(null == utilDate) {
            return null;
        }
        return new java.sql.Timestamp(utilDate.getTime());
    }

}
