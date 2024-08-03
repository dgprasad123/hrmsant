<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <c:set var="r" value="${pageContext.request}" />
    <base href="${initParam['BaseURLPath']}" />  
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Performance Appraisal</title> 

        <link href="resources/css/bootstrap.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css"/>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css"/>
        <link rel="stylesheet" type="text/css" href="css/hrmis.css"/>
        <link rel="stylesheet" type="text/css" href="css/color.css"/>

        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript">
            $(document).ready(function(){
                $('#absentee').datagrid({
                    url: "GetAbsenteeListJSON.htm?parid="+$('#parId').val()
                });
            });
            
            function editAbsentee(val,row){
                var id = row.pabid;
                var data = 'par/editAbsenteeorAchievement.htm?id='+id+'&mode=absentee&parfrmdt='+$('#hidparfrmdt').val()+'&partodt='+$('#hidpartodt').val()+'&csrfPreventionSalt='+$("#csrfPreventionSalt").val();
                return "<a href='"+data+"'>Edit</a>";
            }
            
            function callDeleteAbsenteeFunction(val,row){
                var id = row.pabid;
                //alert("ID is: "+id);
                return "<a href='javascript:void(0)' onclick='deleteAbsentee(\""+id+"\")'>Delete</a>";
                //return "<a href='deleteAbsenteeorAchievement.htm?id="+id+"&mode=absentee'>Delete</a>";
            }
            
            function deleteAbsentee(pabid){
                if(confirm("Are you sure to delete?")){
                    $.ajax({
                        type: "POST",
                        url: "par/deleteAbsenteeorAchievement.htm?id="+pabid+"&mode=absentee&csrfPreventionSalt="+$('#csrfPreventionSalt').val(),
                        dataType: "json"
                    }).done(function(serverResponse) {
                        $.messager.alert(serverResponse.msgType, serverResponse.msg);
                        $('#absentee').datagrid({
                            url: "GetAbsenteeListJSON.htm?parid="+$('#parId').val()
                        });
                    });
                }else{
                    return false;
                }
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
        <table style="font-family:Verdana;" cellpadding="0" cellspacing="0">
            <tr>
                <td style="width:253px;"><div style="border-width:1px;width:251px;color:#333;border-color:#d7d7d7;border-style:solid; border-width:1px 1px 1px 0;padding:10px 0px 10px 0px;text-align:center;">Personal Information</div></td>
                <td style="width:253px;"><div style="border-width:1px;width:251px;font-weight:bold;color:#333;border-color:#d7d7d7;border-style:solid; border-width:1px 1px 0 1px;padding:10px 0px 10px 0px;text-align:center;">Absentee Statement</div></td>
                <td style="width:253px;"><div style="border-width:1px;width:251px;color:#333;border-color:#d7d7d7;border-style:solid; border-width:1px 1px 1px 0;padding:10px 0px 10px 0px;text-align:center;">Achievements</div></td>
                <td style="width:253px;"><div style="border-width:1px;width:251px;color:#333;border-color:#d7d7d7;border-style:solid; border-width:1px 1px 1px 0;padding:10px 0px 10px 0px;text-align:center;">Other Details</div></td>
            </tr>
        </table>
        <form action="par/addPAR.htm" method="POST" commandName="parAbsentee">
            <input type="hidden" name="csrfPreventionSalt" id="csrfPreventionSalt" value="<c:out value='${csrfPreventionSalt}'/>"/>
            <input type="hidden" name="hidparid" id="parId" value="${parMastForm.parid}"/>
            <input type="hidden" name="fiscalyear" id="fiscalyear" value='${parMastForm.fiscalyear}'/>
            <input type="hidden" name="hidparfrmdt" id="hidparfrmdt" value='${parfrmdt}'/>
            <input type="hidden" name="hidpartodt" id="hidpartodt" value='${partodt}'/>
            <div>
                <table id="absentee" class="easyui-datagrid" style="width:100%;height:450px;" title="My Leave"
                       rownumbers="true" pagination="true" singleSelect="true">
                    <thead>
                        <tr>
                            <th data-options="field:'pabid',hidden:true">SL No</th>
                            <th data-options="field:'fromdate',width:100"><b> From Date </b></th>
                            <th data-options="field:'todate',width:100">To Date</th>
                            <th data-options="field:'leavecause',width:150">Leave / Training</th>
                            <th data-options="field:'leaveortrainingtype',width:300">Type of Leave / Training Type</th>
                            <th data-options="field:'temp',width:50,formatter:editAbsentee">Edit</th>
                            <th data-options="field:'temp1',width:50,formatter:callDeleteAbsenteeFunction">Delete</th>
                        </tr> 
                    </thead>
                </table>
                <table border="0">
                    <tr>
                        <td>
                            <input type="submit" name="newPar" value="Add New" class="easyui-linkbutton c1" style="width:120px;height: 30px;"/>
                        </td>
                    </tr>
                </table>
            </div>
            
            <div align="right">
                <div align="right" style="margin-top:20px;margin-bottom:0px;">
                    <input type="hidden" name="mode" value="absentee"/>
                    <input type="hidden" name="pageno" value="2"/>
                    <table border="0" width="100%">
                        <tr>
                            <td width="50%" align="left" style="padding-left:20px;">
                                <input type="submit" name="newPar" value="Previous" class="easyui-linkbutton c7" style="width:100px;height: 30px;"/>
                            </td>
                            <td width="50%" align="right" style="padding-right:20px;">
                                <input type="submit" name="newPar" value="Next" class="easyui-linkbutton c1" style="width:100px;height: 30px;"/>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
        </form>
    </body>
</html>
