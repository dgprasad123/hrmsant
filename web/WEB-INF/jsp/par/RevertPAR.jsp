<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
    <c:set var="r" value="${pageContext.request}" />
    <base href="${initParam['BaseURLPath']}" />  
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css"/>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css"/>

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <style>

            .button {
                background-color: #4CAF50;
                border: none;
                color: white;
                padding: 10px 25px;
                text-align: center;
                text-decoration: none;
                display: inline-block;
                font-size: 16px;
                margin: 4px 2px;
                cursor: pointer;
            }

        </style>    
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
                    var isConfirm = confirm("Are you sure to Revert the PAR?");
                    if (isConfirm == true) {
                        return true;
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
        <form action="RevertSave.htm" method="POST" commandName="parDetail">
            <input type="hidden" name="csrfPreventionSalt" value="<c:out value='${csrfPreventionSalt}'/>"/>
            <input type="hidden" name="parid" value="${ParDetail.parid}"/>
            <input type="hidden" name="parstatus" value="${ParDetail.parstatus}"/>
            <input type="hidden" name="taskid" value="${ParDetail.taskid}"/>
            <input type="hidden" name="fiscalYear" value="${ParDetail.fiscalYear}"/>

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
                            <td align="center" valign="middle" width="30%"><strong>Remarks for Revert:</strong></td>
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
                                    <td align="right" width="40%">
                                        <span style="padding-left:0px;">
                                            <input type="submit" name="revertSubmit" class=" button" value="Revert" onclick="return submitCheck();" />
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
                                <c:if test="${isClosed == 'Y'}">
                                <span style="color:red;">Revert Operation for Financial Year <c:out value="${pardetail.fiscalYear}"/> is closed.</span>
                            </c:if>
                            </tr>
                        </table>
                    </div>
                </div>
            </div>
        </form>
    </body>
</html>
