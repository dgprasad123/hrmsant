<%-- 
    Document   : viewpersonnelIsectionOfficewise
    Created on : 15 Nov, 2023, 12:15:35 PM
    Author     : Adarsh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script> 
        <script src="js/jquery.freezeheader.js"></script>
        <script src="js/bootstrap.min.js"></script>

        <script type="text/javascript">
            $(document).ready(function() {
                $("#dataTab").freezeHeader();

                $("#btnExport2Tabl").click(function() {
                    var x = $("#dttabl").clone();
                    $("#dttabl").find("tr td a").replaceWith(function() {
                        return $.text([this]);
                    });

                    tableToExcel("dttabl", "BillGroupWiseReport", "BillGroupWiseReport.xls");
                    $("#dttabl").html(x);
                });

            });



            function viewPOStatus(empid) {
                var finTrData = "";
                //alert(empid);
                $('#tppoEmpList').empty();
                var url = 'viewPoJSON.htm?empid=' + empid;
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        finTrData = finTrData + "<tr>" +
                                "<td>" + (i + 1) + "</td>" +
                                "<td>" + obj.trnsferorpost + "</td>" +
                                "<td>" + obj.relivingpost + "</td>" +
                                "<td>" + obj.relivingdate + "</td>" +
                                "<td>" + obj.joiningpost + "</td>" +
                                "<td>" + obj.dateOfJoining + "</td>" +
//                                "<td>" + obj.dos + "</td>" +

                                "</tr>";
                    });

                    //$('#empHeading').append("HRMS ID:").append($("#finalEmpId").val());
                    $('#tppoEmpList').append(finTrData);
                });
            }
        </script>

        <style>
            table{
                border-collapse: collapse;
                border: 1px solid;
            }
            table, th, td {
                border: 1px solid black;
            }
            .modal-ku {
                width: 2000px;
                margin: auto;
            }
        </style>

    </head>
    <body>
        <form:form role="form" action="viewpersonnelIsectionOfficewise.htm" commandName="command" method="post">
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">

                       <h2 style="color:green; text-align: center;"><strong><u>Personnel Section Report</u></strong></h2>


                        <br/><br/>

                        <div class="row" style="margin-bottom: 7px; ">
                            <div class="col-lg-12">
                                <div class="col-lg-3"></div>

                                <div class="col-lg-6">
                                    <form:select path="sltOffCode" id="sltOffCode" class="form-control" onchange="removeDepedentDropdown('A');">
                                        <form:option value="">--Select Office--</form:option>
                                        <form:options items="${officelist}" itemValue="offCode" itemLabel="offName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-3">
                                    <input type="submit" name="btnSubmit" value="Search"/>
                                </div>
                            </div>
                            <br/>
                            <br/>

                            <div class="row">
                                <div class="col-lg-12">
                                    <div class="table-responsive" style="overflow:auto;">
                                        <table class="table table-striped" id="dataTab">
                                            <thead>
                                                <tr>
                                                    <th style="background-color:lightblue;color: #000000; ">SL NO</th>
                                                    <th style="background-color:lightblue;color: #000000; ">HRMS ID</th>
                                                    <th style="background-color:lightblue;color: #000000; text-align:center; ">Gpf No</th>
                                                    <th style="background-color:lightblue;color: #000000; ">Name Of Employee</th>
                                                    <th style="background-color:lightblue;color: #000000; ">Date<br/>of<br/>Birth</th>
                                                    <th style="background-color:lightblue;color: #000000; text-align:center; ">Appointment Rank</th>
                                                    <th style="background-color:lightblue;color: #000000; ">Date <br/> of<br/> appointment<br/> (Direct/<br/>Promotee</th>
                                                    <th style="background-color:lightblue;color: #000000; ">Designation</th>   
                                                    <th style="background-color:lightblue;color: #000000; text-align:center; ">Fathers Name</th>
                                                    <th style="background-color:lightblue;color: #000000; text-align:center; ">Home<br/>District</th> 
                                                    <th style="background-color:lightblue;color: #000000; ">Category</th>
                                                    <th style="background-color:lightblue;color: #000000; ">date <br/>of joining<br/>in present post</th>
                                                    <th style="background-color:lightblue;color: #000000; ">Deptt.<br/> Prog.If <br/> any, No.<br/> & date</th>
                                                    <th style="background-color:lightblue;color: #000000; ">Date of <br/> disposal <br/> of Prog.</th>
                                                    <th style="background-color:lightblue;color: #000000; ">Punishment <br/> imposed <br/> (Final Order <br/> No.& date)</th>
                                                    <th style="background-color:lightblue;color: #000000; ">Present <br/>  status <br/>  of the <br/>  Prog.</th>
                                                    <th style="background-color:lightblue;color: #000000; ">Transfer / Posting<BR/>Details</th> 

                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach items="${emplist}" var="elist" varStatus="Count">
                                                    <tr style="font-family:serif;background-color: #e6e7e2; ">
                                                        <td style="text-align:center;"><c:out value="${Count.index+1}"/></td>
                                                        <td><c:out value="${elist.empid}"/></td>
                                                        <td><c:out value="${elist.gpfNo}"/></td>
                                                        <td><c:out value="${elist.empname}"/></td>
                                                        <%--<td><c:out value="${elist.appointmentrank}"/></td>--%>
                                                        <td><c:out value="${elist.dateOfBirth}"/></td>
                                                        <td></td>
                                                        <td><c:out value="${elist.dateOfJoining}"/></td>
                                                         
                                                        <td><c:out value="${elist.presentrank}"/></td>
                                                        <td><c:out value="${elist.fathername}"/></td>
                                                        <td><c:out value="${elist.domicile}"/></td>
                                                        <td><c:out value="${elist.category}"/></td>
                                                        <td><c:out value="${elist.dateofpresentjoiningpost}"/></td>
                                                        <td></td>
                                                        <td></td>
                                                        <td></td>                                                        
                                                        <td></td>
                                                        <td><a href="#" onclick="viewPOStatus('${elist.empid}');" data-remote="false" data-toggle="modal" data-target="#viewTPPOmodal">View</a></td>

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



                <div id="viewTPPOmodal" class="modal fade" role="dialog">
                    <div class="modal-dialog" style="width:65%;" >
                        <!-- Modal content-->
                        <div class="modal-content modal-lg" style="width:125%;">
                            <div class="modal-header" style=" text-align: center;">
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                                <h3 class="table-active"><u>TRANSFER AND POSTING OF POLICE OFFICERS</u></h3>
                                <h4 id="empHeading" class="empheading"><B><U></U></B></h4>
                            </div>
                            <div class="modal-body" >


                                <table class="table" border="1"  cellspacing="10"  style="font-size:12px; font-family:verdana;" id="tabl1"> 

                                    <thead>
                                        <tr>
                                            <th style="text-align:center;">Sl. No.</th>
                                            <th style="text-align:center;">Transfer/<br/>Posting Type</th>
                                            <th style="text-align:center;">Transferred from</th>
                                            <th style="text-align:center;">Date of Relief</th>
                                            <th style="text-align:center;">Posted to</th>
                                            <th style="text-align:center;">Date of Joining</th>

                                            <!--                                        <th style="text-align:center;">Date of Retirement</th>-->


                                        </tr>
                                    </thead>
                                    <tbody id="tppoEmpList">                                                    

                                    </tbody>
                                </table> 

                            </div>
                            <div class="modal-footer">                     
                                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                            </div>
                        </div>
                    </div>
                </div>
            </form:form>
    </body>
</html>
