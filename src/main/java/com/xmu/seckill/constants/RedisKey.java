package com.xmu.seckill.constants;

public class RedisKey {
    //形如 seckill:stock:10001   <10>  表示10001这个商品的库存，存储库存10
    public static final String STOCK_KEY_PREFIX = "seckill:stock:";
    //形如 seckill:result:1:2   <1>表示用户1对商品2的抢购记录，标记为1，表示已秒杀，不允许重复秒杀
    public static final String RESULT_KEY_PREFIX = "seckill:result:";// 记录用户已经抢过的商品，防止用户重复抢购
    //将库存的变化记录在redis中，形如 seckill:stock:dirty    <1,2>(set) 表示商品1和商品2的库存发生了变化
    public static final String STOCK_DIRTY_SET = "seckill:stock:dirty";
}
