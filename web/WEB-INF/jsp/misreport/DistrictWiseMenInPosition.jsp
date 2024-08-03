<%-- 
    Document   : DistrictWiseMenInPosition
    Created on : Nov 11, 2016, 12:19:54 PM
    Author     : Manas Jena
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">

        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript">
            function showList(){
                
            }
        </script>
    </head>
    <body>
        <form action="districtWiseMenInPosition.htm" method="POST" commandName="WorkflowAuthority">
            <div align="center" style="margin-top:5px;margin-bottom:7px;">
                <div class="empInfoDiv" align="center" >
                    <table border="0" width="90%"  cellspacing="0" style="font-size:12px; font-family:verdana;">
                        <thead></thead>
                        <tr style="height:30">
                            <td align="center" width="15%"><b>District:</b>&nbsp;<span style="color: red">*</span></td>
                            <td width="15%">
                                <input class="easyui-combobox" id="sltDistrict" name="sltDistrict" style="width:50%;height:26px;" data-options="valueField:'distCode',textField:'distName',url:'getDistrictList.htm'">                                
                            </td>
                            <td width="15%" align="center"><b>Post Name:</b>&nbsp;<span style="color: red">*</span></td>
                            <td width="15%">
                                <input class="easyui-combobox" id="sltPost" name="sltPost" style="width:60%;height:26px;" data-options="valueField:'postcode',textField:'post',url:'getpostListDeptwise.htm'">                                                     
                            </td>
                            <td width="10%">                                
                                <input type="button" value="Ok" class="sessionstyle" onclick="return showList()"/>                        
                            </td>
                        </tr>
                    </table>
                </div>
                <div>
                    
                </div>
            </div>
        </form>
        
    </body>
</html>
