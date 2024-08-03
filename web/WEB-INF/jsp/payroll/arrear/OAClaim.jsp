<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">                
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script type="text/javascript">

            $(window).load(function () {

                $("#getOaData").on("show.bs.modal", function (e) {
                    var link = $(e.relatedTarget);
                    $(this).find(".modal-body").load(link.attr("href"));
                });

                $('#getOaData').on('hidden.bs.modal', function () {

                    //self.location='OaClaimReport.htm';

                });
            })

            function validate()
            {
                if ($("#billGroupName").val() == "")
                {
                    alert("Please Select Bill Group Name");
                    $("#billGroupName").focus();
                    return false;
                }
            }

        </script>
    </head>
    <body>
        <form:form id="fm" action="OaClaimReport.htm" method="post" name="myForm" commandName="OAClaimModel">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading" align="center" style="background-color: #0E8DE8;color: #ffffff;font-size: 15px;"> 
                        OA CLAIM EMPLOYEE LIST  
                    </div>

                    <div class="row">
                        <div class="col-lg-12">
                            <table class="table table-bordered table-hover table-striped">
                                <tr>
                                    <td>&nbsp; </td>                                            
                                    <td style="width: 30%;" align="center"> Bill Group Name </td>
                                    <td style="width: 30%">
                                        <form:select path="billGroupName" id="billGroupName" class="form-control">
                                            <form:option value="" label="Select" cssStyle="width:30%"/>
                                            <c:forEach items="${billGroup}" var="bg">
                                                <form:option value="${bg.billgroupid}" label="${bg.billgroupdesc}"/>
                                            </c:forEach>                                 
                                        </form:select>
                                    </td>
                                    <td><input type="submit" value="Search" name="search" class="btn btn-primary" onclick="return validate()"/></td>
                                </tr>
                            </table>
                        </div>
                    </div>

                    <div class="table-responsive" style="border:1px solid #0E8DE8;">
                        <table class="table table-bordered table-hover table-striped" style="font-size: 15px;">
                            <thead>
                                <tr>
                                    <th width="3%">Sl No</th>
                                    <th width="4%">HRMS ID</th>
                                    <th width="14%">EMPLOYEE NAME</th>
                                    <th width="16%">DESIGNATION</th>
                                    <th width="9%">PERIOD</th>
                                    <th width="7%">AMOUNT</th>
                                    <th width="5%">ACTION</th>
                                </tr>
                            </thead>
                            <tbody id="dataId">
                                <c:forEach items="${oaClaimList}" var="emp" varStatus="count">
                                    <tr>
                                        <td align="center"> <b>${count.index + 1}</b> </td>
                                        <td> ${emp.hrmsId} </td>
                                        <td> ${emp.empName}  </td>
                                        <td> ${emp.post} </td>
                                        <td id="P${emp.hrmsId}"> ${emp.fromMonth} - ${emp.toMonth}</td>
                                        <td id="A${emp.hrmsId}"> ${emp.txtOaAmount} </td>
                                        <td align="center"> 
                                            <a href="oaClaimData.htm?empId=${emp.hrmsId}&bid=${emp.billGroupName}" data-remote="false" data-toggle="modal" title="View Status" data-target="#getOaData" class="btn btn-default"> Update </a>

                                            <!-- <button type="button" class="btn btn-primary" data-toggle="modal" 
                                                    data-target="#addClaimModal" onclick="getEmpDetails('${emp.hrmsId}')"> Update </button> -->
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <!-- Get OA Claim Employee Data Modal -->
            <div id="getOaData" class="modal fade" role="dialog">
                <div class="modal-dialog" style="width:1000px;">

                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title"> OA CLAIM </h4>
                        </div>
                        <div class="modal-body">

                        </div>
                        <div class="modal-footer">
                            <span id="msg"></span>                        
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>

                </div>
            </div>

        </form:form>
    </body>
</html>
