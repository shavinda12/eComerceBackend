create table carts
(
    id      binary(16) default (uuid_to_bin(uuid())) not null
        primary key,
    date_created date       default (CURDATE())           not null
);

create table cart_items
(
    id         BIGINT auto_increment,
    cart_id    binary(16)    not null,
    product_id bigint        null,
    quantity   int default 1 not null,
    constraint cart_items_carts_products_unique
        unique (cart_id, product_id),
    constraint cart_items_pk
        unique (id),
    constraint cart_items_products_fk
        foreign key (product_id) references products (id)
            on delete cascade,
    constraint cart_items_carts_fk
        foreign key (cart_id) references carts (id)
            on delete cascade
);