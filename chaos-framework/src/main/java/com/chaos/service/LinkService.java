package com.chaos.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaos.domain.ResponseResult;
import com.chaos.domain.dto.AddLinkDto;
import com.chaos.domain.dto.ChangeLinkStatusDto;
import com.chaos.domain.dto.ListLinkDto;
import com.chaos.domain.dto.UpdateLinkDto;
import com.chaos.domain.entity.Link;

/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2023-08-06 00:54:24
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult listLink(ListLinkDto listLinkDto);

    ResponseResult addLink(AddLinkDto addLinkDto);

    ResponseResult getLinkById(Long id);

    ResponseResult updateLink(UpdateLinkDto updateLinkDto);

    ResponseResult deleteLink(Long id);

    ResponseResult changeLinkStatus(ChangeLinkStatusDto dto);
}
