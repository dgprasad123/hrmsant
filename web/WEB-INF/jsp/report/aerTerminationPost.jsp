<%-- 
    Document   : ManageSanctionPost
    Created on : 17 May, 2019, 6:22:40 PM
    Author     : Surendra
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>  

        <script type="text/javascript" src="js/bootstrap.min.js"></script>

        <style type="text/css">
            .saveSuccess{
                color: #00cc66;
                font-weight: bold;
            }
            .saveError{
                color: #ff3333;
                font-weight: bold;
            }
            .row{
                margin-left:0px;
                margin-right:0px;
            }
        </style>
    </head>
    <body>

        <form:form action="displaySchedule1Post.htm" method="POST" commandName="command" >
            <form:hidden path="aerId" id="aerId"/>
            <form:hidden path="gpc" id="gpc"/>
            <form:hidden path="hidPostGrp" id="hidPostGrp"/>
            <form:hidden path="financialYear" id="financialYear"/>
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading"></div>
                    <div class="panel-body">
                        <h2 align='center'>${OffName}</h2>
                        <h4 align='center'>Financial Year:${financialYear}</h4>
                        <h6 align='center'>SCHEDULE 1-A</h6>
                        <div align="center">(Relating to Head of the Office)</div>
                          <button type="button" name="btnPTAer" value="Send" class="btn btn-primary" onclick="return back_page()">Back</button>
                          <div style="clear:both;margin:5px 0px">&nbsp;</div>
                        <table class="table table-striped table-bordered" width="90%">
                            <thead>
                                <tr>

                                    <th width="10%" rowspan="2" >Category of Employee</th>
                                    <th width="25%" rowspan="2">Description of the Posts</th>
                                    <th width="20%" colspan="2">Pay Scale</th>
                                    <th  width="5%" rowspan="2">As per 7th Pay Commission</th>
                                    <th width="8%" rowspan="2">Sanctioned Strength</th>
                                    <th width="8%" rowspan="2">Persons-in-Position</th>
                                    <th width="9%" rowspan="2">Vacancy Position </th>
                                    <th width="5%" rowspan="2"> </th>
                                </tr>
                                <tr>
                                    <th>As per 6th Pay Commission</th>
                                    <th>GP</th>
                                </tr>
                            </thead>

                            <c:forEach var="partAGrpA" items="${PartAGrouplist}">
                                <tr>

                                    <td> ${partAGrpA.group}  </td>
                                    <td> ${partAGrpA.postname} </td>
                                    <td> ${partAGrpA.scaleofPay} </td>
                                    <td> ${partAGrpA.gp} </td>
                                    <td> ${partAGrpA.scaleofPay7th} </td>
                                    <td> ${partAGrpA.sanctionedStrength} </td>
                                    <td> ${partAGrpA.meninPosition} </td>
                                    <td> ${partAGrpA.vacancyPosition} </td>
                                    <td><input type="checkbox" name="chkGpc" value="${partAGrpA.gpc}" class="chkGpcbox" />  </td>

                                </tr>
                            </c:forEach>  



                        </table>
                        <div class="p-3 mb-2 bg-danger text-white" style='color:white;background-color:red;height:50px;padding:5px'>
                            <div style='margin-top:10px'>
                                <input type="checkbox" name="chkGpc" value="NA" class="chkGpcbox" /> <strong>Click here if there is No POST Termination Proposal</strong>
                            </div>
                        </div>   
                         <c:if test="${cntRecord eq '0'}">
                        <div >
                            <h5 style='color:red'>Please click Next button for  termination of post proposal </h5>

                        </div>
                       
                            <div class="modal-footer">
                                <button type="Submit" class="btn btn-primary" onclick="return validate()" >Next</button>
                            </div>
                        </c:if>
                    </div>

                </div>

            </div>



        </form:form>

        <script>
            function validate() {
                var checkgpcvalue = $('.chkGpcbox:checkbox:checked').length;
                if (checkgpcvalue == 0) {
                    alert("Please select at least one checkbox for post termination");
                    return false;
                }
                var checkedValue = $('.chkGpcbox:checkbox:checked').val();                
                if(checkedValue=="NA"){
                    var con=confirm("Do you want to submit without Post Termination Proposal");
                    if(con){
                        return true;
                    } else {
                        return false;
                    }                
                }
               
            }
             function back_page(){                
                window.location="listingTerminationPost.htm?financialYear=${financialYear}";
            }
        </script> 

    </body>
</html>

