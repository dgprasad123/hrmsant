<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        <!-- Custom CSS -->

        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>          


        <link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap4.min.css"/>
        <script type="text/javascript"  src="js/jquery-1.12.4.js"></script>
        <script type="text/javascript"  src="js/jquery.dataTables.min.js"></script>
        <script type="text/javascript"  src="js/dataTables.bootstrap4.min.js"></script>

        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript" src="js/chosen.jquery.min.js"></script>
        <script type='text/javascript'>
            window.print();
        </script>    



    </head>
    <body>
        <h2 align='center'>${vstatus} Quarter List</h2>
        <table class="table table-bordered table-hover table-striped" id='id_tbl'  style="font-size: 14px;margin-right:50px;margin-left:50px;width:95%">
            <thead>
                <tr>
                    <th width="2%">#</th>
                    <th width="5%">DOE</th>                                                    
                    <th width="5%">HRMS Id</th>                                            
                    <th width="15%">Employee Name</th>
                    <th width="10%">Mobile</th>
                    <th width="35%">Post</th>
                    <th width="5%">Unit/Area</th>
                    <th width="5%">QRS Type</th>
                    <th width="5%">QRS NO</th>                  
                    <th width="10%">${vstatus} Date</th>
                </tr>
            </thead>
            <tbody id="wrrgrid">
                <c:forEach items="${quarterList}" var="quarter" varStatus="cnt">

                    <tr  >
                        <td style='border:1px solid #CCC'>${cnt.index+1}</td> 
                        <td style='border:1px solid #CCC'>${quarter.orderDate}</td>
                        <td style='border:1px solid #CCC'>${quarter.empId}</td>                                                
                        <td style='border:1px solid #CCC'>${quarter.empName}</td>
                        <td style='border:1px solid #CCC'>${quarter.mobileno}</td>
                        <td style='border:1px solid #CCC'>${quarter.designation}</td>
                        <td style='border:1px solid #CCC'>${quarter.qrtrunit}</td>                                                                                                
                        <td style='border:1px solid #CCC'>${quarter.qrtrtype}</td>
                        <td style='border:1px solid #CCC'>${quarter.quarterNo}</td>                       
                        <td style='border:1px solid #CCC'>${quarter.dos}</td>
                       
                    </tr>
                </c:forEach>                                        
            </tbody>
        </table>
    </body>
</html>
