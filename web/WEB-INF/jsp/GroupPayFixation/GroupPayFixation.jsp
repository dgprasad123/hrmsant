
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ page contentType="text/html;charset=UTF-8"%>
<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
    int i = 0;
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Pay Fixation</title>
        <base href="${initParam['BaseURLPath']}" />  
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <link rel="stylesheet" type="text/css" href="resources/css/demo.css">
        <script type="text/javascript" src="js/common.js"></script>
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.min.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript" src="js/jquery.datetimepicker.js"></script>
        <script language="javascript" type="text/javascript" >
            function getempList() {
                $('#empList').datagrid('resize');
                var biliGrpId = $('#sltBillGrp').combobox('getValue');
                $('#empList').datagrid({
                    url: "emplist.htm?biliGrpId=" + biliGrpId
                });

            }

            function revisedBasic(val, row) {
                var url = "ThirdschedulePDF.htm?empid=" + row.empId;
                return "<a href=" + url + " target=\"_blank\"><img src=\"images/pdf.png\" width=\"20\" height=\"20\"></a>";
            }

            function mergeCol(val, row) {
                previousBasic = "";
                previousGp = "";
                if (row.previousBasic)
                    previousBasic = row.previousBasic;
                if (row.previousGp)
                    previousGp = row.previousGp;

                return "Basic: "+previousBasic + "<br />GP: " + previousGp;
            }
        </script>
    </head>


    <body>
        <form:form name="myForm"  action="grouppayfixation.htm" method="POST" commandName="emp">
            <div align="center">
                Bill Group : <input class="easyui-combobox"  id="sltBillGrp" name="sltBillGrp" data-options="valueField:'billgroupid',textField:'billgroupdesc',url:'getBillGrpNameJSON.htm',width:'30%'" ></input>

                <button type="button" onclick="getempList()">Ok</button>
            </div>
            <div  style="width:99%;">                        
                <div   style="width:100%;overflow: auto;margin-top:20px;border:1px solid #5095ce;"> 
                    <table id="empList" class="easyui-datagrid" style="width:100%;height:360px;" title="Group Pay Fixation"
                           rownumbers="true" pagination="true" nowrap="false" toolbar="#toolbar">
                        <thead>
                            <tr>
                                <!-- <th data-options="field:'doe'"  width="30%">Date Of entry in<br> the Service Book</th>-->
                                <th data-options="field:'empId',width:80">HRMS ID</th>
                                <th data-options="field:'gpfNo',width:120">GPF No/PRAN</th>
                                <th data-options="field:'empName',width:250">Employee Name.</th>
                                <th data-options="field:'post',width:400">Post</th>
                                <th data-options="field:'curPayScale',width:130">Pay Scale</th>
                                <th data-options="field:'temp1',width:130,formatter:mergeCol">Previous Basic + Grade Pay</th>
                                <th data-options="field:'revisedBasic',width:130">Revised Basic</th>
                                <th data-options="field:'temp',width:100,formatter:revisedBasic,align:'center'">View Detail</th>

                            </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </form:form>
    </body>
</html>
