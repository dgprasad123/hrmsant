/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.dao.misreport;

import java.util.List;

/**
 *
 * @author Manas
 */
public interface QueryBuilderDAO {
    public List getResultSet(String sql) throws Exception;
}
