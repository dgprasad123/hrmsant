<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/sb-admin.css" rel="stylesheet" type="text/css">
        <script src="js/jquery.min.js" type="text/javascript"></script>
        <script src="js/bootstrap.min.js" type="text/javascript"></script>

    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/deptmismenu.jsp"/>                
            <div id="page-wrapper">

                <div class="container-fluid">
                    <!-- Page Heading -->
                    
                    <div class="row">
                        <div class="col-lg-12">
                            <h2>Monthly Activity Court Case Report</h2>
                             <form:form role="form" action="miscommissionCourtCaseList.htm" commandName="miscommissionCourtCaseList"  method="post">
                              <table width="100%" cellspacing="1" cellpadding="4" border="0" bgcolor="#EAEAEA" class="form-table"  >

                                        <tr>
                                            <td align="right">Month:</td>
                                            <td>
                                                <Select name="month"  class="form-control" required="1">
                                                    <option value="">Select Month</option>
                                                    <option value="January"  <c:if test = "${not empty selectmonth && selectmonth=='January'}"> <c:out value='selected="selected"'/></c:if>>January </option>
                                                    <option value="February"  <c:if test = "${not empty selectmonth && selectmonth=='February'}"> <c:out value='selected="selected"'/></c:if>>February</option>
                                                    <option value="March" <c:if test = "${not empty selectmonth && selectmonth=='March'}"> <c:out value='selected="selected"'/></c:if>>March</option>
                                                    <option value="April" <c:if test = "${not empty selectmonth && selectmonth=='April'}"> <c:out value='selected="selected"'/></c:if>>April</option>
                                                    <option value="May" <c:if test = "${not empty selectmonth && selectmonth=='May'}"> <c:out value='selected="selected"'/></c:if>>May</option>
                                                    <option value="June" <c:if test = "${not empty selectmonth && selectmonth=='June'}"> <c:out value='selected="selected"'/></c:if>>June</option>
                                                    <option value="July" <c:if test = "${not empty selectmonth && selectmonth=='July'}"> <c:out value='selected="selected"'/></c:if>>July</option>
                                                    <option value="August"  <c:if test = "${not empty selectmonth && selectmonth=='August'}"> <c:out value='selected="selected"'/></c:if>>August</option>
                                                    <option value="September" <c:if test = "${not empty selectmonth && selectmonth=='September'}"> <c:out value='selected="selected"'/></c:if>>September</option>
                                                    <option value="October" <c:if test = "${not empty selectmonth && selectmonth=='October'}"> <c:out value='selected="selected"'/></c:if>>October</option>
                                                    <option value="November" <c:if test = "${not empty selectmonth && selectmonth=='November'}"> <c:out value='selected="selected"'/></c:if>>November</option>
                                                    <option value="December" <c:if test = "${not empty selectmonth && selectmonth=='December'}"> <c:out value='selected="selected"'/></c:if>>December</option>
                                                    </select>
                                                </td>
                                                <td align="right">Year:</td>
                                                <td>
                                                    <Select name="year"  class="form-control" required="1">
                                                        <option value="">Select Year</option>
                                                        <option value="2017" <c:if test = "${not empty selectyear && selectyear=='2017'}"> <c:out value='selected="selected"'/></c:if>>2017 </option>
                                                    <option value="2018"  <c:if test = "${not empty selectyear && selectyear=='2018'}"> <c:out value='selected="selected"'/></c:if>>2018 </option>
                                                      <option value="2019"  <c:if test = "${not empty selectyear && selectyear=='2019'}"> <c:out value='selected="selected"'/></c:if>>2019 </option>
                                                        <option value="2020"  <c:if test = "${not empty selectyear && selectyear=='2020'}"> <c:out value='selected="selected"'/></c:if>>2020 </option>
                                                         <option value="2021"  <c:if test = "${not empty selectyear && selectyear=='2021'}"> <c:out value='selected="selected"'/></c:if>>2021 </option>
                                                          <option value="2022"  <c:if test = "${not empty selectyear && selectyear=='2022'}"> <c:out value='selected="selected"'/></c:if>>2022 </option>
                                                    </select>   
                                                </td>
                                            </tr> 
                                            <tr><td>&nbsp;</td></tr>
                                            <tr>
                                                <td colspan="4" align="center"><input type="submit" class="btn btn-primary" value="Submit"  /></td>
                                            </tr>
                                            <tr><td>&nbsp;</td></tr>
                                        </table> 
                                        </form:form>
                            <div class="table-responsive">
                                <table id="account" class="table table-bordered table-striped" >
                                    <thead>
                                        <tr style='background-color:black;color:white;font-size:14px'>
                                           
                                            <th>Department</th>
                                            <th>Post</th>
                                            <th>Court Case <br/>No<br/>And Date</th>
                                            <th>Date Of <br/>Submission of <br/>PWC</th>
                                            <th>Date of <br/>Filling Counter <br/>Affedevit</th>
                                            <th>Date of <br/>Interim<br/> Order</th>
                                            <th>Date of <br/>Final <br/>Judgement</th>     
                                            <th>Stay Order<br/> Passed<br/> By order</th>
                                            <th>Steps Taken <br/>For Vacation<br/> Of Stay</th>
                                            <th>Date Of <br/>Vacation of <br/>Stay</th>
                                             <th>Date Of <br/>Publication Of<br/> Final Result</th>
                                            <th>Date Of <br/>Sponsoring of <br/>Candidates</th>
                                            
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${ccourtcaseList}" var="ccourtcaseList">
                                            <tr scope="row">

                                                                                  
                                                <td>${ccourtcaseList.departmentId}</td>                                                
                                                <td>${ccourtcaseList.postId}</td>
                                                <td>${ccourtcaseList.courtCaseNo}<br/>${ccourtcaseList.courtCaseDate}</td>
                                                <td>${ccourtcaseList.pwcDate}</td>
                                                <td>${ccourtcaseList.affedevitdate}</td>  
                                                 <td>${ccourtcaseList.interimorder}</td>  
                                                  <td>${ccourtcaseList.judgementDate}</td>  
                                                  <td>${ccourtcaseList.orderPassed}</td>  
                                                  <td>${ccourtcaseList.stepsStay}</td> 
                                                   <td>${ccourtcaseList.stayDate}</td>  
                                                  <td>${ccourtcaseList.finalResultDate}</td>  
                                                    <td>${ccourtcaseList.sponsoringDate}</td>  
                                                
                                            </tr>
                                        </c:forEach>
                                    </tbody>	
                                </table>

                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </body>
</html>