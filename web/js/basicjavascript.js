/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function trim(stringToTrim) {
	return stringToTrim.replace(/^\s+|\s+$/g,"");
}
function ltrim(stringToTrim) {
	return stringToTrim.replace(/^\s+/,"");
}
function rtrim(stringToTrim) {
	return stringToTrim.replace(/\s+$/,"");
}
function BlankNumericFieldValidation(objCtl, objMsg)
{
    objControl = document.getElementById(objCtl);
    if (trim(objControl.value) == "0" || trim(objControl.value) == "")
    {
        alert(objMsg + ' cannot be 0!');
        objControl.focus();
        return false;
    }
    return true;
}
function onlyNumbers(e)
{
	var browser = navigator.appName;	
	if(browser == "Netscape"){
		var keycode = e.which;		
		if(keycode == 46 || (keycode >=48 && keycode <=57) || keycode == 8 || keycode == 0)
			return true;
		else
			return false;
	}else{
		if(e.keyCode==46 || (e.keyCode >=48 && e.keyCode<=57) || e.keycode == 8 || e.keycode == 0)
			e.returnValue=true;
		else
			e.returnValue=false;		
	}		
}
function onlyInteger(e)
{
	var browser = navigator.appName;	
	if(browser == "Netscape"){
		var keycode = e.which;		
		if((keycode >=48 && keycode <=57) || keycode == 8 || keycode == 0)
			return true;
		else
			return false;
	}else{
		if((e.keyCode >=48 && e.keyCode<=57) || e.keycode == 8 || e.keycode == 0)
			e.returnValue=true;
		else
			e.returnValue=false;		
	}		
}
function onlyCharacters(e)
{
	var browser = navigator.appName;	
	if(browser == "Netscape"){
		var keycode = e.which;	
                //alert(keycode);
		if((keycode >=44 && keycode <=57) || (keycode >=65 && keycode <=90) || (keycode >=97 && keycode <=122) || keycode == 8 || keycode == 32 || keycode == 0)
			return true;
		else
			return false;
	}else{
		if((e.keyCode >=44 && e.keyCode<=57) || (e.keyCode >=65 && e.keyCode <=90) || (e.keyCode >=97 && e.keyCode <=122) || e.keycode == 8 || e.keycode == 32 || e.keycode == 0)
			e.returnValue=true;
		else
			e.returnValue=false;		
	}		
}
