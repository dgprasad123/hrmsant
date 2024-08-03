<%@ page contentType="text/html;charset=windows-1252"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";

    String completedtasklink = "";
%>
<html>
    <head>
        <base href="${initParam['BaseURLPath']}" />  
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <title>Human Resources Management System, Government of Odisha</title>

        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <!-- Custom CSS -->
        <link href="css/sb-admin.css" rel="stylesheet">
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>

        <!-- Bootstrap Core JavaScript -->
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            function openReport() {
                var reportlink = $('#sltReportType').combobox('getValue');
                window.open(reportlink, "_blank");
            }
        </script>
    </head>

    <body style="padding:0px;">
        <div id="wrapper">

            <jsp:include page="../tab/ServiceConditionAdminMenu.jsp"/>        
            <div id="page-wrapper">

                <div class="container-fluid">
                    <div style="padding:10px;height:50%;">
                        <div align="center" style="margin-top:5px;margin-bottom:7px;height:50%;">
                            <table border="0" cellspacing="0" cellpadding="0" style="width:100%">
                                <tr>
                                    <td width="30%" align="center">
                                        Select LIC Report Type
                                    </td>
                                    <td width="70%">
                                        <select name="sltReportType" id="sltReportType" class="easyui-combobox" style="width:300px;">
                                            <option value="">--Select--</option>
                                            <option value="LICTreasuryWise.htm">LIC Treasury Wise</option>
                                            <option value="LICDivisionWise.htm">LIC Division Wise</option>
                                        </select>
                                        &nbsp;&nbsp;&nbsp;&nbsp;
                                        <button type="button" class="easyui-linkbutton c6" onclick="openReport();">Show</button>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
