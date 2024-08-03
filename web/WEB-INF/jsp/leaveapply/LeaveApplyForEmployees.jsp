

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" type="text/css" href="css/bootstrap.css"/>
        <link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap4.min.css"/>
        <link rel="stylesheet" type="text/css" href="css/select.bootstrap.min.css"/>
        <script type="text/javascript"  src="js/jquery-1.12.4.js"></script>
        <script type="text/javascript"  src="js/jquery.dataTables.min.js"></script>
        <script type="text/javascript"  src="js/dataTables.bootstrap4.min.js"></script>
        <script language="javascript" src="js/dataTables.select.min.js" type="text/javascript"></script>
        <script language="javascript" type="text/javascript" >
            $(document).ready(function () {
                //$('#example').DataTable();
                var table = $('#example').DataTable({
                    scrollY: "500px",
                    scrollCollapse: true,
                    paging: true,
                    responsive: true,
                    select: true

                });
                $('#example tbody').on('click', 'tr', function () {
                    var rowData = table.row(this).data();
                    $("#hidempid").val(rowData[0]);
                });
               
            });
            function applyleave() {
                var hidempid = $("#hidempid").val();
               if(hidempid==''){
                   alert("Please select an employee to apply leave");
               }else{
                    location.href = "leaveapply.htm?empId=" + hidempid;
               }
               
            }
        </script>
    </head>
    <body class="boxed-layout pt-40 pb-40 pt-sm-0">
        <form:form action="leaveapplyforemp.htm" method="POST" commandName="leaveForm">
            <div class="main-content">
                <input type="hidden" id="hidempid" name="hidempid"/>
            </div>
            <div class="pull-right btn-group btn-group-sm" style="margin-bottom: 5px">
                <a href="javascript:applyleave()" >
                    <button class="btn btn-info" type="button">
                        Apply 
                    </button>
                </a>
            </div>
            <div data-spy="scroll" data-target="#list-example" data-offset="0" class="scrollspy-example">
                <table id="example" class="table table-striped" width="100%" cellspacing="0">
                    <thead class="table-success">
                        <tr>
                            <th>HRMS ID</th>
                            <th>Employee Name</th>
                            <th>Post</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach  var="emplist" items="${empList}">
                            <tr>
                                <td>${emplist.empid}</td>
                                <td>${emplist.intitals}&nbsp;${emplist.fname}&nbsp;${emplist.mname}&nbsp;${emplist.lname}</td>
                                <td>${emplist.post}</td>
                            </tr>  
                        </c:forEach>
                </table>
            </div>
        </form:form>
    </body>
</html>
