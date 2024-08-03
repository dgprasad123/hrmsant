 


<!DOCTYPE html>


<html>
    <head>
        <base href="" />  
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Service Book</title>        
        <link rel="stylesheet" type="text/css" href="css/PrintDetail.css">   
        <style type="text/css">
            .footer {
                position: fixed;
                left: 0;
                bottom: 0;                
                border-radius: 25px;
                background: red none repeat scroll 0% 0% !important; 
                color: white;
                text-align: center;                                
                display: block; 
                bottom: 30px; 
                left: 20px; 
                padding: 0px 10px 0px 0px;
                box-sizing: border-box;
                cursor: pointer;
            }
            .floatingform{
                position: fixed;
                display: block; 
                border-radius: 25px;
                bottom: 80px; 
                left: 20px;
                padding: 15px 10px 10px 10px;
                border: 1px #666666 solid;
                background: white none repeat scroll 0% 0% !important; 
                width:500px;

            }
        </style>
        <script type="text/javascript">
            $("form#data").submit(function(e) {
                e.preventDefault();
                var formData = new FormData(this);

                $.ajax({
                    url: 'SaveServiceBookAnomali.htm',
                    type: 'POST',
                    data: formData,
                    success: function(data) {
                        alert(data.msg)
                    },
                    cache: false,
                    contentType: false,
                    processData: false
                });
            });
        </script>
    </head>

    <body>
        <div class="se-pre-con"></div>
        <form id="employeeProfile" action="employeeProfile.htm" method="GET" target="_blank">

            <div align="center" style="overflow-x:hidden">

                <div  style="width: 100%">

                    <div>
                        <CENTER>
                            <table width="100%" height="50" border="0" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td >&nbsp;</td>
                                </tr>
                            </table>
                            <table width="100%" border="1" cellpadding="0" cellspacing="0" bordercolor="#000099">

                                <tr>
                                    <td width="100%" height="1600">
                                        <table width="100%" height="1013" border="0" cellpadding="0" cellspacing="0">

                                            <tr>
                                                <td colspan="2" height="70" align="center" valign="middle">&nbsp;</td>
                                            </tr>
                                            <tr>
                                                <td  colspan="2" height="154" align="center" valign="middle">&nbsp;<img src='http://localhost:8080/HRMSOpenSource//images/odgovt.gif' height="144" width="165"/></td>
                                            </tr>
                                            <tr>
                                                <td colspan="2" height="67" align="center" valign="bottom" style="font-family:Arial, Helvetica, sans-serif;font-size:40px;color:#000099">
                                                    <strong>HUMAN RESOURCES MANAGEMENT SYSTEM</strong>    	</td>
                                            </tr>
                                            <tr>
                                                <td  height="51" colspan="2" align="center" valign="top" style="font-family:Arial, Helvetica, sans-serif;font-size:40px;color:#000099"><strong>Government of Odisha</strong></td>
                                            </tr>

                                            <tr> 
                                                <td  colspan="2" height="30" align="center">
                                                    &nbsp;
                                                </td>
                                            </tr>

                                            <tr>
                                                <td height="180" colspan="2" align="center" style="font-family:Arial, Helvetica, sans-serif;font-size:55px;float:right;font-style:normal;color:#000099">&nbsp;</td>
                                            </tr>
                                            <tr>
                                                <td  colspan="2" height="30" align="center">
                                                    &nbsp;
                                                </td>
                                            </tr>
                                            <tr>
                                                <td  colspan="2" height="30" align="center">
                                                    &nbsp;
                                                </td>
                                            </tr>
                                            <tr>
                                                <td  colspan="2" height="30" align="center">
                                                    &nbsp;
                                                </td>
                                            </tr>
                                            <tr>
                                                <td height="40" colspan="2" align="center" style="font-family:Arial, Helvetica, sans-serif;font-size:55px;font-style:normal;color:#000099"><strong>SERVICE BOOK </strong></td>
                                            </tr>
                                            <tr>
                                                <td colspan="2" height="40" align="center" style="font-family:Arial, Helvetica, sans-serif;font-size:55px;color:#000099"><strong>OF</strong></td>
                                            </tr>
                                            <tr>
                                                <td colspan="2" height="150" align="center">
                                                    <img src="profilephoto.htm" id="sbUserPhoto" onerror="callNoImage()" height="150px" width="140px" border="2"/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td colspan="2" height="40" align="center" style="font-family:Arial, Helvetica, sans-serif;font-size:45px;font-style:normal;color:#000099">
                                                    <strong> TARUN CHANDRA PAUL</strong>
                                                </td>	
                                            </tr>
                                            <tr>
                                                <td colspan="2" height="30" align="center" style="font-family:Arial, Helvetica, sans-serif;font-size:25px;font-style:normal;color:#000099">
                                                    <strong>
                                                        DEO CADRE&nbsp;
                                                        &nbsp;
                                                    </strong>

                                                </td>
                                            </tr>
                                            
                                            <tr height="35px">
                                                <td width="515" height="31" align="right" style="font-family:Arial, Helvetica, sans-serif;font-size:25px;font-style:normal;color:#000099">

                                                    <strong>
                                                        HRMS ID :
                                                    </strong>

                                                </td>
                                                <td width="515" height="31" align="left" style="font-family:Arial, Helvetica, sans-serif;font-size:25px;font-style:normal;color:#000099">

                                                    &nbsp;59001354
                                                    &nbsp;
                                                </td>
                                            </tr>

                                            <tr height="35px">
                                                <td width="515" height="31" align="right" style="font-family:Arial, Helvetica, sans-serif;font-size:25px;font-style:normal;color:#000099">

                                                    <strong>
                                                        GPF NO:
                                                    </strong>

                                                </td>
                                                <td width="515" height="31" align="left" style="font-family:Arial, Helvetica, sans-serif;font-size:25px;font-style:normal;color:#000099">

                                                    &nbsp;GAO60821
                                                    &nbsp;
                                                </td>
                                            </tr>
                                            <tr height="35px">
                                                <td width="515" height="31" align="right" style="font-family:Arial, Helvetica, sans-serif;font-size:25px;font-style:normal;color:#000099">
                                                    &nbsp;	
                                                </td>
                                                <td width="515" height="31" align="left" style="font-family:Arial, Helvetica, sans-serif;font-size:25px;font-style:normal;color:#000099">
                                                    &nbsp;
                                                </td>
                                            </tr>
                                            
                                                <tr>
                                                    <td colspan="2" height="100" align="center">
                                                        &nbsp;
                                                    </td>
                                                </tr>
                                            
                                            

                                                <tr>
                                                    <td colspan="2" height="50" align="center" style="font-family:Arial, Helvetica, sans-serif;font-size:25px;font-style:normal;color:#000099">
                                                        &nbsp;
                                                    </td>
                                                </tr>
                                            
                                            

                                                <tr>
                                                    <td colspan="2" height="30" align="center" style="font-family:Arial, Helvetica, sans-serif;font-size:25px;font-style:normal;color:#000099">
                                                        &nbsp;
                                                    </td>
                                                </tr>
                                            

                                            <tr>
                                                <td  colspan="2" height="30" align="center">
                                                    &nbsp;
                                                </td>
                                            </tr>
                                            <tr>
                                                <td  colspan="2" height="30" align="center">
                                                    &nbsp;
                                                </td>
                                            </tr>
                                            <tr>
                                                <td  colspan="2" height="30" align="center">
                                                    &nbsp;
                                                </td>
                                            </tr>
                                            <tr>
                                                <td  colspan="2" height="30" align="center">
                                                    &nbsp;
                                                </td>
                                            </tr>
                                            <tr>
                                                <td  colspan="2" height="30" align="center">
                                                    &nbsp;
                                                </td>
                                            </tr>
                                            <tr>
                                                <td  colspan="2" height="30" align="center">
                                                    &nbsp;
                                                </td>
                                            </tr>
                                            <tr>
                                                <td  colspan="2" height="30" align="center">
                                                    &nbsp;
                                                </td>
                                            </tr>
                                            <tr>
                                                <td  colspan="2" height="30" align="center">
                                                    &nbsp;
                                                </td>
                                            </tr>
                                            <tr>
                                                <td  colspan="2" height="30" align="center">
                                                    &nbsp;
                                                </td>
                                            </tr>
                                            <tr>
                                                <td  colspan="2" height="30" align="center">
                                                    &nbsp;
                                                </td>
                                            </tr>
                                            <tr>
                                                <td  colspan="2" height="30" align="center">
                                                    &nbsp;
                                                </td>
                                            </tr>
                                            <tr>
                                                <td  colspan="2" height="25" align="center">
                                                    &nbsp;
                                                </td>
                                            </tr>
                                            <tr>
                                                <td  colspan="2" height="90" align="center" style="font-family:Arial, Helvetica, sans-serif;font-size:30px;font-style:italic;color:#000099">
                                                    <img src='http://localhost:8080/HRMSOpenSource//images/file.JPG' height="80" width="90"/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td colspan="2" height="25" align="center" style="font-family:Arial, Helvetica, sans-serif;font-size:15px;color:#000099">Developed By</td>
                                            </tr>
                                            <tr>
                                                <td colspan="2" height="25"  align="center" style="font-family:Arial, Helvetica, sans-serif;font-size:15px;color:#000099"> Centre for Modernizing Government Initiative(CMGI)</td>
                                            </tr>
                                            <tr>
                                                <td  colspan="2" height="25" align="center" style="font-family:Arial, Helvetica, sans-serif;font-size:8px;color:#000099;text-transform: uppercase;">
                                                    DATE OF PRINTING: 
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                            <input type="button" name="pagebreak1" style="page-break-before: always;width: 0;height: 0"/>
                            <table width="1040"  style="left: 18px;">
                                <tr>
                                    <td class="printData" style="text-align:left">
                                        GOVERNMENT OF ODISHA
                                    </td>
                                    <td class="printFooter" style="text-align:center;text-transform: uppercase">&nbsp;

                                    </td>
                                    <td colspan="2" class="printData" style="text-align:right;">	
                                        SERVICE BOOK OF  TARUN CHANDRA PAUL
                                        
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="4">
                                        <hr/>
                                    </td>
                                </tr>
                            </table>
                            <table border="1" cellpadding="0" cellspacing="0" width="1040" height="1600" style="left: 18px;">
                                <tr>
                                    <td colspan="3" valign="middle" align="center">                                        
                                        <img src='displayfistpagesb.htm' border="2" id="imgid" onerror="callNoSBPage()" width="750px" height="1200px"/>
                                    </td>
                                </tr>        
                            </table>
                            
                            <table width="1040" style="left: 18px;">
                                <tr>
                                    <td colspan="4" height="5px">
                                        <hr/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="printData" >&nbsp;

                                    </td>
                                    <td class="printFooter" style="text-align:center">&nbsp;</td>
                                    <td class="printData" style="text-align:right; text-transform: uppercase;">Page:1</td>
                                </tr>
                            </table>   
                            <input type="button" name="pagebreak1" style="page-break-before: always;width: 0;height: 0"/>
                            <table width="1040"  style="left: 18px;">
                                <tr>
                                    <td class="printData" style="text-align:left">
                                        GOVERNMENT OF ODISHA
                                    </td>
                                    <td class="printFooter" style="text-align:center;text-transform: uppercase">&nbsp;

                                    </td>
                                    <td colspan="2" class="printData" style="text-align:right;">	
                                        SERVICE BOOK OF  TARUN CHANDRA PAUL
                                        
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="4">
                                        <hr/>
                                    </td>
                                </tr>
                            </table>
                            <table width="1040" height="100" border="1" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td class="printLabel" style="text-align:left;text-transform:uppercase;font-size:18px">NAME: </td>
                                    <td colspan="3" class="printData" style="text-align:left;text-transform:uppercase;font-size:18px">&nbsp; TARUN CHANDRA PAUL</td>
                                </tr>
                                <tr>
                                    <td width="250" class="printLabel" style="text-align:left;text-transform:uppercase;font-size:18px">

                                        GPF NO:


                                    </td>
                                    <td width="250" class="printData" style="text-align:left;text-transform:uppercase;font-size:18px">&nbsp;GAO60821</td>
                                    <td width="250" class="printLabel" style="text-align:left; text-transform:uppercase;font-size:18px">HRMS ID:</td>
                                    <td width="250" class="printData" style="text-align:left;text-transform:uppercase;font-size:18px">&nbsp;59001354</td>
                                </tr>
                            </table>
                            <table width="1040" height="150" border="1" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td width="250" class="printLabel" style="text-align:left;text-transform:uppercase;font-size:18px">Present Address:</br>(Residence)</td>
                                    <td width="750" class="printData" style="text-align:left;text-transform:uppercase;font-size:18px">&nbsp;

                                        	
                                    </td>
                                </tr>
                            </table>
                            <table width="1040" height="100" border="1" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td width="340" class="printLabel" style="text-align:left;text-transform:uppercase;font-size:18px">DATE OF BIRTH <br>BY CHRISTIAN ERA AS <br>NEARLY AS CAN <br>BE ASCERTAINED : </td>
                                    <td width="700" class="printData" style="text-align:left;text-transform:uppercase;font-size:18px">&nbsp;

                                        
                                            20-JUL-1967
                                        
                                        
                                            ( Twentyth   July  Nineteen hundred  Sixty Seven)
                                        

                                    </td>
                                </tr>
                            </table>
                            <table width="1040" height="50" border="1" cellpadding="0" cellspacing="0">
                                <tr class="alternateTD">
                                    <td  class="printLabel" style="text-align:left;text-transform:uppercase;font-size:20px">Employee Educational Details: </td>
                                </tr>
                            </table>
                            
                            <table width="1040" height="50" border="1" cellpadding="0" cellspacing="0">
                                <thead> </thead>
                                <tr>
                                    <td width="300" class="printLabel" style="text-align:left;font-size:18px">HEIGHT(in cm):</td>
                                    <td width="740" class="printData" style="text-align:left;text-transform:uppercase;font-size:18px">&nbsp;
                                        
                                            165.0
                                                                        
                                    </td>
                                </tr>
                            </table>
                            <table width="1040" height="100" border="1" cellpadding="0" cellspacing="0">
                                <thead> </thead>
                                <tr>
                                    <td width="300" class="printLabel" style="text-align:left;text-transform:uppercase;font-size:18px">Personal <br>Identification Mark : </td>
                                    <td width="740" class="printData" style="text-align:left;text-transform:uppercase;font-size:18px">&nbsp;
                                        I-A BLACK MOLE ON THE RIGHT SIDE CHEEK.II-A BLACK MOLE ON RIGHT CHEST.
                                    </td>
                                </tr>
                            </table> 

                            <table width="1040" height="130" border="1" cellpadding="0" cellspacing="0">
                                <thead> </thead>
                                <tr>
                                    <td width="300" class="printLabel" style="text-align:left;text-transform:uppercase;font-size:18px">Father's name <br>and residence : </td>
                                    <td width="740" class="printData" style="text-align:left;text-transform:uppercase;font-size:18px">
                                        
                                            
                                        
                                            
                                        
                                            
                                                <b>LATE GOLAK CH. PAUL</b>
                                            
                                        
                                            
                                        
                                            
                                        
                                            
                                        
                                            
                                        
                                            
                                                <b>MR ABC BH HJK</b>
                                            
                                        
                                        <br><br>
                                        
                                            PO-KAUDKOLE,VIA-DHANMANDAL
                                        
                                            L.I.G.-148, NAYAPALLI BRIT COLONY
                                        
                                            VILL-GARAGALI, PO-KOUDKOLE
                                        &nbsp;
                                    </td>
                                </tr>
                            </table>

                            <table width="1040" height="50" border="1" cellpadding="0" cellspacing="0">		
                                <thead> </thead>
                                <tr height="50" >
                                    <td width="300" class="printLabel" style="text-align:left; font-size:18px">DATE OF ENTRY IN <BR> GOVERMENT SERVICE :</td>
                                    <td width="740" class="printData" style="text-align:left;text-transform:uppercase;font-size:18px">&nbsp;
                                        26-SEP-1998 
                                        
                                            ( Twentysixth   September  Nineteen hundred Ninety Eight)
                                                                        
                                    </td>
                                </tr>
                            </table>
                            <table width="1040" height="80" border="1" cellpadding="0" cellspacing="0">		
                                <thead> </thead>
                                <tr height="80" >
                                    <td width="300" class="printLabel" style="text-align:left; font-size:18px">DATE OF ENTRY IN THE<BR>SERVICE FOR WHICH<BR>SERVICE BOOK CREATED:</td>
                                    <td width="740" class="printData" style="text-align:left;text-transform:uppercase;font-size:18px">&nbsp;

                                        26-SEP-1998
                                        ( Twentysixth   September  Nineteen hundred Ninety Eight)
                                    </td>
                                </tr>
                            </table>
                            <table width="1040" height="50" border="1" cellpadding="0" cellspacing="0">		
                                <thead> </thead>
                                <tr height="50" >
                                    <td width="520" class="printLabel" style="text-align:left; font-size:18px">DECLARATION OF HOME TOWN:</td>
                                    <td width="520" class="printData" style="text-align:left;text-transform:uppercase;font-size:18px">&nbsp;

                                        JAJPUR

                                    </td>
                                </tr>
                            </table>
                            <input type="button" name="pagebreak1" style="page-break-before: always;width: 0;height: 0"/>
                            <table width="1040"  style="left: 18px;">
                                <tr>
                                    <td class="printData" style="text-align:left">
                                        GOVERNMENT OF ODISHA
                                    </td>
                                    <td class="printFooter" style="text-align:center;text-transform: uppercase">&nbsp;

                                    </td>
                                    <td colspan="2" class="printData" style="text-align:right;">	
                                        SERVICE BOOK OF  TARUN CHANDRA PAUL
                                        
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="4">
                                        <hr/>
                                    </td>
                                </tr>
                            </table>
                            <table width="1040" height="50" border="1" cellpadding="0" cellspacing="0">
                                <thead> </thead>
                                <tr>
                                    <td width="500" class="printLabel" style="text-align:left;text-transform:uppercase;font-size:18px">DATE OF SUPERANNUATION: </td>
                                    <td width="500" class="printData" style="text-align:left;text-transform:uppercase;font-size:18px">&nbsp;31-JUL-2027</td>
                                </tr>
                            </table>
                            <table width="1040" border="1" cellpadding="0" cellspacing="0">
                                <thead> </thead>
                                <tr height="50" >
                                    <td width="250" class="printLabel" style="text-align:left; text-transform:uppercase;font-size:18px">Category:</td>
                                    <td width="250" class="printData" style="text-align:left;text-transform:uppercase;font-size:18px">&nbsp;
                                        
                                            GENERAL
                                                                        
                                    </td>
                                    <td width="250" class="printLabel" style="text-align:left;text-transform:uppercase;font-size:18px">Marital Status:</td>
                                    <td width="250" class="printData" style="text-align:left;text-transform:uppercase;font-size:18px">&nbsp;

                                        MARRIED

                                    </td>
                                </tr>
                            </table>
                            <table width="1040" height="50" border="1" cellpadding="0" cellspacing="0">
                                <thead> </thead>
                                <tr>
                                    <td width="280" class="printLabel" style="text-align:left;text-transform:uppercase;font-size:18px">Cell Phone:</td>
                                    <td width="760" class="printData" style="text-align:left;text-transform:uppercase;font-size:18px">&nbsp; 8328871516</td>
                                </tr>
                            </table>
                            <table width="1040" height="50" border="1" cellpadding="0" cellspacing="0">
                                <thead> </thead>
                                <tr class="alternateTD">
                                    <td  class="printLabel" style="text-align:left;text-transform:uppercase;font-size:20px">Identity of the Employee:</td>
                                </tr>
                            </table>
                            <table width="1040" height="50" border="1" cellpadding="0" cellspacing="0">
                                <thead> </thead>
                                <tr>
                                    <td width="220" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">Type of Identification</td>
                                    <td width="240" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">No.</td>
                                    <td width="280" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">Place of Issue</td>
                                    <td width="150" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">Date of Issue</td>
                                    <td width="150" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">Date of Expiry</td>
                                </tr>                            
                                
                                    <tr>
                                        <td width="220" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;PAN</td>
                                        <td width="240" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;BVTPP2779L</td>
                                        <td width="280" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;</td>
                                        <td width="150" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;</td>
                                        <td width="150" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;</td>
                                    </tr>
                                
                                    <tr>
                                        <td width="220" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;AADHAAR</td>
                                        <td width="240" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;317875480480</td>
                                        <td width="280" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;</td>
                                        <td width="150" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;</td>
                                        <td width="150" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;</td>
                                    </tr>
                                
                            </table>
                            <table width="1040" height="50" border="1" cellpadding="0" cellspacing="0">
                                <thead> </thead>
                                <tr class="alternateTD">
                                    <td  class="printLabel" style="text-align:left;text-transform:uppercase;font-size:20px">Family of the Employee:</td>
                                </tr>
                            </table>
                            <table width="1040" height="50" border="1" cellpadding="0" cellspacing="0">                                
                                <tr >
                                    <td width="250" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">Relation Type</td>
                                    <td width="350" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">Name</td>
                                    <td width="250" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">Is Alive?</td>
                                    <td width="250" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">DOB</td>
                                    <td width="250" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">Marital Status</td>
                                    <td width="250" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">Age</td>
                                </tr>
                                                                
                                    <tr>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;HUSBAND</td>
                                        <td width="350" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;DR ABC BH HJK</td>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;</td>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;05-07-2023</td>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;</td>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;0 years 3 months </td>
                                    </tr>                                
                                                                
                                    <tr>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;WIFE</td>
                                        <td width="350" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;SMT APARNA  PAUL</td>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;Y</td>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;18-10-1960</td>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;</td>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;63 years 0 months </td>
                                    </tr>                                
                                                                
                                    <tr>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;FATHER</td>
                                        <td width="350" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;LATE GOLAK CH. PAUL</td>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;</td>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;18-10-1960</td>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;</td>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;63 years 0 months </td>
                                    </tr>                                
                                                                
                                    <tr>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;DAUGHTER</td>
                                        <td width="350" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;MISS BARNALI  PATNAIK</td>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;</td>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;01-08-2023</td>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;</td>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;0 years 2 months </td>
                                    </tr>                                
                                                                
                                    <tr>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;</td>
                                        <td width="350" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;MRS ABC  GHJJ</td>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;</td>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;01-01-1900</td>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;</td>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;123 years 9 months </td>
                                    </tr>                                
                                                                
                                    <tr>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;</td>
                                        <td width="350" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;MRS ABC  HJK</td>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;</td>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;01-01-1900</td>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;</td>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;123 years 9 months </td>
                                    </tr>                                
                                                                
                                    <tr>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;SON</td>
                                        <td width="350" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;SHRI ROHIT  PAUL</td>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;</td>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;17-10-1960</td>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;</td>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;63 years 0 months </td>
                                    </tr>                                
                                                                
                                    <tr>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;FATHER</td>
                                        <td width="350" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;MR ABC BH HJK</td>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;</td>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;01-01-1900</td>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;</td>
                                        <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;123 years 9 months </td>
                                    </tr>                                
                                
                            </table>
                            <table width="1040" height="50" border="1" cellpadding="0" cellspacing="0">
                                <thead> </thead>
                                <tr class="alternateTD">
                                    <td  class="printLabel" style="text-align:left;text-transform:uppercase;font-size:20px">Nominee Detail:</td>
                                </tr>
                            </table>
                            <table width="1040" height="50" border="1" cellpadding="0" cellspacing="0">                                
                                <tr>
                                    <td width="250" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">Relation Type</td>
                                    <td width="350" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">Name</td>
                                    <td width="250" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">Is Alive?</td>
                                    <td width="250" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">DOB</td>
                                    <td width="250" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">Marital Status</td>
                                    <td width="250" class="printLabel" style="text-align:center;text-transform:uppercase;font-size:18px">Age</td>
                                </tr>
                                <tr>
                                    <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;DAUGHTER</td>
                                    <td width="350" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;MISS   </td>
                                    <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;</td>
                                    <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;01-08-2023</td>
                                    <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;</td>
                                    <td width="250" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;0 years 2 months </td>
                                </tr>                                
                            </table>
                            <table width="1040" height="50" border="1" cellpadding="0" cellspacing="0">
                                <thead> </thead>
                                <tr class="alternateTD">
                                    <td  class="printLabel" style="text-align:left;text-transform:uppercase;font-size:20px">Employee Reservation Category:</td>
                                </tr>
                            </table>   
                            <table width="1040" height="50" border="1" cellpadding="0" cellspacing="0">
                                <thead> </thead>
                                <tr>
                                    <td width="740" class="printLabel" style="text-align:left;text-transform:uppercase;font-size:18px">Is Employee under any Reservation Category?</td>
                                    <td width="300" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp; No</td>
                                </tr>
                            </table> 
                            <table width="1040" height="50" border="1" cellpadding="0" cellspacing="0">
                                <thead> </thead>
                                <tr>
                                    <td width="740" class="printLabel" style="text-align:left;text-transform:uppercase;font-size:18px">Reservation Category Under Which Employed </td>
                                    <td width="300" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp;
                                        
                                            GENERAL
                                        
                                    </td>	
                                </tr>
                            </table>
                            <table width="1040" height="50" border="1" cellpadding="0" cellspacing="0">
                                <thead> </thead>
                                <tr>
                                    <td width="740" class="printLabel" style="text-align:left;text-transform:uppercase;font-size:18px">Is Employee under Rehabilitation Assistance Scheme?</td>
                                    <td width="300" class="printData" style="text-align:center;text-transform:uppercase;font-size:18px">&nbsp; No</td>
                                </tr>
                            </table>
                            
                            <table width="1040"   style="left: 18px;">
                                <thead> </thead>
                                <tr>
                                    <td colspan="4" height="5px">
                                        <hr/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="printData" >
                                        &nbsp;
                                    </td>
                                    <td class="printFooter" style="text-align:center">&nbsp;</td>
                                    <td class="printData" style="text-align:right; text-transform: uppercase;">Page:2</td>
                                </tr>
                            </table>
                        </center>
                    </div>                   
                    <div style="page-break-before: always;">
                        <table width="1040" cellspacing="0" cellpadding="0" border="1">
                            <thead>
                                <tr class="alternateTD">
                                    <th width="300" style="text-align:center;font-size:16px;" class="printLabelHeader">Post/Cadre/Scale of Pay</th>
                                    <th width="120" style="text-align:center;font-size:16px;" class="printLabelHeader">Pay</th>
                                    <th width="90" style="text-align:center;font-size:16px;" class="printLabelHeader">WEF</th>
                                    <th width="450" style="text-align:center;font-size:16px;border-right:1px solid #666666;" colspan="2" class="printLabelHeader">Entry in the Service Book </th>
                                </tr>
                            </thead>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">1</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                                    4000-100-6000 
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                            PAY: Rs.4000.0/- 
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        26-SEP-1998
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> PAY FIXATION</u></b>  <br/>
                                                     PAY FIXED  @ Rs.4000/- PM WITH GRADE PAY Rs.50/- PM  WEF 26-SEP-1998 IN THE SCALE OF PAY OF RS. 4000-100-6000/-  HAVING REASON 3rd MACP VIDE FINANCE DEPARTMENT,GOVERNMENT OF ODISHA NOTIFICATION/ OFFICE ORDER NO. 42196/F DATED 26-SEP-1998.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: UNDER SECRETARY, FINANCE DEPARTMENT,GOVERNMENT OF ODISHA ON 26-SEP-1998
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">2</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        26-SEP-1998
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> JOINING</u></b>  <br/>
                                                     IN PURSUANCE OF NOTIFICATION NO. 42196/F DATED 26-SEP-1998,  JOINED AS DATA PROCESSING ASSISTANT(JUNIOR GRADE), FINANCE DEPARTMENT,GOVERNMENT OF ORISSA ON 26-SEP-1998(FN). 
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: UNDER SECRETARY, FINANCE DEPARTMENT,GOVERNMENT OF ODISHA ON 26-SEP-1998
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">3</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                                    4000-100-6000 
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                            PAY: Rs.50000.0/- 
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        24-SEP-1999
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> INCREMENT</u></b>  <br/>
                                                     ALLOWED  ANNUAL INCREMENT @ RS. 500/- RAISING PAY FROM RS. 49500/- TO RS. 50000/- PM  IN THE SCALE OF PAY RS. 4000-100-6000  WEF 24-SEP-1999, FN  GRADE PAY RS. 250.0/- PM,  VIDE HANDLOOMS, TEXTILES AND HANDICRAFTS DEPARTMENT  NOTIFICATION/ OFFICE ORDER NO. 49622/F DATED 6-DEC-1999.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: ASSISTANT DATA PROCESSING OFFICER, FINANCE DEPARTMENT,GOVERNMENT OF ODISHA, ODISHA ON 06-DEC-1999
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">4</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                            PAY: Rs.4500.0/- 
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        02-AUG-2000
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> INCREMENT</u></b>  <br/>
                                                     ALLOWED  ANNUAL INCREMENT @ RS. 100/- RAISING PAY FROM RS. 4400/- TO RS. 4500/- PM  WEF 2-AUG-2000, FN VIDE FINANCE DEPARTMENT  NOTIFICATION/ OFFICE ORDER NO. 37283/F DATED 12-SEP-2000.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: ASSISTANT DATA PROCESSING OFFICER, FINANCE DEPARTMENT,GOVERNMENT OF ODISHA, ODISHA ON 12-SEP-2000
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">5</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                                    4000-100-6000 
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                            PAY: Rs.4300.0/- 
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        01-SEP-2001
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> INCREMENT</u></b>  <br/>
                                                     ALLOWED  ANNUAL INCREMENT @ Rs. 100/- RAISING PAY FROM Rs. 4200/- TO Rs. 4300/- PM  IN THE SCALE OF PAY Rs. 4000-100-6000/- WEF 01-SEP-2001 VIDE FINANCE DEPARTMENT,GOVERNMENT OF ORISSA NOTIFICATION/ OFFICE ORDER NO. 47462/F DATED 14-SEP-2001.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: SECTION OFFICER, FINANCE DEPARTMENT,GOVERNMENT OF ODISHA ON 14-SEP-2001
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">6</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                            PAY: Rs.4400.0/- 
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        11-SEP-2002
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> INCREMENT</u></b>  <br/>
                                                     ALLOWED  ANNUAL INCREMENT @ RS. 100/- RAISING PAY FROM RS. 4300/- TO RS. 4400/- PM  WEF 11-SEP-2002, FN VIDE FINANCE DEPARTMENT  NOTIFICATION/ OFFICE ORDER NO. 41989/F DATED 6-SEP-2002.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: SECTION OFFICER, FINANCE DEPARTMENT,GOVERNMENT OF ODISHA ON 06-SEP-2002
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">7</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                            PAY: Rs.4500.0/- 
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        01-SEP-2003
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> INCREMENT</u></b>  <br/>
                                                     ALLOWED  ANNUAL INCREMENT @ RS. 100/- RAISING PAY FROM RS. 4400/- TO RS. 4500/- PM  IN CELL 10 OF LEVEL 6  WEF 1-SEP-2003, FN VIDE FINANCE DEPARTMENT  NOTIFICATION/ OFFICE ORDER NO. 40267 DATED 11-SEP-2003.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: SECTION OFFICER, FINANCE DEPARTMENT,GOVERNMENT OF ODISHA ON 11-SEP-2003
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">8</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                                    4000-100-6000 
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                            PAY: Rs.4600.0/- 
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        01-SEP-2004
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> INCREMENT</u></b>  <br/>
                                                     ALLOWED  ANNUAL INCREMENT @ Rs. 100/- RAISING PAY FROM Rs. 4500/- TO Rs. 4600/- PM  IN THE SCALE OF PAY Rs. 4000-100-6000/- WEF 01-SEP-2004 VIDE FINANCE DEPARTMENT,GOVERNMENT OF ORISSA NOTIFICATION/ OFFICE ORDER NO. 41055/F DATED 20-SEP-2004.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: SECTION OFFICER, FINANCE DEPARTMENT,GOVERNMENT OF ODISHA ON 20-SEP-2004
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">9</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                                    4000-100-6000 
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                            PAY: Rs.4700.0/- 
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        01-SEP-2005
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> INCREMENT</u></b>  <br/>
                                                     ALLOWED  ANNUAL INCREMENT @ Rs. 100/- RAISING PAY FROM Rs. 4600/- TO Rs. 4700/- PM  IN THE SCALE OF PAY Rs. 4000-100-6000/- WEF 01-SEP-2005 VIDE FINANCE DEPARTMENT,GOVERNMENT OF ORISSA NOTIFICATION/ OFFICE ORDER NO. 43022/F DATED 05-SEP-2005.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: SECTION OFFICER, FINANCE DEPARTMENT,GOVERNMENT OF ODISHA ON 05-SEP-2005
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">10</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                                    5200-20200 
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                            PAY: Rs.8750.0/- 
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        01-JAN-2006
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> PAY FIXATION</u></b>  <br/>
                                                     PAY FIXED  @ Rs.8750/- PM WITH GRADE PAY Rs.2400/- PM  WEF 1-JAN-2006 IN THE SCALE OF PAY OF RS. 5200-20200/- VIDE FINANCE DEPARTMENT,GOVERNMENT OF ODISHA NOTIFICATION/ OFFICE ORDER NO. 187 DATED 02-JAN-2009.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: DATA PROCESSING ASSISTANT,FINANCE DEPARTMENT,GOVERNMENT OF ODISHA,ODISHA ON 06-SEP-2014
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">11</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                                    5200-20200 
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                            PAY: Rs.8750.0/- 
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        01-JAN-2006
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> PAY REVISION</u></b>  <br/>
                                                     PAY REVISED  @ Rs.8750/- PM WITH GRADE PAY Rs.2400/- PM  WEF 1-JAN-2006 IN THE REVISED SCALE OF PAY OF RS. 5200-20200/- VIDE NOTIFICATION/ OFFICE ORDER NO. 187 DATED 2-JAN-2009.  HIS  DATE OF NEXT INCREMENT WILL FALL DUE ON 01-SEP-2006. HE IS ALLOWED TO DRAW  HIS  NEXT & SUBSEQUENT INCREMENTS WEF 01-SEP-2006, 01-SEP-2007 & 01-NOV-2008 RASIING  HIS  PAY TO  RS. 9090/- PM ,  RS. 9440/- PM  &  RS. 9800/- PM  VIDE ORDER NO. 187 DATED 02-JAN-2009.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY:  ON 10-AUG-2021
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">12</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                                    4000-100-6000 
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                            PAY: Rs.4800.0/- 
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        01-SEP-2006
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> INCREMENT</u></b>  <br/>
                                                     ALLOWED  ANNUAL INCREMENT @ RS. 100/- RAISING PAY FROM RS. 4700/- TO RS. 4800/- PM 4000-100-6000 WEF 1-SEP-2006, FN  GRADE PAY RS. 20.0/- PM,  VIDE FINANCE DEPARTMENT  NOTIFICATION/ OFFICE ORDER NO. 38155/F DATED 7-SEP-2006.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: SECTION OFFICER, FINANCE DEPARTMENT,GOVERNMENT OF ODISHA ON 07-SEP-2006
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">13</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                                    4000-100-6000 
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                            PAY: Rs.4900.0/- 
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        01-SEP-2007
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> INCREMENT</u></b>  <br/>
                                                     ALLOWED  ANNUAL INCREMENT @ Rs. 100/- RAISING PAY FROM Rs. 4800/- TO Rs. 4900/- PM  IN THE SCALE OF PAY Rs. 4000-100-6000/- WEF 01-SEP-2007 VIDE FINANCE DEPARTMENT,GOVERNMENT OF ORISSA NOTIFICATION/ OFFICE ORDER NO. 36290/F DATED 01-SEP-2007.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: SECTION OFFICER, FINANCE DEPARTMENT,GOVERNMENT OF ODISHA ON 01-SEP-2007
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">14</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                                    4000-100-6000 
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                            PAY: Rs.5000.0/- 
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        01-SEP-2008
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> INCREMENT</u></b>  <br/>
                                                     ALLOWED  ANNUAL INCREMENT @ Rs. 100/- RAISING PAY FROM Rs. 4900/- TO Rs. 5000/- PM  IN THE SCALE OF PAY Rs. 4000-100-6000/- WEF 01-SEP-2008 VIDE FINANCE DEPARTMENT,GOVERNMENT OF ORISSA NOTIFICATION/ OFFICE ORDER NO. 40581/F DATED 02-SEP-2008.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: SECTION OFFICER, FINANCE DEPARTMENT,GOVERNMENT OF ODISHA ON 02-SEP-2008
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">15</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                                    5200-20200 
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                            PAY: Rs.10170.0/- 
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        01-SEP-2009
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> INCREMENT</u></b>  <br/>
                                                     ALLOWED  ANNUAL INCREMENT @ RS. 370/- RAISING PAY FROM RS. 9800/- TO RS. 10170/- PM  IN THE SCALE OF PAY RS. 5200-20200/- WEF 01-SEP-2009, AN  GRADE PAY RS. 2400.0/- PM,  VIDE FINANCE DEPARTMENT,GOVERNMENT OF ODISHA NOTIFICATION/ OFFICE ORDER NO.  45588/F DATED 10-SEP-2009.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: DATA PROCESSING ASSISTANT,FINANCE DEPARTMENT,GOVERNMENT OF ODISHA,ODISHA ON 06-SEP-2014
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">16</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                                    5200-20200 
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                            PAY: Rs.10550.0/- 
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        01-SEP-2010
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> INCREMENT</u></b>  <br/>
                                                     ALLOWED  ANNUAL INCREMENT @ RS. 380/- RAISING PAY FROM RS. 10170/- TO RS. 10550/- PM  IN THE SCALE OF PAY RS. 5200-20200/- WEF 01-SEP-2010, AN  GRADE PAY RS. 2400.0/- PM,  VIDE FINANCE DEPARTMENT,GOVERNMENT OF ODISHA NOTIFICATION/ OFFICE ORDER NO. 40170/F DATED 18-SEP-2010.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: DATA PROCESSING ASSISTANT,FINANCE DEPARTMENT,GOVERNMENT OF ODISHA,ODISHA ON 06-SEP-2014
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">17</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                                    5200-20200 
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                            PAY: Rs.10940.0/- 
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        01-SEP-2011
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> INCREMENT</u></b>  <br/>
                                                     ALLOWED  ANNUAL INCREMENT @ RS. 390/- RAISING PAY FROM RS. 10550/- TO RS. 10940/- PM  IN THE SCALE OF PAY RS. 5200-20200/- WEF 01-SEP-2011, AN  GRADE PAY RS. 2400.0/- PM,  VIDE FINANCE DEPARTMENT,GOVERNMENT OF ODISHA NOTIFICATION/ OFFICE ORDER NO. 38427/F DATED 05-SEP-2011.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: DATA PROCESSING ASSISTANT,FINANCE DEPARTMENT,GOVERNMENT OF ODISHA,ODISHA ON 06-SEP-2014
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">18</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                                    5200-20200 
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                            PAY: Rs.11340.0/- 
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        01-SEP-2012
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> INCREMENT</u></b>  <br/>
                                                     ALLOWED  ANNUAL INCREMENT @ RS. 400/- RAISING PAY FROM RS. 10940/- TO RS. 11340/- PM  IN THE SCALE OF PAY RS. 5200-20200/- WEF 01-SEP-2012  GRADE PAY RS. 2400.0/- PM,  VIDE FINANCE DEPARTMENT,GOVERNMENT OF ODISHA NOTIFICATION/ OFFICE ORDER NO. 32236/F DATED 10-SEP-2012.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: PRINCIPAL SECRETARY, FINANCE DEPARTMENT,GOVERNMENT OF ODISHA ON 11-SEP-2012
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">19</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                                    5200-20200 
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                            PAY: Rs.11760.0/- 
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        01-SEP-2013
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> INCREMENT</u></b>  <br/>
                                                     ALLOWED  ANNUAL INCREMENT @ RS. 420/- RAISING PAY FROM RS. 11340/- TO RS. 11760/- PM  IN THE SCALE OF PAY RS. 5200-20200/- WEF 01-SEP-2013  GRADE PAY RS. 2400.0/- PM,  VIDE FINANCE DEPARTMENT,GOVERNMENT OF ODISHA NOTIFICATION/ OFFICE ORDER NO. 29100/F DATED 12-SEP-2013.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: DATA PROCESSING ASSISTANT,FINANCE DEPARTMENT,GOVERNMENT OF ODISHA,ODISHA ON 23-SEP-2013
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">20</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                                    5200-20200 
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                            PAY: Rs.12190.0/- 
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        01-SEP-2014
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> INCREMENT</u></b>  <br/>
                                                     ALLOWED  ANNUAL INCREMENT @ RS. 430/- RAISING PAY FROM RS. 11760/- TO RS. 12190/- PM  IN THE SCALE OF PAY RS. 5200-20200/- WEF 01-SEP-2014, FN  GRADE PAY RS. 2400.0/- PM,  VIDE FINANCE DEPARTMENT,GOVERNMENT OF ODISHA NOTIFICATION/ OFFICE ORDER NO. 26075/F DATED 8-SEP-2014.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: DATA PROCESSING ASSISTANT,FINANCE DEPARTMENT,GOVERNMENT OF ODISHA,ODISHA ON 10-SEP-2014
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">21</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                 SENIOR DATA ENTRY OPERATOR </br>   
                                                
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        01-MAR-2015
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> JOINING</u></b>  <br/>
                                                    IN PURSUANCE OF FINANCE DEPARTMENT,GOVERNMENT OF ODISHA  NOTIFICATION NO. 5356/F DATED 07-MAR-2015,  JOINED AS SENIOR DATA ENTRY OPERATOR,FINANCE DEPARTMENT,GOVERNMENT OF ODISHA,ODISHA ON 01-MAR-2015(FN). 
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: SENIOR DATA ENTRY OPERATOR,FINANCE DEPARTMENT,GOVERNMENT OF ODISHA,ODISHA ON 23-MAR-2015
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">22</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                                    5200-20200 
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                            PAY: Rs.16050.0/- 
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        01-MAY-2015
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> PAY FIXATION</u></b>  <br/>
                                                     PAY FIXED  @ Rs.16050/- PM WITH GRADE PAY Rs.2400/- PM  WEF 1-MAY-2015 IN THE SCALE OF PAY OF RS. 5200-20200/-  HAVING REASON  VIDE FINANCE DEPARTMENT,GOVERNMENT OF ODISHA NOTIFICATION/ OFFICE ORDER NO. 14354 DATED 13-MAY-2015.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: UNDER SECRETARY,FINANCE DEPARTMENT,GOVERNMENT OF ODISHA,ODISHA ON 28-MAY-2015
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">23</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                            PAY: Rs.41500.0/- 
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        01-JAN-2016
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> PAY REVISION</u></b>  <br/>
                                                     PAY REVISED  @ Rs.41500/- PM  WEF 1-JAN-2016 VIDE NOTIFICATION/ OFFICE ORDER NO. 35421 DATED 1-DEC-2017.  HIS  DATE OF NEXT INCREMENT WILL FALL DUE ON 01-JAN-2018. HE IS ALLOWED TO DRAW  HIS  NEXT INCREMENT WEF 01-JAN-2017 RAISING PAY TO Rs. 42200/-  IN CELL null OF LEVEL null  VIDE ORDER NO. 35421 DATED 01-DEC-2017.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY:  ON 10-AUG-2021
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">24</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                                    IN CELL 11 OF LEVEL 7 
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                            PAY: Rs.52000.0/- 
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        01-JAN-2016
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> PAY REVISION</u></b>  <br/>
                                                     PAY FIXED  @ Rs.52000/- PM  WEF 1-JAN-2016 IN CELL 11 OF LEVEL 7  VIDE NOTIFICATION/ OFFICE ORDER NO. 01 DATED 14-SEP-2023.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: ASSISTANT DATA PROCESSING OFFICER, FINANCE DEPARTMENT,GOVERNMENT OF ODISHA, ODISHA ON 14-SEP-2023
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">25</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                                    5200-20200 
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                            PAY: Rs.13110.0/- 
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        01-JAN-2016
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> INCREMENT</u></b>  <br/>
                                                     ALLOWED  ANNUAL INCREMENT @ RS. 60/- RAISING PAY FROM RS. 13050/- TO RS. 13110/- PM  IN THE SCALE OF PAY RS. 5200-20200/- WEF 01-JAN-2016, FN  GRADE PAY RS. 2400.0/- PM,  VIDE FINANCE DEPARTMENT,GOVERNMENT OF ODISHA NOTIFICATION/ OFFICE ORDER NO. 297/F DATED 5-JAN-2016.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: SENIOR DATA ENTRY OPERATOR,FINANCE DEPARTMENT,GOVERNMENT OF ODISHA,ODISHA ON 14-JAN-2016
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">26</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                                    5200-20200 
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                            PAY: Rs.13580.0/- 
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        01-JAN-2017
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> INCREMENT</u></b>  <br/>
                                                     ALLOWED  ANNUAL INCREMENT @ RS. 470/- RAISING PAY FROM RS. 13110/- TO RS. 13580/- PM  IN THE SCALE OF PAY RS. 5200-20200/- WEF 01-JAN-2017, FN  GRADE PAY RS. 2400.0/- PM,  VIDE FINANCE DEPARTMENT,GOVERNMENT OF ODISHA NOTIFICATION/ OFFICE ORDER NO. 235/F DATED 5-JAN-2017.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: SENIOR DATA ENTRY OPERATOR,FINANCE DEPARTMENT,GOVERNMENT OF ODISHA,ODISHA ON 12-JAN-2017
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">27</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                                    5200-20200 
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                            PAY: Rs.43500.0/- 
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        01-JAN-2018
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> INCREMENT</u></b>  <br/>
                                                     ALLOWED  ANNUAL INCREMENT  WEF 01-JAN-2018, FN VIDE FINANCE DEPARTMENT,GOVERNMENT OF ODISHA NOTIFICATION/ OFFICE ORDER NO. 438/F DATED 5-JAN-2018.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: SENIOR DATA ENTRY OPERATOR,FINANCE DEPARTMENT,GOVERNMENT OF ODISHA,ODISHA ON 15-JAN-2018
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">28</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                            PAY: Rs.44800.0/- 
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        01-JAN-2019
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> INCREMENT</u></b>  <br/>
                                                     ALLOWED  ANNUAL INCREMENT @ RS. 1300/- RAISING PAY FROM RS. 43500/- TO RS. 44800/- PM  IN THE SCALE OF PAY RS.   WEF 1-JAN-2019, FN VIDE NOTIFICATION/ OFFICE ORDER NO. 443/F DATED 4-JAN-2019.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: SENIOR DATA ENTRY OPERATOR,FINANCE DEPARTMENT,GOVERNMENT OF ODISHA,ODISHA ON 16-JAN-2019
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">29</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                            PAY: Rs.48200.0/- 
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        01-JAN-2020
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> INCREMENT</u></b>  <br/>
                                                     ALLOWED @ RS. 1400/- RAISING PAY FROM RS. 46800/- TO RS. 48200/- PM  WEF 1-JAN-2020, FN VIDE NOTIFICATION/ OFFICE ORDER NO. 693/F DATED 6-JAN-2020.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: SENIOR DATA ENTRY OPERATOR,FINANCE DEPARTMENT,GOVERNMENT OF ODISHA,ODISHA ON 09-JAN-2020
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">30</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                                    IN CELL 19 OF LEVEL 8 
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                            PAY: Rs.49600.0/- 
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        01-JAN-2021
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> INCREMENT</u></b>  <br/>
                                                     ALLOWED @ RS. 1400/- RAISING PAY FROM RS. 48200/- TO RS. 49600/- PM  IN CELL 19 OF LEVEL 8  WEF 1-JAN-2021, FN VIDE NOTIFICATION/ OFFICE ORDER NO. 1289/F DATED 12-JAN-2021.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: SENIOR DATA ENTRY OPERATOR,FINANCE DEPARTMENT,GOVERNMENT OF ODISHA,ODISHA ON 12-JAN-2021
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">31</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                                    IN CELL 20 OF LEVEL 8 
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                            PAY: Rs.51100.0/- 
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        01-JAN-2022
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> INCREMENT</u></b>  <br/>
                                                     ALLOWED  ANNUAL INCREMENT @ RS. 1500/- RAISING PAY FROM RS. 49600/- TO RS. 51100/- PM  IN CELL 20 OF LEVEL 8  WEF 1-JAN-2022, FN VIDE NOTIFICATION/ OFFICE ORDER NO. 309/F DATED 5-JAN-2022.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: SENIOR DATA ENTRY OPERATOR,FINANCE DEPARTMENT,GOVERNMENT OF ODISHA,ODISHA ON 06-JAN-2022
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">32</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                 ASSISTANT DATA PROCESSING OFFICER </br>   
                                                
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        29-MAR-2022(AN)
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> JOINING</u></b>  <br/>
                                                    IN PURSUANCE OF NOTIFICATION NO. 8158/F DATED 02-APR-2022,  JOINED AS ASSISTANT DATA PROCESSING OFFICER, FINANCE DEPARTMENT,GOVERNMENT OF ODISHA, ODISHA ON 29-MAR-2022(AN).
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: SENIOR DATA ENTRY OPERATOR,FINANCE DEPARTMENT,GOVERNMENT OF ODISHA,ODISHA ON 12-APR-2022
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">33</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        16-AUG-2023
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> REGULAR RECRUITMENT DETAILS</u></b>  <br/>
                                                     APPOINTED  AS ACCOUNTANT THROUGH  REGULAR RECRUITMENT  WEF  16-AUG-2023(FN)  WITH PAY @ RS. 5/- PM  WEF 16-AUG-2023 (FN) AND JOINED CADRE VIDE NOTIFICATION/ OFFICE ORDER NO. 42196/F DATED 26-SEP-1998.
                                            &nbsp;	

                                            
                                                <br/>
                                                <p style="font-style: italic;">
                                                    <b>Note:</b> APPOINTED AGAINST THE TEMPORARY POST OF DATA PROCESSING ASSISTANT (JR.GR.) WITH THE STARTING PAY OF RS-4000/- IN THE SCALE OF PAY RS-4000-6000/-WEF 26-9-1998 FN CREATED IN F.D
                                                </p>

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY:  ON 26-SEP-1998
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">34</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        24-AUG-2023
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> REGULAR RECRUITMENT DETAILS</u></b>  <br/>
                                                     APPOINTED  THROUGH  REGULAR RECRUITMENT  WEF  24-AUG-2023(FN)  WITH PAY @ RS. 356/- PM  WEF 24-AUG-2023 (FN) AND JOINED CADRE VIDE NOTIFICATION/ OFFICE ORDER NO. DV DATED 08-AUG-2023.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY:  ON 18-AUG-2023
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">35</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        24-AUG-2023
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> REGULAR RECRUITMENT DETAILS</u></b>  <br/>
                                                     APPOINTED  THROUGH  REGULAR RECRUITMENT  WEF  24-AUG-2023(FN)  WITH PAY @ RS. 356/- PM  + GRADE PAY RS. 2344/- PM  WEF 24-AUG-2023 (FN) AND JOINED CADRE VIDE NOTIFICATION/ OFFICE ORDER NO. DV DATED 08-AUG-2023.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY:  ON 18-AUG-2023
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">36</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                                    IN CELL 2 OF LEVEL 2 
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                            PAY: Rs.2.0/- 
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        01-SEP-2023
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> PAY FIXATION</u></b>  <br/>
                                                     PAY FIXED  @ Rs.2/- PM  WEF 1-SEP-2023 IN CELL 2 OF LEVEL 2  VIDE NOTIFICATION/ OFFICE ORDER NO. 1 DATED 1-SEP-2023.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: ASSISTANT DATA PROCESSING OFFICER, FINANCE DEPARTMENT,GOVERNMENT OF ODISHA, ODISHA ON 13-SEP-2023
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">37</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                                    IN CELL 1 OF LEVEL 1 
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                            PAY: Rs.8000.0/- 
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        01-SEP-2023
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> INCREMENT</u></b>  <br/>
                                                     ALLOWED  ANNUAL INCREMENT @ RS. 500/- RAISING PAY FROM RS. 7500/- TO RS. 8000/- PM  IN CELL 1 OF LEVEL 1  WEF 1-SEP-2023, FN VIDE NOTIFICATION/ OFFICE ORDER NO. 12 DATED 31-AUG-2023.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: ASSISTANT DATA PROCESSING OFFICER, FINANCE DEPARTMENT,GOVERNMENT OF ODISHA, ODISHA ON 13-SEP-2023
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">38</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                                    IN CELL 16 OF LEVEL 15 
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                            PAY: Rs.4000.0/- 
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        01-SEP-2023
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> INCREMENT</u></b>  <br/>
                                                     ALLOWED  ANNUAL INCREMENT @ RS. 150/- RAISING PAY FROM RS. 3850/- TO RS. 4000/- PM  IN CELL 57 OF LEVEL 11  WEF 1-SEP-2023, FN VIDE FISHERIES AND ANIMAL RESOURCES DEVELOPMENT DEPARTMENT  NOTIFICATION/ OFFICE ORDER NO. 111 DATED 21-SEP-2023.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: ASSISTANT DATA PROCESSING OFFICER, FINANCE DEPARTMENT,GOVERNMENT OF ODISHA, ODISHA ON 13-SEP-2023
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">39</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                                    12750-375-16500 
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                            PAY: Rs.50000.0/- 
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        14-SEP-2023
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> INCREMENT</u></b>  <br/>
                                                     ALLOWED  ANTEDATED INCREMENT @ RS. 1100/- RAISING PAY FROM RS. 48900/- TO RS. 50000/- PM  IN THE SCALE OF PAY RS. 12750-375-16500  WEF 14-SEP-2023, FN  GRADE PAY RS. 50.0/- PM,  VIDE NOTIFICATION/ OFFICE ORDER NO. 111 DATED 20-SEP-2023.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: ASSISTANT DATA PROCESSING OFFICER, FINANCE DEPARTMENT,GOVERNMENT OF ODISHA, ODISHA ON 07-SEP-2023
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">40</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                                    12850-300-13150-350-15950-400-17550 
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                            PAY: Rs.1500.0/- 
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        21-SEP-2023
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> PAY FIXATION</u></b>  <br/>
                                                     PAY FIXED  @ Rs.1500/- PM WITH GRADE PAY Rs.70/- PM  WEF 21-SEP-2023 IN THE SCALE OF PAY OF RS. 12850-300-13150-350-15950-400-17550/- VIDE NOTIFICATION/ OFFICE ORDER NO. 141 DATED 13-SEP-2023.  HIS  DATE OF NEXT INCREMENT WILL FALL DUE ON 26-SEP-2023.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: ASSISTANT DATA PROCESSING OFFICER, FINANCE DEPARTMENT,GOVERNMENT OF ODISHA, ODISHA ON 14-SEP-2023
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">41</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                                    IN CELL 16 OF LEVEL 17 
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                            PAY: Rs.2200.0/- 
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        27-SEP-2023
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> PAY REVISION</u></b>  <br/>
                                                     PAY REVISED  @ Rs.2200/- PM  WEF 27-SEP-2023 IN CELL 16 OF LEVEL 17  + SPECIAL PAY @ RS. 2/-  PM  VIDE NOTIFICATION/ OFFICE ORDER NO. 151 DATED 21-SEP-2023.  HIS  DATE OF NEXT INCREMENT WILL FALL DUE ON 25-SEP-2023.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: ASSISTANT DATA PROCESSING OFFICER, FINANCE DEPARTMENT,GOVERNMENT OF ODISHA, ODISHA ON 12-SEP-2023
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">42</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                                    12750-375-16500 
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                            PAY: Rs.2100.0/- 
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        27-SEP-2023
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> PAY REVISION</u></b>  <br/>
                                                     PAY FIXED  @ Rs.2100/- PM WITH GRADE PAY Rs.25/- PM  WEF 27-SEP-2023 IN THE SCALE OF PAY OF RS. 12750-375-16500/- VIDE NOTIFICATION/ OFFICE ORDER NO. 1611 DATED 20-SEP-2023.  HIS  DATE OF NEXT INCREMENT WILL FALL DUE ON 29-SEP-2023.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: ASSISTANT DATA PROCESSING OFFICER, FINANCE DEPARTMENT,GOVERNMENT OF ODISHA, ODISHA ON 14-SEP-2023
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">43</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                                    IN CELL 11 OF LEVEL 15 
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                            PAY: Rs.50001.0/- 
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        28-SEP-2023
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> INCREMENT</u></b>  <br/>
                                                     ALLOWED  STAGNATION INCREMENT @ RS. 1100/- RAISING PAY FROM RS. 48901/- TO RS. 50001/- PM  IN CELL 11 OF LEVEL 15  WEF 28-SEP-2023, FN VIDE HIGHER EDUCATION DEPARTMENT  NOTIFICATION/ OFFICE ORDER NO. 111 DATED 21-SEP-2023.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: ASSISTANT DATA PROCESSING OFFICER, FINANCE DEPARTMENT,GOVERNMENT OF ODISHA, ODISHA ON 14-SEP-2023
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">44</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> LTC</u></b>  <br/>
                                                    ALLOWED TO AVAIL THE BENEFIT OF ALL INDIA LTC DURING THE BLOCK PERIOD COMMENCING FROM 2019 TO 2019 FROM 26-Feb-2019 TO 11-Mar-2019  VIDE FINANCE DEPARTMENT,GOVERNMENT OF ODISHA NOTIFICATION/ OFFICE ORDER NO. 275 DATED 3-Jan-2019.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY:  ON 12-MAY-2021
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">45</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> QUARTER ALLOTMENT</u></b>  <br/>
                                                    QUARTER ALLOTTED NO. 1/1 F-6 ON 31-MAR-2012 AND POSSESSED ON 31-MAR-2012 WITH MONTHLY RENT 331 AND WATER RENT 50 VIDE NOTIFICATION/ OFFICE ORDER NO.1 DATED 31-MAR-2012.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: DATA PROCESSING ASSISTANT,FINANCE DEPARTMENT,GOVERNMENT OF ODISHA,ODISHA ON 21-APR-2012
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">46</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> REDESIGNATION</u></b>  <br/>
                                                    REDESIGNATED AS SENIOR DATA ENTRY OPERATOR,FINANCE DEPARTMENT,GOVERNMENT OF ODISHA,ODISHA IN PURSUANCE OF FINANCE DEPARTMENT,GOVERNMENT OF ODISHA NOTIFICATION/ OFFICE ORDER NO. 5356/F DATED 07-MAR-2015  WITH PAY @ RS. 12190/- PM  IN THE SCALE OF PAY RS. 5200-20200/- + GRADE PAY RS. 2400/- PM  WEF 01-MAR-2015 (FN).
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: DATA PROCESSING ASSISTANT,FINANCE DEPARTMENT,GOVERNMENT OF ODISHA,ODISHA ON 21-MAR-2015
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">47</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> PROMOTION</u></b>  <br/>
                                                    .  ALLOWED TO DRAW  PAY @ RS. 52000/- PM  IN CELL 14 OF LEVEL 9  WEF 23-FEB-2023 (AN).
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY:  ON 12-APR-2022
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">48</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> JOINING IN CADRE</u></b>  <br/>
                                                     JOINED  FROM DEO CADRE OF  VIDE NOTIFICATION/ OFFICE ORDER NO. 8158/F DATED 02-APR-2022.  ALLOWED TO DRAW  PAY @ RS. 52000/- PM  PM  WEF 29-MAR-2022 (AN).
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY:  ON 12-APR-2022
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">49</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> PAY FIXATION</u></b>  <br/>
                                                     PAY FIXED  @ Rs.12000/- PM  IN CELL 12 OF LEVEL 11  VIDE NOTIFICATION/ OFFICE ORDER NO. 10 DATED 13-SEP-2023.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: ASSISTANT DATA PROCESSING OFFICER, FINANCE DEPARTMENT,GOVERNMENT OF ODISHA, ODISHA ON 13-SEP-2023
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">50</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> LOAN SANCTION</u></b>  <br/>
                                                    SANCTIONED MOTOR CYCLE  ADVANCE OF RS. 10160/-  VIDE FINANCE DEPARTMENT,GOVERNMENT OF ODISHA ORDER
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY:  ON 26-NOV-2013
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">51</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> LOAN SANCTION</u></b>  <br/>
                                                    SANCTIONED SPECIAL HOUSE BUILDING ADVANCE OF RS. 50000/-  VIDE FINANCE DEPARTMENT,GOVERNMENT OF ORISSA NOTIFICATION/ OFFICE ORDER NO. 7453/F DATED 18-FEB-2000
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: SECTION OFFICER, FINANCE DEPARTMENT,GOVERNMENT OF ODISHA ON 18-FEB-2000
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">52</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> LOAN SANCTION</u></b>  <br/>
                                                    SANCTIONED COMPUTER ADVANCE OF RS. 10000/-  VIDE FINANCE DEPARTMENT,GOVERNMENT OF ODISHA ORDER
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: SENIOR DATA ENTRY OPERATOR,FINANCE DEPARTMENT,GOVERNMENT OF ODISHA,ODISHA ON 18-JAN-2018
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">53</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> LOAN SANCTION</u></b>  <br/>
                                                    SANCTIONED COMPUTER ADVANCE OF RS. 40000/-  VIDE NOTIFICATION/ OFFICE ORDER NO. 202019198164 DATED 17-FEB-2021
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY:  ON 22-FEB-2021
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">54</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> SERVICE VERIFICATION CERTIFICATE</u></b>  <br/>
                                                    SERVICES VERIFIED  FROM 01-APR-2000 TO 31-MAR-2002 WITH REFERENCE TO PAY ACQUITTANCE ROLL/ ESTABLISHMENT PAY BILLS AND OTHER CONNECTED PAPERS AND FOUND CORRECT  BY UNDER SECRETARY, FINANCE DEPARTMENT,GOVERNMENT OF ORISSA, BHUBANESWAR.
                                            &nbsp;	

                                            
                                                <br/>
                                                <p style="font-style: italic;">
                                                    <b>Note:</b> RECORDS
                                                </p>

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: UNDER SECRETARY, FINANCE DEPARTMENT,GOVERNMENT OF ODISHA ON 31-MAR-2002
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">55</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> SERVICE VERIFICATION CERTIFICATE</u></b>  <br/>
                                                    SERVICES VERIFIED  FROM 01-APR-2002 TO 31-MAR-2003 WITH REFERENCE TO PAY ACQUITTANCE ROLL/ ESTABLISHMENT PAY BILLS AND OTHER CONNECTED PAPERS AND FOUND CORRECT  BY UNDER SECRETARY, FINANCE DEPARTMENT,GOVERNMENT OF ORISSA, BHUBANESWAR.
                                            &nbsp;	

                                            
                                                <br/>
                                                <p style="font-style: italic;">
                                                    <b>Note:</b> RECORDS
                                                </p>

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: UNDER SECRETARY, FINANCE DEPARTMENT,GOVERNMENT OF ODISHA ON 31-MAR-2003
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">56</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> SERVICE VERIFICATION CERTIFICATE</u></b>  <br/>
                                                    SERVICES VERIFIED  FROM 01-APR-2003 TO 31-MAR-2004 WITH REFERENCE TO PAY ACQUITTANCE ROLL/ ESTABLISHMENT PAY BILLS AND OTHER CONNECTED PAPERS AND FOUND CORRECT  BY UNDER SECRETARY, FINANCE DEPARTMENT,GOVERNMENT OF ORISSA, BHUBANESWAR.
                                            &nbsp;	

                                            
                                                <br/>
                                                <p style="font-style: italic;">
                                                    <b>Note:</b> RECORDS
                                                </p>

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: UNDER SECRETARY, FINANCE DEPARTMENT,GOVERNMENT OF ODISHA ON 31-MAR-2004
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">57</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> SERVICE VERIFICATION CERTIFICATE</u></b>  <br/>
                                                    SERVICES VERIFIED  FROM 01-APR-2004 TO 31-MAR-2005 WITH REFERENCE TO PAY ACQUITTANCE ROLL/ ESTABLISHMENT PAY BILLS AND OTHER CONNECTED PAPERS AND FOUND CORRECT  BY UNDER SECRETARY, FINANCE DEPARTMENT,GOVERNMENT OF ORISSA, BHUBANESWAR.
                                            &nbsp;	

                                            
                                                <br/>
                                                <p style="font-style: italic;">
                                                    <b>Note:</b> RECORDS
                                                </p>

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: UNDER SECRETARY, FINANCE DEPARTMENT,GOVERNMENT OF ODISHA ON 31-MAR-2005
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">58</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> SERVICE VERIFICATION CERTIFICATE</u></b>  <br/>
                                                    SERVICES VERIFIED  FROM 01-APR-2005 TO 31-MAR-2006 WITH REFERENCE TO PAY ACQUITTANCE ROLL/ ESTABLISHMENT PAY BILLS AND OTHER CONNECTED PAPERS AND FOUND CORRECT  BY UNDER SECRETARY, FINANCE DEPARTMENT,GOVERNMENT OF ORISSA, BHUBANESWAR.
                                            &nbsp;	

                                            
                                                <br/>
                                                <p style="font-style: italic;">
                                                    <b>Note:</b> RECORDS
                                                </p>

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: UNDER SECRETARY, FINANCE DEPARTMENT,GOVERNMENT OF ODISHA ON 31-MAR-2006
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">59</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> SERVICE VERIFICATION CERTIFICATE</u></b>  <br/>
                                                    SERVICES VERIFIED  FROM 01-APR-2006 TO 31-MAR-2007 WITH REFERENCE TO PAY ACQUITTANCE ROLL/ ESTABLISHMENT PAY BILLS AND OTHER CONNECTED PAPERS AND FOUND CORRECT  BY UNDER SECRETARY, FINANCE DEPARTMENT,GOVERNMENT OF ORISSA, BHUBANESWAR.
                                            &nbsp;	

                                            
                                                <br/>
                                                <p style="font-style: italic;">
                                                    <b>Note:</b> RECORDS
                                                </p>

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: UNDER SECRETARY, FINANCE DEPARTMENT,GOVERNMENT OF ODISHA ON 31-MAR-2007
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">60</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> SERVICE VERIFICATION CERTIFICATE</u></b>  <br/>
                                                    SERVICES VERIFIED  FROM 01-APR-2007 TO 31-MAR-2008 WITH REFERENCE TO PAY ACQUITTANCE ROLL/ ESTABLISHMENT PAY BILLS AND OTHER CONNECTED PAPERS AND FOUND CORRECT  BY UNDER SECRETARY, FINANCE DEPARTMENT,GOVERNMENT OF ORISSA, BHUBANESWAR.
                                            &nbsp;	

                                            
                                                <br/>
                                                <p style="font-style: italic;">
                                                    <b>Note:</b> RECORDS
                                                </p>

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: UNDER SECRETARY, FINANCE DEPARTMENT,GOVERNMENT OF ODISHA ON 31-MAR-2008
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">61</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> SERVICE VERIFICATION CERTIFICATE</u></b>  <br/>
                                                    SERVICES VERIFIED FROM 1-Apr-2008 TO 31-Mar-2009 WITH REFERENCE 
