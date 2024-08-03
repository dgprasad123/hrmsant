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
        <script>
            function validateSend() {
                window.location = "listingTerminationPost.htm?financialYear=${financialYear}";
            }
        </script>
    </head>
    <body>

        <form:form action="#" method="POST" commandName="command">

            <form:hidden path="gpc" id="gpc"/>
            <form:hidden path="hidPostGrp" id="hidPostGrp"/>
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading"></div>
                    <div class="panel-body">
                        <h2 align='center'>${OffName}</h2>
                        <h4 align='center'>Financial Year:${financialYear}</h4>
                        <h6 align='center'>SCHEDULE I-A</h6>
                        <div align="center">(Relating to Head of the Office)</div>

                        <table class="table table-striped table-bordered" width="90%">
                            <thead>
                                <tr>

                                    <th width="1%" >Slno</th>
                                    <th width="25%" >Description of the Posts</th>
                                    <th width="10%" >Pay Scale</th>                                   
                                    <th width="8%" >Go No which is sanctioned</th>
                                    <th width="8%" >Go Date which is sanctioned</th>
                                    <th width="8%" >No of Posts to be terminated</th>
                                    <th width="9%">Date from which posts to be terminated </th>
                                    <th  > Remarks</th>
                                </tr>

                            </thead>
                            <c:set var="GtotalPost" value="${0}" />
                            <c:forEach var="partAGrpA" items="${PartAGrouplist}"  varStatus="theCount">
                                 <c:set var="GtotalPost" value="${GtotalPost+partAGrpA.postTerminated}" />

                                <tr>

                                    <td>${theCount.index + 1}</td>
                                    <td> ${partAGrpA.postname} </td>
                                    <td> ${partAGrpA.scaleofPay} </td>

                                    <td> ${partAGrpA.goNo} </td>
                                    <td> ${partAGrpA.goDate} </td>
                                    <td> ${partAGrpA.postTerminated} </td>
                                    <td> ${partAGrpA.dateTerminated} </td>
                                    <td> ${partAGrpA.remarks} </td>

                                </tr>
                            </c:forEach> 
                            <tr>

                                <td>&nbsp;</td>
                                <td>&nbsp;</td>    
                                <td>&nbsp;</td>    
                                <td>&nbsp;</td>    
                                <td><strong>Total Post Terminated</strong></td>    
                                <td><strong style="color:green">${GtotalPost}</strong></td>    
                                <td>&nbsp;</td>    
                                <td>&nbsp;</td>    
                            </tr> 



                        </table>
                        <div class="modal-footer">
                            <button type="button" name="btnPTAer" value="Send" class="btn btn-primary" onclick="validateSend()">Close</button>
                        </div>
                    </div>

                </div>

            </div>



        </form:form>



    </body>
</html>

