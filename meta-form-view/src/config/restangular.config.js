(function() {
  'use strict';

  angular.module('metaForm')
    .run(RestangularConfig);

  /* @ngInject */
  function RestangularConfig(Restangular, toastr, $state) {
    Restangular
      .setBaseUrl('/api')
      .setDefaultHeaders({'X-Requested-With': 'XMLHttpRequest'})
      .addResponseInterceptor(transformResponse)
      .setErrorInterceptor(errorInterceptor);

    function errorInterceptor(response, deferred, responseHandler) {

      var status = response.status;

      switch(status) {
        case -1:
          toastr.error('网络超时，请稍后再试！');
          break;
        case 403:
          toastr.error('没有权限，请登录后重试！');
          $state.go('login');
          break;
        case 404:
          toastr.error('无效的链接！');
          break;
        default:
          toastr.error('系统错误！');
      }

      //console.error(response.data, status, response.statusText);

      return deferred.reject();
    }

    /**
     * 解析响应
     *
     * @param data
     * @param operation
     * @param what
     * @param url
     * @param res
     * @param deferred
     * @returns {*}
     */
    function transformResponse (data, operation, what, url, res, deferred) {

      var result = data.data, code = data.code;

      // 正确返回
      if (code == 0) {

        // 获取列表
        if (['getList', 'customGETLIST'].indexOf(operation) > -1) {
          result = result || {};
          var pageDesc = result.pageDesc, formModel = result.formModel;

          // 对于列表，如果没有数据必须要返回空数组，否则后续方法会报错
          result = result.objList || [];
          result.pageDesc = pageDesc;
          result.formModel = formModel;

        }

        return result;
      }

      // 自定义后台错误
      else {
        var message = data.message || '系统错误';
        console.error(message + "：", url, res);
        toastr.error(message);
        return deferred.reject(data);
      }

    }
  }
})();