TO PAY ACQUITTANCE ROLL/ ESTABLISHMENT PAY BILLS AND OTHER CONNECTED PAPERS AND FOUND CORRECT BY UNDER SECRETARY,FINANCE DEPARTMENT,GOVERNMENT OF ODISHA,ODISHA
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: UNDER SECRETARY,FINANCE DEPARTMENT,GOVERNMENT OF ODISHA,ODISHA ON 12-MAY-2021
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">62</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> SERVICE VERIFICATION CERTIFICATE</u></b>  <br/>
                                                    SERVICES VERIFIED FROM 1-Apr-2009 TO 31-Mar-2010 WITH REFERENCE 
TO PAY ACQUITTANCE ROLL/ ESTABLISHMENT PAY BILLS AND OTHER CONNECTED PAPERS AND FOUND CORRECT BY UNDER SECRETARY,FINANCE DEPARTMENT,GOVERNMENT OF ODISHA,ODISHA
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: UNDER SECRETARY,FINANCE DEPARTMENT,GOVERNMENT OF ODISHA,ODISHA ON 12-MAY-2021
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">63</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> SERVICE VERIFICATION CERTIFICATE</u></b>  <br/>
                                                    SERVICES VERIFIED FROM 1-Apr-2011 TO 28-Feb-2014 WITH REFERENCE 
