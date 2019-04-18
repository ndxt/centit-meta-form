function beforeUpdate(metaObjectService, entity){
    return 0;
}

function beforeSave(metaObjectService, entity){
    entity.user = 'hi';
    return 0;
}

function beforeDelete(metaObjectService, entity){
    return 0;
}
