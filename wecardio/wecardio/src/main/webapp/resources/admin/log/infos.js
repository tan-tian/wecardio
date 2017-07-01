seajs.use(['$', 'template'], function ($, template) {
    var socket = null;

    $(function () {
        initData();
        initEvent();
    });

    function initEvent() {

        $('#btnStart').click(function () {

            if (socket == null) {
                initWebSocket();
            }
        });

        $('#btnClose').click(function () {
            closeWebSocket();
        });

        $('#btnTest').click(function () {
            sendTestMessage();
        });

        $('#btnClean').click(function () {
            $('.log').remove();
        });

        $('#logLevel').on('click', 'li', function () {
            var level = $(this).attr('data-value');
            sendMessage({level: level});
            $('#btnClean').click();
        });

        $('#logName').on('click', 'li', function () {
            var logger = $(this).attr('data-value');
            sendMessage({logger: logger});
            $('#btnClean').click();
        });

        $('#btnSubmit').click(function () {
            sendMessage({
                keyWord: $('#keyWord').val(),
                userName: $('#userName').val()
            });
            $('#btnClean').click();
        });
    }

    function initData() {
        initWebSocket();

        var logLevel = ["ALL", "TRACE", "DEBUG", "INFO", "WARN", "ERROR"];
        var html = template('templateLogLevel', logLevel);
        $('#logLevel').append(html);

        var logName = ["ALL", "web-access", "druid.sql", "druid.sql.DataSource",
            "druid.sql.Connection", "druid.sql.Statement", "druid.sql.ResultSet"];
        var html = template('templateLogName', logName);
        $('#logName').append(html);
    }

    function initWebSocket() {
        socket = new WebSocket('ws://' + window.location.host + path + '/admin/websocket/log/infos');

        socket.onopen = function () {
            console.log('Info: connection opened.');
        };
        socket.onmessage = function (event) {
            logInfo(event.data);
        };
        socket.onclose = function (event) {
            console.log('Info: connection closed.');
            console.log(event);
            closeWebSocket();
        };
    }

    function closeWebSocket() {
        if (socket != null) {
            socket.close();
            socket = null;
        }
    }

    function logInfo(msg1) {
        var msg = '';
        try {
            msg = $.parseJSON(msg1);
            if (msg.level.levelStr == 'ERROR') {
                notify(msg.level.levelStr + ',' + msg.date, msg.message);
            }
        } catch (e) {
            alert('----' + msg1);
            return;
        }
        var html = template('templateLog', msg);
        $('#logContainer').append(html);
        var h = $(document).height() - $(window).height();
        $(document).scrollTop(h);
    }

    function sendTestMessage() {
        sendMessage({testMessage: 'test.....'});
    }

    function sendMessage(message) {
        if (socket != null) {
            socket.send(JSON.stringify(message));
        }
    }

    function notify(title, content) {

        if (!title && !content) {
            title = "桌面提醒";
            content = "您看到此条信息桌面提醒设置成功";
        }
        var iconUrl = path + "/resources/images/fail.png";
        if (window.webkitNotifications) {
            //chrome老版本
            if (window.webkitNotifications.checkPermission() == 0) {
                var notif = window.webkitNotifications.createNotification(iconUrl, title, content);
                notif.display = function () {
                };
                notif.onerror = function () {
                };
                notif.onclose = function () {
                };
                notif.onclick = function () {
                    this.cancel();
                };
                notif.replaceId = 'Meteoric';
                notif.show();
            } else {
                window.webkitNotifications.requestPermission($jy.notify);
            }
        }
        else if ("Notification" in window) {
            // 判断是否有权限
            if (Notification.permission === "granted") {
                var notification = new Notification(title, {
                    "icon": iconUrl,
                    "body": content
                });
            }
            //如果没权限，则请求权限
            else if (Notification.permission !== 'denied') {
                Notification.requestPermission(function (permission) {
                    // Whatever the user answers, we make sure we store the
                    // information
                    if (!('permission' in Notification)) {
                        Notification.permission = permission;
                    }
                    //如果接受请求
                    if (permission === "granted") {
                        var notification = new Notification(title, {
                            "icon": iconUrl,
                            "body": content
                        });
                    }
                });
            }
        }
    }
});