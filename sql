(1) Write MySQL query to find IPs that mode more than a certain number of requests for a given time period.

select
	ip,
	count(*)
from
	access_log a
where
	a.timestamp >= STR_TO_DATE('2017-01-01.00:00:00', '%Y-%m-%d.%H:%i:%s')
	and a.timestamp <= STR_TO_DATE('2017-01-01.00:00:02', '%Y-%m-%d.%H:%i:%s')
group by ip
having count(id) > 200;

(2) Write MySQL query to find requests made by a given IP.

select
	a.request
from
	access_log a
where
	a.ip = '192.168.234.82'