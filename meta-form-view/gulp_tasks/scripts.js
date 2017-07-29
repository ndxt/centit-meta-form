const gulp = require('gulp');
const eslint = require('gulp-eslint');

const conf = require('../conf/gulp.conf');

gulp.task('scripts', scripts);

function scripts() {
  return gulp.src([conf.path.src('**/*.js'), '!src/themes/bootstrap-material-design/**/*.js'])
    //.pipe(eslint())
    //.pipe(eslint.format())

    .pipe(gulp.dest(conf.path.tmp()));
}
