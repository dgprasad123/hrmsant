<%-- 
    Document   : ASICheckListPage
    Created on : 14 Nov, 2020, 9:11:27 PM
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
            function downloadCheckList() {
                var downloadType = $("input[name='downloadType']:checked").val();
                url = "downloadExcelFormatASICheckList.htm?sltEstablishment=" + $("#sltEstablishment").val() + "&downloadType=" + downloadType;
                window.open(url);
            }
            function generateTicket() {
                var url = "generateHallTicket.htm";
                $.post(url)
                        .done(function(data) {
                            if (data.msg == "Y") {
                                alert("Generate Successfully");
                            } else {
                                alert("Some Error Occured");
                            }
                        });

            }
        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">

                <form:form action="downloadASICheckListPage.htm" commandName="nominationForm">

                    <div class="panel panel-default">
                        <h3 style="text-align:center"> ASI Checklist</h3>

                        <div class="panel-heading">


                        </div>
                        <div class="panel-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="sltEstablishment"> Select Type of Download <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    <input type="radio" name="downloadType" value="B" id="downloadTypeB"/> Both Qualified/ Non Qualified
                                </div>    
                                <div class="col-lg-2">
                                    <input type="radio" name="downloadType" value="Q" id="downloadTypeQ"/> Qualified
                                </div>        
                                <div class="col-lg-2">
                                    <input type="radio" name="downloadType" value="D" id="downloadTypeD"/> Non Qualified
                                </div>     
                                <div class="col-lg-2">
                                    <input type="radio" name="downloadType" value="UR" id="downloadTypeUR"/> UR UNDER TAKING
                                </div>        

                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
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

                            <input type="submit" name="action" value="Generate Qualified List" class="btn btn-primary">
                            <button type="button" class="btn btn-primary" onclick="return generateTicket()">Generate Hall ticket</button>

                            <div class="col-lg-2">
                                <a href="javascript:void(0)" onclick="downloadCheckList()"><input type="button" name="action" value="Download" class="btn btn-primary"/></a>
                                <!--<a href="javascript:void(0)" onclick="downloadCheckListWithPhoto()"><input type="button" name="action" value="Download with Photo" class="btn btn-primary"/></a>-->
                            </div>
                        </div>
                    </div>

                </div>


            </form:form>
        </div>
    </div>
</body>
</html>
