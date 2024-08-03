<%-- 
    Document   : BudgetProposoalReportForDDO
    Created on : 26 Sep, 2019, 3:08:23 PM
    Author     : Surendra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <title>HRMS</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
    </head>
    <body>
        <form:form action="createBudgetProposal.htm" commandName="budget">
        <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="col-lg-12">
                                <h1 align="center">  </h1>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-lg-12">
                                <h3 align="center"> Financial Year 
                                    <form:select path="financialYear" id="financialYear">
                                        <option value=""> Select One </option>
                                        <form:options items="${financialYearList}" itemLabel="label" itemValue="value"/> 
                                    </form:select>
                                        
                                        


                                    <button type="submit" name="btnAer" value="Search" class="btn btn-primary" onclick="return validate()">Show Proposal List</button>   
                                </h3> 
                            </div>
                        </div>    
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th>Sl No</th>
                                    <th>Financial Year</th>
                                    <th>Submitted On</th>
                                    <th>Status</th>
                                    <th></th>
                                    <th colspan="3" align="center">Action</th>

                                </tr>
                            </thead>
                            <tbody>
                                
                                    <tr>
                                        <td>  </td>
                                        <td>  </td>
                                        <td>  </td>
                                        <td> 
                                            
                                           
                                        </td>
                                        <td>
                                            
                                        </td>
                                        <td> &nbsp;
                                            
                                        </td>    
                                        <td> &nbsp;
                                            

                                        </td>    
                                        <td> &nbsp;        
                                            

                                        </td>

                                    </tr>
                                
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <c:if test="${empty budget.financialYear}">
                            <button id="btncreate" type="submit" name="btnAer" value="CreateBudgetProposal" class="btn btn-primary " style="display:none" onclick="return validate()">Create Budget Proposal</button>
                        </c:if>
                        <c:if test="${not empty budget.financialYear}">
                            <button id="btncreate" type="submit" name="btnAer" value="CreateBudgetProposal" class="btn btn-primary " style="display:block" onclick="return validate()">Create Budget Proposal</button>
                        </c:if>
                            
                        

                    </div>
                </div>
            </div>

        </form:form>    
   
    </body>
</html>
