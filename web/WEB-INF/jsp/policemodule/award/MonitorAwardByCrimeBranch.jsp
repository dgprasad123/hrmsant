<%-- 
    Document   : MonitorAwardByCrimeBranch
    Created on : 16 Mar, 2021, 1:35:55 PM
    Author     : Surendra
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
            $(document).ready(function () {
                $("#actionDivId").hide();
            });


            function validate() {

                if ($("#awardMedalTypeId").val() == '') {
                    alert('Please select type of Award/Medal.');
                    return false;
                }
                if ($("#awardYear").val() == '') {
                    alert('Please select Year.');
                    return false;
                } else {
                    $("#actionDivId").show();

                }



            }

            

            function getActtionUrl() {
                url = "RedirectActionAwardViewPageForDGOffice.htm?awardMedalTypeId=" + $("#awardMedalTypeId").val() + "&awardYear=" + $("#awardYear").val() + "&sltActionName=" + $("#sltActionName").val();
                window.open(url);
            }
        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../../tab/hrmsadminmenu.jsp"/>          
            <div id="page-wrapper">
                <form:form action="RedirectActionAwardViewPageForDGOffice.htm" commandName="AwardMedalListForm">
                    <div class="panel panel-default">
                        <h3 style="text-align:center">Recommend List</h3>

                        <div class="panel-heading">

                        </div>
                        <div class="panel-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-3">
                                    <label for="awardMedalName"> Select type of Award/Medal <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select class="form-control" path="awardMedalTypeId" id="awardMedalTypeId">
                                        <form:option value="">--Select One--</form:option>
                                        <form:option value="14">AWARD OF CHIEF MINISTER'S MEDAL FOR EXCELLENCE IN INVESTIGATION</form:option>
                                    </form:select>
                                </div>  
                                <div class="col-lg-2">
                                    <label for="awardYear"> Select Year <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select class="form-control" path="awardYear" id="awardYear">
                                        <form:option value="">--Select One--</form:option>
                                        <form:option value="2020">2020</form:option>
                                        <form:option value="2021">2021</form:option>
                                    </form:select>
                                </div>

                                <div class="col-lg-1">
                                    <input type="button" name="action" value="Ok" class="btn btn-primary" onclick="return validate()"/>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;" id="actionDivId">
                                <div class="col-lg-2">
                                    <label for="sltActionName"> Select Type of Action <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-8">
                                    <form:select class="form-control" path="sltActionName" id="sltActionName">
                                        <form:option value="">--Select--</form:option>
                                        <form:option value="1"> Establishment Wise Officers Recommended  </form:option>
                                        <form:option value="2"> Range Wise Officers Recommended </form:option>
                                        <form:option value="3"> List of Officers Recommendation Pending at Range Level </form:option>
                                        <form:option value="4"> Download Broad Sheet </form:option>
                                    </form:select>
                                </div>

                                <div class="col-lg-2">
                                    <a href="javascript:void(0)" onclick="getActtionUrl()"><input type="button" name="action" value="Action" class="btn btn-primary" onclick="return validate()"/></a>
                                </div>
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
