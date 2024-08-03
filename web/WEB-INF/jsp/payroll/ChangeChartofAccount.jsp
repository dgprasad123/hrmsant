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

            function onlyIntegerRange(e) {
                var browser = navigator.appName;
                if (browser == "Netscape") {
                    var keycode = e.which;
                    if ((keycode >= 48 && keycode <= 57) || keycode == 8 || keycode == 0)
                        return true;
                    else
                        return false;
                } else {
                    if ((e.keyCode >= 48 && e.keyCode <= 57) || e.keycode == 8 || e.keycode == 0)
                        e.returnValue = true;
                    else
                        e.returnValue = false;
                }
            }

        </script>
    </head>
    <body>
        <div class="container-fluid">
            <form:form class="form-inline" action="saveBill.htm" method="POST" commandName="command">
                <div class="panel panel-default">
                    <div class="panel-heading">
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
                                        <td width="15%">
                                            ${command.billNo}                      
                                        </td>
                                        <td width="15%" align="left">&nbsp;</td>
                                        <td width="45%" align="left">
                                            &nbsp;
                                        </td>                                
                                    </tr>
                                    <tr>
                                        <td  align="left">Enter Demand Number</td>
                                        <td>
                                            <form:input path="txtDemandno" id="txtDemandno" class="form-control" maxlength="2" onkeypress="return onlyIntegerRange(event)"/>
                                        </td>
                                        <td align="left">Enter Major Head</td>
                                        <td align="left"> <form:input path="txtmajcode" id="txtmajcode" class="form-control" maxlength="4" onkeypress="return onlyIntegerRange(event)"/> </td>
                                    </tr>
                                    <tr>
                                        <td align="left"> Enter Sub Major Head</td>
                                        <td>
                                            <form:input path="submajcode" id="submajcode" class="form-control" maxlength="2" onkeypress="return onlyIntegerRange(event)"/> 
                                        </td>
                                        <td align="left">
                                            Enter Minor Head
                                        </td>
                                        <td align="left">
                                            <form:input path="txtmincode" id="txtmincode" class="form-control" maxlength="3" onkeypress="return onlyIntegerRange(event)"/> 
                                        </td>
                                    </tr>
                                    <tr>
                                        <td align="left">Enter Sub Head</td>
                                        <td>
                                            <form:input path="submincode1" id="submincode1" class="form-control" maxlength="4" onkeypress="return onlyIntegerRange(event)"/> 
                                        </td>                       
                                        <td  align="left"> Enter Detail Head</td>
                                        <td  align="left">
                                            <form:input path="submincode2" id="submincode2" class="form-control" maxlength="5" onkeypress="return onlyIntegerRange(event)"/> 
                                        </td>
                                    </tr>
                                    <tr>
                                        <td  align="left" colspan="2"> Enter Charged for 2 and Voted for 1  </td>
                                        <td align="left"  colspan="2">
                                            <form:input path="submincode3" id="submincode3" class="form-control" maxlength="1" onkeypress="return onlyIntegerRange(event)"/> 
                                        </td>
                                    </tr>
                                    <tr>
                                        <td align="left">Enter Sector </td>
                                        <td>
                                            <form:input path="sectorCode" id="sectorCode" class="form-control" maxlength="1" onkeypress="return onlyIntegerRange(event)"/> 
                                        </td>                       
                                        <td align="left"> Enter Plan </td>
                                        <td align="left">
                                            <form:input path="planCode" id="planCode" class="form-control" maxlength="2" onkeypress="return onlyIntegerRange(event)"/> 
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                    </div>                
                    <div class="panel-footer">
                        <input type="submit" name="action" value="Cancel" class="btn btn-default"/>
                        <input type="submit" name="action" value="Update" class="btn btn-default"/>
                    </div>
                </div>
            </form:form>
        </div>
    </body>
</html>
