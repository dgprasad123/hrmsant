<%-- 
    Document   : empListForInitiiatingdiscproced
    Created on : Nov 9, 2017, 11:52:47 AM
    Author     : manisha
--%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ page contentType="text/html;charset=UTF-8"%>

<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <style>
            table, th, td {
                border: 1px solid black;
                border-collapse: collapse;
            }
            th, td {
                padding: 5px;
                text-align: left;    
            }
            .table-responsive {
                max-height:450px;
                font-size: 10px;
            }
            .table-bordered{
                font-size: 12px;
            }
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
        <script>
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
            });
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
            function validation() {
                var checkedObj = $("input:checkbox[name='delinquent']:checked");
                if (checkedObj.length == 0) {
                    alert("Please select atleast one employee");
                    return false;
                }
                //var rulename = $("#hidrule").val();
                // alert(rulename);
                if ($("#rule").val() == "rule16") {
                    if (checkedObj.length > 1) {
                        alert("Please select only one employee");
                        return false;
                    }
                }

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

        </script>
    </head>
    <body>
        <form:form commandName="pbean" method="post" action="rule15Controller.htm">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="col-sm-1 text-right"> <label class="control-label">Office:</label></div>
                            <div class="col-sm-6"><form:input path="offName"  class="form-control"/><form:hidden path="offCode"/>  </div>
                            <div class="col-sm-2"><input type="submit" name="action" class="btn btn-default" value="Get Employee"/></div>
                            <div class="col-sm-2" text-left><button type="button" class="btn btn-default" onclick="openOfficeList()">Other Office</button></div>
                        </div>
                    </div>
                    <div style="padding: 0px 150px 0px 15px;">
                        <input type="text" id="myInput" onkeyup="myFunction()" placeholder="Search for names,GPF ACCT NO and Designation..">
                    </div>
                    <div class="panel-body table-responsive">
                        <table class="table table-bordered" id="myTable">
                            <thead>
                                <tr>
                                    <th width="5%">Select</th>
                                    <th width="5%">Sl No</th>
                                    <th width="55%">GPF No</th>
                                    <th width="55%">Employee Name</th>
                                    <th width="45%">Designation</th>
                                </tr>
                            </thead>

                            <tbody>                                        
                                <c:forEach items="${empList}" var="employee" varStatus="cnt">
                                    <tr>
                                        <td><input type="checkbox" name="delinquent" value="${employee.empid}"></td>
                                        <td>${cnt.index+1}</td>
                                        <td>${employee.gpfno}</td>
                                        <td>${employee.fullname}</td>
                                        <td>${employee.post}</td>

                                    </tr>
                                </c:forEach>
                            </tbody>

                        </table>
                    </div>
                    <div class="panel-footer">
                        <%-- <input type="hidden" id="hidrule" value="${rule}" /> --%>
                        <form:hidden path="mode"  />
                        <form:hidden path="rule"  />
                        <input type="submit" name="action" class="btn btn-default" value="Start" onclick="return validation()"/>  
                        <input type="submit" name="action" class="btn btn-default" value="Back" /> 
                    </div>
                </div>
            </div>
        </form:form>

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
                        <button type="button" class="btn btn-default" onclick="selectOffice()">Select</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>

            </div>
        </div>



    </body>
</html>