/**
 * Created by Sebarswee on 2015/6/29.
 */
define('webuploader/0.1.5/baiduUploader', ['$', 'webuploader/0.1.5/webuploader.css', 'webuploader/0.1.5/webuploader'], function (require, exports, module) {
        require('webuploader/0.1.5/webuploader');
        module.exports = {
            create : function (options) {
                return WebUploader.create($.extend({
                    swf : 'Uploader.swf'
                }, options));
            }
        };
    }
);