<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <link href="css/sb-admin.css" rel="stylesheet">
        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                $('.openPopup').on('click', function() {

                    var dataURL = $(this).attr('data-href');

                    $('#modalbody').load(dataURL, function() {
                        $('#processUserModal').modal({show: true});
                    });
                });
            });

            function validateSearch() {
                if ($('#offCode').val() == "") {
                    alert("Please enter Office Code");
                    return false;
                } else if ($('#offCode').val() != "") {
                    if ($('#offCode').val().length < 13 || $('#offCode').val().length > 13) {
                        alert("Office Code must be 13 digits");
                        return false;
                    }
                }

                if ($('#financialYear').val() == "") {
                    alert("Please select Financial Year");
                    return false;
                }
            }
        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>
            <div id="page-wrapper">
                <div class="container-fluid">

                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <div class="row" style="margin-top:30px;margin-bottom: 20px;">
                                <div class="col-lg-12">
                                    <span style="color: #F00000;font-size: 20px;display:block;text-align: center;">
                                        VIEW AER FLOW STATUS
                                    </span>
                                </div>
                            </div>
                        </div>
                        <div class="panel-body">
                            <form:form action="AERProcessUserListDC.htm" method="POST" commandName="command">
                                <div class="row" style="margin-top:30px;margin-bottom: 20px;">
                                    <div class="col-lg-2">
                                        ENTER OFFICE CODE
                                    </div>
                                    <div class="col-lg-3">
                                        <form:input path="offCode" id="offCode" class="form-control"/>
                                    </div>
                                    <div class="col-lg-3">
                                        SELECT FINANCIAL YEAR
                                    </div>
                                    <div class="col-lg-3">
                                        <form:select path="financialYear" id="financialYear" class="form-control">
                                            <form:option value="" label="Select" cssStyle="width:30%"/>
                                            <form:options items="${fiscyear}" itemLabel="fy" itemValue="fy"/>
                                        </form:select>
                                    </div>
                                    <div class="col-lg-1">
                                        <input type="submit" name="btnSubmit" value="Search" class="btn btn-info" onclick="return validateSearch();"/>
                                    </div>
                                </div>
                            </form:form>
                            <table class="table table-bordered">
                                <thead>
                                    <tr>
                                        <th width="15%">SL No</th>
                                        <th width="20%">AER Status</th>
                                        <th width="20%">View Flow</th>
                                    </tr>
                                </thead>
                                <c:if test="${not empty aerliststatus}">
                                    <c:forEach items="${aerliststatus}" var="list" varStatus="cnt">
                                        <tr>
                                            <td>
                                                ${cnt.index + 1}
                                            </td>
                                            <td><c:out value="${list.status}"/></td>
                                            <td>
                                                <a href="javascript:void(0);" data-remote="false" data-target="#processUserModal" data-href="viewAERProcessUserNameList.htm?aerId=${list.aerId}" class="openPopup">
                                                    View
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:if>
                            </table>
                        </div>                            
                    </div>

                    <div id="processUserModal" class="modal fade" role="dialog">
                        <div class="modal-dialog modal-lg">
                            <!-- Modal content-->
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                    <h4 class="modal-title">User List</h4>
                                </div>
                                <div class="modal-body" id="modalbody"></div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
