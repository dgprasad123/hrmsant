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
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script src="js/moment.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>

        <script type="text/javascript">
            $(document).ready(function () {
                $('.goDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('.dateTerminated').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            });
            function openHoOWindow() {
                $('#hoOModal').modal('show');
            }

        </script>

        <style type="text/css">
            body{
                position:relative;
            }
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

        <form:form action="sentSchedule1Post.htm" method="POST" commandName="command">
            <form:hidden path="aerId" id="aerId"/>
            <form:hidden path="gpc" id="gpc"/>
            <form:hidden path="hidPostGrp" id="hidPostGrp"/>
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading"></div>
                    <div class="panel-body">
                        <h2 align='center'>${OffName}</h2>
                        <h4 align='center'>Financial Year:2018-19</h4>
                        <h6 align='center'>SCHEDULE III-A</h6>
                        <div align="center">(Relating to Administrative Department, attached Sub-ordinate Offices, Heads of Department & Sub-ordinate District Offices for the Department as a whole)</div>

                        <table class="table table-striped table-bordered" width="90%">
                            <thead>
                                <tr>

                                    <th width="1%" >Slno</th>
                                    <th width="25%" >Description of the Posts</th>
                                    <th width="10%" >Pay Scale</th>
                                    <th width="8%" >Sanctioned Strength</th>
                                    <th width="8%" >Go No which is sanctioned</th>
                                    <th width="8%" >Go Date which is sanctioned</th>
                                    <th width="8%" >No of Posts to be terminated</th>
                                    <th width="9%">Date from which posts to be terminated </th>
                                    <th  > Remarks</th>
                                </tr>

                            </thead>
                            <c:forEach var="partAGrpA" items="${PartAGrouplist}"  varStatus="theCount">
                                <input type="hidden" name="hiddenGPC" value="${partAGrpA.gpc}"/>
                                <tr>

                                    <td>${theCount.index + 1}</td>
                                    <td> ${partAGrpA.postname} </td>
                                    <td> ${partAGrpA.scaleofPay} </td>
                                    <td> ${partAGrpA.sanctionedStrength} </td>
                                    <td><input type="text" name="goNo" class="form-control" required/></td>
                                    <td>
                                        <div style="position:relative;">
                                            <input type="text" name="goDate" class="form-control goDate" readonly="true" required/>
                                        </div>
                                    </td>
                                    <td><input type="text" name="postTerminated" class="form-control" required/></td>
                                    <td>
                                        <div style="position:relative;">
                                            <input type="text" name="dateTerminated" class="form-control dateTerminated" readonly="true" required/>
                                        </div>
                                    </td>
                                    <td><input type="text" name="remarks" class="form-control" required/></td>

                                </tr>
                            </c:forEach>      



                        </table>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" onclick="openHoOWindow();">Submit</button>
                        </div>
                    </div>

                </div>

            </div>



        </form:form>



    </body>
</html>

