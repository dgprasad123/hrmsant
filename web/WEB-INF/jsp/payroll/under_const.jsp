<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
    String contextPath=request.getContextPath();
    String basePath= request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
%>
<html>
<head>
<base href="${initParam['BaseURLPath']}" />  
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>:: HRMS ::</title>
<link rel="shortcut icon" href="images/favicon.ico"/>

<script type="text/javascript" language="javascript">
    $(document).ready(function(){
        $(this).bind("contextmenu", function(e) {
            e.preventDefault();
        });
        function disableBack() {window.history.forward()}

        window.onload = disableBack();
        window.onpageshow = function (evt) {if (evt.persisted) disableBack()}

        var path = 'login.jsp'; //write here name of your page 
        history.pushState(null, null, path + window.location.search);
        window.addEventListener('popstate', function (event) {
            history.pushState(null, null, path + window.location.search);
        });
    });
    
</script> 

</head>
<body>
    <!--Container begins-->
    <div>
        <table align="center" border="0" cellpadding="1" cellspacing="1" width="90%">
            <tr>
                <td height="585" style="text-align: center;">Under Maintenance</td>
            </tr>
        </table>
    </div>
    <!--Container Ends-->
</body>
</html>