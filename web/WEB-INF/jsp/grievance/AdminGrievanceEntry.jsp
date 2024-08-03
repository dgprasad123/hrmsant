<%-- 
    Document   : EmployeeGrievanceEntry
    Created on : Jan 6, 2018, 7:16:44 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
            function showOtherBlk(val) {
                if (val == 15) {
                    $("#other_blk").show();
                } else {
                    $("#other_blk").hide();
                }
            }
            function myFunction() {
                // Declare variables
                var input, filter, table, tr, td, i;
                input = document.getElementById("myInput");
                filter = input.value.toUpperCase();
                table = document.getElementById("myTable");
                tr = table.getElementsByTagName("tr");

                // Loop through all table rows, and hide those who don't match the search query
                for (i = 0; i < tr.length; i++) {
                    td = tr[i].getElementsByTagName("td")[1];
                    if (td) {
                        if (td.innerHTML.toUpperCase().indexOf(filter) > -1) {
                            tr[i].style.display = "";
                        } else {
                            tr[i].style.display = "none";
                        }
                    }
                }
            }
            function selectEmployee(me) {
                $("#hrmsid").val($(me).val());
                $("#empnameDiv").html($(me).parent().siblings("td").text());
                $('#myModal').modal('toggle');
            }
        </script>
        <style type="text/css">
            #myInput {
                background-image: url('images/searchicon.png'); /* Add a search icon to input */
                background-position: 10px 12px; /* Position the search icon */
                background-repeat: no-repeat; /* Do not repeat the icon image */
                width: 100%; /* Full-width */
                font-size: 16px; /* Increase font-size */
                padding: 12px 20px 12px 40px; /* Add some padding */
                border: 1px solid #ddd; /* Add a grey border */
                margin-bottom: 12px; /* Add some space below the input */
            }

            #myTable {
                border-collapse: collapse; /* Collapse borders */
                width: 100%; /* Full-width */
                border: 1px solid #ddd; /* Add a grey border */
                font-size: 14px; /* Increase font-size */
            }

            #myTable th, #myTable td {
                text-align: left; /* Left-align text */
                padding: 12px; /* Add padding */
            }

            #myTable tr {
                /* Add a bottom border to all table rows */
                border-bottom: 1px solid #ddd;
            }

            #myTable tr.header, #myTable tr:hover {
                /* Add a grey background color to the table header and on hover */
                background-color: #f1f1f1;
            }
            #myTable tbody {
                height: 350px;
                overflow-y: auto;
            }


            #myTable thead, #myTable tbody { display: block; }







        </style>
        <script type="text/javascript">
            function validate() {
                if ($.trim($("#grievanceDetail").val()) == "") {
                    alert("Grievance Detail Cannot be Blank");
                    return false;
                }
            }
            
        </script>
            
    </head>
    <body>
        <div class="panel panel-default">
            <div class="panel-heading">New Grievance Entry</div>
            <div class="panel-body">
                <form:form action="saveEmployeeGrievnceByAdmin.htm" commandName="EmployeeGrievanceForm" method="post" enctype="multipart/form-data">
                    <table class="table table-bordered table-hover" style="width:1000px;" align="center">
                        <tr style="font-weight:bold;background:#3E6A00;color:#FFFFFF;">
                            <td colspan="2" style="color:#FFFFFF;font-weight:bold;">Enter Grievance Details</td>
                        </tr>
                        <tr bgcolor="#FFFFFF">
                            <td>Employee Name:</td>
                            <td>
                                <form:hidden path="hrmsid"/>
                                <form:hidden path="appoffcode"/>
                                <form:hidden path="source"/>
                                <form:hidden path="appmobile"/>
                                <div class="col-lg-4" id="empnameDiv"></div>
                                <input type="button" value="Browse" class="btn btn-success" data-toggle="modal" data-target="#myModal"/>
                            </td>
                        </tr>
                        <tr bgcolor="#FFFFFF">
                            <td>Category:</td>
                            <td>
                                <form:select path="categoryCode" onchange='showOtherBlk(this.value)'>
                                    <form:options items="${categorylist}" itemValue="categoryCode" itemLabel="category"/>                                
                                </form:select>	
                                <span style="display:none;" id="other_blk"><input type="text" name="other_category" class="tbox" size="30"/> <small>(Enter your Category Here)</small></span>
                            </td>
                        </tr>
                        <tr bgcolor="#FFFFFF">
                            <td>Grievance Details:</td>
                            <td><form:textarea path="grievanceDetail" cols="90" rows="5"/></td>
                        </tr>
                        <tr bgcolor="#FFFFFF">
                            <td>Attachment(s) if any?:</td>
                            <td><input type="file" name="attachment" id="attachment" /></td>
                        </tr>
                        <tr bgcolor="#FFFFFF">
                            <td></td>
                            <td>
                                <input type="submit" name="action" value="Save" class="btn btn-success" onclick="return validate()"/>
                                <input type="submit" value="Cancel" class="btn btn-success"/>
                                <br />
                                <span style="color:#888888;font-style:italic;display:none;" id="loader1">Please wait...</span>	
                            </td>	
                        </tr>
                    </table>
                </form:form>
            </div>
        </div>
        <!-- Modal -->
        <div id="myModal" class="modal fade" role="dialog">
            <div class="modal-dialog">

                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Employee List</h4>
                    </div>
                    <div class="modal-body" s>
                        <input type="text" id="myInput" onkeyup="myFunction()" placeholder="Search for names..">
                        <table class="table table-bordered table-hover" id="myTable">                            
                            <tbody>
                                <c:forEach items="${empList}" var="employee">
                                    <tr>
                                        <td><input name="empid" type="radio" value="${employee.empid}" onclick="selectEmployee(this)"/></td>
                                        <td>${employee.fullname}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>

            </div>
        </div>
    </body>
</html>
