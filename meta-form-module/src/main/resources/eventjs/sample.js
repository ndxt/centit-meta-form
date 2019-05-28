// 返回值
// 0  为执行成功；后续的数据写入操作继续执行
// <0 为错误编号；后续的数据库写入操作被终止
// >0 为执行成功；后续的数据写入操作无需运行

function beforeUpdate(mateObjectEvent, entity){
    return 0;
}

function beforeSave(mateObjectEvent, entity){
    return 0;
}

function beforeDelete(mateObjectEvent, entity){
    return 0;
}

function beforeSubmit(mateObjectEvent, entity){
    return 0;
}
