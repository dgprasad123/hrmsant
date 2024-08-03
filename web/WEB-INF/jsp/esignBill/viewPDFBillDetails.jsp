<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">                
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <style type="text/css">
            .fixed-panel {
                min-height: 500;
                max-height: 500;
                overflow-y: scroll;
            }
            .table tbody tr > td.warning {
                background-color: #fcf8e3 !important;
            }

        </style>

        <script type="text/javascript">
                    $(document).ready(function(){
            $("input[type=radio][name=rdToggleSignButton]").change(function(){
            var rdvalue = $(this).val();
                    if (rdvalue == "DSC"){
            $("#digital_sign_blk").show();
                    $("#eSignBlk").hide();
            } else if (rdvalue == "ESC"){
            $("#digital_sign_blk").hide();
                    $("#eSignBlk").show();
            }
            });
                    $("#eSignModal").on("show.bs.modal", function(e) {
            var link = $(e.relatedTarget);
                    $(this).find(".modal-body").load(link.attr("href"));
            });
                    var myfile = "";
                    $('#esignfile').on('change', function() {
            myfile = $(this).val();
                    var ext = myfile.split('.').pop();
                    if (ext != "pdf") {
            alert("Invalid File Type");
                    $(this).val('');
                    return false;
            } else {
            //alert(this.files[0].size);
            var fsize = this.files.item(0).size;
                    var file = Math.round((fsize / 1024));
                    if (file >= 3072) {
            alert("File too Big, please select a file less than 3mb");
                    $(this).val('');
                    return false;
            }
            }
            });
            });
                    function deletePdfLog(billno, esignLogId){
                    var conf = confirm("Do you want to delete this PDF file?");
                            if (conf){
                    //  alert("deletPdfunsignedLogFile.htm?billNo=" + billno + "&esignLogId=" + esignLogId);
                    window.location = "deletPdfunsignedLogFile.htm?billNo=" + billno + "&esignLogId=" + esignLogId;
                    }
                    }
            function deleteSignedPdfLog(billno, esignLogId){
            var conf = confirm("Do you want to delete this Signed PDF file?");
                    if (conf){
            var test = "deletesignedpdf.htm?billNo=" + billno + "&esignLogId=" + esignLogId;
                    //alert(test);
                    window.location = "deletesignedpdf.htm?billNo=" + billno + "&esignLogId=" + esignLogId;
            }
            }
            function getResponseFromTokenRequestData(encryptedKeyID, encryptedData, billNo) {
            var pass = prompt("Please enter your USB Token Password");
                    if (pass == '' || pass == null){
            alert("Please enter your Password.");
                    return false;
            }
            //  var pass="9437634989";
            $('#digital_sign_blk').css('display', 'none');
                    $('#loader_blk').css('display', 'block');
                    $('#id_dsign_button').hide();
                    $.ajax({
                    type: "POST",
                            url: "https://localhost.emudhra.com:26769/DSC/ListToken",
                            contentType: 'application/json',
                            data: JSON.stringify({"encryptionKeyID": encryptedKeyID, "encryptedRequest": encryptedData}),
                            success: function(data) {
                            // console.log(data);
                            // return false;
                            if (data.status === 1) {
                            var responseData = data.responseData;
                                    // $("#console").append("<div>" + responseData + "</div>");
                                    decToken(responseData, billNo, pass);
                            } else {
                            $('#digital_sign_blk').css('display', 'block');
                                    $('#loader_blk').css('display', 'none');
                                    alert("Error At ListToken");
                            }
                            }
                    });
            }
            function decToken(requestData, billNo, pass) {
            //  alert("esignLogId");
            $.ajax({
            type: "post",
                    url: "decListToken.htm",
                    data: {"encryptedRequestData": requestData},
                    success: function(responseData) {
                    //   alert("responseData=="+responseData);
                    // return false;
                    console.log(responseData);
                            // alert(responseData.encryptionKeyID);
                            responseData = responseData.replace('"encryptionKeyID"', 'encryptionKeyID');
                            responseData = responseData.replace('"displayname"', 'displayname');
                            responseData = responseData.replace('"encryptedData"', 'encryptedData');
                            responseData = responseData.replace('encryptionKeyID', '"encryptionKeyID"');
                            responseData = responseData.replace('displayname', '"displayname"');
                            responseData = responseData.replace('encryptedData', '"encryptedData"');
                            const obj = JSON.parse(responseData);
                            if (obj.displayname == ""){
                    alert("Error with Inserted Dongle");
                            return false;
                    }
                    $("#console").append("<div>" + obj.encryptionKeyID + "</div>");
                            $("#console").append("<div>" + obj.encryptedData + "</div>");
                            $("#console").append("<div>Displayname:" + obj.displayname + "</div>");
                            getCertificateData(obj, billNo, pass);
                            $('#digital_sign_blk').css('display', 'block');
                            $('#loader_blk').css('display', 'none');
                    }
            });
            }
            function getCertificateData(decTokenData, billNo, pass) {
            //const obj = JSON.parse(decTokenData);
            $.ajax({
            type: "POST",
                    url: "https://localhost.emudhra.com:26769/DSC/ListCertificate",
                    contentType: 'application/json',
                    data: JSON.stringify({"encryptedRequest": decTokenData.encryptedData, "encryptionKeyId": decTokenData.encryptionKeyID}),
                    success: function(data) {
                    if (data.status == 1) {
                    var encCertificate = data.responseData;
                            decListCertificate(encCertificate, decTokenData.displayname, billNo, pass)
                    } else {
                    $('#digital_sign_blk').css('display', 'block');
                            $('#loader_blk').css('display', 'none');
                            alert("Error At getCertificate");
                    }
                    }
            });
            }
            function decListCertificate(encCertificate, certdisplayname, billNo, pass) {
            // alert(encCertificate);
            // alert(certdisplayname);
            $.ajax({
            type: "POST",
                    //url: "decListCertificate.htm", 
                    url: "esingpdfpages.htm",
                    data: {"encCertificate": encCertificate, "certdisplayname": certdisplayname, "billNo": billNo, "pass": pass},
                    success: function(responsedata) {
                    //  alert(responsedata); 
                    //   return false;
                    //    alert("decListCertificate=="+responseData);
                    generatePDFajax(responsedata, certdisplayname, billNo);
                            $("#console").append("<div>Displayname:" + responsedata + "</div>");
                            $('#digital_sign_blk').css('display', 'block');
                            $('#loader_blk').css('display', 'none');
                    }
            });
            }
            function generatePDFajax(certificatedata, certdisplayname, billNo) {
            const obj = JSON.parse(certificatedata);
                    $.ajax({
                    type: "POST",
                            url: "https://localhost.emudhra.com:26769/DSC/PKCSBulkSign",
                            contentType: 'application/json',
                            data: JSON.stringify({"encryptedRequest": obj.encryptedData, "encryptionKeyId": obj.encryptionKeyID}),
                            success: function(response) {
                            if (response.errorMessage){
                            alert("ERROR: " + response.errorMessage);
                                    $('#digital_sign_blk').css('display', 'block');
                                    $('#loader_blk').css('display', 'none');
                                    return false;
                            }

                            responseData = response.responseData;
                                    $("#console").append("<div>PKCSBulkSign:" + responseData + "</div>");
                                    generatePDFFile(responseData, certdisplayname, obj.tempFilePath, billNo);
                                    $('#digital_sign_blk').css('display', 'block');
                                    $('#loader_blk').css('display', 'none');
                            }
                    });
            }

            function generatePDFFile(responseData, certdisplayname, temppath, billNo) {
            $.ajax({
            type: "post",
                    url: "generatePDFFile.htm",
                    data: {"responseData": responseData, "certdisplayname": certdisplayname, "tempPath": temppath, "billNo": billNo},
                    success: function(data) {
                    //  alert("generatePDFFile=="+data);   
                    // console.log(data.responsedata);
                    $('#digital_sign_blk').css('display', 'block');
                            $('#loader_blk').css('display', 'none');
                            alert("Process Completed");
                    }
            });
            }

            function openESignWindow(billno){
            window.open("eSignWDList.htm?billNo=" + billno);
            }

            function uploadEsignPDF(){
            if ($("#esignfile").val() == ""){
            alert("Please Upload File");
            } else{
            let formData = new FormData();
                    formData.append("file", esignfile.files[0]);
                    formData.append("billno", $("#hidbillno").val());
                    formData.append("typeofbill", $("#typeofbill").val());
                    formData.append("slno", $("#slno").val());
                    $.ajax({
                    url: "UploadESignPDF.htm",
                            type: "POST",
                            data: formData,
                            success: function(data){
                            alert("File Uploaded");
                                    //$("#eSignUploadModal").toggle();
                                    location.reload();
                            },
                            cache: false,
                            contentType: false,
                            processData: false
                    });
            }
            }
        </script>   
    </head>
    <body>
        <input type="hidden" name="billNo" id="billNo" value="${billNo}"/>
        <input type="hidden" name="typeofbill" id="typeofbill" value="${typeofbill}"/>
        <input type="hidden" name="slno" id="slno" value="${slno}"/>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <a href="getPayBillList.htm?billNo=${billNo}" class="btn btn-primary">Return</a>
                </div>
                <div class="panel-body fixed-panel">
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th width="5%">&nbsp;</th>
                                <th width="5%">Sl No</th>
                                <th>PDF File Name</th>
                                <th>PDF Download</th>

                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:set var="totalcnt" value="N" />
                            <c:forEach items="${unsignedpdfList}" var="pdffile" varStatus="cnt">
                                <tr>
                                    <td><input type="checkbox" name="pdflog" value="${pdffile.esignLogId}" checked='checked'/></td>
                                    <td>${cnt.index+1}</td>
                                    <td>${pdffile.unsignedFile}</td>
                                    <td> <c:if test="${pdffile.signedStatus ne 'Y'}">
                                        <a href="downlaodBillBrowserPDFFile.htm?esignLogId=${pdffile.esignLogId}"><img border="0" alt="PDF" src="images/pdf.png" height="20"></a>
                                        </c:if>

                                        <c:if test="${pdffile.signedStatus eq 'Y'}">
                                            <a href="downlaodSignedPDFFile.htm?esignLogId=${pdffile.esignLogId}"><img border="0" alt="PDF" src="images/digital-pdf.png" height="20"></a><br/>

                                        </c:if>


                                    </td>
                                    <td>
                                        <c:if test="${pdffile.signedStatus eq 'Y'}">                                         
                                            <input type="button" name="deleteSignedPdfLog" value=" Delete Signed PDF" onclick="deleteSignedPdfLog('${pdffile.billNo}',${pdffile.esignLogId})" class="btn btn-warning" />

                                        </c:if>

                                        <c:if test="${ checksignStatus eq 'N'}">
                                            <img border="0" alt="PDF DELETE" src="images/Delete-icon.png" height="20" onclick="deletePdfLog('${pdffile.billNo}',${pdffile.esignLogId})"  />
                                        </c:if>
                                    </td>
                                </tr>
                                <c:set var="totalcnt" value="Y" />
                            </c:forEach>
                            <tr>
                                <td>&nbsp;</td>
                            </tr>
                            <tr>
                                <td>&nbsp;</td>
                                <td>&nbsp;</td>
                                <td align="center">
                                    <input type="radio" name="rdToggleSignButton" value="DSC">&nbsp;Digital Signature(DSC)&nbsp;&nbsp;&nbsp;&nbsp;
                                    <input type="radio" name="rdToggleSignButton" value="ESC">&nbsp;E-Signature(OTP)
                                    <div>
                                        <c:if test="${allowEsign eq 'Y' && ddohrmsid eq loginId && checksignStatus eq 'N' && totalcnt eq 'Y' && (billStatusId==2||billStatusId==4||billStatusId==8)}">
                                            <span id="digital_sign_blk" style="display:none;">
                                                <input type="button" id="id_dsign_button" name="button" value="Digital Sign PDFs" class="btn btn-danger" onclick="getResponseFromTokenRequestData('${esign.encryptedKeyID}', '${esign.encryptedData}', '${billNo}')"/>
                                            </span>
                                            <span id="eSignBlk" style="display:none;">                                                
                                                <c:if test="${typeofbill eq 'PAY'}">
                                                    <a href="eSignWDList.htm?billNo=${billNo}" data-remote="false" data-toggle="modal" data-target="#eSignModal">
                                                        <%--<a href="javascript:openESignWindow('${billNo}')">--%>
                                                        <input type="button" id="id_esign_button" value="eMudra Login" class="btn btn-danger"/>
                                                    </a>
                                                </c:if>
                                                <c:if test="${typeofbill ne 'PAY'}">
                                                    <a href="eSignArrearWDList.htm?billNo=${billNo}&slno=${slno}" data-remote="false" data-toggle="modal" data-target="#eSignModal">
                                                        <%--<a href="javascript:openESignWindow('${billNo}')">--%>
                                                        <input type="button" id="id_esign_button" value="eMudra Login" class="btn btn-danger"/>
                                                    </a>
                                                </c:if>
                                                <a href="javascript:void();" data-remote="false" data-toggle="modal" data-target="#eSignUploadModal" class="btn btn-success">E-Sign PDF Upload</a>
                                                <%--<div style="font-weight: bold;font-size: 16px; color: green;">E-SIGN is under maintenance. Please try after some time.</div>--%>
                                            </span>
                                            <span id="loader_blk" style="color:#00488E;font-style:italic;text-align:center;display:none;font-size:15pt;"><img src="images/square_loader.gif" /><br />Processing<br />Please Wait...</span>                                    
                                            </c:if>
                                    </div>
                                </td>
                                <td>&nbsp;</td>
                                <td>&nbsp;</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <div id="eSignModal" class="modal fade" role="dialog">
            <div class="modal-dialog" style="width:1000px;">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">E-SIGNATURE</h4>
                    </div>
                    <div class="modal-body"></div>
                    <div class="modal-footer">                       
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
        <div id="eSignUploadModal" class="modal fade" role="dialog">
            <input type="hidden" id="hidbillno" value="${billNo}"/>
            <div class="modal-dialog" style="width:1000px;">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">E-SIGNATURE</h4>
                    </div>
                    <div class="modal-body">
                        <label for="esignfile">Please Upload Signed PDF File from EMudra Login.</label>
                        <input type="file" name="esignfile" id="esignfile"/>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-success" onclick="uploadEsignPDF();">Upload</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
