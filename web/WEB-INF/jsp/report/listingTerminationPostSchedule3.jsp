<%-- 
    Document   : ManageSanctionPost
    Created on : 17 May, 2019, 6:22:40 PM
    Author     : Surendra
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>



        <style type="text/css">
            .saveSuccess{
                color: #00cc66;
                font-weight: bold;
            }
            .saveError{
                color: #ff3333;
                font-weight: bold;
            }
            .row{
                margin-left:0px;
                margin-right:0px;
            }
        </style>
    </head>
    <body>

        <form:form action="listingTerminationPostSchedule3.htm" method="POST" commandName="command">

            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading"></div>
                    <div class="panel-body">
                        <h2 align='center'>${OffName}</h2>                      
                        <h6 align='center'>SCHEDULE III-A</h6>
                        <div align="center">(Relating to Administrative Department, attached Sub-ordinate Offices, Heads of Department & Sub-ordinate District Offices for the Department as a whole)</div>
                        <div class="row">
                            <div class="col-lg-12">
                                <h3 align="center"> Financial Year 
                                    <form:select path="financialYear" id="financialYear">
                                        <option value=""> Select One </option>
                                        <form:option value="2018-19"> </form:option>
                                        <form:option value="2019-20"> </form:option>
                                    </form:select>
                                  
                                    <button type="submit" name="btnAer" value="Search" class="btn btn-primary" onclick="return validate()">Show Termination Post List</button>   
                                </h3> 
                            </div>
                        </div>   
                        <table class="table table-striped table-bordered" width="90%">
                            <thead>
                                <tr>

                                    <th width="1%" >Sl No</th>
                                    <th  >Financial Year</th>
                                    <th  >Submitted On</th>
                                    <th >Status</th>
                                    <th  >Revert Reason</th>
                                    <th >Action</th>

                                </tr>

                            </thead>
                            <c:forEach var="partAGrpA" items="${PartAGrouplist}"  varStatus="theCount">

                                <tr>

                                    <td>${theCount.index + 1}</td>
                                    <td> ${partAGrpA.fy} </td>
                                    <td> ${partAGrpA.submittedDate} </td>
                                    <td> ${partAGrpA.status} </td>
                                    <td>${partAGrpA.revertReason}</td>
                                    <c:if test="${partAGrpA.notermPOst eq 'Y'}">
                                        <td><strong style='color:red'>No POST Termination Proposal</strong>
                                        &nbsp; <a href="downloadPDFScheduleIIIA.htm?termId=${partAGrpA.termId}&financialYear=${financialYear}"/><span class="fa fa-file-pdf-o" style="color:red"> </span> &nbsp;SCHEDULE III-A</a>
                                        </td>
                                    </c:if>
                                    <c:if test="${empty partAGrpA.notermPOst }">
                                        <td><a href="viewTerminationPOstSchedule3.htm?termId=${partAGrpA.termId}&financialYear=${financialYear}">View</a>
                                        &nbsp; <a href="downloadPDFScheduleIIIA.htm?termId=${partAGrpA.termId}&financialYear=${financialYear}"/><span class="fa fa-file-pdf-o" style="color:red"> </span> &nbsp;SCHEDULE III-A</a>
                                        </td>
                                    </c:if> 



                                </tr>
                            </c:forEach>      



                        </table>
                        <c:if test="${cntRecord eq '0'}">
                            <div class="modal-footer">
                                <a href="aerTerminationPostSchedule3.htm?financialYear=${financialYear}"><button type="button" class="btn btn-primary" >Add New</button></a>
                            </div>
                        </c:if>
                    </div>

                </div>

            </div>



        </form:form>

        <script type="text/javascript">
            function validate() {
                if ($("#financialYear").val() == '') {
                    alert('Please select Financial Year.');
                    $("#financialYear").focus();
                    return false;
                }
            }
        </script>    

    </body>
</html>

