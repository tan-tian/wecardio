/* JSONPath 0.8.0 - XPath for JSON
 *
 * Copyright (c) 2007 Stefan Goessner (goessner.net)
 * Licensed under the MIT (MIT-LICENSE.txt) licence.
 */

var isNode = false;

if (!Array.prototype.map) {
    Array.prototype.map = function (fn) {
        var a = [];
        for (var i = 0; i < this.length; i++) {
            var value = fn(this[i], i);
            if (value == null) {
                continue; //如果函数fn返回null，则从数组中删除该项
            }
            a.push(value);
        }
        return a;
    }
}

if (!Array.prototype.indexOf) {
    Array.prototype.indexOf = function (el, start) {
        var start = start || 0;
        for (var i = 0; i < this.length; ++i) {
            if (this[i] === el) {
                return i;
            }
        }
        return -1;
    };
}

if (!Array.prototype.indexOf) {
    Array.prototype.indexOf = function (el, start) {
        var start = start || 0;
        for (var i = 0; i < this.length; ++i) {
            if (this[i] === el) {
                return i;
            }
        }
        return -1;
    };
}

if (!Array.prototype.forEach) {
    Array.prototype.forEach = function (fn, thisObj) {
        var scope = thisObj || window;
        for (var i = 0, j = this.length; i < j; ++i) {
            fn.call(scope, this[i], i, this);
        }
    };
}

if (!Array.prototype.every) {
    Array.prototype.every = function (fn, thisObj) {
        var scope = thisObj || window;
        for (var i = 0, j = this.length; i < j; ++i) {
            if (!fn.call(scope, this[i], i, this)) {
                return false;
            }
        }
        return true;
    };
}

if (!Array.prototype.some) {
    Array.prototype.some = function (fn, thisObj) {
        var scope = thisObj || window;
        for (var i = 0, j = this.length; i < j; ++i) {
            if (fn.call(scope, this[i], i, this)) {
                return true;
            }
        }
        return false;
    };
}

if (!Array.prototype.filter) {
    Array.prototype.filter = function (fn, thisObj) {
        var scope = thisObj || window;
        var a = [];
        for (var i = 0, j = this.length; i < j; ++i) {
            if (!fn.call(scope, this[i], i, this)) {
                continue;
            }
            a.push(this[i]);
        }
        return a;
    };
}

if (!Array.prototype.map) {
    Array.prototype.map = function (fn, thisObj) {
        var scope = thisObj || window;
        var a = [];
        for (var i = 0, j = this.length; i < j; ++i) {
            a.push(fn.call(scope, this[i], i, this));
        }
        return a;
    };
}

if (!Array.prototype.reduce) {
    Array.prototype.reduce = function (fun /*, initial*/) {
        var len = this.length >>> 0;
        if (typeof fun != "function")
            throw new TypeError();
        if (len == 0 && arguments.length == 1)
            throw new TypeError();
        var i = 0;
        if (arguments.length >= 2) {
            var rv = arguments[1];
        } else {
            do {
                if (i in this) {
                    rv = this[i++];
                    break;
                }
                if (++i >= len)
                    throw new TypeError();
            } while (true);
        }
        for (; i < len; i++) {
            if (i in this)
                rv = fun.call(null, rv, this[i], i, this);
        }
        return rv;
    };
}


