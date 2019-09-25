// 返回值
// 0  为执行成功；后续的数据写入操作继续执行
// <0 为错误编号；后续的数据库写入操作被终止
// >0 为执行成功；后续的数据写入操作无需运行

// 方法的第一个参数为 事件的运行环境，第二个参数为 业务对象

function beforeUpdate(eventRuntime, entity){
    return 0;
}

function beforeSave(eventRuntime, entity){
    return 0;
}

function beforeDelete(eventRuntime, entity){
    return 0;
}

function beforeSubmit(eventRuntime, entity){
    return 0;
}
