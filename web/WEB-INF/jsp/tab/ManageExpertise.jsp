<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <title>Human Resources Management System, Government of Odisha</title>

        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <link rel="stylesheet" type="text/css" href="resources/css/colorbox.css"/>
        <link rel="stylesheet" type="text/css" href="css/popupmain.css"/>
        
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript">
            $(document).ready(function(){
                
            });
            function editRow(val,row){
                return "Edit";
            }
        </script>
        <style type="text/css">
            body{
                font-family:Verdana,sans-serif;
            }
        </style>
    </head>
    <body>
        <div align="center">
            <p>
                <input type="button" value="Download As PDF" onclick="self.location='downloadExpertisePDF.htm'" style="background:#00629B;color:#FFFFFF;padding:5px 10px;border:1px solid #000;cursor:pointer;" />
                <input type="button" value="View All" onclick="window.open('ViewExpertiseList.htm')" style="background:#00629B;color:#FFFFFF;padding:5px 10px;border:1px solid #000;cursor:pointer;" />
                <input type="button" value="Logout" onclick="self.location='logout.htm'" style="background:#00629B;color:#FFFFFF;padding:5px 10px;border:1px solid #000;cursor:pointer;"/>
            </p>
            <table id="expertisedg" class="easyui-datagrid" style="width:100%;height:360px;" title="Manage Expertise"
                   rownumbers="true" url="GetManageExpertiseListJSON.htm" pagination="true" singleSelect="true"
                   data-options="singleSelect:true,collapsible:true,fitColumns:true,nowrap:false" toolbar="#expertisedgTool">
                <thead>
                    <tr>
                        <th data-options="field:'name',width:70">Name</th>
                        <th data-options="field:'areaOfExpertise',width:100">Area of Expertise</th>
                        <th data-options="field:'areaOfInterest',width:100">Area of Interest</th>
                        <th data-options="field:'volWillingness',width:50">Willingness to Volunteer</th>
                    </tr> 
                </thead>
            </table>
        </div>
    </body>
</html>
