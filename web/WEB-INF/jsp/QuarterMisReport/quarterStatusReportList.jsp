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
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <!-- Custom CSS -->
        <link href="css/sb-admin.css" rel="stylesheet">

        <!-- Bootstrap Core JavaScript -->
        <script src="js/bootstrap.min.js"></script>


        <script type="text/javascript">
            $(document).ready(function() {
                if ($("#quarterunitarea").val() != "") {
                    showQuarterType();

                }
            });

            function showQuarterType() {
                var qrtrunit = $("#qrtrunit").val();
                $('#qrtrtype').empty();
                $.post("unitWiseQuarterTypeDataJson.htm", {quarterunitarea: qrtrunit})
                        .done(function(data) {
                            var unitAreaList = data.unitAreaList;

                            $.each(unitAreaList, function(i, obj) {
                                $('#qrtrtype').append($('<option>').text(obj.label).attr('value', obj.value));
                            });
                            $("#qrtrtype").val($("#tqrtrtype").val());
                        })
            }
            function validate() {
                if ($("#quarterunitarea").val() == "") {
                    alert("Choose Unit/Area");
                    return false;
                }
            }
            function EmployeeList() {
                window.location = "getTransferList.htm";
            }
        </script>
    </head>
    <body>
          
        <div id="wrapper">

            <div id="page-wrapper">
                <div class="container-fluid">
                    <div align="center" style="margin-top:5px;margin-bottom:7px;">
                        <form:form action="quarterMisReport.htm" method="POST" commandName="empQuarterBean">


                            <div class="table-responsive">
                                <div class="table-responsive">
                                    <table class="table table-bordered table-hover table-striped">
                                        <thead>
                                            <tr>
                                                <th>#</th>
                                                <th>Consumer No</th>
                                                <th>HRMS Id</th>                                            
                                                <th>Employee Name</th>
                                                <th>QRS NO</th>
                                                <th>QRS Type</th>
                                                <th>Unit/Area</th>                                                                                        
                                                <th>Action</th>
                                            </tr>
                                        </thead>
                                        <tbody id="wrrgrid">
                                            <c:forEach items="${quarterdetail}" var="empquarter" varStatus="cnt">

                                                <tr >
                                                    <td>${cnt.index+1}</td> 
                                                    <td>${empquarter.consumerNo}</td>
                                                    <td>${empquarter.empId}</td>                                                
                                                    <td>${empquarter.empName}</td>
                                                    <td>${empquarter.quarterNo}</td>                                                                                                
                                                    <td>${empquarter.qrtrtype}</td>
                                                    <td>${empquarter.qrtrunit}</td>                                                                                                                                               
                                                    <th>

                                                        <a href="">View | </a>  
                                                        <a href="">Occupation Qrts | </a> 
                                                        <a href="">Vacation Qrts</a> 


                                                    </th>
                                                </tr>
                                            </c:forEach>                                        
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </body>
</html>
