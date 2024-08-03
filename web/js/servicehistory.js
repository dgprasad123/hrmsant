//Date of Creation: 25-JUL-2007
//Created By: Pravat Kumar Nayak
//Software Engineer

//----------------------------Javascript for add row at onClick event of a button------------
if (typeof HTMLElement != "undefined" && !HTMLElement.prototype.insertAdjacentElement) {
    HTMLElement.prototype.insertAdjacentElement = function (where, parsedNode) {
        switch (where) {
            case 'beforeBegin':
                this.parentNode.insertBefore(parsedNode, this)
                break;
            case 'afterBegin':
                this.insertBefore(parsedNode, this.firstChild);
                break;
            case 'beforeEnd':
                this.appendChild(parsedNode);
                break;
            case 'afterEnd':
                if (this.nextSibling)
                    this.parentNode.insertBefore(parsedNode, this.nextSibling);
                else
                    this.parentNode.appendChild(parsedNode);
                break;
        }
    }

    HTMLElement.prototype.insertAdjacentHTML = function (where, htmlStr) {
        var r = this.ownerDocument.createRange();
        r.setStartBefore(this);
        var parsedHTML = r.createContextualFragment(htmlStr);
        this.insertAdjacentElement(where, parsedHTML)
    }
    HTMLElement.prototype.insertAdjacentText = function (where, txtStr) {
        var parsedText = document.createTextNode(txtStr)
        this.insertAdjacentElement(where, parsedText)
    }
}
function AddRow(tbodyId, trClass, tdNumbers)
{
    objTR = document.createElement("TR");
    objTR.className = trClass;
    for (i = 0; i < tdNumbers; i++)
    {
        objTD = document.createElement("TD");
        objTD.innerText = " ";
        objTR.insertAdjacentElement("beforeEnd", objTD);

    }
    objTBody = document.getElementById(tbodyId);
    objTBody.insertAdjacentElement("beforeEnd", objTR);

}
//-------------------------------Javascript for row creation at onLoad event---------------- 
function fixedRows(tbodyId, numOfRows, numOfTd)
{
    var cnt = (document.getElementById(tbodyId)).childNodes.length;
    if (numOfRows % 2 != 0)
    {
        for (var i = 0; i < numOfRows - cnt; i++)
        {
            if ((numOfRows - cnt) % 2 == 0 && ((i + 1) % 2 != 0))
            {
                AddRow(tbodyId, "alternateNormalTD", numOfTd);
            }
            else if ((numOfRows - cnt) % 2 == 0 && ((i + 1) % 2 == 0))
            {
                AddRow(tbodyId, "alternateTD", numOfTd);
            }
            else if ((numOfRows - cnt) % 2 != 0 && ((i + 1) % 2 != 0))
            {
                AddRow(tbodyId, "alternateTD", numOfTd);
            }
            else if ((numOfRows - cnt) % 2 != 0 && ((i + 1) % 2 == 0))
            {
                AddRow(tbodyId, "alternateNormalTD", numOfTd);
            }
        }
    }
    else
    {
        for (var i = 0; i < numOfRows - cnt; i++)
        {
            if ((numOfRows - cnt) % 2 == 0 && ((i + 1) % 2 != 0))
            {
                AddRow(tbodyId, "alternateTD", numOfTd);
            }
            else if ((numOfRows - cnt) % 2 == 0 && ((i + 1) % 2 == 0))
            {
                AddRow(tbodyId, "alternateNormalTD", numOfTd);
            }
            else if ((numOfRows - cnt) % 2 != 0 && ((i + 1) % 2 != 0))
            {
                AddRow(tbodyId, "alternateNormalTD", numOfTd);
            }
            else if ((numOfRows - cnt) % 2 != 0 && ((i + 1) % 2 == 0))
            {
                AddRow(tbodyId, "alternateTD", numOfTd);
            }
        }
    }
}
//-----------------------------------Javascript for Retrieve month in Integer format--------------------------------
function monthint(monthname)
{
    var tmonthint = "";
    switch (monthname)
    {
        case "JAN":
            tmonthint = "0";
            return tmonthint;
            break;
        case "FEB":
            tmonthint = "1";
            return tmonthint;
            break;
        case "MAR":
            tmonthint = "2";
            return tmonthint;
            break;
        case "APR":
            tmonthint = "3";
            return tmonthint;
            break;
        case "MAY":
            tmonthint = "4";
            return tmonthint;
            break;
        case "JUN":
            tmonthint = "5";
            return tmonthint;
            break;
        case "JUL":
            tmonthint = "6";
            return tmonthint;
            break;
        case "AUG":
            tmonthint = "7";
            return tmonthint;
            break;
        case "SEP":
            tmonthint = "8";
            return tmonthint;
            break;
        case "OCT":
            tmonthint = "9";
            return tmonthint;
            break;
        case "NOV":
            tmonthint = "10";
            return tmonthint;
            break;
        case "DEC":
            tmonthint = "11";
            return tmonthint;
            break;
        default:
            tmonthint = "0";
    }


}
//------------------------Javascript for check from Date greater than To Date---------------------------------
function chkdate(frmdate, todate, m1, m2)
{
    var fdate = "";
    var fmonth = "";
    var fmonthint = "";
    var fyear = "";
    var tdate = "";
    var tmonth = "";
    var tmonthint = "";
    var tyear = "";

    if (frmdate.value.length == 10)
    {
        fdate = frmdate.value.substring(0, 1);
        fmonth = frmdate.value.substring(2, 5).toUpperCase();
        fyear = frmdate.value.substring(6, 10);
    }
    else
    {
        fdate = frmdate.value.substring(0, 2);
        fmonth = frmdate.value.substring(3, 6).toUpperCase();
        fyear = frmdate.value.substring(7, 11);
    }
    fmonthint = monthint(fmonth);
    if (todate.value.length == 10)
    {
        tdate = todate.value.substring(0, 1);
        tmonth = todate.value.substring(2, 5).toUpperCase();
        tyear = todate.value.substring(6, 10);
    }
    else
    {
        tdate = todate.value.substring(0, 2);
        tmonth = todate.value.substring(3, 6).toUpperCase();
        tyear = todate.value.substring(7, 11);
    }
    tmonthint = monthint(tmonth);

    var d1 = new Date(fyear, fmonthint, fdate);
    var d2 = new Date(tyear, tmonthint, tdate);

    var Fdate = d1.toDateString();
    var Tdate = d2.toDateString();
    if (d2 < d1)
    {
        alert(m2 + " Shouldn't be be less than " + m1);
        todate.focus();
        return false;
    }

    return true;
}
function chksamedate(frmdate, todate)
{
    var fdate = "";
    var fmonth = "";
    var fmonthint = "";
    var fyear = "";
    var tdate = "";
    var tmonth = "";
    var tmonthint = "";
    var tyear = "";
    var ret = false;

    if (frmdate.value.length == 10)
    {
        fdate = frmdate.value.substring(0, 1);
        fmonth = frmdate.value.substring(2, 5).toUpperCase();
        fyear = frmdate.value.substring(6, 10);
    }
    else
    {
        fdate = frmdate.value.substring(0, 2);
        fmonth = frmdate.value.substring(3, 6).toUpperCase();
        fyear = frmdate.value.substring(7, 11);
    }
    fmonthint = monthint(fmonth);
    if (todate.value.length == 10)
    {
        tdate = todate.value.substring(0, 1);
        tmonth = todate.value.substring(2, 5).toUpperCase();
        tyear = todate.value.substring(6, 10);
    }
    else
    {
        tdate = todate.value.substring(0, 2);
        tmonth = todate.value.substring(3, 6).toUpperCase();
        tyear = todate.value.substring(7, 11);
    }
    tmonthint = monthint(tmonth);

    var d1 = new Date(fyear, fmonthint, fdate);
    var d2 = new Date(tyear, tmonthint, tdate);

    var Fdate = d1.toDateString();
    var Tdate = d2.toDateString();

    if (Fdate == Tdate)
    {
        ret = true;

    } else {

        alert(" From date To date must be same");
        todate.focus();
        ret = false;
    }
    return ret;

}
//-----------------Javascript for Validation For Date----------------------------

