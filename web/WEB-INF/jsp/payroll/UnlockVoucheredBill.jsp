<%-- 
    Document   : UnlockVoucheredBill
    Created on : Feb 23, 2022, 11:54:00 AM
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

            });
            function unlockbillalert(billnumber)
            {
                if (confirm("Are You sure to unlock 40% Vouchered Bill ?"))
                {
                    self.location = "unlockVoucheredBillData.htm?billnumber=" + billnumber;

                } else {
                    return false;
                }

            }
            function chkVoucher(vchNo)
            {
                if (vchNo == "") {
                    alert("Voucher No. cann't be Blank");
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


                    <div class="row">
                        <div class="col-lg-12">                            
                            <ol class="breadcrumb">
                                <li>
                                    <i class="fa fa-dashboard"></i>  <a href="index.html">Dashboard</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> Unlock Vouchered Bill 
                                </li>                                
                            </ol>
                        </div>
                    </div>              
                    <div style="text-align:center;">
                        <form:form action="unlockFortyPercentVoucheredBill.htm" commandName="billDetail" method="POST">
                            <form:hidden path="usertype" id="hidUsertype"/>
                            <form:hidden path="hidbillNo" id="hidbillNo"/>

                            <table border="0" width="60%"  cellspacing="0" style="font-size:12px; font-family:verdana;">
                                <tr style="height: 30px; "> 
                                    <td style="color:black;font-size: 20px;">
                                        <form:label path="billnumber">Enter 40% Bill Id</form:label>
                                        </td>
                                        <td colspan="3" align="center" style="color:red;font-size: 20px;">
                                        <form:input path="billnumber" class="form-control"/>
                                    </td>                                    
                                    <td colspan="4" align="center"  class="btn btn-primary">
                                        <input type="submit" class="form-control" value="GetData" style="color:black;" />                                        
                                    </td>

                                </tr>                
                            </table>
                        </div>
                        <div class="row">
                            <div class="col-lg-12">
                                <h4>Bill Details</h4>
                                <div class="table-responsive">
                                    <table class="table table-bordered table-hover table-striped">
                                        <thead>
                                            <tr style="text-align:center;">                                                
                                                <th width="10%">Bill Id</th>
                                                <th width="10%">Bill No</th>
                                                <th width="10%">Bill Date</th>
                                                <th width="10%">Bill Status</th>
                                                <th width="10%">Bill Group Name</th>
                                                <th width="10%">Month/Year</th>
                                                <th width="20%">Token No/Date</th>                                                
                                                <th width="22%">Voucher No/Voucher Date</th>
                                                <th width="20%">Office</th>
                                                <th width="10%">Unlock Bill</th>                                             
                                                <th width="10%">Bill Type</th>
                                                <th width="15%">Update Vouchered Status</th>                                                   

                                            </tr>
                                        </thead>
                                        <c:if test="${not empty data}">
                                            <c:forEach var="list" items="${data}">
                                                <tbody>
                                                    <tr>

                                                        <td class="form-group">
                                                            <div class="form-group">
                                                                <c:out value="${list.billnumber}"/>
                                                            </div>
                                                        </td> 
                                                        <td class="form-group">
                                                            <div class="form-group">
                                                                <a href="editunlockBill.htm?billNo=${list.billnumber}" > <c:out value="${list.billdesc}"/></a>
                                                            </div>
                                                        </td>
                                                        <td class="form-group">
                                                            <c:out value="${list.billDate}"/>
                                                        </td>
                                                        <td class="form-group">
                                                            <c:out value="${list.billStatus}"/>
                                                        </td>
                                                        <td class="form-group">
                                                            <c:out value="${list.billgrpname}"/>
                                                        </td>
                                                        <td class="form-group">                                         
                                                            <c:out value="${list.aq_month}"/> /
                                                            <c:out value="${list.aq_year}"/>
                                                        </td>
                                                        <td class="form-group">
                                                            <c:out value="${list.tokenNumber}"/> /
                                                            <c:out value="${list.tokendate}"/>
                                                        </td>                                                       
                                                        <td class="form-group">
                                                            <c:out value="${list.vchNo}"/> /
                                                            <c:out value="${list.vchDt}"/>
                                                        </td>
                                                        <td class="form-group">
                                                            <c:out value="${list.offcode}"/>
                                                        </td>
                                                        <td class="form-group"> 
                                                            <c:if test="${usrtype=='A'&& usrtname eq 'hrmsupport7'}">

                                                            <c:if test="${list.billStatusId ==7 }">
                                                                <a href="javascript:void(0);" onclick="return unlockbillalert('<c:out value="${list.billnumber}"/>');">Unlock Vouchered Bill</a>
                                                            </c:if>
                                                            </c:if>
                                                        </td>                                           

                                                        <td class="form-group"> 
                                                            <c:out value="${list.billType}"/>                                                                                     
                                                        </td>

                                                        <td class="form-group">
                                                            <c:if test="${list.billStatusId ==0 || list.billStatusId ==1 || list.billStatusId ==2}">
                                                                <a href="updateVoucherBillStatus.htm?billnumber=<c:out value="${list.billnumber}"/>" onclick="return chkVoucher('<c:out value="${list.vchNo}"/>')">Update Bill Status AS Vouchered</a>
                                                            </c:if>

                                                        </td>
                                                    </tr>
                                                </tbody>
                                            </c:forEach>                                                                             
                                        </c:if>
                                    </table>
                                </div>

                            </div>
                        </form:form>
                    </div>
                </div>                  
            </div>
        </div>
    </body>
</html>
