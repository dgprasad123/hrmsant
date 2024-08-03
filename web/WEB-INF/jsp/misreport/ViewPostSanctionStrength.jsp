<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" type="text/css" href="css/bootstrap.css"/>
        <link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap4.min.css"/>
        <script type="text/javascript"  src="js/jquery-1.12.4.js"></script>
        <script type="text/javascript"  src="js/jquery.dataTables.min.js"></script>
        <script type="text/javascript"  src="js/dataTables.bootstrap4.min.js"></script>
        <script language="javascript" type="text/javascript" >
            $(document).ready(function () {
                $('#example').DataTable({
                    "order": [[1, "ASC"]]
                });
            });
        </script>
    </head>
    <body>

        <table  class="table table-striped table-bordered" width="100%" cellspacing="0">
            <thead>
                <tr>
                    <th rowspan="2">Sl no</th>
                    <th rowspan="2">Name of the Department</th>
                    <th rowspan="2">District</th>
                    <th colspan="2">Post</th>                       
                </tr>
                <tr>
                    <th colspan="2">Junior clerk(GP-1900)</th>                        
                    <th colspan="2">Senior Clerk(GP-2400)</th>
                    <th colspan="2">Head Clerk(GP-4200)</th> 
                    <th colspan="2">Office superintendent (4600)</th> 
                </tr>
                <tr>
                    <th>&nbsp;</th>                        
                    <th>&nbsp;</th>
                    <th>&nbsp;</th> 
                    <th>Sanctioned Strength </th> 
                    <th>Men in position </th> 
                    <th>Sanctioned Strength </th> 
                    <th>Men in position </th> 
                    <th>Sanctioned Strength </th> 
                    <th>Men in position </th> 
                    <th>Sanctioned Strength </th> 
                    <th>Men in position </th> 
                </tr>
            </thead>
             <tbody>
                    <c:forEach var="galist" items="${gaReportlist}" varStatus="theCount">
                        
                        <tr>
                            <td><c:out value="${(theCount.index+1)}"/></td> 
                           <td><c:out value="${galist.deptName}"/></td>
                            <td><c:out value="${galist.distName}"/></td>
                            <td><c:out value="${galist.ss_gp_1900}"/></td>
                            <td><c:out value="${galist.mp_gp_1900}"/></td>
                            <td><c:out value="${galist.ss_gp_2400}"/></td>
                            <td><c:out value="${galist.mp_gp_2400}"/></td>
                            
                            <td><c:out value="${galist.ss_gp_4200}"/></td>
                            <td><c:out value="${galist.mp_gp_4200}"/></td>
                            <td><c:out value="${galist.ss_gp_4600}"/></td>
                            <td><c:out value="${galist.mp_gp_4600}"/></td>
                          
                        </tr>
                    </c:forEach> 
             </tbody>            
            </table>

        </table>


    </body>
</html>
