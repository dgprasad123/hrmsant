<%-- 
    Document   : previewloansaction
    Created on : 14 Mar, 2017, 3:37:30 PM
    Author     : lenovo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  
    <body style='margin-left:50px'>
       <p style="text-style:justify" align="center">GOVERNMENT OF ODISHA </p>
        <p style="text-style:justify" align="center">${LoanForm.offaddress} Department </p> 
         <p style="text-style:justify" align="center">***** </p>
        <div  style="text-style:justify;float:left;width:70%;" >No.${LoanForm.letterNo}</div>
        <div  style="text-style:justify;float:left;width:30%">${LoanForm.letterDate}</div>
        <div style='height:20px'></div>
        <div style="text-style:justify;">
                From<br/>
                    <div style='padding-left:30px'>
                      ${LoanForm.letterformName},<br/>
                     ${LoanForm.letterformdesignation}
                    </div>
      </div> 
         <div style='height:20px'></div>
        <div style="text-style:justify;">
                To<br/>
                    <div style='padding-left:30px'>
                     ${LoanForm.letterto}
                    </div>
          </div>
        <div style='height:20px'></div>
        <div style="text-style:justify;">
             Sir,<br/>
                &nbsp;&nbsp;&nbsp;&nbsp;I am directed to convey the sanction of the Governor under Rules 237-253 of odisha General Rules, Volume-I read with finance Department
                O.M. No. 23997/F, dated 05.06.1993, No.42918/F, dated 30.09.1993, NO 22542/F dt.03.07.2013 and No.5807/F dt. 01.03.2014 to the payment of an
                advance of <strong>RS ${LoanForm.amounpretadv}</strong> only in favour of <strong>${LoanForm.name}</strong> of this department as per the statement enclosed for the purpose of purchase of <strong>${LoanForm.preAdvPur}</strong>.
        </div>            
        <p style="text-style:justify;">
            2. &nbsp;&nbsp;&nbsp;&nbsp;The sanction is subject to availability of funds and will remain valid for one month from the date of issue.          
        </p>
         <p style="text-style:justify;">
            3. &nbsp;&nbsp;&nbsp;&nbsp;The employee concerned is being informed that he will have to pay simple interest at the rate of 10% per annum
            or at such rate of interest as fixed by Government from time to time in respect of this advance. The interest will be calculated on the balance outstanding on the last day of each month.
            The amount of interest thus calculated will be converted will be recovered in <strong>${LoanForm.instalments} instalments</strong> after the whole
            of the principal amount is recovered   .          
        </p>
        <p style="text-style:justify;">
            4. &nbsp;&nbsp;&nbsp;&nbsp;The charge is debitable to <strong>Demand No.5 - 7610 -Loans to Government Servants etc. - 204- Advances for ${LoanForm.preAdvPur} - 0825 -Loans & Advances - 48057 -${LoanForm.preAdvPur} Advances NON-PLAN" during this financial year.</strong>          
        </p>
         <p style="text-style:justify;">
            5. &nbsp;&nbsp;&nbsp;&nbsp;The advance is sanctioned subject to the condition that no such previous advance is outstanding against the officers/employees concerned .        
        </p>
         <p style="text-style:justify;">
            6. &nbsp;&nbsp;&nbsp;&nbsp;The Joint Secretary to Government,${LoanForm.offaddress} Department(Accounts) is Drawing and Disbursing Officer in respect of the advance.     
        </p>
         <p style="text-style:justify;">
            7. &nbsp;&nbsp;&nbsp;&nbsp;The recovery of the advance will commence with commence with the first drawal of pay after the advance is drawn.   
        </p>
         <p style="text-style:justify;">
            8. &nbsp;&nbsp;&nbsp;&nbsp;The statement enclosed includes required information i.e name with designation, basic pay, Grade Pay, Loanee identity (GPF) no, date of birth , rate of recovery and number of instalments etc. of the employee concerned.
        </p>
         <p style="text-style:justify;padding-right:100px" align="right">
            Yours faithfully,
            
        </p>
        <p style="text-style:justify;padding-right:100px" align="right" id="id_from_des">           
            ${LoanForm.letterformdesignation} to Government
        </p>
        <div  style="text-style:justify;float:left;width:70%;" ><strong>Memo No.${LoanForm.memoNo}</strong></div>
        <div  style="text-style:justify;float:left;width:30%"><strong>Dated.${LoanForm.memoDate}</strong></div>
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
        <div style='height:30px'></div>
          <div align="center">${LoanForm.preAdvPur} Advance is sanctioned during ${curreyear}</div>
        <table border=1 align='center' width="100%">
            <thead>
                <tr>
                    <th style="width:'30%'">Name & Designation</th>
                    <th style="width:'15%'">D.O.B</th>
                    <th style="width:'10%'">Present pay<br/>+ <br/>Grade Pay   </th>  
                    <th style="width:'10%'">Temp./<br/>Perm.</th>
                    <th style="width:'10%'" >Surety<br/> bond </th>
                    <th style="width:'5%'" > No.<br/> of <br/>instalment  </th>
                    <th style="width:'10%'" >Amount <br/>per<br/> instalment  </th>
                    <th style="width:'10%'" >Identity No.  </th>
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
    </body>
</html>
