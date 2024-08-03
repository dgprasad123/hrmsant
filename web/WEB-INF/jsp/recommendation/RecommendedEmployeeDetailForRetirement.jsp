<%-- 
    Document   : RecommendedEmployeeDetailForRetirement
    Created on : 20 Oct, 2020, 1:06:56 PM
    Author     : Manisha
--%>



<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        

        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <style type="text/css">
            .loader {
                border: 16px solid #f3f3f3; /* Light grey */
                border-top: 16px solid #3498db; /* Blue */
                border-radius: 50%;
                width: 40px;
                height: 40px;
                animation: spin 2s linear infinite;
            }

            @keyframes spin {
                0% { transform: rotate(0deg); }
                100% { transform: rotate(360deg); }
            }
            .myModalBody{}
        </style>
        <script type="text/javascript">
            $(document).ready(function () {
                maxLength(document.getElementById("recommendationandCommendation"));
            });

            function savedocuments() {

                if ($("#reasonforrecommendation").val() == "") {
                    alert("Overall reasons to nominate for Premature Retirement cannot be blank");
                    $("#reasonforOverall reasons to nominate for Premature Retirementrecommendation").focus();
                    return false;
                }

                var fup = document.getElementById('reasonforrecommendationDocument');
                var fileName = fup.value;
                var ext = fileName.substring(fileName.lastIndexOf('.') + 1);
                var ext = ext.toLowerCase();


                if ((ext != "pdf" && ext != "zip")) {
                    alert("Upload pdf/zip files only For Any recommendation and Commendation document");
                    fup.focus();
                    return false;
                }
                var fi = document.getElementById("reasonforrecommendationDocument");
                var fsize = fi.files.item(0).size;
                var file = Math.round((fsize / 1024));
                if (file >= 20000) {
                    alert("File too Big, please select a file less than 20mb");
                    $("#reasonforrecommendationDocument").val('');
                    return false;
                }

            }



            function maxLength(el) {
                if (!('maxLength' in el)) {
                    var max = el.attributes.maxLength.value;
                    el.onkeypress = function () {
                        if (this.value.length >= max)
                            return false;
                    };
                }
            }



        </script>
    </head>

    <body>
        <form:form action="saveRecommendedEmployeeDetail.htm" commandName="recommendationDetailBean" method="post" class="form-horizontal" enctype="multipart/form-data">
            <div class="container-fluid">

                <div class="panel panel-default">
                    <div class="panel-body"> 
                        <div class="form-group">
                            <label class="control-label col-sm-3" >1.Name and designation of the employee:</label>
                            <div class="col-sm-8">                                
                                <form:hidden path="recommendationId"/>
                                <form:hidden path="recommendeddetailId"/>
                                <b>${recommendationDetailBean.recommendedempname}, ${recommendationDetailBean.recommendedpost}</b>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-3" >2.Name Of the Office:</label>
                            <div class="col-sm-8"> <b>${recommendationDetailBean.recommendedempofficename}</b>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="control-label col-sm-3" >3. Overall reasons to nominate for Premature Retirement:<span style="color: red">*</span></label>
                            <div class="col-sm-7">
                                <c:if test="${empty recommendationDetailBean.submittedondate}">
                                    <form:textarea class="form-control" path="reasonforrecommendation" maxlength="3400"/>
                                    <span style="font-size: 12px;color: red;">(Not more than 200 Words)</span>
                                </c:if>
                                <c:if test="${not empty recommendationDetailBean.submittedondate}">
                                    <p>${recommendationDetailBean.reasonforrecommendation}</p>
                                </c:if>
                            </div>
                            <div class="form-group row">
                                <c:if test="${empty recommendationDetailBean.reasonforrecommendationoriginalfilename && empty recommendationDetailBean.submittedondate}">
                                    <label for="fa" class="col-sm-2 col-form-label">Document (if any) <span style="font-size: x-small;color: red;">(Max 20 MB)</span></label>
                                    <div class="col-sm-2">
                                        <input type="file" class="form-control-file" id="reasonforrecommendationDocument" name='reasonforrecommendationDocument'>                                                                            
                                    </div>
                                </c:if>
                                <c:if test="${not empty recommendationDetailBean.reasonforrecommendationoriginalfilename}">
                                    <a href="downloadFile.htm?recommendeddetailId=${recommendationDetailBean.recommendeddetailId}&documentTypeName=reasonrecommendation">${recommendationDetailBean.reasonforrecommendationoriginalfilename}</a>
                                </c:if>

                            </div> 
                        </div>
                    </div>


                </div>
            </div>

        </div>

        <div class="panel-footer">
            <c:if test="${empty recommendationDetailBean.submittedondate}">
                <input type="submit" name="action" value="Save" class="btn btn-default" onclick="return savedocuments()" />
            </c:if>
            
            <input type="submit" name="action" value="Back" class="btn btn-default" /> 
            <span class="label label-success">${msg}</span>
        </div>        
    </form:form>
</body>
</html>


