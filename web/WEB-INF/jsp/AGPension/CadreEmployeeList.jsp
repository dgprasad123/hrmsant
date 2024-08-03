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
                    var url = "addCadreEmployeeListForPension.htm";
                    var radioName = 'nocRequest_' + appraiseeId;
                    var radioValue = $("input[name='" + radioName + "']:checked").val();
                    var nocfor = $("#nocfor").val();;

                    if (radioValue == "Vigilance" || radioValue == "CB" || radioValue == "Both") {
                        $.post(url, {appraiseeId: appraiseeId, nocRequest: radioValue, nocfor: nocfor})
                                .done(function (data) {
                                    console.log($(me).html());
                                    $(me).parent().html("<strong class='text-danger'>NOC Requested</strong>");
                                });
                    } else {
                        alert("Please Choose NOC Request Radio Button");
                        return false;
                    }
                }

            }
            function back_page(){
                window.location="cadreNocList.htm";
                
            }
            
        </script>   
          
    </head>
    <body>
        <form:form action="SaveCadreNocList.htm" method="POST" commandName="pensionNOCBean" class="form-inline">
            <input type="hidden" name="nocfor" id="nocfor" value="${nocFor}"/>
            <div class="panel panel-default">
                <div class="panel-heading">Employee List For ${nocFor}</div>
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
                                <c:forEach items="${emplistforpension}" var="pensionemp" varStatus="count">
                                    <tr>                                                
                                        <td>${count.index + 1}</td>
                                        <td>${pensionemp.appraiseeId}</td>
                                        <td>${pensionemp.appraiseeName}</td>
                                        <td>${pensionemp.appraiseePost}</td>
                                        <td>
                                            <c:if  test="${empty pensionemp.nocfor }">
                                                 <!--<button type="button" onclick="addEmployeeListForPension(this, '${pensionemp.appraiseeId}')" class="btn btn-primary">Request For NOC</button>-->
                                                
                                                <input type="radio" id="id_request_noc_vigilance" name="nocRequest_${pensionemp.appraiseeId}" value="Vigilance" > <b class='text-info'>Vigilance</b>&nbsp;
                                                <input type="radio" id="id_request_noc_cb" name="nocRequest_${pensionemp.appraiseeId}" value="CB" > <b class='text-info'>Crime Branch</b>
                                                &nbsp;
                                                <input type="radio" id="id_request_noc_both" name="nocRequest_${pensionemp.appraiseeId}" value="Both"  > <b class='text-info'>Both</b>&nbsp;&nbsp;
                                                <input type="button" onclick="addEmployeeListForPension(this, '${pensionemp.appraiseeId}')"  class="btn btn-danger" value="Request NOC For ${nocFor}"/>
                                          
                                            </c:if>

                                            <c:if  test="${not empty pensionemp.nocfor }">
                                                <strong class='text-danger'>NOC Requested</strong>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                                  
                            </tbody>
                        </table>
                    </div>
                </div>
               <div class="panel-footer">
                    
                     &nbsp;<input type="button" name="action" class="btn btn-warning" value="Back" onclick="back_page()"/>
                </div>
            </div>
            
            
        </form:form>            
    </body>
</html>


