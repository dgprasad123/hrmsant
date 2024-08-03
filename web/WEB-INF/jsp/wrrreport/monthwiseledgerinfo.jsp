<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <!-- Custom CSS -->
        <link href="css/sb-admin.css" rel="stylesheet">

        <!-- Bootstrap Core JavaScript -->
        <script src="js/bootstrap.min.js"></script>

        <link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap4.min.css"/>
        <script type="text/javascript"  src="js/jquery-1.12.4.js"></script>
        <script type="text/javascript"  src="js/jquery.dataTables.min.js"></script>
        <script type="text/javascript"  src="js/dataTables.bootstrap4.min.js"></script>
        <style>
            .textrent {
                font-family: Arial, Helvetica, sans-serif;
                font-size: 14px;
                color: #3E2E1C;
            }
        </style>    

    </head>
    <body>
        <jsp:include page="header_quarter.jsp">
            <jsp:param name="menuHighlight" value="NDCAPPLICATIONS" />
        </jsp:include> 
        <div id="wrapper">

            <div id="page-wrapper">
                <div class="container-fluid">
                    <div align="center" style="margin-top:5px;margin-bottom:7px;">

                        <div class="panel panel-primary">
                            <div class="panel-heading">LEDGER INFO</div>
                            <div class="panel-body">
                                <table  cellspacing="0" cellpadding="0" border="0" width="100%">
                                    <tbody><tr>
                                            <td width="50%">
                                                <table width="100%">
                                                    <tbody><tr>
                                                            <td class="textrent" align="right">
                                                                Reg ID
                                                            </td>
                                                            <td>
                                                                :
                                                            </td>
                                                            <td class="textrent" width="225" align="left">
                                                                <span id="Lbl_Reg_Id" style="font-weight:bold;">-NA-</span>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td class="textrent" align="right">
                                                                &nbsp;Ocu Code
                                                            </td>
                                                            <td>
                                                                :
                                                            </td>
                                                            <td class="textrent" align="left">
                                                                <span id="Lbl_Ocu_Cd" style="font-weight:bold;">${empDetails.occuId}</span>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td class="textrent" align="right">
                                                                UNIT
                                                            </td>
                                                            <td>
                                                                :
                                                            </td>
                                                            <td class="textrent" align="left">
                                                                <span id="Lbl_UNIT" style="font-weight:bold;">${empDetails.qrtrunit}</span>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td class="textrent" align="right">
                                                                QTR TYPE
                                                            </td>
                                                            <td>
                                                                :
                                                            </td>
                                                            <td class="textrent" align="left">
                                                                <span id="Lbl_QTR_TYPE" style="font-weight:bold;">${empDetails.qrtrtype}</span>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td class="textrent" align="right">
                                                                QTR NO
                                                            </td>
                                                            <td>
                                                                :
                                                            </td>
                                                            <td class="textrent" align="left">
                                                                <span id="Lbl_BLDG_NO" style="font-weight:bold;">${empDetails.quarterNo}</span>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td class="textrent" align="right">
                                                                Section
                                                            </td>
                                                            <td>
                                                                :
                                                            </td>
                                                            <td class="textrent" align="left">
                                                                <span id="Lbl_Sec" style="font-weight:bold;">${empDetails.section}</span>
                                                            </td>
                                                        </tr>

                                                        <tr>
                                                            <td class="textrent" align="right">
                                                                Department
                                                            </td>
                                                            <td>
                                                                :
                                                            </td>
                                                            <td class="textrent" align="left">
                                                                <span id="Lbl_dept" style="font-weight:bold;"></span>
                                                            </td>
                                                        </tr>
                                                    </tbody></table>
                                            </td>
                                            <td  valign="top" align="left" width="50%">
                                                <table width="70%">
                                                    <tbody><tr>
                                                            <td class="textrent" width="150" align="right">
                                                                Name
                                                            </td>
                                                            <td>
                                                                :
                                                            </td>
                                                            <td class="textrent" width="350" align="left">
                                                                <span id="Lbl_Name" style="font-weight:bold;">${empDetails.empName}</span>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td class="textrent" align="right">
                                                                DDO
                                                            </td>
                                                            <td>
                                                                :
                                                            </td>
                                                            <td class="textrent" align="left">
                                                                <span id="Lbl_DDO" style="color:Blue;"></span>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td class="textrent" align="right">
                                                                &nbsp;Designation
                                                            </td>
                                                            <td>
                                                                :
                                                            </td>
                                                            <td class="textrent" align="left">
                                                                <span id="Lbl_designation" style="font-weight:bold;"></span>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td class="textrent" align="right">
                                                                Allotment Order No
                                                            </td>
                                                            <td>
                                                                :
                                                            </td>
                                                            <td class="textrent" align="left">
                                                                <span id="Lbl_Allotment_OrderNo" style="font-weight:bold;">${empDetails.orderNumber}</span>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td class="textrent" align="right">
                                                                ${empDetails.orderNumber}
                                                            </td>
                                                            <td>
                                                                :
                                                            </td>
                                                            <td class="textrent" align="left">
                                                                <span id="Lbl_Allotment_Date" style="font-weight:bold;"> ${empDetails.allotmentDate}</span>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td class="textrent" align="right">
                                                                Occupation Date
                                                            </td>
                                                            <td>
                                                                :
                                                            </td>
                                                            <td class="textrent" align="left">
                                                                <span id="Lbl_Ocup_Date" style="font-weight:bold;"> ${empDetails.orderDate}</span>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td class="textrent" align="right">
                                                                Retirement Date
                                                            </td>
                                                            <td>
                                                                :
                                                            </td>
                                                            <td class="textrent" align="left">
                                                                <span id="Lbl_Retire_Date" style="font-weight:bold;">${empDetails.dos}</span>
                                                            </td>
                                                        </tr>
                                                    </tbody></table>
                                            </td>
                                        </tr>
                                    </tbody></table>
                                                    
                                                            
                            </div>
                                 <table class="table table-bordered table-hover table-striped">
                                        <thead>
                                            <tr>
                                                <th>#</th>
                                                <th>MONTH</th>
                                                <th>FINANCIAL YEAR</th>
                                                <th>OPBAL</th>                                            
                                                <th>ASSESSMENT</th>
                                                <th>REAL</th>
                                                <th>CLSBAL</th>
                                                <th>REAL DT</th> 
                                                <th>REMARKS</th>

                                            </tr>
                                        </thead>
                                        <tbody id="wrrgrid">
                                          <c:forEach items="${empData}" var="edata" varStatus="cnt">

                                                <tr >
                                                    <td>${cnt.index+1}</td> 
                                                    <td>${edata.strmonth}</td> 
                                                    <td>${edata.year}</strong></a></td>
                                                    <td>${edata.opbla}</td>                                                
                                                    <td>${edata.assessment}</td>
                                                    <td>${edata.real}</td>                                                                                                
                                                    <td>${edata.cbalance}</td>
                                                    <td>${edata.orderDate}</td>
                                                    <td>${edata.nocType}</td> 

                                                </tr>
                                            </c:forEach>                                   
                                        </tbody>
                                    </table>                              
                                                            
                        </div> 


                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
