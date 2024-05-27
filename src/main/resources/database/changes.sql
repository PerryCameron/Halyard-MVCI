#all changes below need to be made to database for Halyard-MVCI to work, select all and run

# this was added 5-27-2024
create table slip_placement
(
    sp_id      INTEGER               NOT NULL auto_increment primary key,
    dock_name        varchar(4) unique        NULL,
    x_web_ui     INTEGER NOT NULL,
    y_web_ui     INTEGER NOT NULL,
    x_app_ui     INTEGER NOT NULL,
    y_app_ui     INTEGER NOT NULL
);

INSERT INTO ecsailor_ECSC_SQL.slip_placement (dock_name, x_web_ui, y_web_ui, x_app_ui, y_app_ui) VALUES ('A', 140, 40, 0, 0);
INSERT INTO ecsailor_ECSC_SQL.slip_placement (dock_name, x_web_ui, y_web_ui, x_app_ui, y_app_ui) VALUES ('B', 430, 40, 0, 0);
INSERT INTO ecsailor_ECSC_SQL.slip_placement (dock_name, x_web_ui, y_web_ui, x_app_ui, y_app_ui) VALUES ('C', 720, 40, 0, 0);
INSERT INTO ecsailor_ECSC_SQL.slip_placement (dock_name, x_web_ui, y_web_ui, x_app_ui, y_app_ui) VALUES ('D', 1010, 40, 0, 0);
INSERT INTO ecsailor_ECSC_SQL.slip_placement (dock_name, x_web_ui, y_web_ui, x_app_ui, y_app_ui) VALUES ('F', 140, 610, 0, 0);



# 10/23/2023 added column to make displaying easier
alter table invoice_item
    add CATEGORY TINYINT default FALSE not null;

update invoice_item set invoice_item.CATEGORY=true where FIELD_NAME='keys';
# ECSC_SQL> update invoice_item set invoice_item.CATEGORY=true where FIELD_NAME='keys'
#     [2023-10-23 17:24:30] 363 rows affected in 297 ms

update invoice_item set invoice_item.CATEGORY=true where FIELD_NAME='Summer Storage';
# ECSC_SQL> update invoice_item set invoice_item.CATEGORY=true where FIELD_NAME='Summer Storage'
#     [2023-10-23 17:25:13] 336 rows affected in 273 ms

update invoice_item set invoice_item.CATEGORY=true where FIELD_NAME='Kayak';
# ECSC_SQL> update invoice_item set invoice_item.CATEGORY=true where FIELD_NAME='Kayak'
#     [2023-10-23 17:26:34] 363 rows affected in 291 ms

# 10/25/2023 added column to signify what category an itemized invoice item belongs to
alter table invoice_item
    add CATEGORY_ITEM varchar(50) default 'none' not null;

#################################################################################################
#                       2023  Changes
#
#################################################################################################

# updating kayak rows to match
UPDATE invoice_item
SET CATEGORY_ITEM = 'Kayak'
WHERE FIELD_NAME IN ('Kayak Shed', 'Kayak Rack', 'Kayak Beach Rack') and FISCAL_YEAR=2023;

UPDATE invoice_item
SET CATEGORY_ITEM = 'Kayak'
WHERE FIELD_NAME IN ('Kayak Shed', 'Kayak Rack', 'Kayak Beach Rack') and FISCAL_YEAR=2024;


# updating key rows to match
UPDATE invoice_item
SET CATEGORY_ITEM = 'Keys'
WHERE FIELD_NAME IN ('Gate Key', 'Sail Loft Key', 'Kayak Shed Key') and FISCAL_YEAR=2023;

UPDATE invoice_item
SET CATEGORY_ITEM = 'Keys'
WHERE FIELD_NAME IN ('Gate Key', 'Sail Loft Key', 'Kayak Shed Key') and FISCAL_YEAR=2024;

#updating summer storage rows to match
UPDATE invoice_item
SET CATEGORY_ITEM = 'Summer Storage'
WHERE FIELD_NAME IN ('Beam Over 5 foot', 'Beam Under 5 foot') and FISCAL_YEAR=2023;

UPDATE invoice_item
SET CATEGORY_ITEM = 'Summer Storage'
WHERE FIELD_NAME IN ('Beam Over 5 foot', 'Beam Under 5 foot') and FISCAL_YEAR=2024;

START TRANSACTION;
-- Calculate SUM(QTY) and SUM(VALUE) for each MS_ID for the specified FIELD_NAMEs
CREATE TEMPORARY TABLE TempSums AS
SELECT
    MS_ID,
    SUM(QTY) as total_qty,
    SUM(VALUE) as total_value
FROM
    invoice_item
WHERE
        FIELD_NAME IN ('Kayak Rack', 'Kayak Beach Rack', 'Kayak Shed')
  AND FISCAL_YEAR = 2023
GROUP BY
    MS_ID;

-- Update the Kayak rows using the summed values
UPDATE
    invoice_item as ii
        JOIN
        TempSums ts ON ii.MS_ID = ts.MS_ID
SET
    ii.QTY = ts.total_qty,
    ii.VALUE = ts.total_value
WHERE
        ii.FIELD_NAME = 'Kayak' AND ii.FISCAL_YEAR = 2023;

-- Commit the transaction
COMMIT;

CREATE TEMPORARY TABLE TempSums2024 AS
SELECT
    MS_ID,
    SUM(QTY) as total_qty,
    SUM(VALUE) as total_value
FROM
    invoice_item
WHERE
        FIELD_NAME IN ('Kayak Rack', 'Kayak Beach Rack', 'Kayak Shed')
  AND FISCAL_YEAR = 2024
GROUP BY
    MS_ID;

-- Update the Kayak rows using the summed values for 2024
UPDATE
    invoice_item as ii
        JOIN TempSums2024 ts ON ii.MS_ID = ts.MS_ID
SET
    ii.QTY = ts.total_qty,
    ii.VALUE = ts.total_value
WHERE
        ii.FIELD_NAME = 'Kayak' AND ii.FISCAL_YEAR = 2024;

-- Commit the transaction
COMMIT;

#################################################################################################
#                       2022  Changes
#
#################################################################################################
delete from invoice_item where FIELD_NAME='new entry' and FISCAL_YEAR='2022';
# ECSC_SQL> delete from invoice_item where FIELD_NAME='new entry' and FISCAL_YEAR='2022'
#     [2023-10-30 14:34:14] 1 row affected in 228 ms
delete from invoice_item where FIELD_NAME='Testing' and FISCAL_YEAR='2022';
# ECSC_SQL> delete from invoice_item where FIELD_NAME='Testing' and FISCAL_YEAR='2022'
#     [2023-10-30 14:36:10] 1 row affected in 228 ms
update db_invoice set widget_type='spinner' where widget_type='combo-box';  # NO more combo boxes, takes care of all years
# ECSC_SQL> update db_invoice set widget_type='spinner' where widget_type='combo-box'
#     [2023-10-30 14:44:06] 49 rows affected in 181 ms


