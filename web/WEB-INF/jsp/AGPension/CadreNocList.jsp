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
                var cons = confirm("Do you sent this information for NOC");
                if (cons) {
                    window.location = "requestForNoc.htm?hrmsid=" + hrmsid;
                }
            }

        </script> 
        <script>
            $(document).ready(function () {
                $('[data-toggle="tooltip"]').tooltip();
            });
        </script>
        
         <script type="text/javascript">
            function openEmployeeListWindow(vals) {
                $("#nocfor").val(vals);
                $('#setEmployee').modal('show');
                
                
            }
            function searchcadreName(){
                var cadreValue=$("#cadreValue").val();
               // alert(offcode);
                var nocFor= $("#nocfor").val();
                window.location="CadreEmployeeList.htm?cadreValue="+cadreValue+"&nocFor="+nocFor;
                //alert(offcode);
                
            }
        </script> 
    </head>
    <body>
        <form:form action="pensionerNocList.htm" method="POST" commandName="pnoc" class="form-inline">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        NOC From Vigilance & Crime branch
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
                                    <!--<th>Status</th>-->
                                    <th>Vigilance NOC Detail</th>                                                                
                                    <th>Crime Branch NOC Detail</th>
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
                                        <td style='color:green'>${pnoc.nocfor}</td>

                                        <td>
                                            <c:if  test="${pnoc.nocRequest eq 'Both' ||  pnoc.nocRequest eq 'Vigilance'}">
                                                <c:if  test="${not empty pnoc.vigilanceNocReason && pnoc.vNocStatus eq 'Y'}">
                                                    <span class="text-danger" style="font-weight: bold;">Declined </span><br/>


                                                </c:if> 
                                                <c:if  test="${ empty pnoc.vigilanceNocReason && pnoc.vNocStatus eq 'Y'}">
                                                    <span class="text-success" style="font-weight: bold;">Approved</span><br/>
                                                    <c:if test="${ empty pnoc.vigilanceNocReason &&  pnoc.vNocStatus eq 'Y'}">
                                                        <a href="GeneratevNoc.htm?nocId=${pnoc.nocId}&hrmsid=${pnoc.hrmsid}" class="btn btn-default" >
                                                            <span class="glyphicon glyphicon-paperclip"></span>  Download document
                                                        </a> 
                                                    </c:if>


                                                </c:if>     

                                                <c:if  test="${not empty pnoc.vigilanceNocReason && pnoc.vNocStatus eq 'Y'}">

                                                    <a href="downloadAttachmentOfNOC.htm?nocId=${pnoc.nocId}" class="btn btn-default" >
                                                        <span class="glyphicon glyphicon-paperclip"></span> Download document
                                                    </a> 
                                                </c:if>


                                                <c:if  test="${pnoc.vNocStatus eq 'N'}">
                                                    <span class="text-danger" >NOC Requested  </span><br/>

                                                </c:if> 
                                            </c:if>        
                                        </td>
                                        <td> 
                                            <c:if  test="${pnoc.nocRequest eq 'Both' || pnoc.nocRequest eq 'CB'}">
                                                <c:if  test="${ empty pnoc.cbNocReason && pnoc.cNocStatus eq 'Y'}">
                                                    <span class="text-success" style="font-weight: bold;">Approved</span><br/>
                                                    <a href="GenerateNoc.htm?nocId=${pnoc.nocId}&hrmsid=${pnoc.hrmsid}" class="btn btn-default" >
                                                        <span class="glyphicon glyphicon-paperclip"></span>  Download document
                                                    </a> 
                                                </c:if>
                                                <c:if  test="${not empty pnoc.cbNocReason && pnoc.cNocStatus eq 'Y'}">
                                                    <span class="text-danger" style="font-weight: bold;"><br/>Declined </span><br/>


                                                </c:if>

                                                <c:if  test="${not empty pnoc.cbNocReason && pnoc.cNocStatus eq 'Y'}">

                                                    <a href="downloadAttachmentOfNOCForCB.htm?nocId=${pnoc.nocId}" class="btn btn-default" >
                                                        <span class="glyphicon glyphicon-paperclip"></span> Download document
                                                    </a> 
                                                </c:if>


                                                <c:if  test="${pnoc.cNocStatus eq 'N'}">
                                                    <span class="text-danger" >NOC Requested  </span><br/>
                                                </c:if> 
                                            </c:if>         
                                        </td>
                                    </tr>

                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <div class="panel-footer">
                        <input type="button" name="promotion" class="btn btn-success" value="Add Employee From Other Cadre For Promotion" onclick="openEmployeeListWindow('PROMOTION')"/>
                        
                       <!-- <button type="button" class="btn btn-primary" onclick="openEmployeeListWindow()">Request NOC for Promotion</button>-->
                        &nbsp;
                        <input type="button" name="pension" class="btn btn-info" value="Add Employee From Other Cadre For Pension" onclick="openEmployeeListWindow('PENSION')"/>

                    </div>

                </div>
            </div>
            <div id="setEmployee" class="modal fade" role="dialog">
                <input type="hidden" name="nocfor" id="nocfor"/>
                <div class="modal-dialog  modal-lg">

                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Choose Cadre Name</h4>
                        </div>
                        <div class="modal-body">                            
                            <div class="form-group">
                                <label class="control-label col-sm-3">Cadre Name</label>
                                <div class="col-sm-7">
                                    <select class="form-control" style="width:500px;" name="cadreValue" id="cadreValue">
                                        <option value="">Select</option> 
                                        <c:forEach items="${cadreList}" var="cadre">
                                            <option value="${cadre.cadreValue}">${cadre.cadreName}</option>
                                        </c:forEach> 
                                    </select>
                                </div>
                                <div class="col-sm-2">
                                    <input type="button" class="btn btn-primary" name="action" value="Search" onclick="searchcadreName()"/>
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
</html>`