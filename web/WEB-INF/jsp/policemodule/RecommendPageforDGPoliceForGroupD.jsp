<%-- 
    Document   : RecommendPageforDGPoliceForGroupD
    Created on : 18 Jan, 2022, 4:46:56 PM
    Author     : Manisha
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" autoFlush="true" buffer="64kb"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri = "http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
        <script type="text/javascript">
            $(document).ready(function() {
                $("#actionDivId").hide();
            });


            function validateGetEmployee() {

                if ($("#sltpostName").val() == '') {
                    alert('Please Select Rank');
                    document.getElementById('sltpostName').focus();
                    return false;
                }
                if ($("#sltNominationForPost").val() == '') {
                    alert('Please Select Nomination for');
                    document.getElementById('sltNominationForPost').focus();
                    return false;
                } else {
                    $("#actionDivId").show();

                }



            }
            function checkStatusForPoliceNomination() {
                $.ajax({
                    type: "POST",
                    data: {currentPost: $("#sltpostName").val(), nominationPost: $("#sltNominationForPost").val()},
                    url: "checkStatusForPoliceNomination.htm",
                    success: function(data) {
                        alert(data);

                    }
                });

            }
            function activePoliceNomination() {
                if (confirm('Are you sure to do it?')) {
                    $.ajax({
                        type: "POST",
                        data: {currentPost: $("#sltpostName").val(), nominationPost: $("#sltNominationForPost").val()},
                        url: "activateStatusForPoliceNomination.htm",
                        success: function(data) {
                            alert(data);
                            if (data == "1") {
                                alert("Selected Nomination status is changed for SP Offices and Range Offices.");
                            }
                        }
                    });
                }
            }
            function deActivePoliceNomination() {
                if (confirm('Are you sure to do it?')) {
                    $.ajax({
                        type: "POST",
                        data: {currentPost: $("#sltpostName").val(), nominationPost: $("#sltNominationForPost").val()},
                        url: "deActivateStatusForPoliceNomination.htm",
                        success: function(data) {
                            alert(data);
                            if (data == "1") {
                                alert("Selected Nomination status is changed for SP Offices and Range Offices.");
                            }
                        }
                    });
                }
            }

            function getNominationRankList(obj) {
                var sltpostName = obj.value;
                if (sltpostName != '') {
                    $('#sltNominationForPost').empty();
                    var url = 'getNominationForRankListJSON.htm?sltpostName=' + sltpostName;
                    $('#sltNominationForPost').append('<option value="">--Select--</option>');
                    $.getJSON(url, function(data) {
                        $.each(data, function(i, obj) {
                            $('#sltNominationForPost').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        });
                    });
                }
            }

            function getActtionUrl() {
                url = "RedirectActionViewPageForDGOffice.htm?sltpostName=" + $("#sltpostName").val() + "&sltNominationForPost=" + $("#sltNominationForPost").val() + "&sltActionName=" + $("#sltActionName").val() + "&cadreName=" + $("#cadreName").val()  + "&fiscalyear=" + $("#fiscalyear").val();
                alert($("#fiscalyear").val());
                window.open(url);
            }
        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <form:form action="RedirectActionViewPageForDGOffice.htm" commandName="EmpDetNom">
                    <div class="panel panel-default">
                        <h3 style="text-align:center">Recommend List</h3>

                        <div class="panel-heading">

                        </div>
                        <div class="panel-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="financialyear"> Financial Year <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    <form:select path="fiscalyear" id="fiscalyear" class="form-control">
                                        <form:option value="">Year</form:option>
                                        <form:options items="${fiscyear}" itemValue="fy" itemLabel="fy"/>
                                    </form:select>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="sltpostName"> Select Rank <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-3">

                                    <form:select path="sltpostName" id="sltpostName" class="form-control">
                                        <form:option value="000000"> Group D </form:option>
                                        <form:option value="140161"> Junior Clerk </form:option>
                                    </form:select>    
                                </div>
                                <div class="col-lg-2">
                                    <label for="sltpostName"> Select Nomination for <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-3">

                                    <form:select path="sltNominationForPost" id="sltNominationForPost" class="form-control">
                                        <form:option value="140161"> Junior Clerk </form:option>
                                        <form:option value="140255"> Senior Clerk </form:option>
                                    </form:select>
                                </div>
                                <div class="col-lg-2">
                                    <input type="button" name="action" value="Show" class="btn btn-primary" onclick="return validateGetEmployee()"/>
                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;" id="actionDivId">
                                <div class="col-lg-2">
                                    <label for="sltActionName"> Select Type of Action <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-8">
                                    <form:select class="form-control" path="sltActionName" id="sltActionName">
                                        <form:option value="">--Select--</form:option>
                                        <form:option value="1"> Establishment Wise Officers Nominated  </form:option>
                                        <form:option value="2"> Range Wise Officers Recommended </form:option>
                                        <form:option value="3"> List of Officers Recommendation Pending at Range Level </form:option>
                                        <form:option value="4"> Download Service Particular </form:option>
                                    </form:select>
                                </div>

                                <div class="col-lg-2">
                                    <a href="javascript:void(0)" onclick="getActtionUrl()"><input type="button" name="action" value="Action" class="btn btn-primary" onclick="return validateGetEmployee()"/></a>
                                </div>

                            </div>
                            <div class="row" style="margin-bottom: 7px;">

                                <div class="col-lg-1">
                                    <input type="button" name="action" value="Check Status" class="btn btn-danger" onclick="return checkStatusForPoliceNomination();"/>
                                </div>
                                <input type="button" name="action" value="Active" class="btn btn-danger" onclick="return activePoliceNomination();"/>
                                <input type="button" name="action" value="De Active" class="btn btn-danger" onclick="return deActivePoliceNomination();"/>
                            </div>
                        </div>
                        <div class="panel-footer">

                        </div>
                    </div>
                </form:form>
            </div>
        </div>

    </body>
</html>
