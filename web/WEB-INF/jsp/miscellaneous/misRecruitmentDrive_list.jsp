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
                                <h2 style="margin-top:0px;">Department Wise Recruitment Drive MIS </h2>

                                <form:form role="form" action="depReqDriveList.htm" commandName="deptRaSchemeList"  method="post">

                                    <table width="100%" cellspacing="1" cellpadding="4" border="0" bgcolor="#EAEAEA" class="form-table" style="border:1px solid #CCCCCC;" >

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
                                            <tr>
                                                <td colspan="4" align="center"><input type="submit" class="btn btn-primary" value="Submit"  /></td>
                                            </tr>
                                        </table>
                                        <!-- /.box-body -->


                                </form:form>
                                <c:if test = "${not empty selectyear}">        
                                    <table id="account" class="table table-bordered table-striped" >
                                        <thead>
                                            <tr style='background-color:black;color:white;font-size:14px'>
                                                <th colspan="2" align='center'>Department Wise Recruitment Drive Entry Status For month Of ${selectmonth}, ${selectyear}</th>

                                            </tr>
                                            <tr style='background-color:black;color:white;font-size:14px'>
                                                <th style="width:70%">Department Name</th>
                                                <th style="width:30%">Status</th>


                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:if test = "${not empty recruitmentList}"> 
                                                <c:forEach items="${recruitmentList}" var="recruitment">
                                                    <tr>
                                                        <td>${recruitment.deptName}</td>
                                                        <td> <c:if test = "${not empty recruitment.recruitDriveId && recruitment.recruitDriveId >='1'}">  <strong style='color:green'>Submitted</strong></c:if><c:if test = "${not empty recruitment.recruitDriveId && recruitment.recruitDriveId=='0'}">  <strong style='color:red'>Pending</strong></c:if></td>


                                                        </tr>
                                                </c:forEach>
                                            </c:if>
                                            <c:if test = "${empty recruitmentList}">
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