function dateValidationforSave(selectdate)
{
    var tempMonth;
    var tempTest;
    //var srcElem=window.event.srcElement;
    var inDate = selectdate.value;
    var formatRegEx = /([1-9]|0[1-9]|[12][0-9]|3[01])-(\w{3})-(1[6-9]|2[0-9])\d{2}/;
    //var dateRegEx = /^([1-9]|0[1-9]|[12][0-9]|3[01])(-)(0[1-9]|1[0-2])(-)(1[0-9]|2[0-9])\d{2}$/;
    if (selectdate.value == "") {
        return false;
    }
    if (formatRegEx.test(inDate) == true)
    {

        if (inDate.length == 10)
        {
            tempTest = inDate.substring(2, 5);
        }
        else
        {
            tempTest = inDate.substring(3, 6);
        }
        regStop = /-/gi;
        arrayMatches = regStop.exec(tempTest);
        if (arrayMatches != null)
        {
            if (arrayMatches.length > 0)
            {
                //alert("Invalid Date Format");
                //selectdate.select();
                //selectdate.focus();
                return false;
            }
        }
        if (tempTest == "JAN" || tempTest == "Jan" || tempTest == "jan")
        {
            tempMonth = inDate.replace(/JAN/i, "01");
            //alert(tempMonth);
        }
        else if (tempTest == "FEB" || tempTest == "Feb" || tempTest == "feb")
        {
            tempMonth = inDate.replace(/FEB/i, "02");
            //alert(tempMonth);
        }
        else if (tempTest == "MAR" || tempTest == "Mar" || tempTest == "mar")
        {
            regEx = /MAR/i;
            tempMonth = inDate.replace(regEx, "03");
            //alert(tempMonth);
        }
        else if (tempTest == "APR" || tempTest == "Apr" || tempTest == "apr")
        {
            regEx = /APR/i;
            tempMonth = inDate.replace(regEx, "04");
            //alert(tempMonth);
        }
        else if (tempTest == "MAY" || tempTest == "May" || tempTest == "may")
        {
            regEx = /MAY/i;
            tempMonth = inDate.replace(regEx, "05");
            //alert(tempMonth);
        }
        else if (tempTest == "JUN" || tempTest == "Jun" || tempTest == "jun")
        {
            regEx = /JUN/i;
            tempMonth = inDate.replace(regEx, "06");
            //alert(tempMonth);
        }
        else if (tempTest == "JUL" || tempTest == "Jul" || tempTest == "jul")
        {
            regEx = /JUL/i;
            tempMonth = inDate.replace(regEx, "07");
            //alert(tempMonth);
        }
        else if (tempTest == "AUG" || tempTest == "Aug" || tempTest == "aug")
        {
            regEx = /AUG/i;
            tempMonth = inDate.replace(regEx, "08");
            //alert(tempMonth);
        }
        else if (tempTest == "SEP" || tempTest == "Sep" || tempTest == "sep")
        {
            regEx = /SEP/i;
            tempMonth = inDate.replace(regEx, "09");
            //alert(tempMonth);
        }
        else if (tempTest == "OCT" || tempTest == "Oct" || tempTest == "oct")
        {
            regEx = /OCT/i;
            tempMonth = inDate.replace(regEx, "10");
            //alert(tempMonth);
        }
        else if (tempTest == "NOV" || tempTest == "Nov" || tempTest == "nov")
        {
            regEx = /NOV/i;
            tempMonth = inDate.replace(regEx, "11");
            //alert(tempMonth);
        }
        else if (tempTest == "DEC" || tempTest == "Dec" || tempTest == "dec")
        {
            regEx = /DEC/i;
            tempMonth = inDate.replace(regEx, "12");
            //alert(tempMonth);
        }
        else
        {
            //alert("Invalid Date Format");
            //selectdate.select();

            //selectdate.focus();
            return false;
        }
        day = tempMonth.substring(0, 2); // parse date into variables
        month = tempMonth.substring(3, 5);
        year = tempMonth.substring(6, 10);
        if (day < 1 || day > 31)
        {
            alert("Day must be between 1 and 31.");
            selectdate.select();
            selectdate.focus();
            return false;
        }
        if (month < 1 || month > 12)
        { // check month range
            alert("Month must be between 1 and 12.");
            selectdate.select();
            selectdate.focus();
            return false;
        }

        if ((month == 4 || month == 6 || month == 9 || month == 11) && day == 31)
        {
            alert("The month specified doesn't have 31 days!")
            selectdate.select();
            selectdate.focus();
            return false;
        }
        if (month == 2)
        { // check for february 29th
            var isleap = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));

            if (day > 29 || (day == 29 && !isleap))
            {
                alert("February " + year + " doesn't have " + day + " days!");
                selectdate.select();
                selectdate.focus();
                return false;
            }
        }

