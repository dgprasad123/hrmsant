<%-- 
    Document   : EmpListForConclusionProceeding
    Created on : 14 Dec, 2020, 3:44:12 PM
    Author     : Manisha
--%>


<%@page contentType="text/html" pageEncoding="UTF-8" autoFlush="true" buffer="64kb"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">                
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>       
        <script type="text/javascript">
            $(document).ready(function() {
                $("#deptName").change(function() {
                    $('#offcode').empty();
                    var url = 'getOfficeListJSON.htm?deptcode=' + this.value;
                    $.getJSON(url, function(result) {
                        $.each(result, function(i, field) {
                            $('#offcode').append($('<option>', {
                                value: field.offCode,
                                text: field.offName
                            }));

                        });
                    });
                });
                $(".loader").hide();
                $.post("GetFiscalYearListJSON.htm", function(data) {
                    for (var i = 0; i < data.length; i++) {
                        $('#fiscalyear').append($('<option>', {value: data[i].fy, text: data[i].fy}));
                    }
                });
            });
            function addEmployeeToConclusionProceeding(me, empid, empSpcForProceeding) {
                var url = "addConclusionProceedingEmpList.htm";
                $.post(url, {empid: empid, empSpcForProceeding: empSpcForProceeding})
                        .done(function(data) {
                            console.log($(me).html());
                            $(me).parent().html("<span>Added</span>");
                        });

            }
            function myFunction() {
                // Declare variables
                var input, filter, table, tr, td, i, txtValue;
                input = document.getElementById("myInput");
                filter = input.value.toUpperCase();
                table = document.getElementById("myTable");
                tr = table.getElementsByTagName("tr");

                // Loop through all table rows, and hide those who don't match the search query
                for (i = 0; i < tr.length; i++) {
                    td = tr[i].getElementsByTagName("td")[1];
                    td1 = tr[i].getElementsByTagName("td")[2];
                    td2 = tr[i].getElementsByTagName("td")[3];
                    td3 = tr[i].getElementsByTagName("td")[4];
                    if (td) {
                        txtValue = td.textContent || td.innerText;
                        txtValue = txtValue + (td1.textContent || td1.innerText);
                        txtValue = txtValue + (td2.textContent || td2.innerText);
                        txtValue = txtValue + (td3.textContent || td3.innerText);
                        if (txtValue.toUpperCase().indexOf(filter) > -1) {
                            tr[i].style.display = "";
                        } else {
                            tr[i].style.display = "none";
                        }
                    }
                }
            }
            function openOfficeList() {
                $('#myModal').modal('show');
            }
            function selectOffice() {
                var selectedOffice = $("#offcode option:selected").val();
                var selectedOffName = $("#offcode option:selected").text();
                alert(selectedOffName);
                if (selectedOffice != "") {
                    $("#offName").val(selectedOffName);
                    $("#offCode").val(selectedOffice);
                }
                $('#myModal').modal('hide');
            }
        </script>   
        <style type="text/css">
            #myInput {
                background-image: url('/images/searchicon.png'); /* Add a search icon to input */
                background-position: 10px 12px; /* Position the search icon */
                background-repeat: no-repeat; /* Do not repeat the icon image */
                width: 100%; /* Full-width */
                font-size: 16px; /* Increase font-size */
                padding: 12px 20px 12px 40px; /* Add some padding */
                border: 1px solid #ddd; /* Add a grey border */
                margin-bottom: 12px; /* Add some space below the input */
            }
        </style>
    </head>
    <body>
        <form:form action="ConclusionProceedingsEmpList.htm" method="POST" commandName="conclusionProceedingsForm" class="form-horizontal">

            <div class="panel panel-default">
                <div class="panel-heading">Employee List</div>
                <div class="panel-body" style="height: 550px;overflow: auto;">
                    <div class="row">
                        <div class="col-6">
                            <input type="text" id="myInput" onkeyup="myFunction()" placeholder="Search.....">
                        </div>
                        <div class="col-4">
                            <input type="button" value="Other Office" onclick="openOfficeList()"/>
                        </div>
                    </div>
                    <div class="table-responsive">
                        <table class="table table-bordered table-hover table-striped" id="myTable">
                            <thead>
                                <tr>                                            
                                    <th width="3%">#</th>
                                    <th>Employee Id</th>
                                    <th>GPF/PRAN NO</th>
                                    <th>Employee Name</th>
                                    <th>Designation</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>                                        
                                <c:forEach items="${emplistForConclusionProceeding}" var="emplist" varStatus="count">
                                    <tr>                                                
                                        <td>${count.index + 1}</td>
                                        <td>${emplist.empid}</td>
                                        <td>${emplist.gpfNo}</td>
                                        <td>${emplist.empNameForProceeding}</td>
                                        <td>${emplist.empPostForProceeding}</td>
                                        <td>
                                            <%--  <c:if test="${emplist.alreadyAdded eq 'Y'}">
                                                <span>Already Added</span>
                                            </c:if>
                                            <c:if test="${emplist.alreadyAdded eq 'N'}"> --%>
                                            <%-- <button type="button" onclick="addEmployeeToConclusionProceeding(this, '${emplist.empid}', '${emplist.empSpcForProceeding}')" class="btn btn-default">Add</button> --%>
                                            <a href="DisciplinaryProceedingList.htm?empid=${emplist.empid}" class="btn-default" ><button type="button" class="btn btn-primary">Backlog Entry</button></a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="panel-footer">
                     <input type="submit" name="action" class="btn btn-default" value="Back"/>                     
                </div>
            </div>

            <div id="myModal" class="modal fade" role="dialog">
                <div class="modal-dialog  modal-lg">

                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Choose Other Office</h4>
                        </div>
                        <div class="modal-body">
                            <form class="form-horizontal">
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Search By</label>
                                    <div class="col-sm-4">
                                        <select class="form-control" name="searchby" id="offcode">
                                            <option value="O">Office</option>
                                            <option value="G">GPF No</option>
                                        </select>
                                    </div>
                                    <div class="col-sm-2">
                                        <input type="text" name="gpfNo" class="form-control"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Department Name: </label>
                                    <div class="col-sm-10">
                                        <select class="form-control" name="deptName" id="deptName">
                                            <option value="">Select</option>
                                            <c:forEach items="${departmentList}" var="department">
                                                <option value="${department.deptCode}">${department.deptName}</option>
                                            </c:forEach>                                        
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Office Name</label>
                                    <div class="col-sm-10">
                                        <select class="form-control" name="offcode" id="offcode">
                                            <option value="">Select</option>                                            
                                        </select>
                                    </div>
                                </div>
                                
                            </form>
                        </div>
                        <div class="modal-footer">
                            <input type="submit" name="action" class="btn btn-default" value="Get Employee"/>
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>

                </div>
            </div>
        </form:form>
    </body>
</html>


