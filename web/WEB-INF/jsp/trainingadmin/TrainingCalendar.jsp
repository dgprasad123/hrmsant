<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
        <style type="text/css">
            #gCalendar td{width:40px;height:80px;vertical-align:top}
            .weekend{color:#FF0000;font-weight:bold;}
            .holiday{color:#FF0000;font-weight:bold;}
            .month_name{display:block;font-size:10pt;text-decoration:none;color:#222222;line-height:20px;text-align:left;padding:2px 10px;}
            .month_name:hover{background:#155890;color:#FFFFFF;}
            .event_exist{background:#9DD88E;font-weight:bold;font-size:12pt;}
            .event_title{position:relative;height:100%;cursor:pointer;}
            .event_popup{display:none;cursor:pointer;position:absolute;left:-10px;top:50px;background:#FFFFD8;border:1px solid #CCCCCC;padding:5px;width:175px;min-height:50px;font-size:8pt;font-weight:normal;color:#33333;z-index:100}
            .event_popup p{font-weight:bold;font-size:10pt;margin:0px;margin-bottom:5px;font-size:8pt;color:#555555;}
            .event_popup ul{list-style-type:none;margin:0px;padding:0px;}
            .event_popup ul li{background:url('images/arrow.png') no-repeat 2px 4px;padding-left:13px;margin-bottom:3px;}
        </style>
        <script type="text/javascript">
            var dateObj = new Date();
            var rmonth = dateObj.getUTCMonth(); //months from 1-12
            var ryear = dateObj.getUTCFullYear();

            function showCalendar()
            {
                $.ajax({
                    url: 'viewCalendar.htm',
                    type: 'get',
                    data: 'year=' + ryear + '&month=' + rmonth,
                    success: function (retVal) {
                        $('#calendar_blk').html(retVal);
$( ".event_title" ).hover(
  function() {
    elem = $(this).children().first();
    elem.fadeIn();
  }, function() {
    elem = $(this).children().first();
    elem.fadeOut();    
  }
);                        
                    }
                });
            }
            function displayMonths()
            {
                $('#month_blk').fadeIn();
                $('#year_blk').fadeOut();
            }
            function displayYears(intYear)
            {
                $('#year_blk').fadeIn();
                $('#month_blk').fadeOut();
            }
            function refreshCal(intMonth, rType)
            {
                if (rType == 'year')
                    ryear = intMonth;
                else
                    rmonth = intMonth;
                showCalendar();
                if (rType == 'year')
                    $('#year_blk').fadeOut();
                else
                    $('#month_blk').fadeOut();
            }
            function filterCalendar(intMonth, intYear)
            {
                rmonth = intMonth;
                ryear = intYear;
                showCalendar();
            }
            function openWindow(linkurl, modname) {
                $("#winfram").attr("src", linkurl);
                $("#win").window("open");
                $("#win").window("setTitle", modname);

            }

            $(document).ready(function () {
                showCalendar();

            });
            function closeIframe()
            {
                    $('#win').dialog('close');
                    showCalendar();
                    return false;             
            }
            function refreshCalendar()
            {
                showCalendar();
                return false;             
            }
        </script>        
    </head>
    <body>
                <jsp:include page="Header.jsp">
            <jsp:param name="menuHighlight" value="CALENDAR" />
        </jsp:include>
                <h1 style="margin:0px;font-size:18pt;color:#777777;border-bottom:1px solid #DADADA;padding-bottom:5px;">Training Calendar</h1>
                <div style="padding-top:10px">
                    <a href="TrainingCalendarList.htm" style="text-decoration:none;color:#333333;"><img src="images/listview.png" alt="" width="20" style="vertical-align:middle;" /> &nbsp;List view</a>&nbsp;&nbsp;&nbsp;
                    <a href="TrainingCalendar.htm" style="text-decoration:none;color:#333333;"><img src="images/calendarview.png" alt="" width="20" style="vertical-align:middle;" /> Calendar view</a>
                </div>
    <form:form  action="TrainingController.htm" method="POST" commandName="TrainingCalendarForm">
        <div id="calendar_blk"></div>
        <div  id="win" class="easyui-window" title="My Window" data-options="modal:true,closed:true,iconCls:'icon-window'" closed="true" style="width:80%;height:400px;padding:5px;">
            <iframe id="winfram"  frameborder="0" scrolling="yes" marginheight="0" marginwidth="0" height="100%" width="100%"></iframe>
        </div>
    </form:form>                
            </div>
        </div>
    </body>
</html>
