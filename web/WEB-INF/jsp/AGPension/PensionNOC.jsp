<%-- 
    Document   : OfficeEmployeeList
    Created on : 16 Feb, 2017, 3:53:03 PM
    Author     : Prashant
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
        </style>
        <script type="text/javascript">
            function requestForNOC(hrmsid) {
               // alert(hrmsid);
                var cons = confirm("Do you sent this information for NOC");
                if (cons) {
                    var radioName = 'nocRequest_' + hrmsid;
                  //  alert(radioName);
                    var radioValue = $("input[name='" + radioName + "']:checked").val();
                    if(radioValue=="Vigilance" || radioValue=="CB" || radioValue=="Both"){
                         window.location = "requestForNoc.htm?hrmsid=" + hrmsid + "&nocRequest=" + radioValue;
                      } else {                          
                             alert("Please Choose NOC Request Radio Button");
                            return false;
                      }
                }
            }
        </script> 
        <script type="text/javascript">
            function openEmployeeListWindow() {
                $('#setEmployee').modal('show');
                
                
            }
            function searchOfficeName(){
                var offcode=$("#offCode").val();
               // alert(offcode);
                window.location="PensionOtherOfficeNocList.htm?searchOffcode="+offcode;
                //alert(offcode);
                
            }
        </script>    
    </head>
    <body>
        <form:form action="pensionerNocList.htm" method="POST" commandName="pnoc" class="form-inline">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        NOC For Pension (Vigilance & Crime branch)
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>                                
                                    <th>Slno</th>
                                    <th>HRMS ID</th>
                                    <th>GPF NO</th>
                                    <th>Name</th>                                
                                    <th>DOB</th>
                                    <th>DOS</th>
                                    <th>POST</th>
                                    <th>NOC For</th>
                                    <th>Action/Status</th>

                                </tr>                            
                            </thead>
                            <tbody>
                                <c:forEach items="${pensionList}" var="pnoc" varStatus="count">
                                    <tr>                                    
                                        <td>${count.index + 1}</td>
                                        <td>${pnoc.hrmsid}</td>
                                        <td>${pnoc.gpfNo}</td>
                                        <td>${pnoc.name}</td>
                                        <td>${pnoc.doj}</td>
                                        <td>${pnoc.dos}</td>
                                        <td>${pnoc.post}</td> 
                                        <td>${pnoc.nocfor}</td>
                                        <td>
                                            
                                          <!--  <c:if test="${pnoc.nocId != 0 }">
                                                <strong class=" text-danger">NOC Requested</strong>
                                            </c:if>-->
                                            <c:if test="${pnoc.nocId == 0 }">
                                                <!--<a href="javascript:void(0)" onclick="requestForNOC(${pnoc.hrmsid})" class='text-info'><strong>Request NOC From</strong></a>&nbsp;&nbsp;-->
                                                <input type="radio" id="id_request_noc_vigilance" name="nocRequest_${pnoc.hrmsid}" value="Vigilance" > <b class='text-info'>Vigilance</b>&nbsp;
                                                <input type="radio" id="id_request_noc_cb" name="nocRequest_${pnoc.hrmsid}" value="CB" > <b class='text-info'>Crime Branch</b>
                                                &nbsp;
                                                <input type="radio" id="id_request_noc_both" name="nocRequest_${pnoc.hrmsid}" value="Both"  > <b class='text-info'>Both</b>&nbsp;&nbsp;
                                                
                                                 <input type="button" onclick="requestForNOC('${pnoc.hrmsid}')"  class="btn btn-danger" value="Request NOC For Pension"/>
                                            </c:if>
                                            <c:if  test="${not empty pnoc.nocfor && pnoc.nocRequest eq 'CB' &&  pnoc.nocRequest ne 'Both'}">
                                                <strong class='text-danger'>NOC Requested For Crime Branch</strong>
                                            </c:if>   
                                            <c:if  test="${not empty pnoc.nocfor && pnoc.nocRequest eq 'Vigilance'  &&  pnoc.nocRequest ne 'Both' }">
                                                <strong class='text-danger'>NOC Requested For Vigilance</strong>
                                            </c:if>    
                                            <c:if  test="${not empty pnoc.nocfor && pnoc.nocRequest eq 'Both' }">
                                                <strong class='text-danger'>NOC Requested For Both Vigilance and Crime Branch</strong>
                                            </c:if>     

                                        </td>

                                    </tr>

                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <div class="panel-footer">
                        <c:if test="${office.lvl eq '01' || office.lvl eq '02' || office.category eq 'DISTRICT COLLECTORATE'}">
                          <button type="button" class="btn btn-primary" onclick="openEmployeeListWindow()">Add Employee From Other Office</button>&nbsp;  
                        </c:if>
                         
                        <input type="submit" name="action" class="btn btn-warning" value="Back"/>


                    </div>

                </div>
            </div>
            
            
            
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
                                    <input type="button" class="btn btn-primary" name="action" value="Search" onclick="searchOfficeName()"/>
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