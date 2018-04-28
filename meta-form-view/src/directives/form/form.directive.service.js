(function() {
  'use strict';

  angular.module('metaForm')
    .factory('MetaFormService', MetaFormInfoService);

  /* @ngInject */
  function MetaFormInfoService($q, FormAPI, $state) {

    return {

      load: _load,

      submit: _submit,

      back: _back

    };

    //////////////////////////////////////////////////////////////////////

    function _back() {
      console.log($state);
    }

    /**
     * 加载数据
     * @param options
     * @private
     * @return Promise {data, fields}
     */
    function _load(options) {

      var modelCode = options.modelCode,
        operation = options.operation,
        primaryData = options.primaryData;

      return FormAPI.one(modelCode)
        .customGET(operation, primaryData)
        .then(function(data) {
          var formModel = data.formModel || {};

          // 重新定义数据格式
          return $q.resolve({
            data: data.obj || {},
            subModelCode:data.subModelCode||[],
            fields: formModel.fields ? formModel.fields : [],
            formType: formModel.formType,
            modelName: formModel.modelName
          });
        });
    }

    /**
     * 提交数据
     * @param options
     * @private
     */
    function _submit(options) {
      var modelCode = options.modelCode,
        operation = options.operation,
        data = options.data;

      switch (operation) {
        // 创建
        case 'create' :
          return _create(modelCode, data);
          break;
        // 修改
        case 'edit' :
          return _edit(modelCode, data);
          break;
        // 删除
        case 'delete' :
          return _delete(modelCode, data);
          break;
        // 不存在的操作直接返回
        default :
          return $q.resolve();
      }
    }

    /**
     * 创建对象
     * @param modelCode
     * @param data
     * @private
     */
    function _create(modelCode, data) {
      return FormAPI.one(modelCode)
        .customPOST(data, 'save');
    }

    /**
     * 修改对象
     *
     * @param modelCode
     * @param data
     * @returns {*}
     * @private
     */
    function _edit(modelCode, data) {
      return FormAPI.one(modelCode)
        .customPUT(data, 'update');
    }

    /**
     * 删除对象
     *
     * @param modelCode
     * @param data
     * @returns {*}
     * @private
     */
    function _delete(modelCode, data) {
      return FormAPI.one(modelCode)
        .customDELETE('delete', data);
    }
  }
})();
