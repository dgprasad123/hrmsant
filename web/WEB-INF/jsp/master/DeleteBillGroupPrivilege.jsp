<%-- 
    Document   : DeleteBillGroupPrivilege
    Created on : Apr 29, 2023, 12:35:22 PM
    Author     : Madhusmita
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script src="js/moment.js" type="text/javascript"></script>
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <title>Unlock Bill</title>        
        <script type="text/javascript">
            $(window).load(function() {
                // Fill modal with content from link href
                $("#billEditModal").on("show.bs.modal", function(e) {
                    var link = $(e.relatedTarget);
                    $(this).find(".modal-body").load(link.attr("href"));
                });
            });
            $(document).ready(function() {
                $('#tokendate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#prevTokendate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#billDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            });




        </script>

    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">     

                <div class="container-fluid">


                    <div class="row">
                        <div class="col-lg-12">                            
                            <ol class="breadcrumb">
                                <li>
                                    <i class="fa fa-dashboard"></i>  <a href="index.html">Dashboard</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> BILL GROUP PRIVILEGE
                                </li>                                
                            </ol>
                        </div>
                    </div>
                    <div style="text-align:center;">
                        <form:form action="deleteBillGroupPrivilege.htm" commandName="OfcForm" method="POST">
                            <form:hidden path="hidOfficeCode"/>
                            <table border="0" width="60%" cellspacing="0" style="font-size:12px; font-family:verdana;">
                                <h4 style="color:red;">${msg}</h4>

                                <tr style="height: 30px">
                                    <td style="text-align:center; font-size: 20px">
                                        <form:label path="offCode">Enter 13 Digit Office Code</form:label>

                                        </td>
                                        <td>
                                        <form:input path="offCode" class="form-control" maxlength="13"/>
                                    </td>
                                    <td width="10%"></td>
                                    <td style="text-align:center;">
                                        <input type="submit" class="btn btn-success" value="Search" />                                        
                                    </td>
                                </tr>                
                            </table>
                        </div>
                        <div class="row">
                            <div class="col-lg-12">
                                <h4>Bill Group Privilege List</h4>
                                <div class="table-responsive">
                                    <table class="table table-bordered table-hover table-striped">
                                        <thead>
                                            <tr>
                                                <th width="5%">Sl No</th>
                                                <th width="15%">Substantive Post Name</th>
                                            </tr>
                                        </thead> 
                                        <tbody>
                                            <c:forEach items="${billgrpspcList}" var="bgspcList" varStatus="cnt">
                                                <tr>
                                                    <td width="5">${cnt.index + 1}</td>
                                                    <td width="15">${bgspcList.billGrpSpn}</td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                        <div class="panel-footer">
                            <a href="deleteBillGroup.htm?officeCode=${officecode}"><input type="button" value="Delete Bill Group" name="action" class="btn btn-primary" onclick="return confirm('Are you sure to Delete?')" /></a>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </body>
</html>