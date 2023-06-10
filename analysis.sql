with user_totals as (
	select u.name, sum(v.weight * s.popularity_num) as voted_total, sum(v.weight) as total_votes from users u
	join votes v on v.user_id = u.id and v.weight > 0
	join submissions sm on sm.id = v.submission_id
	join rounds r on r.id = sm.round_id
	join songs s on s.spotify_uri = sm.spotify_uri
	-- where r.league_id = 'x'
	group by u.name
)
select u.name, voted_total/total_votes as average_vote_popularity
from user_totals u
order by 2 desc;

with user_totals as (
	select u.name, sum(s.popularity_num) as submission_total, count(sm.id) as total_submissions
	from users u
	join submissions sm on sm.user_id = u.id
	join rounds r on r.id = sm.round_id
	join songs s on s.spotify_uri = sm.spotify_uri
	-- where r.league_id = 'x'
	group by u.name
)
select u.name, submission_total/total_submissions as avg_sub_popularity
from user_totals u
order by 2 desc;


select u.name, count(distinct g.name)/count(s.id) as new_genres_per_round
from users u
join submissions s on s.user_id = u.id
join songs s2 on s2.spotify_uri = s.spotify_uri
join artists a on a.id = s2.artist_id
join artists_genres g on g.artist_id = a.id
group by 1
order by 2 desc;