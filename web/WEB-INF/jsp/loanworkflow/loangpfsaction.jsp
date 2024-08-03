<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <link  rel="stylesheet" type="text/css"  href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"/>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
        <script type="text/javascript">
            $(document).ready(function () {
                $('#loandg').datagrid('load', {
                    empid: $('#empId').val()
                });
            });
            function type_designation(vals){
              //  alert(vals);
                $('#id_from_des').html(vals)
            }
            function submitSanction(){
                
                var letterno=$('#letter_no').val();
                var letter_date=$('#letter_date').val();
                 var letter_form_name=$('#letter_form_name').val();
                 var letter_form_designation=$('#letter_form_designation').val();
                   var letter_to_name=$('#letter_to_name').val();
                    var memo_no=$('#memo_no').val();
                     var memo_date=$('#memo_date').val();
                 if(letterno==""){
                     alert("Please enter letter no");
                     return false;
                 }
                 if(letter_date==""){
                     alert("Please enter letter date");
                     return false;
                 }
                 if(letter_form_name==""){
                     alert("Please enter form name");
                     return false;
                 }
                 if(letter_form_designation==""){
                     alert("Please enter letter designation");
                     return false;
                 }
                 if(letter_to_name==""){
                     alert("Please enter to name");
                     return false;
                 }
                 if(memo_no==""){
                     alert("Please enter memo no");
                     return false;
                 }
                 if(memo_date==""){
                     alert("Please enter memo date");
                     return false;
                 }
            }
            
            function downloadpdf(ids){
                var url = "gpftemporarylPDF.htm?loanid="+ids;
                self.location = url;
            }
        </script>
    </head>
    <body style="margin-left:30px ">
        <table width="100%"> 
            <tr>
                <td width="70%">Schedule L III -- Form No. 239</td> 
                <td width="30%"><u>G.P.F. A/C No.  ${LoanGPFForm.gpfno}</u></td>
            </tr>    
                
        </table>    
        <p style="text-style:justify" align="center">GOVERNMENT OF ODISHA </p>
        <p style="text-style:justify" align="center">${LoanGPFForm.officeAddress} Department</div> </p>  
            
    <form class="form-horizontal"  action="saveGpfLoansaction.htm" method="POST" commandName="Loan Saction"  enctype="multipart/form-data" onsubmit="return submitSanction()">
         <input type="hidden" name="loanId" id="loanid" value="${LoanGPFForm.loanId}"/>
            <input type="hidden" name="taskid" id="taskid" value="${LoanGPFForm.taskid}"/>
        <table style="width:100%">
             <tr>
                <td  valign="top">Sanction No</td>
                <td style="width:30%">
                    <c:if test="${ not empty LoanGPFForm.sanNo}">
                        ${LoanGPFForm.sanNo}
                     </c:if> 
                     <c:if test="${ empty LoanGPFForm.sanNo}">
                       <input name="sanNo" class="form-control" id="sanNo"/>
                     </c:if> 
                   
                </td>
                <td>&nbsp;</td>
                <td  valign="top">Sanction Date</td>
                <td style="width:30%">
                     <c:if test="${ not empty LoanGPFForm.sanDate}">
                        ${LoanGPFForm.sanDate}
                     </c:if> 
                     <c:if test="${ empty LoanGPFForm.sanDate}">
                        <input name="sanDate" class="form-control" id="sanDate"/>
                     </c:if>
                  
                </td>
               
            </tr>
             <tr>
                <td>&nbsp;</td>
            </tr>
             <tr >
                <td  valign="top">File No</td>
                <td style="width:30%;"> 
                     <c:if test="${ not empty LoanGPFForm.fileno}">
                        ${LoanGPFForm.fileno}
                     </c:if> 
                     <c:if test="${ empty LoanGPFForm.fileno}">
                        <input name="fileno" class="form-control" id="fileno"/>
                     </c:if>
                   
                </td>
                <td>&nbsp;</td>
                <td  valign="top">Issue No</td>
                <td style="width:30%">
                     <c:if test="${ not empty LoanGPFForm.issueNo}">
                        ${LoanGPFForm.issueNo}
                     </c:if> 
                     <c:if test="${ empty LoanGPFForm.issueNo}">
                        <input name="issueNo" class="form-control" id="issueNo"/>
                     </c:if>
                   
                </td>
               
            </tr>
             <tr>
                <td>&nbsp;</td>
            </tr>
             <tr  style='display:none'>
                <td  valign="top">&nbsp;</td>
                <td style="width:30%;" colspan="3">
                    <input name="letterto" class="form-control" id="letterto_name" value="" type='hidden'/>                   
                </td>    
            </tr>
        </table>  
          <div style="height:20px"></div>
         <p style="text-style:justify;padding-left:10px">
           1. Sanction is hereby accorded under GPF Rule 15/16 of General Provident Fund (Odisha) Rule for grant of advance of
           Rs. <strong>${LoanGPFForm.withdrawalreq}</strong>/-  to <strong>${LoanGPFForm.empName}</strong>,<strong>${LoanGPFForm.designation}</strong> from his/her
           GPF/AISPF/AEIPF Account No. <strong>${LoanGPFForm.gpfno}</strong> to enable him/her to meet the expenditure towards <strong>${LoanGPFForm.purpose}</strong>
         <p style="text-style:justify;padding-left:10px">
           2. The Sanctioned amount is debitable to the Head of account 00-8009-STATEPROVIDENTFUNDS-01-101-GENERALPROVIDENTFUNDS-1686-EmployeesProvidentFund-91043-DepositsofStateGovt.Employees-000-Deposits of State Govt. Employees.       
        </p>
       <!-- <p style="text-style:justify;padding-left:10px">
           3. A sum of Rs.<strong>${LoanGPFForm.balanceCredit}/-</strong>  out of consolidated Advance of Rs.3,87,000/- sanctioned vide No. 7974, dated 19/03/2018 , together with the amount now sanctioned aggregating to Rs.2,50,000 /- will be recovered in 50 monthly installments of Rs.5,000/- each commencing from the salary month December,2019 payable in January,2020.    
        </p>-->
         <p style="text-style:justify;padding-left:10px">
           4. The balance at the credit of <strong>${LoanGPFForm.empName}</strong>,<strong>${LoanGPFForm.designation}</strong> is detailed below;
        </p>
        <table width="100%">
            <tr>
                <td>
                    1. Balance as per Account Slip for
                </td>
                <td>
                    March, ${LoanGPFForm.cyear}
                </td>
                 <td>
                    Rs ${LoanGPFForm.closingbalance}/
                </td>
            </tr>
             <tr>
                <td>
                    2. Subsequent deposit from
                </td>
                <td>
                    <strong>${LoanGPFForm.creditForm}</strong> to <strong>${LoanGPFForm.creditTo}
                </td>
                 <td>
                    Rs ${LoanGPFForm.creditAmount}/
                </td>
            </tr>
            <tr>
                <td>
                    3. Total of Column 1 & 2 above
                </td>
                <td>
                    
                </td>
                 <td>
                     <c:set var="total" value="${LoanGPFForm.closingbalance+LoanGPFForm.creditAmount}" />
                  RS ${total}
                </td>
            </tr>
             <tr>
                <td>
                    4.Subsequent withdrawal if any
                </td>
                <td>
                    -
                </td>
                 <td>
                    RS ${LoanGPFForm.withdrawalAmount}/-
                </td>
            </tr>
             <tr>
                <td>
                    5. Net Balance
                </td>
                <td>
                   
                </td>
                 <td>
                      <c:set var="nettotal" value="${total-LoanGPFForm.withdrawalAmount}" />
                   
                   ------------------------------<br/>
                  RS ${nettotal}
                </td>
            </tr>
        </table>
         <p style="text-style:justify;padding-left:10px">
           Certified that, the annual statement of recovery of the G.P.F of individual subscriber has been sent to A.G.(A&E),Bhubaneswar,Odisha vide letter No.
            <c:if test="${ not empty LoanGPFForm.letterNo}">
                ${LoanGPFForm.letterNo}
             </c:if> 
            <c:if test="${ empty LoanGPFForm.letterNo}">
              <input name="letterNo"  id="letter_no"/>
            </c:if> 
           dt.
            <c:if test="${ not empty LoanGPFForm.letterDate}">
                ${LoanGPFForm.letterDate}
             </c:if> 
            <c:if test="${ empty LoanGPFForm.letterDate}">
              <input name="letterDate"  id="letter_date"/>
            </c:if>
           
        </p>
         <table style="width:100%">
             <tr>
                <td  valign="top">Memo Number</td>
                <td style="width:30%">
                    <c:if test="${ not empty LoanGPFForm.memoNo}">
                        ${LoanGPFForm.memoNo}
                     </c:if> 
                     <c:if test="${ empty LoanGPFForm.memoNo}">
                       <input name="memoNo" class="form-control" id="memoNo"/>
                     </c:if>                    
                </td>
                <td>&nbsp;</td>
                <td  valign="top">Date</td>
                <td style="width:30%">
                     <c:if test="${ not empty LoanGPFForm.memoDate}">
                        ${LoanGPFForm.memoDate}
                     </c:if> 
                     <c:if test="${ empty LoanGPFForm.memoDate}">
                        <input name="memoDate" class="form-control" id="memoDate"/>
                     </c:if>
                  
                </td>              
                
                 
         </table>    
        <p>
            Copy forwarded to PRINCIPAL AG(A&E),ODISHA, BHUBANESWAR for information and necessary action.
        </p>    
        
        <div style="height:20px"></div>
        <div style="text-align:center;">
            <c:if test="${  empty LoanGPFForm.sanNo}">    
                <input class="btn-primary" type="submit" name="Save" value="Submit" style="width:100px;height:30px" />
            </c:if> 
            <c:if test="${ not empty LoanGPFForm.sanNo}">
             <input class="btn-primary" type="button" name="Back" onclick="downloadpdf(${LoanGPFForm.loanId})" value="Downlaod" />
                <br/>
            </c:if>    
        </div>  
    </form>   
    </body>

</html>
