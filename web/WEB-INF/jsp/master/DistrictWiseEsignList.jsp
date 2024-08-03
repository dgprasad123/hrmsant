<%-- 
    Document   : OfficeList
    Created on : Nov 18, 2017, 1:02:16 PM
    Author     : manisha
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">  
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script src="js/moment.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                $('#txtperiodFrom').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#txtperiodTo').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            });
        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <div class="container-fluid">
                    <!-- Page Heading -->
                    <div class="row">
                        <div class="col-lg-12">                            
                            <ol class="breadcrumb">
                                <li>
                                    <i class="fa fa-dashboard"></i>  <a href="index.html">Dashboard</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> District Wise Esign List 
                                </li>
                                <li class="active">
                                    <c:if test="${LoginUserType eq 'A' }">
                                        <i class="fa fa-file"></i> <a href="newPost.htm">New Post</a>
                                    </c:if>
                                </li>
                            </ol>
                        </div>
                    </div>
                    <form:form role="form" action="getDistrictWiseEsignList.htm" commandName="districtWiseEsignBean" method="get" >

                        <div class="row" style="margin-bottom: 7px;">
                          
                            <div class="col-lg-1">
                                <label for="postingUnintDoj">Month:<span style="color: red">*</span></label>
                            </div>

                            <div class="col-lg-2">
                                 <Select name="month"  class="form-control" required="1">
                                <option value="">Select Month</option>
                                <option value="0"  <c:if test = "${not empty selectmonth && selectmonth=='0'}"> <c:out value='selected="selected"'/></c:if>>January </option>
                                <option value="1"  <c:if test = "${not empty selectmonth && selectmonth=='1'}"> <c:out value='selected="selected"'/></c:if>>February</option>
                                <option value="2" <c:if test = "${not empty selectmonth && selectmonth=='2'}"> <c:out value='selected="selected"'/></c:if>>March</option>
                                <option value="3" <c:if test = "${not empty selectmonth && selectmonth=='3'}"> <c:out value='selected="selected"'/></c:if>>April</option>
                                <option value="4" <c:if test = "${not empty selectmonth && selectmonth=='4'}"> <c:out value='selected="selected"'/></c:if>>May</option>
                                <option value="5" <c:if test = "${not empty selectmonth && selectmonth=='5'}"> <c:out value='selected="selected"'/></c:if>>June</option>
                                <option value="6" <c:if test = "${not empty selectmonth && selectmonth=='6'}"> <c:out value='selected="selected"'/></c:if>>July</option>
                                <option value="7"  <c:if test = "${not empty selectmonth && selectmonth=='7'}"> <c:out value='selected="selected"'/></c:if>>August</option>
                                <option value="8" <c:if test = "${not empty selectmonth && selectmonth=='8'}"> <c:out value='selected="selected"'/></c:if>>September</option>
                                <option value="9" <c:if test = "${not empty selectmonth && selectmonth=='9'}"> <c:out value='selected="selected"'/></c:if>>October</option>
                                <option value="10" <c:if test = "${not empty selectmonth && selectmonth=='10'}"> <c:out value='selected="selected"'/></c:if>>November</option>
                                <option value="11" <c:if test = "${not empty selectmonth && selectmonth=='11'}"> <c:out value='selected="selected"'/></c:if>>December</option>
                                </select>
                            </div>
                            <div class="col-lg-1">
                                <label for="postingUnintDoj">To Date:<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                               <Select name="year"  class="form-control" required="1">
                                <option value="">Select Year</option>
                                <option value="2018" <c:if test = "${not empty selectyear && selectyear=='2018'}"> <c:out value='selected="selected"'/></c:if>>2018 </option>
                                <option value="2019"  <c:if test = "${not empty selectyear && selectyear=='2019'}"> <c:out value='selected="selected"'/></c:if>>2019 </option>
                                <option value="2020"  <c:if test = "${not empty selectyear && selectyear=='2020'}"> <c:out value='selected="selected"'/></c:if>>2020 </option>
                                <option value="2021"  <c:if test = "${not empty selectyear && selectyear=='2021'}"> <c:out value='selected="selected"'/></c:if>>2021 </option>
                                <option value="2022"  <c:if test = "${not empty selectyear && selectyear=='2022'}"> <c:out value='selected="selected"'/></c:if>>2022 </option>
                                <option value="2023"  <c:if test = "${not empty selectyear && selectyear=='2023'}"> <c:out value='selected="selected"'/></c:if>>2023 </option>
                                <option value="2024"  <c:if test = "${not empty selectyear && selectyear=='2024'}"> <c:out value='selected="selected"'/></c:if>>2024 </option>
                                </select> 
                            </div>
                            <div class="col-lg-2">
                                <input type="submit" name="action" value="Search" class="btn btn-success"/>
                            </div>
                        </div>
                    </form:form>   
                    <div class="row">
                        <div class="col-lg-12">
                            <h2>District wise E-sign/D-sign List</h2>
                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr>
                                            <th>Sl No</th>
                                            <th>Office Code</th>
                                            <th>Office Name</th>
                                            <th>No. of Dsign</th>
                                            <th>No. of Esign</th>


                                        </tr>
                                    </thead>
                                    <tbody> 
                                        <c:set var="totaldsign" value="${0}"/>
                                        <c:set var="totalesign" value="${0}"/>

                                        <c:forEach items="${esignList}" var="post" varStatus="count">
                                            <c:set var="totaldsign" value="${totaldsign + post.noofDsign}" />
                                            <c:set var="totalesign" value="${totalesign + post.noofEsign}" />

                                            <tr>
                                                <td>${count.index + 1}</td>
                                                <td>${post.officeCode}</td>
                                                <td>${post.officeName}</td>


                                                <td <c:if test="${post.noofDsign!=0}">  </c:if> >


                                                    ${post.noofDsign}

                                                </td> 
                                                <td <c:if test="${post.noofEsign!=0}">  </c:if> >

                                                    ${post.noofEsign}
                                                </td>                                             
                                            </tr>
                                        </c:forEach>
                                        <tr>
                                            <td colspan="3" style="text-align:right"><b>Total:</b></td>
                                            <td>
                                                ${totaldsign}
                                            </td>
                                            <td>
                                                ${totalesign}
                                            </td>
                                        </tr>                  
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


