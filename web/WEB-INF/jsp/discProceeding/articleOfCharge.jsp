<%-- 
    Document   : articleOfCharge
    Created on : Feb 5, 2018, 4:10:28 PM
    Author     : manisha
--%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ page contentType="text/html;charset=UTF-8"%>

<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <style>
            table, th, td {
                border: 1px solid black;
                border-collapse: collapse;
            }
            th, td {
                padding: 5px;
                text-align: left;    
            }
            .table-responsive {
                max-height:450px;
                font-size: 10px;
            }
            .table-bordered{
                font-size: 12px;
            }
        </style>
        <script type="text/javascript">
         <%--   function validation() {
                if ($("#uploadDocument").val() == "") {
                    alert("please upload document");
                    $("#uploadDocument").focus();
                    return false;
                }

                var enterchar = $("#charge").val().length;
                if (enterchar > 100) {
                    alert("Max 100 character allowed.");
                    $("#charge").focus();
                    return false;
                }

                var enterchar = $("#chargeDetails").val().length;
                if (enterchar > 1000) {
                    alert("Max 1000 character allowed.");
                    $("#chargeDetails").focus();
                    return false;
                }
                var enterchar = $("#description").val().length;
                if (enterchar > 1000) {
                    alert("Max 1000 character allowed.");
                    $("#description").focus();
                    return false;
                }

            } --%>
            $(document).ready(function() {
                $("#articleOfCharge").on("keyup", function(evt) {
                    var enterchar = $(this).val().length;
                    if (enterchar > 1000) {
                        alert("Max 1000 character allowed.");
                        evt.preventDefault();
                        return false;
                    } else {
                        var status = "(" + enterchar + "/1000)";
                        $("#chargelen").html(status);
                    }
                });

                $("#statementOfImputation").on("keyup", function(evt) {
                    var enterchar = $(this).val().length;
                    if (enterchar > 1000) {
                        alert("Max 1000 character allowed.");
                        evt.preventDefault();
                        return false;
                    } else {
                        var status = "(" + enterchar + "/1000)";
                        $("#chargedtlslen").html(status);
                    }
                });

                $("#memoOfEvidence").on("keyup", function(evt) {
                    var enterchar = $(this).val().length;
                    if (enterchar > 1000) {
                        alert("Max 1000 character allowed.");
                        evt.preventDefault();
                        return false;
                    } else {
                        var status = "(" + enterchar + "/1000)";
                        $("#desclen").html(status);
                    }
                });

            });
            
             function savedocumentsforarticlecharge() {

                if ($("#articleOfCharge").val() == "") {
                    alert("Please input the value For Article Of Charge");
                    $("#articleOfCharge").focus();
                    return false;
                }
                 if ($("#statementOfImputation").val() == "") {
                    alert("Please input the value For Statement Of Imputation");
                    $("#statementOfImputation").focus();
                    return false;
                }
                 if ($("#memoOfEvidence").val() == "") {
                    alert("Please input the value For Memo OF Evidence");
                    $("#memoOfEvidence").focus();
                    return false;
                }
                 if ($("#briefDescriptionOfDocument").val() == "") {
                    alert("Please input the value For Brief Description Of the Document");
                    $("#briefDescriptionOfDocument").focus();
                    return false;
                }
               
               
                var fup = document.getElementById('articlesofChargeDocument');
                var fileName = fup.value;
                var ext = fileName.substring(fileName.lastIndexOf('.') + 1);
                var ext = ext.toLowerCase();

                var fup2 = document.getElementById('statementOfImputationDocument');
                var fileName2 = fup2.value;
                var ext2 = fileName2.substring(fileName2.lastIndexOf('.') + 1);
                var ext2 = ext2.toLowerCase();

                var fup3 = document.getElementById('memoofEvidenceDocument');
                var fileName3 = fup3.value;
                var ext3 = fileName3.substring(fileName3.lastIndexOf('.') + 1);
                var ext3 = ext3.toLowerCase();
                
                var fup4 = document.getElementById('descriptionOfDocument');
                var fileName4 = fup4.value;
                var ext4 = fileName4.substring(fileName3.lastIndexOf('.') + 1);
                var ext4 = ext4.toLowerCase();

                if (fileName != "" && (ext != "pdf" && ext != "zip")) {
                    alert("Upload pdf/zip files only For article of Charge");
                    fup.focus();
                    return false;
                }
                if (fileName2 != "" && (ext2 != "pdf" && ext2 != "zip")) {
                    alert("Upload pdf/zip files only For statement Of Imputation");
                    fup2.focus();
                    return false;
                }
                if (fileName3 != "" && (ext3 != "pdf" && ext3 != "zip")) {
                    alert("Upload pdf/zip files only For Any employee Activities and Issuedocument");
                    fup2.focus();
                    return false;
                }
                var fi = document.getElementById("articlesofChargeDocument");
                var fsize = fi.files.item(0).size;
                var file = Math.round((fsize / 1024));
                if (file >= 20000) {
                    alert("File too Big, please select a file less than 20mb");
                    $("#articlesofChargeDocument").val('');
                    return false;
                }

                var fi = document.getElementById("statementOfImputationDocument");
                var fsize = fi.files.item(0).size;
                var file = Math.round((fsize / 20000));
                if (file >= 2048) {
                    alert("File too Big, please select a file less than 20mb");
                    $("#statementOfImputationDocument").val('');
                    return false;
                }

                var fi = document.getElementById("memoofEvidenceDocument");
                var fsize = fi.files.item(0).size;
                var file = Math.round((fsize / 1024));
                if (file >= 20000) {
                    alert("File too Big, please select a file less than 20mb");
                    $("#memoofEvidenceDocument").val('');
                    return false;
                }
                var fi = document.getElementById("descriptionOfDocument");
                var fsize = fi.files.item(0).size;
                var file = Math.round((fsize / 1024));
                if (file >= 20000) {
                    alert("File too Big, please select a file less than 20mb");
                    $("#descriptionOfDocument").val('');
                    return false;

            }
        }
        </script>
    </head>
    <body>
        <div id="page-wrapper">
            <form:form action="saveDisccharge.htm" commandName="chargebean" method="post" class="form-horizontal" enctype="multipart/form-data">
                <div class="container-fluid">
                    <div class="panel panel-default">
                        <div class="panel-body">                       
                            <div class="form-group">
                                <label class="control-label col-sm-2" >Articles of Charge <span style="color: red">*</span> </label> 
                                <div class="col-sm-8"> 
                                    <form:hidden path="daId"/>
                                    <form:hidden path="dacid"/> 
                                    <form:textarea class="form-control" path="articleOfCharge"/><br/>
                                   <span style="font-style: italic;color: #008000;">(Not More than 1000 Character)</span> <span id="chargelen">(0/1000)</span>
                                </div>
                                <div  class="col-sm-2">                            
                                    <input type="file" name="articlesofChargeDocument" id="articlesofChargeDocument"  class="form-control-file"/>
                                    <span style="font-style: italic;color: #CD5C5C;">(Only pdf / zip File are allowed)</span>
                                </div> 
                            </div>

                            <div class="form-group">
                                <label class="control-label col-sm-2" >Statement Of Imputation <span style="color: red">*</span> </label>
                                <div class="col-sm-8">
                                    <form:textarea class="form-control" path="statementOfImputation"/><br/>
                                   <span style="font-style: italic;color: #008000;">(Not More than 1000 Character)</span> <span id="chargedtlslen">(0/1000)</span>
                                </div>
                                <div  class="col-sm-2">                            
                                    <input type="file" name="statementOfImputationDocument" id="statementOfImputationDocument"  class="form-control-file"/>
                                     <span style="font-style: italic;color: #CD5C5C;">(Only pdf / zip File are allowed)</span>
                                </div> 
                            </div>

                            <div class="form-group">
                                <label class="control-label col-sm-2">Memo of Evidence <span style="color: red">*</span> </label>
                                <div class="col-sm-8">
                                    <form:textarea class="form-control" path="memoOfEvidence"/><br/>
                                    <span style="font-style: italic;color: #008000;">(Not More than 1000 Character)</span> <span id="chargedtlslen">(0/1000)</span>
                                </div>
                                <div class="col-sm-2"> 
                                    <input type="file" name="memoofEvidenceDocument" id="memoofEvidenceDocument"  class="form-control-file"/>
                                     <span style="font-style: italic;color: #CD5C5C;">(Only pdf / zip File are allowed)</span>
                                </div>
                            </div>
                                    
                        </div>
                        <div class="panel-footer">
                            <input type="hidden" name="openfrom" value="${param.openfrom}"/>
                            <input type="submit" name="action" value="Save" class="btn btn-default" onclick="return savedocumentsforarticlecharge()"/>

                           <c:if test="${chargebean.dacid > 0}">
                                <input type="submit" name="action" value="Delete" class="btn btn-default"  onclick="return confirm('Are you sure to Delete?')"/>
                            </c:if> 
                            <input type="submit" name="action" value="Cancel" class="btn btn-default"/>
                        </div>

                    </div>
                </div>
            </form:form>
        </div>
    </body>
</html>