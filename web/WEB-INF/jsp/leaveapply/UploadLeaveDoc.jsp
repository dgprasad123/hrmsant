<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 

<%
String contextPath=request.getContextPath();
    String basePath= request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
%>
    
<html>
  <head>
         <base href="${initParam['BaseURLPath']}" />  </base
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>UploadLeaveDoc</title>
      <link href="css/uploadfilestyle.css" rel="stylesheet" type="text/css" />
       <script language="javascript" src="js/jquery.min-1.9.1.js" type="text/javascript"></script>
       <script language="javascript" src="js/jquery.knob.js" type="text/javascript"></script>
       <script language="javascript" src="js/jquery.ui.widget.js" type="text/javascript"></script>
       <script language="javascript" src="js/jquery.iframe-transport.js" type="text/javascript"></script>
       <script language="javascript" src="js/jquery.fileupload.js" type="text/javascript"></script>
       <script language="javascript" src="js/script.js" type="text/javascript"></script>
        
  </head>
   <form:form  action="UploadDocumentAction.htm" commandName="uploadedFile"  enctype="multipart/form-data">
   
  <body id="upload">
        <div id="drop" align="center">
            DROP HERE <br>
            <a>Browse </a>
           <input type="file" name="uploadAttachmentFileList[0]"    multiple  class='callbacks'/>
        </div>
        <ul>
        <!-- The file uploads will be shown here -->
        </ul>
     
  </body>
  </form:form>
  
</html>