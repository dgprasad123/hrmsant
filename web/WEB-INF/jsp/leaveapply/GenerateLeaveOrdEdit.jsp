<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<% String url = "";
    String myempId = "";
    String attachId = "";
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
    int i = 0;
%>
<html>
    <head>
        <base href="${initParam['BaseURLPath']}" />          
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <link rel="stylesheet" type="text/css" href="resources/css/colorbox.css"/>
        <script language="javascript" src="js/servicehistory.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript"  src="js/jquery.colorbox-min.js"></script>
        <link href="css/jquery.datetimepicker.css" rel="stylesheet" type="text/css" />
        <script language="javascript" src="js/jquery.datetimepicker.js" type="text/javascript"></script>
        <script language="javascript" type="text/javascript" >
            function searchAuthority() {
                var url = 'leaveauthority.htm';
                $.colorbox({href: url, iframe: true, open: true, width: "70%", height: "450px", overlayClose: false});
            }
            function SelectSpn(empId, empName, desig, spc)
            {
                $.colorbox.close();
                $('#txtSancAuthority').textbox('setValue', empName + "," + desig);
                
                $("#hidAuthEmpId").val(empId);
                $("#hidSpcAuthCode").val(spc);
               
            }
            function savecheck() {
                if ($('#txtSancAuthority').val() == "") {
                    alert("Please Forward the Leave to Issuing authority");
                    return false;
                }
            }

        </script>
        <style type="text/html">
            .watermark {
                opacity: 0.5;
                color: BLACK;
                position: fixed;
                top: auto;
                left: 80%;
            }
        </style>
        <base href="${initParam['BaseURLPath']}" />  
    </head>
    <body>
        <form:form  action="leaveViewData.htm" method="POST" commandName="leaveForm">
            <input type="hidden" name="txtApproveFrom" id="txtApproveFrom" value="${leaveForm.txtApproveFrom}"/>
            <input type="hidden" name="txtApproveTo" id="txtApproveTo" value="${leaveForm.txtApproveTo}"/>
            <input type="hidden" name="tollid" id="tollid" value="${leaveForm.tollid}"/>
            <input type="hidden" name="leaveId" id="leaveId" value="${leaveForm.leaveId}"/>
            <input type="hidden" name="sltActionType" id="sltActionType" value="${leaveForm.sltActionType}"/>
            <form:hidden path="hidAuthEmpId" id="hidAuthEmpId" />
            <form:hidden path="hidSpcAuthCode" id="hidSpcAuthCode" />
            <input type="hidden" name="hidempId" id="hidempId" value="${leaveForm.hidempId}"/>
            <input type="hidden" name="hidTaskId" id="hidTaskId" value="${leaveForm.hidTaskId}"/>
            <input type="hidden" name="txtauthnote" id="txtauthnote" value="${leaveForm.txtauthnote}"/>

            <div align="center">
                <div style="width:99%;">
                    <div id="tbl-container" class="easyui-panel" title="Forward To Section"  style="width:100%;height: 400px;font-size:12px; font-family:verdana;">
                        <table cellpadding="5">
                            <c:if test = "${leaveForm.sltActionType == '1'}"> 
                                <tr style="height: 40px" >                               
                                    <td align="center">&nbsp; </td>
                                    <td >&nbsp;</td>                                                                                          
                                    <td colspan="4">&nbsp;</td>
                                </tr>
                                <tr style="height: 40px" >                               
                                    <td align="center">&nbsp; </td>
                                    <td >Forward to Leave Establishment Section  : <span style="color: red">*</span>:</td>                                                                                          
                                    <td colspan="4"><input class="easyui-textbox" id="txtSancAuthority" type="text"  name="txtSancAuthority" style="width:300px;height:25px"    readonly="true"></input>
                                        <a href="javascript:void(searchAuthority())" id="Search" class="easyui-linkbutton"> Search </a>
                                    </td>
                                </tr>
                            </c:if>
                            <c:if test = "${leaveForm.sltActionType == '42'}"> 
                                <tr style="height: 40px" >                               
                                    <td align="center">&nbsp; </td>
                                    <td >&nbsp;</td>                                                                                          
                                    <td colspan="4">&nbsp;</td>
                                </tr>
                                <tr style="height: 40px" >                               
                                    <td align="center">&nbsp; </td>
                                    <td >&nbsp;</td>                                                                                          
                                    <td colspan="4"><span style="color: red">Leave is Declined</span>
                                    </td>
                                </tr>
                            </c:if>

                        </table>
                    </div>
                </div>
            </div>
            <div style="text-align:center;padding:5px">
                <table>
                    <tr  height="40px">
                        <td align="center" colspan="4">
                            <c:if test = "${leaveForm.sltActionType == '1'}"> 
                                <input class="easyui-linkbutton" type="submit" name="Submit" value="Submit" onclick="return savecheck()"/>      
                            </c:if>
                        </td>
                    </tr>
                </table>
            </div>
        </form:form>

    </body>
</html>
