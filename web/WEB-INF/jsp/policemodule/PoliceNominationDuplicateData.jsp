<%@page contentType="text/html" pageEncoding="UTF-8" autoFlush="true" buffer="64kb"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">    
        <link href="css/sb-admin.css" rel="stylesheet">

        <script src="js/jquery.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {

            });
            function deleteDuplicateGPFNo(gpfno) {
                if (confirm('Are you sure to Delete Duplicate Data?')) {
                    window.location = "PoliceNominationDuplicateDataDelete.htm?gpfno="+gpfno;
                    return true;
                } else {
                    return false;
                }
            }
            function validateForm() {
                if ($("#gpfno").val() == "") {
                    alert("Please enter GPF No");
                    $("#gpfno").focus();
                    return false;
                }
                return true;
            }
        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>
            <form:form action="PoliceNominationDuplicateDataPage.htm" commandName="EmpDetNom">
                <div id="page-wrapper">
                    <div class="panel panel-default">
                        <h3 style="text-align:center"> CHECK DUPLICATE DATA FOR POLICE NOMINATION</h3>
                        <div class="panel-heading">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">
                                    <label for="gpfno">Enter GPF No</label>
                                </div>
                                <div class="col-lg-5">
                                    <form:input path="gpfno" id="gpfno" class="form-control" maxlength="30"/>
                                </div>
                                <div class="col-lg-3">
                                    <input type="submit" name="submit" value="Search" onclick="return validateForm();"/>
                                </div>
                            </div>
                        </div>

                        <div class ="panel-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-11" style="text-align: center;">
                                    <c:if test="${not empty gpfno && empty empname}">
                                        <strong>
                                            No Duplicate Data.
                                        </strong>
                                    </c:if>
                                    <c:if test="${not empty empname}">
                                        <strong>
                                            <c:out value="${empname}"/><br />
                                            Duplicate Data found. To remove duplicate data <a href="javascript:void(0);" onclick="deleteDuplicateGPFNo('${gpfno}');">Click Here</a>
                                        </strong>
                                    </c:if>
                                    <c:if test="${not empty isDeleted && isDeleted eq 'Y'}">
                                        <span style="font-weight: bold; font-size: 16px; color: #008900;">Successfully Deleted</span>
                                    </c:if>
                                    <c:if test="${not empty isDeleted && isDeleted eq 'N'}">
                                        <span style="font-weight: bold; font-size: 16px; color: #e94e02;">Error Deleting the Data</span>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                        <div class="panel-footer">
                            
                        </div>
                    </div>
                </div>
            </form:form>
        </div>
    </body>
</html>