//		if(dateRegEx.test(tempMonth)==false)
//		{
//			
//			alert("Invalid Date Format");
//			selectdate.select();
//			selectdate.focus();
//			return false;
//		}
    }
    else
    {
//		if(dateRegEx.test(inDate)==false)
//		{
        //alert("Invalid Date Format");
        //selectdate.select();
        //selectdate.focus();	
        return false;
//		}
    }
    return true;
}

function dateValidation(selectdate)
{
    var tempMonth;
    var tempTest;
    var inDate = selectdate.value;
    var formatRegEx = /([1-9]|0[1-9]|[12][0-9]|3[01])-(\w{3})-(1[6-9]|2[0-9])\d{2}/;
    //var dateRegEx = /^([1-9]|0[1-9]|[12][0-9]|3[01])(-)(0[1-9]|1[0-2])(-)(1[0-9]|2[0-9])\d{2}$/;
    if (selectdate.value == "") {
        return false;
    }
    if (formatRegEx.test(inDate) == true)
    {

        if (inDate.length == 10)
        {
            tempTest = inDate.substring(2, 5);
        }
        else
        {
            tempTest = inDate.substring(3, 6);
        }
        regStop = /-/gi;
        arrayMatches = regStop.exec(tempTest);
        if (arrayMatches != null)
        {
            if (arrayMatches.length > 0)
            {
                alert("Invalid Date Format");
                selectdate.select();
                selectdate.focus();
                return false;
            }
        }
        if (tempTest == "JAN" || tempTest == "Jan" || tempTest == "jan")
        {
            tempMonth = inDate.replace(/JAN/i, "01");
            //alert(tempMonth);
        }
        else if (tempTest == "FEB" || tempTest == "Feb" || tempTest == "feb")
        {
            tempMonth = inDate.replace(/FEB/i, "02");
            //alert(tempMonth);
        }
        else if (tempTest == "MAR" || tempTest == "Mar" || tempTest == "mar")
        {
            regEx = /MAR/i;
            tempMonth = inDate.replace(regEx, "03");
            //alert(tempMonth);
        }
        else if (tempTest == "APR" || tempTest == "Apr" || tempTest == "apr")
        {
            regEx = /APR/i;
            tempMonth = inDate.replace(regEx, "04");
            //alert(tempMonth);
        }
        else if (tempTest == "MAY" || tempTest == "May" || tempTest == "may")
        {
            regEx = /MAY/i;
            tempMonth = inDate.replace(regEx, "05");
            //alert(tempMonth);
        }
        else if (tempTest == "JUN" || tempTest == "Jun" || tempTest == "jun")
        {
            regEx = /JUN/i;
            tempMonth = inDate.replace(regEx, "06");
            //alert(tempMonth);
        }
        else if (tempTest == "JUL" || tempTest == "Jul" || tempTest == "jul")
        {
            regEx = /JUL/i;
            tempMonth = inDate.replace(regEx, "07");
            //alert(tempMonth);
        }
        else if (tempTest == "AUG" || tempTest == "Aug" || tempTest == "aug")
        {
            regEx = /AUG/i;
            tempMonth = inDate.replace(regEx, "08");
            //alert(tempMonth);
        }
        else if (tempTest == "SEP" || tempTest == "Sep" || tempTest == "sep")
        {
            regEx = /SEP/i;
            tempMonth = inDate.replace(regEx, "09");
            //alert(tempMonth);
        }
        else if (tempTest == "OCT" || tempTest == "Oct" || tempTest == "oct")
        {
            regEx = /OCT/i;
            tempMonth = inDate.replace(regEx, "10");
            //alert(tempMonth);
        }
        else if (tempTest == "NOV" || tempTest == "Nov" || tempTest == "nov")
        {
            regEx = /NOV/i;
            tempMonth = inDate.replace(regEx, "11");
            //alert(tempMonth);
        }
        else if (tempTest == "DEC" || tempTest == "Dec" || tempTest == "dec")
        {
            regEx = /DEC/i;
            tempMonth = inDate.replace(regEx, "12");
            //alert(tempMonth);
        }
        else
        {
            alert("Invalid Date Format");
            selectdate.select();

            selectdate.focus();
            return false;
        }
        day = tempMonth.substring(0, 2); // parse date into variables
        month = tempMonth.substring(3, 5);
        year = tempMonth.substring(6, 10);
        if (day < 1 || day > 31)
        {
            alert("Day must be between 1 and 31.");
            selectdate.select();
            selectdate.focus();
            return false;
        }
        if (month < 1 || month > 12)
        { // check month range
            alert("Month must be between 1 and 12.");
            selectdate.select();
            selectdate.focus();
            return false;
        }

        if ((month == 4 || month == 6 || month == 9 || month == 11) && day == 31)
        {
            alert("The month specified doesn't have 31 days!")
            selectdate.select();
            selectdate.focus();
            return false;
        }
        if (month == 2)
        { // check for february 29th
            var isleap = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));

            if (day > 29 || (day == 29 && !isleap))
            {
                alert("February " + year + " doesn't have " + day + " days!");
                selectdate.select();
                selectdate.focus();
                return false;
            }
        }

