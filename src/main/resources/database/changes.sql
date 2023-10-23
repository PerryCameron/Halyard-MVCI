# 10/23/2023 added column to make displaying easier
alter table invoice_item
    add IS_CATEGORY TINYINT default FALSE not null;
