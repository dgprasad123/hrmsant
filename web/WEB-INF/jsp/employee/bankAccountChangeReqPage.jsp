<%-- 
    Document   : bankAccountChangeReqPage
    Created on : 21 Sep, 2020, 6:49:48 PM
    Author     : Surendra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script>
            $(document).ready(function () {

            });

            function validate() {
                if ($('#sltBank').val() == '') {
                    alert('Please select Bank.');
                    return false;
                }
                if ($('#sltbranch').val() == '') {
                    alert('Please select Branch.');
                    return false;
                }

                var numbers = /^[0-9]+$/;
                if ($('#bankaccno').val() == '') {
                    alert('Bank Acoount Number should not be blank.');
                    document.getElementById('bankaccno').focus();
                    return false;

                } else {

                    if (!$('#bankaccno').val().match(numbers)) {
                        alert('Please input numeric characters only.');
                        document.getElementById('bankaccno').focus();
                        return false;
                    }
                }
                if ($('#retype').val() == '') {
                    alert('Retype Bank Account Number.');
                    document.getElementById('retype').focus();
                    return false;

                } else {

                    if (!$('#retype').val().match(numbers)) {
                        alert('Please input numeric characters only.');
                        document.getElementById('retype').focus();
                        return false;
                    }
                }

                if ($('#bankaccno').val() != $('#retype').val()) {
                    alert('Bank Acoount Number not match with Retype Bank Account Number.');
                    return false;
                }

                var allowedFiles = [".jpeg", ".jpg", ".JPEG", ".png", ".pdf"];
                var fileUpload = document.getElementById("docProof");
                var lblError = document.getElementById("lblError");
                var regex = new RegExp("([a-zA-Z0-9\s_\\.\-:])+(" + allowedFiles.join('|') + ")$");
                if (!regex.test(fileUpload.value.toLowerCase())) {
                    lblError.innerHTML = "Please upload files having extensions: <b>" + allowedFiles.join(', ') + "</b> only.";
                    return false;
                }
                lblError.innerHTML = "";

                var fi = document.getElementById("docProof");
                var fsize = fi.files.item(0).size;
                var file = Math.round((fsize / 1024));
                if (file >= 2048) {
                    alert("File too Big, please select a file less than 2mb");
                    
                    return false;
                }


                var result = confirm("Are you sure to change the bank details?");
                if (result) {
                    return true;
                } else {
                    return false;
                }
            }

            function getBranchList(me) {

                $('#sltbranch').find('option:not(:first)').remove();
                $.ajax({
                    type: "POST",
                    url: "bankbranchlistJSON.htm?bankcode=" + $(me).val(),
                    success: function (data) {
                        $.each(data, function (i, obj)
                        {
                            $('#sltbranch').append($('<option>', {
                                value: obj.branchcode,
                                text: obj.branchname

                            }));

                        });
                    }
                });
            }



        </script>
    </head>
    <body>
        <form:form action="updateRequestbankaccount.htm" method="post" commandName="AqmastModel" enctype="multipart/form-data">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Bank Account
                    </div>
                    <div class="panel-body">
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-12">
                                <label>Please verify bank account number and ifsc code before change.</label>
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                Select Bank
                            </div>
                            <div class="col-lg-3">   
                                <form:select path="bankName" id="sltBank" class="form-control"  onchange="getBranchList(this);">
                                    <form:options items="${bankList}" itemValue="bankcode" itemLabel="bankname"/>
                                </form:select>
                            </div>
                            <div class="col-lg-3">
                                Select Branch
                            </div>
                            <div class="col-lg-3">
                                <form:select path="branchName" id="sltbranch" class="form-control">
                                    <form:options items="${branchList}" itemValue="branchcode" itemLabel="branchname"/>
                                </form:select>
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                Bank Account Number
                            </div>
                            <div class="col-lg-3">   
                                <form:input path="bankAccNo" id="bankaccno" class="form-control"
                                            onselectstart="return false" onpaste="return false;" oncopy="return false" 
                                            oncut="return false" ondrag="return false" ondrop="return false" autocomplete="off"/>
                            </div>
                            <div class="col-lg-3">
                                <c:if test="${not empty DUPLICATE}">
                                    <span style="font-size: 16px; font-weight: bold;color: #F00000;">
                                        Duplicate Bank Account Number(HRMS ID - <c:out value="${DUPLICATE}"/>)
                                    </span>
                                </c:if>
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                Retype Bank Account Number
                            </div>
                            <div class="col-lg-3">   
                                <input type="text" name="retype" id="retype" class="form-control"
                                       onselectstart="return false" onpaste="return false;" oncopy="return false" 
                                       oncut="return false" ondrag="return false" ondrop="return false" autocomplete="off"/>
                            </div>
                        </div>   
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                Upload Document Proof 
                            </div>
                            <div class="col-lg-3">   
                                <input type="file" id="docProof" name="bankdocProof" />  <span id="lblError" style="color: red;"></span>
                            </div>
                            <div class="col-lg-3">
                                <span style="font-size: 16px; font-weight: bold;color: blue;">
                                    Uploaded Document must not be more than 2MB.
                                </span>
                            </div>
                        </div>     
                    </div>
                    <div class="panel-footer">
                        <a href="showbankaccountList.htm"><button type="button" name="back" value="Back" class="btn btn-primary">Back</button></a>
                        <button type="submit" name="Submit" value="Change" class="btn btn-success" onclick="return validate()" >Change</button>

                    </div>
                </div>
            </div>
        </form:form>

    </body>
</html>
