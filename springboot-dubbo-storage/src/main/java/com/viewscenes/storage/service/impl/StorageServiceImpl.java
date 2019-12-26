package com.viewscenes.storage.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viewscenes.common.dto.StorageDTO;
import com.viewscenes.storage.entity.Storage;
import com.viewscenes.storage.mapper.StorageMapper;
import com.viewscenes.storage.service.StorageService;

@Service
public class StorageServiceImpl implements StorageService {

    @Autowired
    StorageMapper storageMapper;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public int decreaseStorage(StorageDTO storageDTO) {
        logger.info("开始扣减库存。商品编号:{}", storageDTO.getCommodityCode());
        try {
        	Storage storage = new Storage();
        	BeanUtils.copyProperties(storageDTO,storage);
        	int upCount = storageMapper.decreaseStorage(storage);
        	return upCount;
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return 0;
    }
}
