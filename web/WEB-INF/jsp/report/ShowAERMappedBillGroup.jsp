<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        

        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script type="text/javascript">
            function deleteMappedBillGroupFromAER(billgroupid) {
                //alert("aerid is: " + $('#hidaerid').val());
                //alert("billgroupid is: " + billgroupid);
                if (confirm("Are you sure to Delete?")) {
                    window.location.href = "deleteAERBillGroupList.htm?aerId=" + $('#hidaerid').val() + "&billgroupid=" + billgroupid;
                }
            }
        </script>
    </head>
    <body>
        <input type="hidden" id="hidaerid" value="${aerId}"/>
        <div class="container" style="width:100%; height:450px; overflow: auto;">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <div class="row">
                        <div class="col-lg-12">
                            Mapped Bill Group List
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th width="15%">Bill Group Name</th>
                                <th width="20%">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:if test="${not empty mappedbillgrouplist}">
                                <c:forEach var="list" items="${mappedbillgrouplist}">
                                    <tr>
                                        <td>
                                            <c:out value="${list.label}"/>
                                        </td>
                                        <td>
                                            <a href="javascript:void(0);" onclick="deleteMappedBillGroupFromAER('<c:out value="${list.value}"/>');">Delete</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <%--<c:if test="${not empty mappedbillgrouplist}">
            <c:forEach var="list" items="${mappedbillgrouplist}">
                <div class="row">
                    <div class="col-lg-5 col-xs-12 col-centered">
                        <c:out value="${list.label}"/>
                    </div>
                    <div class="col-lg-7 col-xs-12">
                        <a href="javascript:void(0);" onclick="deleteMappedBillGroupFromAER('<c:out value="${list.value}"/>');">Delete</a>
                    </div>
                </div>
                <hr />
            </c:forEach>
        </c:if>--%>

    </body>
</html>
