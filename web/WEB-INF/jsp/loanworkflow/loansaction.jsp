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
        <link rel="stylesheet" type="text/css" href="resources/css/themes/color.css">
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
            function type_designation(vals) {
                //  alert(vals);
                $('#id_from_des').html(vals)
            }
            function submitSanction() {
               // alert("Hi");
                var letterno = $('#letter_no').val();
                var letter_date = $('#letter_date').val();
                var letter_form_name = $('#letter_form_name').val();
                var letter_form_designation = $('#letter_form_designation').val();
                var letter_to_name = $('#letter_to_name').val();
                var memo_no = $('#memo_no').val();
                var memo_date = $('#memo_date').val();
                if (letterno == "") {
                    alert("Please enter letter no");
                    return false;
                }
                if (letter_date == "") {
                    alert("Please enter letter date");
                    return false;
                }
                if (letter_form_name == "") {
                    alert("Please enter form name");
                    return false;
                }
                if (letter_form_designation == "") {
                    alert("Please enter letter designation");
                    return false;
                }
                if (letter_to_name == "") {
                    alert("Please enter to name");
                    return false;
                }
                if (memo_no == "") {
                    alert("Please enter memo no");
                    return false;
                }
                if (memo_date == "") {
                    alert("Please enter memo date");
                    return false;
                }
            }

            function downloadpdf(loanId) {
              //  alert(loanId);
                var url = "DownloadPDF.htm?loanid="+loanId;;
                self.location = url;
            }
        </script>
    </head>
    <body style="margin-left:30px ">
        <p style="text-style:justify" align="center">GOVERNMENT OF ODISHA </p>
        <p style="text-style:justify" align="center">${LoanForm.offaddress} Department</div> </p>  

    <form class="form-horizontal"  action="saveLoansaction.htm" method="POST" commandName="Loan Saction"  enctype="multipart/form-data" onsubmit="return submitSanction()">
        <input type="hidden" name="loanId" id="loanid" value="${LoanForm.loanId}"/>
        <input type="hidden" name="taskid" id="taskid" value="${LoanForm.taskid}"/>
        <table style="width:100%">
            <tr>
                <td  valign="top">Letter No</td>
                <td style="width:30%">
                    <c:if test="${ not empty LoanForm.letterNo}">
                        ${LoanForm.letterNo}
                    </c:if> 
                    <c:if test="${ empty LoanForm.letterNo}">
                        <input name="letterNo" class="form-control" id="letter_no"/>
                    </c:if> 

                </td>
                <td>&nbsp;</td>
                <td  valign="top">Date</td>
                <td style="width:30%">
                    <c:if test="${ not empty LoanForm.letterDate}">
                        ${LoanForm.letterDate}
                    </c:if> 
                    <c:if test="${ empty LoanForm.letterDate}">
                        <input name="letterDate" class="form-control" id="letter_date"/>
                    </c:if>

                </td>

            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr >
                <td  valign="top">Letter From Name</td>
                <td style="width:30%;"> 
                    <c:if test="${ not empty LoanForm.letterformName}">
                        ${LoanForm.letterformName}
                    </c:if> 
                    <c:if test="${ empty LoanForm.letterformName}">
                        <input name="letterformName" class="form-control" id="letter_form_name"/>
                    </c:if>

                </td>
                <td>&nbsp;</td>
                <td  valign="top">Letter From Designation</td>
                <td style="width:30%">
                    <c:if test="${ not empty LoanForm.letterformdesignation}">
                        ${LoanForm.letterformdesignation}
                    </c:if> 
                    <c:if test="${ empty LoanForm.letterformdesignation}">
                        <input name="letterformdesignation" class="form-control" id="letter_form_designation"/>
                    </c:if>

                </td>

            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr >
                <td  valign="top">Letter To</td>
                <td style="width:30%;" colspan="3">
                    <c:if test="${ not empty LoanForm.letterto}">
                        ${LoanForm.letterto}
                    </c:if> 
                    <c:if test="${ empty LoanForm.letterto}">
                        <input name="letterto" class="form-control" id="letterto_name" value="The Account General (A & E), Odisha, Bhubaneswar."/>
                    </c:if>

                </td>



            </tr>
        </table>  
        <div style="height:20px"></div>
        <p style="text-style:justify;padding-left:10px"><strong>Sub:</strong>Sanction of advance for purchase of <strong>${LoanForm.preAdvPur}</strong> in favour of  <strong>${LoanForm.name}</strong> of <strong>${LoanForm.offaddress}</strong> Department</p>
        <div style="height:20px"></div>
        <p style="text-style:justify;padding-left:10px">
            Sir,<br/>
            &nbsp;&nbsp;&nbsp;&nbsp;I am directed to convey the sanction of the Governor under Rules 237-253 of odisha General Rules, Volume-I read with finance Department
            O.M. No. 23997/F, dated 05.06.1993, No.42918/F, dated 30.09.1993, NO 22542/F dt.03.07.2013 and No.5807/F dt. 01.03.2014 to the payment of an
            advance of <strong>RS ${LoanForm.amounpretadv}</strong> only in favour of <strong>${LoanForm.name}</strong> of this department as per the statement enclosed for the purpose of purchase of <strong>${LoanForm.preAdvPur}</strong>.
        </p>
        <p style="text-style:justify;padding-left:10px">
            2. &nbsp;&nbsp;&nbsp;&nbsp;The sanction is subject to availability of funds and will remain valid for one month from the date of issue.          
        </p>
        <p style="text-style:justify;padding-left:10px">
            3. &nbsp;&nbsp;&nbsp;&nbsp;The employee concerned is being informed that he will have to pay simple interest at the rate of 10% per annum
            or at such rate of interest as fixed by Government from time to time in respect of this advance. The interest will be calculated on the balance outstanding on the last day of each month.
            The amount of interest thus calculated will be converted will be recovered in <strong>${LoanForm.instalments} instalments</strong> after the whole
            of the principal amount is recovered   .          
        </p>
        <p style="text-style:justify;padding-left:10px">
            4. &nbsp;&nbsp;&nbsp;&nbsp;The charge is debitable to <strong>Demand No.5 - 7610 -Loans to Government Servants etc. - 204- Advances for ${LoanForm.preAdvPur} - 0825 -Loans & Advances - 48057 -${LoanForm.preAdvPur} Advances NON-PLAN" during this financial year.</strong>          
        </p>
        <p style="text-style:justify;padding-left:10px">
            5. &nbsp;&nbsp;&nbsp;&nbsp;The advance is sanctioned subject to the condition that no such previous advance is outstanding against the officers/employees concerned .        
        </p>
        <p style="text-style:justify;padding-left:10px">
            6. &nbsp;&nbsp;&nbsp;&nbsp;The Joint Secretary to Government,${LoanForm.offaddress} Department(Accounts) is Drawing and Disbursing Officer in respect of the advance.     
        </p>
        <p style="text-style:justify;padding-left:10px">
            7. &nbsp;&nbsp;&nbsp;&nbsp;The recovery of the advance will commence with commence with the first drawal of pay after the advance is drawn.   
        </p>
        <p style="text-style:justify;padding-left:10px">
            8. &nbsp;&nbsp;&nbsp;&nbsp;The statement enclosed includes required information i.e name with designation, basic pay, Grade Pay, Loanee identity (GPF) no, date of birth , rate of recovery and number of instalments etc. of the employee concerned.
        </p>
        <p style="text-style:justify;padding-right:100px" align="right">
            Yours faithfully,

        </p>
        <p style="text-style:justify;padding-right:100px" align="right" id="id_from_des">           

        </p>
        <table style="width:100%;margin-top:100px;margin-bottom:30px">
            <tr>
                <td  valign="top">Memo No</td>
                <td style="width:30%">
                    <c:if test="${ not empty LoanForm.memoNo}">
                        ${LoanForm.memoNo}
                    </c:if> 
                    <c:if test="${ empty LoanForm.memoNo}">
                        <input name="memoNo" class="form-control" id="memo_no"/>
                    </c:if>

                </td>
                <td>&nbsp;</td>
                <td  valign="top">Memo Date</td>
                <td style="width:30%">
                    <c:if test="${ not empty LoanForm.memoDate}">
                        ${LoanForm.memoDate}
                    </c:if> 
                    <c:if test="${ empty LoanForm.memoDate}">
                        <input name="memoDate" class="form-control" id="memo_date"/>
                    </c:if>

                </td>

            </tr>
        </table>


        <p style="text-style:justify;">
            1. &nbsp;&nbsp;&nbsp;&nbsp; Copy alonwith copy of the statement forwarded to Finance Department/Accounts Section(in duplicate )/ Treasury Officer, Special Treasury, No.-II OLA 
            Campus, Bhuabneswar / Person Concerned / Personal File / G.F For information and necessary action. The employees concerned are requested to kindly ensure that the computer
            is purchased and mortgaged to Government in time and watch the recovery of advance along with interest.
        </p>
        <p style="text-style:justify;">
            2.&nbsp;&nbsp;&nbsp;&nbsp;The advance is sanctioned on the undertaking that a Computer purchased and the amount taken as advance is limited to the price paid for it including such items as cost of taking delivery and subsequent repairs.
            But the advance must not be drawn from the Treasury untill it is actually needed for the purchase of the ${LoanForm.preAdvPur}. The date of drawal of the advance and the date of purchase should be reported to Finance Department as soon as they are done.
        </p>
        <p style="text-style:justify;">
            3.&nbsp;&nbsp;&nbsp;&nbsp;The employees concerned should execute agreement in FORM-19 of the O.G.F.R Volume-II and furnish the same to the concerned Drawing and Disbursing
            Officer before drawal of the advance. THE D.D.O should scrutinize the same and record a certificate of execution of agreement of the bill and after disbursement of the advance, return
            the agreement of the bill and after disbursement of the advance, return the agreement to the sanctioning authority indicating 
            the date of drawal and disbursement.The later on receipt of agreement shall scrutinize the same and thereafter inform the Accountant General,
            Odisha that the sanctioned advance has been paid to the concerned officer on execution of proper agreement.

        </p>
        <p style="text-style:justify;">
            4.&nbsp;&nbsp;&nbsp;&nbsp; A Mortgage Bond in the prescribed form should be executed by the employee concerned immediately after the ${LoanForm.preAdvPur} is purchased.
            The bond should be submitted to the Finance Department, within one month from the date on which the advance is drawn for safe custody.
            A full specification of the conveyance, purchased given at full cost price should be entered in the schedule to the mortgage bond which is no circumstances be omitted or left incomplete. 
        </p>
        <p style="text-style:justify;">
            5.&nbsp;&nbsp;&nbsp;&nbsp; Contravention of those orders will render the Government servant liable to refund the whole of the amount
            advance with interest accrued, unless good reason is shown to the contrary. The amount to be refunded must be recovered in not more than three consecutive monthly instalments.
        </p>
        <p style="text-style:justify;padding-right:100px" align="right">
            <br/>
            ${LoanForm.letterformdesignation}

        </p>
        <div align="center">${LoanForm.preAdvPur} Advance is sanctioned during ${curreyear}</div>
        <table id="loandg" class="easyui-datagrid" style="width:100%;height:400px;" title="Loan List"
               rownumbers="true" singleSelect="true" url="SactionOrder.htm" singleSelect="true" pagination="true" collapsible="false"
               data-options="nowrap:false" toolbar="#toolbar">
            <thead>
                <tr>
                    <th data-options="field:'name',width:'30%'">Name & Designation</th>
                    <th data-options="field:'dob',width:'15%'">D.O.B</th>
                    <th data-options="field:'basicsalary',width:'10%'">Present pay<br/>+ <br/>Grade Pay   </th>  
                    <th data-options="field:'jobtype',width:'10%'">Temp./<br/>Perm.</th>
                    <th data-options="field:'suretybond',width:'10%'" >Surety<br/> bond </th>
                    <th data-options="field:'installment',width:'5%'" > No.<br/> of <br/>instalment  </th>
                    <th data-options="field:'amount',width:'10%'" >Amount <br/>per<br/> instalment  </th>
                    <th data-options="field:'gpfno',width:'10%'" >Identity No.  </th>
                </tr> 
            </thead>
            <tr>
                <td>${LoanForm.empName}</td>
                <td>${LoanForm.empdob}</td>
                <td>${LoanForm.basicsalary}</td>
                <td> ${LoanForm.jobType}</td>
                <td>Not <br/> applicable</td>
                <td>${LoanForm.instalments}</td>
                <td>${LoanForm.amountadv}</td>
                <td> ${LoanForm.gpfno}</td>
            </tr>
        </table>
        <div style="text-align:center;">
            <c:if test="${  empty LoanForm.letterNo}">    
                <input class="btn-primary" type="submit" name="Save" value="Submit" style="width:100px;height:30px" />
            </c:if> 
            <c:if test="${ not empty LoanForm.letterNo}">
             <input class="btn-primary" type="button" name="Back" onclick="downloadpdf(${LoanForm.loanId})" value="Downlaod" />
                <br/>
            </c:if>    
        </div>       

    </form>
</body>

</html>
