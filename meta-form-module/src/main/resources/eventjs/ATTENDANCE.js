// 返回值
// 0  为执行成功；后续的数据写入操作继续执行
// <0 为错误编号；后续的数据库写入操作被终止
// >0 为执行成功；后续的数据写入操作无需运行

function beforeUpdate(mateObjectEvent, entity){
    return 0;
}

function beforeSave(mateObjectEvent, entity){
    var optType = mateObjectEvent.getRequestAttribute("optType");
    var allUsers = mateObjectEvent.metaObjectService.listObjectsByProperties("F_USER_INFO", {isVaild : "T" } );
    for( i=0; i<allUsers.length; i++) {
        var user = allUsers[i];
        var checkinList = mateObjectEvent.metaObjectService.listObjectsByProperties("BcXJGx6VTnWHg9_C4YobpQ",
                {
                    attendanceCardno : user.userWord,
                    attendanceTime_ge: date(),
                    attendanceTime_lt: date()+1
                })
        mateObjectEvent.databaseRuntime.query("0000124");
        mateObjectEvent.databaseRuntime.execute("0000124");
        var obj = {};
        mateObjectEvent.metaObjectService.saveObject("",obj)
    };
    return 1;
}

function beforeDelete(mateObjectEvent, entity){
    return 0;
}

function beforeSubmit(mateObjectEvent, entity){
    return 0;
}