//		if(dateRegEx.test(tempMonth)==false)
//		{
//			
//			alert("Invalid Date Format");
//			selectdate.select();
//			selectdate.focus();
//			return false;
//		}
    }
    else
    {
//		if(dateRegEx.test(inDate)==false)
//		{
        alert("Invalid Date Format");
        selectdate.select();
        selectdate.focus();
        return false;
//		}
    }
    return true;
}

//function dateValidation(selectdate)
//{
//	var tempMonth;
//	var tempTest;
//	var srcElem=window.event.srcElement;
//	var inDate = selectdate.value;
//	var formatRegEx = /([1-9]|0[1-9]|[12][0-9]|3[01])\/|-(\w{3})\/|-(1[6-9]|2[0-9])\d{2}/;
//	var dateRegEx = /^([1-9]|0[1-9]|[12][0-9]|3[01])(\/|-)(0[1-9]|1[0-2])(\/|-)(1[0-9]|2[0-9])\d{2}$/;
//	if(selectdate.value==""){
//		return false;
//	}
//	if(formatRegEx.test(inDate)==true)
//	{
//		
//		if(inDate.length==10)
//		{
//			tempTest = inDate.substring(2,5);
//		}
//		else
//		{
//			tempTest = inDate.substring(3,6);
//		}
//		regStop = /\/|-/gi;
//		arrayMatches = regStop.exec(tempTest);
//		if(arrayMatches!=null)
//		{
//			if(arrayMatches.length>0)
//			{
//				alert("Invalid Date Format");
//				selectdate.select();
//				selectdate.focus();
//				return false;
//			}
//		}
//		if(tempTest=="JAN" || tempTest=="Jan" || tempTest=="jan" )
//		{
//			tempMonth=inDate.replace(/JAN/i,"01");
//			//alert(tempMonth);
//		}
//		else if(tempTest=="FEB" || tempTest=="Feb" || tempTest=="feb")
//		{
//			tempMonth=inDate.replace(/FEB/i,"02");
//			//alert(tempMonth);
//		}
//		else if(tempTest=="MAR" || tempTest=="Mar" || tempTest=="mar")
//		{
//			regEx = /MAR/i;
//			tempMonth=inDate.replace(regEx,"03");
//			//alert(tempMonth);
//		}
//		else if(tempTest=="APR" || tempTest=="Apr" || tempTest=="apr")
//		{
//			regEx = /APR/i;
//			tempMonth=inDate.replace(regEx,"04");
//			//alert(tempMonth);
//		}
//		else if(tempTest=="MAY" || tempTest=="May" || tempTest=="may")
//		{
//			regEx = /MAY/i;
//			tempMonth=inDate.replace(regEx,"05");
//			//alert(tempMonth);
//		}
//		else if(tempTest=="JUN" || tempTest=="Jun" || tempTest=="jun")
//		{
//			regEx = /JUN/i;
//			tempMonth=inDate.replace(regEx,"06");	
//			//alert(tempMonth);
//		}
//		else if(tempTest=="JUL" || tempTest=="Jul" ||tempTest=="jul")
//		{
//			regEx = /JUL/i;
//			tempMonth=inDate.replace(regEx,"07");
//			//alert(tempMonth);
//		}
//		else if(tempTest=="AUG" || tempTest=="Aug" || tempTest=="aug")
//		{
//			regEx = /AUG/i;
//			tempMonth=inDate.replace(regEx,"08");
//			//alert(tempMonth);
//		}
//		else if(tempTest=="SEP" || tempTest=="Sep" || tempTest=="sep")
//		{
//			regEx = /SEP/i;
//			tempMonth=inDate.replace(regEx,"09");
//			//alert(tempMonth);
//		}
//		else if(tempTest=="OCT" || tempTest=="Oct" || tempTest=="oct")
//		{
//			regEx = /OCT/i;
//			tempMonth=inDate.replace(regEx,"10");
//			//alert(tempMonth);
//		}
//		else if(tempTest=="NOV" || tempTest=="Nov" || tempTest=="nov")
//		{
//			regEx = /NOV/i;
//			tempMonth=inDate.replace(regEx,"11");
//			//alert(tempMonth);
//		}
//		else if(tempTest=="DEC" || tempTest=="Dec" ||tempTest=="dec")
//		{
//			regEx = /DEC/i;
//			tempMonth=inDate.replace(regEx,"12");
//			//alert(tempMonth);
//		}
//		else
//		{
//			alert("Invalid Date Format");
//			selectdate.select();
//			selectdate.focus();
//			return false;
//		}
//		day= tempMonth.substring(0,2); // parse date into variables
//		month = tempMonth.substring(3,5);
//		year = tempMonth.substring(6,10);
//		if (day < 1 || day > 31) 
//		{
//			alert("Day must be between 1 and 31.");
//			selectdate.select();
//			selectdate.focus();	
//			return false;
//		}	
//		if (month < 1 || month > 12) 
//		{ // check month range
//			alert("Month must be between 1 and 12.");
//			selectdate.select();
//			selectdate.focus();	
//			return false;
//		}
//		
//		if ((month==4 || month==6 || month==9 || month==11) && day==31) 
//		{
//			alert("The month specified doesn't have 31 days!")
//			selectdate.select();
//			selectdate.focus();	
//			return false;
//		}
//		if (month == 2)
//		{ // check for february 29th
//			var isleap = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
//		
//			if (day>29 || (day==29 && !isleap)) 
//			{
//				alert("February " + year + " doesn't have " + day + " days!");
//				selectdate.select();
//				selectdate.focus();	
//				return false;
//			}
//		}
//		
//		if(dateRegEx.test(tempMonth)==false)
//		{
//			
//			alert("Invalid Date Format");
//			selectdate.select();
//			selectdate.focus();
//			return false;
//		}
//	}
//	else
//	{
//		if(dateRegEx.test(inDate)==false)
//		{
//			alert("Invalid Date Format");
//			selectdate.select();
//			selectdate.focus();	
//			return false;
//		}
//	}
//    return true;
//}
//----------------------------Javascript for OK button---------------------------------------- 
function okcheck(EmpId, GPF)
{

    var empid = document.getElementById(EmpId);
    var gpfno = document.getElementById(GPF);
    if (empid.value == "" && gpfno.value == "")
    {
        alert("Please Enter HRMS ID or GPF/ PPAN No.");
        if (empid.value == "")
        {
            empid.focus();
            return false;
        }
        if (gpfno.value == "")
        {
            gpfno.focus();
            return false;
        }
        return false;
    }
    return true;

}
//----------------------------Javascript for Confirmation message for any button---------------- 
function confirmMessage(msg)
{
    var bdelete = confirm(msg);
    if (bdelete)
    {
        return true;
    }
    else
    {
        return false;
    }

}
//---------------------------Javascript for check Rupess type field---------------------------
function isNumber(txtId)
{
    var re = /(^(0|[1-9][0-9]*)$)|((^(0?|[1-9][0-9]*)\.(0*[1-9][0-9]*)$)|(^[1-9]+[0-9]*\.0+$)|(^0\.0+$))/;
    //var txtId = document.getElementById(txtInput);
    if (txtId.value.length != 0)
    {
        var ok = re.exec(txtId.value);
        if (!ok)
        {
            alert('Please Enter Number or Decimal values');

            txtId.value = "";
            txtId.focus();
            return false;

        }
    }
}

