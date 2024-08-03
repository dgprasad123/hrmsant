
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">

        </script>
    </head>
    <body>

        <form:form action="updateLeaveBalance.htm" commandName="clupdateform" >
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 style="text-align:center">  
                            <c:if test="${clupdateform.tolId == 'CL'}">
                                UPDATE CASUAL LEAVE
                            </c:if> 
                            <c:if test="${clupdateform.tolId == 'EL'}">
                                UPDATE EARNED LEAVE
                            </c:if> 
                            <c:if test="${clupdateform.tolId == 'COL'}">
                                UPDATE COMMUTED LEAVE
                            </c:if> 
                            <c:if test="${clupdateform.tolId == 'HPL'}">
                                UPDATE HALF PAY LEAVE
                            </c:if> 
                            <c:if test="${clupdateform.tolId == 'ML'}">
                                UPDATE MATERNITY LEAVE
                            </c:if> 
                            <c:if test="${clupdateform.tolId == 'PL'}">
                                UPDATE PATERNITY LEAVE
                            </c:if> 
                            <c:if test="${clupdateform.tolId == 'AEL'}">
                                UPDATE ADDITIONAL EARNED LEAVE
                            </c:if> 
                            <c:if test="${clupdateform.tolId == 'EEL'}">
                                UPDATE EX-INDIA EARNED LEAVE
                            </c:if> 
                            <c:if test="${clupdateform.tolId == 'EIC'}">
                                UPDATE EX-INDIA COMMUTED LEAVE
                            </c:if> 
                            <c:if test="${clupdateform.tolId == 'EOL'}">
                                UPDATE EXTRAORDINARY LEAVE
                            </c:if> 
                            <c:if test="${clupdateform.tolId == 'HDL'}">
                                UPDATE HALF DAY CASUAL LEAVE
                            </c:if> 
                            <c:if test="${clupdateform.tolId == 'QL'}">
                                UPDATE QUARANTINE LEAVE
                            </c:if> 
                            <c:if test="${clupdateform.tolId == 'SCL'}">
                                UPDATE SPECIAL CASUAL LEAVE
                            </c:if> 
                        </h3>
                        <form:hidden path="empid"/>
                        <form:hidden path="offCode"/>
                        <form:hidden path="postCode"/>
                        <form:hidden path="tolId"/>
                    </div>
                    <div class ="panel-body">

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-1">
                                <label></label>
                            </div>
                            <div class="col-lg-2">
                                <label for="cl">  </label>


                            </div>
                            <div class="col-lg-3">

                            </div>
                            <div class="col-lg-3">

                            </div>
                            <div class="col-lg-3">

                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-1">
                                <label>1.</label>
                            </div>
                            <div class="col-lg-2">
                                <label for="cl"> Year </label>
                            </div>
                            <div class="col-lg-3">
                                <form:select path="year" id="year"  size="1" class="form-control">
                                    <form:option value="">Select One</form:option>
                                    <form:option value="2021">2021</form:option>
                                    <form:option value="2022">2022</form:option>
                                    <form:option value="2023">2023</form:option>
                                     <form:option value="2024">2024</form:option>
                                      <form:option value="2025">2025</form:option>
                                        <form:option value="2026">2026</form:option>
                                      <form:option value="2027">2027</form:option>
                                        <form:option value="2028">2028</form:option>
                                      <form:option value="2029">2029</form:option>
                                      <form:option value="2030">2030</form:option>
                                </form:select>
                            </div>
                            <div class="col-lg-3">

                            </div>
                            <div class="col-lg-3">

                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-1">
                                <label>2.</label>
                            </div>
                            <div class="col-lg-2">
                                <label for="cl"> Month </label>
                            </div>
                            <div class="col-lg-3">
                                <form:select path="month" id="month"  size="1" class="form-control">
                                    <form:option value=" ">Select Year</form:option>
                                    <form:option value="01">January</form:option>
                                    <form:option value="02">February</form:option>
                                    <form:option value="03">March</form:option>
                                    <form:option value="04">April</form:option>
                                    <form:option value="05">May</form:option>
                                    <form:option value="06">June</form:option>
                                    <form:option value="07">July</form:option>
                                    <form:option value="08">August</form:option>
                                    <form:option value="09">September</form:option>
                                    <form:option value="10">October</form:option>
                                    <form:option value="11">November</form:option>
                                    <form:option value="12">December</form:option>
                                </form:select>
                            </div>
                            <div class="col-lg-3">

                            </div>
                            <div class="col-lg-3">

                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-1">
                                <label>3.</label>
                            </div>
                            <div class="col-lg-2">
                                <label for="cl"> Total Balance </label>
                            </div>
                            <div class="col-lg-3">
                                <form:input path="totalCl" class="form-control"/>
                            </div>
                            <div class="col-lg-3">

                            </div>
                            <div class="col-lg-3">

                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-1">
                                <label>4.</label>
                            </div>
                            <div class="col-lg-2">
                                <label for="cl"> Available  </label>
                            </div>
                            <div class="col-lg-3">
                                <form:input path="totalClAvail" class="form-control"/>
                            </div>
                            <div class="col-lg-3">

                            </div>
                            <div class="col-lg-3">

                            </div>
                        </div>

                    </div>
                    <div class="panel-footer">
                        <input type="submit" value="Save" name="btn" class="btn btn-success" onclick="return validateForm()"/>
                        <input type="submit" value="Back" name="btn" class="btn btn-danger" />
                    </div>
                </div>
            </div>
        </form:form>


    </body>
</html>

