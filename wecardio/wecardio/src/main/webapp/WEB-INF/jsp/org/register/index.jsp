<%--
  User: Sebarswee
  Date: 2015/6/24
  Time: 10:58
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/headModule.jsp" %>
    <title><spring:message code="system.name"/> | <spring:message code="org.register.title"/></title>
    <link rel="stylesheet" type="text/css" href="${path}/resources/css/zhuce.css" />
    <link rel="stylesheet" type="text/css" href="${path}/resources/org/register/css/register.css" />
    <script type="text/javascript">
        seajs.use(['$', 'validate', 'msgBox', 'base64', 'rsa'], function ($, validate, msgBox, base64, rsa) {
            // 刷新验证码
            $("#captchaImage").on('click', function() {
                $(this).attr("src", path + "/guest/common/captcha?d=" + (new Date()).valueOf());
            });
            $('div.yanzhengma').on('click', function() {
                $(this).children('img').attr("src", path + "/guest/common/captcha?d=" + (new Date()).valueOf());
            });
            // 同意协议
            $(".mainLi7Link").on('click', function() {
                $(this).toggleClass("dianji");
            });

            // 验证
            var $form = $('#registerForm');
            $form.validate({
                rules : {
                    email : {
                        required : true,
                        email : true,
                        remote : {
                            url: path + "/org/register/checkEmail",
                            cache : false
                        }
                    },
                    password: {
                        required: true,
                        minlength: 6
                    },
                    rePassword: {
                        required: true,
                        equalTo: "#password"
                    },
                    mobile : {
                        required : true
                    },
                    captcha : {
                        required : true
                    }
                },
                messages : {
                    email : {
                        remote : "<spring:message code="org.register.exist"/>"
                    }
                },
                errorClass : "fieldError",
                errorPlacement : function(error, element) {
                    error.insertAfter(element.parent());
                }
            });

            // 登录按钮事件
            $('#submitBtn').on('click', function() {
                // 验证成功后
                if ($form.valid()) {
                    if (!$('a.mainLi7Link').hasClass('dianji')) {
                        msgBox.warn("<spring:message code="org.register.agree"/>", null, null);
                        return false;
                    }

                    $.ajax({
                        url : "${path}//guest/common/publicKey",
                        type : "GET",
                        dataType : "json",
                        cache : false,
                        beforeSend : function() {
                        },
                        success: function(data) {
                            var $password = $("#password");
                            var rsaKey = new rsa.RSAKey();
                            rsaKey.setPublic(base64.b64tohex(data['modulus']), base64.b64tohex(data['exponent']));
                            var enPassword = base64.hex2b64(rsaKey.encrypt($password.val()));
                            $.post($form.attr('action'), {
                                email : $('#email').val(),
                                enPassword : enPassword,
                                mobile : $('#mobile').val(),
                                captcha : $('#captcha').val()
                            }, function (data) {
                                if (data.type == 'success') {
                                    msgBox.tips(data['content'], 1, function() {
                                        window.location.href = path + '/org';
                                    });
                                } else {
                                    msgBox.warn(data['content'], null, null);
                                    $("#captchaImage").trigger('click');
                                    $('#captcha').val('');
                                }
                            });
                        }
                    });
                }
            });
        });
    </script>
</head>
<body>
<div class="zhuceDiv">
    <div class="zhuceTop">
        <div class="logo">
            <img src="${path}/resources/images/logo.png" width="677" height="90" />
        </div>
    </div>
    <div class="zhuceCenter">
        <form id="registerForm" action="${path}/org/register/submit" method="post">
            <ul class="mainUl">
                <li class="mainLi1">
                    <a href="${path}/org/login"><spring:message code="org.register.login"/></a>
                </li>
                <li class="mainLi5">
                    <input type="text" id="email" name="email" placeholder="<spring:message code="org.register.mail"/>" />
                </li>
                <li class="mainLi3">
                    <input type="password" id="password" name="password" autocomplete="off" placeholder="<spring:message code="org.register.password"/>" />
                </li>
                <li class="mainLi3">
                    <input type="password" name="rePassword" autocomplete="off" placeholder="<spring:message code="org.register.rePassword"/>" />
                </li>
                <li class="mainLi4">
                    <input type="text" id="mobile" name="mobile" placeholder="<spring:message code="org.register.mobile"/>" />
                </li>
                <li class="mainLi6">
                    <input type="text" id="captcha" name="captcha" autocomplete="off" placeholder="<spring:message code="org.register.captcha"/>" />
                    <div class="yanzhengma">
                        <img id="captchaImage" height="42" width="153" src="${path}/guest/common/captcha" title="<spring:message code="org.captcha.imageTitle"/>"/>
                    </div>
                </li>
                <li class="mainLi7">
                    <a class="mainLi7Link"></a><a><spring:message code="org.register.agreement"/></a>
                </li>
                <li class="mainLi8">
                    <a class="mainLi8Link" id="submitBtn"><spring:message code="org.register.submit"/></a>
                </li>
            </ul>
        </form>
    </div>

    <div class="zhuceBottom">
        <div class="zhuceBottomCenter">
            <div class="zhuceBottomLeft"><spring:message code="system.footer.copyright"/></div>
            <div class="zhuceBottomRight"><spring:message code="system.footer.support"/></div>
        </div>
    </div>
</div>
</body>
</html>