define('jsonPath/0.8.0/jsonPath', function (require, exports, module) {
// Keep compatibility with old browsers
    if (!Array.isArray) {
        Array.isArray = function (vArg) {
            return Object.prototype.toString.call(vArg) === "[object Array]";
        };
    }

// Make sure to know if we are in real node or not (the `require` variable
// could actually be require.js, for example.
    var isNode = typeof module !== 'undefined' && !!module.exports;

    var vm = isNode ?
        require('vm') : {
        runInNewContext: function (expr, context) {
            with (context) return eval(expr);
        }
    };


    var cache = {};

    function push(arr, elem) {
        arr = arr.slice();
        arr.push(elem);
        return arr;
    }

    function unshift(elem, arr) {
        arr = arr.slice();
        arr.unshift(elem);
        return arr;
    }

    function jsonPath(obj, expr, arg) {
        var P = {
            resultType: arg && arg.resultType || "VALUE",
            flatten: arg && arg.flatten || false,
            wrap: (arg && arg.hasOwnProperty('wrap')) ? arg.wrap : true,
            sandbox: (arg && arg.sandbox) ? arg.sandbox : {},
            normalize: function (expr) {
                if (cache[expr]) return cache[expr];
                var subx = [];
                var normalized = expr.replace(/[\['](\??\(.*?\))[\]']/g, function ($0, $1) {
                    return "[#" + (subx.push($1) - 1) + "]";
                })
                    .replace(/'?\.'?|\['?/g, ";")
                    .replace(/(;)?(\^+)(;)?/g, function (_, front, ups, back) {
                        return ';' + ups.split('').join(';') + ';';
                    })
                    .replace(/;;;|;;/g, ";..;")
                    .replace(/;$|'?\]|'$/g, "");
                var exprList = normalized.split(';').map(function (expr) {
                    var match = expr.match(/#([0-9]+)/);
                    return !match || !match[1] ? expr : subx[match[1]];
                });
                return cache[expr] = exprList;
            },
            asPath: function (path) {
                var x = path, p = "$";
                for (var i = 1, n = x.length; i < n; i++)
                    p += /^[0-9*]+$/.test(x[i]) ? ("[" + x[i] + "]") : ("['" + x[i] + "']");
                return p;
            },
            trace: function (expr, val, path) {
                // no expr to follow? return path and value as the result of this trace branch
                if (!expr.length) return [{path: path, value: val}];

                var loc = expr[0], x = expr.slice(1);
                // the parent sel computation is handled in the frame above using the
                // ancestor object of val
                if (loc === '^') return path.length ? [{path: path.slice(0, -1), expr: x, isParentSelector: true}] : [];

                // we need to gather the return value of recursive trace calls in order to
                // do the parent sel computation.
                var ret = [];

                function addRet(elems) {
                    ret = ret.concat(elems);
                }

                if (val && val.hasOwnProperty(loc)) // simple case, directly follow property
                    addRet(P.trace(x, val[loc], push(path, loc)));
                else if (loc === "*") { // any property
                    P.walk(loc, x, val, path, function (m, l, x, v, p) {
                        addRet(P.trace(unshift(m, x), v, p));
                    });
                }
                else if (loc === "..") { // all chid properties
                    addRet(P.trace(x, val, path));
                    P.walk(loc, x, val, path, function (m, l, x, v, p) {
                        if (typeof v[m] === "object")
                            addRet(P.trace(unshift("..", x), v[m], push(p, m)));
                    });
                }
                else if (loc[0] === '(') { // [(expr)]
                    addRet(P.trace(unshift(P.eval(loc, val, path[path.length], path), x), val, path));
                }
                else if (loc.indexOf('?(') === 0) { // [?(expr)]
                    P.walk(loc, x, val, path, function (m, l, x, v, p) {
                        if (P.eval(l.replace(/^\?\((.*?)\)$/, "$1"), v[m], m, path))
                            addRet(P.trace(unshift(m, x), v, p));
                    });
                }
                else if (loc.indexOf(',') > -1) { // [name1,name2,...]
                    for (var parts = loc.split(','), i = 0; i < parts.length; i++)
                        addRet(P.trace(unshift(parts[i], x), val, path));
                }
                else if (/^(-?[0-9]*):(-?[0-9]*):?([0-9]*)$/.test(loc)) { // [start:end:step]  python slice syntax
                    addRet(P.slice(loc, x, val, path));
                }

                // we check the resulting values for parent selections. for parent
                // selections we discard the value object and continue the trace with the
                // current val object
                return ret.reduce(function (all, ea) {
                    return all.concat(ea.isParentSelector ? P.trace(ea.expr, val, ea.path) : [ea]);
                }, []);
            },
            walk: function (loc, expr, val, path, f) {
                if (Array.isArray(val))
                    for (var i = 0, n = val.length; i < n; i++)
                        f(i, loc, expr, val, path);
                else if (typeof val === "object")
                    for (var m in val)
                        if (val.hasOwnProperty(m))
                            f(m, loc, expr, val, path);
            },
            slice: function (loc, expr, val, path) {
                if (!Array.isArray(val)) return;
                var len = val.length, parts = loc.split(':'),
                    start = (parts[0] && parseInt(parts[0])) || 0,
                    end = (parts[1] && parseInt(parts[1])) || len,
                    step = (parts[2] && parseInt(parts[2])) || 1;
                start = (start < 0) ? Math.max(0, start + len) : Math.min(len, start);
                end = (end < 0) ? Math.max(0, end + len) : Math.min(len, end);
                var ret = [];
                for (var i = start; i < end; i += step)
                    ret = ret.concat(P.trace(unshift(i, expr), val, path));
                return ret;
            },
            eval: function (code, _v, _vname, path) {
                if (!$ || !_v) return false;
                if (code.indexOf("@path") > -1) {
                    P.sandbox["_path"] = P.asPath(path.concat([_vname]));
                    code = code.replace(/@path/g, "_path");
                }
                if (code.indexOf("@") > -1) {
                    P.sandbox["_v"] = _v;
                    code = code.replace(/@/g, "_v");
                }
                try {
                    return vm.runInNewContext(code, P.sandbox);
                }
                catch (e) {
                    console.log(e);
                    throw new Error("jsonPath: " + e.message + ": " + code);
                }
            }
        };

        var $ = obj;
        var resultType = P.resultType.toLowerCase();
        if (expr && obj && (resultType == "value" || resultType == "path")) {
            var exprList = P.normalize(expr);
            if (exprList[0] === "$" && exprList.length > 1) exprList.shift();
            var result = P.trace(exprList, obj, ["$"]);
            result = result.filter(function (ea) {
                return ea && !ea.isParentSelector;
            });
            if (!result.length) return P.wrap ? [] : false;
            if (result.length === 1 && !P.wrap && !Array.isArray(result[0].value)) return result[0][resultType] || false;
            return result.reduce(function (result, ea) {
                var valOrPath = ea[resultType];
                if (resultType === 'path') valOrPath = P.asPath(valOrPath);
                if (P.flatten && Array.isArray(valOrPath)) {
                    result = result.concat(valOrPath);
                } else {
                    result.push(valOrPath);
                }
                return result;
            }, []);
        }
    }

    module.exports = jsonPath;
});