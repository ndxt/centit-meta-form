(function() {
    'use strict';

    angular.module('metaForm')
        .config(RestangularConfig);

    /* @ngInject */
    function RestangularConfig(RestangularProvider) {
        RestangularProvider
            .setBaseUrl('/centit-meta-form/service')
            .setDefaultHeaders({'X-Requested-With': 'XMLHttpRequest'})
            .addResponseInterceptor(transformResponse);

        function transformResponse (data, operation, what, url, res, deferred) {

            var result = data.data, code = data.code;

            // 正确返回
            if (code == 0 && result) {

                // 获取列表
                if (['getList', 'customGETLIST'].indexOf(operation) > -1) {
                    var pageDesc = result.pageDesc, listDesc = result.listDesc;

                    result = result.objList || [];
                    result.pageDesc = pageDesc;
                    result.listDesc = listDesc;

                }


                return result;
            }

            return ['getList', 'customGETLIST'].indexOf(operation) > -1 ? [] : {};
        }
    }
})();
