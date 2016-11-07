CREATE TABLE account
(
    id      IDENTITY PRIMARY KEY,
    type    VARCHAR(20) NOT NULL,
    name    VARCHAR(120) NOT NULL,
    description VARCHAR(200) ,
    parent_account LONG,
    mailing_address VARCHAR(200) ,
    telephone VARCHAR(200) ,
    fax VARCHAR(200) ,
    email VARCHAR(120)

);

CREATE INDEX account_name on account(name);
CREATE INDEX account_type on account(type);