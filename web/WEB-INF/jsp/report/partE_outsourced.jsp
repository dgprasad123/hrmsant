<%-- 
    Document   : partE_outsourced
    Created on : 18 May, 2019, 12:41:22 PM
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



                if (otherCategory == "") {
                    alert("Please select Category Type");
                    return false;
                }

                if ($('#otherSS').val() == '') {
                    alert("Number cannot be blank");
                    return false;
                }

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
            function delete_otherEst(aerOtherId, aerId, partType) {
                var conf = confirm("Do you want to confirm to delete this Information");
                if (conf) {

                    window.location = "deleteOtherEst.htm?aerOtherId=" + aerOtherId + "&aerId=" + aerId + "&partType=" + partType;
                }
            }
        </script>   
    </head>
    <body>
        <jsp:include page="aerTab.jsp">
            <jsp:param name="menuHighlight" value="PART_E" />
        </jsp:include>
        <form:form action="createOtherEst.htm" commandName="command" >


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
                        <div class="panel-heading">PART-E ( Outsourced / on Contract)</div>

                        <div class="panel-body">
                            <c:if test="${Submittedstatus eq 'true'}">
                                <form:hidden path="financialYear"/>
                                <form:hidden path="aerId"/>
                                <input type="hidden" name="other6thPay" value="0"/>
                                <input type="hidden" name="other7thPay" value="0"/>
                                <input type="hidden" name="otherPost" value="0"/>
                                <input type="hidden" name="otherVacancy" value="0"/>
                                <input type="hidden" name="group" value=""/>
                                <input type="hidden" name="partType" value="E"/>

                                <div class="form-row">
                                    <div class="form-group col-md-6">
                                        <label for="Category2">Category<strong style="color:red">*</strong></label>
                                        <form:select path="otherCategory" class="form-control" id="otherCategory" >
                                            <option value="">Select  Category </option>                                      
                                            <form:option value="Outsourced Employee / On Contract">Outsourced Employee / On Contract</form:option>
                                            <form:option value="Other Consultant">Other Consultant</form:option>
                                        </form:select>   
                                    </div>
                                    <div class="form-group col-md-3">
                                        <label for="inputPassword4">Number<strong style="color:red">*</strong></label>
                                        <form:input path="otherSS" id="otherSS" class="form-control" onkeypress="return onlyIntegerRange(event)" maxlength="4"/>
                                    </div>


                                    <div class="form-group col-md-3">
                                        <label for="inputPassword4">Remarks</label>
                                        <form:input path="otherRemarks" id="otherRemarks" class="form-control" maxlength="200"/>
                                    </div>
                                </div>

                                <div class="form-row">
                                    <div class="form-group col-md-3">&nbsp;</div>
                                    <div class="form-group col-md-3">&nbsp;</div>
                                    <div class="form-group col-md-3">
                                        <input type="submit" name="action" id="btnSave" value="Save" class="btn btn-primary"  onclick="return validateData()"/> 
                                    </div>
                                </div> 
                            </c:if>
                        </div>
                        <table class="table table-bordered">
                            <thead>
                                <tr class="bg-info text-white">
                                    <th>#</th>
                                    <th>Category</th>
                                    <th>Number</th>
                                    <th>Remarks</th>                          
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${aeDetails}" var="ae" varStatus="cnt">
                                    <tr>
                                        <th scope="row">${cnt.index+1}</th>
                                        <td>${ae.otherCategory}</td>  
                                        <td>${ae.otherSS}</td>
                                        <td>${ae.otherRemarks}</td>
                                        <td>
                                            <c:if test="${Submittedstatus eq 'true'}">
                                                <a href="editOtherEst.htm?aerOtherId=${ae.aerOtherId}&aerId=${ae.aerId}&partType=E"><span class="glyphicon glyphicon-pencil"></span>Edit</a>
                                                &nbsp;
                                                <a href=#" onclick="delete_otherEst(${ae.aerOtherId},${ae.aerId}, 'E')" ><span class="glyphicon glyphicon-remove"></span> Delete</a>
                                            </c:if>&nbsp;
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>        

                    </div> 





                </div>
            </div>



        </form:form>
    </body>
</html>


