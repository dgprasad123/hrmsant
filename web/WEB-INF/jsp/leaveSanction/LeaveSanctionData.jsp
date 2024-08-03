<%-- 
    Leave Sanction Data
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
            <form:form class="form-inline" action="saveLeaveSanctionControllerData.htm" method="POST" commandName="leave">
                <html:hidden property="ordtype" styleId="ordtype" />	
                <html:hidden property="leaveId" />
                <html:hidden property="empId" />
                <html:hidden property="notid" />


                <div class="panel panel-default">

                    <div class="panel-body">
                        <div class="row">
                            <div class="col-lg-12">
                                <table class="table table-bordered">
                                    <tr>
                                        <td width="25%" align="left">
                                            Order No
                                        </td>
                                        <td width="25%" >
                                            <form:input path="txtOrdNo" styleId="txtOrdNo" styleClass="textresizelevel" style="width:200px;text-align: left;" maxlength="51" onkeyup="maxlengthcheck('txtOrdNo', 50)"/>
                                        </td>

                                        <td width="25%" align="left"> Order Date </td>
                                        <td width="25%" align="left">
                                            <form:input path="txtOrdDate" styleId="txtOrdDate" styleClass="textresizelevel" style="width:60%;text-align: left;"  maxlength="11" onblur="dateValidation(this)" />
                                        </td>                                
                                    </tr>
                                    <tr>
                                        <td  align="left" colspan="4">Details Of Sanctioning Authority</td>

                                    </tr>

                                </table>
                            </div>
                        </div>


                    </div>                
                    <div class="panel-footer">
                        <input type="submit" name="action" value="Save" class="btn btn-default" onclick="return validateForm()"/>

                        <input type="submit" name="action" value="Back" class="btn btn-default"/>

                    </div>
                </div>
            </form:form>
        </div>
    </body>
</html>
