<%-- 
    Document   : UnlockBill
    Created on : Mar 26, 2018, 2:37:15 PM
    Author     : Madhusmita
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script src="js/moment.js" type="text/javascript"></script>
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <title>Unlock Bill</title>        
        <script type="text/javascript">
            $(window).load(function() {
                // Fill modal with content from link href
                $("#billEditModal").on("show.bs.modal", function(e) {
                    var link = $(e.relatedTarget);
                    $(this).find(".modal-body").load(link.attr("href"));
                });
            });
            $(document).ready(function() {
                $('#tokendate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#prevTokendate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#billDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            });

            function billEditNo(billnumber) {

                $('#hidbillNo').val(billnumber);
                //alert($('#hidbillNo').val());
                //$('#hidUsertype').val(usertype);
                //alert($('#hidUsertype').val());                
                $.getJSON("billEditNo.htm?billnumber=" + billnumber, function(data) {
                    $('#hidbillNo').val(data.billnumber);
                    $('#fromMonth').val(data.fromMonth);
                    $('#fromYear').val(data.fromYear);
                    $('#toMonth').val(data.toMonth);
                    $('#toYear').val(data.toYear);
                    $('#ddoCode').val(data.ddoCode);
                    $('#billType').val(data.billType);
                    $('#payHead').val(data.payHead);
                    $('#tokenNumber').val(data.tokenNumber);
                    $('#tokendate').val(data.tokendate);
                    $('#rqdReports').val(data.rqdReports);

                    $('#prevTokenNumber').val(data.prevTokenNumber);
                    $('#prevTokendate').val(data.prevTokendate);
                    $('#isresubmitted').val(data.isresubmitted);

                    $('#processFromDate').val(data.processFromDate);
                    $('#processToDate').val(data.processToDate);

                    $('#billDate').val(data.billDate);
                    $('#billStatusId').val(data.billStatusId);
                    $('#billdesc').val(data.billdesc);
                    $('#prevTokenNumber').show();
                    $('#prevTokendate').show();
                    $('#isresubmitted').show();

                    $('#processFromDate').show();
                    $('#processToDate').show();



                });
                $('#billEditModal').modal('toggle');
            }
            function addOfficeData(obj)
            {
                $('#offname').empty();
                var dist_code = obj.value;
                //alert(dist_code);
                if (dist_code != '') {
                    var url = 'getoffice.htm?dist_code=' + dist_code;
                    $('#offname').append('<option value="">--Select Office--</option>');
                    $.getJSON(url, function(data) {
                        $.each(data, function(i, obj) {
                            $('#offname').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        });
                    });
                } else {
                    $('#offname').children().remove().end().append('<option selected value="">--Select Office--<\/option>');
                }
            }
            function unlockbillalert(billnumber)
            {
                if (confirm("Are You sure to unlock ?"))
                {
                    //self.location = "unlockbilldata.htm?billnumber=" + billnumber;
                    return false;
                } else {
                    return false;
                }
            }
            function unlockalert(billnumber)
            {
                if (confirm("Are You sure to unlock ?"))
                {                    
                    //self.location = "unlockBillForReSubmission.htm?billnumber=" + billnumber;
                    return true;
                } else {
                    return false;
                }
            }

            function objectBillAlert()
            {
                var checkBoxlength = $("input[name=objectbill]:checked").length;
                if (checkBoxlength == 0) {
                    alert("Please select bill.");
                    return false;
                }
                else
                {
                    alert("Are you Sure to Object Bill ?")
                    return true;
                }
            }
            function requiredReport()
            {
                var chkBoxlength = $("input[name=chkReports]:checked").length;
                if (chkBoxlength != 0) {
                    $('#hidrqdReports').val('R');
                }

                if ($("#tokenNumber").val() != '')
                {
                    if ($("#tokendate").val() == '')
                    {
                        alert("Enter Token Date");
                        $("#tokendate").focus();
                        return false;
                    }
                }
                return true;
            }
            function Blankfield() {
                if ($("#tokenNumber").val() != '')
                {
                    if ($("#tokendate").val() == '')
                    {
                        alert("Enter Token Date");
                        $("#tokendate").focus();
                        return false;
                    }
                }
            }
            //function billEdit(){
            //$('#billEditInfo').modal('toggle');                
            //}

            //status view by pabitra 

            function billStatusview(billnumber) {

                $.getJSON("billStatusview.htm?billnumber=" + billnumber, function(data) {
                    document.getElementById("vofficename").innerHTML = data.officename;
                    document.getElementById("vddocode").innerHTML = data.ddoccode;
                    document.getElementById("vfromMonth").innerHTML = data.fromMonth;
                    document.getElementById("vfromYear").innerHTML = data.fromYear;
                    document.getElementById("vbillno").innerHTML = data.billid;
                    document.getElementById("vbillType").innerHTML = data.billType;
                    document.getElementById("priority").innerHTML = data.priority;
                    document.getElementById("vbillDate").innerHTML = data.billDate;

                });
                $('#billStatusModal').modal('toggle');
            }
        </script>

    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">     

                <div class="container-fluid">


                    <div class="row">
                        <div class="col-lg-12">                            
                            <ol class="breadcrumb">
                                <li>
                                    <i class="fa fa-dashboard"></i>  <a href="index.html">Dashboard</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> Unlock BIll 
                                </li>                                
                            </ol>
                        </div>
                    </div>              
                    <div style="text-align:center;">
                        <form:form action="unlockbill.htm" commandName="billDetail" method="POST">
                            <form:hidden path="usertype" id="hidUsertype"/>
                            <form:hidden path="hidbillNo" id="hidbillNo"/>
                            <form:hidden path="rqdReports" id="rqdReports"/>
                            <form:hidden path="billStatusId" id="billStatusId"/>

                            <table border="0" width="60%" cellspacing="0" style="font-size:12px; font-family:verdana;">
                                <tr style="height: 30px">
                                    <td style="text-align:center;">
                                        <div class="col-md-4"> <form:label path="offcode">OFFICE CODE</form:label> </div>
                                        <div class="col-md-8"> <form:input path="offcode" class="form-control"/></div>
                                    </td> 
                                    <td style="text-align:center;">
                                        <div class="col-md-4"> <form:label path="billnumber">BILL ID</form:label></div>
                                        <div class="col-md-8"><form:input path="billnumber" class="form-control"/></div>
                                    </td>
                                    <td colspan="2">
                                        <input type="submit" class="btn btn-success form-control" value="Ok" />                                        
                                    </td>
                                </tr>                
                            </table>

                        </div>
                        <div class="row">
                            <div class="col-lg-12">
                                <h3>Bill List</h3>
                                <div class="table-responsive">
                                    <table class="table table-bordered table-hover table-striped">
                                        <thead>
                                            <tr>
                                                <th width="5%">Select Bill</th>
                                                <th width="10%">Bill Id</th>
                                                <th width="10%">Bill No</th>
                                                <th width="10%">Bill Date</th>
                                                <th width="15%">Bill Status</th>
                                                <th width="15%">Bill Group Name</th>
                                                <th width="10%">Month/Year</th>
                                                <th width="20%">Token No/Date</th>
                                                <th width="30%">Previous Token No/Date</th>
                                                <th width="20%">Office</th>
                                                <th width="10%">Unlock Bill</th>
                                                <th width="10%">Unlock To Resubmit</th>
                                                <th width="10%">Unlock For Error</th> 
                                                <!--  <th width="10%">Unlock Error/Objection Bill</th> -->
                                                <th width="10%">Bill Status Details</th>
                                                <th width="10%">Print Bill</th>
                                                <th width="10%">Download Bill</th>
                                                <th width="10%">Bill Type</th>
                                                <th width="10%">Bill Process Status</th>
                                                    <c:if test="${usrtype=='A' || usrtype=='D' }">
                                                    <th width="10%">Bill Edit</th>
                                                    </c:if>
                                            </tr>
                                        </thead>
                                        <c:if test="${not empty data}">
                                            <c:forEach var="list" items="${data}">
                                                <tbody>
                                                    <tr>
                                                        <td class="form-group"> 
                                                            <c:if test="${list.billStatusId ==5}">
                                                                <div class="form-group">
                                                                    <input type="checkbox" name="objectbill" value="${list.billnumber}"/>
                                                                </div>
                                                            </c:if>                                       
                                                        </td>
                                                        <td class="form-group">
                                                            <div class="form-group">
                                                                <a href="editunlockBill.htm?billNo=${list.billnumber}" > <c:out value="${list.billnumber}"/></a>
                                                            </div>
                                                        </td>
                                                        <td class="form-group">
                                                            <c:out value="${list.billdesc}"/>
                                                        </td>
                                                        <td class="form-group">
                                                            <c:out value="${list.billDate}"/>
                                                        </td>
                                                        <td class="form-group">
                                                            <c:out value="${list.billStatus}"/>
                                                        </td>
                                                        <td class="form-group">
                                                            <c:out value="${list.billgrpname}"/>
                                                        </td>
                                                        <td class="form-group">                                         
                                                            <c:out value="${list.aq_month}"/> /
                                                            <c:out value="${list.aq_year}"/>
                                                        </td>
                                                        <td class="form-group">
                                                            <c:out value="${list.tokenNumber}"/> /
                                                            <c:out value="${list.tokendate}"/>
                                                        </td>
                                                        <td class="form-group">
                                                            <c:out value="${list.prevTokenNumber}"/> /
                                                            <c:out value="${list.prevTokendate}"/>
                                                        </td>
                                                        <td class="form-group">
                                                            <c:out value="${list.offcode}"/>
                                                        </td>
                                                        <td class="form-group"> 
                                                            <c:if test="${usrtype=='A' && usrtname eq 'hrmsupport7'}"> 
                                                                <c:if test="${list.billStatusId eq 3}">
                                                                    <a href="unlockbilldata.htm?billnumber=<c:out value="${list.billnumber}"/>" onclick="return unlockbillalert('<c:out value="${list.billnumber}"/>');">Unlock</a> 
                                                                </c:if>
                                                            </c:if>
                                                        </td> 
                                                        <td class="form-group"> 
                                                            <c:if test="${list.billStatusId eq 2}">
                                                                <%--<a href=" javascript:unlockalert('<c:out value="${list.billnumber}"/>');">Unlock</a> --%>
                                                                <a href="unlockbilltoResubmit.htm?billnumber=<c:out value="${list.billnumber} "/>" onclick="return unlockalert('<c:out value="${list.billnumber}"/>');">Unlock</a> 
                                                            </c:if>                                    
                                                        </td>
                                                        <td class="form-group">
                                                            <c:if test="${usrtype=='A' && usrtname eq 'hrmsupport7'}"> 
                                                                <c:if test="${list.billStatusId eq 3}">
                                                                    <%--<a href=" javascript:unlockalert('<c:out value="${list.billnumber}"/>');">Unlock</a> --%>
                                                                    <a href="unlockBillForError.htm?billnumber=<c:out value="${list.billnumber} "/>" onclick="return unlockalert('<c:out value="${list.billnumber}"/>');">Unlock for Error</a> 
                                                                </c:if>
                                                            </c:if> 
                                                        </td>
                                                        <!--   <td class="form-group"> 
                                                        <c:if test="${usrtype=='A' && usrtname eq 'hrmsupport7'}">
                                                            <c:if test="${list.billStatusId ==4 || list.billStatusId ==8 }">                                                               
                                                                <a href="unlockbilldata.htm?billnumber=<c:out value="${list.billnumber}"/>" onclick="return unlockbillalert('<c:out value="${list.billnumber}"/>');">Unlock Error/Objection Bill</a> 
                                                            </c:if>
                                                        </c:if>                                   
                                                    </td>-->
                                                        <td class="form-group"> 
                                                            <a href="showUploadBillStatus.htm?billNo=<c:out value="${list.billnumber} "/>">Bill Details</a>                                                                                  
                                                        </td>
                                                        <td class="form-group"> 
                                                            <a href="payBillReportAction.htm?txtbilltype=PAY&billNo=<c:out value="${list.billnumber} "/>">Print Bill</a>                                                                                  
                                                        </td>
                                                        <td class="form-group"> 
                                                            <a href="saveBillURL.htm?billNo=${list.encryBillId}">Download Bill</a>                                                                                  
                                                        </td>
                                                        <td class="form-group"> 
                                                            <c:out value="${list.billType}"/>                                                                                
                                                        </td>
                                                        <td class="form-group">
                                                            <c:if test="${billstatus!=null}">
                                                                <a href="javascript:billStatusview('<c:out value="${billstatus}"/>')"   style="width:50px;color:red;font-weight:bold;">Bill is Under process</a> 
                                                            </c:if>
                                                            <c:if test="${billstatus==null}">
                                                                <a >Bill is  Not in Process Queue</a> 
                                                            </c:if>
                                                        </td>
                                                        <td>
                                                            <c:if test="${usrtype=='A' || usrtype=='D'}">
                                                                <c:if test="${list.billStatusId != 3 && list.billStatusId != 5 && list.billStatusId != 7}">                                                          
                                                                    <a href="javascript:billEditNo('<c:out value="${list.billnumber}"/>')" style="width:50px">Bill Edit</a>
                                                                    <c:if test="${list.billType eq 'ARREAR_6' and (list.arrearpercent eq '20' or list.arrearpercent eq '30' or list.arrearpercent eq '50' or list.arrearpercent eq '10')}">
                                                                        <br /><br />
                                                                        <a href="browseAquitanceArr.htm?billNo=${list.billnumber}" target="_blank" style="width:50px">Edit <c:out value="${list.arrearpercent}"/>%</a>
                                                                    </c:if>
                                                                </c:if>
                                                            </c:if>
                                                        </td>
                                                    </tr>
                                                </tbody>
                                            </c:forEach>                                                                             
                                        </c:if>
                                    </table>
                                </div>
                                <filedset>
                                    <div align="left">   
                                        <c:if test="${usrtype eq 'A' && usrtname eq 'hrmsupport7'}">
                                            <input type="submit" value="ObjectBill" name="action" style="width:100px;" onclick="return objectBillAlert()" class="btn btn-default"/>
                                        </c:if>
                                    </div>
                                </filedset>

                                <div class="modal fade" id="billEditModal" role="dialog" >
                                    <div class="modal-dialog">
                                        <!-- Modal content-->

                                        <div class="modal-content modal-lg">
                                            <div class="modal-header">
                                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                                                <h4 class="modal-title">Bill Details</h4>
                                            </div>
                                            <div class="modal-body" >
                                                <div class=row">
                                                    <table border="1" width="100%"  cellspacing="0" style="font-size:15px; font-family:verdana;padding-left:10px;">
                                                        <tr> 

                                                            <td>

                                                                <form:label path="fromMonth">From Month</form:label>
                                                                </td> 
                                                                <td>
                                                                <form:select class="form-control" id="fromMonth" path="fromMonth">
                                                                    <form:option value="0">--Select--</form:option>
                                                                    <form:option value="1">January</form:option>
                                                                    <form:option value="2">February</form:option>
                                                                    <form:option value="3">March</form:option>
                                                                    <form:option value="4">April</form:option>
                                                                    <form:option value="5">May</form:option>
                                                                    <form:option value="6">June</form:option>
                                                                    <form:option value="7">July</form:option>
                                                                    <form:option value="8">August</form:option>
                                                                    <form:option value="9">September</form:option>
                                                                    <form:option value="10">October</form:option>
                                                                    <form:option value="11">November</form:option>
                                                                    <form:option value="12">December</form:option>                                                                    
                                                                </form:select>
                                                            </td>  

                                                            <c:choose>
                                                                <c:when test="${usrtype=='A' && (usrtname eq 'hrmsupport7' || usrtname eq 'hrmsupport1' || usrtname eq 'hrmsupport2')}">
                                                                    <td>
                                                                        <form:label path="tokenNumber">Token No</form:label>
                                                                        </td>
                                                                        <td>
                                                                        <form:input path="tokenNumber" id='tokenNumber' readonly="false" class="form-control"/>
                                                                    </td>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <form:hidden path="tokenNumber"/>
                                                                </c:otherwise>
                                                            </c:choose>                                                            
                                                        </tr>
                                                        <tr>                                           
                                                            <td>
                                                                <form:label path="fromYear">From Year</form:label>
                                                                </td>
                                                                <td>
                                                                <form:select class="form-control" path="fromYear" id="fromYear">
                                                                    <form:option value="0">--Select--</form:option>
                                                                    <form:option value="1978">1978</form:option>
                                                                    <form:option value="1979">1979</form:option>
                                                                    <form:option value="1980">1980</form:option>
                                                                    <form:option value="1981">1981</form:option>
                                                                    <form:option value="1982">1982</form:option>
                                                                    <form:option value="1983">1983</form:option>
                                                                    <form:option value="1984">1984</form:option>
                                                                    <form:option value="1985">1985</form:option>
                                                                    <form:option value="1986">1986</form:option>
                                                                    <form:option value="1987">1987</form:option>
                                                                    <form:option value="1988">1988</form:option>
                                                                    <form:option value="1989">1989</form:option>
                                                                    <form:option value="1990">1990</form:option>
                                                                    <form:option value="1991">1991</form:option>
                                                                    <form:option value="1992">1992</form:option>
                                                                    <form:option value="1993">1993</form:option>
                                                                    <form:option value="1994">1994</form:option>
                                                                    <form:option value="1995">1995</form:option>
                                                                    <form:option value="1996">1996</form:option>
                                                                    <form:option value="1997">1997</form:option>
                                                                    <form:option value="1998">1998</form:option>
                                                                    <form:option value="1999">1999</form:option>
                                                                    <form:option value="2000">2000</form:option>
                                                                    <form:option value="2001">2001</form:option>
                                                                    <form:option value="2002">2002</form:option>
                                                                    <form:option value="2003">2003</form:option>
                                                                    <form:option value="2004">2004</form:option>
                                                                    <form:option value="2005">2005</form:option>
                                                                    <form:option value="2006">2006</form:option>
                                                                    <form:option value="2007">2007</form:option>
                                                                    <form:option value="2008">2008</form:option>
                                                                    <form:option value="2009">2009</form:option>
                                                                    <form:option value="2010">2010</form:option>
                                                                    <form:option value="2011">2011</form:option>
                                                                    <form:option value="2012">2012</form:option>
                                                                    <form:option value="2013">2013</form:option>
                                                                    <form:option value="2014">2014</form:option>
                                                                    <form:option value="2015">2015</form:option>
                                                                    <form:option value="2016">2016</form:option>
                                                                    <form:option value="2017">2017</form:option>
                                                                    <form:option value="2018">2018</form:option>
                                                                    <form:option value="2019">2019</form:option>
                                                                    <form:option value="2020">2020</form:option>
                                                                    <form:option value="2021">2021</form:option>
                                                                    <form:option value="2022">2022</form:option>
                                                                    <form:option value="2023">2023</form:option>
                                                                    <form:option value="2024">2024</form:option>
                                                                </form:select>
                                                            </td>
                                                            <c:choose>
                                                                <c:when test="${usrtype=='A' && (usrtname eq 'hrmsupport7' || usrtname eq 'hrmsupport1' || usrtname eq 'hrmsupport2')}">
                                                                    <td>
                                                                        <form:label path="tokendate">Token Date</form:label>
                                                                        </td>
                                                                        <td>
                                                                        <form:input path="tokendate" id='tokendate' readonly="false" class="form-control"/>
                                                                    </td>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <form:hidden path="tokendate"/>
                                                                </c:otherwise>
                                                            </c:choose>                                                            
                                                        </tr>
                                                        <tr>                                           
                                                            <td>
                                                                <form:label path="toMonth">To Month</form:label>
                                                                </td>
                                                                <td style="width:50%;">
                                                                <form:select class="form-control" path="toMonth" id="toMonth">
                                                                    <form:option value="0">--Select--</form:option>
                                                                    <form:option value="1">January</form:option>
                                                                    <form:option value="2">February</form:option>
                                                                    <form:option value="3">March</form:option>
                                                                    <form:option value="4">April</form:option>
                                                                    <form:option value="5">May</form:option>
                                                                    <form:option value="6">June</form:option>
                                                                    <form:option value="7">July</form:option>
                                                                    <form:option value="8">August</form:option>
                                                                    <form:option value="9">September</form:option>
                                                                    <form:option value="10">October</form:option>
                                                                    <form:option value="11">November</form:option>
                                                                    <form:option value="12">December</form:option>                                                                    
                                                                </form:select>                                                                                                                    
                                                            </td>
                                                            <c:choose>
                                                                <c:when test="${usrtype=='A' && (usrtname eq 'hrmsupport7' || usrtname eq 'hrmsupport1' || usrtname eq 'hrmsupport2')}">
                                                                    <td>
                                                                        <form:label path="prevTokenNumber">Previous Token No</form:label>
                                                                    </td>
                                                                    <td>
                                                                        <form:input path="prevTokenNumber" id="prevTokenNumber" readonly="false" class="form-control"/>
                                                                    </td>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <form:hidden path="prevTokenNumber"/>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </tr>
                                                        <tr>                                           
                                                            <td>
                                                                <form:label path="toYear">To Year</form:label>
                                                                </td>
                                                                <td >
                                                                <form:select class="form-control" path="toYear" id="toYear">
                                                                    <form:option value="0">--Select--</form:option>
                                                                    <form:option value="1978">1978</form:option>
                                                                    <form:option value="1979">1979</form:option>
                                                                    <form:option value="1980">1980</form:option>
                                                                    <form:option value="1981">1981</form:option>
                                                                    <form:option value="1982">1982</form:option>
                                                                    <form:option value="1983">1983</form:option>
                                                                    <form:option value="1984">1984</form:option>
                                                                    <form:option value="1985">1985</form:option>
                                                                    <form:option value="1986">1986</form:option>
                                                                    <form:option value="1987">1987</form:option>
                                                                    <form:option value="1988">1988</form:option>
                                                                    <form:option value="1989">1989</form:option>
                                                                    <form:option value="1990">1990</form:option>
                                                                    <form:option value="1991">1991</form:option>
                                                                    <form:option value="1992">1992</form:option>
                                                                    <form:option value="1993">1993</form:option>
                                                                    <form:option value="1994">1994</form:option>
                                                                    <form:option value="1995">1995</form:option>
                                                                    <form:option value="1996">1996</form:option>
                                                                    <form:option value="1997">1997</form:option>
                                                                    <form:option value="1998">1998</form:option>
                                                                    <form:option value="1999">1999</form:option>
                                                                    <form:option value="2000">2000</form:option>
                                                                    <form:option value="2001">2001</form:option>
                                                                    <form:option value="2002">2002</form:option>
                                                                    <form:option value="2003">2003</form:option>
                                                                    <form:option value="2004">2004</form:option>
                                                                    <form:option value="2005">2005</form:option>
                                                                    <form:option value="2006">2006</form:option>
                                                                    <form:option value="2007">2007</form:option>
                                                                    <form:option value="2008">2008</form:option>
                                                                    <form:option value="2009">2009</form:option>
                                                                    <form:option value="2010">2010</form:option>
                                                                    <form:option value="2011">2011</form:option>
                                                                    <form:option value="2012">2012</form:option>
                                                                    <form:option value="2013">2013</form:option>
                                                                    <form:option value="2014">2014</form:option>
                                                                    <form:option value="2015">2015</form:option>
                                                                    <form:option value="2016">2016</form:option>
                                                                    <form:option value="2017">2017</form:option>
                                                                    <form:option value="2018">2018</form:option>
                                                                    <form:option value="2019">2019</form:option>  
                                                                    <form:option value="2020">2020</form:option>
                                                                    <form:option value="2021">2021</form:option>
                                                                    <form:option value="2022">2022</form:option>
                                                                    <form:option value="2023">2023</form:option>
                                                                    <form:option value="2024">2024</form:option>
                                                                </form:select>
                                                            </td>
                                                            <c:choose>
                                                                <c:when test="${usrtype=='A' && (usrtname eq 'hrmsupport7' || usrtname eq 'hrmsupport1' || usrtname eq 'hrmsupport2')}">
                                                                    <td>
                                                                        <form:label path="prevTokendate">Previous Token Date</form:label>
                                                                    </td>
                                                                    <td>
                                                                        <form:input path="prevTokendate" id="prevTokendate" readonly="false" class="form-control"/>
                                                                    </td>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <form:hidden path="prevTokendate"/>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </tr>
                                                        <tr>                                           
                                                            <td>
                                                                <form:label path="ddoCode">DDO Code</form:label>
                                                                </td>
                                                                <td >
                                                                <form:input path="ddoCode" id="ddoCode" class="form-control" maxlength="9" readonly="true"/>                                                     
                                                            </td>
                                                            <c:choose>
                                                                <c:when test="${usrtype=='A' && (usrtname eq 'hrmsupport7' || usrtname eq 'hrmsupport1' || usrtname eq 'hrmsupport2')}">
                                                                    <td>
                                                                        <label for="isresubmitted">Is resubmitted</label>
                                                                    </td>
                                                                    <td>
                                                                        <form:select class="form-control" path="isresubmitted" id="isresubmitted">
                                                                            <form:option value="">--Select--</form:option>
                                                                            <form:option value="N">N</form:option> 
                                                                            <form:option value="Y">Y</form:option> 
                                                                        </form:select>
                                                                    </td>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <form:hidden path="isresubmitted"/>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </tr>
                                                        <tr>                                           
                                                            <td>
                                                                <form:label path="billType">Bill Type</form:label>
                                                                </td>
                                                                <td>
                                                                <form:select class="form-control" path="billType" id="billType">
                                                                    <form:option value="">--Select--</form:option>
                                                                    <form:option value="42">SALARY (42)</form:option>                                                                    
                                                                    <form:option value="42">DAILY WAGE SALARY - DAILY BASIS (26)</form:option>
                                                                    <form:option value="42">DAILY WAGE SALARY - MONTHLY (27)</form:option> 
                                                                   <%-- <form:option value="21">GRANT-IN AID (21)</form:option>--%>
                                                                    <form:option value="69">GRANT-IN AID (SALARY-69)</form:option>
                                                                    <form:option value="43">ARREAR SALARY(43)(OTHER ARREAR/7TH PAY ARREAR/7TH PAY ARREAR(60%))</form:option>
                                                                </form:select>
                                                            </td>
                                                            <td>
                                                                <c:if test="${usrtype=='A' && (usrtname eq 'hrmsupport7' || usrtname eq 'hrmsupport1' || usrtname eq 'hrmsupport2')}">
                                                                    <form:label path="billDate">Bill Date</form:label>
                                                                </c:if>
                                                            </td>
                                                            <td>
                                                                <c:if test="${usrtype=='A' && (usrtname eq 'hrmsupport7' || usrtname eq 'hrmsupport1' || usrtname eq 'hrmsupport2')}">
                                                                    <form:input path="billDate" id='billDate' readonly="false" class="form-control"/>
                                                                </c:if>
                                                                <c:if test="${usrtname ne 'hrmsupport7'}">
                                                                    <form:hidden path="billDate"/>
                                                                </c:if>
                                                            </td>
                                                        </tr>

                                                        <tr>                                           
                                                            <td>
                                                                <c:if test="${usrtype eq 'A' && (usrtname eq 'hrmsupport7' || usrtname eq 'hrmsupport1' || usrtname eq 'hrmsupport2')}">
                                                                    <label for="billdesc">Bill No</label>
                                                                </c:if>
                                                            </td>
                                                            <td>
                                                                <c:if test="${usrtype eq 'A' && (usrtname eq 'hrmsupport7' || usrtname eq 'hrmsupport1' || usrtname eq 'hrmsupport2')}">
                                                                    <form:input path="billdesc" class="form-control" maxlength="40"/>                                                     
                                                                </c:if>
                                                                <c:if test="${usrtype ne 'A' && (usrtname eq 'hrmsupport7' || usrtname eq 'hrmsupport1' || usrtname eq 'hrmsupport2')}">
                                                                    <form:hidden path="billdesc"/>
                                                                </c:if>
                                                            </td>
                                                            <td></td>
                                                        </tr>

                                                        <tr>                                           
                                                            <td>
                                                                <form:label path="payHead">Object Head</form:label>
                                                                </td>
                                                                <td>
                                                                <form:select class="form-control" path="payHead" id="payHead">
                                                                    <form:option value="">--Select--</form:option>
                                                                    <form:option value="000">CONTRACTUAL (000)</form:option>
                                                                    <form:option value="136">REGULAR (136)</form:option>
                                                                    <form:option value="855">ARREAR (855)</form:option>
                                                                    <form:option value="921">GRANT-IN AID (921)</form:option>
                                                                    <form:option value="506">CONTRACTUAL DEO (506)</form:option>
                                                                    <form:option value="523">ALLOWANCE ARREAR (523)</form:option> 
                                                                </form:select>                                                    
                                                            </td>
                                                        </tr> 
                                                        <tr>                                           
                                                            <td>
                                                                <form:label path="processFromDate">From Date</form:label>
                                                                </td>
                                                                <td >
                                                                <form:input path="processFromDate" id="processFromDate" class="form-control" maxlength="2"/>                                                     
                                                            </td>
                                                        </tr> 
                                                        <tr>                                           
                                                            <td>
                                                                <form:label path="processToDate">To Date</form:label>
                                                                </td>
                                                                <td >
                                                                <form:input path="processToDate" id="processToDate" class="form-control" maxlength="2" />                                                     
                                                            </td>
                                                        </tr> 

                                                    </table>
                                                    </br></br>
                                                    <%--  <div class="form-group">
                                                          <input type="checkbox" name="chkReports" id="chkReports" value="Y"/>
                                                          <label for="chkReports">Required Reports</label>
                                                      </div>--%>
                                                </div>
                                            </div>
                                            <br>
                                            <div class="modal-footer">
                                                <input type="submit" name="action" class="btn btn-default" style="width:70px" value="Update" onclick="return requiredReport()" /> 
                                            </div>
                                        </div>
                                    </div>
                                </div>


                                <div class="modal fade" id="billStatusModal" role="dialog" >
                                    <div class="modal-dialog">
                                        <!-- Modal content-->

                                        <div class="modal-content modal-lg">
                                            <div class="modal-header">
                                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                                                <h4 class="modal-title">Bill Status Details</h4>
                                            </div>
                                            <div class="modal-body" >
                                                <div class=row">
                                                    <table class="table">
                                                        <thead class="thead-light">
                                                            <tr>
                                                                <th scope="col">#</th>
                                                                <th scope="col">Office Name</th>
                                                                <th scope="col">DDO Code</th>
                                                                <th scope="col">Month</th>
                                                                <th scope="col">Year</th>
                                                                <th scope="col">Bill No</th>
                                                                <th scope="col">Bill Type</th>
                                                                <th scope="col">Bill Priority</th>
                                                                <th scope="col">Bill Date</th>
                                                            </tr>
                                                        </thead>
                                                        <tbody>
                                                            <tr>
                                                                <th scope="row">1</th>
                                                                <td id="vofficename"></td>
                                                                <td id="vddocode"></td>
                                                                <td id="vfromMonth"></td>
                                                                <td id="vfromYear"></td>
                                                                <td id="vbillno"></td>
                                                                <td id="vbillType"></td>
                                                                <td id="priority"></td>
                                                                <td id="vbillDate"></td>

                                                            </tr>

                                                        </tbody>
                                                    </table>

                                                </div>
                                            </div>

                                        </div>
                                    </div>
                                </div>
                            </form:form>
                        </div>
                    </div>                  
                </div>
            </div>
        </div>
    </body>
</html>