function isNumber11(txtId) {

    var str = /^\d+$/;
    var str1 = /^\d+.\d+$/;
    var totday = txtId.value;
    if (totday != "")
    {
        if ((str.test(totday) != true) && (str1.test(totday) != true)) {
            alert("Enter only numbers");
            txtId.select();
            txtId.focus();
            return false;
        }
    }
}

function isNumberAndMaxlength(txtObj, maxlength) {
    var str = /^\d+$/;
    var totday = txtObj.value;
    var srcElem = window.event.srcElement;
    if (totday != "")
    {

        if (!str.test(totday)) {

            alert("Enter only numbers");


            srcElem.value = "";
            srcElem.focus();
            return false;

        }
        if (totday.length > maxlength)
        {

            alert("Please enter " + maxlength + " number of digits");
            //srcElem.value="";
            srcElem.value = srcElem.value.substring(0, maxlength);
            srcElem.select();
            srcElem.focus();
            return false;

        }
    }
}

//function isNumber(txtId){
//		//alert(id);
//	var str=/\d+\.\d+/;
//	//var empid=document.getElementById(idElem);
//	//alert(empid);
//		if(!str.exec(txtId.value)){
//			alert("Enter only numbers");
//			txtId.select();
//			txtId.focus();
//		}
//		else
//		{
//			return true;
//		}
//}
//-------------------------------Javascript for maximum length check-------------------------------
function maxlengthcheck(currentId, mlength)
{

    //var srcElem = window.event.srcElement;
    var currentIdObject = document.getElementById(currentId);
    if (currentIdObject.value.length > mlength)
    {
        alert("Enter only " + mlength + " characters.");
        currentIdObject.value = currentIdObject.value.substring(0, mlength);
        srcElem.select();
        return false;
    }
}


