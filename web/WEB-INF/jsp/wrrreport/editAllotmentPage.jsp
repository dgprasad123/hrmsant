<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>        
        <link rel="stylesheet" type="text/css" href="css/jquery.datetimepicker.css"/>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script language="javascript" src="js/jquery.datetimepicker.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/jquery.colorbox-min.js"></script>

        <!-- Custom CSS -->
        <link href="css/sb-admin.css" rel="stylesheet">

        <!-- Bootstrap Core JavaScript -->
        <script src="js/bootstrap.min.js"></script>

        <script type="text/javascript">       // allotment                 
            $(document).ready(function () {
                
            });
            
        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="panel panel-default">
                        <div class="panel-header">Quarter Details</div>
                        <form:form class="form-horizontal" action="saveQuarterAllotment.htm" method="POST"  commandName="empQuarterBean">
                            
                            <div class="panel-body">
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Unit/Area</label>
                                    <div  class="col-2">${empQuarterBean.qrtrunit}</div >
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Quarter Type</label>
                                    <div  class="col-2">${empQuarterBean.qrtrtype}</div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Quarter No</label>
                                    <div  class="col-2">${empQuarterBean.quarterNo}</div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">HRMS Id</label>
                                    <div  class="col-2"><b>${empQuarterBean.empId}</b></div>
                                </div> 
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Employee Name</label>
                                    <div  class="col-2"><b>${empQuarterBean.empName}</b>, ${empQuarterBean.designation}</div>
                                </div>                                
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Mobile No</label>
                                    <div  class="col-sm-2">
                                        <form:hidden path="qaId"/>
                                        <form:hidden path="empId"/>
                                        <form:hidden path="mobileno"/>
                                        <form:hidden path="qrtrunit"/>
                                        <form:hidden path="qrtrtype"/>
                                        ${empQuarterBean.mobileno}
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="control-label col-sm-2">New HRMS ID</label>
                                    <div  class="col-sm-2"><form:input path="newempId" class="form-control" maxlength="8"/></div>
                                </div>                                
                            </div>
                            <div class="panel-footer">                                
                                <input type="submit" name="action" class="btn btn-primary windowbtn" value="Back"/> 
                                <input type="submit" name="action" class="btn btn-primary windowbtn" value="Save"/>                                
                            </div>
                        </form:form>
                        
                    </div>                    

                </div>
            </div>
        </div>
    </body>
</html>
