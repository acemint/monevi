INSERT INTO
	monevi_region(id, mark_for_delete, created_date, created_by, updated_date, updated_by, "name")
VALUES
	(uuid_in(md5(random()::text || clock_timestamp()::text)::cstring), FALSE, NOW(), 'ADMIN', NOW(), 'ADMIN', 'Kemanggisan'),
	(uuid_in(md5(random()::text || clock_timestamp()::text)::cstring), FALSE, NOW(), 'ADMIN', NOW(), 'ADMIN', 'Alam Sutera'),
	(uuid_in(md5(random()::text || clock_timestamp()::text)::cstring), FALSE, NOW(), 'ADMIN', NOW(), 'ADMIN', 'Senayan'),
	(uuid_in(md5(random()::text || clock_timestamp()::text)::cstring), FALSE, NOW(), 'ADMIN', NOW(), 'ADMIN', 'Bandung'),
	(uuid_in(md5(random()::text || clock_timestamp()::text)::cstring), FALSE, NOW(), 'ADMIN', NOW(), 'ADMIN', 'Malang'),
	(uuid_in(md5(random()::text || clock_timestamp()::text)::cstring), FALSE, NOW(), 'ADMIN', NOW(), 'ADMIN', 'Bekasi');