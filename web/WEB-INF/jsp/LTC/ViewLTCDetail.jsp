<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% int i = 1;
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Apply LTC: Basic Info</title>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">   
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script src="js/moment.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/jquery.min.js"></script>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>  
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
 
 </script>
    </head>
    <body style="padding-top: 10px;" onblur="javascript:window.close();">

        <div style="width:95%;margin:0px auto;border-top:0px;">
            <div class="row">
               <form:form action="SaveBasicInfo.htm" method="post" commandName="LTCBean" id="frmLTC">
                   <form:hidden path="ltcId"/>
            <div class="container-fluid">
                <div class="panel panel-default" style="padding-left:10px;">
                    <div class="panel-heading" style="margin-bottom:10px;padding-bottom:5px;color:#0052A7">
                        
                        <strong style="font-size:14pt;">LTC Application Details</strong>
                    </div>
                    <div>
                        <table class="table table-bordered" style="font-size:10pt;">
                                    <tr bgcolor="#EAEAEA">
                                        <td colspan="4"><strong style="font-size:12pt;">Basic Information</strong></td>
                                    </tr>                            
                            <tr>
                                <td><label for="orderNumber">Name:</label></td>
                                <td colspan="3">${empName}</td>
                            </tr>
                            <tr>
                                <td><label for="orderDate"> Designation:</label></td>
                                <td colspan="3">${designation}</td>
                            </tr>
                            <tr>
                                <td><label for="orderNumber">Place of Visit:</label></td>
                                <td>${ltBean.visitPlace}</td>
                                <td><label for="orderDate">Date of Commencement:</label></td>
                                <td>${ltBean.dateOfCommencement}</td>
                            </tr>   
                            <tr>
                                <td><label for="orderNumber">Leave Type:</label></td>
                                <td colspan="3">${ltBean.leaveType}</td>
                            </tr>                             
                            <tr>
                                <td><label for="orderNumber">From Date:</label></td>
                                <td>${ltBean.fromDate}</td>
                                <td><label for="orderDate">To Date:</label></td>
                                <td>${ltBean.toDate}</td>
                            </tr>                            
                        </table>

                     
                        

                                                                

                                <table width="100%" class="table tabler-bordered" style="border:1px solid #CCC;font-size:10pt;">
                                    <tr bgcolor="#EAEAEA">
                                        <td></td>
                                        <td colspan="2"><strong style="font-size:12pt;">Details of Place of Visit</strong></td>
                                        <td></td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td width="10"></td>
                                        <td width="20">(a)</td>
                                        <td width="350">Place of Visit</td>
                                        <td width="300">${ltBean.placeofVisit}</td>
                                        <td>&nbsp;</td>
                                    </tr> 
                                    <tr>
                                        <td></td>
                                        <td>(b)</td>
                                        <td>Visit State</td>
                                        <td>${ltBean.visitState}</td>
                                        <td>&nbsp;</td>
                                    </tr> 
                                    <tr>
                                        <td></td>
                                        <td>(c)</td>
                                        <td>Mode of Journey</td>
                                        <td>${ltBean.modeOfJourney}</td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td>(d)</td>
                                        <td>Appropriate Distance both ways</td>
                                        <td>${ltBean.appropriateDistance}</td>
                                        <td>&nbsp;</td>
                                    </tr>                                  
                                </table>
<table class="table table-bordered" style="font-size:10pt;">
                            <thead>
                                   <tr bgcolor="#EAEAEA">
                                        <td colspan="7"><strong style="font-size:12pt;">Family Members</strong></td>
                                    </tr>                                
                                <tr bgcolor="#EAEAEA">
                                    <th>Sl No.</th>
                                    <th>Name</th>
                                    <th>Relationship</th>
                                    <th>Age/DOB</th>
                                    <th>Marital Status</th>
                                    <th>State Govt Emp?</th>
                                    <th>Monthly Income</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${ltBean.familyList}" var="fl" varStatus="theCounter">
                                    <tr>
                                        <td>${theCounter.index+1}</td>
                                        <td>${fl.fMemberName}</td>
                                        <td>${fl.fMemberRelationship}</td>
                                        <td>${fl.fMemberdob}</td>
                                        <td>${fl.fMemberMstatus}</td>
                                        <td>${fl.isStateGovt}</td>
                                        <td>${fl.monthlyIncome}</td>
                                        </tr>
                                
                                </c:forEach>
                            </tbody>
                        </table>     

                                        <table width="100%" class="table tabler-bordered" style="font-size:10pt;border:1px solid #CCC">
                                            <tr bgcolor="#EAEAEA">
                                                <td></td>
                                                <td colspan="3"><strong style="font-size:12pt;">Total reimbursable estimated cost of journey, both ways</strong></td>
                                                <td>&nbsp;</td>
                                            </tr>
                                            <tr>
                                                <td width="10"></td>
                                                <td width="20">(a)</td>
                                                <td width="350">Approximate fare by train</td>
                                                <td width="300">${ltBean.costByTrain}</td>
                                                <td>&nbsp;</td>
                                            </tr> 
                                            <tr>
                                                <td></td>
                                                <td>(b)</td>
                                                <td>Approximate fare by Road</td>
                                                <td>${ltBean.costByRoad}</td>
                                                <td>&nbsp;</td>
                                            </tr> 
                                            <tr>
                                                <td></td>
                                                <td>(c)</td>
                                                <td>Approximate fare by other means of travel</td>
                                                <td>${ltBean.costByOther}</td>
                                                <td>&nbsp;</td>
                                            </tr>
                                        </table>

                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-4">
                                        <label for="orderNumber">Amount of Advance Applied for:<span style="color: red">*</span></label>
                                    </div>
                                    <div class="col-lg-4">   
                                        ${ltBean.advanceAmount}
                                    </div>
                                </div>
                                    <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-4">
                                        <label for="orderDate"> Any other Relevant Information required by Sanctioning Authority:<span style="color: red">*</span></label>
                                    </div>
                                    <div class="col-lg-8">
                                        ${ltBean.anyOtherInfo}
                                    </div>
                                </div>  

                                        
                        
                    </div>


                </div>
            </div>




        </form:form>
            </div>
        </div>
               
       
    </body>
</html>
