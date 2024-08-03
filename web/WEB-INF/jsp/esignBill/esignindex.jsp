<%-- 
    Document   : esignindex
    Created on : 24 Nov, 2022, 10:34:20 AM
    Author     : lenovo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
          <script type="text/javascript">
              function getResponseFromTokenRequestData(encryptedKeyID, encryptedData) {
                $.ajax({
                    type: "POST",
                    url: "https://localhost.emudhra.com:26769/DSC/ListToken",
                    contentType: 'application/json',
                    data: JSON.stringify({"encryptionKeyID": encryptedKeyID, "encryptedRequest": encryptedData}),
                    success: function(data) {
                        console.log(data);
                        if (data.status === 1) {
                            var responseData = data.responseData;
                         $("#console").append("<div>" + responseData + "</div>");
                            decToken(responseData);
                        } else {
                            alert("Error At ListToken");
                        }
                    }
                });
            }
            function decToken(requestData) {
                $.ajax({
                    type: "post",
                    url: "decListToken.htm",                    
                    data: {"encryptedRequestData": requestData},
                    success: function(responseData) {
                      //  console.log(responseData);
                        
                       // return false;
                        const obj = JSON.parse(responseData);                        
                        $("#console").append("<div>" + obj.encryptionKeyID + "</div>");
                       $("#console").append("<div>" + obj.encryptedData + "</div>");
                      $("#console").append("<div>Displayname:" + obj.displayname + "</div>");
                        getCertificateData(obj);
                    }
                });
            }
            function getCertificateData(decTokenData) {
                //const obj = JSON.parse(decTokenData);
                $.ajax({
                    type: "POST",
                    url: "https://localhost.emudhra.com:26769/DSC/ListCertificate",
                    contentType: 'application/json',
                    data: JSON.stringify({"encryptedRequest": decTokenData.encryptedData, "encryptionKeyId": decTokenData.encryptionKeyID}),
                    success: function(data) {
                        if (data.status == 1) {
                            var encCertificate = data.responseData;
                            decListCertificate(encCertificate,decTokenData.displayname)
                        } else {
                            alert("Error At getCertificate");
                        }
                    }
                });
            }
            function decListCertificate(encCertificate, certdisplayname) {
                $.ajax({
                    type: "POST",
                    //url: "decListCertificate.htm", 
                    url:"singlebillFrontPageSign.htm",
                    data: {"encCertificate": encCertificate, "certdisplayname": certdisplayname,"billNo":"54666574"},
                    success: function(responsedata) {
                        generatePDFajax(responsedata,certdisplayname);
                        $("#console").append("<div>Displayname:" + responsedata + "</div>");                        
                    }
                });
            }
            function generatePDFajax(certificatedata, certdisplayname) {
                const obj = JSON.parse(certificatedata);
                $.ajax({
                    type: "POST",
                    url: "https://localhost.emudhra.com:26769/DSC/PKCSBulkSign",
                    contentType: 'application/json',
                    data: JSON.stringify({"encryptedRequest": obj.encryptedData, "encryptionKeyId": obj.encryptionKeyID}),
                    success: function(response) {
                        responseData = response.responseData;
                        $("#console").append("<div>PKCSBulkSign:" + responseData + "</div>");
                        generatePDFFile(responseData, certdisplayname,obj.tempFilePath);
                    }
                });
            }
            
            function generatePDFFile(responseData, certdisplayname,temppath) {
                $.ajax({
                    type: "post",
                    url: "generatePDFFile.htm",
                    data: {"responseData" : responseData, "certdisplayname" : certdisplayname, "tempPath":temppath},
                    success: function(data) {
                        console.log(data.responsedata);                      
                    }
                });
            }
         </script>     
    </head>
    <body>
        <input type="button" name='button' value="Digital SIgn" class="btn btn-primary btn-lg" onclick="getResponseFromTokenRequestData('${esign.encryptedKeyID}', '${esign.encryptedData}')"/><br/><br/>
        <iframe src="http://localhost:8080/HRMSOpenSource/singlebillFrontPagePDF.htm?billNo=54666574" title="testPdf" height="1000px" width="1000px" />
    </body>
</html>
