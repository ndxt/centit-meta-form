

function beforeUpdate(metaObjectService, entity){
    return 0;
}

function beforeSave(metaObjectService, entity){
    //metaObjectService.
    entity.user = 'hi';
    return 0;
}

function beforeDelete(metaObjectService, entity){
    return 0;
}

function beforeSubmit(metaObjectService, entity){
    return 0;
}
