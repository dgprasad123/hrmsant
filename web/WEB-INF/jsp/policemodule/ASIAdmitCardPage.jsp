<%-- 
    Document   : ASIAdmitCardPage
    Created on : 15 Nov, 2020, 6:56:34 PM
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

        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                $("#divEstab").hide();
                $("#divCenter").hide();
            });
            function validate() {

            }

            function togglediv() {
                var radioValue = $("input[name='rdoEstablishment']:checked").val();
                if (radioValue == 'D') {
                    $("#divEstab").show();
                    $("#divCenter").hide();
                    $("#divhallticket").hide();
                } else if (radioValue == 'C') {
                    $("#divEstab").hide();
                    $("#divCenter").show();
                    $("#divhallticket").hide();
                } else {
                    $("#divhallticket").show();
                    $("#divEstab").hide();
                    $("#divCenter").hide();
                }
            }

            function downloadAdmitCard() {
                url = "downloadASIAdmitCard.htm?sltEstablishment=" + $("#sltEstablishment").val();
                window.open(url);
            }
            function downloadCenterWiseCandidateList() {
                url = "downloadCenterWiseCandidateList.htm?sltCenter=" + $("#sltCenter").val();
                window.open(url);
            }  
            function downloadDistrictWiseHallTicketNumber() {
                alert($("#sltEstablishmenthall").val());
                url = "downloadDistrictWiseHallTicketNumber.htm?sltEstablishment=" + $("#sltEstablishmenthall").val();
                window.open(url);
            }



        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">

                <form:form action="downloadASICheckListPage.htm" commandName="nominationForm">

                    <div class="panel panel-default">
                        <h3 style="text-align:center"> ASI Admit Card</h3>

                        <div class="panel-heading">


                        </div>
                        <div class="panel-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="sltEstablishment"> Select Download Category <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    <input type="radio" name="rdoEstablishment" id="establishmnetRdo" onclick="togglediv()" value="D"/> District/Establishment
                                </div>
                                <div class="col-lg-2">
                                    <input type="radio" name="rdoEstablishment" id="centerRdo" onclick="togglediv()" value="C"/> Center
                                </div>
                                <div class="col-lg-2">
                                    <input type="radio" name="rdoEstablishment" id="hallticketRdo" onclick="togglediv()" value="H"/> Hall ticket District wise
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;" id="divEstab">
                                <div class="col-lg-2">
                                    <label for="sltEstablishment"> Select Establishment <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select class="form-control" path="sltEstablishment" id="sltEstablishment">
                                        <form:option value="">--Select One--</form:option>
                                        <form:option value="ALL">ALL</form:option>
                                        <form:options items="${offList}" itemLabel="label" itemValue="value"/>
                                    </form:select>
                                </div>

                                <div class="col-lg-2">

                                    <a href="javascript:void(0)" onclick="downloadAdmitCard()"><input type="button" name="action" value="Download" class="btn btn-primary"/></a>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;" id="divCenter">
                                <div class="col-lg-2">
                                    <label for="sltEstablishment"> Select Center <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select class="form-control" path="sltCenter" id="sltCenter">
                                        <form:option value="">--Select One--</form:option>
                                        <form:option value="">ALL</form:option>
                                        <form:options items="${centerList}" itemLabel="label" itemValue="value"/>
                                    </form:select>
                                </div>

                                <div class="col-lg-2">

                                    <a href="javascript:void(0)" onclick="downloadCenterWiseCandidateList()"><input type="button" name="action" value="Download" class="btn btn-primary"/></a>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;" id="divhallticket">
                                <div class="col-lg-2">
                                    <label for="sltEstablishment"> Select Establishment <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select class="form-control" path="sltEstablishment" id="sltEstablishmenthall">
                                        <form:option value="">--Select One--</form:option>
                                        <form:option value="ALL">ALL</form:option>
                                        <form:options items="${offList}" itemLabel="label" itemValue="value"/>
                                    </form:select>
                                </div>

                                <div class="col-lg-2">

                                    <a href="javascript:void(0)" onclick="downloadDistrictWiseHallTicketNumber()"><input type="button" name="action" value="Download" class="btn btn-primary"/></a>
                                </div>
                            </div>
                        </div>

                    </div>


                </form:form>
            </div>
        </div>
    </body>
</html>
