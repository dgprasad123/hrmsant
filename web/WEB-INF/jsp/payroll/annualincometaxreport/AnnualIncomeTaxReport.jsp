<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Annual Income TAX Report</title>
        
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript">
            $(document).ready(function(){
                $('#billList').combobox({
                    url: 'GetBillListJSON.htm?offCode='+$("#offCode").val()
                });
            });
            
            function getEmpList(){
                var finyr = $('#finyear').combobox('getValue');
                var bill = $('#billList').combobox('getValue');
                $('#billgrpid').val(bill);
                $('#finyr').val(finyr);
                $('#emplistdg').datagrid({
                    url : 'getEmpListJSON.htm?finyear='+finyr+'&billname='+bill
                });
            }
            
            function downloadReport(val, row) {
                var excelLink = 'AnnualIncomeTAXExcelReport.htm?empid=' + row.empId + '&offcode='+$('#offCode').val()+'&fyear='+$('#finyr').val();
                //alert("Link is: "+excelLink);
                return "<a href='" + excelLink + "' target='_blank'>Download</a>";
            }
        </script>
    </head>
    <body>
        <input type="hidden" id="offCode" value="${offcode}"/>
        <input type="hidden" id="billgrpid"/>
        <input type="hidden" id="finyr"/>
        <div align="center">
            <table border="0" width="100%" cellspacing="0" style="font-size:12px; font-family:verdana;">
                <tr style="height:40px;">
                    <td width="20%" align="center">
                        Select Financial Year:
                    </td>
                    <td width="25%">
                        <input name="finyear" id="finyear" class="easyui-combobox" style="width:50%" data-options="valueField:'value',textField:'label',url:'GetFinYearJSON.htm'" />
                    </td>
                    <td width="25%" align="center">
                        Select Bill:
                    </td>
                    <td width="20%">
                        <input name="billList" id="billList" class="easyui-combobox" style="width:70%" data-options="valueField:'value',textField:'label'" />
                    </td>
                    <td width="10%">
                        <input type="submit" value="Process" class="easyui-linkbutton" onclick="getEmpList();"/>
                    </td>
                </tr>
            </table>
        </div>
        <div align="center">
            <table id="emplistdg" class="easyui-datagrid" style="width:100%;height:400px;" title="Annual Income TAX Report"
                   rownumbers="true" singleSelect="true" singleSelect="true" pagination="true" collapsible="false"
                   data-options="nowrap:false">
                <thead>
                    <tr>
                        <th data-options="field:'empId',width:80">HRMS ID</th>
                        <th data-options="field:'accType',width:150">Account Type</th>
                        <th data-options="field:'gpfno',width:150">GPF No</th>
                        <th data-options="field:'name',width:300">Employee Name</th>
                        <th data-options="field:'temp',width:120,formatter:downloadReport">Download IT Report</th>
                    </tr> 
                </thead>
            </table>
        </div>
    </body>
</html>
