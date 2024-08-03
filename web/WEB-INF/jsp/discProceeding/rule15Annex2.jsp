
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:HRMS:</title>
        
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css"/>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css"/>
        <link rel="stylesheet" type="text/css" href="css/hrmis.css" />

        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript">
            
            $(document).ready(function(){
                
            });
            
            function editAchievement(val,row){
                var id = row.pacid;
                var data = 'editAbsenteeorAchievement.htm?id='+id+'&mode=achievement&fiscalyear='+$('#fiscalyear').val();
                return "<a href='"+data+"'>Edit</a>";
            }
            
            function callDeleteAchievementFunction(val,row){
                var id = row.pacid;
                return "<a href='javascript:void(0)' onclick='deleteAchievement(\""+id+"\")'>Delete</a>";
                //return "<a href='deleteAbsenteeorAchievement.htm?id="+id+"&mode=achievement'>Delete</a>";
            }
            
            function deleteAchievement(pacid){
                if(confirm("Are you sure to delete?")){
                    $.ajax({
                        type: "GET",
                        url: "deleteAbsenteeorAchievement.htm?id="+pacid+"&mode=achievement&fsclyr="+$('#fiscalyear').val(),
                        dataType: "json"
                    }).done(function(serverResponse) {
                        $.messager.alert(serverResponse.msgType, serverResponse.msg);
                        $('#achievement').datagrid('reload');
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
        <table style="font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 15px;margin-top: 5px;" width="99%" cellpadding="0" cellspacing="0">
            <tr>
                <td style="width:24%;">
                    <div style="border-width:1px;width:99%;color:#333;border-color:#d7d7d7;border-style:solid; border-width:1px 1px 1px 0;text-align:center;">
                        MEMORANDUM
                    </div>
                </td>
                <td style="width:24%;">
                    <div style="border-width:1px;width:99%;color:#333;border-color:#d7d7d7;border-style:solid; border-width:1px 1px 1px 0;text-align:center;">
                        ANNEXURE-I
                   </div>
                </td>
                <td style="width:24%;">
                    <div style="border-width:1px;width:99%;font-weight:bold;color:#333;border-color:#d7d7d7;border-style:solid; border-width:1px 1px 0 1px;text-align:center;">
                        ANNEXURE-II
                    </div>
                </td>
                <td style="width:24%;">
                    <div style="border-width:1px;width:99%;color:#333;border-color:#d7d7d7;border-style:solid; border-width:1px 1px 1px 0;text-align:center;">
                        ANNEXURE-III
                    </div>
                </td>
            </tr>
        </table>
        
        <form action="saveRule15Anex2.htm" method="POST" commandName="rule15Form">
            <input type="hidden" name="hidDacId" id="hidDacId" value='${DACID}'/>
            <input type="hidden" name="hidAnnx2HrmsId" id="hidAnnx2HrmsId" value='${Annex2Value.empHrmsId}'/>
            <div id="tbl-container" class="easyui-panel" title="ANNEXURE-II" align="center"  style="width:100%;overflow: auto;">
                <div align="left" style="padding-left:10px">
                    <table width="100%" border="0" style="font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 13px;">
                        <tr>
                            <td colspan="2" align="center" style="font-weight: bold;">ANNEXURE-II</td>
                        </tr>
                        <tr style="height: 10px;">
                            <td colspan="2" align="center" style="font-weight: bold;">STATEMENT OF IMPUTATIONS OF MISCONDUCT</td>
                        </tr>
                        <tr style="height: 10px;">
                            <td colspan="2">&nbsp;</td>
                        </tr>
                        <tr style="margin-top: 5px;">
                            <td><p style="text-align: justify;">&nbsp;&nbsp;&nbsp;Statement of imputations of misconduct in support of the articles of 
                                    charge framed against Sri <span style="font-weight:bold;">
                                        <c:out value="${Annex2Value.empName}"/>, <c:out value="${Annex2Value.empCurDegn}"/></span>.</p>
                            </td>
                        </tr>
                        <c:forEach var="i" begin="1" end="3">
                            <tr>
                                <td style="height:10px;"><p style="text-align: justify;">Article-${i}. <input type="text" name="rule15Articles" id="rule15Articles" class="easyui-textbox" maxlength="19" style="width:60%;border:1px solid #000000;border-radius: 0.3em;"/></p></td>
                            </tr>
                        </c:forEach>
                        
                        
                    </table>
                </div>
            </div>
            
            <div align="center" width="99%" style="margin-top:5px;margin-bottom:10px;">
                <div align="center" class="easyui-panel" width="99%">
                    <table border="0" width="100%">
                        <tr>
                            <td width="50%" align="left" style="padding-left:20px;">
                                <input type="submit" name="btnSubmit3" value="Previous" class="easyui-linkbutton"/>
                            </td>
                            <td width="50%" align="right" style="padding-right:20px;">
                                <input type="submit" class="easyui-linkbutton" name="btnAnnexture3" value="ANNEXURE-III" 
                                        style="width:40%;font-weight: bold;border: 1px solid #2A7CBE;"/>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
            
        </form>
    </body>
</html>



