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
                            <h2>Monthly Activity Pending Requisition Report</h2>

                            <form:form role="form" action="miscommissionPendingList.htm" commandName="miscommissionPendingList"  method="post">
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
                                            <th>Number<br/>Of<br/>Vacancy</th>
                                            <th>Date Of <br/>Receipt of <br/>Requisition</th>
                                            <th>Advertisement <br/>No <br/>Date</th>
                                            <th>Date of <br/>Preliminary<br/> Examination</th>
                                            <th>Date of <br/>Written <br/>Examination</th>     
                                            <th>Date of <br/>Viva-Voce/Skill<br/> Test</th>
                                            <th>Date Of <br/>Publication Of<br/> Final Result</th>
                                            <th>Date Of <br/>Sponsoring of <br/>Candidates</th>
                                            <th>Court Cases If Any</th>

                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${cpendingList}" var="cpendingList">
                                            <tr scope="row">


                                                <td>${cpendingList.departmentId}</td>                                                
                                                <td>${cpendingList.postId}</td>
                                                <td>${cpendingList.noofVan}</td>
                                                <td>${cpendingList.reqDate}</td>
                                                <td>${cpendingList.advNo}<br/> ${cpendingList.advDate}</td>  
                                                <td>${cpendingList.examdate}</td>  
                                                <td>${cpendingList.writtendate}</td>  
                                                <td>${cpendingList.vivavoceDate}</td>  
                                                <td>${cpendingList.finalResultDate}</td> 
                                                <td>${cpendingList.sponsoringDate}</td>  
                                                <td>${cpendingList.coursecaseDetails}</td>  

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