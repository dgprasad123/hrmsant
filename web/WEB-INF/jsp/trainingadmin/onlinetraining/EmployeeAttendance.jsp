<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Manage Online Institutes</title>

        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">

        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript" src="js/datagrid-detailview.js"></script>
        <link rel="stylesheet" type="text/css" href="resources/css/bootstrap.min.css" />
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>

        <style type="text/css">
            .institute_form td{padding:6px;}
            .form-control{height:30px;}
            body{margin:0px;font-family: 'Arial', sans-serif;background:#2A3F54}
            #left_container{background:#2A3F54;width:18%;float:left;min-height:700px;color:#FFFFFF;font-size:15pt;font-weight:bold;}
            #left_container ul{list-style-type:none;margin:0px;padding:0px;}
            #left_container ul li a{display:block;color:#EEEEEE;font-weight:normal;font-size:10pt;text-decoration:none;padding:10px 0px;padding-left:15px;}
            #left_container ul li a:hover{background:#465F79;color:#FFFFFF;}
            #left_container ul li a.sel{display:block;color:#EEEEEE;background:#367CAD;font-weight:normal;font-size:10pt;text-decoration:none;padding:10px 0px;padding-left:15px;}            
            table {border:1px solid #DADADA;}
            .tblres td{padding:5px;}
            .panel-header{background:#5593BC;color:#FFFFFF;}
            .panel-title{margin-bottom:5px;}
            .panel-body{font-size:15pt;}
            .datagrid-header{background:#EAEAEA;border-style:none;}
            .datagrid-header-row{font-weight:bold;}
            .datagrid-cell, .datagrid-cell-group, .datagrid-header-rownumber, .datagrid-cell-rownumber{font-size:10pt;}
        </style>

        <script type="text/javascript">

            function validateCalculate()
            {
                if ($('#employeeID').val() == '')
                {
                    alert("Please enter Employee ID.");
                    return false;
                }
                $.ajax({
                    url: "contractualPayFixationResult.htm?empId=" + $('#employeeID').val(),
                    success: function(result) {
                        $('#result_blk').html(result);
                        return false;
                    }});
                return false;
            }
        </script>
    </head>
    <body>
        <jsp:include page="Header.jsp">
            <jsp:param name="menuHighlight" value="INSTITUTES" />
        </jsp:include>
        <div style="width:100%;margin:0px auto;"><h1 style="font-size:18pt;margin:0px;margin-bottom:10px;">View Attendance</h1>

            <form:form class="form-control-inline"  action="EmployeeAttendance.htm" method="POST" commandName="EmpAttendanceBean">
                <table width="100%" class="institute_form" cellspacing="1" cellpadding="4" border="0" bgcolor="#EAEAEA" style="border:1px solid #CCCCCC;font-size:10pt;margin-top:10px;">
                    <tr>
                        <td align="right" width="15%"><span style="color:#FF0000;">*</span> Employee:</td>
                        <td><form:select path="employeeId" id="employeeId" class="form-control" style="width:95%;height:10%">
                                <form:option value=""> -- Select -- </form:option>
                                <form:options items="${empList}" itemValue="label" itemLabel="value"/>
                            </form:select></td>
                        <td></td>
                    </tr>  
                    <tr>
                        <td align="right" width="15%"><span style="color:#FF0000;">*</span> From Date:</td>
                        <td> <div class="input-group">
                                <input type="text" class="form-control" name="txtperiodFrom" id="txtperiodFrom" readonly="true" value="${fromdate}}"/>
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-time"></span>
                                </span>
                            </div>    
                        </td>
                        <td></td>
                    </tr> 
                    <tr>
                        <td>  <input type="submit" value="Search" name="save" class="btn btn-primary"/></td>
                    </tr>
                    <!--<tr>
                        <td align="right" width="15%"><span style="color:#FF0000;">*</span> To Date:</td>
                        <td>
                          <div class='input-group date' id='txtperiodTo'><input type="text" value="" id="txtperiodFrom" name="txtperiodTo" path="txtperiodTo" class="form-control" />
                                    </div> 
                            
                           </td>
                        <td></td>
                    </tr> -->
                </table>

                <div style="clear:both">&nbsp;</div>
            </form:form>

            <div id="result_blk">
                <h1 style="font-size:18pt;margin:0px;margin-bottom:10px;color:#008900;">Today's Attendance</h1>



                <table width='100%' class="table table-striped" cellspacing="5" cellpadding="5">
                    <tr>
                        <th>Sl No.</td>
                        <th>Name</td>
                        <th>In Punch Time</td>
                        <th>Out Punch Time</td>
                        <th>Working Hour</td>  
                        <th>View</th>
                    </tr>
                    <c:forEach var="eachBody" items="${todayList}" varStatus="slnoCnt">

                        <tr>
                            <td> ${slnoCnt.index+1}</th>

                            <td> ${eachBody.empName}</td>
                            <td> ${eachBody.inTime}</td>
                            <td><c:if test = "${eachBody.outTime ne eachBody.inTime}">
                                    ${eachBody.outTime}
                                </c:if>
                                &nbsp;
                            </td>
                            <td><c:if test = "${eachBody.outTime ne eachBody.inTime}">
                                    ${eachBody.workingHour}
                                </c:if>
                                &nbsp;
                            </td>
                            <td>
                                <a href="viewEmployeeAttendance.htm?empId=${eachBody.empId}" target="_blank"><img border="0" alt="PDF" src="images/view_icon.png" height="20"></a>                    
                            </td>
                        </tr>
                    </c:forEach>

                </table>
            </div>
        </div>
        <script type="text/javascript">
            $(function() {
                $('#txtperiodFrom').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            });
        </script>
    </body>
</html>