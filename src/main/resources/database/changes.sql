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

# 10/25/2023 added column to signify what category an itemized invoice item belongs to
alter table invoice_item
    add CATEGORY varchar(50) default 'none' not null;

# updating kayak rows to match
UPDATE invoice_item
SET category = 'Kayak'
WHERE FIELD_NAME IN ('Kayak Shed Key', 'Kayak Rack', 'Kayak Beach Rack') and FISCAL_YEAR=2023;
# ECSC_SQL> UPDATE invoice_item
#           SET category = 'kayak'
#           WHERE FIELD_NAME IN ('Kayak Shed Key', 'Kayak Rack', 'Kayak Beach Rack') and FISCAL_YEAR=2023
#               [2023-10-25 16:25:13] 1,008 rows affected in 223 ms

# updating key rows to match
UPDATE invoice_item
SET category = 'Keys'
WHERE FIELD_NAME IN ('Gate Key', 'Sail Loft Key', 'Kayak Shed Key') and FISCAL_YEAR=2023;
# ECSC_SQL> UPDATE invoice_item
#           SET category = 'Keys'
#           WHERE FIELD_NAME IN ('Gate Key', 'Sail Loft Key', 'Kayak Shed Key') and FISCAL_YEAR=2023
#               [2023-10-25 16:30:56] 1,008 rows affected in 258 ms

#updating summer storage rows to match
UPDATE invoice_item
SET category = 'Summer Storage'
WHERE FIELD_NAME IN ('Beam Over 5 foot', 'Beam Under 5 foot') and FISCAL_YEAR=2023;
# ECSC_SQL> UPDATE invoice_item
#           SET category = 'Summer Storage'
#           WHERE FIELD_NAME IN ('Beam Over 5 foot', 'Beam Under 5 foot') and FISCAL_YEAR=2023
#               [2023-10-25 16:33:05] 672 rows affected in 250 ms