function maxlengthcheckdigit(currentId, mlength)
{
    //var srcElem = window.event.srcElement;
    var currentId = document.getElementById(currentId);

    if (currentId.value.length > mlength)
    {
        alert("Enter only " + mlength + " digits.");
        currentId.value = currentId.value.substring(0, mlength);
        //srcElem.select();
        return false;
    }
}

//-------------------------Javascript for focus on Employeeid textbox----------------------- 
function focusText(str) {
    var empid = document.getElementById(str);
    if (empid.value == "")
    {
        empid.focus();
    }
}
//------------------------------Javascript for take Numbers----------------------------------
//call the below fun as--- onkeypress = 'return numbersonly(this,event)'
function numbersonly(myfield, e, dec)
{
    var key;
    var keychar;
    if (window.event)
        key = window.event.keyCode;
    else if (e)
        key = e.which;
    else
        return true;
    keychar = String.fromCharCode(key);

    //control keys

    if ((key == null) || (key == 0) || (key == 8) || (key == 9) || (key == 13) || (key == 27))
        return true;

    //numbers
    else if (("0123456789".indexOf(keychar) > -1))
        return true;

    //decimal point jump
    else if (dec && (keychar == "."))
    {
        myfield.form.elements[dec].focus();
        return false;
    }
    else
        alert("Please Enter Only Numbers ");
    return false;


}

