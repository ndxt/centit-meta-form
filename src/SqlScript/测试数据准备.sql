
insert into M_Model_Data_Field (Model_Code,column_Name,Column_Type,Access_Type,Input_Type,
Reference_Type,Reference_Data,Input_Hint)
select 'NEW_OPPORTUNITIE', column_Name , 'T', 'N','input',
Reference_Type,Reference_Data,column_comment
from F_META_COLUMN where table_id='105';

select * from F_META_TABLE;