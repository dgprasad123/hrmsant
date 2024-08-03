<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Employee Quarter Allotment</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min.js"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <style type="text/css">
            h1{font-size:15pt;font-weight:bold;margin-bottom:10px;}

            .apply-table td{padding:5px;}
            .tblres td{padding:5px;}
        </style>
    </head>
    <body>
        <div style="width:80%;margin:0px auto;">
            <h1>Manage Employee Quarter Allotment</h1>
            <c:if test="${opt eq 'view'}">
                <div id="formadd_blk">
                    <table width="100%" cellspacing="1" cellpadding="4" border="0" bgcolor="#EAEAEA" style="border:1px solid #CCCCCC;margin-top:10px;" align="center" class="apply-table">
                        <tr bgcolor="#007CC1" style="font-weight:bold;color:#FFFFFF;">
                            <td colspan="4"></td>
                        </tr>
                        <tr bgcolor="#FFFFFF">
                            <td align="right">Order Number:</td>
                            <td>
                                <c:out value="${eqBean.orderNumber}"/>
                            </td>
                            <td align="right">Order Date:</td>
                            <td>
                                <c:out value="${eqBean.orderDate}"/>
                            </td>                    
                        </tr>                      
                        <tr bgcolor="#FFFFFF">
                            <td width="18%" align="right">Select Quarter:</td>
                            <td colspan="3">
                                <c:out value="${qtrpoolname}"/>
                            </td>
                        </tr> 
                        <tr bgcolor="#FFFFFF">
                            <td align="right">Quarter Number:</td>
                            <td colspan="3">
                                <c:out value="${eqBean.quarterNo}"/>
                            </td>
                        </tr> 
                        <tr bgcolor="#FFFFFF">
                            <td align="right">Allotment Date:</td>
                            <td>
                                <c:out value="${eqBean.allotmentDate}"/>
                            </td>
                            <td align="right">Possession Date:</td>
                            <td>
                                <c:out value="${eqBean.possessionDate}"/>
                            </td>                    
                        </tr>  
                        <tr bgcolor="#FFFFFF">
                            <td align="right">Address:</td>
                            <td colspan="3">
                                <c:out value="${eqBean.address}"/>
                            </td>
                        </tr>                
                        <tr bgcolor="#FFFFFF">
                            <td align="right">Quarter Rent:</td>
                            <td>
                                <c:out value="${eqBean.quarterRent}"/>
                            </td>
                            <td align="right">Sewerage Rent:</td>
                            <td>
                                <c:out value="${eqBean.sewerageRent}"/>
                            </td>  
                        </tr>
                        <tr bgcolor="#FFFFFF">
                            <td align="right">Water Rent:</td>
                            <td>
                                <c:out value="${eqBean.waterRent}"/>
                            </td>
                            <td align="right">Are you getting HRA?</td>
                            <td>
                                <c:if test="${eqBean.isGetHra == 'N'}">
                                    No
                                </c:if>
                                <c:if test="${eqBean.isGetHra == 'Y'}">
                                    Yes
                                </c:if>
                            </td>                    
                        </tr> 
                        <tr bgcolor="#FFFFFF">
                            <td colspan="4" align="center">
                                <a href="addEmpQuarterAllotment.htm"><button type="button" class="btn btn-primary btn-success">&laquo;Back</button></a>
                            </td>
                        </tr>
                    </table>
                </div>
            </c:if>
            <table border='0' cellspacing='1' width='100%' align='center' class='tblres table-bordered' style='font-size:10pt;margin-top:10px;margin-bottom:10px;'>
                <tr style="font-weight:bold;background:#0D508E;color:#FFFFFF;">
                    <td>Order Number</td>
                    <td>Order Date</td>
                    <td>Qtr No.</td>
                    <td>Allotment Date</td>
                    <td>Possession Date</td>
                    <td>Address</td>
                    <td>Quarter Rent</td>
                    <td>Water Rent</td>
                    <td>Sewerage Rent</td>
                    <td>Surrendered?</td>
                    <td>Getting HRA?</td>
                    <td align="center"></td>
                    <td align="center"></td>
                </tr>
                <c:forEach items="${eqList}" var="eqallot">
                    <tr>
                        <td>${eqallot.orderNumber}</td>
                        <td>${eqallot.orderDate}</td>
                        <td>${eqallot.quarterNo}</td>
                        <td>${eqallot.allotmentDate}</td>
                        <td>${eqallot.possessionDate}</td>
                        <td>${eqallot.address}</td>
                        <td>${eqallot.quarterRent}</td>
                        <td>${eqallot.waterRent}</td>
                        <td>${eqallot.sewerageRent}</td>
                        <td>${eqallot.ifSurrendered}</td>
                        <td>${eqallot.isGetHra}</td>
                        <td align="center">
                            <c:if test="${eqallot.isValidated eq 'N'}">
                                <a href="editEmpQuarterAllotment.htm?qaId=${eqallot.qaId}">Edit</a>
                            </c:if>
                            <c:if test="${eqallot.isValidated eq 'Y'}">
                                <a href="viewEmpQuarterAllotment.htm?qaId=${eqallot.qaId}">View</a>
                            </c:if>
                        </td>
                        <td align="center"><c:choose><c:when test="${eqallot.ifSurrendered == 'Yes'}"><span style="color:#FF0000;font-weight:bold;">SURRENDERED</span></c:when><c:otherwise><a href="javascript:void(0)" class="btn btn-default" onclick="javascript: surrenderAllotment(${eqallot.qaId})">Surrender</a></c:otherwise></c:choose></td>
                            </tr>                            
                </c:forEach>
            </table>
        </div>
    </body>
</html>
