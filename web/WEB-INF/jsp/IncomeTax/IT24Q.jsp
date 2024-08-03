<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>

    </head>
    <body>
        <div id="page-wrapper">
            <form class="form-inline" action="IT24Q.htm">
                <div class="container-fluid" style="padding-top: 5px;padding-bottom: 5px;">
                    <div class="panel panel-default">
                        <div class="row">
                            <div class="col-lg-12" align="center"> 
                                <b style="color:#0D8BE6;font-size:20px;">FORM NO: 24Q</b> 
                            </div>
                        </div>
                        <div class="panel-heading" style="background-color:#FFFFFF;">
                                <div class="row">
                                    <div class="col-lg-12">
                                        <div class="form-group" style="width:20%;">
                                            <label for="finYear">&nbsp;Financial Year:</label>
                                            <select name="finYear" id="finYear" class="form-control">
                                                <option value="${fiscalYear}">${fiscalYear}</option>
                                            </select>
                                        </div>
                                        <div class="form-group" style="width:20%;">
                                            <label for="finQuarter">&nbsp;Quarter:</label>
                                            <select id="finQuarter" name="finQuarter" class="form-control">
                                                <option value="">Select</option>
                                                <option value="1">First Quarter</option>
                                                <option value="2">Second Quarter</option>
                                                <option value="3">Third Quarter</option>
                                                <option value="4">Last Quarter</option>
                                            </select>
                                        </div>
                                        <button type="submit" class="btn btn-primary" id="btn_submit" onclick="javascript: return validate()">Submit</button>
                                        <span id="loader" style="font-size:8pt;font-style:italic;color:#777777;visibility: hidden;"> Please wait...</span>
                                    </div>
                                </div>                            
                            <div class="row" style="margin-top:10px;">
                                <div class="col-lg-3">
                                    <label>1(a).Tax Deduction and Collection Account Number (TAN):</label>
                                </div>
                                <div class="form-group" class="col-lg-10">
                                    ${IB.tan}
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-lg-3">
                                    <label>(b).Permanent Account Number (PAN):</label>
                                </div>
                                <div class="form-group" class="col-lg-10">
                                    PANNOTREQD
                                </div>
                            </div>  
                            <div class="row">
                                <div class="col-lg-3">
                                    <label>(c).Financial Year:</label>
                                </div>
                                <div class="form-group" class="col-lg-10">
                                    ${IB.financialYear}
                                </div>
                            </div>  
                            <div class="row">
                                <div class="col-lg-3">
                                    <label>(d).Has the statement been filed earlier for this quarter(Yes/No):</label>
                                </div>
                                <div class="form-group" class="col-lg-10">
                                    
                                </div>
                            </div>                              
                            <div class="row" style="margin-top:10px;">
                                <div class="col-lg-3">
                                    <label>(2).Particulars of the Deductor (Employer):</label>
                                </div>
                                <div class="form-group" class="col-lg-10">
                                    
                                </div>
                            </div>                            
                            <div class="row">
                                <div class="col-lg-3">
                                    <label>(a).Name of the employer:</label>
                                </div>
                                <div class="form-group" class="col-lg-10">
                                    ${IB.employerName}
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-lg-3">
                                    <label>(b).If Central/State Govt:</label>
                                </div>
                                <div class="form-group" class="col-lg-10">
                                    
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-lg-3">
                                    <label>AIN Code of PAO/TO/CDDO:</label>
                                </div>
                                <div class="form-group" class="col-lg-10">
                                    
                                </div>
                            </div>  
                            <div class="row">
                                <div class="col-lg-3">
                                    <label>(c).TAN Registration No:</label>
                                </div>
                                <div class="form-group" class="col-lg-10">
                                    
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-lg-3">
                                    <label>(d).Address:</label>
                                </div>
                                <div class="form-group" class="col-lg-10">
                                    ${IB.address}
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-lg-3">
                                    <label>State:</label>
                                </div>
                                <div class="form-group" class="col-lg-10">
                                    ODISHA
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-lg-3">
                                    <label>Pin Code:</label>
                                </div>
                                <div class="form-group" class="col-lg-10">
                                    ${IB.pinCode}
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-lg-3">
                                    <label>Telephone No:</label>
                                </div>
                                <div class="form-group" class="col-lg-10">
                                    ${IB.phone}
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-lg-3">
                                    <label>Alternate Telephone No:</label>
                                </div>
                                <div class="form-group" class="col-lg-10">
                                    
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-lg-3">
                                    <label>Email:</label>
                                </div>
                                <div class="form-group" class="col-lg-10">
                                    
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-lg-3">
                                    <label>Alternate Email:</label>
                                </div>
                                <div class="form-group" class="col-lg-10">
                                    
                                </div>
                            </div>   
                            
                            <div class="row" style="margin-top:10px;">
                                <div class="col-lg-3">
                                    <label>(3).Particulars of the person responsible for deduction of tax:</label>
                                </div>
                                <div class="form-group" class="col-lg-10">
                                    
                                </div>
                            </div>                            
                            <div class="row">
                                <div class="col-lg-3">
                                    <label>(a).Name:</label>
                                </div>
                                <div class="form-group" class="col-lg-10">
                                    
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-lg-3">
                                    <label>(b).If Central/State Govt:</label>
                                </div>
                                <div class="form-group" class="col-lg-10">
                                    
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-lg-3">
                                    <label>AIN Code of PAO/TO/CDDO:</label>
                                </div>
                                <div class="form-group" class="col-lg-10">
                                    
                                </div>
                            </div>  
                            <div class="row">
                                <div class="col-lg-3">
                                    <label>(c).TAN Registration No:</label>
                                </div>
                                <div class="form-group" class="col-lg-10">
                                    
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-lg-3">
                                    <label>(d).Address:</label>
                                </div>
                                <div class="form-group" class="col-lg-10">
                                    
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-lg-3">
                                    <label>State:</label>
                                </div>
                                <div class="form-group" class="col-lg-10">
                                    
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-lg-3">
                                    <label>Pin Code:</label>
                                </div>
                                <div class="form-group" class="col-lg-10">
                                    
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-lg-3">
                                    <label>Telephone No:</label>
                                </div>
                                <div class="form-group" class="col-lg-10">
                                    
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-lg-3">
                                    <label>Alternate Telephone No:</label>
                                </div>
                                <div class="form-group" class="col-lg-10">
                                    
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-lg-3">
                                    <label>Email:</label>
                                </div>
                                <div class="form-group" class="col-lg-10">
                                    
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-lg-3">
                                    <label>Alternate Email:</label>
                                </div>
                                <div class="form-group" class="col-lg-10">
                                    
                                </div>
                            </div>  
                            <div class="row">
                                <div class="col-lg-3">
                                    <label>Mobile No.:</label>
                                </div>
                                <div class="form-group" class="col-lg-10">
                                    
                                </div>
                            </div>  
                           <div class="row" style="margin-top:10px;">
                                <div class="col-lg-10">
                                    <label>(4).Details of tax deducted and paid to the credit of the Central Government:</label>
                                </div>
                            </div>  
                            
                           <div class="row">
                                <div class="col-lg-12">
                                    <table width="100%" class="table table-bordered" style="font-size:11pt;">
                                        <tr>
                                            <td>Sl No.</td>
                                            <td>Tax</td>
                                            <td>Surcharge</td>
                                            <td>Education Cess</td>
                                            <td>Interest</td>
                                            <td>Fee</td>
                                            <td>Penalty/Others</td>
                                            <td>Total amount deposited as per challan/Book Adjustment</td>
                                            <td>Mode of TDS deposit through Challan(C)/Book Adjustment(B)</td>
                                            <td>BSR Code/Receipt Number of Form No. 24G</td>
                                            <td>Challan Serial No./DDO Serial No. of Form 24G</td>
                                            <td>Date on which amount deposited through challan/Date of transfer Voucher</td>
                                            <td>Minor Head of Challan</td>
                                        </tr>
                                        <tr>
                                            <td>[301]</td>
                                            <td>[302]</td>
                                            <td>[303]</td>
                                            <td>[304]</td>
                                            <td>[305]</td>
                                            <td>[306]</td>
                                            <td>[307]</td>
                                            <td>[308]</td>
                                            <td>[309]</td>
                                            <td>[310]</td>
                                            <td>[311]</td>
                                            <td>[312]</td>
                                            <td>[313]</td>                                           
                                        </tr>
                                        <c:forEach items="${IB.dataList}" var="list" varStatus="cnt">
                                        <tr>
                                            <td>
                                                ${cnt.index+1}
                                            </td>
                                            <td>

                                            </td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td></td>
                                            <td>${list.amount}</td>
                                            <td></td>
                                            <td>${list.challanNo}</td>
                                            <td>${list.ddoSerial}</td>
                                            <td>${list.dateDeposited}</td>
                                            
                                            <td></td>                                           
                                        </tr>
                                        </c:forEach>
                                    </table>
                                </div>
                            </div>                              
                        </div>


                    </div>
                </div>
            </form>        
        </div>        
    </body>
</html>
