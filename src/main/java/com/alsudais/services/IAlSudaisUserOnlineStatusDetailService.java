package com.alsudais.services;

import com.alsudais.beans.request.UserOnlineStatusDetailRequestBean;
import reactor.core.publisher.Mono;

public interface IAlSudaisUserOnlineStatusDetailService {

    public Mono<?> fetchUserOnlineStatusByUserId(String userId);
    public Mono<?> updateUserOnlineStatus(UserOnlineStatusDetailRequestBean userOnlineStatusDetailRequestBean);
    public Mono<?> fetchHistoricalData(String userId);
}
