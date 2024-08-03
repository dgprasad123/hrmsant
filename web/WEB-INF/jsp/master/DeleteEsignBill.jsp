<%-- 
    Document   : UnlockBill
    Created on : Mar 26, 2018, 2:37:15 PM
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
                                    <i class="fa fa-file"></i> Esign BIll Details 
                                </li>                                
                            </ol>
                        </div>
                    </div>              
                    <div style="text-align:center;">

                        <form:form action="searchEsignBill.htm" commandName="districtWiseEsignBean" method="POST">
                            <table border="0" width="50%" cellspacing="0" style="font-size:12px; font-family:verdana;">
                                <h4 style="color:red;">Bill Status Submitted to treasury/Token Generated/ Vouchered will not be shown</h4><br/>

                                <tr style="height: 30px;">
                                   <%-- <td style="text-align:center;">
                                        <form:label path="officeCode">OFFICE CODE</form:label>
                                        </td>
                                        <td>
                                        <form:input path="officeCode" class="form-control"/>
                                    </td>--%>
                                    <td style="text-align:center;">
                                        <form:label path="billnumber">BILL ID</form:label>
                                        </td>
                                        <td width="50%">
                                        <form:input path="billnumber" class="form-control"/>
                                    </td>
                                    <td width="5%" ></td>
                                    <td style="text-align:center;">
                                        <input type="submit" class="btn btn-success form-control" value="Ok" />                                        
                                    </td>
                                </tr> 
                            </table>

                        </div>
                        <div class="row">
                            <div class="col-lg-12">
                                <br/><br/>
                                <div class="table-responsive">
                                    <table class="table table-bordered table-hover table-striped">
                                        <thead>
                                            <tr>
                                                <th width="5%">Month</th>
                                                <th width="10%">Year</th>
                                                <th width="15%">Bill Description</th>
                                                <th width="15%">Bill Status</th>
                                                <th width="15%">Created date</th>
                                                <th width="15%">E-sign PDF date</th>
                                                <th width="10%">UnSigned PDF file</th>
                                                <th width="10%">Signed PDF file</th>
                                                <th width="10%">Action</th>


                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${esignBillListItem}" var="esignlist" varStatus="cnt">
                                                <tr>
                                                    <td width="5%">${esignlist.month}</td>
                                                    <td>${esignlist.year}</td>
                                                    <td>${esignlist.billdesc}</td>
                                                    <td>${esignlist.bill_status}</td>
                                                    <td>${esignlist.create_pdf_date}</td>
                                                    <td>${esignlist.esign_pdf_date}</td>
                                                    <td>${esignlist.unsigned_pdf_file}</td>
                                                    <td>${esignlist.signed_pdf_file}</td>
                                                    <td><a href="deleteEsignbill.htm?billId=${esignlist.billno}&esignLogId=${esignlist.id_esign_log}" onclick="return confirm('Are you sure to Delete?')"><span class="glyphicon glyphicon-trash"></span></a></td>
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
