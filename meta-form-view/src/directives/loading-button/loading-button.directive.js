/**
 * LoadingButton 指令
 *
 * 作用：防止按钮重复提交表单。使用这个指令，会自动给按钮添加以下3个特性：
 *  1、点击按钮后添加等待动画
 *  2、等待点击事件返回后，才可以再次点击（防止重复提交）
 *  3、如果按钮处于禁用状态，不执行点击事件
 *
 *  使用方法：
 *    <a class="btn" centit-loading-button ng-click="click()" ng-disabled="form.$invalid"></a>
 *
 *  注意事项：
 *    click() 必须返回 Promise
 *
 *  参考：http://msurguy.github.io/ladda-bootstrap/
 */
(function() {
  'use strict';

  angular.module('metaForm')
    .directive('centitLoadingButton', LoadingButtonDirective);

  /* @ngInject */
  function LoadingButtonDirective($parse, $timeout, $q) {
    return {
      restrict: 'EA',

      priority: 1,  // 在 ngClick(0) 之前执行

      require: '?ngClick',

      link: function (scope, element, attrs) {
        // 依赖 Ladda @ https://github.com/msurguy/ladda-bootstrap
        if (!window.Ladda) return;

        element.addClass('ladda-button')
          .attr('data-style', 'zoom-out');

        $timeout(function() {
          var loading = Ladda.create(element[0]);

          var oldClickFn = $parse(attrs.ngClick);
          if (!oldClickFn) return;

          // 是否禁用
          var isDisabled = $parse(attrs.ngDisabled);

          // 重新绑定 click 事件
          element.off('click');

          // 防止重复提交
          element.on('click', newClickFn);

          /**
           * 新 click 事件
           * @param e
           */
          function newClickFn(e) {
            e.preventDefault();

            // 如果按钮处于 disable 状态，不继续执行 click 事件
            if (element.attr('disabled')) return;

            loading.start();

            // 基于原有的 ngClick 事件，必须返回 Promise ！！
            oldClickFn(scope)
              .then(function(data) {
                recoverClickEvent();
                $q.resolve(data);
              })
              .catch(function(data) {
                recoverClickEvent();
                $q.reject(data);
              })
          }

          /**
           * 恢复按钮 click 事件
           */
          function recoverClickEvent() {
            loading.stop();

            // 恢复原来的 disabled 状态
            if (isDisabled(scope)) {
              element.attr('disabled', '');
            }

          }

        });
      }
    };
  }
})();
