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
        <link rel="stylesheet" type="text/css" href="resources/css/bootstrap.min.css" />
        <style type="text/css">
            h1{font-size:15pt;font-weight:bold;margin-bottom:10px;}
            #training_form td{padding:6px;}
            .form-control{height:30px;}
            #table_cadre tr td{padding:4px;}
        </style>
        <script type="text/javascript">
            $(document).ready(function () {
                $.ajax({
                    url: 'getPreviousTrainingList.htm',
                    type: 'get',
                    data: 'trainingId=100',
                    success: function (retVal) {
                        $('#previous_training').html(retVal);
                    }
                });
            });
            function selectCadre(cID, grade, gradeId, ele)
            {
                var isChecked = 'N';
                if (ele.checked)
                {
                    $('#li_' + cID+'_'+gradeId).css('background', '#FFF0AF');
                    isChecked = 'Y';
                }
                else
                {
                    $('#li_' + cID+'_'+gradeId).css('background', '#FFFFFF');
                }
                                $.ajax({
                    url: 'AssignCadreListAction.htm',
                    data: 'cadreId='+cID+'&trainingId='+$('#trainingId').val()+'&date='+$('#programDate').val()+'&status='+isChecked+'&grade='+grade,
                    type: 'get',
                    success: function (retVal) {
                    }
                });
            }
            function  checkSelected(chkValue)
            {
                if (chkValue == 99999)
                {
                    for (var i = 0; i < document.getElementById('frmTraining').elements['tp_ids[]'].length; i++)
                    {
                        if (document.getElementById('frmTraining').elements['tp_ids[]'][i].value != 99999)
                            document.getElementById('frmTraining').elements['tp_ids[]'][i].checked = false;
                    }
                }
                else
                {
                    for (var i = 0; i < document.getElementById('frmTraining').elements['tp_ids[]'].length; i++)
                    {
                        if (document.getElementById('frmTraining').elements['tp_ids[]'][i].value == 99999)
                            document.getElementById('frmTraining').elements['tp_ids[]'][i].checked = false;
                    }
                }
            }   
function savePreviousTraining()
            {
                hasSelected = false;
                strIds = "";
                     if (document.getElementById('frmTraining').elements['tp_ids[]'])
                    {
                        for (var i = 0; i < document.getElementById('frmTraining').elements['tp_ids[]'].length; i++)
                        {
                            if (document.getElementById('frmTraining').elements['tp_ids[]'][i].checked)
                            {
                                hasSelected = true;
                                break;
                            }
                        }
                    }
                    if (!hasSelected)
                    {
                        alert("Please select your Previous Training. If you did not take any. Please select 'None of the Above' Option at the very bottom.");
                        return false;
                    }
                    else
                    {
                        for (var i = 0; i < document.getElementById('frmTraining').elements['tp_ids[]'].length; i++)
                        {
                            if (document.getElementById('frmTraining').elements['tp_ids[]'][i].checked)
                            {
                                strIds += (strIds == '') ? document.getElementById('frmTraining').elements['tp_ids[]'][i].value : (',' + document.getElementById('frmTraining').elements['tp_ids[]'][i].value);
                            }
                        }
                    }
                    if (strIds)
                    {
                        document.getElementById('tpids').value = strIds;
                    }
                    if(confirm("Are you sure you want to save the selected Programs in your Previous Training List?"))
                    {
                    $('#frmTraining').form('submit', {
                    url: 'savePreviousTraining.htm',
                    success: function(result) {
                        var result = eval('(' + result + ')');
                        if (result.message) {
                            $.messager.show({
                                title: 'Error',
                                msg: result.message
                            });
                        } else {
parent.window.closeIframe();
                        }
                    }
                });      }              
            }            
        </script>
    </head>
    <body>
        <form id="frmTraining" class="form-control-inline"  action="AssignCandreAction.htm" method="POST" commandName="CadreForm">
            
            <div id="previous_training" style="width:100%;font-size:9pt;">

            </div>
            <p align="center"><input type="button" value="Save & Close Window" class="btn btn-primary btn-sm" onclick="javascript: return savePreviousTraining();" /></p>
        </form>
    </body>
</html>