TO PAY ACQUITTANCE ROLL/ ESTABLISHMENT PAY BILLS AND OTHER CONNECTED PAPERS AND FOUND CORRECT BY UNDER SECRETARY,FINANCE DEPARTMENT,GOVERNMENT OF ODISHA,ODISHA
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: UNDER SECRETARY,FINANCE DEPARTMENT,GOVERNMENT OF ODISHA,ODISHA ON 12-MAY-2021
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">64</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> SERVICE VERIFICATION CERTIFICATE</u></b>  <br/>
                                                    SERVICES VERIFIED FROM 1-Mar-2014 TO 28-Feb-2017 WITH REFERENCE 
TO PAY ACQUITTANCE ROLL/ ESTABLISHMENT PAY BILLS AND OTHER CONNECTED PAPERS AND FOUND CORRECT BY UNDER SECRETARY,FINANCE DEPARTMENT,GOVERNMENT OF ODISHA,ODISHA
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: UNDER SECRETARY,FINANCE DEPARTMENT,GOVERNMENT OF ODISHA,ODISHA ON 12-MAY-2021
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">65</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> SERVICE VERIFICATION CERTIFICATE</u></b>  <br/>
                                                    SERVICES VERIFIED ON 16-Dec-2011 FROM 8-Apr-2010 TO 31-Mar-2011 WITH REFERENCE 
