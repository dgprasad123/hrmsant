<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <title>HRMS</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h2 align="center"><c:out value="${officename}"/></h2>
                    <h4 align="center">Financial Year:${financialYear}</h4>
                    <h6 align="center">SCHEDULE II-A</h6>
                    <div align="center">(Relating to Head of the Department)</div>
                  
                    <c:if test="${type eq '2'}">
                          <a href="PostTerminationCOScheduleIIBack.htm?financialYear=${financialYear}"><button type="submit" name="btnPTAer" value="Back" class="btn btn-primary">Back</button></a>
                    </c:if>
                          
                    <c:if test="${type eq '1'}">
                          <a href="AOViewPostTerminationList.htm?fy=${financialYear}"><button type="submit" name="btnPTAer" value="Back" class="btn btn-primary">Back</button></a>
                    </c:if>      
                  
                </div>
                <div class="panel-body">
                    <table class="table table-bordered" width="100%">
                        <thead>
                            <tr>
                                <th width="5%">SL No</th>
                                <th width="30%">Description of Posts</th>
                                <th width="15%">Pay Scale</th>
                                <th width="5%">GO No.(if available)</th>
                                <th width="10%">GO Date in which sanctioned(if available)</th>
                                <th width="5%">No. of Posts to be terminated</th>
                                <th width="10%">Date from which posts(s) to be terminated</th>
                                <th width="30%">Remarks</th>
                            </tr>
                        </thead>
                        <tbody>
                             <c:set var="GtotalPost" value="${0}" />
                            <c:forEach items="${viewlist}" var="list" varStatus="count">
                                 <c:set var="GtotalPost" value="${GtotalPost+list.noOfPost}" />
                                <tr>
                                    <td>
                                        ${count.index + 1}
                                    </td>
                                    <td>
                                        <c:out value="${list.postname}"/>
                                    </td>
                                    <td> 
                                        <c:out value="${list.payscale}"/>
                                    </td>
                                    <td>
                                        <c:out value="${list.goNumber}"/>
                                    </td>
                                    <td>
                                        <c:out value="${list.goDate}"/>
                                    </td>
                                    <td>
                                        <c:out value="${list.noOfPost}"/>
                                    </td>
                                    <td>
                                        <c:out value="${list.terminatedDate}"/>
                                    </td>    
                                    <td>
                                        <c:out value="${list.postRemarks}"/>
                                    </td>    
                                </tr>
                                   
                            </c:forEach>
                            <tr>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>    
                                    <td>&nbsp;</td>    
                                    <td>&nbsp;</td>    
                                    <td><strong>Total Post Terminated</strong></td>   
                                    <td><strong style="color:green">${GtotalPost}</strong></td>    
                                    <td>&nbsp;</td>    
                                    <td>&nbsp;</td>    
                                </tr>      
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer"></div>
            </div>
        </div>
    </body>
</html>
