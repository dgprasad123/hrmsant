<%-- 
    Document   : Home
    Created on : 5 Dec, 2016, 12:27:03 PM
    Author     : Manoj PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Training Institutes:: HRMS</title>
        <link href="https://fonts.googleapis.com/css?family=Cabin" rel="stylesheet">
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <style type="text/css">
            body{margin:0px;background:#EAEAEA;font-family:'Cabin', 'Sans Serif'}
            #menu_container{background:#0D395D;margin:0px auto;height:40px;}
            #menu_wrap{width:1000px;margin:0px auto;}
            #menu_wrap ul{margin:0px;padding:0px;list-style-type:none;}
            #menu_wrap ul li a{float:left;padding:0px 15px;line-height:40px;color:#FFFFFF;text-decoration:none;}
            #menu_wrap ul li a:hover{color:#a3a183}
            .institute{border:1px solid #DADADA;padding:7px;margin-bottom:10px;border-radius:5px;}
            .institute h2{font-size:14pt;color:#0173c9;margin:0px;}
        </style>
        <script type="text/javascript">
            	$.ajax({
	  url: 'TrainingInstitutesList.htm',
	  type: 'get',
	  success: function(retVal) {
              $('#institute_blk').html(retVal);
	  }
	});
            </script>
    </head>
    <body>
                        <jsp:include page="HeaderFront.jsp">
            <jsp:param name="menuHighlight" value="CALENDAR" />
        </jsp:include>
            <h1 style="margin:0px;font-size:18pt;margin-bottom:8px;">Training Institutes</h1>
            <div id="institute_blk"></div>
                        <jsp:include page="FooterFront.jsp">
            <jsp:param name="menuHighlight" value="CALENDAR" />
        </jsp:include>
    </body>
</html>
