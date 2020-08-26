update users set password = concat('{MD5}', password);
commit;
