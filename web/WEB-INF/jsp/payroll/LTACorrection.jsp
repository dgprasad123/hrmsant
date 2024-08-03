<%-- 
    Document   : ltaCorrection
    Created on : May 31, 2018, 4:29:31 AM
    Author     : Madhusmita
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script src="js/bootstrap-datetimepicker.js" type="text/javascript"></script>
        <title>Unlock Bill</title>        
        <script type="text/javascript">
            $(document).ready(function () {
                
            });
            
            function validate(){
                alert('Hi');
            }

        </script>
    </head>
    <body>
        <form:form action="ltaCorrection.htm" commandName="ltaCorrection" method="POST">
            <div id="wrapper">
                <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
                <div id="page-wrapper">     

                    <div class="container-fluid">
                        <div class="row">
                            <div class="col-lg-12">                            
                                <ol class="breadcrumb">
                                    <li>
                                        <i class="fa fa-dashboard"></i>  <a href="#">Dashboard</a>
                                    </li>
                                    <li class="active">
                                        <i class="fa fa-file"></i> Loan Correction 
                                    </li>                                
                                </ol>
                            </div>
                        </div>
                        <div class="row"> 
                            <div class="col-sm-12">
                                <input type="hidden" name="empid" id="empid" value="${empid}}"/> 
                                <input type="hidden" name="loantp" id="loantp" value="${loantp}"/>
                                <table border="0" width="80%" class="table" cellspacing="0" style="font-size:12px; font-family:verdana;">
                                    <tr>
                                        <td>
                                            <form:label path="loanTp">LOAN TYPE :</form:label>
                                            </td>
                                            <td>
                                            <form:select class="form-control" path="loanName">
                                                <form:option value="">--Select Bill Type--</form:option>
                                                <form:option value="HBA">HOUSE BUILDING ADVANCE</form:option>
                                                <form:option value="MCA">MOTOR CYCLE  ADVANCE</form:option>
                                                <form:option value="MOPA">MOPED ADVANCE</form:option>
                                                <form:option value="CMPA">COMPUTER ADVANCE</form:option>
                                                <form:option value="VE">VEHICLE/MOTOR/CAR ADVANCE</form:option>                                                   
                                            </form:select>
                                        </td>                                     

                                        <%--<td><a href="ltaCorrection.htm?loanTp=${loantype.loanTp}" class="btn btn-primary" target="_blank">Search</a></td>                                        --%>
                                        <td>
                                            <input type="submit" class="form-control" value="Search"/>                                        
                                        </td>
                                    </tr>
                                </table>

                            </div>
                        </div>
                        <div class="clearfix"></div>
                        <div class="row">

                            <div class="col-sm-4">



                                <div class="table-responsive">
                                    <table class="table table-bordered table-hover table-striped">
                                        <thead>
                                            <tr>
                                                <th class="col-sm-2" >SL NO</th>                                  
                                                <th class="col-sm-10">EMPLOYEE NAME AND ACCOUNT NO</th>                                                                 
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:if test="${not empty loanList}">
                                                <c:forEach items="${loanList}" var="eloan" varStatus="count">
                                                    <tr>                                               
                                                        <td class="form-group" >
                                                            <c:out value="${count.index + 1}"/>
                                                        </td>                                                            
                                                        <td class="form-group">
                                                            <a href="ltaCorrectionData.htm?">
                                                                <c:out value="${eloan.empName}"/> /
                                                                <c:out value="${eloan.gpfNo}"/>
                                                            </a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </c:if>
                                        </tbody>
                                    </table>

                                </div>


                            </div>
                            <div class="col-sm-8">
                                <div class="row">
                                    <div class="col-sm-12">
                                        <h3 for="sel1" style="text-align: center;">FROM AG DETAILS</h3>
                                    </div> 
                                </div>
                                <div class="clearfix"></div>
                                <div class="row">
                                    <div class="col-sm-3">
                                        <label> Sanction Number </label>

                                    </div> 
                                    <div class="col-sm-3">
                                        <label> Sanction Date </label>
                                    </div> 
                                    <div class="col-sm-3">
                                        <label> Loan Sub Type </label>
                                    </div> 
                                    <div class="col-sm-3">
                                        <label> Instalment </label>
                                    </div> 
                                </div>
                                <div class="clearfix"></div>
                                <div class="row">
                                    <div class="col-sm-3">
                                        11
                                        <input type="hidden" value="1000" id="sncnoAG"/>
                                    </div> 
                                    <div class="col-sm-3">
                                        13-jun-2018 
                                        <input type="hidden" value="1000" id="sncndtAG"/>
                                    </div> 
                                    <div class="col-sm-3">
                                        HBA-PART 
                                        <input type="hidden" value="1000" id="loansubTypAG"/>
                                    </div> 
                                    <div class="col-sm-3">
                                        1000
                                        <input type="hidden" value="1000" id="instAmtAG"/>
                                    </div> 
                                </div>
                                <div class="clearfix"></div>
                                <div class="row">
                                    <div class="col-sm-12">
                                        <h3 for="sel1" style="text-align: center;">FROM HRMS DETAILS</h3>
                                    </div> 
                                </div>
                                <div class="clearfix"></div>
                                <div class="row">
                                    <div class="col-sm-3">
                                        <label> Sanction Number </label>

                                    </div> 
                                    <div class="col-sm-3">
                                        <label> Sanction Date </label>
                                    </div> 
                                    <div class="col-sm-3">
                                        <label> Loan Sub Type </label>
                                    </div> 
                                    <div class="col-sm-3">
                                        <label> Instalment </label>
                                    </div> 
                                </div>
                                <div class="clearfix"></div>
                                <div class="row">
                                    <div class="col-sm-3">
                                        <div class="form-group">
                                            <input type="text" id="sncno" class="form-control" placeholder="Sanction No">
                                        </div>

                                    </div> 
                                    <div class="col-sm-3">
                                        <div class="form-group">
                                            <div class='input-group date' id='sncndt'>
                                                    <input class="form-control" id="sncndt" name="sncndt" />
                                                <span class="input-group-addon">
                                                    <span class="glyphicon glyphicon-time"></span>
                                                </span>
                                            </div>
                                        </div> 
                                    </div> 
                                    <div class="col-sm-3">
                                        <div class="form-group">
                                            <input type="text" id="loansubTyp" class="form-control" placeholder="Loan SubType">
                                        </div>
                                    </div> 
                                    <div class="col-sm-3">
                                        1000 
                                        <input type="hidden" value="1000" id="instAmtHRMS"/>
                                    </div> 
                                </div>
                                <div class="row">
                                    <div class="col-md-12 center-block">
                                        <button type="button" id="singlebutton" name="singlebutton" class="btn btn-success center-block" onclick="validate()">Update</button>
                                    </div>

                                </div>
                            </div>
                        </div>

                    </div>                  
                </div>
            </div>
        </div>
    </form:form>
</body>
</html>
