-- 参数1：库存key
-- 参数2：用户key
-- 检查用户是否已参与过秒杀
if redis.call('exists', KEYS[2]) == 1 then
    return 0
end

-- 获取当前库存
local stock = tonumber(redis.call('get', KEYS[1]))
if stock == nil then
    return -2  -- 库存未初始化
end
if stock <= 0 then
    return -1
end

-- 扣减库存并设置用户已参与标记
redis.call('decr', KEYS[1])
redis.call('set', KEYS[2], 1, 'EX', 86400)
return 1