TO PAY ACQUITTANCE ROLL/ ESTABLISHMENT PAY BILLS AND OTHER CONNECTED PAPERS AND FOUND CORRECT BY UNDER SECRETARY,FINANCE DEPARTMENT,GOVERNMENT OF ODISHA,ODISHA
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: UNDER SECRETARY,FINANCE DEPARTMENT,GOVERNMENT OF ODISHA,ODISHA ON 12-MAY-2021
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">66</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> SERVICE VERIFICATION CERTIFICATE</u></b>  <br/>
                                                    SERVICES VERIFIED ON 29-Apr-2022 FROM 1-Mar-2017 TO 31-Mar-2022 WITH REFERENCE 
TO PAY ACQUITTANCE ROLL/ ESTABLISHMENT PAY BILLS AND OTHER CONNECTED PAPERS AND FOUND CORRECT BY UNDER SECRETARY,FINANCE DEPARTMENT,GOVERNMENT OF ODISHA,ODISHA
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: UNDER SECRETARY,FINANCE DEPARTMENT,GOVERNMENT OF ODISHA,ODISHA ON 09-MAY-2022
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">67</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> SERVICE VERIFICATION CERTIFICATE</u></b>  <br/>
                                                    SERVICES VERIFIED ON 31-Mar-2000 FROM 21-Oct-1998 TO 31-Mar-2000 WITH REFERENCE 
