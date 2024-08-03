
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
%>
<html>
    <head>
        <base href="${initParam['BaseURLPath']}" />  
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>PROCEEDINGS</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>

        <script type="text/javascript">
            $(document).ready(function() {
                $('#empWitnesListDg').datagrid({
                    url: "GetEMPWitnessListJSON.htm?DOHRMSId=" + $('#hidWitnessDoHrmsId').val() + "&DACID=" + $('#hidWitnessDacId').val() + "&MODE=" + $('#hidMode').val() + "&offCode=" + $('#hidOffCode2').val(),
                    onLoadSuccess: function(data) {
                        var rows = $(this).datagrid('getRows');
                        for (i = 0; i < rows.length; ++i) {
                            if (rows[i]['chkVal'] == 1)
                                $(this).datagrid('checkRow', i);
                        }
                    }
                });
            });

            function saveWitness() {
                var ids = [];
                var rows = $('#empWitnesListDg').datagrid('getSelections');
                if (rows.length > 0) {
                    for (var i = 0; i < rows.length; i++) {
                        ids.push(rows[i].doHrmsId);
                    }
                    $('#hidWitnessIds').val(ids);
                } else {
                    alert("Select an Employee");
                    return false;
                }
            }


        </script> 
    </head>
    <body>
        <form action="saveRule15Witness.htm" method="POST" commandName="Rule15ChargeBean">
            <input type="hidden" name="hidWitnesDaid" id="hidWitnesDaid" value='${WitnesValue.daid}'/>
            <input type="hidden" name="hidWitnessDoHrmsId" id="hidWitnessDoHrmsId" value='${WitnesValue.doHrmsId}'/>
            <input type="hidden" name="hidWitnessDacId" id="hidWitnessDacId" value='${WitnesValue.dacId}'/>
            <input type="hidden" name="hidMode" id="hidMode" value='${WitnesValue.mode}'/>
            <input type="hidden" name="hidWitnessIds" id="hidWitnessIds"/>
            <input type="hidden" name="hidOffCode2" id="hidOffCode2" value='${WitnesValue.hidOffCode}'/>

           
            <div align="center" width="99%" style="margin-top:5px;margin-bottom:1px;height:62%;overflow: auto;">
                <table id="empWitnesListDg" class="easyui-datagrid" style="width:95%;" title="Add Witness" rownumbers="true" 
                       singleSelect="false" collapsible="false" toolbar="#toolbar">
                    <thead>
                        <tr> 
                            <th data-options="field:'doHrmsId',hidden:true"></th>
                            <th data-options="field:'chkVal',width:50,checkbox:true"></th>
                            <th data-options="field:'doEmpName',width:250">EMPLOYEE NAME</th>
                            <th data-options="field:'doEmpCurDegn',width:300">DESIGNATION</th>
                        </tr> 
                    </thead>
                </table>
            </div>

            <div align="center" style="margin-top:1px;margin-bottom:10px;">
                <div align="center" class="easyui-panel" width="95%">
                    <table border="0" width="100%">
                        <tr>
                            <td width="50%" align="left" style="padding-left:20px;">
                                <input type="submit" name="rule15AddWitness" value="Back" class="easyui-linkbutton" data-options="iconCls:'icon-remove'"/>
                            </td>
                            <td width="50%" align="right" style="padding-right:20px;">
                                <c:if test="${not empty UpdBtn}">
                                    <input type="submit" name="rule15AddWitness" onclick="return saveWitness()" value="Update" class="easyui-linkbutton" data-options="iconCls:'icon-add'"/>
                                </c:if>
                                <c:if test="${empty UpdBtn}">
                                    <input type="submit" name="rule15AddWitness" value="Save" onclick="return saveWitness()"
                                           class="easyui-linkbutton" data-options="iconCls:'icon-add'"/>
                                </c:if>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>

        </form>
    </body>
</html>
