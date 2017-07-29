const conf = require('./gulp.conf');
const proxy = require('./proxy.conf');

module.exports = function () {

  return {
    server: {
      baseDir: [
        conf.paths.tmp,
        conf.paths.src
      ],
      routes: {
        '/bower_components': 'bower_components'
      },
      middleware: [proxy.api.middleware]
    },
    open: false
  };
};
