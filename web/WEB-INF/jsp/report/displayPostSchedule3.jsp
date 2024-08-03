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
                // $('#hoOModal').modal('show');
                var con = confirm("Do ypu want to send this report to FD");
                if (!con) {

                    return false;
                }
            }
            function validateSend() {
                var govalidate = $(".govalidate");
                for (var i = 0; i < govalidate.length; i++) {
                    if ($(govalidate[i]).val() == '') {
                        alert("Please Enter Go No");
                       // alert("If GO NO is not availabel Please mention NA")
                        return false;
                    }
                }
                var goDate = $(".goDate");
                for (var i = 0; i < govalidate.length; i++) {
                    if ($(goDate[i]).val() == '') {
                        alert("Please Enter GO Date");
                      //  alert("If GO Date is not availabel Please mention Today's Date")
                        return false;
                    }
                }
                var noOfPost = $(".noOfPost");
                for (var i = 0; i < noOfPost.length; i++) {
                    if ($(noOfPost[i]).val() == '') {
                        alert("Please Enter No of Posts to be terminated");
                        return false;
                    }
                }

                var dateOfTermination = $(".dateOfTermination");
                for (var i = 0; i < dateOfTermination.length; i++) {
                    if ($(dateOfTermination[i]).val() == '') {
                        alert("Please Enter Date of Termination");
                        return false;
                    }
                }
                var remarks = $(".remarks");
                for (var i = 0; i < remarks.length; i++) {
                    if ($(remarks[i]).val() == '') {
                        alert("Please Enter Remarks");
                        return false;
                    }
                }
                var con = confirm("Do ypu want to send this report to FD");
                if (!con) {

                    return false;
                }
            }
              function back_page(){                
                window.location="listingTerminationPostSchedule3.htm?financialYear=${financialYear}";
            }
            function validate_gono_go_date(ids){
                var chkVal="checkbox_"+ids;
                var gonoVal="gono_"+ids;
                var godateVal="godate_"+ids;
                if(document.getElementById(chkVal).checked) {
                   $("#"+gonoVal).val("NA");
                   $("#"+godateVal).val("NA");
                   $("#"+gonoVal).attr('readonly', true);
                }  else {
                     $("#"+gonoVal).val("");
                   $("#"+godateVal).val("");
                     $("#"+gonoVal).attr('readonly', false);
                 }    
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

        <form:form action="sentSchedule3Post.htm" method="POST" commandName="command">

            <form:hidden path="gpc" id="gpc"/>
            <form:hidden path="hidPostGrp" id="hidPostGrp"/>
             <form:hidden path="financialYear" id="financialYear"/>
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading"></div>
                    <div class="panel-body">
                        <h2 align='center'>${OffName}</h2>
                        <h4 align='center'>Financial Year:${financialYear}</h4>
                        <h6 align='center'>SCHEDULE III-A</h6>
                        <div align="center">(Relating to Administrative Department, attached Sub-ordinate Offices, Heads of Department & Sub-ordinate District Offices for the Department as a whole)</div>
                         <button type="button" name="btnPTAer" value="Send" class="btn btn-primary" onclick="return back_page()">Back</button>
                        <div style="clear:both;margin:5px 0px">&nbsp;</div>
                        <table class="table table-striped table-bordered" width="90%">
                            <thead>
                                <tr>

                                    <th width="1%" >Slno</th>
                                    <th width="20%" >Description of the Posts</th>
                                    <th width="10%" >Pay Scale</th>
                                    <th width="3%" >Sanctioned Strength</th>
                                    <th width="7%" >GO NO & Date <strong style='color:red'>Not Available</strong></th>
                                    <th width="3%" >Go No which is sanctioned</th>
                                    <th width="10%" >Go Date which is sanctioned</th>
                                    <th width="8%" >No of Posts to be terminated</th>
                                    <th width="10%">Date from which posts to be terminated </th>
                                    <th  > Remarks</th>
                                </tr>

                            </thead>
                            <c:forEach var="partAGrpA" items="${PartAGrouplist}"  varStatus="theCount">
                                <input type="hidden" name="hiddenpostName" value="${partAGrpA.postname}"/>
                                <input type="hidden" name="hiddengp" value="${partAGrpA.gp}"/>
                                <input type="hidden" name="hiddePayScale" value="${partAGrpA.scaleofPay}"/>
                                <tr>

                                    <td>${theCount.index + 1}</td>
                                    <td> ${partAGrpA.postname} </td>
                                    <td> ${partAGrpA.scaleofPay} </td>
                                    <td> ${partAGrpA.sanctionedStrength} </td>
                                    <td><input type="checkbox" name="go_ava" class="form-control"  onclick="validate_gono_go_date(${theCount.index + 1})" id="checkbox_${theCount.index + 1}"/></td>
                                    <td><input type="text" name="goNo" class="form-control govalidate"  id="gono_${theCount.index + 1}"/></td>
                                    <td>
                                        <div style="position:relative;">
                                            <input type="text" name="goDate" class="form-control goDate" readonly="true"  id="godate_${theCount.index + 1}" />
                                        </div>
                                    </td>
                                    <td><input type="text" name="postTerminated" class="form-control noOfPost" /></td>
                                    <td>
                                        <div style="position:relative;">
                                            <input type="text" name="dateTerminated" class="form-control dateTerminated dateOfTermination" readonly="true" />
                                        </div>
                                    </td>
                                    <td><input type="text" name="remarks" class="form-control remarks" /></td>

                                </tr>
                            </c:forEach>      



                        </table>
                        <div class="modal-footer">
                            <button type="submit" name="btnPTAer" value="Send" class="btn btn-primary" onclick="return validateSend()">Send</button>
                        </div>
                    </div>

                </div>

            </div>



        </form:form>



    </body>
</html>

