package xyz.frt.yiban.basesdk.service;

import xyz.frt.yiban.basesdk.common.JsonResult;

public interface AuthorizeService {

    /**
     * 易班用户授权的回调地址
     *
     * @param code 回调地址返回的授权码
     * @return 授权结果
     */
    public JsonResult authorize(String code);

}
