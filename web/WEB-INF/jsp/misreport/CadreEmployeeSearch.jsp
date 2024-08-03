<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <!-- Custom CSS -->
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js" type="text/javascript"></script>

        <!-- Bootstrap Core JavaScript -->
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script src="js/common.js" type="text/javascript"></script>
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
        <script type="text/javascript">
            function viewEmployeeData(empid) {
                var cadrecode = $("#cadrecode").val();
                window.location = "getCadreEmpData.htm?empid=" + empid + "&cadrecode=" + cadrecode;
            }
            function viewEREmpSheet(empid) {
                // var cadrecode=$("#cadrecode").val();
                window.location = "viewEREmpSheet.htm?empid=" + empid;
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
        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <div class="container-fluid">
                    <!-- Page Heading -->
                    <div class="row">
                        <div class="col-lg-12">                            
                            <ol class="breadcrumb">
                                <li>
                                    <i class="fa fa-dashboard"></i>  <a href="index.html">Dashboard</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> Cadre 
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> Employee List 
                                </li>
                            </ol>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-12">
                            <form class="form-horizontal" action="cadreEmployeeSearch.htm" method="post">
                                <div class="form-group">
                                    <label class="control-label col-sm-2" for="allYear">Allotment Year :</label>
                                    <div class="col-sm-4">
                                        <input type="text" name="allYear" class="form-control" placeholder="YYYY" value="${param.allYear}" maxlength="4"/>                                        
                                    </div>                                    
                                </div>                                
                                <div class="form-group">
                                    <label class="control-label col-sm-2" for="cadrecode">Cadre:${LoginUserBean.loginusername}</label>
                                    <div class="col-sm-4">
                                        <select class="form-control"  name="cadrecode" id="cadrecode" required="1">
                                            <option value="">Select</option>${param.cadrecode}
                                            <c:if test="${LoginUserBean.loginusername eq 'services1'}">
                                            <option value="1101" <c:if test="${param.cadrecode == '1101'}"> selected="selected"</c:if>>IAS</option>
                                            <option value="1166" <c:if test="${param.cadrecode == '1166'}"> selected="selected"</c:if>>IPS</option>
                                            <option value="1103" <c:if test="${param.cadrecode == '1103'}"> selected="selected"</c:if>>OAS (GA & PG)</option>
                                            <option value="2668" <c:if test="${param.cadrecode == '2668'}"> selected="selected"</c:if>>OAS (R & DM)</option>
                                            </c:if>
                                            <c:if test="${LoginUserBean.loginusername eq 'OFS_BRANCH'}">
                                            <option value="0723" <c:if test="${param.cadrecode == '0723'}"> selected="selected"</c:if>>OFS</option>
                                            </c:if>
                                            <c:if test="${LoginUserBean.loginusername eq 'OTAS_BRANCH'}">
                                            <option value="0730" <c:if test="${param.cadrecode == '0730'}"> selected="selected"</c:if>>OTAS</option>
                                            </c:if>
                                            <c:if test="${LoginUserBean.loginusername eq 'OIS_BRANCH'}">
                                            <option value="1601" <c:if test="${param.cadrecode == '1601'}"> selected="selected"</c:if>>OIS(INDUSTRY)</option>
                                            <option value="7301" <c:if test="${param.cadrecode == '7301'}"> selected="selected"</c:if>>OIS(MSME)</option>
                                            </c:if>
                                            <c:if test="${LoginUserBean.loginusername eq 'OFORS_BRANCH'}">
                                            <option value="1008" <c:if test="${param.cadrecode == '1008'}"> selected="selected"</c:if>>ODISHA FOREST SERVICE</option>
                                            <option value="1009" <c:if test="${param.cadrecode == '1009'}"> selected="selected"</c:if>>ODISHA SUBORDINATE FOREST SERVICE</option>
                                            </c:if>
                                            </select>
                                        </div>
                                        <label class="control-label col-sm-2">Sort By:</label>
                                        <div class="col-sm-4">
                                            <select class="form-control"  name="sortby" id="sortby">
                                                <option value="">Select</option>
                                                <option value="DOS">Date of Superannuation</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="col-sm-2">&nbsp;</div>
                                        <div class="col-sm-4">
                                            <button type="submit" class="btn btn-primary">Submit</button>&nbsp;&nbsp;
                                            <button type="reset" class="btn btn-primary">Reset</button>
                                        </div>
                                    </div>       

                                </form>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-lg-12" style="padding: 0px 15px 0px 15px;">
                                <input type="text" id="myInput" onkeyup="myFunction()" placeholder="Search by Employee Name..">
                            </div>
                        </div>
                        <div class="row">                            
                            <div class="col-lg-12">                                
                                <div class="table-responsive">
                                    <table class="table table-bordered table-hover table-striped" id="myTable">
                                        <thead>
                                            <tr>
                                                <th width="5%">Sl No</th>   
                                                <th  width="5%">Action</th>
                                                <th  width="20%">Name</th>
                                                <th  width="10%">Id No</th>
                                                <th  width="15%">DOB<br/>DOS</th>
                                                <th  width="15%">Joining Date</th>
                                                <th  width="10%">Recruitment Source</th>
                                                <th  width="5%">Allotment Year</th>
                                                <th  width="20%">Post Position</th>
                                                <th  width="10%">Station</th>                                            
                                                <!--<th>Pay Scale</th>                                            -->
                                            </tr>
                                        </thead>
                                        <tbody id="incumbancyList">                                        
                                        <c:forEach items="${employees}" var="employee" varStatus="counter">
                                            <tr>
                                                <td> ${counter.count}</td>   
                                                <td>
                                                    <div class="dropdown">
                                                        <button class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown">Action
                                                            <span class="caret"></span></button>
                                                        <ul class="dropdown-menu">
                                                            <li><a href='javascript:viewEmployeeData("${employee.empid}")'>View Details</a></li>
                                                            <li><a href='javascript:viewEREmpSheet("${employee.empid}")'>ER Sheet</a></li>
                                                            <li><a href='#'>Transfer</a></li>
                                                            <li><a href="#">Relieve</a></li>
                                                            <li><a href="#">Join</a></li>
                                                            <li><a href="#">Join In Additional Charge</a></li>
                                                        </ul>
                                                    </div>                                                                                                       
                                                </td>
                                                <td><span style="color: tomato;">${employee.empid}</span><br/>
                                                    ${employee.fname} ${employee.mname} ${employee.lname}</td>     
                                                <td>${employee.cadreId}</td>
                                                <td><span style="color: green;"> ${employee.dob}</span> </br> <span style="color: tomato;">${employee.dos}</span></td>
                                                <td><span style="color: green;"> ${employee.joindategoo}</span></td>
                                                <td>${employee.recsource}</td>
                                                <td>${employee.cardeallotmentyear}</td>
                                                <td>${employee.spn}</td>
                                                <td>${employee.station}</td>                                                
                                                <!--<td>${employee.basic}</td>-->

                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- Modal -->
        <div id="myModal" class="modal fade" role="dialog">
            <div class="modal-dialog" style="width:900px;">

                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Employee Detail</h4>
                    </div>
                    <div class="modal-body" style="height:450px;">

                        <div class="form-inline">
                            <label class="control-label col-sm-4">Name:</label>
                            <div class="col-sm-8">
                                <input type="text" class="form-control"  name="fname" placeholder="First Name" id="fname" readonly="true"/>
                                <input type="text" class="form-control"  name="mname" placeholder="Middle Name" id="mname" readonly="true"/>
                                <input type="text" class="form-control"  name="lname" placeholder="Last Name" id="lname" readonly="true"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-4">ID Number:</label>
                            <div class="col-sm-8">
                                <input type="text" id="idmark" name="idmark" size="20" class="form-control"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-4">Allotment Year:</label>
                            <div class="col-sm-8">                                
                                <input type="text" id="cardeallotmentyear" name="cardeallotmentyear" size="4" class="form-control"/>                                
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-4">Recruitment Source:</label>
                            <div class="col-sm-8">
                                <select class="form-control"  name="recryear" id="recryear" >
                                    <option>R.R.</option>
                                    <option>SCS</option>
                                    <option>NSCS</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-4">DOJ to the Service:</label>
                            <div class="col-sm-8">
                                <input type="date" name="dojg" class="form-control" placeholder="Date of Joining to the Service"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-4">Home State:</label>
                            <div class="col-sm-8">
                                <select class="form-control"  name="homestate" id="homestate" >

                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-4">Home District:</label>
                            <div class="col-sm-8">
                                <select class="form-control"  name="homedist" id="homedist" >
                                    <option>R.R.</option>
                                    <option>SCS</option>
                                    <option>NSCS</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-4">Address:</label>
                            <div class="col-sm-8">
                                <textarea class="form-control" name="permanentaddr" id="permanentaddr"></textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-4">Current Position:</label>
                            <div class="col-sm-8">
                                <input type="text" id="spn" name="spn" size="20" readonly="true" maxlength="100" class="form-control"/>  
                                <!--
                                <button type="button" class="btn btn-primary">Change</button></div>
                                -->
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-4">DOJ to Current Position:</label>
                                <div class="col-sm-8">
                                    <input type="date" name="dateOfCurPosting" id="dateOfCurPosting" class="form-control" placeholder="Date of Joining to Current Position"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-4">Pay Scale:</label>
                                <div class="col-sm-8"><input type="text" id="payScale" name="payScale" size="20" maxlength="100" class="form-control"/></div>
                            </div>

                        </div>
                        <div class="modal-footer">
                            <span id="msg"></span>
                            <button type="button" class="btn btn-default">Save</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>
    </body>
</html>
