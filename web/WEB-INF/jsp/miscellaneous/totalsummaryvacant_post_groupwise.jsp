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
                                <h2 style="margin-top:0px;">Group Wise Vacant Post Summary ${misStatus} </h2>
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
                                <c:if test = "${not empty selectyear}">        
                                    <table id="account" class="table table-bordered table-striped" >
                                        <thead>
                                            
                                            <tr style='background-color:black;color:white;font-size:14px'>
                                                 <th valign='top'>Department  Name</th>
                                                <th valign='top'>Group Name</th>
                                                 <th>Sanction Strength of Base Level Post</th>
                                                   <th>Men in Position</th>
                                                <th>${TextValue1} </th>
                                                <th> ${TextValue2}</th>
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
                                                <c:set var="proDeptPostBalanceTotal" value="${0}" />                                               <c:set var="sancPostTotal" value="${0}" />
                                                 <c:set var="maninPostTotal" value="${0}" />   
                                                    <c:set var="bpost" value="${0}" />   
                                                <c:forEach items="${vacantPostList}" var="vacantPost">
                                                    
                                                    <c:set var="postClearedTotal" value="${postClearedTotal + vacantPost.postCleared}" />
                                                    <c:set var="baseLevelPostTotal" value="${baseLevelPostTotal + vacantPost.baseLevelPost}" />
                                                    <c:set var="vacRetirmentTotal" value="${vacRetirmentTotal + vacantPost.vacRetirment}" />

                                                    <c:set var="postFilledUpTotal" value="${postFilledUpTotal + vacantPost.postFilledUp}" />
                                                    <c:set var="totalpostFilledUp" value="${totalpostFilledUp + vacantPost.totalpostFilledUp}" />
                                                   

                                                    <c:set var="propostRequisitionSentTotal" value="${propostRequisitionSentTotal + vacantPost.propostRequisitionSent}" />
                                                    <c:set var="deptPostCommTotal" value="${deptPostCommTotal + vacantPost.deptPostComm}" />
                                                    <c:set var="proDeptPostBalanceTotal" value="${proDeptPostBalanceTotal + vacantPost.proDeptPostBalance}" />
                                                     <c:set var="sancPostTotal" value="${sancPostTotal + vacantPost.sancPost}" />
                                                      <c:set var="maninPostTotal" value="${maninPostTotal + vacantPost.maninPost}" />
                                                   <c:set var="bpost" value="${vacantPost.postFilledUp - vacantPost.totalpostFilledUp}" />
                                                    <c:set var="postsBalanceTotal" value="${postsBalanceTotal + bpost}" />
                                                      <tr scope="row">
                                                           <td>${vacantPost.deptName}</td> 
                                                        <td><a href='#'>${vacantPost.groupName}</a></td>
                                                         <td>${vacantPost.sancPost}</td> 
                                                           <td>${vacantPost.maninPost}</td> 
                                                        <td>${vacantPost.postCleared}</td>                                                
                                                        <td>${vacantPost.baseLevelPost}</td>
                                                        <td>${vacantPost.vacRetirment}</td>

                                                        <td>${vacantPost.postFilledUp}</td>
                                                        <td>${vacantPost.totalpostFilledUp}</td>
                                                        <td>${vacantPost.postsBalance}</td>				
                                                        <td>${vacantPost.propostRequisitionSent}</td>
                                                        <td>${vacantPost.deptPostComm}</td>
                                                        <td>${vacantPost.proDeptPostBalance}</td>




                                                    </tr>
                                                </c:forEach>
                                                <tr style='background-color:black;color:white;font-size:14px'>
                                                    <th>&nbsp;</th>
                                                    <th>&nbsp;</th>
                                                     <th>${sancPostTotal}</th>
                                                      <th>${maninPostTotal}</th>
                                                    <th>${postClearedTotal}</th>
                                                    <th>${baseLevelPostTotal}</th>
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
                                                    <th colspan="11" style='color:red' align='center'><h3>Data is not available</h3></th>
                                            </tr>
                                        </c:if>     
                                        </tbody>	
                                    </table>  
                                </c:if>
                                 <table width="100%" cellspacing="1" cellpadding="4" border="0" bgcolor="#EAEAEA" class="form-table" style="border:1px solid #CCCCCC;" >
                                  
                                   <tr>
                                        <td colspan="4" align="center"><input type="submit" class="btn btn-primary" value="Back" onclick="Javascript:window.location='summarydeptVacantPostList.htm?month=${selectmonth}&year=${selectyear}'"  /></td>
                                    </tr>
                                 </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