TO PAY ACQUITTANCE ROLL/ ESTABLISHMENT PAY BILLS AND OTHER CONNECTED PAPERS AND FOUND CORRECT BY UNDER SECRETARY, FINANCE DEPARTMENT,GOVERNMENT OF ODISHA
                                            &nbsp;	

                                            
                                                <br/>
                                                <p style="font-style: italic;">
                                                    <b>Note:</b> RECORDS
                                                </p>

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: UNDER SECRETARY, FINANCE DEPARTMENT,GOVERNMENT OF ODISHA ON 31-MAR-2000
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">68</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> SERVICE VERIFICATION CERTIFICATE</u></b>  <br/>
                                                    SERVICES VERIFIED ON 13-Oct-2023 FROM 5-Oct-2023 TO 6-Oct-2023 WITH REFERENCE 
TO PAY ACQUITTANCE ROLL/ ESTABLISHMENT PAY BILLS AND OTHER CONNECTED PAPERS AND FOUND CORRECT BY DISTRICT TUBERCULOSIS   OFFICER,FAMILY WELFARE AND IMMUNISATION,KHORDHA
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: DISTRICT TUBERCULOSIS   OFFICER,FAMILY WELFARE AND IMMUNISATION,KHORDHA ON 13-OCT-2023
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">69</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> RELIEVE</u></b>  <br/>
                                                     IN PURSUANCE OF FINANCE DEPARTMENT,GOVERNMENT OF ODISHA  NOTIFICATION NO. 5356/F DATED 07-MAR-2015,  RELIEVED FROM  ON 07-MAR-2015(FN), IN ORDER TO JOIN AS DATA PROCESSING ASSISTANT,FINANCE DEPARTMENT,GOVERNMENT OF ODISHA,ODISHA.
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: DATA PROCESSING ASSISTANT,FINANCE DEPARTMENT,GOVERNMENT OF ODISHA,ODISHA ON 23-MAR-2015
                                    </td>
                                </tr>
                            
                                <tr height="355px">
                                    <td width="300" height="355" align="left" style="font-size:18px;padding-left:5px;" class="printDataInner">
                                        <div style="font-family:Arial,Helvetica,sans-serif;font-size:10px;">70</div>
                                        <div style="height:340px;display: table;">
                                            <span style="vertical-align:middle;display: table-cell;">
                                                  
                                                
                                                
                                            </span>
                                        </div>		
                                    </td>
                                    <td width="120" height="355" align="left" style="font-size:18px;" class="printDataInner">
                                        
                                    </td>
                                    <td width="90" height="355" align="left" style="font-size:18px;" class="printDataInner">                                    
                                        
                                    </td>
                                    <td width="450" align="left" style="font-size:18px;text-transform: none;border-right:1px solid #666666;" id="serviceHistory2" class="printDataInner">
                                        <div style="word-wrap: break-word;width: 450px;padding: 5px;">
                                            <b><u> RELIEVE</u></b>  <br/>
                                                     IN PURSUANCE OF NOTIFICATION NO. 8158/F DATED 02-APR-2022,  RELIEVED FROM  SENIOR DATA ENTRY OPERATOR,FINANCE DEPARTMENT,GOVERNMENT OF ODISHA,ODISHA ON 29-MAR-2022(AN).
                                            &nbsp;	

                                            
                                        </div>
                                    </td>
                                </tr>
                                <tr height="10px">
                                    <td align="right" style="font-size:10px;text-transform: uppercase;" colspan="4">
                                        &nbsp;ENTRY TAKEN BY: SENIOR DATA ENTRY OPERATOR,FINANCE DEPARTMENT,GOVERNMENT OF ODISHA,ODISHA ON 12-APR-2022
                                    </td>
                                </tr>
                            
                        </table>
                    </div>
                </div>
            </div>
        </form>
    </body>
</html>
