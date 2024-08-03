<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Update Employee Previous Training</title>

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
            .tblres td{padding:5px;}
        </style>
        <script type="text/javascript">
            function saveEmpPreviousTraining()
            {
                trainingId = $('#training_id').val();
                hrmsId = $('#hrms_id').val();
                if (hrmsId == '')
                {
                    alert("Please select HRMS Id.");
                    $('#hrms_id')[0].focus();
                    return false;
                }
                if (trainingId == '')
                {
                    alert("Please select Training.");
                    $('#training_id')[0].focus();
                    return false;
                }

                $('#loader').css('display', 'block');
                $('#btn_search').css('display', 'none');
                $.ajax({
                    url: 'saveEmpPreviousTraining.htm',
                    type: 'get',
                    data: 'trainingId=' + trainingId + '&hrmsId=' + hrmsId,
                    success: function(retVal) {
                        $('#result_blk').html(retVal);
                        $('#loader').css('display', 'none');
                        $('#btn_search').css('display', 'block');
                $('#training_id').val("");
                $('#hrms_id').val("");                        
                    }
                });
            }


        </script>
    </head>
    <body>
        <div style="width:90%;margin:0px auto;"><h1 style="font-size:18pt;margin:0px;">Update Employee Previous Training</h1></div>
        <form:form class="form-control-inline"  action="SearchEmployee.htm" method="POST" commandName="EmployeeSearch">
            <input type="hidden" name="facultyCode" id="facultyCode" value="0" />
            <input type="hidden" name="opt" id="opt" value="" />
            <table width="90%" align="center" class="training_form" cellspacing="1" cellpadding="4" border="0" bgcolor="#EAEAEA" style="border:1px solid #CCCCCC;font-size:10pt;margin-top:10px;">

                <tr>
                    <td align="right" width="120">HRMS ID:</td>
                    <td width="200"><input type="text" name="hrms_id" id="hrms_id" class="form-control-inline" /></td>
                    <td align="right" width="120">Training:</td>
                    <td><select name="training_id" id="training_id" size="1">
                            <option value="">-Select Training Program-</option>
                            <c:forEach items="${trainingList}" var="tList">
                                <option value="${tList.trainingProgramID}">${tList.programName} [${tList.instituteName}, ${tList.venue}] - ${tList.financialYear}</option>
                            </c:forEach>
                        </select></td>
                </tr>                        
                <tr>
                    <td colspan="4" align="center">
                        <input type="button" id="btn_search" value="Save Employee Training" name="save" class="btn btn-primary btn-sm" onclick="javascript: saveEmpPreviousTraining()" />
                        <span id="loader" style="display:none;"><img src="images/ajax-loader_1.gif" /> <span style="color:#666666;font-size:9pt;font-style:italic;">Please wait...</span></span>
                    </td>
                </tr>
            </table>
        </form:form>
        <div id="result_blk">

        </div>
    </div>
</div>

</body>
</html>
