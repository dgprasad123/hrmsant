<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript" src="js/datagrid-detailview.js"></script>
        <script type="text/javascript" src="js/webcam.js"></script>
        <script type="text/javascript"  src="js/jquery.colorbox-min.js"></script>
        <script type="text/javascript">
            function saveNOC()
            {
                 var fup = document.getElementById('vNocFileName');
                var fileName = fup.value;
                var ext = fileName.substring(fileName.lastIndexOf('.') + 1);
                var ext = ext.toLowerCase();
                
                if ((ext != "pdf" && ext != "zip")) {
                    alert("Upload pdf/zip files only");
                    fup.focus();
                    return false;
                }
                  var fi = document.getElementById("file_att");
                    var fsize = fi.files.item(0).size;
                    var file = Math.round((fsize / 1024));
                    if (file >= 2048) {
                        alert("File too Big, please select a file less than 2mb");
                        $("#file_att" ).val('');
                        return false;
                    }
                }
        </script>    

    </head>

    <c:choose>
        <c:when test = "${fn:contains(LoginUserBean.loginuserid, 'alf')}">
            <body style="margin-top:0px;background:#188B7A;">
                <jsp:include page="../tab/AlfaMenu.jsp"/>  
                <div id="wrapper"> 
                    <div id="page-wrapper" style="margin-top:145px;z-index:0;">
                    </c:when>
                    <c:when test = "${fn:contains(LoginUserBean.loginusername, 'paradmin')}">
                        <body style="margin-top:0px;background:#188B7A;">
                            <jsp:include page="../tab/ParMenu.jsp"/>  
                            <div id="wrapper"> 
                                <div id="page-wrapper" style="margin-top:145px;z-index:0;">
                                </c:when>
                                <c:otherwise>
                                    <body>
                                        <div id="wrapper">
                                            <jsp:include page="../tab/hrmsadminmenu.jsp"/>
                                            <div id="page-wrapper">
                                            </c:otherwise>
                                        </c:choose>

                                        <div class="container-fluid" style="padding-top: 125px;padding-bottom: 125px;">
                                            <div class="panel panel-default">
                                                <div class="panel-heading">
                                                    <div class="row">
                                                        <div class="col-lg-12">
                                                            <h2 style="color:  #0071c5;" align="center"> Crime Branch NOC Upload Panel</h2>
                                                        </div>
                                                    </div>

                                                </div>
                                                <div class="panel panel-primary">
                                                    <div class="panel-heading">Employee Details</div>
                                                    <div class="panel-body">
                                                        <c:forEach items="${pensionList}" var="pnoc" varStatus="count">
                                                           <form action="saveCBNOC"  method="POST"  onsubmit="return saveNOC()" enctype="multipart/form-data">
                                                                <input type="hidden" name="nocId" value=" ${pnoc.nocId}"/>
                                                                <input type="hidden" name="hrmsid" value=" ${pnoc.hrmsid}"/>
                                                                <div class="form-group row">
                                                                    <label for="Name:" class="col-sm-2 col-form-label">Name:</label>
                                                                    <div class="col-sm-10">
                                                                        ${pnoc.name}
                                                                    </div>
                                                                </div>
                                                                <div class="form-group row">
                                                                    <label for="fa" class="col-sm-2 col-form-label">Father's Name:</label>
                                                                    <div class="col-sm-10">
                                                                        ${pnoc.fatherName}
                                                                    </div>
                                                                </div>
                                                                 <div class="form-group row">
                                                                    <label for="fa" class="col-sm-2 col-form-label">Department Name:</label>
                                                                    <div class="col-sm-10">
                                                                        ${pnoc.departmentName}
                                                                    </div>
                                                                </div> 
                                                                 <div class="form-group row">
                                                                    <label for="fa" class="col-sm-2 col-form-label">Office Name:</label>
                                                                    <div class="col-sm-10">
                                                                        ${pnoc.offname}
                                                                    </div>
                                                                </div>    
                                                                <div class="form-group row">
                                                                    <label for="fa" class="col-sm-2 col-form-label">DDO Code:</label>
                                                                    <div class="col-sm-10">
                                                                        ${pnoc.ddoCode}
                                                                    </div>
                                                                </div>
                                                                <div class="form-group row">
                                                                    <label for="fa" class="col-sm-2 col-form-label">HRMS ID:</label>
                                                                    <div class="col-sm-10">
                                                                        ${pnoc.hrmsid}
                                                                    </div>
                                                                </div>    
                                                                <div class="form-group row">
                                                                    <label for="fa" class="col-sm-2 col-form-label">Post:</label>
                                                                    <div class="col-sm-10">
                                                                        ${pnoc.post}
                                                                    </div>
                                                                </div>     
                                                                   
                                                                <div class="form-group row">
                                                                    <label for="fa" class="col-sm-2 col-form-label">Present Address:</label>
                                                                    <div class="col-sm-10">
                                                                        ${pnoc.address}
                                                                    </div>
                                                                </div> 
                                                                <div class="form-group row">
                                                                    <label for="fa" class="col-sm-2 col-form-label">DOB:</label>
                                                                    <div class="col-sm-10">
                                                                        ${pnoc.dob}
                                                                    </div>
                                                                </div> 
                                                                 <div class="form-group row">
                                                                    <label for="fa" class="col-sm-2 col-form-label">DOS:</label>
                                                                    <div class="col-sm-10">
                                                                        ${pnoc.dos}
                                                                    </div>
                                                                </div>   
                                                                 <div class="form-group row">
                                                                    <label for="fa" class="col-sm-2 col-form-label">Mobile:</label>
                                                                    <div class="col-sm-10">
                                                                        ${pnoc.mobile}
                                                                    </div>
                                                                </div>
                                                                 <div class="form-group row">
                                                                    <label for="fa" class="col-sm-2 col-form-label">NOC Upload:</label>
                                                                    <div class="col-sm-10">
                                                                        <input type="file" class="form-control-file" id="cNocFileName" name='cNocFileName'>   
                                                                    </div>
                                                                </div> 
                                                                 <button type="submit" class="btn btn-primary mb-2">Submit</button>   
                                                            </form>
                                                        </c:forEach>             

                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                            </body>
                            </html>
