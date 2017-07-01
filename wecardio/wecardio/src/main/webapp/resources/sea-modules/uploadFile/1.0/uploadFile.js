/**
 * 文件上传模块
 */
define(
    'uploadFile/1.0/uploadFile',
    ['$', 'msgBox/pc/1.0/msgBox'],
    function (require, exports, module) {
        var msgBox = require('msgBox/pc/1.0/msgBox');

        var uploadFile = {
            /**
             * 打开上传界面
             * @param options
             * {
             *  relTable:'T_PRODUCE_INFO',
             *  relId:'',关联的业务表主键ID，
             *  guid:'',
             *  fileSizeLimit:'5M',允许上传的文件大小
             *  multi:true,//是否允许多文件上传
             *  fileTypeExts : '*.jpg;*.png;*.bmp',//上传允许的文件类型
             *  close:function(data){},//上传界面关闭的回调函数
             *  handler:function(data){},上传时，动态回调函数，返回上传的文件信息
             *  }
             */
            open: function (options) {
                //打开上传窗口
                msgBox.exWindow.open({
                    title: '上传附件',
                    url: path + '/admin/file/toPage/uploadFile?' +
                    'relTable='+(options.relTable||'') +"" +
                    '&relId='+(options.relId||'') +"" +
                    '&guid='+(options.guid||'') +"" +
                    "&fileSizeLimit=" + (options.fileSizeLimit || '') + '' +
                    '&multi='+ (options.multi || '') +'' +
                    '&fileTypeExts='+(options.fileTypeExts || ''),
                    width: '400px',
                    height: '400px',
                    handler: function (data) {
                        if(options.handler){
                            options.handler && options.handler(data);
                        }
                    },
                    close: function (data) {
                       if(options.close){
                           options.close && options.close(data);
                       }
                    }
                });
            }
        };

        module.exports = uploadFile;
    }
);