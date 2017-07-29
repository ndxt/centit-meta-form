(function() {
  'use strict';

  angular.module('metaForm')
    .config(FormlyTypeConfig);

  /* @ngInject */
  function FormlyTypeConfig(formlyConfigProvider) {

    // 只读文本
    formlyConfigProvider.setType(new Text());

    // 单选框
    formlyConfigProvider.setType(new Checkbox());

    // 日期选择框
    formlyConfigProvider.setType(new DatePicker());

    // 月选择框
    formlyConfigProvider.setType(new MonthPicker());

    // 年选择框
    formlyConfigProvider.setType(new YearPicker());


    ///////////////////////////////////

    /**
     * 只读文本
     *
     * @constructor
     */
    function Text() {
      this.name = 'text';

      this.template = '<p class="form-control-static">{{::model[options.key]}}</p>';

      this.wrapper = ['bootstrapLabel'];

    }

    /**
     * Checkbox 配置
     * 为了和后台配合，在原有的checkbox基础上对model值进行了修改
     *
     * 原来：选中 ture  没有选中 false
     * 现在：选中 'T'  没有选中 'F'
     *
     * @constructor
     */
    function Checkbox() {
      this.name = 'checkbox';

      this.templateUrl = 'config/formly/checkbox.html';

      this.controller = controller;

      this.apiCheck = apiCheck;

      ///////////////////////////////////

      /* @ngInject */
      function controller($scope) {
        $scope.update = update;

        ///////////////////////////////////

        /**
         * 根据点击事件更新model值
         *
         * @param $event
         * @returns {string}
         */
        function update($event) {
          var checkbox = $event.target;
          return checkbox.checked ? 'T' : 'F';
        }
      }

      function apiCheck(check) {
        return {
          templateOptions: {
            label: check.string
          }
        };
      }
    }

    /**
     * 日期选择框控件
     * 默认格式为：2016-07-06
     *
     * @constructor
     */
    function DatePicker(options) {
      options = options || {};

      // 日期格式化
      var format = options.format || 'yyyy-MM-dd',
        momentFormat = options.momentFormat || 'YYYY-MM-DD',
        clearText = options.clearText || '清除',
        closeText = options.closeText || '关闭',
        currentText = options.currentText || '今日',
        minMode = options.minMode,
        maxMode = options.maxMode;

      this.name = 'datePicker';

      this.templateUrl = 'config/formly/datepicker.html';

      this.wrapper = ['bootstrapLabel', 'bootstrapHasError'];

      this.controller = controller;

      this.defaultOptions = {
        templateOptions: {
          datepickerOptions: {
            format: format,
            initDate: new Date(),
            minMode: minMode,
            maxMode: maxMode
          }
        }
      };

      ///////////////////////////////////

      /* @ngInject */
      function controller($scope) {
        var options = $scope.options,
          key = options.key,
          model = $scope.model, value = model[key];

        var datepicker = $scope.datepicker = {
          date: new Date(value),
          clearText: clearText,
          closeText: closeText,
          currentText: currentText,
          opened: false, // 日期框是否打开
          change: change, // 日期改变
          toggle: toggle // 打开、关闭日期框
        };

        ///////////////////////////////////

        /**
         * 日期改变时，将日期框的日期对象，转换成所需格式的字符串
         */
        function change() {

          if (datepicker.date) {
            model[key] = moment(datepicker.date).format(momentFormat);
          }
          // 清除日期时
          else {
            model[key] = undefined;
          }

        }

        /**
         * 打开、关闭日期框
         */
        function toggle() {
          datepicker.opened = !datepicker.opened;
        }
      }
    }

    /**
     * 月选择框
     * @constructor
     */
    function MonthPicker() {

      DatePicker.call(this, {
        format: 'MM',
        momentFormat: 'MM',
        currentText: '当月',
        minMode: 'month',
        maxMode: 'month'
      });

      this.name = 'monthPicker';
    }

    /**
     * 年选择框
     * @constructor
     */
    function YearPicker() {

      DatePicker.call(this, {
        format: 'yyyy',
        momentFormat: 'YYYY',
        currentText: '今年',
        minMode: 'year',
        maxMode: 'year'
      });

      this.name = 'yearPicker';
    }

  }
})();
