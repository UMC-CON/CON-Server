package com.umc.cons.common.blacklist;

import org.springframework.data.repository.CrudRepository;

public interface BlackListRepository extends CrudRepository<BlackList, String> {
    @Override
    boolean existsById(String accessToken);

}
