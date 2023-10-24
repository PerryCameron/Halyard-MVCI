# 10/23/2023 added column to make displaying easier
alter table invoice_item
    add IS_CATEGORY TINYINT default FALSE not null;

update invoice_item set invoice_item.IS_CATEGORY=true where FIELD_NAME='keys';
# ECSC_SQL> update invoice_item set invoice_item.IS_CATEGORY=true where FIELD_NAME='keys'
#     [2023-10-23 17:24:30] 363 rows affected in 297 ms

update invoice_item set invoice_item.IS_CATEGORY=true where FIELD_NAME='Summer Storage';
# ECSC_SQL> update invoice_item set invoice_item.IS_CATEGORY=true where FIELD_NAME='Summer Storage'
#     [2023-10-23 17:25:13] 336 rows affected in 273 ms

update invoice_item set invoice_item.IS_CATEGORY=true where FIELD_NAME='Kayak';
# ECSC_SQL> update invoice_item set invoice_item.IS_CATEGORY=true where FIELD_NAME='Kayak'
#     [2023-10-23 17:26:34] 363 rows affected in 291 ms
