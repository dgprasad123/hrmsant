<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";

%>
<html>
    <head>

        <title>:: HRMS, Government of Odisha ::</title>

        <style>
            #loadingMessage {
                width: 100%;
                height: 100%;
                z-index: 1000;
                background: #ccc;
                top: 0px;
                left: 0px;
                position: absolute;
            }
        </style>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <link rel="stylesheet" type="text/css" href="resources/css/demo.css">
        <link rel="stylesheet" href="css/swc.css">
        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery-1.8.0.min.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript" src="js/debug.js"></script>

        <script type="text/javascript">
            var globalId = "";
            $(function () {
                // Call After Load
                $('#offemptree').tree({
                    checkbox: false,
                    url: 'employeeTree.htm?id=&empid=' + $("#hidempId").val(),
                    method: 'GET',
                    onLoadSuccess: function (node, data) {
                        globalId = $("#hidoffcode").val();
                        loadRightpanel('PA' + $("#hidoffcode").val());
                        
                    },
                    onClick: function (node) {
                        var str = node.id.substr(0, 2);
                        if (str == 'PA' || str == 'RE' || str == 'CO') {
                            globalId = $("#hidoffcode").val();
                        } else {
                            globalId = node.id;
                        }
                        loadRightpanel(node.id);
                    },
                    formatter: function (node) {
                        var s = node.text;
                        if (node.children) {
                            s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
                        }
                        return s;
                    }
                });
                $('#tablist').tabs({
                    onSelect: function (title, index) {
                        if (index == 0) {
                            $('#rightpanel').panel({
                                href: "getRollWiseLink.htm?rollId=" + $("#rollId").val(),
                                loadingMessage: 'Loading...'
                            });
                        } else if (index == 1) {
                            window.location.replace("myPage.htm");
                        }
                    },
                    onLoad: function (rightpanel) {

                        $('#rightpanel').panel({
                            href: "getRollWiseLink.htm?rollId=" + $("#rollId").val(),
                            loadingMessage: 'Loading...'
                        });
                    }

                });



                /*Call After Load*/

                $('#win').window({
                    onClose: function () {

                        $("#win").window("setTitle", '');
                        $("#winfram").attr("src", "");
                    },
                    onBeforeOpen: function () {
                        loadingMessage: 'Loading...'

                    }

                });

            });
            function displayNextPage(me) {

                var pageNo = parseInt($(me).text().trim());
                var start = (pageNo - 1) * 6;
                var end = pageNo * 6;
                var pagingServicList = $(me).parent().parent();
                var lilength = $(pagingServicList).siblings(".serviceList").children('li');
                var lilengthi = $(lilength).length;
                for (i = 0; i < lilengthi; i++) {
                    $(lilength[i]).hide();
                }
                for (i = start; i < end; i++) {
                    $(lilength[i]).show();
                }

                //$("li.start").siblings()

            }
            function loadRightpanel(globalId) {

                $('#rightpanel').append('<table width="320" border="0"><tr><td colspan="2" rowspan="1">' + '<li style="float:left;margin-left:2px;margin-top:10px;font-size:12px;"><a href="finalfutureproposallist.htm" style="font-size:15px;">Increment Proposal</a></li>' + '</td>\n\
             </tr><td colspan="2" rowspan="1">' + '<li style="float:left;margin-left:2px;margin-top:10px;font-size:12px;"><a href="monthlyretirement.htm.htm" style="font-size:15px;">Employee Retirement</a></li>' + '</td></tr></table>');
            }
            
             
             
                    
                  
                
            
            $(document).ready(function () {
                
                
            });
            function openWindow(linkurl, modname) {
                $("#win").window("open");

                $("#win").window("setTitle", modname);
                $("#winfram").attr("src", linkurl);
            }


            if ((screen.width == 1920) && (screen.height == 1080)) {
                document.write("<style type='text/css'>");
                document.write("#main-layout {");
                document.write("height:650px;");
                document.write("}");
                document.write(".modGrp{");
                document.write("height:190px;");
                document.write("min-height:190px;");
                document.write("border:#b1c242 solid 1px;  ");
                document.write("width:519px;");
                document.write("margin:1.1em 0.5em 0em 0.5em;");
                document.write("float: left;");
                document.write("}");
                document.write("<\/style>");
            } else if ((screen.width == 1366) && (screen.height == 768)) {
                document.write("<style type='text/css'>");
                document.write("#main-layout {");
                document.write("height:650px;");
                document.write("}");
                document.write(".modGrp{");
                document.write("height:190px;");
                document.write("min-height:190px;");
                document.write("border:#b1c242 solid 1px;  ");
                document.write("width:330px;");
                document.write("margin:1.1em 0.5em 0em 0.5em;");
                document.write("float: left;");
                document.write("}");
                document.write("<\/style>");
            } else if ((screen.width == 1280) && (screen.height == 1024)) {
                document.write("<style type='text/css'>");
                document.write("#main-layout {");
                document.write("height:650px;");
                document.write("}");
                document.write(".modGrp{");
                document.write("height:190px;");
                document.write("min-height:190px;");
                document.write("border:#b1c242 solid 1px;  ");
                document.write("width:500px;");
                document.write("margin:1.1em 0.5em 0em 0.5em;");
                document.write("float: left;");
                document.write("}");
                document.write("<\/style>");
            }
        </script>
        <style>



            .pagingServicList{
                background-color: #e5f0c9;			
                height:23px;
                background-repeat:repeat-x;
                text-align:center;
                padding-top:5px;
                padding-right: 5px;
                vertical-align:middle;
                color:#000000;
                font-family:Arial, Helvetica, sans-serif;
                font-weight:bold;
                font-size:0.9em;        
                text-transform:uppercase;
            }
            .modGrp{
                width: 30%;
                margin-bottom: 5px;
                margin-right: 5px;
                border:#5095ce solid 1px;
                float: left;
            }
            .modGrp span div{
                background-color: #e5f0c9;		
                height:28px;
                background-repeat:repeat-x;
                text-align:center;
                padding-top:5px;
                vertical-align:middle;
                color:#000000;
                font-family:Arial, Helvetica, sans-serif;
                font-weight:bold;
                font-size:0.9em;        
                text-transform:uppercase;       
            }
            .serviceList{
                list-style:none;
                display: block;
                padding:0.5em 0em 0.5em 1em;
                margin:0px;
                height:117px;
                min-height: 117px;
            }
            .serviceList li{
                margin-bottom:5px;
            }
            .serviceList li a{
                text-decoration:none;
                color:#000000;
            }
            .serviceList li a:hover{
                color:#FF6C00;
                font-weight:normal;
            }
        </style>
    </head>
    <body style="padding:0px;">
        <form action="tabController.htm" commandName="tform">
            <input type="hidden" name="offCode" id="hidoffcode" value="${tform.offCode}"/>
            <input type="hidden" name="employeeId" id="hidempId" value="${tform.employeeId}" width="400px"/>
            <input type="hidden" name="rollId" id="rollId" value="${tform.rollId}"/>
            <jsp:include page="topbanner.jsp"/>

            <c:if test="${gisAlertData.size() > 0 }">

                <div style="padding:10px;" class="maintext">
                </c:if>
                <c:if test="${gisAlertData.size() < 1 }">
                    <div style="padding:10px;">
                    </c:if>  


                    <div id="main-layout" class="easyui-layout" style="height:650px;">
                        <div region="west" title="Privilege Panel" split="true" style="width:295px;">
                            <div id="tablist" class="easyui-tabs" fit="true" plain="true" border="false" >
                                <div  title="My Office" style="overflow:auto;" id="myoffice" >
                                    <div  class="easyui-panel" style="padding: 5px;width:100%;">
                                        <ul class="easyui-tree" id="offemptree"></ul>
                                    </div>
                                </div>
                                <div  title="My Page" style="overflow:auto;" id="mypage">
                                </div>
                            </div>
                        </div>
                        <div region="center" id="rightpanel" title="Service Panel">
                        </div>
                        <div region="center" id="rightpanel1" title="Service Panel">
                        </div>

                        <div  id="win" class="easyui-window" title="My Window" data-options="modal:true,closed:true,iconCls:'icon-window',fit:true" style="width:1200px;height:500px;padding:5px;">
                            <iframe id="winfram" frameborder="0" scrolling="yes" marginheight="0" marginwidth="0" height="100%" width="100%">

                            </iframe>
                            
                        </div>
                    </div>
                </div>
               
        </form>


        <script src="js/swc.js"></script>
    </body>
</html>