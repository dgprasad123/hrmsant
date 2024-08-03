<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script  type="text/javascript">
            function deleteCandidate(candidateId, categoryId)
            {
                if (confirm("Are you sure you want to delete the Candidate?"))
                    self.location = 'deleteCandidate.htm?candidateId=' + candidateId + '&categoryId=' + categoryId;
            }

        </script>
        <style type="text/css">
            table.form-table td{padding:5px;}
        </style>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/deptmismenu.jsp"/>               
            <!-- Site wrapper -->
            <div class="wrapper">            
                <div id="page-wrapper">
                    <div class="container-fluid">
                        <!-- Page Heading -->
                        <div class="row">
                            <div class="col-lg-12">                            

                            </div>
                        </div>
                        <div class="row" style="min-height:600px">
                            <div class="col-lg-12">
                                <h2 style="margin-top:0px;">Department Wise BLEO Recruitment MIS </h2>

                                <form:form role="form" action="deptBleoList.htm" commandName="deptRaSchemeList"  method="post">

                                    <table width="100%" cellspacing="1" cellpadding="4" border="0" bgcolor="#EAEAEA" class="form-table" style="border:1px solid #CCCCCC;" >
                                        <tr>
                                            <td align="right">Department Name:</td>
                                            <td colspan="3">
                                                <Select name="departmentId" id="departmentId"  class="form-control" onchange="javascript: getDeptwiseCadreList()" required>
                                                    <option value="">Select Department</option>
                                                    <c:forEach items="${departmentList}" var="department">
                                                        <option value="${department.value}"  <c:if test = "${not empty selectdeptid && selectdeptid==department.value}"> <c:out value='selected="selected"'/></c:if>>${department.label}</option>
                                                    </c:forEach>

                                                </select>  

                                            </td>

                                        </tr> 
                                                                            
                                            <tr>
                                                <td colspan="4" align="center"><input type="submit" class="btn btn-primary" value="Submit"  /></td>
                                            </tr>
                                        </table>
                                        <!-- /.box-body -->


                                </form:form>
                                  <c:if test = "${not empty selectdeptid}">        
                                <table id="account" class="table table-bordered table-striped" >
                                    <thead>
                                        <tr style='background-color:black;color:white;font-size:14px'>
                                             <th colspan="2" align='center'>Department Wise BLEO Entry Status For month Of ${selectmonth}, ${selectyear}</th>
                                           
                                        </tr>
                                        <tr style='background-color:black;color:white;font-size:14px'>
                                            <th style="width:70%">Department Name</th>
                                            <th style="width:30%">Status</th>
                                                                         
                                          
                                        </tr>
                                    </thead>
                                    <tbody>
                                          <c:if test = "${not empty bleoList}"> 
                                         <c:forEach items="${bleoList}" var="bleo">
                                            <tr>
                                            <td>${bleo.deptId}</td>
                                            <td> <c:if test = "${not empty bleo.recruitEoId && bleo.recruitEoId >='1'}">  <strong style='color:green'>Submitted</strong></c:if><c:if test = "${not empty bleo.recruitEoId && bleo.recruitEoId=='0'}">  <strong style='color:red'>Pending</strong></c:if></td>


                                      </tr>
                                        </c:forEach>
                                       </c:if>
                                        <c:if test = "${empty bleoList}">
                                              <tr>
                                                  <th colspan="10" style='color:red' align='center'><h3>Data is not available</h3></th>
                                              </tr>
                                        </c:if>     
                                    </tbody>	
                                </table>  
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
