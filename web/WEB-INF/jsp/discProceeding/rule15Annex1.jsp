
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
            
            $(document).ready(function(){
               $('#chargeListDg').datagrid({
                    url: "ChargeListJSON.htm?DAID="+$('#hidDaId').val()
                });
            });
            
            function saveAndExit(){
                if(confirm('Are You sure to Save Charges and Witness?')){
                    return true;
                }else{
                    return false;
                }
                
               return false; 
            }
            
            function addCharges(){
                var url="addNewCharge.htm?DOHRMSID="+$('#hidAnnx1HrmsId').val()+"&DAID="+$('#hidDaId').val()+"&offCode="+$('#hidOffCode1').val();
                self.location = url;
            }
            
            function edtCharge(val,row){
                var dacId = row.chargeDacId;
                var edtChrg = "editCharge.htm?DOHRMSID="+$('#hidAnnx1HrmsId').val()+"&DAID="+$('#hidDaId').val()+"&DACID="+dacId+"&offCode="+$('#hidOffCode1').val();
                return "<a href='"+edtChrg+"'>Edit</a>"; 
            }
            function edtWitness(val,row){
                var dacId = row.chargeDacId;
                var mod="E";
                var edtWitness = "editWitness.htm?DOHRMSID="+$('#hidAnnx1HrmsId').val()+"&DAID="+$('#hidDaId').val()+"&DACID="+dacId+"&MODE="+mod+"&offCode="+$('#hidOffCode1').val();
                return "<a href='"+edtWitness+"'>Edit</a>"; 
            }
            
            function addWitness(){
                var mod="N";
                var row = $('#chargeListDg').datagrid('getSelected');
                if (row) {
                    $('#hidDaCIds').val(row.chargeDacId);
                    var url="addNewWitness.htm?DOHRMSID="+$('#hidAnnx1HrmsId').val()+"&DAID="+$('#hidDaId').val()+"&DACID="+$('#hidDaCIds').val()+"&MODE="+mod+"&offCode="+$('#hidOffCode1').val();
                    self.location = url;
                } else {
                    alert("Select a Charge");
                }
            }
            
            function delChargeWitness(val,row){
                var dacId = row.chargeDacId;
                var delData = 'deleteChargeWitness.htm?DACID='+dacId+"&DOHRMSID="+$('#hidAnnx1HrmsId').val()+"&DAID="+$('#hidDaId').val()+"&offCode="+$('#hidOffCode1').val();
                return "<a href='"+delData+"'><img src='images/Delete-icon.png'></a>"; 
            }
            
            function delWitness(val,row){
                var dacId= row.chargeDacId;
                var wName= row.witnessName;
                if(wName !=''){
                    return "<a href='javascript:void(0)' onclick='delWitnessOnly(\""+dacId+"\")'><img src='images/Delete-icon.png'></a>"; 
                }else{
                    return "<a href='javascript:void(0)' onclick='delWitnessMsg()'><img src='images/Delete-icon.png'></a>"; 
                }
            }
            
            function delWitnessMsg(){
                alert("No Witness Found...Can't Delete");  
            }
            
            function delWitnessOnly(dacId){
                $.ajax({
                    type: "POST",
                    url: "deleteWitness.htm?DACID="+dacId+"&DOHRMSID="+$('#hidAnnx1HrmsId').val()+"&DAID="+$('#hidDaId').val()+"&offCode="+$('#hidOffCode1').val(),
                    dataType: "json"
                }).done(function(serverResponse) {
                    //$.messager.alert(serverResponse.msgType, serverResponse.msg);
                    $('#chargeListDg').datagrid({
                        url: "ChargeListJSON.htm?DAID="+$('#hidDaId').val()
                    });
                });
            }
            
        </script>
        <style type="text/css">
            body{
                font-family: Verdana;
                font-size:16px;
            }
        </style>
    </head>
    <body>
        <table style="font-family:Verdana;margin-top: 5px;" width="99%" cellpadding="0" cellspacing="0">
            <tr>
                <td style="width:24%;">
                    <div style="border-width:1px;width:99%;color:#333;border-color:#d7d7d7;border-style:solid; border-width:1px 1px 1px 0;text-align:center;">
                        IRREGULARITIES
                    </div>
                </td>
                <td style="width:24%;">
                    <div style="border-width:1px;width:99%;font-weight:bold;color:#333;border-color:#d7d7d7;border-style:solid; border-width:1px 1px 0 1px;text-align:center;">
                        CHARGES AND WITNESS
                   </div>
                </td>
            </tr>
        </table>
        
        <form action="saveRule15Anex1.htm" method="POST" commandName="DiscProceedingBean">
            
            <input type="hidden" name="hidAnnx1DoHrmsId" id="hidAnnx1HrmsId" value='${Annex1Value.doHrmsId}'/>
            <input type="hidden" name="hidAnnx1DaId" id="hidDaId" value='${Annex1Value.daid}'/>
            <input type="hidden" name="hidDaCIds" id="hidDaCIds"/>
            <input type="hidden" name="hidOffCode1" id="hidOffCode1" value='${Annex1Value.hidOffCode1}'/>
            
            <div align="center" width="99%" style="margin-top:5px;min-height:60%;overflow: auto;">
                <table id="chargeListDg" class="easyui-datagrid" width="100%" style="min-height:99%;" title="CHARGES AND WITNESS" rownumbers="true"
                       pagination="true" toolbar="#toolbar" data-options="singleSelect:true" nowrap="false">
                    <thead>
                         <tr>
                            <th  rowspan="2" data-options="field:'chargeDacId',hidden:true,align:'center'"></th>
                            <th  rowspan="2" data-options="field:'rule15Articles',width:200,align:'left'"><span style="font-weight: bold;">CHARGE</span></th>
                            <th  rowspan="2" data-options="field:'rule15DocumentName',width:140,align:'left'"><span style="font-weight: bold;">DOCUMENT NAME</span></th>
                            <th  rowspan="2" data-options="field:'witnessName',width:300,align:'left'"><span style="font-weight: bold;">WITNESS</span></th>
                            <th  colspan="2"><span style="font-weight: bold;">EDIT</span></th>
                            <th  colspan="2"><span style="font-weight: bold;">DELETE</span></th>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <th data-options="field:'temp2',width:60,align:'center',formatter:edtCharge">Charge</th>
                            <th data-options="field:'temp3',width:60,align:'center',formatter:edtWitness">Witness</th>
                            
                            <th data-options="field:'temp4',width:60,align:'center',formatter:delChargeWitness">Charge</th>
                            <th data-options="field:'temp5',width:60,align:'center',formatter:delWitness">Witness</th>
                        </tr>
                    </thead>
                </table>
                
                <div id="toolbar">
                    <a href="javascript:void(0)" class="easyui-linkbutton" plain="true" 
                       data-options="iconCls:'icon-add'" onclick="return addCharges()"><span style="font-size:15px;font-weight: bold;">Add Charges</span>
                    </a>
                    
                    <a href="javascript:void(0)" class="easyui-linkbutton" plain="true"  onclick="return addWitness()"
                       data-options="iconCls:'icon-add'"><span style="font-size:15px;font-weight: bold;">Add Witness</span>
                    </a>
                </div>
            </div>
            
            <div align="center" width="99%" style="margin-top:5px;margin-bottom:10px;">
                <div align="center" class="easyui-panel" width="99%">
                    <table border="0" width="100%">
                        <tr>
                            <c:if test="${not empty ShowBtn}">
                                <td width="50%" align="right" style="padding-right:20px;">
                                    <input type="submit" name="rule15Over" value="Save And Exit" onclick="return saveAndExit()"
                                           class="easyui-linkbutton" data-options="iconCls:'icon-add'"/>
                                </td>
                            </c:if>
                        </tr>
                    </table>
                </div>
            </div>
            
        </form>
    </body>
</html>

