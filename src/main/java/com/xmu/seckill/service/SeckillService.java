package com.xmu.seckill.service;

import com.xmu.seckill.common.Result;

public interface SeckillService {
    Result<String> doSeckill(Long productId, Long userId);
}
