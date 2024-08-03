<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>

        <style>
            .clearfix::after {
                content: "";
                clear: both;
                display: table;
            }
            
            
            .end-btn {
  margin-top: 0vw;
  background-color: #DAA521;
  border: none;
  width: 9vw;
  height: 2vw;
  font-size: 14px;
  color: #FFFFFF;
  border-radius: 0.5vw;
  font-weight: bold;
  cursor: pointer;
}
        </style>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
         <script src="js/moment.js" type="text/javascript"></script>
           <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>

        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript" src="js/chosen.jquery.min.js"></script>

        <script type="text/javascript">
            function callNoImage(imgId) {
                var userPhoto = document.getElementById(imgId);
                userPhoto.src = "images/NoEmployee.png";

            }
            $(function() {
                $('#txtperiodFrom').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#txtperiodTo').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            });
        </script>
    </head>
    <body>
        <div class="container-fluid">

            <div class="row">
                <div class="col-lg-12">
                    <h2 align="center">
                        Superannuation Projection Report
                    </h2>
                </div>
            </div>
            <hr />
            <form:form action="ViewSupperannuationProjectionReport.htm" commandName="command">
              <!--  <div class="row">
                    <div class="col-lg-3">

                    </div>
                    <div class="col-lg-4">
                        <form:select path="sltYear" class="form-control">
                            <form:option value=""> Select Year </form:option>
                            <form:options itemLabel="label" itemValue="value" items="${yearList}"/>
                        </form:select>
                    </div>
                    <div class="col-lg-1">
                        <button type="submit" class="form-control btn btn-danger">View</button>
                    </div>
                    <div class="col-lg-2">

                    </div>
                </div>
                OR-->

                <div class="row" >
                    <div class="col-lg-1"  style="left:50px;">From Date:</div>

                    <div class="col-lg-2" >

                        <div style="left:50px;" class='input-group date' >

                            <form:input class="form-control" path="txtperiodFrom" id="txtperiodFrom" readonly="true"/>
                            <span class="input-group-addon">
                                <span class="glyphicon glyphicon-time"></span>
                            </span>
                        </div>
                    </div>
                    <div class="col-lg-1" style="left:50px;" >To Date:</div>
                    <div class="col-lg-2" >
                        <div class='input-group date' >
                            <form:input class="form-control" path="txtperiodTo" id="txtperiodTo" readonly="true"/>
                            <span class="input-group-addon">
                                <span class="glyphicon glyphicon-time"></span>
                            </span>
                        </div>
                    </div>
                    <div class="col-lg-2" >
                        <input type="submit" name="action" value="Search" class="btn btn-success"/>
                    </div>
                </div> 


            </form:form>  
        </div>

        <div class="panel-body">
            <table class="table table-bordered" width="100%">
                <thead>
                    <tr>
                        <th width="1%">Sl No</th>
                        <th >Photo</th>                               
                        <th>Name</th>  
                        <th>Contact No</th> 
                        <th >Account</th>                       
                        <th >Joining Date</th>                       
                        <th >DOS</th>
                        <th >&nbsp;</th>

                    </tr>
                </thead>
                <c:forEach items="${emplist}" var="list" varStatus="count">
                    <tr>
                        <td><c:out value="${count.index + 1}"/></td>
                        <td>
                            <img src="displayprofilephoto.htm?empid=${list.empid}" id="sbUserPhoto${count.index + 1}" onerror="callNoImage('sbUserPhoto${count.index + 1}')"  height="100px" border="2"/>
                        </td>
                        <td><strong>${list.empname}</strong><br/>
                            <span class="text-success"> ${list.designation}<br/></span>
                            <strong  class="text-info">HRMS Id:${list.empid}</strong>
                        </td>
                        <td><strong  class="text-success">${list.mobile}</strong></td>   
                        <td>(${list.accountType})&nbsp;${list.gpfNo}</td>                        
                        <td>${list.dateOfJoining}</td>                      
                        <td> <strong  class="text-warning">${list.dateOfSuperannuation}</strong></td>
                        <td><input type="button" value="Push SMS" name='push' class='btn btn-danger'/><br><br>
                            <button class="end-btn"><a href="employeeAbstractReport.htm?empid=${list.empid}" target="_blank">ABSTRACT REPORT &nbsp;&nbsp;> </a></button>
                        </td>
                    </tr>

                </c:forEach>
            </table> 
        </div>

    </body>
</html>
