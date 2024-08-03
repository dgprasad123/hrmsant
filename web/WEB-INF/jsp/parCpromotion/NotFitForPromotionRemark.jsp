<%-- 
    Document   : NotFitForPromotionRemark
    Created on : Mar 19, 2020, 4:00:22 PM
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
            var allowedExtensions = /(\.pdf)$/i;
            function uploadValidation(me) {
                if ($(me).val() != '') {
                    var fileId = $(me).attr("id");
                    var fi = document.getElementById(fileId);
                    var fsize = fi.files.item(0).size;
                    var file = Math.round((fsize / 1024));
                    if (file >= 4096) {
                        alert("File too Big, please select a file less than 4mb");
                        $("#" + fileId).val('');
                        return false;
                    } else {
                        if (!allowedExtensions.exec($("#" + fileId).val())) {
                            alert('Please upload file having extensions .pdf only.');
                            $("#" + fileId).val('');
                            return false;
                        } else {
                            $("#clearbtn").show();
                            return true;
                        }

                    }
                }
            }
            function validation() {
                if ($("#reportingRemarks").val() == "") {
                    alert("please enter the remarks");
                    $("#reportingRemarks").focus();
                    return false;
                }
                var enterchar = $("#reportingRemarks").val().length;
                if (enterchar > 100) {
                    alert("Max 100 character allowed.");
                    $("#reportingRemarks").focus();
                    return false;
                }
            }
        </script>
    </head>
    <body>
        <div id="page-wrapper">
            <form:form action="remarkOfNotFitForPromotionReport.htm" method="post" commandName="groupCEmployee" class="form-horizontal" enctype="multipart/form-data">
                <div class="container-fluid">
                    <div class="panel panel-default">
                        <div class="panel-body">                       

                            <div class="form-group">
                                <label class="control-label col-sm-2" >Remarks<span style="color: red">*</span></label>
                                <div class="col-sm-8"> 
                                    <form:hidden path="isfitforShoulderingResponsibilityReviewing"/>
                                    <form:hidden path="taskId"/>
                                    <form:hidden path="isfitforShoulderingResponsibilityAccepting"/>
                                    <form:hidden path="promotionId"/>
                                    <form:hidden path="groupCpromotionId"/> 
                                    <form:hidden path="remarkauthoritytype"/>
                                    <form:textarea class="form-control" path="reportingRemarks"/>             
                                </div>
                                <span style="font-style: italic;color: #008000;">(Not More than 1000 Character)</span> <span id="chargelen">(0/1000)</span>
                            </div>

                            <c:if test="${groupCEmployee.remarkauthoritytype ne 'REVIEWING' and groupCEmployee.remarkauthoritytype ne 'ACCEPTING'}">
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Document</label>
                                    <div class="col-sm-2"> 
                                        <c:if  test="${not empty groupCEmployee.originalFilename}">
                                            ${groupCEmployee.originalFilename}
                                        </c:if>

                                        <input type="file" name="uploadDocument" id="uploadDocument"  class="form-control-file" onchange="return uploadValidation(this)"/>
                                        <span style="color: red;">(Only .pdf files are allowed)</span>
                                    </div>
                                </div>
                            </c:if>
                        </div>
                        <div class="panel-footer">
                            <input type="submit" name="action" value="Save" class="btn btn-default" onclick="return validation()"/>
                            <input type="submit" name="action" value="Back" class="btn btn-default"/>
                        </div>

                    </div>
                </div>
            </form:form>
        </div>
    </body>
</html>