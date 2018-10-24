<%--
  Created by IntelliJ IDEA.
  User: tantian
  Date: 2015/8/22
  Time: 20:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <%--<meta http-equiv="X-UA-Compatible" content="IE=edge">--%>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <%@include file="/common/jstlLib.jsp" %>
    <meta http-equiv="Expires" content="0"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Cache-Control" content="no-cache"/>
    <meta name="author" content="hiteam"/>
    <meta name="copyright" content="hiteam"/>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" href="${path}/resources/admin/log/bootstrap-3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" href="${path}/resources/admin/log/bootstrap-3.3.5/css/docs.min.css">
    <style type="text/css">
        .bs-callout {
            padding-top: 5px;
            padding-bottom: 5px;
            margin-top: 5px;
            margin-bottom: 10px;
        }

        body > .container {
            padding-top: 60px;
        }
    </style>
    <script type="text/javascript" src="${path}/resources/sea-modules/jquery/1.11.2/jquery-1.11.2.min.js"></script>
    <script type="text/javascript" src="${path}/resources/sea-modules/seajs/sea.js"></script>
    <script type="text/javascript" src="${path}/resources/sea-modules/seajs/seajs-preload.js"></script>
    <script type="text/javascript" src="${path}/resources/sea-modules/seajs/init.js"></script>
    <script type="text/javascript">
        var path = '${path}';
        var basePath = '${basePath}';
        var userTypePath = '${sessionScope.userTypePath}';
    </script>

    <script src="${path}/resources/admin/log/bootstrap-3.3.5/js/bootstrap.min.js"></script>
    <script src="${path}/resources/admin/log/infos.js"></script>
</head>
<body>
<header class="navbar navbar-default navbar-fixed-top navbar-static-top" id="top" role="banner">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand">日志</a>
        </div>
        <div id="navbar">
            <ul class="nav navbar-nav">
                <li class="active"><a>首页</a></li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                       aria-expanded="false">日志级别 <span class="caret"></span></a>
                    <ul class="dropdown-menu" id="logLevel">
                    </ul>
                </li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                       aria-expanded="false">日志名称 <span class="caret"></span></a>
                    <ul class="dropdown-menu" id="logName">
                    </ul>
                </li>
            </ul>

            <form class="navbar-form navbar-left" role="search">
                <div class="form-group">
                    <input type="text" class="form-control" id="keyWord" name="keyWord" placeholder="关键字">
                    <input type="text" class="form-control" id="userName" name="userName" placeholder="用户名">
                </div>
                <button class="btn btn-default" type="button" id="btnSubmit">Submit</button>
                <div class="btn-group" role="group" data-toggle="buttons">
                    <button type="button" class="btn btn-sm btn-success" id="btnStart">启动监听</button>
                    <button type="button" class="btn btn-sm btn-success" id="btnClose">停止监听</button>
                </div>
                <button type="button" class="btn btn-sm btn-success" id="btnClean">清理日志</button>
                <button type="button" class="btn btn-sm btn-success" id="btnTest">发送测试</button>
            </form>
        </div>
    </div>
</header>

<div class="container" id="main">
    <%--<div id="logContainer" class="bs-example bs-example-bg-classes" data-example-id="contextual-backgrounds-helpers">--%>
    <div id="logContainer" class="bs-example-bg-classes" data-example-id="contextual-backgrounds-helpers">
    </div>
</div>
<textarea id="templateLog" style="display: none;">
        <div class="log bs-callout {{if level.levelStr == 'INFO'}}bs-callout-info bg-info text-info
        {{else if level.levelStr == 'DEBUG'}}bs-callout-warning bg-warning text-warning
        {{else if level.levelStr == 'WARN'}}bs-callout-danger bg-danger text-danger
        {{else if level.levelStr == 'TRACE'}}bs-callout-primary bg-primary text-primary
        {{else if level.levelStr == 'ERROR'}}bs-callout-danger bg-danger text-danger{{/if}}">
            {{date}} [{{thread}}] {{level.levelStr}}
            {{logger}} -[{{usrName}}] {{message}}
            {{if exceptionMessage}}
                <br/>
                {{#exceptionMessage}}
            {{/if}}
        </div>
</textarea>
<textarea id="templateLog2" style="display: none;">
                <p class="{{if level.levelStr == 'INFO'}}bg-info text-info
        {{else if level.levelStr == 'DEBUG'}}bg-warning text-warning
        {{else if level.levelStr == 'WARN'}}bg-danger text-danger
        {{else if level.levelStr == 'TRACE'}}bg-primary text-primary
        {{else if level.levelStr == 'ERROR'}}bg-danger text-danger{{/if}}">{{date}} [{{thread}}] {{level.levelStr}}
                    {{logger}} -[{{usrName}}] {{message}}</p>
</textarea>
<textarea id="templateLogLevel" style="display:none;">
    {{each}}
        <li data-value="{{$value}}"><a>{{$value}}</a></li>
    {{/each}}
</textarea>
<textarea id="templateLogName" style="display:none;">
    {{each}}
         <li data-value="{{$value}}"><a>{{$value}}</a></li>
    {{/each}}
</textarea>
</body>
</html>
