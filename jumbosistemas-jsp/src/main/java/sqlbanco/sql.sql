-- DROP TABLE public.model_login;

CREATE TABLE public.model_login
(
  login character varying(200) NOT NULL,
  senha character varying(200) NOT NULL,
  id integer NOT NULL DEFAULT nextval('model_login_id_seq'::regclass),
  nome character varying(300) NOT NULL,
  email character varying(300) NOT NULL,
  useradmin boolean NOT NULL DEFAULT false,
  usuario_id bigint NOT NULL DEFAULT 1,
  CONSTRAINT model_login_pkey PRIMARY KEY (id),
  CONSTRAINT usuario_fk FOREIGN KEY (usuario_id)
      REFERENCES public.model_login (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT login_unique UNIQUE (login)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.model_login
  OWNER TO postgres;



CREATE DATABASE "curso-jsp"
  WITH OWNER = postgres
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'Portuguese_Brazil.1252'
       LC_CTYPE = 'Portuguese_Brazil.1252'
       CONNECTION LIMIT = -1;



CREATE TABLE public.model_login
(
  login character varying(200) NOT NULL,
  senha character varying(200) NOT NULL,
  id integer NOT NULL DEFAULT nextval('model_login_id_seq'::regclass),
  nome character varying(300) NOT NULL,
  email character varying(300) NOT NULL,
  CONSTRAINT model_login_pkey PRIMARY KEY (id),
  CONSTRAINT login_unique UNIQUE (login)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.model_login
  OWNER TO postgres;
  
  
  alter table model_login add column useradmin boolean not null default false;
  
  alter table model_login add column usuario_id bigint not null default 1;
  
  alter table model_login add constraint usuario_fk foreign key (usuario_id) references model_login (id);
  
  alter table model_login add column perfil character varying(200);