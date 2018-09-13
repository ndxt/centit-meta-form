const httpProxy = require('http-proxy');

/**
 * api代理配置
 * @type {{middleware: ({route, handle}|*)}}
 */
exports.api = {
  middleware: middleware({
    route: '/api',

    target: 'http://localhost:8081/centit-meta-form',

    cookie: {
      path: '/',
      //domain: 'localhost'
    }
  })
};

/**
 * 代理中间件
 * @param opts
 * @returns {{route: *, handle: Function}}
 */
function middleware(opts) {
  var proxy =  httpProxy.createProxyServer({});

  /**
   * 替换cookie
   * @param type 类型：Path Domain
   * @param cookie
   * @param replace
   * @returns {*}
   */
  function replaceCookie(type, cookie, replace) {
    var regx = eval('/(' + type + ')=.*?(;|$)/gi');

    // cookie中包含
    if (cookie.match(regx)) {
      return cookie.replace(regx, '$1='+replace+'$2')
    }
    // 没有则添加
    else {
      return cookie + '; ' + type + '=' + replace;
    }
  }

  return {
    route: opts.route,

    handle: function(req, res, next) {
      proxy.web(req, res, {
        target: opts.target
      });

      proxy.on('proxyRes', function (proxyRes) {
        var existingCookies = proxyRes.headers['set-cookie'];
        if (!existingCookies) return;

        var path = opts.cookie.path,
            domain = opts.cookie.domain;

        proxyRes.headers['set-cookie'] = existingCookies.map(function(cookie) {

          // 替换 Path
          if (path)
            cookie = replaceCookie('Path', cookie, path);

          // 替换 Domain
          if (domain)
            cookie = replaceCookie('Domain', cookie, domain);

          return cookie;
        });
      });

      proxy.on('error', function () {
        next();
      });
    }
  };
}



