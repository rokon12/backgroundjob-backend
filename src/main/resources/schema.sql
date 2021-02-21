DROP TABLE IF EXISTS `jobs`;
CREATE TABLE IF NOT EXISTS `jobs`
(
    uuid                   VARCHAR(36) PRIMARY KEY,
    version                INT,
    created_date_time      DATETIME,
    last_updated_date_time DATETIME,
    job_name               VARCHAR(200),
    payload                VARCHAR(10000),
    status                 VARCHAR(100),
    callback_url           VARCHAR(1000)
);

//uuid, version, created_date_time, job_name, payload, status, callback_url
-- CREATE TABLE posts (id SERIAL PRIMARY KEY, title VARCHAR(255), content VARCHAR(255));
