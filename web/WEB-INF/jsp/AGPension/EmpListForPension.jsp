<%-- 
    Document   : EmpListForPension
    Created on : 28 Oct, 2020, 1:31:16 PM
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
            function addEmployeeListForPension(me, appraiseeId) {

                var cons = confirm("Do you sent this information for NOC");
                if (cons) {
                    var url = "addEmployeeListForPension.htm";
                    var radioName = 'nocRequest_' + appraiseeId;
                    var radioValue = $("input[name='" + radioName + "']:checked").val();

                    if (radioValue == "Vigilance" || radioValue == "CB" || radioValue == "Both") {
                        $.post(url, {appraiseeId: appraiseeId, nocRequest: radioValue})
                                .done(function(data) {
                                    console.log($(me).html());
                                    $(me).parent().html("<strong class='text-danger'>NOC Requested</strong>");
                                });
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
            function searchOfficeName() {
                var offcode = $("#offCode").val();
                // alert(offcode);
                window.location = "promotionNocList.htm?searchOffcode=" + offcode;
                //alert(offcode);

            }
        </script>    
    </head>
    <body>
        <form:form action="pensionerNocList.htm" method="POST" commandName="pensionNOCBean" class="form-inline">

            <div class="panel panel-default">
                <div class="panel-heading">Employee List For Promotion</div>
                <div class="panel-body" style="height: 550px;overflow: auto;">
                    <div class="table-responsive">
                        <table class="table table-bordered table-hover table-striped">
                            <thead>
                                <tr>                                            
                                    <th width="3%">#</th>
                                    <th>HRMS Id</th>
                                    <th>Employee Name</th>
                                    <th>Designation</th>
                                    <th>Action/Status</th>
                                </tr>
                            </thead>
                            <tbody>   
                                <c:set var="empid" value="" />
                                <c:forEach items="${emplistforpension}" var="pensionemp" varStatus="count">
                                    <c:if  test="${ pensionemp.appraiseeId ne empid}">
                                         <c:set var="empid" value="${pensionemp.appraiseeId}" />

                                        <tr>                                                
                                            <td>${count.index + 1}</td>
                                            <td>${pensionemp.appraiseeId}</td>
                                            <td>${pensionemp.appraiseeName}</td>
                                            <td>${pensionemp.appraiseePost}</td>
                                            <td>
                                                <c:if  test="${ (pensionemp.nocRequest ne 'Vigilance' &&  pensionemp.nocRequest ne 'Both') ||  pensionemp.noofdays eq 1}">
                                                    <input type="radio" id="id_request_noc_vigilance" name="nocRequest_${pensionemp.appraiseeId}" value="Vigilance" > <b class='text-info'>Vigilance</b>&nbsp;
                                                </c:if>  
                                                <c:if  test="${ (pensionemp.nocRequest ne 'CB'  &&  pensionemp.nocRequest ne 'Both') ||  pensionemp.noofdays eq 1}">
                                                    <input type="radio" id="id_request_noc_cb" name="nocRequest_${pensionemp.appraiseeId}" value="CB" > <b class='text-info'>Crime Branch</b>
                                                    &nbsp;
                                                </c:if>       


                                                <c:if  test="${ empty pensionemp.nocfor ||  pensionemp.noofdays eq 1 }">    
                                                    <input type="radio" id="id_request_noc_both" name="nocRequest_${pensionemp.appraiseeId}" value="Both"  > <b class='text-info'>Both</b>&nbsp;&nbsp;
                                                </c:if> 
                                                <c:if  test="${ pensionemp.nocRequest ne 'Both' ||  pensionemp.noofdays eq 1}">
                                                    <input type="button" onclick="addEmployeeListForPension(this, '${pensionemp.appraiseeId}')"  class="btn btn-danger" value="Request NOC For Promotion"/> 
                                                </c:if>   
                                                <br/>



                                                <c:if  test="${not empty pensionemp.nocfor && pensionemp.nocRequest eq 'CB' &&  pensionemp.nocRequest ne 'Both' &&  pensionemp.noofdays eq 0 }">
                                                    <strong class='text-danger'>NOC Requested For Crime Branch</strong>
                                                </c:if>   
                                                <c:if  test="${not empty pensionemp.nocfor && pensionemp.nocRequest eq 'Vigilance'  &&  pensionemp.nocRequest ne 'Both' &&  pensionemp.noofdays eq 0 }">
                                                    <strong class='text-danger'>NOC Requested For Vigilance</strong>
                                                </c:if>    
                                                <c:if  test="${not empty pensionemp.nocfor && pensionemp.nocRequest eq 'Both' &&  pensionemp.noofdays eq 0 }">
                                                    <strong class='text-danger'>NOC Requested For Both Vigilance and Crime Branch</strong>
                                                </c:if>
                                                <c:if  test="${  pensionemp.noofdays eq 1 }">
                                                    <strong class='text-danger'>Previous NOC Request TimeOut</strong>
                                                </c:if>    
                                            </td>
                                        </tr>
                                    </c:if>   
                                </c:forEach>

                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="panel-footer">
                    <c:if test="${office.lvl eq '01' || office.lvl eq '02' || office.category eq 'DISTRICT COLLECTORATE'}">
                        <button type="button" class="btn btn-primary" onclick="openEmployeeListWindow()">Add Employee From Other Office</button>
                    </c:if>

                    &nbsp;<input type="submit" name="action" class="btn btn-warning" value="Back"/>
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


