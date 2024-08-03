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
        </style>
        <script type="text/javascript">
            $(document).ready(function () {
                $.ajax({
                    url: 'GetAssignedSponsorList.htm',
                    type: 'get',
                    data: 'trainingId='+$('#trainingId').val(),
                    success: function (retVal) {
                        $('#sponsor_list').html(retVal);
                    }
                });
            });
            function selectSponsor(cID, ele)
            {
                var isChecked = 'N';
                if (ele.checked)
                {
                    $('#li_' + cID).css('background', '#FFF0AF');
                    isChecked = 'Y';
                }
                else
                {
                    $('#li_' + cID).css('background', '#FFFFFF');
                }
                                $.ajax({
                    url: 'AssignSponsorListAction.htm',
                    data: 'sponsorCode='+cID+'&trainingId='+$('#trainingId').val()+'&status='+isChecked,
                    type: 'get',
                    success: function (retVal) {
                        
                    }
                });
            }
        </script>
    </head>
    <body>
        <form:form class="form-control-inline"  action="AssignFacultyAction.htm" method="POST" commandName="FacultyForm" onsubmit="return validateAdd()">
            <input type="hidden" name="trainingId" id="trainingId" value="${trainingId}" />
            
            <div id="sponsor_list">

            </div>
            <p align="center"><input type="button" value="Close Window" class="btn btn-primary btn-sm" onclick="javascript: window.parent.closeIframe();" /></p>
        </form:form>
    </body>
</html>
