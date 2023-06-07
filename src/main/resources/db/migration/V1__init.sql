
    create table member (
       id bigint not null auto_increment,
        create_date datetime(6),
        birth_year varchar(4) not null,
        email varchar(255) not null unique,
        is_verified_email bit not null,
        nickname varchar(255) not null unique,
        password varchar(255) not null,
        profile_image_url varchar(255) not null,
        provider varchar(255),
        provider_id tinytext,
        role varchar(255),
        primary key (id)
    );

    create table travelogue (
       id bigint not null auto_increment,
        create_date datetime(6),
        etc bigint,
        lodge bigint,
        total bigint not null,
        transportation bigint,
        country_name varchar(255) not null,
        is_published bit not null,
        end_date date not null,
        start_date date not null,
        thumbnail tinytext,
        title varchar(255) not null,
        view_count bigint not null,
        member_id bigint,
        primary key (id),
        foreign key (member_id) references member (id)
    );

    create table sub_travelogue (
       id bigint not null auto_increment,
        create_date datetime(6),
        content LONGTEXT not null,
        day_seq integer not null,
        title varchar(50) not null,
        travelogue_id bigint,
        primary key (id),
        foreign key (travelogue_id) references travelogue (id)
    );

    create table address (
       sub_travelogue_id bigint not null,
       region varchar(255) not null,
       primary key (sub_travelogue_id, region),
       foreign key (sub_travelogue_id) references sub_travelogue (id)
    );

    create table bookmark (
       id bigint not null auto_increment,
        create_date datetime(6),
        member_id bigint,
        travelogue_id bigint,
        primary key (id),
        foreign key (member_id) references member (id) on delete cascade,
        foreign key (travelogue_id) references travelogue (id) on delete cascade
    );

    create table likes (
      id bigint not null auto_increment,
      create_date datetime(6),
      member_id bigint not null,
      travelogue_id bigint not null,
      primary key (id),
      foreign key (member_id) references member (id) on delete cascade,
      foreign key (travelogue_id) references travelogue (id) on delete cascade
    );

    create table transportation (
       sub_travelogue_id bigint not null,
        transportation_set varchar(255) not null,
        primary key (sub_travelogue_id, transportation_set),
        foreign key (sub_travelogue_id) references sub_travelogue (id)
    );

    create table travel_photo (
       id bigint not null auto_increment,
        url varchar(255) not null,
        sub_travelogue_id bigint,
        primary key (id),
        foreign key (sub_travelogue_id) references sub_travelogue (id)
    );

