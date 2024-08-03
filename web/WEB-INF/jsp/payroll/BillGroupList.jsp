<%-- 
    Document   : SectionDefination
    Created on : Nov 21, 2016, 3:12:08 PM
    Author     : Manas Jena
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>      
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type='text/javascript'>
            function delete_group(ids) {
                var con = confirm("Do you want to delete this Group information");
                if (con) {
                    window.location = "deleteGroupData.htm?groupId=" + ids;

                }
            }

            function excludeFromAquitance(billgroupid, status) {
                //alert("Inside excludeFromAquitance and billgroupid is: "+billgroupid);
                if (confirm("Are you sure to Hide this Bill Group in Aquitance?")) {
                    self.location = "excludeBillGroupFromAquitance.htm?groupId=" + billgroupid + "&status=" + status;
                }
            }
            function includeToAquitance(billgroupid, status) {
                //alert("Inside excludeFromAquitance and billgroupid is: "+billgroupid);
                if (confirm("Are you sure to Show this Bill Group in Aquitance?")) {
                    self.location = "excludeBillGroupFromAquitance.htm?groupId=" + billgroupid + "&status=" + status;
                }
            }

            function excludeFromTreasury(billgroupid, status) {
                //alert("Inside excludeFromTreasury and billgroupid is: "+billgroupid);
                if (confirm("Are you sure not to submit this Bill Group to Treasury?")) {
                    self.location = "excludeFromTreasury.htm?groupId=" + billgroupid + "&status=" + status;
                }
            }
            function includeToTreasury(billgroupid, status) {
                //alert("Inside excludeFromTreasury and billgroupid is: "+billgroupid);
                if (confirm("Are you sure to submit this Bill Group to Treasury?")) {
                    self.location = "excludeFromTreasury.htm?groupId=" + billgroupid + "&status=" + status;
                }
            }
        </script>
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <div class="row">
                        <div class="col-lg-12">

                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th width="5%">SL NO</th>
                                <th width="40%">DESCRIPTION</th>
                                <th width="20%">CHART OF ACCOUNT</th>
                                <th width="5%">&nbsp;</th>
                                <th width="5%">&nbsp;</th>
                                <th width="5%">&nbsp;</th>
                                <th width="10%">Hide Vacant Post in Aquitance</th>
                                <th width="10%">Do Not Submit to Treasury</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${groupList}" var="group" varStatus="cnt">
                                <tr>
                                    <td>${cnt.index+1}</td>
                                    <td>${group.billgroupdesc}</td>
                                    <td>${group.chartofaccount}</td>

                                    <td><a href="editGroupList.htm?groupId=${group.billgroupid}">Edit</a></td>
                                    <td><a href="configBillGroup.htm?groupId=${group.billgroupid}">Configure</a></td>
                                    <td><a href="maptosection.htm?groupId=${group.billgroupid}">Map</a></td>
                                    <td align="center">
                                        <c:if test="${group.showInAquitance eq 'Y'}">
                                            <a href="javascript:void(0);" onclick="excludeFromAquitance('${group.billgroupid}', 'N')">Hide</a>
                                        </c:if>
                                        <c:if test="${group.showInAquitance eq 'N'}">
                                            <a href="javascript:void(0);" onclick="includeToAquitance('${group.billgroupid}', 'Y')">Show</a>
                                        </c:if>
                                    </td>
                                    <td align="center">
                                        <c:if test="${group.submitToTreasury eq 'Y'}">
                                            <a href="javascript:void(0);" onclick="excludeFromTreasury('${group.billgroupid}', 'N')">Hide</a>
                                        </c:if>
                                        <c:if test="${group.submitToTreasury eq 'N'}">
                                            <a href="javascript:void(0);" onclick="includeToTreasury('${group.billgroupid}', 'Y')">Show</a>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
             
                <c:if test="${isDDO == 'B'}">
                    <div class="panel-footer"> 
                        <button type="button" class="btn btn-default" disabled="true"> New Group</button> 
                    </div>
                </c:if>
                <c:if test="${isDDO != 'B'}">
                    <div class="panel-footer"> 
                        <button type="submit" class="btn btn-default" onclick="javascript: self.location = 'addGroupList.htm'">New Group</button>
                    </div>
                </c:if>

            </div>
        </div>
    </body>
</html>
