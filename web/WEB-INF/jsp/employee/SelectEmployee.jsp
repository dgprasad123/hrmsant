<%-- 
    Document   : SelectEmployee
    Created on : 7 Sep, 2018, 1:08:58 PM
    Author     : Surendra
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>      
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type='text/javascript'>
            function getSearchEmployeeData() {
                gpfno=$("#gpfnoId").val();
                $.ajax({
                    type: 'GET',
                    url: 'getEmployeeDataasJson.htm?gpfno='+gpfno,
                    async: false,
                    beforeSend: function () {/*loading*/
                    },
                    dataType: 'json',
                    success: function (result) {
                        
                        //var obj = JSON.parse(result.toString());
                        
                        $("#employeeId").val(result[0].empid);
                        $("#td1").html(result[0].empid+'</br>'+result[0].gpfno);
                        $("#td2").html(result[0].empName);
                        $("#td3").html(result[0].curDesg);
                        
                    }
                });
            }
        </script>
    </head>
    <body>

        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <div class="row">
                        <div class="col-lg-12">
                            Select Employee 
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    Search By GPf No <input type="text" id="gpfnoId"/> <input type="button" value="Search" onclick="getSearchEmployeeData()"/>
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th> Select One </th>
                                <th  width="20%">Emp Id/ GPF No</th>
                                <th  width="40%">Employee Name</th>
                                <th  width="40%">Designation</th>

                            </tr>
                        </thead>
                        <tbody>

                            <tr>
                                <td > <input type="radio" name="empId" id="employeeId"/> </td>
                                <td id="td1"> </td>
                                <td id="td2">
                                    
                                </td>
                                <td  id="td3">
                                    
                                </td>

                            </tr>

                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">                    
                    Select Employee        
                </div>
            </div>
        </div>

    </body>
</html>