//---------------------Javascript for enable textbox--------------------------------------------------//
function disableDrop(radiobtn, sltdept, sltoff, sltauth, txtdept, txtoff, txtauth, btnDept, btnOffice)
{
    var temprad = document.getElementsByName(radiobtn);

    for (i = 0; i < temprad.length; i++)
    {
        var radval = temprad[i].value;
        if (temprad[i].checked == true)
        {
            if (radval == 'GOO')
            {
                document.getElementById(sltdept).style.display = "inline";
                document.getElementById(sltoff).style.display = "inline";
                document.getElementById(sltauth).style.display = "inline";
                document.getElementById(btnDept).style.display = "inline";
                document.getElementById(btnOffice).style.display = "inline";
                document.getElementById(txtdept).style.display = "none";
                document.getElementById(txtoff).style.display = "none";
                document.getElementById(txtauth).style.display = "none";

            }
            else
            {
                document.getElementById(sltdept).style.display = "none";
                document.getElementById(sltoff).style.display = "none";
                document.getElementById(sltauth).style.display = "none";
                document.getElementById(btnDept).style.display = "none";
                document.getElementById(btnOffice).style.display = "none";
                document.getElementById(txtdept).style.display = "inline";
                document.getElementById(txtoff).style.display = "inline";
                document.getElementById(txtauth).style.display = "inline";
            }
        }
    }
}
function clearThreeSelectBoxes(paramone, paramtwo, paramthree)
{

    var param1 = document.getElementById(paramone);
    var param2 = document.getElementById(paramtwo);
    var param3 = document.getElementById(paramthree);

    param1.value = '';
    param2.value = '';
    param3.value = '';


}
function clearTwoSelectBoxes(paramone, paramtwo)
{

    var param1 = document.getElementById(paramone);
    var param2 = document.getElementById(paramtwo);

    param1.value = '';
    param2.value = '';


}
function clearOneSelectBoxes(paramone)
{

    var param1 = document.getElementById(paramone);

    param1.value = '';


}

