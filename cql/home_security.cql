// Create Keyspace home_security 
CREATE KEYSPACE home_security WITH replication = {
  'class': 'SimpleStrategy',
  'replication_factor': '1'
};

// Use keyspace 
use home_security

// Create Home table 
CREATE TABLE home (
home_id text,
address text,
city text,
state text,
zip text,
contact_name text,
phone text,
alt_phone text,
phone_password text,
email text,
main_code text,
guest_code text,
PRIMARY KEY (home_id)
);

//Insert Single Row into Home Table.
INSERT INTO home (home_id, address, city, state, zip, contact_name, phone, phone_password, email, main_code, guest_code) VALUES ('H01474777', '518 Crestview Drive', 'Beverly Hills', 'CA', '90046', 'Jed Clampett', '310-775-4011', 'oil', 'jclampett@bhb.com', '5599', '7778');

//Create table activity
CREATE TABLE activity (
  home_id text,
  datetime timestamp,
  code_used text,
  event text,
  PRIMARY KEY (home_id, datetime)
) WITH CLUSTERING ORDER BY (datetime DESC);

//Inssert Single Row into Activity Table.
INSERT INTO activity (home_id, datetime, event, code_used) VALUES ('H01474777', '2014-05-21 07:32:16', 'alarm set', '5599');

//Create Index on Column
CREATE INDEX code_used_index ON activity (code_used);

