<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";

%>
<script type='text/javascript'>
    function delete_data(ids) {
        var con = confirm("Do you want to delete this information");
        if (con) {
            window.location = "deleteVacantPost.htm?taskId=" + ids;

        }
    }
</script>  

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
            <jsp:include page="../tab/deptadminmenu.jsp"/>        
            <div id="page-wrapper">

                <div class="container-fluid">
                    <!-- Page Heading -->
                    <div class="row">
                        <div class="col-lg-12">                            
                            <ol class="breadcrumb">
                                <li>
                                    <i class="fa fa-dashboard"></i>  <a href="#">Dashboard</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> Department Vacant Post List 
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> <a href="newDeptvacantpost.htm">New Department Vacant Post</a>
                                </li>
                            </ol>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-12">

                            <h2><strong>Department Vacant Post List</h2> 
                            <c:if test = "${misStatus eq 'Y'}">
                                <c:set var = "TextValue" value = "01.06.2019"/> 
                                <c:set var = "TextValue1" value = "No Of Posts cleared after 01.06.2019 "/> 
                                <c:set var = "TextValue2" value = "No Of  cleared Post Prior to 01.06.2019"/> 
                            </c:if>
                            <c:if test = "${misStatus eq 'N'}">
                                <c:set var = "TextValue" value = "01.04.2013"/> 
                                <c:set var = "TextValue1" value = "No Of Posts cleared by F.D/E.C after 01.04.2013 "/> 
                                <c:set var = "TextValue2" value = "No Of Backlog Vacant Post Prior to 01.04.2013"/> 
                            </c:if>
                            <form:form role="form" action="getDeptvacantpostList.htm" commandName="getDeptvacantpostList"  method="post">
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

                                <c:if test = "${not empty selectyear}">        
                                    <table id="account" class="table table-bordered table-striped" >
                                        <thead>
                                            <tr style='background-color:black;color:white;font-size:14px'>
                                                <th colspan="11" align='center'>Base Level Vacant Post (Direct Recruitment)</th>

                                            </tr>
                                            <tr style='background-color:black;color:white;font-size:14px'>
                                                <th>&nbsp;</th>
                                                <th valign='top'>Department Name</th>
                                                <th>${TextValue2} </th>
                                                <th>${TextValue1} </th>
                                                <th>No Of Post Vacant Due to Retirement</th>
                                                <th>Total posts to be filled Up</th>
                                                <th>Total posts filled Up</th>
                                                <th>Balance Posts</th>
                                                <th>No of balance posts for which requisition sent</th>
                                                <th>No of balance posts for which requisition is to be sent</th>
                                                <th>No of balance posts for which recruitment is to be made Departmentally</th>                                        



                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:if test = "${not empty vacantPostList}"> 
                                                <c:set var="postClearedTotal" value="${0}" />
                                                <c:set var="baseLevelPostTotal" value="${0}" />
                                                <c:set var="vacRetirmentTotal" value="${0}" />

                                                <c:set var="postFilledUpTotal" value="${0}" />
                                                <c:set var="totalpostFilledUp" value="${0}" />
                                                <c:set var="postsBalanceTotal" value="${0}" />

                                                <c:set var="propostRequisitionSentTotal" value="${0}" />
                                                <c:set var="deptPostCommTotal" value="${0}" />
                                                <c:set var="proDeptPostBalanceTotal" value="${0}" />
                                                <c:set var="slno" value="${0}" />
                                                <c:set var="bpost" value="${0}" />
                                                <c:set var="proDeptPostBalance" value="${0}" />   
                                                <c:forEach items="${vacantPostList}" var="vacantPost">
                                                    <c:set var="postClearedTotal" value="${postClearedTotal + vacantPost.postCleared}" />
                                                    <c:set var="baseLevelPostTotal" value="${baseLevelPostTotal + vacantPost.baseLevelPost}" />
                                                    <c:set var="vacRetirmentTotal" value="${vacRetirmentTotal + vacantPost.vacRetirment}" />

                                                    <c:set var="postFilledUpTotal" value="${postFilledUpTotal + vacantPost.postFilledUp}" />
                                                    <c:set var="totalpostFilledUp" value="${totalpostFilledUp + vacantPost.totalpostFilledUp}" />


                                                    <c:set var="propostRequisitionSentTotal" value="${propostRequisitionSentTotal + vacantPost.propostRequisitionSent}" />
                                                    <c:set var="deptPostCommTotal" value="${deptPostCommTotal + vacantPost.deptPostComm}" />

                                                    <c:set var="bpost" value="${vacantPost.postFilledUp - vacantPost.totalpostFilledUp}" />
                                                    <c:set var="slno" value="${slno +1}" />
                                                    <c:set var="postsBalanceTotal" value="${postsBalanceTotal + bpost}" />
                                                    <c:set var="proDeptPostBalance" value="${(bpost-(vacantPost.propostRequisitionSent+vacantPost.deptPostComm))}" />
                                                    <c:set var="proDeptPostBalanceTotal" value="${proDeptPostBalanceTotal + proDeptPostBalance}" />
                                                    <tr scope="row">
                                                        <td>${slno}</td>
                                                        <td><a href='summaryvacantdeptgroupwise.htm?deptid=${vacantPost.deptId}&month=${vacantPost.month}&year=${vacantPost.year}&deptname=${vacantPost.deptName}'>${vacantPost.deptName}</a></td>
                                                        <td>${vacantPost.baseLevelPost}</td>
                                                        <td>${vacantPost.postCleared}</td>                                                

                                                        <td>${vacantPost.vacRetirment}</td>

                                                        <td>${vacantPost.postFilledUp}</td>
                                                        <td>${vacantPost.totalpostFilledUp}</td>
                                                        <td>${bpost}</td>				
                                                        <td>${vacantPost.propostRequisitionSent}</td>
                                                        <td>${vacantPost.deptPostComm}</td>
                                                        <td>${proDeptPostBalance}</td>




                                                    </tr>
                                                </c:forEach>
                                                <tr style='background-color:black;color:white;font-size:14px'>
                                                    <th>&nbsp;</th>
                                                    <th>&nbsp;</th>
                                                    <th>${baseLevelPostTotal}</th>
                                                    <th>${postClearedTotal}</th>

                                                    <th>${vacRetirmentTotal}</th>
                                                    <th>${postFilledUpTotal}</th>
                                                    <th>${totalpostFilledUp}</th>
                                                    <th>${postsBalanceTotal}</th>
                                                    <th>${propostRequisitionSentTotal}</th>
                                                    <th>${deptPostCommTotal}</th>
                                                    <th>${proDeptPostBalanceTotal}</th>
                                                </tr>
                                            </c:if>
                                            <c:if test = "${empty vacantPostList}">
                                                <tr>
                                                    <th colspan="13" style='color:red' align='center'><h3>Data is not available</h3></th>
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