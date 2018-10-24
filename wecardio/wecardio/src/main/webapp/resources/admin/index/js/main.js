/**
 * Created by tantian on 2015/6/18.
 */
seajs.use(['msgBox','template'], function (msgBox, template) {
    $(document).ready(function () {
        initMenuEvent();

        $('a.ico_set').on('click', function () {
            msgBox.exWindow.open({
                title : '&nbsp;',
                width : '430px',
                height : '325px',
                url : path + '/admin/password/reset'
            });
        });

        var langs = [{name: '中文', local: 'zh_CN'}, {name: 'English', local: 'en_US'}];
        var templateData = {currentLangName: '中文', langs: langs};

        for(var i = 0; i < langs.length; i++){
            if(langs[i].local == language) {
                templateData.currentLangName = langs[i].name;
            }
        }

        var html = template('templateLang', templateData);

        $('#langContent').append(html)
            .on('click', '.language', function () {
                var lang = $(this).attr('data-value-lang');
                $.post(path + '/' + userTypePath + '/global/switch/lang', {language: lang}, function () {
                    window.location.reload();
                });
            });
    });

    /**
     * 菜单事件
     */
    function initMenuEvent() {
        var $menuBox = $("div.menuUlBoxHead");
        var $subMenu = $("ul.menuBtListUl li");
        $menuBox.on('click', function() {
            var $this = $(this);
            var clickTyp = parseInt($this.attr('clickType'));
            if (clickTyp == 0) {

            } else if (clickTyp == 1) {

            } else if (clickTyp == 2) {
                // 有子级菜单，先展开子级菜单
                $("ul.menuBtListUl").slideUp('fast');
                $this.parents("div.menuUlBox").find("ul.menuBtListUl").slideDown('fast');

                $menuBox.removeClass("menuUlBoxHeadOn");
                $menuBox.find('h1').removeClass("up");
                $this.addClass("menuUlBoxHeadOn");
                $this.find('h1').addClass("up");
            } else if (clickTyp == 3) {
                // 只有一级菜单，直接打开时
                $("ul.menuBtListUl").slideUp('fast');

                $menuBox.find('h1').removeClass("up");
                $menuBox.removeClass("menuUlBoxHeadOn");
                $this.addClass("menuUlBoxHeadOn");
                $this.find('h1').addClass("up");

                var url = $(this).attr('hrefvalue');
                $("#iframe").attr('src', url);
            }
        });
        // 子级菜单点击事件
        $subMenu.click(function() {
            var $this = $(this);
            $subMenu.removeClass("lived");
            $this.addClass("lived");
            var url = $this.attr('hrefvalue');
            $("#iframe").attr('src',url);
        });
    }
});

function setFrameHeight() {
    var ifm= document.getElementById("iframe");
    var subWeb = document.frames ? document.frames["iframe"].document : ifm.contentDocument;

    if (ifm != null && subWeb != null) {
        try {
            var minHeight = $('#listBox').parent().height() + 25;
            ifm.height = Math.max(Math.min(subWeb.body.scrollHeight, subWeb.documentElement.scrollHeight), minHeight);
        } catch (e) {
        }
    }
}
window.setInterval("setFrameHeight()", 200);

$(function() {

    $("div.xw_toggleLanguage").click(function(){
        $(this).find(".xw_toggleLanguageSlider").slideDown();
    });
    $("div.xw_toggleLanguage").mouseleave(function(){
        $(this).find(".xw_toggleLanguageSlider").slideUp();
    });
    $("h3.xw_language").click(function(){
        var textVal = $(this).html();
        $(this).parents("div.xw_toggleLanguage").find("h2.textH").html(textVal);
    });
});