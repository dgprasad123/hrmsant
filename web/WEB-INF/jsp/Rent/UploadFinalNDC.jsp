<%-- 
    Document   : ApplyNDC
    Created on : 1 Sep, 2020, 11:04:55 AM
    Author     : Manoj PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Upload Final NDC</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <link rel="stylesheet" href="css/chosen.css">

        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript" src="js/chosen.jquery.min.js"></script>
        <script type="text/javascript">
            function validateForm()
            {
                if($('#ndcFile').val() == '')
                {
                    alert("Please attach NDC File (PDF).");
                    return false;
                }
            }
                    <c:if test="${result == 'success'}">
                          opener.location.reload(); // or opener.location.href = opener.location.href;
                            window.close(); // or self.close();
                    </c:if>
            </script>
    </head>
    <body>
        <form:form action="SaveFinalNDC.htm" method="POST" commandName="QuarterBean" enctype="multipart/form-data" onsubmit="javascript: return validateForm()">
            <div class="container">
                <h2>Upload Final NDC (With DS) for Quarters NDC (GA Pool Quarters)</h2>
                <input type="hidden" name="applicationId" id="applicationId" value="${applicationId}" />
                <div class="panel-group">

                    <div class="panel panel-success">
                        <div class="panel-heading" style="font-weight:bold;font-size:13pt;">NDC Upload:</div>
                        <div class="panel-body">
                            <table class="table" style="font-size:12pt;">
                                                                <tr>
                                    <td colspan="2">
                                            <div class="row" id="has_cleared_outstanding_blk">
                                                <div class="col-lg-4" style="text-align:right;"><strong>Attach Final NDC (PDF file):</strong></div>
                                                <div class="col-lg-6"><input type="file" class="form-control" name="ndcFile" id="ndcFile" /></div>
                                            </div>                                            
                                    </td>
                                </tr>  
                                <tr>
                                    <td colspan="2" style="font-weight:bold;" align="center"><input type="submit" value="Submit" id="btnSubmit" class="btn btn-success" />
                                        <input type="button" value="Close" class="btn btn-danger" onclick="javascript:window.close();" /></td>
                                </tr>  
                            </table>     

                        </div>
                    </div>

                                    </div>
            </div>

        </form:form>

    </body>
</html>
