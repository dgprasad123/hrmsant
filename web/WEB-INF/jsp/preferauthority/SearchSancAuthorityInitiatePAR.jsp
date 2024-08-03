<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
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
            
            $(document).ready(function(){
               $('#sanctionedauth').datagrid({
                   url: "GetSanctionedAuthorityListInitiatePARJSON.htm?processid="+$('#hidprocessCode').val()+"&fiscalYear="+$('#hidfslYear').val()+"&spc="+$('#hidspc').val()
                }); 
            });
            
            function radioFunc(val,row){
                var data = row.combination;
                //alert(data);
                return "<input type='radio' name='rdEmpid' id='rdEmpid' value='"+data+"' onclick='SelectPost(\""+data+"\")'/>";
            }
            
            function SelectPost() {
                var radval = $("input[name*='rdEmpid']:radio:checked").val();
                var radspl = radval.split(':');
                var empId = radspl[0];
                var name = radspl[1];
                var desg = radspl[2];
                var spc = radspl[3];
                var authType = $("#hidauthtype").val();
                var rowid = $("#hidrowid").val();
                //alert("authType is: "+authType+" and rowid is: "+rowid);
                parent.SelectSpn(empId, name, desg, spc, authType, rowid);
            }
        </script>
    </head>
    <body>
        <table id="sanctionedauth" class="easyui-datagrid" style="width:100%;height:400px;" title="Sanctioned Authority"
            rownumbers="true" pagination="true" nowrap="false" singleSelect="true" data-options="collapsible:false">
            <thead>
                <tr>
                    <th data-options="field:'combination',width:50,formatter:radioFunc"></th>
                    <th data-options="field:'empname',width:250">Name</th>
                    <th data-options="field:'post',width:400">Designation</th>
                </tr> 
            </thead>
        </table>
        <form action="addAuthorityInitiatePAR.htm" method="POST" commandName="WorkflowAuthority">
            <input type="hidden" name="hidauthtype" id="hidauthtype" value='${authType}'/>
            <input type="hidden" name="hidrowid" id="hidrowid" value='${rowid}'/>
            
            <input type="hidden" name="hidprocessCode" id="hidprocessCode" value='${processCode}'/>
            <input type="hidden" name="hidfslYear" id="hidfslYear" value='${fslYear}'/>
            <input type="hidden" name="spc" id="hidspc" value='${spc}'/>
            
            <table>
                <tr>
                    <td>
                        <button type="submit">Search</button>
                    </td>
                </tr>
            </table>
        </form>
    </body>
</html>
