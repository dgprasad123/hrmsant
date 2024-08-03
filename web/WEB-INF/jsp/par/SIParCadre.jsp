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
                
                $('#datatable').DataTable({
                    "paging": true  // Enable pagination
                });
            });
            
            function validateSearch() {
                if($("#sltSearchCriteria").val() === "" )         
                {
                    alert("Please select a Search Criteria");
                    $("#sltSearchCriteria").focus();
                    return false;
                }
                if($("#searchString").val() === "" )         
                {
                    alert("Please Enter a Search String");
                    $("#searchString").focus();
                    return false;
                }
                return true;
            }
        </script>
    </head>
    <body>
        <div id="wrapper"> 
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>
            <form:form id="siParForm" action="SIParCaderPage.htm" method="POST" commandName="SIParCader"> 
                <div id="page-wrapper">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h3 style="text-align:center">CADRE DETAILS FOR SI & EQUIVALENT RANKS (GROUP - B) OFFICERS </h3>
                        </div>

                        <div class="row" style="margin: 10px;"> 
                            <div class="col-lg-4" align="right"><b>Search Criteria</b></div>
                            <div class="col-lg-2">
                                <form:select class="form-control" path="sltSearchCriteria" id="sltSearchCriteria">
                                    <form:option value=""> -- Select --</form:option>
                                    <form:option value="hrmsId"> HRMS ID </form:option>
                                    <form:option value="accountType"> GPF / PRAN </form:option>
                                    <form:option value="mobileNumber"> Mobile No </form:option>
                                </form:select>
                            </div>
                            <div class="col-lg-2">
                                <form:input type="text" path="searchString" id="searchString" class="form-control" autocomplete="off" ></form:input> 
                                </div>
                                <div class="col-lg-1" align="left">
                                    <input type="submit" class="btn btn-success btn-md" id="btn_Search" name="btnSearch" value="Search" onclick="return validateSearch()"/>                              
                                </div>
                                <br> 
                                <br>
                                <div class="panel-body">
                                    <div class="row">
                                        <div class="col-lg-12">                            
                                            <div class="">
                                                <table id="datatable" class="table table-bordered table-hover table-striped dataTable no-footer" width="99%">
                                                    <thead class="tblTrColor">
                                                        <tr>
                                                            <th width="1%"> # </th> 
                                                            <th width="8%"> HRMS ID <br/>GPF / PRAN No </th> 
                                                            <th width="12%"> Employee Name <br/> Mobile </th> 
                                                            <th width="7%"> Date of Birth </th>
                                                            <th width="6%"> Post Group </th>
                                                            <th width="8%"> Current Cadre </th>
                                                            <th width="15%"> Designation </th>
                                                            <th width="20%"> Current Office </th>
                                                            <th width="4%"> Action </th>
                                                        </tr>
                                                    </thead>
                                                    <tbody id="sipardatagrid">
                                                        <c:if test="${not empty EmpDataList}">
                                                            <c:forEach items="${EmpDataList}" var="eachEmp" varStatus="cnt">
                                                                <tr>
                                                                    <th>${cnt.index + 1}</th>
                                                                    <td>${eachEmp.hrmsId},<br/><b style="color:#FF4500;">(${eachEmp.gpfNumber})</b></td>
                                                                    <td>${eachEmp.empName}<br/><b style="color:#FF4500;">${eachEmp.mobileNumber}</b></td>
                                                                    <td>${eachEmp.dob}</td>
                                                                    <td>${eachEmp.postGroupType}</td>
                                                                    <td>${eachEmp.cadreName}</td>
                                                                    <td>${eachEmp.spn}</td>
                                                                    <td>${eachEmp.officeName}</td>
                                                                    <td>
                                                                        <a href="<c:url value="SIParCadreEditPage.htm?hrmsId=${eachEmp.encHrmsId}"/>" class="btn btn-primary">Edit</a>
                                                                    </td>
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