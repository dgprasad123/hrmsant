<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script src="js/bootstrap-datetimepicker.js" type="text/javascript"></script>
        <script type="text/javascript">

            function validateForm()
            {
                if ($('#amonth').val() == '')
                {
                    alert("Please select Month.");
                    return false;
                }
                if ($('#documentFile').val() == '')
                {
                    alert("Please attach a valid Excel File.");
                    return false;
                }
                var ext = $('#documentFile').val().split('.').pop();
                if (ext != 'xls')
                {
                    alert("Please attach a valid Excel (.xls) File.");
                    return false;
                }
                if (confirm("Are you sure you want to upload the Excel file?"))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            /*$(document).ready(function(){
             if($('#isExisting').val() == 'true')
             {
             $('.container-fluid').css('display', 'none'); 
             $('.container-fluid1').css('display', 'block'); 
             }
             }); */
            function viewExcel()
            {
                self.location = 'ImportFailure.htm?month=' + $('#amonth').val() + '&year=' + $('#ayear').val();
            }
            var ext = $('#documentFile').val().split('.').pop();
            if(ext != 'xls')
            {
                alert("Please attach a valid Excel (.xls) File.");
                return false;
            } 
            if(confirm("Are you sure you want to upload the Excel file?"))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
           /*$(document).ready(function(){
              if($('#isExisting').val() == 'true')
              {
                 $('.container-fluid').css('display', 'none'); 
                 $('.container-fluid1').css('display', 'block'); 
              }
           }); */
    function viewExcel()
    {
        self.location = 'ImportFailure.htm?month='+$('#amonth').val()+'&year='+$('#ayear').val();
    }
        </script>
    </head>
    <body>
        <div class="container-fluid">
            <form:form class="form-horizontal" action="UploadAbsenteeExcel.htm" enctype="multipart/form-data" method="POST" commandName="ExcelImport" onsubmit="javascript: return validateForm()">
                <input type="hidden" name="isExisting" id="isExisting" value="${isExisting}" />
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h1 style="margin:0px;font-size:15pt;">Manage Employee Attendance</h1>             
                    </div>
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-lg-6">

                                <div class="form-group">
                                    <label class="control-label col-sm-3" for="amonth">Month:</label>
                                    <div class="col-sm-3">
                                        <form:select path="amonth" id="amonth" class="form-control" >
                                            <form:option value="">--Select One--</form:option>
                                            <form:option value="0">Jan</form:option>
                                            <form:option value="1">Feb</form:option>
                                            <form:option value="2">Mar</form:option>
                                            <form:option value="3">Apr</form:option>
                                            <form:option value="4">May</form:option>
                                            <form:option value="5">Jun</form:option>
                                            <form:option value="6">Jul</form:option>
                                            <form:option value="7">Aug</form:option>
                                            <form:option value="8">Sep</form:option>
                                            <form:option value="9">Oct</form:option>
                                            <form:option value="10">Nov</form:option>
                                            <form:option value="11">Dec</form:option>
                                        </form:select>                                        
                                    </div>
                                    <label class="control-label col-sm-3" for="ayear">Year:</label>
                                    <div class="col-sm-3">
                                        <form:select path="ayear" id="ayear" class="form-control">
                                            <form:options items="${yearList}" itemValue="label" itemLabel="value"/>
                                        </form:select>                                        
                                    </div>                                    
                                </div>                                    
                                <div class="form-group">
                                    <label class="control-label col-sm-3">Attach Excel:</label>
                                    <div class="col-sm-9">
                                        <input type="file" name="documentFile" id="documentFile" />
                                        <span style="color:#FF0000;font-weight:bold;">(Please attach Excel (.xls) file only)</span>
                                    </div>

                                </div> 
                                <div class="form-group">
                                    <label class="control-label col-sm-3"></label>
                                    <div class="col-sm-9">
                                        <img src="images/excel_homeguard_sample.jpg" alt="" /><br />
                                        <span style="color:#008900;font-weight:bold;">N.B. Please prepare the excel as per the above screenshot.<br /><br />You can upload excel once per month. So Please verify your file before uploading.</span>
                                    </div>

                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="panel-footer">
                        <input type="submit" class="btn btn-default" name="action" value="Save"/>
                        <c:if test="${command.isupdated > 0}">
                            <input type="submit" class="btn btn-default" style="background:#890000;color:#FFFFFF;" name="action" value="Delete" onclick="javascript: return deleteAdInfo()" />
                        </c:if>
                        <input type="button" class="btn btn-default" name="action" value="Cancel"/>
                        <input type="button" class="btn btn-default" name="view" style="background:#00799B;color:#FFFFFF;" value="View Uploaded Excel" onclick="javascript: viewExcel()"/>
                    </div>
                </div>
            </form:form>
        </div>

        <div class="container-fluid1" style="display:none;">
            <div class="panel panel-default">
                <div class="panel-heading">
                </div>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-lg-12">
                            <h2 align="center" style="color:#FF0000;font-weight:bold;">You have already imported the Excel for this Month.</h2>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </body>
</html>
