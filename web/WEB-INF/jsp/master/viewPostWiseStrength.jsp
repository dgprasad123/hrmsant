<%-- 
    Document   : viewPostWiseStrength
    Created on : Sep 23, 2022, 4:18:11 PM
    Author     : Madhusmita
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <link rel="stylesheet" href="css/chosen.css">

        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script src="js/chosen.jquery.min.js"></script>
        <title>:: HRMS, Government of Odisha ::</title>
        <script type="text/javascript">

            $(document).ready(function() {
                $("input[type=checkbox][name=chkPost]").click(function() {
                    var checkStatus = this.checked;
                    if (checkStatus == true) {
                        $("#btnUpdatePost").removeAttr("disabled");

                    } else {
                        $("#btnUpdatePost").attr("disabled", "disabled");
                    }
                })

            });

            function updatePostInSpn(duplicatePost, finalPost, finalPostNm) {
                //alert(finalPostNm);
                var checkBoxArray = "";
                $("input[name='chkPost']:checked").each(function() {
                    if (checkBoxArray == "") {
                        checkBoxArray = $(this).val();
                    } else {
                        checkBoxArray = checkBoxArray + "," + $(this).val();
                        cnt++;
                    }
                });
                if (confirm(checkBoxArray + " Updated to Final Post Named as: " + finalPostNm) )
                {
                    var url = 'updatePostName.htm?dupPostCode=' + duplicatePost + "&finPost=" + finalPost + "&postList=" + checkBoxArray +"&finalPostNm="+finalPostNm;
                    $.getJSON(url, function(data) {

                        $("#postList").hide();
                        $("#btnUpdatePost").hide();
                        $("#empHeading").append("Substantive Post Updated");


                    });
                }
            }
             function confirmValidateToUpdate(dupPostCode,finPostCode) {
                if (confirm("Are you Sure to Merge Duplicate Post with Final Post ?")) {
                    var url = "getMergedPost.htm?dupPost=" + dupPostCode + "&finPost=" + finPostCode;
                    $('#btnUpdate').hide();
                    self.location = url;
                }
                return false;
            }
        </script>

        <style>
            .empheading
            {
                font-weight: bold;
                font-size: 25px;
                text-align: center;
            }

        </style>
    </head>
    <body>
        <form:form action="mergePosts.htm" commandName="post" method="post" >
            <%--<form:hidden path="hidDupPostCode" value="${postList.dupPostCode}"/>
            <form:hidden path="hidFinPostCode" path="${postList.finPostCode}"/>--%>


            <form:hidden path="hidDupPostCode" id="hidDupPostCode"/>
            <form:hidden path="hidFinPostCode" id="hidFinPostCode"/>
            <div id="showPost">
                <table class="table" border="2"  cellspacing="10"  style="font-size:12px; font-family:verdana;" id="tabl1">                    

                    <tr><td style="text-align:left; font-size: 12px;padding-left:20px" >
                            <label style="font-size: large">Duplicate Post: </label>
                        </td>
                        <td style="padding-right:80px;color: red;font-size: medium;">
                            <c:out value='${duplicatePostNm}'/>
                        </td>
                    </tr>
                    <tr>
                        <td style="text-align:left; font-size: 12px;padding-left:20px" >
                            <label style="font-size: large">Final Post: </label>
                        </td>
                        <td style="padding-right:80px;color: green;font-size: medium;">
                            <c:out value='${finalPostNm}'/> 
                        </td>
                    </tr>
                </table> 
            </div>
            <div id="postList">
                <table class="table" border="0"  cellspacing="0"  style="font-size:12px; font-family:verdana;">
                    <thead>
                        <tr>
                            <th>Sl No</th>
                            <th>Post Name</th>
                            <th>Total No. of Post</th>
                        </tr>
                    </thead>
                    <tbody id="totalPostList">

                        <c:if test="${not empty totalPostList}">
                            <c:forEach items="${totalPostList}" var="postList">
                                <tr>
                                    <td style="padding-right:5px">
                                        <input type="checkbox" name="chkPost" value="${postList.post}">
                                    </td>
                                    <td>${postList.post}</td>
                                    <td>${postList.totSpc}</td>
                                </tr>
                            </c:forEach>
                        </c:if>        
                        <c:if test="${empty totalPostList}">
                            <tr>
                                <td colspan="3" style="color:red;font-size: 20px;text-align: center;"><h3>DATA NOT AVAILABLE</h3></td>
                            </tr>
                        </c:if>            
                    </tbody>
                </table>
            </div>
            <div align="center" style="color:green;"><h4 id="empHeading" class="empheading"><B><U></U></B></h4></div>
            <br/><br/>
            <div class="row" align="center">
                <%--<a href="javascript:getPostUpdated('<c:out value="${postList.dupPostCode}"/>','<c:out value="${postList.finPostCode}"/>')">
                    <button type="button"  id="btnUpdate" style="width:100px" class="btn btn-primary">Update</button></a>--%>
                <%--<input type="submit" id="btnUpdate" name="submit" value="Update" class="btn btn-primary" style="width:100px"/>--%>
                <button type="button"  id="btnMerge" style="width:200px; font-weight: bold;" class="btn btn-md btn-success" 
                        onclick="return confirmValidateToUpdate('<c:out value="${duplicatePost}"/>','<c:out value="${finalPost}"/>');">Merge Duplicates</button>

            </div>
            <br/>


           <div class="row" align="center" id="divUpdate">
                <a href="javascript:updatePostInSpn('<c:out value="${duplicatePost}"/>','<c:out value="${finalPost}"/>','<c:out value="${finalPostNm}"/>');" >
                    <button type="button" style="width:200px" id="btnUpdatePost" disabled="true" class="btn btn-primary">Update SPN</button> </a>  
            </div>
                    <br/>
                    
                    <p style="text-align: center"><span style="color:red; font-size: medium">N:B. Select the check boxes to Update final post against above post name</span></p> 

        </form:form>
    </body>
</html>

