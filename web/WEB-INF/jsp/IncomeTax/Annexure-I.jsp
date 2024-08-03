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
            <form class="form-inline" action="DDOEmployeeWiseIT.htm">
                <div class="container-fluid" style="padding-top: 5px;padding-bottom: 5px;">
                    <div class="panel panel-default" style="padding:10px;">
<div class="panel-heading">
                                <div class="row">
                                    <div class="col-lg-12">
                                        <div class="form-group" style="width:20%;">
                                            <label for="sltYear">&nbsp;Financial Year:</label>
                                            <select path="sltYear" id="sltYear" class="form-control" onchange="showMonth()">
                                                <option value="${fiscalYear}">${fiscalYear}</option>
                                            </select>
                                        </div>
                                        <div class="form-group" style="width:20%;">
                                            <label for="sltMonth">&nbsp;Quarter:</label>
                                            <select id="quarter" class="form-control">
                                                <option value="">Select</option>
                                                <option value="1">Q1</option>
                                                <option value="2">Q2</option>
                                                <option value="3">Q3</option>
                                                <option value="4">Q4</option>
                                            </select>
                                        </div>
                                        <button type="submit" class="btn btn-primary" id="btn_submit" onclick="javascript: return validate()">Submit</button>
                                        <span id="loader" style="font-size:8pt;font-style:italic;color:#777777;visibility: hidden;"> Please wait...</span>
                                    </div>
                                </div>
                            </div>                        
                        <div class="row" style="margin-bottom:10px;">
                            <div class="col-lg-12" align="center"> 
                                <b style="color:#0D8BE6;font-size:20px;">ANNEXURE - I: DEDUCTEE WISE BREAK UP OF TDS</b> 
                            </div>
                        </div>
                        <table align="center" class="table" style="margin-top:20px;margin-bottom:20px;font-size:11pt;" border="1">
                            <tr>
                                <td width="40%">Name of the Employer:</td>
                                <td>${ai.employerName}</td>
                            </tr>
                            <tr>
                                <td>TAN No:</td>
                                <td>${ai.tanNo}</td>
                            </tr>  
                            <tr>
                                <td>BSR Code of Branch/Receipt Number of form No. 24G:</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td>Date on which Challan deposited/Transfer Voucher date (dd/mm/yyyy):</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td>Challan Serial Number/DDO Serial No. of Form No. 24G:</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td>Amount as per Challan:</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td>Total TDS to be allocated among deductees as in the vertical total of col.326:</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td>Total interest to be allocated among deductees below:</td>
                                <td></td>
                            </tr>                            
                        </table>                        
                        <div class="row">
                            <div class="col-lg-12" style="height:400px;overflow:scroll;">
                                <table width="100%" class="table table-bordered" style="font-size:9pt;">
                                    <tr style="color:#890000;">
                                        <td>Challan Sl No.</td>
                                        <td>Update Mode for Deductee (Add /Update /PAN Update)</td>
                                        <td>BSR Code of Branch where Tax Deposited</td>
                                        <td>Date on which Tax Deposited</td>
                                        <td>Transfer Voucher /Challan Serial No.</td>
                                        <td>Section under which Payment made</td>
                                        <td>Total TDS to be allocated among deductees as in the vertical total of col.21</td>
                                        <td>Interest</td>
                                        <td>Others</td>
                                        <td>Total</td>
                                        <td>Sr No.</td>
                                        <td>Employee Reference No. provided by Employer</td>
                                        <td>Last PAN of Employee</td>
                                        <td>PAN of the Employee</td>
                                        <td>Name of the Employee</td>
                                        <td>Date of Payment/Credit (DD/MM/YYYY)</td>
                                        <td>Amount paid/ Credited</td>
                                        <td>TDS</td>
                                        <td>Surcharge</td>
                                        <td>Education Cess</td>
                                        <td>Total Tax Deducted</td>
                                        <td>Last Total Tax Deducted</td>
                                        <td>Date of Deduction</td>
                                        <td>Remarks</td>
                                        <td>Certificate Number issued by Accessing Officer u/s 197 for no-deduction /lower deduction</td>
                                    </tr>
                                    <tr>
                                        <td>1</td>
                                        <td>2</td>
                                        <td>3</td>
                                        <td>4</td>
                                        <td>5</td>
                                        <td>6</td>
                                        <td>7</td>
                                        <td>8</td>
                                        <td>9</td>
                                        <td>10</td>
                                        <td>11</td>
                                        <td>12</td>
                                        <td>13</td>
                                        <td>14</td>
                                        <td>15</td>
                                        <td>16</td>
                                        <td>17</td>
                                        <td>18</td>
                                        <td>19</td>
                                        <td>20</td>
                                        <td>21</td>
                                        <td>22</td>
                                        <td>23</td>
                                        <td>24</td>
                                        <td>25</td>
                                    </tr>

                                    <tr bgcolor="#EAEAEA" style="font-weight:bold;">
                                        <td>[314]</td>
                                        <td>[315]</td>
                                        <td>[316]</td>
                                        <td>[317]</td>
                                        <td>[318]</td>
                                        <td>[319]</td>
                                        <td>[320]</td>
                                        <td>[321]</td>
                                        <td>[322]</td>
                                        <td>[323]</td>
                                        <td>[324]</td>
                                        <td>[325]</td>
                                        <td>[326]</td>                                           
                                        <td>[327]</td>                                           
                                        <td>[328]</td>                                           
                                        <td>[329]</td>                                           
                                    </tr>
        

<c:if test="${not empty ai.dataList}">
    <c:forEach var="lst" items="${ai.dataList}" varStatus="slnoCnt">
                                    <tr>
                                        <td>${slnoCnt.index+1}</td>
                                        <td>${lst.empId}</td>
                                        <td>${lst.pan}</td>
                                        <td>${lst.empName}</td>
                                        <td>92B</td>
                                        <td>${lst.dateDeposited}</td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                        <td>${lst.amount}</td>
                                        <td>${lst.amount}</td> 
                                        <td>${lst.dateDeposited}</td> 
                                        <td></td> 
                                        <td></td> 
                                    </tr> 
    </c:forEach>                                    
</c:if>                                    
                                                                           
                                </table>
                            </div>

                        </div>
                    </div>
                </form>        
            </div>        
    </body>
</html>
