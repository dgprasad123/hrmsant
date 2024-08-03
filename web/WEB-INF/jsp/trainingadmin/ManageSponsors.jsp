<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Manage Sponsors</title>

        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">

        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript" src="js/datagrid-detailview.js"></script>
        <link rel="stylesheet" type="text/css" href="resources/css/bootstrap.min.css" />
        <style type="text/css">
            .training_form td{padding:6px;}
            .form-control{height:30px;}
            body{margin:0px;font-family: 'Arial', sans-serif;background:#F7F7F7}
            #left_container{background:#2A3F54;width:18%;float:left;min-height:700px;color:#FFFFFF;font-size:15pt;font-weight:bold;}
            #left_container ul{list-style-type:none;margin:0px;padding:0px;}
            #left_container ul li a{display:block;color:#EEEEEE;font-weight:normal;font-size:10pt;text-decoration:none;padding:10px 0px;padding-left:15px;}
            #left_container ul li a:hover{background:#465F79;color:#FFFFFF;}
            #left_container ul li a.sel{display:block;color:#EEEEEE;background:#367CAD;font-weight:normal;font-size:10pt;text-decoration:none;padding:10px 0px;padding-left:15px;}            
                        table {border:1px solid #DADADA;}
            .panel-header{background:#5593BC;color:#FFFFFF;}
            .panel-title{margin-bottom:5px;}
            .panel-body{font-size:15pt;}
            .datagrid-header{background:#EAEAEA;border-style:none;}
            .datagrid-header-row{font-weight:bold;}
            .datagrid-cell, .datagrid-cell-group, .datagrid-header-rownumber, .datagrid-cell-rownumber{font-size:10pt;}
        </style>
        <script type="text/javascript">
            function validateAdd()
            {
                $('#sponsorName').val($('#sponsorName').val().trim());
                if ($('#sponsorName').val().trim() == '')
                {
                    alert("Please enter Sponsor Name.");
                    $('#sponsorName').focus();
                    return false;
                }
            }
            $(document).ready(function () {

                $('#dg').datagrid({
                    url: "getSponsorList.htm"
                });
            });

        </script>
    </head>
    <body>
                <jsp:include page="Header.jsp">
            <jsp:param name="menuHighlight" value="SPONSORS" />
        </jsp:include>
                <h1 style="margin:0px;font-size:18pt;color:#333333;border-bottom:1px solid #DADADA;padding-bottom:5px;">Manage Sponsors</h1>
                <form:form class="form-control-inline"  action="AddTrainingSponsor.htm" method="POST" commandName="TrainingSponsorForm">
                    <input type="hidden" name="sponsorCode" id="sponsorCode" value="0" />
                    <input type="hidden" name="opt" id="opt" value="" />
                    <table width="100%" class="training_form" cellspacing="1" cellpadding="4" border="0" bgcolor="#EAEAEA" style="border:1px solid #CCCCCC;font-size:10pt;margin-top:10px;">
                        <tr>
                            <td align="right" width="25%">Sponsor Name:</td>
                            <td><input type="text" name="sponsorName" id="sponsorName" size="50" class="form-control-inline" /></td>
                        </tr> 
                        <tr>
                            <td colspan="2" align="center"><input type="submit" value="Save Sponsor" name="save" class="btn btn-primary btn-sm" onclick="return validateAdd()" />
                                <input type="button" value="Cancel" name="close" class="btn btn-primary btn-sm" onclick="self.location = 'ManageSponsors.htm'" />
                            </td>
                        </tr>                       
                    </table>
                </form:form>
                <table id="dg" class="easyui-datagrid" style="width:100%;height:350px;" title="Manage Sponsors" 
                       data-options="rownumbers:true,fitColumns:true,singleSelect:true,collapsible:true,method:'get',pagination: true" toolbar="#toolbar">
                    <thead>
                        <tr>
                            <th data-options="field:'sponsorName',width:'50%'" style="font-weight:bold;">Sponsor Name</th>
                            <th data-options="field:'editSponsor',width:'3%',formatter:editSponsor" align='center'></th>                        
                            <th data-options="field:'deleteSponsor',width:'3%',formatter:deleteSponsor" align='center'></th>
                        </tr> 
                    </thead>
                </table>
            </div>
        </div>
        <script type="text/javascript">
            function editSponsor(val, row)
            {
                str1 = '<a href="javascript:void(0)" onclick="javascript: editSponsorAction(' + row.sponsorCode + ', \'' + row.sponsorName + '\')" title="Edit Sponsor"><img src="images/edit.png" alt="Edit Sponsor" /></a>';
                return str1;
            }
            function deleteSponsor(val, row)
            {
                str1 = '<a href="javascript:void(0)" onclick="javascript: deleteSponsorAction(' + row.sponsorCode + ')" title="Delete Sponsor"><img src="images/delete_icon.png" alt="Delete Sponsor" /></a>';
                return str1;
            }
            function deleteSponsorAction(sponsorID)
            {
                if (confirm("Are you sure you want to delete the Sponsor?"))
                {
                    $.ajax({
                        type: 'GET',
                        url: 'DeleteSponsor.htm?sponsorCode=' + sponsorID,
                        success: function (response) {
                            $('#dg').datagrid('reload');
                        }
                    });
                }
            }

            function editSponsorAction(sponsorCode, sponsorName)
            {
                $('#sponsorName').val(sponsorName);
                $('#sponsorCode').val(sponsorCode);
            }
        </script>         
    </body>
</html>
