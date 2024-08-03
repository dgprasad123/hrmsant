<%-- 
    Document   : PARRequestNRC
    Created on : Feb 4, 2020, 5:17:31 PM
    Author     : manisha
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"  autoFlush="true" buffer="64kb"%>
<!DOCTYPE html>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<html>
    <head>
        <title>::Performance Appraisal::</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script> 
        <script src="js/bootstrap.min.js"></script>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">                                      
    </head>
    <body>

        <form:form action="downloadNRCPdf.htm" commandName="parApplyForm" method="POST" class="form-inline">
            <div align="center" style="margin-top:5px;margin-bottom:10px;">
                <div align="center">
                    <table border="0" width="99%" cellpadding="0"  cellspacing="0" style="font-size:12px; font-family:verdana;">
                        <tr>
                            <td style="background-color:#5095ce;color:#FFFFFF;padding:0px;font-weight:bold;" align="center"><h2>Requested For NRC</h2></td>
                        </tr>                        
                    </table>
                </div>
            </div>

            <div   style="width:100%;margin-top:1px;border:1px solid #5095ce;margin-top:5px;">                                                        

                <table border="0"  cellpadding="5" cellspacing="0" width="100%" class="tableview">
                    <tr style="height: 40px">                               
                        <td align="center" width="10%"> 1. </td>
                        <td  width="20%">Financial Year:</td>
                        <td width="70%">
                            <span>
                                ${parApplyForm.fiscalYear}
                            </span>
                        </td> 
                    </tr>

                    <tr style="height: 40px">                               
                        <td align="center" width="10%"> 2. </td>
                        <td  width="20%">From Date:</td>
                        <td width="70%">
                            <span>
                                ${parApplyForm.prdFrmDate}
                            </span>
                        </td>
                    </tr>
                    <tr style="height: 40px">                               
                        <td align="center" width="10%"> 3. </td>
                        <td  width="20%">To Date:</td>
                        <td width="70%"> <span> ${parApplyForm.prdToDate}</span></td>                                         
                    </tr>
                    <tr style="height: 40px">                               
                        <td align="center" width="10%"> 4. </td>
                        <td  width="20%">HRMS ID</td>
                        <td width="70%"> <span>${parApplyForm.empId}</span></td>                                         
                    </tr>
                    <tr style="height: 40px">                               
                        <td align="center" width="10%"> 5. </td>
                        <td  width="20%">Full name of the officer</td>
                        <td width="70%"> <span>${parApplyForm.empName}</span></td>                                         
                    </tr>
                    <tr style="height: 40px">                               
                        <td align="center" width="10%"> 6. </td>
                        <td  width="20%">Date of birth</td>
                        <td width="70%"> <span>${parApplyForm.dob}</span></td>                                         
                    </tr>
                    <tr style="height: 40px">                               
                        <td align="center" width="10%"> 7. </td>
                        <td  width="20%">Service to which the officer belongs</td>
                        <td width="70%"> <span>${parApplyForm.cadrecode}</span></td>                                         
                    </tr>
                    <tr style="height: 40px">                               
                        <td align="center" width="10%"> 8. </td>
                        <td  width="20%">Group to which the officer belongs</td>
                        <td width="70%"> <span>${parApplyForm.empGroup}</span></td>                                         
                    </tr>
                    <tr style="height: 40px">                               
                        <td align="center" width="10%"> 9. </td>
                        <td  width="20%">Designation during the period of report</td>
                        <td width="70%"> <span>${parApplyForm.empDesg}</span></td>                                         
                    </tr>
                    <tr style="height: 40px">                               
                        <td align="center" width="10%"> 10. </td>
                        <td  width="20%">Office to which posted</td>
                        <td width="70%"> <span>${parApplyForm.empOffice}</span></td>                                         
                    </tr>
                    <tr style="height: 40px">                               
                        <td align="center" width="10%"> 11. </td>
                        <td  width="20%">Head Quarter(if any)</td>
                        <td width="70%"> <span>${parApplyForm.sltHeadQuarter}</span></td>                                         
                    </tr>
                    <tr style="height: 40px">                               
                        <td align="center" width="10%"> 12. </td>
                        <td  width="20%">Reason for NRC</td>
                        <td width="70%"> <span>${parApplyForm.nrcreason}</span></td>                                         
                    </tr>

                </table>

                <div class="panel panel-default">                            
                    <label class="control-label col-sm-2" ><b>Remarks</b></label>
                    <div class="panel-body">
                        ${parApplyForm.nrcremarks}
                    </div>

                </div>
                <div class="panel panel-default">                            
                    <label class="control-label col-sm-2" ><b>Date Of Submission</b></label>
                    <div class="panel-body">
                        ${parApplyForm.submitedonNRC}
                    </div>

                </div>
            </div>
            <div align="center" style="margin-top: 5px;">
                <c:if test="${LoginUserBean.loginusertype eq 'G' or LoginUserBean.loginusertype eq 'B'}">
                    <form:hidden path="parId"/>
                    <form:hidden path="empName"/>
                    <form:hidden path="prdFrmDate"/>
                    <form:hidden path="prdToDate"/>
                    <form:hidden path="hasadminPriv"/>                                
                    <c:if test="${LoginUserBean.loginusertype eq 'G' && users.hasparreviewingAuthorization == 'Y'}"> 
                        <input type="submit" class="btn btn-primary" name="action" value="Accepted"/>
                    </c:if>
                    <input type="submit" class="btn btn-primary" name="action" value="Download"/>
                </c:if>
            </div>

        </form:form>
    </body>
</html>
