// 返回值
// 0  为执行成功；后续的数据写入操作继续执行
// <0 为错误编号；后续的数据库写入操作被终止
// >0 为执行成功；后续的数据写入操作无需运行

function beforeUpdate(mateObjectEvent, entity){
    return 0;
}

function beforeSave(mateObjectEvent, entity){
    // var optType = mateObjectEvent.getRequestAttribute("optType");
    // var allUsers = mateObjectEvent.metaObjectService.listObjectsByProperties("F_USER_INFO", {isVaild : "T" } );
    // for( i=0; i<allUsers.length; i++) {
    //     var user = allUsers[i];
    //     var checkinList = mateObjectEvent.metaObjectService.listObjectsByProperties("BcXJGx6VTnWHg9_C4YobpQ",
    //             {
    //                 attendanceCardno : user.userWord,
    //                 attendanceTime_ge: date(),
    //                 attendanceTime_lt: date()+1
    //             })
    //   var alllist=  mateObjectEvent.databaseRunTime.query("0000000284","select * from oa_attendance_list where attendance_id=872373");
    //     mateObjectEvent.databaseRunTime.execute("0000000284","insert into OA_ATTENDANCE_INFO(ATTENDANCE_MONTH,usercode) values(1,2)");
    //     var obj = {};
    //     mateObjectEvent.metaObjectService.saveObject("",obj)
    // };
    var allUsers = mateObjectEvent.databaseRunTime.query('0000000324',"select user_code,user_word from f_userinfo  where is_valid='T' and user_word is not null");
    for( i=0; i<allUsers.length; i++) {
        var user = allUsers[i];
        var card = mateObjectEvent.databaseRunTime.query('0000000284',"select attendance_cardno,trunc(attendance_time) atten_time,min(attendance_time) min_time,max(attendance_time) max_time from oa_attendance_list where trunc(attendance_time,'mm')=date'2019-5-1' \n" +
            "and attendance_cardno=:1 group by attendance_cardno,trunc(attendance_time)",[user.userWord]);
        for (j=0; j<card.length; j++) {
            mateObjectEvent.databaseRunTime.execute("0000000284", "insert into OA_ATTENDANCE_DETAIL(usercode,attendance_date,class_type,IN_TIME,OUT_TIME,attendance_cardno) values(:1,trunc(:2),:3,:4,:5,:6)", [user.userCode, card[j].attenTime, '1', card[j].minTime, card[j].maxTime,card[j].attendanceCardno]);
        }
    }
    return 1;
}

function beforeDelete(mateObjectEvent, entity){
    return 0;
}

function beforeSubmit(mateObjectEvent, entity){
    return 0;
}
