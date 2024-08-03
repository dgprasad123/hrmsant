<%-- 
    Document   : PhotoUploadInterface
    Created on : 17 Mar, 2016, 2:43:54 PM
    Author     : Surendra
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<html>
    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <title>Human Resources Management System, Government of Odisha</title>

        <script language="javascript"  src="js/jquery.min-1.9.1.js" type="text/javascript" ></script>
        <script language="javascript" type="text/javascript">
            function checkUpload() {
                //alert($('#pphoto').val());
                if ($('#pphoto').val() != '') {
                    var ext = $('#pphoto').val().split('.').pop().toLowerCase();
                    var filesize = $("#pphoto")[0].files[0].size;
                    if ($.inArray(ext, ['jpg', 'png','jpeg']) == -1) {
                        alert('JPEG or PNG files only!');
                        return false;
                    }
                    if (filesize > 5242880) {
                        alert('Image Size must not exceed 5 MB!');
                        return false;
                    }
                } else {
                    alert("Please select an Image File");
                    return false;
                }
            }
        </script>
    </head>
    <body>
        <form:form method="post" enctype="multipart/form-data"  
                   modelAttribute="uploadedFileSBDdo" action="uploadedFileSBDdo.htm">  
            <div align="center" style="color:#008000;font-size:20px;"> 

            </div>
            <div align="center" style="margin-top:50px;">
                <input type="file" name="file" id="pphoto"/>
                <input type ="submit" value="Upload" onclick="return checkUpload()"/>
            </div>
        </form:form>
    </body>
</html>
