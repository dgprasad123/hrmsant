<%-- 
    Document   : PARDistrictwisePrivilegeForPolice
    Created on : 9 Jun, 2022, 12:41:37 PM
    Author     : Manisha
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">    
        <link href="css/sb-admin.css" rel="stylesheet" type="text/css">
        <link href="css/select2.min.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" type="text/css" href="css/jquery.dataTables.css">

        <script src="js/jquery.min.js"></script> 
        <script src="js/jquery2.0.3.min.js"></script>
        <script src="js/jquery-ui.min.js"></script> 
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/jquery.dataTables.js"></script>
        <script src="js/select2.min.js"></script>

        <style>
            .loader {
                border: 16px solid #f3f3f3; /* Light grey */
                border-top: 16px solid #3498db; /* Blue */
                border-radius: 50%;
                width: 40px;
                height: 40px;
                animation: spin 2s linear infinite;
            }
            .tblTrColor{
                background: rgb(174,238,209);
                background: radial-gradient(circle, rgba(174,238,209,0.9976191160057774) 0%, rgba(148,231,233,1) 100%);
                color: #000000;
                font-weight: bold;
            }
            @keyframes spin {
                0% { transform: rotate(0deg); }
                100% { transform: rotate(360deg); }
            }
            .pageHeadingTxt{
                background-color: #DDEDE0;
                font-weight: bold;
                text-transform: uppercase;
                text-align: center;
                color:#000;
                font-size:18px;
                line-height: 20px;
                position: relative;
                letter-spacing: 0.2px;
                padding: 10px 0px;
            }
            .pageSubHeading{
                background-color: #DDEDE0;
                font-weight: bold;
                text-transform: uppercase;
                text-align: center;
                color:#0D8CE7;
                font-size:17px;
                line-height: 20px;
                position: relative;
                letter-spacing: 0.2px;
                padding: 10px 0px;
            }
            .myModalBody{}
            .new_sty{width:33%;}
            .new_sty li{width: 98%;}
            .new_sty li a{width: 100%;}
        </style>


        <script type="text/javascript">
            $(document).ready(function() {
                
                $(this).bind("contextmenu", function (e) {
                    e.preventDefault();
                });
                
                var table = $('#datatable').DataTable();
            });
            
        </script>

    </head>
    <div id="wrapper"> 
        <jsp:include page="../tab/hrmsadminmenu.jsp"/>
        
        <form:form action="SiEqivOfficerList.htm" method="post" commandName="ParAdminSearchCriteria">
            <div id="page-wrapper">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 style="text-align:center"> Custodian wise list of Officers for SI & Equivalent Ranks (Group - B) </h3>
                    </div>

                    <div class="row" style="margin: 5px;"> 
                        <div class="col-lg-5"> &nbsp; </div>
                        <div class="col-lg-2"> 
                            <input type="submit" class="btn btn-success btn-md" id="downloadBtn" name="Download" value="Download Excel"/>
                        </div>
                        <div class="col-lg-5"> &nbsp; </div>
                    </div>

                    <div class="panel-body">
                        <div class="row">
                            <div class="col-lg-12">                            
                                <div class="table-responsive">
                                    <table id="datatable" class="table table-bordered table-hover table-striped" width="99%">
                                        <thead class="tblTrColor">
                                            <tr>
                                                <th width="1%"> # </th> 
                                                <th width="10%"> HRMS Id<br>GPF / PRAN NO </th> 
                                                <th width="15%"> Employee Name<br>Mobile</th>                    
                                                <th width="7%"> Date of Birth </th>
                                                <th width="20%"> Designation </th>
                                                <th width="7%"> Post Group </th>
                                                <th width="10%"> Current cadre </th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:if test="${not empty siEqivOfficerList}">
                                                <c:forEach items="${siEqivOfficerList}" var="eachEmp" varStatus="cnt">
                                                    <tr>
                                                        <td> ${cnt.index + 1} </td>
                                                        <td> ${eachEmp.empId},<br/><b style="color:#FF4500;">(${eachEmp.gpfno})</b> </td>
                                                        <td> <b>${eachEmp.empName}</b>,<br/><b style="color:#FF4500;">(${eachEmp.mobile})</b> </td>
                                                        <td> ${eachEmp.dob} </td>
                                                        <td> ${eachEmp.postName} </td>
                                                        <td> ${eachEmp.groupName} </td>
                                                        <td> ${eachEmp.cadreName} </td>
                                                    </tr>
                                                </c:forEach>
                                            </c:if>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>        
                </div>
            </div>
        </form:form>
    </div>    
</body>
</html>


