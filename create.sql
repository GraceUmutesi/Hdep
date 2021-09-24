CREATE DATABASE blogpost;
\c blogpost
CREATE TABLE posts(id SERIAL PRIMARY KEY, content VARCHAR);
CREATE DATABASE blogpost_test WITH TEMPLATE blogpost;