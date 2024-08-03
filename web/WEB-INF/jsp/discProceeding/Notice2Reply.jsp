<%-- 
    Document   : Notice2Reply
    Created on : Oct 22, 2018, 5:04:34 PM
    Author     : manisha
--%>



<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:HRMS:</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script src="js/bootstrap-datetimepicker.js" type="text/javascript"></script>

        <style type="text/css">
            .headr{
                font-weight: bold;
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 16px;
            }
        </style>

        <script language="javascript" type="text/javascript">
            $(document).ready(function() {
                $('#memoDate').datetimepicker({
                    format: 'D-MMM-YYYY'
                });
            });
            function saveMemo() {
                var memoNo = $('#rule15MemoNo').val();
                if (memoNo == '') {
                    alert("Please enter Memorandum No");
                    return false;
                }
                if (memoNo.length >= 18) {
                    alert("Please Enter a Memorandum No between 1 and 18");
                    return false;
                }
                var ordrDate = $('#rule15MemoDate').datebox('getValue');// will get the date value
                if (ordrDate == '') {
                    alert("Please enter Date");
                    return false;
                }
                var charge = $('#annex1Charge').val();
                if (charge.length >= 495) {
                    alert("Please Enter value between 0 and 495");
                    return false;
                }

            }
            function openAnnexture1() {
                location.href = "";
            }
        </script>
    </head>
    <body style="padding:0px;">        
        <form:form action="viewNotice2Reply.htm" method="POST" commandName="noticeBean" class="form-inline">
           
            <div class="panel-footer">
                
                        
                      <div class="panel panel-default">                            
                        <div class="panel-heading" align="center">   Remark of Delinquent Officer:<br></div>
                        <div class="panel-body"> 
                             <form:hidden path="daId"/>
                    <form:hidden path="taskId"/>
                    
                            <table width="100%" border="0" style="font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 13px;">
                                
                                    <tr>
                                        <td>  
                                            <c:forEach items="${noticeBeanList}" var="noticeBean" >
                                                 ${noticeBean.remark} <br><br>                                   
                                            </c:forEach>
                                        </td>
                                    </tr>

                                </table>
                            </div>  
                        </div>
                       


                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
