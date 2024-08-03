<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link rel="stylesheet" href="css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="font-awesome/css/font-awesome.min.css">        
        <link rel="stylesheet" href="css/sb-admin.css">

        <script type="text/javascript" src="js/jquery.min.js"></script>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            function validateSearch() {
                if ($('#txtGPFNo').val() == "") {
                    alert("Please enter GPF No");
                    $('#txtGPFNo').focus();
                    return false;
                }
                return true;
            }
        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/agOdishaMenu.jsp"/>
            <div id="page-wrapper">
                <form:form action="SearchGPFNo.htm" commandName="agPensionForm">
                    <div class="container-fluid" style="margin-bottom:40px; margin-top: 30px;">
                        <div class="panel panel-default">
                            <div class="panel-heading" >
                                <div class="row">
                                    <div class="col-lg-4">
                                        <label>Enter GPF No</label>
                                    </div>
                                    <div class="col-lg-4">
                                        <form:input path="txtGPFNo" id="txtGPFNo" class="form-control"/>
                                    </div>
                                    <div class="col-lg-1">
                                        <input type="submit" name="btnSubmit" value="Search" class="form-control" onclick="return validateSearch();"/>
                                    </div>
                                    <div class="col-lg-3"></div>
                                </div>
                            </div>
                        </div>
                        <div class="panel-body row">
                            <table class="table table-hover" style="border:1px solid">
                                <thead>
                                    <tr>
                                        <th>HRMS ID</th>
                                        <th>Name</th>
                                        <th>Office</th>
                                        <th>Designation</th>
                                        <th colspan="2">Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:if test="${not empty empdata}">
                                        <c:if test="${empdata.gpfAssumed eq 'Y'}">
                                            <tr>
                                                <td colspan="5">
                                                    <span style="display: block;text-align:center;font-size: 15px; font-weight: bold; color: #F00000;">
                                                        DUMMY ACCOUNT NO. YOU CANNOT VIEW SERVICE BOOK.
                                                    </span>
                                                </td>
                                            </tr>
                                        </c:if>
                                        <c:if test="${empdata.gpfAssumed ne 'Y'}">
                                            <tr>
                                                <td> ${empdata.empid} </td>
                                                <td> ${empdata.empname} </td>
                                                <td> ${empdata.offname} </td>
                                                <td> ${empdata.designation} </td>
                                                <td> 
                                                    <a href="ServicebookPDFforAG.htm" target="_blank">Service Book</a> |
                                                    <a href="eServiceBook.htm" target="_blank">Download Annexure</a> |
                                                    <a href="viewAnnexure.htm?empid=${empdata.empid}" target="_blank">View Annexure</a>
                                                </td>
                                            </tr>
                                        </c:if>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                        <div class="panel-footer">

                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </body>
</html>
