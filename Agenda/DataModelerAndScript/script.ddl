CREATE TABLE events (
    id_event        INTEGER NOT NULL,
    name            VARCHAR2(32 CHAR),
    "Start"         DATE,
    end             DATE,
    localization    VARCHAR2(128 CHAR),
    color           VARCHAR2(32 CHAR),
    id_note         INTEGER,
    items_id        INTEGER,
    notes_id_note   INTEGER
);

ALTER TABLE events ADD CONSTRAINT events_pk PRIMARY KEY ( id_event );

CREATE TABLE items (
    id            INTEGER NOT NULL,
    name          VARCHAR2(64 CHAR) NOT NULL,
    color         VARCHAR2(32 CHAR),
    type          VARCHAR2(10 CHAR),
    users_email   VARCHAR2(64 CHAR) NOT NULL
);

ALTER TABLE items ADD CONSTRAINT items_pk PRIMARY KEY ( id );

CREATE TABLE notes (
    id_note           INTEGER NOT NULL,
    title             VARCHAR2(64 CHAR),
    text              VARCHAR2(256 CHAR),
    color             VARCHAR2(32 CHAR),
    id_event          INTEGER,
    items_id          INTEGER,
    events_id_event   INTEGER
);

ALTER TABLE notes ADD CONSTRAINT notes_pk PRIMARY KEY ( id_note );

CREATE TABLE todo (
    id_todo           INTEGER NOT NULL,
    name              VARCHAR2(64 CHAR),
    deadline          DATE,
    status            INTEGER,
    priority          INTEGER,
    items_id          INTEGER,
    events_id_event   INTEGER,
    notes_id_note     INTEGER
);

ALTER TABLE todo ADD CONSTRAINT todo_pk PRIMARY KEY ( id_todo );

CREATE TABLE users (
    email      VARCHAR2(64 CHAR) NOT NULL,
    password   VARCHAR2(64 CHAR) NOT NULL
);

ALTER TABLE users ADD CONSTRAINT users_pk PRIMARY KEY ( email );

ALTER TABLE events
    ADD CONSTRAINT events_items_fk FOREIGN KEY ( items_id )
        REFERENCES items ( id );

ALTER TABLE events
    ADD CONSTRAINT events_notes_fk FOREIGN KEY ( notes_id_note )
        REFERENCES notes ( id_note );

ALTER TABLE items
    ADD CONSTRAINT items_users_fk FOREIGN KEY ( users_email )
        REFERENCES users ( email );

ALTER TABLE notes
    ADD CONSTRAINT notes_events_fk FOREIGN KEY ( events_id_event )
        REFERENCES events ( id_event );

ALTER TABLE notes
    ADD CONSTRAINT notes_items_fk FOREIGN KEY ( items_id )
        REFERENCES items ( id );

ALTER TABLE todo
    ADD CONSTRAINT todo_events_fk FOREIGN KEY ( events_id_event )
        REFERENCES events ( id_event );

ALTER TABLE todo
    ADD CONSTRAINT todo_items_fk FOREIGN KEY ( items_id )
        REFERENCES items ( id );

ALTER TABLE todo
    ADD CONSTRAINT todo_notes_fk FOREIGN KEY ( notes_id_note )
        REFERENCES notes ( id_note );




CREATE SEQUENCE  "G8_MTRACEWICZ"."EVENTS_ID_SEQ"  MINVALUE 1 MAXVALUE 2147483647 INCREMENT BY 1 START WITH 1 NOCACHE  ORDER  NOCYCLE ;


CREATE SEQUENCE  "G8_MTRACEWICZ"."ITEMS_ID_SEQ"  MINVALUE 1 MAXVALUE 2147483647 INCREMENT BY 1 START WITH 1 NOCACHE  ORDER  NOCYCLE ;


CREATE SEQUENCE  "G8_MTRACEWICZ"."NOTES_ID_SEQ"  MINVALUE 1 MAXVALUE 2147483647 INCREMENT BY 1 START WITH 1 NOCACHE  ORDER  NOCYCLE ;


CREATE SEQUENCE  "G8_MTRACEWICZ"."TODO_ID_SEQ"  MINVALUE 1 MAXVALUE 2147483647 INCREMENT BY 1 START WITH 1 NOCACHE  ORDER  NOCYCLE ;



create or replace TRIGGER COMPLETE_TODO_TRIGER 
BEFORE DELETE ON TASKS 
  FOR EACH ROW
BEGIN
  IF (:old.status = 1) THEN  
   INSERT INTO completed_todo VALUES(:OLD.NAME
   ,:OLD.DEADLINE
   ,:OLD.STATUS
   ,:OLD.PRIORITY
   ,:OLD.NOTES_ID
   ,:OLD.EVENTS_ID
   ,:OLD.ITEMS_ID
   ,sysdate);
  END IF;
END;


create or replace TRIGGER EVENTS_ID_TRIGGER 
BEFORE INSERT ON EVENTS 
  FOR EACH ROW
DECLARE
BEGIN
  IF :NEW.id_event  IS NULL THEN
    :new.id_event := EVENTS_ID_SEQ.nextval;
  END IF;
END;
ALTER TRIGGER "G8_MTRACEWICZ"."EVENTS_ID_TRIGGERS" ENABLE;

create or replace TRIGGER ITEMS_ID_TRIGGER 
BEFORE INSERT ON ITEMS 
  FOR EACH ROW
DECLARE
BEGIN
  IF :NEW.id  IS NULL THEN
    :new.id := ITEMS_ID_SEQ.nextval;
  END IF;
END;
ALTER TRIGGER "G8_MTRACEWICZ"."ITEMS_ID_TRIGGERS" ENABLE;

create or replace TRIGGER NOTE_ID_TRIGGER 
BEFORE INSERT ON NOTES 
  FOR EACH ROW
DECLARE
BEGIN
  IF :NEW.notes_id  IS NULL THEN
    :new.notes_id := NOTES_ID_SEQ.nextval;
  END IF;
END;
ALTER TRIGGER "G8_MTRACEWICZ"."NOTE_ID_TRIGGERS" ENABLE;

  CREATE OR REPLACE TRIGGER "G8_MTRACEWICZ"."TODO_ID_TRIGGERS" 
BEFORE INSERT ON TASKS 
  FOR EACH ROW
DECLARE
BEGIN
  IF :NEW.id_todo  IS NULL THEN
    :new.id_todo := TODO_ID_SEQ.nextval;
  END IF;
END;
/
ALTER TRIGGER "G8_MTRACEWICZ"."TODO_ID_TRIGGERS" ENABLE;