//------------------End Here------------------------------------------------------------------------//


//-------------------Java Script for Number with decimal points without characters-----------------------//


function numbersWithDecimalOnly(myfield, e)
{
    var key;
    var keychar;
    if (window.event)
        key = window.event.keyCode;
    else if (e)
        key = e.which;
    else
        return true;
    keychar = String.fromCharCode(key);

    //control keys

    if ((key == null) || (key == 0) || (key == 8) || (key == 9) || (key == 13) || (key == 27))
        return true;

    //numbers
    else if (("0123456789".indexOf(keychar) > -1))
        return true;

    //decimal point jump
    else if (keychar == ".")
    {

        return true;
    }
    else
        keychar.value = '';
    alert("Enter only numbers");
    alert(keychar);

    return false;


}
//----------------------------------------------------End of method --------------------

function IsNumeric(sText) {
    var ValidChars = "0123456789.";
    var IsNumber = true;
    var Char;

    for (i = 0; i < sText.length && IsNumber == true; i++) {
        Char = sText.charAt(i);
        if (ValidChars.indexOf(Char) == -1) {
            IsNumber = false;
        }
    }

    return IsNumber;
}
//-----------------------------------------------------------Numbers With Decimal---------------------------------

function isAmount(e, txtId)
{

    var str1 = /^\d+(\.\d{1,2})?$/;
    var totday = txtId.value;
    if (totday != "")
    {
        if ((str1.test(totday) != true)) {
            alert("Enter only Amount");
            //  txtId.value="";
            txtId.select();
            txtId.focus();
            return false;
        }
    }


}
//-----------------------------------------------------------END of Method----------------------------------------

function amountCheck(amtId)
{
    var amt = document.getElementById(amtId);

    if (amt.value != "")
    {
        var str1 = /^\d+(\.\d{1,2})?$/;
        var totday = amt.value;
        if ((str1.test(totday) != true))
        {
            alert("Enter only Amount");
            amt.select();
            amt.focus();
            return false;
        }

    }
    return true;
}




//--------------------------------Number With Decimal And Maxlength--follow parameters as given in example example(isAmountAndMaxlengthCheck(this,15)------

function isAmountAndMaxlengthCheck(txtObj, length)
{

    var str1 = /^\d+(\.\d{1,2})?$/;
    var totday = txtObj.value;
    if (totday != "")
    {
        if ((str1.test(totday) != true)) {
            alert("Enter only a valid number.");
            //  txtId.value="";
            txtObj.select();
            txtObj.focus();
            return false;
        }
    }
    if (txtObj.value.length > length) {
        alert("Enter " + length + " numbers only.");
        txtObj.select();
        txtObj.focus();
        return false;
    }


}

function isPositiveNumber(txtInput)
{
    var re = /^(0|[1-9][0-9]*)$/;
    var txtId = document.getElementById(txtInput);
    var ok = re.exec(txtId.value);
    if (txtId.value != '')
    {
        if (!ok)
        {
            alert('Please Enter Numbers only');
            txtId.select();
            txtId.focus();
            return false;
        }
    }

    return true;
}

//-------------------------------------------------------  End of Code -----------------------------------------

function maxlengthcheck1(txtObj, mlength)
{

    if (txtObj.value.length > mlength) {
        alert("Enter only " + mlength + " characters.");
        txtObj.select();
        txtObj.focus();
        return false;
    }


}
