# Redis Geo

# 장소 등록
```
localhost:6379> GEOADD testplace 127.1096577 37.3214976 "test01"
-> zset으로 저장됨
```


# Hash 확인
```
localhost:6379> geohash testplace test01
1) "wydkk6nmmx0"
```

# http://geohash.org/ 에서 확인