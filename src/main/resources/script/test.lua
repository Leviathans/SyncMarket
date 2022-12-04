local key1 = KEYS[1]
local key2 = KEYS[2]
local key3 = KEYS[3]
local key4 = KEYS[4]

local arg1 = ARGV[1]
local arg2 = ARGV[2]
local arg3 = ARGV[3]
local arg4 = ARGV[4]

local hbSPrice = redis.call(key1, arg1)
local binanceSPrice = redis.call(key2, arg2)
local hbUPrice = redis.call(key3, arg3)
local binanceUPrice = redis.call(key4, arg4)

redis.log(hbSPrice)
redis.log(binanceSPrice)
redis.log(hbUPrice)
redis.log(binanceUPrice)
local ret = {}
ret[1] = ret1
ret[2] = ret2
ret[3] = ret3
ret[4] = ret4
return ret
