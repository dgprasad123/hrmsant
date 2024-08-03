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
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                getDeptWiseOfficeList();
            });
            function getDeptWiseOfficeList() {
                var deptcode = $('#hidAuthDeptCode').val();
                var url = 'getOfficeListJSON.htm?deptcode=' + deptcode;
                $('#hidAuthOffCode').empty();
                $('#hidAuthOffCode').append('<option value="">--Select Office--</option>');
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#hidAuthOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                });
            }

            function getOfficeWisePostList() {
                var offcode = $('#hidAuthOffCode').val();

                var url = 'getPostListTrainingJSON.htm?deptcode=' + $('#hidAuthDeptCode').val() + '&offcode=' + offcode;
                $('#authSpc').empty();
                $('#authSpc').append('<option value="">--Select Post--</option>');

                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#authSpc').append('<option value="' + obj.value + '">' + obj.label + ' (' + obj.desc + ')</option>');
                    });
                });
            }
            function getPost(obj, idx) {
                $('#authName').val(obj[idx].text);
                $('#authVal').val(obj[idx].value);
            }
            function saveCheck()
            {
                if (!$('#chkComplete')[0].checked)
                {
                    alert("Please Agree that you have completed the Training.");
                    $('#chkComplete')[0].focus();
                    return false;
                }
                if ($('#hasIntimation').val() == 'N' && $('#intimationFile').val() == '')
                {
                    alert("Please attach Intimation Letter.");
                    $('#intimationFile')[0].focus();
                    return false;
                }
                if ($('#hasReceipt').val() == 'N' && $('#receiptFile').val() == '')
                {
                    alert("Please attach Money Receipt File.");
                    $('#receiptFile')[0].focus();
                    return false;
                }                
                if ($('#hasCertificate').val() == 'N' && $('#certificateFile').val() == '')
                {
                    alert("Please attach Certificate File.");
                    $('#certificateFile')[0].focus();
                    return false;
                }
                if ($('#hasBank').val() == 'N' && $('#bankFile').val() == '')
                {
                    alert("Please attach Bank Account Details.");
                    $('#bankFile')[0].focus();
                    return false;
                }
                //return false;
            }
        </script>
    </head>
    <body>
        <form:form action="SaveTrainingDocuments.htm" method="POST" enctype="multipart/form-data" commandName="TrainingDocumentBean" onsubmit="return saveCheck();">
            <input type="hidden" name="trainingProgramId" value="${trainingProgramId}" />
            <input type="hidden" name="hasReceipt" id="hasReceipt" value="${otBean.hasReceipt}" />
            <input type="hidden" name="hasCertificate" id="hasCertificate" value="${otBean.hasCertificate}" />
            <input type="hidden" name="hasIntimation" id="hasIntimation" value="${otBean.hasIntimation}" />
            <input type="hidden" name="hasBank" id="hasBank" value="${otBean.hasBank}" />
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <strong style="color:#008900;">Upload Documents</strong>
                    </div>
                    <div class="panel-body">

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="instituteCode">Institute Name</label>
                            </div>
                            <div class="col-lg-6">   
                                ${otBean.instituteName}                            
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="txtTrainingProgram"> Training Program</label>
                            </div>
                            <div class="col-lg-5">
                                ${otBean.txtTrainingProgram}  
                            </div>
                        </div>                            
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="txtAmount">Course Fee</label>
                            </div>
                            <div class="col-lg-2">
                                ${otBean.txtAmount}                   
                            </div>                            
                        </div>
                    </div>
                    <div class="container-fluid">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <strong style="color:#008900;">Attach Documents</strong>
                            </div>
                            <div class="panel-body">
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-2">
                                    </div>
                                    <div class="col-lg-6">
                                        <input type="checkbox" name="chkComplete" id="chkComplete" />
                                        <label for="chkComplete" style="color:#FF0000;">I accept that I have successfully completed the online training with certificate.</label>
                                    </div>
                                </div>
                                <c:if test = "${otBean.hasIntimation == 'N'}">
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-2">
                                        <label for="intimationFile">Intimation Letter:</label>
                                    </div>
                                    <div class="col-lg-5">
                                        <input type="file" name="intimationFile" id="intimationFile" accept="application/pdf" />
                                        <span style="color:#FF0000;font-size:9pt;font-style:italic;">(Please attach only PDF file.)</span>
                                    </div>
                                </div> 
                                </c:if>                                
                                <c:if test = "${otBean.hasReceipt == 'N'}">
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-2">
                                        <label for="receiptFile">Money Receipt:</label>
                                    </div>
                                    <div class="col-lg-5">
                                        <input type="file" name="receiptFile" id="receiptFile" accept="application/pdf" />
                                        <span style="color:#FF0000;font-size:9pt;font-style:italic;">(Please attach only PDF file.)</span>
                                    </div>
                                </div> 
                                </c:if>
                                <c:if test = "${otBean.hasCertificate == 'N'}"><div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-2">
                                        <label for="certificateFile">Certificate File:</label>
                                    </div>
                                    <div class="col-lg-6">   
                                        <input type="file" name="certificateFile" id="certificateFile" accept="application/pdf" />
                                        <span style="color:#FF0000;font-size:9pt;font-style:italic;">(Please attach only PDF file.)</span>
                                    </div>
                                    
                                </div>
                                    </c:if>
                                <c:if test = "${otBean.hasBank == 'N'}">
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-2">
                                        <label for="bankFile">Bank Account Detail:</label>
                                    </div>
                                    <div class="col-lg-5">
                                        <input type="file" name="bankFile" id="bankFile" accept="application/pdf" />
                                        <span style="color:#FF0000;font-size:9pt;font-style:italic;">(Please attach only PDF file.)</span>
                                    </div>
                                </div> 
                                </c:if>                           


                            </div>
                            <div class="panel-footer">
                                <button type="submit" name="submit" value="Save" class="btn btn-primary">Upload Documents</button>
                            </div>
                        </div>
                    </div>                            

                </div>
            </div>

                       
        </form:form>
    </body>
</html>