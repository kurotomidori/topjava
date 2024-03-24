curl http://localhost:8080/topjava/rest/meals
curl http://localhost:8080/topjava/rest/meals/100009
curl -X DELETE http://localhost:8080/topjava/rest/meals/100003
curl "http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-30&startTime=09:00:00&endDate=2020-01-30&endTime=21:00:00"
curl "http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-30&startTime=09:00:00&endDate=&endTime="
curl -X PUT http://localhost:8080/topjava/rest/meals/100009 -H "Content-Type:application/json;charset=UTF-8"  -d "{\"id\":100009,\"dateTime\":\"2020-01-31T20:00:00\",\"description\":\"обновлённый ужин\",\"calories\":610}"
curl -X POST http://localhost:8080/topjava/rest/meals -H "Content-Type:application/json;charset=UTF-8" -d "{\"id\":null,\"dateTime\":\"2023-01-31T10:11:00\",\"description\":\"новая еда\",\"calories\":110}"