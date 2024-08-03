/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.common;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.sql.DataSource;

/**
 *
 * @author sur
 */
public class DataBaseFunctions {

    public static void closeSqlObjects(CallableStatement proc) {
        try {
            if (proc != null) {
                proc.close();
                proc = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*THIS FOLLOWING METHODS ARE USED FOR CLOSING SQL OBJECTS */

    public static void closeSqlObjects(DataSource ds, Statement st, Statement st1, Connection con) {
        try {

            if (st != null) {
                st.close();
                st = null;
            }
            if (st1 != null) {
                st1.close();
                st1 = null;
            }
            if (con != null) {
                con.close();
                con = null;
            }
            if (ds != null) {
                ds = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void closeSqlObjects(DataSource ds, Statement st, Connection con) {
        try {

            if (st != null) {
                st.close();
                st = null;
            }
            if (con != null) {
                con.close();
                con = null;
            }
            if (ds != null) {
                ds = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void closeSqlObjects(DataSource ds, Connection con) {
        try {

            if (con != null) {
                con.close();
                con = null;
            }
            if (ds != null) {
                ds = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void closeSqlObjects(ResultSet rs, Statement st, Connection con) {
        try {
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (st != null) {
                st.close();
                st = null;
            }
            if (con != null) {
                con.close();
                con = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void closeSqlObjects(ResultSet rs, Statement st) {
        try {
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (st != null) {
                st.close();
                st = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void closeSqlObjects(ResultSet rs1, ResultSet rs2) {
        try {
            if (rs1 != null) {
                rs1.close();
                rs1 = null;
            }
            if (rs2 != null) {
                rs2.close();
                rs2 = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void closeSqlObjects(ResultSet rs1, ResultSet rs2, ResultSet rs3) {
        try {
            if (rs1 != null) {
                rs1.close();
                rs1 = null;
            }
            if (rs2 != null) {
                rs2.close();
                rs2 = null;
            }
            if (rs3 != null) {
                rs3.close();
                rs3 = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void closeSqlObjects(ResultSet rs1, ResultSet rs2, ResultSet rs3, ResultSet rs4) {
        try {
            if (rs1 != null) {
                rs1.close();
                rs1 = null;
            }
            if (rs2 != null) {
                rs2.close();
                rs2 = null;
            }
            if (rs3 != null) {
                rs3.close();
                rs3 = null;
            }
            if (rs4 != null) {
                rs4.close();
                rs4 = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void closeSqlObjects(Statement st1, Statement st2) {
        try {
            if (st1 != null) {
                st1.close();
                st1 = null;
            }
            if (st2 != null) {
                st2.close();
                st2 = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void closeSqlObjects(Statement st1, Statement st2, Statement st3) {
        try {
            if (st1 != null) {
                st1.close();
                st1 = null;
            }
            if (st2 != null) {
                st2.close();
                st2 = null;
            }
            if (st3 != null) {
                st3.close();
                st3 = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void closeSqlObjects(Statement st1, Statement st2, Statement st3, Statement st4) {
        try {
            if (st1 != null) {
                st1.close();
                st1 = null;
            }
            if (st2 != null) {
                st2.close();
                st2 = null;
            }
            if (st3 != null) {
                st3.close();
                st3 = null;
            }
            if (st4 != null) {
                st4.close();
                st4 = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void closeSqlObjects(PreparedStatement pst1, PreparedStatement pst2) {
        try {
            if (pst1 != null) {
                pst1.close();
                pst1 = null;
            }
            if (pst2 != null) {
                pst2.close();
                pst2 = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeSqlObjects(PreparedStatement pst1, PreparedStatement pst2, PreparedStatement pst3) {
        try {
            if (pst1 != null) {
                pst1.close();
                pst1 = null;
            }
            if (pst2 != null) {
                pst2.close();
                pst2 = null;
            }
            if (pst3 != null) {
                pst3.close();
                pst3 = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeSqlObjects(PreparedStatement pst1, PreparedStatement pst2, PreparedStatement pst3, PreparedStatement pst4) {
        try {
            if (pst1 != null) {
                pst1.close();
                pst1 = null;
            }
            if (pst2 != null) {
                pst2.close();
                pst2 = null;
            }
            if (pst3 != null) {
                pst3.close();
                pst3 = null;
            }
            if (pst4 != null) {
                pst4.close();
                pst4 = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeSqlObjects(PreparedStatement pst, ResultSet rs, Statement st, Connection con) {
        try {
            closeSqlObjects(rs, st, con);
            closeSqlObjects(pst);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeSqlObjects(PreparedStatement pst, ResultSet rs1, Statement st1, ResultSet rs2, Statement st2, Connection con) {
        try {
            closeSqlObjects(rs1, st1);
            closeSqlObjects(rs2, st2);
            closeSqlObjects(pst, con);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeSqlObjects(PreparedStatement pst1, PreparedStatement pst2, ResultSet rs1, ResultSet rs2, Connection con) {
        try {
            closeSqlObjects(pst1, rs1, null);
            closeSqlObjects(pst2, rs2, con);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeSqlObjects(PreparedStatement pst, ResultSet rs, Connection con) {
        try {
            closeSqlObjects(pst);
            closeSqlObjects(rs);
            closeSqlObjects(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeSqlObjects(PreparedStatement pst, Connection con) {
        try {
            closeSqlObjects(pst);
            closeSqlObjects(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeSqlObjects(PreparedStatement pst) {
        try {
            if (pst != null) {
                pst.close();
                pst = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeSqlObjects(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
                rs = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeSqlObjects(Statement st) {
        try {
            if (st != null) {
                st.close();
                st = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeSqlObjects(Connection con) {
        try {
            if (con != null) {
                con.close();
                con = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
