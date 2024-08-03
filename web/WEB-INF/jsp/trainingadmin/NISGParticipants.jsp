<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Training Calendar:: HRMS</title>
        <style type="text/css">
            body{margin:0px;font-family: 'Arial', sans-serif;background:#F7F7F7}
            #left_container{background:#2A3F54;width:18%;float:left;min-height:700px;color:#FFFFFF;font-size:15pt;font-weight:bold;}
            #left_container ul{list-style-type:none;margin:0px;padding:0px;}
            #left_container ul li a{display:block;color:#EEEEEE;font-weight:normal;font-size:10pt;text-decoration:none;padding:10px 0px;padding-left:15px;}
            #left_container ul li a:hover{background:#465F79;color:#FFFFFF;}
            #left_container ul li a.sel{display:block;color:#EEEEEE;background:#367CAD;font-weight:normal;font-size:10pt;text-decoration:none;padding:10px 0px;padding-left:15px;}
        </style>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <link rel="stylesheet" type="text/css" href="resources/css/bootstrap.min.css" />
        <style type="text/css">
            .window .window-header{background:#5593BC;}
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
            $(document).ready(function(){
                $('#loader').css('display', 'block');
                $.ajax({
                  url: 'GetNISGParticipants.htm',
                  type: 'get',
                  success: function(retVal) {
                       $('#participant_list').html(retVal); 
                       $('#loader').css('display', 'none');
                  }
                });                
            });
            function showNISGWindow(participantID)
            {
                window.open('NISGParticipantDetail.htm?participantID='+participantID, '', 'width=700,height=800,scrollbars=1,left=200');
            }
            </script>
    </head>
    <body>
        <jsp:include page="Header.jsp">
            <jsp:param name="menuHighlight" value="NISG" />
        </jsp:include>
                <h1 style="margin:0px;font-size:18pt;color:#333333;border-bottom:1px solid #DADADA;padding-bottom:5px;">View NISG Participant List</h1>
                
                <div id="participant_list">
                    <span align="center" id="loader" style="display:block;margin-top:100px;text-align:center;"><img src="images/big-loader.gif" alt="" /></span>
                </div>    
            </div>
        </div>
    </body>
</html>
