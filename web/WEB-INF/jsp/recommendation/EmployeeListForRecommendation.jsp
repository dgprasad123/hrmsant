<%-- 
    Document   : EmployeeListForRecommendation
    Created on : 16 Oct, 2020, 4:43:53 PM
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
        <link rel="stylesheet" href="css/chosen.css">
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script> 
        <script src="js/chosen.jquery.min.js"></script>
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
        </style>
        <script type="text/javascript">
            function addEmployeeForRecommendation(me, recommendedempId, recommendedspc, recommendationId) {
                var url = "addrecommendationEmployeeList.htm";
                $.post(url, {recommendedempId: recommendedempId, recommendedspc: recommendedspc, recommendationId: recommendationId})
                        .done(function (data) {
                            console.log($(me).html());
                            $(me).parent().html("<span>Added</span>");
                        });

            }
            function openEmployeeListWindow() {
                $('#setEmployee').modal('show');
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
                    td = tr[i].getElementsByTagName("td")[2];
                    if (td) {
                        txtValue = td.textContent || td.innerText;
                        if (txtValue.toUpperCase().indexOf(filter) > -1) {
                            tr[i].style.display = "";
                        } else {
                            tr[i].style.display = "none";
                        }
                    }
                }
            }
            $(document).ready(function() {
                //$('#offCode').chosen();
            })
        </script>        
    </head>
    <body  style="background-color:#FFFFFF;margin-top:10px;">
        <form:form action="recommendationEmployeeList.htm" method="POST" commandName="recommendationDetailBean" class="form-inline">
            <div style="padding: 0px 15px 0px 15px;">
                <input type="text" id="myInput" onkeyup="myFunction()" placeholder="Search for names..">
            </div>
            <div class="panel panel-default">
                <div class="panel-heading">Employee List For Nomination</div>
                <div class="panel-body" style="height: 480px;overflow: auto;">
                    <div class="table-responsive">
                        <table class="table table-bordered table-hover table-striped" id="myTable">
                            <thead>
                                <tr>                                            
                                    <th width="3%">#</th>
                                    <th>GPF No/ PRAN No</th>
                                    <th>Employee Name</th>
                                    <th>Designation</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>                                        
                                <c:forEach items="${recommendationemplist}" var="recommendationemp" varStatus="count">
                                    <tr>                                                
                                        <td>${count.index + 1}</td>
                                        <td>${recommendationemp.recommendedempGpfNo}</td>                                        
                                        <td>${recommendationemp.recommendedempname}</td>
                                        <td>${recommendationemp.recommendedpost}</td>
                                        <td>
                                            <c:if test="${recommendationemp.alreadyAdded eq 'Y'}">
                                                <span>Already Added</span>
                                            </c:if>
                                            <c:if test="${recommendationemp.alreadyAdded eq 'N'}">
                                                <button type="button" onclick="addEmployeeForRecommendation(this, '${recommendationemp.recommendedempId}', '${recommendationemp.recommendedspc}', '${recommendationemp.recommendationId}')" class="btn btn-default">Add</button>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="panel-footer">
                    <form:hidden path="recommendationId"/>
                    <input type="submit" name="action" class="btn btn-default" value="Back"/>
                    <c:if test="${office.lvl eq '01' || office.lvl eq '02' || office.category eq 'DISTRICT COLLECTORATE'}">
                        <button type="button" class="btn btn-primary" onclick="openEmployeeListWindow()">Add Employee From Other Office</button>
                    </c:if>
                        <!--
                    <input type="button" name="add" class="btn btn-default" value="Any Other Officer on Deputation"/>
                        -->
                </div>
            </div>
            <%-- This Modal Open While the Authority will click on Add Employee For Other Office Button--%>
            <div id="setEmployee" class="modal fade" role="dialog">
                <div class="modal-dialog  modal-lg">

                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Choose Office Name</h4>
                        </div>
                        <div class="modal-body">                            
                            <div class="form-group">
                                <label class="control-label col-sm-3">Office Name</label>
                                <div class="col-sm-7">
                                    <select class="form-control" style="width:500px;" name="offcode" id="offCode">
                                        <option value="">Select</option> 
                                        <c:forEach items="${officelistcowise}" var="office">
                                            <option value="${office.offCode}">${office.offName}</option>
                                        </c:forEach> 
                                    </select>
                                </div>
                                <div class="col-sm-2">
                                    <input type="submit" class="btn btn-primary" name="action" value="Search"/>
                                </div>
                            </div>
                            <table class="table table-bordered">
                                <tbody id="empdatatable">


                                </tbody>
                            </table>
                        </div>
                        <div class="modal-footer">                                                         

                            <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>


