<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <link rel="stylesheet" type="text/css" href="resources/css/colorbox.css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript" src="js/jquery.colorbox-min.js"></script>
        
        <script type="text/javascript">
           $(document).ready(function () {
                $('#deptname').combobox({url: 'getDeptListLoanJSON.htm',
                    onSelect: function (record) {
                        $('#officename').combobox('clear');
                        $('#post').combobox('clear');

                        var url = 'getOfficeListJSON.htm?deptcode=' + record.value;
                        $('#officename').combobox('reload', url);
                    }
                });

                $('#officename').combobox({
                    onSelect: function (record) {
                        $('#post').combobox('clear');
                        var url = 'getPostListLoanJSON.htm?offcode=' + record.value;
                        $('#post').combobox('reload', url);
                    }
                });
            });
        </script>    
        
    </head>

    <body>       
        <form action="showOtherOffice.htm" method="POST" commandName="otherOfficeForm">

            <input type="hidden" name="hidOffCode" id="hidOffCode"/>
            <input type="hidden" name="hidOffName" id="hidOffName"/>
            <input type="hidden" name="hidSPC" id="hidSPC"/>
            
            <div id="tbl-container" class="easyui-panel" title="Outside Office"  style="width:100%;overflow: auto;">
                <div align="left" style="padding-left:10px">
                    <table style="width:100%">
                        <tr>
                            <td>1. Department</td>
                            <td width="70%">
                                <input name="deptCode" id="deptname" class="easyui-combobox" style="width:500px;" 
                                       data-options="valueField:'value',textField:'label'" />
                            </td>
                        </tr>
                        <tr>
                            <td>2. Office Name</td>
                            <td align="left">
                                <input name="hidOffCode" id="officename" class="easyui-combobox" style="width:500px;" 
                                       data-options="valueField:'value',textField:'label'"/>
                            </td>   
                        </tr>
                    </table>
                    
                    <div style="text-align:center;padding:5px">
                        <input class="easyui-linkbutton" type="submit" name="ShowEmployee" value="Show" />
                        <input class="easyui-linkbutton" type="submit" name="Back" value="Back"/>

                    </div>      
                </div>
            </div>
        </form>
    </body>
</html>