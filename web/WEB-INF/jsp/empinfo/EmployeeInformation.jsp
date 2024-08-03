<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                $('#mobile').keypress(function(e) {
                    if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
                        //display error message
                        $("#errmsg").html("Digits Only").show().fadeOut("slow");
                        return false;
                    }
                });
            });
        </script>
        <style type="text/css">
            body{
                font-family:Verdana,sans-serif;
            }
        </style>
    </head>
    <body>
        <form action="GetEmployeeInformation.htm" method="POST" commandName="employeeInformation">
            <div align="center" style="margin-top:5px;margin-bottom:10px;">
                <div class="easyui-panel" align="center">
                    <table width="80%" cellspcaing="0" cellpadding="0" border="0">
                        <tr style="height:60px;">
                            <td width="15%" align="center">
                                HRMS ID
                            </td>
                            <td width="30%">
                                <input name="empid" id="empid" class="easyui-textbox" value="${empinfo.empid}"/>
                            </td>
                            <td width="15%" align="center">
                                GPF No
                            </td>
                            <td width="30%">
                                <input name="gpfno" id="gpfno" class="easyui-textbox" value="${empinfo.gpfno}"/>
                            </td>
                            <td width="10%">
                                <button type="submit" class="easyui-linkbutton">Ok</button>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>

            <div align="center" style="margin-top:5px;margin-bottom:10px;">
                <div class="easyui-panel" align="center">
                    <table width="100%" height="350px" cellspcaing="0" cellpadding="0" border="0">
                        <tr style="height:40px;">
                            <td width="30%" style="padding-left:40px;">
                                EMPLOYEE NAME
                            </td>
                            <td width="70%" style="padding-left:40px;font-weight: bold;">
                                <c:out value="${empinfo.empname}"/>
                            </td>
                        </tr>
                        <tr style="height:40px;">
                            <td style="padding-left:40px;">
                                DESIGNATION
                            </td>
                            <td style="padding-left:40px;font-weight: bold;"">
                                <c:out value="${empinfo.empdesg}"/>
                            </td>
                        </tr>
                        <tr style="height:40px;">
                            <td style="padding-left:40px;">
                                CADRE
                            </td>
                            <td style="padding-left:40px;font-weight: bold;">
                                <c:if test="${not empty empinfo.empid}">
                                    <c:if test="${not empty empinfo.cadreName}">
                                        <c:out value="${empinfo.cadreName}"/>
                                    </c:if>
                                    <c:if test="${empty empinfo.cadreCode}">
                                        <span id="cadre"></span>
                                        &nbsp;&nbsp;
                                        <input type="hidden" name="cadreCode" id="cadreCode"/>
                                        <a href="javascript:void(0)" id="change" onclick="javascript:window.open('AddCadre.htm', 'Add Cadre',
                                                    'width=600,height=800,scrollbars=1')">
                                            <button type="button" class="easyui-linkbutton">Add Cadre</button>
                                        </a>
                                    </c:if>
                                </c:if>
                            </td>
                        </tr>
                        <tr style="height:40px;">
                            <td style="padding-left:40px;">
                                MOBILE
                            </td>
                            <td style="padding-left:40px;font-weight: bold;">
                                <c:if test="${not empty empinfo.empid}">
                                    <c:if test="${not empty empinfo.mobile}">
                                        <c:out value="${empinfo.mobile}"/>
                                    </c:if>
                                    <c:if test="${empty empinfo.mobile}">
                                        <input name="mobile" id="mobile" maxlength="11" autocomplete="off"/>
                                        &nbsp;<span id="errmsg"></span>
                                    </c:if>
                                </c:if>
                            </td>
                        </tr>
                    </table>
                </div>
                <div align="center">
                    <span style="display:block;text-align: center;">
                        <button type="submit" class="easyui-linkbutton">Update</button>
                    </span>
                </div>
            </div>
        </form>
    </body>
</html>
