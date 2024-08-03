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
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>>
        <script language="javascript" type="text/javascript">
            function checkUpload() {
                //alert($('#pphoto').val());
                if ($('#pphoto').val() != '') {
                    var ext = $('#pphoto').val().split('.').pop().toLowerCase();
                    var filesize = $("#pphoto")[0].files[0].size;
                    if ($.inArray(ext, ['jpg', 'jpeg', 'png']) == -1) {
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
        
        <body>
        <div class="container" style="margin-top:100px">
            <div class="panel-group">
                <div class="panel panel-success">
                    <div class="panel-heading"><strong>Upload HRMS Profile Photo</strong></div>
                    <div class="panel-body">
                        <form:form method="post" enctype="multipart/form-data"   modelAttribute="uploadedFile" action="fileUpload.htm">  
                            <div class="form-group">                              
                                <input type="file" class="form-control"  name="file" id="pphoto" /><br/>
                                <i style='color:red'>Image(JPEG/PNG) Size should not exceed 4 MB!</i>
                            </div>                           
                           
                            <input type="submit" value="Upload" onclick="return checkUpload()" class="btn btn-info"/>

                        </form:form>
                    </div>
                </div>
            </div> 
        </div>    

    </body>    
    
</html>
