<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <title>${message("org.register.mailTitle")}</title>
    <meta name="author" content="hiteam" />
    <meta name="copyright" content="hiteam" />
</head>
<body>
<p>${username}:</p>
<p>${message("org.register.welcome")}</p>
<p>${message("org.register.content")}</p>
<p>
    <a href="${config("siteUrl")}/org/register/active?username=${username}&key=${token.token}" target="_blank">${config("siteUrl")}/org/register/active?username=${username}&key=${token.token}</a>
</p>
<p>${config("siteName")}</p>
<p>${.now?string("yyyy-MM-dd")}</p>
</body>
</html>