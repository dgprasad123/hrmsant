<%-- 
    Document   : CreateAERData
    Created on : 5 Jun, 2018, 11:14:19 AM
    Author     : Surendra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>HRMS</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>

        <style>
            body {padding-top:50px;}

            .box {
                border-radius: 3px;
                box-shadow: 0 2px 5px 0 rgba(0, 0, 0, 0.16), 0 2px 10px 0 rgba(0, 0, 0, 0.12);
                padding: 10px 25px;
                text-align: right;
                display: block;
                margin-top: 60px;
            }
            .box-icon {
                background-color: #57a544;
                border-radius: 50%;
                display: table;
                height: 100px;
                margin: 0 auto;
                width: 100px;
                margin-top: -61px;
            }
            .box-icon span {
                color: #fff;
                display: table-cell;
                text-align: center;
                vertical-align: middle;
            }
            .info h4 {
                font-size: 26px;
                letter-spacing: 2px;
                text-transform: uppercase;
            }
            .info > p {
                color: #717171;
                font-size: 16px;
                padding-top: 10px;
                text-align: justify;
            }
            .info > a {
                background-color: #03a9f4;
                border-radius: 2px;
                box-shadow: 0 2px 5px 0 rgba(0, 0, 0, 0.16), 0 2px 10px 0 rgba(0, 0, 0, 0.12);
                color: #fff;
                transition: all 0.5s ease 0s;
            }
            .info > a:hover {
                background-color: #0288d1;
                box-shadow: 0 2px 3px 0 rgba(0, 0, 0, 0.16), 0 2px 5px 0 rgba(0, 0, 0, 0.12);
                color: #fff;
                transition: all 0.5s ease 0s;
            }
        </style>
        <script type="text/javascript">
            $(document).ready(function () {
                if ($('#otherSS').val() == 0) {
                    $('#otherSS').val('');
                }
                if ($('#otherVacancy').val() == 0) {
                    $('#otherVacancy').val('');
                }
            });
            function validateData() {
                var otherCategory = $("#otherCategory").val();
                var otherPost = $("#otherPost").val();
                var other6thPay = $("#other6thPay").val();
                var other7thPay = $("#other7thPay").val();


                if (otherCategory == "") {
                    alert("Please select Category Type");
                    return false;
                }
                if (otherPost == "") {
                    alert("Post cannot be blank");
                    return false;
                }

                if (other7thPay == "") {
                    alert("7th Pay Commission cannot be blank");
                    return false;
                }
                if ($('#otherSS').val() == '') {
                    alert("Men-in-position cannot be blank");
                    return false;
                }

                var SS = $('#otherSS').val();
                var Vacancy = $('#otherVacancy').val();

                // return false;
            }
            function onlyIntegerRange(e) {
                var browser = navigator.appName;
                if (browser == "Netscape") {
                    var keycode = e.which;
                    if ((keycode >= 48 && keycode <= 57) || keycode == 8 || keycode == 0)
                        return true;
                    else
                        return false;
                } else {
                    if ((e.keyCode >= 48 && e.keyCode <= 57) || e.keycode == 8 || e.keycode == 0)
                        e.returnValue = true;
                    else
                        e.returnValue = false;
                }
            }
            function delete_otherEst(aerOtherId, aerId, fy) {
                var conf = confirm("Do you want to confirm to delete this Information");
                if (conf) {

                    window.location = "deleteOtherEst.htm?aerOtherId=" + aerOtherId + "&aerId=" + aerId + "&fy=" + fy;
                }
            }
        </script>   
    </head>
    <body>
         <c:if test="${partType eq 'C'}">
            <jsp:include page="aerTab.jsp">
                <jsp:param name="menuHighlight" value="PART_C" />
            </jsp:include>
         </c:if>
        <c:if test="${partType eq 'D'}">
            <jsp:include page="aerTab.jsp">
                <jsp:param name="menuHighlight" value="PART_D" />
            </jsp:include>
         </c:if>
        <c:if test="${partType eq 'E'}">
            <jsp:include page="aerTab.jsp">
                <jsp:param name="menuHighlight" value="PART_E" />
            </jsp:include>
         </c:if>
        <form:form action="saveOtherEst.htm" commandName="command" >

            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="col-lg-12">
                                <h4 align="center"> ${OffName} </h4>
                            </div>
                        </div>                        
                    </div> 

                    <div class="panel panel-info">
                        <!--  <div class="panel-heading"> PART-C ( Non-Regular Establishment)</div>-->
                        <c:if test="${partType eq 'C'}">
                            <div class="panel-heading"> PART-C ( Non-Regular Establishment)</div>
                        </c:if>
                        <c:if test="${partType eq 'D'}">
                            <div class="panel-heading"> PART-D ( Other Establishment)</div>
                        </c:if>
                        <c:if test="${partType eq 'E'}">
                            <div class="panel-heading"> PART-E ( Outsourced / on Contract)</div>
                        </c:if>  


                        <div class="panel-body">
                            <form:hidden path="financialYear"/>
                            <form:hidden path="aerId"/>
                            <form:hidden path="aerOtherId"/>

                            <c:if test="${partType eq 'C'}">   
                                <input type="hidden" name="other6thPay" value="0"/>
                                <input type="hidden" name="otherVacancy" value="0"/>
                                <input type="hidden" name="group" value=""/>
                                 <input type="hidden" name="partType" value="C"/>
                                <div class="form-row">
                                    <div class="form-group col-md-3">
                                        <label for="Category2">Category<strong style="color:red">*</strong></label>
                                        <form:select path="otherCategory" class="form-control" id="otherCategory" >
                                            <form:option value="Contractual Employees (Appointed vide GA & PG Department Resolution)">Contractual Employees (Appointed vide GA&PG Department Resolution)</form:option>
                                        </form:select>   
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label for="inputPassword4">Description of the Posts<strong style="color:red">*</strong></label>
                                        <form:input path="otherPost" id="otherPost" class="form-control" maxlength="50"/>
                                    </div>
                                    <div class="form-group col-md-3">
                                        <label for="inputPassword4">pay Scale (7th Pay)<strong style="color:red">*</strong></label>
                                        <form:input path="other7thPay" id="other7thPay" class="form-control" maxlength="20"/>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label for="inputPassword4">Persons-in-Position<strong style="color:red">*</strong></label>
                                        <form:input path="otherSS" id="otherSS" onkeypress="return onlyIntegerRange(event)" class="form-control" maxlength="4"/>
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label for="inputPassword4">Remarks</label>
                                        <form:input path="otherRemarks" id="otherRemarks" class="form-control" maxlength="200"/>
                                    </div>
                                </div>
                            </c:if> 
                            <c:if test="${partType eq 'D'}">
                                <input type="hidden" name="other6thPay" value="0"/>
                                <input type="hidden" name="other7thPay" value="0"/>
                                <input type="hidden" name="otherPost" value="0"/>
                                <input type="hidden" name="otherVacancy" value="0"/>
                                <input type="hidden" name="partType" value="D"/>
                                <div class="form-row">
                                    <div class="form-group col-md-4">
                                        <label for="Category2">Category<strong style="color:red">*</strong></label>
                                        <form:select path="otherCategory" class="form-control" id="otherCategory" >
                                           <form:option value="Work Charged">Work Charged</form:option>
                                            <form:option value="Job Contact (under Revenue Department)">Job Contact (under Revenue Department)</form:option>
                                            <form:option value="Temporary status employee (As per FD Circular)">Temporary status employee (As per FD Circular)</form:option>
                                        </form:select>   
                                    </div>
                                    <div class="form-group col-md-2">
                                        <label for="inputPassword4">Number<strong style="color:red">*</strong></label>
                                        <form:input  path="otherSS" id="otherSS" onkeypress="return onlyIntegerRange(event)"  class="form-control" maxlength="4"/>
                                    </div>
                                    <div class="form-group col-md-3">
                                        <label for="inputPassword4">Group<strong style="color:red">*</strong></label>
                                       <form:select path="group" class="form-control" id="group" >
                                             <option value="">Select  Group </option>
                                            <form:option value="A">A</form:option>
                                            <form:option value="B">B</form:option>
                                            <form:option value="C">C</form:option>
                                             <form:option value="C">D</form:option>
                                            
                                        </form:select>
                                    </div>
                                   
                                    <div class="form-group col-md-3">
                                        <label for="inputPassword4">Remarks</label>
                                        <form:input path="otherRemarks" id="otherRemarks" class="form-control" maxlength="200"/>
                                    </div>
                                </div>
                            </c:if>
                            <c:if test="${partType eq 'E'}">
                                <input type="hidden" name="other6thPay" value="0"/>
                                <input type="hidden" name="other7thPay" value="0"/>
                                <input type="hidden" name="otherPost" value="0"/>
                                <input type="hidden" name="otherVacancy" value="0"/>
                                <input type="hidden" name="group" value=""/>
                                <input type="hidden" name="partType" value="E"/>
                                 <div class="form-row">
                                    <div class="form-group col-md-4">
                                        <label for="Category2">Category<strong style="color:red">*</strong></label>
                                        <form:select path="otherCategory" class="form-control" id="otherCategory" >
                                            <form:option value="Outsourced Employee">Outsourced Employee</form:option>
                                           <form:option value="Other Consultant">Other Consultant</form:option>
                                        </form:select>   
                                    </div>
                                    <div class="form-group col-md-4">
                                        <label for="inputPassword4">Number<strong style="color:red">*</strong></label>
                                        <form:input  path="otherSS" id="otherSS" onkeypress="return onlyIntegerRange(event)"  class="form-control" maxlength="4"/>
                                    </div>
                                  
                                   
                                    <div class="form-group col-md-4">
                                        <label for="inputPassword4">Remarks</label>
                                        <form:input path="otherRemarks" id="otherRemarks" class="form-control" maxlength="200"/>
                                    </div>
                                </div>
                             </c:if>    
                                

                            <div class="form-row">
                                <div class="form-group col-md-3">&nbsp;</div>
                                <div class="form-group col-md-3">&nbsp;</div>
                                <div class="form-group col-md-3">
                                    <input type="submit" name="action" id="btnSave" value="Save" class="btn btn-primary"  /> 
                                </div>
                            </div> 
                        </div>

                    </div>       
                </div>       




            </div>
        </div>



    </form:form>
</body>
</html>
