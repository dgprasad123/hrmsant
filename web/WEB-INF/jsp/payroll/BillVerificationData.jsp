<%-- 
    Document   : BillBrowserData
    Created on : Oct 30, 2017, 11:42:46 PM
    Author     : Manas

--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script src="js/bootstrap-datetimepicker.js" type="text/javascript"></script>
        <script type="text/javascript">
            function validateForm() {

                if ($("#billdesc").val() == '') {
                    alert('Please enter Bill number.');
                    $("#billdesc").focus();
                    return false;
                }
                if ($("#billDate").val() == '') {
                    alert('Please enter Bill Date.');
                    $("#billDate").focus();
                    return false;
                } else {
                    var obj = $("#billDate").val();
                    ret = isDate($("#billDate").val(), 'Incorrect date');
                    if (ret == false) {
                        $("#billDate").focus();
                        return false;
                    }
                }

                if ($("#treasury").val() == '') {
                    alert('Please Select Treasury .');
                    $("#treasury").focus();
                    return false;
                }

            }


            function isDate(date, msg) {
                // dd-mmm-yyyy format

                if (date.length == 0) {
                    return true; // Ignore null value
                }
                if (date.length == 10) {
                    date = "0" + date; // Add a leading zero
                }
                if (date.length != 11) {
                    alert(msg);
                    return false;
                }
                day = date.substring(0, 2);
                month = date.substring(3, 6).toUpperCase();
                year = date.substring(7, 11);
                if (isNaN(day) || (day < 0) || isNaN(year) || (year < 1)) {
                    alert(msg);
                    return false;
                }

                // Ensure valid month and set maximum days for that month...
                if ((month == "JAN") || (month == "jan") || (month == "MAR") || (month == "mar") || (month == "MAY") || (month == "may") ||
                        (month == "JUL") || (month == "jul") || (month == "AUG") || (month == "aug") || (month == "OCT") || (month == "oct") ||
                        (month == "DEC")) {
                    monthdays = 31
                }
                else if ((month == "APR") || (month == "apr") || (month == "JUN") || (month == "jun") || (month == "sep") || (month == "SEP") ||
                        (month == "NOV") || (month == "nov")) {
                    monthdays = 30
                }
                else if ((month == "FEB") || (month == "feb")) {
                    monthdays = ((year % 4) == 0) ? 29 : 28;
                }
                else {
                    alert(msg);
                    return false;
                }
                if (day > monthdays) {
                    alert(msg);
                    return false;
                }
                return true;
            }


        </script>
    </head>
    <body>
        <div class="container-fluid">
            <form:form class="form-inline" action="saveBill.htm" method="POST" commandName="command">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        ${aerMessage}
                        Chart of Account: ${command.chartofAcct}    
                    </div>
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-lg-12">
                                <table class="table table-bordered">
                                    <tr>
                                        <td width="15%" align="left">&nbsp;&nbsp;Bill ID
                                            <form:hidden path="billNo"/>
                                            <form:hidden path="txtbilltype"/>
                                            <form:hidden path="sltYear"/>
                                            <form:hidden path="sltMonth"/>
                                        </td>
                                        <td width="15%" >
                                            ${command.billNo}                      
                                        </td>

                                        <td width="15%" align="left">Treasury</td>
                                        <td width="45%" align="left">
                                            <form:select path="treasury" id="treasury" class="form-control" style="width:80%">
                                        <option value=""> Select Treasury </option>
                                        <form:options items="${treasuryList}" itemValue="treasuryCode" itemLabel="treasuryName"/>
                                    </form:select>
                                    </td>                                
                                    </tr>
                                    <tr>
                                        <td  align="left">&nbsp;&nbsp;Bill No</td>
                                        <td>
                                            <form:input path="billdesc" id="billdesc" class="form-control"/>
                                        </td>
                                        <td align="left">DDO Name and Designation</td>
                                        <td align="left"></td>
                                    </tr>
                                    <tr>
                                        <td align="left">&nbsp;&nbsp;Bill Date </td>
                                        <td><form:input path="billDate" id="billDate" class="form-control"/> </td>
                                        <td align="left">&nbsp;&nbsp;Bill Type</td>
                                        <td>${billDtls.billType}</td>
                                    </tr>
                                    <tr>
                                        <td  align="left">&nbsp;&nbsp;Description</td>
                                        <td colspan="3">${command.billdesc}</td>

                                    </tr>
                                    <tr>
                                        <td align="left">AG Bill Type</td>
                                        <td>
                                            <c:out value="${billDtls.agbillTypeId}"/>
                                        </td>
                                        <td align="left">Bill Type</td>
                                        <td>
                                            <c:out value="${billDtls.typeofBillString}"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td align="left">From Date</td>
                                        <td>
                                            <c:out value="${billDtls.salFromdate}"/>
                                        </td>
                                        <td align="left">To Date</td>
                                        <td>
                                            <c:out value="${billDtls.salTodate}"/>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td align="left">&nbsp;&nbsp;Co Code</td>
                                        <td>${billDtls.coCode}</td>
                                        <td align="left">Co Name</td>
                                        <td align="left">${billDtls.coName}</td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-lg-4">
                                <div align="center"><b>** Bill Gross:  ${billDtls.grossAmount}</b></div>

                            </div>
                            <div class="col-lg-4">                                
                                <div align="center"><b>** Net Amount:  ${billDtls.netAmount}</b></div>
                            </div>
                            <div class="col-lg-4">
                                <div align="center" > </div>

                            </div>
                        </div>    

                        <div class="row">
                            <div class="col-lg-12 text-danger"> ** Note: Bill Gross and Net will be found correct when you press <b>Save Button</b>. (After correction of any amount you have to click <b>Save Button</b> again) </div>  
                        </div>                 

                        <div class="row">

                            <c:forEach items="${errorList}" var="error">
                                <div style="color: red"> ${error} </div>
                            </c:forEach>

                        </div>

                    </div>     

                    <div class="pull-left">

                        <c:if test="${errorList.size() eq 0}">
                            <input type="submit" name="action" value="Upload" class="btn btn-default"/>
                        </c:if>
                        <c:if test="${errorList.size() gt 0}">
                            <div style="color: red"> Bill is Unlocked. Now you can Change Bill. </div>
                        </c:if>    

                        <input type="submit" name="action" value="Cancel" class="btn btn-default"/>
                    </div>

                    <div class="pull-right"> 
                        <input type="submit" name="action" value="Download" class="btn btn-default"/>

                    </div>
                </div>

            </form:form>
        </div>
    </body>
</html>
