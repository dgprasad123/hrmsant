<%-- 
    Document   : EmpQtrAllotment
    Created on : 16 Apr, 2018, 3:25:47 PM
    Author     : Manoj PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Officewise Quarter Allotment List</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <style type="text/css">
            h1{font-size:15pt;font-weight:bold;margin-bottom:10px;}

            .apply-table td{padding:5px;}
            .tblres td{padding:5px;}
        </style>
       
    </head>
    <body>
        <div style="width:90%;margin:0px auto;">
            <h1 align="center">QUARTER ALLOTMENT STATEMENT<br />
            ${officeName}</h1>                           
                <table border='0' cellspacing='1' width='100%' align='center' class='tblres table-bordered' style='font-size:10pt;margin-top:10px;'>
                    <tr style="font-weight:bold;background:#0D508E;color:#FFFFFF;">
                        <td>Sl No.</td>
                        <td>GPF No.</td>
                        <td>Employee Name/Post</td>
                        <td>Allotment Order No. & Date</td>
                        <td>Date of Possession</td>
                        <td>Qtr No. & Address</td>
                        <td>Monthly Rent (In Rs.)</td>
                    </tr>
                <c:forEach items="${quarterList}" var="eqallot" varStatus="count">
                    <tr>
                        <td>${count.index + 1}</td>
                        <td>${eqallot.gpfNo}</td>
                        <td>${eqallot.empName}<br /><strong>${eqallot.designation}</strong></td>
                        <td><strong>${eqallot.orderNumber}</strong><br />${eqallot.orderDate}</td>
                        <td>${eqallot.possessionDate}</td>
                        <td><strong>${eqallot.quarterNo}</strong><br />${eqallot.address}</td>
                        <td>${eqallot.quarterRent}</td>
                    </tr>                            
                </c:forEach>
            </table>
        </div>



        


        <script type="text/javascript">
            var obj = document.frmEmpQuarter;
            $(function() {
                $('#allotmentDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#possessionDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#orderDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                if ($('#opt').val() == 'edit')
                {
                    $('#formadd_blk').slideDown();
                }
                $('#order_date').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#surrender_date').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            });
            function surrenderAllotment(bqaId) {
                $('#AddModal').modal('show');
                $("#bqaId").val(bqaId);
            }
        </script>


    </body>
</html>
