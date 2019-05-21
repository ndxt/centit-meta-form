

function beforeUpdate(mateObjectEvent, entity){
    return 0;
}

function beforeSave(mateObjectEvent, entity){
    //metaObjectService.
    entity.user = 'hi';
    return 0;
}

function beforeDelete(mateObjectEvent, entity){
    return 0;
}

function beforeSubmit(mateObjectEvent, entity){
    return 0;
}
