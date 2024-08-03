<%-- 
    Document   : RevertGroupCPAR
    Created on : 22 Oct, 2021, 11:09:12 AM
    Author     : Manisha
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
   
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css"/>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css"/>

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script language="javascript" type="text/javascript">
            function saveCheck() {
                if ($('#revertremarks').val() == '') {
                    alert("Please enter Remarks");
                    $('#revertremarks').focus();
                    return false;
                } else {
                    return true;
                }
            }
            function submitCheck() {
                var isValidated = saveCheck();
                if (isValidated == true) {
                    var isConfirm = confirm("Are you sure to Revert the PAR Of GroupC Employee?");
                    if (isConfirm == true) {
                        alert("Reverted Successfully.");
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        </script>
    </head>
    <body>
        <form action="RevertGroupCPAR.htm" method="POST" commandName="groupCEmployee">
           
            <div align="center" style="margin-top:5px;margin-bottom:10px;">
                <div align="center">
                    <table border="0" width="90%"  cellspacing="0" style="font-size:12px; font-family:verdana;">
                    </table>
                </div>
            </div>

            <div align="center">
                <div style="overflow: auto; scrollbar-base-color:#A6D3FF;">
                    <table width="100%" cellpadding="0" cellspacing="0" style="font-family:Verdana;font-size:13px;color:black;">
                        <tr>
                            <td align="center" valign="middle" width="30%">Remarks for Revert:</td>
                            <td width="70%">
                                <textarea name="revertremarks" id="revertremarks" style="width:90%;border:1px solid #000000;"></textarea>
                            </td>
                        </tr>
                    </table>
                </div>

                <div align="center">
                    <div style="margin-top:10px;"> 
                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                            <tr style="height:40px">
                                
                                <c:if test="${revertstatus == null || revertstatus != 'Y'}">
                                    <td align="left" width="30%">
                                        <span style="padding-left:10px;">
                                            <input type="hidden" name="groupCpromotionId" value="${groupCEmployee.groupCpromotionId}"/>
                                            <input type="hidden" name="remarkauthoritytype" value="${groupCEmployee.remarkauthoritytype}"/>
                                            <input type="hidden" name="taskId" value="${groupCEmployee.taskId}"/>
                                            <input type="submit" name="action"  value="Submit" onclick="return submitCheck();"/>
                                        </span>
                                    </td>
                                    <td width="70%">&nbsp;</td>
                                </c:if>
                                <c:if test="${revertstatus != null && revertstatus == 'Y'}">
                                    <td width="30%">&nbsp;</td>
                                    <td width="70%">
                                        <span style="color:red;">Reverted Successfully.</span>
                                    </td>
                                </c:if>
                                
                            </tr>
                        </table>
                    </div>
                </div>
            </div>
        </form>
    </body>
</html>

