/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var monthNames = ["JAN", "FEB", "MAR", "APR", "MAY", "JUN",
    "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"
];

function myformatter(date) {
    var y = date.getFullYear();
    var m = date.getMonth();
    var d = date.getDate();
    //(d < 10 ? ('0' + d) : d) + '-' + (m < 10 ? ('0' + m) : m) + '-' + y;
    return (d < 10 ? ('0' + d) : d) + '-' + monthNames[m] + '-' + y;
}
/*By Manas*/
function loadCombo(url, comboid, combotext, combovalue) {
	$('#' + comboid).empty(); 
    $.getJSON(url, function (data) {
        $.each(data, function (i, obj) {
            $('#' + comboid).append($('<option>').text(obj[combotext]).attr('value', obj[combovalue]));
        });
    });
}
function clearform(divid) {
    $("#" + divid).find('input:text, input:checkbox').each(function () {
        $(this).val();
    });
}
function myparser(s) {
    if (!s)
        return new Date();    
    var ss = (s.split('-'));    
       
    if(ss[0].length == 4){
        y = parseInt(ss[0]);
        d = parseInt(ss[2]);
    }else{
        d = parseInt(ss[0]);
        y = parseInt(ss[2]);
    }
    if(ss[1].length == 3){
        found = $.inArray(ss[1].toUpperCase(), monthNames);
        m = parseInt(found);
    }else{
        m = parseInt(ss[1])-1;
    }
    
    
    /*if(m < 10){
        m = "0"+m;
    }
    if(d < 10){
        d = "0"+d;
    }*/
    if (y) { 
        return new Date(y , m , d); //d + '-' + monthNames[m - 1] + '-' + y;
    } else {
        return new Date();
    }
}


