
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:HRMS:</title>
        
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css"/>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css"/>
        <link rel="stylesheet" type="text/css" href="css/hrmis.css" />

        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script language="javascript" src="js/jquery.datetimepicker.js" type="text/javascript"></script>
        <link href="css/jquery.datetimepicker.css" rel="stylesheet" type="text/css" />
        
        <script type="text/javascript">
            $(document).ready(function(){
                $('.txtDate').datetimepicker({
                    timepicker: false,
                    format: 'd-M-Y',
                    closeOnDateSelect: true,
                    validateOnBlur: false
                });
            });
            
            function saveAnnex1(){
                var dataString="";
                var charges=$("#annx1ChargeDtls").val();
                $('.annxCharge').each(function(){
                    if(dataString == ""){
                        dataString = $(this).val();
                    }else{
                        dataString = dataString+"~"+$(this).val();
                    }
                });
                $('#hidAnnex1Charges').val(dataString);
                
                var charges=$("#annex1Charge").val();
                if (charges == "" || charges.charAt(0)== "")
                {
                    alert("Please Enter Charges.");
                    $("#annex1Charge").blur();
                    return false;
                }    
                var chargeDtls=$("#annx1ChargeDtls").val();
                if (chargeDtls == "" || chargeDtls.charAt(0)== "")
                {
                    alert("Please Enter Charge Details.");
                    return false;
                }
                
            }
            
        </script>
        <style type="text/css">
            body{
                font-family: Verdana;
                font-size:16px;
            }
        </style>
    </head>
    <body>
       <!-- <table style="font-family:Verdana;margin-top: 5px;" width="99%" cellpadding="0" cellspacing="0">
            <tr>
                <td style="width:24%;">
                    <div style="border-width:1px;width:99%;color:#333;border-color:#d7d7d7;border-style:solid; border-width:1px 1px 1px 0;text-align:center;">
                        MEMORANDUM
                    </div>
                </td>
                <td style="width:24%;">
                    <div style="border-width:1px;width:99%;font-weight:bold;color:#333;border-color:#d7d7d7;border-style:solid; border-width:1px 1px 0 1px;text-align:center;">
                        ANNEXURE-I
                   </div>
                </td>
                <td style="width:24%;">
                    <div style="border-width:1px;width:99%;color:#333;border-color:#d7d7d7;border-style:solid; border-width:1px 1px 1px 0;text-align:center;">
                        ANNEXURE-II
                    </div>
                </td>
                <td style="width:24%;">
                    <div style="border-width:1px;width:99%;color:#333;border-color:#d7d7d7;border-style:solid; border-width:1px 1px 1px 0;text-align:center;">
                        ANNEXURE-III
                    </div>
                </td>
            </tr>
        </table>
        -->
        
        
        <form action="saveRule15Anex1.htm" method="POST" commandName="rule15Form">
            
            <input type="hidden" name="hidAnnx1HrmsId" id="hidAnnx1HrmsId" value='${Annex1Value.empHrmsId}'/>
            <input type="hidden" name="hidDaId" id="hidDaId" value='${DAID}'/>
            <input type="hidden" name="hidAnnex1Charges" id="hidAnnex1Charges"/>
            
            <div align="center" width="99%" style="margin-top:5px;margin-bottom:10px;">
                <div align="center" class="easyui-panel" title="OFFICE ORDER" width="99%">
                <table width="100%" border="0" style="font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 13px;">
                    <tr>
                        <td colspan="2" align="center" style="font-weight: bold;font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 16px;">Government of Odisha<br/>
                            [<c:out value="${Rule15Memo.deptName}"/> DEPARTMENT]
                        </td>
                    </tr>
                    <tr>
                        <td>&nbsp; ***** &nbsp;</td>
                    </tr>
                    <tr>
                        <td colspan="2" align="center" style="font-weight: bold;font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 14px;text-decoration: underline;">
                            OFFICE ORDER
                        </td>
                    </tr>
                    <tr height="15px;">
                        <td width="50%"><label>Order No: xxxx order no</label> </td>
                        <td width="50%"><label>Order Date: xxxx order date</label> </td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                    </tr>
                </table>
                
                <table id="tab1" width="100%" border="0" style="font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 13px;">
                    <tr>
                        <td><p style="text-align: justify;">Whereas charges were framed against Sri/Smt. ...... vide this Department Proceeding No......
                            under rule 15 of OCS (C.C & A) Rules, 1962 and </p></td>
                    </tr>
                    <tr>
                        <td><p style="text-align: justify;">Whereas he/she has/has not furnished his/her written statement of defence against 
                                the charges framed in the said Proceedings.</p></td>
                    </tr>
                    <tr>
                        <td><p style="text-align: justify;">Now therefore, after careful consideration of the charges, (explanation) of Sri/Smt..... 
                                Government have been pleased to appoint ......... as Inquiring Officer to enquire into the charges framed against Sri/Smt...
                                in the said proceeding as per the Rule 15(4) of OCS (C.C & A) Rules,1962.</p>
                        </td>
                    </tr>
                    <tr>
                        <td><p style="text-align: justify;">Government have also been pleased to nominate ......... as Marshalling Officer 
                                u/r 15(5) of OCS (C.C & A) Rules,1962 to produce the relevant records and to adduce evidence at the time of enquiry</p>
                        </td>
                    </tr>
                </table>
                <table width="100%" border="0">
                        <tr>
                            <td width="50%">&nbsp;</td>
                            <td width="50%" align="center">by Order of the Governor</td>
                        </tr>
                        <tr style="height:10px;">
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td align="center">Commissioner-cum-Secretary to Govt.</td>
                        </tr>
                    </table>
                
                </div>
            </div>
                        
        </form>
    </body>
</html>

