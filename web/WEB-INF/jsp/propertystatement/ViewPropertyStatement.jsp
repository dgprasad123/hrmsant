<%-- 
    Document   : ViewPropertyStatement
    Created on : Apr 12, 2017, 2:44:36 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>::Property Statement::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">  
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
    </head>
    <body>
        <div id="wrapper">
            <div id="page-wrapper">
                <div class="container">
                    <!-- Page Heading -->
                    <div class="row" align="center"><h1>PROPERTY STATEMENT FOR STATE GOVERNMENT EMPLOYEES</h1></div>
                    <div class="row" align="center"><h1>FORM</h1></div>
                    <div class="row">
                        <div class="col-sm-4" align="right">
                            <h3>Name (in full) of Officer : </h3>
                        </div>
                        <div class="col-sm-8" align="left">
                            <h3><u>${propertyStatement.fullname}</u></h3>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-4" align="right">
                            <h3>Designation : </h3>
                        </div>
                        <div class="col-sm-8" align="left">
                            <h3><u>${propertyStatement.spn}</u></h3>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-4" align="right">
                            <h3>Financial Year : </h3>
                        </div>
                        <div class="col-sm-8" align="left">
                            <h3><u>${propertyStatement.fiscalyear}</u></h3>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-4" align="right">
                            <h3>From Date : </h3>
                        </div>
                        <div class="col-sm-2" align="left">
                            <h3><u>${propertyStatement.fromdate}</u></h3>
                        </div>
                        <div class="col-sm-4" align="right">
                            <h3>To Date : </h3>
                        </div>
                        <div class="col-sm-2" align="left">
                            <h3><u>${propertyStatement.todate}</u></h3>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-4" align="right">
                            <h3>Pay Scale : </h3>
                        </div>
                        <div class="col-sm-8" align="left">
                            <h3><u>${propertyStatement.payscale}</u></h3>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-4" align="right">
                            <h3>Basic Pay : </h3>
                        </div>
                        <div class="col-sm-8" align="left">
                            <h3><u>${propertyStatement.curbasicsalary}</u></h3>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-4" align="right">
                            <h3>Grade Pay : </h3>
                        </div>
                        <div class="col-sm-8" align="left">
                            <h3><u>${propertyStatement.gradepay}</u></h3>
                        </div>
                    </div>
                    <div class="row" align="center"><h1>A. IMMOVABLE PROPERTY</h1></div>
                    <div class="row" align="center"><div class="col-sm-8" align="left"><h3>(1) LANDS</h3></div></div>
                    <div class="row">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th>Sl. No.</th>
                                    <th>Precise location</th>
                                    <th>Area</th>
                                    <th>Nature of land</th>
                                    <th>Extent of interest</th>
                                    <th>Value(INR)</th>
                                    <th>In whose name (self, wife, child, dependant, other relation or benamidar) the asset is or was</th>
                                    <th>Date and manner of acquisition or disposal</th>
                                    <th>Remarks</th>
                                </tr>
                                <tr>
                                    <th>(1)</th>
                                    <th>(2)</th>
                                    <th>(3)</th>
                                    <th>(4)</th>
                                    <th>(5)</th>
                                    <th>(6)</th>
                                    <th>(7)</th>
                                    <th>(8)</th>
                                    <th>(9)</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:set var="theCountIm" value="0"/>
                                <c:set var="isLandsExist" value="false"/>
                                <c:set var="isHouseExist" value="false"/>
                                <c:set var="isOtherImmovableExist" value="false"/>

                                <c:set var="isOtherMovableExist" value="false"/>
                                <c:set var="isAllMovableExist" value="false"/>

                                <c:forEach items="${immovablePropertyDetailList}" var="immovablePropertyDetail">
                                    <c:if test="${immovablePropertyDetail.propertyId == 7 and immovablePropertyDetail.propertyValue ne '' and not empty immovablePropertyDetail.propertyValue}">
                                        <c:set var="theCountIm" value="${theCountIm + 1}" scope="page"/> 
                                        <c:set var="isLandsExist" value="true"/>
                                        <tr>
                                            <td>${theCountIm}</td>
                                            <td>${immovablePropertyDetail.propertyLocation}</td>
                                            <td>${immovablePropertyDetail.propertyArea} ${immovablePropertyDetail.areaunit}</td>
                                            <td>${immovablePropertyDetail.propertyNature}</td>
                                            <td>${immovablePropertyDetail.interest}</td>
                                            <td>${immovablePropertyDetail.propertyValue}</td>
                                            <td>${immovablePropertyDetail.propertyOwnerDtl} <c:if test="${immovablePropertyDetail.propertyOwner == 5}">(${immovablePropertyDetail.otherPropertyOwner}) </c:if></td>
                                            <td><fmt:formatDate pattern = "dd-MM-yyyy" value = "${immovablePropertyDetail.dateOfAcq}" /> <br/> ${immovablePropertyDetail.manner}</td>
                                            <td>${immovablePropertyDetail.remark}</td>
                                        </tr>
                                    </c:if>
                                </c:forEach>
                                <c:if test="${isLandsExist eq 'false'}">
                               <td colspan="9" style="height: 25px;width: 100%;text-align: center">
                                    <span style="font-size:20pt;">
                                        <b>Nil Property Submitted</b>
                                    </span>
                                </td>
                            </c:if>
                            </tbody>
                        </table>
                    </div>
                    <div class="row" align="center"><div class="col-sm-8" align="left"><h3>(2) HOUSES</h3></div></div>
                    <div class="row">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th>Sl. No.</th>
                                    <th>Precise location</th>                                    
                                    <th>Extent of interest</th>
                                    <th>Value(INR)</th>
                                    <th>In whose name (self, wife, child, dependant, other relation or benamidar) the asset is or was</th>
                                    <th>Date and manner of acquisition or disposal</th>
                                    <th>Remarks</th>
                                </tr>
                                <tr>
                                    <th>(1)</th>
                                    <th>(2)</th>
                                    <th>(3)</th>
                                    <th>(4)</th>
                                    <th>(5)</th>
                                    <th>(6)</th>
                                    <th>(7)</th>                                    
                                </tr>
                            </thead>
                            <tbody>
                                <c:set var="theCountIm" value="0" scope="page" />
                                <c:forEach items="${immovablePropertyDetailList}" var="immovablePropertyDetail">
                                    <c:if test="${immovablePropertyDetail.propertyId == 8 and immovablePropertyDetail.propertyValue ne '' and not empty immovablePropertyDetail.propertyValue}">
                                        <c:set var="theCountIm" value="${theCountIm + 1}" scope="page"/> 
                                        <c:set var="isHouseExist" value="true"/>
                                        <tr>
                                            <td>${theCountIm}</td>
                                            <td>${immovablePropertyDetail.propertyLocation}</td>                                        
                                            <td>${immovablePropertyDetail.interest}</td>
                                            <td>${immovablePropertyDetail.propertyValue}</td>
                                            <td>${immovablePropertyDetail.propertyOwnerDtl} <c:if test="${immovablePropertyDetail.propertyOwner == 5}">(${immovablePropertyDetail.otherPropertyOwner}) </c:if></td>
                                            <td><fmt:formatDate pattern = "dd-MM-yyyy" value = "${immovablePropertyDetail.dateOfAcq}" /><br/> ${immovablePropertyDetail.manner}</td>
                                            <td>${immovablePropertyDetail.remark}</td>
                                        </tr>
                                    </c:if>
                                </c:forEach>
                                <c:if test="${isHouseExist eq 'false'}">
                                <td colspan="9" style="height: 25px;width: 100%;text-align: center">
                                    <span style="font-size:20pt;">
                                        <b>Nil Property Submitted</b>
                                    </span>
                                </td>
                            </c:if>
                            </tbody>
                        </table>
                    </div>
                    <div class="row" align="center"><div class="col-sm-12" align="left"><h3>(3) Immovable properties of other description (including mortgages and such other rights)</h3></div></div>
                    <div class="row">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th>Sl. No.</th>
                                    <th>Brief description</th>                                    
                                    <th>Extent of interest</th>
                                    <th>Value(INR)</th>
                                    <th>In whose name (self, wife, child, dependant, other relation or benamidar) the asset is or was</th>
                                    <th>Date and manner of acquisition or disposal</th>
                                    <th>Remarks</th>
                                </tr>
                                <tr>
                                    <th>(1)</th>
                                    <th>(2)</th>
                                    <th>(3)</th>
                                    <th>(4)</th>
                                    <th>(5)</th>
                                    <th>(6)</th>
                                    <th>(7)</th>                                    
                                </tr>
                            </thead>
                            <tbody>
                                <c:set var="theCountIm" value="0" scope="page" />
                                <c:forEach items="${immovablePropertyDetailList}" var="immovablePropertyDetail">
                                    <c:if test="${immovablePropertyDetail.propertyId == 9}">
                                        <c:set var="theCountIm" value="${theCountIm + 1}" scope="page"/>
                                        <c:set var="isOtherImmovableExist" value="true"/>
                                        <tr>
                                            <td>${theCountIm}</td>
                                            <td>${immovablePropertyDetail.propertyName} (${immovablePropertyDetail.descofothitem})</td>                                        
                                            <td>${immovablePropertyDetail.interest}</td>
                                            <td>${immovablePropertyDetail.propertyValue}</td>
                                            <td>${immovablePropertyDetail.propertyOwnerDtl} <c:if test="${immovablePropertyDetail.propertyOwner == 5}">(${immovablePropertyDetail.otherPropertyOwner}) </c:if></td>
                                            <td><fmt:formatDate pattern = "dd-MM-yyyy" value = "${immovablePropertyDetail.dateOfAcq}" /><br/> ${immovablePropertyDetail.manner}</td>
                                            <td>${immovablePropertyDetail.remark}</td>
                                        </tr>
                                    </c:if>
                                </c:forEach>
                                <c:if test="${isOtherImmovableExist eq 'false'}">
                                <td colspan="9" style="height: 25px;width: 100%;text-align: center">
                                    <span style="font-size:20pt;">
                                        <b>Nil Property Submitted</b>
                                    </span>
                                </td>
                            </c:if>
                            </tbody>
                        </table>
                    </div>
                    <div class="row" align="center"><h1>B. MOVABLE PROPERTY</h1></div>
                    <div class="row" align="center"><div class="col-sm-12" align="left"><h3>(1) Cash, Bank balance, Credit, Insurance policies, shares, Debentures, etc.</h3></div></div>
                    <div class="row">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th>Sl. No.</th>
                                    <th>Description of items</th>                                                                        
                                    <th>Value(INR)</th>
                                    <th>In whose name (self, wife, child, dependant, other relation or benamidar) the asset is or was</th>                                    
                                    <th>Date and manner of acquisition or disposal</th>
                                    <th>Loans that may have been given to others</th>
                                    <th>Remarks</th>
                                </tr>
                                <tr>
                                    <th>(1)</th>
                                    <th>(2)</th>
                                    <th>(3)</th>
                                    <th>(4)</th>
                                    <th>(5)</th>
                                    <th>(6)</th>
                                    <th>(7)</th>                                    
                                </tr>
                            </thead>
                            <tbody>
                                <c:set var="theCountIm" value="0" scope="page" />
                                <c:forEach items="${movablePropertyDetailList}" var="movablePropertyDetail" varStatus="theCount">
                                    <c:if test="${movablePropertyDetail.propertyId != 10 and movablePropertyDetail.propertyValue ne '' and not empty movablePropertyDetail.propertyValue}">
                                        <c:set var="theCountIm" value="${theCountIm + 1}" scope="page"/> 
                                        <c:set var="isAllMovableExist" value="true"/> 
                                        <tr>
                                            <td>${theCountIm}</td>
                                            <td>${movablePropertyDetail.propertyName}</br>${movablePropertyDetail.descofothitem}</td>                                        
                                            <td>${movablePropertyDetail.propertyValue}</td>                                        
                                            <td>${movablePropertyDetail.propertyOwnerDtl} <c:if test="${movablePropertyDetail.propertyOwner == 5}">(${movablePropertyDetail.otherPropertyOwner}) </c:if></td>
                                            <td> <fmt:formatDate pattern = "dd-MM-yyyy" value = "${movablePropertyDetail.dateOfAcq}" /> <br/> ${movablePropertyDetail.manner}</td>
                                            <td>${movablePropertyDetail.loan}</td>
                                            <td>${movablePropertyDetail.remark}</td>
                                        </tr>
                                    </c:if>
                                </c:forEach>
                                <c:if test="${isAllMovableExist eq 'false'}">
                                <td colspan="9" style="height: 25px;width: 100%;text-align: center">
                                    <span style="font-size:20pt;">
                                        <b>Nil Property Submitted</b>
                                    </span>
                                </td>
                            </c:if>
                            </tbody>
                        </table>
                    </div>
                    <div class="row" align="center"><div class="col-sm-12" align="left"><h3>(2) Other movable (including jewellery and other valuable, motor vehicles, refrigerators and other articles or materials of value exceeding  two months basic pay for each item (Both Gazetted Officers and Non-gazetted Officers).</h3></div></div>
                    <div class="row">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th>Sl. No.</th>
                                    <th>Description of items</th>                                                                        
                                    <th>Value(INR)</th>
                                    <th>In whose name (self, wife, child, dependant, other relation or benamidar) the asset is or was</th>                                    
                                    <th>Date and manner of acquisition or disposal</th>                                    
                                    <th>Remarks</th>
                                </tr>
                                <tr>
                                    <th>(1)</th>
                                    <th>(2)</th>
                                    <th>(3)</th>
                                    <th>(4)</th>
                                    <th>(5)</th>
                                    <th>(6)</th>                                                                        
                                </tr>
                            </thead>
                            <tbody>
                                <c:set var="theCountIm" value="0" scope="page" />
                                <c:forEach items="${movablePropertyDetailList}" var="movablePropertyDetail"  varStatus="theCount">
                                    <c:if test="${movablePropertyDetail.propertyId == 10 and movablePropertyDetail.propertyValue ne '' and not empty movablePropertyDetail.propertyValue}">
                                        <c:set var="theCountIm" value="${theCountIm + 1}" scope="page"/>
                                        <c:set var="isOtherMovableExist" value="true"/>
                                        <tr>
                                            <td>${theCountIm}</td>
                                            <td>${movablePropertyDetail.propertyName} (${movablePropertyDetail.descofothitem})</td>                                        
                                            <td>${movablePropertyDetail.propertyValue}</td>                                        
                                            <td>${movablePropertyDetail.propertyOwnerDtl} <c:if test="${movablePropertyDetail.propertyOwner == 5}">(${movablePropertyDetail.otherPropertyOwner}) </c:if></td>
                                            <td><fmt:formatDate pattern = "dd-MM-yyyy" value = "${movablePropertyDetail.dateOfAcq}" /><br/> ${movablePropertyDetail.manner}</td>

                                            <td>${movablePropertyDetail.remark}</td>
                                        </tr>
                                    </c:if>
                                </c:forEach>
                                <c:if test="${isOtherMovableExist eq 'false'}">
                                <td colspan="9" style="height: 25px;width: 100%;text-align: center">
                                    <span style="font-size:20pt;">
                                        <b>Nil Property Submitted</b>
                                    </span>
                                </td>
                            </c:if>
                            </tbody>
                        </table>
                    </div>
                    <div class="row"><p>I hereby declare that the declaration made above is complete true and correct to the best of my knowledge and belief.</p></div>
                    <br/>
                    <br/>
                    <br/>
                    <br/>
                    <div class="row">
                        <div class="col-sm-6">Date: <fmt:formatDate pattern = "dd-MM-yyyy" value = "${propertyStatement.submittedOn}" /></div>
                        <div class="col-sm-6" align="right">Signature...........</div>
                    </div>
                    <br/>
                    <div class="row">
                        <div class="col-sm-2">Note:-(1)</div>
                        <div class="col-sm-10">The categories of assets noted in brackets in above heads are only illustrative and not meant to be exhaustive. In case of jewelleries and ornaments their total weight in totals and their cash value should be given in column 3 of Form B(2).</div>
                    </div>
                    <div class="row">
                        <div class="col-sm-2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(2)</div>
                        <div class="col-sm-10">In filling the form, endeavour should be made to provide Government with as complete a picture as possible of the Government servant’s assets and no asset of appreciable value should be omitted by reason of any literal interpretation of the directions given.</div>
                    </div>
                    <div class="row"><div class="col-sm-12">&nbsp;</div></div>
                </div>
            </div>
        </div>
    </div>
</div>
</div>

</body>
</html>
