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
            $(function() {
                // Call After Load
                $('#offemptree').tree({
                    checkbox: false,
                    url: 'employeeTree.htm?id=&empid=' + $("#hidempId").val(),
                    method: 'GET',                    
                    onLoadSuccess: function(node, data) {
                        globalId = $("#hidoffcode").val();
                        loadRightpanel('PA' + $("#hidoffcode").val());
                    },
                    onClick: function(node) {
                        var str = node.id.substr(0, 2);
                        if (str == 'PA' || str == 'RE' || str == 'CO' || str == 'NG' ) {
                            globalId = $("#hidoffcode").val();
                        } else {
                            globalId = node.id;
                        }
                        loadRightpanel(node.id);
                    },
                    formatter: function(node) {
                        var s = node.text;
                        if (node.children) {
                            s += '&nbsp;<span style=\'color:blue\'>(' + node.children.length + ')</span>';
                        }
                        return s;
                    }
                });
                $('#tablist').tabs({
                    onSelect: function(title, index) {
                        if (index == 0) {
                            $('#rightpanel').panel({
                                href: "getRollWiseLink.htm?rollId=" + $("#rollId").val(),
                                loadingMessage: 'Loading...'
                            });
                        } else if (index == 1) {
                            window.location.replace("myPage.htm");
                        }
                    },
                    onLoad: function(rightpanel) {
                        $('#rightpanel').panel({
                            href: "getRollWiseLink.htm?rollId=" + $("#rollId").val(),
                            loadingMessage: 'Loading...'
                        });
                    }

                });



                /*Call After Load*/

                $('#win').window({
                    onClose: function() {

                        $("#win").window("setTitle", '');
                        $("#winfram").attr("src", "");
                    },
                    onBeforeOpen: function() {
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

                $('#rightpanel').panel({
                    href: "getRollWiseLink.htm?nodeID=" + globalId + "&rollId=" + $("#rollId").val(),
                    loadingMessage: 'Loading...',
                    onLoad: function() {
                        var divlen = $('.modGrp').length;
                        /*Add Pagging in Module Group*/

                        for (i = 1; i <= divlen; i++) {
                            $("#modgrouplistpaging").append('<li style="float:left;margin-left:2px;">' + i + '<\/li>');
                        }
                        /*Add Pagging in Module Group*/
                        i = 0;
                        $('.modGrp').each(function() {
                            i++;
                            insideUL = $(this).children('.serviceList').children();
                            var moduleListLen = $(insideUL).length;
                            var noofpage = moduleListLen / 6;
                            if (moduleListLen % 6 > 0 && noofpage > 1) {
                                noofpage++;
                            }
                            if (noofpage > 1) {
                                for (j = 1; j < noofpage; j++) {
                                    $(this).children('.pagingServicList').children().append('<li style="float:left;margin-left:2px;" onclick="displayNextPage(this)"><a href="javascript:displayNextPage(this)">' + j + '<\/a><\/li>');
                                }
                            }
                            j = 0;
                            $(insideUL).each(function() {
                                j++;
                                if (j > 6) {
                                    $(this).hide();
                                }
                            });
                        });
                    }
                });
            }
            $(document).ready(function() {


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
            #loadingMessage {
                width: 100%;
                height: 100%;
                z-index: 1000;
                background: #ccc;
                top: 0px;
                left: 0px;
                position: absolute;
            }

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

                        <div  id="win" class="easyui-window" title="My Window" data-options="modal:true,closed:true,iconCls:'icon-window',fit:true" style="width:1200px;height:500px;padding:5px;">
                            <iframe id="winfram" frameborder="0" scrolling="yes" marginheight="0" marginwidth="0" height="100%" width="100%">

                            </iframe>

                        </div>
                    </div>
                </div>
                <c:if test="${gisAlertData.size() > 0 }">
                    <div id="boxes">

                        <div style="top: 50%; left: 50%;width:600px; display: none;" id="dialog" class="window" > 
                            <div id="san">
                                <c:forEach var="obj" items="${gisAlertData}">
                                    <div class="row" style="margin-bottom: 7px;">
                                        <span style="color:blue">${obj.empName}, ${obj.gfpNo} </span> --  Group Insurance Scheme (GIS) Passbook not yet opened. 
                                    </div>
                                </c:forEach>    
                            </div>
                        </div>
                        <div style="width: 2478px; font-size: 32pt; color:white; height: 1202px; display: none; opacity: 0.4;" id="mask"></div>
                    </div>
                </c:if>  
        </form>


        <script src="js/swc.js"></script>
    </body>
</html>