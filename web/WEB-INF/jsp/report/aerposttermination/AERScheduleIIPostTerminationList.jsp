<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <title>HRMS</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
                function editTerminationScheduleII(pid,fy,type){
                    var mess=confirm("Previous post termination proposal will delete and will create the fresh proposal ");
                    if(mess) {
                        window.location="PostTerminationCOScheduleII.htm?btnPTAer=Create&editPropsalId="+pid+"&financialYear="+fy;
                    }
                }
        </script>    
    </head>
    <body>
        <form:form action="PostTerminationCOScheduleII.htm?btnPTAer=GetList" method="POST" commandName="command">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading"></div>
                    <div class="panel-body">
                        <h2 align="center">${officename}</h2>
                        <div class="row">
                            <div class="col-lg-12">
                                <h3 align="center"> Financial Year 
                                    <form:select path="financialYear" id="financialYear">
                                        <option value=""> Select One </option>
                                        <form:option value="2018-19"> </form:option>
                                        <form:option value="2019-20"> </form:option>
                                         <form:option value="2020-21"> </form:option>
                                    </form:select>

                                    <button type="submit" name="btnAer" value="Search" class="btn btn-primary" onclick="return validate()">Show Termination Post List</button>   
                                </h3> 
                            </div>
                        </div>  
                        <h6 align="center">SCHEDULE II-A</h6>
                        <div align="center">(Relating to Head of the Department)</div>

                        <table class="table table-striped table-bordered" width="90%">
                            <thead>
                                <tr>
                                    <th width="1%">Sl No</th>
                                    <th>Financial Year</th>
                                    <th>Submitted On</th>
                                    <th>Status</th>
                                    <th>Revert Reason</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <c:forEach var="list" items="${terminationlist}" varStatus="cnt">
                                <tr>
                                    <td>${cnt.index + 1}</td>
                                    <td>2020-21</td>
                                    <td>${list.submittedDate}</td>
                                    <td>${list.status}</td>
                                    <td>${list.revertReason}</td>
                                    <c:if test="${list.notermPost eq 'Y'}">
                                        <td><strong style='color:red'>No POST Termination Proposal</strong>&nbsp;|&nbsp;
                                            <a href="downloadPDFScheduleIIA.htm?propsalId=${list.proposalId}&financialYear=${financialYear}"/><span class="fa fa-file-pdf-o" style="color:red"> </span> &nbsp;SCHEDULE II-A</a>
                                            <c:if test="${list.isAoApproved eq 'N'}">
                                                &nbsp;|&nbsp;<a href="#"  onclick="editTerminationScheduleII(${list.proposalId},'${financialYear}',2)"/>Edit</a>
                                            </c:if>
                                        </td>
                                    </c:if>
                                    <c:if test="${empty list.notermPost }">
                                        <td><a href="viewPostTerminationScheduleII.htm?propsalId=${list.proposalId}&financialYear=${financialYear}&type=2"/>View</a>&nbsp;|&nbsp;
                                            <a href="downloadPDFScheduleIIA.htm?propsalId=${list.proposalId}&financialYear=${financialYear}"/><span class="fa fa-file-pdf-o" style="color:red"> </span>&nbsp;SCHEDULE II-A</a>
                                            <c:if test="${list.isAoApproved eq 'N'}">
                                                &nbsp;|&nbsp;<a href="#" onclick="editTerminationScheduleII(${list.proposalId},'${financialYear}',2)"/>Edit</a>
                                            </c:if>
                                        </td>
                                    </c:if> 

                                </tr>
                            </c:forEach> 
                        </table>
                        <c:if test="${cntRecord eq '0'}">
                            <div class="modal-footer">
                                <a href="PostTerminationCOScheduleII.htm?btnPTAer=Create&financialYear=${financialYear}"><button type="button" class="btn btn-primary">Create</button></a>
                            </div>
                        </c:if>  
                    </div>
                </div>
            </div>
        </form:form>           
    </body>
</html>
