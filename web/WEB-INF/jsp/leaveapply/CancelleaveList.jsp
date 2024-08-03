<%@ page contentType="text/html;charset=windows-1252" session="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <head>        
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
                    function searchLeaveData() {
                    if($('#criteria').val() == ""){
                    alert("Please enter search criteria");
                            $('#criteria').focus();
                            return false;
                    } if ($('#searchString').val() == ""){
                    alert("Please enter search string");
                            $('#searchString').focus();
                            return false;
                    }

                    }
        </script>
    </head>
    <body>


        <div id="page-wrapper">
            <form:form action="cancelleavelist.htm" commandName="leave" method="post" >
                <div class="container-fluid">
                    <div >
                        <label class="control-label col-sm-3">Search criteria</label>
                        <div class="col-sm-3">

                            <form:select class="form-control" id="criteria" path="criteria">
                                <form:option value="">select</form:option>
                                <form:option value="HRMSID">HRMS ID</form:option>
                                <form:option value="TASKID">TASK ID</form:option>
                            </form:select>
                        </div>
                        <label class="control-label col-sm-3">Search String</label>
                        <div class="col-sm-3" >
                            <form:input path="searchString" id="searchString" class="form-control"  />
                        </div>
                    </div>
                </div> 
                <input type="submit" value="Search" name="Search" onclick="return searchLeaveData()"/>
                <div class="panel-footer">
                    <div class="table-responsive">
                        <div class="table-responsive">
                            <table class="table table-bordered table-hover table-striped">
                                <thead>

                                    <tr>
                                        <th width="10%">Sl. No.</th>
                                        <th width="10%">Task Id</th>
                                        <th width="10%">Type of Leave</th>
                                        <th width="10%">Apply <br>From Date</th>
                                        <th width="10%">Apply <br>To Date</th>
                                        <th width="10%">Approve <br>From Date</th>
                                        <th width="10%">Approve <br>To Date</th>
                                        <th width="10%">Status</th>
                                        <th width="10%">Edit</th>
                                        <th width="10%">Delete</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:if test="${not empty searchleavelist}">
                                        <c:forEach items="${searchleavelist}" var="searchleavelist" varStatus="count">

                                            <tr>
                                                <td><c:out value="${count.index + 1}"/></td>
                                                <td>${searchleavelist.taskId}</td>
                                                <td>${searchleavelist.sltleaveType}</td>                                               
                                                <td> ${searchleavelist.txtperiodFrom}</td>                                                
                                                <td> ${searchleavelist.txtperiodTo}</td>
                                                <td> ${searchleavelist.txtApproveFrom}</td>                                                
                                                <td> ${searchleavelist.txtApproveTo}</td>
                                                <td>${searchleavelist.status}</td>
                                                <td><a href="editLeaveData.htm?taskId=${searchleavelist.taskId}&criteria=${leave.criteria}&searchstring=${leave.searchString}">Edit</a></td>  
                                                <td><a href="deleteLeaveData.htm?taskId=${searchleavelist.taskId}&criteria=${leave.criteria}&searchstring=${leave.searchString}" onclick="return confirm('Are you sure to Delete ?')">Delete</a></td>  
                                            </tr>
                                        </c:forEach>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                     </div>

                </form:form>
           
        </div>

    </body>
</html>


