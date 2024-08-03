<%-- 
    Document   : AvailablePostListForSectionList
    Created on : 11 Feb, 2021, 11:11:19 AM
    Author     : Surendra
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="container-fluid">
    <div class="panel panel-default">
        <div class="panel-heading">
            <div class="row">
                <div class="col-lg-12">
                    <input type="hidden" id="unmapspcid" value="${unmapSpcCode}"/>
                </div>
            </div>
        </div>
        <div class="panel-body">
            <table class="table table-bordered">
                <thead>
                    <tr>
                        <th width="5%">Select</th>
                        <th width="5%">Sl No</th>
                        <th width="30%">Section Name</th>
                        <th width="10%">Section Type</th>
                        <th width="30%">Bill Name</th>
                        <th width="10%">Bill Type</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${sectionList}" var="section" varStatus="cnt">
                        <tr>
                            <td> <input type="radio" id="sectionMapId${cnt.index+1}" name="sectionMapId" value="${section.sectionId}"/> </td>
                            <td>${cnt.index+1}</td>
                            <td>${section.section}</td>
                            <td>${section.radoption}</td>
                            <td>${section.billgroup}</td>
                            <td>
                                <c:if test="${section.billType eq 'CONT6_REG'}">
                                    CONTRACTUAL SIX YEARS
                                </c:if>
                                <c:if test="${section.billType ne 'CONT6_REG'}">
                                    ${section.billType}
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <div class="panel-footer">                    

        </div>
    </div>
</div>
